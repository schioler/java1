package dk.schioler.economy.out.data;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import dk.schioler.economy.model.Account;
import dk.schioler.economy.visitor.Visitor;

public class AccountHarvesterVisitor implements Visitor {
   protected static final Logger LOG = Logger.getLogger(AccountHarvesterVisitor.class);
   private final List<Account> relevantAccounts;
   private final Map<Long, AccountHarvester> accountHarvesterMap = new TreeMap<Long, AccountHarvester>();
   private final int periodCount;

   public AccountHarvesterVisitor(List<Account> relevantAccounts, int periodCount) {
      super();
      this.relevantAccounts = relevantAccounts;
      this.periodCount = periodCount;
//      for (Account account : relevantAccounts) {
//         LOG.debug("relevantAccounts=" + account.getId() + ", " + account.getFullPath());
//      }
   }

   public boolean isRelevant(Account a) {
      boolean found = false;
      for (Account account : relevantAccounts) {
         if (account.getId().equals(a.getId())) {
            found = true;
            break;
         }
      }
      return found;
   }

   @Override
   public boolean visit(Account element) {

      if (isRelevant(element)) {
//         LOG.debug("Relevant element:"+element );
         AccountHarvester accountHarvester = accountHarvesterMap.get(element.getId());
         if (accountHarvester == null) {
            accountHarvester = new AccountHarvester(element, periodCount);
            accountHarvesterMap.put(element.getId(), accountHarvester);
         }
         accountHarvester.addAccountData(element);
      }
      return true;
   }

   @Override
   public boolean init() {
      return false;
   }

   public Map<Long, AccountHarvester> getAccountHarvesterMap() {
      return accountHarvesterMap;
   }

}
