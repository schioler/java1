package dk.schioler.economy;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.jfree.util.Log;
import org.junit.Test;

import dk.schioler.economy.model.Account;
import dk.schioler.economy.model.Line;
import dk.schioler.economy.model.Match;
import dk.schioler.economy.model.User;
import dk.schioler.economy.visitor.VisitorLogTree;

public class AddLineValuesTest {
   private static final Logger LOG = Logger.getLogger(AddLineValuesTest.class);

   User user = new User(new Long(1), "test", null);
   AccountPersisterTestIFactorympl factorympl = new AccountPersisterTestIFactorympl(user);
   /*
    *
   2013-09-18 23:58:16,706 DEBUG dk.schioler.economy.visitor.VisitorLogTree: - Account [id=1, parentId=-1, userId=1, name=Bolig, path=, level=0, type=TYPE_REGULAR, expensesTotal=0.00, expensesRegular=0.00, expensesNonRegular=0.00, expensesExtra=0.00]
   2013-09-18 23:58:16,706 DEBUG dk.schioler.economy.visitor.VisitorLogTree: -     Account [id=2, parentId=1, userId=1, name=Finans, path=Bolig, level=1, type=TYPE_REGULAR, expensesTotal=0.00, expensesRegular=0.00, expensesNonRegular=0.00, expensesExtra=0.00]
   2013-09-18 23:58:16,706 DEBUG dk.schioler.economy.visitor.VisitorLogTree: -         Account [id=3, parentId=2, userId=1, name=Lån, path=Bolig/Finans, level=2, type=TYPE_REGULAR, expensesTotal=0.00, expensesRegular=0.00, expensesNonRegular=0.00, expensesExtra=0.00]

   2013-09-18 23:58:16,706 DEBUG dk.schioler.economy.visitor.VisitorLogTree: -     Account [id=4, parentId=1, userId=1, name=Tag, path=Bolig, level=1, type=TYPE_REGULAR, expensesTotal=0.00, expensesRegular=0.00, expensesNonRegular=0.00, expensesExtra=0.00]

   2013-09-18 23:58:16,707 DEBUG dk.schioler.economy.visitor.VisitorLogTree: -     Account [id=5, parentId=1, userId=1, name=VVS, path=Bolig, level=1, type=TYPE_NON_REGULAR, expensesTotal=0.00, expensesRegular=0.00, expensesNonRegular=0.00, expensesExtra=0.00]
   2013-09-18 23:58:16,707 DEBUG dk.schioler.economy.visitor.VisitorLogTree: -         Account [id=6, parentId=5, userId=1, name=Extra, path=Bolig/VVS, level=2, type=TYPE_EXTRAORDINAIRE, expensesTotal=0.00, expensesRegular=0.00, expensesNonRegular=0.00, expensesExtra=0.00]
    */
   @Test
   public void test() {
      BigDecimal zero = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_EVEN);
      /*
       * 1: create accountStructure 2: add Lines (well known) 3: verify that
       * expenses has been added the right places and summs up correctly.
       */
      Locale.setDefault(new Locale("da", "DK"));
      AccountTreeRoot root = factorympl.buildAccountTree(user.getId());
      VisitorLogTree v = new VisitorLogTree();
      root.accept(v);

      float line1exp = Float.parseFloat("145.45");
      //                 public Match(Long id, Date ts, Long userId, Long accountId, Long lineId, Long filterId, Long patternId) {
      //      public Line(Long id, Long userId, String origin, Date date, String text, BigDecimal amount, Date timestamp, Match match) {
      Match match1 = new Match(Long.valueOf(1), null, Long.valueOf(1), Long.valueOf(3), Long.valueOf(1), null, null);
      Line line1 = new Line(Long.valueOf(1), Long.valueOf(1), "testdata", new Date(), "Bolig/Finans/Lån", new BigDecimal(line1exp).setScale(2, BigDecimal.ROUND_HALF_EVEN), new Date(), match1);
      root.addLine(line1);
//      root.accept(v);
      // level 0
      Account boligAccount = root.findAccount(Long.valueOf(1));
      assertEquals(new BigDecimal(line1exp).setScale(2, BigDecimal.ROUND_HALF_EVEN), boligAccount.getExpensesTotal());
      assertEquals(new BigDecimal(line1exp).setScale(2, BigDecimal.ROUND_HALF_EVEN), boligAccount.getExpensesRegular());
      assertEquals(zero, boligAccount.getExpensesNonRegular());

