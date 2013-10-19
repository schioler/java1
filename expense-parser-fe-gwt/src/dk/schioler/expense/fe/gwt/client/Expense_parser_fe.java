package dk.schioler.expense.fe.gwt.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootLayoutPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Expense_parser_fe implements EntryPoint {
   /**
    * The message displayed to the user when the server cannot be reached or
    * returns an error.
    */
//   private static final String SERVER_ERROR = "An error occurred while " + "attempting to contact the server. Please check your network " + "connection and try again.";

   /**
    * Create a remote service proxy to talk to the server-side Greeting service.
    */
//   private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);

   /**
    * This is the entry point method.
    */
   public void onModuleLoad() {
      RemoteFacade remoteFacade = new RemoteFacadeExpenseImpl();
      Gui gui = new Gui(remoteFacade);
      RootLayoutPanel rootLayoutPanel = RootLayoutPanel.get();
      rootLayoutPanel.setStylePrimaryName("body");
      rootLayoutPanel.add(gui.getRootElement());
   }

//   private class GuiElements {
//      MenuBar menuBar;
//      Label errorLabel;
//      Label textToServerLabel;
//      Button sendButton;
//      TextBox nameField;
//
//      DialogBox dialogBox;
//      HTML serverResponseLabel;
//      Button closeButton;
//
//   }

//   private MenuBar buildMenu() {
//      MenuBar globalMenuBar = new MenuBar(false);
//
//      MenuBar accountMenuBar = new MenuBar(true);
//      MenuItem itemAccountUpload = new MenuItem(accountLoad);
//      MenuItem itemAccountShow = new MenuItem(accountShow);
//      accountMenuBar.addItem(itemAccountShow);
//      accountMenuBar.addItem(itemAccountUpload);
//
//      MenuBar expenseMenuBar = new MenuBar(true);
//      MenuItem expenseUpload = new MenuItem(expenseLoad);
//      MenuItem itemExpenseShow = new MenuItem(expenseShow);
//      expenseMenuBar.addItem(itemExpenseShow);
//      expenseMenuBar.addItem(expenseUpload);
//
//      MenuBar patternMenuBar = new MenuBar(true);
//      MenuItem itemPatternLoad = new MenuItem("Load", new Command() {
//
//         @Override
//         public void execute() {
//            Window.alert("Pattern Load");
//         }
//      });
//      MenuItem itemPatternShow = new MenuItem("Show", new Command() {
//
//         @Override
//         public void execute() {
//            Window.alert("Pattern Show");
//         }
//      });
//
//      patternMenuBar.addItem(itemPatternShow);
//      patternMenuBar.addItem(itemPatternLoad);
//
//      MenuItem itemExpense = new MenuItem(expenseName, expenseMenuBar);
//      MenuItem itemAccount = new MenuItem(accountName, accountMenuBar);
//      MenuItem itemPattern = new MenuItem("Pattern", patternMenuBar);
//
//      globalMenuBar.addItem(itemAccount);
//      globalMenuBar.addItem(itemPattern);
//      globalMenuBar.addItem(itemExpense);
////      globalMenuBar.setStylePrimaryName("menu");
//
//      return globalMenuBar;
//
//   }
//
//   SafeHtml accountName = new SafeHtml() {
//      private static final long serialVersionUID = 1L;
//
//      @Override
//      public String asString() {
//
//         return "Account";
//      }
//   };
//
//   SafeHtml accountLoad = new SafeHtml() {
//      private static final long serialVersionUID = 1L;
//
//      @Override
//      public String asString() {
//
//         return "Upload";
//      }
//   };
//
//   SafeHtml accountShow = new SafeHtml() {
//      private static final long serialVersionUID = 1L;
//
//      @Override
//      public String asString() {
//
//         return "Show";
//      }
//   };
//
//   SafeHtml expenseName = new SafeHtml() {
//      private static final long serialVersionUID = 1L;
//
//      @Override
//      public String asString() {
//
//         return "Expense";
//      }
//   };
//
//   SafeHtml expenseLoad = new SafeHtml() {
//      private static final long serialVersionUID = 1L;
//
//      @Override
//      public String asString() {
//
//         return "Upload";
//      }
//   };
//
//   SafeHtml expenseShow = new SafeHtml() {
//      private static final long serialVersionUID = 1L;
//
//      @Override
//      public String asString() {
//
//         return "Show";
//      }
//   };
//
//   class MenuHandler implements ClickHandler {
//
//      GuiElements guiElements;
//
//      public MenuHandler(GuiElements guiElements) {
//         super();
//         this.guiElements = guiElements;
//      }
//
//      @Override
//      public void onClick(ClickEvent event) {
//
//      }
//
//   }
//
//   // Create a handler for the sendButton and nameField
//   class ButtonHandler implements ClickHandler, KeyUpHandler {
//      GuiElements guiElements;
//
//      public ButtonHandler(GuiElements guiElements) {
//         super();
//         this.guiElements = guiElements;
//      }
//
//      /**
//       * Fired when the user clicks on the sendButton.
//       */
//      public void onClick(ClickEvent event) {
//         sendNameToServer();
//      }
//
//      /**
//       * Fired when the user types in the nameField.
//       */
//      public void onKeyUp(KeyUpEvent event) {
//         if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
//            sendNameToServer();
//         }
//      }
//
//      /**
//       * Send the name from the nameField to the server and wait for a response.
//       */
//      private void sendNameToServer() {
//         // First, we validate the input.
//         guiElements.errorLabel.setText("");
//         String textToServer = guiElements.nameField.getText();
//         if (!FieldVerifier.isValidName(textToServer)) {
//            guiElements.errorLabel.setText("Please enter at least four characters");
//            return;
//         }
//
//         // Then, we send the input to the server.
//         guiElements.sendButton.setEnabled(false);
//         guiElements.textToServerLabel.setText(textToServer);
//         guiElements.serverResponseLabel.setText("");
//         greetingService.greetServer(textToServer, new AsyncCallback<String>() {
//            public void onFailure(Throwable caught) {
//               // Show the RPC error message to the user
//               guiElements.dialogBox.setText("Remote Procedure Call - Failure");
//               guiElements.serverResponseLabel.addStyleName("serverResponseLabelError");
//               guiElements.serverResponseLabel.setHTML(SERVER_ERROR);
//               guiElements.dialogBox.center();
//               guiElements.closeButton.setFocus(true);
//            }
//
//            public void onSuccess(String result) {
//               guiElements.dialogBox.setText("Remote Procedure Call");
//               guiElements.serverResponseLabel.removeStyleName("serverResponseLabelError");
//               guiElements.serverResponseLabel.setHTML(result);
//               guiElements.dialogBox.center();
//               guiElements.closeButton.setFocus(true);
//            }
//         });
//      }
//   }
}
