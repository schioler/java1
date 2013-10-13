package dk.schioler.economy.persister.mysql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import dk.schioler.economy.model.User;
import dk.schioler.economy.persister.UserPersister;

@Component("userPersister")
public class UserPersisterMysql implements UserPersister {
   private static final Logger LOG = Logger.getLogger(UserPersisterMysql.class);

   @Autowired
   JdbcTemplate jdbcTemplate;

   private static final class UserRowMapper implements RowMapper<User> {
      public User mapRow(ResultSet rs, int rowNum) throws SQLException {
         User pm = new User(rs.getLong("id"), rs.getString("name"), rs.getTimestamp("ts"));
         return pm;
      }
   }

   static final String SQL_USER_COLS = "id, name,  ts";
   static final String SQL_USER_TABLE = "USER_";

   static final String INSERT_USER_SQL = "insert into " + SQL_USER_TABLE + " (  name) values (?)";

   static final String SQL_SELECT_USER = "select " + SQL_USER_COLS + " from " + SQL_USER_TABLE;
   static final String SQL_GET_USER_ON_ID = SQL_SELECT_USER + " where id =?";
   static final String SQL_GET_USER_ON_NAME = SQL_SELECT_USER + " where name =? ";

   static final String SQL_UPDATE_USER = "update  " + SQL_USER_TABLE + " set name =? where id =? ";
   static final String SQL_DELETE_USER = "delete from  " + SQL_USER_TABLE + " where id =? ";

   public User createUser(User user) {
      User u = null;
      int update = jdbcTemplate.update(INSERT_USER_SQL, user.getName());
      if (update > 0) {
         u = getUser(user.getName());
      }
      return u;
   }

   private User get(String sql, Object... objects) {
      List<User> uList = (List<User>) jdbcTemplate.query(sql, new UserRowMapper(), objects);
      User u = null;
      if (uList != null && uList.size() == 1) {
         u = uList.get(0);
      }
      return u;
   }

   public User getUser(String name) {
      LOG.debug("getUser=" + name);
      return get(SQL_GET_USER_ON_NAME, name);
   }

   public User getUser(Long id) {
      LOG.debug("getUser=" + id);
      return get(SQL_GET_USER_ON_ID, id);
   }

   public List<User> getUsers() {
      return jdbcTemplate.query(SQL_SELECT_USER, new UserRowMapper());
   }

   public int saveUser(User user) {
      LOG.debug("saveUser=" + user);
      return jdbcTemplate.update(SQL_UPDATE_USER, user.getName(), user.getId());
   }

   public int deleteUser(Long id) {
      return jdbcTemplate.update(SQL_DELETE_USER, id);
   }

}
