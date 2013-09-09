package dk.schioler.economy.visitor;

import dk.schioler.economy.model.Account;

public interface Visitor {
   public boolean visit(Account element);
   public boolean init();
}
