package dk.schioler.economy.main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.stereotype.Component;

import dk.schioler.economy.command.DoPatternRetrieve;
import dk.schioler.economy.expenseparser.schema.ExpenseParserConfigType;
import dk.schioler.economy.expenseparser.schema.PatternFileType;
import dk.schioler.economy.inoutput.PatternInOutputReader;
import dk.schioler.economy.util.Log4JLoader;
import dk.schioler.economy.util.MarshallHelper;
import dk.schioler.economy.util.SpringFrameworkHelper;

@Component("doPatternDumpMain")
public class DoPatternDumpMain {
   static {
      Log4JLoader.loadLog();
      Locale.setDefault(new Locale("da", "DK"));
   }

   @Autowired
   MarshallHelper marshallHelper;

   @Autowired
   DoPatternRetrieve doPatternRetrieve;

   @Autowired
   PatternInOutputReader patternFileReader;

   private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmmss");

   private void doDump(String userConfigFile) throws IOException {
      ExpenseParserConfigType userConfig = marshallHelper.loadUserConfig(userConfigFile);
      String owner = userConfig.getUser().getOwner();
      PatternFileType patternFile = userConfig.getConfig().getPatternFile();
      String suffix = patternFile.getType();

      String name = patternFile.getName();
      File file = new File(name);
      String absolutePath = file.getAbsolutePath();
      String dumpFileBase = absolutePath.substring(0, absolutePath.lastIndexOf(suffix));
      Date date = new Date();
      String dumpFile = dumpFileBase + "-dump-" + sdf.format(date) + suffix;

      Map<String, List<String>> execute = doPatternRetrieve.execute(owner);

      FileOutputStream fos = new FileOutputStream(dumpFile);
      patternFileReader.writePatterns(execute, fos);
      fos.flush();
      fos.close();

   }

   public static void main(String[] args) {
      AbstractApplicationContext springCtx = SpringFrameworkHelper.getDefaultApplicationContext();
      try {

         DoPatternDumpMain main = (DoPatternDumpMain) springCtx.getBean("doPatternDumpMain");
         main.doDump(args[0]);

      } catch (Exception e) {
         e.printStackTrace();
      } finally {
         springCtx.close();
      }
   }

}
