package dk.schioler.economy;

import java.util.List;

import org.apache.log4j.Logger;

import dk.schioler.economy.model.Account;
import dk.schioler.economy.model.Line;
import dk.schioler.economy.visitor.GetAccountOnFullPathVisitor;
import dk.schioler.economy.visitor.Visitor;
import dk.schioler.economy.visitor.VisitorAccountsAsList;

public class AccountTreeRoot {
   private static final Logger LOG = Logger.getLogger(AccountTreeRoot.class);
   private final Account root;
   private Long userId;

   public AccountTreeRoot(Long userId) {
      super();
      this.userId = userId;
      root = new Account(Account.ROOT);
   }

   public AccountTreeRoot() {
      root = new Account(Account.ROOT);
   }

   public AccountTreeRoot(AccountTreeRoot root) {
      super();
      this.userId = root.userId;
      this.root = new Account(root.root);
   }

   public Account getRoot() {
      return root;
   }

   public void setUserId(Long userId) {
      this.userId = userId;
      root.setUserId(userId);
   }

   public int clear() {
      int size = getAsList().size();
      return size;
   }

   public boolean removeAccount(Account account) {

      return root.removeAccount(account);
   }

   public Account findAccount(Long id) {
      Account a = root.getAccount(id);
        return a;
   }

     public static class AccountTreeDimensions {
      int maxLevel = 0;
      int countAccounts = 0;

      public AccountTreeDimensions(int maxLevel, int countAccounts) {
         super();
         this.maxLevel = maxLevel;
         this.countAccounts = countAccounts;
      }

      public int getMaxLevel() {
         return maxLevel;
      }

      public int getCountAccounts() {
         return countAccounts;
      }

   }

   public AccountTreeDimensions getAccountTreeSize() {
      VisitorBuildAccountTreeDimensions v = new VisitorBuildAccountTreeDimensions();
      root.accept(v);
      return v.getDimensions();
   }

   public int getAccountTreeSize(int maxLevel) {
      VisitorBuildAccountTreeDimensionsWithMaxLevel v = new VisitorBuildAccountTreeDimensionsWithMaxLevel(maxLevel);
      root.accept(v);
      return v.countAccounts;
   }

   public Account addLine(Line line) {
      LOG.debug("addLine:" + line + ", root=" + root);
      Account retVal = root.addLine(line);

      return retVal;
   }

   public Account findAccountOnFullPath(String path) {
      LOG.debug("findAccountOnFullPath: path=" + path);
      GetAccountOnFullPathVisitor visitor = new GetAccountOnFullPathVisitor(path);
      root.accept(visitor);

      return visitor.getTarget();

   }

   public List<Account> getAsList() {
      VisitorAccountsAsList va = new VisitorAccountsAsList();
      //      this.accept(va);
      root.accept(va);
      return va.getAsList();
   }

   public boolean accept(Visitor visitor) {
      LOG.debug("acceptVisitor: " + visitor);
      boolean success = true;
      root.accept(visitor);
      return success;
   }

}
