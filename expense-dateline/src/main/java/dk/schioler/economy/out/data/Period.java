package dk.schioler.economy.out.data;

import java.util.Date;

import org.apache.log4j.Logger;

import dk.schioler.economy.AccountTreeRoot;
import dk.schioler.economy.ExpenseException;
import dk.schioler.economy.model.Account;
import dk.schioler.economy.model.Line;
import dk.schioler.economy.util.Util;
import dk.schioler.economy.visitor.Visitor;
import dk.schioler.economy.visitor.VisitorLogTree;

/**
 *
 * @author lasc Currently always a month
 */
public class Period {
   private static final Logger LOG = Logger.getLogger(Period.class);
   private final Date startDate;
   private final Date endDate;

   private final AccountTreeRoot accountTreeRoot;

   public Period(AccountTreeRoot accountTreeRoot, Date firstDate) {
      super();
      this.accountTreeRoot = accountTreeRoot;
      Date[] spanMonth = Util.spanMonth(firstDate);
      startDate = spanMonth[0];
      endDate =  spanMonth[1];
      LOG.debug("startDate=" + startDate + ", endDate=" + endDate);
   }

   public boolean matches(Line line) {
      boolean retVal = true;
      if (line.getDate().getTime() >= startDate.getTime() && line.getDate().getTime() <= endDate.getTime()) {
//         LOG.debug("date matched");
         retVal = true;
      } else {
         retVal = false;
      }
      return retVal;
   }

   public boolean addLine(Line line) {
//      LOG.trace("addingLine=" + line);
      boolean retVal = true;
      Account a = accountTreeRoot.addLine(line);
      if (a == null) {
//         Visitor v = new VisitorLogTree();
//         accountTreeRoot.accept(v);
         throw new ExpenseException("Line not added to tree . check log");
      }
//      LOG.trace("addLine: return=" + retVal + ",  account=" + a);
      return retVal;
   }

   public Date getStartDate() {
      return startDate;
   }

   public Date getEndDate() {
      return endDate;
   }

   public AccountTreeRoot getAccountTreeRoot() {
      return accountTreeRoot;
   }

   @Override
   public String toString() {
      return "Period [startDate=" + startDate + ", endDate=" + endDate + "]";
   }

}
