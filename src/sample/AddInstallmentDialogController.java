package sample;

import constants.ApplicationConstants;
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
        alert.setTitle(ApplicationConstants.WARNING_DIALOG);

        btnAddInstallment.setOnAction(event -> {
            String installmentString = installmentAmount.getText();
            String depositor = depositorName.getText();
            LocalDate date = depositDate.getValue();
            if (!installmentString.matches(ApplicationConstants.NUMBER_VALIDATION_REGEX)) {
                installmentAmount.setStyle(ApplicationConstants.ERROR_ENTRY);
                alert.setContentText("Input fields not valid");
                alert.showAndWait();
            } else if (depositor.length() <= 0) {
                depositorName.setStyle(ApplicationConstants.ERROR_ENTRY);
                alert.setContentText("Enter Depositor Name");
                alert.showAndWait();
            } else if (!(date.compareTo(LocalDate.now()) <= 0)) {
                depositDate.setStyle(ApplicationConstants.ERROR_ENTRY);
                alert.setContentText("Date field should be upto today");
                alert.showAndWait();
            } else {
                installmentAmount.setStyle(ApplicationConstants.CORRECT_ENTRY);
                depositorName.setStyle(ApplicationConstants.CORRECT_ENTRY);
                depositDate.setStyle(ApplicationConstants.CORRECT_ENTRY);
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
        int itemID = Integer.parseInt(items.getItemID());
        BusinessImplementation businessImplementation = new BusinessImplementation();
        Installment installment = new Installment();
        installment.setDepositAmount(Integer.parseInt(installmentAmount.getText()));
        installment.setDepositor(depositorName.getText());
        installment.setDate(Date.valueOf(depositDate.getValue()));
        businessImplementation.addInstallment(installment, itemID);
    }
}
