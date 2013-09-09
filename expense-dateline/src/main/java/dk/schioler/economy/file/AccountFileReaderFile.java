package dk.schioler.economy.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import dk.schioler.economy.AccountTreeRoot;
import dk.schioler.economy.ExpenseException;
import dk.schioler.economy.model.Account;
import dk.schioler.economy.model.Pattern;
import dk.schioler.economy.util.FileUtil;

public class AccountFileReaderFile implements AccountFile {
   private static final Logger LOG = Logger.getLogger(AccountFileReaderFile.class);
   public static final String COLUMN_SEPARATOR = ";";

   public String getUserName() {
      throw new ExpenseException("not implemented...");
   }

   public AccountTreeRoot buildAccountStructure(File file) {
      AccountTreeRoot root = new AccountTreeRoot();
      BufferedReader br = null;

      Account currentParent = null;
      Account currentAccount = null;

      try {
         br = FileUtil.openFile(file.getAbsolutePath());
         String line = br.readLine();
         // first line: read count until avg and regular -> denotes how many
         // levels possible
         int maxAccountDepth = 0;
         String[] firstLine = line.split(COLUMN_SEPARATOR);
         for (int i = 0; i < firstLine.length; i++) {
            if ("AVG".equalsIgnoreCase(firstLine[i])) {
               maxAccountDepth = i;
               break;
            }
         }
         // read the accounts:
         line = br.readLine();
         while (line != null) {
            LOG.debug(line);
            String[] splitted = line.split(COLUMN_SEPARATOR);
            boolean isAccountLine = isAccountLine(splitted, maxAccountDepth);
            if (isAccountLine) {
               LOG.debug(Arrays.toString(splitted));

               for (int i = 0; i < splitted.length && i < maxAccountDepth; i++) {
                  if (StringUtils.isNotBlank(splitted[i])) {
                     int level = i;
                     String avg;
                     if (splitted.length >= maxAccountDepth) {
                        avg = splitted[maxAccountDepth];
                     } else {
                        avg = "y";
                     }
                     String regular;
                     if (splitted.length >= maxAccountDepth + 1) {
                        regular = splitted[maxAccountDepth + 1];
                     } else {
                        regular = "n";
                     }
                     String name = splitted[i];
                     String path = null;

                     if (level == 0) {
                        path = "";
                     } else if (level > 0) {
                        if (currentParent != null) {
                           if (currentParent.getLevel() > level) {
                              path = currentParent.getFullPath();
                           } else if (currentParent.getLevel() == level) {
                              path = currentParent.getPath();
                           }
                        }
                     }

                     currentAccount = new Account(null, null, null, name, path, level, "y".equalsIgnoreCase(avg), "y".equalsIgnoreCase(regular), null);
                     LOG.debug("Account=" + currentAccount + ", curParent=" + currentParent);
                     root.addAccountToTree(currentAccount);

                     // Leaving this line, prepare for next
                     // update parent:
                     if (level == 0) {
                        LOG.debug("level==0, setting curParent=" + currentAccount);
                        currentParent = currentAccount;
                     } else if (level > 0) {
                        LOG.debug("level>0, currentParent=" + currentParent);
                        if (currentParent != null) {
                           if (currentParent.getLevel() > level) {
                              currentParent = currentAccount;
                           } else if (currentParent.getLevel() == level) {
                              currentParent = currentAccount;
                           } else if (currentParent.getLevel() < level) {
                              // just leave it, as it is the right parent

                           }
                        }
                     }

                     break;
                  }

               }

            }

            line = br.readLine();
         }
      } catch (IOException e) {

         e.printStackTrace();
      } finally {
         FileUtil.closeReader(br);
      }
      return root;
   }

   private boolean isAccountLine(String[] line, int maxAccountDepth) {
      boolean isLine = false;
      if (line.length > 0) {
         for (int i = 0; i < line.length && i < maxAccountDepth; i++) {
            if (StringUtils.isNotBlank(line[i])) {
               isLine = true;
               break;
            }
         }
      } else {
         isLine = false;
      }
      return isLine;
   }

   public List<Pattern> readPatternMatches(File file) {
      List<Pattern> patterns = new ArrayList<Pattern>();
      BufferedReader br = null;

      String categoryId = null;
      String groupId = null;
      String accountId = null;

      try {
         br = FileUtil.openFile(file.getAbsolutePath());
         String line = br.readLine();

         while (line != null) {
            LOG.debug(line);
            String[] splitted = line.split(",");
            boolean isAccountLine = isAccountLine(splitted, 0);
            if (isAccountLine) {

               String name = splitted[3];
               if (StringUtils.isNotBlank(splitted[0])) {
                  // Category Account
                  categoryId = name;
                  groupId = null;
                  accountId = null;
               } else if (StringUtils.isNotBlank(splitted[1])) {
                  // Group Account
                  groupId = name;
                  accountId = null;
               } else if (StringUtils.isNotBlank(splitted[2])) {
                  accountId = name;

                  for (int i = 4; i < splitted.length; i++) {
                     if (StringUtils.isNotBlank(splitted[i])) {
                        String path = categoryId + Account.PATH_SEPARATOR + groupId + Account.PATH_SEPARATOR + accountId;
                        Pattern pm = new Pattern(null, null, splitted[i], path);
                        patterns.add(pm);
                     }
                  }
               }

            }

            line = br.readLine();
         }
      } catch (IOException e) {

         e.printStackTrace();
      } finally {
         FileUtil.closeReader(br);
      }
      return patterns;
   }

}
