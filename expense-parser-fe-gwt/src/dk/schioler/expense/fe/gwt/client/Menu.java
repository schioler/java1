package dk.schioler.expense.fe.gwt.client;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.Widget;

public class Menu {

   final Gui gui;

   public Menu(Gui gui) {
      super();
      this.gui = gui;
   }

   Command buildShowPageCommand(final String page) {
      return new Command() {

         @Override
         public void execute() {
            gui.showPage(page);
         }
      };
   }

   public Widget buildMainMenu() {
      MenuBar globalMenuBar = new MenuBar(false);

      MenuBar accountMenuBar = new MenuBar(true);
      MenuItem itemAccountNew = new MenuItem("Show", buildShowPageCommand(Gui.PAGE_ACCOUNT_SHOW));
      MenuItem itemAccountImport = new MenuItem("Import", buildShowPageCommand(Gui.PAGE_ACCOUNT_IMPORT));
      MenuItem itemAccountExport = new MenuItem("Export", buildShowPageCommand(Gui.PAGE_ACCOUNT_EXPORT));
      accountMenuBar.addItem(itemAccountNew);
      accountMenuBar.insertSeparator(1);
      accountMenuBar.addItem(itemAccountImport);
      accountMenuBar.addItem(itemAccountExport);

      MenuBar patternMenuBar = new MenuBar(true);
      MenuItem itemPatternShow = new MenuItem("Show",buildShowPageCommand(Gui.PAGE_PATTERN_SHOW) );
      MenuItem itemPatternMatch = new MenuItem("Match",buildShowPageCommand(Gui.PAGE_PATTERN_DO_MATCH) );
      MenuItem itemPatternExport = new MenuItem("Export",buildShowPageCommand(Gui.PAGE_PATTERN_EXPORT) );
      MenuItem itemPatternImport = new MenuItem("Import",buildShowPageCommand(Gui.PAGE_PATTERN_IMPORT) );

      patternMenuBar.addItem(itemPatternShow);
      patternMenuBar.insertSeparator(1);
      patternMenuBar.addItem(itemPatternMatch);
      patternMenuBar.insertSeparator(1);
      patternMenuBar.addItem(itemPatternImport);
      patternMenuBar.addItem(itemPatternExport);


      MenuBar expenseMenuBar = new MenuBar(true);
      MenuItem itemExpenseShow = new MenuItem("Show", buildShowPageCommand(Gui.PAGE_EXPENSE_SHOW));
      MenuItem expenseUpload = new MenuItem("Import", buildShowPageCommand(Gui.PAGE_EXPENSE_IMPORT));
      expenseMenuBar.addItem(itemExpenseShow);
      expenseMenuBar.addItem(expenseUpload);

      MenuBar reportMenuBar = new MenuBar(true);
      MenuItem itemReportShow = new MenuItem("Show", buildShowPageCommand(Gui.PAGE_REPORT_SHOW));

      reportMenuBar.addItem(itemReportShow);

      MenuItem itemExpense = new MenuItem("Expense", expenseMenuBar);
      MenuItem itemAccount = new MenuItem("Account", accountMenuBar);
      MenuItem itemPattern = new MenuItem("Pattern", patternMenuBar);
      MenuItem itemReport = new MenuItem("Report", reportMenuBar);

      itemExpense.setStylePrimaryName("mainMenuItem");
      itemAccount.setStylePrimaryName("mainMenuItem");
      itemPattern.setStylePrimaryName("mainMenuItem");
      itemReport.setStylePrimaryName("mainMenuItem");

      globalMenuBar.addItem(itemAccount);
      globalMenuBar.addItem(itemPattern);
      globalMenuBar.addItem(itemExpense);
      globalMenuBar.addItem(itemReport);

      return globalMenuBar;
   }

}
