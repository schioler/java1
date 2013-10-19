package dk.schioler.economy.persister.mysql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;

import dk.schioler.economy.model.Account;
import dk.schioler.economy.model.Line;
import dk.schioler.economy.model.User;
import dk.schioler.economy.persister.AccountPersister;
import dk.schioler.economy.persister.UserPersister;
import dk.schioler.economy.util.Log4JLoader;
import dk.schioler.economy.util.SpringFrameworkHelper;
import dk.schioler.economy.util.Util;

public class LinePersisterMySqlTest {
   static {
      Log4JLoader.loadLog();
   }

   private static final Logger LOG = Logger.getLogger(LinePersisterMySqlTest.class);

   @Test
   public void testPersistLine() {
      AbstractApplicationContext springCtx = SpringFrameworkHelper.getDefaultApplicationContext();
      UserPersister userPersister = (UserPersister) springCtx.getBean("userPersister");
      LinePersisterMySql linePersister = (LinePersisterMySql) springCtx.getBean("linePersister");
      User user = userPersister.getUser("lars.schioler");
      try {
         Locale dkLocale = new Locale("da", "DK");
         Date date = new Date();
         date = DateUtils.truncate(date, Calendar.DAY_OF_MONTH);
         Line line = new Line(null, user.getId(), "test", date, "linetext æøå", Util.createBigDecimal("-32.915,60", dkLocale), null, null);
         Line persistLine = linePersister.persistLine(line);
         LOG.debug(persistLine);
         Line line2 = linePersister.getLineNoMatch(user.getId(), line.getOrigin(), line.getDate(), line.getText(), line.getAmount());
         LOG.debug(line2);
         assertEquals(user.getId(), line2.getUserId());
         assertEquals(line.getAmount(), line2.getAmount());
      } catch (Exception e) {
         LOG.error(e.getMessage(), e);
         fail(e.getMessage());
      } finally {
         linePersister.deleteAllLinesOnUser(user.getId());
         springCtx.close();
      }
   }

}
