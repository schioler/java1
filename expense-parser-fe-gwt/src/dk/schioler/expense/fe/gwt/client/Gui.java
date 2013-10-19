package dk.schioler.expense.fe.gwt.client;

import java.util.Map;
import java.util.TreeMap;

import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import dk.schioler.expense.fe.gwt.client.page.AccountImport;
import dk.schioler.expense.fe.gwt.client.page.AccountShow;

/**
 * This class holds info about the gui: menu + related pages.
 * @author lasc
 *
 */
public class Gui {
   // Account
   public static final String PAGE_ACCOUNT_SHOW = "accountShow";
   public static final String PAGE_ACCOUNT_ADD = "accountAdd";
   public static final String PAGE_ACCOUNT_IMPORT = "accountImport";
   public static final String PAGE_ACCOUNT_EXPORT = "accountExport";

   // Pattern
   public static final String PAGE_PATTERN_SHOW = "patternShow";
   public static final String PAGE_PATTERN_IMPORT = "patternImport";
   public static final String PAGE_PATTERN_EXPORT = "patternExport";
   public static final String PAGE_PATTERN_DO_MATCH = "patternDoMatch";
   public static final String PAGE_PATTERN_SHOW_UNMATCHED = "patternShowUnMatched";

   // Expenses
   public static final String PAGE_EXPENSE_IMPORT = "expenseImport";
   public static final String PAGE_EXPENSE_SHOW = "expenseShow";

   // Reports
   public static final String PAGE_REPORT_SHOW = "reportShow";

   // Page layot column controler
   VerticalPanel verticalPanel = new VerticalPanel();
   // Header
   HorizontalPanel horizontalPanel1 = new HorizontalPanel();
   // Content
   HorizontalPanel horizontalPanel2 = new HorizontalPanel();
   // Footer
   HorizontalPanel horizontalPanel3 = new HorizontalPanel();

   Menu menu;

   // CurrentPage
   Page currentPage;

   // All known pages:
   Page accountShow;
   Page accountImport;
   Page accountExport;

   private Map<String, Page> pages = new TreeMap<String, Page>();

   final RemoteFacade remoteFacade;

   public Gui(RemoteFacade remoteFacade) {
      this.remoteFacade = remoteFacade;
      accountImport = new AccountImport(this.remoteFacade);
      accountShow = new AccountShow();
      initPanelStyles();
      menu = new Menu(this);
      horizontalPanel1.add(menu.buildMainMenu());
      pages.put(accountShow.getName(), accountShow);
      pages.put(accountImport.getName(), accountImport);

      showPage(PAGE_ACCOUNT_SHOW);

   }

   private void initPanelStyles() {
      verticalPanel.setStylePrimaryName("verticalTop");
      verticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
      verticalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);

      horizontalPanel1.setStylePrimaryName("horizontal1");
      horizontalPanel1.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
      horizontalPanel1.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);

      horizontalPanel2.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
      horizontalPanel2.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
      horizontalPanel2.setStylePrimaryName("horizontal2");
      horizontalPanel2.setBorderWidth(1);

      horizontalPanel3.setStylePrimaryName("horizontal3");

      verticalPanel.add(horizontalPanel1);
      verticalPanel.add(horizontalPanel2);
      verticalPanel.add(horizontalPanel3);
   }

   public void showPage(String page) {
      //      Window.alert(page);
      if (currentPage != null) {
         int widgetIndex = horizontalPanel2.getWidgetIndex(currentPage.getRootWidget());
         if (widgetIndex > -1) {
            horizontalPanel2.remove(currentPage.getRootWidget());
         }
      }

      if (PAGE_ACCOUNT_SHOW.equals(page)) {

         horizontalPanel2.add(accountShow.getRootWidget());
         currentPage = accountShow;
      } else if (PAGE_ACCOUNT_IMPORT.equals(page)) {
         horizontalPanel2.add(accountImport.getRootWidget());
         currentPage = accountImport;
      } else {

      }

   }

   public Widget getRootElement() {
      return this.verticalPanel;
   }

   public RemoteFacade getRemoteFacade() {
      return remoteFacade;
   }

}
