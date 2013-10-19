package dk.schioler.economy.out.text;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import dk.schioler.economy.out.data.AccountHarvester;
import dk.schioler.economy.out.data.Period;
import dk.schioler.economy.out.data.TimeLine;
import dk.schioler.economy.util.Util;

public class FormatterFullCSV {
   public static final String OUTPUT_SEPARATOR = ";";
   DecimalFormat decimalFormat = Util.configureDecimalFormatForCSV();
   private SimpleDateFormat sdf = new SimpleDateFormat("MMMMM yyyy");

   public StringBuffer printLegend(int accountLevels, TimeLine timeline, int countMonths) {
      StringBuffer legend = new StringBuffer();
      for (int i = 0; i < accountLevels; i++) {
         if (i == 0) {
            legend.append(timeline.getUser().getName());
            legend.append(", over " + countMonths + " months: " + sdf.format(timeline.getStartDate()) + " to " + sdf.format(timeline.getEndDate()));
         }

         legend.append(OUTPUT_SEPARATOR);
      }
      return legend;
   }

   public StringBuffer buildCalculationHeaders(int accountLevels) {
      StringBuffer headers = new StringBuffer();
      for (int i = 0; i < accountLevels; i++) {
         headers.append(OUTPUT_SEPARATOR);
      }
      for (int i = 0; i < accountLevels; i++) {
         if (i == 0) {
            headers.append("Total");
         }
         headers.append(OUTPUT_SEPARATOR);
      }
      for (int i = 0; i < accountLevels; i++) {
         if (i == 0) {
            headers.append("avgTotal");
         }
         headers.append(OUTPUT_SEPARATOR);
      }

      for (int i = 0; i < accountLevels; i++) {
         if (i == 0) {
            headers.append("Regular");
         }
         headers.append(OUTPUT_SEPARATOR);
      }
      for (int i = 0; i < accountLevels; i++) {
         if (i == 0) {
            headers.append("avgRegular");
         }
         headers.append(OUTPUT_SEPARATOR);
      }
      for (int i = 0; i < accountLevels; i++) {
         if (i == 0) {
            headers.append("NonRegular");
         }
         headers.append(OUTPUT_SEPARATOR);
      }
      for (int i = 0; i < accountLevels; i++) {
         if (i == 0) {
            headers.append("avgNonRegular");
         }
         headers.append(OUTPUT_SEPARATOR);
      }
      for (int i = 0; i < accountLevels; i++) {
         if (i == 0) {
            headers.append("Extra");
         }
         headers.append(OUTPUT_SEPARATOR);
      }
      for (int i = 0; i < accountLevels; i++) {
         if (i == 0) {
            headers.append("avgExtra");
         }
         headers.append(OUTPUT_SEPARATOR);
      }

      return headers;
   }

   public void addAccountCalculations(StringBuilder[] rows, int i, int accountLevels, AccountHarvester[][] accountHarvesterTable) {
      for (int j = 0; j < accountHarvesterTable[i].length; j++) {
         if (accountHarvesterTable[i][j] != null) {
            AccountHarvester as = accountHarvesterTable[i][j];
            for (int k = 0; k < accountLevels; k++) {
               if (as.getLevel() == k) {
                  rows[i].append(decimalFormat.format(as.getSummedExpensesTotal()));
               }
               rows[i].append(OUTPUT_SEPARATOR);
            }
            for (int k = 0; k < accountLevels; k++) {
               if (as.getLevel() == k) {
                  rows[i].append(decimalFormat.format(as.getAvgExpensesTotal()));
               }
               rows[i].append(OUTPUT_SEPARATOR);
            }

            for (int k = 0; k < accountLevels; k++) {
               if (as.getLevel() == k) {
                  rows[i].append(decimalFormat.format(as.getSummedExpensesRegular()));
               }
               rows[i].append(OUTPUT_SEPARATOR);
            }
            for (int k = 0; k < accountLevels; k++) {
               if (as.getLevel() == k) {
                  rows[i].append(decimalFormat.format(as.getAvgExpensesRegular()));
               }
               rows[i].append(OUTPUT_SEPARATOR);
            }

            for (int k = 0; k < accountLevels; k++) {
               if (as.getLevel() == k) {
                  rows[i].append(decimalFormat.format(as.getSummedExpensesNonRegular()));
               }
               rows[i].append(OUTPUT_SEPARATOR);
            }

            for (int k = 0; k < accountLevels; k++) {
               if (as.getLevel() == k) {
                  rows[i].append(decimalFormat.format(as.getAvgExpensesNonRegular()));
               }
               rows[i].append(OUTPUT_SEPARATOR);
            }

            for (int k = 0; k < accountLevels; k++) {
               if (as.getLevel() == k) {
                  rows[i].append(decimalFormat.format(as.getSummedExpensesForExtra()));
               }
               rows[i].append(OUTPUT_SEPARATOR);
            }

            for (int k = 0; k < accountLevels; k++) {
               if (as.getLevel() == k) {
                  rows[i].append(decimalFormat.format(as.getAvgExpensesForExtra()));
               }
               rows[i].append(OUTPUT_SEPARATOR);
            }

         } else {
            // for(int k = 0; k < 6;k++)
            // rows[i].append(OUTPUT_SEPARATOR);
         }
      }
   }

