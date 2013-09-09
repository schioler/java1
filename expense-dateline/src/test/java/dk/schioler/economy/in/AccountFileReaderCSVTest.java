package dk.schioler.economy.in;

import static org.junit.Assert.*;

import java.io.File;

import org.apache.log4j.Logger;
import org.junit.Test;

import dk.schioler.economy.AccountTreeRoot;
import dk.schioler.economy.file.AccountFileReaderFile;
import dk.schioler.economy.util.Log4JLoader;
import dk.schioler.economy.visitor.VisitorLogTree;

public class AccountFileReaderCSVTest {
   private static final Logger LOG = Logger.getLogger(AccountFileReaderCSVTest.class);
   static {
      Log4JLoader.loadLog();
   }

   @Test
   public void test() {
      try {
         String file = "src/main/doc/account-list.csv";
         AccountFileReaderFile reader = new AccountFileReaderFile();
         AccountTreeRoot buildAccountStructure = reader.buildAccountStructure(new File(file));
         VisitorLogTree v = new VisitorLogTree();
         buildAccountStructure.accept(v);
      } catch (Exception e) {
         LOG.error(e.getMessage(), e);
         fail(e.getMessage());
      }
   }

}
