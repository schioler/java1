package dk.schioler.economy.in;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.jopendocument.dom.spreadsheet.Sheet;
import org.jopendocument.dom.spreadsheet.SpreadSheet;
import org.junit.Test;

import dk.schioler.economy.AccountTreeRoot;
import dk.schioler.economy.file.AccountFileReaderODS;
import dk.schioler.economy.util.Log4JLoader;
import dk.schioler.economy.visitor.VisitorLogTree;

public class AccountFileReaderODSTest {
   static {
      Log4JLoader.loadLog();
   }

   @Test
   public void testBuildAccountStructure() {
      try {
         AccountFileReaderODS accReader = new AccountFileReaderODS();

         String fileName = "src/test/resources/account-list/account-list.ods";
         AccountTreeRoot buildAccountStructure = accReader.buildAccountStructure(new File(fileName));
         VisitorLogTree v = new VisitorLogTree();
         buildAccountStructure.accept(v);

      } catch (Exception e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

   }

}
