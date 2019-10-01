package modal;

public class DashboardItem {
    private int customerID;
    private int amount;
    private String name;
    private String address;
    private double duration;
    private int totalInstallment;
    private int totalInterest;

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public int getTotalInstallment() {
        return totalInstallment;
    }

    public void setTotalInstallment(int totalInstallment) {
        this.totalInstallment = totalInstallment;
    }

    public int getTotalInterest() {
        return totalInterest;
    }

    public void setTotalInterest(int totalInterest) {
        this.totalInterest = totalInterest;
    }

    @Override
    public String toString() {
        return "DashboardItem{" +
                "customerID=" + customerID +
                ", amount=" + amount +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", duration=" + duration +
                ", totalInstallment=" + totalInstallment +
                ", totalInterest=" + totalInterest +
                '}';
    }
}
