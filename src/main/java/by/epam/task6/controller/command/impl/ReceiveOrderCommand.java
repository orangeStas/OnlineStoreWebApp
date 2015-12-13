package by.epam.task6.controller.command.impl;

import by.epam.task6.bean.Order;
import by.epam.task6.bean.User;
import by.epam.task6.bean.transferobject.OrderData;
import by.epam.task6.controller.command.Command;
import by.epam.task6.controller.constant.JspPageName;
import by.epam.task6.controller.constant.RequestParameterName;
import by.epam.task6.exception.controller.command.CommandException;
import by.epam.task6.exception.service.ServiceException;
import by.epam.task6.service.GenericService;
import by.epam.task6.service.impl.ReceiveOrderService;

import javax.servlet.http.HttpServletRequest;
/**
 * Instance of Command {@link Command}
 * First part of logic for view order's products
 * Validate request parameters and create bean for transfer on the service layer
 */
public class ReceiveOrderCommand implements Command {
    /**
     * Perform creating Order bean for transfer to the service layer {@link Order}
     * Set into request list of order's products and information about order {@link OrderData}
     * @param request object that contains the request the client has made of the servlet
     * @return string that contains link on the page with list of order's products and order information
     * @throws CommandException if service exception is detected
     */
    @Override
    public String execute(HttpServletRequest request) throws CommandException {

        String page;

        if (validateParameters(request)) {

            GenericService<Order, OrderData> receiveOrderService = ReceiveOrderService.getInstance();
            OrderData orderData;
            try {
                Order order = new Order();
                order.setIdCustomer(((User) request.getSession().getAttribute(RequestParameterName.USER)).getId());
                order.setIsConfirmed(false);
                orderData = receiveOrderService.execute(order);
            } catch (ServiceException e) {
                throw new CommandException("Receive Order Command Exception", e);
            }

            if (orderData != null && orderData.getProducts().size() != 0) {
                request.setAttribute(RequestParameterName.PRODUCTS, orderData.getProducts());
                request.setAttribute(RequestParameterName.ORDER, orderData.getOrder());

            }
            page = JspPageName.CONFIRM_PAGE;
        }
        else {
            page = JspPageName.LOGIN;
        }
        return page;
    }

    /**
     * Validate request session on containing user attribute
     * @param request object that contains the request the client has made of the servlet
     * @return true, if request session contain user attribute
     */
    private boolean validateParameters(HttpServletRequest request) {
        if (request.getSession().getAttribute(RequestParameterName.USER) != null) {
            return true;
        }
        else {
            return false;
        }
    }
}
