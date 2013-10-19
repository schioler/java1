package dk.schioler.economy.inoutput;

public interface FactoryAccountInOutputReader {
   public AccountInOutputReader getAccountInOutputReader(String type);
}
