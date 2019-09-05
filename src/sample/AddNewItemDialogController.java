package sample;

import exception.BusinessException;
import implementation.BusinessImplementation;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.SQLException;

public class AddNewItemDialogController {
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

    public void addNewItem() throws BusinessException, SQLException {
        BusinessImplementation businessImplementation = new BusinessImplementation();

        if (!amountField.getText().matches("[0-9]")){

        } else if (typeChooser.getSelectionModel().isEmpty()) {

        } else if (startDatePicker.getValue() == null) {

        } else if (rateField.getText().matches("[0.0-9.9]")) {

        } else if (deadlinePicker.getValue() == null) {

        } else if (descriptionArea.getText() == null) {

        } else {
            businessImplementation.addNewCustomerItem(amountField.getText(), typeChooser.getItems(), startDatePicker.getValue(), rateField.getText(), deadlinePicker.getValue(), descriptionArea.getText());
        }
    }
}
