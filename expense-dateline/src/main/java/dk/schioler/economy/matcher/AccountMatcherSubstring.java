package dk.schioler.economy.matcher;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jfree.util.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dk.schioler.economy.ExpenseException;
import dk.schioler.economy.model.Line;
import dk.schioler.economy.model.Match;
import dk.schioler.economy.model.Pattern;
import dk.schioler.economy.model.User;
import dk.schioler.economy.persister.PatternPersister;

@Component("accountMatcher")
public class AccountMatcherSubstring implements AccountMatcher {
   private static final Logger LOG = Logger.getLogger(AccountMatcherSubstring.class);

   @Autowired
   PatternPersister patternPersister;

   List<Pattern> patterns = new ArrayList<Pattern>();
   List<Filter> filters = new ArrayList<Filter>();

   boolean initDone = false;

   public void init(User user) {
      if (user != null) {
         patterns = patternPersister.getPatternsOnUserId(user.getId());
         for (Pattern p : patterns) {
            LOG.debug(p);
         }
         initDone = true;
      } else {
         throw new ExpenseException("user an not be null");
      }
   }

   static final String SKIP = "SKIP";

   public Match matchText(User user, Line line) {
      LOG.debug("match:" + line);
      if (!initDone) {
         throw new ExpenseException("Init must be called before matching....");
      }
      Match m = null;
      FilterResponse filterResponse = null;
      for (Filter filter : filters) {
         filterResponse = filter.applyFilter(line);
         if (filterResponse != null) {
            break;
         }
      }
      if (filterResponse == null) {
         String lTex = line.getText();
         String textUpper = lTex.toUpperCase();
//         LOG.debug("PatternMatch:" + textUpper);
         if (textUpper.indexOf(SKIP) == -1) {
            for (Pattern pattern : patterns) {
               //               LOG.debug("Pattern=" + pattern);
               String pUperString = pattern.getPattern().toUpperCase();
               Log.debug("pUber=" + pUperString);
               if (textUpper.indexOf(pUperString) > -1) {
//                  LOG.debug("**** MATCH on pattern=" + pUperString);
                  m = new Match(null, null, user.getId(), pattern.getAccountId(), line.getId(), null, pattern.getId());
                  break;
               }
            }

         } else {
//            LOG.debug("found " + SKIP + " - skipping line");
         }
      } else {
         if (filterResponse.getResponse().equals(FilterResponse.ResponseType.MATCH)) {
            m = new Match(null, null, user.getId(), filterResponse.getAccountId(), line.getId(), filterResponse.getFilterId(), null);
         }
      }
      return m;
   }
}
