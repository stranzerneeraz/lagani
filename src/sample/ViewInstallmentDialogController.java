package sample;

import exception.BusinessException;
import implementation.BusinessImplementation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import modal.Installment;
import modal.Items;

import java.util.ArrayList;

public class ViewInstallmentDialogController {
    @FXML
    private ListView installmentView;

    private ObservableList<Installment> observableInstallmentList = FXCollections.observableArrayList();
    private ArrayList<Installment> installmentList;
    BusinessImplementation businessImplementation = new BusinessImplementation();

    public void initialize(Items selectedItem) throws BusinessException {
        installmentList = businessImplementation.getInstallmentData(selectedItem.getItemID());
        for (Installment installment : installmentList) {
            observableInstallmentList.add(installment);
        }
        installmentView.getItems().addAll(observableInstallmentList);
        installmentView.setCellFactory(new Callback<ListView<Installment>, ListCell<Installment>>() {
            @Override
            public ListCell<Installment> call(ListView<Installment> param) {
                ListCell<Installment> cell = new ListCell<Installment>() {
                    @Override
                    protected void updateItem(Installment installment, boolean empty) {
                        super.updateItem(installment, empty);
                        if (empty || installment == null) {
                            setText("");
                        } else {
                            setText("Amount " + "\t\t\t\t\t\t\t" + installment.getDepositAmount() + "\n" +
                                    "Depositor " + "\t\t\t\t\t\t" + installment.getDepositor() + "\n" +
                                    "Start Date " + "\t\t\t\t\t\t" + installment.getDate() + "\n");
                        }
                    }
                };
                return cell;
            }
        });
    }
}
