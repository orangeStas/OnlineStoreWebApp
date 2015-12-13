package by.epam.task6.dao.impl;

import by.epam.task6.bean.transferobject.PaginationData;
import by.epam.task6.bean.Product;
import by.epam.task6.dao.ProductDao;
import by.epam.task6.dao.connectionpool.IConnectionPool;
import by.epam.task6.dao.connectionpool.impl.ConnectionPoolImpl;
import by.epam.task6.exception.dao.DaoException;
import by.epam.task6.exception.dao.connectionpool.ConnectionPoolException;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * MySQL Instance of a Product Data Access Object
 * Perform direct changes in the sql database
 * Singleton class
 */
public class MySqlProductDao implements ProductDao {
    private static final Logger LOGGER = Logger.getLogger(MySqlProductDao.class.getPackage().getName());

    private static final MySqlProductDao instance = new MySqlProductDao();

    private static final String GET_COUNT_PRODUCTS_SQL = "SELECT COUNT(*) from product";
    private static final String GET_PRODUCTS_PER_PAGE_SQL = "SELECT * from product limit ?,?";
    private static final String ADD_PRODUCT_SQL = "insert into product (`name`, description, price, image) values (?,?,?,?)";
    private static final String FIND_PRODUCT_SQL = "select * from product where idProduct=?";
    private static final String UPDATE_PRODUCT_SQL = "update product set `name` = ?, description=?, price=?, image=? where idProduct=?";
    private static final String DELETE_PRODUCT_SQL = "delete from product where idProduct=?";

    private MySqlProductDao(){}

    public static MySqlProductDao getInstance() {
        return instance;
    }

    /**
     * Perform sql request to sql database and return count records from products table
     * @return count records of Product entity {@link Product}
     * @throws DaoException if sql or connection pool error was detected
     */
    @Override
    public int getCountProducts() throws DaoException {
        IConnectionPool connectionPool = ConnectionPoolImpl.getInstance();
        int count = 0;

        try(
            Connection connection = connectionPool.getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_COUNT_PRODUCTS_SQL);
            ResultSet resultSet = statement.executeQuery();
        )
        {
            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            LOGGER.error("Getting products Sql error");
            throw new DaoException("Error read db.", e);
        } catch (ConnectionPoolException e) {
            LOGGER.error("Connection pool error");
            throw new DaoException("ConnectionPoolImpl Exception", e);
        }

