package dk.schioler.economy.out.data;

import org.apache.log4j.Logger;

import dk.schioler.economy.ExpenseException;
import dk.schioler.economy.model.Account;
import dk.schioler.economy.visitor.Visitor;

public class VisitorBuildAccountTable implements Visitor {
   static Logger LOG = Logger.getLogger(VisitorBuildAccountTable.class);

   private final Account[][] rowAndCol;

   private int currentRow = 0;
   private int periodIdx = 0;
   private final int rowCount;
   private final int accountLevelCount;
   private final int periodCount;

   public VisitorBuildAccountTable(int rowCount, int accountLevelCount, int periodCount) {
      super();
      this.rowCount = rowCount;
      this.accountLevelCount = accountLevelCount;
      this.periodCount = periodCount;
      rowAndCol = new Account[rowCount][accountLevelCount * periodCount];
      LOG.debug(this);
   }

   @Override
   public String toString() {
      return "VisitorBuildOutputGridRowAndCol [currentRow=" + currentRow + ", periodIdx=" + periodIdx + ", rowCount=" + rowCount + ", accountLevelCount=" + accountLevelCount + ", periodCount="
            + periodCount + "]";
   }

   /**
    * builds a grid of accounts, each with. periodIdx must be set before each
    * call to visit
    */
   public boolean visit(Account account) {
      // level is 0 based, and can be used as column idx here
      int level = account.getLevel();
      if (level < accountLevelCount) {
         int col = level + (periodIdx * accountLevelCount);
//         LOG.debug("visit period=" + periodIdx + ", curRow=" + currentRow + ", col=" + col + ", level=" + level + " account=" + account);

         if (currentRow > rowAndCol.length || col > rowAndCol[currentRow].length) {
            throw new ExpenseException("bad idxfor colAndRow array, currentRow=" + currentRow + ", col=" + col);
         }
         rowAndCol[currentRow][col] = account;
         currentRow++;
      }
      return true;
   }

   public boolean init() {
      return false;
   }

   public int getPeriodIdx() {
      return periodIdx;
   }

   public void setPeriodIdx(int periodIdx) {
      this.periodIdx = periodIdx;
      currentRow = 0;
   }

   public int getCurrentRow() {
      return currentRow;
   }

   public Account[][] getRowAndCol() {
      return rowAndCol;
   }

   public int getRowCount() {
      return rowCount;
   }

   public int getAccountLevvelCount() {
      return accountLevelCount;
   }

   public int getPeriodCount() {
      return periodCount;
   }

   public void setCurrentRow(int currentRow) {
      this.currentRow = currentRow;
   }

}
