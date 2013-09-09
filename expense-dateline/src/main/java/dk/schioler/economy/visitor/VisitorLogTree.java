package dk.schioler.economy.visitor;

import org.apache.log4j.Logger;

import dk.schioler.economy.model.Account;

public class VisitorLogTree implements Visitor {

   private static final Logger LOG = Logger.getLogger(VisitorLogTree.class);

   public boolean visit(Account element) {
      int level = element.getLevel();
      String prefix = "";
      for (int i = 0; i < level; i++) {
         prefix = prefix + "    ";
      }
      LOG.debug(prefix + element);
      return true;
   }

   public boolean init() {
      // TODO Auto-generated method stub
      return false;
   }

}
