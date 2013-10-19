package dk.schioler.economy.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.stereotype.Component;

import dk.schioler.economy.AccountTreeRoot;
import dk.schioler.economy.command.DoAccountInsert;
import dk.schioler.economy.expenseparser.schema.AccountFileType;
import dk.schioler.economy.expenseparser.schema.ExpenseParserConfigType;
import dk.schioler.economy.inoutput.AccountInOutputReader;
import dk.schioler.economy.inoutput.FactoryAccountInOutputReader;
import dk.schioler.economy.util.Log4JLoader;
import dk.schioler.economy.util.MarshallHelper;
import dk.schioler.economy.util.SpringFrameworkHelper;

@Component("doAccountInsertMain")
public class DoAccountInsertMain {
   static {
      Log4JLoader.loadLog();
      Locale.setDefault(new Locale("da", "DK"));
   }
   private static final Logger LOG = Logger.getLogger(DoAccountInsertMain.class);

   @Autowired
   MarshallHelper marshallHelper;

   @Autowired
   DoAccountInsert doAccountInsert;

   @Autowired
   FactoryAccountInOutputReader accountInputReaderFactory;

   private void doIt(String userConfigFile) throws IOException {

      ExpenseParserConfigType userConfig = marshallHelper.loadUserConfig(userConfigFile);
      AccountFileType accountPlan = userConfig.getConfig().getAccountPlan();

      String user = userConfig.getUser().getOwner();

      String name = accountPlan.getName();
      String type = accountPlan.getType();

      LOG.debug("user=" + user + ", accountFile=" + name + ", type=" + type);

      AccountInOutputReader accountInputReader = accountInputReaderFactory.getAccountInOutputReader(type);

      FileInputStream fis = new FileInputStream(new File(name));

      AccountTreeRoot accountStructure = accountInputReader.readAccounts(fis);

      doAccountInsert.execute(user, accountStructure);
   }

   public static void main(String[] args) {
      AbstractApplicationContext springCtx = SpringFrameworkHelper.getDefaultApplicationContext();
      try {

         DoAccountInsertMain main = (DoAccountInsertMain) springCtx.getBean("doAccountInsertMain");
         main.doIt(args[0]);

      } catch (Exception e) {
         LOG.error(e.getMessage(), e);
      } finally {
         springCtx.close();
      }
   }

}
