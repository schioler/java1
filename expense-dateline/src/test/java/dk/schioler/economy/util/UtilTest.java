package dk.schioler.economy.util;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

public class UtilTest {

   @Test
   public void test() {
      try {
         SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
         Date inDate = sdf.parse("2012-07-06 23:12:45");
         Date startDate = sdf.parse("2012-07-01 00:00:00");
         Date endDate = sdf.parse("2012-07-31 23:59:59");

         Date[] spanMonth = Util.spanMonth(inDate);

         assertEquals(startDate, spanMonth[0]);
         assertEquals(endDate, spanMonth[1]);

      } catch (Exception e) {
         e.printStackTrace();
         fail(e.getMessage());
      }
   }

}
