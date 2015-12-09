package by.epam.task6.service;

import by.epam.task6.exception.service.ServiceException;

import java.io.Serializable;

/**
 * Interface for Service layer that can be used for single specified type entity object.
 * Realized Command pattern.
 * @param <K> The type of object that layer accept from controller layer
 * @param <T> The type of object that return to controller layer
 */
public interface GenericService<K, T extends Serializable> {
    /**
     * Perform logic for single specified entity object
     * @param bean object that layer accept from controller layer
     * @return object that return to controller layer
     * @throws ServiceException if dao error was detected
     */
    T execute(K bean) throws ServiceException;
}
