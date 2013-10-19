package dk.schioler.expense.fe.gwt.client;

import java.util.TreeMap;

import com.google.gwt.core.client.GWT;

public class RemoteFacadeExpenseImpl implements RemoteFacade {

   private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);

   TreeMap<String, Object> services = new TreeMap<String, Object>();

   public RemoteFacadeExpenseImpl() {
      services.put(GreetingServiceAsync.class.getName(), greetingService);
   }

   @Override
   public Object getService(String name) {

      return services.get(name);
   }

}
