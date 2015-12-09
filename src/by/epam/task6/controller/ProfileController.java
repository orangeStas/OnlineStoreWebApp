package by.epam.task6.controller;

import by.epam.task6.controller.command.Command;
import by.epam.task6.controller.command.CommandHelper;
import by.epam.task6.controller.command.CommandName;
import by.epam.task6.controller.constant.JspPageName;
import by.epam.task6.controller.constant.RequestParameterName;
import by.epam.task6.controller.security.SecurityHandler;
import by.epam.task6.exception.controller.command.CommandException;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * HTTP servlet suitable for a Web application.
 * Handle HTTP requests by dispatching them to the handler methods for each HTTP request type
 * This servlet handle security of clients, handle reiterative request from client(f5),
 * iterative load data when client or admin change localization,
 * define user type(client/admin) and redirect him to right home page.
 */
public class ProfileController extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(ProfileController.class.getPackage().getName());

    /**
     * Called by the server (via the service method) to allow a servlet to handle a GET request.
     * Security handling, right redirecting client types, loading client/admin data
     * @param request object that contains the request the client has made of the servlet
     * @param response object that contains the response the servlet sends to the client
     * @throws ServletException if an input or output error is detected when the servlet handles the GET request
     * @throws IOException if the request for the GET could not be handled
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String page;

        switch (SecurityHandler.defineUserType(request)) {
            case USER: {
                page = JspPageName.HOME;
                break;
            }
            case ADMIN: {
                page = JspPageName.HOME_ADMIN;
                break;
            }
            default: {
                page = JspPageName.LOGIN;
            }
        }

        if (request.getParameter(RequestParameterName.CURRENT_PAGE) != null) {
            page = request.getParameter(RequestParameterName.CURRENT_PAGE);
        }

        String commandName;
        try {
            if ((commandName = request.getParameter(RequestParameterName.SUB_COMMAND)) != null) {
                Command command = CommandHelper.getCommand(commandName);
                command.execute(request);
            }
            else {
                Command command = CommandHelper.getCommand(CommandName.LOAD_PRODUCTS.toString());
                command.execute(request);
            }
        } catch (CommandException e) {
            LOGGER.debug("Execute command failed.");
            throw new ServletException(e);
        }

        RequestDispatcher requestDispatcher = request.getRequestDispatcher(page);

        if (requestDispatcher != null) {
            requestDispatcher.forward(request, response);
        }
    }
}
