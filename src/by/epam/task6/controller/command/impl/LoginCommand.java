package by.epam.task6.controller.command.impl;

import by.epam.task6.bean.Account;
import by.epam.task6.bean.User;
import by.epam.task6.controller.command.Command;
import by.epam.task6.controller.constant.JspPageName;
import by.epam.task6.controller.constant.RequestParameterName;
import by.epam.task6.exception.controller.command.CommandException;
import by.epam.task6.exception.service.ServiceException;
import by.epam.task6.service.GenericService;
import by.epam.task6.service.impl.LoginService;

import javax.servlet.http.HttpServletRequest;

/**
 * Instance of Command {@link Command}
 * First part of logic for login user in application
 * Validate request parameters and create bean for transfer on the service layer
 */
public class LoginCommand implements Command {

    /**
     * Perform creating Account bean for transfer to the service layer {@link Account}
     * Set user into session
     * Check if user was banned
     * @param request object that contains the request the client has made of the servlet
     * @return string that contains link on the Profile controller thar perform updating data on user page else
     *          on login page if user type wrong data or user's account was banned.
     * @throws CommandException if service exception is detected
     */
    @Override
    public String execute(HttpServletRequest request) throws CommandException {

        String page;

        if (validateRequest(request)) {

            Account account = new Account();
            account.setLogin(request.getParameter(RequestParameterName.USER_NAME));
            account.setPassword(request.getParameter(RequestParameterName.PASSWORD));
            GenericService<Account, User> loginService2 = LoginService.getInstance();
            User user = null;
            try {
                user = loginService2.execute(account);
            } catch (ServiceException e) {
                throw new CommandException("Login Command Exception", e);
            }

            if (user == null) {
                return JspPageName.LOGIN + "?" + RequestParameterName.MESSAGE + "=incorrect";
            }

            if (user.isBanned()) {
                return JspPageName.LOGIN + "?" + RequestParameterName.MESSAGE + "=ban";
            }

            if (user.isAdmin()) {
                request.getSession(true).setAttribute(RequestParameterName.ADMIN, user);
            }
            else {
                request.getSession(true).setAttribute(RequestParameterName.USER, user);
            }
            page = JspPageName.PROFILE;
        }
        else {
            page = JspPageName.LOGIN;
        }

        return page;
    }

    /**
     * Validate request parameters on containing customer and product properties
     * @param request object that contains the request the client has made of the servlet
     * @return true, if request contain all needed parameters
     */
    private boolean validateRequest(HttpServletRequest request) {
        if ((!request.getParameter(RequestParameterName.USER_NAME).isEmpty()) &&
                (!request.getParameter(RequestParameterName.PASSWORD).isEmpty())) {
            return true;
        }
        else {
            return false;
        }
    }
}
