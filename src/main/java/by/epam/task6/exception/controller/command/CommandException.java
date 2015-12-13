package by.epam.task6.exception.controller.command;

import by.epam.task6.exception.ApplicationException;

public class CommandException extends ApplicationException {
    public CommandException(String message, Exception e) {
        super(message, e);
    }

    public CommandException(String message) {
        super(message);
    }
}
