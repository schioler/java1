package dk.schioler.economy.in.parser;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.junit.Test;

import dk.schioler.economy.input.parser.ParserDanskeBank;
import dk.schioler.economy.model.Line;
import dk.schioler.economy.util.Log4JLoader;

public class ParserDanskeTest {
   static {
      Log4JLoader.loadLog();
      Locale.setDefault(new Locale("da", "DK"));
   }

   static Logger LOG = Logger.getLogger(ParserDanskeTest.class);

   public static final String LINE1 = "\"01.11.2012\";\"Næsgårdens Vandværk\";\"-488,99\";\"32.915,60\";\"Udført\";\"Nej\"";
   public static final String LINE2 = "\"02.11.2012\";\"Den Alm Danske Lægeforen\";\"-481,75\";\"32.433,85\";\"Udført\";\"Nej\"";
   public static final String LINE3 = "\"30.11.2012\";\"Overført fra: 4820285298\";\"10.500,00\";\"42.933,85\";\"Udført\";\"Nej\"";

   @Test
   public void testParse() {
      NumberFormat nf = NumberFormat.getNumberInstance();
      DecimalFormat df = (DecimalFormat) nf;
      ParserDanskeBank parser = new ParserDanskeBank();
      try {
         Number parse = df.parse("-488,99");
         BigDecimal bd = new BigDecimal(parse.floatValue()).setScale(2, BigDecimal.ROUND_HALF_EVEN);
         SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
         Line line = parser.parse(new Long(1), "danske", LINE1);
         LOG.debug("line1=" + line);
         assertEquals(sdf.parse("01.11.2012"), line.getDate());
         assertEquals(bd, line.getAmount());
         assertEquals("Næsgårdens Vandværk" , line.getText());
         assertEquals(new Long(1) , line.getUserId());
         assertEquals("danske" , line.getOrigin());
      } catch (Exception e) {
         LOG.error(e.getMessage(), e);
         fail(e.getMessage());
      }

   }

}
