package dk.schioler.economy.command;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import dk.schioler.economy.ExpenseException;
import dk.schioler.economy.input.parser.Parser;
import dk.schioler.economy.model.Line;
import dk.schioler.economy.persister.LinePersister;

@Component("doExpenseInputCommand")
@Scope("prototype")
public class DoExpensesInputCommand extends BaseCommand {

   static Logger LOG = Logger.getLogger(DoExpensesInputCommand.class);

   @Resource
   private Map<String, Parser> parserMap;

   @Autowired
   private LinePersister linePersister;

   public List<Line> getLinesUnmatched(Long userId) {
      return linePersister.getLinesUnMatched(userId);
   }

   public void persistInput(InputStream input, String owner, String origin) {
      lookupUser(owner);
      Parser parser = parserMap.get(origin);
      BufferedReader br = null;

      try {

         InputStreamReader isr = new InputStreamReader(input, parser.getEncoding());

         br = new BufferedReader(isr);

         String lineString = br.readLine();
         while (lineString != null && !("".equals(lineString))) {
//            LOG.debug("line=" + lineString);
            Line accountLine = parser.parse(user.getId(), origin, lineString);
//            LOG.debug(accountLine);

            if (accountLine != null) {
               try {
                  linePersister.persistLine(accountLine);
               } catch (org.springframework.dao.DuplicateKeyException e) {
                  // ignore - attempt to insert same line - ok to bounce
                  LOG.info("attempted to insert same line again..." + accountLine);
               }
            }
            lineString = br.readLine();
         }

      } catch (FileNotFoundException e) {
         throw new ExpenseException(e.getMessage(), e);
      } catch (IOException e) {
         throw new ExpenseException(e.getMessage(), e);
//      } finally {
//         IOUtils.closeQuietly(br);
      }

   }


}
