package dk.schioler.economy.out.data;

import java.util.Date;

public interface OutputDataBuilder {
  
   public void buildOutData(TimeLine timeline, String accountName, int countLevels);

   public String[][] getCategories();

   public Date[] getDates();

   public Object[][] getValues();

}
