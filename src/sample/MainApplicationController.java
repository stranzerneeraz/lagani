package sample;

import com.sun.javafx.scene.control.skin.VirtualFlow;
import constants.ApplicationConstants;
import exception.BusinessException;
import implementation.BusinessImplementation;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Callback;
import modal.*;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Set;

public class MainApplicationController {
    @FXML
    private TabPane tabPane;
    @FXML
    private Tab dashboardTab;
    @FXML
    private Tab viewCustomersTab;
    @FXML
    private Tab profileTab;
    @FXML
    private Tab adminTab;
    @FXML
    private TableView<DashboardItem> dashboardTableView;
    @FXML
    private TableColumn<DashboardItem, Integer> serialNoDashboard;
    @FXML
    private Pagination dashboardTableViewPagination;
    @FXML
    private Button btnLogout;
    @FXML
    private TextField searchCustomer;
    @FXML
    private ListView<Customers> customerListView;
    @FXML
    private Label nameViewLabel;
    @FXML
    private Label addressViewLabel;
    @FXML
    private Label fatherViewLabel;
    @FXML
    private Label contactViewLabel;
    @FXML
    private Label spouseViewLabel;
    @FXML
    private TableView<Items> itemsTableView;
    @FXML
    private TableColumn<Items, Integer> itemNo;
    @FXML
    private TableColumn<Items, String> viewInstallment;
    @FXML
    private TableColumn<Items, String> addInstallment;
    @FXML
    private TableColumn<Items, String> calculate;
    @FXML
    private ContextMenu listContextMenu;
    @FXML
    private Button btnAddNewItem;
    @FXML
    private BorderPane mainBorderPane;
    @FXML
    private TextField nameProfile;
    @FXML
    private TextField firmnameProfile;
    @FXML
    private TextField contactProfile;
    @FXML
    private TextField emailProfile;
    @FXML
    private TextField addressProfile;
    @FXML
    private TextField getCustomerID;
    @FXML
    private Button btnViewCustomer;
    @FXML
    private TextField getItemID;
    @FXML
    private Button btnViewItem;
    @FXML
    private TextField getInstallmentID;
    @FXML
    private Button btnViewInstallment;
    @FXML
    private Button btnViewBackup;
    @FXML
    private VBox customerAdminPanel;
    @FXML
    private TextField adminFullName;
    @FXML
    private TextField adminAddress;
    @FXML
    private TextField adminWard;
    @FXML
    private TextField adminFatherName;
    @FXML
    private TextField adminSpouseName;
    @FXML
    private TextField adminContactNumber;
    @FXML
    private TextField adminRemarks;
    @FXML
    private Button btnUpdateCustomer;
    @FXML
    private VBox itemAdminPanel;
    @FXML
    private TextField adminItemAmount;
    @FXML
    private ComboBox adminTypeChooser;
    @FXML
    private DatePicker adminStartDate;
    @FXML
    private TextField adminRate;
    @FXML
    private DatePicker adminDeadline;
    @FXML
    private TextArea adminDescription;
    @FXML
    private Button btnUpdateItem;
    @FXML
    private VBox installmentAdminPanel;
    @FXML
    private TextField adminInstallmentAmount;
    @FXML
    private TextField adminDepositor;
    @FXML
    private DatePicker adminDepositDate;
    @FXML
    private Button btnUpdateInstallment;
    @FXML
    private VBox backupAdminPanel;
    @FXML
    private Button btnBrowse;
    @FXML
    private Text adminPathViewer;
    @FXML
    private Button btnBackup;

    private ObservableList<DashboardItem> dashboardItemObservableList;
    private ObservableList<Customers> customersObservableList = FXCollections.observableArrayList();
    private ObservableList<Items> itemsObservableList;
    private int id = LoginController.id();
    private String searchString = "";
    private Stage window;
    private Window dialogWindow;
    private int rowsPerPage = 5;
    private int test;

    public MainApplicationController() {
        itemsObservableList = FXCollections.observableArrayList();
    }

