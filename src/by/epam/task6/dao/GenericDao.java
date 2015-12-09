package by.epam.task6.dao;

import by.epam.task6.exception.dao.DaoException;

import java.io.Serializable;

/**
 * Interface for a Data Access Object that can be used for a single specified type entity object.
 * A single instance implementing this interface can be used only for the type of bean object specified in the type parameters.
 * Define CRUD operations
 * @param <T> The type of the entity object for which this instance is to be used.
 * @param <PK> The type of the transfer object
 */
public interface GenericDao<T, PK extends Serializable> {
    /**
     * Add entity to database
     * @param newInstance  added entity
     * @return transfer object for entity
     * @throws DaoException if database error was detected
     */
    PK create(T newInstance) throws DaoException;

    /**
     * Get the entity with the specified type and id from the datastore.
     * @param id id of found entity
     * @return special type of entity
     * @throws DaoException if database error was detected
     */
    T find(PK id) throws DaoException;

    /**
     * Update the specified entity in the database.
     * @param transientObject updated entity
     * @throws DaoException if database error was detected
     */
    void update(T transientObject) throws DaoException;

    /**
     * Remove the specified entity from the database.
     * @param persistentObject removed entity
     * @throws DaoException if database error was detected
     */
    void delete(T persistentObject) throws DaoException;
}
