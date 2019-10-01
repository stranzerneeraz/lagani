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
    Alert alert = new Alert(Alert.AlertType.WARNING);
   private Items items;

    /**
     * gets input from add installment dialog
     * verifies the input field
     * close the window on adding installment
     * @param selectedItem
     */
    public void initialize(Items selectedItem) {
        alert.setTitle(ApplicationConstants.WARNING_DIALOG);
        depositDate.setValue(LocalDate.now());
        items = selectedItem;
        btnAddInstallment.setOnAction(event -> {
            String installmentString = installmentAmount.getText();
            String depositor = depositorName.getText();
            LocalDate date = depositDate.getValue();
            if (!installmentString.matches(ApplicationConstants.NUMBER_VALIDATION_REGEX)) {
                installmentAmount.setStyle(ApplicationConstants.ERROR_ENTRY);
                alert.setContentText("Enter valid installment amount");
                alert.showAndWait();
            } else if (depositor.length() <= 0) {
                depositorName.setStyle(ApplicationConstants.ERROR_ENTRY);
                alert.setContentText("Enter Depositor Name");
                alert.showAndWait();
            } else if (!(date.compareTo(LocalDate.now()) <= 0)) {
                depositDate.setStyle(ApplicationConstants.ERROR_ENTRY);
                alert.setContentText("Date should not be in future");
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

    /**
     *adds new installment to database
     * @throws BusinessException
     */
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
