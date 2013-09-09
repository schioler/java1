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

public class PatternPersisterMysqlTest {
   static {
      Log4JLoader.loadLog();
   }

   private static final Logger LOG = Logger.getLogger(PatternPersisterMysqlTest.class);

   @Test
   public void test() {

      AbstractApplicationContext springCtx = SpringFrameworkHelper.getDefaultApplicationContext();

      UserPersister userPersister = (UserPersister) springCtx.getBean("userPersister");
      AccountPersister accPersister = (AccountPersister) springCtx.getBean("accountPersister");
      PatternPersister persister = (PatternPersister) springCtx.getBean("patternPersister");

      String username = "user";
      String accName = "accountName";
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

         Account account = accPersister.getAccount(u.getId(), "", accName);
         if (account == null) {
            a = new Account(null, null, u.getId(), accName, "", 0, true, true, null);
            a = accPersister.createAccount(a);
         } else {
            a = account;
         }

         Pattern pattern1 = new Pattern(null, a.getId(), "ilva", a.getFullPath());
         Pattern createPattern = persister.createPattern(a.getId(), pattern1);
         LOG.debug(createPattern);
         Pattern pattern2 = new Pattern(null, a.getId(), "silva", a.getFullPath());
         createPattern = persister.createPattern(a.getId(), pattern2);
         LOG.debug(createPattern);

         List<Pattern> patterns = persister.getPatternsOnAccountId(a.getId());
         assertEquals(2, patterns.size());

         int deletePatterns = persister.deletePatterns(a.getId());
         assertEquals(2, deletePatterns);

         patterns = persister.getPatternsOnAccountId(a.getId());
         assertEquals(0, patterns.size());

         persister.createPattern(a.getId(), pattern1);
         persister.createPattern(a.getId(), pattern2);

         patterns = persister.getPatternsOnAccountId(a.getId());
         assertEquals(2, patterns.size());

         int deleteAllPatternsOnUser = persister.deleteAllPatternsOnUser(u.getId());
         assertEquals(2, deleteAllPatternsOnUser);
         patterns = persister.getPatternsOnAccountId(a.getId());
         assertEquals(0, patterns.size());
      } catch (Exception e) {
         LOG.error(e.getMessage(), e);
         fail(e.getMessage());
      } finally {
         userPersister.deleteUser(u.getId());
         accPersister.deleteAccount(a.getId());
      }
   }

}
