package sample;

import exception.BusinessException;
import implementation.BusinessImplementation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;
import modal.Customers;
import modal.User;

import java.io.IOException;
import java.sql.SQLException;

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
        ObservableList<Customers> observableArrayList = FXCollections.observableArrayList(businessImplementation.getCustomers());
        customerListView.setItems(observableArrayList);
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
