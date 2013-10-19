package dk.schioler.economy.out.text;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import dk.schioler.economy.ExpenseException;
import dk.schioler.economy.util.Util;

public class TextOutputCsvAllV1  {
   private static final Logger LOG = Logger.getLogger(TextOutputCsvAllV1.class);

   DecimalFormat decimalFormat = Util.configureDecimalFormatForCSV();
   private FormatterFullCSV formatter = new FormatterFullCSV();

   private StringBuffer buildDateHeaderRow(String name, int countCategories, Date[] expenseDates) {
      StringBuffer sBuffer = new StringBuffer();
      for (int i = 0; i < countCategories; i++) {
         sBuffer.append(formatter.getOutputSeparator());
      }

      for (int i = 0; i < expenseDates.length; i++) {
         for (int j = 0; j < countCategories * 4; j++) {
            if (j == 0) {
               sBuffer.append(formatter.formatDate(expenseDates[i]));
            }
            sBuffer.append(formatter.getOutputSeparator());
         }
      }

      return sBuffer;
   }

   private StringBuffer buildExpenseHeaderRow(String name, int countLevels, Date[] expenseDates) {
      StringBuffer sBuffer = new StringBuffer();
      for (int i = 0; i < countLevels; i++) {
         sBuffer.append(formatter.getOutputSeparator());
      }

      for (int i = 0; i < expenseDates.length; i++) {
         for (int j = 0; j < countLevels; j++) {
            sBuffer.append("Total").append(formatter.getOutputSeparator());
            sBuffer.append("Reg").append(formatter.getOutputSeparator());
            sBuffer.append("NonReg").append(formatter.getOutputSeparator());
            sBuffer.append("Extra").append(formatter.getOutputSeparator());

         }
      }

      return sBuffer;
   }


   public void write(String name, String[][] categoryValues, Date[] expenseDates, Object[][] values, File outputDirectory) {
      int countLevels = categoryValues.length;
      int countPeriods = expenseDates.length;
      int countDataColumns = countLevels * countPeriods;
      int countDataRows = categoryValues[0].length;

      List<String> lines = new ArrayList<String>();

      if (values instanceof BigDecimal[][]) {
         BigDecimal[][] accValues = (BigDecimal[][]) values;

         // SummationArea: traversing 1 row at a time
         StringBuilder sBuilder = new StringBuilder();
         for (int nameCol = 0; nameCol < countLevels; nameCol++) {
            sBuilder.append(";");
         }
         for (int i = 0; i < countLevels; i++) {

            sBuilder.append("Total").append(";");
            sBuilder.append("TotalAvg").append(";");
            sBuilder.append("Reg").append(";");
            sBuilder.append("RegAvg").append(";");
            sBuilder.append("NonReg").append(";");
            sBuilder.append("NonRegAvg").append(";");
            sBuilder.append("Extra").append(";");
            sBuilder.append("ExtraAvg").append(";");
         }
         lines.add(sBuilder.toString());

         for (int row = 0; row < countDataRows; row++) {
            StringBuilder line = new StringBuilder();

            for (int nameCol = 0; nameCol < countLevels; nameCol++) {
               if (categoryValues[nameCol][row] != null) {
                  line.append(categoryValues[nameCol][row]);
               }
               line.append(";");
            }
            for (int j = 0; j < accValues.length; j++) {
               if (accValues[j][row] != null) {
                  line.append(decimalFormat.format(accValues[j][row]));
               }
               line.append(";");
            }
            lines.add(line.toString());
         }

         lines.add("");
         lines.add("");

         StringBuffer buildHeaderRow = buildDateHeaderRow(name, countLevels, expenseDates);
         lines.add(buildHeaderRow.toString());
         StringBuffer buildExpenseHeaderRow = buildExpenseHeaderRow(name, countLevels, expenseDates);
         lines.add(buildExpenseHeaderRow.toString());
         int rowOffset = countDataRows;
         for (int row = 0; row < countDataRows; row++) {
            StringBuilder line = new StringBuilder();

            for (int nameCol = 0; nameCol < countLevels; nameCol++) {
               if (categoryValues[nameCol][row] != null) {
                  line.append(categoryValues[nameCol][row]);
               }
               line.append(";");
            }
            for (int j = 0; j < accValues.length; j++) {
               if (accValues[j][row + rowOffset] != null) {
                  line.append(decimalFormat.format(accValues[j][row + rowOffset]));
               }
               line.append(";");
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
         LOG.error(e.getMessage(), e);
      } finally {
         IOUtils.closeQuietly(bWriter);
      }

   }

}
