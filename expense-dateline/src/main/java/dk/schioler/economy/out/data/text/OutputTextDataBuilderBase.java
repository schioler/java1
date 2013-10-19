package dk.schioler.economy.out.data.text;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.PeriodType;

import dk.schioler.economy.AccountTreeRoot;
import dk.schioler.economy.model.Account;
import dk.schioler.economy.out.data.AccountHarvester;
import dk.schioler.economy.out.data.AccountHarvesterVisitor;
import dk.schioler.economy.out.data.Period;
import dk.schioler.economy.out.data.TimeLine;
import dk.schioler.economy.out.data.VisitorGetAccountRowsInLevel;
import dk.schioler.economy.out.data.VisitorIsRelevantTypeInPath;
import dk.schioler.economy.out.data.VisitorIsRelevantTypeInPath.OutputDataType;

public abstract class OutputTextDataBuilderBase implements OutputTextDataBuilder {
   protected static final Logger LOG = Logger.getLogger(OutputTextDataBuilderBase.class);

   protected Account[][] accountGrid;

   protected String[][] accountNames;
   protected Date[] dates;
   protected Object[][] values;

   private int countLevels;
   private int countCols;
   private int countRows;
   private int countPeriods;

   /**
    *
    * Builds structure for output to gridBased output.
    * Categories: Accounts in levels. categoriees.length= countLevels
    * Dates: All periods covered by this outputter
    * Values: Contains all values. Location in grid matches account/categories and the period it is under
    *
    */

