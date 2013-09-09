package dk.schioler.economy.main;

import java.util.Locale;

import org.springframework.context.support.AbstractApplicationContext;

import dk.schioler.economy.command.DoAccountInsert;
import dk.schioler.economy.command.DoPatternInsert;
import dk.schioler.economy.util.Log4JLoader;
import dk.schioler.economy.util.SpringFrameworkHelper;

public class DoPatternInsertMain {
   static {
      Log4JLoader.loadLog();
      Locale.setDefault(new Locale("da", "DK"));
   }

   public static void main(String[] args) {
      AbstractApplicationContext springCtx = SpringFrameworkHelper.getDefaultApplicationContext();
      try {
         DoPatternInsert doInput = (DoPatternInsert) springCtx.getBean("doPatternInsert");
         doInput.doPatternInsert("src/main/expense-files/konto-liste-src.csv");

      } catch (Exception e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      } finally {
         springCtx.close();
      }
   }

}
