package by.epam.task6.dao;

import by.epam.task6.bean.User;
import by.epam.task6.exception.dao.DaoException;

/**
 * Interface for for a Data Access Object that uses for User entity {@link User}
 */
public interface UserDao extends GenericDao<User, Integer> {

    /**
     * Adds id of User entity to ban list {@link User}
     * @param user User entity
     * @throws DaoException if database error was detected
     */
    void addToBanList(User user) throws DaoException;
}
