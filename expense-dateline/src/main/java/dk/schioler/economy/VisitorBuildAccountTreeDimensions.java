package dk.schioler.economy;

import dk.schioler.economy.AccountTreeRoot.AccountTreeDimensions;
import dk.schioler.economy.model.Account;
import dk.schioler.economy.visitor.Visitor;

public class VisitorBuildAccountTreeDimensions implements Visitor {
   int maxLevel = 0;
   int countAccounts = 0;

   public boolean visit(Account element) {
      maxLevel = Math.max(maxLevel, element.getLevel());
      countAccounts++;
      return true;
   }

   public boolean init() {
      return false;
   }

   public AccountTreeDimensions getDimensions() {
      return new AccountTreeRoot.AccountTreeDimensions(maxLevel, countAccounts);
   }

   public int getMaxLevel() {
      return maxLevel;
   }

   public int getCountAccounts() {
      return countAccounts;
   }

}