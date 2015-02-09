package com.redhat;

/**
 * Created by Viral Gohel
 */
public interface CustomerService {
    public void save(Customer customer);
    public void rollback(Customer customer) throws Exception;
    Customer findCustomer(String name);
}
