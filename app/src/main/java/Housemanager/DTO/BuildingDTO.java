package Housemanager.DTO;

public class BuildingDTO {
    String employeeName;
    Long buildingId;
    String city;
    String address;

    public BuildingDTO(String employeeName, Long buildingId, String city, String address) {
        this.employeeName = employeeName;
        this.buildingId = buildingId;
        this.city = city;
        this.address = address;
    }

    @Override
    public String toString() {
        return "========================================\n" +
                "BuildingDTO\n" +
                "Employee_name: " + employeeName + "\n" +
                "Building_id: " + buildingId + "\n" +
                "City: " + city + "\n" +
                "Address: " + address + "\n" +
                "========================================";
    }

}
