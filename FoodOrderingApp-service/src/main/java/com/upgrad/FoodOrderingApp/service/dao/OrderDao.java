package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrderEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class OrderDao {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Method to fetch orders by address from DB
     * @param addressEntity
     * @return
     */
    public List<OrderEntity> getOrdersByAddress(AddressEntity addressEntity) {
        return entityManager.createNamedQuery("getOrdersByAddress", OrderEntity.class)
            .setParameter("addressUUID", addressEntity.getUuid())
            .getResultList();
    }
}