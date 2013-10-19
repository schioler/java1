package dk.schioler.economy.out.text;

import java.io.PrintWriter;
import java.util.Date;

import dk.schioler.economy.out.Outputter;

public interface TextOutput extends Outputter {
   public abstract void writeToPrinter(String name, String[][] categoryValues, Date[] expenseDates, Object[][] values, PrintWriter out);
}