package by.epam.task6.controller.command.impl;

import by.epam.task6.bean.Product;
import by.epam.task6.controller.command.Command;
import by.epam.task6.controller.constant.JspPageName;
import by.epam.task6.controller.constant.RequestParameterName;
import by.epam.task6.exception.controller.command.CommandException;
import by.epam.task6.exception.service.ServiceException;
import by.epam.task6.service.GenericService;
import by.epam.task6.service.impl.AddNewProductService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

/**
 * Instance of Command {@link Command}
 * First part of logic for adding new product to database
 * Validate request parameters and create bean for transfer on the service layer
 */
public class AddNewProductCommand implements Command {
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

            product.setName(request.getParameter(RequestParameterName.PRODUCT_NAME));
            product.setDescription(request.getParameter(RequestParameterName.PRODUCT_DESCRIPTION));
            product.setPrice(Double.parseDouble(request.getParameter(RequestParameterName.PRODUCT_PRICE)));

            GenericService<Product, Boolean> addProductService = AddNewProductService.getInstance();

            try {
                if (request.getPart(RequestParameterName.IMAGE).getInputStream().available() != 0){
                    String imageName = loadImage(request);
                    if (imageName != null) {
                        product.setImagePath("images" + File.separator + imageName);
                    }
                }
                addProductService.execute(product);
                page = JspPageName.PROFILE;
            } catch (ServiceException | ServletException | IOException e) {
                throw new CommandException("Add product command exception", e);
            }
        }
        else {
            page = JspPageName.EDIT_PRODUCT_PAGE;
        }

        return page;
    }

    /**
     * Validate request parameters on containing product properties
     * @param request object that contains the request the client has made of the servlet
     * @return true, if request contain all needed parameters
     */
    private boolean validateParameters(HttpServletRequest request) {
        if (request.getSession().getAttribute(RequestParameterName.ADMIN) != null &&
                !request.getParameter(RequestParameterName.PRODUCT_NAME).isEmpty() &&
                !request.getParameter(RequestParameterName.PRODUCT_DESCRIPTION).isEmpty() &&
                !request.getParameter(RequestParameterName.PRODUCT_PRICE).isEmpty()) {
            try {
                Double.parseDouble(request.getParameter(RequestParameterName.PRODUCT_PRICE));
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

    /**
     * Method perform copying selected image to the web package
     * @param request object that contains the request the client has made of the servlet
     *                Must contain Part with image
     * @return name of image's file, that will be store in the database
     * @throws CommandException if IOException has been defined
     */
    private String loadImage(HttpServletRequest request) throws CommandException {
        String imagePath = request.getServletContext().getRealPath("/images/");

        if (imagePath == null) {
            return null;
        }

        try {
            if (!Files.exists(Paths.get(imagePath))) {
                Files.createDirectory(Paths.get(imagePath));
            }
        } catch (IOException e) {
            throw new CommandException("Can't create folder for images", e);
        }

        String imageName = Double.toString(new Date().getTime()) + ".jpg";
        File file = new File(imagePath + File.separator +  imageName);

        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new CommandException("Can't load image", e);
        }

        try(
            InputStream inputStream = request.getPart(RequestParameterName.IMAGE).getInputStream();
            FileOutputStream outputStream = new FileOutputStream(file)
        )
        {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
        } catch (ServletException | IOException e) {
            throw new CommandException("Can't copy image", e);
        }

        return imageName;
    }
}
