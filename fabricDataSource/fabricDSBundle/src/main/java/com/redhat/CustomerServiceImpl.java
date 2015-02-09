package com.redhat;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Created by Viral Gohel
 */
public class CustomerServiceImpl implements CustomerService {

    private final EntityManager entityManager;

    public CustomerServiceImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void save(Customer customer) {
        entityManager.persist(customer);
    }

    @Override
    public void rollback(Customer customer) throws Exception{
        save(customer);
        throw new Exception();
    }

    @Override
    public Customer findCustomer(String name) {
        List<Customer> customers=entityManager.createQuery("select c from Customer c where c.name=:name",Customer.class)
                .setParameter("name",name)
                .getResultList();
        if (customers.isEmpty())
            return null;
        return customers.get(0);
    }
}
