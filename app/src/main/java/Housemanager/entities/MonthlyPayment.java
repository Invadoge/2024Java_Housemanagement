package Housemanager.entities;

import java.math.BigDecimal;
import java.time.LocalDate;

import Housemanager.UI.DisplayField;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity
@Table(name = "monthly_payment")
public class MonthlyPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "building_id")
    @DisplayField
    private Long buildingId;

    @Column(name = "apartment_number")
    @DisplayField
    private Short apartmentNumber;

    @Column(name = "paid_by")
    @DisplayField
    @Pattern(regexp = "^([A-Z]).*")
    private String paidBy;

    @Column(name = "payment_month")
    @DisplayField
    private LocalDate paymentMonth;


    @Column(name = "amount")
    @Positive
    @DisplayField
    private BigDecimal amount;

    @Column(name = "outstanding_balance")
    private BigDecimal outstandingBalance;

    //#region RELATIONSHIPS
    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "apartment_number", referencedColumnName = "apartment_number", insertable = false, updatable = false),
        @JoinColumn(name = "building_id", referencedColumnName = "building_id", insertable = false, updatable = false)
    })
    private Apartment apartment;
    //#endregion RELATIONSHIPS

    public MonthlyPayment(Long buildingId, Short apartmentNumber, String paidBy, LocalDate payDate, BigDecimal amountPaid){
        this.setBuildingId(buildingId);
        this.setApartmentNumber(apartmentNumber);
        this.setPaidBy(paidBy);
        this.setPaymentMonth(payDate);
        this.setAmount(amountPaid);
    }
    
    public Long getBuildingId() {
        return buildingId;
    }
    public void setBuildingId(Long buildingId) {
        this.buildingId = buildingId;
    }
    public Short getApartmentNumber() {
        return apartmentNumber;
    }
    public void setApartmentNumber(Short apartmentNumber) {
        this.apartmentNumber = apartmentNumber;
    }

    public String getPaidBy() {
        return paidBy;
    }

    public void setPaidBy(String paidBy) {
        this.paidBy = paidBy;
    }

    public LocalDate getPaymentMonth() {
        return paymentMonth;
    }

    public void setPaymentMonth(LocalDate paymentMonth) {
        this.paymentMonth = paymentMonth;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        if(amount.compareTo(BigDecimal.ZERO) < 0){
            throw new IllegalArgumentException("MonthlyPayment::Amount cannot be less than 0!");
        }
        this.amount = amount;
    }

    public void setOutstandingBalance(BigDecimal outstandingBalance) {
        this.outstandingBalance = outstandingBalance;
    }

    public BigDecimal getOutstandingBalance(){
        return outstandingBalance;
    }
}

