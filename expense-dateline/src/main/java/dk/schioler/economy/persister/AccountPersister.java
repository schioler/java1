package dk.schioler.economy.persister;

import java.util.List;

import dk.schioler.economy.AccountTreeRoot;
import dk.schioler.economy.model.Account;

public interface AccountPersister {
   public Account createAccount(Account account);

   public Long getAccountId(Long userId, String fullPath);

//   public Account getAccount(Long accountId);

   public List<Account> getAccounts(Long userId);

   public int deleteAccount(Long id);

   public int deleteAllAccountsOnUser(Long userId);

   public AccountTreeRoot buildAccountTree(Long userId);

//   public AccountTreeRoot buildAccountTreeWithPatterns(Long userId);
}
