package dk.schioler.economy.persister;

import java.util.Date;
import java.util.List;

import dk.schioler.economy.model.Account;
import dk.schioler.economy.model.Line;

public interface LinePersister {
   public void persistLine(Line line);

   public void persistUnMatchedLine(Line line);

   public List<Line> getLines(Long userId, Date start, Date end);
   public void clearUnmatchedLines();


}
