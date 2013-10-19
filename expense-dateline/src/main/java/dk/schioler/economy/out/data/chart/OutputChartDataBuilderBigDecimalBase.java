package dk.schioler.economy.out.data.chart;

import java.math.BigDecimal;

public abstract class OutputChartDataBuilderBigDecimalBase extends OutputChartDataBuilderBase {

   @Override
   public void instantiateValueTable() {
      if (accountGrid != null && accountGrid.length > 0) {
         values = new BigDecimal[accountGrid.length][accountGrid[0].length];
      } else {
         values = new BigDecimal[0][0];
      }
   }

}
