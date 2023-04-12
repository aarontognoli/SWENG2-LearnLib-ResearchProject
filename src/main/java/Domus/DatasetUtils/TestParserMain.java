package Domus.DatasetUtils;

import java.io.IOException;
import java.util.List;

public class TestParserMain {
    public static void main(String[] args) throws IOException {
        List<DomusRecord> records = DomusParser.parse("src/main/resources/Domus Series 2/User 1/Day 1.vna");
        records.forEach(System.out::println);
        List<DomusActivityRecord> records2 = DomusParser.parseActivities("src/main/resources/Domus Series 2/User 1/User1-activity-annotation-series2.txt");
        records2.forEach(System.out::println);

    }
}
