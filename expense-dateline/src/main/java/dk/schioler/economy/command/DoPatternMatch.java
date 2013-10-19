package dk.schioler.economy.command;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import dk.schioler.economy.matcher.AccountMatcher;
import dk.schioler.economy.model.Line;
import dk.schioler.economy.model.Match;
import dk.schioler.economy.persister.LinePersister;

@Component("doPatternMatch")
@Scope("prototype")
public class DoPatternMatch extends BaseCommand {

   static Logger LOG = Logger.getLogger(DoPatternMatch.class);

   @Autowired
   private AccountMatcher accountMatcher;

   @Autowired
   private LinePersister linePersister;

   public List<Line> execute(String owner) {
      lookupUser(owner);
      accountMatcher.init(user);

      List<Line> linesUnMatched = linePersister.getLinesUnMatched(user.getId());
      for (Line line : linesUnMatched) {
         Match match = accountMatcher.matchText(user, line);
         if (match != null) {
            linePersister.persistMatch(match);

         }
      }
      System.out.println("***************************");
      List<Line> linesUnMatched2 = linePersister.getLinesUnMatched(user.getId());
      return linesUnMatched2;

   }

}
