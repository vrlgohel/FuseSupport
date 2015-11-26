/**
 * 
 */
package com.redhat;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author vgohel
 *
 */
public class SpringServer {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		new ClassPathXmlApplicationContext("META-INF/spring/camelContext.xml");		
		System.in.read();		
		System.exit(0);
		
	}

}
