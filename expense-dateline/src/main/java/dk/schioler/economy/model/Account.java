package dk.schioler.economy.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import dk.schioler.economy.ExpenseException;
import dk.schioler.economy.visitor.Visitor;

public class Account implements Serializable, Comparable<Account> {
   private static final Logger LOG = Logger.getLogger(Account.class);

   public static final String ROOT_NAME = "AQWERTYUIOPLKJNEVERHAPPEN";
   public static final Account ROOT = new Account(Long.valueOf("0"), null, null, ROOT_NAME, null, null);;
   public static final String PATH_SEPARATOR = "/";
   public static final String TYPE_REGULAR = "R";
   public static final String TYPE_NON_REGULAR = "N";
   public static final String TYPE_EXTRAORDINAIRE = "E";
   public static final String TYPE_ALL = "A";

   public enum Type {
      REGULAR, NON_REGULAR, EXTRAORDINAIRE
   };

   private static final long serialVersionUID = 1L;

   private Long id;

   private Account parent;
   private Long userId;
   private final String name;
   private final int level;
   private final Type type;
   private final Date timestamp;

   private final List<Pattern> patterns = new ArrayList<Pattern>();

   private final List<Line> lines = new ArrayList<Line>();

   private final List<Account> children = new ArrayList<Account>();

   // Calculated...
   private BigDecimal expensesTotal = new BigDecimal("0");
   private BigDecimal expensesRegular = new BigDecimal("0");
   private BigDecimal expensesNonRegular = new BigDecimal("0");
   private BigDecimal expensesExtra = new BigDecimal("0");

   public Account(Long id, Account parent, Long userId, String name, Type type, Date timestamp) {
      super();
      this.id = id;
      if (!ROOT_NAME.equals(name)) {
         if (parent == null) {
            throw new ExpenseException("ParentAccount is required....");
         }
      }
      this.parent = parent;
      this.userId = userId;
      this.name = name;
      //      this.path = path;

      this.type = type;
      this.timestamp = timestamp;
      this.expensesTotal = expensesTotal.setScale(2, BigDecimal.ROUND_HALF_EVEN);
      this.expensesRegular = expensesRegular.setScale(2, BigDecimal.ROUND_HALF_EVEN);
      this.expensesNonRegular = expensesNonRegular.setScale(2, BigDecimal.ROUND_HALF_EVEN);
      this.expensesExtra = expensesExtra.setScale(2, BigDecimal.ROUND_HALF_EVEN);
      if (parent != null) {
         this.level = parent.getLevel() + 1;
         parent.addChild(this);
      } else {
         this.level = 0;
      }
   }

   /**
    * CopyConstructor.....
    * @param account
    */
   public Account(Account account) {
      super();
      this.id = account.getId();
      this.parent = account.getParent();
      this.userId = account.getUserId();
      this.name = account.getName();
      //      this.path = account.getPath();
      this.level = account.getLevel();
      this.type = account.getType();
      this.timestamp = account.getTimestamp() == null ? null : new Date(account.getTimestamp().getTime());
      for (Account account2 : account.children) {
         children.add(new Account(account2));
      }
      for (Line line : account.lines) {
         lines.add(new Line(line));
      }
      for (Pattern pattern : account.patterns) {
         patterns.add(new Pattern(pattern));
      }
      this.addToExtra(account.getExpensesExtra());
      this.addToNonRegular(account.getExpensesNonRegular());
      this.addToRegular(account.getExpensesRegular());
   }

   public boolean addChild(Account child) {
      return this.children.add(child);
   }

   public static Type getType(String aType) {
      Type type = null;
      if (Account.TYPE_REGULAR.equalsIgnoreCase(aType)) {
         type = Type.REGULAR;
      } else if (Account.TYPE_NON_REGULAR.equalsIgnoreCase(aType)) {
         type = Type.NON_REGULAR;
      } else if (Account.TYPE_EXTRAORDINAIRE.equalsIgnoreCase(aType)) {
         type = Type.EXTRAORDINAIRE;
      } else {
         throw new ExpenseException("No type for :" + aType);
      }
      return type;
   }

