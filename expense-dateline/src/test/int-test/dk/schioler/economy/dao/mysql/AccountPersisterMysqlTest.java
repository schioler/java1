package dk.schioler.economy.dao.mysql;

import static org.junit.Assert.*;

import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;

import dk.schioler.economy.ExpenseException;
import dk.schioler.economy.model.Account;
import dk.schioler.economy.model.Pattern;
import dk.schioler.economy.model.User;
import dk.schioler.economy.model.Account.Type;
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
      String accName = "aName";
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


         a = new Account(null, Account.ROOT, u.getId(), accName+"1", Type.REGULAR, null);
         a = accPersister.createAccount(a);
         Account retrievedAccount = a.getAccount(a.getId());
         assertEquals(true, retrievedAccount.isRegular());

         LOG.debug(a);

         Account a2 = new Account(null, a, u.getId(), accName+"2",  Type.REGULAR, null);

         a2 = accPersister.createAccount(a2);
         retrievedAccount = a2.getAccount(a2.getId());
         assertEquals(true, retrievedAccount.isRegular());
         assertEquals(a2.getParent().getId(), a.getId() );
         LOG.debug(a);

         a = new Account(null, a2, u.getId(), accName+"3",Type.NON_REGULAR, null);
         a = accPersister.createAccount(a);
         retrievedAccount = a.getAccount(a.getId());
         assertEquals(false, retrievedAccount.isRegular());
         LOG.debug(a);


         Account acc1 = new Account(null, Account.ROOT, u.getId(), "noParent-id",Type.NON_REGULAR, null);
         Account acc2 = new Account(null, acc1, u.getId(), "pop",Type.NON_REGULAR, null);
         Account acc3 = new Account(null, acc2, u.getId(), "poppedreng",Type.NON_REGULAR, null);

         accPersister.createAccount(acc3);

         try {
            accPersister.createAccount(acc1);
            fail("no expected");
         } catch (ExpenseException e) {
            LOG.debug("expectedd exception...");
         }
      } catch (Exception e) {
         LOG.error(e.getMessage(), e);
         fail(e.getMessage());
      } finally {
         accPersister.deleteAllAccountsOnUser(u.getId());
         userPersister.deleteUser(u.getId());
      }
   }

}
