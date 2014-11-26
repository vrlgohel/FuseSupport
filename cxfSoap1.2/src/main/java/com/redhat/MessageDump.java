/**
 * 
 */
package com.redhat;

import javax.xml.soap.SOAPMessage;

import org.apache.camel.Exchange;
import org.apache.camel.component.cxf.CxfConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author vgohel
 *
 */
public class MessageDump {

	private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

	public SOAPMessage processMessage(Exchange exchange) {
		SOAPMessage soapMessage = (SOAPMessage) exchange.getIn().getHeader(
				CxfConstants.CAMEL_CXF_MESSAGE, SOAPMessage.class);
		logger.info("Got the soap message ......");
		return soapMessage;
	}
}
