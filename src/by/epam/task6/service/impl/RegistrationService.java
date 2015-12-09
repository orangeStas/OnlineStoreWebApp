package by.epam.task6.service.impl;

import by.epam.task6.bean.Account;
import by.epam.task6.bean.User;
import by.epam.task6.bean.transferobject.RegistrationData;
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
 * Form logic with controller layer {@link by.epam.task6.controller.command.impl.RegistrationCommand}
 */
public class RegistrationService implements GenericService<RegistrationData, Boolean> {
    private static final RegistrationService instance = new RegistrationService();

    private RegistrationService(){}

    public static RegistrationService getInstance() {
        return instance;
    }

    /**
     * Define type of database and get needed DAO instance.
     * Form Account entity and send it to the DAO layer {@link AccountDao} for insert into database
     * Form User entity and send it to the DAO layer {@link UserDao} for insert into database
     * @param registrationData RegistrationData transfer object {@link RegistrationData}
     * @return true if registration was successful
     * @throws ServiceException if dao error was detected or type of database are not define
     */
    @Override
    public Boolean execute(RegistrationData registrationData) throws ServiceException {
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

        Account account = new Account();
        account.setLogin(registrationData.getLogin());
        account.setPassword(registrationData.getPassword());

        User user = new User();
        user.setFirstName(registrationData.getFirstName());
        user.setSecondName(registrationData.getSecondName());

        try {
            if (accountDao.findIdAccount(account) != -1) {
                return false;
            }
            int id = accountDao.create(account);
            user.setId(id);
            userDao.create(user);
        } catch (DaoException e) {
            throw new ServiceException("Registration Service Exception", e);
        }
        return true;
    }
}
