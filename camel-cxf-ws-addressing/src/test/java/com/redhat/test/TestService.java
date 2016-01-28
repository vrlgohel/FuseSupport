package com.redhat.test;

import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.apache.cxf.frontend.ClientProxyFactoryBean;
import org.apache.cxf.ws.addressing.WSAddressingFeature;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.redhat.HelloService;
import com.redhat.HelloServiceImpl;

public class TestService extends CamelSpringTestSupport {
	private final Logger logger = LoggerFactory.getLogger(TestService.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.camel.test.spring.CamelSpringTestSupport#
	 * createApplicationContext()
	 */
	@Override
	protected AbstractApplicationContext createApplicationContext() {
		return new ClassPathXmlApplicationContext("META-INF/spring/context.xml");
	}

	@Test
	public void testService() throws Exception {
		ClientProxyFactoryBean factoryBean = new ClientProxyFactoryBean();
		factoryBean.setServiceClass(com.redhat.HelloService.class);
		logger.info("Service Class is set ...");
		factoryBean.setAddress("http://localhost:9000/addressing");
		factoryBean.getFeatures().add(new WSAddressingFeature());
		HelloService service = (HelloService) factoryBean.create();
		service.sayHello("Viral is the CXF Expert ....");
	}
}
