package dk.schioler.economy.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import dk.schioler.economy.visitor.Visitor;

public class Account implements Serializable, Comparable<Account> {
   private static final Logger LOG = Logger.getLogger(Account.class);

   public static final String PATH_SEPARATOR = "/";

   private static final long serialVersionUID = 1L;

   private final Long id;
   private final Long parentId;
   private Long userId;
   private final String name;
   private final String path;
   private final int level;
   private final boolean useInAverage;
   private final boolean isRegular;
   private final Date timestamp;

   private final List<Pattern> patterns = new ArrayList<Pattern>();

   private final List<Account> children = new ArrayList<Account>();

   private final List<Line> lines = new ArrayList<Line>();

   private BigDecimal expensesTotal = new BigDecimal("0");
   private BigDecimal expensesRegular = new BigDecimal("0");
   private BigDecimal expensesNonRegular = new BigDecimal("0");
   private BigDecimal expensesForAvg = new BigDecimal("0");
   private BigDecimal childExpenses = new BigDecimal("0");

   public Account(Long id, Long parentId, Long userId, String name, String path, int level, boolean useInAverage, boolean isRegular, Date timestamp) {
      super();
      this.id = id;
      this.parentId = parentId;
      this.userId = userId;
      this.name = name;
      this.path = path;
      this.level = level;
      this.useInAverage = useInAverage;
      this.isRegular = isRegular;
      this.timestamp = timestamp;
      this.expensesTotal = expensesTotal.setScale(2, BigDecimal.ROUND_UP);
      this.childExpenses = childExpenses.setScale(2, BigDecimal.ROUND_UP);
   }

   public void addPattern(Pattern pattern) {
      patterns.add(pattern);
   }

   public void addPatternString(String pattern) {
      patterns.add(new Pattern(null, this.getId(), pattern, this.getFullPath()));
   }

   public List<Pattern> getPatterns() {
      return patterns;
   }

   public List<Account> getChildAccounts() {
      return children;
   }

   public Account getAccount(Long id) {
      Account a = null;
      if (id.equals(this.getId())) {
         a = this;
      } else {
         for (Account account : children) {
            a = account.getAccount(id);
            if (a != null) {
               break;
            }
         }
      }
      return a;
   }

   public boolean accept(Visitor visitor) {
      boolean goOn = visitor.visit(this);
      if (goOn) {
         for (Account e : children) {
            Account value = e;
            goOn = value.accept(visitor);
            if (!goOn) {
               break;
            }
         }
      }
      return goOn;
   }

   public Account addLine(Line line) {
      Account retVal = null;
      if (line.getAccountId() == getId()) {
         lines.add(line);
         LOG.debug("addLine: " + getFullPath() + ",  ExpensesTotal=" + expensesTotal + ",  ExpensesRegular=" + expensesRegular + ",  ExpensesAvg="
               + expensesForAvg + ", line.amount=" + line.getAmount() + ", text=" + line.getText());
         expensesTotal = expensesTotal.add(line.getAmount()).setScale(2, BigDecimal.ROUND_UP);
         if (this.isRegular) {
            expensesRegular = expensesRegular.add(line.getAmount()).setScale(2, BigDecimal.ROUND_UP);
         } else {
            expensesNonRegular = expensesNonRegular.add(line.getAmount()).setScale(2, BigDecimal.ROUND_UP);
         }
         if (this.useInAverage) {
            expensesForAvg = expensesForAvg.add(line.getAmount()).setScale(2, BigDecimal.ROUND_UP);
         }
         LOG.debug("addLine: " + getFullPath() + ",  ExpensesTotal=" + expensesTotal + ",  ExpensesRegular=" + expensesRegular + ",  ExpensesAvg="
               + expensesForAvg + ", line.amount=" + line.getAmount() + ", text=" + line.getText());
         retVal = this;
      } else {
         for (Account e : children) {
             retVal = e.addLine(line);
            if (retVal != null) {
               expensesTotal = expensesTotal.add(line.getAmount()).setScale(2, BigDecimal.ROUND_UP);
               if (retVal.isRegular) {
                  expensesRegular = expensesRegular.add(line.getAmount()).setScale(2, BigDecimal.ROUND_UP);
               } else {
                  expensesNonRegular = expensesNonRegular.add(line.getAmount()).setScale(2, BigDecimal.ROUND_UP);
               }
               if (retVal.useInAverage) {
                  expensesForAvg = expensesForAvg.add(line.getAmount()).setScale(2, BigDecimal.ROUND_UP);
               }
               LOG.debug("addLine: " + getFullPath() + ", wayback success. Expenses=" + expensesTotal + ", childExpenses=" + childExpenses
                     + ", line.amount=" + line.getAmount() + ", text=" + line.getText());
               break;
            }
         }
      }

      return retVal;
   }

