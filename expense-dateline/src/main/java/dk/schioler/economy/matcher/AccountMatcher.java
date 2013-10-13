package dk.schioler.economy.matcher;

import dk.schioler.economy.model.Line;
import dk.schioler.economy.model.Match;
import dk.schioler.economy.model.User;


public interface AccountMatcher {
   public void init(User user);
   public Match matchText(User user, Line line);
}
