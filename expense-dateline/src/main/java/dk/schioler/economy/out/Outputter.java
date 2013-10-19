package dk.schioler.economy.out;

import java.io.File;
import java.util.Date;


public interface Outputter {

   public abstract void write(String name, String[][] categoryValues, Date[] expenseDates, Object[][] values, File outputFile);


}
