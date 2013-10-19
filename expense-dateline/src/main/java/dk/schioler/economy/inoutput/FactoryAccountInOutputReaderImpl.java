package dk.schioler.economy.inoutput;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import dk.schioler.economy.ExpenseException;

@Component("accountInOutputReaderFactory")
public class FactoryAccountInOutputReaderImpl implements FactoryAccountInOutputReader {

   @Resource
   private Map<String, AccountInOutputReader> accountInOutputReaderMap;

   public AccountInOutputReader getAccountInOutputReader(String type) {
      AccountInOutputReader accountInOutputReader = accountInOutputReaderMap.get(type);
      if (accountInOutputReader == null) {
         throw new ExpenseException("AccountFileReaderNotSupported:" + type);
      }
      return accountInOutputReader;
   }

}
