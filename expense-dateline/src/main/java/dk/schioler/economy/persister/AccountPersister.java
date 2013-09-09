package dk.schioler.economy.persister;

import java.util.List;

import dk.schioler.economy.AccountTreeRoot;
import dk.schioler.economy.model.Account;

public interface AccountPersister {
   public Account createAccount(Account account);

   public Account getAccount(Long userId, String path, String name);

   public Account getAccount(Long accountId);

   // public Long getAccountId(String name, String path);

   public List<Account> getAccounts(Long userId);

   public int deleteAccount(Long id);

   public int deleteAllAccountsOnUser(Long userId);

   public AccountTreeRoot buildAccountTree(Long userId);
}
