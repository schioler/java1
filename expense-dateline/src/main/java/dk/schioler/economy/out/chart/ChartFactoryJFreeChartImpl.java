package dk.schioler.economy.out.chart;

import org.springframework.stereotype.Component;

@Component("chartFactory")
public class ChartFactoryJFreeChartImpl implements ChartFactory {

   public static final String CHART_BAR = "bar";
   public static final String CHART_TIMESERIES = "timeseries";

   public Chart getChart(String type) throws ChartNotFoundException {
      if (CHART_BAR.equalsIgnoreCase(type)) {
         return new ChartJFreeBar();
      } else if (CHART_TIMESERIES.equalsIgnoreCase(type)) {
         return new ChartJFreeTimeseries();
      } else {
         throw new ChartNotFoundException("missing chart="+type);
      }

   }

}
