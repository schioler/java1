package dk.schioler.economy.out.chart;

import java.io.OutputStream;
import java.util.Date;

import dk.schioler.economy.out.Outputter;

public interface Chart extends Outputter{

   public abstract void writeToStream(String name, String[][] categoryValues, Date[] expenseDates, Object[][] values, OutputStream outStream);
}