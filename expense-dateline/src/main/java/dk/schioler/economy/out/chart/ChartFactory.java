package dk.schioler.economy.out.chart;

public interface ChartFactory {
   public Chart getChart(String type) throws ChartNotFoundException;
}
