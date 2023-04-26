package Domus.DatasetUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class DomusParser {
    public static List<DomusRecord> parse(String pathname) throws IOException {
        List<DomusRecord> records = new ArrayList<>();
        File file = new File(pathname);
        BufferedReader br = new BufferedReader(new FileReader(file));
        String st;
        List<String> attributes;

        while ((st = br.readLine()) != null) {
            attributes = Arrays.stream(st.split("\"")).filter(s -> !s.isBlank()).toList();

            DomusRecord dr = new DomusRecord(LocalTime.parse(attributes.get(0), DateTimeFormatter.ofPattern("H:m:s")),
                    attributes.get(1),
                    attributes.get(2),
                    attributes.get(3),
                    SensorState.valueOf(attributes.get(4)));

            // filter out the infrared KitchenSink sensor because it is repetitive and there is
            // already another sensor that indicates when the sink is open (TapeColdWaterSink)
            if (!dr.sensorID().equals("IR01"))
                records.add(dr);
        }
        return records;
    }
    public static List<DomusActivityRecord> parseActivities(String pathname) throws IOException {
        List<DomusActivityRecord> records = new ArrayList<>();
        File file = new File(pathname);
        BufferedReader br = new BufferedReader(new FileReader(file));
        String st;
        List<String> attributes;
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-LLL-yyyy HH:mm:ss", Locale.ENGLISH);
        while ((st = br.readLine()) != null) {
            attributes = Arrays.stream(st.split("\t")).filter(s -> !s.isBlank()).toList();

                //skips invalid lines
            try {
                DomusActivityRecord dr = new DomusActivityRecord(LocalDateTime.parse(attributes.get(0), format),
                        LocalDateTime.parse(attributes.get(1), format),
                        attributes.get(2));
                records.add(dr);
            }
            catch (DateTimeParseException e)
            {
                System.out.println(e.getMessage());
            }



        }
        return records;
    }

}
