package dk.schioler.expense.fe.gwt.client;

import com.google.gwt.user.client.ui.Widget;

public interface Page {
   public Widget getRootWidget();
//   public void build();
   public String getName();
   public void show();
}