   public List<Account> getChildren() {
      return children;
   }

   public BigDecimal getExpensesNonRegular() {
      return expensesNonRegular;
   }

   public Account addAccount(Account account) {
      // LOG.debug("addAccount:" + account + " to " + this);
      String path = account.getPath();
      // LOG.debug("accountPath=" + path);
      // String thisPath = this.account.getPath();
      // LOG.debug("thisPath=" + thisPath);

      Account child = null;
      if (this.getFullPath().equals(path)) {
         child = account;
         children.add(account);
         LOG.debug("Found parent=" + this.getFullPath() + " to " + account.getFullPath());

      } else {
         for (Account entry : children) {
            Account value = entry;
            child = value.addAccount(account);
            if (child != null) {
               LOG.debug("Added to some child:" + account.getFullPath() + ", this:" + this.getFullPath());
               break;
            }
         }
      }
      return child;
   }

   public List<Line> getLines() {
      return lines;
   }

   // public BigDecimal getExpenses() {
   // return expensesTotal;
   // }

   public BigDecimal getChildExpenses() {
      return childExpenses;
   }

   public BigDecimal getExpensesTotal() {
      return expensesTotal;
   }

   public BigDecimal getExpensesRegular() {
      return expensesRegular;
   }

   public BigDecimal getExpensesForAvg() {
      return expensesForAvg;
   }

   public String getFullPath() {
      if (StringUtils.isNotBlank(path)) {
         return path + PATH_SEPARATOR + name;
      } else {
         return name;
      }
   }

   public static String getPathSeparator() {
      return PATH_SEPARATOR;
   }

   public Long getId() {
      return id;
   }

   public Long getParentId() {
      return parentId;
   }

   public Long getUserId() {
      return userId;
   }

   public String getName() {
      return name;
   }

   public String getPath() {
      return path;
   }

   public int getLevel() {
      return level;
   }

   public boolean isUseInAverage() {
      return useInAverage;
   }

   public boolean isRegular() {
      return isRegular;
   }

   public Date getTimestamp() {
      return timestamp;
   }

   public void setUserId(Long userId) {
      this.userId = userId;
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((id == null) ? 0 : id.hashCode());
      result = prime * result + (isRegular ? 1231 : 1237);
      result = prime * result + level;
      result = prime * result + ((name == null) ? 0 : name.hashCode());
      result = prime * result + ((parentId == null) ? 0 : parentId.hashCode());
      result = prime * result + ((path == null) ? 0 : path.hashCode());
      result = prime * result + ((timestamp == null) ? 0 : timestamp.hashCode());
      result = prime * result + (useInAverage ? 1231 : 1237);
      result = prime * result + ((userId == null) ? 0 : userId.hashCode());
      return result;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (getClass() != obj.getClass())
         return false;
      Account other = (Account) obj;
      if (id == null) {
         if (other.id != null)
            return false;
      } else if (!id.equals(other.id))
         return false;
      if (isRegular != other.isRegular)
         return false;
      if (level != other.level)
         return false;
      if (name == null) {
         if (other.name != null)
            return false;
      } else if (!name.equals(other.name))
         return false;
      if (parentId == null) {
         if (other.parentId != null)
            return false;
      } else if (!parentId.equals(other.parentId))
         return false;
      if (path == null) {
         if (other.path != null)
            return false;
      } else if (!path.equals(other.path))
         return false;
      if (timestamp == null) {
         if (other.timestamp != null)
            return false;
      } else if (!timestamp.equals(other.timestamp))
         return false;
      if (useInAverage != other.useInAverage)
         return false;
      if (userId == null) {
         if (other.userId != null)
            return false;
      } else if (!userId.equals(other.userId))
         return false;
      return true;
   }

   @Override
   public String toString() {
      return "Account [id=" + id + ", parentId=" + parentId + ", userId=" + userId + ", name=" + name + ", path=" + path + ", level=" + level
            + ", useInAverage=" + useInAverage + ", isRegular=" + isRegular + ", timestamp=" + timestamp + ", patterns=" + patterns + ", children="
            + children + ", lines=" + lines + ", expenses=" + expensesTotal + ", childExpenses=" + childExpenses + "]";
   }

   public int compareTo(Account o) {
      return this.getFullPath().compareTo(o.getFullPath());
   }

}
