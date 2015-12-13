package by.epam.task6.dao;

import by.epam.task6.bean.Account;
import by.epam.task6.exception.dao.DaoException;

/**
 * Interface for for a Data Access Object that uses for Account entity {@link Account}
 */
public interface AccountDao extends GenericDao<Account, Integer> {

    /**
     * Find id of account
     * @param account entity Account {@link Account}
     * @return id of input account entity
     * @throws DaoException DaoException if database error was detected
     */
    int findIdAccount(Account account) throws DaoException;
}
