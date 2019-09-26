package sample;

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
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

public class LoginController {
    @FXML
    public TextField username;
    @FXML
    public PasswordField password;
    @FXML
    private Button loginButton;
    public static int id;

    public int checkUser() throws BusinessException {
        BusinessImplementation businessImplementation = new BusinessImplementation();
        id = businessImplementation.authenticateUser(username.getText(), password.getText());
        if (id != 0) {
            Parent root;
            try {
                root = FXMLLoader.load(getClass().getResource("mainApplication.fxml"));
                Stage stage = new Stage();
                stage.setTitle("Lagani");
                stage.setMaximized(true);
                stage.setScene(new Scene(root, 1200,640));
                stage.show();

                Scene scene = loginButton.getScene();
                if (scene != null) {
                    Window window = scene.getWindow();
                    if (window != null) {
                        window.hide();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning alert");
            alert.setContentText("Username or Password does not match");
            alert.showAndWait();
        }
        return id;
    }

    public static int id() {
        return id;
    }
}