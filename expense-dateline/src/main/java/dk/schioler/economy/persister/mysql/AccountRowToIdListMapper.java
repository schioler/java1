package dk.schioler.economy.persister.mysql;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class AccountRowToIdListMapper implements RowMapper<Long> {

   public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
      long id = rs.getLong("id");
      return new Long(id);
   }

}
