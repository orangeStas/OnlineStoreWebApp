package by.epam.task6.dao.impl;

import by.epam.task6.bean.User;
import by.epam.task6.dao.UserDao;
import by.epam.task6.dao.connectionpool.IConnectionPool;
import by.epam.task6.dao.connectionpool.impl.ConnectionPoolImpl;
import by.epam.task6.exception.dao.DaoException;
import by.epam.task6.exception.dao.connectionpool.ConnectionPoolException;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * MySQL Instance of a User Data Access Object
 * Perform direct changes in the sql database
 * Singleton class
 */
public class MySqlUserDao implements UserDao {
    private static final MySqlUserDao instance = new MySqlUserDao();

    private static final Logger LOGGER = Logger.getLogger(MySqlUserDao.class.getPackage().getName());

    private static final String CREATE_CUSTOMER_SQL_REQUEST = "INSERT INTO CUSTOMER (idCustomer, first_name, last_name) VALUES (?,?,?)";
    private static final String FIND_CUSTOMER_SQL = "select first_name, last_name, isAdmin from customer where idCustomer = ?";
    private static final String CHECK_BANNED_ID_SQL = "SELECT idCustomer FROM bannedaccount where idCustomer=?";
    private static final String UPDATE_CUSTOMER_SQL = "update customer set first_name = ?, last_name=?, isadmin=? where idCustomer = ?";
    private static final String DELETE_CUSTOMER_SQL = "delete from customer where idCustomer=?";
    private static final String ADD_TO_BAN = "insert into bannedaccount (idCustomer) values (?)";


    private MySqlUserDao(){}

    public static MySqlUserDao getInstance() {
        return instance;
    }

    /**
     * Perform insert sql request and add new User entity to database.
     * If User entity don't have id, it will produce sql exception
     * @param user User entity {@link User}
     * @return id of added User entity
     * @throws DaoException if sql or connection pool error was detected
     */
    @Override
    public Integer create(User user) throws DaoException {
        IConnectionPool connectionPool = ConnectionPoolImpl.getInstance();

        try(
            Connection connection = connectionPool.getConnection();
            PreparedStatement statement = connection.prepareStatement(CREATE_CUSTOMER_SQL_REQUEST);
        )
        {
            statement.setInt(1, user.getId());
            statement.setString(2, user.getFirstName());
            statement.setString(3, user.getSecondName());
            statement.execute();
        } catch (SQLException e) {
            LOGGER.error("Insert user sql error");
            throw new DaoException("Sql Exception", e);
        } catch (ConnectionPoolException e) {
            LOGGER.error("Connection Pool error");
            throw new DaoException("Connection Pool Exception", e);
        }

        return user.getId();
    }

    /**
     * Perform select sql request and return User entity by input id
     * @param id id of found entity
     * @return User entity {@link User}
     * @throws DaoException
     */
    @Override
    public User find(Integer id) throws DaoException {
        IConnectionPool connectionPool = ConnectionPoolImpl.getInstance();

        User user = null;
        try(
            Connection connection = connectionPool.getConnection();
            PreparedStatement statement = connection.prepareStatement(FIND_CUSTOMER_SQL)
        )
        {
            statement.setInt(1, id);
            try(ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    user = new User();
                    user.setId(id);
                    user.setFirstName(resultSet.getString(1));
                    user.setSecondName(resultSet.getString(2));
                    user.setIsAdmin(resultSet.getBoolean(3));
                }
            }
            if (user != null) {
                user.setIsBanned(checkBannedAccount(connection, id));
            }
        } catch (SQLException e) {
            LOGGER.error("Find user Sql error");
            throw new DaoException("Sql Exception", e);
        } catch (ConnectionPoolException e) {
            LOGGER.error("Connection Pool error");
            throw new DaoException("Connection Pool Exception", e);
        }
        return user;
    }

    /**
     * Perform update sql request and update User entity in sql database
     * @param user updated entity
     * @throws DaoException if sql or connection pool error was detected
     */
    @Override
    public void update(User user) throws DaoException {
        IConnectionPool connectionPool = ConnectionPoolImpl.getInstance();
        try(
            Connection connection = connectionPool.getConnection();
            PreparedStatement statement = connection.prepareStatement(UPDATE_CUSTOMER_SQL);
        ) {
            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getSecondName());
            statement.setBoolean(3, user.isAdmin());
            statement.setInt(4, user.getId());
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
     * Perform delete sql request and remove User entity from sql database
     * @param user removed entity
     * @throws DaoException if sql or connection pool error was detected
     */
    @Override
    public void delete(User user) throws DaoException {
        IConnectionPool connectionPool = ConnectionPoolImpl.getInstance();
        try(
                Connection connection = connectionPool.getConnection();
                PreparedStatement statement = connection.prepareStatement(DELETE_CUSTOMER_SQL);
        ) {
            statement.setInt(1, user.getId());
            statement.execute();
        } catch (SQLException e) {
            LOGGER.error("Delete user Sql error");
            throw new DaoException("Delete Sql Exception", e);
        } catch (ConnectionPoolException e) {
            LOGGER.error("Connection Pool error");
            throw new DaoException("Connection Pool Exception", e);
        }
    }

    /**
     * Check user id in banned list in sql database
     * @param connection sql connection
     * @param id id of User entity {@link User}
     * @return true if banned list contains User entity id
     * @throws SQLException if sql or connection pool error was detected
     */
    private boolean checkBannedAccount(Connection connection, int id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(CHECK_BANNED_ID_SQL);
        statement.setInt(1, id);
        ResultSet resultSet = statement.executeQuery();
        boolean result = resultSet.next();
        resultSet.close();
        statement.close();
        return result;
    }

    /**
     * Adds id of User entity to ban list {@link User}
     * @param user User entity
     * @throws DaoException if database error was detected
     */
    @Override
    public void addToBanList(User user) throws DaoException {
        IConnectionPool connectionPool = ConnectionPoolImpl.getInstance();
        try(
            Connection connection = connectionPool.getConnection();
            PreparedStatement statement = connection.prepareStatement(ADD_TO_BAN)
        )
        {
            statement.setInt(1, user.getId());
            statement.execute();
        } catch (SQLException e) {
            LOGGER.error("Add to ban list sql error");
            throw new DaoException("Insert sql exception", e);
        } catch (ConnectionPoolException e) {
            LOGGER.error("Connection Pool error");
            throw new DaoException("Connection Pool Exception", e);
        }
    }
}
