package dk.schioler.economy.out.text;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import dk.schioler.economy.ExpenseException;

@Component("textOutputFactory")
public class FactoryTextOutputImpl implements FactoryTextOutput {

   @Resource
   private Map<String, TextOutput> textOutputterMap;

   public TextOutput getTextOutput(String type) {
      TextOutput textOutput = textOutputterMap.get(type);
      if (textOutput == null) {
         throw new ExpenseException("Unknown TextOutputter:" + type);
      }
      return textOutput;
   }

}
