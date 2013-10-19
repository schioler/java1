package dk.schioler.expense.fe.gwt.client.page;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.TreeViewModel;

import dk.schioler.expense.fe.gwt.client.Gui;
import dk.schioler.expense.fe.gwt.client.Page;

public class AccountShow implements Page {

   final String name = Gui.PAGE_ACCOUNT_SHOW;
   Widget root;

   public AccountShow() {
      // Create a model for the tree.
      TreeViewModel model = new CustomTreeModel();

      /*
       * Create the tree using the model. We specify the default value of the
       * hidden root node as "Item 1".
       */
      CellTree tree = new CellTree(model, "Account 1");
      root = tree;

   }

   @Override
   public Widget getRootWidget() {
      return root;
   }

   @Override
   public String getName() {
      return name;
   }

   @Override
   public void show() {
      // TODO Auto-generated method stub

   }

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

}