   public static String getTypeAsString(Type type) {
      String typeStr = null;
      if (Type.REGULAR.equals(type)) {
         typeStr = Account.TYPE_REGULAR;
      } else if (Type.NON_REGULAR.equals(type)) {
         typeStr = Account.TYPE_NON_REGULAR;
      } else if (Type.EXTRAORDINAIRE.equals(type)) {
         typeStr = Account.TYPE_EXTRAORDINAIRE;
      } else {
         throw new ExpenseException("Un-known type= " + type);
      }
      return typeStr;
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
      LOG.debug("accepting visitor. host ='" + getFullPath() + "'");
      boolean goOn = visitor.visit(this);
      if (goOn) {
         for (Account e : children) {
            goOn = e.accept(visitor);
            if (!goOn) {
               break;
            }
         }
      }
      return goOn;
   }

   public void addToNonRegular(BigDecimal amount) {
      expensesTotal = expensesTotal.add(amount).setScale(2, BigDecimal.ROUND_HALF_EVEN);
      expensesNonRegular = expensesNonRegular.add(amount).setScale(2, BigDecimal.ROUND_HALF_EVEN);
   }

   public void addToRegular(BigDecimal amount) {
      expensesTotal = expensesTotal.add(amount).setScale(2, BigDecimal.ROUND_HALF_EVEN);
      expensesRegular = expensesRegular.add(amount).setScale(2, BigDecimal.ROUND_HALF_EVEN);
   }

   public void addToExtra(BigDecimal amount) {
      expensesTotal = expensesTotal.add(amount).setScale(2, BigDecimal.ROUND_HALF_EVEN);
      expensesExtra = expensesExtra.add(amount).setScale(2, BigDecimal.ROUND_HALF_EVEN);
   }

   public Account addLine(Line line) {
      //      LOG.debug("addLine: this.id=" + this.getId() + ", line=" + line.getAccountId());
      Account retVal = null;
      if (line.getAccountId().equals(getId())) {
         //         LOG.debug("trace equals");
         if (!lines.contains(line)) {
            lines.add(line);
            if (isRegular()) {
               addToRegular(line.getAmount());
            } else if (isNonRegular()) {
               addToNonRegular(line.getAmount());
            } else if (isExtra()) {
               addToExtra(line.getAmount());
            }
            retVal = this;
         } else {
            LOG.warn("attempted to add same line again - bounce! line=" + line);
         }
      } else {
         for (Account a : children) {
            retVal = a.addLine(line);
            if (retVal != null) {
               if (retVal.isRegular()) {
                  addToRegular(line.getAmount());
               } else if (retVal.isNonRegular()) {
                  addToNonRegular(line.getAmount());
               } else if (retVal.isExtra()) {
                  addToExtra(line.getAmount());
               }
               break;
            }
         }
      }
      //      LOG.debug(this.getId() + ", returning: " + retVal);
      return retVal;
   }

   public List<Account> getChildren() {
      return children;
   }

   public BigDecimal getExpensesNonRegular() {
      return expensesNonRegular;
   }

   //   public Account addAccount(Account account) {
   //
   //      String thisFullPath = this.getFullPath();
   //      if (!PATH_SEPARATOR.equals(thisFullPath)) {
   //         thisFullPath = thisFullPath + PATH_SEPARATOR;
   //      }
   //
   //      Account child = null;
   //      if (thisFullPath.equals(accountPath)) {
   //         child = account;
   //         children.add(account);
   //      } else {
   //         for (Account entry : children) {
   //            Account value = entry;
   //            child = value.addAccount(account);
   //            if (child != null) {
   //               break;
   //            }
   //         }
   //      }
   //      return child;
   //   }

