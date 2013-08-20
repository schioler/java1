package dk.schioler.economy.main;

import java.util.Locale;

import org.springframework.context.support.AbstractApplicationContext;

import dk.schioler.economy.in.DoAccountAndPatternInsert;
import dk.schioler.economy.util.Log4JLoader;
import dk.schioler.economy.util.SpringFrameworkHelper;

public class DoAccountAndPatternInsertMain {
   static {
      Log4JLoader.loadLog();
      Locale.setDefault(new Locale("da", "DK"));
   }

   public static void main(String[] args) {
      AbstractApplicationContext springCtx = SpringFrameworkHelper.getDefaultApplicationContext();
      try {
         DoAccountAndPatternInsert doInput =  (DoAccountAndPatternInsert) springCtx.getBean("doAccountInsert");
         doInput.doAccountInsert("src/main/doc/konto-liste-src-export.csv");
         
      } catch (Exception e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      } finally {
         springCtx.close();
      }
   }

}
