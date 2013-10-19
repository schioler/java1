package dk.schioler.economy.out.text;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import dk.schioler.economy.ExpenseException;
import dk.schioler.economy.model.Account;
import dk.schioler.economy.out.data.AccountHarvester;
import dk.schioler.economy.out.data.Period;
import dk.schioler.economy.out.data.TimeLine;
import dk.schioler.economy.out.data.VisitorBuildAccountTable;

@Deprecated
public class TextOutputOverviewCSV  {
   private static final Logger LOG = Logger.getLogger(TextOutputOverviewCSV.class);

   TimeLine timeline;
   File file;
   int rowCount;
   int accountLevels;
   int countMonths;
   String[][] accountNames;
   Account[][] accountData;
   AccountHarvester[][] accountHarvesterTable;
   FormatterFullCSV formatter = new FormatterFullCSV();

   public TextOutputOverviewCSV() {
      super();
   }

   public void doOutput() {
      // gather data:
      buildAccountGrid();
      buildAccountNameGrid();
      buildAccountHarvesterTable();

      PrintWriter pw = null;
      try {
         pw = new PrintWriter(file);
         StringBuffer printLegend = formatter.printLegend(accountLevels, timeline, countMonths);
         pw.println(printLegend);

         StringBuffer calculatedHeaders = formatter.buildCalculationHeaders(accountLevels);
         StringBuffer totalsRow = formatter.buildTotalsRow(accountLevels, accountHarvesterTable, countMonths);

         // Build Rows: accounts + summed/div
         StringBuilder[] rows = new StringBuilder[rowCount];
         for (int i = 0; i < rows.length; i++) {
            rows[i] = new StringBuilder();
            addAccountNameToRow(rows, i);
            formatter.addAccountCalculations(rows, i, accountLevels, accountHarvesterTable);
            LOG.debug(rows[i]);
         }

         pw.println(calculatedHeaders.toString());
         pw.println(totalsRow.toString());
         for (int i = 0; i < rows.length; i++) {
            pw.println(rows[i]);
         }
         pw.println();
         pw.println();
         // ******************************************

         // build account output
         StringBuffer accountHeaders = formatter.buildAccountHeaders(accountLevels, timeline.getPeriods());
         pw.println(accountHeaders.toString());

         // Build Rows: accounts + accData
         rows = new StringBuilder[rowCount];
         for (int i = 0; i < rows.length; i++) {
            rows[i] = new StringBuilder();
            addAccountNameToRow(rows, i);
            addAccountData(rows, i);
            LOG.debug(rows[i]);
         }

         for (int i = 0; i < rows.length; i++) {
            pw.println(rows[i]);
         }

         pw.flush();
      } catch (IOException e) {
         throw new ExpenseException(e.getMessage(), e);
      } finally {
         IOUtils.closeQuietly(pw);
      }

   }

   public void buildAccountHarvesterTable() {
      accountHarvesterTable = new AccountHarvester[this.accountData.length][this.accountLevels];
      for (int i = 0; i < accountData.length; i++) {
         for (int j = 0; j < accountData[i].length; j++) {
            Account account = accountData[i][j];
            if (account != null) {
               int colIdx = account.getLevel();

               if (accountHarvesterTable[i][colIdx] == null) {
                  accountHarvesterTable[i][colIdx] = new AccountHarvester(account, this.countMonths);
               }
               accountHarvesterTable[i][colIdx].addAccountData(account);
            }
         }
      }

   }

   private void buildAccountNameGrid() {
      accountNames = new String[rowCount][accountLevels];
      for (int i = 0; i < accountNames.length; i++) {
         for (int j = 0; j < accountNames[i].length; j++) {
            Account account = accountData[i][j];
            if (account != null)
               accountNames[i][j] = account.getName();
         }
      }

   }

   public void buildAccountGrid() {
      VisitorBuildAccountTable visitor = new VisitorBuildAccountTable(rowCount, accountLevels, countMonths);
      // idx of the period -> turns to column idx
      int periodIdx = 0;
      for (Period p : timeline.getPeriods()) {
         visitor.setPeriodIdx(periodIdx);
         p.getAccountTreeRoot().accept(visitor);
         periodIdx++;
      }
      accountData = visitor.getRowAndCol();
   }

   private void addAccountData(StringBuilder[] rows, int i) {
      for (int j = 0; j < accountData[i].length; j++) {
         if (accountData[i][j] != null) {
            rows[i].append(formatter.format(accountData[i][j].getExpensesTotal()));
         }
         rows[i].append(formatter.getOutputSeparator());

      }
   }