    private Alert alertWarning = new Alert(Alert.AlertType.WARNING);
    private Alert alertSuccess = new Alert(AlertType.INFORMATION);
    private BusinessImplementation businessImplementation = new BusinessImplementation();
    private Customers selectionCustomer = new Customers();

    public void initialize() throws BusinessException {
        dashboardTab();
//        dashboardTab.selectedProperty().addListener(tabListener("dashboardTab"));
//        viewCustomersTab.selectedProperty().addListener(tabListener("viewCustomersTab"));
//        profileTab.selectedProperty().addListener(tabListener("profileTab"));
//        adminTab.selectedProperty().addListener(tabListener("adminTab"));

        tabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
            if (newTab == dashboardTab) {
                try {
                    dashboardTab();
                } catch (BusinessException e) {
                    e.printStackTrace();
                }
            } else if (newTab == viewCustomersTab) {
                try {
                    viewCustomers();
                } catch (BusinessException e) {
                    e.printStackTrace();
                }
            } else if (newTab == profileTab) {
                try {
                    profileTab();
                } catch (BusinessException e) {
                    e.printStackTrace();
                }
            } else if (newTab == adminTab) {
                adminTab();
            }
        });
    }

    private ChangeListener<Boolean> tabListener(String tabName) {
        return (obs, wasSelected, isNowSelected) -> {
            if (isNowSelected) {
                if (tabName.equals("dashboardTab")) {
                    try {
                        dashboardTab();
                    } catch (BusinessException e) {
                        e.printStackTrace();
                    }
                } else if (tabName.equals("viewCustomersTab")) {
                    try {
                        viewCustomers();
                    } catch (BusinessException e) {
                        e.printStackTrace();
                    }
                } else if (tabName.equals("profileTab")) {
                    try {
                        profileTab();
                    } catch (BusinessException e) {
                        e.printStackTrace();
                    }
                } else if (tabName.equals("adminTab")) {
                    adminTab();
                }
            }
        };
    }

    @FXML
    public void dashboardTab() throws BusinessException {
        dashboardTableViewPagination.setPageFactory(this::createPage);
        ArrayList<DashboardItem> dashboardItem = businessImplementation.getDashboardItem();
        dashboardItemObservableList = FXCollections.observableArrayList(dashboardItem);
        int pageCount = (dashboardItemObservableList.size() / rowsPerPage) + 1;
        dashboardTableViewPagination.setPageCount(pageCount);
        serialNoDashboard.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(
                dashboardTableView.getItems().indexOf(cellData.getValue()) + 1));
        dashboardTableView.setItems(dashboardItemObservableList);
    }

    private Node createPage(int pageIndex) {
        int fromIndex = pageIndex * rowsPerPage;
        int toIndex = Math.min(fromIndex + rowsPerPage, dashboardItemObservableList.size());
        dashboardTableView.setItems(FXCollections.observableArrayList(dashboardItemObservableList.subList(fromIndex, toIndex)));
        return new BorderPane(dashboardTableView);
    }

    /**
     * Update Profile of the User
     */
    public void updateProfile() throws BusinessException {
        alertWarning.setTitle("Warning Dialog!");
        User user = new User();
        if (nameProfile.getText().length() <= 0) {
            nameProfile.setStyle("-fx-text-fill: red;");
            alertWarning.setContentText("Enter name");
            alertWarning.showAndWait();
        } else if (firmnameProfile.getText().length() <= 0) {
            firmnameProfile.setStyle("-fx-text-fill: red;");
            alertWarning.setContentText("Enter firm name");
            alertWarning.showAndWait();
        } else if (!contactProfile.getText().matches("^[0-9]{10}$")) {
            contactProfile.setStyle("-fx-text-fill: red;");
            alertWarning.setContentText("Enter contact number");
            alertWarning.showAndWait();
        } else if (!emailProfile.getText().matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")) {
            emailProfile.setStyle("-fx-text-fill: red;");
            alertWarning.setContentText("Enter email address");
            alertWarning.showAndWait();
        } else if (addressProfile.getText().length() <= 0) {
            addressProfile.setStyle("-fx-text-fill: red;");
            alertWarning.setContentText("Enter address");
            alertWarning.showAndWait();
        } else {
            nameProfile.setStyle("-fx-text-fill: black;");
            firmnameProfile.setStyle("-fx-text-fill: black;");
            contactProfile.setStyle("-fx-text-fill: black;");
            addressProfile.setStyle("-fx-text-fill: black;");

            user.setName(nameProfile.getText());
            user.setFirmname(firmnameProfile.getText());
            user.setContact(Long.parseLong(contactProfile.getText()));
            user.setEmail(emailProfile.getText());
            user.setAddress(addressProfile.getText());
            businessImplementation.updateUserProfile(user, id);
        }
    }

    /**
     * View Profile of the User
     */
    @FXML
    public void profileTab() throws BusinessException {
        User user = businessImplementation.getUser(id);
        nameProfile.setText(user.getName());
        firmnameProfile.setText(user.getFirmname());
        contactProfile.setText("" + user.getContact());
        addressProfile.setText(user.getAddress());
    }

    /**
     * View Customers who takes loan
     * Searching of the customer
     */
    @FXML
    public void viewCustomers() throws BusinessException {
        getCustomersForView(searchString);
        searchCustomer.textProperty().addListener(((observable, oldValue, newValue) -> {
            searchString = newValue;
            try {
                getCustomersForView(searchString);
            } catch (BusinessException e) {
                e.printStackTrace();
            }
        }));
        customerListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        btnAddNewItem.setDisable(true);

        customerListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (null != newValue) {
                btnAddNewItem.setDisable(false);
                Customers selectedCustomer = customerListView.getSelectionModel().getSelectedItem();
                nameViewLabel.setText(selectedCustomer.getFullName());
                fatherViewLabel.setText("F: " + selectedCustomer.getFatherName());
                addressViewLabel.setText(selectedCustomer.getAddress());
                spouseViewLabel.setText("S: " + selectedCustomer.getSpouseName());
                contactViewLabel.setText("" + selectedCustomer.getContactNo());
                selectionCustomer = selectedCustomer;
                try {
                    fetchCustomerItem(selectedCustomer.getCustomerID());
                } catch (BusinessException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void getCustomersForView(String searchString) throws BusinessException {
        listContextMenu = new ContextMenu();
        ArrayList<Customers> customerList = businessImplementation.getCustomers(searchString);
        customersObservableList.removeAll(customersObservableList);
        for (Customers customers : customerList) {
            customersObservableList.add(customers);
        }
        customerListView.getItems().clear();
        customerListView.getItems().addAll(customersObservableList);
        customerListView.setCellFactory(new Callback<ListView<Customers>, ListCell<Customers>>() {
            @Override
            public ListCell<Customers> call(ListView<Customers> param) {
                ListCell<Customers> cell = new ListCell<Customers>() {
                    private Tooltip tooltip = new Tooltip();

                    @Override
                    protected void updateItem(Customers customers, boolean empty) {
                        super.updateItem(customers, empty);
                        if (empty || customers == null) {
                            setText("");
                            setTooltip(null);
                        } else {
                            setText(customers.getFullName() + ", " + customers.getAddress());
                            tooltip.setText("" + customers.getCustomerID());
                            setTooltip(tooltip);
                        }
                    }
                };
                cell.emptyProperty().addListener(
                        (obs, wasEmpty, isNowEmpty) -> {
                            if (isNowEmpty) {
                                cell.setContextMenu(null);
                            } else {
                                cell.setContextMenu(listContextMenu);
                            }
                        }
                );
                return cell;
            }
        });
    }

    /**
     * Shows Item for the customer in Table View
     * Addition and Calculation of Installment
     */
    public void fetchCustomerItem(int id) throws BusinessException {
        ArrayList<Items> itemList = businessImplementation.getItems(id);
        itemsObservableList = FXCollections.observableArrayList(itemList);

        Tooltip tooltip = new Tooltip();
        itemNo.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(itemsTableView.getItems().indexOf(cellData.getValue()) + 1));

        viewInstallment.setCellValueFactory(new PropertyValueFactory<>("Dummy"));
        Callback<TableColumn<Items, String>, TableCell<Items, String>> cellFactoryView = new Callback<TableColumn<Items, String>, TableCell<Items, String>>() {
            @Override
            public TableCell call(final TableColumn<Items, String> param) {
                final TableCell<Items, String> cell = new TableCell<Items, String>() {
                    final Button btnViewInstallment = new Button("View");

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            tooltip.setText("" + itemsObservableList.size());
                            setTooltip(tooltip);
                            btnViewInstallment.setOnAction(event -> {
                                Items selectionItem = getTableView().getItems().get(getIndex());
                                Dialog<ButtonType> dialog = new Dialog<>();
                                dialog.initOwner(mainBorderPane.getScene().getWindow());
                                dialog.setTitle("Installments");
                                FXMLLoader fxmlLoader = new FXMLLoader();
                                fxmlLoader.setLocation(getClass().getResource("viewInstallmentDialog.fxml"));
                                dialogWindow = dialog.getDialogPane().getScene().getWindow();
                                try {
                                    dialog.getDialogPane().setContent(fxmlLoader.load());
                                    ViewInstallmentDialogController viewInstallmentDialogController = fxmlLoader.getController();
                                    viewInstallmentDialogController.initialize(selectionItem);
                                    dialogWindow.setOnCloseRequest(closeEvent -> dialog.close());
                                    dialog.showAndWait();
                                } catch (IOException | BusinessException e) {
                                    try {
                                        throw new BusinessException(e);
                                    } catch (BusinessException ex) {
                                        ex.printStackTrace();
                                    }
                                }
                            });
                            setGraphic(btnViewInstallment);
                            setText(null);
                        }
                    }
                };
                return cell;
            }
        };
        viewInstallment.setCellFactory(cellFactoryView);

        addInstallment.setCellValueFactory(new PropertyValueFactory<>("Dummy1"));
        Callback<TableColumn<Items, String>, TableCell<Items, String>> cellFactoryAdd = new Callback<TableColumn<Items, String>, TableCell<Items, String>>() {
            @Override
            public TableCell call(final TableColumn<Items, String> param) {
                final TableCell<Items, String> cell = new TableCell<Items, String>() {
                    final Button btnAddInstallment = new Button("Add");

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);

                        } else {
                            btnAddInstallment.setOnAction(event -> {
                                Items selectedItem = getTableView().getItems().get(getIndex());
                                Dialog<ButtonType> dialog = new Dialog<>();
                                dialog.initOwner(mainBorderPane.getScene().getWindow());
                                dialog.setTitle("Add Installment");
                                FXMLLoader fxmlLoader = new FXMLLoader();
                                fxmlLoader.setLocation(getClass().getResource("addInstallmentDialog.fxml"));
                                dialogWindow = dialog.getDialogPane().getScene().getWindow();
                                try {
                                    dialog.getDialogPane().setContent(fxmlLoader.load());
                                    AddInstallmentDialogController addInstallmentDialogController = fxmlLoader.getController();
                                    addInstallmentDialogController.initialize(selectedItem);
                                    dialogWindow.setOnCloseRequest(closeEvent -> dialog.close());
                                    dialog.showAndWait();
                                } catch (IOException e) {
                                    try {
                                        throw new BusinessException(e);
                                    } catch (BusinessException ex) {
                                        ex.printStackTrace();
                                    }
                                }
                            });
                            setGraphic(btnAddInstallment);
                            setText(null);
                        }
                    }
                };
                return cell;
            }
        };
        addInstallment.setCellFactory(cellFactoryAdd);

        calculate.setCellValueFactory(new PropertyValueFactory<>("Dummy2"));
        Callback<TableColumn<Items, String>, TableCell<Items, String>> cellFactoryCalculate = new Callback<TableColumn<Items, String>, TableCell<Items, String>>() {
            @Override
            public TableCell call(final TableColumn<Items, String> param) {
                final TableCell<Items, String> cell = new TableCell<Items, String>() {
                    final Button btnCalculate = new Button("Calculate");

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            btnCalculate.setOnAction(event -> {
                                Items items = getTableView().getItems().get(getIndex());
                                window = new Stage();
                                window.initOwner(mainBorderPane.getScene().getWindow());
                                window.setTitle("Calculation");
                                try {
                                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("calculation.fxml"));
                                    Parent root = fxmlLoader.load();
                                    CalculationController calculationController = fxmlLoader.getController();
                                    calculationController.initialize(items);
                                    Scene scene = new Scene(root, 800, 500);
                                    window.setScene(scene);
                                    window.showAndWait();
                                } catch (IOException | BusinessException e) {
                                    e.printStackTrace();
                                }
                            });
                            setGraphic(btnCalculate);
                            setText(null);
                        }
                    }
                };
                return cell;
            }
        };
        calculate.setCellFactory(cellFactoryCalculate);

        itemsTableView.setItems(itemsObservableList);
        ScrollBar table1HorizontalScrollBar = findScrollBar(itemsTableView, Orientation.HORIZONTAL);
        ScrollBar table1VerticalScrollBar = findScrollBar(itemsTableView, Orientation.VERTICAL);
        table1HorizontalScrollBar.setVisible(true);
        table1VerticalScrollBar.setVisible(false);
        VirtualFlow flow1 = (VirtualFlow) itemsTableView.lookup(".virtual-flow");
        flow1.requestLayout();
    }

    private ScrollBar findScrollBar(TableView<?> table, Orientation orientation) {
        Set<Node> set = table.lookupAll(".scroll-bar");
        for (Node node : set) {
            ScrollBar bar = (ScrollBar) node;
            if (bar.getOrientation() == orientation) {
                return bar;
            }
        }
        return null;
    }

    /**
     * Adds new Customer who takes loan
     */
    @FXML
    public void addNewCustomer() throws BusinessException {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        dialog.setTitle("Add New Customer");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("addNewCustomerDialog.fxml"));
        dialogWindow = dialog.getDialogPane().getScene().getWindow();
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
            AddNewCustomerDialogController addNewCustomerDialogController = fxmlLoader.getController();
            test = addNewCustomerDialogController.initialize();
            if (test != 0) {
                viewCustomers();
            }
            dialogWindow.setOnCloseRequest(event -> dialog.close());
            dialog.showAndWait();
        } catch (IOException e) {
            throw new BusinessException(e);
        }
    }

    /**
     * Adds new Item for the customer
     */
    @FXML
    public void addNewItem() throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("addNewItemDialog.fxml"));
        stage.initOwner(mainBorderPane.getScene().getWindow());
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.setTitle("Add New Item");
        AddNewItemDialogController addNewItemDialogController = fxmlLoader.getController();
        addNewItemDialogController.updateDialogBox(selectionCustomer);
        try {
            fetchCustomerItem(selectionCustomer.getCustomerID());
        } catch (BusinessException e) {
            e.printStackTrace();
        }
        stage.showAndWait();
        stage.setOnCloseRequest(event -> {
            stage.close();
        });
