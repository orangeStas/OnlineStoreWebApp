package by.epam.task6.service.impl;

import by.epam.task6.bean.transferobject.CustomerProductData;
import by.epam.task6.bean.Order;
import by.epam.task6.bean.Product;
import by.epam.task6.dao.OrderDAO;
import by.epam.task6.dao.connectionpool.DBParameter;
import by.epam.task6.dao.connectionpool.DBResourceManager;
import by.epam.task6.dao.impl.MySqlOrderDao;
import by.epam.task6.exception.dao.DaoException;
import by.epam.task6.exception.service.ServiceException;
import by.epam.task6.service.DBType;
import by.epam.task6.service.GenericService;

/**
 * Singleton instance of GenericService
 * @see GenericService
 * Realized Command pattern.
 * Form logic with controller layer {@link by.epam.task6.controller.command.impl.RemoveProductFromOrderCommand}
 */
public class RemoveProductFromOrderService implements GenericService<CustomerProductData, Boolean> {

    private static final RemoveProductFromOrderService instance = new RemoveProductFromOrderService();

    private RemoveProductFromOrderService(){}

    public static RemoveProductFromOrderService getInstance() {
        return instance;
    }

    /**
     * Define type of database and get needed DAO instance.
     * Form Product entity and Order entity for send they to the DAO layer {@link OrderDAO} for
     * remove product from order
     * @param customerProductData CustomerProductEntity transfer object {@link CustomerProductData} that contains
     *                            customer id and removing product id
     * @return true if removing was successful
     * @throws ServiceException if dao error was detected or type of database are not define
     */
    @Override
    public Boolean execute(CustomerProductData customerProductData) throws ServiceException {
        OrderDAO orderDao;

        DBType dbType = DBType.valueOf(DBResourceManager.getInstance().getValue(DBParameter.DB_TYPE).toUpperCase());

        switch (dbType) {
            case MYSQL: {
                orderDao = MySqlOrderDao.getInstance();
                break;
            }
            default: {
                throw new ServiceException("Undefined database type");
            }
        }

        Order order;

        try {
            order = orderDao.find(customerProductData.getIdCustomer());
            Product product = new Product();
            product.setId(customerProductData.getIdProduct());
            orderDao.removeProductFromOrder(order, product);
        } catch (DaoException e) {
            throw new ServiceException("Remove product from order service Exception");
        }

        return true;
    }
}
