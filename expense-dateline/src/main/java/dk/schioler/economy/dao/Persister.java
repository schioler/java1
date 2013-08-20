package dk.schioler.economy.dao;

import java.util.Date;
import java.util.List;

import dk.schioler.economy.Line;

public interface Persister {
   public void persistLine(Line line);

   public void persistUnMatchedLine(Line line);

   public List<Line> getLines(Date start, Date end);
   public void clearUnmatchedLines();
}
