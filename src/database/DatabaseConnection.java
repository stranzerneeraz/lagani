package database;

import constants.ApplicationConstants;
import exception.BusinessException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    public static Connection getConnection() throws BusinessException {
        Connection connection = null;
        try {
            Class.forName(ApplicationConstants.DRIVER_NAME).newInstance();
            connection = DriverManager.getConnection(ApplicationConstants.DB_URL, ApplicationConstants.DB_USERNAME,
                    ApplicationConstants.DB_PASSWORD);
            System.out.println("Connection Successful");
        } catch (SQLException se) {
            System.out.println(se);
            throw new BusinessException(se);
        } catch (ClassNotFoundException ce) {
            System.out.println(ce);
            throw new BusinessException(ce);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new BusinessException(e);
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return connection;
    }
}