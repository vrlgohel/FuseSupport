package com.redhat.test;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author vgohel
 *
 */
public class StopMulticastExceptionTest extends CamelSpringTestSupport {

	/*
	 * @see
	 * org.apache.camel.test.spring.CamelSpringTestSupport#createApplicationContext
	 * ()
	 */
	@Override
	protected AbstractApplicationContext createApplicationContext() {
		return new ClassPathXmlApplicationContext("META-INF/spring/context.xml");
	}

	@Produce(uri = "direct:start")
	protected ProducerTemplate producerTemplate;

	@EndpointInject(uri = "mock:endpoint1")
	protected MockEndpoint endpoint1;

	@EndpointInject(uri = "mock:endpoint2")
	protected MockEndpoint endpoint2;

	@EndpointInject(uri = "mock:endpoint3")
	protected MockEndpoint endpoint3;

	@EndpointInject(uri = "mock:endpoint4")
	protected MockEndpoint endpoint4;

	@Test
	public void endpoint1FailureTest() throws Exception {
		endpoint1.whenAnyExchangeReceived(new Processor() {

			public void process(Exchange exchange) throws Exception {
				throw new RuntimeException(
						"We got a Runtime Exception in endpoint1 failure....");
			}
		});
		endpoint2.expectedMinimumMessageCount(0);
		endpoint3.expectedMessageCount(0);
		endpoint4.expectedMessageCount(1);

		String result = producerTemplate.requestBody("direct:start",
				"Viral's Test for endpoint1 failure", String.class);
		assertEquals("Stop", result);
		assertMockEndpointsSatisfied();
	}

	@Test
	public void endpoint2FailureTest() throws Exception {
		endpoint2.whenAnyExchangeReceived(new Processor() {

			public void process(Exchange exchange) throws Exception {
				throw new RuntimeException(
						"We got a Runtime Exception in endpoint2 failure ....");
			}
		});
		endpoint1.expectedMinimumMessageCount(0);
		endpoint3.expectedMessageCount(0);
		endpoint4.expectedMessageCount(1);

		String result = producerTemplate.requestBody("direct:start",
				"Viral's Test for endpoint2 failure", String.class);
		assertEquals("Stop", result);
		assertMockEndpointsSatisfied();
	}
}
