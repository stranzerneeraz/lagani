package sample;

import exception.BusinessException;
import implementation.BusinessImplementation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    private ListView<Installment> calculationListView;
    @FXML
    private Label rateFieldI;
    @FXML
    private Label rateFieldC;
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
    private Label calculationInstallment;
    @FXML
    private Label calculationGrandTotal;

    private ObservableList<Installment> observableInstallmentList = FXCollections.observableArrayList();
    private ArrayList<Installment> installmentList;
    private double rate;
    private int totalInstallmentAmount = 0;

    BusinessImplementation businessImplementation=new BusinessImplementation();

    public void initialize(Items items) throws BusinessException {
        rate = Double.valueOf(items.getRate());
        rateFieldI.setText("@" + rate);
        rateFieldC.setText("@" + rate);
        updateInstallmentListView(items);
        updateCalculationSection(items);
    }

    public void updateInstallmentListView(Items items) throws BusinessException {
        loadInstallmentData(Integer.valueOf(items.getItemID()));;
        for (Installment installment: installmentList) {
            observableInstallmentList.add(installment);
        }
        calculationListView.getItems().addAll(observableInstallmentList);
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
                            setText("Amount " + "\t\t\t\t\t\t\t" + installment.getDepositAmount() + "\n" +
                                    "Depositor " + "\t\t\t\t\t\t" + installment.getDepositor() + "\n" +
                                    "Start Date " + "\t\t\t\t\t\t" + installment.getDate() + "\n" +
                                    "End Date " + "\t\t\t\t\t\t\t" + installment.getEndDate() + "\n" +
                                    "Duration " + "\t\t\t\t\t\t\t" + installment.getDuration() + "\n" +
                                    "Total Interest " + "\t\t\t\t\t\t" + installment.getTotalInterest() + "\n" +
                                    "Total Amount " + "\t\t\t\t\t\t" + installment.getTotalAmount());
                        }
                    }
                };
                return cell;
            }
        });
    }

    public void loadInstallmentData(int itemId) throws BusinessException {
        installmentList = businessImplementation.getInstallmentData(itemId);
        for(Installment installment: installmentList){
            installment.setEndDate(LocalDate.now().toString());
            installment.setDuration(calculateDurationInMonths(installment.getDate()));
            installment.setTotalInterest(calculateInterestAmount(installment.getDuration(), rate, installment.getDepositAmount()));
            installment.setTotalAmount(installment.getTotalInterest() + installment.getDepositAmount());
            totalInstallmentAmount += installment.getTotalAmount();
        }
    }

    public void updateCalculationSection(Items items) {
        int principal = Integer.valueOf(items.getPrincipal());
        int duration = calculateDurationInMonths(Date.valueOf(items.getStartDate()));
        int interest = calculateInterestAmount(duration, rate, principal);
        int totalAmount = principal + interest;
        int grandTotal = totalAmount - totalInstallmentAmount;
        calculationPrincipal.setText("" + principal);
        calculationStartDate.setText("" + items.getStartDate());
        calculationEndDate.setText(LocalDate.now().toString());
        calculationDuration.setText("" + duration);
        calculationInterest.setText("" + interest);
        calculationTotalAmount.setText("" + totalAmount);
        calculationInstallment.setText("" + totalInstallmentAmount);
        calculationGrandTotal.setText("" + grandTotal);
    }

    public int calculateDurationInMonths(Date fromDate){
        long monthsBetween = ChronoUnit.MONTHS.between(
                LocalDate.parse(fromDate.toString()).withDayOfMonth(1),
                LocalDate.parse(new Date(System.currentTimeMillis()).toString()).withDayOfMonth(1));
        return (int) monthsBetween;
    }

    public int calculateInterestAmount(int noOfMonths, double rate, int amount){
        int months = noOfMonths;
        int totalAmount = amount;
        while(months>12){
            totalAmount+=(totalAmount * rate * 12)/100;
            months-=12;
        }
        if(months>0){
            totalAmount+=(totalAmount*rate*months)/100;
        }
        return totalAmount-amount;
    }
}
