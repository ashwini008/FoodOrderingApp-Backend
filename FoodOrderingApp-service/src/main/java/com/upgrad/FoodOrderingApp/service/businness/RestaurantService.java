package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CategoryDao;
import com.upgrad.FoodOrderingApp.service.dao.RestaurantDao;
import com.upgrad.FoodOrderingApp.service.dao.RestaurantCategoryDao;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantCategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.InvalidRatingException;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class RestaurantService {

    @Autowired
    RestaurantDao restaurantDao;

    @Autowired
    CategoryDao categoryDao;

    @Autowired
    RestaurantCategoryDao restuarantCategoryDao;

    // this method call the getAllRestaurants methods from RestaurantDao to get the list of all the restaurants
    @Transactional(propagation = Propagation.REQUIRED)
    public List<RestaurantEntity> getAllRestaurants() {
        return restaurantDao.getAllRestaurants();
    }
    // method to get restaurant by category id
    public List<RestaurantEntity> restaurantByCategory(String categoryId) throws CategoryNotFoundException {
        if (categoryId == null || categoryId == "") {
            throw new CategoryNotFoundException("CNF-001", "Category id field should not be empty");
        }

        CategoryEntity categoryEntity = categoryDao.getCategoryByUuid(categoryId);
        if (categoryEntity == null) {
            throw new CategoryNotFoundException("CNF-002", "No category by this id");
        }
        List<RestaurantCategoryEntity> restaurantCategoryEntities = restuarantCategoryDao.getRestaurantsByCategoryId(categoryEntity);

        if (restaurantCategoryEntities.isEmpty()) {
            return null;
        }
        List<RestaurantEntity> restaurantEntities = new ArrayList<>();
        for (RestaurantCategoryEntity rc : restaurantCategoryEntities) {
            restaurantEntities.add(rc.getRestaurant());
        }
        return restaurantEntities;
    }

    //Method to get restaurant by name
    public List<RestaurantEntity> restaurantsByName(String restaurantName) throws RestaurantNotFoundException {

        if (restaurantName == null || restaurantName == "") {
            throw new RestaurantNotFoundException("RNF-003", "Restaurant name field should not be empty");
        }

        List<RestaurantEntity> restaurantEntities = restaurantDao.restaurantsByName(restaurantName);
        return restaurantEntities;
    }
    // Method to get restaurant by restaurant id
    public RestaurantEntity restaurantByUUID(String uuid) throws RestaurantNotFoundException {
        if (uuid == null || uuid == "" || uuid.isEmpty()) {
            throw new RestaurantNotFoundException("RNF-002", "Restaurant id field should not be empty");
        }
        RestaurantEntity restaurantEntity = restaurantDao.getRestaurantByUuid(uuid);

        if (restaurantEntity == null) {
            throw new RestaurantNotFoundException("RNF-001", "No restaurant by this id");
        }

        return restaurantEntity;
    }
    // Method to update restaurant details and ratings
    @Transactional(propagation = Propagation.REQUIRED)
    public RestaurantEntity updateRestaurantRating(RestaurantEntity restaurant, Double customerRating)
            throws InvalidRatingException {
        if (customerRating < 1.0 || customerRating > 5.0) {
            throw new InvalidRatingException("IRE-001", "Restaurant should be in the range of 1 to 5");
        }
        // calculate new average rating.
        Double newAverageRating =
                ((restaurant.getCustomerRating()) * ((double) restaurant.getNumOfCustomersRated())
                        + customerRating)
                        / ((double) restaurant.getNumOfCustomersRated() + 1);
        restaurant.setCustomerRating(newAverageRating);
        restaurant.setNumberCustomersRated(
                restaurant.getNumOfCustomersRated()
                        + 1); // update the number of customers who gave rating
        return restaurantDao.updateRestaurantRating(restaurant);
    }

}
