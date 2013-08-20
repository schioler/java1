package dk.schioler.economy;

import java.io.Serializable;

public class PatternMatch implements Serializable {

   /**
    * 
    */
   private static final long serialVersionUID = 1L;

   private final long id;
   private final long accountId;
   private final String pattern;

   public PatternMatch(long id, long accountId, String pattern) {
      super();
      this.id = id;
      this.accountId = accountId;
      this.pattern = pattern;
   }

   public long getId() {
      return id;
   }

   public long getAccountId() {
      return accountId;
   }

   public String getPattern() {
      return pattern;
   }

}
