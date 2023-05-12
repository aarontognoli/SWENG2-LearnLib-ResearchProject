package Domus.DatasetUtils;

import java.time.LocalTime;
import java.util.Objects;

//
public class DomusRecord {
    private final LocalTime time;
    private final String sensorID;
    private final String sensorName;
    private final String sensorLocation;
    private final SensorState state;

    public DomusRecord(LocalTime time, String sensorID, String sensorName, String sensorLocation, SensorState state) {
        this.time = time;
        this.sensorID = sensorID;
        this.sensorName = sensorName;
        this.sensorLocation = sensorLocation;
        this.state = state;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DomusRecord that = (DomusRecord) o;
        return Objects.equals(sensorName, that.sensorName) && Objects.equals(sensorLocation, that.sensorLocation) && state == that.state;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sensorName, sensorLocation, state);
    }

    @Override
    public String toString() {
        return sensorName + "-" + sensorLocation + "-" + state;
    }

    public LocalTime time() {
        return time;
    }

    public String sensorID() {
        return sensorID;
    }

    public String sensorName() {
        return sensorName;
    }

    public String sensorLocation() {
        return sensorLocation;
    }

    public SensorState state() {
        return state;
    }

}
