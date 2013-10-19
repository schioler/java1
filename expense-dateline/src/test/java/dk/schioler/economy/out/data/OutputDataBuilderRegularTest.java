package dk.schioler.economy.out.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;

import dk.schioler.economy.AccountPersisterTestIFactorympl;
import dk.schioler.economy.AccountTreeRoot;
import dk.schioler.economy.model.Account;
import dk.schioler.economy.model.Line;
import dk.schioler.economy.model.Match;
import dk.schioler.economy.model.User;
import dk.schioler.economy.out.data.chart.OutputChartDataBuilder;
import dk.schioler.economy.out.data.chart.OutputChartDataBuilderAccount;
import dk.schioler.economy.out.data.chart.OutputChartDataBuilderBigDecimalExtraordinaire;
import dk.schioler.economy.out.data.chart.OutputChartDataBuilderBigDecimalNonRegular;
import dk.schioler.economy.out.data.chart.OutputChartDataBuilderBigDecimalRegular;
import dk.schioler.economy.out.data.chart.OutputChartDataBuilderBigDecimalTotal;
import dk.schioler.economy.visitor.VisitorLogTree;

public class OutputDataBuilderRegularTest {
   private static final Logger LOG = Logger.getLogger(OutputDataBuilderRegularTest.class);
   SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");

