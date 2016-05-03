package com.redhat;

import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by Viral Gohel
 */
public class JmsAckTest extends CamelSpringTestSupport {

    private Logger logger= LoggerFactory.getLogger(JmsAckTest.class.getName());

    @Override
    protected AbstractApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("META-INF/spring/camel-context.xml");
    }

    /**
     * here we have regular Junit @Test method
     * Note, make sure to follow this order of 'expect, send, assert'
     * Otherwise your tests will not work as expected
     */
    @Test
    public void testRoute() throws Exception {
        String address = "jms:jndi:dynamicQueues/testQueue"
                + "?jndiInitialContextFactory"
                + "=org.apache.activemq.jndi.ActiveMQInitialContextFactory"
                + "&jndiConnectionFactoryName=ConnectionFactory&jndiURL="
                + "vm://localhost";

        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setServiceClass(GreetingService.class);
        factory.setAddress(address);
        GreetingService greeter = factory.create(GreetingService.class);
        String response = greeter.echo("Viral");
        logger.info("Response :" + response);
    }

}