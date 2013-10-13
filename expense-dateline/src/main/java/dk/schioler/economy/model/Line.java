package dk.schioler.economy.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

public class Line implements Serializable {

   private static final long serialVersionUID = 1L;

   private final Long id;
   private final Long userId;
   private final String origin;
   private final Date date;
   private final String text;
   private final BigDecimal amount;

   private final Date timestamp;

   private final Match match;

   public Line(Long id, Long userId, String origin, Date date, String text, BigDecimal amount, Date timestamp, Match match) {
      super();
      this.id = id;
      this.userId = userId;
      this.origin = origin;
      this.date = date;
      this.text = text;
      this.amount = amount;
      this.timestamp = timestamp;
      this.match = match;
   }

   public Line(Line line) {
      super();
      this.id = line.getId();
      this.userId = line.getUserId();
      this.origin = line.getOrigin();
      this.date = new Date(line.getDate().getTime());
      this.text = line.getText();
      this.amount = new BigDecimal(line.getAmount().toString());
      this.timestamp = new Date(line.getTimestamp().getTime());
      this.match = new Match(line.getMatch());
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

   public Long getId() {
      return id;
   }

   public Long getUserId() {
      return userId;
   }

   public Long getAccountId() {
      if (match != null)
         return match.getAccountId();
      return null;
   }

   public Long getFilterId() {
      if (match != null)
         return match.getFilterId();
      return null;
   }

   public Long getPatternId() {
      if (match != null)
         return match.getPatternId();
      return null;
   }

   public Long getMatchId() {
      if (match != null)
         return match.getAccountId();
      return null;
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((amount == null) ? 0 : amount.hashCode());
      result = prime * result + ((date == null) ? 0 : date.hashCode());
      result = prime * result + ((id == null) ? 0 : id.hashCode());
      result = prime * result + ((match == null) ? 0 : match.hashCode());
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
      if (id == null) {
         if (other.id != null)
            return false;
      } else if (!id.equals(other.id))
         return false;
      if (match == null) {
         if (other.match != null)
            return false;
      } else if (!match.equals(other.match))
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

   @Override
   public String toString() {
      return getDate() +";  " +  StringUtils.leftPad(text, 75) + ", " + StringUtils.leftPad(amount.toString(), 10);
   }

   public Match getMatch() {
      return match;
   }

}
