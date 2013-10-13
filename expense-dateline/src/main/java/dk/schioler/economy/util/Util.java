package dk.schioler.economy.util;

import java.io.File;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;

public class Util {
   private static final Logger LOG = Logger.getLogger(Util.class);

   public synchronized static BigDecimal createBigDecimal(Number n) {
      BigDecimal parse = new BigDecimal(n.floatValue());
      parse = parse.setScale(2, BigDecimal.ROUND_HALF_EVEN);
      return parse;
   }

   public synchronized static BigDecimal createBigDecimal(String s, Locale locale) throws ParseException {
      NumberFormat nf = NumberFormat.getNumberInstance(locale);
      DecimalFormat df = (DecimalFormat) nf;
      Number n = df.parse(s);
      return createBigDecimal(n);
   }

   public synchronized static BigDecimal createBigDecimal(String s) throws ParseException {
      return createBigDecimal(s, Locale.getDefault());
   }

   public static String buildOutputFileName(File inputFile, String suffix, String pattern, Date minDate, Date maxDate) {
      File logFile = inputFile;
      File outputDirectory = null;
      if (logFile.isFile()) {
         outputDirectory = logFile.getParentFile();
      } else if (logFile.isDirectory()) {
         outputDirectory = logFile;
      }

      File out = new File(outputDirectory.getAbsoluteFile() + "/out");
      if (!out.exists()) {
         out.mkdir();
      }

      MinAndMaxDates minAndMax = getOutputFileDateFormatFrom(minDate, maxDate);
      String fileName = minAndMax.minDate + "_" + minAndMax.maxDate + "_" + pattern + suffix;
      return outputDirectory.getAbsolutePath() + "/out/" + fileName;

   }

   public static String buildOutputFileName(String suffix, Date minDate, Date maxDate) {
      MinAndMaxDates minAndMax = getOutputFileDateFormatFrom(minDate, maxDate);
      String fileName = minAndMax.minDate + "_" + minAndMax.maxDate + suffix;

      return fileName;
   }

   public static MinAndMaxDates getOutputFileDateFormatFrom(Date minDate, Date maxDate) {
      return new MinAndMaxDates(outFileFormatter.format(minDate), outFileFormatter.format(maxDate));
   }

   public static MinAndMaxDates getDayOfWeekFrom(Date minDate, Date maxDate) {
      return new MinAndMaxDates(dayOfWeek.format(minDate), dayOfWeek.format(maxDate));
   }

   static String outFileNameFormat = "yyyy-MM-dd-HHmmss";
   static SimpleDateFormat outFileFormatter = new SimpleDateFormat(outFileNameFormat);
   static SimpleDateFormat dayOfWeek = new SimpleDateFormat("EEE", new Locale(Locale.ENGLISH.getLanguage()));

   public static final class MinAndMaxDates {
      public String minDate;
      public String maxDate;

      public MinAndMaxDates(String minDate, String maxDate) {
         super();
         this.minDate = minDate;
         this.maxDate = maxDate;
      }

   }

   public static Date[] spanMonth(Date firstDate) {
      Date[] startAndEnd = new Date[2];
      startAndEnd[0] = DateUtils.truncate(firstDate, Calendar.MONTH);
      Date startDateEndOfMonth = DateUtils.addMonths(startAndEnd[0], 1);
      startAndEnd[1] = DateUtils.addSeconds(startDateEndOfMonth, -1);
      LOG.debug("dates=" + Arrays.toString(startAndEnd));
      return startAndEnd;
   }

   public static DecimalFormat configureDecimalFormatForCSV() {
      DecimalFormat decimalFormat = null;
      NumberFormat f = NumberFormat.getInstance(Locale.getDefault());
      if (f instanceof DecimalFormat) {
         decimalFormat = (DecimalFormat) f;
         decimalFormat.setDecimalSeparatorAlwaysShown(true);
         decimalFormat.applyPattern("####,###,##0.00");
      }
      return decimalFormat;
   }
}
