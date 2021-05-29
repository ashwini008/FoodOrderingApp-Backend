package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrdersEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

//This Class is created to access DB with respect to Order entity

@Repository
public class OrderDao {
    @PersistenceContext
    private EntityManager entityManager;

    //to save order in db
    public OrdersEntity saveOrder(OrdersEntity ordersEntity) {
        entityManager.persist(ordersEntity);
        return ordersEntity;
    }

    //To get List of order from the db Corresponding to Customers
    public List<OrdersEntity> getOrdersByCustomers(CustomerEntity customerEntity) {
        try {
            List<OrdersEntity> ordersEntities = entityManager.createNamedQuery("getOrdersByCustomers",OrdersEntity.class).setParameter("customer", customerEntity).getResultList();
            return ordersEntities;
        }catch (NoResultException nre) {
            return null;
        }
    }

    //To get all the order corresponding to the address
    //AddressEntity needed
}
