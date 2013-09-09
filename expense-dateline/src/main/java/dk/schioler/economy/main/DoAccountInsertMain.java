package dk.schioler.economy.main;

import java.util.Locale;

import org.springframework.context.support.AbstractApplicationContext;

import dk.schioler.economy.command.DoAccountInsert;
import dk.schioler.economy.util.Log4JLoader;
import dk.schioler.economy.util.SpringFrameworkHelper;

public class DoAccountInsertMain {
   static {
      Log4JLoader.loadLog();
      Locale.setDefault(new Locale("da", "DK"));
   }

   public static void main(String[] args) {
      AbstractApplicationContext springCtx = SpringFrameworkHelper.getDefaultApplicationContext();
      try {
         DoAccountInsert doInput =  (DoAccountInsert) springCtx.getBean("doAccountInsert");
         doInput.doAccountInsert("src/main/tokenfiles-lasc/account-list.ods");

      } catch (Exception e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      } finally {
         springCtx.close();
      }
   }

}
