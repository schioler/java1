package dk.schioler.economy.out;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;

import dk.schioler.economy.AccountTreeRoot;
import dk.schioler.economy.model.Account;
import dk.schioler.economy.model.Line;

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

      startDate = DateUtils.truncate(firstDate, Calendar.MONTH);
      Date startDateEndOfMonth = DateUtils.addMonths(startDate, 1);
      endDate = DateUtils.addSeconds(startDateEndOfMonth, -1);
      LOG.debug("startDate=" + startDate + ", endDate=" + endDate);
   }

   public boolean matches(Line line) {
      boolean retVal = true;
      if (line.getDate().getTime() >= startDate.getTime() && line.getDate().getTime() <= endDate.getTime()) {
         LOG.debug("date matched");
         retVal = true;
      } else {
         retVal = false;
      }
      return retVal;
   }

   public boolean addLine(Line line) {
      boolean retVal = true;
      if (line.getDate().getTime() >= startDate.getTime() && line.getDate().getTime() <= endDate.getTime()) {
         Account a =accountTreeRoot.addLine(line);
         if (a == null)
            retVal = false;
      } else {
         retVal = false;
      }
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