//        Dialog<ButtonType> dialog = new Dialog<>();
//        dialog.initOwner(mainBorderPane.getScene().getWindow());
//        dialog.setTitle("Add New Item");
//        FXMLLoader fxmlLoader = new FXMLLoader();
//        fxmlLoader.setLocation(getClass().getResource("addNewItemDialog.fxml"));
//        dialogWindow = dialog.getDialogPane().getScene().getWindow();
//        try {
//            dialog.getDialogPane().setContent(fxmlLoader.load());
//            AddNewItemDialogController addNewItemDialogController = fxmlLoader.getController();
//            addNewItemDialogController.updateDialogBox(selectionCustomer);
//            Scene scene = dialog.getDialogPane().getScene();
//
//            dialogWindow.setOnCloseRequest((WindowEvent event) -> {
//                dialog.close();
//            });
//
//            dialog.onCloseRequestProperty();
//            fetchCustomerItem(selectionCustomer.getCustomerID());
//
//            dialog.showAndWait();
//
//        } catch (IOException e) {
//            throw new BusinessException(e);
//        }
    }

    @FXML
    public void adminTab() {
        customerAdminPanel.setVisible(false);
        itemAdminPanel.setVisible(false);
        installmentAdminPanel.setVisible(false);
        backupAdminPanel.setVisible(false);
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning Dialog");
        btnViewCustomer.setOnAction(event -> {
            String customerID = getCustomerID.getText();
            if (!customerID.matches("[0-9]*[0-9]+")) {
                getCustomerID.setStyle("-fx-text-fill: red;");
                alertWarning.setContentText("Enter Customer ID");
                alertWarning.showAndWait();
            } else {
                getCustomerID.setStyle("-fx-text-fill: black;");
                try {
                    Customers customers = businessImplementation.getCustomerByID(Integer.parseInt(customerID));
                    if (customers == null) {
                        alert.setContentText("Enter valid customer ID");
                        alert.showAndWait();
                    } else {
                        adminViewCustomer(customers);
                    }
                } catch (BusinessException e) {
                    e.printStackTrace();
                }
            }
        });
        btnViewItem.setOnAction(event -> {
            String itemID = getItemID.getText();
            if (!itemID.matches("[0-9]*[0-9]+")) {
                getItemID.setStyle("-fx-text-fill: red;");
                alertWarning.setContentText("Enter Item ID");
                alertWarning.showAndWait();
            } else {
                getItemID.setStyle("-fx-text-fill: black;");
                try {
                    Items items = businessImplementation.getItemByID(Integer.parseInt(itemID));
                    if (null == items) {
                        alert.setContentText("Enter valid item ID");
                        alert.showAndWait();
                    } else {
                        adminViewItem(items);
                    }
                } catch (BusinessException e) {
                    e.printStackTrace();
                }
            }
        });
        btnViewInstallment.setOnAction(event -> {
            String installmentID = getInstallmentID.getText();
            if (!installmentID.matches("[0-9]*[0-9]+")) {
                getInstallmentID.setStyle("-fx-text-fill: red;");
                alertWarning.setContentText("Enter Installment ID");
                alertWarning.showAndWait();
            } else {
                getInstallmentID.setStyle("-fx-text-fill: black;");
                try {
                    Installment installment = businessImplementation.getInstallmentByID(Integer.parseInt(installmentID));
                    if (null == installment) {
                        alert.setContentText("Enter valid installment ID");
                        alert.showAndWait();
                    } else {
                        adminViewInstallment(installment);
                    }
                } catch (BusinessException e) {
                    e.printStackTrace();
                }
            }
        });
        btnViewBackup.setOnAction(event -> {
            customerAdminPanel.setVisible(false);
            itemAdminPanel.setVisible(false);
            installmentAdminPanel.setVisible(false);
            backupAdminPanel.setVisible(true);
            backup();
        });
    }

    public void adminViewCustomer(Customers customers) {
        customerAdminPanel.setVisible(true);
        itemAdminPanel.setVisible(false);
        installmentAdminPanel.setVisible(false);
        backupAdminPanel.setVisible(false);
        alertWarning.setTitle("Warning Dialog!");

        adminFullName.setText(customers.getFullName());
        adminAddress.setText(customers.getAddress());
        adminWard.setText("" + customers.getWard());
        adminFatherName.setText(customers.getFatherName());
        adminSpouseName.setText(customers.getSpouseName());
        adminContactNumber.setText("" + customers.getContactNo());
        adminRemarks.setText(customers.getRemarks());

        btnUpdateCustomer.setOnAction(event -> {
            if (adminFullName.getText().length() <= 0) {
                adminFullName.setStyle("-fx-text-fill: red;");
                alertWarning.setContentText("Enter full name");
                alertWarning.showAndWait();
            } else if (adminAddress.getText().length() <= 0) {
                adminAddress.setStyle("-fx-text-fill: red;");
                alertWarning.setContentText("Enter address");
                alertWarning.showAndWait();
            } else if (!adminWard.getText().matches("[0-9]*[0-9]+")) {
                adminWard.setStyle("-fx-text-fill: red;");
                alertWarning.setContentText("Enter ward number in integer");
                alertWarning.showAndWait();
            } else if (adminFatherName.getText().length() <= 0) {
                adminFatherName.setStyle("-fx-text-fill: red;");
                alertWarning.setContentText("Enter father's name");
                alertWarning.showAndWait();
            } else if (adminSpouseName.getText().length() <= 0) {
                adminSpouseName.setStyle("-fx-text-fill: red;");
                alertWarning.setContentText("Enter spouse's name");
                alertWarning.showAndWait();
            } else if (!adminContactNumber.getText().matches("^[0-9]{10}$")) {
                adminContactNumber.setStyle("-fx-text-fill: red;");
                alertWarning.setContentText("Enter contact number");
                alertWarning.showAndWait();
            } else if (adminRemarks.getText().length() <= 0) {
                adminRemarks.setStyle("-fx-text-fill: red;");
                alertWarning.setContentText("Enter remarks");
                alertWarning.showAndWait();
            } else {
                adminFullName.setStyle("-fx-text-fill: black;");
                adminAddress.setStyle("-fx-text-fill: black;");
                adminWard.setStyle("-fx-text-fill: black;");
                adminFatherName.setStyle("-fx-text-fill: black;");
                adminSpouseName.setStyle("-fx-text-fill: black;");
                adminContactNumber.setStyle("-fx-text-fill: black;");
                adminRemarks.setStyle("-fx-text-fill: black;");

                customers.setFullName(adminFullName.getText());
                customers.setAddress(adminAddress.getText());
                customers.setWard(Integer.parseInt(adminWard.getText()));
                customers.setFatherName(adminFatherName.getText());
                customers.setSpouseName(adminSpouseName.getText());
                customers.setContactNo(Long.parseLong(adminContactNumber.getText()));
                customers.setRemarks(adminRemarks.getText());
                try {
                    businessImplementation.updateCustomerData(customers);
                } catch (BusinessException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void adminViewItem(Items items) {
        customerAdminPanel.setVisible(false);
        itemAdminPanel.setVisible(true);
        installmentAdminPanel.setVisible(false);
        backupAdminPanel.setVisible(false);
        alertWarning.setTitle("Warning Dialog!");

        adminItemAmount.setText(items.getPrincipal());
        adminTypeChooser.setValue(items.getType());
        adminStartDate.setValue(items.getStartDate().toLocalDate());
        adminRate.setText(items.getRate());
        adminDeadline.setValue(items.getDeadline().toLocalDate());
        adminDescription.setText(items.getDescription());

        btnUpdateItem.setOnAction(event -> {
            if (!adminItemAmount.getText().matches("^[0-9]+(\\.[0-9]+)?$")) {
                adminItemAmount.setStyle("-fx-text-fill: red;");
                alertWarning.setContentText("Input fields not valid");
                alertWarning.showAndWait();
            } else if (!(adminStartDate.getValue().compareTo(LocalDate.now()) <= 0)) {
                adminStartDate.setStyle("-fx-text-fill: red;");
                alertWarning.setContentText("Date field should be upto today");
                alertWarning.showAndWait();
            } else if (!adminRate.getText().matches("^[0-9](\\.[0-9]+)?$")) {
                adminRate.setStyle("-fx-text-fill: red;");
                alertWarning.setContentText("Rate should be in decimal");
                alertWarning.showAndWait();
            } else if (!(adminDeadline.getValue().compareTo(LocalDate.now()) > 0)) {
                adminDeadline.setStyle("-fx-text-fill: red;");
                alertWarning.setContentText("Deadline should be in future");
                alertWarning.showAndWait();
            } else if (adminDescription.getText().length() <= 0) {
                adminDescription.setStyle("-fx-text-fill: red;");
                alertWarning.setContentText("Enter description");
                alertWarning.showAndWait();
            } else {
                adminItemAmount.setStyle("-fx-text-fill: black;");
                adminStartDate.setStyle("-fx-text-fill: black;");
                adminRate.setStyle("-fx-text-fill: black;");
                adminDeadline.setStyle("-fx-text-fill: black;");
                adminDescription.setStyle("-fx-text-fill: black;");

                items.setPrincipal(adminItemAmount.getText());
                items.setStartDate(Date.valueOf(adminStartDate.getValue()));
                items.setRate(adminRate.getText());
                items.setDeadline(Date.valueOf(adminDeadline.getValue()));
                items.setDescription(adminDescription.getText());
                try {
                    businessImplementation.updateItemData(items);
                } catch (BusinessException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void adminViewInstallment(Installment installment) {
        customerAdminPanel.setVisible(false);
        itemAdminPanel.setVisible(false);
        installmentAdminPanel.setVisible(true);
        backupAdminPanel.setVisible(false);
        alertWarning.setTitle("Warning Dialog!");

        adminInstallmentAmount.setText("" + installment.getDepositAmount());
        adminDepositor.setText(installment.getDepositor());
        adminDepositDate.setValue(installment.getDate().toLocalDate());

        btnUpdateInstallment.setOnAction(event -> {
            if (!adminInstallmentAmount.getText().matches("^[0-9]+(\\.[0-9]+)?$")) {
                adminInstallmentAmount.setStyle("-fx-text-fill: red;");
                alertWarning.setContentText("Input fields not valid");
                alertWarning.showAndWait();
            } else if (adminDepositor.getText().length() <= 0) {
                adminDepositor.setStyle("-fx-text-fill: red;");
                alertWarning.setContentText("Enter Depositor Name");
                alertWarning.showAndWait();
            } else if (!(adminDepositDate.getValue().compareTo(LocalDate.now()) <= 0)) {
                adminDepositDate.setStyle("-fx-text-fill: red;");
                alertWarning.setContentText("Date field should be upto today");
                alertWarning.showAndWait();
            } else {
                adminInstallmentAmount.setStyle("-fx-text-fill: black;");
                adminDepositor.setStyle("-fx-text-fill: black;");
                adminDepositDate.setStyle("-fx-text-fill: black;");

                installment.setDepositAmount(Integer.parseInt(adminInstallmentAmount.getText()));
                installment.setDepositor(adminDepositor.getText());
                installment.setDate(java.sql.Date.valueOf(adminDepositDate.getValue()));
                try {
                    businessImplementation.updateInstallmentData(installment);
                } catch (BusinessException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Logout code
     */
    @FXML
    public void logout() {
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("loginPage.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Lagani");
            stage.setScene(new Scene(root, 720, 480));
            stage.show();

            Scene scene = btnLogout.getScene();
            if (scene != null) {
                Window window = scene.getWindow();
                if (window != null) {
                    window.hide();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void backup() {
        btnBackup.setVisible(false);
        String osName = System.getProperty("os.name").toLowerCase();
        boolean isMacOs = osName.startsWith("mac");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
        LocalDateTime now = LocalDateTime.now();
        String formatDateTime = now.format(formatter);
        btnBrowse.setOnAction(event -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            File selectedDirectory = directoryChooser.showDialog(mainBorderPane.getScene().getWindow());
            if (null != selectedDirectory) {
                if (isMacOs) {
                    System.out.println("get an windows pc");
                } else {
                    File file = new File(selectedDirectory + "/Lagani-dump-" + formatDateTime + ".sql");
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    adminPathViewer.setText(String.valueOf(file));
                    btnBackup.setVisible(true);
                    btnBackup.setOnAction(event1 -> backupWindowsDB(ApplicationConstants.DB_USERNAME, ApplicationConstants.DB_PASSWORD, ApplicationConstants.DB_NAME, file));
                }
            }
        });

    }

    public boolean backupWindowsDB(String dbUsername, String dbPassword, String dbName, File path) {
        String executeCmd = "C:/Program Files/MySQL/MySQL Server 8.0/bin/mysqldump.exe -u" + dbUsername + " -p" + dbPassword + " --add-drop-database -B " + dbName + " -r" + path;
        Process runtimeProcess;
        try {
            runtimeProcess = Runtime.getRuntime().exec(executeCmd);
            int processComplete = runtimeProcess.waitFor();
            if (processComplete == 0) {
                alertSuccess.setTitle("Success Dialog");
                alertSuccess.setContentText("Backup created successfully at " + path);
                alertSuccess.showAndWait();
                return true;
            } else {
                alertWarning.setTitle("Warning Dialog");
                alertWarning.setContentText("Could not create the backup");
                alertWarning.showAndWait();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
