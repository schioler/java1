package dk.schioler.economy.persister.mysql;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import dk.schioler.economy.model.Pattern;
import dk.schioler.economy.persister.PatternPersister;

@Component("patternPersister")
public class PatternPersisterMysql implements PatternPersister {

   private static final Logger LOG = Logger.getLogger(PatternPersisterMysql.class);
   @Autowired
   JdbcTemplate jdbcTemplate;

   static final String PATTERN_TABLE = "PATTERN";
   static final String PATTERN_TABLE_WITH_ALIAS = PATTERN_TABLE + " p";
   static final String PATTERN_COLS = "p.id,  p.user_id, p.account_id, p.account_path, p.pattern ";

   static final String GET_MATCH_SQL = "Select " + PATTERN_COLS + " from " + PATTERN_TABLE_WITH_ALIAS + " where  p.account_id =? and p.pattern=?";

   public Pattern getPattern(Long accountId, String pattern) {
      List<Pattern> accounts = jdbcTemplate.query(GET_MATCH_SQL, new PatternRowMapper(), accountId, pattern);
      return accounts.get(0);
   }

   static final String INSERT_PATTERN_SQL = "insert into " + PATTERN_TABLE + " (user_id, account_id,pattern, account_path) values (?, ?,?,?)";

   public Pattern createPattern(Long accountId, Pattern pattern) {
      int update = jdbcTemplate.update(INSERT_PATTERN_SQL, pattern.getUserId(), accountId, pattern.getPattern(), pattern.getAccountPath());

      Pattern pm = null;
      if (update > 0) {
         pm = getPattern(accountId, pattern.getPattern());
      }

      return pm;
   }

   static final String GET_PATTERN_ON_ACCOUNT_SQL = "Select " + PATTERN_COLS + " from " + PATTERN_TABLE_WITH_ALIAS + " where p.account_id =?";

   public List<Pattern> getPatternsOnAccountId(Long accountId) {
      List<Pattern> accounts = jdbcTemplate.query(GET_PATTERN_ON_ACCOUNT_SQL, new PatternRowMapper(), accountId);
      return accounts;
   }

   static final String DELETE_PATTERN_ON_ACCOUNT_SQL = "DELETE FROM " + PATTERN_TABLE + " where account_id =? ";

   public int deletePatterns(Long accountId) {
      return jdbcTemplate.update(DELETE_PATTERN_ON_ACCOUNT_SQL, accountId);
   }

   static final String DELETE_PATTERN_ON_USER_SQL = "DELETE FROM " + PATTERN_TABLE + " where user_id = ?";

   public int deleteAllPatternsOnUser(Long userId) {
      return jdbcTemplate.update(DELETE_PATTERN_ON_USER_SQL, userId);
   }

   static final String GET_PATTERN_ON_USER_SQL = "Select " + PATTERN_COLS + " from "+PATTERN_TABLE_WITH_ALIAS + " where p.user_id = ?";
   public List<Pattern> getPatternsOnUserId(Long userId) {
      List<Pattern> patterns = jdbcTemplate.query(GET_PATTERN_ON_USER_SQL, new PatternRowMapper(), userId);
      return patterns;
   }

   static final String GET_PATTERNS_ON_ACCOUNT_LIST = "Select " + PATTERN_COLS + " from "+PATTERN_TABLE_WITH_ALIAS + " where account_id in ";
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

      String string = GET_PATTERNS_ON_ACCOUNT_LIST  + accountPlaces.toString();
      LOG.debug(string);
      List<Pattern> query = jdbcTemplate.query(string, new PatternRowMapper(), accountIdList.toArray());
      return query;
   }
}
