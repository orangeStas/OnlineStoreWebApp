package by.epam.task6.dao.impl;

import by.epam.task6.bean.Account;
import by.epam.task6.dao.AccountDao;
import by.epam.task6.dao.connectionpool.IConnectionPool;
import by.epam.task6.dao.connectionpool.impl.ConnectionPoolImpl;
import by.epam.task6.exception.dao.DaoException;
import by.epam.task6.exception.dao.connectionpool.ConnectionPoolException;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * MySQL Instance of a Account Data Access Object
 * Perform direct changes in the sql database
 * Singleton class
 */
public class MySqlAccountDao implements AccountDao {
    private static final MySqlAccountDao instance = new MySqlAccountDao();
    private static final Logger LOGGER = Logger.getLogger(MySqlAccountDao.class.getPackage().getName());

    private static final String ADD_ACCOUNT_SQL = "INSERT INTO ACCOUNT (login, password) VALUES (?,?)";
    private static final String GET_ID_SQL = "SELECT idCustomer FROM account where login = ? and password = ?";

    private MySqlAccountDao() {}

    public static MySqlAccountDao getInstance() {
        return instance;
    }

    /**
     * Insert new Account entity into database
     * and return id of added account
     * @param account Account entity {@link Account}
     * @return id of added account
     * @throws DaoException if define error in sql or connection pool
     */
    @Override
    public Integer create(Account account) throws DaoException {
        IConnectionPool connectionPool = ConnectionPoolImpl.getInstance();
        try(
            Connection connection = connectionPool.getConnection();
            PreparedStatement statement = setStatement(connection,ADD_ACCOUNT_SQL, account.getLogin(), account.getPassword())
        )
        {
            statement.execute();
        } catch (SQLException e) {
            LOGGER.error("Sql error");
            throw new DaoException("Sql Exception", e);
        } catch (ConnectionPoolException e) {
            LOGGER.error("Connection Pool error");
            throw new DaoException("Connection Pool Exception", e);
        }

        return findIdAccount(account);
    }

    @Override
    public Account find(Integer id) {
        return null;
    }

    @Override
    public void update(Account transientObject) {

    }

    @Override
    public void delete(Account persistentObject) {

    }

    /**
     * Find id account in the database by login and password
     * @param account entity Account {@link Account}
     * @return id of searched account
     * @throws DaoException if define error in sql or connection pool
     */
    @Override
    public int findIdAccount(Account account) throws DaoException {
        IConnectionPool connectionPool = ConnectionPoolImpl.getInstance();

        int id = -1;
        try(
            Connection connection = connectionPool.getConnection();
            PreparedStatement statement = setStatement(connection, GET_ID_SQL, account.getLogin(), account.getPassword());
            ResultSet resultSet = statement.executeQuery()
        )
        {
            if (resultSet.next()) {
                id = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            LOGGER.error("Find account sql error");
            throw new DaoException("Sql Exception", e);
        } catch (ConnectionPoolException e) {
            LOGGER.error("Connection Pool error");
            throw new DaoException("Connection Pool Exception", e);
        }
        return id;
    }

    /**
     * Get statement from connection and set parameters in the statement
     * @param connection Database connection from that gets statement
     * @param sqlRequest String that contains sql request
     * @param login String that contains login parameter
     * @param password String that contains password parameter
     * @return ready sql statement
     * @throws SQLException if statement cant' accept parameters
     */
    private PreparedStatement setStatement(Connection connection, String sqlRequest, String login, String password) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(sqlRequest);
        statement.setString(1, login);
        statement.setString(2, DigestUtils.md5Hex(password));
        return statement;
    }
}
