package dk.schioler.economy.out.chart;

import dk.schioler.economy.ExpenseException;

public class ChartNotFoundException extends ExpenseException {

   private static final long serialVersionUID = 1L;

   public ChartNotFoundException() {
      super();
   }

   public ChartNotFoundException(String arg0, Throwable arg1) {
      super(arg0, arg1);
   }

   public ChartNotFoundException(String arg0) {
      super(arg0);
   }

   public ChartNotFoundException(Throwable arg0) {
      super(arg0);
   }

}
