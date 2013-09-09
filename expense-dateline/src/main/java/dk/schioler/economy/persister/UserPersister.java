package dk.schioler.economy.persister;

import java.util.List;

import dk.schioler.economy.model.User;

public interface UserPersister {

   public User createUser(User user);
   public User getUser(String name);
   public User getUser(Long id);
   public List<User> getUsers();
   public int saveUser(User user);

   public int deleteUser(Long id);

}
