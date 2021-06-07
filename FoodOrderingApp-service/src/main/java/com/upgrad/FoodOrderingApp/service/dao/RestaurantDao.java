package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class RestaurantDao {

    @PersistenceContext
    private EntityManager entityManager;

    //Method to get the list of all the restaurants
    public List<RestaurantEntity> restaurantsByRating() {
        try{
            return entityManager.createNamedQuery("restaurantsByRating", RestaurantEntity.class)
                    .getResultList();
        } catch (NoResultException nre) {
            return null;
        }
    }

    //Method to get the restaurants by its id
    public RestaurantEntity getRestaurantByUuid(String uuid) {
        try {
            RestaurantEntity restaurantEntity = entityManager.createNamedQuery("getRestaurantByUuid", RestaurantEntity.class).setParameter("uuid", uuid).getSingleResult();
            return restaurantEntity;
        } catch (NoResultException nre) {
            return null;
        }
    }

    // Method to get the restaurants by its name
    public List<RestaurantEntity> restaurantsByName(String restaurantName) {
        try {
            String restaurantNameLow = "%" + restaurantName.toLowerCase() + "%";
            List<RestaurantEntity> restaurantEntities = entityManager.createNamedQuery("restaurantByName", RestaurantEntity.class).setParameter("restaurant_name_lower", restaurantNameLow).getResultList();
            return restaurantEntities;
        } catch (NoResultException nre) {
            return null;
        }
    }

    // method to update restaurant ratings
    public RestaurantEntity updateRestaurantRating(RestaurantEntity restaurantEntity) {
        return entityManager.merge(restaurantEntity);
    }

    // method to update restaurant details
    public RestaurantEntity updateRestaurantDetails(RestaurantEntity restaurantEntity) {
        return entityManager.merge(restaurantEntity);
    }

}
