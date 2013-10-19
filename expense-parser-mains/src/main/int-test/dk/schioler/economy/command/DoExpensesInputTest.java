package dk.schioler.economy.command;

import static org.junit.Assert.*;

import java.util.Locale;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;

import dk.schioler.economy.main.DoExpenseInputMain;
import dk.schioler.economy.util.Log4JLoader;
import dk.schioler.economy.util.SpringFrameworkHelper;

public class DoExpensesInputTest {
   static {
      Log4JLoader.loadLog();
      Locale.setDefault(new Locale("da", "DK"));
   }

   static Logger LOG = Logger.getLogger(DoExpensesInputTest.class);

   @Test
   public void testPersistFiles() {

//      String configFile = "src/test/resources/account-files/danske/expense-parser-test-danske.xml";
//      AbstractApplicationContext springCtx = SpringFrameworkHelper.getDefaultApplicationContext();
//      try {
//         DoExpensesInput doInput = (DoExpensesInput) springCtx.getBean("doInput");
//         doInput.readUserFile(configFile);
//         doInput.establishInputFiles();
//         doInput.persistFiles();
//      } catch (Exception e) {
//         LOG.error(e.getMessage(), e);
//         fail(e.getMessage());
//      } finally {
//         springCtx.close();
//      }
   }

}
