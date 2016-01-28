/**
 * 
 */
package com.redhat;

import javax.jws.WebService;

/**
 * @author Viral Gohel
 *
 */
@WebService
public class HelloServiceImpl implements HelloService {
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.redhat.HelloService#sayHello(java.lang.String)
	 */
	@Override
	public String sayHello(String name) {
		return "Hello : " + name;
	}
}
