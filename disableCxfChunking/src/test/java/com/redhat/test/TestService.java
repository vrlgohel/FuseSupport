package com.redhat.test;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.frontend.ClientProxyFactoryBean;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.ws.Endpoint;

/**
 * Created by Viral Gohel
 */
public class TestService extends Assert{
    private static Logger logger= LoggerFactory.getLogger(TestService.class);

    @BeforeClass
    public static void prepareEndpoint() throws Exception{
        logger.info("Publishing Endpoint .............");
        Endpoint.publish("http://localhost:9002/disableChunking",new HelloWSImpl());
        logger.info("Endpoint is now published ....");
        Thread.sleep(2000);
    }

    @Test
    public void testService() throws Exception{
        ClientProxyFactoryBean clientProxyFactoryBean=new ClientProxyFactoryBean();
        clientProxyFactoryBean.setAddress("http://localhost:9002/disableChunking");
        clientProxyFactoryBean.setServiceClass(HelloWs.class);
        HelloWs port= (HelloWs) clientProxyFactoryBean.create();
        Client client= ClientProxy.getClient(port);
        HTTPConduit httpConduit= (HTTPConduit) client.getConduit();
        HTTPClientPolicy httpClientPolicy=new HTTPClientPolicy();
        httpClientPolicy.setAllowChunking(false);
        httpConduit.setClient(httpClientPolicy);

        client.getInInterceptors().add(new LoggingInInterceptor());
        client.getOutInterceptors().add(new LoggingOutInterceptor());
        StringBuilder crazyLongName = new StringBuilder();
        for(int i=0; i<800; i++)
        {
            crazyLongName.append("BLA BLA BLA" );
        }
        logger.info("Endpoint is invoked now ...");

        port.hello(crazyLongName.toString()).toString();
    }
}
