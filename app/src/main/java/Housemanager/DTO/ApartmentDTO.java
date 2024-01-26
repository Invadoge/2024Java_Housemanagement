package Housemanager.DTO;

import java.math.BigDecimal;

public class ApartmentDTO {
    Long buildingId;
    Short aptNumber;
    Short floorNumber;
    BigDecimal aptArea;
    String aptOwner;

    public ApartmentDTO(Long buildingId, Short aptNumber, Short floorNumber, BigDecimal aptArea, String aptOwner) {
        this.buildingId = buildingId;
        this.aptNumber = aptNumber;
        this.floorNumber = floorNumber;
        this.aptArea = aptArea;
        this.aptOwner = aptOwner;
    }

    @Override
    public String toString() {
        return "========================================\n" +
                "ApartmentDTO\n" +
                "Building_id: " + buildingId + "\n" +
                "Aptartment_number: " + aptNumber + "\n" +
                "Floor_number: " + floorNumber + "\n" +
                "Aptartment_area: " + aptArea + "\n" +
                "Apartment_owner: " + aptOwner + "\n" +
                "========================================";
    }

}
