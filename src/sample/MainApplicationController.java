package sample;

import com.sun.javafx.scene.control.skin.VirtualFlow;
import exception.BusinessException;
import implementation.BusinessImplementation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Callback;
import modal.Customers;
import modal.Installment;
import modal.Items;
import modal.User;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

public class MainApplicationController {
    @FXML
    private TextField nameProfile;
    @FXML
    private TextField firmnameProfile;
    @FXML
    private TextField contactProfile;
    @FXML
    private TextField addressProfile;
    @FXML
    private Button btnLogout;
    @FXML
    private TextField searchCustomer;
    @FXML
    private ListView<Customers> customerListView;
    @FXML
    private TableView<Items> itemsTableView;
    @FXML
    private TableColumn<Items, Integer> itemNo;
    @FXML
    private TableColumn<Items, String> itemType;
    @FXML
    private TableColumn<Items, String> itemDescription;
    @FXML
    private TableColumn<Items, Integer> itemAmount;
    @FXML
    private TableColumn<Items, Date> itemStartDate;
    @FXML
    private TableColumn<Items, Date> itemDeadline;
    @FXML
    private TableColumn<Items, String> viewInstallment;
    @FXML
    private TableColumn<Items, String> addInstallment;
    @FXML
    private TableColumn<Items, String> calculate;
    @FXML
    private ContextMenu listContextMenu;
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
    private Button btnAddNewItem;
    @FXML
    private BorderPane mainBorderPane;
    @FXML
    private VBox customerAdminPanel;
    @FXML
    private VBox itemAdminPanel;
    @FXML
    private VBox installmentAdminPanel;
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
    private TextField adminInstallmentAmount;
    @FXML
    private TextField adminDepositor;
    @FXML
    private DatePicker adminDepositDate;
    @FXML
    private Button btnUpdateInstallment;

    private ObservableList<Customers> customersObservableList = FXCollections.observableArrayList();
    private ObservableList<Items> itemsObservableList;
    private int id = LoginController.id();
    private String searchString = "";
    private Stage window;
    private Window dialogWindow;

    Alert alert = new Alert(Alert.AlertType.WARNING);

    private BusinessImplementation businessImplementation = new BusinessImplementation();
    private Customers selectionCustomer = new Customers();

    /**
     * Update Profile of the User
     */
    public void updateProfile() throws BusinessException {
        businessImplementation.updateUserProfile(nameProfile.getText(), firmnameProfile.getText(), contactProfile.getText(), addressProfile.getText(), id);
    }

    /**
     * View Profile of the User
     */
    public void profileTab() throws BusinessException {
        User user = businessImplementation.getUser(id);
        nameProfile.setText(user.getName());
        firmnameProfile.setText(user.getFirmname());
        contactProfile.setText(user.getContact());
        addressProfile.setText(user.getAddress());
    }

