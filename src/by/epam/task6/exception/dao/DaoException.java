package by.epam.task6.exception.dao;

import by.epam.task6.exception.ApplicationException;

public class DaoException extends ApplicationException {
    public DaoException(String message, Exception e) {
        super(message, e);
    }

    public DaoException(String message) {
        super(message);
    }
}
