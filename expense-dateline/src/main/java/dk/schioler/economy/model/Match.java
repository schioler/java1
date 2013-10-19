package dk.schioler.economy.model;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.Validate;

public class Match implements Serializable {

   /**
    *
    */
   private static final long serialVersionUID = 1L;

   private final Long id;
   private final Date ts;
   private final Long userId;
   private final Long accountId;
   private final Long lineId;
   private final Long filterId;
   private final Long patternId;

   public Match(Long id, Date ts, Long userId, Long accountId, Long lineId, Long filterId, Long patternId) {
      super();
      this.id = id;
      this.ts = ts;
      this.userId = userId;
      this.accountId = accountId;
      this.lineId = lineId;
      this.filterId = filterId;
      this.patternId = patternId;
   }

   public Match(Match match) {
      super();
      this.id = match.getId();
      this.ts = new Date(match.ts.getTime());
      this.userId = match.getUserId();
      this.accountId = match.getAccountId();
      this.lineId = match.getLineId();
      this.filterId = match.getFilterId();
      this.patternId = match.getPatternId();
   }

   public Long getId() {
      return id;
   }

   public Date getTs() {
      return ts;
   }

   public Long getUserId() {
      return userId;
   }

   public Long getAccountId() {
      return accountId;
   }

   public Long getLineId() {
      return lineId;
   }

   public Long getFilterId() {
      return filterId;
   }

   public Long getPatternId() {
      return patternId;
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((accountId == null) ? 0 : accountId.hashCode());
      result = prime * result + ((filterId == null) ? 0 : filterId.hashCode());
      result = prime * result + ((id == null) ? 0 : id.hashCode());
      result = prime * result + ((lineId == null) ? 0 : lineId.hashCode());
      result = prime * result + ((patternId == null) ? 0 : patternId.hashCode());
      result = prime * result + ((ts == null) ? 0 : ts.hashCode());
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
      Match other = (Match) obj;
      if (accountId == null) {
         if (other.accountId != null)
            return false;
      } else if (!accountId.equals(other.accountId))
         return false;
      if (filterId == null) {
         if (other.filterId != null)
            return false;
      } else if (!filterId.equals(other.filterId))
         return false;
      if (id == null) {
         if (other.id != null)
            return false;
      } else if (!id.equals(other.id))
         return false;
      if (lineId == null) {
         if (other.lineId != null)
            return false;
      } else if (!lineId.equals(other.lineId))
         return false;
      if (patternId == null) {
         if (other.patternId != null)
            return false;
      } else if (!patternId.equals(other.patternId))
         return false;
      if (ts == null) {
         if (other.ts != null)
            return false;
      } else if (!ts.equals(other.ts))
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
      return "Match [id=" + id + ", ts=" + ts + ", userId=" + userId + ", accountId=" + accountId + ", lineId=" + lineId + ", filterId=" + filterId + ", patternId=" + patternId + "]";
   }

}
