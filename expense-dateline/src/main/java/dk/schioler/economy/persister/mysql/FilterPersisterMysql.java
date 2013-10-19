package dk.schioler.economy.persister.mysql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import dk.schioler.economy.model.Pattern;
import dk.schioler.economy.persister.PatternPersister;

@Component("filterPersister")
public class FilterPersisterMysql implements PatternPersister {

   /*
    * create table FILTER (
    ID int NOT NULL AUTO_INCREMENT,
    TS TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    USER_ID INT NOT NULL,
    ACCOUNT_ID int not null,
    ACCOUNT_PATH varchar(600) not null,
    EXP_ORIGIN varchar(50),
    EXP_DATE timestamp ,
    EXP_TEXT varchar(300),
    EXP_AMOUNT decimal (10,2) ,
    PRIMARY KEY (ID),
    INDEX IDX_ACC (ACCOUNT_ID),
    INDEX IDX_USER(USER_ID),
    FOREIGN KEY (USER_ID)  REFERENCES USERT(ID)  ON DELETE CASCADE,
    FOREIGN KEY (ACCOUNT_ID)  REFERENCES ACCOUNT(ID)  ON DELETE CASCADE
  ) ENGINE=INNODB;
    */
   private static final Logger LOG = Logger.getLogger(FilterPersisterMysql.class);
   @Autowired
   JdbcTemplate jdbcTemplate;

   public Pattern getPattern(Long accountId, String pattern) {
      List<Pattern> accounts = jdbcTemplate.query(GET_MATCH_SQL, new PatternMatchRowMapper(), accountId, pattern);
      return accounts.get(0);
   }

   // public Pattern(Long id, Long accountId, String pattern, String
   // accountPath)
   private static final class PatternMatchRowMapper implements RowMapper<Pattern> {
      public Pattern mapRow(ResultSet rs, int rowNum) throws SQLException {
//         Pattern pm = new Pattern(rs.getLong("id"), rs.getLong("account_id"), rs.getString("pattern"), rs.getString("account_path"));
//         return pm;
         return null;
      }
   }

   static final String ROWS = "select p.id, p.account_id, p.account_path, p.pattern ";

   static final String DELETE_MATCH_ON_USER_SQL = "DELETE FROM PATTERN where account_id in (SELECT * FROM (SELECT a.id as id FROM ACCOUNT a, PATTERN p, USERT u where a.id = p.account_id and a.user_id = u.id and u.id = ? ) as PA)";

   static final String DELETE_MATCH_ON_ACCOUNT_SQL = "DELETE FROM PATTERN where account_id in (SELECT * FROM (SELECT a.id as id FROM ACCOUNT a, PATTERN p where a.id = p.account_id and a.id =? ) as PA)";

   static final String INSERT_MATCH_SQL = "insert into PATTERN (account_id,pattern, account_path) values (?,?,?)";
   static final String GET_MATCH_SQL = ROWS + " from PATTERN p where  p.account_id =? and p.pattern=?";

   static final String GET_MATCH_ON_ACCOUNT_SQL = ROWS + " from PATTERN p where account_id =?";

   static final String GET_MATCH_ON_USER_SQL = ROWS + " from PATTERN p, ACCOUNT a where p.account_id =a.id and a.user_id = ?";

   public Pattern createPattern(Long accountId, Pattern pattern) {
      int update = jdbcTemplate.update(INSERT_MATCH_SQL, accountId, pattern.getPattern(), pattern.getAccountPath());

      Pattern pm = null;
      if (update > 0) {
         pm = getPattern(accountId, pattern.getPattern());
      }

      return pm;
   }

   public List<Pattern> getPatternsOnAccountId(Long accountId) {
      List<Pattern> accounts = jdbcTemplate.query(GET_MATCH_ON_ACCOUNT_SQL, new PatternMatchRowMapper(), accountId);
      return accounts;
   }

   public int deletePatterns(Long accountId) {
      return jdbcTemplate.update(DELETE_MATCH_ON_ACCOUNT_SQL, accountId);
   }

   public int deleteAllPatternsOnUser(Long userId) {
      return jdbcTemplate.update(DELETE_MATCH_ON_USER_SQL, userId);
   }

   public List<Pattern> getPatternsOnUserId(Long userId) {
      List<Pattern> patterns = jdbcTemplate.query(GET_MATCH_ON_USER_SQL, new PatternMatchRowMapper(), userId);
      return patterns;
   }

   public List<Pattern> getPatternsOnAccountIdList(List<Long> accountIdList) {
      int count = accountIdList.size();
      StringBuilder accountPlaces = new StringBuilder();
      if (count > 0) {

         accountPlaces.append("(?");
         for (int i = 1; i < count; i++) {
            accountPlaces.append(",").append("?");
         }
         accountPlaces.append(")");
      }
      String sql = ROWS + " from PATTERN p where account_id in" + accountPlaces.toString();
      LOG.debug(sql);
      List<Pattern> query = jdbcTemplate.query(sql, new PatternMatchRowMapper(), accountIdList.toArray());
      return query;
   }
}
