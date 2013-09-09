package dk.schioler.economy.util;

import java.io.IOException;

import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;

import dk.schioler.economy.accountparser.schema.AccountParserType;

public class MarshallHelperTest {

	@Test
	public void testLoadUserConfig() {
		AbstractApplicationContext context = SpringFrameworkHelper.getDefaultApplicationContext();
		MarshallHelper helper = (MarshallHelper) context.getBean("marshallHelper");
		try {
			AccountParserType userConfig = helper.loadUserConfig("src/test/resources/user-input-sample.xml");
			System.out.println(userConfig.getOwner());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
