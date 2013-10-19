package dk.schioler.economy.out.data.chart;

import java.util.Date;
import java.util.List;
import java.util.Stack;

import org.apache.log4j.Logger;

import dk.schioler.economy.AccountTreeRoot;
import dk.schioler.economy.model.Account;
import dk.schioler.economy.out.data.Period;
import dk.schioler.economy.out.data.TimeLine;
import dk.schioler.economy.out.data.VisitorGetAccountRowsInLevel;

public abstract class OutputChartDataBuilderBase implements OutputChartDataBuilder {
   protected static final Logger LOG = Logger.getLogger(OutputChartDataBuilderBase.class);

   protected Account[][] accountGrid;

   protected String[][] accountNames;
   protected Date[] dates;
   protected Object[][] values;

   private int countLevels;
   private int countCols;
   private int countRows;
   private int countPeriods;

   public static final String EXPENSE_TOTAL = "total";
   public static final String EXPENSE_REGULAR = "regular";
   public static final String EXPENSE_NON_REGULAR = "non-regular";
   public static final String EXPENSE_EXTRAORDINAIRE = "extraordinaire";
   public static final String EXPENSE_ALL = "ALL";

   /**
    *
    * Builds structure for output to gridBased output.
    * Categories: Accounts in levels. categoriees.length= countLevels
    * Dates: All periods covered by this outputter
    * Values: Contains all values. Location in grid matches account/categories and the period it is under
    *
    */

   public void buildOutData(TimeLine timeline, String accountName, int countLevelsDesired) {
      LOG.debug("BuildOutData: accountName=" + accountName + ", levelsDesired=" + countLevelsDesired);
      List<Period> periods = timeline.getPeriods();



      VisitorGetAccountRowsInLevel visitor = new VisitorGetAccountRowsInLevel( countLevelsDesired, accountName, new VisitorChartIsRelevantTypeInPath(this.getTargetType()));
      for (Period period : periods) {
         AccountTreeRoot accountTreeRoot = period.getAccountTreeRoot();
         accountTreeRoot.accept(visitor);
         break;
      }

      List<Account> accounts = visitor.getAccounts();
      Account root = visitor.getAccount();
      int countLevelsFound = visitor.getActualCountLevels();
      LOG.debug("maxLevel=" + countLevelsFound + ",root=" + root);

      for (Account account : accounts) {
         LOG.debug(account);
      }

      this.countRows = accounts.size();

      if (this.countRows > 0) {
         // Ok we can build the Grid - relevant accounts were found.
         // Dimensions:
         // countCols= countPeriods * countLevels
         // countRows= countAccounts in accountList
         this.countPeriods = periods.size();

         this.countLevels = Math.min(countLevelsFound, countLevelsDesired);
         this.countCols = countPeriods * this.countLevels;

         LOG.debug("BuildOutData: Type=" + this.getClass().getSimpleName() + ", Account:" + accountName + ", levels=" + this.countLevels + ", countPeriods=" + countPeriods + ", countCols="
               + countCols + ", rows=" + countRows);

         // ******************* Initialize tables

         this.dates = new Date[countPeriods];
         this.accountGrid = new Account[countCols][countRows];
         this.accountNames = new String[countLevels][countRows];

         // ******************* Account grid

         for (int periodIdx = 0; periodIdx < periods.size(); periodIdx++) {
            Period period = periods.get(periodIdx);
            dates[periodIdx] = period.getStartDate();
            int levelIdx = 0;
            AccountTreeRoot accountTreeRoot = period.getAccountTreeRoot();

            for (int i = 0; i < accounts.size(); i++) {
               Account account = accounts.get(i);
               Account accountInPeriod = accountTreeRoot.findAccount(account.getId());
               levelIdx = account.getLevel() - root.getLevel();

               int cIdx = (periodIdx * countLevels) + levelIdx;
               LOG.debug("found Account: period-idx=" + periodIdx + ", colIdx=" + cIdx + ", rowi=" + i + ", acc=" + accountInPeriod);
               accountGrid[cIdx][i] = accountInPeriod;
            }
         }

         // ********************* Account names

         Stack<String> stack = new Stack<String>();

         for (int row = 0; row < countRows; row++) {
            for (int col = 0; col < countLevels; col++) {
               Account a = accountGrid[col][row];
               if (a != null) {
                  LOG.debug("Building names: col=" + col + ", row=" + row + ", found account=" + a);

                  if (col == 0) {
                     accountNames[col][row] = a.getFullPath();
                     stack.push(a.getFullPath());
                  } else if (col > 0) {
                     accountNames[col][row] = a.getName();
                     for (int i = col - 1; i >= 0; i--) {
                        String string = stack.get(i);
                        accountNames[i][row] = string;
                     }
                     int delta = stack.size() - col;
                     if (delta >= 0) {
                        for (; delta > 0; delta--) {
                           stack.pop();
                        }
                        stack.push(a.getName());
                     }
                  }

                  LOG.debug(stack);
               }
            }
         }

         //******************* Fill values in return table
         instantiateValueTable();
         for (int i = 0; i < accountGrid.length; i++) {
            for (int j = 0; j < accountGrid[i].length; j++) {
               if (accountGrid[i][j] != null) {
                  values[i][j] = getValue(accountGrid[i][j]);
               }
            }
         }
      } else {
         this.accountNames = new String[0][0];
         this.dates = new Date[0];
         instantiateValueTable();

      }

   }

   public abstract dk.schioler.economy.out.data.VisitorIsRelevantTypeInPath.OutputDataType getTargetType();

   public abstract void instantiateValueTable();

   public abstract Object getValue(Account a);

   public String[][] getCategories() {
      return accountNames;
   }

   public Date[] getDates() {
      return dates;
   }

   public Object[][] getValues() {
      return values;
   }

}
