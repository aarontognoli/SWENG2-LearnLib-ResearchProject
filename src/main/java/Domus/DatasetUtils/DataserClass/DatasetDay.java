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

    List<DomusRecord> preTea= new ArrayList<>();
    List<DomusRecord> duringTea= new ArrayList<>();
    List<DomusRecord> postTea= new ArrayList<>();
    private void divideRecords(LocalTime teaStart,LocalTime teaEnd)
    {
        for(DomusRecord r : records)
        {
            if(r.time().isBefore(teaStart))
            {
                preTea.add(r);
            }
            else if(r.time().isBefore(teaEnd))
            {
                duringTea.add(r);
            }
            else
            {
                postTea.add(r);
            }
        }
    }

    public List<DomusRecord> getPreTea() {
        return new ArrayList<>(preTea);
    }

    public List<DomusRecord> getDuringTea() {
        return new ArrayList<>(duringTea);
    }

    public List<DomusRecord> getPostTea() {
        return new ArrayList<>(postTea);
    }
}
