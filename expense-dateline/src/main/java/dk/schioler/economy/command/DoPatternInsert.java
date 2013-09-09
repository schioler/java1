package dk.schioler.economy.command;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jfree.util.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dk.schioler.economy.AccountTreeRoot;
import dk.schioler.economy.ExpenseException;
import dk.schioler.economy.file.PatternFile;
import dk.schioler.economy.model.Account;
import dk.schioler.economy.model.User;
import dk.schioler.economy.persister.AccountPersister;
import dk.schioler.economy.persister.PatternPersister;
import dk.schioler.economy.persister.UserPersister;
import dk.schioler.economy.visitor.VisitorInsertPatterns;
import dk.schioler.economy.visitor.VisitorLogTree;

@Component("doPatternInsert")
public class DoPatternInsert {
   private static final Logger LOG = Logger.getLogger(DoPatternInsert.class);

   @Autowired
   UserPersister userPersister;

   @Autowired
   PatternPersister patternPersister;

   @Autowired
   AccountPersister accountPersister;

   @Autowired
   PatternFile patternFile;

   public void doPatternInsert(String patternFileName) {
      LOG.info("Loading patterns from " + patternFileName);

      Map<String, List<String>> readPatterns = patternFile.readPatterns(new File(patternFileName));
      for (Entry<String, List<String>> entry : readPatterns.entrySet()) {
         String path = entry.getKey();
         List<String> value = entry.getValue();
         LOG.info("Found " + path + "=" + value);
      }

      String username = patternFile.getUsername();
      User user = userPersister.getUser(username);

      LOG.info("Loading the accountplan for user=" + user);
      AccountTreeRoot accountTreeRoot = accountPersister.buildAccountTree(user.getId());
      VisitorLogTree v = new VisitorLogTree();
      accountTreeRoot.accept(v);

      LOG.info("stitching patterns into accounts");
      for (Entry<String, List<String>> entry : readPatterns.entrySet()) {
         String path = entry.getKey();
         List<String> patterns = entry.getValue();
         if (!patterns.isEmpty()) {
            Account account = accountTreeRoot.findAccountOnNamePath(path);
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

      int deleteAllPatternsOnUser = patternPersister.deleteAllPatternsOnUser(user.getId());
      LOG.info("Cleared the way for new patterns for user=" + user + ", deleted " + deleteAllPatternsOnUser + " old patterns.");

      LOG.info("Persisting patterns on accounts");
      VisitorInsertPatterns vip = new VisitorInsertPatterns(patternPersister);
      accountTreeRoot.accept(vip);
   }
}
