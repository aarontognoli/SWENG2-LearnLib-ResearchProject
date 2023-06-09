package Domus.DatasetUtils.DatasetClass;


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
    public DatasetDay(List<DomusRecord> pre,List<DomusRecord> during, List<DomusRecord> post)
    {
        this.records.addAll(pre);
        this.preTea.addAll(pre);

        this.records.addAll(during);
        this.duringTea.addAll(during);

        this.records.addAll(post);
        this.postTea.addAll(post);
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
            else if(r.time().isBefore(teaEnd)|| r.time().equals(teaEnd))
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
