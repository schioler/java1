package dk.schioler.economy.out.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;

import dk.schioler.economy.AccountPersisterTestIFactorympl;
import dk.schioler.economy.AccountTreeRoot;
import dk.schioler.economy.model.Account;
import dk.schioler.economy.model.User;
import dk.schioler.economy.out.data.VisitorIsRelevantTypeInPath.OutputDataType;
import dk.schioler.economy.out.data.chart.VisitorChartIsRelevantTypeInPath;
import dk.schioler.economy.visitor.VisitorLogTree;

public class VisitorGetAccountRowsInLevelTest {
   private static final Logger LOG = Logger.getLogger(OutputDataBuilderRegularTest.class);
   SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");

   User user = new User(new Long(1), "test", null);
   AccountPersisterTestIFactorympl factory = new AccountPersisterTestIFactorympl(user);

   @Test
   public void test() {
      try {
         Long userId = Long.valueOf(1);
         // Creates tree of 6 accounts
         AccountTreeRoot treeRoot = factory.buildAccountTree(user.getId());
         // Account boligAccount = new Account(Long.valueOf(1), Long.valueOf(-1), userId, "Bolig", "", 0, Type.TYPE_REGULAR, new Date());
         //    Account boligAccountFinans = new Account(Long.valueOf(2), Long.valueOf(1), userId,"Finans", "Bolig", 1, Type.TYPE_REGULAR, new Date());
         //       Account boligAccountFinansLaan = new Account(Long.valueOf(3), Long.valueOf(2), userId, "Lån", "Bolig/Finans", 2, Type.TYPE_REGULAR, new Date());
         //
         //    Account boligAccountTag = new Account(Long.valueOf(4), Long.valueOf(1), userId, "Tag", "Bolig", 1, Type.TYPE_REGULAR, new Date());
         //
         //    Account boligAccountVVS = new Account(Long.valueOf(5), Long.valueOf(1), userId, "VVS", "Bolig", 1 , Type.TYPE_NON_REGULAR, new Date());
         //       Account boligAccountVVSExtra = new Account(Long.valueOf(6), Long.valueOf(5), userId, "Extra", "Bolig/VVS", 2 , Type.TYPE_EXTRAORDINAIRE, new Date());
         //       Account boligAccountVVSCheck = new Account(Long.valueOf(7), Long.valueOf(5), userId, "Check", "Bolig/VVS", 2 , Type.TYPE_NON_REGULAR, new Date());

         List<Account> accounts = null;
         Account account = null;
         int actualCountLevels = 0;
         VisitorGetAccountRowsInLevel getAccountsInLevel = null;
         VisitorLogTree treeLogger = new VisitorLogTree(true);
         treeRoot.accept(treeLogger);

         // adding a level: levels=2, 3 accounts.
         getAccountsInLevel = new VisitorGetAccountRowsInLevel(2, "/Bolig", new VisitorChartIsRelevantTypeInPath(VisitorIsRelevantTypeInPath.OutputDataType.REGULAR));
         treeRoot.accept(getAccountsInLevel);

         accounts = getAccountsInLevel.getAccounts();
         account = getAccountsInLevel.getAccount();
         actualCountLevels = getAccountsInLevel.getActualCountLevels();

         for (Account account2 : accounts) {
            LOG.debug(account2);
         }

         assertEquals(3, accounts.size());
         assertEquals("Bolig", account.getName());
         assertEquals(2, actualCountLevels);

         getAccountsInLevel = new VisitorGetAccountRowsInLevel(3, "/Bolig", new VisitorChartIsRelevantTypeInPath(VisitorIsRelevantTypeInPath.OutputDataType.REGULAR));
         treeRoot.accept(getAccountsInLevel);

         accounts = getAccountsInLevel.getAccounts();
         account = getAccountsInLevel.getAccount();
         actualCountLevels = getAccountsInLevel.getActualCountLevels();

         for (Account account2 : accounts) {
            LOG.debug(account2);
         }

         assertEquals(4, accounts.size());
         assertEquals("Bolig", account.getName());
         assertEquals(3, actualCountLevels);

         getAccountsInLevel = new VisitorGetAccountRowsInLevel(1, "/Bolig/Finans", new VisitorChartIsRelevantTypeInPath(VisitorIsRelevantTypeInPath.OutputDataType.REGULAR));
         treeRoot.accept(getAccountsInLevel);

         accounts = getAccountsInLevel.getAccounts();
         account = getAccountsInLevel.getAccount();
         actualCountLevels = getAccountsInLevel.getActualCountLevels();

         assertEquals(1, accounts.size());
         assertEquals("/Bolig/Finans", account.getFullPath());
         assertEquals(1, actualCountLevels);

         getAccountsInLevel = new VisitorGetAccountRowsInLevel(2, "/Bolig/Finans", new VisitorChartIsRelevantTypeInPath(VisitorIsRelevantTypeInPath.OutputDataType.REGULAR));
         treeRoot.accept(getAccountsInLevel);

         accounts = getAccountsInLevel.getAccounts();
         account = getAccountsInLevel.getAccount();
         actualCountLevels = getAccountsInLevel.getActualCountLevels();

         assertEquals(2, accounts.size());
         assertEquals("/Bolig/Finans", account.getFullPath());
         assertEquals(2, actualCountLevels);

         getAccountsInLevel = new VisitorGetAccountRowsInLevel(3, "/Bolig/Finans", new VisitorChartIsRelevantTypeInPath(VisitorIsRelevantTypeInPath.OutputDataType.REGULAR));
         treeRoot.accept(getAccountsInLevel);

         accounts = getAccountsInLevel.getAccounts();
         account = getAccountsInLevel.getAccount();
         actualCountLevels = getAccountsInLevel.getActualCountLevels();

         assertEquals(2, accounts.size());
         assertEquals("/Bolig/Finans", account.getFullPath());
         assertEquals(2, actualCountLevels);

         LOG.debug("***************");
         getAccountsInLevel = new VisitorGetAccountRowsInLevel(1, "/Bolig", new VisitorChartIsRelevantTypeInPath(VisitorIsRelevantTypeInPath.OutputDataType.NONREGULAR));
         treeRoot.accept(getAccountsInLevel);
         accounts = getAccountsInLevel.getAccounts();
         //         account = getAccountsInLevel.getAccount();
         actualCountLevels = getAccountsInLevel.getActualCountLevels();

         for (Account account2 : accounts) {
            LOG.debug(account2);
         }

         assertEquals(1, accounts.size());
         assertEquals("/Bolig", getAccountsInLevel.getAccount().getFullPath());
         assertEquals(1, actualCountLevels);

         LOG.debug("***************");
         getAccountsInLevel = new VisitorGetAccountRowsInLevel(2, "/Bolig", new VisitorChartIsRelevantTypeInPath(VisitorIsRelevantTypeInPath.OutputDataType.NONREGULAR));
         treeRoot.accept(getAccountsInLevel);
         accounts = getAccountsInLevel.getAccounts();
         account = getAccountsInLevel.getAccount();
         actualCountLevels = getAccountsInLevel.getActualCountLevels();

         for (Account account2 : accounts) {
            LOG.debug(account2);
         }

         assertEquals(2, accounts.size());
         assertEquals("/Bolig", account.getFullPath());
         assertEquals(2, actualCountLevels);

         LOG.debug("***************");
         getAccountsInLevel = new VisitorGetAccountRowsInLevel(3, "/Bolig", new VisitorChartIsRelevantTypeInPath(VisitorIsRelevantTypeInPath.OutputDataType.NONREGULAR));
         treeRoot.accept(getAccountsInLevel);
         accounts = getAccountsInLevel.getAccounts();
         account = getAccountsInLevel.getAccount();
         actualCountLevels = getAccountsInLevel.getActualCountLevels();

         for (Account account2 : accounts) {
            LOG.debug(account2);
         }

         assertEquals(3, accounts.size());
         assertEquals("/Bolig", account.getFullPath());
         assertEquals(3, actualCountLevels);
      } catch (Exception e) {
         LOG.error(e.getMessage(), e);
         fail(e.getMessage());
      }

   }

}
