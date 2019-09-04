package implementation;

import database.DatabaseConnection;
import exception.BusinessException;
import modal.Customers;
import modal.User;

import java.sql.*;
import java.util.ArrayList;

public class BusinessImplementation {

    Connection connection = null;

    public int authenticateUser(String username, String password) throws BusinessException, SQLException {
        connection = DatabaseConnection.getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        int id = 0;

        try {
            String sql = "SELECT * FROM userdata WHERE username = ? AND password = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, password);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                id = resultSet.getInt("userID");
            } else {
                id = 0;
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
        getUser(id);
        return id;
    }

    public void updateUserProfile(String nameProfile, String firmnameProfile, String contactProfile, String addressProfile, int id) throws BusinessException, SQLException {
        connection = DatabaseConnection.getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            String sql = "UPDATE userdata SET Name = ?, firmName = ?, contact = ?, Address = ? WHERE userID = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, nameProfile);
            statement.setString(2, firmnameProfile);
            statement.setString(3, contactProfile);
            statement.setString(4, addressProfile);
            statement.setString(5, String.valueOf(id));

            statement.executeUpdate();
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
    }

    public User getUser(int id) throws BusinessException, SQLException {
        connection = DatabaseConnection.getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        User user = null;

        try {
            String sql = "SELECT * FROM userdata WHERE userID = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, String.valueOf(id));
            resultSet = statement.executeQuery();
            user = new User();

            resultSet.next();
            user.setName(resultSet.getString("Name"));
            user.setFirmname(resultSet.getString("firmName"));
            user.setAddress(resultSet.getString("Address"));
            user.setContact(resultSet.getString("contact"));
        } catch (SQLException e) {
            e.printStackTrace();
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
        return user;
    }

    public ArrayList getCustomers() throws BusinessException, SQLException {
        connection = DatabaseConnection.getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Customers customers = null;
        ArrayList<Customers> customerList = null;

        try {
            String sql = "SELECT * FROM customers ORDER BY fullName ASC";
            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();
            customerList = new ArrayList<>();

            while (resultSet.next()) {
                customers = new Customers();
                customers.setCustomerID(resultSet.getInt("customer_id"));
                customers.setFullName(resultSet.getString("fullName"));
                customers.setSpouseName(resultSet.getString("spouceName"));
                customers.setFatherName(resultSet.getString("fatherName"));
                customers.setAddress(resultSet.getString("address"));
                customers.setWard(resultSet.getInt("ward"));
                customers.setCreatedAt(resultSet.getString("createdAt"));
                customers.setRemarks(resultSet.getString("remarks"));
                customers.setUpdatedAt(resultSet.getString("updatedAt"));
                customers.setContactNo(resultSet.getInt("contactNo"));

                customerList.add(customers);
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
        return customerList;
    }
}