      // level 1
      Account finansAccount = root.findAccount(Long.valueOf(2));
      assertEquals(new BigDecimal(line1exp).setScale(2, BigDecimal.ROUND_HALF_EVEN), finansAccount.getExpensesTotal());
      assertEquals(new BigDecimal(line1exp).setScale(2, BigDecimal.ROUND_HALF_EVEN), finansAccount.getExpensesRegular());
      assertEquals(zero, finansAccount.getExpensesNonRegular());
      // level 2
      Account laanAccount = root.findAccount(Long.valueOf(3));
      assertEquals(new BigDecimal(line1exp).setScale(2, BigDecimal.ROUND_HALF_EVEN), laanAccount.getExpensesTotal());
      assertEquals(new BigDecimal(line1exp).setScale(2, BigDecimal.ROUND_HALF_EVEN), laanAccount.getExpensesRegular());
      assertEquals(zero, laanAccount.getExpensesNonRegular());

      // level 1, another branch
      Account tagAccount = root.findAccount(Long.valueOf(4));
      assertEquals(zero, tagAccount.getExpensesTotal());
      assertEquals(zero, tagAccount.getExpensesRegular());
      assertEquals(zero, tagAccount.getExpensesNonRegular());

      LOG.debug("will attempt to add same line....");
      root.addLine(line1);

      root.accept(v);
      // level 0
      boligAccount = root.findAccount(Long.valueOf(1));
      assertEquals(new BigDecimal(line1exp).setScale(2, BigDecimal.ROUND_HALF_EVEN), boligAccount.getExpensesTotal());
      assertEquals(new BigDecimal(line1exp).setScale(2, BigDecimal.ROUND_HALF_EVEN), boligAccount.getExpensesRegular());
      assertEquals(zero, boligAccount.getExpensesNonRegular());

      // level 1
      finansAccount = root.findAccount(Long.valueOf(2));
      assertEquals(new BigDecimal(line1exp).setScale(2, BigDecimal.ROUND_HALF_EVEN), finansAccount.getExpensesTotal());
      assertEquals(new BigDecimal(line1exp).setScale(2, BigDecimal.ROUND_HALF_EVEN), finansAccount.getExpensesRegular());
      assertEquals(zero, finansAccount.getExpensesNonRegular());
      // level 2
      laanAccount = root.findAccount(Long.valueOf(3));
      assertEquals(new BigDecimal(line1exp).setScale(2, BigDecimal.ROUND_HALF_EVEN), laanAccount.getExpensesTotal());
      assertEquals(new BigDecimal(line1exp).setScale(2, BigDecimal.ROUND_HALF_EVEN), laanAccount.getExpensesRegular());
      assertEquals(zero, laanAccount.getExpensesNonRegular());

      // level 1, another branch
      tagAccount = root.findAccount(Long.valueOf(4));
      assertEquals(zero, tagAccount.getExpensesTotal());
      assertEquals(zero, tagAccount.getExpensesRegular());
      assertEquals(zero, tagAccount.getExpensesNonRegular());

      // *********************************************
      //       new line Regular, Bolig/Tag
      float line2exp = Float.parseFloat("85.50");
      BigDecimal line2BD = new BigDecimal(line2exp).setScale(2, BigDecimal.ROUND_HALF_EVEN);
      //    public Match(Long id, Date ts, Long userId, Long accountId, Long lineId, Long filterId, Long patternId) {
      //public Line(Long id, Long userId, String origin, Date date, String text, BigDecimal amount, Date timestamp, Match match) {
      Match match2 = new Match(Long.valueOf(2), null, Long.valueOf(1), Long.valueOf(4), Long.valueOf(2), null, null);
      Line line2 = new Line(Long.valueOf(2), Long.valueOf(1), "Bolig/Tag", new Date(), "testline2", line2BD, new Date(), match2);
      root.addLine(line2);

      BigDecimal sum = new BigDecimal("230.95").setScale(2, BigDecimal.ROUND_HALF_EVEN);

      // level 0
      boligAccount = root.findAccount(Long.valueOf(1));
      assertEquals(sum, boligAccount.getExpensesTotal());
      assertEquals(sum, boligAccount.getExpensesRegular());
      assertEquals(zero, boligAccount.getExpensesNonRegular());

      // level 1
      finansAccount = root.findAccount(Long.valueOf(2));
      assertEquals(new BigDecimal(line1exp).setScale(2, BigDecimal.ROUND_HALF_EVEN), finansAccount.getExpensesTotal());
      assertEquals(new BigDecimal(line1exp).setScale(2, BigDecimal.ROUND_HALF_EVEN), finansAccount.getExpensesRegular());
      assertEquals(zero, finansAccount.getExpensesNonRegular());
      // level 2
      laanAccount = root.findAccount(Long.valueOf(3));
      assertEquals(new BigDecimal(line1exp).setScale(2, BigDecimal.ROUND_HALF_EVEN), laanAccount.getExpensesTotal());
      assertEquals(new BigDecimal(line1exp).setScale(2, BigDecimal.ROUND_HALF_EVEN), laanAccount.getExpensesRegular());
      assertEquals(zero, laanAccount.getExpensesNonRegular());

