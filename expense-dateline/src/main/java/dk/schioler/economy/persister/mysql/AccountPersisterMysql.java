package dk.schioler.economy.persister.mysql;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import dk.schioler.economy.AccountTreeRoot;
import dk.schioler.economy.ExpenseException;
import dk.schioler.economy.model.Account;
import dk.schioler.economy.persister.AccountPersister;

@Component("accountPersister")
public class AccountPersisterMysql implements AccountPersister {

   private static final Logger LOG = Logger.getLogger(AccountPersisterMysql.class);

   @Autowired
   JdbcTemplate jdbcTemplate;

   static final String SQL_ACCOUNT_COLS = "id, user_id, parent_id, name, path,  level, atype, ts ";
   static final String ACCOUNT_TABLE = "ACCOUNT";

   static final String SQL_SELECT = "select " + SQL_ACCOUNT_COLS + " from " + ACCOUNT_TABLE;

   static final String GET_ACCOUNT_ON_PATH_AND_NAME_SQL = "select id from " + ACCOUNT_TABLE + " where path =? and user_id = ?";

   @Override
   public Long getAccountId(Long userId, String fullPath) {
      LOG.debug("userId=" + userId + ", fullPath=" + fullPath);
      List<Long> ids = jdbcTemplate.query(GET_ACCOUNT_ON_PATH_AND_NAME_SQL, new AccountRowToIdListMapper(), fullPath, userId);
      Long id = null;
      if (ids.size() > 0) {
         id = ids.get(0);
      }
      return id;
   }

   static final String INSERT_ACCOUNT_SQL = "insert into ACCOUNT (user_id,  parent_id, name, path, atype, level) values (?,?,?,?,?,?)";

   public Account createAccount(Account account) {
      if (Account.ROOT_NAME.equals(account.getName())) {
         throw new ExpenseException("attempt to persist ROOT account - not permitted");
      }
      if (account.getId() != null && account.getId().longValue() > 0) {
         return account;
      }
      Account parent = account.getParent();
      if (parent.getId() == null) {
         parent = createAccount(parent);

      }

      LOG.debug("parent=" + parent);

      Long parentId = parent.getId();
      String type = Account.getTypeAsString(account.getType());

      int update = 0;
      try {
         update = jdbcTemplate.update(INSERT_ACCOUNT_SQL, account.getUserId(), parentId, account.getName(), account.getFullPath(), type, account.getLevel());
         LOG.debug("InsertResult=" + update);
         if (update > 0) {
            Long id = getAccountId(account.getUserId(), account.getFullPath());
            account.setId(id);
         }
         return account;
      } catch (org.springframework.dao.DuplicateKeyException e) {
         throw new ExpenseException(e.getMessage(), e);
      }
   }

   int count = 0;

   public AccountTreeRoot buildAccountTree(Long userId) {
      count++;
      LOG.debug("*************************** Will build accountTree: frothe " + count + " time, userId=" + userId);

      return getAccountTreeRoot(userId);
   }

   static final String GET_ACCOUNT_LIST_ON_USER_SQL = SQL_SELECT + "  where  user_id = ? order by parent_id ";

   private AccountTreeRoot getAccountTreeRoot(Long userId) {
      AccountRowToTreeMapper rMapper = new AccountRowToTreeMapper();
      jdbcTemplate.query(GET_ACCOUNT_LIST_ON_USER_SQL, rMapper, userId);
      return rMapper.getRoot();
   }

   public List<Account> getAccounts(Long userId) {
      return getAccountTreeRoot(userId).getAsList();
   }

   static final String DELETE_ACCOUNT_ON_ID_SQL = "delete from ACCOUNT where id = ? ";

   public int deleteAccount(Long id) {
      return jdbcTemplate.update(DELETE_ACCOUNT_ON_ID_SQL, id);
   }

   static final String DELETE_ACCOUNT_ON_USER_SQL = "delete from ACCOUNT where user_id = ? ";

   public int deleteAllAccountsOnUser(Long userId) {

      return jdbcTemplate.update(DELETE_ACCOUNT_ON_USER_SQL, userId);

   }

}
