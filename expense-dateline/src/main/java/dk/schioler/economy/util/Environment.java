package dk.schioler.economy.util;

import java.net.URL;
import java.net.URLClassLoader;

public class Environment {
   public static void printClasspath(){
      ClassLoader sysClassLoader = ClassLoader.getSystemClassLoader();

      //Get the URLs
      URL[] urls = ((URLClassLoader)sysClassLoader).getURLs();

      for(int i=0; i< urls.length; i++)
      {
          System.out.println(urls[i].getFile());
      }       
   }
}
