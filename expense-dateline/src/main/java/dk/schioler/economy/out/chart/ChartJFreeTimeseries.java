package dk.schioler.economy.out.chart;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.Month;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.springframework.stereotype.Component;

import dk.schioler.economy.ExpenseException;
import dk.schioler.economy.model.Account;
import dk.schioler.economy.util.OutUtil;
import dk.schioler.economy.util.OutUtil.MinAndMaxDates;

@Component("chartJFreeTimeseries")
public class ChartJFreeTimeseries extends JFreeBaseChart {

   private static final Logger LOG = Logger.getLogger(ChartJFreeTimeseries.class);
   SimpleDateFormat sdFormat = new SimpleDateFormat("MM-yy");

   public ChartJFreeTimeseries() {
      super();
   }

   // values idx matches: [][] : categorryValues, []: expenseDates
   public void write(String name, String[][] categoryValues, Date[] expenseDates, Object[][] values, File outputDirectory) {

      JFreeChart createChart = createChart(name, categoryValues, expenseDates, values);
      writeToFile(name + "-timeseries_graph.png", outputDirectory, createChart);

   }

   protected JFreeChart createChart(String name, String[][] categoryValues, Date[] expenseDates, Object[][] values) {

      int levels = values.length / expenseDates.length;
      TimeSeriesCollection tsCollection = new TimeSeriesCollection();
      Map<String, TimeSeries> timeseries = new TreeMap<String, TimeSeries>();

      int dateIdx = 0;
      for (int col = 0; col < values.length; col++) {
         for (int row = 0; row < values[col].length; row++) {
            if (((col + 1) % categoryValues.length == 0)) {
               BigDecimal vBD = (BigDecimal) values[col][row];
               if (vBD != null) {
                  Long value = vBD.longValue();
                  String catego = "";
                  for (int k = 0; k < categoryValues.length; k++) {
                     String string = categoryValues[k][row];
                     if (k == 0) {
                        if (StringUtils.isNotBlank(string)) {
                           catego = string;
                        }
                     } else {
                        if (StringUtils.isNotBlank(string)) {
                           catego = catego + Account.PATH_SEPARATOR + string;
                        }
                     }
                     LOG.debug("kol=" + k + ",row=" + row + ", accountName=" + string + ", catego=" + catego);
                  }

                  TimeSeries ts = null;
                  if (!timeseries.containsKey(catego)) {
                     ts = new TimeSeries(catego, Month.class);
                     tsCollection.addSeries(ts);
                     timeseries.put(catego, ts);
                  } else {
                     ts = timeseries.get(catego);
                  }

                  Date currentIntervalTime = expenseDates[dateIdx];
                  Month month = new Month(currentIntervalTime);
                  LOG.debug("col=" + col + ", row=" + row + ", dateIdx=" + dateIdx + ", catego=" + catego + ", date=" + month);
                  ts.add(month, value);

               }
            }
         }
         if (col % levels == (levels - 1)) {
            dateIdx++;
         }
      }

      MinAndMaxDates minAndMaxDates = OutUtil.getOutputFileDateFormatFrom(expenseDates[0], expenseDates[expenseDates.length - 1]);

      String title = name + " over " + expenseDates.length + " mths: " + minAndMaxDates.minDate + " to " + minAndMaxDates.maxDate;

      JFreeChart chart = ChartFactory.createTimeSeriesChart(title, "Time", "Amount", tsCollection, true, true, false);

      formatChart(chart, tsCollection.getSeries().size());
      return chart;
   }

   @Override
   public void writeToStream(String name, String[][] categoryValues, Date[] expenseDates, Object[][] values, OutputStream outStream) {
      JFreeChart chart = createChart(name, categoryValues, expenseDates, values);
      try {
         ChartUtilities.writeChartAsPNG(outStream, chart, width, height);
      } catch (IOException e) {
         throw new ExpenseException(e.getMessage(), e);
      }
   }

}
