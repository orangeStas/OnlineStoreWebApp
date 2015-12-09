package by.epam.task6.service.impl;

import by.epam.task6.bean.User;
import by.epam.task6.dao.UserDao;
import by.epam.task6.dao.connectionpool.DBParameter;
import by.epam.task6.dao.connectionpool.DBResourceManager;
import by.epam.task6.dao.impl.MySqlUserDao;
import by.epam.task6.exception.dao.DaoException;
import by.epam.task6.exception.service.ServiceException;
import by.epam.task6.service.DBType;
import by.epam.task6.service.GenericService;

/**
 * Singleton instance of GenericService
 * @see GenericService
 * Realized Command pattern.
 * Form logic with controller layer {@link by.epam.task6.controller.command.impl.BanUserCommand}
 */
public class BanUserService implements GenericService<User, Boolean> {

    private static final BanUserService instance = new BanUserService();

    private BanUserService() {}

    public static BanUserService getInstance() {
        return instance;
    }

    /**
     * Define type of database and get needed DAO instance.
     * Send request to DAO layer for insert user's id to ban list
     * @param bean object that layer accept from controller layer {@link User}. Must contains filled user's id.
     * @return true if service execute own purpose
     * @throws ServiceException if if dao error was detected or type of database are not define
     */
    @Override
    public Boolean execute(User bean) throws ServiceException {
        UserDao userDao;

        DBType dbType = DBType.valueOf(DBResourceManager.getInstance().getValue(DBParameter.DB_TYPE).toUpperCase());

        switch (dbType) {
            case MYSQL: {
                userDao = MySqlUserDao.getInstance();
                break;
            }
            default: {
                throw new ServiceException("Undefined database type");
            }
        }

        try {
            userDao.addToBanList(bean);
        } catch (DaoException e) {
            throw new ServiceException("Add to ban list service exception.", e);
        }

        return true;
    }
}
