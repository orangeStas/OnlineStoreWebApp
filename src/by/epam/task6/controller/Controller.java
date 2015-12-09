package by.epam.task6.controller;

import by.epam.task6.controller.command.Command;
import by.epam.task6.controller.command.CommandHelper;
import by.epam.task6.controller.command.CommandName;
import by.epam.task6.controller.constant.JspPageName;
import by.epam.task6.controller.constant.RequestParameterName;
import by.epam.task6.controller.security.SecurityHandler;
import by.epam.task6.controller.security.UserType;
import by.epam.task6.exception.controller.command.CommandException;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * HTTP servlet suitable for a Web application.
 * Handle HTTP requests by dispatching them to the handler methods for each HTTP request type
 * This servlet execute commands by client's requests
 */
@MultipartConfig
public class Controller extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(Controller.class.getPackage().getName());

    /**
     * Called by the server (via the service method) to allow a servlet to handle a POST request.
     * Method accept commands from client and handle almost all logic of the application
     * Defining type of command, Getting needed Command class and execute it.
     * @param request object that contains the request the client has made of the servlet
     * @param response object that contains the response the servlet sends to the client
     * @throws ServletException if an input or output error is detected when the servlet handles the request
     * @throws IOException if the request for the POST could not be handled
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String commandName = request.getParameter(RequestParameterName.COMMAND_NAME);
        RequestDispatcher requestDispatcher;

        if (SecurityHandler.defineUserType(request) == UserType.UNDEFINED && !commandName.equals(CommandName.LANGUAGE.toString().toLowerCase())) {
            requestDispatcher = request.getRequestDispatcher(JspPageName.LOGIN);
            requestDispatcher.forward(request, response);
        }
        else {

            Command command = CommandHelper.getCommand(commandName);
            String page = null;

            try {
                page = command.execute(request);
            } catch (CommandException e) {
                LOGGER.error("Execute command failed");
                throw new ServletException(e);
            }
            response.sendRedirect(page);
        }
    }

    /**
     * Called by the server (via the service method) to allow a servlet to handle a GET request.
     * This method handle logout clients from account, clearing cache and invalidation session
     * @param request object that contains the request the client has made of the servlet
     * @param response object that contains the response the servlet sends to the client
     * @throws ServletException if an input or output error is detected when the servlet handles the GET request
     * @throws IOException if the request for the GET could not be handled
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Cache-Control", "no-cache, no-store");
        response.setHeader("Pragma", "no-cache");
        String page;

        String commandName = request.getParameter(RequestParameterName.COMMAND_NAME);
        if (commandName == null) {
            page = JspPageName.LOGIN;
        }

        else {
            Command command = CommandHelper.getCommand(commandName);

            try {
                page = command.execute(request);
            } catch (CommandException e) {
                LOGGER.debug("Execute command failed");
                page = JspPageName.ERROR;
            }
        }

        response.sendRedirect(page);
    }
}
