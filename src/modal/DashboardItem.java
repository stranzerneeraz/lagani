package modal;

public class DashboardItem {
    private int customerID;
    private int amount;
    private String name;
    private String address;
    private int duration;
    private int totalInstallment;

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

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getTotalInstallment() {
        return totalInstallment;
    }

    public void setTotalInstallment(int totalInstallment) {
        this.totalInstallment = totalInstallment;
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
                '}';
    }
}
