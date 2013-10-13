package dk.schioler.economy.visitor;

import java.util.ArrayList;
import java.util.List;

import org.jfree.util.Log;

import dk.schioler.economy.model.Account;

public class VisitorGetLeafAccounts implements Visitor {

   List<Account> leafAccounts = new ArrayList<Account>();

   public boolean visit(Account element) {
      Log.debug(element);
      if (element.getChildren().size() == 0) {

         leafAccounts.add(element);
      }
      return true;
   }

   public boolean init() {
      // TODO Auto-generated method stub
      return false;
   }

   public List<Account> getLeafAccounts() {
      return leafAccounts;
   }

   public void setLeafAccounts(List<Account> leafAccounts) {
      this.leafAccounts = leafAccounts;
   }

}
