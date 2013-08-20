package dk.schioler.economy.util;

import java.io.FileInputStream;
import java.io.IOException;

import javax.xml.bind.JAXBElement;
import javax.xml.transform.stream.StreamSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.Unmarshaller;
import org.springframework.stereotype.Component;

import dk.schioler.economy.accountparser.schema.AccountParserType;

@Component("marshallHelper")
public class MarshallHelper {

	@Autowired
	private Unmarshaller unmarshaller;

	public MarshallHelper() {
		super();
		System.out.println("MarshallHelper.MarshallHelper()");
	}

	@SuppressWarnings("unchecked")
	public AccountParserType loadUserConfig(String file) throws IOException {
		FileInputStream is = null;
		AccountParserType rv = null;
		try {
			is = new FileInputStream(file);
			JAXBElement<AccountParserType> jb = (JAXBElement<AccountParserType>) this.unmarshaller
					.unmarshal(new StreamSource(is));

			rv = jb.getValue();
		} finally {
			if (is != null) {
				is.close();
			}
		}
		return rv;
	}
}
