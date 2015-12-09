package by.epam.task6.controller.command;

import by.epam.task6.controller.command.impl.*;
import by.epam.task6.controller.command.impl.LogoutCommand;

import java.util.HashMap;

/**
 * Class for handling commands and command types
 */
public class CommandHelper {

    private CommandHelper(){
    }

    private static HashMap<CommandName, Command> availableCommands = new HashMap<>();
    static {
        availableCommands.put(CommandName.LOGIN, new LoginCommand());
        availableCommands.put(CommandName.LOGOUT, new LogoutCommand());
        availableCommands.put(CommandName.REGISTER, new RegistrationCommand());
        availableCommands.put(CommandName.LANGUAGE, new ChangeLanguageCommand());
        availableCommands.put(CommandName.LOAD_PRODUCTS, new LoadProductsCommand());
        availableCommands.put(CommandName.CHOOSE_PRODUCT, new AddProductToOrderCommand());
        availableCommands.put(CommandName.RECEIVE_ORDER, new ReceiveOrderCommand());
        availableCommands.put(CommandName.CONFIRM_ORDER, new ConfirmOrderCommand());
        availableCommands.put(CommandName.FIND_PRODUCT_TO_EDIT, new FindProductToEditCommand());
        availableCommands.put(CommandName.EDIT_PRODUCT, new EditProductCommand());
        availableCommands.put(CommandName.REMOVE_PRODUCT, new RemoveProductCommand());
        availableCommands.put(CommandName.ADD_PRODUCT, new AddNewProductCommand());
        availableCommands.put(CommandName.REMOVE_PRODUCT_FROM_ORDER, new RemoveProductFromOrderCommand());
        availableCommands.put(CommandName.LOAD_UNPAID_ORDERS, new LoadUnpaidOrdersCommand());
        availableCommands.put(CommandName.BAN_USER, new BanUserCommand());
    }

    /**
     * Method for returning instance of Command by user request
     * May produce exception if command for command type doesn't exist
     * @param commandName string that contains command type
     * @return instance of needed Command
     */
    public static Command getCommand(String commandName) {
        CommandName name = CommandName.valueOf(commandName.toUpperCase());
        return availableCommands.get(name);
    }

}
