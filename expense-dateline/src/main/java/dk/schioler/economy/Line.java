package dk.schioler.economy;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class Line implements Serializable {

   @Override
   public String toString() {
      return "Line [id=" + id + ", accountId=" + accountId + ", owner=" + owner + ", origin=" + origin + ", date=" + date
            + ", text=" + text + ", amount=" + amount + ", timestamp=" + timestamp + "]";
   }

   private static final long serialVersionUID = 1L;

   private final long id;
   private final long accountId;
   private final String owner;
   private final String origin;
   private final Date date;
   private final String text;
   private final BigDecimal amount;

   private final Date timestamp;

   public static long getSerialversionuid() {
      return serialVersionUID;
   }

   public Line(long id, long accountId, String owner, String origin, Date date, String text, BigDecimal amount, Date timestamp) {
      super();
      this.id = id;
      this.accountId = accountId;
      this.owner = owner;
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

   public String getOwner() {
      return owner;
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

}
