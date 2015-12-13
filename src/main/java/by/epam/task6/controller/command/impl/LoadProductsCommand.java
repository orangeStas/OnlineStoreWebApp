package by.epam.task6.controller.command.impl;

import by.epam.task6.bean.Order;
import by.epam.task6.bean.User;
import by.epam.task6.bean.transferobject.OrderData;
import by.epam.task6.bean.transferobject.PaginationData;
import by.epam.task6.bean.transferobject.ProductPerPageData;
import by.epam.task6.controller.command.Command;
import by.epam.task6.controller.command.CommandName;
import by.epam.task6.controller.constant.JspPageName;
import by.epam.task6.controller.constant.RequestParameterName;
import by.epam.task6.exception.controller.command.CommandException;
import by.epam.task6.exception.service.ServiceException;
import by.epam.task6.service.GenericService;
import by.epam.task6.service.impl.LoadProductsService;
import by.epam.task6.service.impl.ReceiveOrderService;

import javax.servlet.http.HttpServletRequest;
/**
 * Instance of Command {@link Command}
 * First part of logic for view products on current user page and view how many products has been added to order by customer
 */
public class LoadProductsCommand implements Command {

    /**
     * use if request doesn't contain number of current page
     */
    private static final int DEFAULT_PAGE_NUMBER = 1;
    /**
     * set count of products that will be view on page
     */
    private static final int DEFAULT_RECORDS_PER_PAGE = 3;

    /**
     * Perform creating PaginationData bean and Order bean  for transfer to the service layer {@link PaginationData} {@link Order}
     * Set into request list of products and count product in order that will be view on the page
     * @param request object that contains the request the client has made of the servlet
     * @return string that contains link on the Profile controller thar perform updating data on user page
     * @throws CommandException if service exception is detected
     */
    @Override
    public String execute(HttpServletRequest request) throws CommandException {
        String page;

        GenericService<PaginationData, ProductPerPageData> loadProductsService = LoadProductsService.getInstance();
        GenericService<Order, OrderData> receiveOrderService = ReceiveOrderService.getInstance();

        OrderData orderData;
        try {
            PaginationData paginationData = formPaginationData(request);
            ProductPerPageData productsPerPageData = loadProductsService.execute(paginationData);

            if (productsPerPageData != null) {

                int countPages = (int) Math.ceil(productsPerPageData.getCountAllProducts() * 1.0 / DEFAULT_RECORDS_PER_PAGE);
                request.setAttribute(RequestParameterName.COUNT_PAGES, countPages);

                request.setAttribute(RequestParameterName.PRODUCTS, productsPerPageData.getProducts());
            }

            if (request.getSession().getAttribute(RequestParameterName.USER) != null) {
                Order order = new Order();
                order.setIdCustomer(((User) request.getSession().getAttribute(RequestParameterName.USER)).getId());
                order.setIsConfirmed(false);
                orderData = receiveOrderService.execute(order);
                if (orderData != null) {
                    request.setAttribute(RequestParameterName.COUNT_PRODUCTS_IN_ORDER, orderData.getProducts().size());
                }
            }
            page = JspPageName.PROFILE + "?" + RequestParameterName.SUB_COMMAND + "=" + CommandName.LOAD_PRODUCTS.toString().toLowerCase() +"&" +
                    RequestParameterName.PAGE_NUMBER + "=" + request.getParameter(RequestParameterName.PAGE_NUMBER);
        } catch (ServiceException e) {
            throw new CommandException("LoadProducts Command Exception");
        }

        return page;
    }

    /**
     * Generate PaginationData bean {@link PaginationData} that contain needed count products per page and
     * define offset position from which database will be getting products.
     * @param request object that contains the request the client has made of the servlet.
     *                Must contain number of page
     * @return {@link PaginationData} that contain needed count products per page and offset position
     */
    private PaginationData formPaginationData(HttpServletRequest request) {
        int pageNumber = DEFAULT_PAGE_NUMBER;
        int recordsPerPage = DEFAULT_RECORDS_PER_PAGE;

        if (request.getParameter(RequestParameterName.PAGE_NUMBER) != null) {
            pageNumber = Integer.parseInt(request.getParameter(RequestParameterName.PAGE_NUMBER));
        }

        request.setAttribute(RequestParameterName.PAGE_NUMBER, pageNumber);

        int offset = (pageNumber - 1) * recordsPerPage;

        PaginationData paginationData = new PaginationData();
        paginationData.setCountRecordsPerPage(recordsPerPage);
        paginationData.setOffset(offset);

        return paginationData;
    }

}