   @Test
   public void test() {
      //      OutputDataBuilderRegular outputDataBuilderRegular = new OutputDataBuilderRegular();
      try {
         Long userId = Long.valueOf(1);
         User user = new User(userId, "testUser", new Date());
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
         match = new Match(null, null, userId, new Long(3), new Long(1), null, null);
         Line line1 = new Line(new Long(1), userId, "test1", sdFormat.parse("2013-09-20"), "Lån1", new BigDecimal(-4000), null, match);
         match = new Match(null, null, userId, new Long(3), new Long(2), null, null);
         Line line2 = new Line(new Long(2), userId, "test2", sdFormat.parse("2013-09-21"), "Lån2", new BigDecimal(-400), null, match);

         // regular, Bolig/tag
         match = new Match(null, null, userId, new Long(4), new Long(3), null, null);
         Line line3 = new Line(new Long(3), userId, "test3", sdFormat.parse("2013-09-23"), "Tag", new BigDecimal(-3000), null, match);
         match = new Match(null, null, userId, new Long(4), new Long(4), null, null);
         Line line4 = new Line(new Long(4), userId, "test4", sdFormat.parse("2013-11-20"), "Tag2", new BigDecimal(-300), null, match);

         // extra, Bolig/VVS/Extra
         match = new Match(null, null, userId, new Long(6), new Long(5), null, null);
         Line line5 = new Line(new Long(5), userId, "test", sdFormat.parse("2013-09-20"), "VVSExt1", new BigDecimal(-2000), null, match);
         match = new Match(null, null, userId, new Long(6), new Long(6), null, null);
         Line line6 = new Line(new Long(6), userId, "test", sdFormat.parse("2013-10-20"), "VVSExt2", new BigDecimal(-200), null, match);
         match = new Match(null, null, userId, new Long(6), new Long(7), null, null);
         Line line7 = new Line(new Long(7), userId, "test", sdFormat.parse("2013-11-20"), "VVSExt3", new BigDecimal(-20), null, match);

         // regular, Bolig/Finans/Lån
         match = new Match(null, null, userId, new Long(3), new Long(8), null, null);
         Line line8 = new Line(new Long(8), userId, "test", sdFormat.parse("2013-09-22"), "Lån3", new BigDecimal(-40), null, match);
         // nonregular, Bolig/VVS/Check
         match = new Match(null, null, userId, new Long(7), new Long(9), null, null);
         Line line9 = new Line(new Long(9), userId, "test", sdFormat.parse("2013-10-12"), "VVSBesøg", new BigDecimal(-500), null, match);

         //         // regular, Bolig/Finans/Lån
         //         Line line1 = new Line(new Long(1), new Long(3), userId, "test1", sdFormat.parse("2013-09-20"), "Lån1", new BigDecimal(-4000), null);
         //         Line line2 = new Line(new Long(2), new Long(3), userId, "test2", sdFormat.parse("2013-09-21"), "Lån2", new BigDecimal(-400), null);
         //
         //         // regular, Bolig/tag
         //         Line line3 = new Line(new Long(3), new Long(4), userId, "test3", sdFormat.parse("2013-09-23"), "Tag", new BigDecimal(-3000), null);
         //         Line line4 = new Line(new Long(4), new Long(4), userId, "test4", sdFormat.parse("2013-11-20"), "Tag2", new BigDecimal(-300), null);
         //
         //         // extra, Bolig/VVS/Extra
         //         Line line5 = new Line(new Long(5), new Long(6), userId, "test", sdFormat.parse("2013-09-20"), "VVSExt1", new BigDecimal(-2000), null);
         //         Line line6 = new Line(new Long(6), new Long(6), userId, "test", sdFormat.parse("2013-10-20"), "VVSExt2", new BigDecimal(-200), null);
         //         Line line7 = new Line(new Long(7), new Long(6), userId, "test", sdFormat.parse("2013-11-20"), "VVSExt3", new BigDecimal(-20), null);
         //
         //         // regular, Bolig/Finans/Lån
         //         Line line8 = new Line(new Long(8), new Long(3), userId, "test", sdFormat.parse("2013-09-22"), "Lån3", new BigDecimal(-40), null);
         //         // nonregular, Bolig/VVS/Check
         //         Line line9 = new Line(new Long(9), new Long(7), userId, "test", sdFormat.parse("2013-10-12"), "VVSBesøg", new BigDecimal(-500), null);

         // Bolig:
         // 09: total=4000+400+3000+40+2000, reg=4000+400+3000+40, nonreg=0,extra=2000
         BigDecimal per1Total = new BigDecimal("-9440.00");
         BigDecimal per1Reg = new BigDecimal("-7440.00");
         BigDecimal per1NonReg = new BigDecimal("0.00");
         BigDecimal per1Extra = new BigDecimal("-2000.00");
         // 10 total=200+500, reg=0, nonreg=500,extra=200
         BigDecimal per2Total = new BigDecimal("-700.00");
         BigDecimal per2Reg = new BigDecimal("0.00");
         BigDecimal per2NonReg = new BigDecimal("-500.00");
         BigDecimal per2Extra = new BigDecimal("-200.00");
         // 11: total=300,29 reg=300, nonreg=0,extra=20
         BigDecimal per3Total = new BigDecimal("-320.00");
         BigDecimal per3Reg = new BigDecimal("-300.00");
         BigDecimal per3NonReg = new BigDecimal("0.00");
         BigDecimal per3Extra = new BigDecimal("-20.00");

         // VisitorLogTree treeLogger = new VisitorLogTree(true);
         // treeRoot.accept(treeLogger);

         TimeLineImpl timeLine = new TimeLineImpl();
         AccountPersisterTestIFactorympl accPersister = new AccountPersisterTestIFactorympl(user);
         AccountTreeRoot buildAccountTree = accPersister.buildAccountTree(user.getId());
         VisitorLogTree visitorLogTree = new VisitorLogTree();
         buildAccountTree.accept(visitorLogTree);
         timeLine.setUser(user);
         timeLine.setAccountPersister(accPersister);
         timeLine.createTimelineForPeriod(line1.getDate(), line7.getDate());
         List<Period> periods = timeLine.getPeriods();
         assertEquals(3, periods.size());
         assertEquals(sdFormat.parse("2013-09-01"), periods.get(0).getStartDate());
         assertEquals(sdFormat.parse("2013-10-01"), periods.get(1).getStartDate());
         assertEquals(sdFormat.parse("2013-11-01"), periods.get(2).getStartDate());

         // Creates 3 periods
         timeLine.addLine(line1);
         timeLine.addLine(line2);
         timeLine.addLine(line3);
         timeLine.addLine(line4);
         timeLine.addLine(line5);
         timeLine.addLine(line6);
         timeLine.addLine(line7);
         timeLine.addLine(line8);
         timeLine.addLine(line9);

         //*************************************************

         OutputChartDataBuilder dataBuilder = null;

         dataBuilder = new OutputChartDataBuilderBigDecimalRegular();
         dataBuilder.buildOutData(timeLine, "/Bolig", 1);

         Date[] dates = dataBuilder.getDates();
         assertEquals(3, dates.length);
         assertEquals(sdFormat.parse("2013-09-01"), dates[0]);
         assertEquals(sdFormat.parse("2013-10-01"), dates[1]);
         assertEquals(sdFormat.parse("2013-11-01"), dates[2]);

         BigDecimal[][] values = (BigDecimal[][]) dataBuilder.getValues();
         assertEquals(3, values.length);
         assertEquals(1, values[0].length);
         assertEquals(1, values[1].length);
         assertEquals(1, values[2].length);

         assertEquals(per1Reg, values[0][0]);
         assertEquals(per2Reg, values[1][0]);
         assertEquals(per3Reg, values[2][0]);

         String[][] categories = dataBuilder.getCategories();
         assertEquals(categories.length, 1);
         assertEquals(categories[0].length, 1);
         assertEquals("/Bolig", categories[0][0]);

         //***************************

         //    Account boligAccountVVS = new Account(Long.valueOf(5), Long.valueOf(1), userId, "VVS", "Bolig", 1 , Type.TYPE_NON_REGULAR, new Date());
         //       Account boligAccountVVSExtra = new Account(Long.valueOf(6), Long.valueOf(5), userId, "Extra", "Bolig/VVS", 2 , Type.TYPE_EXTRAORDINAIRE, new Date());
         //       Account boligAccountVVSCheck = new Account(Long.valueOf(7), Long.valueOf(5), userId, "Check", "Bolig/VVS", 2 , Type.TYPE_NON_REGULAR, new Date());

         // extra, Bolig/VVS/Extra
         //         Line line5 = new Line(new Long(5), new Long(6), userId, "test", sdFormat.parse("2013-09-20"), "VVSExt1", new BigDecimal(-2000), null);
         //         Line line6 = new Line(new Long(6), new Long(6), userId, "test", sdFormat.parse("2013-10-20"), "VVSExt2", new BigDecimal(-200), null);
         //         Line line7 = new Line(new Long(7), new Long(6), userId, "test", sdFormat.parse("2013-11-20"), "VVSExt3", new BigDecimal(-20), null);

         // nonregular, Bolig/VVS/Check
         //         Line line9 = new Line(new Long(9), new Long(7), userId, "test", sdFormat.parse("2013-10-12"), "VVSBesøg", new BigDecimal(-500), null);

         BigDecimal zero = new BigDecimal("0.00");

         dataBuilder = new OutputChartDataBuilderBigDecimalRegular();
         dataBuilder.buildOutData(timeLine, "/Bolig/VVS", 2);

         values = (BigDecimal[][]) dataBuilder.getValues();
         categories = dataBuilder.getCategories();
         dates = dataBuilder.getDates();

         assertEquals(0, categories.length);
         assertEquals(0, values.length);
         assertEquals(0, dates.length);

         //          ******************************
         dataBuilder = new OutputChartDataBuilderBigDecimalNonRegular();
         dataBuilder.buildOutData(timeLine, "/Bolig/VVS", 2);

         values = (BigDecimal[][]) dataBuilder.getValues();
         categories = dataBuilder.getCategories();
         dates = dataBuilder.getDates();

         assertEquals(6, values.length);
         assertEquals(2, values[0].length);
         assertEquals(2, values[1].length);
         assertEquals(2, values[2].length);
         assertEquals(2, values[3].length);
         assertEquals(2, values[4].length);
         assertEquals(2, values[5].length);

         assertEquals(zero, values[0][0]);
         assertEquals(null, values[0][1]);
         assertEquals(null, values[1][0]);
         assertEquals(zero, values[1][1]);

         assertEquals(new BigDecimal("-500.00"), values[2][0]);
         assertEquals(null, values[2][1]);
         assertEquals(null, values[3][0]);
         assertEquals(new BigDecimal("-500.00"), values[3][1]);

         assertEquals(zero, values[4][0]);
         assertEquals(null, values[4][1]);
         assertEquals(null, values[5][0]);
         assertEquals(zero, values[5][1]);

         assertEquals("/Bolig/VVS", categories[0][0]);
         assertEquals("/Bolig/VVS", categories[0][1]);
         assertEquals(null, categories[1][0]);
         assertEquals("Check", categories[1][1]);

         assertEquals(3, dates.length);
         assertEquals(sdFormat.parse("2013-09-01"), dates[0]);
         assertEquals(sdFormat.parse("2013-10-01"), dates[1]);
         assertEquals(sdFormat.parse("2013-11-01"), dates[2]);

         //***************************

         dataBuilder = new OutputChartDataBuilderBigDecimalExtraordinaire();
         dataBuilder.buildOutData(timeLine, "/Bolig/VVS", 2);

         values = (BigDecimal[][]) dataBuilder.getValues();
         categories = dataBuilder.getCategories();
         dates = dataBuilder.getDates();

         assertEquals(6, values.length);
         assertEquals(2, values[0].length);
         assertEquals(2, values[1].length);
         assertEquals(2, values[2].length);
         assertEquals(2, values[3].length);
         assertEquals(2, values[4].length);
         assertEquals(2, values[5].length);

         //         BigDecimal zero = new BigDecimal("0.00");
         assertEquals(new BigDecimal("-2000.00"), values[0][0]);
         assertEquals(null, values[0][1]);
         assertEquals(null, values[1][0]);
         assertEquals(new BigDecimal("-2000.00"), values[1][1]);

         assertEquals(new BigDecimal("-200.00"), values[2][0]);
         assertEquals(null, values[2][1]);
         assertEquals(null, values[3][0]);
         assertEquals(new BigDecimal("-200.00"), values[3][1]);

         assertEquals(new BigDecimal("-20.00"), values[4][0]);
         assertEquals(null, values[4][1]);
         assertEquals(null, values[5][0]);
         assertEquals(new BigDecimal("-20.00"), values[5][1]);

         assertEquals("/Bolig/VVS", categories[0][0]);
         assertEquals("/Bolig/VVS", categories[0][1]);
         assertEquals(null, categories[1][0]);
         assertEquals("Extra", categories[1][1]);

         assertEquals(3, dates.length);
         assertEquals(sdFormat.parse("2013-09-01"), dates[0]);
         assertEquals(sdFormat.parse("2013-10-01"), dates[1]);
         assertEquals(sdFormat.parse("2013-11-01"), dates[2]);

         //***************************

         dataBuilder = new OutputChartDataBuilderBigDecimalTotal();
         dataBuilder.buildOutData(timeLine, "/Bolig/VVS", 2);

         values = (BigDecimal[][]) dataBuilder.getValues();
         categories = dataBuilder.getCategories();
         dates = dataBuilder.getDates();

         assertEquals(6, values.length);
         assertEquals(3, values[0].length);
         assertEquals(3, values[1].length);
         assertEquals(3, values[2].length);
         assertEquals(3, values[3].length);
         assertEquals(3, values[4].length);
         assertEquals(3, values[5].length);

         //         BigDecimal zero = new BigDecimal("0.00");
         assertEquals(new BigDecimal("-2000.00"), values[0][0]);
         assertEquals(null, values[0][1]);
         assertEquals(null, values[0][2]);
         assertEquals(null, values[1][0]);
         assertEquals(new BigDecimal("-2000.00"), values[1][1]);
         assertEquals(zero, values[1][2]);

         assertEquals(new BigDecimal("-700.00"), values[2][0]);
         assertEquals(null, values[2][1]);
         assertEquals(null, values[2][2]);
         assertEquals(null, values[3][0]);
         assertEquals(new BigDecimal("-200.00"), values[3][1]);
         assertEquals(new BigDecimal("-500.00"), values[3][2]);

         assertEquals(new BigDecimal("-20.00"), values[4][0]);
         assertEquals(null, values[4][1]);
         assertEquals(null, values[4][2]);
         assertEquals(null, values[5][0]);
         assertEquals(new BigDecimal("-20.00"), values[5][1]);
         assertEquals(zero, values[5][2]);

         assertEquals("/Bolig/VVS", categories[0][0]);
         assertEquals("/Bolig/VVS", categories[0][1]);
         assertEquals("/Bolig/VVS", categories[0][2]);
         assertEquals(null, categories[1][0]);
         assertEquals("Extra", categories[1][1]);
         assertEquals("Check", categories[1][2]);

         assertEquals(3, dates.length);
         assertEquals(sdFormat.parse("2013-09-01"), dates[0]);
         assertEquals(sdFormat.parse("2013-10-01"), dates[1]);
         assertEquals(sdFormat.parse("2013-11-01"), dates[2]);
         //**************************************************
         dataBuilder = new OutputChartDataBuilderAccount();
         dataBuilder.buildOutData(timeLine, "/Bolig/VVS", 2);

         Account[][] accountValues = (Account[][]) dataBuilder.getValues();
         categories = dataBuilder.getCategories();
         dates = dataBuilder.getDates();

         assertEquals(6, accountValues.length);
         assertEquals(3, accountValues[0].length);
         assertEquals(3, accountValues[1].length);
         assertEquals(3, accountValues[2].length);
         assertEquals(3, accountValues[3].length);
         assertEquals(3, accountValues[4].length);
         assertEquals(3, accountValues[5].length);

         //         BigDecimal zero = new BigDecimal("0.00");
         assertEquals(new BigDecimal("-2000.00"), accountValues[0][0].getExpensesTotal());
         assertEquals(null, accountValues[0][1]);
         assertEquals(null, accountValues[0][2]);
         assertEquals(null, accountValues[1][0]);
         assertEquals(new BigDecimal("-2000.00"), accountValues[1][1].getExpensesTotal());
         assertEquals(zero, accountValues[1][2].getExpensesTotal());

         assertEquals(new BigDecimal("-700.00"), accountValues[2][0].getExpensesTotal());
         assertEquals(null, accountValues[2][1]);
         assertEquals(null, accountValues[2][2]);
         assertEquals(null, accountValues[3][0]);
         assertEquals(new BigDecimal("-200.00"), accountValues[3][1].getExpensesTotal());
         assertEquals(new BigDecimal("-500.00"), accountValues[3][2].getExpensesTotal());

         assertEquals(new BigDecimal("-20.00"), accountValues[4][0].getExpensesTotal());
         assertEquals(null, accountValues[4][1]);
         assertEquals(null, accountValues[4][2]);
         assertEquals(null, accountValues[5][0]);
         assertEquals(new BigDecimal("-20.00"), accountValues[5][1].getExpensesTotal());
         assertEquals(zero, accountValues[5][2].getExpensesTotal());

         assertEquals("/Bolig/VVS", categories[0][0]);
         assertEquals("/Bolig/VVS", categories[0][1]);
         assertEquals("/Bolig/VVS", categories[0][2]);
         assertEquals(null, categories[1][0]);
         assertEquals("Extra", categories[1][1]);
         assertEquals("Check", categories[1][2]);

         assertEquals(3, dates.length);
         assertEquals(sdFormat.parse("2013-09-01"), dates[0]);
         assertEquals(sdFormat.parse("2013-10-01"), dates[1]);
         assertEquals(sdFormat.parse("2013-11-01"), dates[2]);

      } catch (Exception e) {
         LOG.error(e.getMessage(), e);
         fail(e.getMessage());
      }

   }
}
