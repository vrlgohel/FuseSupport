/**
 * 
 */
package com.redhat;

/**
 * @author vgohel
 *
 */
public class ExampleBean {
	
	public String message;

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	public void sayHello() {
		System.out.println("Viral's testing ....");
	}
	
}
