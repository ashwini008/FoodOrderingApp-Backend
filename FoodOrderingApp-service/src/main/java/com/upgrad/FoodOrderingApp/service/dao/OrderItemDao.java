package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.OrderItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrderEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

//This Class is created to access DB with respect to OrderItem entity

@Repository
public class OrderItemDao {

    @PersistenceContext
    private EntityManager entityManager;


    //To get the items by order
    public List<OrderItemEntity> getItemsByOrders(OrderEntity orderEntity) {
        try{
            List<OrderItemEntity> orderItemEntities = entityManager.createNamedQuery("getItemsByOrders", OrderItemEntity.class).setParameter("ordersEntity", orderEntity).getResultList();
            return orderItemEntities;
        }catch (NoResultException nre) {
            return null;
        }
    }

    // Method to save order item to DB
    public OrderItemEntity saveOrderItem(OrderItemEntity orderItemEntity){
        entityManager.persist(orderItemEntity);
        return orderItemEntity;
    }

    // method to fetch order items of an order
    public List<OrderItemEntity> getOrderItemsByOrder(OrderEntity orderEntity) {
        try {
            List<OrderItemEntity> orderItemEntities = entityManager.createNamedQuery("getOrderItemsByOrder",OrderItemEntity.class).setParameter("orders", orderEntity).getResultList();
            return orderItemEntities;
        }catch (NoResultException nre){
            return null;
        }
    }
}
