package dk.schioler.economy.out.data.text;

import dk.schioler.economy.out.data.VisitorIsRelevantTypeInPath.OutputDataType;

public class OutputTextDataBuilderAll extends OutputTextDataBuilderBase {

   @Override
   public OutputDataType getTargetType() {
      return OutputDataType.ALL;
   }
   //
   //   @Override
   //   public void instantiateValueTable() {
   //      if (accountGrid != null && accountGrid.length > 0) {
   //         values = new Account[accountGrid.length][accountGrid[0].length];
   //      } else {
   //         values = new Account[0][0];
   //      }
   //   }
   //
   //   @Override
   //   public Object getValue(Account a) {
   //      return a;
   //   }

}
