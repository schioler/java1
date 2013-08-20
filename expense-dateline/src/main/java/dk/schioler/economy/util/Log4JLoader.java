package dk.schioler.economy.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Log4JLoader {
   public static final void loadLog() {
      InputStream resourceAsStream = null;
      try {
         resourceAsStream = Log4JLoader.class.getResourceAsStream("/log4j.properties");
         Properties p = new Properties();
         if (resourceAsStream != null) {
            p.load(resourceAsStream);
            org.apache.log4j.PropertyConfigurator.configure(p);
         }
      } catch (IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      } finally {
         try {
            if (resourceAsStream != null) {
               resourceAsStream.close();
            }
         } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }

      }
   }
}
