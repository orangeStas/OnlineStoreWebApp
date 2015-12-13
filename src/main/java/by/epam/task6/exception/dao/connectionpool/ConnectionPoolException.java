package by.epam.task6.exception.dao.connectionpool;

import by.epam.task6.exception.ApplicationException;

public class ConnectionPoolException extends ApplicationException {
    private static final long serialVersionUID = 1L;


    public ConnectionPoolException(String message, Exception e) {
        super(message, e);
    }

    public ConnectionPoolException(String message) {
        super(message);
    }
}
