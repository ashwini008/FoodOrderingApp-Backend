package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CategoryDao;
import com.upgrad.FoodOrderingApp.service.dao.ItemDao;
import com.upgrad.FoodOrderingApp.service.dao.RestaurantDao;
import com.upgrad.FoodOrderingApp.service.dao.RestaurantItemDao;
import com.upgrad.FoodOrderingApp.service.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class ItemService {

    @Autowired
    private ItemDao itemDao;

    @Autowired
    private RestaurantDao restaurantDao;

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private RestaurantItemDao restaurantItemDao;

    public List<ItemEntity> getItemsByPopularity(final RestaurantEntity restaurantEntity) {
        return itemDao.getItemsByPopularity(restaurantEntity.getId());
    }

    // Method to get Items By filtering Category UUID and Restaurant UUID - Returns list of items in a category in a restaurant

    public List<ItemEntity> getItemsByCategoryAndRestaurant(String restaurantUuid, String categoryUuid) {

        RestaurantEntity restaurantEntity = restaurantDao.getRestaurantByUuid(restaurantUuid);

        CategoryEntity categoryEntity = categoryDao.getCategoryByUuid(categoryUuid);

        List<RestaurantItemEntity> restaurantItemEntities = restaurantItemDao.getItemByRestaurant(restaurantEntity);

        List<CategoryItemEntity> categoryItemEntities = categoryDao.getItemByCategory(categoryEntity);

        List<ItemEntity> itemEntities = new LinkedList<>();
        restaurantItemEntities.forEach(restaurantItemEntity -> {
            categoryItemEntities.forEach(categoryItemEntity -> {
                if (restaurantItemEntity.getItem().equals(categoryItemEntity.getItem())) {
                    itemEntities.add(restaurantItemEntity.getItem());
                }
            });
        });
        return itemEntities;
    }
}

