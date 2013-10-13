package dk.schioler.economy.persister.mysql;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import dk.schioler.economy.ExpenseException;
import dk.schioler.economy.model.Line;
import dk.schioler.economy.model.Match;
import dk.schioler.economy.persister.LinePersister;
import dk.schioler.economy.util.Util;

@Component("linePersister")
public class LinePersisterMySql implements LinePersister {
   private static final Logger LOG = Logger.getLogger(LinePersisterMySql.class);

   static final String SQL_FIELD_AND_VALUES = "(account_id,user_id ,EXP_ORIGIN,EXP_DATE, EXP_TEXT , EXP_AMOUNT) " + "values " + "(?,?,?,?,?,?)";

   @Autowired
   private JdbcTemplate jdbcTemplate;

   static final String SQL_LINE_TABLE = "LINE ";
   static final String SQL_MATCH_TABLE = "MATCH_ ";
   static final String SQL_LINE_TABLE_WITH_ALIAS = SQL_LINE_TABLE + " l ";
   static final String SQL_MATCH_TABLE_WITH_ALIAS = SQL_MATCH_TABLE + " m ";

   static final String SQL_LINE_COLS = "l.id, l.user_id,  l.exp_origin, l.exp_date, l.exp_text, l.exp_amount ";
   static final String SQL_MATCH_COLS = "m.id, m.user_id,  m.line_id, m.account_id, m.pattern_id, m.filter_id ";

   static final String SQL_LINE_GET_LINES = "SELECT " + SQL_LINE_COLS + " from LINE where user_id = ? and exp_date >=? and exp_date <=? order by exp_date";

   private final class LineWithMatchRowMapper implements RowMapper<Line> {
      public Line mapRow(ResultSet rs, int rowNum) throws SQLException {
         Match match = mapMatch(rs);
         return mapLine(rs, match);
      }
   }

   private final class LineNoMatchRowMapper implements RowMapper<Line> {
      public Line mapRow(ResultSet rs, int rowNum) throws SQLException {
         return mapLine(rs, null);
      }
   }

   private final class MatchRowMapper implements RowMapper<Match> {
      public Match mapRow(ResultSet rs, int rowNum) throws SQLException {
         return mapMatch(rs);
      }
   }

   private final Line mapLine(ResultSet rs, Match match) throws SQLException {
      Long id = rs.getLong("l.id");
      Long userId = rs.getLong("l.user_id");
      String origin = rs.getString("l.exp_origin");
      Date date = rs.getDate("l.exp_date");
      String text = rs.getString("l.exp_text");
      Float f = rs.getFloat("l.exp_amount");
      BigDecimal amount = Util.createBigDecimal(f);
      Line line = new Line(id, userId, origin, date, text, amount, null, match);
      return line;
   }

   private final Match mapMatch(ResultSet rs) throws SQLException {
      Match match = null;
      Long matchId = rs.getLong("m.id");
      Long userId = rs.getLong("m.user_id");
      Long lineId = rs.getLong("m.line_id");
      Long accountId = rs.getLong("m.account_id");
      Long patternId = rs.getLong("m.pattern_id");
      Long filterId = rs.getLong("m.filter_id");
      match = new Match(matchId, null, userId, accountId, lineId, filterId, patternId);
      return match;
   }

   static final String SQL_LINE_INSERT = "insert into " + SQL_LINE_TABLE + " (user_id ,EXP_ORIGIN,EXP_DATE, EXP_TEXT , EXP_AMOUNT) values (?,?,?,?,?)";

   @Override
   public Line persistLine(Line line) {
      LOG.debug("persist: line=" + line);
      Line returnLine = null;
      if (line != null) {
         int update = jdbcTemplate.update(SQL_LINE_INSERT, line.getUserId(), line.getOrigin(), line.getDate(), line.getText(), line.getAmount().setScale(2, BigDecimal.ROUND_HALF_EVEN));
         if (update == 1) {
            returnLine = getLineNoMatch(line.getUserId(), line.getOrigin(), line.getDate(), line.getText(), line.getAmount());
         }
      }
      return returnLine;
   }

   static final String SQL_LINE_GET_SINGLE_LINE_NO_MATCH = "SELECT " + SQL_LINE_COLS + " from " + SQL_LINE_TABLE_WITH_ALIAS
         + " WHERE  l.user_id = ? "
         + "AND l.exp_date =? "
         + "AND l.exp_origin =? "
         + "AND l.exp_amount=?";

