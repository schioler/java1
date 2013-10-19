package dk.schioler.economy;

import dk.schioler.economy.model.Account;
import dk.schioler.economy.visitor.Visitor;

public class VisitorBuildAccountTreeDimensionsWithMaxLevel implements Visitor {
   final int maxLevel;
   int countAccounts = 0;
   


   public VisitorBuildAccountTreeDimensionsWithMaxLevel(int maxLevel) {
      super();
      this.maxLevel = maxLevel;
   }

   public boolean visit(Account element) {
      if (element.getLevel() < maxLevel)
         countAccounts++;
      return true;
   }

   public boolean init() {
      return false;
   }

   public int getMaxLevel() {
      return maxLevel;
   }

   public int getCountAccounts() {
      return countAccounts;
   }

}