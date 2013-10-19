package dk.schioler.economy.matcher;

import dk.schioler.economy.model.Line;

public interface Filter {
   public abstract FilterResponse applyFilter(Line filter);
}
