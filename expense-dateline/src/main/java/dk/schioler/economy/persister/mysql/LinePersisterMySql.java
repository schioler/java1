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

import dk.schioler.economy.model.Line;
import dk.schioler.economy.persister.LinePersister;

@Component("linePersister")
public class LinePersisterMySql implements LinePersister {
   private static final Logger LOG = Logger.getLogger(LinePersisterMySql.class);

   @Autowired
   private JdbcTemplate jdbcTemplate;
   static final String SQL_FIELD_AND_VALUES = "(account_id,user_id ,EXP_ORIGIN,EXP_DATE, EXP_TEXT , EXP_AMOUNT) "
         + "values " + "(?,?,?,?,?,?)";

   static final String SQL_LINE_UNMATCHED_INSERT = "insert into LINE_UNMATCHED (user_id ,EXP_ORIGIN,EXP_DATE, EXP_TEXT , EXP_AMOUNT) values (?,?,?,?,?)";

   static final String SQL_LINE_INSERT = "insert into LINE (account_id,user_id ,EXP_ORIGIN,EXP_DATE, EXP_TEXT , EXP_AMOUNT) values (?,?,?,?,?,?)";

   static final String SQL_LINE_UNMATCHED_CLEAR = "delete from LINE_UNMATCHED";


   public void persistLine(Line line) {
      LOG.debug("persist: line=" + line);
      if (line != null) {
         jdbcTemplate.update(SQL_LINE_INSERT, line.getAccountId(), line.getUserId(), line.getOrigin(), line.getDate(),
               line.getText(), line.getAmount());
      }
   }

   public void clearUnmatchedLines() {
      jdbcTemplate.update(SQL_LINE_UNMATCHED_CLEAR);
   }

   public List<Line> getLines(Long userId, Date start, Date end) {
      List<Line> lines = jdbcTemplate.query(SQL_LINE_GET_LINES, new LineRowMapper(), userId, start, end);
      return lines;
   }

   public void persistUnMatchedLine(Line line) {
      LOG.debug("persist: line-unmatched=" + line);
      if (line != null) {
         jdbcTemplate.update(SQL_LINE_UNMATCHED_INSERT, line.getUserId(), line.getOrigin(),
               line.getDate(), line.getText(), line.getAmount());
      }

   }

   static final String SQL_LINE_GET_LINES = "SELECT id, account_id, user_id,  exp_origin, exp_date, exp_text, exp_amount from LINE where user_id = ? and exp_date >=? and exp_date <=? order by exp_date";


   private static final class LineRowMapper implements RowMapper<Line> {
      public Line mapRow(ResultSet rs, int rowNum) throws SQLException {
         Long id = rs.getLong("id");
         Long accountId = rs.getLong("account_id");
         Long userId = rs.getLong("user_id");
         String origin = rs.getString("exp_origin");
         Date date = rs.getDate("exp_date");
         String text = rs.getString("exp_text");
         BigDecimal amount = new BigDecimal(rs.getFloat("exp_amount"));
         Line line = new Line(id, accountId, userId, origin, date, text, amount, null);
         return line;
      }
   }

}
