package Domus.DatasetUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DomusParser {
    public static List<DomusRecord> parse(String pathname) throws IOException {
        List<DomusRecord> records = new ArrayList<>();
        File file = new File(pathname);
        BufferedReader br = new BufferedReader(new FileReader(file));
        String st;
        List<String> attributes;

        while ((st = br.readLine()) != null) {
            attributes = Arrays.stream(st.split("\"")).filter(s -> !s.isBlank()).toList();

            DomusRecord dr = new DomusRecord(LocalTime.parse(attributes.get(0), DateTimeFormatter.ofPattern("H:mm:s")),
                    attributes.get(1),
                    attributes.get(2),
                    attributes.get(3),
                    SensorState.valueOf(attributes.get(4)));

            records.add(dr);
        }
        return records;
    }

}
