package dk.schioler.economy.inoutput;

public interface FactoryPatternInOutputReader {
   public PatternInOutputReader getPatternInOutputReader(String type);
}
