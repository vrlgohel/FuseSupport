package com.redhat.test;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

import com.redhat.DynamicRouter;

/**
 * @author Viral Gohel
 *
 */
public class DynamicRouterTest extends CamelTestSupport {

	@Override
	protected RouteBuilder createRouteBuilder() throws Exception {
		return new DynamicRouter();
	}

	@Test
	public void testDynamicRouter() throws Exception {
		getMockEndpoint("mock:a").expectedMessageCount(1);
		getMockEndpoint("mock:b").expectedMessageCount(1);
		getMockEndpoint("mock:c").expectedMessageCount(1);
		getMockEndpoint("mock:other").expectedMessageCount(1);
		getMockEndpoint("mock:result").expectedMessageCount(1);

		template.sendBody("direct:start", "Camel Rocks !!");
		assertMockEndpointsSatisfied();
	}
}
