package dk.schioler.economy.matcher;

import dk.schioler.economy.model.User;


public interface AccountMatcher {
   public void init(User user);
   public long matchText(String text);
}
