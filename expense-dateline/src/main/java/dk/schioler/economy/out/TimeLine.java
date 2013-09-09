package dk.schioler.economy.out;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import dk.schioler.economy.AccountTreeRoot;
import dk.schioler.economy.ExpenseException;
import dk.schioler.economy.model.Line;
import dk.schioler.economy.model.User;
import dk.schioler.economy.persister.AccountPersister;

@Component("timeline")
@Scope("prototype")
public class TimeLine {
   private static final Logger LOG = Logger.getLogger(TimeLine.class);

   @Autowired
   AccountPersister accountPersister;

   List<Period> periods = new ArrayList<Period>();
   private User user;

   public User getUser() {
      return user;
   }

   public void setUser(User user) {
      this.user = user;
   }

   /**
    * If periods are not consecutive, but have holes, this method will establish
    * the remaining period(s).
    */
   public void ensureConsecutivePeriods() {

   }

   Period getPeriod(Line line) {
      Period retval = null;
      for (Period p : periods) {
         LOG.debug("getPeriod, period=" + p);
         if (p.matches(line)) {
            retval = p;
            break;
         }
      }
      if (retval == null) {
         LOG.debug("Will add new period, line= " + line);
         AccountTreeRoot accountTree = accountPersister.buildAccountTree(user.getId());
         retval = new Period(accountTree, line.getDate());
         periods.add(retval);
      }
      return retval;
   }

   public boolean addLine(Line line) {
      boolean success = false;
      Period p = getPeriod(line);
      success = p.addLine(line);
      return success;
   }

   public String getOutput() {
      StringBuilder sb = new StringBuilder();

      VisitorBuildOutputRows visitor = new VisitorBuildOutputRows(OUTPUT_SEPARATOR);
      int periodIdx = 0;
      for (Period p : periods) {
         visitor.setPeriodIdx(periodIdx);
         p.getAccountTreeRoot().accept(visitor);

         periodIdx++;
      }

      sb.append(buildHeader(visitor));
      sb.append(visitor.getOutput());

      return sb.toString();
   }

   public static final String OUTPUT_SEPARATOR = ";";

   public StringBuilder buildHeader(VisitorBuildOutputRows visitor) {
      int maxLevel = visitor.getMaxLevel() + 1;
      StringBuilder sb = new StringBuilder();
      // account columns:
      for (int i = 0; i < maxLevel; i++) {
         sb.append(OUTPUT_SEPARATOR);
      }
      SimpleDateFormat sdf = new SimpleDateFormat("MMMMM yyyy");
      for (Period p : periods) {
         sb.append(sdf.format(p.getStartDate()));
         for (int i = 1; i <= maxLevel; i++) {
            sb.append(OUTPUT_SEPARATOR);
         }
      }

      sb.append("SummedTotal").append(OUTPUT_SEPARATOR);
      sb.append("SummedRegular").append(OUTPUT_SEPARATOR);
      sb.append("SummedNonRegular").append(OUTPUT_SEPARATOR);
      sb.append("Summed4Avg").append(OUTPUT_SEPARATOR);
      sb.append("Total pr period").append(OUTPUT_SEPARATOR);
      sb.append("Regular pr period").append(OUTPUT_SEPARATOR);
      sb.append("Non regular pr period").append(OUTPUT_SEPARATOR);
      sb.append("4Avg pr period").append(OUTPUT_SEPARATOR);
      return sb.append("\n");
   }
}
