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

public class PatternFileODSTest {

   @Test
   public void testReadPatterns() {
      try {
         File file = new File("src/test/resources/pattern-fileread/test-patterns.ods");
         FileInputStream fis = new FileInputStream(file);
         PatternInOutputReaderODS patternFileODS = new PatternInOutputReaderODS();
         Map<String, List<String>> accountsOnPatterns = patternFileODS.readPatterns(fis);

         for (Entry<String, List<String>> e : accountsOnPatterns.entrySet()) {
            System.out.println(e.getKey() + "=" + e.getValue());
         }

         String teaterKey = "Fritid/Underholdning/teater";
         List<String> patterns = accountsOnPatterns.get(teaterKey);
         System.out.println(patterns);
         assertNotNull(patterns);
         assertEquals(2, patterns.size());
         assertTrue(patterns.contains("NBT.DK"));
         assertTrue(patterns.contains("GILLELEJE HALLEN"));

         String barKey = "Rådighed/Mad/Cafe + Bar";
         patterns = accountsOnPatterns.get(barKey);
         System.out.println(patterns);
         assertNotNull(patterns);
         assertEquals(35, patterns.size());
         assertTrue(patterns.contains("GRANOLA"));
         assertTrue(patterns.contains("KAFFEPLANTAGEN - V"));

         String mKey = "Bolig/Indkøb/Møbler";
         patterns = accountsOnPatterns.get(mKey);
         System.out.println(patterns);
         assertNotNull(patterns);
         assertEquals(3, patterns.size());
         assertTrue(patterns.contains(" ILVA "));
         assertTrue(patterns.contains("IKEA"));
      } catch (Exception e) {
         e.printStackTrace();
         fail(e.getMessage());
      }
   }

   @Test
   public void testReadPatternsDuplicateKey() {
      File file = new File("src/test/resources/pattern-fileread/test-patterns-duplicate-key.ods");
      PatternInOutputReaderODS patternFileODS = new PatternInOutputReaderODS();
      try {
         FileInputStream fis = new FileInputStream(file);
         patternFileODS.readPatterns(fis);
         fail("duplicatekey exception should have been thrown");
      } catch (ExpensePatternDuplicateAccountKeyException dke) {
         System.out.println("caught expected exception=" + dke.getMessage());
      } catch (Exception e) {
         e.printStackTrace();
         fail("caught un-expected exception=" + e.getMessage());
      }

   }


}
