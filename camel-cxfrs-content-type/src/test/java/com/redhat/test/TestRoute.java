package com.redhat.test;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;

import java.util.Iterator;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;


public class TestRoute extends CamelTestSupport {
	private Logger logger = LoggerFactory.getLogger(TestRoute.class);

	protected RouteBuilder[] createRouteBuilders() throws Exception {
		return new RouteBuilder[] { new RouteBuilder() {
			public void configure() {
				from("cxfrs://http://localhost:8123?resourceClasses=com.redhat.Service&loggingFeatureEnabled=true")
						.setHeader(Exchange.CONTENT_TYPE, constant("application/json")).process(new Processor() {

					public void process(Exchange exchange) throws Exception {
						Map<String, Object> headMap = exchange.getIn().getHeaders();
						Iterator<Map.Entry<String, Object>> entries = headMap.entrySet().iterator();
						while (entries.hasNext()) {
							Map.Entry<String, Object> entry = entries.next();
							logger.info("Key : " + entry.getKey() + ", Value : " + entry.getValue());
						}
						exchange.getIn().setBody(exchange);
					}
				}).to("log:cxfrs8080").setBody().simple("<done/>");
			}
		} };
	}

	@Test
	public void testPostConsumer() throws Exception {
		HttpPost post = new HttpPost("http://localhost:8123/");
		post.addHeader("Accept", "text/xml");
		StringEntity entity = new StringEntity("{'id': 1}");
		// entity.setContentType("application/json");
		post.setEntity(entity);
		HttpClient httpclient = new DefaultHttpClient();

		try {
			HttpResponse response = httpclient.execute(post);
			assertEquals(200, response.getStatusLine().getStatusCode());
			assertEquals("<done/>", EntityUtils.toString(response.getEntity()));
		} finally {
			httpclient.getConnectionManager().shutdown();
		}
	}

}