      // level 1, another branch
      tagAccount = root.findAccount(Long.valueOf(4));
      assertEquals(line2BD, tagAccount.getExpensesTotal());
      assertEquals(line2BD, tagAccount.getExpensesRegular());
      assertEquals(zero, tagAccount.getExpensesNonRegular());

      float vvsexp = Float.parseFloat("1000.95");
      BigDecimal line3BD = new BigDecimal(vvsexp).setScale(2, BigDecimal.ROUND_HALF_EVEN);
      // Bolig/VVS
      //    public Match(Long id, Date ts, Long userId, Long accountId, Long lineId, Long filterId, Long patternId) {
      //public Line(Long id, Long userId, String origin, Date date, String text, BigDecimal amount, Date timestamp, Match match) {
      Match match3 = new Match(Long.valueOf(3), null, Long.valueOf(1), Long.valueOf(5), Long.valueOf(3), null, null);
      Line line3 = new Line(Long.valueOf(3), Long.valueOf(1), "testdata3", new Date(), "Bolig/VVS", line3BD, new Date(), match3);
      LOG.debug(line3);
      Account account4Line = root.addLine(line3);
      LOG.debug(account4Line);
      LOG.debug("");
      VisitorLogTree vl = new VisitorLogTree();
      root.accept(vl);
      BigDecimal vvssum = new BigDecimal("1231.90").setScale(2, BigDecimal.ROUND_HALF_EVEN);

      // level 0
      boligAccount = root.findAccount(Long.valueOf(1));
      assertEquals(vvssum, boligAccount.getExpensesTotal());
      assertEquals(sum, boligAccount.getExpensesRegular());
      assertEquals(line3BD, boligAccount.getExpensesNonRegular());

      // level 1
      finansAccount = root.findAccount(Long.valueOf(2));
      assertEquals(new BigDecimal(line1exp).setScale(2, BigDecimal.ROUND_HALF_EVEN), finansAccount.getExpensesTotal());
      assertEquals(new BigDecimal(line1exp).setScale(2, BigDecimal.ROUND_HALF_EVEN), finansAccount.getExpensesRegular());
      assertEquals(zero, finansAccount.getExpensesNonRegular());
      // level 2
      laanAccount = root.findAccount(Long.valueOf(3));
      assertEquals(new BigDecimal(line1exp).setScale(2, BigDecimal.ROUND_HALF_EVEN), laanAccount.getExpensesTotal());
      assertEquals(new BigDecimal(line1exp).setScale(2, BigDecimal.ROUND_HALF_EVEN), laanAccount.getExpensesRegular());
      assertEquals(zero, laanAccount.getExpensesNonRegular());

      // level 1, Bolig/Tag branch
      tagAccount = root.findAccount(Long.valueOf(4));
      assertEquals(line2BD, tagAccount.getExpensesTotal());
      assertEquals(line2BD, tagAccount.getExpensesRegular());
      assertEquals(zero, tagAccount.getExpensesNonRegular());

      // level 1, Bolig/VVS
      tagAccount = root.findAccount(Long.valueOf(5));
      assertEquals(line3BD, tagAccount.getExpensesTotal());
      assertEquals(line3BD, tagAccount.getExpensesNonRegular());
      assertEquals(zero, tagAccount.getExpensesRegular());

      float extraAmount = Float.parseFloat("100");
      BigDecimal line4BD = new BigDecimal(extraAmount).setScale(2, BigDecimal.ROUND_HALF_EVEN);
      // Bolig/VVS
      //    public Match(Long id, Date ts, Long userId, Long accountId, Long lineId, Long filterId, Long patternId) {
      //public Line(Long id, Long userId, String origin, Date date, String text, BigDecimal amount, Date timestamp, Match match) {
      Match match4 = new Match(Long.valueOf(4), null, Long.valueOf(1), Long.valueOf(6), Long.valueOf(4), null, null);
      Line line4 = new Line(Long.valueOf(4), Long.valueOf(1), "testdata3", new Date(), "Bolig/VVS/Extra", line4BD, new Date(), match4);
      LOG.debug(line4);
      Account account6Line = root.addLine(line4);
      LOG.debug(account6Line);
      LOG.debug("");
      BigDecimal extraSum = new BigDecimal("1331.90").setScale(2, BigDecimal.ROUND_HALF_EVEN);

      // level 0
      boligAccount = root.findAccount(Long.valueOf(1));
      assertEquals(extraSum, boligAccount.getExpensesTotal());
      assertEquals(sum, boligAccount.getExpensesRegular());
      assertEquals(line4BD, boligAccount.getExpensesExtra());

   }

}
