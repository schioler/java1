package dk.schioler.economy.out.data;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import dk.schioler.economy.out.data.VisitorIsRelevantTypeInPath.OutputDataType;

public class OutputDataBuilderTest {

   @Test
   public void testGetCategories() {
      OutputDataType type = OutputDataType.TOTAL;
      assertEquals("TOTAL", type.toString());
   }

}
