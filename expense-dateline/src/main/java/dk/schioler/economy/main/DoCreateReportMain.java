package dk.schioler.economy.main;

import java.util.Locale;

import org.apache.log4j.Logger;
import org.springframework.context.support.AbstractApplicationContext;

import dk.schioler.economy.command.DoReport;
import dk.schioler.economy.util.Log4JLoader;
import dk.schioler.economy.util.SpringFrameworkHelper;

public class DoCreateReportMain {
   static {
      Log4JLoader.loadLog();
      Locale.setDefault(new Locale("da", "DK"));
   }

   private static final Logger LOG = Logger.getLogger(DoCreateReportMain.class);
   public static void main(String[] args) {
      AbstractApplicationContext springCtx = SpringFrameworkHelper.getDefaultApplicationContext();
      try {
         DoReport doReport =    (DoReport) springCtx.getBean("doReport");

         doReport.readUserFile("src/main/tokenfiles-lasc/expense-parser.xml");
         doReport.doReport();


      } catch (Exception e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      } finally {
         springCtx.close();
      }
   }

}
