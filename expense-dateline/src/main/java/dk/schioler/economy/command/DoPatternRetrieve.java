package dk.schioler.economy.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dk.schioler.economy.AccountTreeRoot;
import dk.schioler.economy.ExpenseException;
import dk.schioler.economy.model.Account;
import dk.schioler.economy.model.Pattern;
import dk.schioler.economy.persister.AccountPersister;
import dk.schioler.economy.persister.PatternPersister;
import dk.schioler.economy.visitor.VisitorGetLeafAccounts;
import dk.schioler.economy.visitor.VisitorLogTree;

@Component("doPatternRetrieve")
public class DoPatternRetrieve extends BaseCommand{
   private static final Logger LOG = Logger.getLogger(DoPatternRetrieve.class);

   @Autowired
   AccountPersister accountPersister;

   @Autowired
   PatternPersister patternPersister;

   public Map<String, List<String>> execute(String username) {
      lookupUser(username);
      try {

         AccountTreeRoot accountTree = accountPersister.buildAccountTree(user.getId());
         VisitorLogTree v = new VisitorLogTree();
         accountTree.accept(v);
         LOG.debug("*******************");

         VisitorGetLeafAccounts leafAccountsVisitor = new VisitorGetLeafAccounts();
         accountTree.accept(leafAccountsVisitor);
         List<Account> leafAccounts = leafAccountsVisitor.getLeafAccounts();
         LOG.debug("Found count leafAccounts=" + leafAccounts.size());
         List<Long> list = new ArrayList<Long>();
         for (Account account : leafAccounts) {
            list.add(account.getId());
         }

         LOG.info("Will lookup patterns on the found leafs");
         List<Pattern> patterns = patternPersister.getPatternsOnAccountIdList(list);
         for (Pattern pattern : patterns) {
            for (Account account : leafAccounts) {
               if (pattern.getAccountId().equals(account.getId())) {
                  account.addPattern(pattern);
                  break;
               }
            }
         }
         Map<String, List<String>> pats = new TreeMap<String, List<String>>();
         for (Account a : leafAccounts) {
            pats.put(a.getFullPath(), a.getPatternsAsStrings());
         }

         LOG.debug("*******************");
         return pats;
      } catch (Exception e) {
         throw new ExpenseException(e.getMessage(), e);
      }
   }
}
