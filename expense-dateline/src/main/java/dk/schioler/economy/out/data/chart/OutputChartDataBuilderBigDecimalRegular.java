package dk.schioler.economy.out.data.chart;

import dk.schioler.economy.model.Account;
import dk.schioler.economy.out.data.VisitorIsRelevantTypeInPath.OutputDataType;

public class OutputChartDataBuilderBigDecimalRegular extends OutputChartDataBuilderBigDecimalBase {

   @Override
   public Object getValue(Account account) {

      return account.getExpensesRegular();
   }
   @Override
   public OutputDataType getTargetType() {
      return OutputDataType.REGULAR;
   }
}
