package mop;

import org.jxmapviewer.viewer.GeoPosition;
import way.Route;
import way.TrafficInfo;

public class MopInfo {

    // General information about MOP
    private String branch;
    private String locality;
    private final String name;
    private final GeoPosition geoPosition;
    private final String road;
    private final String direction;
    private int type;
    private double mileage;
    private MopParkingSpacesInfo parkingSpacesInfo;
    private MopEquipmentInfo equipmentInfo;
    private Route route = new Route();

    public MopInfo(String branch, String locality, String name, GeoPosition geoPosition,
                   String road, String direction, int type,
                   MopParkingSpacesInfo parkingSpacesInfo, MopEquipmentInfo equipmentInfo, double mileage) {
        this.branch = branch;
        this.locality = locality;
        this.name = name;
        this.geoPosition = geoPosition;
        this.road = road;
        this.direction = direction;
        this.type = type;
        this.parkingSpacesInfo = parkingSpacesInfo;
        this.equipmentInfo = equipmentInfo;
        this.mileage = mileage;
    }

    public String getBranch() {
        return branch;
    }

    public String getLocality() {
        return locality;
    }

    public String getName() {
        return name;
    }

    public GeoPosition getGeoPosition() {
        return geoPosition;
    }

    public String getRoad() {
        return road;
    }

    public String getDirection() {
        return direction;
    }

    public Integer getType() {
        return type;
    }

    public MopParkingSpacesInfo getParkingSpacesInfo() {
        return parkingSpacesInfo;
    }

    public MopEquipmentInfo getEquipmentInfo() {
        return equipmentInfo;
    }

    public double getMileage() {
        return mileage;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public TrafficInfo getTrafficInfo() {
        return route.getTrafficInfo();
    }

    public void setEquipmentInfo(MopEquipmentInfo equipmentInfo) {
        this.equipmentInfo = equipmentInfo;
    }

    public void setParkingSpacesInfo(MopParkingSpacesInfo parkingSpacesInfo) {
        this.route.removeSpacesInfo(direction, this.parkingSpacesInfo);
        this.parkingSpacesInfo = parkingSpacesInfo;
        this.route.addSpacesInfo(direction, this.parkingSpacesInfo);
        System.out.println(this.route.getSpacesByDirection().get(direction).getCarSpaces());
    }
}
