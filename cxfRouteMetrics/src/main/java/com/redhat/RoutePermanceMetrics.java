/**
 * 
 */
package com.redhat;

import javax.jws.WebService;

/**
 * @author vgohel
 *
 */
@WebService
public class RoutePermanceMetrics implements RouteMetrics {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.redhat.RouteMetrics#sayHi(java.lang.String)
	 */
	public String sayHi(String name) {
		return "Web service returned : " + name;
	}
}
