package dk.schioler.economy.out.data;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.Test;

import dk.schioler.economy.AccountPersisterTestIFactorympl;
import dk.schioler.economy.AccountTreeRoot;
import dk.schioler.economy.model.Account;
import dk.schioler.economy.model.Line;
import dk.schioler.economy.model.Match;
import dk.schioler.economy.model.User;

public class AccountHarvesterVisitorTest {
   private static final Logger LOG = Logger.getLogger(AccountHarvesterVisitorTest.class);
   SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");

   @Test
   public void test() {
      try {
         Long userId = Long.valueOf(1);
         User user = new User(userId, "testUser", new Date());
         AccountPersisterTestIFactorympl accPersister = new AccountPersisterTestIFactorympl(user);

         Account boligAccount = accPersister.getAccount(Long.valueOf(1));
         Account boligAccountFinans = accPersister.getAccount(Long.valueOf(2));
         Account boligAccountFinansLaan = accPersister.getAccount(Long.valueOf(3));

         Account boligAccountTag = accPersister.getAccount(Long.valueOf(4));

//         Account boligAccountVVS = accPersister.getAccount(Long.valueOf(5));
         Account boligAccountVVSExtra = accPersister.getAccount(Long.valueOf(6));
         Account boligAccountVVSCheck = accPersister.getAccount(Long.valueOf(7));

         // Creates tree of 6 accounts
         // AccountTreeRoot treeRoot = TestDataFactory.buildAccounts(userId);
         // Account boligAccount = new Account(Long.valueOf(1), Long.valueOf(-1), userId, "Bolig", "", 0, Type.TYPE_REGULAR, new Date());
         //    Account boligAccountFinans = new Account(Long.valueOf(2), Long.valueOf(1), userId,"Finans", "Bolig", 1, Type.TYPE_REGULAR, new Date());
         //       Account boligAccountFinansLaan = new Account(Long.valueOf(3), Long.valueOf(2), userId, "Lån", "Bolig/Finans", 2, Type.TYPE_REGULAR, new Date());
         //
         //    Account boligAccountTag = new Account(Long.valueOf(4), Long.valueOf(1), userId, "Tag", "Bolig", 1, Type.TYPE_REGULAR, new Date());
         //
         //    Account boligAccountVVS = new Account(Long.valueOf(5), Long.valueOf(1), userId, "VVS", "Bolig", 1 , Type.TYPE_NON_REGULAR, new Date());
         //       Account boligAccountVVSExtra = new Account(Long.valueOf(6), Long.valueOf(5), userId, "Extra", "Bolig/VVS", 2 , Type.TYPE_EXTRAORDINAIRE, new Date());
         //       Account boligAccountVVSCheck = new Account(Long.valueOf(7), Long.valueOf(5), userId, "Check", "Bolig/VVS", 2 , Type.TYPE_NON_REGULAR, new Date());

         // regular, Bolig/Finans/Lån
         Match match = null;
         match = new Match(null, null, userId, boligAccountFinansLaan.getId(), new Long(1), null, null);
         Line line1 = new Line(new Long(1), userId, "test1", sdFormat.parse("2013-09-20"), "Lån1", new BigDecimal(-4000), null, match);
         match = new Match(null, null, userId, boligAccountFinansLaan.getId(), new Long(2), null, null);
         Line line2 = new Line(new Long(2), userId, "test2", sdFormat.parse("2013-09-21"), "Lån2", new BigDecimal(-400), null, match);

         // regular, Bolig/tag
         match = new Match(null, null, userId, boligAccountTag.getId(), new Long(3), null, null);
         Line line3 = new Line(new Long(3), userId, "test3", sdFormat.parse("2013-09-23"), "Tag", new BigDecimal(-3000), null, match);
         match = new Match(null, null, userId, boligAccountTag.getId(), new Long(4), null, null);
         Line line4 = new Line(new Long(4), userId, "test4", sdFormat.parse("2013-11-20"), "Tag2", new BigDecimal(-300), null, match);

         // extra, Bolig/VVS/Extra
         match = new Match(null, null, userId, boligAccountVVSExtra.getId(), new Long(5), null, null);
         Line line5 = new Line(new Long(5), userId, "test", sdFormat.parse("2013-09-20"), "VVSExt1", new BigDecimal(-2000), null, match);
         match = new Match(null, null, userId, boligAccountVVSExtra.getId(), new Long(6), null, null);
         Line line6 = new Line(new Long(6), userId, "test", sdFormat.parse("2013-10-20"), "VVSExt2", new BigDecimal(-200), null, match);
         match = new Match(null, null, userId, boligAccountVVSExtra.getId(), new Long(7), null, null);
         Line line7 = new Line(new Long(7), userId, "test", sdFormat.parse("2013-11-20"), "VVSExt3", new BigDecimal(-20), null, match);

         // regular, Bolig/Finans/Lån
         match = new Match(null, null, userId, boligAccountFinansLaan.getId(), new Long(8), null, null);
         Line line8 = new Line(new Long(8), userId, "test", sdFormat.parse("2013-09-22"), "Lån3", new BigDecimal(-40), null, match);
         // nonregular, Bolig/VVS/Check
         match = new Match(null, null, userId, boligAccountVVSCheck.getId(), new Long(9), null, null);
         Line line9 = new Line(new Long(9), userId, "test", sdFormat.parse("2013-10-12"), "VVSBesøg", new BigDecimal(-500), null, match);

         TimeLineImpl timeLine = new TimeLineImpl();

         timeLine.setUser(user);
         timeLine.setAccountPersister(accPersister);

         // Creates 3 periods
         timeLine.createTimelineForPeriod(line1.getDate(), line7.getDate());
         List<Period> periods = timeLine.getPeriods();
         assertEquals(3, periods.size());
         assertEquals(sdFormat.parse("2013-09-01"), periods.get(0).getStartDate());
         assertEquals(sdFormat.parse("2013-10-01"), periods.get(1).getStartDate());
         assertEquals(sdFormat.parse("2013-11-01"), periods.get(2).getStartDate());

         timeLine.addLine(line1); // bolig/finans/lån,   09, -4000, R
         timeLine.addLine(line2); // bolig/finans/lån,   09, -400, R
         timeLine.addLine(line3); // bolig/tag,          09, -3000, R
         timeLine.addLine(line4); // bolig/tag,          11, -300, R
         timeLine.addLine(line5); // bolig/vvs/extra,    09, -2000, E
         timeLine.addLine(line6); // bolig/vvs/extra,    10, -200, E
         timeLine.addLine(line7); // bolig/vvs/extra,    11, -20, E
         timeLine.addLine(line8); // bolig/finans/lån,   09, -40, R
         timeLine.addLine(line9); // bolig/vvs/check,    10, -500, N

         // ******************* data init done.

         List<Account> relevantAccounts = new ArrayList<Account>();
         relevantAccounts.add(boligAccountFinans);
         relevantAccounts.add(boligAccountFinansLaan);

         AccountHarvesterVisitor visitor = new AccountHarvesterVisitor(relevantAccounts, 3);
         for (Period period : periods) {
            AccountTreeRoot accountTreeRoot = period.getAccountTreeRoot();
            accountTreeRoot.accept(visitor);
         }
         Map<Long, AccountHarvester> accountHarvesterMap = visitor.getAccountHarvesterMap();
         AccountHarvester accountHarvester = accountHarvesterMap.get(boligAccountFinans.getId());
         assertNotNull(accountHarvester);
         assertEquals(new BigDecimal("-4440.00"), accountHarvester.getSummedExpensesTotal());
         assertEquals(new BigDecimal("-4440.00"), accountHarvester.getSummedExpensesRegular());
         assertEquals(new BigDecimal("-0.00"), accountHarvester.getSummedExpensesNonRegular());
         assertEquals(new BigDecimal("-0.00"), accountHarvester.getSummedExpensesForExtra());
         accountHarvester = accountHarvesterMap.get(boligAccountFinansLaan.getId());
         assertNotNull(accountHarvester);
         assertEquals(new BigDecimal("-4440.00"), accountHarvester.getSummedExpensesTotal());
         assertEquals(new BigDecimal("-4440.00"), accountHarvester.getSummedExpensesRegular());
         assertEquals(new BigDecimal("-0.00"), accountHarvester.getSummedExpensesNonRegular());
         assertEquals(new BigDecimal("-0.00"), accountHarvester.getSummedExpensesForExtra());

         relevantAccounts = new ArrayList<Account>();
         relevantAccounts.add(boligAccount);
         visitor = new AccountHarvesterVisitor(relevantAccounts, 3);
         for (Period period : periods) {
            AccountTreeRoot accountTreeRoot = period.getAccountTreeRoot();
            accountTreeRoot.accept(visitor);
         }
         accountHarvesterMap = visitor.getAccountHarvesterMap();

         accountHarvester = accountHarvesterMap.get(boligAccount.getId());
         assertNotNull(accountHarvester);
         assertEquals(new BigDecimal("-10460.00"), accountHarvester.getSummedExpensesTotal());
         assertEquals(new BigDecimal("-7740.00"), accountHarvester.getSummedExpensesRegular());
         assertEquals(new BigDecimal("-500.00"), accountHarvester.getSummedExpensesNonRegular());
         assertEquals(new BigDecimal("-2220.00"), accountHarvester.getSummedExpensesForExtra());

         assertEquals(new BigDecimal("-3486.67"), accountHarvester.getAvgExpensesTotal());
         assertEquals(new BigDecimal("-2580.00"), accountHarvester.getAvgExpensesRegular());
         assertEquals(new BigDecimal("-166.67"), accountHarvester.getAvgExpensesNonRegular());
         assertEquals(new BigDecimal("-740.00"), accountHarvester.getAvgExpensesForExtra());


      } catch (Exception exception) {
         LOG.error(exception.getMessage(), exception);
         fail(exception.getMessage());
      }

   }
}
