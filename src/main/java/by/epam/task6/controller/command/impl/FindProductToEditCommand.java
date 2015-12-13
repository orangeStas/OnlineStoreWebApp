package by.epam.task6.controller.command.impl;

import by.epam.task6.bean.Product;
import by.epam.task6.controller.command.Command;
import by.epam.task6.controller.constant.JspPageName;
import by.epam.task6.controller.constant.RequestParameterName;
import by.epam.task6.exception.controller.command.CommandException;
import by.epam.task6.exception.service.ServiceException;
import by.epam.task6.service.GenericService;
import by.epam.task6.service.impl.FindProductToEditService;

import javax.servlet.http.HttpServletRequest;

/**
 * Instance of Command {@link Command}
 * First part of logic for searching product for edition
 * Validate request parameters and create bean for transfer on the service layer
 */
public class FindProductToEditCommand implements Command {

    /**
     * Perform creating Product bean for transfer to the service layer {@link Product}
     * Insert into request product that has been found
     * @param request object that contains the request the client has made of the servlet
     * @return string that contains link on the page for editing product properties
     * @throws CommandException if service exception is detected
     */
    @Override
    public String execute(HttpServletRequest request) throws CommandException {
        String page;

        if (validateParameters(request)) {
            GenericService<Integer, Product> findProductService = FindProductToEditService.getInstance();
            Integer idProduct = Integer.parseInt(request.getParameter(RequestParameterName.ID_PRODUCT));
            Product product;
            try {
                product = findProductService.execute(idProduct);
                if (product != null) {
                    request.setAttribute(RequestParameterName.PRODUCT, product);
                    page = JspPageName.EDIT_PRODUCT_PAGE;
                }
                else {
                    page = JspPageName.HOME_ADMIN;
                }
            } catch (ServiceException e) {
                throw new CommandException("Find Product Command Exception", e);
            }
        }
        else {
            page = JspPageName.HOME_ADMIN;
        }

        return page;
    }

    /**
     * Validate request parameters on containing product's id
     * @param request object that contains the request the client has made of the servlet
     * @return true, if request contain product's id
     */
    private boolean validateParameters(HttpServletRequest request) {
        if (request.getParameter(RequestParameterName.ID_PRODUCT) != null) {
            return true;
        }
        else {
            return false;
        }
    }
}
