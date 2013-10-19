package dk.schioler.economy.command;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import dk.schioler.economy.model.Line;
import dk.schioler.economy.out.data.OutputDataBuilderFactory;
import dk.schioler.economy.out.data.TimeLine;
import dk.schioler.economy.out.data.chart.OutputChartDataBuilder;
import dk.schioler.economy.out.data.text.OutputTextDataBuilder;
import dk.schioler.economy.persister.LinePersister;

@Component("doOutputCommand")
@Scope("prototype")
public class DoOutputCommand extends BaseCommand {

   static Logger LOG = Logger.getLogger(DoOutputCommand.class);

   @Autowired
   private LinePersister linePersister;

   @Autowired
   private TimeLine timeLine;

   @Autowired
   private OutputDataBuilderFactory outputDataBuilderFactory;

//   SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
//
//   public void execute() {
//      //      lookupUser();
//      timeLine.setUser(user);
//
//      List<ReportType> reports = userInput.getOutput().getReport();
//      for (ReportType report : reports) {
//         Date startDate = report.getStartDate().getTime();
//         Date endDate = report.getEndDate().getTime();
//         String reportName = report.getReportName();
//         String outputDirectory = report.getOutputDirectory();
//         LOG.debug("Report=" + reportName + ", startDate=" + startDate + ", end=" + endDate + ", outputDir=" + outputDirectory);
//
//         // Gather Data, establish timeline:
//         fillTimeLineWithExpenseLines(startDate, endDate);
//
//         List<DataType> data = report.getData();
//         for (DataType dataType : data) {
//            int countLevels = dataType.getCountLevels();
//            String account = dataType.getAccount();
//            LOG.debug("acountLevels=" + countLevels + ", account=" + account);
//
//            String[] split = account.split(",");
//            for (int i = 0; i < split.length; i++) {
//               String accountString = split[i].trim();
//               LOG.debug(accountString);
//
//               String accountNameForFileString = null;
//               if ("/".equals(accountString)) {
//                  accountNameForFileString = "ROOT";
//               } else {
//                  accountNameForFileString = accountString;
//                  accountNameForFileString = accountNameForFileString.substring(1);
//                  accountNameForFileString = accountNameForFileString.replace("/", "_");
//
//               }
//
//               String start = sdf.format(startDate);
//               String end = sdf.format(endDate);
//
//               List<CSVType> csv = dataType.getCsv();
//               for (CSVType csvType : csv) {
//                  String csvDataType = csvType.getCsvDataType();
//                  OutputTextDataBuilder textDataBuilder = outputDataBuilderFactory.getTextDataBuilder(csvDataType);
//                  textDataBuilder.buildOutData(timeLine, account, countLevels);
//                  String[][] categories = textDataBuilder.getCategories();
//                  Date[] dates = textDataBuilder.getDates();
//                  Object[][] values = textDataBuilder.getValues();
//
//                  String type = csvType.getType();
//                  TextOutput textOutput = textOutputFactory.getTextOutput(type);
//                  textOutput.write(start + "_" + end + "-" + reportName + "-" + accountNameForFileString + "-" + countLevels + "-levels", categories, dates, values, new File(outputDirectory));
//               }
//
//            }
//         }
//         clearTimeline();
//
//      }
//
//   }

   public OutputChartDataBuilder buildChartData(String expenseType, String account, int countLevels) {
      OutputChartDataBuilder outputDataBuilder = outputDataBuilderFactory.getChartDataBuilder(expenseType);
      outputDataBuilder.buildOutData(timeLine, account, countLevels);
      return outputDataBuilder;
   }

   public OutputTextDataBuilder buildTextData(String csvDataType, String account, int countLevels) {

      OutputTextDataBuilder textDataBuilder = outputDataBuilderFactory.getTextDataBuilder(csvDataType);
      textDataBuilder.buildOutData(timeLine, account, countLevels);
      return textDataBuilder;

   }

   public void clearTimeline() {
      timeLine.clear();
   }

   public void fillTimeLineWithExpenseLines(String owner, Date startDate, Date endDate) {
      lookupUser(owner);
      timeLine.setUser(user);
      timeLine.createTimelineForPeriod(startDate, endDate);
      List<Line> lines = linePersister.getLines(user.getId(), startDate, endDate);
      for (Line line : lines) {
         boolean success = this.timeLine.addLine(line);
         if (!success) {
            LOG.warn("unreckonized:" + line);
         }
      }
      //      return timeLine;

   }

}
