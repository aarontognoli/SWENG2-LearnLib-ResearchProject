package AstarBstar;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.automatalib.automata.fsa.impl.compact.CompactDFA;
import net.automatalib.serialization.dot.GraphDOT;
import net.automatalib.visualization.Visualization;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;

import static AstarBstar.Main.toCharacterArray;

public class SystemTestMain {

    public static void main(String[] args) {

        Gson gson = new Gson();
        JsonSupportClass<Character> myObj;
        try (Reader reader = new FileReader("./myDFA.json")) {
            //Gson solution for generic types
            Type myType = new TypeToken<JsonSupportClass<Character>>() {}.getType();
            // Convert JSON File to Java Object
            myObj = gson.fromJson(reader, myType);
            CompactDFA<Character> dfa = myObj.getDFA(false);

            GraphDOT.write(dfa, dfa.getInputAlphabet(), System.out);
            Visualization.visualize(dfa, dfa.getInputAlphabet());

            System.out.println(dfa.accepts(toCharacterArray("aaa")));
            System.out.println(dfa.accepts(toCharacterArray("aabb")));
            System.out.println(dfa.accepts(toCharacterArray("bbb")));
            System.out.println(dfa.accepts(toCharacterArray("")));
            System.out.println(dfa.accepts(toCharacterArray("bbbaa")));
            System.out.println(dfa.accepts(toCharacterArray("aabaa")));

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}

