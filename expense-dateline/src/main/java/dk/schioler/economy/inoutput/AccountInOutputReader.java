package dk.schioler.economy.inoutput;

import java.io.InputStream;

import dk.schioler.economy.AccountTreeRoot;

public interface AccountInOutputReader {

   public AccountTreeRoot readAccounts(InputStream file);

}
