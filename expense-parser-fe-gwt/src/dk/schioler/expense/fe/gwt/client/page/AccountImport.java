package dk.schioler.expense.fe.gwt.client.page;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

import dk.schioler.expense.fe.gwt.client.GreetingServiceAsync;
import dk.schioler.expense.fe.gwt.client.Gui;
import dk.schioler.expense.fe.gwt.client.Page;
import dk.schioler.expense.fe.gwt.client.RemoteFacade;

public class AccountImport implements Page {

   final String name = Gui.PAGE_ACCOUNT_IMPORT;
   final Widget root;
   final RemoteFacade facade;

   ClickHandler onClickHandler = new ClickHandler() {
      public void onClick(ClickEvent event) {
         service.greetServer("importAccountFromClient", new AsyncCallback<String>() {

            @Override
            public void onSuccess(String result) {
               Document doc = XMLParser.parse(result);
               Node item = doc.getElementsByTagName("account").item(0);
               Element element = (Element) item;
               String contentString = "";
               NodeList childrenList = element.getChildNodes();
               for (int i = 0; i < childrenList.getLength(); i++) {
                  Node n = childrenList.item(i);
                  contentString = contentString + "name='"+n.getNodeName() + "':" + n.getNodeValue() + ", \r\n ";
               }

               tBox.setText(element.getNodeName() + ", Content:\n\r" + contentString);

            }

            @Override
            public void onFailure(Throwable caught) {

            }
         });
      }
   };
   private GreetingServiceAsync service;
   Button b;
   VerticalPanel vPanel;
   TextArea tBox;

   public AccountImport(RemoteFacade facade) {
      this.facade = facade;
      service = (GreetingServiceAsync) facade.getService(GreetingServiceAsync.class.getName());

      tBox = new TextArea();
      tBox.setText("Initial text");
      tBox.setStylePrimaryName("textBoxAccount");

      b = new Button("Jump!", onClickHandler);

      VerticalPanel vPanel = new VerticalPanel();
      vPanel.add(tBox);
      vPanel.add(b);

      root = vPanel;
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

}
