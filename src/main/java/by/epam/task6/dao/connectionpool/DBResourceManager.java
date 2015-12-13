package by.epam.task6.dao.connectionpool;

import java.util.ResourceBundle;

/**
 * Load parameters for connect to database
 * Singleton pattern
 */
public class DBResourceManager {
    private final static DBResourceManager instance = new DBResourceManager();

    private DBResourceManager(){}

    private ResourceBundle bundle = ResourceBundle.getBundle("db");

    public static DBResourceManager getInstance(){
        return instance;
    }

    public String getValue(String key){
        return bundle.getString(key);
    }
}
