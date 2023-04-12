package Domus.DatasetUtils;

import java.time.LocalTime;
import java.util.Objects;

//
public record DomusRecord(LocalTime time, String sensorID, String sensorName, String sensorLocation, SensorState state) {

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
}
