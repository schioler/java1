package dk.schioler.economy.input.parser;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import dk.schioler.economy.util.Util;

@Component("parserDanskeBank")
public class ParserDanskeBank extends ParserBase {
   private static final Logger LOG = Logger.getLogger(ParserDanskeBank.class);
   static String separator = ";";
   //
   //   public static final String LINE1 = "\"01.11.2012\";\"Næsgårdens Vandværk\";\"-488,99\";\"32.915,60\";\"Udført\";\"Nej\"";
   //   public static final String LINE2 = "\"02.11.2012\";\"Den Alm Danske Lægeforen\";\"-481,75\";\"32.433,85\";\"Udført\";\"Nej\"";
   //   public static final String LINE3 = "\"30.11.2012\";\"Overført fra: 4820285298\";\"10.500,00\";\"42.933,85\";\"Udført\";\"Nej\"";

   static final int IDX_DATE = 0;
   static final int IDX_DESCRIPTION = 1;
   static final int IDX_AMOUNT = 2;
   static final String DANSKEBANK_ENCODING = "ISO-8859-1";

   private final Locale dkLocale = new Locale("da", "DK");

   @Override
   protected String getSeparator() {
      return separator;
   }

   @Override
   protected BigDecimal getAmount(String[] fields) {
      String amount = fields[IDX_AMOUNT].replace("\"", "");
      BigDecimal bd = null;
      try {
         bd = Util.createBigDecimal(amount, dkLocale);
      } catch (ParseException e) {
         LOG.error(e);
      }

      return bd;
   }

   @Override
   protected String getText(String[] fields) {
      String string = fields[IDX_DESCRIPTION];
      return string.replace("\"", "");
   }

   SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

   @Override
   protected Date getDate(String[] fields) {
      Date parse = null;
      try {
         String dStr = fields[IDX_DATE];
         parse = sdf.parse(dStr.replace("\"", ""));
      } catch (ParseException e) {
         e.printStackTrace();
      }
      return parse;
   }

   protected boolean includeLineInOutput(String[] line) {
      boolean include = false;
      String l = line[0].replace("\"", "");
      if (!(l.startsWith("Dato")) && !(l.startsWith("Konto")) && !(l.startsWith("Data"))) {
         include = super.includeLineInOutput(line);
      }
      return include;
   }

   public String getEncoding() {

      return DANSKEBANK_ENCODING;
   }

}
