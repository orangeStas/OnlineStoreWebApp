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

import static by.epam.task6.service.DBType.valueOf;

/**
 * Singleton instance of GenericService
 * @see GenericService
 * Realized Command pattern.
 * Form logic with controller layer {@link by.epam.task6.controller.command.impl.EditProductCommand}
 */
public class EditProductService implements GenericService<Product, Boolean> {

    private static final EditProductService instance = new EditProductService();

    private EditProductService() {}

    public static EditProductService getInstance() {
        return instance;
    }

    /**
     * Define type of database and get needed DAO instance.
     * Send product entity to the DAO layer for update own properties
     * @param product Product entity {@link Product}
     * @return true if service execute own purpose
     * @throws ServiceException if if dao error was detected or type of database are not define
     */
    @Override
    public Boolean execute(Product product) throws ServiceException {
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
            productDao.update(product);
        } catch (DaoException e) {
            throw new ServiceException("Update product service exception");
        }

        return true;
    }
}
