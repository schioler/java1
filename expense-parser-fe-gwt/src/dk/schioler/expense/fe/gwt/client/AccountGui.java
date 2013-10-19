package dk.schioler.expense.fe.gwt.client;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.TreeViewModel;

public class AccountGui implements ExpenseGui  {

   Command cmd = new Command() {

      @Override
      public void execute() {
         Window.alert("You selected a menu item!");
      }
   };

//   public Widget buildContextMenu() {
//      MenuBar expenseMenuBar = new MenuBar(true);
//      MenuItem expenseUpload = new MenuItem("Import", cmd);
//      MenuItem itemExpenseShow = new MenuItem("Export", cmd);
//      itemExpenseShow.setStylePrimaryName("contextMenuItem");
//
//      expenseUpload.setStylePrimaryName("contextMenuItem");
//      expenseMenuBar.addItem(itemExpenseShow);
//      expenseMenuBar.addItem(expenseUpload);
//      expenseMenuBar.setStylePrimaryName("contextMenu");
//      return expenseMenuBar;
//   }


   /**
    * The model that defines the nodes in the tree.
    */
   private static class CustomTreeModel implements TreeViewModel {

     /**
      * Get the {@link NodeInfo} that provides the children of the specified
      * value.
      */
     public <T> NodeInfo<?> getNodeInfo(T value) {
       /*
        * Create some data in a data provider. Use the parent value as a prefix
        * for the next level.
        */
       ListDataProvider<String> dataProvider = new ListDataProvider<String>();
       for (int i = 0; i < 2; i++) {
         dataProvider.getList().add(value + "." + String.valueOf(i));
       }

       // Return a node info that pairs the data with a cell.
       return new DefaultNodeInfo<String>(dataProvider, new TextCell());
     }

     /**
      * Check if the specified value represents a leaf node. Leaf nodes cannot be
      * opened.
      */
     public boolean isLeaf(Object value) {
       // The maximum length of a value is ten characters.
       return value.toString().length() > 20;
     }
   }
   @Override
   public Widget buildContent() {
      // Create a model for the tree.
      TreeViewModel model = new CustomTreeModel();

      /*
       * Create the tree using the model. We specify the default value of the
       * hidden root node as "Item 1".
       */
      CellTree tree = new CellTree(model, "Account 1");
      return tree;
   }

   @Override
   public String getName() {
      return "Account";
   }

}
