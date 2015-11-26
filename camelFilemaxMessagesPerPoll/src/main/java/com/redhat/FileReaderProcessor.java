/**
 * 
 */
package com.redhat;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.model.FromDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author vgohel
 *
 */
public class FileReaderProcessor implements Processor {
	private CamelContext camelContext;
	private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.camel.Processor#process(org.apache.camel.Exchange)
	 */
	public void process(Exchange exchange) throws Exception {
		String nameString = (String) exchange.getIn().getHeader("CamelFileName");		
		logger.info("Files processed : " + nameString);		
	}

	/**
	 * @return the camelContext
	 */
	public CamelContext getCamelContext() {
		return camelContext;
	}

	/**
	 * @param camelContext the camelContext to set
	 */
	public void setCamelContext(CamelContext camelContext) {
		this.camelContext = camelContext;
	}

}
