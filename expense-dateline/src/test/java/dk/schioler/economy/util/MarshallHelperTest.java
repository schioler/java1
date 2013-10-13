package dk.schioler.economy.util;

import java.io.IOException;

import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;

import dk.schioler.economy.expenseparser.schema.ExpenseParserConfigType;

public class MarshallHelperTest {

	@Test
	public void testLoadUserConfig() {
		AbstractApplicationContext context = SpringFrameworkHelper.getDefaultApplicationContext();
		MarshallHelper helper = (MarshallHelper) context.getBean("marshallHelper");
		try {
		   ExpenseParserConfigType userConfig = helper.loadUserConfig("src/test/resources/account-files/danske/expense-parser-test-danske.xml");
			System.out.println(userConfig.getUser().getOwner());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
