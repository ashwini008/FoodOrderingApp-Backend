package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.AddressDao;
import com.upgrad.FoodOrderingApp.service.dao.OrderDao;
import com.upgrad.FoodOrderingApp.service.entity.*;
import com.upgrad.FoodOrderingApp.service.exception.AddressNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SaveAddressException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class AddressService {

    @Autowired
    private AddressDao addressDao;

    @Autowired
    private OrderDao orderDao;

    /**
     * This method fetches state based on UUID
     * @param stateUUID
     * @return
     * @throws AddressNotFoundException
     */
    public StateEntity getStateByUUID(final String stateUUID) throws AddressNotFoundException {
        final StateEntity stateEntity = addressDao.getStateByUUID(stateUUID);

        if (stateEntity == null) {
            throw new AddressNotFoundException("ANF-002", "No state by this id");
        }

        return stateEntity;
    }

    /**
     * This method saves the address to DB
     * @param addressEntity
     * @param customerEntity
     * @return
     * @throws SaveAddressException
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public AddressEntity saveAddress(final AddressEntity addressEntity, final CustomerEntity customerEntity) throws SaveAddressException {
        if (isEmpty(addressEntity.getCity())
            || isEmpty(addressEntity.getLocality())
            || isEmpty(addressEntity.getPincode())
            || isEmpty(addressEntity.getFlatBuilNo())) {
            throw new SaveAddressException("SAR-001", "No field can be empty");
        }

        if (addressEntity.getPincode().length() != 6 || !StringUtils.isNumeric((addressEntity.getPincode()))) {
            throw new SaveAddressException("SAR-002", "Invalid pincode");
        }

        final List<CustomerEntity> customerEntities = new ArrayList<>();
        customerEntities.add(customerEntity);
        addressEntity.setCustomers(customerEntities);

        return addressDao.saveAddress(addressEntity);
    }

    /**
     * This method fetches all the addresses of a customer
     * @param customerEntity
     * @return
     */
    public List<AddressEntity> getAllAddress(final CustomerEntity customerEntity) {
        final List<AddressEntity> addressEntities = new ArrayList<>();
        final List<CustomerAddressEntity> customerAddressEntities = addressDao.getAllAddress(customerEntity);

        if (!customerAddressEntities.isEmpty()) {
            customerAddressEntities.forEach(customerAddressEntity -> addressEntities.add(customerAddressEntity.getAddress()));
        }

        return addressEntities;
    }

    /**
     * Method to fetch address details from address UUID
     * @param uuid
     * @param customerEntity
     * @return
     * @throws AddressNotFoundException
     * @throws AuthorizationFailedException
     */
    public AddressEntity getAddressByUUID(final String uuid, final CustomerEntity customerEntity)
        throws AddressNotFoundException, AuthorizationFailedException {

        if (isEmpty(uuid)) {
            throw new AddressNotFoundException("ANF-005", "Address id can not be empty");
        }

        final AddressEntity addressEntity = addressDao.getAddressByUUID(uuid);
        if (addressEntity == null) {
            throw new AddressNotFoundException("ANF-003", "No address by this id");
        }

        final CustomerAddressEntity customerAddressEntity = addressDao.getCustomerAddress(addressEntity, customerEntity);
        if (customerAddressEntity == null) {
            throw new AddressNotFoundException("ATHR-004", "You are not authorized to view/update/delete any one else's address");
        }

        return addressEntity;
    }

    /**
     * Method to delete address from DB
     * @param addressEntity
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public AddressEntity deleteAddress(final AddressEntity addressEntity) {
        final List<OrderEntity> orderEntities = orderDao.getOrdersByAddress(addressEntity);

        if (orderEntities.size() > 0) { // if address attached to an order, set it as inactive
            addressEntity.setActive(0);
            return addressDao.updateAddress(addressEntity);
        } else {
            addressDao.deleteAddress(addressEntity);
            return addressEntity;
        }
    }

    /**
     * Method to fetch all states from DB
     * @return
     */
    public List<StateEntity> getAllStates() {
        return addressDao.getAllStates();
    }

    /**
     * Method to implement empty validation check
     * @param value
     * @return
     */
    public boolean isEmpty(final String value) {
        return value == null || value.isEmpty();
    }

    public AddressEntity getAddressById(String id) {
        return addressDao.getAddressByUUID(id);
    }
}
