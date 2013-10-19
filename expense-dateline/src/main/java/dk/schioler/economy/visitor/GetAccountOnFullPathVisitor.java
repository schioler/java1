package dk.schioler.economy.visitor;

import org.jfree.util.Log;

import dk.schioler.economy.model.Account;

public class GetAccountOnFullPathVisitor implements Visitor {

   final String fullPath;
   Account target;

   public GetAccountOnFullPathVisitor(String fullPath) {
      super();
      this.fullPath = Account.PATH_SEPARATOR.equals(fullPath) ? "" : fullPath;
   }

   @Override
   public boolean visit(Account element) {
      Log.debug(element.getFullPath());
      if (fullPath.equals(element.getFullPath())) {
         target = element;
         return false;
      }
      return true;
   }

   @Override
   public boolean init() {
      // TODO Auto-generated method stub
      return false;
   }

   public Account getTarget() {
      return target;
   }

}
