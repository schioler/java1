package dk.schioler.economy.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class Line implements Serializable {

   private static final long serialVersionUID = 1L;

   private final long id;
   private final long accountId;
   private final Long userId;
   private final String origin;
   private final Date date;
   private final String text;
   private final BigDecimal amount;

   private final Date timestamp;



   public Line(long id, long accountId, Long userId, String origin, Date date, String text, BigDecimal amount, Date timestamp) {
      super();
      this.id = id;
      this.accountId = accountId;
      this.userId = userId;
      this.origin = origin;
      this.date = date;
      this.text = text;
      this.amount = amount;
      this.timestamp = timestamp;
   }

   public Date getDate() {
      return date;
   }

   public String getText() {
      return text;
   }

   public BigDecimal getAmount() {
      return amount;
   }

   public Date getTimestamp() {
      return timestamp;
   }

   public String getOrigin() {
      return origin;
   }

   public long getId() {
      return id;
   }

   public long getAccountId() {
      return accountId;
   }

   public Long getUserId() {
      return userId;
   }

   @Override
   public String toString() {
      return "Line [id=" + id + ", accountId=" + accountId + ", userId=" + userId + ", origin=" + origin + ", date=" + date + ", text=" + text
            + ", amount=" + amount + ", timestamp=" + timestamp + "]";
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + (int) (accountId ^ (accountId >>> 32));
      result = prime * result + ((amount == null) ? 0 : amount.hashCode());
      result = prime * result + ((date == null) ? 0 : date.hashCode());
      result = prime * result + (int) (id ^ (id >>> 32));
      result = prime * result + ((origin == null) ? 0 : origin.hashCode());
      result = prime * result + ((text == null) ? 0 : text.hashCode());
      result = prime * result + ((timestamp == null) ? 0 : timestamp.hashCode());
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
      Line other = (Line) obj;
      if (accountId != other.accountId)
         return false;
      if (amount == null) {
         if (other.amount != null)
            return false;
      } else if (!amount.equals(other.amount))
         return false;
      if (date == null) {
         if (other.date != null)
            return false;
      } else if (!date.equals(other.date))
         return false;
      if (id != other.id)
         return false;
      if (origin == null) {
         if (other.origin != null)
            return false;
      } else if (!origin.equals(other.origin))
         return false;
      if (text == null) {
         if (other.text != null)
            return false;
      } else if (!text.equals(other.text))
         return false;
      if (timestamp == null) {
         if (other.timestamp != null)
            return false;
      } else if (!timestamp.equals(other.timestamp))
         return false;
      if (userId == null) {
         if (other.userId != null)
            return false;
      } else if (!userId.equals(other.userId))
         return false;
      return true;
   }

   

}
