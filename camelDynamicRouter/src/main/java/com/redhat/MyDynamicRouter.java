package com.redhat;

import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Viral Gohel
 *
 */
public class MyDynamicRouter {
	private static final String PROPERTY_NAME_INVOKED = "invoked";
	private static final Logger logger = LoggerFactory
			.getLogger(MyDynamicRouter.class);

	public String routeMe(String body,
			@Properties Map<String, Object> properties) {
		logger.info("Exchange.SLIP_ENDPOINT={}, invoked={}",
				properties.get(Exchange.SLIP_ENDPOINT),
				properties.get(PROPERTY_NAME_INVOKED));
		int invoked = 0;

		Object current = properties.get(PROPERTY_NAME_INVOKED);
		if (current != null)
			invoked = Integer.valueOf(current.toString());
		invoked++;

		properties.put(PROPERTY_NAME_INVOKED, invoked);

		if (invoked == 1) {
			return "mock:a";
		} else if (invoked == 2) {
			return "mock:b,mock:c";
		} else if (invoked == 3) {
			return "direct:other";
		} else if (invoked == 4) {
			return "mock:result";
		}
		return null;
	}
}
