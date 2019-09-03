package implementation;

import database.DatabaseConnection;
import exception.BusinessException;

import java.sql.*;

public class BusinessImplementation {

    Connection connection = null;

    public boolean authenticateUser(String username, String password) throws BusinessException, SQLException {
        connection = DatabaseConnection.getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        boolean isValidUser = false;

        try {
            String sql = "SELECT * FROM userdata WHERE username = ? AND password = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, password);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                isValidUser = true;
            } else {
                isValidUser = false;
            }
        } catch (SQLException se) {
            throw new BusinessException(se);
        } finally {
            if (null != resultSet) {
                resultSet.close();
            }
            if (null != statement) {
                statement.close();
            }
            if (null != connection) {
                connection.close();
            }
        }
        return isValidUser;
    }
}
