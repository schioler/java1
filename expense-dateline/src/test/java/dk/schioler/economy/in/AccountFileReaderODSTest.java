package dk.schioler.economy.in;

import java.io.File;
import java.io.FileInputStream;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import static org.junit.Assert.*;

import org.springframework.context.support.AbstractApplicationContext;

import dk.schioler.economy.AccountTreeRoot;
import dk.schioler.economy.inoutput.AccountInOutputReaderODS;
import dk.schioler.economy.model.Account;
import dk.schioler.economy.model.User;
import dk.schioler.economy.util.Log4JLoader;
import dk.schioler.economy.util.SpringFrameworkHelper;
import dk.schioler.economy.visitor.VisitorLogTree;

public class AccountFileReaderODSTest {
   static {
      Log4JLoader.loadLog();
   }

   @Test
   public void testBuildAccountStructure() {
      FileInputStream fis= null;
//      AbstractApplicationContext defaultApplicationContext = SpringFrameworkHelper.getDefaultApplicationContext();
      try {
         AccountInOutputReaderODS accReader = new AccountInOutputReaderODS();

         String fileName = "src/test/resources/account-list/account-list-test.ods";
         fis = new FileInputStream(new File(fileName));
         AccountTreeRoot root = accReader.readAccounts(fis );
         VisitorLogTree v = new VisitorLogTree();
         root.accept(v);

         String huslejeString = "/Bolig/Fællesskab/Husleje";
         String transBilAnskaf = "/Transport/Bil/Anskaffelse";
         String rådigMadSuper = "/Rådighed/Mad/Supermarked";
         String rådig = "/Rådighed";
         Account huslejeAccount = root.findAccountOnFullPath(huslejeString);
         assertNotNull(huslejeAccount);
         assertEquals(true, huslejeAccount.isRegular());
         assertEquals(3, huslejeAccount.getLevel());

         Account bilAnskafAccount = root.findAccountOnFullPath(transBilAnskaf);
         assertNotNull(bilAnskafAccount);
         assertEquals(true, bilAnskafAccount.isExtra());
         assertEquals(3, huslejeAccount.getLevel());

         Account superMrktAccount = root.findAccountOnFullPath(rådigMadSuper);
         assertNotNull(superMrktAccount);
         assertEquals(true, superMrktAccount.isNonRegular());
         assertEquals(3, huslejeAccount.getLevel());

         Account raadighed = root.findAccountOnFullPath(rådig);
         assertNotNull(raadighed);
         assertEquals(true, raadighed.isNonRegular());
         assertEquals(1, raadighed.getLevel());

      } catch (Exception e) {
         e.printStackTrace();
         fail(e.getMessage());
      } finally {
         IOUtils.closeQuietly(fis);
      }

   }

}
