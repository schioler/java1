package dk.schioler.economy.out.data.chart;

import dk.schioler.economy.model.Account;

public class OutputChartDataBuilderBigDecimalExtraordinaire extends OutputChartDataBuilderBigDecimalBase {


   @Override
   public Object getValue(Account account) {

      return account.getExpensesExtra();
   }
   @Override
   public dk.schioler.economy.out.data.VisitorIsRelevantTypeInPath.OutputDataType getTargetType() {
      return dk.schioler.economy.out.data.VisitorIsRelevantTypeInPath.OutputDataType.EXTRAORDINAIRE;
   }

}
