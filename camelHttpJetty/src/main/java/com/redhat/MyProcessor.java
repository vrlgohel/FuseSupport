/**
 * 
 */
package com.redhat;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/**
 * @authored by vgohel on Nov 6, 2014
 * 
 */
public class MyProcessor implements Processor {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.camel.Processor#process(org.apache.camel.Exchange)
	 */
	public void process(Exchange exchange) throws Exception {
		// TODO Auto-generated method stub
		String param = (String) exchange.getIn().getHeader("name");
		exchange.getIn().setBody(
				"<html><body>Name is " + param + "</body></html>");
	}

}
