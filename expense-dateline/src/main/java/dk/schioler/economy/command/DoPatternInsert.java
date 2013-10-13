package dk.schioler.economy.command;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dk.schioler.economy.AccountTreeRoot;
import dk.schioler.economy.ExpenseException;
import dk.schioler.economy.inoutput.ExpensePatternMissingAccountKeyException;
import dk.schioler.economy.inoutput.VerifyAllAccountPresentInPatternListVisitor;
import dk.schioler.economy.model.Account;
import dk.schioler.economy.persister.AccountPersister;
import dk.schioler.economy.persister.LinePersister;
import dk.schioler.economy.persister.PatternPersister;
import dk.schioler.economy.visitor.Visitor;
import dk.schioler.economy.visitor.VisitorInsertPatterns;
import dk.schioler.economy.visitor.VisitorLogTree;

@Component("doPatternInsert")
public class DoPatternInsert extends BaseCommand {
   private static final Logger LOG = Logger.getLogger(DoPatternInsert.class);

   @Autowired
   PatternPersister patternPersister;

   @Autowired
   LinePersister linePersister;

   @Autowired
   AccountPersister accountPersister;

   public void doPatternInsert(Map<String, List<String>> readPatterns, String username) {

      lookupUser(username);

      LOG.info("Loading the accountplan for user=" + user);

      AccountTreeRoot accountTreeRoot = accountPersister.buildAccountTree(user.getId());
      Visitor v = new VisitorLogTree();
      accountTreeRoot.accept(v);

      v = new VerifyAllAccountPresentInPatternListVisitor(readPatterns);
      accountTreeRoot.accept(v);
      List<String> missingAccountsList = ((VerifyAllAccountPresentInPatternListVisitor) v).getMissingAccountsList();

      if (missingAccountsList.size() > 0) {
         LOG.error("found accounts with no entry in this collection");
         for (String string : missingAccountsList) {
            LOG.error("Missing accountKey in patternFile=" + string);
         }
         throw new ExpensePatternMissingAccountKeyException("Patterns file is not in sync with current accounts - some accounts are missing - please add.. Check log for details.");
      }

      LOG.info("stitching patterns into accounts");
      for (Entry<String, List<String>> entry : readPatterns.entrySet()) {
         String path = entry.getKey();
         List<String> patterns = entry.getValue();
         if (!patterns.isEmpty()) {
            Account account = accountTreeRoot.findAccountOnFullPath(path);
            if (account == null) {
               throw new ExpenseException("Found no matching account on path=" + path);
            }
            for (String string : patterns) {
               if (StringUtils.isNotBlank(string)) {
                  account.addPatternString(string);
               }
            }
         }
      }

      linePersister.deleteAllMatchsOnUser(user.getId());
      int deleteAllPatternsOnUser = patternPersister.deleteAllPatternsOnUser(user.getId());
      LOG.info("Cleared the way for new patterns for user=" + user + ", deleted " + deleteAllPatternsOnUser + " old patterns.");

      LOG.info("Persisting patterns on accounts");
      VisitorInsertPatterns vip = new VisitorInsertPatterns(patternPersister);
      accountTreeRoot.accept(vip);
   }
}