   public boolean removeAccount(Account account) {
      boolean removed = false;

      // first immediate children..
      for (Iterator<Account> iterator = children.iterator(); iterator.hasNext();) {
         Account childAccount = iterator.next();
         if (childAccount.getId().equals(account.getId())) {
            iterator.remove();
            removed = true;
            break;
         }
      }
      if (!removed) {
         // now further down the line...
         for (Iterator<Account> iterator = children.iterator(); iterator.hasNext();) {
            Account childAccount = iterator.next();
            removed = childAccount.removeAccount(account);
            if (removed) {
               break;
            }
         }
      }
      return removed;
   }

   public List<Long> getThisAndChildIds() {
      List<Long> ids = new ArrayList<Long>();
      addThisAndChildIdsTo(ids);
      return ids;
   }

   private void addThisAndChildIdsTo(List<Long> ids) {
      ids.add(id);
      for (Account a : this.children) {
         a.addThisAndChildIdsTo(ids);
      }
   }

   public List<Account> getThisAndChildren() {
      List<Account> accounts = new ArrayList<Account>();
      addThisAndChildrenTo(accounts);
      return accounts;
   }

   private void addThisAndChildrenTo(List<Account> accounts) {
      accounts.add(this);
      for (Account a : this.children) {
         a.addThisAndChildrenTo(accounts);
      }
   }

   public List<Line> getLines() {
      return lines;
   }

   public BigDecimal getExpensesTotal() {
      return expensesTotal;
   }

   public BigDecimal getExpensesRegular() {
      return expensesRegular;
   }

   public String getFullPath() {
      if (ROOT.getName().equals(getName())) {
         return "";
      } else if (parent == null) {
         return PATH_SEPARATOR + name;
      } else {
         return parent.getFullPath() + PATH_SEPARATOR + name;
      }

   }

   public static String getPathSeparator() {
      return PATH_SEPARATOR;
   }

   public Long getId() {
      return id;
   }

   public Long getUserId() {
      return userId;
   }

   public String getName() {
      return name;
   }

   public int getLevel() {
      return level;
   }

   public Date getTimestamp() {
      return timestamp;
   }

   public void setUserId(Long userId) {
      this.userId = userId;
      if (!children.isEmpty()) {
         for (Account a : children) {
            a.setUserId(userId);
         }
      }
   }

   public void addPattern(Pattern pattern) {
      patterns.add(pattern);
   }

   public void addPatternString(String pattern) {
      //      throw new ExpenseException("added pattern..");
      patterns.add(new Pattern(null, this.getUserId(), this.getId(), pattern, this.getFullPath()));
   }

   public List<Pattern> getPatterns() {
      return patterns;
   }

   public List<String> getPatternsAsStrings() {

      List<String> list = new ArrayList<String>();
      for (Pattern p : patterns) {
         list.add(p.getPattern());
      }
      return list;
   }

   public int compareTo(Account o) {
      return this.getFullPath().compareTo(o.getFullPath());
   }

   public Type getType() {
      return type;
   }

   public boolean isRegular() {
      return Type.REGULAR.equals(this.getType());
   }

   public boolean isNonRegular() {
      return Type.NON_REGULAR.equals(this.getType());
   }

   public boolean isExtra() {
      return Type.EXTRAORDINAIRE.equals(this.getType());
   }

   public BigDecimal getExpensesExtra() {
      return expensesExtra;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public void setParent(Account parent) {
      this.parent = parent;
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + level;
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
      if (!getFullPath().equals(other.getFullPath()))
         return false;
      return true;
   }

   public Account getParent() {
      return parent;
   }

   @Override
   public String toString() {
      String parentPathString = "n/a";
      if (parent != null) {
         parentPathString = parent.getFullPath();
      }
      return "Account [id=" + id + ", name=" + name + ", parent=" + parentPathString + ", userId=" + userId + ", level=" + level + ", type=" + type + ", expensesTotal=" + expensesTotal
            + ", expensesRegular=" + expensesRegular + ", expensesNonRegular=" + expensesNonRegular + ", expensesExtra=" + expensesExtra + "]";
   }
}
