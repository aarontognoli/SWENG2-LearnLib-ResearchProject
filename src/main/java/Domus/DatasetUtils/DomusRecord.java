package Domus.DatasetUtils;

import java.time.LocalTime;

//
public record DomusRecord(LocalTime time, String sensorID, String sensorName, String sensorLocation, SensorState state) {

}
