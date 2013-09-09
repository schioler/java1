package dk.schioler.economy.dao.mysql;

import static org.junit.Assert.*;

import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;

import dk.schioler.economy.model.Account;
import dk.schioler.economy.model.Pattern;
import dk.schioler.economy.model.User;
import dk.schioler.economy.persister.AccountPersister;
import dk.schioler.economy.persister.PatternPersister;
import dk.schioler.economy.persister.UserPersister;
import dk.schioler.economy.util.Log4JLoader;
import dk.schioler.economy.util.SpringFrameworkHelper;

public class AccountPersisterMysqlTest {
   static {
      Log4JLoader.loadLog();
   }

   private static final Logger LOG = Logger.getLogger(AccountPersisterMysqlTest.class);

   @Test
   public void test() {

      AbstractApplicationContext springCtx = SpringFrameworkHelper.getDefaultApplicationContext();

      UserPersister userPersister = (UserPersister) springCtx.getBean("userPersister");
      AccountPersister accPersister = (AccountPersister) springCtx.getBean("accountPersister");

      String username = "test.user";
      String accName = "test.accountName";
      User u = null;
      Account a = null;
      try {
         User user = userPersister.getUser(username);
         if (user == null) {
            u = new User(null, username, null);
            u = userPersister.createUser(u);
         } else {
            u = user;
         }

         a = new Account(null, null, u.getId(), accName+"1", "", 0, true, true, null);
         a = accPersister.createAccount(a);
         Account retrievedAccount = a.getAccount(a.getId());
         assertEquals(true, retrievedAccount.isRegular());
         assertEquals(true, retrievedAccount.isUseInAverage());
         LOG.debug(a);

         a = new Account(null, null, u.getId(), accName+"2", "", 0, false, true, null);
         a = accPersister.createAccount(a);
         retrievedAccount = a.getAccount(a.getId());
         assertEquals(true, retrievedAccount.isRegular());
         assertEquals(false, retrievedAccount.isUseInAverage());
         LOG.debug(a);

         a = new Account(null, null, u.getId(), accName+"3", "", 0, true, false, null);
         a = accPersister.createAccount(a);
         retrievedAccount = a.getAccount(a.getId());
         assertEquals(false, retrievedAccount.isRegular());
         assertEquals(true, retrievedAccount.isUseInAverage());
         LOG.debug(a);

      } catch (Exception e) {
         LOG.error(e.getMessage(), e);
         fail(e.getMessage());
      } finally {
         accPersister.deleteAllAccountsOnUser(u.getId());
         userPersister.deleteUser(u.getId());
      }
   }

}
