package Domus.DatasetUtils;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class CustomDomusRecord extends DomusRecord{

    public CustomDomusRecord(LocalTime time, String sensorID, String sensorName, String sensorLocation, SensorState state) {
        super(time, sensorID, sensorName, sensorLocation, state);
    }

    public CustomDomusRecord(DomusRecord record)
    {
        super(record.time(), record.sensorID(), record.sensorName(), record.sensorLocation(), record.state());
    }
    @Override
    public String toString() {
        return sensorID()+"-"+sensorLocation().charAt(0);
    }
    public static List<DomusRecord> toCustomDomusRecord(List<DomusRecord> l)
    {
        List<DomusRecord> out = new ArrayList<>();
        for(DomusRecord r : l)
        {
            out.add(new CustomDomusRecord(r));
        }
        return out;
    }
}
