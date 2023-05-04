package Domus.DatasetUtils.DatasetClass;

import java.util.ArrayList;
import java.util.List;

public class Dataset {
    List<List<DatasetDay>> users= new ArrayList<>();
    public void addUser()
    {
        users.add(new ArrayList<>());
    }
    public void addDayToLastUser(DatasetDay day) throws Exception {
        if(users.isEmpty())
            throw new Exception("Empty");
        users.get(users.size()-1).add(day);
    }

    public List<List<DatasetDay>> getUsers() {
        return new ArrayList<>(users);
    }
}
