package by.epam.task6.service.impl;

import by.epam.task6.bean.Product;
import by.epam.task6.dao.ProductDao;
import by.epam.task6.dao.connectionpool.DBParameter;
import by.epam.task6.dao.connectionpool.DBResourceManager;
import by.epam.task6.dao.impl.MySqlProductDao;
import by.epam.task6.exception.dao.DaoException;
import by.epam.task6.exception.service.ServiceException;
import by.epam.task6.service.DBType;
import by.epam.task6.service.GenericService;

import static by.epam.task6.service.DBType.*;

/**
 * Singleton instance of GenericService
 * @see GenericService
 * Realized Command pattern.
 * Form logic with controller layer {@link by.epam.task6.controller.command.impl.AddNewProductCommand}
 */
public class AddNewProductService implements GenericService<Product, Boolean> {

    private static final AddNewProductService instance = new AddNewProductService();

    private AddNewProductService() {}

    public static AddNewProductService getInstance() {
        return instance;
    }

    /**
     * Define type of database and get needed DAO instance.
     * Send Product entity to the DAO layer {@link ProductDao}
     * @param newProduct Product entity {@link Product}
     * @return true if service execute own purpose
     * @throws ServiceException if if dao error was detected or type of database are not define
     */
    @Override
    public Boolean execute(Product newProduct) throws ServiceException {
        ProductDao productDao;

        DBType dbType = valueOf(DBResourceManager.getInstance().getValue(DBParameter.DB_TYPE).toUpperCase());

        switch (dbType) {
            case  MYSQL : {
                productDao = MySqlProductDao.getInstance();
                break;
            }
            default: {
                throw new ServiceException("Undefined database type");
            }
        }

        try {
            productDao.create(newProduct);
        } catch (DaoException e) {
            throw new ServiceException("Adding new product Service Exception", e);
        }

        return true;
    }
}
