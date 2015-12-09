package by.epam.task6.service.impl;

import by.epam.task6.bean.Order;
import by.epam.task6.dao.OrderDAO;
import by.epam.task6.dao.connectionpool.DBParameter;
import by.epam.task6.dao.connectionpool.DBResourceManager;
import by.epam.task6.dao.impl.MySqlOrderDao;
import by.epam.task6.exception.dao.DaoException;
import by.epam.task6.exception.service.ServiceException;
import by.epam.task6.service.DBType;
import by.epam.task6.service.GenericService;

import static by.epam.task6.service.DBType.valueOf;

/**
 * Singleton instance of GenericService
 * @see GenericService
 * Realized Command pattern.
 * Form logic with controller layer {@link by.epam.task6.controller.command.impl.ConfirmOrderCommand}
 */
public class ConfirmOrderService implements GenericService<Order, Boolean> {

    private static final ConfirmOrderService instance = new ConfirmOrderService();

    private ConfirmOrderService(){}

    public static ConfirmOrderService getInstance() {
        return instance;
    }

    /**
     * Define type of database and get needed DAO instance.
     * Find order by customer id from DAO layer {@link OrderDAO}
     * and send this Order to DAO layer for making him unavailable for adding products
     * @param order Order entity {@link Order}
     * @return true if service execute own purpose
     * @throws ServiceException if if dao error was detected or type of database are not define
     */
    @Override
    public Boolean execute(Order order) throws ServiceException {
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

        try {
            order = orderDao.find(order.getIdCustomer());
            orderDao.confirmOrder(order);
        } catch (DaoException e) {
            throw new ServiceException("Confirm order service Exception.", e);
        }

        return true;
    }
}
