package by.epam.task6.dao.impl;

import by.epam.task6.bean.Order;
import by.epam.task6.bean.Product;
import by.epam.task6.bean.transferobject.UnpaidOrdersData;
import by.epam.task6.dao.OrderDAO;
import by.epam.task6.dao.connectionpool.IConnectionPool;
import by.epam.task6.dao.connectionpool.impl.ConnectionPoolImpl;
import by.epam.task6.exception.dao.DaoException;
import by.epam.task6.exception.dao.connectionpool.ConnectionPoolException;
import org.apache.log4j.Logger;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
/**
 * MySQL Instance of a Order Data Access Object
 * Perform direct changes in the sql database
 * Singleton class
 */
public class MySqlOrderDao implements OrderDAO {

    private static final Logger LOGGER = Logger.getLogger(MySqlOrderDao.class.getPackage().getName());

    private static final MySqlOrderDao instance = new MySqlOrderDao();

    private static final String CREATE_ORDER_SQL = "insert into `order` (idCustomer, total_cost, `date`) values(?,?,?)";
    private static final String FIND_ORDER_BY_CUSTOMER_ID = "select * from `order` where idCustomer=? and isConfirmed=0";
    private static final String UPDATE_TOTAL_COST_SQL = "update `order` " +
                                                        "set total_cost = (select T from" +
                                                        "(SELECT sum(price) as T" +
                                                        " from `order` " +
                                                        "inner join orderproduct on `order`.idOrder = orderproduct.idOrder " +
                                                        "inner join product on `orderproduct`.idProduct = product.idProduct " +
                                                        "where idCustomer = ? and `order`.idOrder=? limit 1) as t2 ) " +
                                                        "where idCustomer = ? and isConfirmed=0 limit 1";

    private static final String GET_PRODUCTS_FOR_ORDER_SQL = "SELECT product.idProduct, product.`name`, product.description, product.price, product.image " +
                                                                "from `order` " +
                                                                "inner join orderproduct on `order`.idOrder = orderproduct.idOrder " +
                                                                "inner join product on orderproduct.idProduct = product.idProduct " +
                                                                "where `order`.idCustomer = ? and `order`.isConfirmed=?";

    private static final String ADD_PRODUCT_TO_ORDER_SQL = "insert into orderproduct (idOrder, idProduct) values (?,?)";

    private static final String DELETE_ORDER_SQL = "delete from `order` where idOrder=?";
    private static final String DELETE_PRODUCTS_FROM_ORDER_SQL = "delete from orderproduct where idOrder=?";
    private static final String CONFIRM_ORDER_SQL = "update `order` set isConfirmed = 1 where idOrder=?";
    private static final String REMOVE_PRODUCT_FROM_ORDER_SQL = "delete from orderproduct where idOrder=? and idProduct=? limit 1";

    private static final String GET_UNPAID_ORDERS = "select distinct * from `order` " +
                                                    "WHERE `order`.idCustomer not in " +
                                                    "(select idCustomer from bannedaccount) and `order`.isConfirmed = 0";

    private MySqlOrderDao(){}

    public static MySqlOrderDao getInstance() {
        return instance;
    }

    /**
     * Add entity to database
     * @param order added Order entity {@link Order}
     * @return id of added order
     * @throws DaoException if sql or connection pool error was detected
     */
    @Override
    public Integer create(Order order) throws DaoException {
        IConnectionPool connectionPool = ConnectionPoolImpl.getInstance();

        try(
            Connection connection = connectionPool.getConnection();
            PreparedStatement statement = connection.prepareStatement(CREATE_ORDER_SQL)
        )
        {
            statement.setInt(1, order.getIdCustomer());
            statement.setDouble(2, order.getTotalCost());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currTime = simpleDateFormat.format(order.getDate());
            statement.setString(3, currTime);
            statement.execute();
        } catch (SQLException e) {
            LOGGER.error("Create order sql error");
            throw new DaoException("Sql Exception", e);
        } catch (ConnectionPoolException e) {
            LOGGER.error("Connection Pool error");
            throw new DaoException("Connection Pool Exception", e);
        }

        return find(order.getIdCustomer()).getIdOrder();
    }

