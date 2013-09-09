package dk.schioler.economy;

import static org.junit.Assert.fail;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;

import dk.schioler.economy.file.AccountFileReaderFile;
import dk.schioler.economy.model.Account;
import dk.schioler.economy.model.Pattern;
import dk.schioler.economy.util.Log4JLoader;
import dk.schioler.economy.visitor.Visitor;
import dk.schioler.economy.visitor.VisitorLogTree;

public class AccountTreeRootTest {
   private static final Logger LOG = Logger.getLogger(AccountTreeRootTest.class);
   static {
      Log4JLoader.loadLog();
   }

   @Test
   public void test() {
      try {
         String file = "src/test/resources/konto-liste-src-export.csv";
         File f = new File(file);
         AccountFileReaderFile reader = new AccountFileReaderFile();
         AccountTreeRoot accountTreeRoot = reader.buildAccountStructure(f);
         Visitor v = new VisitorLogTree();
         accountTreeRoot.accept(v);

         List<Pattern> readPatternMatches = reader.readPatternMatches(f);
         for (Pattern patternMatch : readPatternMatches) {

            Account findAccountOnNamePath = accountTreeRoot.findAccountOnNamePath(patternMatch.getAccountPath());
            LOG.debug(patternMatch);
            LOG.debug(findAccountOnNamePath);
         }


      } catch (Exception e) {
         LOG.error(e.getMessage(), e);
         fail(e.getMessage());
      }
   }

}
