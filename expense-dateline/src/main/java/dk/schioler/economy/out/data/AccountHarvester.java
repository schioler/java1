package dk.schioler.economy.out.data;

import java.math.BigDecimal;
import java.math.RoundingMode;

import dk.schioler.economy.ExpenseException;
import dk.schioler.economy.model.Account;

public class AccountHarvester {
//   private static final Logger LOG = Logger.getLogger(AccountHarvester.class);

   private final Account a;
   private final BigDecimal periods;
   private BigDecimal summedExpensesTotal = new BigDecimal(0);
   private BigDecimal summedExpensesRegular = new BigDecimal(0);
   private BigDecimal summedExpensesNonRegular = new BigDecimal(0);
   private BigDecimal summedExpensesForExtra = new BigDecimal(0);

   public AccountHarvester(Account a, int periods) {
      super();
      this.a = a;
      this.periods = new BigDecimal(periods);
   }

   public int getLevel() {
      return a.getLevel();
   }

   /*
    *
    */
   public void addAccountData(Account account) {
      if (account.getId().equals(a.getId())) {
         summedExpensesTotal = summedExpensesTotal.add(account.getExpensesTotal());
         summedExpensesForExtra = summedExpensesForExtra.add(account.getExpensesExtra());
         summedExpensesRegular = summedExpensesRegular.add(account.getExpensesRegular());
         summedExpensesNonRegular = summedExpensesNonRegular.add(account.getExpensesNonRegular());
      } else {
         throw new ExpenseException("Attempted to add accountData to another account. this.a.id= " + a.getId() + ". newAccount.id=" + account.getId());
      }
   }

   public BigDecimal getSummedExpensesTotal() {
      return summedExpensesTotal;
   }

   public BigDecimal getSummedExpensesRegular() {
      return summedExpensesRegular;
   }

   public BigDecimal getSummedExpensesNonRegular() {
      return summedExpensesNonRegular;
   }

   public BigDecimal getSummedExpensesForExtra() {
      return summedExpensesForExtra;
   }

   public BigDecimal getAvgExpensesTotal() {
      return summedExpensesTotal.divide(periods, RoundingMode.HALF_EVEN);
   }

   public BigDecimal getAvgExpensesRegular() {
      return summedExpensesRegular.divide(periods, RoundingMode.HALF_EVEN);
   }

   public BigDecimal getAvgExpensesNonRegular() {
      return summedExpensesNonRegular.divide(periods, RoundingMode.HALF_EVEN);
   }

   public BigDecimal getAvgExpensesForExtra() {
      return summedExpensesForExtra.divide(periods, RoundingMode.HALF_EVEN);
   }

   @Override
   public String toString() {
      return "AccountSummed [summedExpensesTotal=" + summedExpensesTotal + ", summedExpensesRegular=" + summedExpensesRegular + ", summedExpensesNonRegular=" + summedExpensesNonRegular
            + ", summedExpensesForAvg=" + summedExpensesForExtra + "]";
   }

}
