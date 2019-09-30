package sample;

import com.sun.javafx.scene.control.skin.VirtualFlow;
import constants.ApplicationConstants;
import exception.BusinessException;
import implementation.BusinessImplementation;
import javafx.beans.property.ReadOnlyObjectWrapper;
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
import javafx.stage.*;
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
    private TableView<DashboardItem> dashboardTableView;
    @FXML
    private TableColumn<DashboardItem, Integer> serialNoDashboard;
    @FXML
    private Pagination pagination;
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
    private Button btnAddNewItem;
    @FXML
    private BorderPane mainBorderPane;
    @FXML
    private TextField nameProfile;
    @FXML
    private TextField firmNameProfile;
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
    private Button btnSelectMySQL;
    @FXML
    private Label mySQLPathViewer;
    @FXML
    private Button btnBrowse;
    @FXML
    private Label adminPathViewer;
    @FXML
    private Button btnBackup;

    private ObservableList<Customers> customersObservableList = FXCollections.observableArrayList();
    private ObservableList<Items> itemsObservableList;
    private int id = LoginController.id();
    private String searchString = "";
    private Stage window;
    private Window dialogWindow;
    private int rowsPerPage = 50;

    public MainApplicationController() {
        itemsObservableList = FXCollections.observableArrayList();
    }

    private Alert alertWarning = new Alert(Alert.AlertType.WARNING);
    private Alert alertSuccess = new Alert(AlertType.INFORMATION);
    private BusinessImplementation businessImplementation = new BusinessImplementation();
    private Customers selectionCustomer = new Customers();

    public void initialize() {
    }

    /**
     * Creates Pagination for TableView in Dashboard Tab
     */
    @FXML
    public void dashboardTab() {
        pagination.setPageFactory(this::createPage);
    }

    /**
     * Display items according to Pagination
     * @param pageIndex
     * @return
     */
    private Node createPage(int pageIndex) {
        int lastIndex;
        int count = 0;
        ArrayList<DashboardItem> dashboardItem = new ArrayList<>();
        try {
            count = businessImplementation.countDashboardItem();
            dashboardItem = businessImplementation.getDashboardItem(pageIndex * rowsPerPage);
        } catch (BusinessException e) {
            e.printStackTrace();
        }
        ObservableList<DashboardItem> dashboardItemObservableList = FXCollections.observableArrayList(dashboardItem);
        int itemsPerPage = 1;
        int page = pageIndex * itemsPerPage;
        int pageCount = (count / rowsPerPage) + 1;
        int displace = count % rowsPerPage;
        if (displace > 0) {
            lastIndex = count / rowsPerPage;
        } else {
            lastIndex = count / rowsPerPage - 1;
        }
        for (int i = page; i < page + itemsPerPage; i++) {
            TableView<DashboardItem> table = new TableView<>();
            serialNoDashboard.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(
                    dashboardTableView.getItems().indexOf(cellData.getValue()) + (pageIndex * rowsPerPage) + 1));
            dashboardTableView.setItems(dashboardItemObservableList);
            if (lastIndex == pageIndex) {
                table.setItems(FXCollections.observableArrayList(dashboardItemObservableList.subList(pageIndex * rowsPerPage, pageIndex * rowsPerPage + displace)));
            } else {
                table.setItems(FXCollections.observableArrayList(dashboardItemObservableList.subList(pageIndex * rowsPerPage, pageIndex * rowsPerPage + dashboardItemObservableList.size())));
            }
        }
        pagination.setPageCount(pageCount);
        return new BorderPane(dashboardTableView);
    }

    /**
     * Searching of Customers
     * Gets selected Customer for showing items
     * @throws BusinessException
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

    /**
     * Get Customers to be displayed in ListView
     * @param searchString
     * @throws BusinessException
     */
    private void getCustomersForView(String searchString) throws BusinessException {
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
                        int index = this.getIndex();
                        if (empty || customers == null) {
                            setText("");
                            setTooltip(null);
                        } else {
                            setText((index + 1) + ". " + customers.getFullName() + ", " + customers.getAddress());
                            tooltip.setText("" + customers.getCustomerID());
                            setTooltip(tooltip);
                        }
                    }
                };
                return cell;
            }
        });
    }

    /**
     * Fetches Items of Customer and displays in TableView
     *
     * @param id
     * @throws BusinessException
     */
    private void fetchCustomerItem(int id) throws BusinessException {
        ArrayList<Items> itemList = businessImplementation.getItems(id);
        itemsObservableList = FXCollections.observableArrayList(itemList);
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
                            Items selectedItem = getTableView().getItems().get(getIndex());
                            if(selectedItem.getIsActive() == 1){
                                btnAddInstallment.setOnAction(event -> {
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
                            } else {
                                btnAddInstallment.setDisable(true);
                            }

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
        assert table1HorizontalScrollBar != null;
        table1HorizontalScrollBar.setVisible(true);
        assert table1VerticalScrollBar != null;
        table1VerticalScrollBar.setVisible(false);
        VirtualFlow flow1 = (VirtualFlow) itemsTableView.lookup(".virtual-flow");
        flow1.requestLayout();
    }

    /**
     * Property for Scrollbar
     *
     * @param table
     * @param orientation
     * @return
     */
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
     * Adds new Customer
     *
     * @throws BusinessException
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
            addNewCustomerDialogController.initialize();
            dialogWindow.setOnCloseRequest(event -> dialog.close());
            dialog.showAndWait();
        } catch (IOException e) {
            throw new BusinessException(e);
        }
        viewCustomers();
    }

    /**
     * Adds new Item for selected Customer
     *
     * @throws BusinessException
     */
    @FXML
    public void addNewItem() throws BusinessException {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        dialog.setTitle("Add New Item");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("addNewItemDialog.fxml"));
        dialogWindow = dialog.getDialogPane().getScene().getWindow();
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
            AddNewItemDialogController addNewItemDialogController = fxmlLoader.getController();
            addNewItemDialogController.updateDialogBox(selectionCustomer);
            dialogWindow.setOnCloseRequest((WindowEvent event) -> dialog.close());
            dialog.onCloseRequestProperty();
            fetchCustomerItem(selectionCustomer.getCustomerID());
            dialog.showAndWait();
        } catch (IOException | BusinessException e) {
            throw new BusinessException(e);
        }
        fetchCustomerItem(selectionCustomer.getCustomerID());
    }

    /**
     * View User data in Profile Tab
     *
     * @throws BusinessException
     */
    @FXML
    public void profileTab() throws BusinessException {
        User user = businessImplementation.getUser(id);
        nameProfile.setText(user.getName());
        firmNameProfile.setText(user.getFirmname());
        contactProfile.setText("" + user.getContact());
        emailProfile.setText(user.getEmail());
        addressProfile.setText(user.getAddress());
    }

    /**
     * Updates User Profile
     *
     * @throws BusinessException
     */
    @FXML
    public void updateProfile() throws BusinessException {
        alertWarning.setTitle(ApplicationConstants.WARNING_DIALOG);
        User user = new User();
        if (nameProfile.getText().length() <= 0) {
            nameProfile.setStyle(ApplicationConstants.ERROR_ENTRY);
            alertWarning.setContentText("Enter name");
            alertWarning.showAndWait();
        } else if (firmNameProfile.getText().length() <= 0) {
            firmNameProfile.setStyle(ApplicationConstants.ERROR_ENTRY);
            alertWarning.setContentText("Enter firm name");
            alertWarning.showAndWait();
        } else if (!contactProfile.getText().matches(ApplicationConstants.CONTACT_NUMBER_VALIDATION_REGEX)) {
            contactProfile.setStyle(ApplicationConstants.ERROR_ENTRY);
            alertWarning.setContentText("Enter contact number");
            alertWarning.showAndWait();
        } else if (!emailProfile.getText().matches(ApplicationConstants.EMAIL_VALIDATION_REGEX)) {
            emailProfile.setStyle(ApplicationConstants.ERROR_ENTRY);
            alertWarning.setContentText("Enter email address");
            alertWarning.showAndWait();
        } else if (addressProfile.getText().length() <= 0) {
            addressProfile.setStyle(ApplicationConstants.ERROR_ENTRY);
            alertWarning.setContentText("Enter address");
            alertWarning.showAndWait();
        } else {
            nameProfile.setStyle(ApplicationConstants.CORRECT_ENTRY);
            firmNameProfile.setStyle(ApplicationConstants.CORRECT_ENTRY);
            contactProfile.setStyle(ApplicationConstants.CORRECT_ENTRY);
            addressProfile.setStyle(ApplicationConstants.CORRECT_ENTRY);

            user.setName(nameProfile.getText());
            user.setFirmname(firmNameProfile.getText());
            user.setContact(Long.parseLong(contactProfile.getText()));
            user.setEmail(emailProfile.getText());
            user.setAddress(addressProfile.getText());
            businessImplementation.updateUserProfile(user, id);
        }
    }

    /**
     * Accepts Id of data that needs to be modified
     * Backup button included
     */
    @FXML
    public void adminTab() {
        customerAdminPanel.setVisible(false);
        itemAdminPanel.setVisible(false);
        installmentAdminPanel.setVisible(false);
        backupAdminPanel.setVisible(false);
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(ApplicationConstants.WARNING_DIALOG);
        btnViewCustomer.setOnAction(event -> {
            String customerID = getCustomerID.getText();
            if (!customerID.matches(ApplicationConstants.WARD_ID_VALIDATION_REGEX)) {
                getCustomerID.setStyle(ApplicationConstants.ERROR_ENTRY);
                alertWarning.setContentText("Enter Customer ID");
                alertWarning.showAndWait();
            } else {
                getCustomerID.setStyle(ApplicationConstants.CORRECT_ENTRY);
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
            if (!itemID.matches(ApplicationConstants.WARD_ID_VALIDATION_REGEX)) {
                getItemID.setStyle(ApplicationConstants.ERROR_ENTRY);
                alertWarning.setContentText("Enter Item ID");
                alertWarning.showAndWait();
            } else {
                getItemID.setStyle(ApplicationConstants.CORRECT_ENTRY);
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
            if (!installmentID.matches(ApplicationConstants.WARD_ID_VALIDATION_REGEX)) {
                getInstallmentID.setStyle(ApplicationConstants.ERROR_ENTRY);
                alertWarning.setContentText("Enter Installment ID");
                alertWarning.showAndWait();
            } else {
                getInstallmentID.setStyle(ApplicationConstants.CORRECT_ENTRY);
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
            try {
                backup();
            } catch (BusinessException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * View Customer data that needs to be updated
     *
     * @param customers
     */
    private void adminViewCustomer(Customers customers) {
        customerAdminPanel.setVisible(true);
        itemAdminPanel.setVisible(false);
        installmentAdminPanel.setVisible(false);
        backupAdminPanel.setVisible(false);
        alertWarning.setTitle(ApplicationConstants.WARNING_DIALOG);

        adminFullName.setText(customers.getFullName());
        adminAddress.setText(customers.getAddress());
        adminWard.setText("" + customers.getWard());
        adminFatherName.setText(customers.getFatherName());
        adminSpouseName.setText(customers.getSpouseName());
        adminContactNumber.setText("" + customers.getContactNo());
        adminRemarks.setText(customers.getRemarks());

        btnUpdateCustomer.setOnAction(event -> {
            if (adminFullName.getText().length() <= 0) {
                adminFullName.setStyle(ApplicationConstants.ERROR_ENTRY);
                alertWarning.setContentText("Enter full name");
                alertWarning.showAndWait();
            } else if (adminAddress.getText().length() <= 0) {
                adminAddress.setStyle(ApplicationConstants.ERROR_ENTRY);
                alertWarning.setContentText("Enter address");
                alertWarning.showAndWait();
            } else if (!adminWard.getText().matches(ApplicationConstants.WARD_ID_VALIDATION_REGEX)) {
                adminWard.setStyle(ApplicationConstants.ERROR_ENTRY);
                alertWarning.setContentText("Enter ward number in integer");
                alertWarning.showAndWait();
            } else if (adminFatherName.getText().length() <= 0) {
                adminFatherName.setStyle(ApplicationConstants.ERROR_ENTRY);
                alertWarning.setContentText("Enter father's name");
                alertWarning.showAndWait();
            } else if (adminSpouseName.getText().length() <= 0) {
                adminSpouseName.setStyle(ApplicationConstants.ERROR_ENTRY);
                alertWarning.setContentText("Enter spouse's name");
                alertWarning.showAndWait();
            } else if (!adminContactNumber.getText().matches(ApplicationConstants.CONTACT_NUMBER_VALIDATION_REGEX)) {
                adminContactNumber.setStyle(ApplicationConstants.ERROR_ENTRY);
                alertWarning.setContentText("Enter contact number");
                alertWarning.showAndWait();
            } else if (adminRemarks.getText().length() <= 0) {
                adminRemarks.setStyle(ApplicationConstants.ERROR_ENTRY);
                alertWarning.setContentText("Enter remarks");
                alertWarning.showAndWait();
            } else {
                adminFullName.setStyle(ApplicationConstants.CORRECT_ENTRY);
                adminAddress.setStyle(ApplicationConstants.CORRECT_ENTRY);
                adminWard.setStyle(ApplicationConstants.CORRECT_ENTRY);
                adminFatherName.setStyle(ApplicationConstants.CORRECT_ENTRY);
                adminSpouseName.setStyle(ApplicationConstants.CORRECT_ENTRY);
                adminContactNumber.setStyle(ApplicationConstants.CORRECT_ENTRY);
                adminRemarks.setStyle(ApplicationConstants.CORRECT_ENTRY);

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

    /**
     * View Item data that needs to be updated
     *
     * @param items
     */
    private void adminViewItem(Items items) {
        customerAdminPanel.setVisible(false);
        itemAdminPanel.setVisible(true);
        installmentAdminPanel.setVisible(false);
        backupAdminPanel.setVisible(false);
        alertWarning.setTitle(ApplicationConstants.WARNING_DIALOG);

        adminItemAmount.setText(items.getPrincipal());
        adminTypeChooser.setValue(items.getType());
        adminStartDate.setValue(items.getStartDate().toLocalDate());
        adminRate.setText(items.getRate());
        adminDeadline.setValue(items.getDeadline().toLocalDate());
        adminDescription.setText(items.getDescription());

        btnUpdateItem.setOnAction(event -> {
            if (!adminItemAmount.getText().matches(ApplicationConstants.NUMBER_VALIDATION_REGEX)) {
                adminItemAmount.setStyle(ApplicationConstants.ERROR_ENTRY);
                alertWarning.setContentText("Input fields not valid");
                alertWarning.showAndWait();
            } else if (!(adminStartDate.getValue().compareTo(LocalDate.now()) <= 0)) {
                adminStartDate.setStyle(ApplicationConstants.ERROR_ENTRY);
                alertWarning.setContentText("Date field should be upto today");
                alertWarning.showAndWait();
            } else if (!adminRate.getText().matches(ApplicationConstants.NUMBER_VALIDATION_REGEX)) {
                adminRate.setStyle(ApplicationConstants.ERROR_ENTRY);
                alertWarning.setContentText("Rate should be in decimal");
                alertWarning.showAndWait();
            } else if (!(adminDeadline.getValue().compareTo(LocalDate.now()) > 0)) {
                adminDeadline.setStyle(ApplicationConstants.ERROR_ENTRY);
                alertWarning.setContentText("Deadline should be in future");
                alertWarning.showAndWait();
            } else if (adminDescription.getText().length() <= 0) {
                adminDescription.setStyle(ApplicationConstants.ERROR_ENTRY);
                alertWarning.setContentText("Enter description");
                alertWarning.showAndWait();
            } else {
                adminItemAmount.setStyle(ApplicationConstants.CORRECT_ENTRY);
                adminStartDate.setStyle(ApplicationConstants.CORRECT_ENTRY);
                adminRate.setStyle(ApplicationConstants.CORRECT_ENTRY);
                adminDeadline.setStyle(ApplicationConstants.CORRECT_ENTRY);
                adminDescription.setStyle(ApplicationConstants.CORRECT_ENTRY);

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

    /**
     * View Installment data that needs to be updated
     *
     * @param installment
     */
    private void adminViewInstallment(Installment installment) {
        customerAdminPanel.setVisible(false);
        itemAdminPanel.setVisible(false);
        installmentAdminPanel.setVisible(true);
        backupAdminPanel.setVisible(false);
        alertWarning.setTitle(ApplicationConstants.WARNING_DIALOG);

        adminInstallmentAmount.setText("" + installment.getDepositAmount());
        adminDepositor.setText(installment.getDepositor());
        adminDepositDate.setValue(installment.getDate().toLocalDate());

        btnUpdateInstallment.setOnAction(event -> {
            if (!adminInstallmentAmount.getText().matches(ApplicationConstants.NUMBER_VALIDATION_REGEX)) {
                adminInstallmentAmount.setStyle(ApplicationConstants.ERROR_ENTRY);
                alertWarning.setContentText("Input fields not valid");
                alertWarning.showAndWait();
            } else if (adminDepositor.getText().length() <= 0) {
                adminDepositor.setStyle(ApplicationConstants.ERROR_ENTRY);
                alertWarning.setContentText("Enter Depositor Name");
                alertWarning.showAndWait();
            } else if (!(adminDepositDate.getValue().compareTo(LocalDate.now()) <= 0)) {
                adminDepositDate.setStyle(ApplicationConstants.ERROR_ENTRY);
                alertWarning.setContentText("Date field should be upto today");
                alertWarning.showAndWait();
            } else {
                adminInstallmentAmount.setStyle(ApplicationConstants.CORRECT_ENTRY);
                adminDepositor.setStyle(ApplicationConstants.CORRECT_ENTRY);
                adminDepositDate.setStyle(ApplicationConstants.CORRECT_ENTRY);

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

    /**
     * Backup User Data
     */
    private void backup() throws BusinessException {
        btnBackup.setVisible(false);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(ApplicationConstants.BACKUP_DATE_FORMAT);
        LocalDateTime now = LocalDateTime.now();
        String formatDateTime = now.format(formatter);
        String mySQLDumpPath = businessImplementation.getMySQLDumpPath();
        mySQLPathViewer.setText(mySQLDumpPath);

        btnBrowse.setOnAction(event -> {

            DirectoryChooser directoryChooser = new DirectoryChooser();
            File selectedDirectory = directoryChooser.showDialog(mainBorderPane.getScene().getWindow());
            if (null != selectedDirectory) {
                File file = new File(selectedDirectory + "/Lagani-dump-" + formatDateTime + ".sql");
                adminPathViewer.setText(String.valueOf(file));
                btnBackup.setVisible(true);
                btnBackup.setOnAction(event1 -> {
                    String finalMySQLDumpPath = null;
                    try {
                        finalMySQLDumpPath = businessImplementation.getMySQLDumpPath();
                        file.createNewFile();
                        backupDatabase(ApplicationConstants.DB_USERNAME, ApplicationConstants.DB_PASSWORD, ApplicationConstants.DB_NAME, file, finalMySQLDumpPath);
                    } catch (IOException | BusinessException e) {
                        e.printStackTrace();
                    }
                });

            }
        });
        btnSelectMySQL.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            File selectedFile = fileChooser.showOpenDialog(mainBorderPane.getScene().getWindow());
            if (null != selectedFile) {
                mySQLPathViewer.setText(String.valueOf(selectedFile));
                try {
                    businessImplementation.updateMySQLDumpPath(String.valueOf(selectedFile));
                } catch (BusinessException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Backup data
     * @param dbUsername
     * @param dbPassword
     * @param dbName
     * @param path
     */
    private void backupDatabase(String dbUsername, String dbPassword, String dbName, File path, String mySQLDumpPath) {
        String executeCmd = mySQLDumpPath + " -u" + dbUsername + " -p" + dbPassword + " --add-drop-database -B " + dbName + " -r" + path;
        Process runtimeProcess;
        try {
            runtimeProcess = Runtime.getRuntime().exec(executeCmd);
            int processComplete = runtimeProcess.waitFor();
            if (processComplete == 0) {
                alertSuccess.setTitle(ApplicationConstants.SUCCESS_DIALOG);
                alertSuccess.setContentText("Backup created successfully at " + path);
                alertSuccess.showAndWait();
            } else {
                alertWarning.setTitle(ApplicationConstants.WARNING_DIALOG);
                alertWarning.setContentText("Could not create the backup");
                alertWarning.showAndWait();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
