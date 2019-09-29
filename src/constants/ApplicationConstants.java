package constants;

public class ApplicationConstants {
    public static String EMPTY_STRING = "";
    public static String DRIVER_NAME = "com.mysql.jdbc.Driver";
    public static String DB_NAME = "lagani";
    public static  final String DB_USERNAME = "root";
    public static final String  DB_PASSWORD = "stranzer12@";
    public static final String DB_URL = "jdbc:mysql://localhost:3306/lagani?autoReconnect=true&useSSL=false";
    public static final  int DB_PORT = 3306;

    public static final String USER_AUTHENTICATION_SQL = "SELECT * FROM userdata WHERE username = ? AND password = ?";
    public static final String COUNT_DASHBOARD_ITEM_SQL = "SELECT COUNT(*) FROM customers INNER JOIN items ON customers.customerID = items.customers_customerID LEFT OUTER JOIN (SELECT items_itemID, sum(depositAmount) AS installmentAmount FROM installment GROUP BY items_itemID) AS installments ON items.itemID = installments.items_itemID;";
    public static final String GET_DASHBOARD_ITEM_SQL = "SELECT * FROM customers INNER JOIN items ON customers.customerID = items.customers_customerID LEFT OUTER JOIN (SELECT items_itemID, sum(depositAmount) AS installmentAmount FROM installment GROUP BY items_itemID) AS installments ON items.itemID = installments.items_itemID ORDER BY customerID LIMIT ?, 50;";
    public static final String GET_CUSTOMERS_SQL = "SELECT * FROM customers WHERE customerID like ? or fullName like ? or address like ? or fatherName like ? or spouseName like ? ORDER BY fullName ASC;";
    public static final String GET_ITEMS_SQL = "SELECT * FROM items WHERE customers_customerID = ? ORDER BY startDate DESC";
    public static final String GET_INSTALLMENT_DATA_SQL = "select * from installment where items_itemId=? ORDER BY date ASC";
    public static final String ADD_NEW_CUSTOMER_SQL = "INSERT INTO customers (fullName, spouseName, fatherName, address, ward, createdAt, isActive, remarks, updatedAt, contactNo) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    public static final String ADD_NEW_ITEM_SQL = "INSERT INTO items (type, startDate, principal, rate, description, image, status, createdAt, updatedAt, closerName, totalAmount, closingAmount, isActive, deadline, closingDate, customers_customerID) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
    public static final String ADD_NEW_INSTALLMENT_SQL = "INSERT INTO installment (depositor, depositAmount, date, items_itemID) VALUES(?, ?, ?, ?);";
    public static final String GET_USER_DATA_SQL = "SELECT * FROM userdata WHERE userID = ?";
    public static final String UPDATE_USER_PROFILE_SQL = "UPDATE userdata SET Name = ?, firmName = ?, contact = ?, email = ?, Address = ? WHERE userID = ?";
    public static final String GET_CUSTOMER_BY_ID_SQL = "SELECT * FROM customers WHERE customerID = ?;";
    public static final String GET_ITEM_BY_ID_SQL = "SELECT * FROM items WHERE itemID = ?;";
    public static final String GET_INSTALLMENT_BY_ID_SQL = "SELECT * FROM installment WHERE installmentID = ?;";
    public static final String UPDATE_CUSTOMER_DATA_SQL = "UPDATE customers SET fullName = ?, address = ?, ward = ?, fatherName = ?, spouseName = ?, contactNo = ?, remarks = ? WHERE customerID = ?";
    public static final String UPDATE_ITEM_DATA_SQL = "UPDATE items SET principal = ?, type = ? startDate = ?, rate = ?, deadline = ?, description = ? WHERE userID = ?";
    public static final String UPDATE_INSTALLMENT_DATA_SQL = "UPDATE installment SET depositAmount = ?, depositor = ?, date = ? WHERE installmentID = ?";

    public static final String MAC_MYSQL_DUMP = "/Applications/AMPPS/mysql/bin/mysqldump";
    public static final String WINDOWS_MYSQL_DUMP = "C:/Program Files/MySQL/MySQL Server 8.0/bin/mysqldump.exe";
}
