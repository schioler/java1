package dk.schioler.economy.in.parser;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

@Component("parserNykredit")
public class ParserNykredit extends ParserBase {
   private static final Logger LOG = Logger.getLogger(ParserNykredit.class);
   static String separator = ";";

   /*
    * sample: 28-06-2013;Fremmed automat i åbningstid;-8,00;N;192,98;28-06-2013;
    */;
   static final int IDX_DATE = 0;
   static final int IDX_DESCRIPTION = 1;
   static final int IDX_AMOUNT = 2;

   NumberFormat nf = NumberFormat.getNumberInstance();
   DecimalFormat df = (DecimalFormat) nf;

   @Override
   protected String getSeparator() {
      return separator;
   }

   @Override
   protected BigDecimal getAmount(String[] fields) {
      String amount = fields[IDX_AMOUNT].replace("\"", "");
      // LOG.debug("amount=" + amount);
      BigDecimal bd = null;
      try {
         Number parse = df.parse(amount);
         bd = new BigDecimal(parse.floatValue());
      } catch (ParseException e) {
         LOG.error(e);
      }

      return bd;
   }

   @Override
   protected String getText(String[] fields) {
      return fields[IDX_DESCRIPTION];
   }

   SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

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
      if (!(l.startsWith("Dato")) && !(l.startsWith("Konto"))&& !(l.startsWith("Data"))) {
         include = super.includeLineInOutput(line);
      }
      return include;
   }

}
