package dk.schioler.economy.command;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dk.schioler.economy.accountparser.schema.AccountParserType;
import dk.schioler.economy.accountparser.schema.DateRangeType;
import dk.schioler.economy.accountparser.schema.ReportType;
import dk.schioler.economy.model.Line;
import dk.schioler.economy.model.User;
import dk.schioler.economy.out.TimeLine;
import dk.schioler.economy.persister.LinePersister;
import dk.schioler.economy.persister.UserPersister;
import dk.schioler.economy.util.MarshallHelper;

@Component("doReport")
public class DoReport {

   static Logger LOG = Logger.getLogger(DoReport.class);

   @Autowired
   private MarshallHelper marshallHelper;

   @Autowired
   private LinePersister linePersister;

   @Autowired
   private UserPersister userPersister;


   @Autowired
   private TimeLine timeLine;

   // Data from user.
   private AccountParserType accountParserUserConfig;
//   private String owner;
   private List<ReportType> reports;
   private List<Line> unReckognizedLines = new ArrayList<Line>();

   private User user;

   public void readUserFile(String userFile) throws IOException {
      accountParserUserConfig = marshallHelper.loadUserConfig(userFile);
      reports = accountParserUserConfig.getOutput().getReport();

      String owner = accountParserUserConfig.getOwner();
      user = userPersister.getUser(owner);
      this.timeLine.setUser(user);
      LOG.debug("user=" + user);
      LOG.debug("Reports=" + reports);
   }

   public void doReport() {
      for (ReportType report : reports) {
         DateRangeType dateRange = report.getDateRange();
         Calendar startDate = dateRange.getStartDate();
         Calendar endDate = dateRange.getEndDate();
         LOG.debug("Report. startDate=" + startDate.getTime() + ", end=" + endDate.getTime());
         fillTimeLineWithExpenseLines(user, startDate.getTime(), endDate.getTime());
         String output = timeLine.getOutput();
         LOG.debug(output);
         try {
            PrintWriter pw = new PrintWriter(new File("test-out.csv"));
            pw.print(output);
            pw.flush();
            pw.close();
         } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
      }

   }

   private void fillTimeLineWithExpenseLines(User owner, Date startDate, Date endDate) {
      List<Line> lines = linePersister.getLines(owner.getId(), startDate, endDate);

      for (Line line : lines) {
         LOG.debug("looking at line:" + line);
         boolean success = this.timeLine.addLine(line);
         if (!success) {
            unReckognizedLines.add(line);
         }
      }

      this.timeLine.ensureConsecutivePeriods();
      for (Line line : unReckognizedLines) {
         LOG.debug("unreckonized:" + line);
      }
   }

}
