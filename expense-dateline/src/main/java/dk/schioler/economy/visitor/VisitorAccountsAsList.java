package dk.schioler.economy.visitor;

import java.util.ArrayList;
import java.util.List;

import dk.schioler.economy.model.Account;

public class VisitorAccountsAsList implements Visitor {

   List<Account> asList = new ArrayList<Account>();

   public boolean visit(Account element) {
      if(!Account.ROOT_NAME.equals(element.getName()))
         asList.add(element);

      return true;
   }

   public boolean init() {

      return true;
   }

   public List<Account> getAsList() {
      return asList;
   }

   public void setAsList(List<Account> asList) {
      this.asList = asList;
   }

}