        return count;
    }

    /**
     * Perform sql request to sql database and return list of products that will be view at the user page
     * @param paginationData object with parameters for select request that contains needed count records and offset position
     * @return list of Product entities {@link Product}
     * @throws DaoException if sql or connection pool error was detected
     */
    @Override
    public List<Product> loadProductPerPage(PaginationData paginationData) throws DaoException {
        IConnectionPool connectionPool = ConnectionPoolImpl.getInstance();
        List<Product> products = new ArrayList<>();

        try(
                Connection connection = connectionPool.getConnection();
                PreparedStatement statement = connection.prepareStatement(GET_PRODUCTS_PER_PAGE_SQL);
        )
        {
            statement.setInt(2, paginationData.getCountRecordsPerPage());
            statement.setInt(1, paginationData.getOffset());
            try(ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Product product = new Product();
                    product.setId(resultSet.getInt(1));
                    product.setName(resultSet.getString(2));
                    product.setDescription(resultSet.getString(3));
                    product.setPrice(resultSet.getDouble(4));
                    product.setImagePath(resultSet.getString(5));
                    products.add(product);
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Getting products Sql error");
            throw new DaoException("Error read db.", e);
        } catch (ConnectionPoolException e) {
            LOGGER.error("Connection pool error");
            throw new DaoException("ConnectionPoolImpl Exception", e);
        }

        return products;
    }

    /**
     * Perform sql request and add Product entity into sql database
     * @param product Product entity {@link Product}
     * @return return 1 - not need for logic
     * @throws DaoException if sql or connection pool error was detected
     */
    @Override
    public Integer create(Product product) throws DaoException {
        IConnectionPool connectionPool = ConnectionPoolImpl.getInstance();

        try(
            Connection connection = connectionPool.getConnection();
            PreparedStatement statement = connection.prepareStatement(ADD_PRODUCT_SQL);
        )
        {
            statement.setString(1, product.getName());
            statement.setString(2, product.getDescription());
            statement.setDouble(3, product.getPrice());
            statement.setString(4, product.getImagePath());
            statement.execute();
        } catch (SQLException e) {
            LOGGER.error("Create product Sql error");
            throw new DaoException("Error read db.", e);
        } catch (ConnectionPoolException e) {
            LOGGER.error("Connection pool error");
            throw new DaoException("ConnectionPoolImpl Exception", e);
        }
        return 1;
    }

    /**
     * Perform select sql request and return Product entity by id
     * @param id id of found entity
     * @return Product entity {@link Product}
     * @throws DaoException if sql or connection pool error was detected
     */
    @Override
    public Product find(Integer id) throws DaoException {
        IConnectionPool connectionPool = ConnectionPoolImpl.getInstance();
        Product product = null;
        try(
                Connection connection = connectionPool.getConnection();
                PreparedStatement statement = connection.prepareStatement(FIND_PRODUCT_SQL);
        )
        {
            statement.setInt(1, id);
            try(ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    product = new Product();
                    product.setId(resultSet.getInt(1));
                    product.setName(resultSet.getString(2));
                    product.setDescription(resultSet.getString(3));
                    product.setPrice(resultSet.getDouble(4));
                    product.setImagePath(resultSet.getString(5));
                }
            }
            statement.execute();
        } catch (SQLException e) {
            LOGGER.error("Finding product Sql error");
            throw new DaoException("Error read db.", e);
        } catch (ConnectionPoolException e) {
            LOGGER.error("Connection pool error");
            throw new DaoException("ConnectionPoolImpl Exception", e);
        }

        return product;
    }

    /**
     * Perform update sql request and update information about Product entity
     * @param product Product entity {@link Product}
     * @throws DaoException if sql or connection pool error was detected
     */
    @Override
    public void update(Product product) throws DaoException {
        IConnectionPool connectionPool = ConnectionPoolImpl.getInstance();
        try(
                Connection connection = connectionPool.getConnection();
                PreparedStatement statement = connection.prepareStatement(UPDATE_PRODUCT_SQL);
        ) {
            statement.setString(1, product.getName());
            statement.setString(2, product.getDescription());
            statement.setDouble(3, product.getPrice());
            statement.setString(4, product.getImagePath());
            statement.setInt(5, product.getId());
            statement.execute();
        } catch (SQLException e) {
            LOGGER.error("Update user Sql error");
            throw new DaoException("Sql Exception", e);
        } catch (ConnectionPoolException e) {
            LOGGER.error("Connection Pool error");
            throw new DaoException("Connection Pool Exception", e);
        }
    }

    /**
     * Perform delete sql request and remove product entity from sql database
     * @param product Product entity {@link Product}
     * @throws DaoException if sql or connection pool error was detected
     */
    @Override
    public void delete(Product product) throws DaoException {
        IConnectionPool connectionPool = ConnectionPoolImpl.getInstance();
        try(
                Connection connection = connectionPool.getConnection();
                PreparedStatement statement = connection.prepareStatement(DELETE_PRODUCT_SQL);
        ) {
            statement.setInt(1, product.getId());
            statement.execute();
        } catch (SQLException e) {
            LOGGER.error("Delete user Sql error");
            throw new DaoException("Sql Exception", e);
        } catch (ConnectionPoolException e) {
            LOGGER.error("Connection Pool error");
            throw new DaoException("Connection Pool Exception", e);
        }
    }
}
