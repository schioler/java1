package dk.schioler.economy.visitor;

import org.apache.log4j.Logger;

import dk.schioler.economy.model.Account;
import dk.schioler.economy.model.User;

public class VisitorUpdateAccountUserId implements Visitor {
   private static final Logger LOG = Logger.getLogger(VisitorUpdateAccountUserId.class);

   private User user;

   public VisitorUpdateAccountUserId(User user) {
      super();
      this.user = user;
   }

   public boolean visit(Account element) {
      LOG.debug("Visit to:" + element);
      Account account = element ;
      account.setUserId(user.getId());
      return true;
   }

   public boolean init() {
      return true;
   }

}
