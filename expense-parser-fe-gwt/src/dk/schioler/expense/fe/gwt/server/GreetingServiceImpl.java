package dk.schioler.expense.fe.gwt.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import dk.schioler.expense.fe.gwt.client.GreetingService;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements GreetingService {

   public String greetServer(String input) throws IllegalArgumentException {


      StringBuffer xml = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
      xml.append("<expense-parser>");
      xml.append("<account>");
      xml.append(input);
      xml.append("</account>");
      xml.append("</expense-parser>");


      return xml.toString();
   }


}
