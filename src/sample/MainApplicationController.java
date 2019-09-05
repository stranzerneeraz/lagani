package sample;

import exception.BusinessException;
import implementation.BusinessImplementation;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.LoadException;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Callback;
import modal.Customers;
import modal.Items;
import modal.User;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

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
    private ListView<Items> itemsListView;
    @FXML
    private Label nameViewLabel;
    @FXML
    private Label contactViewLabel;
    @FXML
    private Label familyViewLabel;
    @FXML
    private Button btnAddNewItem;
    @FXML
    private BorderPane mainBorderPane;
    private ObservableList<Customers> customersObservableList;
    private ObservableList<Items> itemsObservableList;
    private int id = LoginController.id();

    BusinessImplementation businessImplementation = new BusinessImplementation();

    public void updateProfile() throws SQLException, BusinessException {
        businessImplementation.updateUserProfile(nameProfile.getText(), firmnameProfile.getText(), contactProfile.getText(),
                addressProfile.getText(), id);
    }

    public void profileTab() throws SQLException, BusinessException {
        User user = businessImplementation.getUser(id);
        nameProfile.setText(user.getName());
        firmnameProfile.setText(user.getFirmname());
        contactProfile.setText(user.getContact());
        addressProfile.setText(user.getAddress());
    }

    public void viewCustomers() throws SQLException, BusinessException {
        ArrayList<Customers> customerList = businessImplementation.getCustomers();
        customerListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        customerListView.getSelectionModel().selectFirst();
        customersObservableList = FXCollections.observableArrayList();
        for (Customers customers : customerList) {
            customersObservableList.add(customers);
        }
        customerListView.setItems(customersObservableList);
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
                return cell;
            }
        });

        customerListView.getSelectionModel().selectedItemProperty().addListener(
                (ObservableValue<? extends Customers> ov, Customers old_val, Customers new_val) -> {
                    Customers selectedCustomer = customerListView.getSelectionModel().getSelectedItem();
                    nameViewLabel.setText(selectedCustomer.getFullName());
                    contactViewLabel.setText("" + selectedCustomer.getContactNo());
                    familyViewLabel.setText(selectedCustomer.getFatherName() + "/" + selectedCustomer.getSpouseName());
                    int selectionID = selectedCustomer.getCustomerID();
                    System.out.println(selectionID);
                    try {
                        fetchCustomerItem(selectionID);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } catch (BusinessException e) {
                        e.printStackTrace();
                    }
                });
    }

    public void fetchCustomerItem(int id) throws SQLException, BusinessException {
        ArrayList<Items> itemList = businessImplementation.getItems(id);
        itemsListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        itemsListView.getSelectionModel().selectFirst();
        itemsObservableList = FXCollections.observableArrayList();
        for (Items items : itemList) {
            itemsObservableList.add(items);
        }
        itemsListView.setItems(itemsObservableList);
        itemsListView.setCellFactory(new Callback<ListView<Items>, ListCell<Items>>() {
            @Override
            public ListCell<Items> call(ListView<Items> param) {
                ListCell<Items> cell = new ListCell<Items>() {
                    @Override
                    protected void updateItem(Items items, boolean empty) {
                        super.updateItem(items, empty);
                        if (empty || items == null) {
                            setText("");
                        } else {
                            setText(items.getType() + "\n" + items.getPrincipal() + "\t" + items.getRate() + "\n" + items.getDescription());
                        }
                    }
                };
                return cell;
            }
        });
    }

    @FXML
//    void addNewItem() throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("addNewItemDialog.fxml"));
//        Parent parent = fxmlLoader.load();
//        AddNewItemDialogController dialogController = fxmlLoader.<AddNewItemDialogController>getController();
//        dialogController.
////        dialogController.setAppMainObservableList(tvObservableList);
//
//        Scene scene = new Scene(parent, 300, 200);
//        Stage stage = new Stage();
//        stage.initModality(Modality.APPLICATION_MODAL);
//        stage.setScene(scene);
//        stage.showAndWait();
//    }

    public void addNewItem() throws BusinessException, SQLException {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        dialog.setTitle("Add New Item");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("addNewItemDialog.fxml"));
        try {
            fxmlLoader.load();
//            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println("message" + e);
            throw new BusinessException(e);
        }
        dialog.showAndWait();


        dialog.getDialogPane().getButtonTypes().add(ButtonType.APPLY);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK){
            AddNewItemDialogController addNewItemDialogController = fxmlLoader.getController();
            addNewItemDialogController.addNewItem();
        }
    }

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
