package dk.schioler.economy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import dk.schioler.economy.model.Account;
import dk.schioler.economy.model.Line;
import dk.schioler.economy.model.Pattern;
import dk.schioler.economy.visitor.Visitor;
import dk.schioler.economy.visitor.VisitorAccountsAsList;

public class AccountTreeRoot {
   private static final Logger LOG = Logger.getLogger(AccountTreeRoot.class);
   private final List<Account> children = new ArrayList<Account>();

   public void addAccountToTree(Account account) throws ParentAccountNotFoundException {
      LOG.debug("adding account=" + account.getFullPath());

      boolean success = false;
      if (StringUtils.isBlank(account.getPath())) {
         children.add(account);
         success = true;
         LOG.debug("successful add child to this:" + this);
      } else {
         for (Account entry : children) {
            Account child = entry.addAccount(account);
            if (child != null) {
               success = true;
               LOG.debug(" has been added:" + account.getFullPath());
               LOG.debug(" ");
               break;
            }
         }
      }

      if (!success) {
         throw new ParentAccountNotFoundException(account.toString());
      }
   }

   public Account findAccount(Long id) {
      Account a = null;
      for (Account account : children) {
         a = account.getAccount(id);
         if (a != null) {
            break;
         }
      }

      return a;
   }

   public void addPattern(Pattern pattern) {

   }

   public class AccountTreeSize implements Visitor {
      int maxLevel = 0;
      int countAccounts = 0;

      public boolean visit(Account element) {
         maxLevel = Math.max(maxLevel, element.getLevel());
         return true;
      }

      public boolean init() {
         return false;
      }

      public int getMaxLevel() {
         return maxLevel;
      }

      public int getCountAccounts() {
         return countAccounts;
      }

   }

   public AccountTreeSize getAccountTreeSize() {
      AccountTreeSize v = new AccountTreeSize();
      if (!children.isEmpty()) {
         for (Account ace : children) {
            ace.accept(v);
         }
      }
      return v;
   }

   public Account addLine(Line line) {
      Account retVal = null;
      for (Account e : this.children) {
         retVal = e.addLine(line);
         if (retVal != null) {
            break;
         }
      }
      return retVal;
   }

   public Account findAccountOnNamePath(String path) {
      LOG.debug(path);
      String[] split = path.split(Account.PATH_SEPARATOR);
      Account retVal = findAccount(split, 0, children);

      return retVal;

   }

   public List<Account> getAsList() {
      VisitorAccountsAsList va = new VisitorAccountsAsList();
      this.accept(va);
      return va.getAsList();
   }

   private Account findAccount(String[] path, int curIdx, List<Account> accounts) {
      /*
       * Find account with cur-idx name in accounts. If found: idx++,
       * accounts.chidren and call recursively If not found: return null
       */
      Account retVal = null;
      String name = path[curIdx];
      LOG.debug(Arrays.toString(path) + ", idx=" + curIdx + ", name=" + name);

      for (Account e : accounts) {

         // LOG.debug(ate);
         if (name.equals(e.getName())) {
            if (curIdx == (path.length - 1)) {
               // found it!
               LOG.debug("found accoubtTreeElement:" + e);
               retVal = e;
               break;
            } else {
               int nextIdx = curIdx + 1;
               if (nextIdx < path.length) {
                  retVal = findAccount(path, nextIdx, e.getChildAccounts());
                  if (retVal != null) {
                     break;
                  }
               }
            }
         } else {

         }
      }

      return retVal;

   }

   public boolean accept(Visitor visitor) {
      boolean success = true;
      for (Account entry : children) {
         Account child = entry;
         success = child.accept(visitor);
         if (!success) {
            break;
         }
      }

      return success;
   }

   @Override
   public String toString() {
      return "AccountTreeRoot [children=" + children.size() + "]";
   }

}
