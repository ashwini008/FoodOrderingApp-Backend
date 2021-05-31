package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.AddressService;
import com.upgrad.FoodOrderingApp.service.businness.CustomerService;
import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import com.upgrad.FoodOrderingApp.service.exception.AddressNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SaveAddressException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
public class AddressController {

    @Autowired
    private AddressService addressService;

    @Autowired
    private CustomerService customerService;

    /**
     * This controller method is used to save the address
     * @param authorization - access token
     * @param saveAddressRequest - address object
     * @return
     * @throws AuthorizationFailedException
     * @throws SaveAddressException
     * @throws AddressNotFoundException
     */
    @RequestMapping(method = RequestMethod.POST, path = "/address",
        consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SaveAddressResponse> saveAddress(
        @RequestHeader("authorization") final String authorization,
        @RequestBody(required = false) final SaveAddressRequest saveAddressRequest)
            throws AuthorizationFailedException, SaveAddressException, AddressNotFoundException {

        final String accessToken = authorization.split("Bearer ")[1];
        final CustomerEntity customerEntity = customerService.getCustomer(accessToken);
        final StateEntity stateEntity = addressService.getStateByUUID(saveAddressRequest.getStateUuid());
        final AddressEntity addressEntity = new AddressEntity();

        addressEntity.setActive(1);
        addressEntity.setCity(saveAddressRequest.getCity());
        addressEntity.setFlatBuilNo(saveAddressRequest.getFlatBuildingName());
        addressEntity.setLocality(saveAddressRequest.getLocality());
        addressEntity.setPincode(saveAddressRequest.getPincode());
        addressEntity.setState(stateEntity);
        addressEntity.setUuid(UUID.randomUUID().toString());

        final AddressEntity addressEntitySave = addressService.saveAddress(addressEntity, customerEntity);
        final SaveAddressResponse saveAddressResponse= new SaveAddressResponse()
            .id(addressEntitySave.getUuid())
            .status("ADDRESS SUCCESSFULLY REGISTERED");

        return new ResponseEntity<>(saveAddressResponse, HttpStatus.CREATED);
    }

    /**
     * This controller method retrieves all addresses of customer
     * @param authorization
     * @return
     * @throws AuthorizationFailedException
     */
    @RequestMapping(method = RequestMethod.GET, path = "/address/customer",
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AddressListResponse> getAllAddresses(@RequestHeader("authorization") final String authorization)
        throws AuthorizationFailedException {

        final String accessToken = authorization.split("Bearer ")[1];
        final CustomerEntity customerEntity = customerService.getCustomer(accessToken);
        final List<AddressEntity> addressEntities = addressService.getAllAddress(customerEntity);
        final List<AddressList> addressList = new ArrayList<>();

        if (!addressEntities.isEmpty()) {
            addressEntities.forEach(addressEntity -> addressList.add(createAddressData(addressEntity)));
        }

        final AddressListResponse addressListResponse = new AddressListResponse().addresses(addressList);

        return new ResponseEntity<>(addressListResponse, HttpStatus.OK);
    }

    /**
     * This controller method deletes a customer's address
     * @param authorization
     * @param addressId
     * @return
     * @throws AddressNotFoundException
     * @throws AuthorizationFailedException
     */
    @RequestMapping(method = RequestMethod.DELETE, path = "/address/{address_id}",
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<DeleteAddressResponse> deleteAddress(
        @RequestHeader("authorization") final String authorization,
        @PathVariable("address_id") final String addressId)
        throws AddressNotFoundException, AuthorizationFailedException {

        final String accessToken = authorization.split("Bearer ")[1];
        final CustomerEntity customerEntity = customerService.getCustomer(accessToken);
        final AddressEntity addressEntity = addressService.getAddressByUUID(addressId, customerEntity);
        final AddressEntity addressEntityDelete = addressService.deleteAddress(addressEntity);
        DeleteAddressResponse deleteAddressResponse = new DeleteAddressResponse()
            .id(UUID.fromString(addressEntityDelete.getUuid()))
            .status("ADDRESS DELETED SUCCESSFULLY");

        return new ResponseEntity<>(deleteAddressResponse, HttpStatus.OK);
    }

    /**
     * This controller method returns list of states
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, path = "/states",
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<StatesListResponse> getAllStates() {

        final List<StateEntity> stateEntities = addressService.getAllStates();
        final StatesListResponse statesListResponse = new StatesListResponse();

        if (!stateEntities.isEmpty()) {
            final List<StatesList> statesLists = new ArrayList<>();
            stateEntities.forEach(stateEntity -> {
                final StatesList statesList = new StatesList().id(UUID.fromString(stateEntity.getUuid())).stateName(stateEntity.getStateName());
                statesLists.add(statesList);
            });
            statesListResponse.states(statesLists);
        }

        return new ResponseEntity<>(statesListResponse, HttpStatus.OK);
    }

    /**
     * Method to format the address data before sending to client
     * @param addressEntity
     * @return
     */
    private AddressList createAddressData(final AddressEntity addressEntity) {
        final AddressList addressList = new AddressList();
        addressList.id(UUID.fromString(addressEntity.getUuid()))
            .city(addressEntity.getCity())
            .flatBuildingName(addressEntity.getFlatBuilNo())
            .locality(addressEntity.getLocality())
            .pincode(addressEntity.getPincode());

        AddressListState addressListState = new AddressListState();
        addressListState.id(UUID.fromString(addressEntity.getState().getUuid()))
            .stateName(addressEntity.getState().getStateName());
        addressList.state(addressListState);

        return addressList;
    }
}
