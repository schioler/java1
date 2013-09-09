package dk.schioler.economy.matcher;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dk.schioler.economy.ExpenseException;
import dk.schioler.economy.model.Pattern;
import dk.schioler.economy.model.User;
import dk.schioler.economy.persister.PatternPersister;

@Component("accountMatcher")
public class AccountMatcherSubstring implements AccountMatcher {
   private static final Logger LOG = Logger.getLogger(AccountMatcherSubstring.class);

   @Autowired
   PatternPersister patternPersister;

   List<Pattern> patterns = new ArrayList<Pattern>();

   boolean initDone = false;

   public void init(User user) {
      patterns = patternPersister.getPatternsOnUserId(user.getId());
      for (Pattern p : patterns) {
         LOG.debug(p);
      }
      initDone = true;
   }

   public long matchText(String text) {
      if (!initDone) {
         throw new ExpenseException("Init must be clled before matching....");
      }
      long id = -1;
      for (Pattern pattern : patterns) {
//         LOG.trace("matching. text=" + text + ", with " + pattern.getPattern());
         if (text.toUpperCase().indexOf(pattern.getPattern().toUpperCase()) > -1) {
            id = pattern.getAccountId();
            break;
         }
      }
      LOG.debug("returning: "+id + " on " + text);
      return id;
   }
}
