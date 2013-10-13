package dk.schioler.economy.out.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import dk.schioler.economy.AccountTreeRoot;
import dk.schioler.economy.model.Line;
import dk.schioler.economy.model.User;
import dk.schioler.economy.persister.AccountPersister;
import dk.schioler.economy.util.Util;

@Component("timeline")
@Scope("prototype")
public class TimeLineImpl implements TimeLine {
   private static final Logger LOG = Logger.getLogger(TimeLineImpl.class);

   @Autowired
   AccountPersister accountPersister;

   List<Period> periods = new ArrayList<Period>();
   private User user;
   private Date startDate;
   private Date endDate;
   private long actualMinDate;
   private long actualMaxDate;

   public User getUser() {
      return user;
   }

   public void setUser(User user) {
      this.user = user;
   }

   public List<Period> getPeriods() {
      return periods;
   }

   public Period getPeriod(Line line) {
      Period retval = null;
      for (Period p : periods) {
         if (p.matches(line)) {
            retval = p;
            break;
         }
      }
      return retval;
   }

   public boolean addLine(Line line) {
      boolean success = false;
      Period p = getPeriod(line);
      success = p.addLine(line);
      if (success) {
         actualMinDate = Math.min(actualMinDate, line.getDate().getTime());
         actualMaxDate = Math.max(actualMaxDate, line.getDate().getTime());
      }
      return success;
   }

   public Date getStartDate() {
      return startDate;
   }

   public Date getEndDate() {
      return endDate;
   }

   public void createTimelineForPeriod(Date startDate, Date endDate) {
      LOG.debug("createTimeline:" + startDate + ", end:" + endDate);
      if (!periods.isEmpty()) {
         periods.clear();
      }

      Date[] startMonth = Util.spanMonth(startDate);
      Date[] endPeriod = Util.spanMonth(endDate);

      this.startDate = startMonth[0];
      this.endDate = endPeriod[1];
      this.actualMaxDate = startDate.getTime();
      this.actualMinDate = endDate.getTime();

      AccountTreeRoot accountTree = null;
      Date curMonth = startMonth[0];
      for (; curMonth.getTime() < this.endDate.getTime();) {
         if (accountTree == null) {
            accountTree = accountPersister.buildAccountTree(user.getId());
         } else {
            accountTree = new AccountTreeRoot(accountTree);
         }
         Period period = new Period(accountTree, curMonth);
         periods.add(period);
         curMonth = DateUtils.addMonths(curMonth, 1);
      }

      LOG.info("Created timeline from " + this.startDate + " to " + this.endDate + " with " + periods.size() + " periods");

   }

   public void setAccountPersister(AccountPersister accountPersister) {
      this.accountPersister = accountPersister;
   }

   public void clear() {
      this.startDate = null;
      this.endDate = null;
      this.actualMaxDate = -1;
      this.actualMinDate = -1;
      this.periods.clear();
      LOG.debug("Timeline has been cleared!");
   }

   public Date getActualMinDate() {
      return new Date(actualMinDate);
   }

   public Date getActualMaxDate() {
      return new Date(actualMaxDate);
   }

}
