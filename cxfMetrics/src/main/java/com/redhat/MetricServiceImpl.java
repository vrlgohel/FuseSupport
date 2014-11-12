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
public class MetricServiceImpl implements MetricService{

	public String sayHello(String name) {		
		return "Web Service returned : " + name;
	}
}
