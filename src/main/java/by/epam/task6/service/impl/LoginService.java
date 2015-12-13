package by.epam.task6.service.impl;

import by.epam.task6.bean.Account;
import by.epam.task6.bean.User;
import by.epam.task6.dao.AccountDao;
import by.epam.task6.dao.UserDao;
import by.epam.task6.dao.connectionpool.DBParameter;
import by.epam.task6.dao.connectionpool.DBResourceManager;
import by.epam.task6.dao.impl.MySqlAccountDao;
import by.epam.task6.dao.impl.MySqlUserDao;
import by.epam.task6.exception.dao.DaoException;
import by.epam.task6.exception.service.ServiceException;
import by.epam.task6.service.DBType;
import by.epam.task6.service.GenericService;

import static by.epam.task6.service.DBType.valueOf;

/**
 * Singleton instance of GenericService
 * @see GenericService
 * Realized Command pattern.
 * Form logic with controller layer {@link by.epam.task6.controller.command.impl.LoginCommand}
 */
public class LoginService implements GenericService<Account, User> {

    private static final LoginService instance = new LoginService();

    private LoginService(){}

    public static LoginService getInstance() {
        return instance;
    }

    /**
     * Define type of database and get needed DAO instance.
     * Send request with Account entity to the DAO layer {@link AccountDao} for getting id
     * Send id of Account to the DAO layer {@link UserDao} for getting User entity
     * method return null if id account not fined
     * @param account Account entity {@link Account}
     * @return User entity {@link User}
     * @throws ServiceException if if dao error was detected or type of database are not define
     */
    @Override
    public User execute(Account account) throws ServiceException {
        User user = null;

        AccountDao accountDao;
        UserDao userDao;

        DBType dbType = valueOf(DBResourceManager.getInstance().getValue(DBParameter.DB_TYPE).toUpperCase());
        switch (dbType) {
            case  MYSQL : {
                accountDao = MySqlAccountDao.getInstance();
                userDao = MySqlUserDao.getInstance();
                break;
            }
            default: {
                throw new ServiceException("Undefined database type");
            }
        }

        try {
            int idUser = accountDao.findIdAccount(account);
            if (idUser != -1) {
                user = userDao.find(idUser);
            }

        } catch (DaoException e) {
            throw new ServiceException("Login Service Exception", e);
        }

        return user;
    }
}
