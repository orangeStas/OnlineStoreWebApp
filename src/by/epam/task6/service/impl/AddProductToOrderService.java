package by.epam.task6.service.impl;

import by.epam.task6.bean.transferobject.CustomerProductData;
import by.epam.task6.bean.Order;
import by.epam.task6.bean.Product;
import by.epam.task6.dao.OrderDAO;
import by.epam.task6.dao.ProductDao;
import by.epam.task6.dao.connectionpool.DBParameter;
import by.epam.task6.dao.connectionpool.DBResourceManager;
import by.epam.task6.dao.impl.MySqlOrderDao;
import by.epam.task6.dao.impl.MySqlProductDao;
import by.epam.task6.exception.dao.DaoException;
import by.epam.task6.exception.service.ServiceException;
import by.epam.task6.service.DBType;
import by.epam.task6.service.GenericService;

import java.util.Date;

import static by.epam.task6.service.DBType.valueOf;

/**
 * Singleton instance of GenericService
 * @see GenericService
 * Realized Command pattern.
 * Form logic with controller layer {@link by.epam.task6.controller.command.impl.AddProductToOrderCommand}
 */
public class AddProductToOrderService implements GenericService<CustomerProductData, Boolean> {
    private static final AddProductToOrderService instance = new AddProductToOrderService();

    private AddProductToOrderService(){}

    public static AddProductToOrderService getInstance() {
        return instance;
    }

    /**
     * Define type of database and get needed DAO instance.
     * Check if customer already have order and add product to this order
     * else create new order and add product to new order
     * @param customerProductData CustomerProduct transfer object {@link CustomerProductData}
     * @return true if service execute own purpose
     * @throws ServiceException if dao error was detected or type of database are not define
     */
    @Override
    public Boolean execute(CustomerProductData customerProductData) throws ServiceException {
        OrderDAO orderDao;
        ProductDao productDao;

        DBType dbType = valueOf(DBResourceManager.getInstance().getValue(DBParameter.DB_TYPE).toUpperCase());

        switch (dbType) {
            case  MYSQL : {
                productDao = MySqlProductDao.getInstance();
                orderDao = MySqlOrderDao.getInstance();
                break;
            }
            default: {
                throw new ServiceException("Undefined database type");
            }
        }

        Order order;

        try {
            if ((order = orderDao.find(customerProductData.getIdCustomer())) == null) {
                order = new Order();
                order.setIdCustomer(customerProductData.getIdCustomer());
                order.setTotalCost(0.0);
                order.setDate(new Date());
                order.setIdOrder(orderDao.create(order));
            }
            Product product = productDao.find(customerProductData.getIdProduct());
            orderDao.addProductToOrder(order, product);
        } catch (DaoException e) {
            throw new ServiceException("Add product service Exception.", e);
        }

        return true;
    }
}
