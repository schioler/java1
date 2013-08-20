package dk.schioler.economy.util;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class SpringFrameworkHelper {
	public static AbstractApplicationContext getDefaultApplicationContext() {
		return getApplicationContext("/applicationContext.xml");
	}

	public static AbstractApplicationContext getApplicationContext(String file) {
	   AbstractApplicationContext ctx =new ClassPathXmlApplicationContext(new String[] {file});
//	   ctx.registerShutdownHook();
		return ctx;
	}

}