   public StringBuffer buildTotalsRow(int accountLevels, AccountHarvester[][] accountHarvesterTable, int countMonths) {
      BigDecimal periods = new BigDecimal(countMonths);

      BigDecimal fullTotal = new BigDecimal(0);
      BigDecimal fullRegular = new BigDecimal(0);
      BigDecimal fullNonRegular = new BigDecimal(0);
      BigDecimal fullExtra = new BigDecimal(0);
      BigDecimal fullTotalAvg = new BigDecimal(0);
      BigDecimal fullRegularAvg = new BigDecimal(0);
      BigDecimal fullNonRegularAvg = new BigDecimal(0);
      BigDecimal fullExtraAvg = new BigDecimal(0);

      for (int i = 0; i < accountHarvesterTable.length; i++) {
         // this is top cat account.
         if (accountHarvesterTable[i][0] != null) {
            fullTotal = fullTotal.add(accountHarvesterTable[i][0].getSummedExpensesTotal());
            fullRegular = fullRegular.add(accountHarvesterTable[i][0].getSummedExpensesRegular());
            fullNonRegular = fullNonRegular.add(accountHarvesterTable[i][0].getSummedExpensesNonRegular());
            fullExtra = fullExtra.add(accountHarvesterTable[i][0].getSummedExpensesForExtra());
         }
      }
      fullTotalAvg = fullTotal.divide(periods, RoundingMode.HALF_EVEN);
      fullRegularAvg = fullRegular.divide(periods, RoundingMode.HALF_EVEN);
      fullNonRegularAvg = fullNonRegular.divide(periods, RoundingMode.HALF_EVEN);
      fullExtraAvg = fullExtra.divide(periods, RoundingMode.HALF_EVEN);

      StringBuffer row = new StringBuffer();
      // this covers the account name columns:
      for (int i = 0; i < accountLevels; i++) {
         row.append(";");
      }

      for (int i = 0; i < accountLevels; i++) {
         if (i == 0)
            row.append(decimalFormat.format(fullTotal));
         row.append(";");
      }
      for (int i = 0; i < accountLevels; i++) {
         if (i == 0)
            row.append(decimalFormat.format(fullTotalAvg));
         row.append(";");
      }
      for (int i = 0; i < accountLevels; i++) {
         if (i == 0)
            row.append(decimalFormat.format(fullRegular));
         row.append(";");
      }
      for (int i = 0; i < accountLevels; i++) {
         if (i == 0)
            row.append(decimalFormat.format(fullRegularAvg));
         row.append(";");
      }
      for (int i = 0; i < accountLevels; i++) {
         if (i == 0)
            row.append(decimalFormat.format(fullNonRegular));
         row.append(";");
      }

      for (int i = 0; i < accountLevels; i++) {
         if (i == 0)
            row.append(decimalFormat.format(fullNonRegularAvg));
         row.append(";");
      }

      for (int i = 0; i < accountLevels; i++) {
         if (i == 0)
            row.append(decimalFormat.format(fullExtra));
         row.append(";");
      }

      for (int i = 0; i < accountLevels; i++) {
         if (i == 0)
            row.append(decimalFormat.format(fullExtraAvg));
         row.append(";");
      }

      for (int i = 0; i < accountLevels * countMonths; i++) {
         row.append(OUTPUT_SEPARATOR);
      }
      return row;
   }

   public StringBuffer buildAccountHeaders(int accountLevels, List<Period> periods) {
      StringBuffer headers = new StringBuffer();
      for (int i = 0; i < accountLevels; i++) {
         if (i == 0) {
            headers.append("AccountDetails");
         }
         headers.append(OUTPUT_SEPARATOR);
      }

      for (Period p : periods) {
         for (int i = 0; i < accountLevels; i++) {
            if (i == 0) {
               Date startDate = p.getStartDate();
               headers.append(sdf.format(startDate));
            }
            headers.append(OUTPUT_SEPARATOR);
         }
      }

      return headers;
   }

   public String format(BigDecimal bd) {
      return decimalFormat.format(bd);
   }

   public String getOutputSeparator() {
      return OUTPUT_SEPARATOR;
   }

   public String formatDate(Date date) {
      return sdf.format(date);
   }


}
