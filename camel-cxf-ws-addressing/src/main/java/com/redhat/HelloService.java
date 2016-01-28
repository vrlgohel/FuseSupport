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
public interface HelloService {

	public String sayHello(String name);
}
