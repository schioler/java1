package dk.schioler.economy.command;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dk.schioler.economy.AccountTreeRoot;
import dk.schioler.economy.ExpenseException;
import dk.schioler.economy.file.PatternFile;
import dk.schioler.economy.model.Account;
import dk.schioler.economy.model.Pattern;
import dk.schioler.economy.model.User;
import dk.schioler.economy.persister.AccountPersister;
import dk.schioler.economy.persister.PatternPersister;
import dk.schioler.economy.persister.UserPersister;
import dk.schioler.economy.visitor.VisitorGetLeafAccounts;
import dk.schioler.economy.visitor.VisitorLogTree;

@Component("doPatternDump")
public class DoPatternDump {
   private static final Logger LOG = Logger.getLogger(DoPatternDump.class);

   @Autowired
   PatternFile patternFile;

   @Autowired
   UserPersister userPersister;

   @Autowired
   AccountPersister accountPersister;

   @Autowired
   PatternPersister patternPersister;

   public void execute(String patternFileName, String username) {

      try {
         User user = userPersister.getUser(username);
         if (user == null) {
            throw new ExpenseException("no valid user=" + username);
         }
         LOG.debug("user=" + user);

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
         for (Account a : leafAccounts) {
            LOG.debug(a.getFullPath() + ":" + a.getPatterns());
         }

         File f = new File(patternFileName);
         if (f.exists() && f.isFile()) {
            long lastModified = f.lastModified();
            Date fileDate = new Date(lastModified);
            Date runDate = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmmss");
            String lastModifiedAsString = sdf.format(fileDate);
            String runDateAsString = sdf.format(runDate);

            String filenameBase = patternFileName.substring(0, patternFileName.lastIndexOf("."));
            String suffix = patternFileName.substring(patternFileName.lastIndexOf("."));

            String newFilename = filenameBase + "." + lastModifiedAsString + "." + runDateAsString + suffix;
            LOG.info(patternFileName + " exists, will make backup to :" + newFilename);
            f.renameTo(new File(newFilename));

         }
         f = new File(patternFileName);
         LOG.info("Will dump patterns to file");

         patternFile.dumpPatternsTo(user, leafAccounts, f);

         LOG.debug("*******************");
      } catch (Exception e) {
         e.printStackTrace();
      }
   }
}
