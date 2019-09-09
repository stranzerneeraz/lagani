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
import java.sql.SQLException;
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
    private TableColumn<Items,Date> itemDeadline;
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
    private Label familyViewLabel;
    @FXML
    private Label contactViewLabel;
    @FXML
    private Button btnAddNewItem;
    @FXML
    private BorderPane mainBorderPane;
    private ObservableList<Customers> customersObservableList = FXCollections.observableArrayList();
    private ObservableList<Items> itemsObservableList;
    private int id = LoginController.id();

    BusinessImplementation businessImplementation = new BusinessImplementation();
    private Customers selectionCustomer = new Customers();

    public void updateProfile() throws SQLException, BusinessException {
        businessImplementation.updateUserProfile(nameProfile.getText(), firmnameProfile.getText(), contactProfile.getText(), addressProfile.getText(), id);
    }

    public void profileTab() throws SQLException, BusinessException {
        User user = businessImplementation.getUser(id);
        nameProfile.setText(user.getName());
        firmnameProfile.setText(user.getFirmname());
        contactProfile.setText(user.getContact());
        addressProfile.setText(user.getAddress());
    }

    public <customers> void viewCustomers() throws SQLException, BusinessException {
        listContextMenu = new ContextMenu();
        ArrayList<Customers> customerList = businessImplementation.getCustomers();
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
        customerListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        customerListView.getSelectionModel().selectFirst();
        btnAddNewItem.setDisable(true);

        customerListView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (null != newValue) {
                        btnAddNewItem.setDisable(false);
                        Customers selectedCustomer = customerListView.getSelectionModel().getSelectedItem();
                        nameViewLabel.setText(selectedCustomer.getFullName());
                        addressViewLabel.setText(selectedCustomer.getAddress());
                        familyViewLabel.setText(selectedCustomer.getFatherName() + "/" + selectedCustomer.getSpouseName());
                        contactViewLabel.setText("" + selectedCustomer.getContactNo());
                        selectionCustomer = selectedCustomer;
                        try {
                            fetchCustomerItem(selectedCustomer.getCustomerID());
                        } catch (SQLException e) {
                            e.printStackTrace();
                        } catch (BusinessException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

    public void fetchCustomerItem(int id) throws SQLException, BusinessException {
        ArrayList<Items> itemList = businessImplementation.getItems(id);
        itemsObservableList = FXCollections.observableArrayList(itemList);

        itemNo.setCellValueFactory(new PropertyValueFactory<>("itemID"));
        itemType.setCellValueFactory(new PropertyValueFactory<>("type"));
        itemDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        itemAmount.setCellValueFactory(new PropertyValueFactory<>("principal"));
        itemStartDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        itemDeadline.setCellValueFactory(new PropertyValueFactory<>("deadline"));
        viewInstallment.setCellValueFactory(new  PropertyValueFactory<>("Dummy"));
        Callback<TableColumn<Items, String>, TableCell<Items, String>> cellFactoryView = //
                new Callback<TableColumn<Items, String>, TableCell<Items, String>>() {
                    @Override
                    public TableCell call(final TableColumn<Items, String> param) {
                        final TableCell<Items, String> cell = new TableCell<Items, String>() {

                            final Button btn = new Button("View Installment");

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    btn.setOnAction(event -> {
                                        Items items = getTableView().getItems().get(getIndex());
                                        System.out.println(items.getType()
                                                + "   " + items.getPrincipal());
                                    });
                                    setGraphic(btn);
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                };
        viewInstallment.setCellFactory(cellFactoryView);

        addInstallment.setCellValueFactory(new  PropertyValueFactory<>("Dummy"));
        Callback<TableColumn<Items, String>, TableCell<Items, String>> cellFactoryAdd = //
                new Callback<TableColumn<Items, String>, TableCell<Items, String>>() {
                    @Override
                    public TableCell call(final TableColumn<Items, String> param) {
                        final TableCell<Items, String> cell = new TableCell<Items, String>() {

                            final Button btn = new Button("Add Installment");

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    btn.setOnAction(event -> {
                                        Items items = getTableView().getItems().get(getIndex());
                                        System.out.println(items.getType()
                                                + "   " + items.getPrincipal());
                                    });
                                    setGraphic(btn);
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                };
        addInstallment.setCellFactory(cellFactoryAdd);

        calculate.setCellValueFactory(new  PropertyValueFactory<>("Dummy"));
        Callback<TableColumn<Items, String>, TableCell<Items, String>> cellFactoryCalculate = //
                new Callback<TableColumn<Items, String>, TableCell<Items, String>>() {
                    @Override
                    public TableCell call(final TableColumn<Items, String> param) {
                        final TableCell<Items, String> cell = new TableCell<Items, String>() {

                            final Button btn = new Button("Calculate");

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    btn.setOnAction(event -> {
                                        Items items = getTableView().getItems().get(getIndex());
                                        System.out.println(items.getType()
                                                + "   " + items.getPrincipal());
                                    });
                                    setGraphic(btn);
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

    @FXML
    public void addNewCustomer() throws BusinessException {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        dialog.setTitle("Add New Customer");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("addNewCustomerDialog.fxml"));
        Window dialogWindow = dialog.getDialogPane().getScene().getWindow();
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
            AddNewCustomerDialogController addNewCustomerDialogController = fxmlLoader.getController();
            addNewCustomerDialogController.initialize();
            dialogWindow.setOnCloseRequest(event -> {
                dialog.close();
            });
            dialog.showAndWait();
        } catch (IOException e) {
            throw new BusinessException(e);
        }
    }

    @FXML
    public void addNewItem() throws BusinessException {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        dialog.setTitle("Add New Item");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("addNewItemDialog.fxml"));
        Window dialogWindow = dialog.getDialogPane().getScene().getWindow();
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
            AddNewItemDialogController addNewItemDialogController = fxmlLoader.getController();
            addNewItemDialogController.updateDialogBox(selectionCustomer);
            dialogWindow.setOnCloseRequest(event -> {
                dialog.close();
            });
            dialog.showAndWait();
        } catch (IOException e) {
            throw new BusinessException(e);
        }
    }

    /**
     * When user press Logout Button
     * Takes user to Login page
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
