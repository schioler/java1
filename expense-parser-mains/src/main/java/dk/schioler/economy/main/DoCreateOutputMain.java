package dk.schioler.economy.main;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.stereotype.Component;

import dk.schioler.economy.command.DoOutputCommand;
import dk.schioler.economy.expenseparser.schema.CSVType;
import dk.schioler.economy.expenseparser.schema.ChartType;
import dk.schioler.economy.expenseparser.schema.DataType;
import dk.schioler.economy.expenseparser.schema.ExpenseParserConfigType;
import dk.schioler.economy.expenseparser.schema.OutputType;
import dk.schioler.economy.expenseparser.schema.ReportType;
import dk.schioler.economy.model.Account;
import dk.schioler.economy.out.chart.Chart;
import dk.schioler.economy.out.chart.ChartFactory;
import dk.schioler.economy.out.data.chart.OutputChartDataBuilder;
import dk.schioler.economy.out.data.text.OutputTextDataBuilder;
import dk.schioler.economy.out.text.TextOutput;
import dk.schioler.economy.out.text.FactoryTextOutput;
import dk.schioler.economy.util.Log4JLoader;
import dk.schioler.economy.util.MarshallHelper;
import dk.schioler.economy.util.SpringFrameworkHelper;

@Component("doOutputMain")
public class DoCreateOutputMain {
   static {
      Log4JLoader.loadLog();
      Locale.setDefault(new Locale("da", "DK"));
   }

   private static final Logger LOG = Logger.getLogger(DoCreateOutputMain.class);
   SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
   @Autowired
   MarshallHelper marshallHelper;

   @Autowired
   DoOutputCommand doOutputCommand;

   @Autowired
   private ChartFactory chartFactory;

   @Autowired
   private FactoryTextOutput textOutputFactory;

   public void doIt(String configFile) throws IOException {
      ExpenseParserConfigType userConfig = marshallHelper.loadUserConfig(configFile);

      String owner = userConfig.getUser().getOwner();
      OutputType output = userConfig.getOutput();
      List<ReportType> reports = output.getReport();
      for (ReportType report : reports) {
         Calendar startDate = report.getStartDate();
         Calendar endDate = report.getEndDate();
         String outputDirectory = report.getOutputDirectory();
         String reportName = report.getReportName();

         doOutputCommand.fillTimeLineWithExpenseLines(owner, startDate.getTime(), endDate.getTime());

         List<DataType> dataList = report.getData();
         for (DataType data : dataList) {
            String account = data.getAccount();

            String[] split = account.split(",");
            for (int i = 0; i < split.length; i++) {
               int countLevels = data.getCountLevels();
               String accountString = split[i].trim();
               LOG.debug(accountString);

               String accountNameForFileString = null;
               if ("/".equals(accountString)) {
                  accountString = Account.ROOT_NAME;
                  accountNameForFileString = "ROOT";

               } else {
                  accountNameForFileString = accountString;
                  accountNameForFileString = accountNameForFileString.substring(1);
                  accountNameForFileString = accountNameForFileString.replace("/", "_");

               }

               String start = sdf.format(startDate.getTime());
               String end = sdf.format(endDate.getTime());

               List<CSVType> csvList = data.getCsv();
               for (CSVType csvType : csvList) {
                  String csvDataType = csvType.getCsvDataType();
                  OutputTextDataBuilder textDataBuilder = doOutputCommand.buildTextData(csvDataType, accountString, countLevels);
                  String[][] categories = textDataBuilder.getCategories();
                  Date[] dates = textDataBuilder.getDates();
                  Object[][] values = textDataBuilder.getValues();

                  String type = csvType.getType();
                  TextOutput textOutput = textOutputFactory.getTextOutput(type);
                  textOutput.write(start + "_" + end + "-" + reportName + "-" + accountNameForFileString + "-" + countLevels + "-levels", categories, dates, values, new File(outputDirectory));
               }

               List<ChartType> chartList = data.getChart();
               for (ChartType chartType : chartList) {
                  String chartDataType = chartType.getChartDataType();
                  OutputChartDataBuilder chartData = doOutputCommand.buildChartData(chartDataType, accountString, countLevels);
                  String[][] categories = chartData.getCategories();
                  Date[] dates = chartData.getDates();
                  Object[][] values = chartData.getValues();

                  String type = chartType.getType();
                  Chart chart = chartFactory.getChart(type);
                  chart.write(start + "_" + end + "-" + reportName + "-" + accountNameForFileString + "-" + chartDataType + "-" + countLevels + "-levels", categories, dates, values, new File(
                        outputDirectory));
               }
            }
         }

         doOutputCommand.clearTimeline();
      }

   }

   public static void main(String[] args) {
      AbstractApplicationContext springCtx = SpringFrameworkHelper.getDefaultApplicationContext();
      try {
         DoCreateOutputMain main = (DoCreateOutputMain) springCtx.getBean("doOutputMain");
         main.doIt(args[0]);
      } catch (Exception e) {
         LOG.error(e.getMessage(), e);
      } finally {
         springCtx.close();
      }
   }

}
