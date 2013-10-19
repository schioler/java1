package dk.schioler.economy.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.stereotype.Component;

import dk.schioler.economy.command.DoPatternInsert;
import dk.schioler.economy.expenseparser.schema.ExpenseParserConfigType;
import dk.schioler.economy.inoutput.FactoryPatternInOutputReader;
import dk.schioler.economy.inoutput.PatternInOutputReader;
import dk.schioler.economy.util.Log4JLoader;
import dk.schioler.economy.util.MarshallHelper;
import dk.schioler.economy.util.SpringFrameworkHelper;

@Component("doPatternInsertMain")
public class DoPatternInsertMain {
   static {
      Log4JLoader.loadLog();
      Locale.setDefault(new Locale("da", "DK"));
   }

   @Autowired
   MarshallHelper marshallHelper;

   @Autowired
   DoPatternInsert doPatternInsert;

   @Autowired
   FactoryPatternInOutputReader factoryPatternInputReader;

   private void doIt(String configFile) throws IOException {
      ExpenseParserConfigType userConfig = marshallHelper.loadUserConfig(configFile);

      String owner = userConfig.getUser().getOwner();
      String patternFileName = userConfig.getConfig().getPatternFile().getName();
      String type = userConfig.getConfig().getPatternFile().getType();

      FileInputStream fis = null;
      try {
         fis = new FileInputStream(new File(patternFileName));
         // account.getFullPath, Patterns*
         PatternInOutputReader patternInOutputReader = factoryPatternInputReader.getPatternInOutputReader(type);
         Map<String, List<String>> readPatterns = patternInOutputReader.readPatterns(fis);
         doPatternInsert.doPatternInsert(readPatterns, owner);
      } finally {
         IOUtils.closeQuietly(fis);
      }

   }

   public static void main(String[] args) {
      AbstractApplicationContext springCtx = SpringFrameworkHelper.getDefaultApplicationContext();
      try {

         DoPatternInsertMain main = (DoPatternInsertMain) springCtx.getBean("doPatternInsertMain");
         main.doIt(args[0]);

      } catch (Exception e) {
         e.printStackTrace();
      } finally {
         springCtx.close();
      }
   }
}
