package dk.schioler.economy.dao.mysql;

import static org.junit.Assert.*;

import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;

import dk.schioler.economy.model.User;
import dk.schioler.economy.persister.UserPersister;
import dk.schioler.economy.util.Log4JLoader;
import dk.schioler.economy.util.SpringFrameworkHelper;

public class UserPersisterMysqlTest {
   static {
      Log4JLoader.loadLog();
   }

   private static final Logger LOG = Logger.getLogger(UserPersisterMysqlTest.class);

   @Test
   public void test() {
      AbstractApplicationContext springCtx = SpringFrameworkHelper.getDefaultApplicationContext();
      UserPersister persister = (UserPersister) springCtx.getBean("userPersister");

      List<User> users = persister.getUsers();
      for (User user : users) {
         LOG.debug(user);
         persister.deleteUser(user.getId());
      }

      User u = new User(null, "test", null);
      User createUser = persister.createUser(u);
      LOG.debug(createUser);
      User u2 = new User(null, "test2", null);
      User cu2 = persister.createUser(u2);
      LOG.debug(cu2);

      try {
         User u3 = new User(null, "test2", null);
         persister.createUser(u3);
         fail("should have thrown...");
      } catch (Exception e) {
         LOG.debug(e.getMessage());
      }

      User updateUser = new User(cu2.getId(), "updated", cu2.getTimestamp());
      LOG.debug(updateUser);
      int saveUser = persister.saveUser(updateUser);
      assertEquals(1, saveUser);

      User user = persister.getUser(updateUser.getId());
      assertEquals(updateUser.getId(), user.getId());
      assertEquals("updated", user.getName());

      users = persister.getUsers();
      assertEquals(2, users.size());
      for (User usert : users) {
         LOG.debug(user);
         persister.deleteUser(usert.getId());
      }
   }

}
