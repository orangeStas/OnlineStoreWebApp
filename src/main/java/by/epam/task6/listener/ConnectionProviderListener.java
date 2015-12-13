package by.epam.task6.listener;

import by.epam.task6.dao.connectionpool.impl.ConnectionPoolImpl;
import by.epam.task6.exception.dao.connectionpool.ConnectionPoolException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ConnectionProviderListener implements ServletContextListener {

    private ConnectionPoolImpl connectionPool;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {

        connectionPool = ConnectionPoolImpl.getInstance();
        try {
            connectionPool.init();
        } catch (ConnectionPoolException e) {
            throw new RuntimeException("Connection pool init error.");
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        if (connectionPool != null) {
            connectionPool.dispose();
        }
    }
}
