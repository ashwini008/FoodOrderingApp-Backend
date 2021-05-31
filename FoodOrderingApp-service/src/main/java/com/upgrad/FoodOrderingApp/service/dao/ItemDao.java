package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class ItemDao {

    @PersistenceContext
    private EntityManager entityManager;

    public List<ItemEntity> getItemsByPopularity(final long restaurantId) {
        return entityManager.createNamedQuery("topFiveItemsByRestaurant", ItemEntity.class)
            .setParameter(0, restaurantId)
            .getResultList();
    }
}
