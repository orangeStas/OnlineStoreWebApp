package by.epam.task6.dao;

import by.epam.task6.bean.Product;
import by.epam.task6.bean.transferobject.PaginationData;
import by.epam.task6.exception.dao.DaoException;

import java.util.List;
/**
 * Interface for for a Data Access Object that uses for Product entity {@link Product}
 */
public interface ProductDao extends GenericDao<Product, Integer> {
    /**
     * Get count of all products that store in the database
     * @return
     * @throws DaoException if database error was detected
     */
    int getCountProducts() throws DaoException;

    /**
     * Get list of product that that will be view on the page
     * @param paginationData object with parameters for select request
     * @return
     * @throws DaoException if database error was detected
     */
    List<Product> loadProductPerPage(PaginationData paginationData) throws DaoException;
}
