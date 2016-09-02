/**
 *
 */
package com.redhat.client;

import java.util.List;

import com.example.customerservice.Customer;
import com.example.customerservice.CustomerService;
import com.example.customerservice.NoSuchCustomerException;



/**
 * @author Viral Gohel
 *
 */
public class CustomerServiceTester{

	CustomerService customerService;
	List<Customer> customers = null;

	public CustomerService getCustomerService() {
		return customerService;
	}

	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}

	public void testCustomerService() throws NoSuchCustomerException {

		
		System.out.println("Sending request for customers named Smith");
		customers = customerService.getCustomersByName("Smith");
		System.out.println("Response received");
		Customer customer = new Customer();
		customer.setName("Smith");
		customerService.updateCustomer(customer);

		System.out.println("All calls were successful");
	}	
}
