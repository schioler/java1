package dk.schioler.economy.main;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.stereotype.Component;

import dk.schioler.economy.command.DoPatternMatch;
import dk.schioler.economy.expenseparser.schema.ExpenseParserConfigType;
import dk.schioler.economy.model.Line;
import dk.schioler.economy.util.Log4JLoader;
import dk.schioler.economy.util.MarshallHelper;
import dk.schioler.economy.util.SpringFrameworkHelper;

@Component("doPatternMatchMain")
public class DoPaternMatchMain {
   static {
      Log4JLoader.loadLog();
      Locale.setDefault(new Locale("da", "DK"));
   }

   static Logger LOG = Logger.getLogger(DoPaternMatchMain.class);

   @Autowired
   MarshallHelper marshallHelper;

   @Autowired
   DoPatternMatch doPatternMatch;

   public void doIt(String configFile) throws IOException {
      ExpenseParserConfigType userConfig = marshallHelper.loadUserConfig(configFile);

      List<Line> linesUnMatched2 = doPatternMatch.execute(userConfig.getUser().getOwner());

      int count = 0;
      for (Line line : linesUnMatched2) {
         count++;
         System.out.println(StringUtils.leftPad(String.valueOf(line.getId()), 5) + ": " + line.getDate() + ";   " + StringUtils.leftPad(line.getOrigin(), 25) + ";   "
               + StringUtils.leftPad(line.getText(), 60) + ";   " + StringUtils.leftPad(String.valueOf(line.getAmount().toPlainString()), 15));
      }
      LOG.debug("DoPatternMatch.execute(): lines unmatched.count=" + count);
   }

   public static void main(String[] args) {
      AbstractApplicationContext springCtx = SpringFrameworkHelper.getDefaultApplicationContext();
      try {

         DoPaternMatchMain main =  (DoPaternMatchMain) springCtx.getBean("doPatternMatchMain");
         main.doIt(args[0]);

      } catch (Exception e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      } finally {
         springCtx.close();
      }

   }

}
