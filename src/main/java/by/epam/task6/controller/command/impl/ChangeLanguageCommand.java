package by.epam.task6.controller.command.impl;

import by.epam.task6.controller.command.Command;
import by.epam.task6.controller.constant.RequestParameterName;
import by.epam.task6.exception.controller.command.CommandException;

import javax.servlet.http.HttpServletRequest;
/**
 * Instance of Command {@link Command}
 */
public class ChangeLanguageCommand implements Command {

    /**
     * Change language of content pages
     * Set new language into request
     * @param request object that contains the request the client has made of the servlet
     * @return string that contains link on the page from which user changed localization
     * @throws CommandException
     */
    @Override
    public String execute(HttpServletRequest request) throws CommandException {
        String page;
        request.getSession().setAttribute(RequestParameterName.CURRENT_LANGUAGE, request.getParameter(RequestParameterName.LANGUAGE));
        page = request.getParameter(RequestParameterName.CONTROLLER_PAGE)
                + "?" + RequestParameterName.SUB_COMMAND + "=" + request.getParameter(RequestParameterName.SUB_COMMAND)
                + "&" + RequestParameterName.CURRENT_PAGE + "=" + request.getParameter(RequestParameterName.CURRENT_PAGE);
        return page;
    }
}