   public void buildOutData(TimeLine timeline, String accountPath, int countLevelsDesired) {
      List<Period> periods = timeline.getPeriods();

      countLevelsDesired = accountPath.equals(Account.ROOT_NAME) ? countLevelsDesired + 1 : countLevelsDesired;
      VisitorGetAccountRowsInLevel visitor = new VisitorGetAccountRowsInLevel( countLevelsDesired, accountPath, new VisitorTextIsRelevantTypeInPath(VisitorIsRelevantTypeInPath.OutputDataType.ALL));
      for (Period period : periods) {
         AccountTreeRoot accountTreeRoot = period.getAccountTreeRoot();
         accountTreeRoot.accept(visitor);
         break;
      }

      List<Account> relevantAccounts = visitor.getAccounts();

      //      for (Account account : relevantAccounts) {
      //         LOG.debug(account);
      //      }

      this.countRows = relevantAccounts.size();

      if (this.countRows > 0) {
         // Ok we can build the Grid - relevant accounts were found.
         // Dimensions:
         // countCols= countPeriods * countLevels
         // countRows= countAccounts in accountList
         this.countPeriods = periods.size();
         Account root = visitor.getAccount();

         int countLevelsFound = visitor.getActualCountLevels();
         LOG.debug("root=" + root.getFullPath() + ", maxLevel=" + countLevelsFound);

         this.countLevels = Math.min(countLevelsFound, countLevelsDesired);
         this.countCols = countPeriods * this.countLevels;

         LOG.debug("BuildOutData: Type=" + this.getClass().getSimpleName() + ", Account:" + accountPath + ", actual levels=" + this.countLevels + ", countPeriods=" + countPeriods + ", countCols="
               + countCols + ", rows=" + countRows);

         Date actualMinDate = timeline.getActualMinDate();
         Date actualMaxDate = timeline.getActualMaxDate();
         DateTime amin = new DateTime(actualMinDate.getTime());
         DateTime amax = new DateTime(actualMaxDate.getTime());
         org.joda.time.Period jodaP = new org.joda.time.Period(amin, amax, PeriodType.months().withDaysRemoved());
         int months = jodaP.getMonths() + 1;
         LOG.debug("******* Months="+months+", start="+actualMinDate+", end="+actualMaxDate);
         AccountHarvesterVisitor harvesterVisitor = new AccountHarvesterVisitor(relevantAccounts, months);
         for (Period p : periods) {
            AccountTreeRoot accountTreeRoot = p.getAccountTreeRoot();
            accountTreeRoot.accept(harvesterVisitor);
         }
         Map<Long, AccountHarvester> accountHarvesterMap = harvesterVisitor.getAccountHarvesterMap();
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

            for (int i = 0; i < relevantAccounts.size(); i++) {
               Account account = relevantAccounts.get(i);
               Account accountInPeriod = accountTreeRoot.findAccount(account.getId());
               //               LOG.debug("relevantAccount=" + account.getFullPath() + ", root=" + root);
               levelIdx = account.getLevel() - root.getLevel();

               int cIdx = (periodIdx * countLevels) + levelIdx;
               //               LOG.debug("found Account: period-idx=" + periodIdx + ", colIdx=" + cIdx + ", rowi=" + i + ", acc=" + accountInPeriod);
               accountGrid[cIdx][i] = accountInPeriod;
            }
         }

         // ********************* Account names

         for (int row = 0; row < countRows; row++) {
            for (int col = 0; col < countLevels; col++) {
               Account a = accountGrid[col][row];
               if (a != null) {
                  //                  LOG.debug(col + ", " + row + ", found name=" + a);
                  accountNames[col][row] = a.getName();
               }
            }
         }

         //******************* Fill values in return table
         int colsForSums = countLevels * 8;// each type+each typeAvg
         int colsForPeriods = countPeriods * countLevels * 4; // tot,reg,non-reg,extra
         int valuesCols = Math.max(colsForSums + colsForPeriods, 8);
         values = new BigDecimal[valuesCols][countRows];

         for (int gridRow = 0; gridRow < accountGrid[0].length; gridRow++) {
            for (int gridCol = 0; gridCol < accountNames.length; gridCol++) {
               Account loopAccount = accountGrid[gridCol][gridRow];
               if (loopAccount != null) {
                  //                  LOG.debug("buildin' Sums, looking at account=" + loopAccount);
                  AccountHarvester accountHarvester = accountHarvesterMap.get(loopAccount.getId());

                  values[gridCol + 0 * countLevels][gridRow] = accountHarvester.getSummedExpensesTotal();
                  values[gridCol + 1 * countLevels][gridRow] = accountHarvester.getAvgExpensesTotal();
                  values[gridCol + 2 * countLevels][gridRow] = accountHarvester.getSummedExpensesRegular();
                  values[gridCol + 3 * countLevels][gridRow] = accountHarvester.getAvgExpensesRegular();
                  values[gridCol + 4 * countLevels][gridRow] = accountHarvester.getSummedExpensesNonRegular();
                  values[gridCol + 5 * countLevels][gridRow] = accountHarvester.getAvgExpensesNonRegular();
                  values[gridCol + 6 * countLevels][gridRow] = accountHarvester.getSummedExpensesForExtra();
                  values[gridCol + 7 * countLevels][gridRow] = accountHarvester.getAvgExpensesForExtra();

               }
            }
         }
         printValues();

         for (int gridRow = 0; gridRow < accountGrid[0].length; gridRow++) {
            for (int gridCol = 0; gridCol < accountGrid.length; gridCol++) {
               Account loopAccount = accountGrid[gridCol][gridRow];
               if (loopAccount != null) {
                  int valueColOffSet = gridCol * 4 + colsForSums;
                  //                  LOG.debug("gridCol=" + gridCol + ", colsForSums=" + colsForSums + ", colOffset=" + valueColOffSet + ", row=" + gridRow);
                  values[valueColOffSet + 0][gridRow] = loopAccount.getExpensesTotal();
                  values[valueColOffSet + 1][gridRow] = loopAccount.getExpensesRegular();
                  values[valueColOffSet + 2][gridRow] = loopAccount.getExpensesNonRegular();
                  values[valueColOffSet + 3][gridRow] = loopAccount.getExpensesExtra();
               }
            }
         }
         printValues();
      } else {
         this.accountNames = new String[0][0];
         this.dates = new Date[0];
         values = new BigDecimal[0][0];

      }

   }

   private void printValues() {
      System.out.println("*************");
      for (int i = 0; i < values[0].length; i++) {
         String rowNameString = StringUtils.leftPad("Row " + i + " :", 10);
         StringBuilder sBuilder = new StringBuilder(rowNameString);
         for (int j = 0; j < values.length; j++) {

            String leftPad;
            if (values[j][i] == null) {
               leftPad = StringUtils.leftPad("-", 10);
            } else {
               leftPad = StringUtils.leftPad(values[j][i].toString(), 10);
            }
            sBuilder.append(leftPad).append(";");
         }
         System.out.println(sBuilder.toString());
      }
      System.out.println("*************");
   }

   public abstract OutputDataType getTargetType();

   //   public abstract void instantiateValueTable();
   //
   //   public abstract Object getValue(Account a);

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
