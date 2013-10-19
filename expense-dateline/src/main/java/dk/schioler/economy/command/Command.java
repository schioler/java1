package dk.schioler.economy.command;

import dk.schioler.economy.expenseparser.schema.ExpenseParserConfigType;

public interface Command {

   public abstract void execute();

   public abstract void setUserInput(ExpenseParserConfigType userInput);

}