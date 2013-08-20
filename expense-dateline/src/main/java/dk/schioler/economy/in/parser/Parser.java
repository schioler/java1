package dk.schioler.economy.in.parser;

import dk.schioler.economy.Line;

public interface Parser {
	public Line parse(String owner, String origin, String string);

}
