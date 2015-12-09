import by.epam.task6.dao.connectionpool.IConnectionPool;
import by.epam.task6.dao.connectionpool.impl.ConnectionPoolImpl;
import by.epam.task6.exception.dao.connectionpool.ConnectionPoolException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConnectionPoolTest {

    private IConnectionPool connectionPool;
    private static final String GET_PRODUCTS_SQL = "SELECT * from product";

    @Before
    public void init() {
        connectionPool = ConnectionPoolImpl.getInstance();
        try {
            connectionPool.init();
        } catch (ConnectionPoolException e) {
            e.printStackTrace();
        }
    }

    @After
    public void close() {
        connectionPool.dispose();
    }

    @Test
    public void testPool() {
        for (int i = 0; i < 10000; i++) {

            new Thread() {
                @Override
                public void run() {
                    try(Connection connection = connectionPool.getConnection();
                        PreparedStatement statement = connection.prepareStatement(GET_PRODUCTS_SQL);
                        ResultSet resultSet = statement.executeQuery();
                    ) {

                    } catch (SQLException | ConnectionPoolException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }
}
