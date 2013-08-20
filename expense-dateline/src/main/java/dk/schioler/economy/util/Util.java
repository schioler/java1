package dk.schioler.economy.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Util {
   
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

   
   public static MinAndMaxDates getOutputFileDateFormatFrom(Date minDate, Date maxDate){
      return new MinAndMaxDates(outFileFormatter.format(minDate), outFileFormatter.format(maxDate));
   }
   public static MinAndMaxDates getDayOfWeekFrom(Date minDate, Date maxDate){      
      return new MinAndMaxDates(dayOfWeek.format(minDate), dayOfWeek.format(maxDate));
   }
   
   static String outFileNameFormat = "yyyy-MM-dd-HHmmss";
   static SimpleDateFormat outFileFormatter = new SimpleDateFormat(outFileNameFormat);
   static SimpleDateFormat dayOfWeek = new SimpleDateFormat("EEE", new Locale(Locale.ENGLISH.getLanguage()));
   
   
   
   public static final class MinAndMaxDates{
      public String minDate;
      public String maxDate;
      public MinAndMaxDates(String minDate, String maxDate) {
         super();
         this.minDate = minDate;
         this.maxDate = maxDate;
      }
      
   }
}
