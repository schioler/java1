package dk.schioler.economy.persister.mysql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import dk.schioler.economy.AccountTreeRoot;
import dk.schioler.economy.model.Account;
import dk.schioler.economy.persister.AccountPersister;

@Component("accountPersister")
public class AccountPersisterMysql implements AccountPersister {

   private static final Logger LOG = Logger.getLogger(AccountPersisterMysql.class);

   @Autowired
   JdbcTemplate jdbcTemplate;

   static final String SQL_ACCOUNT_ROWS = "id, user_id, parent_id, name, path, avg, regular, level, ts";
   static final String SQL_SELECT = "select " + SQL_ACCOUNT_ROWS + " from ACCOUNT";

   static final String GET_ACCOUNT_LIST_SQL = SQL_SELECT + "  where  user_id = ? order by parent_id ";

   static final String GET_ACCOUNT_ON_ID_SQL = SQL_SELECT + " where id =?";
   static final String GET_ACCOUNT_ON_PATH_AND_NAME_SQL = SQL_SELECT + " where path =? and name=? and user_id = ?";

   static final String INSERT_ACCOUNT_SQL = "insert into ACCOUNT (user_id,  parent_id, name, path, avg, regular, level) values (?,?,?,?,?,?,?)";
   static final String DELETE_ACCOUNT_ON_USER_SQL = "delete from ACCOUNT where user_id = ? ";
   static final String DELETE_ACCOUNT_ON_ID_SQL = "delete from ACCOUNT where id = ? ";

   private static final class AccountRowMapper implements RowMapper<Account> {
      public Account mapRow(ResultSet rs, int rowNum) throws SQLException {
         boolean avg = "y".equalsIgnoreCase(rs.getString("AVG"));
         boolean regular = "y".equalsIgnoreCase(rs.getString("REGULAR"));

         Account pm = new Account(rs.getLong("id"), rs.getLong("parent_id"), rs.getLong("user_id"), rs.getString("name"), rs.getString("path"),
               rs.getInt("level"), avg, regular, rs.getDate("ts"));
         return pm;
      }
   }

   public Account getAccount(Long userId, String path, String name) {
      LOG.debug("userId=" + userId + ", path=" + path + ", name=" + name);
      List<Account> accounts = jdbcTemplate.query(GET_ACCOUNT_ON_PATH_AND_NAME_SQL, new AccountRowMapper(), path, name, userId);
      Account a = null;
      if (accounts.size() > 0) {
         a = accounts.get(0);
      }
      return a;
   }

   public Account getAccount(Long accountId) {
      List<Account> accounts = jdbcTemplate.query(GET_ACCOUNT_ON_ID_SQL, new AccountRowMapper(), accountId);
      return accounts.get(0);
   }

   public Account createAccount(Account account) {
      String path = account.getPath();

      Account parent = null;

      if (!StringUtils.isBlank(path)) {
         String parentPath = null;
         String parentName = null;

         int lastIndexOf = path.lastIndexOf(Account.PATH_SEPARATOR);
         if (lastIndexOf > -1) {
            parentPath = path.substring(0, lastIndexOf);
            parentName = path.substring(lastIndexOf + 1);
         } else {
            parentPath = "";
            parentName = path;
         }
         parent = getAccount(account.getUserId(), parentPath, parentName);
      }

      LOG.debug("parent=" + parent);

      Long parentId = Long.valueOf("-1");
      if (parent != null) {
         parentId = parent.getId();
      }
      String avg = BooleanUtils.toString(account.isUseInAverage(), "Y", "N");
      String regular = BooleanUtils.toString(account.isRegular(), "Y", "N");
      int update = jdbcTemplate.update(INSERT_ACCOUNT_SQL, account.getUserId(), parentId, account.getName(), account.getPath(), avg, regular,
            account.getLevel());
      LOG.debug("InsertResult=" + update);
      Account a = null;
      if (update > 0) {
         a = getAccount(account.getUserId(), account.getPath(), account.getName());
      }
      return a;
   }

   public AccountTreeRoot buildAccountTree(Long userId) {
      LOG.debug("Will build accountTree: userId=" + userId);
      List<Account> accounts = getAccounts(userId);

      AccountTreeRoot root = new AccountTreeRoot();
      for (Account account : accounts) {
         // LOG.debug(account);
         root.addAccountToTree(account);
      }
      return root;
   }

   public List<Account> getAccounts(Long userId) {
      LOG.debug("getAccounts=" + userId);
      return jdbcTemplate.query(GET_ACCOUNT_LIST_SQL, new AccountRowMapper(), userId);
   }

   public int deleteAccount(Long id) {
      String delete = "delete from ACCOUNT where id =?";
      return jdbcTemplate.update(delete, id);
   }

   public int deleteAllAccountsOnUser(Long userId) {
      String delete = "delete from ACCOUNT where user_id =?";
      return jdbcTemplate.update(delete, userId);

   }

}
