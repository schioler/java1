package dk.schioler.economy.util;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;

public class SpringFrameworkHelperTest {

	static {
		Log4JLoader.loadLog();
	}
	@Test
	public void testGetDefaultApplicationContext() {
		AbstractApplicationContext applicationContext = SpringFrameworkHelper.getDefaultApplicationContext();
		
	}

}
