/**
 * 
 */
package com.redhat.test;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.apache.camel.CamelContext;
import org.apache.camel.ExchangePattern;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.DefaultCamelContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.redhat.CustomRouter;

/**
 * @author vgohel
 * 
 */
public class CustomRouteTest {
	private static final String DIRECT_SOURCE = "direct:source";
	private static final String MOCK_OTHER = "mock:other";
	private static final String MOCK_USD = "mock:usd";
	private static final String MOCK_EURO = "mock:euro";
	private static final String SAMPLE_DIR_FILE_PATH = "sample.directory.path";

	private CamelContext camelContext;
	private ProducerTemplate producerTemplate;

	private CustomRouter customRoute;

	private MockEndpoint mockOther;
	private MockEndpoint mockEuro;
	private MockEndpoint mockUsd;
	private File sampleDataDirectory;

	/**
	 * 
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		camelContext = new DefaultCamelContext();
		customRoute = new CustomRouter();
		customRoute.sourceUri = DIRECT_SOURCE;
		customRoute.euroUri = MOCK_EURO;
		customRoute.usdUri = MOCK_USD;
		customRoute.otherUri = MOCK_OTHER;

		mockOther = camelContext.getEndpoint(MOCK_OTHER, MockEndpoint.class);
		mockUsd = camelContext.getEndpoint(MOCK_USD, MockEndpoint.class);
		mockEuro = camelContext.getEndpoint(MOCK_EURO, MockEndpoint.class);

		sampleDataDirectory = new File(
				System.getProperty("sample.directory.path"));

		camelContext.addRoutes(customRoute);
		camelContext.start();

		producerTemplate = camelContext.createProducerTemplate();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		camelContext.stop();
	}

	@Test
	public void shouldRouteEuroPaymentsToEuroQueue() throws Exception {

		mockEuro.expectedMessageCount(1);
		mockUsd.expectedMessageCount(0);
		mockOther.expectedMessageCount(0);

		producerTemplate.sendBody(DIRECT_SOURCE, ExchangePattern.InOnly,
				getSampleMessage("VariousEUPayments.xml"));

		assertMockEndpoints(100);
	}

	@Test
	public void shouldRouteUsPaymentsToUsQueue() throws Exception {

		mockEuro.expectedMessageCount(0);
		mockUsd.expectedMessageCount(1);
		mockOther.expectedMessageCount(0);

		producerTemplate.sendBody(DIRECT_SOURCE, ExchangePattern.InOnly,
				getSampleMessage("VariousUSPayments.xml"));

		assertMockEndpoints(100);
	}

	@Test
	public void shouldRouteUnknownPaymentsToUnknownQueue() throws Exception {
		// Set expectations
		mockEuro.expectedMessageCount(0);
		mockUsd.expectedMessageCount(0);
		mockOther.expectedMessageCount(1);

		// Perform Test
		producerTemplate.sendBody(DIRECT_SOURCE, ExchangePattern.InOnly,
				getSampleMessage("VariousUnknownPayments.xml"));

		// Assert
		assertMockEndpoints(100);
	}

	private String getSampleMessage(String pFileName) throws Exception {
		StringBuffer sb = new StringBuffer(1024);

		BufferedReader input = new BufferedReader(new InputStreamReader(
				new FileInputStream(new File(sampleDataDirectory, pFileName))));

		for (String line = input.readLine(); line != null; line = input
				.readLine()) {
			sb.append(line);
		}

		return sb.toString();
	}

	private void assertMockEndpoints(long delayMs) throws Exception {
		// Assert
		mockEuro.setResultWaitTime(delayMs);
		mockEuro.assertIsSatisfied();
		mockUsd.setResultWaitTime(delayMs);
		mockUsd.assertIsSatisfied();
		mockOther.setResultWaitTime(delayMs);
		mockOther.assertIsSatisfied();
	}

}
