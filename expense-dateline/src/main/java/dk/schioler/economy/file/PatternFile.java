package dk.schioler.economy.file;

import java.io.File;
import java.util.List;
import java.util.Map;

import dk.schioler.economy.model.Account;
import dk.schioler.economy.model.User;

public interface PatternFile {
   public void dumpPatternsTo(User user, List<Account> leafAccounts, File file);

   public Map<String, List<String>> readPatterns(File file);

   public String getUsername();
}
