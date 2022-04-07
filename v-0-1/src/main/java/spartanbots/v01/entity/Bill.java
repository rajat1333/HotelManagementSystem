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

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    @Column(name = "bookingId")
    private String bookingId;

    @Column(name = "amount")
    private double amount;

    @Column(name = "taxAmount")
    private double taxAmount;

    @Column(name = "status")
    private String status;

    @Column(name = "discountAmount")
    private double discountAmount;

    @Column(name = "totalPayableAmount")
    private double totalPayableAmount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(double taxAmount) {
        this.taxAmount = taxAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    @Override
    public String toString() {
        return "Bill{" +
                "id=" + id +
                ", bookingId='" + bookingId + '\'' +
                ", amount=" + amount +
                ", taxAmount=" + taxAmount +
                ", status='" + status + '\'' +
                ", discountAmount=" + discountAmount +
                ", totalPayableAmount=" + totalPayableAmount +
                '}';
    }
}
