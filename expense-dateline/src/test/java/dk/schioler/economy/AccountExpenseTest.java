package dk.schioler.economy;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import org.junit.Test;

import dk.schioler.economy.model.Account;
import dk.schioler.economy.model.Account.Type;

public class AccountExpenseTest {

   @Test
   public void testAddExpenses() {
      Locale.setDefault(new Locale("da", "DK"));
      BigDecimal bd1 = new BigDecimal("10.50");
      BigDecimal bd2 = new BigDecimal("10.51");
      BigDecimal bd12 = new BigDecimal("21.01");
      BigDecimal bd3 = new BigDecimal("10.49");
      BigDecimal bd123 = new BigDecimal("31.50");

      BigDecimal bd4 = new BigDecimal("10.85");
      BigDecimal bd1234 = new BigDecimal("42.35");


      Account a = new Account(null, null, null, "test", Type.REGULAR, null);

      a.addToRegular(bd1);

      assertEquals(bd1, a.getExpensesRegular());

      a.addToRegular(bd2);

      assertEquals(bd12, a.getExpensesTotal());

      a.addToRegular(bd3);

      assertEquals(bd123, a.getExpensesTotal());
      a.addToRegular(bd4);

      assertEquals(bd1234, a.getExpensesTotal());
      NumberFormat nf = NumberFormat.getNumberInstance();
      DecimalFormat df = (DecimalFormat) nf;
      String amount = "8,85";
      BigDecimal bd = null;
      BigDecimal bd12345 = new BigDecimal("51.20");
      try {
         Number parse = df.parse(amount);


         bd = new BigDecimal(parse.floatValue()).setScale(2, BigDecimal.ROUND_HALF_EVEN);
         System.out.println("bf="+bd);
         a.addToRegular(bd);
         assertEquals(bd12345, a.getExpensesTotal());
      } catch (ParseException e) {
         e.printStackTrace();
      }


   }
}
