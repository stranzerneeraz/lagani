package sample;

import exception.BusinessException;
import implementation.BusinessImplementation;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Window;
import modal.Customers;

public class AddNewCustomerDialogController {
    @FXML
    private TextField fullNameField;
    @FXML
    private TextField addressField;
    @FXML
    private TextField wardNoField;
    @FXML
    private TextField fatherNameField;
    @FXML
    private TextField spouseNameField;
    @FXML
    private TextField contactNumberField;
    @FXML
    private TextField remarksField;
    @FXML
    private Button btnUpdateCustomer;

    private Alert alert = new Alert(Alert.AlertType.WARNING);
    public int customerCheck;

    public int initialize() {
        alert.setTitle("Warning Dialog");
        btnUpdateCustomer.setOnAction(event -> {
            if (fullNameField.getText().length() <= 0) {
                fullNameField.setStyle("-fx-text-fill: red;");
                alert.setContentText("Enter full name");
                alert.showAndWait();
            } else if (addressField.getText().length() <= 0) {
                addressField.setStyle("-fx-text-fill: red;");
                alert.setContentText("Enter address");
                alert.showAndWait();
            } else if (!wardNoField.getText().matches("[0-9]*[0-9]+")) {
                wardNoField.setStyle("-fx-text-fill: red;");
                alert.setContentText("Enter ward number in integer");
                alert.showAndWait();
            } else if (fatherNameField.getText().length() <= 0) {
                fatherNameField.setStyle("-fx-text-fill: red;");
                alert.setContentText("Enter father's name");
                alert.showAndWait();
            } else if (spouseNameField.getText().length() <= 0) {
                spouseNameField.setStyle("-fx-text-fill: red;");
                alert.setContentText("Enter spouse's name");
                alert.showAndWait();
            } else if (!contactNumberField.getText().matches("^[0-9]{10}$")) {
                contactNumberField.setStyle("-fx-text-fill: red;");
                alert.setContentText("Enter contact number");
                alert.showAndWait();
            } else if (remarksField.getText().length() <= 0) {
                remarksField.setStyle("-fx-text-fill: red;");
                alert.setContentText("Enter remarks");
                alert.showAndWait();
            } else {
                fullNameField.setStyle("-fx-text-fill: black;");
                addressField.setStyle("-fx-text-fill: black;");
                wardNoField.setStyle("-fx-text-fill: black;");
                fatherNameField.setStyle("-fx-text-fill: black;");
                spouseNameField.setStyle("-fx-text-fill: black;");
                contactNumberField.setStyle("-fx-text-fill: black;");
                remarksField.setStyle("-fx-text-fill: black;");
                try {
                    addNewCustomer();
                    Scene scene = btnUpdateCustomer.getScene();
                    if (scene != null) {
                        Window window = scene.getWindow();
                        if (window != null) {
                            customerCheck = 1;
                            window.hide();
                        }
                    }
                } catch (BusinessException e) {
                    e.printStackTrace();
                }
            }
        });
        return customerCheck;
    }

    public int addNewCustomer() throws BusinessException {
        BusinessImplementation businessImplementation = new BusinessImplementation();
        Customers customers = new Customers();
        customers.setFullName(fullNameField.getText());
        customers.setAddress(addressField.getText());
        customers.setWard(Integer.parseInt(wardNoField.getText()));
        customers.setFatherName(fatherNameField.getText());
        customers.setSpouseName(spouseNameField.getText());
        customers.setContactNo(Long.parseLong(contactNumberField.getText()));
        customers.setRemarks(remarksField.getText());
        customerCheck = 1;
        businessImplementation.addNewCustomer(customers);
        return customerCheck;
    }
}
