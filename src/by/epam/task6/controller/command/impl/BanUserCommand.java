package by.epam.task6.controller.command.impl;

import by.epam.task6.bean.User;
import by.epam.task6.controller.command.Command;
import by.epam.task6.controller.command.CommandName;
import by.epam.task6.controller.constant.JspPageName;
import by.epam.task6.controller.constant.RequestParameterName;
import by.epam.task6.exception.controller.command.CommandException;
import by.epam.task6.exception.service.ServiceException;
import by.epam.task6.service.GenericService;
import by.epam.task6.service.impl.BanUserService;

import javax.servlet.http.HttpServletRequest;

/**
 * Instance of Command {@link Command}
 * First part of logic for adding user to ban list in the database
 * Validate request parameters and create bean for transfer on the service layer
 */
public class BanUserCommand implements Command {
    /**
     * Perform creating User bean for transfer to the service layer {@link User}
     * @param request object that contains the request the client has made of the servlet
     * @return string that contains link on the Profile controller thar perform updating data on user page
     * @throws CommandException if service exception is detected
     */
    @Override
    public String execute(HttpServletRequest request) throws CommandException {
        String page;
        if (validateParameters(request)) {
            GenericService<User, Boolean> banService = BanUserService.getInstance();

            User user = new User();

            user.setId(Integer.parseInt(request.getParameter(RequestParameterName.USER_ID)));

            try {
                banService.execute(user);
            } catch (ServiceException e) {
                throw new CommandException("Ban user Command Exception", e);
            }
        }

        page = JspPageName.PROFILE + "?" + RequestParameterName.SUB_COMMAND + "=" + CommandName.LOAD_UNPAID_ORDERS.toString().toLowerCase()
                + "&" + RequestParameterName.CURRENT_PAGE + "=" + JspPageName.ORDERS_PAGE;

        return page;

    }

    /**
     * Validate request parameters on containing customer's id
     * @param request object that contains the request the client has made of the servlet
     * @return true, if request contain all needed parameters
     */
    private boolean validateParameters(HttpServletRequest request) {
        if (request.getSession().getAttribute(RequestParameterName.ADMIN) != null &&
                !request.getParameter(RequestParameterName.USER_ID).isEmpty()) {
            try {
                Integer.parseInt(request.getParameter(RequestParameterName.USER_ID));
            }
            catch (NumberFormatException e) {
                return false;
            }
            return true;
        }
        else {
            return false;
        }
    }
}
