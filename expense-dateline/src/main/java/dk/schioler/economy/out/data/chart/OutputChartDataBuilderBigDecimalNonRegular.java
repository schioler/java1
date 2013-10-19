package dk.schioler.economy.out.data.chart;

import dk.schioler.economy.model.Account;

public class OutputChartDataBuilderBigDecimalNonRegular extends OutputChartDataBuilderBigDecimalBase {



   @Override
   public Object getValue(Account account) {
      return account.getExpensesNonRegular();
   }
   @Override
   public dk.schioler.economy.out.data.VisitorIsRelevantTypeInPath.OutputDataType getTargetType() {
      return dk.schioler.economy.out.data.VisitorIsRelevantTypeInPath.OutputDataType.NONREGULAR;
   }
}
