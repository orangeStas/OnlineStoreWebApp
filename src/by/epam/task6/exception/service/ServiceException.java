package by.epam.task6.exception.service;

import by.epam.task6.exception.ApplicationException;

public class ServiceException extends ApplicationException {
    public ServiceException(String message, Exception e) {
        super(message, e);
    }

    public ServiceException(String message) {
        super(message);
    }
}
