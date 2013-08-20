package dk.schioler.economy.in.parser;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

@Component("parserLaanOgSpar")
public class ParserLaanOgSpar extends ParserBase {
   private static final Logger LOG = Logger.getLogger(ParserLaanOgSpar.class);
   /*
    * Date1;Date2;Desc;Amount;Saldo
    * "28-06-2013";"28-06-2013";"Telia, juni";"-338,00";"42.289,13"
    */
   static String separator = ";";
   static final int IDX_DATE = 1;
   static final int IDX_DESCRIPTION = 2;
   static final int IDX_AMOUNT = 3;
   // NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("da",
   // "DK"));
   NumberFormat nf = NumberFormat.getNumberInstance(new Locale("da", "DK"));
   DecimalFormat df = (DecimalFormat) nf;

   @Override
   protected String getSeparator() {
      return separator;
   }

   @Override
   protected BigDecimal getAmount(String[] fields) {
      String amount = fields[IDX_AMOUNT];
      amount = StringUtils.remove(amount, "\"");
      // LOG.debug("amount=" + amount);
      BigDecimal bd = null;
      try {
         Number parse = df.parse(amount);
         // df.
         bd = new BigDecimal(parse.floatValue());
         // bd.
      } catch (ParseException e) {
         LOG.error(e);
      }

      return bd;
   }

   @Override
   protected String getText(String[] fields) {
      String text = fields[IDX_DESCRIPTION];
      return StringUtils.remove(text, "\"");
   }

   SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

   @Override
   protected Date getDate(String[] fields) {
      String date = fields[IDX_DATE];
      date = StringUtils.remove(date, "\"");
      Date parse = null;
      try {
         parse = sdf.parse(date);
      } catch (ParseException e) {
         LOG.error(e);
      }
      return parse;
   }

   protected boolean includeLineInOutput(String[] line) {
      boolean include = false;
      if (!(line[0].startsWith("Date1"))) {
         include = super.includeLineInOutput(line);
      }
      return include;
   }
}
