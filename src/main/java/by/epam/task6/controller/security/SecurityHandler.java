package by.epam.task6.controller.security;

import by.epam.task6.controller.command.CommandName;
import by.epam.task6.controller.constant.RequestParameterName;

import javax.servlet.http.HttpServletRequest;

/**
 * Checks user in session
 */
public class SecurityHandler {

    private SecurityHandler(){}

    /**
     * Method check user in session and define user type {@link UserType}
     * @param request object that contains the request the client has made of the servlet
     * @return type of user (admin/user)
     */
    public static UserType defineUserType(HttpServletRequest request) {

        String commandName;
        if ((commandName = request.getParameter(RequestParameterName.COMMAND_NAME)) != null) {
            commandName = commandName.toUpperCase();
            if (commandName.equals(CommandName.LOGIN.toString()) ||
                    commandName.equals(CommandName.REGISTER.toString())) {
                return UserType.USER;
            }
        }

        if (checkUserInSession(request)) {
            return UserType.USER;
        }
        else if (checkAdminInSession(request)) {
            return UserType.ADMIN;
        }
        else return UserType.UNDEFINED;
    }

    private static boolean checkUserInSession(HttpServletRequest request) {
        return (request.getSession().getAttribute(RequestParameterName.USER) != null);
    }

    private static boolean checkAdminInSession(HttpServletRequest request) {
        return (request.getSession().getAttribute(RequestParameterName.ADMIN) != null);
    }
}
