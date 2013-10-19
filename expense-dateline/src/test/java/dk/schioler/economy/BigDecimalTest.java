package dk.schioler.economy;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Locale;

import org.junit.Test;

import dk.schioler.economy.util.Util;

public class BigDecimalTest {

   @Test
   public void test() {
      Locale.setDefault(new Locale("da", "DK"));
      BigDecimal bDecimal = null;
      try {
         String amount = "-278,79";
//         bDecimal = new BigDecimal(amount);
         System.out.println(bDecimal);
         bDecimal = Util.createBigDecimal(amount);
         System.out.println(bDecimal);
      } catch (ParseException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

}
