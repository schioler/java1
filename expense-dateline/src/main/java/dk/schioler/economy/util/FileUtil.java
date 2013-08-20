package dk.schioler.economy.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import org.apache.commons.io.IOUtils;

public class FileUtil {
   public static BufferedReader openFile(String file) {
      BufferedReader br = null;
      try {
         br = new BufferedReader(new FileReader(new File(file)));

      } catch (FileNotFoundException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      return br;
   }

   public static void closeReader(BufferedReader br) {
      IOUtils.closeQuietly(br);
   }
}
