package by.epam.task6.service.impl;

import by.epam.task6.bean.transferobject.UnpaidOrdersData;
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
 * Form logic with controller layer {@link by.epam.task6.controller.command.impl.LoadUnpaidOrdersCommand}
 */
public class LoadUnpaidOrdersService implements GenericService<Boolean, UnpaidOrdersData> {
    private static final LoadUnpaidOrdersService instance = new LoadUnpaidOrdersService();

    private LoadUnpaidOrdersService() {}

    public static LoadUnpaidOrdersService getInstance() {
        return instance;
    }

    /**
     * Define type of database and get needed DAO instance.
     * Send request to DAO layer for getting {@link UnpaidOrdersData}
     * @param bean object that layer accept from controller layer
     * @return {@link UnpaidOrdersData} transfer object that contains list of Order entities {@link by.epam.task6.bean.Order}
     * @throws ServiceException if dao error was detected or type of database are not define
     */
    @Override
    public UnpaidOrdersData execute(Boolean bean) throws ServiceException {
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

        UnpaidOrdersData unpaidOrdersData = null;

        try {
            unpaidOrdersData = orderDao.findUnpaidOrders();
        } catch (DaoException e) {
            throw new ServiceException("Load unpaid orders service exception.", e);
        }
        return unpaidOrdersData;
    }
}
