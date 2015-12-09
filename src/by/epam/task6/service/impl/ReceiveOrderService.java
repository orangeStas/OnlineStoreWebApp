package by.epam.task6.service.impl;

import by.epam.task6.bean.Order;
import by.epam.task6.bean.Product;
import by.epam.task6.bean.transferobject.OrderData;
import by.epam.task6.dao.OrderDAO;
import by.epam.task6.dao.connectionpool.DBParameter;
import by.epam.task6.dao.connectionpool.DBResourceManager;
import by.epam.task6.dao.impl.MySqlOrderDao;
import by.epam.task6.exception.dao.DaoException;
import by.epam.task6.exception.service.ServiceException;
import by.epam.task6.service.DBType;
import by.epam.task6.service.GenericService;

import java.util.List;

import static by.epam.task6.service.DBType.valueOf;
/**
 * Singleton instance of GenericService
 * @see GenericService
 * Realized Command pattern.
 * Form logic with controller layer {@link by.epam.task6.controller.command.impl.ReceiveOrderCommand}
 */
public class ReceiveOrderService implements GenericService<Order, OrderData> {
    private static final ReceiveOrderService instance = new ReceiveOrderService();

    private ReceiveOrderService() {}

    public static ReceiveOrderService getInstance() {
        return instance;
    }

    /**
     * Define type of database and get needed DAO instance.
     * Send request to the DAO layer {@link OrderDAO} for getting list of product entities in the order and order
     * method may return OrderData transfer object with empty list of products
     * @param order Order entity {@link Order}
     * @return Orderdata transfer object that contains list of product entities of order and order entity {@link OrderData}
     * @throws ServiceException if if dao error was detected or type of database are not define
     */
    @Override
    public OrderData execute(Order order) throws ServiceException {
        List<Product> products;
        OrderDAO orderDao;

        DBType dbType = valueOf(DBResourceManager.getInstance().getValue(DBParameter.DB_TYPE).toUpperCase());
        switch (dbType) {
            case  MYSQL : {
                orderDao = MySqlOrderDao.getInstance();
                break;
            }
            default: {
                throw new ServiceException("Undefined database type");
            }
        }

        OrderData orderData = null;
        try {
            order = orderDao.find(order.getIdCustomer());
            if (order != null) {
                orderData = new OrderData();
                products = orderDao.findProductsByOrder(order);
                orderData.setOrder(order);
                orderData.setProducts(products);
            }
        } catch (DaoException e) {
            throw new ServiceException("Load products by order service Exception.", e);
        }
        return orderData;
    }
}