   private void addAccountNameToRow(StringBuilder[] rows, int i) {
      for (int j = 0; j < accountNames[i].length; j++) {
         if (accountNames[i][j] != null) {
            rows[i].append(accountNames[i][j]);
         }
         rows[i].append(formatter.getOutputSeparator());
      }
   }

   private StringBuffer buildHeaderRow(String name, int countCategories, Date[] expenseDates) {
      StringBuffer sBuffer = new StringBuffer();
      for (int i = 0; i < countCategories; i++) {
         if (i == 0) {
            sBuffer.append(name);
         }
         sBuffer.append(formatter.getOutputSeparator());
      }

      for (int i = 0; i < expenseDates.length; i++) {
         for (int j = 0; j < countCategories; j++) {
            if (j == 0) {
               sBuffer.append(formatter.formatDate(expenseDates[i]));
            }
            sBuffer.append(formatter.getOutputSeparator());
         }
      }

      return sBuffer;
   }

   public void write(String name, String[][] categoryValues, Date[] expenseDates, Object[][] values, File outputDirectory) {
      int countLevels = categoryValues.length;
      int countPeriods = expenseDates.length;
      int countDataColumns = countLevels * countPeriods;
      int countDataRows = categoryValues[0].length;
      boolean rowCountMatch = countDataRows == values[0].length ? true : false;
      boolean colCountMatch = countDataColumns == values.length ? true : false;

      if (!(rowCountMatch && colCountMatch)) {
         throw new ExpenseException("categoryValues, expenseDates and values doees not match: rowCountMatch=" + rowCountMatch + ", colCountMatch=" + colCountMatch);
      }

      List<String> lines = new ArrayList<String>();

      if (values instanceof BigDecimal[][]) {
         StringBuffer header = buildHeaderRow(name, countLevels, expenseDates);
         LOG.debug("Header=" + header);
         lines.add(header.toString());
         BigDecimal[][] bdValues = (BigDecimal[][]) values;
         StringBuffer line = new StringBuffer();
         for (int i = 0; i < countDataRows; i++) {
            // one row at a time
            for (int j = 0; j < categoryValues.length; j++) {
               if (categoryValues[j][i] != null && j == categoryValues.length - 1) {
                  line.append(categoryValues[j][i]);
               } else if (categoryValues[j][i] != null && categoryValues[j + 1][i] == null) {
                  line.append(categoryValues[j][i]);
               }
               line.append(formatter.getOutputSeparator());
            }

            for (int j = 0; j < bdValues.length; j++) {
               if (bdValues[j][i] != null) {
                  line.append(formatter.format(bdValues[j][i]));
               }
               line.append(formatter.getOutputSeparator());
            }
            lines.add(line.toString());
            line = new StringBuffer();
         }

      } else if (values instanceof Account[][]) {
         Account[][] accValues = (Account[][]) values;
         int countColumnsWithThe4Values = countLevels * countPeriods * 4;

         BigDecimal[][] outputData = new BigDecimal[countColumnsWithThe4Values][countDataRows];

         // traversing 1 row at a time
         for (int i = 0; i < accValues[0].length; i++) {
            for (int j = 0; j < accValues.length; j++) {
               if (accountData[j][i] != null) {
                  int colOffset = j * 4;
                  outputData[colOffset + 0][i] = accountData[j][i].getExpensesTotal();
                  outputData[colOffset + 1][i] = accountData[j][i].getExpensesRegular();
                  outputData[colOffset + 2][i] = accountData[j][i].getExpensesNonRegular();
                  outputData[colOffset + 3][i] = accountData[j][i].getExpensesExtra();
               }
            }
         }

         for (int i = 0; i < outputData[0].length; i++) {
            StringBuilder line = new StringBuilder();

            for (int j = 0; j < countLevels; j++) {
               if (categoryValues[j][i] != null) {
                  line.append(categoryValues[j][i]);
               }
               line.append("");
            }
            for (int j = 0; j < outputData.length; j++) {
               if (outputData[j][i] != null) {
                  line.append(outputData[j][i]);
               }
               line.append("");
            }
            lines.add(line.toString());
         }

      } else {
         throw new ExpenseException("Unknown datatype in values array");
      }
      BufferedWriter bWriter = null;

      try {
         String targetFile = outputDirectory.getAbsolutePath() + "/" + name + "-text.csv";
         LOG.debug("Writing to " + targetFile);
         bWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(targetFile)), "UTF-8"));
         for (String string : lines) {
            bWriter.write(string + "\n");
         }
      } catch (Exception e) {

      } finally {
         IOUtils.closeQuietly(bWriter);
      }
   }
}
