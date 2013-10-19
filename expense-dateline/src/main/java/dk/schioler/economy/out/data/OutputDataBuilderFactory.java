package dk.schioler.economy.out.data;

import dk.schioler.economy.out.data.chart.OutputChartDataBuilder;
import dk.schioler.economy.out.data.text.OutputTextDataBuilder;

public interface OutputDataBuilderFactory {
   public OutputChartDataBuilder getChartDataBuilder(String accountDataType);

   public OutputTextDataBuilder getTextDataBuilder(String accountDataType);
}
