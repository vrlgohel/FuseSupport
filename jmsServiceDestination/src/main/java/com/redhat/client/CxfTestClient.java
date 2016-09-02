/**
 *
 */
package com.redhat.client;

import org.apache.cxf.common.i18n.Exception;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.example.customerservice.NoSuchCustomerException;

/**
 * @author Viral Gohel
 *
 */
public class CxfTestClient {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception, NoSuchCustomerException {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("client-app.xml");
		CustomerServiceTester client = (CustomerServiceTester) context.getBean("tester");

		client.testCustomerService();
		context.destroy();
		context.stop();
	}

}
