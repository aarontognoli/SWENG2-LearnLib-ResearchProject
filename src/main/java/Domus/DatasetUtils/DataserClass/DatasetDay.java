package Domus.DatasetUtils.DataserClass;


import Domus.DatasetUtils.DomusParser;
import Domus.DatasetUtils.DomusRecord;

import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class DatasetDay {
    List<DomusRecord> records= new ArrayList<>();
    //firstDataset
    public DatasetDay(String path) throws IOException
    {
        this.records = DomusParser.parse(path);
    }

    public List<DomusRecord> getRecords() {
        return new ArrayList<>(records);
    }
    //used for Second DataSet
    public DatasetDay(String path, LocalTime teaStart, LocalTime teaEnd) throws IOException {
        this(path);
        divideRecords(teaStart,teaEnd);

    }

    List<DomusRecord> preTea;
    List<DomusRecord> duringTea;
    List<DomusRecord> postTea;
    private void divideRecords(LocalTime teaStart,LocalTime teaEnd)
    {
        //todo
    }
}
