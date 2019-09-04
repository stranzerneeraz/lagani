package sample;

import exception.BusinessException;
import implementation.BusinessImplementation;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Callback;
import modal.Customers;
import modal.User;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

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
    private ListView<String> customerListView;
    @FXML
    private ListView<String> itemsListView;
    @FXML
    private Label nameViewLabel;
    @FXML
    private ContextMenu listContextMenu;
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
        ArrayList customerListString = new ArrayList<String>();
        ArrayList<Customers> csList = businessImplementation.getCustomers();
        int index = 0;
        for (Customers customers : csList) {
            index++;
            customerListString.add(index + ". " + customers.getFullName() + ",\n " + customers.getAddress());
        }
        ObservableList<String> observableArrayList = FXCollections.observableArrayList(customerListString);
        customerListView.setItems(observableArrayList);
        customerListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        customerListView.getSelectionModel().selectFirst();

        customerListView.getSelectionModel().selectedItemProperty().addListener(
                (ObservableValue<? extends String> ov, String old_val, String new_val) -> {
            String selectedItem = customerListView.getSelectionModel().getSelectedItem();
            System.out.println("Item selected : " + selectedItem);


        });

        String selectedItem = customerListView.getSelectionModel().getSelectedItem();
        System.out.println(selectedItem);
    }
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
