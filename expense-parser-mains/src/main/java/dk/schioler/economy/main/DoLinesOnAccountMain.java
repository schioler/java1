package dk.schioler.economy.main;

import java.io.PrintWriter;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.springframework.context.support.AbstractApplicationContext;

import dk.schioler.economy.command.DoLinesOnAccount;
import dk.schioler.economy.model.User;
import dk.schioler.economy.persister.UserPersister;
import dk.schioler.economy.util.Log4JLoader;
import dk.schioler.economy.util.SpringFrameworkHelper;

public class DoLinesOnAccountMain {
   static {
      Log4JLoader.loadLog();
      Locale.setDefault(new Locale("da", "DK"));
   }

   private static final Logger LOG = Logger.getLogger(DoLinesOnAccountMain.class);

   public static void main(String[] args) {
      AbstractApplicationContext springCtx = SpringFrameworkHelper.getDefaultApplicationContext();
      try {

         String owner ="lars.schioler";
         UserPersister userPersister = (UserPersister) springCtx.getBean("userPersister");
         User user = userPersister.getUser(owner);

         DoLinesOnAccount doLinesOnAccount = (DoLinesOnAccount) springCtx.getBean("doLinesOnAccount");
         PrintWriter pw = new PrintWriter(System.out);
         doLinesOnAccount.setWriter(pw);
         doLinesOnAccount.setAccountFullPath("/Rådighed");
         doLinesOnAccount.setUser(user);
         doLinesOnAccount.execute();



      } catch (Exception e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      } finally {
         springCtx.close();
      }
   }

}
