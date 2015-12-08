package sample;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * 
 */

/**
 * @author vgohel
 *
 */
public class ChoiceTest extends CamelTestSupport {

	@EndpointInject
	private ProducerTemplate producerTemplate;

	@Override
	public boolean isUseAdviceWith() {
		return true;
	}

	/**
	 *
	 * Creates the route builder with choice definition.
	 *
	 * @return route builder with choice
	 * @throws Exception
	 */
	@Override
	protected RouteBuilder createRouteBuilder() throws Exception {

		RouteBuilder routeBuilder = new RouteBuilder() {

			@Override
			public void configure() throws Exception {
				from("direct:start").routeId("choiceTestRoute")

						.choice().id("check").when(simple("${body} == 1"))

						.process(new Processor() {

					public void process(Exchange exchange) throws Exception {
						System.out.println("I print all the time!");
					}
				})

						.end();
			}
		};
		return routeBuilder;
	}

	@Test
	public void shouldAddMockEndpointToEndOfRoute() throws Exception {

		context.getRouteDefinition("choiceTestRoute").adviceWith(context, new AdviceWithRouteBuilder() {

			@Override
			public void configure() throws Exception {

				weaveAddLast().to("mock:endpoint");

			}
		});

		MockEndpoint mockEndpoint = getMockEndpoint("mock:endpoint");
		mockEndpoint.expectedMessageCount(1);

		context.start();

		producerTemplate.sendBody("direct:start", Integer.valueOf(1));

		assertMockEndpointsSatisfied();
	}
}
