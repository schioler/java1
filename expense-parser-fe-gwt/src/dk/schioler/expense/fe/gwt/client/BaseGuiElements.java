package dk.schioler.expense.fe.gwt.client;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class BaseGuiElements {


   VerticalPanel verticalPanel;

   ExpenseGui accountGui = new AccountGui();

   public BaseGuiElements() {
      verticalPanel = new VerticalPanel();
      verticalPanel.setStylePrimaryName("verticalTop");
      verticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
      verticalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);

      HorizontalPanel horizontalPanel1 = new HorizontalPanel();
      horizontalPanel1.setStylePrimaryName("horizontal1");
      horizontalPanel1.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
      horizontalPanel1.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
      horizontalPanel1.add(buildMainMenu());
      verticalPanel.add(horizontalPanel1);

//      Widget context = accountGui.buildContextMenu();
//      context.setStylePrimaryName("horizontal2Col1");
      Widget content = accountGui.buildContent();
//      content.setStylePrimaryName("horizontal2Col2");


      HorizontalPanel horizontalPanel2 = new HorizontalPanel();
      horizontalPanel2.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
      horizontalPanel2.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
      horizontalPanel2.setStylePrimaryName("horizontal2");
      horizontalPanel2.setBorderWidth(1);
//      horizontalPanel2.add(context);
      horizontalPanel2.add(content);
//      horizontalPanel2.setCellWidth(context, "10%");
      horizontalPanel2.setCellWidth(content, "90%");
      verticalPanel.add(horizontalPanel2);



//      FlexTable flexTable = new FlexTable();
//      flexTable.setStylePrimaryName("horizontal2");
//      flexTable.setWidget(0, 0, context);
//      flexTable.setWidget(0, 1, content);
//      verticalPanel.add(flexTable);

      HorizontalPanel horizontalPanel3 = new HorizontalPanel();
      horizontalPanel3.setStylePrimaryName("horizontal3");
      verticalPanel.add(horizontalPanel3);

   }

   Command cmd = new Command() {

      @Override
      public void execute() {
         Window.alert("You selected a menu item!");
      }
   };

   Widget buildMainMenu() {
      MenuBar globalMenuBar = new MenuBar(false);

      MenuBar accountMenuBar = new MenuBar(true);
      MenuItem itemAccountNew = new MenuItem("New", cmd);
      MenuItem itemAccountImport = new MenuItem("Import", cmd);
      MenuItem itemAccountExport = new MenuItem("Export", cmd);
      accountMenuBar.addItem(itemAccountNew);
      accountMenuBar.insertSeparator(1);
      accountMenuBar.addItem(itemAccountImport);
      accountMenuBar.addItem(itemAccountExport);



      MenuBar expenseMenuBar = new MenuBar(true);
      MenuItem expenseUpload = new MenuItem("Upload", cmd);
      MenuItem itemExpenseShow = new MenuItem("Show", cmd);
      expenseMenuBar.addItem(itemExpenseShow);
      expenseMenuBar.addItem(expenseUpload);

      MenuBar patternMenuBar = new MenuBar(true);
      MenuItem itemPatternLoad = new MenuItem("Load", cmd);
      MenuItem itemPatternShow = new MenuItem("Show", cmd);

      patternMenuBar.addItem(itemPatternShow);
      patternMenuBar.addItem(itemPatternLoad);

      MenuItem itemExpense = new MenuItem("Expense", expenseMenuBar);
      MenuItem itemAccount = new MenuItem("Account", accountMenuBar);
      MenuItem itemPattern = new MenuItem("Pattern", patternMenuBar);

      itemExpense.setStylePrimaryName("mainMenuItem");
      itemAccount.setStylePrimaryName("mainMenuItem");
      itemPattern.setStylePrimaryName("mainMenuItem");

      globalMenuBar.addItem(itemAccount);
      globalMenuBar.addItem(itemPattern);
      globalMenuBar.addItem(itemExpense);

      return globalMenuBar;
   }

   public VerticalPanel getVerticalPanel() {
      return verticalPanel;
   }
}
