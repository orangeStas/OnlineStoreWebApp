package by.epam.task6.dao;

import by.epam.task6.bean.Order;
import by.epam.task6.bean.Product;
import by.epam.task6.bean.transferobject.UnpaidOrdersData;
import by.epam.task6.exception.dao.DaoException;

import java.util.List;
/**
 * Interface for for a Data Access Object that uses for Order entity {@link Order}
 */
public interface OrderDAO extends GenericDao<Order, Integer> {
    /**
     * Adds Product entity to list of products of Order entity
     * @param order Order entity {@link Order}
     * @param product Product entity {@link Product}
     * @throws DaoException if database error was detected
     */
    void addProductToOrder(Order order, Product product) throws DaoException;

    /**
     * Gets list of products by order from database
     * @param order Order entity {@link Order}
     * @return list of Product entities, may return empty list
     * @throws DaoException if database error was detected
     */
    List<Product> findProductsByOrder(Order order) throws DaoException;

    /**
     * Makes order unavailable for adding products.
     * @param order Order entity {@link Order}
     * @throws DaoException if database error was detected
     */
    void confirmOrder(Order order) throws DaoException;

    /**
     * Removes product from order
     * @param order Order entity from that product will be removed {@link Order}
     * @param product Removing Product entity {@link Product}
     * @throws DaoException if database error was detected
     */
    void removeProductFromOrder(Order order, Product product) throws DaoException;

    /**
     * Gets list of unpaid orders
     * @return {@link UnpaidOrdersData} that contains list with Order entity {@link Order}. May return empty list
     * @throws DaoException if database error was detected
     */
    UnpaidOrdersData findUnpaidOrders() throws DaoException;


}
