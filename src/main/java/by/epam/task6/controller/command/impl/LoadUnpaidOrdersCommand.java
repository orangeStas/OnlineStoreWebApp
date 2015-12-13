package by.epam.task6.controller.command.impl;

import by.epam.task6.bean.transferobject.UnpaidOrdersData;
import by.epam.task6.controller.command.Command;
import by.epam.task6.controller.command.CommandName;
import by.epam.task6.controller.constant.JspPageName;
import by.epam.task6.controller.constant.RequestParameterName;
import by.epam.task6.exception.controller.command.CommandException;
import by.epam.task6.exception.service.ServiceException;
import by.epam.task6.service.GenericService;
import by.epam.task6.service.impl.LoadUnpaidOrdersService;

import javax.servlet.http.HttpServletRequest;

/**
 * Instance of Command {@link Command}
 * First part of logic for load orders from database
 */
public class LoadUnpaidOrdersCommand implements Command {

    /**
     * Perform calling to the service layer and wait for response
     * @param request object that contains the request the client has made of the servlet
     * @return string that contains link on the Profile controller thar perform updating data on user page
     * @throws CommandException if service exception is detected
     */
    @Override
    public String execute(HttpServletRequest request) throws CommandException {
        String page;

        if (validateParameters(request)) {
            GenericService<Boolean, UnpaidOrdersData> loadOrdersService = LoadUnpaidOrdersService.getInstance();

            UnpaidOrdersData unpaidOrdersData;
            try {
                unpaidOrdersData = loadOrdersService.execute(true);

                if (unpaidOrdersData != null) {
                    request.setAttribute(RequestParameterName.UNPAID_ORDERS, unpaidOrdersData.getUnpaidOrders());
                }
                page = JspPageName.PROFILE + "?" + RequestParameterName.SUB_COMMAND + "=" + CommandName.LOAD_UNPAID_ORDERS.toString().toLowerCase();
            } catch (ServiceException e) {
                throw new CommandException("LoadOrders Command Exception");
            }
        }
        else {
            page = JspPageName.LOGIN;
        }


        return page;
    }

    /**
     * Validate request parameters on containing admin in session
     * @param request object that contains the request the client has made of the servlet
     * @return true, if request contain all needed parameters
     */
    private boolean validateParameters(HttpServletRequest request) {
        if (request.getSession().getAttribute(RequestParameterName.ADMIN) != null) {
            return true;
        }

        else {
            return false;
        }
    }
}
