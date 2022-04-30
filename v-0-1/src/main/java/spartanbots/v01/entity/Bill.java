package spartanbots.v01.entity;

import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Column;
import javax.persistence.Id;

/**
 * @author Rajat Masurkar
 */
@Document(collection ="bill")
public class Bill {
    @Id
    @Column(name = "id")
    private int id;

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    @Column(name = "bookingId")
    private int bookingId;

    @Column(name = "totalAmount")
    private double totalAmount;

    @Column(name = "taxAmount")
    private double taxAmount;

    @Column(name = "paymentMode")
    private String paymentMode;

    @Column(name = "paymentStatus")
    private String paymentStatus;

    @Column(name = "rewardPointsUsed")
    private double rewardPointsUsed;

    @Column(name = "discountAmount")
    private double discountAmount;

    @Column(name = "totalPayableAmount")
    private double totalPayableAmount;

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public double getAmountPayableByRewardPoints() {
        return amountPayableByRewardPoints;
    }

    public void setAmountPayableByRewardPoints(double amountPayableByRewardPoints) {
        this.amountPayableByRewardPoints = amountPayableByRewardPoints;
    }

    @Column(name = "amountPayableByRewardPoints")
    private double amountPayableByRewardPoints;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public double getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(double taxAmount) {
        this.taxAmount = taxAmount;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public double getTotalPayableAmount() {
        return totalPayableAmount;
    }

    public void setTotalPayableAmount(double totalPayableAmount) {
        this.totalPayableAmount = totalPayableAmount;
    }

    public double getRewardPointsUsed() {
        return rewardPointsUsed;
    }

    public void setRewardPointsUsed(double rewardPointsUsed) {
        this.rewardPointsUsed = rewardPointsUsed;
    }

    @Override
    public String toString() {
        return "Bill{" +
                "id=" + id +
                ", bookingId='" + bookingId + '\'' +
                ", totalAmount=" + totalAmount +
                ", taxAmount=" + taxAmount +
                ", paymentMode='" + paymentMode + '\'' +
                ", paymentStatus='" + paymentStatus + '\'' +
                ", rewardPointsUsed=" + rewardPointsUsed +
                ", discountAmount=" + discountAmount +
                ", totalPayableAmount=" + totalPayableAmount +
                '}';
    }
}
