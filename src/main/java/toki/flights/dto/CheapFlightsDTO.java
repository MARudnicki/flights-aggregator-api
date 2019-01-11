package toki.flights.dto;

public class CheapFlightsDTO {

    private String id;
    private String departure;
    private String arrival;
    private String departureTime;
    private String arrivalTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getArrival() {
        return arrival;
    }

    public void setArrival(String arrival) {
        this.arrival = arrival;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    @Override
    public String toString() {
        return "ID:" + getId() + " DEPT:" +  getDeparture() + " ARVL:" + getArrival()
                + " DEPTTIME:" + getDepartureTime() +" ARVLTIME:" + getArrivalTime() + "\n";
    }
}