    /**
     * Perform select request and get the Order entity from sql database by id of User entity.
     * @param id id of User entity {@link by.epam.task6.bean.User}
     * @return Order entity {@link Order}
     * @throws DaoException if sql or connection pool error was detected
     */
    @Override
    public Order find(Integer id) throws DaoException {
        IConnectionPool connectionPool = ConnectionPoolImpl.getInstance();
        Order order = null;
        try(
            Connection connection = connectionPool.getConnection();
            PreparedStatement statement = connection.prepareStatement(FIND_ORDER_BY_CUSTOMER_ID)
        )
        {
            statement.setInt(1, id);
            try(ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    order = new Order();
                    order.setIdOrder(resultSet.getInt(1));
                    order.setIdCustomer(resultSet.getInt(2));
                    order.setTotalCost(resultSet.getDouble(3));
                    order.setDate(new Date(resultSet.getTimestamp(4).getTime()));
                    order.setIsConfirmed(resultSet.getBoolean(5));
                }
            }

        } catch (SQLException e) {
            LOGGER.error("Find order sql error");
            throw new DaoException("Sql Exception", e);
        } catch (ConnectionPoolException e) {
            LOGGER.error("Connection Pool error");
            throw new DaoException("Connection Pool Exception", e);
        }
        return order;
    }

    /**
     * Calculate total cost for order and update it
     * @param order Order entity {@link Order}
     * @throws DaoException if sql or connection pool error was detected
     */
    @Override
    public void update(Order order) throws DaoException {
        IConnectionPool connectionPool = ConnectionPoolImpl.getInstance();
        try(
            Connection connection = connectionPool.getConnection();
            PreparedStatement statement = connection.prepareStatement(UPDATE_TOTAL_COST_SQL)
        )
        {
            statement.setInt(1, order.getIdCustomer());
            statement.setInt(2, order.getIdOrder());
            statement.setInt(3, order.getIdCustomer());
            statement.execute();
        } catch (SQLException e) {
            LOGGER.error("Update order sql error");
            throw new DaoException("Sql Exception", e);
        } catch (ConnectionPoolException e) {
            LOGGER.error("Connection Pool error");
            throw new DaoException("Connection Pool Exception", e);
        }
    }

    /**
     * Remove order from sql database and remove products from this order
     * @param order Order entity {@link Order}
     * @throws DaoException if sql or connection pool error was detected
     */
    @Override
    public void delete(Order order) throws DaoException {
        IConnectionPool connectionPool = ConnectionPoolImpl.getInstance();
        try(
                Connection connection = connectionPool.getConnection();
                PreparedStatement statement = connection.prepareStatement(DELETE_ORDER_SQL)
        )
        {
            deleteProductsFromOrder(connection, order);
            statement.setInt(1, order.getIdOrder());
            statement.execute();
        } catch (SQLException e) {
            LOGGER.error("Delete order sql error");
            throw new DaoException("Sql Exception", e);
        } catch (ConnectionPoolException e) {
            LOGGER.error("Connection Pool error");
            throw new DaoException("Connection Pool Exception", e);
        }
    }

    /**
     * Remove products from order
     * @param connection Connection from connection pool
     * @param order Order entity {@link Order}
     * @throws DaoException if sql or connection pool error was detected
     */
    private void deleteProductsFromOrder(Connection connection, Order order) throws DaoException {
        try(
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_PRODUCTS_FROM_ORDER_SQL)
        )
        {
            preparedStatement.setInt(1, order.getIdOrder());
            preparedStatement.execute();
        } catch (SQLException e) {
            LOGGER.error("Delete products from order sql error");
            throw new DaoException("Sql Exception", e);
        }
    }

    /**
     * Add product to list of products of order
     * @param order Order entity {@link Order}
     * @param product Product entity {@link Product}
     * @throws DaoException if sql or connection pool error was detected
     */
    @Override
    public void addProductToOrder(Order order, Product product) throws DaoException {
        IConnectionPool connectionPool = ConnectionPoolImpl.getInstance();

        try(
            Connection connection = connectionPool.getConnection();
            PreparedStatement statement = connection.prepareStatement(ADD_PRODUCT_TO_ORDER_SQL)
        )
        {
            statement.setInt(1, order.getIdOrder());
            statement.setInt(2, product.getId());
            statement.execute();
        } catch (SQLException e) {
            LOGGER.error("Add product to order sql error");
            throw new DaoException("Sql Exception", e);
        } catch (ConnectionPoolException e) {
            LOGGER.error("Connection Pool error");
            throw new DaoException("Connection Pool Exception", e);
        }
        update(order);
    }

    /**
     * Get list of products from Order,
     * may return empty list
     * @param order Order entity {@link Order}
     * @return list of products in order
     * @throws DaoException
     */
    @Override
    public List<Product> findProductsByOrder(Order order) throws DaoException {
        IConnectionPool connectionPool = ConnectionPoolImpl.getInstance();
        List<Product> products = new ArrayList<>();

        try(
            Connection connection = connectionPool.getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_PRODUCTS_FOR_ORDER_SQL)
        )
        {
            statement.setInt(1, order.getIdCustomer());
            statement.setBoolean(2, order.isConfirmed());
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
            LOGGER.error("Find products by order sql error");
            throw new DaoException("Sql Exception", e);
        } catch (ConnectionPoolException e) {
            LOGGER.error("Connection Pool error");
            throw new DaoException("Connection Pool Exception", e);
        }

        return products;
    }

    /**
     * Set order status isConfirmed, after this order makes not available for adding products
     * @param order Order entity {@link Order}
     * @throws DaoException if sql or connection pool error was detected
     */
    @Override
    public void confirmOrder(Order order) throws DaoException {
        IConnectionPool connectionPool = ConnectionPoolImpl.getInstance();

        try(
            Connection connection = connectionPool.getConnection();
            PreparedStatement statement = connection.prepareStatement(CONFIRM_ORDER_SQL);
        )
        {
            statement.setInt(1, order.getIdOrder());
            statement.execute();
        } catch (SQLException e) {
            LOGGER.error("Confirm order sql error");
            throw new DaoException("Sql Exception", e);
        } catch (ConnectionPoolException e) {
            LOGGER.error("Connection Pool error");
            throw new DaoException("Connection Pool Exception", e);
        }
    }

    /**
     * Remove product from order
     * @param order Order entity from that product will be removed {@link Order}
     * @param product Removing Product entity {@link Product}
     * @throws DaoException if sql or connection pool error was detected
     */
    @Override
    public void removeProductFromOrder(Order order, Product product) throws DaoException {
        IConnectionPool connectionPool = ConnectionPoolImpl.getInstance();

        try(
                Connection connection = connectionPool.getConnection();
                PreparedStatement statement = connection.prepareStatement(REMOVE_PRODUCT_FROM_ORDER_SQL)
        )
        {
            statement.setInt(1, order.getIdOrder());
            statement.setInt(2, product.getId());
            statement.execute();
        } catch (SQLException e) {
            LOGGER.error("Remove product from order sql error");
            throw new DaoException("Sql Exception", e);
        } catch (ConnectionPoolException e) {
            LOGGER.error("Connection Pool error");
            throw new DaoException("Connection Pool Exception", e);
        }
        update(order);
    }

    /**
     * Gets list of unpaid orders
     * @return {@link UnpaidOrdersData} that contains list with Order entity {@link Order}. May return empty list
     * @throws DaoException if database error was detected
     */
    @Override
    public UnpaidOrdersData findUnpaidOrders() throws DaoException {
        IConnectionPool connectionPool = ConnectionPoolImpl.getInstance();
        List<Order> unpaidOrders = new ArrayList<>();
        try(
            Connection connection = connectionPool.getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_UNPAID_ORDERS);
        )
        {
            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    Order order = new Order();
                    order.setIdOrder(resultSet.getInt(1));
                    order.setIdCustomer(resultSet.getInt(2));
                    order.setDate(new Date(resultSet.getTimestamp(4).getTime()));
                    if (!unpaidOrders.contains(order))
                    {
                        unpaidOrders.add(order);
                    }

                }
            }
        } catch (SQLException e) {
            LOGGER.error("Get unpaid orders sql error");
            throw new DaoException("select sql exception");
        } catch (ConnectionPoolException e) {
            LOGGER.error("Connection Pool error");
            throw new DaoException("Connection Pool Exception", e);
        }
        UnpaidOrdersData unpaidOrdersData = new UnpaidOrdersData();
        unpaidOrdersData.setUnpaidOrders(unpaidOrders);
        return unpaidOrdersData;
    }
}
