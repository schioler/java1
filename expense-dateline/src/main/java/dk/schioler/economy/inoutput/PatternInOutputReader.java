package dk.schioler.economy.inoutput;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

public interface PatternInOutputReader {
   public void writePatterns(Map<String, List<String>> patternMap, OutputStream outStream);

   //   public void writePatternsTo(Map<String, List<String>> patterns, File file);

   public Map<String, List<String>> readPatterns(InputStream is);
}
