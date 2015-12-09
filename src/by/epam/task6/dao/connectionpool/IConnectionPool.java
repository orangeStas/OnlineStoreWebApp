package by.epam.task6.dao.connectionpool;

import by.epam.task6.exception.dao.connectionpool.ConnectionPoolException;

import java.sql.Connection;

/**
 * Interface for custom simple Connection pool
 */
public interface IConnectionPool {
    /**
     * Initialize database driver, connect to database, create connections
     * Method execute in listener {@link by.epam.task6.listener.ConnectionProviderListener}
     * @throws ConnectionPoolException appears when can't connect to database or initialize database driver
     */
    void init() throws ConnectionPoolException;

    /**
     * Dispose all created database connections
     */
    void dispose();

    /**
     * Get free connection from connection's queue
     * @return DataBase connection
     * @throws ConnectionPoolException appears when can't get free connection
     */
    Connection getConnection() throws ConnectionPoolException;

    /**
     * Interface for wrapper Connection
     */
    interface IPooledConnection extends Connection{}
}
