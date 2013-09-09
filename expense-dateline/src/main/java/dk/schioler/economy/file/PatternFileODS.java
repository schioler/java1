package dk.schioler.economy.file;

import java.awt.Color;
import java.awt.Point;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jopendocument.dom.spreadsheet.Cell;
import org.jopendocument.dom.spreadsheet.Column;
import org.jopendocument.dom.spreadsheet.MutableCell;
import org.jopendocument.dom.spreadsheet.Range;
import org.jopendocument.dom.spreadsheet.Sheet;
import org.jopendocument.dom.spreadsheet.SpreadSheet;
import org.springframework.stereotype.Component;

import dk.schioler.economy.ExpenseException;
import dk.schioler.economy.model.Account;
import dk.schioler.economy.model.Pattern;
import dk.schioler.economy.model.User;

@Component("patternFile")
public class PatternFileODS implements PatternFile {
   private static final Logger LOG = Logger.getLogger(PatternFileODS.class);

   public void dumpPatternsTo(User user, List<Account> leafAccounts, File file) {
      try {

         int countDataRows = leafAccounts.size();
         int countHeaderRows = 1;
         int countDataColumns = 0;
         int countPathColumns = 1;

         int maxLengthPath = 0;
         for (Account account : leafAccounts) {
            countDataColumns = Math.max(countDataColumns, account.getPatterns().size());
            maxLengthPath = Math.max(maxLengthPath, account.getFullPath().length());
         }
         // countColumns++;
         countDataColumns = Math.max(countDataColumns, 10);

         LOG.debug("countDateColumns=" + countDataColumns + ", dataRows=" + countDataRows);
         Collections.sort(leafAccounts);

         SpreadSheet spreadSheet = SpreadSheet.create(1, (countDataColumns + countPathColumns), (countDataRows + countHeaderRows));

         Sheet sheet = spreadSheet.getFirstSheet();
         sheet.ensureColumnCount((countDataColumns + countPathColumns));
         sheet.ensureRowCount((countDataRows + countHeaderRows));
         sheet.setName("patterns");

         String currentRoot = null;
         Color rootBgColor = Color.YELLOW;
         for (int y = 0; y < (countDataRows + countHeaderRows); y++) {
            if (y == 0) {
               MutableCell<SpreadSheet> userCell = sheet.getCellAt(0, y);
               userCell.setValue(user.getName());
            } else if (y > 0) {
               int accountIdx = y - 1;
               // skipping first row, used for username etc. Handled
               // elsewhere....
               Account currentAccount = leafAccounts.get(accountIdx);
               String fullPath = currentAccount.getFullPath();
               boolean setRootBg = false;

               if ((currentRoot == null) || (!fullPath.startsWith(currentRoot))) {
                  setRootBg = true;
                  currentRoot = fullPath.split(Account.PATH_SEPARATOR)[0];
               }

               List<Pattern> patterns = currentAccount.getPatterns();
               LOG.debug(currentAccount.getFullPath() + ", patterns=" + patterns);

               for (int x = 0; x < (countDataColumns + countPathColumns); x++) {
                  MutableCell<SpreadSheet> cell = sheet.getCellAt(x, y);
                  Column<SpreadSheet> column = sheet.getColumn(x);
                  if (x == 0) {
                     column.setWidth(new Long((long) (maxLengthPath * 1.7F)));

                     cell.setValue(fullPath);
                     if (setRootBg) {
                        cell.setBackgroundColor(rootBgColor);
                     }
                  } else if (x > 0) {
                     column.setWidth(new BigDecimal("40"));

                     if (x <= patterns.size()) {
                        Pattern pattern = patterns.get(x - 1);
                        String patternValue = pattern.getPattern();
                        LOG.debug("patternValue=" + patternValue);
                        if (!StringUtils.isBlank(patternValue)) {
                           cell.setValue(patternValue);
                        }
                     }
                     if (setRootBg) {
                        cell.setBackgroundColor(rootBgColor);
                     } else {
                        if (1 == (y % 3)) {
                           cell.setBackgroundColor(Color.LIGHT_GRAY);
                        }
                     }

                  }
               }
               setRootBg = false;
            }
         }
         sheet.getSpreadSheet().saveAs(file);

      } catch (Exception e) {
         LOG.error(e.getMessage(), e);
         throw new ExpenseException(e);
      }
   }

   public Map<String, List<String>> readPatterns(File file) {
      Map<String, List<String>> retVal = new TreeMap<String, List<String>>();

      try {
         final Sheet sheet = SpreadSheet.createFromFile(file).getFirstSheet();
         Range usedRange = sheet.getUsedRange();
         Point startPoint = usedRange.getStartPoint();
         Point endPoint = usedRange.getEndPoint();

         Cell<SpreadSheet> usernameCell = sheet.getImmutableCellAt(startPoint.x, startPoint.y);
         username = usernameCell.getTextValue();
         if (StringUtils.isBlank(username)) {
            throw new ExpenseException("username must be present in cell A1.");
         }
         LOG.info("found username=" + username);

         String key = null;
         List<String> patterns = new ArrayList<String>();
         for (int y = startPoint.y + 1; y <= endPoint.y; y++) {
            for (int x = startPoint.x; x < endPoint.x; x++) {
               Cell<SpreadSheet> cell = sheet.getImmutableCellAt(x, y);
               String textValue = cell.getTextValue();
               // LOG.debug(textValue);

               if (x == 0) {
                  key = textValue;
                  if (StringUtils.isBlank(key)) {
                     throw new ExpenseException("Found a first/accountpath column with empty value in file:" + file);
                  }
                  patterns = new ArrayList<String>();
                  retVal.put(key, patterns);
               } else {
                  if (StringUtils.isNotBlank(textValue)) {
                     patterns.add(textValue);
                  }

               }
            }

         }

      } catch (Exception e) {
         LOG.error(e.getMessage(), e);
         throw new ExpenseException(e);
      }
      return retVal;
   }

   String username;

   public String getUsername() {
      return username;
   }

}
