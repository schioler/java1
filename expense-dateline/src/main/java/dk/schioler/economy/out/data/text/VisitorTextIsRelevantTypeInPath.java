package dk.schioler.economy.out.data.text;

import dk.schioler.economy.model.Account;
import dk.schioler.economy.out.data.VisitorIsRelevantTypeInPath;

public class VisitorTextIsRelevantTypeInPath implements VisitorIsRelevantTypeInPath {

   final private OutputDataType targetType;
   private boolean isPresent = false;

   public VisitorTextIsRelevantTypeInPath(OutputDataType targetType) {
      super();
      this.targetType = targetType;
   }

   @Override
   public boolean visit(Account element) {
      boolean rv = true;
      if (OutputDataType.ALL.equals(targetType)) {
         isPresent = true;
         rv = false;
      }
      return rv;
   }

   @Override
   public boolean init() {
      isPresent = false;
      return false;
   }

   public OutputDataType getTargetType() {
      return targetType;
   }

   public boolean isPresent() {
      return isPresent;
   }

}
