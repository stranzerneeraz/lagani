package sample;

import constants.ApplicationConstants;
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

    public void initialize() {
        alert.setTitle(ApplicationConstants.WARNING_DIALOG);
        btnUpdateCustomer.setOnAction(event -> {
            if (fullNameField.getText().length() <= 0) {
                fullNameField.setStyle(ApplicationConstants.ERROR_ENTRY);
                alert.setContentText("Enter full name");
                alert.showAndWait();
            } else if (addressField.getText().length() <= 0) {
                addressField.setStyle(ApplicationConstants.ERROR_ENTRY);
                alert.setContentText("Enter address");
                alert.showAndWait();
            } else if (!wardNoField.getText().matches(ApplicationConstants.WARD_ID_VALIDATION_REGEX)) {
                wardNoField.setStyle(ApplicationConstants.ERROR_ENTRY);
                alert.setContentText("Enter ward number");
                alert.showAndWait();
            } else if (fatherNameField.getText().length() <= 0) {
                fatherNameField.setStyle(ApplicationConstants.ERROR_ENTRY);
                alert.setContentText("Enter father's name");
                alert.showAndWait();
            } else if (spouseNameField.getText().length() <= 0) {
                spouseNameField.setStyle(ApplicationConstants.ERROR_ENTRY);
                alert.setContentText("Enter spouse's name");
                alert.showAndWait();
            } else if (!contactNumberField.getText().matches(ApplicationConstants.CONTACT_NUMBER_VALIDATION_REGEX)) {
                contactNumberField.setStyle(ApplicationConstants.ERROR_ENTRY);
                alert.setContentText("Enter contact number");
                alert.showAndWait();
            } else if (remarksField.getText().length() <= 0) {
                remarksField.setStyle(ApplicationConstants.ERROR_ENTRY);
                alert.setContentText("Enter remarks");
                alert.showAndWait();
            } else {
                fullNameField.setStyle(ApplicationConstants.CORRECT_ENTRY);
                addressField.setStyle(ApplicationConstants.CORRECT_ENTRY);
                wardNoField.setStyle(ApplicationConstants.CORRECT_ENTRY);
                fatherNameField.setStyle(ApplicationConstants.CORRECT_ENTRY);
                spouseNameField.setStyle(ApplicationConstants.CORRECT_ENTRY);
                contactNumberField.setStyle(ApplicationConstants.CORRECT_ENTRY);
                remarksField.setStyle(ApplicationConstants.CORRECT_ENTRY);

                try {
                    addNewCustomer();
                    Scene scene = btnUpdateCustomer.getScene();
                    if (scene != null) {
                        Window window = scene.getWindow();
                        if (window != null) {
                            window.hide();
                        }
                    }
                } catch (BusinessException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void addNewCustomer() throws BusinessException {
        BusinessImplementation businessImplementation = new BusinessImplementation();
        Customers customers = new Customers();
        customers.setFullName(fullNameField.getText());
        customers.setAddress(addressField.getText());
        customers.setWard(Integer.parseInt(wardNoField.getText()));
        customers.setFatherName(fatherNameField.getText());
        customers.setSpouseName(spouseNameField.getText());
        customers.setContactNo(Long.parseLong(contactNumberField.getText()));
        customers.setRemarks(remarksField.getText());
        businessImplementation.addNewCustomer(customers);
    }
}
