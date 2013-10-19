package dk.schioler.economy.out.data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import dk.schioler.economy.AccountTreeRoot;
import dk.schioler.economy.model.Account;
import dk.schioler.economy.out.data.chart.OutputChartDataBuilder;
import dk.schioler.economy.visitor.Visitor;

public class NotUsedOutputDataBuilderAllDataOneLevel implements Visitor, OutputChartDataBuilder {
   static Logger LOG = Logger.getLogger(NotUsedOutputDataBuilderAllDataOneLevel.class);

   int curDateIdx;
   int accountLevels = 1;
   String accountName;

   String[][] categories = new String[4][1];
   Date[] dates;
   BigDecimal[][] values;

   public NotUsedOutputDataBuilderAllDataOneLevel() {
      super();
      categories[OutDataConstants.CATEGORY_TOTAL_IDX][0]= "Total";
      categories[OutDataConstants.CATEGORY_REGULAR_IDX][0] = "Regular";
      categories[OutDataConstants.CATEGORY_NONREGULAR_IDX][0] = "Non-Regular";
      categories[OutDataConstants.CATEGORY_EXTRA_IDX][0] = "Extraordinaire";
   }

   public boolean visit(Account element) {
      if (accountName.equalsIgnoreCase(element.getFullPath())) {

         values[curDateIdx][OutDataConstants.CATEGORY_TOTAL_IDX] = values[curDateIdx][OutDataConstants.CATEGORY_TOTAL_IDX].add(element.getExpensesTotal().abs());
         values[curDateIdx][OutDataConstants.CATEGORY_REGULAR_IDX] = values[curDateIdx][OutDataConstants.CATEGORY_REGULAR_IDX].add(element.getExpensesRegular().abs());
         values[curDateIdx][OutDataConstants.CATEGORY_NONREGULAR_IDX] = values[curDateIdx][OutDataConstants.CATEGORY_NONREGULAR_IDX].add(element.getExpensesNonRegular().abs());
         values[curDateIdx][OutDataConstants.CATEGORY_EXTRA_IDX] = values[curDateIdx][OutDataConstants.CATEGORY_EXTRA_IDX].add(element.getExpensesExtra().abs());
         LOG.debug("periodIdx=" + curDateIdx + ", total=" + values[curDateIdx][OutDataConstants.CATEGORY_TOTAL_IDX].longValue());

         return false;
      }

      return true;
   }

   public boolean init() {
      // TODO Auto-generated method stub
      return false;
   }

   public String[][] getCategories() {
      return categories;
   }

   public BigDecimal[][] getValues() {
      return values;
   }

   public void buildOutData(TimeLine timeline, String accountName, int countLevels) {
      this.accountName = accountName;
      List<Period> periods = timeline.getPeriods();
      initTables(periods.size());
      for (Period period : periods) {
         AccountTreeRoot accountTreeRoot = period.getAccountTreeRoot();
         accountTreeRoot.accept(this);
         dates[curDateIdx] = period.getStartDate();
         curDateIdx++;

      }

   }

   private void initTables(int size) {
      values = new BigDecimal[size][4];
      dates = new Date[size];
      for (int i = 0; i < size; i++) {
         values[i][OutDataConstants.CATEGORY_TOTAL_IDX] = new BigDecimal(0);
         values[i][OutDataConstants.CATEGORY_REGULAR_IDX] = new BigDecimal(0);
         values[i][OutDataConstants.CATEGORY_NONREGULAR_IDX] = new BigDecimal(0);
         values[i][OutDataConstants.CATEGORY_EXTRA_IDX] = new BigDecimal(0);
      }
      this.curDateIdx = 0;
   }

   public Date[] getDates() {
      return dates;
   }

   public void setCountLevels(int levels) {
      this.accountLevels = levels;
   }

}
