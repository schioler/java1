package dk.schioler.economy.out.data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import dk.schioler.economy.model.Account;
import dk.schioler.economy.visitor.Visitor;

public class VisitorBuildTablesForOutputOneAccountAndChildren implements Visitor {
   static Logger LOG = Logger.getLogger(VisitorBuildTablesForOutputOneAccountAndChildren.class);
   int firstLevel = -1;
   int curDateIdx;
   int accountLevels;
   String accountName;
   List<String> accountNames = new ArrayList<String>();
   List<List<BigDecimal>> totals = new ArrayList<List<BigDecimal>>();
   List<List<BigDecimal>> regular = new ArrayList<List<BigDecimal>>();
   List<List<BigDecimal>> nonRegular = new ArrayList<List<BigDecimal>>();
   List<List<BigDecimal>> extra = new ArrayList<List<BigDecimal>>();

   public VisitorBuildTablesForOutputOneAccountAndChildren(int accountLevels, String accountName, int countAccounts) {
      super();
      this.accountLevels = accountLevels;
      this.accountName = accountName;
      for (int i = 0; i < countAccounts; i++) {
         totals.add(new ArrayList<BigDecimal>());
         regular.add(new ArrayList<BigDecimal>());
         nonRegular.add(new ArrayList<BigDecimal>());
         extra.add(new ArrayList<BigDecimal>());
      }
   }

   public void resetCurAccountIdx() {
      curDateIdx = 0;
   }

   public boolean visit(Account element) {
      LOG.debug("visit:" + element);
      LOG.debug("levels=" + accountLevels + ", accountName=" + accountName);
      if (StringUtils.isBlank(accountName)) {
         if ((accountLevels) > element.getLevel()) {
            LOG.debug("addoing...");
            String fullPath = element.getFullPath();
            if (!accountNames.contains(fullPath)) {
               accountNames.add(fullPath);
            }
            int indexOf = accountNames.indexOf(fullPath);
            totals.get(indexOf).add(element.getExpensesTotal().abs());
            regular.get(indexOf).add(element.getExpensesRegular().abs());
            nonRegular.get(indexOf).add(element.getExpensesNonRegular().abs());
            extra.get(indexOf).add(element.getExpensesExtra().abs());

         }
      } else if (element.getFullPath().indexOf(accountName) > -1) {
         if (firstLevel == -1) {
            firstLevel = element.getLevel();
         }
         if ((accountLevels + firstLevel) > element.getLevel()) {
            LOG.debug("addoing...");
            String fullPath = element.getFullPath();
            if (!accountNames.contains(fullPath)) {
               accountNames.add(fullPath);
            }
            int indexOf = accountNames.indexOf(fullPath);
            totals.get(indexOf).add(element.getExpensesTotal().abs());
            regular.get(indexOf).add(element.getExpensesRegular().abs());
            nonRegular.get(indexOf).add(element.getExpensesNonRegular().abs());
            extra.get(indexOf).add(element.getExpensesExtra().abs());
         }

      } else {
         LOG.debug("Skipping " + element.getFullPath() + ", knows not of" + accountName);
      }
      return true;
   }

   public boolean init() {
      // TODO Auto-generated method stub
      return false;
   }

   public List<String> getAccountNames() {
      return accountNames;
   }

   public void setAccountNames(List<String> accountNames) {
      this.accountNames = accountNames;
   }

   public List<List<BigDecimal>> getTotals() {
      return totals;
   }

   public void setTotals(List<List<BigDecimal>> totals) {
      this.totals = totals;
   }

   public List<List<BigDecimal>> getRegular() {
      return regular;
   }

   public List<List<BigDecimal>> getNonRegular() {
      return nonRegular;
   }

   public List<List<BigDecimal>> getExtra() {
      return extra;
   }

}
