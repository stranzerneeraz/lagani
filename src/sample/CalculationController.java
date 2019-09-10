package sample;

import exception.BusinessException;
import implementation.BusinessImplementation;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import modal.Installment;
import modal.Items;

import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class CalculationController {
    @FXML
    private ListView calculationListView;
    @FXML
    private Label calculationPrincipal;
    @FXML
    private Label calculationStartDate;
    @FXML
    private Label calculationEndDate;
    @FXML
    private Label calculationDuration;
    @FXML
    private Label calculationInterest;
    @FXML
    private Label calculationTotalAmount;
    @FXML
    private Label calculationInvestment;
    @FXML
    private Label calculationGrandTotal;

    ArrayList<Installment> installmentList;
    private double rate;

    public void initialize(Items items) throws BusinessException {
        System.out.println(items.toString());
        loadInstallmentData(items.getItemID());
        ArrayList<Installment> arrayList = null;
        rate = items.getRate();
        for (Installment installment : installmentList) {
            arrayList.add(installment);
        }
        calculationListView.getItems().add(arrayList);
            calculationListView.setCellFactory(new Callback<ListView<Installment>, ListCell<Installment>>() {
                @Override
                public ListCell<Installment> call(ListView<Installment> param) {
                    ListCell<Installment> cell = new ListCell<Installment>() {
                        @Override
                        protected void updateItem(Installment installment, boolean empty) {
                            super.updateItem(installment, empty);
                            if (empty || installment == null) {
                                setText("");
                            } else {
                                setText(installment.getDepositAmount() + ", fsdfa" );
                            }
                        }
                    };
                    return cell;
                }
            });


    }

    public void loadInstallmentData(int itemId) throws BusinessException {
        BusinessImplementation businessImplementation=new BusinessImplementation();
         installmentList = businessImplementation.getInstallmentData(itemId);
        for(Installment installment: installmentList){
            installment.setEndDate(LocalDate.now().toString());
            installment.setDuration(calculateDurationInMonths(installment.getDate()));
            installment.setTotalInterest(calculateInterestAmount(installment.getDuration(), rate, installment.getDepositAmount()));
            installment.setTotalAmount(installment.getTotalInterest() + installment.getDepositAmount());
        }
    }

    public int calculateDurationInMonths(Date fromDate){
        long monthsBetween = ChronoUnit.MONTHS.between(
                LocalDate.parse(fromDate.toString()).withDayOfMonth(1),
                LocalDate.parse(new Date(System.currentTimeMillis()).toString()).withDayOfMonth(1));
        return (int) monthsBetween;
    }

    public int calculateInterestAmount(int noOfMonths, double rate, int amount){
        int months = noOfMonths;
        int totalamount = amount;
        while(months>12){
            totalamount+=(totalamount * rate * 12)/100;
            months-=12;
        }
        if(months>0){
            totalamount+=(totalamount*rate*months)/100;
        }
        return totalamount-amount;
    }
}
