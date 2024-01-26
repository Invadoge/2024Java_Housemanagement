package Housemanager.entities;

import Housemanager.UI.DisplayField;
import Housemanager.entities.Enums.ResidentType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "apartment_resident")
public class ApartmentResidents {
    // #region TABLE_COLUMNS
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "resident_id")
    private Long residentId;

    @DisplayField
    @NotBlank
    @Positive
    @Column(name = "building_id", nullable = false)
    private Long buildingId;

    @DisplayField
    @NotBlank
    @Positive
    @Column(name = "apartment_number", nullable = false)
    private Short apartmentNumber;

    @DisplayField
    @Column(name = "resident_name")
    @Pattern(regexp = "^([A-Z]).*", message = "Resident name should begin with big Letter!")
    private String residentName;

    @DisplayField
    @Positive
    @Min(1)
    @Max(110)
    @Column(name = "resident_age")
    private Short residentAge;

    @DisplayField
    @NotBlank
    @Enumerated(EnumType.STRING)
    @Column(name = "resident_type")
    private ResidentType residentType;
    // #endregion TABLE_COLUMNS

    // #region RELATIONSHIPS
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "apartment_number", referencedColumnName = "apartment_number", insertable = false, updatable = false),
            @JoinColumn(name = "building_id", referencedColumnName = "building_id", insertable = false, updatable = false)
    })
    private Apartment apartment;
    // #endregion RELATIONSHIPS

    public ApartmentResidents(Long buildingId, Short apartmentNumber, String residentName, Short residentAge,
            ResidentType residentType) {
        this.setBuildingId(buildingId);;
        this.setApartmentNumber(apartmentNumber);
        this.setResidentName(residentName);
        this.setResidentAge(residentAge);
        this.setResidentType(residentType);
    }
}