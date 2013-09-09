package dk.schioler.economy.file;

import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.Stack;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jopendocument.dom.spreadsheet.Cell;
import org.jopendocument.dom.spreadsheet.Range;
import org.jopendocument.dom.spreadsheet.Sheet;
import org.jopendocument.dom.spreadsheet.SpreadSheet;

import dk.schioler.economy.AccountTreeRoot;
import dk.schioler.economy.ExpenseException;
import dk.schioler.economy.model.Account;

public class AccountFileReaderODS implements AccountFile {
   private static final Logger LOG = Logger.getLogger(AccountFileReaderODS.class);

   private String username;

   public String getUserName() {
      return username;
   }

   public AccountTreeRoot buildAccountStructure(File file) {
      int avgIdx = -1;
      int regularIdx = -1;
      AccountTreeRoot root = new AccountTreeRoot();
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

         for (int x = startPoint.x; x <= endPoint.x; x++) {
            Cell<SpreadSheet> cell = sheet.getImmutableCellAt(x, startPoint.y);

            // LOG.debug("x=" + x + ", y= " + startPoint.y + ", val=" +
            // cell.getValue());
            if ("AVG".equalsIgnoreCase(cell.getTextValue()) || "GNSNT".equalsIgnoreCase(cell.getTextValue())) {
               avgIdx = x;
            }
            if ("REGULAR".equalsIgnoreCase(cell.getTextValue()) || "FAST".equalsIgnoreCase(cell.getTextValue())) {
               regularIdx = x;
            }

         }
         int maxAccountCol = Math.min(avgIdx, regularIdx);
         LOG.debug("avgIdx=" + avgIdx + "; regularIdx=" + regularIdx);
         Stack<Account> stack = new Stack<Account>();
         for (int y = startPoint.y + 1; y <= endPoint.y; y++) {
            for (int x = startPoint.x; x < maxAccountCol; x++) {
               Cell<SpreadSheet> cell = sheet.getImmutableCellAt(x, y);
               // LOG.debug("x=" + x + ", y= " + y + ", val=" +
               // cell.getValue());
               String value = cell.getTextValue();
               if (StringUtils.isNotBlank(value)) {
                  boolean useInAvg = true;
                  String avgTxt = sheet.getImmutableCellAt(avgIdx, y).getTextValue();
                  if ("n".equalsIgnoreCase(avgTxt)) {
                     useInAvg = false;
                  }

                  boolean useInRegular = false;
                  String regTxt = sheet.getImmutableCellAt(regularIdx, y).getTextValue();
                  if ("y".equalsIgnoreCase(regTxt)) {
                     useInRegular = true;
                  }
                  if (value.contains("/")) {
                     throw new ExpenseException("Forward slash is not allowed as part of accountname:" + value);
                  }

                  if (stack.isEmpty()) {
                     Account a = new Account(null, null, null, value, "", x, useInAvg, useInRegular, null);
                     root.addAccountToTree(a);
                     stack.push(a);
                  } else {
                     Account peek = stack.peek();
                     // LOG.debug(peek);
                     if (peek.getLevel() + 1 == x) {
                        // peekaccount is parent
                        Account a = new Account(null, null, null, value, peek.getFullPath(), x, useInAvg, useInRegular, null);
                        root.addAccountToTree(a);
                        stack.push(a);
                     } else if (peek.getLevel() == x) {
                        // same level - replace peek in stack
                        Account a = new Account(null, null, null, value, peek.getPath(), x, useInAvg, useInRegular, null);
                        root.addAccountToTree(a);
                        stack.pop();
                        stack.push(a);
                     } else if (peek.getLevel() > x) {
                        // pop accounts until last pop'ed matches x level
                        Account pop = null;
                        int diff = peek.getLevel() - x;
                        for (int i = 0; i <= diff; i++) {
                           pop = stack.pop();
                        }
                        Account a = new Account(null, null, null, value, pop.getPath(), x, useInAvg, useInRegular, null);
                        root.addAccountToTree(a);
                        stack.push(a);
                     }

                  }

               }
            }
         }

      } catch (IOException e) {
         throw new ExpenseException(e);
      }
      return root;
   }
}
