package dk.schioler.economy.out.attic;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import dk.schioler.economy.model.Account;
import dk.schioler.economy.out.AccountRowException;
import dk.schioler.economy.visitor.Visitor;

@Deprecated
public class VisitorBuildOutputRows implements Visitor {
   static Logger LOG = Logger.getLogger(VisitorBuildOutputRows.class);
   private final List<AccountRow> rowList = new ArrayList<AccountRow>();

   private int currentRow = 0;
   private int maxLevel = 0;
   private int periodIdx = 0;
   private final String separator;

   public VisitorBuildOutputRows(String separator) {
      super();
      this.separator = separator;
   }

   /**
    * builds a grid of accoutnRows, each with
    */
   public boolean visit(Account account) {
//      LOG.debug(this);
      if (currentRow == rowList.size()) {
         AccountRow row = new AccountRow(account.getId(), account.getName(), account.getLevel());
         row.addAccountData(periodIdx, account);
         rowList.add(row);
      } else if (currentRow < rowList.size()) {
         AccountRow row = rowList.get(currentRow);
         row.addAccountData(periodIdx, account);
      } else {
         throw new AccountRowException("attempted to add out-of-sync row");
      }
      currentRow++;
      maxLevel = Math.max(maxLevel, account.getLevel());

      return true;
   }

   public boolean init() {
      return false;
   }

   @Override
   public String toString() {
      return "VisitorBuildOutputRows [periodIdx=" + periodIdx + ", currentRow=" + currentRow + ", maxLevel=" + maxLevel + "]";
   }

   public String getOutput() {
      StringBuilder sb = new StringBuilder();

      for (AccountRow row : rowList) {
         sb.append(row.getRowAsString(separator, maxLevel)).append("\n");
      }

      return sb.toString();
   }

   public int getPeriodIdx() {
      return periodIdx;
   }

   public void setPeriodIdx(int periodIdx) {
      this.periodIdx = periodIdx;
      currentRow = 0;
   }

   public List<AccountRow> getRows() {
      return rowList;
   }

   public int getCurrentRow() {
      return currentRow;
   }

   public int getMaxLevel() {
      return maxLevel;
   }
}
