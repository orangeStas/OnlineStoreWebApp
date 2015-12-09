package by.epam.task6.controller.command;

import by.epam.task6.exception.controller.command.CommandException;

import javax.servlet.http.HttpServletRequest;

/**
 * Realization of Command pattern
 */
public interface Command {
    /**
     * @param request object that contains the request the client has made of the servlet
     * @return string that contains link to page, on which user will be redirected
     * @throws CommandException if service exception is detected
     */
    String execute(HttpServletRequest request) throws CommandException;
}
