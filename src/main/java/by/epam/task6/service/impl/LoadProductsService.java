package by.epam.task6.service.impl;

import by.epam.task6.bean.transferobject.PaginationData;
import by.epam.task6.bean.transferobject.ProductPerPageData;
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
 * Form logic with controller layer {@link by.epam.task6.controller.command.impl.LoadProductsCommand}
 */
public class LoadProductsService implements GenericService<PaginationData, ProductPerPageData> {

    private static final LoadProductsService instance = new LoadProductsService();

    private LoadProductsService(){}

    public static LoadProductsService getInstance() {
        return instance;
    }

    /**
     * Define type of database and get needed DAO instance.
     * Send request for getting list of product entities to the DAO layer {@link ProductDao}
     * method may return ProductPerPageData transfer object with empty list of products
     * @param paginationData PaginationData transfer object {@link PaginationData}
     * @return ProductPerPageData transfer object {@link ProductPerPageData} contains list of products and count of all products
     * @throws ServiceException if if dao error was detected or type of database are not define
     */
    @Override
    public ProductPerPageData execute(PaginationData paginationData) throws ServiceException {
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

        ProductPerPageData productPerPageData = null;
        int countAllProducts;
        try {
            if ((countAllProducts = productDao.getCountProducts()) != 0) {
                productPerPageData = new ProductPerPageData();
                productPerPageData.setCountAllProducts(countAllProducts);
                productPerPageData.setProducts(productDao.loadProductPerPage(paginationData));
            }
        } catch (DaoException e) {
            throw new ServiceException("Load products service exception", e);
        }

        return productPerPageData;
    }
}
