package dk.schioler.economy.command;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dk.schioler.economy.accountparser.schema.AccountParserType;
import dk.schioler.economy.accountparser.schema.DirectoryType;
import dk.schioler.economy.accountparser.schema.FileType;
import dk.schioler.economy.accountparser.schema.InputType;
import dk.schioler.economy.in.parser.Parser;
import dk.schioler.economy.matcher.AccountMatcher;
import dk.schioler.economy.model.Line;
import dk.schioler.economy.model.User;
import dk.schioler.economy.persister.LinePersister;
import dk.schioler.economy.persister.UserPersister;
import dk.schioler.economy.util.MarshallHelper;

@Component("doInput")
public class DoExpensesInput {

   static Logger LOG = Logger.getLogger(DoExpensesInput.class);

   @Autowired
   private MarshallHelper marshallHelper;

   /*
    * Holds parsers for all known origins.
    */
   @Resource
   private Map<String, Parser> parserMap;

   @Autowired
   private AccountMatcher accountMatcher;

   @Autowired
   private LinePersister linePersister;

   @Autowired
   private UserPersister userPersister;

   // Data from user.
   private AccountParserType accountParserUserConfig;
   // private String owner;
   private User owner;
   private List<FileType> inputFiles = new ArrayList<FileType>();

   public void readUserFile(String userFile) throws IOException {
      accountParserUserConfig = marshallHelper.loadUserConfig(userFile);
      String lowner = accountParserUserConfig.getOwner();
      owner = userPersister.getUser(lowner);
   }

   public void establishInputFiles() {
      InputType input = accountParserUserConfig.getInput();
      List<FileType> list = input.getFile();
      for (FileType fileType : list) {
         File f = new File(fileType.getPath());
         if (f.isFile()) {
            this.inputFiles.add(fileType);
            LOG.debug("added " + fileType.getPath() + " to fileList");
         }
      }

      List<DirectoryType> dirList = input.getDirectory();
      for (DirectoryType dirType : dirList) {
         String path = dirType.getPath();
         File javaFile = new File(path);
         if (javaFile.isDirectory()) {
            String origin = dirType.getOrigin();
            File[] listFiles = javaFile.listFiles();
            for (int i = 0; i < listFiles.length; i++) {
               FileType fileType = new FileType();
               fileType.setOrigin(origin);
               fileType.setPath(listFiles[i].getAbsolutePath());
               inputFiles.add(fileType);
               LOG.debug("added " + fileType.getPath() + " to fileList");
            }
         }
      }
   }

   public void persistFiles() {
      linePersister.clearUnmatchedLines();
      accountMatcher.init(owner);
      for (FileType fileType : this.inputFiles) {
         persistFile(fileType);
      }
   }

   private void persistFile(FileType fileType) {
      File file = new File(fileType.getPath());
      LOG.debug(parserMap);
      Parser parser = parserMap.get(fileType.getOrigin());
      BufferedReader br = null;

      try {

         br = new BufferedReader(new FileReader(file));
         String lineString = br.readLine();

         while (lineString != null && !("".equals(lineString))) {
            LOG.debug("line=" + lineString);
            Line accountLine = parser.parse(getOwner().getId(), fileType.getOrigin(), lineString);
            LOG.debug(accountLine);

            if (accountLine != null) {
               long accountId = accountMatcher.matchText(accountLine.getText());

               if (accountId > 0) {
                  try {
                     accountLine = new Line(-1, accountId, accountLine.getUserId(), accountLine.getOrigin(), accountLine.getDate(),
                           accountLine.getText(), accountLine.getAmount(), accountLine.getTimestamp());
                     linePersister.persistLine(accountLine);
                  } catch (org.springframework.dao.DuplicateKeyException e) {
                     // ignore - attempt to insert same line - ok to bounce
                     LOG.info("attempted to insert same line again..." + accountLine);
                  }
               } else {

                  linePersister.persistUnMatchedLine(accountLine);
               }
            }
            lineString = br.readLine();
         }
      } catch (FileNotFoundException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      } catch (IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      } finally {
         IOUtils.closeQuietly(br);
      }

   }

   public User getOwner() {
      return owner;
   }

}
