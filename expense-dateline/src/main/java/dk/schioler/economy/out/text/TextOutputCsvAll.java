package dk.schioler.economy.out.text;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import dk.schioler.economy.ExpenseException;
import dk.schioler.economy.model.Account;
import dk.schioler.economy.util.Util;

@Component("textOutputCsvAll")
public class TextOutputCsvAll implements TextOutput {
   private static final Logger LOG = Logger.getLogger(TextOutputCsvAll.class);

   DecimalFormat decimalFormat = Util.configureDecimalFormatForCSV();
   private FormatterFullCSV formatter = new FormatterFullCSV();

   StringBuilder buildHeader(int countLevels) {
      StringBuilder sBuilder = new StringBuilder();
      // Start cols.
      for (int nameCol = 0; nameCol < countLevels; nameCol++) {
         sBuilder.append(";");
      }
      // calculated rows
      for (int i = 0; i < countLevels; i++) {
         if (i == 0)
            sBuilder.append("Total");
         sBuilder.append(";");
      }
      for (int i = 0; i < countLevels; i++) {
         if (i == 0)
            sBuilder.append("TotalAvg");
         sBuilder.append(";");
      }
      for (int i = 0; i < countLevels; i++) {
         if (i == 0)
            sBuilder.append("Reg");
         sBuilder.append(";");
      }
      for (int i = 0; i < countLevels; i++) {
         if (i == 0)
            sBuilder.append("RegAvg");
         sBuilder.append(";");
      }
      for (int i = 0; i < countLevels; i++) {
         if (i == 0)
            sBuilder.append("NonReg");
         sBuilder.append(";");
      }
      for (int i = 0; i < countLevels; i++) {
         if (i == 0)
            sBuilder.append("NonRegAvg");
         sBuilder.append(";");
      }
      for (int i = 0; i < countLevels; i++) {
         if (i == 0)
            sBuilder.append("Extra");
         sBuilder.append(";");
      }
      for (int i = 0; i < countLevels; i++) {
         if (i == 0)
            sBuilder.append("ExtraAvg");
         sBuilder.append(";");
      }

      return sBuilder;
   }

   StringBuilder buildPeriodHeaders(int countLevels) {
      StringBuilder sBuilder = new StringBuilder();
      // Start cols.
      for (int nameCol = 0; nameCol < countLevels; nameCol++) {
         sBuilder.append(";");
      }
      return sBuilder;
   }

   @Override
   public void write(String name, String[][] categoryValues, Date[] expenseDates, Object[][] values, File outputDirectory) {
      List<String> lines = buildLines(name, categoryValues, expenseDates, values);
      BufferedWriter bWriter = null;

      try {
         if (!outputDirectory.exists())
            outputDirectory.mkdirs();
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

   @Override
   public void writeToPrinter(String name, String[][] categoryValues, Date[] expenseDates, Object[][] values, PrintWriter outStream) {
      List<String> buildLines = buildLines(name, categoryValues, expenseDates, values);
      for (String string : buildLines) {
         outStream.println(string);
      }
   }

   public List<String> buildLines(String name, String[][] categoryValues, Date[] expenseDates, Object[][] values) {
      int countLevels = categoryValues.length;
      int countPeriods = expenseDates.length;

      int countSumCols = 8 * countLevels;

      int countDataRows = categoryValues[0].length;

      List<String> lines = new ArrayList<String>();

      if (values instanceof BigDecimal[][]) {
         BigDecimal[][] accValues = (BigDecimal[][]) values;

         StringBuffer sb = new StringBuffer();
         for (int i = 0; i < countSumCols + countLevels; i++) {
            sb.append(";");
         }
         for (Date period : expenseDates) {
            String capitalise = StringUtils.capitalize(this.formatter.formatDate(period));
            sb.append(capitalise);
            for (int i = 0; i < countLevels * 4; i++) {
               sb.append(";");
            }
         }
         lines.add(sb.toString());

         // SummationArea: traversing 1 row at a time
         StringBuilder sBuilder = buildHeader(countLevels);

         for (int i = 0; i < countPeriods; i++) {
            for (int j = 0; j < countLevels; j++) {
               sBuilder.append("Total").append(";");
               sBuilder.append("Regular").append(";");
               sBuilder.append("NonReg").append(";");
               sBuilder.append("Extra").append(";");
            }
         }
         lines.add(sBuilder.toString());

         for (int row = 0; row < countDataRows; row++) {
            StringBuilder line = new StringBuilder();

            for (int nameCol = 0; nameCol < countLevels; nameCol++) {
               if (categoryValues[nameCol][row] != null) {
                  String n = Account.ROOT_NAME.equals(categoryValues[nameCol][row]) ? "ROOT" : categoryValues[nameCol][row];
                  line.append(n);
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

      } else {
         throw new ExpenseException("Unknown datatype in values array");
      }
      return lines;
   }

}
