package dk.schioler.economy.main;

import java.util.Locale;

import org.jfree.util.Log;
import org.springframework.context.support.AbstractApplicationContext;

import dk.schioler.economy.expenseparser.schema.ExpenseParserConfigType;
import dk.schioler.economy.model.User;
import dk.schioler.economy.persister.UserPersister;
import dk.schioler.economy.util.Log4JLoader;
import dk.schioler.economy.util.MarshallHelper;
import dk.schioler.economy.util.SpringFrameworkHelper;

public class DoUsersInsertMain {
   static {
      Log4JLoader.loadLog();
      Locale.setDefault(new Locale("da", "DK"));
   }

   public static void main(String[] args) {
      AbstractApplicationContext springCtx = SpringFrameworkHelper.getDefaultApplicationContext();
      try {

         MarshallHelper marshallHelper = (MarshallHelper) springCtx.getBean("marshallHelper");
         ExpenseParserConfigType userConfig = marshallHelper.loadUserConfig(args[0]);

         String owner = userConfig.getUser().getOwner();

         UserPersister userPersister = (UserPersister) springCtx.getBean("userPersister");
         User user = new User(null, owner, null);
         user = userPersister.createUser(user);
         Log.debug("user=" + user);



      } catch (Exception e) {
         e.printStackTrace();
      } finally {
         springCtx.close();
      }
   }

}
