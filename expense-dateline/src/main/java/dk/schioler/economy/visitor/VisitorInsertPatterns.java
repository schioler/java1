package dk.schioler.economy.visitor;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import dk.schioler.economy.model.Account;
import dk.schioler.economy.model.Pattern;
import dk.schioler.economy.persister.PatternPersister;

public class VisitorInsertPatterns implements Visitor {
   private static final Logger LOG = Logger.getLogger(VisitorInsertPatterns.class);

   final PatternPersister patternPersister;

   public VisitorInsertPatterns(PatternPersister patternPersister) {
      super();
      this.patternPersister = patternPersister;
   }

   public boolean visit(Account element) {
      LOG.debug("visit to " + element);
      Account account = element;
      List<Pattern> patternsPersisted = new ArrayList<Pattern>();
      for (Pattern pattern : account.getPatterns()) {
         LOG.debug(" - - will persist pattern:" + pattern);
         Pattern persistedPattern = patternPersister.createPattern(account.getId(), pattern);
         patternsPersisted.add(persistedPattern);
      }
      account.getPatterns().clear();
      account.getPatterns().addAll(patternsPersisted);
      return true;
   }

   public boolean init() {

      return true;
   }

}
