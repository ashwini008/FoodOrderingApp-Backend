package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class AddressDao {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Method to get state details from DB using state UUID
     * @param stateUUID
     * @return
     */
    public StateEntity getStateByUUID(final String stateUUID) {
        try {
            return entityManager.createNamedQuery("stateUuid", StateEntity.class)
                .setParameter("stateUuid", stateUUID).getSingleResult();
        } catch (NoResultException exe) {
            return null;
        }
    }

    /**
     * Method to push address into DB
     * @param addressEntity
     * @return
     */
    public AddressEntity saveAddress(final AddressEntity addressEntity) {
        entityManager.persist(addressEntity);
        return addressEntity;
    }

    /**
     * Method to pull all customer address entries from DB
     * @param customerEntity
     * @return
     */
    public List<CustomerAddressEntity> getAllAddress(final CustomerEntity customerEntity) {
        return entityManager.createNamedQuery("customerAddress", CustomerAddressEntity.class)
            .setParameter("customerId", customerEntity.getId())
            .getResultList();
    }

    /**
     * Method to get address from DB using address UUID
     * @param addressUUID
     * @return
     */
    public AddressEntity getAddressByUUID(final String addressUUID) {
        try {
            return entityManager.createNamedQuery("addressUuid", AddressEntity.class)
                .setParameter("addressUuid", addressUUID)
                .getSingleResult();
        } catch (NoResultException exe) {
            return null;
        }
    }

    /**
     * Method to get customer address from DB
     * @param addressEntity
     * @param customerEntity
     * @return
     */
    public CustomerAddressEntity getCustomerAddress(final AddressEntity addressEntity, final CustomerEntity customerEntity) {
        try {
            return entityManager.createNamedQuery("customerAddByIDs", CustomerAddressEntity.class)
                .setParameter("addressId", addressEntity.getId())
                .setParameter("customerId", customerEntity.getId())
                .getSingleResult();
        } catch (NoResultException exe) {
            return null;
        }
    }

    /**
     * Method to delete address from DB
     * @param addressEntity
     */
    public void deleteAddress(final AddressEntity addressEntity) {
        entityManager.remove(addressEntity);
    }

    /**
     * Method to update address
     * @param addressEntity
     * @return
     */
    public AddressEntity updateAddress(final AddressEntity addressEntity) {
        return entityManager.merge(addressEntity);
    }

    /**
     * Method to fetch all states from DB
     * @return
     */
    public List<StateEntity> getAllStates() {
        return entityManager.createNamedQuery("allStates", StateEntity.class).getResultList();
    }

}
