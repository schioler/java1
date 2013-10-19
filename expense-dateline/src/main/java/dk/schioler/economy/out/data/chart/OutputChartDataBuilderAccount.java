package dk.schioler.economy.out.data.chart;

import dk.schioler.economy.model.Account;

public class OutputChartDataBuilderAccount extends OutputChartDataBuilderBase {

   @Override
   public dk.schioler.economy.out.data.VisitorIsRelevantTypeInPath.OutputDataType getTargetType() {
      return dk.schioler.economy.out.data.VisitorIsRelevantTypeInPath.OutputDataType.ALL;
   }

   @Override
   public void instantiateValueTable() {
      if (accountGrid != null && accountGrid.length > 0) {
         values = new Account[accountGrid.length][accountGrid[0].length];
      } else {
         values = new Account[0][0];
      }
   }

   @Override
   public Object getValue(Account a) {
      return a;
   }

}
