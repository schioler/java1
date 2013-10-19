package dk.schioler.economy.persister.mysql;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import dk.schioler.economy.model.Pattern;

// public Pattern(Long id, Long accountId, String pattern, String
// accountPath)
public class PatternRowMapper implements RowMapper<Pattern> {
   public Pattern mapRow(ResultSet rs, int rowNum) throws SQLException {
      Pattern pm = new Pattern(rs.getLong("id"), rs.getLong("user_id"), rs.getLong("account_id"), rs.getString("pattern"), rs.getString("account_path"));
      return pm;
   }
}