    /**
     * View Customers who takes loan
     * Searching of the customer
     */
    public <customers> void viewCustomers() throws BusinessException {
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
                    @Override
                    protected void updateItem(Customers customers, boolean empty) {
                        super.updateItem(customers, empty);
                        if (empty || customers == null) {
                            setText("");
                        } else {
                            setText(customers.getFullName() + ", " + customers.getAddress());
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

        itemNo.setCellValueFactory(new PropertyValueFactory<>("itemID"));
        itemType.setCellValueFactory(new PropertyValueFactory<>("type"));
        itemDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        itemAmount.setCellValueFactory(new PropertyValueFactory<>("principal"));
        itemStartDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        itemDeadline.setCellValueFactory(new PropertyValueFactory<>("deadline"));

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
        Callback<TableColumn<Items, String>, TableCell<Items, String>> cellFactoryCalculate =
                new Callback<TableColumn<Items, String>, TableCell<Items, String>>() {
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
        ScrollBar table1HorizontalScrollBar = findScrollBar( itemsTableView, Orientation.HORIZONTAL);
        ScrollBar table1VerticalScrollBar = findScrollBar( itemsTableView, Orientation.VERTICAL);
        table1HorizontalScrollBar.setVisible(true);
        table1VerticalScrollBar.setVisible(false);
        VirtualFlow flow1 = (VirtualFlow) itemsTableView.lookup(".virtual-flow");
        flow1.requestLayout();
    }

    private ScrollBar findScrollBar(TableView<?> table, Orientation orientation) {
        Set<Node> set = table.lookupAll(".scroll-bar");
        for( Node node: set) {
            ScrollBar bar = (ScrollBar) node;
            if( bar.getOrientation() == orientation) {
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
            addNewCustomerDialogController.initialize();
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
            dialogWindow.setOnCloseRequest(event -> dialog.close());
            dialog.showAndWait();
        } catch (IOException e) {
            throw new BusinessException(e);
        }
    }

    public void adminTab() {
        customerAdminPanel.setVisible(false);
        itemAdminPanel.setVisible(false);
        installmentAdminPanel.setVisible(false);
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning Dialog");
        btnViewCustomer.setOnAction(event -> {
            int customerID = Integer.parseInt(getCustomerID.getText());
            int cID = 0;
            try {
                Customers customers = businessImplementation.getCustomerByID(customerID);
                if (customers == null) {
                    alert.setContentText("Enter valid customer ID");
                    alert.showAndWait();
                } else {
                    adminViewCustomer(customers);
                }
            } catch (BusinessException e) {
                e.printStackTrace();
            }
        });
        btnViewItem.setOnAction(event -> {
            int itemID = Integer.parseInt(getItemID.getText());
            try {
                if (null == businessImplementation.getItemByID(itemID)) {
                    alert.setContentText("Enter valid item ID");
                    alert.showAndWait();
                } else {
                    Items items = businessImplementation.getItemByID(itemID);
                    adminViewItem(items);
                }
            } catch (BusinessException e) {
                e.printStackTrace();
            }
        });
        btnViewInstallment.setOnAction(event -> {
            int installmentID = Integer.parseInt(getInstallmentID.getText());
            try {
                if (null == businessImplementation.getInstallmentByID(installmentID)) {
                    alert.setContentText("Enter valid installment ID");
                    alert.showAndWait();
                } else {
                    Installment installment = businessImplementation.getInstallmentByID(installmentID);
                    adminViewInstallment(installment);
                }
            } catch (BusinessException e) {
                e.printStackTrace();
            }
        });
    }

    public void adminViewCustomer(Customers customers) {
        customerAdminPanel.setVisible(true);
        itemAdminPanel.setVisible(false);
        installmentAdminPanel.setVisible(false);
        alert.setTitle("Warning Dialog!");

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
                alert.setContentText("Enter full name");
                alert.showAndWait();
            } else if (adminAddress.getText().length() <= 0) {
                adminAddress.setStyle("-fx-text-fill: red;");
                alert.setContentText("Enter address");
                alert.showAndWait();
            } else if (!adminWard.getText().matches("[0-9]*[0-9]+")) {
                adminWard.setStyle("-fx-text-fill: red;");
                alert.setContentText("Enter ward number in integer");
                alert.showAndWait();
            } else if (adminFatherName.getText().length() <= 0) {
                adminFatherName.setStyle("-fx-text-fill: red;");
                alert.setContentText("Enter father's name");
                alert.showAndWait();
            } else if (adminSpouseName.getText().length() <= 0) {
                adminSpouseName.setStyle("-fx-text-fill: red;");
                alert.setContentText("Enter spouse's name");
                alert.showAndWait();
            } else if (!adminContactNumber.getText().matches("^[0-9]{10}$")) {
                adminContactNumber.setStyle("-fx-text-fill: red;");
                alert.setContentText("Enter contact number");
                alert.showAndWait();
            } else if (adminRemarks.getText().length() <= 0) {
                adminRemarks.setStyle("-fx-text-fill: red;");
                alert.setContentText("Enter remarks");
                alert.showAndWait();
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
                    businessImplementation.updateCustomerData(customers.getFullName(), customers.getAddress(), customers.getWard(),
                            customers.getFatherName(), customers.getSpouseName(), customers.getContactNo(), customers.getRemarks(),
                            customers.getCustomerID());
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
        alert.setTitle("Warning Dialog!");

        adminItemAmount.setText("" + items.getPrincipal());
        adminRate.setText("" + items.getRate());
        adminDescription.setText(items.getDescription());

        btnUpdateItem.setOnAction(event -> {
            if (!adminItemAmount.getText().matches("^[0-9]+(\\.[0-9]+)?$")) {
                adminItemAmount.setStyle("-fx-text-fill: red;");
                alert.setContentText("Input fields not valid");
                alert.showAndWait();
            } else if (!(adminStartDate.getValue().compareTo(LocalDate.now()) <= 0)) {
                adminStartDate.setStyle("-fx-text-fill: red;");
                alert.setContentText("Date field should be upto today");
                alert.showAndWait();
            } else if (!adminRate.getText().matches("^[0-9](\\.[0-9]+)?$")) {
                adminRate.setStyle("-fx-text-fill: red;");
                alert.setContentText("Rate should be in decimal");
                alert.showAndWait();
            } else if (!(adminDeadline.getValue().compareTo(LocalDate.now()) > 0)) {
                adminDeadline.setStyle("-fx-text-fill: red;");
                alert.setContentText("Deadline should be in future");
                alert.showAndWait();
            } else if (adminDescription.getText().length() <= 0) {
                adminDescription.setStyle("-fx-text-fill: red;");
                alert.setContentText("Enter description");
                alert.showAndWait();
            } else {
                adminItemAmount.setStyle("-fx-text-fill: black;");
                adminStartDate.setStyle("-fx-text-fill: black;");
                adminRate.setStyle("-fx-text-fill: black;");
                adminDeadline.setStyle("-fx-text-fill: black;");
                adminDescription.setStyle("-fx-text-fill: black;");
            }
            items.setPrincipal(Integer.parseInt(adminItemAmount.getText()));
            items.setStartDate(java.sql.Date.valueOf(adminStartDate.getValue()));
            items.setRate(Double.parseDouble(adminRate.getText()));
            items.setDeadline(java.sql.Date.valueOf(adminDeadline.getValue()));
            items.setDescription(adminDescription.getText());

            try {
                businessImplementation.updateItemDate(items.getPrincipal(), items.getType(), items.getStartDate(), items.getRate(), items.getDeadline(),
                        items.getDescription(), items.getItemID());
            } catch (BusinessException e) {
                e.printStackTrace();
            }
        });
    }

    public void adminViewInstallment(Installment installment) {
        customerAdminPanel.setVisible(false);
        itemAdminPanel.setVisible(false);
        installmentAdminPanel.setVisible(true);
        alert.setTitle("Warning Dialog!");

        adminInstallmentAmount.setText("" + installment.getDepositAmount());
        adminDepositor.setText(installment.getDepositor());

        btnUpdateInstallment.setOnAction(event -> {
                    if (!adminInstallmentAmount.getText().matches("^[0-9]+(\\.[0-9]+)?$")) {
                        adminInstallmentAmount.setStyle("-fx-text-fill: red;");
                        alert.setContentText("Input fields not valid");
                        alert.showAndWait();
                    } else if (adminDepositor.getText().length() <= 0) {
                        adminDepositor.setStyle("-fx-text-fill: red;");
                        alert.setContentText("Enter Depositor Name");
                        alert.showAndWait();
                    } else if (!(adminDepositDate.getValue().compareTo(LocalDate.now()) <= 0)) {
                        adminDepositDate.setStyle("-fx-text-fill: red;");
                        alert.setContentText("Date field should be upto today");
                        alert.showAndWait();
                    } else {
                        adminInstallmentAmount.setStyle("-fx-text-fill: black;");
                        adminDepositor.setStyle("-fx-text-fill: black;");
                        adminDepositDate.setStyle("-fx-text-fill: black;");
                    }
            installment.setDepositAmount(Integer.parseInt(adminInstallmentAmount.getText()));
            installment.setDepositor(adminDepositor.getText());
            installment.setDate(java.sql.Date.valueOf(adminDepositDate.getValue()));

            try {
                businessImplementation.updateInstallmentData(installment.getDepositAmount(), installment.getDepositor(), installment.getDate(),
                        installment.getInstallmentID());
            } catch (BusinessException e) {
                e.printStackTrace();
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
}
