package dk.schioler.economy.out.attic;

import dk.schioler.economy.model.Account;
import dk.schioler.economy.out.data.AccountHarvester;

public class AccountCalculationsBuilder {

   public AccountHarvester[][] buildAccountCalculations(Account[][] accountData, int accountLevels, int countPeriods) {
      AccountHarvester[][] accountAmount = new AccountHarvester[accountData.length][accountLevels];
      for (int i = 0; i < accountData.length; i++) {
         for (int j = 0; j < accountData[i].length; j++) {
            Account account = accountData[i][j];
            if (account != null) {
               int colIdx = account.getLevel();

               if (accountAmount[i][colIdx] == null) {
                  accountAmount[i][colIdx] = new AccountHarvester(account , countPeriods);
               }
               accountAmount[i][colIdx].addAccountData(account);
            }
         }
      }
      return accountAmount;
   }
}
