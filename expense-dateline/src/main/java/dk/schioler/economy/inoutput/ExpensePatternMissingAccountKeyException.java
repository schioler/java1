package dk.schioler.economy.inoutput;

import dk.schioler.economy.ExpenseException;

public class ExpensePatternMissingAccountKeyException extends ExpenseException {

   /**
    *
    */
   private static final long serialVersionUID = 1L;

   public ExpensePatternMissingAccountKeyException() {
   }

   public ExpensePatternMissingAccountKeyException(String arg0, Throwable arg1) {
      super(arg0, arg1);
   }

   public ExpensePatternMissingAccountKeyException(String arg0) {
      super(arg0);
     }

   public ExpensePatternMissingAccountKeyException(Throwable arg0) {
      super(arg0);

   }

}
