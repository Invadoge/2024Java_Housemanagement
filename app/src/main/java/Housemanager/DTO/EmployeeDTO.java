package Housemanager.DTO;

import java.math.BigDecimal;

public class EmployeeDTO {
    Long employeeId;
    String employeeName;
    String employeeCompany;
    BigDecimal profits;

    public EmployeeDTO(Long employeeId, String employeeName, String employeeCompany, BigDecimal profits) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.employeeCompany = employeeCompany;
        this.profits = profits;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public String getEmployeeCompany() {
        return employeeCompany;
    }

    public BigDecimal getProfits() {
        return profits;
    }

    @Override
    public String toString() {
        return "========================================\n" +
                "EmployeeDTO\n" +
                "Employee_id: " + employeeId + "\n" +
                "Employee_name: " + employeeName + "\n" +
                "Employee_company: " + employeeCompany + "\n" +
                "Profits: " + profits + "\n" +
                "========================================";
    }

}
