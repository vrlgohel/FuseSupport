package com.redhat;

import org.apache.camel.builder.RouteBuilder;

/**
 * @author vgohel
 *
 */
public class MuticastExceptionTest extends RouteBuilder {

	/*
	 * @see org.apache.camel.builder.RouteBuilder#configure()
	 */
	@Override
	public void configure() throws Exception {

		/*
		 * You can optionally set the exception thrown by the route to handle it
		 * in a custom manner.
		 * 
		 * onException(Exception.class).handled(true).log("log:onException")
		 * .to("mock:endpoint4").transform(constant("Stopped !!!"));
		 */
		from("direct:start")
				.multicast()
				.log("Starting Executing the endpoints ......")
				.stopOnException()
				.parallelProcessing()
				.to("mock:endpoint1", "mock:endpoint2", "mock:endpoint4",
						"mock:endpoint3")
				.transform(constant("Hello to you now !!!!"));
	}

}
