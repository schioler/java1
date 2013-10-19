package dk.schioler.economy.persister.mysql;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import dk.schioler.economy.AccountTreeRoot;
import dk.schioler.economy.ExpenseException;
import dk.schioler.economy.model.Account;
import dk.schioler.economy.model.Account.Type;

public class AccountRowToTreeMapper implements RowMapper<Account> {
   AccountTreeRoot root = new AccountTreeRoot();

   public Account mapRow(ResultSet rs, int rowNum) throws SQLException {
      String aType = rs.getString("atype");
      Type type = Account.getType(aType);
      long long1 = rs.getLong("parent_id");
      Account parent = root.findAccount(new Long(long1));
      int int1 = rs.getInt("level");
      Account pm = new Account(rs.getLong("id"), parent, rs.getLong("user_id"), rs.getString("name"), type, rs.getDate("ts"));
      if (int1 != pm.getLevel()) {
         throw new ExpenseException("level not valid...");
      }
      return pm;
   }

   public AccountTreeRoot getRoot() {
      return root;
   }
}
