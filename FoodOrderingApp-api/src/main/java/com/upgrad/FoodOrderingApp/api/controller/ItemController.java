package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.ItemList;
import com.upgrad.FoodOrderingApp.api.model.ItemListResponse;
import com.upgrad.FoodOrderingApp.service.businness.ItemService;
import com.upgrad.FoodOrderingApp.service.businness.RestaurantService;
import com.upgrad.FoodOrderingApp.service.common.ItemType;
import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
public class ItemController {

    @Autowired
    private ItemService itemService;

    @Autowired
    private RestaurantService restaurantService;

    /**
     * This controller method fetches the top 5 popular items from restaurant
     * @param restaurantId
     * @return
     * @throws RestaurantNotFoundException
     */
    @RequestMapping(method = RequestMethod.GET, path = "/item/restaurant/{restaurant_id}",
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<ItemListResponse> getTopFiveItems(@PathVariable("restaurant_id") final String restaurantId)
        throws RestaurantNotFoundException {

        final RestaurantEntity restaurantEntity = restaurantService.restaurantByUUID(restaurantId);
        final List<ItemEntity> itemEntities = itemService.getItemsByPopularity(restaurantEntity);
        final ItemListResponse itemLists = new ItemListResponse();

        if (!itemEntities.isEmpty()) {
            itemEntities.forEach(itemEntity -> itemLists.add(createItemListData(itemEntity)));
        }

        return new ResponseEntity<>(itemLists, HttpStatus.OK);
    }

    // method to generate data set
    private ItemList createItemListData(final ItemEntity itemEntity) {
        final ItemList itemList = new ItemList();
        itemList.id(UUID.fromString(itemEntity.getUuid()))
            .itemName(itemEntity.getItemName())
            .price(itemEntity.getPrice());

        final String itemType = itemEntity.getType().equals("0") ? ItemType.VEG : ItemType.NON_VEG;
        itemList.itemType(ItemList.ItemTypeEnum.fromValue(itemType));

        return itemList;
    }
}
