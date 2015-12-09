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
 * Form logic with controller layer {@link by.epam.task6.controller.command.impl.RemoveProductCommand}
 */
public class RemoveProductService implements GenericService<Product, Boolean> {

    private static final RemoveProductService instance = new RemoveProductService();

    private RemoveProductService(){}

    public static RemoveProductService getInstance() {
        return instance;
    }

    /**
     * Define type of database and get needed DAO instance.
     * Send product entity to DAO layer {@link ProductDao} for removing this product from database
     * @param product Product entity {@link Product}
     * @return true if removing was successful
     * @throws ServiceException if dao error was detected or type of database are not define
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
            productDao.delete(product);
        } catch (DaoException e) {
            throw new ServiceException("Remove product service exception");
        }

        return true;
    }
}
