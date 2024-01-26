package Housemanager.entities;

import java.math.BigDecimal;
import java.util.Set;

import javax.annotation.Nonnegative;

import Housemanager.UI.DisplayField;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "building")
public class Building {
    //#region TABLE_COLUMNS
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "building_id")
    private Long id;

    @Column(name = "building_address")
    @DisplayField
    private String buildingAddress;

    @Column(name = "city")
    @DisplayField
    @Pattern(regexp = "^([A-Z]).*")
    private String city;

    @Column(name = "pet_tax")
    @Nonnegative
    @DisplayField
    private BigDecimal petTax;

    @Column(name = "elevator_tax")
    @Nonnegative
    @DisplayField
    private BigDecimal elevatorTax;

    @Column(name = "managed_by_employee")
    private Long managedByEmployee;
    //#endregion TABLE_COLUMNS

    //#region RELATIONSHIPS
    @ManyToOne
    @JoinColumn(name = "managed_by_employee", referencedColumnName = "employee_id", insertable = false, updatable = false)
    private BuildingManagement managedBy;

    @OneToMany(mappedBy = "building", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Apartment> apartments;
    //#endregion RELATIONSHIPS

    public Building(String buildingAddress, String city, BigDecimal petTax, BigDecimal elevatorTax) {
        
        if (elevatorTax == null || 
            elevatorTax.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Building Constructor::Elevator tax must be a positive non-null value.");
        }
        else if (petTax == null || 
                petTax.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Building Constructor::Pet tax must be a positive non-null value.");
        }
        this.buildingAddress = buildingAddress;
        this.city = city;
        this.petTax = petTax;
        this.elevatorTax = elevatorTax;
    }
}
