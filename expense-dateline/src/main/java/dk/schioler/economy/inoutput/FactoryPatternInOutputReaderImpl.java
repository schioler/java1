package dk.schioler.economy.inoutput;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import dk.schioler.economy.ExpenseException;

@Component("factoryPatternInOutputReader")
public class FactoryPatternInOutputReaderImpl implements FactoryPatternInOutputReader {

   @Resource
   private Map<String, PatternInOutputReader> patternInOutputReaderMap;

   public PatternInOutputReader getPatternInOutputReader(String type) {
      PatternInOutputReader accountFileReader = patternInOutputReaderMap.get(type);
      if (accountFileReader == null) {
         throw new ExpenseException("PatternInOutputReaderNotSupported:" + type);
      }
      return accountFileReader;
   }

}
