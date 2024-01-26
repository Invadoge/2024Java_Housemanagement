package Housemanager.DTO;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MonthlyPaymentDTO {
    private String companyName;
    private String employeeName;
    private Long buildingId;
    private Short apartmentNumber;
    private LocalDate paymentMonth;
    private BigDecimal amount;

    
    public MonthlyPaymentDTO(String companyName, String employeeName, Long buildingId, Short apartmentNumber,
            LocalDate paymentMonth, BigDecimal amount) {
        this.companyName = companyName;
        this.employeeName = employeeName;
        this.buildingId = buildingId;
        this.apartmentNumber = apartmentNumber;
        this.paymentMonth = paymentMonth;
        this.amount = amount;
    }


    @Override
    public String toString() {
        return "MonthlyPaymentDTO [companyName=" + companyName + ", employeeName=" + employeeName + ", buildingId="
                + buildingId + ", apartmentNumber=" + apartmentNumber + ", paymentMonth=" + paymentMonth + ", amount="
                + amount + "]";
    }

    

    
}
