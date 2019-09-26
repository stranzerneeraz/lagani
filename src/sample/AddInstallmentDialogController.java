package sample;

import exception.BusinessException;
import implementation.BusinessImplementation;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Window;
import modal.Installment;
import modal.Items;

import java.sql.Date;
import java.time.LocalDate;

public class AddInstallmentDialogController {
    @FXML
    private TextField installmentAmount;
    @FXML
    private TextField depositorName;
    @FXML
    private DatePicker depositDate;
    @FXML
    private Button btnAddInstallment;
    private Items items;

    public void initialize(Items selectedItem) {
        items = selectedItem;
        depositDate.setValue(LocalDate.now());
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning Dialog");

        btnAddInstallment.setOnAction(event -> {
            String installmentString = installmentAmount.getText();
            String depositor = depositorName.getText();
            LocalDate date = depositDate.getValue();
            if (!installmentString.matches("^[0-9]+(\\.[0-9]+)?$")) {
                installmentAmount.setStyle("-fx-text-fill: red;");
                alert.setContentText("Input fields not valid");
                alert.showAndWait();
            } else if (depositor.length() <= 0) {
                depositorName.setStyle("-fx-text-fill: red;");
                alert.setContentText("Enter Depositor Name");
                alert.showAndWait();
            } else if (!(date.compareTo(LocalDate.now()) <= 0)) {
                depositDate.setStyle("-fx-text-fill: red;");
                alert.setContentText("Date field should be upto today");
                alert.showAndWait();
            } else {
                installmentAmount.setStyle("-fx-text-fill: black;");
                depositorName.setStyle("-fx-text-fill: black;");
                depositDate.setStyle("-fx-text-fill: black;");
                try {
                    addInstallment();
                } catch (BusinessException e) {
                    e.printStackTrace();
                }
                Scene scene = btnAddInstallment.getScene();
                if (scene != null) {
                    Window window = scene.getWindow();
                    if (window != null) {
                        window.hide();
                    }
                }
            }
        });
    }

    public void addInstallment() throws BusinessException {
        int itemID = Integer.valueOf(items.getItemID());
        BusinessImplementation businessImplementation = new BusinessImplementation();
        Installment installment = new Installment();
        installment.setDepositAmount(Integer.parseInt(installmentAmount.getText()));
        installment.setDepositor(depositorName.getText());
        installment.setDate(Date.valueOf(depositDate.getValue()));
        businessImplementation.addInstallment(installment, itemID);
    }
}
