package dk.schioler.economy.file;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.junit.Test;

import dk.schioler.economy.inoutput.ExpensePatternDuplicateAccountKeyException;
import dk.schioler.economy.inoutput.PatternInOutputReaderODS;

public class PatternWriteODSTest {



   @Test
   public void testWritePatterns() {
      File file = new File("src/test/resources/pattern-fileread/test-write-patterns.ods");
      PatternInOutputReaderODS patternFileODS = new PatternInOutputReaderODS();
      Map<String, List<String>> patternMap = new TreeMap<String, List<String>>();

      String[] patterns1 = { "Boligtest" };
      String[] patterns2 = { "Festtest2", "teterrt" };
      String[] patterns3 = { "Dillertes", "sfdgf", "asesse", "fgfg" };
      String[] patterns4 = { "Diller/Tøstest" };
      String[] patterns5 = { "Mulletest", "kost", "pest", "slet", "tuk" };
      String[] patterns6 = { "Sweattest", "pillll", "eeeffing" };
      String[] patterns7 = { "Sqtrettest", "suk" };
      String[] patterns8 = { "TKtest", "uioyui" };
      String[] patterns9 = { "PVtest", "ymmuii", "tommy" };
      String[] patterns10 = { "AQWtest", "polllen", "seleri" };

      patternMap.put("/Bolig1", Arrays.asList(patterns1));
      patternMap.put("/Bolig1/Fest2", Arrays.asList(patterns2));
      patternMap.put("/Bolig1/Diller4", Arrays.asList(patterns3));
      patternMap.put("/Bolig1/Diller4/Tøs1", Arrays.asList(patterns4));
      patternMap.put("/Bil/Mulle5", Arrays.asList(patterns5));
      patternMap.put("/Bil/Sweat3", Arrays.asList(patterns6));
      patternMap.put("/Rådighed/Sqt2", Arrays.asList(patterns7));
      patternMap.put("/Rådighed/TK2", Arrays.asList(patterns8));
      patternMap.put("/Øvrige/PV3", Arrays.asList(patterns9));
      patternMap.put("/Øvrige/AQW3", Arrays.asList(patterns10));

      try {
         FileOutputStream fos = new FileOutputStream(file);
         patternFileODS.writePatterns(patternMap, fos);
         fos.flush();
         fos.close();
      } catch (Exception e) {
         e.printStackTrace();
         fail("caught un-expected exception=" + e.getMessage());
      }

   }
}
