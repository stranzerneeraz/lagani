package modal;

public class Items {
    private int itemID;
    private String type;
    private String startDate;
    private int principal;
    private Double rate;
    private String description;
    private String status;
    private String createdAt;
    private String updatedAt;
    private String closerName;
    private int totalAmount;
    private int closingAmount;
    private int customerIdFk;
    private boolean isActive;
    private String deadline;
    private String closingDate;

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public int getPrincipal() {
        return principal;
    }

    public void setPrincipal(int principal) {
        this.principal = principal;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCloserName() {
        return closerName;
    }

    public void setCloserName(String closerName) {
        this.closerName = closerName;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getClosingAmount() {
        return closingAmount;
    }

    public void setClosingAmount(int closingAmount) {
        this.closingAmount = closingAmount;
    }

    public String getDeadline() {
        return deadline;
    }

    public String getClosingDate() {
        return closingDate;
    }

    public void setClosingDate(String closingDate) {
        this.closingDate = closingDate;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public int getCustomerIdFk() {
        return customerIdFk;
    }

    public void setCustomerIdFk(int customerIdFk) {
        this.customerIdFk = customerIdFk;
    }

    @Override
    public String toString() {
        return "Items{" +
                "itemID=" + itemID +
                ", type='" + type + '\'' +
                ", startDate='" + startDate + '\'' +
                ", principal=" + principal +
                ", rate=" + rate +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", closerName='" + closerName + '\'' +
                ", totalAmount=" + totalAmount +
                ", closingAmount=" + closingAmount +
                ", customerIdFk=" + customerIdFk +
                ", isActive=" + isActive +
                ", deadline='" + deadline + '\'' +
                ", closingDate='" + closingDate + '\'' +
                '}';
    }
}
