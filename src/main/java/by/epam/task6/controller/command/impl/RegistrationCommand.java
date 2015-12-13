package by.epam.task6.controller.command.impl;

import by.epam.task6.bean.transferobject.RegistrationData;
import by.epam.task6.controller.command.Command;
import by.epam.task6.controller.constant.JspPageName;
import by.epam.task6.controller.constant.RequestParameterName;
import by.epam.task6.exception.controller.command.CommandException;
import by.epam.task6.exception.service.ServiceException;
import by.epam.task6.service.GenericService;
import by.epam.task6.service.impl.RegistrationService;

import javax.servlet.http.HttpServletRequest;
/**
 * Instance of Command {@link Command}
 * First part of logic for register user in the application
 * Validate request parameters and create bean for transfer on the service layer
 */
public class RegistrationCommand implements Command {

    /**
     * Perform creating RegistrationData bean for transfer to the service layer {@link RegistrationData}
     * Set message about banned account into request if account was banned
     * @param request object that contains the request the client has made of the servlet
     * @return string that contains link on the login page if registration successful else on the registration page
     * @throws CommandException if service exception is detected
     */
    @Override
    public String execute(HttpServletRequest request) throws CommandException {
        String page;

        if (validateRequest(request)) {

            GenericService<RegistrationData, Boolean> registrationService = RegistrationService.getInstance();

            RegistrationData registrationData = new RegistrationData();

            registrationData.setLogin(request.getParameter(RequestParameterName.USER_NAME));
            registrationData.setPassword(request.getParameter(RequestParameterName.PASSWORD));
            registrationData.setFirstName(request.getParameter(RequestParameterName.FIRST_NAME));
            registrationData.setSecondName(request.getParameter(RequestParameterName.LAST_NAME));

            try {
                if (registrationService.execute(registrationData)){
                    page = JspPageName.LOGIN;
                }
                else {
                    page = JspPageName.REGISTRATION + "?" + RequestParameterName.MESSAGE + "=user_exist";
                }

            } catch (ServiceException e) {
                throw new CommandException("Registration Command Exception", e);
            }
        }

        else {
            page = JspPageName.REGISTRATION;
        }

        return page;
    }

    /**
     * Validate request parameters on containing account and user properties
     * @param request object that contains the request the client has made of the servlet
     * @return true, if request contain all needed parameters
     */
    private boolean validateRequest(HttpServletRequest request) {
        if ((!request.getParameter(RequestParameterName.USER_NAME).isEmpty()) &&
                (!request.getParameter(RequestParameterName.PASSWORD).isEmpty()) &&
                (!request.getParameter(RequestParameterName.FIRST_NAME).isEmpty()) &&
                (!request.getParameter(RequestParameterName.LAST_NAME).isEmpty()))
        {
            return true;
        }
        else {
            return false;
        }
    }
}
