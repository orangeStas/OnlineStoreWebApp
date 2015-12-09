package by.epam.task6.controller.command.impl;

import by.epam.task6.controller.command.Command;
import by.epam.task6.controller.constant.JspPageName;
import by.epam.task6.controller.constant.RequestParameterName;
import by.epam.task6.exception.controller.command.CommandException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Instance of Command {@link Command}
 * Using for user logout
 */
public class LogoutCommand implements Command {

    /**
     * Remove user from session
     * @param request object that contains the request the client has made of the servlet
     * @return string that contains link on login page
     * @throws CommandException
     */
    @Override
    public String execute(HttpServletRequest request) throws CommandException {
        String page = JspPageName.LOGIN;

        HttpSession session = request.getSession(true);
        session.removeAttribute(RequestParameterName.USER);
        session.removeAttribute(RequestParameterName.ADMIN);
        session.invalidate();

        return page;
    }
}
