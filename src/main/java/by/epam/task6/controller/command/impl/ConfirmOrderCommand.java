package by.epam.task6.controller.command.impl;

import by.epam.task6.bean.Order;
import by.epam.task6.bean.User;
import by.epam.task6.controller.command.Command;
import by.epam.task6.controller.constant.JspPageName;
import by.epam.task6.controller.constant.RequestParameterName;
import by.epam.task6.exception.controller.command.CommandException;
import by.epam.task6.exception.service.ServiceException;
import by.epam.task6.service.GenericService;
import by.epam.task6.service.impl.ConfirmOrderService;

import javax.servlet.http.HttpServletRequest;

/**
 * Instance of Command {@link Command}
 * First part of logic for confirming client's order
 */
public class ConfirmOrderCommand implements Command {
    /**
     * Perform creating Product bean for transfer to the service layer {@link Order}
     * @param request object that contains the request the client has made of the servlet
     * @return string that contains link on the page from which user confirmed order
     * @throws CommandException if service exception is detected
     */
    @Override
    public String execute(HttpServletRequest request) throws CommandException {
        String page;

        GenericService<Order, Boolean> confirmOrderService = ConfirmOrderService.getInstance();
        Order order;
        try {
            order = new Order();
            order.setIdCustomer(((User) request.getSession().getAttribute(RequestParameterName.USER)).getId());
            confirmOrderService.execute(order);
            page = JspPageName.CONFIRM_PAGE;
        } catch (ServiceException e) {
            throw new CommandException("Confirm Order Command Exception", e);
        }
        return page;
    }
}
