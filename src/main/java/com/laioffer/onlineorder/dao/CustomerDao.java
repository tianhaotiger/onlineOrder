package com.laioffer.onlineorder.dao;

import com.laioffer.onlineorder.entity.Authorities;
import com.laioffer.onlineorder.entity.Customer;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CustomerDao {

    @Autowired
    private SessionFactory sessionFactory;

    public void signUp(Customer customer) {
        Authorities authorities = new Authorities();
        authorities.setAuthorities("ROLE_USER");
        authorities.setEmail(customer.getEmail());

        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.save(authorities);
            session.save(customer);
            session.getTransaction().commit();

        } catch (Exception ex) {   // 如果有重复注册，可以throw exception
            ex.printStackTrace();
            if (session != null) session.getTransaction().rollback();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public Customer getCustomer(String email) {
        Customer customer = null;
        try (Session session = sessionFactory.openSession()) {  // try with resource, auto close
            customer = session.get(Customer.class, email);  // go to the customer database to search the primary key email
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return customer;
    }
}
