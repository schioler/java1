package dk.schioler.economy.out.chart;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import dk.schioler.economy.ExpenseException;
import dk.schioler.economy.model.Account;

public class ChartJFreeBar extends JFreeBaseChart {
   static Logger LOG = Logger.getLogger(ChartJFreeBar.class);
   SimpleDateFormat sdFormat = new SimpleDateFormat("MM-yy");

   public ChartJFreeBar() {
      super();
   }

   // values idx matches: [][] : categorryValues, []: expenseDates
   /* (non-Javadoc)
    * @see dk.schioler.economy.out.chart.Chart#write(java.lang.String[], java.util.Date[], java.math.BigDecimal[][], java.io.File)
    */
   public void write(String name, String[][] categoryValues, Date[] expenseDates, Object[][] values, File outputDirectory) {
      JFreeChart chart = createChart(name, categoryValues, expenseDates, values);

      writeToFile(name + "-bar_graph.png", outputDirectory, chart);

   }

   protected JFreeChart createChart(String name, String[][] categoryValues, Date[] expenseDates, Object[][] values) {

      int levels = values.length / expenseDates.length;
      //      LOG.debug(Arrays.toString(expenseDates) + ", levels=" + levels);
      DefaultCategoryDataset dataset = new DefaultCategoryDataset();
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

                  String format = "";

                  format = sdFormat.format(expenseDates[dateIdx]);
                  //                  LOG.debug("col=" + col + ", row=" + row + ", dateIdx=" + dateIdx + ", catego=" + catego + ", date=" + format + ", value=" + value);
                  //            LOG.debug("date=" + format + ", categorty=" + catego + ", value=" + value);
                  dataset.addValue(value, catego, format);
                  //               }
               }
            }
         }
         if (col % levels == (levels - 1)) {
            dateIdx++;
         }
      }

      JFreeChart chart = ChartFactory.createBarChart(name, // chart title
            "Month", // domain axis label
            "Value", // range axis label
            dataset, // data
            PlotOrientation.VERTICAL, // orientation
            true, // include legend
            true, // tooltips?
            false // URLs?
            );
      CategoryPlot plot = (CategoryPlot) chart.getPlot();
      BarRenderer renderer = (BarRenderer) plot.getRenderer();
      renderer.setItemMargin(0.0);
      CategoryAxis domainAxis = plot.getDomainAxis();
      domainAxis.setCategoryMargin(0.4);

      int columnCount = dataset.getColumnCount();

      for (int idx = 0; idx < columnCount; idx++) {
         RenderFormat format = RENDER_FORMATS.get(idx % RENDER_FORMATS.size());
         renderer.setSeriesPaint(idx, format.color);
      }
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
