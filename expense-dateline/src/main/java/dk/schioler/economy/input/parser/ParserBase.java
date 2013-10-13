package dk.schioler.economy.input.parser;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.log4j.Logger;

import dk.schioler.economy.model.Line;

public abstract class ParserBase implements Parser {
   static final Logger LOG = Logger.getLogger(ParserBase.class);

   public Line parse(Long userId, String origin, String line) {
      // LOG.debug("owner=" + userId + ", origin=" + origin + ", line=" + line);
      String separator = getSeparator();
      String[] fields = line.split(separator);
      Line retVal = null;

      if (includeLineInOutput(fields)) {
         BigDecimal amount = getAmount(fields);
         String text = getText(fields);
         Date lineDate = getDate(fields);

         //         public Line(Long id, Long userId, String origin, Date date, String text, BigDecimal amount, Date timestamp, Match match) {

         retVal = new Line(null, userId, origin, lineDate, text, amount, null, null);
      }
      return retVal;
   }

   /*
    * Pt only positive numbers. There may also occur other criteria at some
    * point
    */
   protected boolean includeLineInOutput(String[] line) {
      boolean include = false;

      BigDecimal amount = getAmount(line);

      if (amount.floatValue() < 0F) {
         include = true;
      }

      return include;
   }

   protected abstract BigDecimal getAmount(String[] fields);

   protected abstract String getText(String[] fields);

   protected abstract Date getDate(String[] fields);

   protected abstract String getSeparator();

}
