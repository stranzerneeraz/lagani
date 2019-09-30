package modal;

import javafx.beans.property.SimpleStringProperty;

import java.sql.Date;

public class Items {
    private SimpleStringProperty itemID = new SimpleStringProperty("");
    private SimpleStringProperty type = new SimpleStringProperty("");
    private Date startDate;
    private SimpleStringProperty principal = new SimpleStringProperty("");
    private SimpleStringProperty rate = new SimpleStringProperty("");
    private SimpleStringProperty description = new SimpleStringProperty("");
    private SimpleStringProperty status = new SimpleStringProperty("");
    private SimpleStringProperty createdAt = new SimpleStringProperty("");
    private SimpleStringProperty updatedAt = new SimpleStringProperty("");
    private SimpleStringProperty closerName = new SimpleStringProperty("");
    private SimpleStringProperty totalAmount = new SimpleStringProperty("");
    private SimpleStringProperty closingAmount = new SimpleStringProperty("");
    private int isActive;
    private Date deadline;
    private SimpleStringProperty closingDate = new SimpleStringProperty("");

    public String getItemID() {
        return itemID.get();
    }

    public SimpleStringProperty itemIDProperty() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID.set(itemID);
    }

    public String getType() {
        return type.get();
    }

    public SimpleStringProperty typeProperty() {
        return type;
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getPrincipal() {
        return principal.get();
    }

    public SimpleStringProperty principalProperty() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal.set(principal);
    }

    public String getRate() {
        return rate.get();
    }

    public SimpleStringProperty rateProperty() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate.set(rate);
    }

    public String getDescription() {
        return description.get();
    }

    public SimpleStringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public String getStatus() {
        return status.get();
    }

    public SimpleStringProperty statusProperty() {
        return status;
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public String getCreatedAt() {
        return createdAt.get();
    }

    public SimpleStringProperty createdAtProperty() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt.set(createdAt);
    }

    public String getUpdatedAt() {
        return updatedAt.get();
    }

    public SimpleStringProperty updatedAtProperty() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt.set(updatedAt);
    }

    public String getCloserName() {
        return closerName.get();
    }

    public SimpleStringProperty closerNameProperty() {
        return closerName;
    }

    public void setCloserName(String closerName) {
        this.closerName.set(closerName);
    }

    public String getTotalAmount() {
        return totalAmount.get();
    }

    public SimpleStringProperty totalAmountProperty() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount.set(totalAmount);
    }

    public String getClosingAmount() {
        return closingAmount.get();
    }

    public SimpleStringProperty closingAmountProperty() {
        return closingAmount;
    }

    public void setClosingAmount(String closingAmount) {
        this.closingAmount.set(closingAmount);
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public String getClosingDate() {
        return closingDate.get();
    }

    public SimpleStringProperty closingDateProperty() {
        return closingDate;
    }

    public void setClosingDate(String closingDate) {
        this.closingDate.set(closingDate);
    }

    @Override
    public String toString() {
        return "Items{" +
                "itemID=" + itemID +
                ", type=" + type +
                ", startDate=" + startDate +
                ", principal=" + principal +
                ", rate=" + rate +
                ", description=" + description +
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", closerName=" + closerName +
                ", totalAmount=" + totalAmount +
                ", closingAmount=" + closingAmount +
                ", isActive=" + isActive +
                ", deadline=" + deadline +
                ", closingDate=" + closingDate +
                '}';
    }


    //    private int itemID;
//    private String type;
//    private Date startDate;
//    private int principal;
//    private Double rate;
//    private String description;
//    private String status;
//    private String createdAt;
//    private String updatedAt;
//    private String closerName;
//    private int totalAmount;
//    private int closingAmount;
//    private int customerIdFk;
//    private boolean isActive;
//    private Date deadline;
//    private String closingDate;
//
//    public int getItemID() {
//        return itemID;
//    }
//
//    public void setItemID(int itemID) {
//        this.itemID = itemID;
//    }
//
//    public String getType() {
//        return type;
//    }
//
//    public void setType(String type) {
//        this.type = type;
//    }
//
//    public Date getStartDate() {
//        return startDate;
//    }
//
//    public void setStartDate(Date startDate) {
//        this.startDate = startDate;
//    }
//
//    public int getPrincipal() {
//        return principal;
//    }
//
//    public void setPrincipal(int principal) {
//        this.principal = principal;
//    }
//
//    public Double getRate() {
//        return rate;
//    }
//
//    public void setRate(Double rate) {
//        this.rate = rate;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    public String getStatus() {
//        return status;
//    }
//
//    public void setStatus(String status) {
//        this.status = status;
//    }
//
//    public String getCreatedAt() {
//        return createdAt;
//    }
//
//    public void setCreatedAt(String createdAt) {
//        this.createdAt = createdAt;
//    }
//
//    public String getUpdatedAt() {
//        return updatedAt;
//    }
//
//    public void setUpdatedAt(String updatedAt) {
//        this.updatedAt = updatedAt;
//    }
//
//    public String getCloserName() {
//        return closerName;
//    }
//
//    public void setCloserName(String closerName) {
//        this.closerName = closerName;
//    }
//
//    public int getTotalAmount() {
//        return totalAmount;
//    }
//
//    public void setTotalAmount(int totalAmount) {
//        this.totalAmount = totalAmount;
//    }
//
//    public int getClosingAmount() {
//        return closingAmount;
//    }
//
//    public void setClosingAmount(int closingAmount) {
//        this.closingAmount = closingAmount;
//    }
//
//    public String getClosingDate() {
//        return closingDate;
//    }
//
//    public void setClosingDate(String closingDate) {
//        this.closingDate = closingDate;
//    }
//
//    public boolean isActive() {
//        return isActive;
//    }
//
//    public void setActive(boolean active) {
//        isActive = active;
//    }
//
//    public Date getDeadline() {
//        return deadline;
//    }
//
//    public void setDeadline(Date deadline) {
//        this.deadline = deadline;
//    }
//
//    public int getCustomerIdFk() {
//        return customerIdFk;
//    }
//
//    public void setCustomerIdFk(int customerIdFk) {
//        this.customerIdFk = customerIdFk;
//    }

//    @Override
//    public String toString() {
//        return "Items{" +
//                "itemID=" + itemID +
//                ", type='" + type + '\'' +
//                ", startDate=" + startDate +
//                ", principal=" + principal +
//                ", rate=" + rate +
//                ", description='" + description + '\'' +
//                ", status='" + status + '\'' +
//                ", createdAt='" + createdAt + '\'' +
//                ", updatedAt='" + updatedAt + '\'' +
//                ", closerName='" + closerName + '\'' +
//                ", totalAmount=" + totalAmount +
//                ", closingAmount=" + closingAmount +
//                ", customerIdFk=" + customerIdFk +
//                ", isActive=" + isActive +
//                ", deadline=" + deadline +
//                ", closingDate='" + closingDate + '\'' +
//                '}';
//    }
}
