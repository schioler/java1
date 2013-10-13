package dk.schioler.economy.out.attic;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import dk.schioler.economy.model.Account;
import dk.schioler.economy.out.AccountRowException;

public class AccountRow {
   private static final Logger LOG = Logger.getLogger(AccountRow.class);
   private final Long accountId;
   private final String accountName;
   private final int level;
   private List<Account> columnList = new ArrayList<Account>();

   private BigDecimal summedExpensesTotal = new BigDecimal(0);
   private BigDecimal summedExpensesRegular = new BigDecimal(0);
   private BigDecimal summedExpensesNonRegular = new BigDecimal(0);
   private BigDecimal summedExpensesExtra = new BigDecimal(0);

   public AccountRow(Long accountId, String accountName, int level) {
      super();
      this.accountId = accountId;
      this.accountName = accountName;
      this.level = level;
   }

   /*
    *
    */
   void addAccountData(int periodIdx, Account account) {
      LOG.debug("account.level="+account.getLevel()  + ", this:" + this.accountName + ", aId=" + accountId + " , level" + level);
      if (level == account.getLevel()) {
         if (accountId.equals(account.getId())) {
            if (columnList.size() == (periodIdx)) {
               LOG.debug("in right period, will add to periods");
               columnList.add(account);
               summedExpensesTotal = summedExpensesTotal.add(account.getExpensesTotal());
               summedExpensesExtra = summedExpensesExtra.add(account.getExpensesExtra());
               summedExpensesRegular = summedExpensesRegular.add(account.getExpensesRegular());
               summedExpensesNonRegular = summedExpensesNonRegular.add(account.getExpensesNonRegular());
               // LOG.debug("summed4Avg=" + summedExpensesForAvg);

            } else {
               throw new AccountRowException("attempted to add accountData, but periodidx did not match next entry");
            }
         } else {
            throw new AccountRowException("attempted to add accountData, but accountId did not match");
         }
      } else {
         throw new AccountRowException("attempted to add accountData, but levels did not match");
      }
   }

   /*
    *
    */
   public String getRowAsString(String separator, int countLevels) {
      StringBuilder sb = new StringBuilder();
      sb.append(placeInLevel(accountName, separator, countLevels));

      for (Account bd : columnList) {
         sb.append(placeInLevel(bd, separator, countLevels));
      }
      // add summed amounts:
      int units = columnList.size();
      BigDecimal divisor = new BigDecimal(units);
      LOG.debug("periods=" + units + ", divisor=" + divisor);
      LOG.debug("Account.name=" + accountName+ ", summed4Avg=" + summedExpensesExtra + ", summedRegular=" + summedExpensesRegular + ", totalSum=" + summedExpensesTotal);
      BigDecimal avg = summedExpensesExtra.divide(divisor,RoundingMode.HALF_EVEN);
      BigDecimal regularAvg = summedExpensesRegular.divide(divisor,RoundingMode.HALF_EVEN);
      BigDecimal nonRegularAvg = summedExpensesNonRegular.divide(divisor,RoundingMode.HALF_EVEN);
      BigDecimal totalPrPeriod = summedExpensesTotal.divide(divisor,RoundingMode.HALF_EVEN);

      LOG.debug("Account.name=" + accountName+ ", summed4AvgAvg=" + avg + ", summedRegularAvg=" + regularAvg + ", totalSumAvg=" + totalPrPeriod);

      NumberFormat instance = NumberFormat.getInstance();
      instance.setMaximumFractionDigits(0);
      instance.setRoundingMode(RoundingMode.HALF_EVEN);


//      sb.append("SummedTotal").append(OUTPUT_SEPARATOR);
//      sb.append("SummedRegular").append(OUTPUT_SEPARATOR);
//      sb.append("SummedNonRegular").append(OUTPUT_SEPARATOR);
//      sb.append("Summed4Avg").append(OUTPUT_SEPARATOR);
//      sb.append("Total pr period").append(OUTPUT_SEPARATOR);
//      sb.append("Regular pr period").append(OUTPUT_SEPARATOR);
//      sb.append("Non regular pr period").append(OUTPUT_SEPARATOR);
//      sb.append("4Avg pr period").append(OUTPUT_SEPARATOR);
      // total, total pr period, used4avg, avg, regulars summed, regular pr
      // period
      sb.append(instance.format(summedExpensesTotal)).append(separator);
      sb.append(instance.format(summedExpensesRegular)).append(separator);
      sb.append(instance.format(summedExpensesNonRegular)).append(separator);
      sb.append(instance.format(summedExpensesExtra)).append(separator);

      sb.append(instance.format(totalPrPeriod)).append(separator);
      sb.append(instance.format(regularAvg)).append(separator);
      sb.append(instance.format(nonRegularAvg)).append(separator);
      sb.append(instance.format(avg)).append(separator);
      return sb.toString();
   }

   private StringBuilder placeInLevel(String o, String separator, int countLevels) {
      StringBuilder sb = new StringBuilder();
      for (int i = 0; i <= countLevels; i++) {
         if (i == level) {
            sb.append(o.toString());
         }
         sb.append(separator);
      }

      return sb;
   }

   private StringBuilder placeInLevel(Account o, String separator, int countLevels) {
      StringBuilder sb = new StringBuilder();
      for (int i = 0; i <= countLevels; i++) {
         if (i == level) {
            NumberFormat instance = NumberFormat.getInstance();
            instance.setMaximumFractionDigits(0);
            instance.setRoundingMode(RoundingMode.HALF_EVEN);
            String bd = instance.format(o.getExpensesTotal());
            sb.append(bd);

         }

         sb.append(separator);
      }

      return sb;
   }

   @Override
   public String toString() {
      return "AccountRow [accountId=" + accountId + ", level=" + level + ", periods=" + columnList + "]";
   }

}
