package modal;

public class Installment {
    private int installmentID;
    private String depositor;
    private int depositAmount;
    private String date;

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
