package dk.schioler.economy.matcher;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import dk.schioler.economy.model.Pattern;
import dk.schioler.economy.model.User;

@Component("accountMatcherNoUse")
public class AccountMatcherMysql implements AccountMatcher {
   private static final Logger LOG = Logger.getLogger(AccountMatcherMysql.class);
   @Autowired
   JdbcTemplate jdbcTemplate;

   boolean initDone = false;
   Map<String, Long> cachedMatches = null;

   static final String SQL_SELECT = "Select id,account_id, pattern from PATTERN";

//   private void init() {
//      List<PatternMatch> pms = this.jdbcTemplate.query(SQL_SELECT, new PatternMapper());
//      cachedMatches = new TreeMap<String, Long>();
//      for (PatternMatch patternMatch : pms) {
//         cachedMatches.put(patternMatch.getPattern().toUpperCase(), patternMatch.getAccountId());
//      }
//   }

   private static final class PatternMapper implements RowMapper<Pattern> {
      public Pattern mapRow(ResultSet rs, int rowNum) throws SQLException {
         Pattern pm = new Pattern(rs.getLong("id"), rs.getLong("account_id"), rs.getString("pattern"), null);
         return pm;
      }
   }

   private static final class AccountMapper implements RowMapper<Long> {
      public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
         Long id = rs.getLong("id");
         return id;
      }
   }

   String SQL_DIRECT = "SELECT id,account_id, pattern FROM PATTERN_MATCH WHERE locate(PATTERN,?) > 0";
   String SQL_FROM_ACCONT = "SELECT id FROM ACCOUNT WHERE  locate(NAME,?) > 0";

   public long lookInAccount(Object[] args) {
      List<Long> pms = this.jdbcTemplate.query(SQL_FROM_ACCONT, args, new AccountMapper());
      if (pms.size() == 1) {
         LOG.debug("found " + args[0]+" in ACCOUNT table with id=" + pms.get(0));
         return pms.get(0);
      } else {
         return 0;
      }

   }

   public long matchText(String text) {

      String txtUpper = text.toUpperCase();
      Object[] args = new Object[1];
      args[0] = txtUpper;
      List<Pattern> pms = this.jdbcTemplate.query(SQL_DIRECT, args, new PatternMapper());
      if (pms.size() == 0) {
         return lookInAccount(args);
      } else if (pms.size() == 1) {
         LOG.debug("found match for " + args[0] + " in pattern table. AccountId="+ pms.get(0).getAccountId());
         return pms.get(0).getAccountId();
      } else if (pms.size() > 1) {
         return lookInAccount(args);
      } else {
         return 0;
      }

      // String txtUpper = text.toUpperCase();
      // long accountId = 0;
      // for (Entry<String, Long> e : cachedMatches.entrySet()) {
      // String key = e.getKey();
      // if (txtUpper.contains(key)) {
      // accountId = e.getValue();
      // break;
      // }
      // }

      // return pm.getAccountId();
   }

   public void init(User user) {
      // TODO Auto-generated method stub

   }


}
