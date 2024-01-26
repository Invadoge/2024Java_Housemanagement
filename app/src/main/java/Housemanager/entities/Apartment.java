package Housemanager.entities;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import Housemanager.UI.DisplayField;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "apartment")
public class Apartment {
    // #region TABLE_COLUMNS
    @Id
    @Column(name = "building_id")
    @DisplayField
    private Long buildingId;

    @Id
    @Column(name = "apartment_number")
    @DisplayField
    private Short apartmentNumber;

    @Positive
    @Column(name = "apartment_floor")
    @DisplayField
    private Short apartmentFloor;

    @Positive
    @Column(name = "apartment_area")
    @DisplayField
    private BigDecimal apartmentArea;

    @Column(name = "apartment_owner")
    @DisplayField
    @Pattern(regexp = "^([A-Z]).*")
    private String apartmentOwner;

    // Uncomment if needed
    // For the purpose of this project it wont serve a function
    // A proper database should have contact info of owner
    // @Column()
    // private String ownerEmail;

    // #endregion TABLE_COLUMNS

    // #region RELATIONSHIPS
    @OneToMany(mappedBy = "apartment", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<ApartmentResidents> residents;

    @OneToMany(mappedBy = "apartment", cascade = CascadeType.ALL)
    private List<MonthlyPayment> monthlyPayments;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "building_id", referencedColumnName = "building_id", insertable = false, updatable = false)
    private Building building;
    // #endregion RELATIONSHIPS

    public Apartment(Long buildingId, Short aptNumber, Short aptFloor, BigDecimal aptArea, String aptOwner) {
        this.apartmentNumber = aptNumber;
        this.buildingId = buildingId;
        this.apartmentFloor = aptFloor;
        this.apartmentArea = aptArea;
        this.apartmentOwner = aptOwner;
    }

}
