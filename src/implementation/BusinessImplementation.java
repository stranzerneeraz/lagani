package implementation;

import database.DatabaseConnection;
import exception.BusinessException;
import modal.Customers;
import modal.Installment;
import modal.Items;
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

    public ArrayList<Customers> getCustomers() throws BusinessException, SQLException {
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
                customers.setCustomerID(resultSet.getInt("customerID"));
                customers.setFullName(resultSet.getString("fullName"));
                customers.setSpouseName(resultSet.getString("spouseName"));
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

    public ArrayList<Items> getItems() throws BusinessException, SQLException {
        connection = DatabaseConnection.getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Items items = null;
        ArrayList<Items> itemList = null;

        try {
            String sql = "SELECT * FROM customers ORDER BY fullName ASC";
            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();
            itemList = new ArrayList<>();

            while (resultSet.next()) {
                items = new Items();
                items.setItemID(resultSet.getInt("itemID"));
                items.setType(resultSet.getString("type"));
                items.setStartDate(resultSet.getString("startDate"));
                items.setPrincipal(resultSet.getInt("principal"));
                items.setRate(resultSet.getDouble("rate"));
                items.setDescription(resultSet.getString("description"));
                items.setStatus(resultSet.getString("status"));
                items.setCreatedAt(resultSet.getString("createdAt"));
                items.setUpdatedAt(resultSet.getString("updatedAt"));
                items.setCloserName(resultSet.getString("closerName"));
                items.setTotalAmount(resultSet.getInt("totalAmount"));
                items.setClosingAmount(resultSet.getInt("closingAmount"));

                itemList.add(items);
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
        return itemList;
    }

    public ArrayList<Installment> getInstallmentData() throws BusinessException, SQLException {
        connection = DatabaseConnection.getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Installment installment = null;
        ArrayList<Installment> installmentList = null;

        try {
            String sql = "SELECT * FROM customers ORDER BY fullName ASC";
            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();
            installmentList = new ArrayList<>();

            while (resultSet.next()) {
                installment = new Installment();
                installment.setInstallmentID(resultSet.getInt("installmentID"));
                installment.setDepositor(resultSet.getString("depositor"));
                installment.setDepositAmount(resultSet.getInt("depositAmount"));
                installment.setDate(resultSet.getString("date"));

                installmentList.add(installment);
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
        return installmentList;
    }
}
