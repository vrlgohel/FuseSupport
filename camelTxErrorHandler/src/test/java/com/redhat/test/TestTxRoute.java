package com.redhat.test;

import org.apache.camel.ExchangePattern;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Created by vgohel on 8/31/15.
 */
public class TestTxRoute extends CamelSpringTestSupport {
    @Override
    protected AbstractApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("META-INF/spring/camelRoute.xml");
    }

    @Test
    public void testRoute() throws Exception{
        String filename="src/jmsMessage/person.xml";
        InputStream inputStream=new FileInputStream(filename);
        template.sendBody("activemq:queue:testQueueIn", ExchangePattern.InOnly,inputStream);
        assertMockEndpointsSatisfied();
    }
}
