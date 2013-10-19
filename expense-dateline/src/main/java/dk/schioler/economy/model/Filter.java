package dk.schioler.economy.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang.Validate;

public class Filter implements Serializable {

   /**
    *
    */
   private static final long serialVersionUID = 1L;

   private final Long id;
   private final Long accountId;
   private final Long userId;
   private final String pattern;
   private final String accountPath;
   private final String expOrigin;
   private final Date expDate;
   private final String expText;
   private final BigDecimal expAmount;





   public Filter(Long id, Long accountId, Long userId, String pattern, String accountPath, String expOrigin, Date expDate, String expText, BigDecimal expAmount) {
      super();
      this.id = id;
      this.accountId = accountId;
      this.userId = userId;
      this.pattern = pattern;
      this.accountPath = accountPath;
      this.expOrigin = expOrigin;
      this.expDate = expDate;
      this.expText = expText;
      this.expAmount = expAmount;
      Validate.notNull(accountId, "no emppty accountId allowed");
      Validate.notNull(userId, "no empty userId allowed");
//      Validate.notEmpty(pattern, "no empty patterns allowed");
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

   public Long getUserId() {
      return userId;
   }


   public String getExpOrigin() {
      return expOrigin;
   }


   public Date getExpDate() {
      return expDate;
   }


   public String getExpText() {
      return expText;
   }


   public BigDecimal getExpAmount() {
      return expAmount;
   }



}
