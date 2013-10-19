package dk.schioler.economy.out.data;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import dk.schioler.economy.ExpenseException;
import dk.schioler.economy.out.data.VisitorIsRelevantTypeInPath.OutputDataType;
import dk.schioler.economy.out.data.chart.OutputChartDataBuilder;
import dk.schioler.economy.out.data.chart.OutputChartDataBuilderBigDecimalExtraordinaire;
import dk.schioler.economy.out.data.chart.OutputChartDataBuilderBigDecimalNonRegular;
import dk.schioler.economy.out.data.chart.OutputChartDataBuilderBigDecimalRegular;
import dk.schioler.economy.out.data.chart.OutputChartDataBuilderBigDecimalTotal;
import dk.schioler.economy.out.data.text.OutputTextDataBuilder;
import dk.schioler.economy.out.data.text.OutputTextDataBuilderAll;

@Component("outputDataBuilderFactory")
public class OutputDataBuilderFactoryImpl implements OutputDataBuilderFactory {

   public OutputChartDataBuilder getChartDataBuilder(String accountDataType) {
      OutputChartDataBuilder retVal = null;
      if (OutputDataType.REGULAR.toString().equalsIgnoreCase(accountDataType)) {
         retVal = new OutputChartDataBuilderBigDecimalRegular();

      } else if (OutputDataType.NONREGULAR.toString().equalsIgnoreCase(accountDataType)) {
         retVal = new OutputChartDataBuilderBigDecimalNonRegular();
      } else if (OutputDataType.EXTRAORDINAIRE.toString().equalsIgnoreCase(accountDataType)) {
         retVal = new OutputChartDataBuilderBigDecimalExtraordinaire();
      } else if (OutputDataType.TOTAL.toString().equalsIgnoreCase(accountDataType)) {
         retVal = new OutputChartDataBuilderBigDecimalTotal();
      } else {
         throw new ExpenseException("Unsupported accaountDataType:" + accountDataType);
      }
      return retVal;
   }

   @Override
   public OutputTextDataBuilder getTextDataBuilder(String accountDataType) {
      OutputTextDataBuilder retVal = null;
      if (OutputDataType.ALL.toString().equalsIgnoreCase(accountDataType) || StringUtils.isBlank(accountDataType)) {
         retVal = new OutputTextDataBuilderAll();
      } else {
         throw new ExpenseException("Unsupported textDataType:" + accountDataType);
      }
      return retVal;
   }

}
