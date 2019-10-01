package sample;

import constants.ApplicationConstants;
import exception.BusinessException;
import implementation.BusinessImplementation;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

public class LoginController {
    @FXML
    private GridPane loginPane;
    @FXML
    public TextField username;
    @FXML
    public PasswordField password;
    @FXML
    private Button loginButton;
    public static int id;

    /**
     * user login section
     * @return
     * @throws BusinessException
     * @throws IOException
     */
    public int checkUser() throws BusinessException, IOException {
        BusinessImplementation businessImplementation = new BusinessImplementation();
        id = businessImplementation.authenticateUser(username.getText(), password.getText());
        if (id != 0) {
            Parent root;
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("mainApplication.fxml"));
            Stage stage = new Stage();
            stage.initOwner(loginPane.getScene().getWindow());
            stage.setTitle("Lagani");
            stage.setMaximized(true);
            root = fxmlLoader.load();
            stage.setScene(new Scene(root, 1200,640));
            Scene scene = loginButton.getScene();
            if (scene != null) {
                Window window = scene.getWindow();
                if (window != null) {
                    window.hide();
                }
            }
            MainApplicationController mainApplicationController = fxmlLoader.getController();
            mainApplicationController.initialize();
            stage.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(ApplicationConstants.WARNING_DIALOG);
            alert.setContentText("Username or Password does not match");
            alert.showAndWait();
        }
        return id;
    }

    /**
     * sets id for accessing in main application controller
     * @return
     */
    public static int id() {
        return id;
    }
}