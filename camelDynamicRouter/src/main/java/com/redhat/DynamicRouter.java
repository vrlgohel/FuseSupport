package com.redhat;

import org.apache.camel.builder.RouteBuilder;

import com.sun.org.apache.bcel.internal.generic.NEW;

/**
 * @author Viral Gohel
 *
 */
public class DynamicRouter extends RouteBuilder{
	@Override
	public void configure() throws Exception {
		from("direct:start")
		.dynamicRouter(method(MyDynamicRouter.class,"routeMe"));
		
		from("direct:other")
		.to("mock:other");
	}

}
