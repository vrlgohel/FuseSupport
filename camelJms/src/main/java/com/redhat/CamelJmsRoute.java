/**
 * 
 */
package com.redhat;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author vgohel
 *
 */
public class CamelJmsRoute extends RouteBuilder {

	private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.camel.builder.RouteBuilder#configure()
	 */
	@Override
	public void configure() throws Exception {
		from("file:input?noop=true").to("jms:incomingOrders");

		// content-based router
		from("jms:incomingOrders").choice()
				.when(header("CamelFileName").endsWith(".xml"))
				.to("jms:xmlOrders")
				.when(header("CamelFileName").regex("^.*(csv|csl)$"))
				.to("jms:csvOrders").otherwise().to("jms:badOrders");

		from("jms:xmlOrders").filter(xpath("/order[not(@test)]")).process(
				new Processor() {

					public void process(Exchange exchange) throws Exception {
						logger.info("Received XML Order :"
								+ exchange.getIn().getHeader("CamelFileName"));
					}
				});

	}
}
