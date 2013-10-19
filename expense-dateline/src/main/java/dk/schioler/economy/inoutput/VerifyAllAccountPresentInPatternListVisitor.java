package dk.schioler.economy.inoutput;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dk.schioler.economy.model.Account;
import dk.schioler.economy.visitor.Visitor;

public class VerifyAllAccountPresentInPatternListVisitor implements Visitor {

   Map<String, List<String>> readPatterns;
   List<String> missinngAccountsList = new ArrayList<String>();

   public VerifyAllAccountPresentInPatternListVisitor(Map<String, List<String>> readPatterns) {
      this.readPatterns = readPatterns;
   }

   @Override
   public boolean visit(Account account) {
      if (account.getChildren().size() == 0) {
         String fullPath = account.getFullPath();
         if (!readPatterns.containsKey(fullPath)) {
            missinngAccountsList.add(fullPath);
         }
      }
      return true;
   }

   @Override
   public boolean init() {
      // TODO Auto-generated method stub
      return false;
   }

   public List<String> getMissingAccountsList() {
      return missinngAccountsList;
   }


}
