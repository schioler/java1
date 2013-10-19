package dk.schioler.economy.out.data;

import dk.schioler.economy.visitor.Visitor;

public interface VisitorIsRelevantTypeInPath extends Visitor {
   public enum OutputDataType {
      REGULAR, NONREGULAR, EXTRAORDINAIRE, TOTAL, ALL
   }

   public OutputDataType getTargetType() ;

   public boolean isPresent() ;

}
