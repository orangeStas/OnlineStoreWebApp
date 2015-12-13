package by.epam.task6.controller.command.impl;

import by.epam.task6.bean.Product;
import by.epam.task6.controller.command.Command;
import by.epam.task6.controller.constant.JspPageName;
import by.epam.task6.controller.constant.RequestParameterName;
import by.epam.task6.exception.controller.command.CommandException;
import by.epam.task6.exception.service.ServiceException;
import by.epam.task6.service.GenericService;
import by.epam.task6.service.impl.RemoveProductService;

import javax.servlet.http.HttpServletRequest;
/**
 * Instance of Command {@link Command}
 * First part of logic for removing product from database
 * Validate request parameters and create bean for transfer on the service layer
 */
public class RemoveProductCommand implements Command {
    /**
     * Perform creating Product bean for transfer to the service layer {@link Product}
     * @param request object that contains the request the client has made of the servlet
     * @return string that contains link on the Profile controller thar perform updating data on user page
     * @throws CommandException if service exception is detected
     */
    @Override
    public String execute(HttpServletRequest request) throws CommandException {
        String page;

        if (validateParameters(request)) {
            Product product = new Product();
            product.setId(Integer.parseInt(request.getParameter(RequestParameterName.ID_PRODUCT)));

            GenericService<Product, Boolean> service = RemoveProductService.getInstance();

            try {
                service.execute(product);
            } catch (ServiceException e) {
                throw new CommandException("Remove product command exception", e);
            }
        }

        page = JspPageName.PROFILE + "?" + RequestParameterName.PAGE_NUMBER + "=" + request.getParameter(RequestParameterName.PAGE_NUMBER);

        return page;
    }

    /**
     * Validate request parameters on containing product id
     * @param request object that contains the request the client has made of the servlet
     * @return true, if request contain product id
     */
    private boolean validateParameters(HttpServletRequest request) {
        if (request.getSession().getAttribute(RequestParameterName.ADMIN) != null &&
                !request.getParameter(RequestParameterName.ID_PRODUCT).isEmpty()) {
            return true;
        }
        else {
            return false;
        }
    }
}
