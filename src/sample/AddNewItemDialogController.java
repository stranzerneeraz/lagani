package sample;

import exception.BusinessException;
import implementation.BusinessImplementation;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
        alert.setTitle("Warning Dialog");

        btnUpdateNewItem.setOnAction(event -> {
            String amountString = amountField.getText();
            String itemType = typeChooser.getValue().toString();
            LocalDate startDate = startDatePicker.getValue();
            String rateString = rateField.getText();
            LocalDate deadline = deadlinePicker.getValue();
            String description = descriptionArea.getText();

            if (!amountString.matches("^[0-9]+(\\.[0-9]+)?$")) {
                amountField.setStyle("-fx-text-fill: red;");
                alert.setContentText("Input fields not valid");
                alert.showAndWait();
            } else if (!(startDate.compareTo(LocalDate.now()) <= 0)) {
                startDatePicker.setStyle("-fx-text-fill: red;");
                alert.setContentText("Date field should be upto today");
                alert.showAndWait();
            } else if (!rateString.matches("^[0-9](\\.[0-9]+)?$")) {
                rateField.setStyle("-fx-text-fill: red;");
                alert.setContentText("Rate should be in decimal");
                alert.showAndWait();
            } else if (!(deadline.compareTo(LocalDate.now()) > 0)) {
                deadlinePicker.setStyle("-fx-text-fill: red;");
                alert.setContentText("Deadline should be in future");
                alert.showAndWait();
            } else if (description.length() <= 0) {
                descriptionArea.setStyle("-fx-text-fill: red;");
                alert.setContentText("Enter description");
                alert.showAndWait();
            } else {
                amountField.setStyle("-fx-text-fill: black;");
                startDatePicker.setStyle("-fx-text-fill: black;");
                rateField.setStyle("-fx-text-fill: black;");
                deadlinePicker.setStyle("-fx-text-fill: black;");
                descriptionArea.setStyle("-fx-text-fill: black;");
                try {
                    addNewItem();
                    Scene scene = btnUpdateNewItem.getScene();
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

    public void addNewItem() throws BusinessException {
        int customerID = customers.getCustomerID();
        BusinessImplementation businessImplementation = new BusinessImplementation();
        Items items = new Items();
        items.setPrincipal(Integer.parseInt(amountField.getText()));
        items.setType(typeChooser.getValue().toString());
        items.setStartDate(Date.valueOf(startDatePicker.getValue()));
        items.setRate(Double.parseDouble(rateField.getText()));
        items.setDeadline(Date.valueOf(deadlinePicker.getValue()));
        items.setDescription(descriptionArea.getText());
        businessImplementation.addNewCustomerItem(items, customerID);
    }
}