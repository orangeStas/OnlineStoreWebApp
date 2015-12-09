package by.epam.task6.controller.command.impl;

import by.epam.task6.bean.User;
import by.epam.task6.bean.transferobject.CustomerProductData;
import by.epam.task6.controller.command.Command;
import by.epam.task6.controller.constant.JspPageName;
import by.epam.task6.controller.constant.RequestParameterName;
import by.epam.task6.exception.controller.command.CommandException;
import by.epam.task6.exception.service.ServiceException;
import by.epam.task6.service.GenericService;
import by.epam.task6.service.impl.AddProductToOrderService;

import javax.servlet.http.HttpServletRequest;

/**
 * Instance of Command {@link Command}
 * First part of logic for adding product to customer's order to database
 * Validate request parameters and create bean for transfer on the service layer
 */
public class AddProductToOrderCommand implements Command {
    /**
     * Perform creating CustomerProductData bean for transfer to the service layer {@link CustomerProductData}
     * @param request object that contains the request the client has made of the servlet
     * @return string that contains link on the Profile controller thar perform updating data on user page
     * @throws CommandException if service exception is detected
     */
    @Override
    public String execute(HttpServletRequest request) throws CommandException {
        String page = null;

        if (validateParameters(request)) {
            CustomerProductData customerProductData = new CustomerProductData();
            customerProductData.setIdCustomer(((User) request.getSession().getAttribute(RequestParameterName.USER)).getId());
            customerProductData.setIdProduct(Integer.parseInt(request.getParameter(RequestParameterName.ID_PRODUCT)));

            GenericService<CustomerProductData, Boolean> addProductService = AddProductToOrderService.getInstance();

            try {
                addProductService.execute(customerProductData);
                page = JspPageName.PROFILE + "?" + RequestParameterName.PAGE_NUMBER + "=" + request.getParameter(RequestParameterName.PAGE_NUMBER);
            } catch (ServiceException e) {
                throw new CommandException("Add product to order command Exception", e);
            }
        }
        return page;
    }

    /**
     * Validate request parameters on containing customer and product properties
     * @param request object that contains the request the client has made of the servlet
     * @return true, if request contain all needed parameters
     */
    private boolean validateParameters(HttpServletRequest request) {
        if ((request.getSession().getAttribute(RequestParameterName.USER) != null) &&
                (!request.getParameter(RequestParameterName.ID_PRODUCT).isEmpty())) {
            try {
                Integer.parseInt(request.getParameter(RequestParameterName.ID_PRODUCT));
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
