package dk.schioler.economy.visitor;

import org.apache.log4j.Logger;

import dk.schioler.economy.model.Account;
import dk.schioler.economy.model.User;
import dk.schioler.economy.persister.AccountPersister;

public class VisitorInsertAccount implements Visitor {
   private static final Logger LOG = Logger.getLogger(VisitorInsertAccount.class);

   AccountPersister accountPersister;
   User user;

   public VisitorInsertAccount(AccountPersister accountPersister, User user) {
      super();
      this.accountPersister = accountPersister;
      this.user = user;
   }

   public boolean visit(Account element) {
      LOG.debug("Visit to:" + element);
      boolean success = true;
      try {
         Account account = element ;
         Account account2 = accountPersister.createAccount(account);
         LOG.debug(account2);
      } catch (Exception e) {
         LOG.error(e.getMessage(), e);
         success = false;
      }

      return success;
   }

   public boolean init() {
      accountPersister.deleteAllAccountsOnUser(user.getId());
      return true;
   }

}
