package sample;

import constants.ApplicationConstants;
import exception.BusinessException;
import implementation.BusinessImplementation;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.Window;
import modal.Customers;
import modal.Items;

import java.sql.Date;
import java.time.LocalDate;

public class AddNewItemDialogController {
    @FXML
    private Label dialogHeaderLabel;
    @FXML
    private TextField amountField;
    @FXML
    private ComboBox typeChooser;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private TextField rateField;
    @FXML
    private DatePicker deadlinePicker;
    @FXML
    private TextArea descriptionArea;
    @FXML
    private Button btnUpdateNewItem;
    private Customers customers;

    public void updateDialogBox(Customers selectionCustomer) {
        customers = selectionCustomer;
        dialogHeaderLabel.setText("Add New Item for " + selectionCustomer.getFullName());
        startDatePicker.setValue(LocalDate.now());
        rateField.setText("2.5");
        deadlinePicker.setValue(LocalDate.now().plusYears(1));
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(ApplicationConstants.WARNING_DIALOG);

        btnUpdateNewItem.setOnAction(event -> {
            String amountString = amountField.getText();
            LocalDate startDate = startDatePicker.getValue();
            String rateString = rateField.getText();
            LocalDate deadline = deadlinePicker.getValue();
            String description = descriptionArea.getText();

            if (!amountString.matches(ApplicationConstants.NUMBER_VALIDATION_REGEX)) {
                if (Integer.parseInt(amountString) > 2_000_000_000) {
                    amountField.setStyle(ApplicationConstants.ERROR_ENTRY);
                    alert.setContentText("Input fields not valid");
                    alert.showAndWait();
                }
            } else if (!(startDate.compareTo(LocalDate.now()) <= 0)) {
                startDatePicker.setStyle(ApplicationConstants.ERROR_ENTRY);
                alert.setContentText("Date field should be upto today");
                alert.showAndWait();
            } else if (!rateString.matches(ApplicationConstants.NUMBER_VALIDATION_REGEX)) {
                rateField.setStyle(ApplicationConstants.ERROR_ENTRY);
                alert.setContentText("Rate should be in decimal");
                alert.showAndWait();
            } else if (!(deadline.compareTo(LocalDate.now()) > 0)) {
                deadlinePicker.setStyle(ApplicationConstants.ERROR_ENTRY);
                alert.setContentText("Deadline should be in future");
                alert.showAndWait();
            } else if (description.length() <= 0) {
                descriptionArea.setStyle(ApplicationConstants.ERROR_ENTRY);
                alert.setContentText("Enter description");
                alert.showAndWait();
            } else {
                amountField.setStyle(ApplicationConstants.CORRECT_ENTRY);
                startDatePicker.setStyle(ApplicationConstants.CORRECT_ENTRY);
                rateField.setStyle(ApplicationConstants.CORRECT_ENTRY);
                deadlinePicker.setStyle(ApplicationConstants.CORRECT_ENTRY);
                descriptionArea.setStyle(ApplicationConstants.CORRECT_ENTRY);
                try {
                    addNewItem();
                        Scene scene = btnUpdateNewItem.getScene();
                        if (scene != null) {
                            Window window = scene.getWindow();
                            if (window != null) {
                                Stage stage = (Stage) window;
                                stage.close();
                            }
                        }
                } catch (BusinessException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void addNewItem() throws BusinessException {
        int customerID = customers.getCustomerID();
        BusinessImplementation businessImplementation = new BusinessImplementation();
        Items items = new Items();
        items.setPrincipal(amountField.getText());
        items.setType(typeChooser.getValue().toString());
        items.setStartDate(Date.valueOf(startDatePicker.getValue()));
        items.setRate(rateField.getText());
        items.setDeadline(Date.valueOf(deadlinePicker.getValue()));
        items.setDescription(descriptionArea.getText());
        businessImplementation.addNewCustomerItem(items, customerID);
    }
}