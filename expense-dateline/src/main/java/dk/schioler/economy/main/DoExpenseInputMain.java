package dk.schioler.economy.main;

import java.util.Locale;

import org.apache.log4j.Logger;
import org.springframework.context.support.AbstractApplicationContext;

import dk.schioler.economy.command.DoExpensesInput;
import dk.schioler.economy.util.Log4JLoader;
import dk.schioler.economy.util.SpringFrameworkHelper;

public class DoExpenseInputMain {
   static {
      Log4JLoader.loadLog();
      Locale.setDefault(new Locale("da", "DK"));
   }

   static Logger LOG = Logger.getLogger(DoExpenseInputMain.class);

   public static void main(String[] args) {
      AbstractApplicationContext springCtx = SpringFrameworkHelper.getDefaultApplicationContext();
      try {
         DoExpensesInput doInput = (DoExpensesInput) springCtx.getBean("doInput");
         doInput.readUserFile(args[0]);
         doInput.establishInputFiles();
         doInput.persistFiles();
      } catch (Exception e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      } finally {
         springCtx.close();
      }
      /*
       * Load account plan
       *
       * for each file for each line parse determine category/group/account if
       * found -> write to database else -> write to errorfile
       */

   }

}
