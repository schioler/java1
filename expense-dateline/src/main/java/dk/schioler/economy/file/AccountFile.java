package dk.schioler.economy.file;

import java.io.File;

import dk.schioler.economy.AccountTreeRoot;

public interface AccountFile {
   public String getUserName();
   public AccountTreeRoot buildAccountStructure(File file);

}
