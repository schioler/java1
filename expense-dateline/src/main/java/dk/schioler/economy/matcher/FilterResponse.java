package dk.schioler.economy.matcher;

public class FilterResponse {
   public static enum ResponseType {
      SKIP, MATCH
   };

   final ResponseType response;
   final Long accountId;
   final Long filterId;

   public FilterResponse(ResponseType response, Long accountId, Long filterId) {
      super();
      this.response = response;
      this.accountId = accountId;
      this.filterId = filterId;

   }

   public ResponseType getResponse() {
      return response;
   }

   public Long getAccountId() {
      return accountId;
   }

   public Long getFilterId() {
      return filterId;
   }

   
}
