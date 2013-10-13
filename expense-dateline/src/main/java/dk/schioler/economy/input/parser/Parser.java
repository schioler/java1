package dk.schioler.economy.input.parser;

import dk.schioler.economy.model.Line;

public interface Parser {
	public Line parse(Long userId, String origin, String string);
	public String getEncoding();

}