   public Line getLineNoMatch(Long userId, String origin, Date date, String text, BigDecimal amount) {
      List<Line> lines = jdbcTemplate.query(SQL_LINE_GET_SINGLE_LINE_NO_MATCH, new LineNoMatchRowMapper(), userId, date, origin, amount);
      Line line = null;
      if (lines.size() > 0) {
         line = lines.get(0);
      }
      return line;
   }

   static final String SQL_LINES_GET_MATCHED = "SELECT " + SQL_LINE_COLS +","+ SQL_MATCH_COLS + " FROM " + SQL_LINE_TABLE_WITH_ALIAS + "," + SQL_MATCH_TABLE_WITH_ALIAS
         + " WHERE l.id = m.line_id AND l.user_id = ? AND l.exp_date >= ? AND l.exp_date < ? order by l.exp_date";

   public List<Line> getLines(Long userId, Date start, Date end) {
      List<Line> lines = jdbcTemplate.query(SQL_LINES_GET_MATCHED, new LineWithMatchRowMapper(), userId, start, end);
      return lines;
   }

   static final String SQL_LINE_GET_LINE_ON_USER_ACC = "SELECT " + SQL_LINE_COLS+", " + SQL_MATCH_COLS + " from " + SQL_LINE_TABLE_WITH_ALIAS + "," + SQL_MATCH_TABLE_WITH_ALIAS
         + " WHERE l.id = m.line_id AND l.user_id = ? AND m.account_id = ? order by l.exp_date ";

   public List<Line> getLinesOn(Long userId, Long accountId) {
      return jdbcTemplate.query(SQL_LINE_GET_LINE_ON_USER_ACC, new LineWithMatchRowMapper(), userId, accountId);
   }

   static final String SQL_LINE_GET_UNMATCHED = "SELECT " + SQL_LINE_COLS +" FROM " + SQL_LINE_TABLE_WITH_ALIAS + " WHERE l.user_id = ? AND l.id not in (select line_id as id FROM " + SQL_MATCH_TABLE
         + " where user_id = ?) order by l.exp_origin, l.exp_text";

   @Override
   public List<Line> getLinesUnMatched(Long userId) {
      List<Line> lines = jdbcTemplate.query(SQL_LINE_GET_UNMATCHED, new LineNoMatchRowMapper(), userId, userId);
      return lines;
   }

   static final String SQL_MATCH_INSERT = "insert into " + SQL_MATCH_TABLE + " (user_id ,account_id, line_id, pattern_id, filter_id ) values (?,?,?,?,?)";

   @Override
   public Match persistMatch(Match match) {
      Match returnMatch = null;
      if (match != null) {
         int update = jdbcTemplate.update(SQL_MATCH_INSERT, match.getUserId(), match.getAccountId(), match.getLineId(), match.getPatternId(), match.getFilterId());
         if (update == 1) {
            returnMatch = getMatch(match.getAccountId(), match.getLineId());
         }
      }
      return returnMatch;

   }

   static final String SQL_MATCH_GET_SINGLE = "SELECT " + SQL_MATCH_COLS + " from " + SQL_MATCH_TABLE_WITH_ALIAS + " WHERE m.account_id=? AND m.line_id =?";

   @Override
   public Match getMatch(Long accountId, Long lineId) {
      List<Match> matches = jdbcTemplate.query(SQL_MATCH_GET_SINGLE, new MatchRowMapper(), accountId, lineId);
      Match m = null;
      if (matches.size() > 0) {
         m = matches.get(0);
      }
      return m;

   }

   @Override
   public int deleteMatchsOnAccount(Long accountId) {
      throw new ExpenseException("method not implemented");
   }

   static final String MATCH_DELETE_ON_USER = "Delete from " + SQL_MATCH_TABLE + " where user_id =?";

   @Override
   public int deleteAllMatchsOnUser(Long userId) {

      return jdbcTemplate.update(MATCH_DELETE_ON_USER, userId);
   }

   static final String LINE_DELETE_ON_USER = "Delete from " + SQL_LINE_TABLE + " where user_id =?";

   @Override
   public int deleteAllLinesOnUser(Long userId) {

      return jdbcTemplate.update(LINE_DELETE_ON_USER, userId);
   }

}
