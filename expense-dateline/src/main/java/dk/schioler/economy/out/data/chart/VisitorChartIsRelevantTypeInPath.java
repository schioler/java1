package dk.schioler.economy.out.data.chart;

import org.apache.log4j.Logger;

import dk.schioler.economy.ExpenseException;
import dk.schioler.economy.model.Account;
import dk.schioler.economy.model.Account.Type;
import dk.schioler.economy.out.data.VisitorIsRelevantTypeInPath;

public class VisitorChartIsRelevantTypeInPath implements VisitorIsRelevantTypeInPath {
   private static final Logger LOG = Logger.getLogger(VisitorChartIsRelevantTypeInPath.class);
   final private OutputDataType targetType;
   private boolean isPresent = false;

   public VisitorChartIsRelevantTypeInPath(OutputDataType targetType) {
      super();
      this.targetType = targetType;
      LOG.debug("TargeType=" + this.targetType);
   }

   @Override
   public boolean visit(Account element) {
      LOG.debug("visit at " + element.getFullPath());
      boolean rv = true;
      if (OutputDataType.ALL.equals(targetType)) {
         isPresent = true;
         rv = false;
      } else if (OutputDataType.REGULAR.equals(targetType)) {
         Type type = element.getType();
         if (Type.REGULAR.equals(type)) {
            isPresent = true;
            rv = false;
         } else {
            // keep on looking.....
         }
      } else if (OutputDataType.NONREGULAR.equals(targetType)) {
         Type type = element.getType();
         if (Type.NON_REGULAR.equals(type)) {
            isPresent = true;
            rv = false;
         } else {
            // keep on looking.....
         }
      } else if (OutputDataType.EXTRAORDINAIRE.equals(targetType)) {
         Type type = element.getType();
         if (Type.EXTRAORDINAIRE.equals(type)) {
            isPresent = true;
            rv = false;
         } else {
            // keep on looking.....
         }
      } else if (OutputDataType.TOTAL.equals(targetType)) {
         isPresent = true;
         rv = false;
      } else {
         throw new ExpenseException("un-known TargetType=" + targetType);
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
      LOG.debug("isPresent=:" + isPresent);
      return isPresent;
   }

}
