package AstarBstar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.learnlib.algorithms.lstar.dfa.ClassicLStarDFA;
import de.learnlib.algorithms.lstar.dfa.ClassicLStarDFABuilder;

import de.learnlib.api.oracle.EquivalenceOracle;
import de.learnlib.api.oracle.MembershipOracle;
import de.learnlib.datastructure.observationtable.writer.ObservationTableASCIIWriter;
import de.learnlib.oracle.equivalence.DFAWMethodEQOracle;
import de.learnlib.util.Experiment;
import de.learnlib.util.statistics.SimpleProfiler;
import net.automatalib.automata.fsa.DFA;
import net.automatalib.automata.fsa.impl.compact.CompactDFA;
import net.automatalib.commons.smartcollections.ArrayUtil;
import net.automatalib.graphs.Graph;
import net.automatalib.serialization.dot.GraphDOT;
import net.automatalib.visualization.Visualization;
import net.automatalib.visualization.dot.DOT;
import net.automatalib.visualization.dot.DOTComponent;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {

        // create oracle
        MembershipOracle.DFAMembershipOracle<Character> testOracle = new Oracle();
        //Equivalence Oracle
        EquivalenceOracle.DFAEquivalenceOracle<Character> eqtest = new DFAWMethodEQOracle<>( testOracle,4);


        ClassicLStarDFA<Character> lstar = new ClassicLStarDFABuilder<Character>()
                .withAlphabet(TestDriver.SIGMA)
                .withOracle(testOracle)
                .create();


        Experiment.DFAExperiment<Character> experiment = new Experiment.DFAExperiment<>( lstar,eqtest,TestDriver.SIGMA);



        // turn on time profiling
        experiment.setProfile(true);

        // enable logging of models
        experiment.setLogModels(true);

        // run experiment
        experiment.run();

        // get learned model
        DFA<?, Character> result = experiment.getFinalHypothesis();
        //try system
        System.out.println(result.accepts(toCharacterArray("aaa")));
        System.out.println(result.accepts(toCharacterArray("aabb")));
        System.out.println(result.accepts(toCharacterArray("bbb")));
        System.out.println(result.accepts(toCharacterArray("")));
        System.out.println(result.accepts(toCharacterArray("bbbaa")));
        //TODO find way to save result
        /*
        CompactDFA<Character> toJson = (CompactDFA<Character>) result;
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        //save result to Json
        try (FileWriter writer = new FileWriter("./myDFA.json")) {
            gson.toJson(toJson, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        // report results
        System.out.println("-------------------------------------------------------");

        // profiling
        System.out.println(SimpleProfiler.getResults());

        // learning statistics
        System.out.println(experiment.getRounds().getSummary());
        System.out.println();
        new ObservationTableASCIIWriter<>().write(lstar.getObservationTable(), System.out);

        // model statistics
        System.out.println("States: " + result.size());
        System.out.println("Sigma: " + TestDriver.SIGMA.size());

        // show model
        System.out.println();
        System.out.println("Model: ");
        GraphDOT.write(result, TestDriver.SIGMA, System.out); // may throw IOException!



        Visualization.visualize(result, TestDriver.SIGMA);

        System.out.println("-------------------------------------------------------");




    }


    public static List<Character> toCharacterArray(String s)
    {
        List<Character> out = new ArrayList<>();
        for(char c : s.toCharArray())
        {
            out.add(c);
        }
        return out;
    }

}

