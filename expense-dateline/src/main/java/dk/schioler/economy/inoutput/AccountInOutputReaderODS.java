package dk.schioler.economy.inoutput;

import java.awt.Point;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Stack;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jopendocument.dom.ODPackage;
import org.jopendocument.dom.spreadsheet.Cell;
import org.jopendocument.dom.spreadsheet.Range;
import org.jopendocument.dom.spreadsheet.Sheet;
import org.jopendocument.dom.spreadsheet.SpreadSheet;
import org.springframework.stereotype.Component;

import dk.schioler.economy.AccountTreeRoot;
import dk.schioler.economy.ExpenseException;
import dk.schioler.economy.model.Account;
import dk.schioler.economy.model.Account.Type;

@Component
public class AccountInOutputReaderODS implements AccountInOutputReader {
   private static final Logger LOG = Logger.getLogger(AccountInOutputReaderODS.class);

   public AccountTreeRoot readAccounts(InputStream inputStream) {

      int typeIdx = -1;
      AccountTreeRoot root = new AccountTreeRoot();
      try {
         ODPackage odPackage = ODPackage.createFromStream(inputStream, null);
         SpreadSheet spreadSheet = SpreadSheet.get(odPackage);
         final Sheet sheet = spreadSheet.getFirstSheet();
         Range usedRange = sheet.getUsedRange();
         Point startPoint = usedRange.getStartPoint();
         Point endPoint = usedRange.getEndPoint();
         //         LOG.debug("startPoint=" + startPoint + ", endPoint=" + endPoint);

         for (int x = startPoint.x; x <= endPoint.x; x++) {
            Cell<SpreadSheet> cell = sheet.getImmutableCellAt(x, startPoint.y);
            // LOG.debug("x=" + x + ", y= " + startPoint.y + ", val=" +
            // cell.getValue());
            if ("TYPE".equalsIgnoreCase(cell.getTextValue())) {
               typeIdx = x;
            }
         }
         //         LOG.debug("typedx=" + typeIdx);
         if (typeIdx == -1) {
            throw new ExpenseException("Found no type column in provided spreadsheet");
         }

         int maxAccountCol = typeIdx;
         Stack<Account> stack = new Stack<Account>();
         for (int y = startPoint.y + 1; y <= endPoint.y; y++) {
            for (int x = startPoint.x; x < maxAccountCol; x++) {
               Cell<SpreadSheet> cell = sheet.getImmutableCellAt(x, y);
               //LOG.debug("x=" + x + ", y= " + y + ", val=" + cell.getValue());
               String value = cell.getTextValue();
               if (StringUtils.isNotBlank(value)) {
                  String typeStr = sheet.getImmutableCellAt(typeIdx, y).getTextValue();
                  Type type = null;
                  if (StringUtils.isBlank(typeStr)) {
                     type = Type.REGULAR;
                  } else {
                     type = Account.getType(typeStr);
                  }

                  if (value.contains("/")) {
                     throw new ExpenseException("Forward slash is not allowed as part of accountname:" + value);
                  }

                  if (stack.isEmpty()) {
                     //                     public Account(Long id, Account parent, Long userId, String name, int level, Type type, Date timestamp)
                     Account a = new Account(null, root.getRoot(), null, value, type, null);
                     LOG.debug("empty stack. adding account=" + a);
                     //                     root.addAccountToTree(a);
                     stack.push(a);
                  } else {
                     Account peek = stack.peek();
                     // LOG.debug(peek);
                     if (peek.getLevel() == x) {
                        // peekaccount is parent
                        Account a = new Account(null, peek, null, value, type, null);
                        LOG.debug("parent in stack. adding account=" + a);
                        //                        root.addAccountToTree(a);
                        stack.push(a);
                     } else if (peek.getLevel() - 1 == x) {
                        // same level - replace peek in stack
                        Account a = new Account(null, peek.getParent(), null, value, type, null);
                        LOG.debug("schwester in stack. adding account=" + a);
                        //                        root.addAccountToTree(a);
                        stack.pop();
                        stack.push(a);
                     } else if (peek.getLevel() - 1 > x) {
                        // pop accounts until last pop'ed matches x level
                        Account pop = null;
                        int diff = peek.getLevel() - x - 1;
                        for (int i = 0; i <= diff; i++) {
                           pop = stack.pop();
                        }
                        Account a = new Account(null, pop.getParent(), null, value, type, null);
                        //                        root.addAccountToTree(a);
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
