package dk.schioler.economy.out.data;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import dk.schioler.economy.model.Account;
import dk.schioler.economy.visitor.Visitor;

public class VisitorGetAccountRowsInLevel implements Visitor {
   private static final Logger LOG = Logger.getLogger(VisitorGetAccountRowsInLevel.class);

   private final int targetCountLevels;
   private final String targetAccountFullPath;
   private final VisitorIsRelevantTypeInPath.OutputDataType targetType;
   private final VisitorIsRelevantTypeInPath isRelevantInPathVisitor;

   private Account account;
   private List<Account> accounts = new ArrayList<Account>();
   private int accountsMaxLevel = 0;

   public VisitorGetAccountRowsInLevel(int targetCountLevels, String targetAccountFullPath, VisitorIsRelevantTypeInPath isRelevantInPathVisitor) {
      super();
      this.targetCountLevels = targetCountLevels;
      this.targetAccountFullPath = targetAccountFullPath.equals(Account.ROOT_NAME) ? "" : targetAccountFullPath;
      this.targetType = isRelevantInPathVisitor.getTargetType();
      this.isRelevantInPathVisitor = isRelevantInPathVisitor;
      LOG.debug(this);
   }

   public boolean visit(Account host) {
      LOG.debug(" visit at : '" + host.getFullPath() + "', account=" + account);
      isRelevantInPathVisitor.init();
      if (account == null) {
         if (targetAccountFullPath.equals(host.getFullPath())) {

            host.accept(isRelevantInPathVisitor);
            if (isRelevantInPathVisitor.isPresent()) {
               account = host;
               accounts.add(host);
               accountsMaxLevel = host.getLevel();
               LOG.debug("foundRootAccount: " + account);
            } else {
               LOG.info("found target account, but no targetTypes in path: targetType=" + targetType + ", levels=" + targetCountLevels + ", maxLevel=" + accountsMaxLevel + ", account=" + host);
               return false;
            }
         }
         return true;
      } else {
         // target account has been found previously, now a child or from another toplevel account....
         if (host.getFullPath().indexOf(account.getFullPath()) > -1) {
            // child :-)
            if (account.getLevel() + targetCountLevels > host.getLevel()) {

               host.accept(isRelevantInPathVisitor);
               if (isRelevantInPathVisitor.isPresent()) {

                  accounts.add(host);
                  accountsMaxLevel = Math.max(accountsMaxLevel, host.getLevel());
                  LOG.debug("added child: " + host.getFullPath() + ", maxLevel=" + accountsMaxLevel + ", accountsSize" + accounts.size() + ", " + host);
               }
               return true;
            } else {
               return true;
            }

         } else {
            return true;
         }
      }
   }

   public boolean init() {
      // TODO Auto-generated method stub
      return false;
   }

   public List<Account> getAccounts() {
      return accounts;
   }

   public int getTargetCountLevels() {
      return targetCountLevels;
   }

   public String getTargetAccountFullPath() {
      return targetAccountFullPath;
   }

   public Account getAccount() {
      return account;
   }

   public int getActualCountLevels() {
      if (accounts.size() == 1) {
         return 1;
      } else {
         if (account != null) {
            return (accountsMaxLevel - account.getLevel()) + 1;
         } else {
            return 0;
         }
      }
   }

   public VisitorIsRelevantTypeInPath.OutputDataType getTargetType() {
      return targetType;
   }

   @Override
   public String toString() {
      return "VisitorGetAccountRowsInLevel [targetCountLevels=" + targetCountLevels + ", targetAccountFullPath=" + targetAccountFullPath + ", targetType=" + targetType + ", isRelevantInPathVisitor="
            + isRelevantInPathVisitor + ", account=" + account + ", accounts=" + accounts + ", accountsMaxLevel=" + accountsMaxLevel + "]";
   }

}
