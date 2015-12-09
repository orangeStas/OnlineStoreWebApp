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
 * Form logic with controller layer {@link by.epam.task6.controller.command.impl.FindProductToEditCommand}
 */
public class FindProductToEditService implements GenericService<Integer, Product> {
    private static final FindProductToEditService instance = new FindProductToEditService();

    private FindProductToEditService() {}

    public static FindProductToEditService getInstance() {
        return instance;
    }

    /**
     * Define type of database and get needed DAO instance.
     * Send request with product id to the DAO layer for getting Product entity
     * @param id id of Product entity {@link Product}
     * @return Product entity {@link Product}
     * @throws ServiceException if if dao error was detected or type of database are not define
     */
    @Override
    public Product execute(Integer id) throws ServiceException {
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

        Product product;
        try{
            product = productDao.find(id);
        } catch (DaoException e) {
            throw new ServiceException("Find product service exception");
        }

        return product;
    }
}
