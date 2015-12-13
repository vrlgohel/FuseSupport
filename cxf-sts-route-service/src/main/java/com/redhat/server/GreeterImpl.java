/**
 * 
 */
package com.redhat.server;

import org.apache.hello_world_soap_http.Greeter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Viral Gohel
 *
 */
@javax.jws.WebService(name = "Greeter", serviceName = "SOAPService", targetNamespace = "http://apache.org/hello_world_soap_http", wsdlLocation = "file:./wsdl/hello_world.wsdl")

public class GreeterImpl implements Greeter {

	private static final Logger LOG = LoggerFactory.getLogger(GreeterImpl.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.objectweb.hello_world_soap_http.Greeter#greetMe(java.lang.String)
	 */
	public String greetMe(String me) {
		LOG.info("Executing operation greetMe");
		System.out.println("Executing operation greetMe");
		System.out.println("Message received: " + me + "\n");
		return "Hello " + me;
	}
}
