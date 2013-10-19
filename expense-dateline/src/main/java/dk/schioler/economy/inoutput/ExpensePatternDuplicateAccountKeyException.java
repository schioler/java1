package dk.schioler.economy.inoutput;

import dk.schioler.economy.ExpenseException;

public class ExpensePatternDuplicateAccountKeyException extends ExpenseException {

   /**
    *
    */
   private static final long serialVersionUID = 1L;

   public ExpensePatternDuplicateAccountKeyException() {
   }

   public ExpensePatternDuplicateAccountKeyException(String arg0, Throwable arg1) {
      super(arg0, arg1);
   }

   public ExpensePatternDuplicateAccountKeyException(String arg0) {
      super(arg0);
     }

   public ExpensePatternDuplicateAccountKeyException(Throwable arg0) {
      super(arg0);

   }

}
