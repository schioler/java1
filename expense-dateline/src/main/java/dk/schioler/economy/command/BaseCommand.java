package dk.schioler.economy.command;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import dk.schioler.economy.ExpenseException;
import dk.schioler.economy.expenseparser.schema.ExpenseParserConfigType;
import dk.schioler.economy.model.User;
import dk.schioler.economy.persister.UserPersister;

public abstract class BaseCommand implements Command {
   private static final Logger LOG = Logger.getLogger(BaseCommand.class);

   @Autowired
   UserPersister userPersister;

   protected User user;
   protected dk.schioler.economy.expenseparser.schema.ExpenseParserConfigType userInput;

   public BaseCommand() {
      super();
   }

   public void setUserInput(ExpenseParserConfigType userInput) {
      this.userInput = userInput;

   }

   public ExpenseParserConfigType getUserInput() {
      return userInput;
   }


   protected void lookupUser(String owner) {

      user = userPersister.getUser(owner);

      if (user == null) {
         throw new ExpenseException("Could not find a user by the name of '" + owner + "'");
      }
   }

   @Override
   public void execute() {
      LOG.debug("Execute default called - no subClass implementation.....");

   }
}