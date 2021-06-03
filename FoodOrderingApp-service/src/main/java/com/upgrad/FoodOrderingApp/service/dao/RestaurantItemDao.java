package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantItemEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

/* Interacting With Database Repository */
@Repository
public class RestaurantItemDao {

    @PersistenceContext
   private  EntityManager entityManager;

    /* Get Items By Restaurant */
    public List<RestaurantItemEntity> getItemByRestaurant(RestaurantEntity restaurantEntity) {
        try {
            List<RestaurantItemEntity> restaurantItemEntities = entityManager.createNamedQuery("getItemsByRestaurant", RestaurantItemEntity.class).setParameter("restaurantId", restaurantEntity.getId()).getResultList();
            return restaurantItemEntities;
        } catch (NoResultException nre) {
            return null;
        }
    }

}