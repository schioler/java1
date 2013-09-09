package dk.schioler.economy.model;

import java.io.Serializable;

import org.apache.commons.lang.Validate;

public class Pattern implements Serializable {

   /**
    *
    */
   private static final long serialVersionUID = 1L;

   private final Long id;
   private final Long accountId;
   private final String pattern;
   private final String accountPath;

   public Pattern(Long id, Long accountId, String pattern, String accountPath) {
      super();
      this.id = id;
      this.accountId = accountId;
      this.pattern = pattern;
      this.accountPath = accountPath;
      Validate.notEmpty(pattern, "no empty patterns allowed");
      Validate.notEmpty(accountPath, "no empty accountpath allowed");
   }

   public Long getId() {
      return id;
   }

   public Long getAccountId() {
      return accountId;
   }

   public String getPattern() {
      return pattern;
   }

   public String getAccountPath() {
      return accountPath;
   }

   @Override
   public String toString() {
      return "Pattern [id=" + id + ", accountId=" + accountId + ", pattern=" + pattern + ", accountPath=" + accountPath + "]";
   }

  

}
