package Domus;

import AstarBstar.JsonSupportClass;
import Domus.DatasetUtils.CustomGson;
import Domus.DatasetUtils.DomusRecord;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.automatalib.automata.fsa.impl.compact.CompactDFA;
import net.automatalib.serialization.dot.GraphDOT;
import net.automatalib.visualization.Visualization;
import net.automatalib.visualization.dot.DOT;

import java.io.*;
import java.lang.reflect.Type;

public class VisualizeGraph {

    public static void main(String[] args) throws IOException {
        //DOT.renderDOT(new File("./TestDot1u-1d.dot"),true);
        DOT.renderDOTExternal(new File("./TestDot1u-1d.dot"),"svg");
    }
}
