package Housemanager.entities;

import java.util.Set;

import Housemanager.UI.DisplayField;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "building_management")
public class BuildingManagement {
    @Column(name = "employee_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeId;

    @Column(name = "employee_name")
    @DisplayField
    @Pattern(regexp = "^([A-Z]).*")
    private String employeeName;

    // Note: it's better to have a separate table for companies
    // Doing it as a column instead for simplicity
    @Column(name = "company_name")
    @DisplayField
    private String companyName;

    @Column(name = "managed_buildings")
    private Short managedBuildings;

    @OneToMany(mappedBy = "managedBy", fetch = FetchType.EAGER)
    private Set<Building> buildings;

    public BuildingManagement(String eName, String cName) {
        this.employeeName = eName;
        this.companyName = cName;
        managedBuildings = 0;
    }

    public void incrementBuildings(){
        ++managedBuildings;
    }

    public void decrementBuildings(){
        --managedBuildings;
    }
}
