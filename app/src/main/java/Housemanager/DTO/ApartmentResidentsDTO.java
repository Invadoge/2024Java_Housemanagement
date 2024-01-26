package Housemanager.DTO;

import Housemanager.entities.Enums.ResidentType;
import lombok.Setter;

@Setter
public class ApartmentResidentsDTO {
    Long buildingId;
    Short apartmentNumber;
    String residentName;
    Short residentAge;
    ResidentType residentType;

    public ApartmentResidentsDTO(Long buildingId, Short apartmentNumber, String residentName, Short residentAge,
            ResidentType residentType) {
        this.setBuildingId(buildingId);
        ;
        this.setApartmentNumber(apartmentNumber);
        this.setResidentName(residentName);
        this.setResidentAge(residentAge);
        this.setResidentType(residentType);
    }

    @Override
    public String toString() {
        return "========================================\n" +
                "ApartmentResidentsDTO\n" +
                "Building_id=" + buildingId + "\n" +
                "Apartment_number=" + apartmentNumber + "\n" +
                "Resident_name=" + residentName + "\n" +
                "ResidentAge=" + residentAge + "\n" +
                "ResidentType=" + residentType;
    }

}
