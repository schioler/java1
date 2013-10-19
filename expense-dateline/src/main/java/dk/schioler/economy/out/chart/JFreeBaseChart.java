package dk.schioler.economy.out.chart;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;

public abstract class JFreeBaseChart implements Chart {
   private static final Logger LOG = Logger.getLogger(JFreeBaseChart.class);
   final int width;
   final int height;

   public JFreeBaseChart() {
      super();
      this.width = 2500;
      this.height = 1500;
   }

   public int getWidth() {
      return width;
   }

   public int getHeight() {
      return height;
   }

   protected static final List<RenderFormat> RENDER_FORMATS = new ArrayList<RenderFormat>();
   static {
      RENDER_FORMATS.add(new RenderFormat(128, 35, 158));
      RENDER_FORMATS.add(new RenderFormat(0, 200, 0));
      RENDER_FORMATS.add(new RenderFormat(0, 0, 255));
      RENDER_FORMATS.add(new RenderFormat(200, 0, 200));
      RENDER_FORMATS.add(new RenderFormat(255, 0, 0));
      RENDER_FORMATS.add(new RenderFormat(255, 128, 0));
      RENDER_FORMATS.add(new RenderFormat(255, 255, 0));
      RENDER_FORMATS.add(new RenderFormat(0, 255, 255));
      RENDER_FORMATS.add(new RenderFormat(128, 0, 128));
      RENDER_FORMATS.add(new RenderFormat(0, 0, 0));
      RENDER_FORMATS.add(new RenderFormat(80, 94, 26));
      RENDER_FORMATS.add(new RenderFormat(19, 111, 9));
   }

   protected static final class RenderFormat {
      Color color;
      final boolean linesVisible = true;
      final boolean shapesVisible = true;
      final boolean shapesFilled = true;
      final Shape shape = new Ellipse2D.Double(-3.0, -3.0, 4.0, 4.0);
      final Stroke stroke = new BasicStroke(1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER);

      public RenderFormat(int red, int green, int blue) {
         super();
         color = new Color(red, green, blue);
      }
   }

   public void formatChart(JFreeChart chart, int countTS) {
      XYPlot plot = chart.getXYPlot();
      plot.setDomainCrosshairVisible(true);
      plot.setRangeCrosshairVisible(true);
      plot.getDomainAxis().setAutoRange(true);

      ValueAxis rangeAxis = plot.getRangeAxis();
      rangeAxis.setAutoRange(true);
      rangeAxis.setStandardTickUnits(NumberAxis.createStandardTickUnits(Locale.getDefault()));

      XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();

      for (int idx = 0; idx < countTS; idx++) {
         RenderFormat format = RENDER_FORMATS.get(idx % RENDER_FORMATS.size());
         renderer.setDrawSeriesLineAsPath(false);
         renderer.setSeriesPaint(idx, format.color);
         renderer.setSeriesLinesVisible(idx, format.linesVisible);
         renderer.setSeriesShape(idx, format.shape);
         renderer.setSeriesShapesVisible(idx, format.shapesVisible);
         renderer.setSeriesShapesFilled(idx, true);
         renderer.setSeriesStroke(idx, format.stroke);

      }
   }

   protected void writeToFile(String name, File outputDirectory, JFreeChart chart) {
      try {

         if (!outputDirectory.exists()) {
            outputDirectory.mkdirs();
         }

         String filename = name.replace("/", "_");
         File outputFile = new File(outputDirectory.getAbsolutePath() + "/" + filename);
         LOG.info("writing chart to : " + outputFile);
         ChartUtilities.saveChartAsPNG(outputFile, chart, getWidth(), getHeight());
         //         ChartUtilities.writeChartAsPNG(out, chart, width, height);
      } catch (IOException e) {
         LOG.error(e.getMessage(), e);
      }
   }

}
