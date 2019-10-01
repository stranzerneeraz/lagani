package implementation;

import constants.ApplicationConstants;
import database.DatabaseConnection;
import exception.BusinessException;
import modal.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

import static java.time.temporal.ChronoUnit.DAYS;

public class BusinessImplementation {
    private Connection connection = null;

    /**
     * Checks whether user is authorize or not
     * @param username
     * @param password
     * @return
     * @throws BusinessException
     */
    public int authenticateUser(String username, String password) throws BusinessException {
        connection = DatabaseConnection.getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        int id = 0;
        try {
            String sql = ApplicationConstants.USER_AUTHENTICATION_SQL;
            statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, password);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                id = resultSet.getInt("userID");
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

    /**
     * count total items that needs to be displayed in dashboard
     * @return
     * @throws BusinessException
     */
    public int countDashboardItem() throws BusinessException {
        connection = DatabaseConnection.getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        int count = 0;
        try {
            String sql = ApplicationConstants.COUNT_DASHBOARD_ITEM_SQL;
            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();

            resultSet.next();
            count = resultSet.getInt(1);
        } catch (
                SQLException e) {
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
        return count;
    }

    /**
     * gets item to be displayed in the dashboard table view
     * @param offset
     * @return
     * @throws BusinessException
     */
    public ArrayList<DashboardItem> getDashboardItem(int offset) throws BusinessException {
        connection = DatabaseConnection.getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        DashboardItem dashboardItem;
        ArrayList<DashboardItem> itemArrayList = null;
        try {
            String sql = ApplicationConstants.GET_DASHBOARD_ITEM_SQL;
            statement = connection.prepareStatement(sql);
            statement.setInt(1, offset);
            resultSet = statement.executeQuery();
            itemArrayList = new ArrayList<>();

            while (resultSet.next()) {
                dashboardItem = new DashboardItem();
                dashboardItem.setCustomerID(resultSet.getInt("customerID"));
                dashboardItem.setAmount(resultSet.getInt("principal"));
                dashboardItem.setName(resultSet.getString("fullName"));
                dashboardItem.setAddress(resultSet.getString("address"));
                if (null != resultSet.getDate("startDate")) {
                    dashboardItem.setDuration(calculateDurationInMonths(resultSet.getDate("startDate")));
                }
                if (0 != resultSet.getDouble("rate") && resultSet.getInt("principal") != 0) {
                    dashboardItem.setTotalInterest(calculateInterestAmount(dashboardItem.getDuration(), resultSet.getDouble("rate"), resultSet.getInt("principal")));
                }
                dashboardItem.setTotalInstallment(resultSet.getInt("installmentAmount"));

                itemArrayList.add(dashboardItem);
            }
        } catch (
                SQLException e) {
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
        return itemArrayList;
    }

    /**
     * gets list of customers
     * @param searchString
     * @return
     * @throws BusinessException
     */
    public ArrayList<Customers> getCustomers(String searchString) throws BusinessException {
        connection = DatabaseConnection.getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Customers customers;
        ArrayList<Customers> customerList;
        try {
            String sql = ApplicationConstants.GET_CUSTOMERS_SQL;
            statement = connection.prepareStatement(sql);
            statement.setString(1, "%" + searchString + "%");
            statement.setString(2, "%" + searchString + "%");
            statement.setString(3, "%" + searchString + "%");
            statement.setString(4, "%" + searchString + "%");
            statement.setString(5, "%" + searchString + "%");
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
        return customerList;
    }

    /**
     * gets list of items of specific customer
     * @param id
     * @return
     * @throws BusinessException
     */
    public ArrayList<Items> getItems(int id) throws BusinessException {
        connection = DatabaseConnection.getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet1 = null;
        ResultSet resultSet2 = null;
        Items items;
        ArrayList<Items> itemList;
        try {
            String sqlActive = ApplicationConstants.GET_ACTIVE_ITEMS_SQL;
            statement = connection.prepareStatement(sqlActive);
            statement.setInt(1, id);
            resultSet1 = statement.executeQuery();

            String sqlNotActive = ApplicationConstants.GET_NOT_ACTIVE_ITEMS_SQL;
            statement = connection.prepareStatement(sqlNotActive);
            statement.setInt(1, id);
            resultSet2 = statement.executeQuery();

            itemList = new ArrayList<>();

            while (resultSet1.next()) {
                items = new Items();
                items.setItemID(resultSet1.getString("itemID"));
                items.setType(resultSet1.getString("type"));
                items.setStartDate(resultSet1.getDate("startDate"));
                items.setPrincipal(resultSet1.getString("principal"));
                items.setRate(resultSet1.getString("rate"));
                items.setDescription(resultSet1.getString("description"));
                items.setStatus(resultSet1.getString("status"));
                items.setCreatedAt(resultSet1.getString("createdAt"));
                items.setUpdatedAt(resultSet1.getString("updatedAt"));
                items.setCloserName(resultSet1.getString("closerName"));
                items.setTotalAmount(resultSet1.getString("totalAmount"));
                items.setClosingAmount(resultSet1.getString("closingAmount"));
                items.setIsActive(resultSet1.getInt("isActive"));
                items.setClosingDate(resultSet1.getString("closingDate"));
                items.setDeadline(resultSet1.getDate("deadline"));

                itemList.add(items);
            }
            while (resultSet2.next()) {
                items = new Items();
                items.setItemID(resultSet2.getString("itemID"));
                items.setType(resultSet2.getString("type"));
                items.setStartDate(resultSet2.getDate("startDate"));
                items.setPrincipal(resultSet2.getString("principal"));
                items.setRate(resultSet2.getString("rate"));
                items.setDescription(resultSet2.getString("description"));
                items.setStatus(resultSet2.getString("status"));
                items.setCreatedAt(resultSet2.getString("createdAt"));
                items.setUpdatedAt(resultSet2.getString("updatedAt"));
                items.setCloserName(resultSet2.getString("closerName"));
                items.setTotalAmount(resultSet2.getString("totalAmount"));
                items.setClosingAmount(resultSet2.getString("closingAmount"));
                items.setIsActive(resultSet2.getInt("isActive"));
                items.setClosingDate(resultSet2.getString("closingDate"));
                items.setDeadline(resultSet2.getDate("deadline"));

                itemList.add(items);
            }
        } catch (SQLException se) {
            throw new BusinessException(se);
        } finally {
            if (null != resultSet1) {
                try {
                    resultSet1.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (null != resultSet2) {
                try {
                    resultSet2.close();
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
        return itemList;
    }

    /**
     * gets list of installment for specific item
     * @param itemId
     * @return
     * @throws BusinessException
     */
    public ArrayList<Installment> getInstallmentData(int itemId) throws BusinessException {
        connection = DatabaseConnection.getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Installment installment;
        ArrayList<Installment> installmentList;
        try {
            String sql = ApplicationConstants.GET_INSTALLMENT_DATA_SQL;
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
        return installmentList;
    }

    /**
     * adds new customer to the list
     * @param customers
     * @throws BusinessException
     */
    public void addNewCustomer(Customers customers) throws BusinessException {
        connection = DatabaseConnection.getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            String sql = ApplicationConstants.ADD_NEW_CUSTOMER_SQL;
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

    /**
     * adds new item for the customer
     * @param items
     * @param customerID
     * @throws BusinessException
     */
    public void addNewCustomerItem(Items items, int customerID) throws BusinessException {
        connection = DatabaseConnection.getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            String sql = ApplicationConstants.ADD_NEW_ITEM_SQL;
            statement = connection.prepareStatement(sql);
            statement.setString(1, items.getType());
            statement.setDate(2, items.getStartDate());
            statement.setInt(3, Integer.parseInt(items.getPrincipal()));
            statement.setDouble(4, Double.parseDouble(items.getRate()));
            statement.setString(5, items.getDescription());
            statement.setString(6, null);
            statement.setString(7, "not paid");
            statement.setDate(8, Date.valueOf(LocalDate.now()));
            statement.setDate(9, Date.valueOf(LocalDate.now()));
            statement.setString(10, "Bill Gates");
            statement.setInt(11, 10000);
            statement.setInt(12, 5000);
            statement.setInt(13, 1);
            statement.setDate(14, items.getDeadline());
            statement.setDate(15, Date.valueOf(LocalDate.now().plusYears(1)));
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

    /**
     * adds installment to the item
     * @param installment
     * @param itemID
     * @throws BusinessException
     */
    public void addInstallment(Installment installment, int itemID) throws BusinessException {
        connection = DatabaseConnection.getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            String sql = ApplicationConstants.ADD_NEW_INSTALLMENT_SQL;
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

    /**
     * close item after all payment is complete
     * @param closerName
     * @param closingAmount
     * @param id
     * @throws BusinessException
     */
    public void closeCustomerItem(String closerName, int closingAmount, int id) throws BusinessException {
        connection = DatabaseConnection.getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            String sql = ApplicationConstants.CLOSE_CUSTOMER_ITEM;
            statement = connection.prepareStatement(sql);
            statement.setString(1, closerName);
            statement.setInt(2, closingAmount);
            statement.setInt(3, 0);
            statement.setDate(4, Date.valueOf(LocalDate.now()));
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

    /**
     * gets user data to display in profile
     * @param id
     * @return
     * @throws BusinessException
     */
    public User getUser(int id) throws BusinessException {
        connection = DatabaseConnection.getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        User user = null;
        try {
            String sql = ApplicationConstants.GET_USER_DATA_SQL;
            statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();
            user = new User();

            resultSet.next();
            user.setName(resultSet.getString("Name"));
            user.setFirmname(resultSet.getString("firmName"));
            user.setAddress(resultSet.getString("Address"));
            user.setEmail(resultSet.getString("email"));
            user.setContact(resultSet.getLong("contact"));
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
        return user;
    }

    /**
     * updates user data from profile tab
     * @param user
     * @param id
     * @throws BusinessException
     */
    public void updateUserProfile(User user, int id) throws BusinessException {
        connection = DatabaseConnection.getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            String sql = ApplicationConstants.UPDATE_USER_PROFILE_SQL;
            statement = connection.prepareStatement(sql);
            statement.setString(1, user.getName());
            statement.setString(2, user.getFirmname());
            statement.setLong(3, user.getContact());
            statement.setString(4, user.getEmail());
            statement.setString(5, user.getAddress());
            statement.setInt(6, id);

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

    /**
     * gets customer detail that needs to be updated
     * @param customerID
     * @return
     * @throws BusinessException
     */
    public Customers getCustomerByID(int customerID) throws BusinessException {
        connection = DatabaseConnection.getConnection();
        PreparedStatement statement;
        ResultSet resultSet;
        Customers customers = null;

        try {
            String sql = ApplicationConstants.GET_CUSTOMER_BY_ID_SQL;
            statement = connection.prepareStatement(sql);
            statement.setInt(1, customerID);
            resultSet = statement.executeQuery();
            customers = new Customers();

            resultSet.next();
            customers.setCustomerID(resultSet.getInt("customerID"));
            customers.setFullName(resultSet.getString("fullName"));
            customers.setAddress(resultSet.getString("address"));
            customers.setWard(resultSet.getInt("ward"));
            customers.setFatherName(resultSet.getString("fatherName"));
            customers.setSpouseName(resultSet.getString("spouseName"));
            customers.setContactNo(resultSet.getLong("contactNo"));
            customers.setRemarks(resultSet.getString("remarks"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }

    /**
     * updates customer data
     * @param customer
     * @throws BusinessException
     */
    public void updateCustomerData(Customers customer) throws BusinessException {
        connection = DatabaseConnection.getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            String sql = ApplicationConstants.UPDATE_CUSTOMER_DATA_SQL;
            statement = connection.prepareStatement(sql);
            statement.setString(1, customer.getFullName());
            statement.setString(2, customer.getAddress());
            statement.setInt(3, customer.getWard());
            statement.setString(4, customer.getFatherName());
            statement.setString(5, customer.getSpouseName());
            statement.setLong(6, customer.getContactNo());
            statement.setString(7, customer.getRemarks());
            statement.setInt(8, customer.getCustomerID());

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
        System.out.println("Customer updated");
    }

    /**
     * gets item detail that needs to be updated
     * @param itemID
     * @return
     * @throws BusinessException
     */
    public Items getItemByID(int itemID) throws BusinessException {
        connection = DatabaseConnection.getConnection();
        PreparedStatement statement;
        ResultSet resultSet;
        Items items = null;
        try {
            String sql = ApplicationConstants.GET_ITEM_BY_ID_SQL;
            statement = connection.prepareStatement(sql);
            statement.setInt(1, itemID);
            resultSet = statement.executeQuery();
            items = new Items();

            resultSet.next();
            items.setItemID(resultSet.getString("itemID"));
            items.setPrincipal(resultSet.getString("principal"));
            items.setType(resultSet.getString("type"));
            items.setStartDate(resultSet.getDate("createdAt"));
            items.setRate(resultSet.getString("rate"));
            items.setDeadline(resultSet.getDate("deadline"));
            items.setDescription(resultSet.getString("description"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    /**
     * updates item data
     * @param item
     * @throws BusinessException
     */
    public void updateItemData(Items item) throws BusinessException {
        connection = DatabaseConnection.getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            String sql = ApplicationConstants.UPDATE_ITEM_DATA_SQL;
            statement = connection.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(item.getPrincipal()));
            statement.setString(2, item.getType());
            statement.setDate(3, item.getStartDate());
            statement.setDouble(4, Double.parseDouble(item.getRate()));
            statement.setDate(5, item.getDeadline());
            statement.setString(6, item.getDescription());
            statement.setInt(7, Integer.parseInt(item.getItemID()));

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
        System.out.println("Item updated");
    }

    /**
     * gets installment detail that needs to be updated
     * @param installmentID
     * @return
     * @throws BusinessException
     */
    public Installment getInstallmentByID(int installmentID) throws BusinessException {
        connection = DatabaseConnection.getConnection();
        PreparedStatement statement;
        ResultSet resultSet;
        Installment installment = null;
        try {
            String sql = ApplicationConstants.GET_INSTALLMENT_BY_ID_SQL;
            statement = connection.prepareStatement(sql);
            statement.setInt(1, installmentID);
            resultSet = statement.executeQuery();
            installment = new Installment();

            resultSet.next();
            installment.setInstallmentID(resultSet.getInt("installmentID"));
            installment.setDepositAmount(resultSet.getInt("depositAmount"));
            installment.setDepositor(resultSet.getString("depositor"));
            installment.setDate(resultSet.getDate("date"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return installment;
    }

    /**
     * updates installment data
     * @param installment
     * @throws BusinessException
     */
    public void updateInstallmentData(Installment installment) throws BusinessException {
        connection = DatabaseConnection.getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            String sql = ApplicationConstants.UPDATE_INSTALLMENT_DATA_SQL;
            statement = connection.prepareStatement(sql);
            statement.setInt(1, installment.getDepositAmount());
            statement.setString(2, installment.getDepositor());
            statement.setDate(3, installment.getDate());
            statement.setInt(4, installment.getInstallmentID());

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

    /**
     * gets MySQL dump path for backup process
     * @return
     * @throws BusinessException
     */
    public String getMySQLDumpPath() throws BusinessException {
        connection = DatabaseConnection.getConnection();
        PreparedStatement statement;
        ResultSet resultSet;
        String mySQLDumpPath = null;
        try {
            String sql = ApplicationConstants.GET_MYSQL_DUMP_PATH_SQL;
            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();
            resultSet.next();
            mySQLDumpPath= resultSet.getString("mySQLDump");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mySQLDumpPath;
    }

    /**
     * updates MySQL dump path if necessary
     * @param selectedFile
     * @throws BusinessException
     */
    public void updateMySQLDumpPath(String selectedFile) throws BusinessException {
        connection = DatabaseConnection.getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            String sql = ApplicationConstants.UPDATE_MYSQL_DUMP_PATH_SQL;
            statement = connection.prepareStatement(sql);
            statement.setString(1, selectedFile);

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

    /**
     * calculates duration of loan
     * @param fromDate
     * @return
     */
    private double calculateDurationInMonths(Date fromDate){
        double months=0;
        long days = DAYS.between(LocalDate.parse(fromDate.toString()),
                LocalDate.parse(new Date(System.currentTimeMillis()).toString()));
        if(days<15){
            months = 0.5;
        }else{
            months = (int) days/30;
            int monthsRem = (int) days%30;
            months = monthsRem<=15?months+0.5:months+1;
        }
        return months;
    }

    /**
     * calculates total interest
     * @param noOfMonths
     * @param rate
     * @param amount
     * @return
     */
    private int calculateInterestAmount(double noOfMonths, double rate, int amount){
        double months = noOfMonths;
        int totalAmount = amount;
        while(months>12){
            totalAmount+=(totalAmount * rate * 12)/100;
            months-=12;
        }
        if(months>0){
            totalAmount+=(totalAmount*rate*months)/100;
        }
        return totalAmount-amount;
    }
}
