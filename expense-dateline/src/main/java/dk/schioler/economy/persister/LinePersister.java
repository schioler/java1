package dk.schioler.economy.persister;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import dk.schioler.economy.model.Line;
import dk.schioler.economy.model.Match;

public interface LinePersister {
   public Line persistLine(Line line);

  public List<Line> getLines(Long userId, Date start, Date end);

   public Line getLineNoMatch(Long userId, String origin, Date date, String text, BigDecimal amount);

   public List<Line> getLinesOn(Long userId, Long accountId);

   public List<Line> getLinesUnMatched(Long userId);


   public int deleteAllLinesOnUser(Long userId);

   // ******************

   public Match persistMatch(Match match);

   public Match getMatch(Long accountId, Long lineId);

   public int deleteMatchsOnAccount(Long accountId);

   public int deleteAllMatchsOnUser(Long userId);
}
