package modal;

import java.sql.Date;

public class Installment {
    private int installmentID;
    private String depositor;
    private int depositAmount;
    private Date date;
    private String endDate;
    private int duration;
    private int totalInterest;
    private int totalAmount;
    private int itemIdFk;

    public int getInstallmentID() {
        return installmentID;
    }

    public void setInstallmentID(int installmentID) {
        this.installmentID = installmentID;
    }

    public String getDepositor() {
        return depositor;
    }

    public void setDepositor(String depositor) {
        this.depositor = depositor;
    }

    public int getDepositAmount() {
        return depositAmount;
    }

    public void setDepositAmount(int depositAmount) {
        this.depositAmount = depositAmount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getTotalInterest() {
        return totalInterest;
    }

    public void setTotalInterest(int totalInterest) {
        this.totalInterest = totalInterest;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getItemIdFk() {
        return itemIdFk;
    }

    public void setItemIdFk(int itemIdFk) {
        this.itemIdFk = itemIdFk;
    }

    @Override
    public String toString() {
        return "Installment{" +
                "installmentID=" + installmentID +
                ", depositor='" + depositor + '\'' +
                ", depositAmount=" + depositAmount +
                ", date=" + date +
                ", endDate='" + endDate + '\'' +
                ", duration=" + duration +
                ", totalInterest=" + totalInterest +
                ", totalAmount=" + totalAmount +
                ", itemIdFk=" + itemIdFk +
                '}';
    }
}
