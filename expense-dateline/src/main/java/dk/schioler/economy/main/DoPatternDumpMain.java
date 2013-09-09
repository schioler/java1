package dk.schioler.economy.main;

import java.util.Locale;

import org.springframework.context.support.AbstractApplicationContext;

import dk.schioler.economy.command.DoAccountInsert;
import dk.schioler.economy.command.DoPatternDump;
import dk.schioler.economy.util.Log4JLoader;
import dk.schioler.economy.util.SpringFrameworkHelper;

public class DoPatternDumpMain {
   static {
      Log4JLoader.loadLog();
      Locale.setDefault(new Locale("da", "DK"));
   }

   public static void main(String[] args) {
      AbstractApplicationContext springCtx = SpringFrameworkHelper.getDefaultApplicationContext();
      try {
         DoPatternDump doDump =   (DoPatternDump) springCtx.getBean("doPatternDump");
         doDump.execute("patternDump.ods", "lars");

      } catch (Exception e) {
         e.printStackTrace();
      } finally {
         springCtx.close();
      }
   }

}
