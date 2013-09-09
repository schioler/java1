package dk.schioler.economy.main;

import java.util.Locale;

import org.springframework.context.support.AbstractApplicationContext;

import dk.schioler.economy.command.DoPatternInsert;
import dk.schioler.economy.util.Log4JLoader;
import dk.schioler.economy.util.SpringFrameworkHelper;

public class DoPatternInputMain {
   static {
      Log4JLoader.loadLog();
      Locale.setDefault(new Locale("da", "DK"));
   }

   public static void main(String[] args) {
      AbstractApplicationContext springCtx = SpringFrameworkHelper.getDefaultApplicationContext();
      try {
         DoPatternInsert doDump =   (DoPatternInsert) springCtx.getBean("doPatternInsert");
         doDump.doPatternInsert("src/main/tokenfiles-lasc/patternSrc.ods");

      } catch (Exception e) {
         e.printStackTrace();
      } finally {
         springCtx.close();
      }
   }

}
