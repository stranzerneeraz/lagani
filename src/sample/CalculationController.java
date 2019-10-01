package sample;

import exception.BusinessException;
import implementation.BusinessImplementation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;
import modal.Installment;
import modal.Items;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static java.time.temporal.ChronoUnit.DAYS;

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
    @FXML
    private Button btnCompletePayment;

    private BusinessImplementation businessImplementation=new BusinessImplementation();
    private ObservableList<Installment> observableInstallmentList = FXCollections.observableArrayList();
    private ArrayList<Installment> installmentList;
    private double rate;
    private int totalInstallmentAmount = 0;

    /**
     * gets item to be displayed in calculate section
     * @param items
     * @throws BusinessException
     */
    public void initialize(Items items) throws BusinessException {
        rate = Double.parseDouble(items.getRate());
        rateFieldI.setText("@" + rate);
        rateFieldC.setText("@" + rate);
        updateInstallmentListView(items);
        updateCalculationSection(items);
    }

    /**
     * gets item to be displayed in installment column in calculate section
     * @param items
     * @throws BusinessException
     */
    private void updateInstallmentListView(Items items) throws BusinessException {
        loadInstallmentData(Integer.parseInt(items.getItemID()));;
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

    /**
     * gets specific data
     * passes data for calculating duration and interest
     * @param itemId
     * @throws BusinessException
     */
    private void loadInstallmentData(int itemId) throws BusinessException {
        installmentList = businessImplementation.getInstallmentData(itemId);
        for(Installment installment: installmentList){
            installment.setEndDate(LocalDate.now().toString());
            installment.setDuration(calculateDurationInMonths(installment.getDate(), Date.valueOf(LocalDate.now())));
            installment.setTotalInterest(calculateInterestAmount(installment.getDuration(), rate, installment.getDepositAmount()));
            installment.setTotalAmount(installment.getTotalInterest() + installment.getDepositAmount());
            totalInstallmentAmount += installment.getTotalAmount();
        }
    }

    /**
     * gets item data to display in calculate column in calculate section
     * @param items
     */
    private void updateCalculationSection(Items items) {
        int principal = Integer.parseInt(items.getPrincipal());
        double duration;
        calculationPrincipal.setText("" + principal);
        calculationStartDate.setText("" + items.getStartDate());
        if (items.getIsActive() == 0) {
            calculationEndDate.setText(items.getClosingDate());
            duration = calculateDurationInMonths(items.getStartDate(), Date.valueOf(calculationEndDate.getText()));
        } else {
            calculationEndDate.setText(LocalDate.now().toString());
            duration = calculateDurationInMonths(items.getStartDate(), Date.valueOf(calculationEndDate.getText()));
        }
        int interest = calculateInterestAmount(duration, rate, principal);
        int totalAmount = principal + interest;
        int grandTotal = totalAmount - totalInstallmentAmount;
        calculationDuration.setText("" + duration);
        calculationInterest.setText("" + interest);
        calculationTotalAmount.setText("" + totalAmount);
        calculationInstallment.setText("" + totalInstallmentAmount);
        calculationGrandTotal.setText("" + grandTotal);
        if (items.getIsActive() == 1) {
            btnCompletePayment.setOnAction(event -> {
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Complete Payment");
                dialog.setHeaderText("Closer Name");
                dialog.setContentText("Please enter closer name:");
                Optional<String> result = dialog.showAndWait();
                result.ifPresent(name -> {
                    try {
                        businessImplementation.closeCustomerItem(name, grandTotal, Integer.parseInt(items.getItemID()));
                    } catch (BusinessException e) {
                        e.printStackTrace();
                    }
                });
            });
        } else {
            btnCompletePayment.setDisable(true);
        }
    }

    /**
     * calculates duration of loan
     * @param fromDate
     * @param endDate
     * @return
     */
    private double calculateDurationInMonths(Date fromDate, Date endDate){
        double months=0;
        long days = DAYS.between(LocalDate.parse(fromDate.toString()), LocalDate.parse(endDate.toString()));
        if(days<15){
            months = 0.5;
        }else{
            months = (int) days/30;
            int monthsRem = (int) days%30;
            months = monthsRem<=15?months+0.5:months+1;
        }
        return months;
    }

    /**
     * calculates total interest
     * @param noOfMonths
     * @param rate
     * @param amount
     * @return
     */
    private int calculateInterestAmount(double noOfMonths, double rate, int amount){
        double months = noOfMonths;
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
