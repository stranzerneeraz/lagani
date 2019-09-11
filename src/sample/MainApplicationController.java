package sample;

import exception.BusinessException;
import implementation.BusinessImplementation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Callback;
import modal.Customers;
import modal.Items;
import modal.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

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

    private ObservableList<Customers> customersObservableList = FXCollections.observableArrayList();
    private ObservableList<Items> itemsObservableList;
    private int id = LoginController.id();
    private String searchString = "";
    private Stage window;
    private Window dialogWindow;

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
