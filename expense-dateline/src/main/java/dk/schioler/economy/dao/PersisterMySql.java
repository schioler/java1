package dk.schioler.economy.dao;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import dk.schioler.economy.Line;

@Component("persister")
public class PersisterMySql implements Persister {
   private static final Logger LOG = Logger.getLogger(PersisterMySql.class);

   @Autowired
   private JdbcTemplate jdbcTemplate;
   static final String SQL_FIELD_AND_VALUES = "(account_id,EXP_OWNER ,EXP_ORIGIN,EXP_DATE, EXP_TEXT , EXP_AMOUNT) " + "values "
         + "(?,?,?,?,?,?)";
   static final String SQL_LINE_UNMATCHED_INSERT = "insert into LINE_UNMATCHED " + SQL_FIELD_AND_VALUES;
   static final String SQL_LINE_INSERT = "insert into LINE " + SQL_FIELD_AND_VALUES;

   static final String SQL_LINE_UNMATCHED_CLEAR =  "delete from LINE_UNMATCHED";
   
   public void persistLine(Line line) {
      LOG.debug("persist: line=" + line);
      if (line != null) {
         jdbcTemplate.update(SQL_LINE_INSERT, line.getAccountId(), line.getOwner(), line.getOrigin(), line.getDate(),
               line.getText(), line.getAmount());
      }
   }

   public void clearUnmatchedLines() {
      jdbcTemplate.update(SQL_LINE_UNMATCHED_CLEAR);
   }

   public List<Line> getLines(Date start, Date end) {
      // TODO Auto-generated method stub
      return null;
   }

   public void persistUnMatchedLine(Line line) {
      LOG.debug("persist: line-unmatched=" + line);
      if (line != null) {
         jdbcTemplate.update(SQL_LINE_UNMATCHED_INSERT, line.getAccountId(), line.getOwner(), line.getOrigin(), line.getDate(),
               line.getText(), line.getAmount());
      }

   }

}
