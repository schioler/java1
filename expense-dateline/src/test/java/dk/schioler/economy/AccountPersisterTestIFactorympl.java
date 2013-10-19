package dk.schioler.economy;

import java.util.Date;
import java.util.List;

import dk.schioler.economy.model.Account;
import dk.schioler.economy.model.Account.Type;
import dk.schioler.economy.model.User;
import dk.schioler.economy.persister.AccountPersister;

public class AccountPersisterTestIFactorympl implements AccountPersister {

   private final User user;
   private final AccountTreeRoot root;

   public AccountPersisterTestIFactorympl(User user) {
      super();
      this.user = user;
      root = new AccountTreeRoot(user.getId());
      initTestData();
   }

   public void initTestData() {
      root.clear();
      Account boligAccount = new Account(Long.valueOf(1), root.getRoot(), user.getId(), "Bolig",  Type.REGULAR, new Date());
      Account boligAccountFinans = new Account(Long.valueOf(2), boligAccount, user.getId(), "Finans", Type.REGULAR, new Date());
      Account boligAccountFinansLaan = new Account(Long.valueOf(3), boligAccountFinans, user.getId(), "Lån", Type.REGULAR, new Date());

      Account boligAccountTag = new Account(Long.valueOf(4), boligAccount, user.getId(), "Tag", Type.REGULAR, new Date());

      Account boligAccountVVS = new Account(Long.valueOf(5), boligAccount, user.getId(), "VVS",  Type.NON_REGULAR, new Date());
      Account boligAccountVVSExtra = new Account(Long.valueOf(6), boligAccountVVS, user.getId(), "Extra",  Type.EXTRAORDINAIRE, new Date());
      Account boligAccountVVSCheck = new Account(Long.valueOf(7), boligAccountVVS, user.getId(), "Check",  Type.NON_REGULAR, new Date());


//      root.addAccountToTree(boligAccount);
//      root.addAccountToTree(boligAccountFinans);
//      root.addAccountToTree(boligAccountFinansLaan);
//      root.addAccountToTree(boligAccountTag);
//      root.addAccountToTree(boligAccountVVS);
//      root.addAccountToTree(boligAccountVVSExtra);
//      root.addAccountToTree(boligAccountVVSCheck);
   }

   public Account createAccount(Account account) throws ParentAccountNotFoundException {
//      root.addAccountToTree(account);
//      return root.findAccount(account.getId());
      return null;
   }



   @Override
   public Long getAccountId(Long userId, String fullPath) {
      if (user.getId().equals(userId)) {

         Account account = root.findAccountOnFullPath(fullPath);
         return account.getId();
      }
      return null;
   }

   public Account getAccount(Long accountId) {

      return root.findAccount(accountId);
   }

   public List<Account> getAccounts(Long userId) {
      if (user.getId().equals(userId)) {
         return root.getAsList();
      }
      return null;
   }

   public int deleteAccount(Long id) {
      Account findAccount = root.findAccount(id);
      boolean removeAccount = root.removeAccount(findAccount);
      return removeAccount ? 1 : 0;
   }

   public int deleteAllAccountsOnUser(Long userId) {
      if (user.getId().equals(userId)) {
         return root.clear();
      }
      return 0;
   }

   public AccountTreeRoot buildAccountTree(Long userId) {
      return new AccountTreeRoot(root);
   }

}
