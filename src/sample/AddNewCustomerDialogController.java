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
    private Button btnAddNewCustomer;
    private Alert alert = new Alert(Alert.AlertType.WARNING);

    /**
     * gets input from add customer dialog
     * verifies the input field
     * close the window on adding customer
     */
    public void initialize() {
        alert.setTitle(ApplicationConstants.WARNING_DIALOG);
        btnAddNewCustomer.setOnAction(event -> {
            if (fullNameField.getText().length() <= 0) {
                fullNameField.setStyle(ApplicationConstants.ERROR_ENTRY);
                alert.setContentText("Enter full name");
                alert.showAndWait();
            } else if (addressField.getText().length() <= 0) {
                addressField.setStyle(ApplicationConstants.ERROR_ENTRY);
                alert.setContentText("Enter address");
                alert.showAndWait();
            } else if (wardNoField.getText().length() > 0 && !wardNoField.getText().matches(ApplicationConstants.WARD_ID_VALIDATION_REGEX)) {
                wardNoField.setStyle(ApplicationConstants.ERROR_ENTRY);
                alert.setContentText("Enter valid ward number");
                alert.showAndWait();
            } else if (contactNumberField.getText().length() > 0 &&!contactNumberField.getText().matches(ApplicationConstants.CONTACT_NUMBER_VALIDATION_REGEX)) {
                contactNumberField.setStyle(ApplicationConstants.ERROR_ENTRY);
                alert.setContentText("Enter valid contact number");
                alert.showAndWait();
            } else {
                fullNameField.setStyle(ApplicationConstants.CORRECT_ENTRY);
                addressField.setStyle(ApplicationConstants.CORRECT_ENTRY);
                wardNoField.setStyle(ApplicationConstants.CORRECT_ENTRY);
                contactNumberField.setStyle(ApplicationConstants.CORRECT_ENTRY);
                try {
                    addNewCustomer();
                    Scene scene = btnAddNewCustomer.getScene();
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

    /**
     * adds customer data to database
     * @throws BusinessException
     */
    private void addNewCustomer() throws BusinessException {
        BusinessImplementation businessImplementation = new BusinessImplementation();
        Customers customers = new Customers();
        customers.setFullName(fullNameField.getText());
        customers.setAddress(addressField.getText());
        if (wardNoField.getText().length()>0) {
            customers.setWard(Integer.parseInt(wardNoField.getText()));
        } else {
            customers.setWard(0);
        }
        customers.setFatherName(fatherNameField.getText());
        customers.setSpouseName(spouseNameField.getText());
        if (contactNumberField.getText().length() > 0) {
            customers.setContactNo(Long.parseLong(contactNumberField.getText()));
        } else {
            customers.setContactNo(0);
        }
        customers.setRemarks(remarksField.getText());
        businessImplementation.addNewCustomer(customers);
    }
}
