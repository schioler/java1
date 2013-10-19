package dk.schioler.economy.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.stereotype.Component;

import dk.schioler.economy.command.DoExpensesInputCommand;
import dk.schioler.economy.expenseparser.schema.DirectoryType;
import dk.schioler.economy.expenseparser.schema.ExpenseParserConfigType;
import dk.schioler.economy.expenseparser.schema.FileType;
import dk.schioler.economy.expenseparser.schema.InputType;
import dk.schioler.economy.util.Log4JLoader;
import dk.schioler.economy.util.MarshallHelper;
import dk.schioler.economy.util.SpringFrameworkHelper;

@Component("DoExpenseInputMain")
public class DoExpenseInputMain {

   static {
      Log4JLoader.loadLog();
      Locale.setDefault(new Locale("da", "DK"));
   }

   static Logger LOG = Logger.getLogger(DoExpenseInputMain.class);

   @Autowired
   private DoExpensesInputCommand doExpenseInputCommand;

   @Autowired
   private MarshallHelper marshallHelper;

   public DoExpenseInputMain() {
      super();
   }

   public void doInputExpenses(String userFile) throws IOException {
      ExpenseParserConfigType userConfig = marshallHelper.loadUserConfig(userFile);
      String owner = userConfig.getUser().getOwner();

      InputType input = userConfig.getInput();

      List<String> errorsList = new ArrayList<String>();
      List<DirectoryType> directory = input.getDirectory();
      for (DirectoryType directoryType : directory) {
         String origin = directoryType.getOrigin();
         String path = directoryType.getPath();
         LOG.debug("Expense Directory= " + path);
         String s = directoryType.getSuffix();
         final String suffix = StringUtils.isNotBlank(s) ? s : ".csv";
         File dir = new File(path);
         if (dir.exists() && dir.isDirectory()) {
            File[] list = dir.listFiles(new FilenameFilter() {

               public boolean accept(File dir, String name) {
                  return name.endsWith(suffix) ? true : false;
               }
            });
            for (int i = 0; i < list.length; i++) {

               LOG.debug("******* File found=" + list[i].getAbsoluteFile());
               FileInputStream fis = null;
               if (list[i].exists()) {
                  try {
                     fis = new FileInputStream(list[i]);
                     doExpenseInputCommand.persistInput(fis, owner, origin);
                  } catch (Exception e) {
                     LOG.error(e.getMessage(), e);
                  } finally {
                     IOUtils.closeQuietly(fis);
                  }
               }
            }

         } else {
            LOG.warn("A specified directory was not found: " + dir.getAbsolutePath());
            errorsList.add("Not found: " + dir.getAbsolutePath());

         }

      }

      List<FileType> file = input.getFile();
      for (FileType fileType : file) {
         String origin = fileType.getOrigin();
         String path = fileType.getPath();
         String s = fileType.getSuffix();
         final String suffix = StringUtils.isNotBlank(s) ? s : ".csv";
         File f = new File(path);
         if (f.exists() && f.getName().endsWith(suffix)) {

            FileInputStream fis = null;
            try {
               fis = new FileInputStream(f);
               doExpenseInputCommand.persistInput(fis, owner, origin);
            } catch (Exception e) {
               LOG.error(e.getMessage(), e);
            } finally {
               IOUtils.closeQuietly(fis);
            }
         } else {
            LOG.warn("A specified directory was not found: " + f.getAbsolutePath());
            errorsList.add("Not found: " + f.getAbsolutePath());
         }
      }

      if (errorsList.size() > 0) {
         LOG.warn("***********************");
         for (String in : errorsList) {
            LOG.warn(in);
         }
         LOG.warn("***********************");
      }

   }

   public static void main(String[] args) {
      AbstractApplicationContext springCtx = SpringFrameworkHelper.getDefaultApplicationContext();
      try {
         DoExpenseInputMain main = (DoExpenseInputMain) springCtx.getBean("DoExpenseInputMain");

         main.doInputExpenses(args[0]);

      } catch (Exception e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      } finally {
         springCtx.close();
      }

   }

}
