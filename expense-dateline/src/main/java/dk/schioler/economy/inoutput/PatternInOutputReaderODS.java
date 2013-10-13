package dk.schioler.economy.inoutput;

import java.awt.Color;
import java.awt.Point;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jopendocument.dom.ODPackage;
import org.jopendocument.dom.spreadsheet.Cell;
import org.jopendocument.dom.spreadsheet.Column;
import org.jopendocument.dom.spreadsheet.MutableCell;
import org.jopendocument.dom.spreadsheet.Range;
import org.jopendocument.dom.spreadsheet.Sheet;
import org.jopendocument.dom.spreadsheet.SpreadSheet;
import org.springframework.stereotype.Component;

import dk.schioler.economy.ExpenseException;
import dk.schioler.economy.model.Account;

@Component
public class PatternInOutputReaderODS implements PatternInOutputReader {
   private static final Logger LOG = Logger.getLogger(PatternInOutputReaderODS.class);

   public void writePatterns(Map<String, List<String>> patternMap, OutputStream outStream) {
      try {

         int countDataRows = patternMap.keySet().size();
         int countHeaderRows = 1;
         int countDataColumns = 0;
         int countPathColumns = 1;

         int maxLengthPath = 0;
         for (java.util.Map.Entry<String, List<String>> e : patternMap.entrySet()) {
            countDataColumns = Math.max(countDataColumns, e.getValue().size());
            maxLengthPath = Math.max(maxLengthPath, e.getKey().length());
         }

         countDataColumns = Math.max(countDataColumns, 10);

//         LOG.trace("countDateColumns=" + countDataColumns + ", dataRows=" + countDataRows);


         SpreadSheet spreadSheet = SpreadSheet.create(1, (countDataColumns + countPathColumns), (countDataRows + countHeaderRows));

         Sheet sheet = spreadSheet.getFirstSheet();
         sheet.ensureColumnCount((countDataColumns + countPathColumns));
         sheet.ensureRowCount((countDataRows + countHeaderRows));
         sheet.setName("patterns");


         Set<String> keySet = patternMap.keySet();
         List<String> accounts = new ArrayList<String>();
         Collections.addAll(accounts, keySet.toArray(new String[keySet.size()]));
         Collections.sort(accounts);

         String currentRoot = null;
         boolean setRootBg = true;
         int countRowsSinceLastRoot = 0;
//         int countBGCols = 0;
         for (int row = 0; row < countDataRows; row++) {
            String fullPath = accounts.get(row);
//            LOG.debug(fullPath);
//            LOG.debug("curRoot=" + currentRoot + ", bgCools=" + countBGCols);
            if (currentRoot == null || (!fullPath.startsWith(currentRoot))) {
               currentRoot = Account.PATH_SEPARATOR + fullPath.split(Account.PATH_SEPARATOR)[1];
               setRootBg = true;

            }
            for (int col = 0; col <= countDataColumns; col++) {
               MutableCell<SpreadSheet> cell = sheet.getCellAt(col, row);

               if (setRootBg) {
                  Color color = new Color(119, 174, 237);
                  cell.setBackgroundColor(color);
                  countRowsSinceLastRoot = 0;
               } else {
                  if (0 == (countRowsSinceLastRoot % 8)) {
                     Color color = new Color(187, 211, 237);
//                     countBGCols++;
                     cell.setBackgroundColor(color);
                  }
               }
            }
            countRowsSinceLastRoot++;
            setRootBg = false;

         }

         for (int row = 0; row < accounts.size(); row++) {
            String fullPath = accounts.get(row);
            List<String> patterns = patternMap.get(fullPath);
//            LOG.trace(fullPath + ", patterns=" + patterns);

            for (int col = 0; col <= patterns.size(); col++) {
               MutableCell<SpreadSheet> cell = sheet.getCellAt(col, row);
               Column<SpreadSheet> column = sheet.getColumn(col);
               if (col == 0) {
                  column.setWidth(new Long((long) (maxLengthPath * 1.7F)));

                  cell.setValue(fullPath);
               } else if (col > 0) {
                  column.setWidth(new BigDecimal("40"));

                  String patternValue = patterns.get(col - 1);
                  //                  LOG.debug("patternValue=" + patternValue);
                  if (!StringUtils.isBlank(patternValue)) {
                     cell.setValue(patternValue);
                  }

               }
            }

         }

         spreadSheet.getPackage().save(outStream);

      } catch (Exception e) {
         LOG.error(e.getMessage(), e);
         throw new ExpenseException(e);
      }
   }

   public Map<String, List<String>> readPatterns(InputStream inputStream) {
      Map<String, List<String>> retVal = new TreeMap<String, List<String>>();

      Sheet sheet;
      try {
         ODPackage odPackage = ODPackage.createFromStream(inputStream, null);
         SpreadSheet spreadSheet = SpreadSheet.get(odPackage);
         sheet = spreadSheet.getFirstSheet();
      } catch (IOException e) {
         throw new ExpenseException(e.getMessage(), e);
      }
      Range usedRange = sheet.getUsedRange();
      Point startPoint = usedRange.getStartPoint();
      Point endPoint = usedRange.getEndPoint();
//      LOG.trace("Found used range: startPoint=" + startPoint + ", endPoint=" + endPoint);

      String key = null;
      List<String> patterns = null;
      for (int y = startPoint.y; y <= endPoint.y; y++) {
         //         LOG.trace("new row=" + y);
         patterns = new ArrayList<String>();
         for (int x = startPoint.x; x <= endPoint.x; x++) {
            //            LOG.trace("new col=" + x);
            Cell<SpreadSheet> cell = sheet.getImmutableCellAt(x, y);
            String textValue = cell.getTextValue();
            if (x == 0) {
               //               LOG.trace(textValue + ": x==0, grabbing key");
               key = textValue.trim();
            } else {
               if (StringUtils.isNotBlank(textValue)) {
                  //                  LOG.trace(textValue + ": will add to patterns: " + patterns);
                  patterns.add(textValue);
               }
            }
         }
         if (StringUtils.isNotBlank(key)) {
//            LOG.trace("will add key=" + key + ":" + patterns);
            if (retVal.containsKey(key)) {
               throw new ExpensePatternDuplicateAccountKeyException("found duplicate key=" + key + " - keys must be unique");
            }
            retVal.put(key, patterns);
         }
      }

      return retVal;
   }

}
