package implementation;

import database.DatabaseConnection;
import exception.BusinessException;
import modal.Customers;
import modal.Installment;
import modal.Items;
import modal.User;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class BusinessImplementation {
    Connection connection = null;

    public int authenticateUser(String username, String password) throws BusinessException {
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
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (null != statement) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (null != connection) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println(id);
        return id;
    }

    public void updateUserProfile(String nameProfile, String firmnameProfile, String contactProfile, String addressProfile, int id) throws BusinessException {
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
            statement.setInt(5, id);

            statement.executeUpdate();
        } catch (SQLException se) {
            throw new BusinessException(se);
        } finally {
            if (null != resultSet) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (null != statement) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (null != connection) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public User getUser(int id) throws BusinessException {
        connection = DatabaseConnection.getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        User user = null;
        try {
            String sql = "SELECT * FROM userdata WHERE userID = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
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
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (null != statement) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (null != connection) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println(user);
        return user;
    }

    public ArrayList<Customers> getCustomers(String searchString) throws BusinessException {
        connection = DatabaseConnection.getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Customers customers = null;
        ArrayList<Customers> customerList = null;
        System.out.println("Entry of search " +searchString);
        try {
            String sql = "SELECT * FROM customers WHERE fullName like ? or address like ? or fatherName like ? or spouseName like ? " +
                    "ORDER BY fullName ASC;";
            statement = connection.prepareStatement(sql);
            statement.setString(1, "%"+searchString+"%");
            statement.setString(2, "%"+searchString+"%");
            statement.setString(3, "%"+searchString+"%");
            statement.setString(4, "%"+searchString+"%");
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
                customers.setContactNo(resultSet.getLong("contactNo"));

                customerList.add(customers);
            }
        } catch (SQLException se) {
            throw new BusinessException(se);
        } finally {
            if (null != resultSet) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (null != statement) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (null != connection) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println(customerList);
        return customerList;
    }

    public ArrayList<Items> getItems(int id) throws BusinessException {
        connection = DatabaseConnection.getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Items items = null;
        ArrayList<Items> itemList = null;
        try {
            String sql = "SELECT * FROM items WHERE customers_customerID = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();
            itemList = new ArrayList<>();

            while (resultSet.next()) {
                items = new Items();
                items.setItemID(resultSet.getInt("itemID"));
                items.setType(resultSet.getString("type"));
                items.setStartDate(resultSet.getDate("startDate"));
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
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (null != statement) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (null != connection) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println(itemList);
        return itemList;
    }

    public void addNewCustomer(Customers customers) throws BusinessException {
        connection = DatabaseConnection.getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            String sql = "INSERT INTO customers (fullName, spouseName, fatherName, address, ward, createdAt, isActive, remarks, " +
                    "updatedAt, contactNo) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            statement = connection.prepareStatement(sql);
            statement.setString(1, customers.getFullName());
            statement.setString(2, customers.getSpouseName());
            statement.setString(3, customers.getFatherName());
            statement.setString(4, customers.getAddress());
            statement.setInt(5, customers.getWard());
            statement.setDate(6, Date.valueOf(LocalDate.now()));
            statement.setInt(7, 1);
            statement.setString(8, customers.getRemarks());
            statement.setDate(9, Date.valueOf(LocalDate.now()));
            statement.setLong(10, customers.getContactNo());

            statement.executeUpdate();
        } catch (SQLException se) {
            throw new BusinessException(se);
        } finally {
            if (null != resultSet) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (null != statement) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (null != connection) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("Customer added successfully");
    }

    public void addNewCustomerItem(Items items, int customerID) throws BusinessException {
        connection = DatabaseConnection.getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            String sql = "INSERT INTO items (type, startDate, principal, rate, description, image, status, createdAt, updatedAt, closerName, totalAmount, " +
                    " closingAmount, isActive, deadline, closingDate, customers_customerID) VALUES(?, ?, ?, ?,       ?, ?, ?, ?,      ?, ?, ?, ?,      ?, ?, ?, ?);";
            statement = connection.prepareStatement(sql);
            statement.setString(1, items.getType());
            statement.setDate(2, items.getStartDate());
            statement.setInt(3, items.getPrincipal());
            statement.setDouble(4, items.getRate());
            statement.setString(5, items.getDescription());
            statement.setString(6, null);
            statement.setString(7, "not paid");
            statement.setDate(8, Date.valueOf("2019-1-1"));
            statement.setDate(9, Date.valueOf("2019-12-12"));
            statement.setString(10, "Bill Gates");
            statement.setInt(11, 10000);
            statement.setInt(12, 5000);
            statement.setInt(13, 1);
            statement.setDate(14, Date.valueOf(items.getDeadline()));
            statement.setDate(15, Date.valueOf("2019-12-30"));
            statement.setInt(16, customerID);

            statement.executeUpdate();
        } catch (SQLException se) {
            throw new BusinessException(se);
        } finally {
            if (null != resultSet) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (null != statement) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (null != connection) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("Item added successfully");
    }

    public ArrayList<Installment> getInstallmentData(int itemId) throws BusinessException {
        connection = DatabaseConnection.getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Installment installment = null;
        ArrayList<Installment> installmentList = null;
        try {
            String sql = "select * from installment where items_itemId=?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, itemId);
            resultSet = statement.executeQuery();
            installmentList = new ArrayList<>();

            while (resultSet.next()) {
                installment = new Installment();
                installment.setInstallmentID(resultSet.getInt("installmentID"));
                installment.setDepositor(resultSet.getString("depositor"));
                installment.setDepositAmount(resultSet.getInt("depositAmount"));
                installment.setDate(resultSet.getDate("date"));
                installment.setItemIdFk(resultSet.getInt("items_itemID"));

                installmentList.add(installment);
            }
        } catch (SQLException se) {
            throw new BusinessException(se);
        } finally {
            if (null != resultSet) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    throw new BusinessException(e);
                }
            }
            if (null != statement) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    throw new BusinessException(e);
                }
            }
            if (null != connection) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new BusinessException(e);
                }
            }
        }
        System.out.println(installmentList);
        return installmentList;
    }

    public void addInstallment(Installment installment, int itemID) throws BusinessException {
        connection = DatabaseConnection.getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            String sql = "INSERT INTO installment (depositor, depositAmount, date, items_itemID) VALUES(?, ?, ?, ?);";
            statement = connection.prepareStatement(sql);
            statement.setString(1, installment.getDepositor());
            statement.setInt(2, installment.getDepositAmount());
            statement.setDate(3, installment.getDate());
            statement.setInt(4, itemID);

            statement.executeUpdate();
        } catch (SQLException se) {
            throw new BusinessException(se);
        } finally {
            if (null != resultSet) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (null != statement) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (null != connection) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("Installment added successfully");
    }
}
