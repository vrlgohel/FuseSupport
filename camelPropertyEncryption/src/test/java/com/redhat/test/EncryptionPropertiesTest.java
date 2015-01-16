package com.redhat.test;

import com.redhat.EncryptionRoute;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jasypt.JasyptPropertiesParser;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * Created by Viral Gohel
 */
public class EncryptionPropertiesTest extends CamelTestSupport {
    /**
     * Factory method which derived classes can use to create a {@link org.apache.camel.builder.RouteBuilder}
     * to define the routes for testing
     */
    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new EncryptionRoute();
    }

    @Override
    protected CamelContext createCamelContext() throws Exception {
        JasyptPropertiesParser propertiesParser=new JasyptPropertiesParser();
        propertiesParser.setPassword("encryptionPassword");

        PropertiesComponent propertiesComponent=new PropertiesComponent();
        propertiesComponent.setLocation("classpath:encryption.properties");
        propertiesComponent.setPropertiesParser(propertiesParser);

        CamelContext camelContext=new DefaultCamelContext();
        camelContext.addComponent("properties",propertiesComponent);
        return camelContext;
    }

    @Test
    public void testProperties() throws Exception{
        MockEndpoint mockEndpoint=getMockEndpoint("mock:out");
        mockEndpoint.setExpectedMessageCount(1);
        mockEndpoint.message(0).header("customPassword").isEqualTo("myPassword");

        template.sendBody("direct:start","Bla Bla Bla !!");
        assertMockEndpointsSatisfied();
    }
}
