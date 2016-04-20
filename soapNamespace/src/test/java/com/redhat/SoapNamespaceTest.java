package com.redhat;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.frontend.ClientProxyFactoryBean;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxws.JaxWsClientFactoryBean;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.ws.Endpoint;
import java.util.Collections;
import java.util.Map;

/**
 * Created by Viral Gohel
 */
public class SoapNamespaceTest extends Assert {
    private static Logger logger = LoggerFactory.getLogger(SoapNamespaceTest.class);

    @BeforeClass
    public static void prepareEndpoint() throws Exception {
        logger.info("Publishing Endpoint ..........");
        Endpoint endpoint = Endpoint.publish("http://localhost:9002/test", new SoapNamespaceImpl());
        logger.info("****** Endpoint is not published ****");
    }

    @Test
    public void testService() throws Exception {

        //We are dropping namespace from all elements qualified by 'http://redhat.com'
        Map<String, String> outTransformMap = Collections.singletonMap("{http://redhat.com}*", "*");
        /* Replace 'http://redhat.com' with your namespace.
        * Refer more here, http://cxf.apache.org/docs/transformationfeature.html
        * */


        org.apache.cxf.interceptor.transform.TransformOutInterceptor transformOutInterceptor =
                new org.apache.cxf.interceptor.transform.TransformOutInterceptor();
        transformOutInterceptor.setOutTransformElements(outTransformMap);
        ClientProxyFactoryBean clientProxyFactoryBean = new ClientProxyFactoryBean();
        clientProxyFactoryBean.setAddress("http://localhost:9002/test");
        clientProxyFactoryBean.setServiceClass(SoapNamespaceInt.class);
        SoapNamespaceInt port = (SoapNamespaceInt) clientProxyFactoryBean.create();
        Client client = ClientProxy.getClient(port);
        client.getInInterceptors().add(new LoggingInInterceptor());
        client.getOutInterceptors().add(new LoggingOutInterceptor());
        client.getOutInterceptors().add(new StreamInterceptor());  // This interceptor is included in the outbound chain, just to replace the namespace prefix to highlight the issue
        client.getOutInterceptors().add(transformOutInterceptor);
        port.sayHi("Viral");
    }

}
