package Domus.Performance;

import Domus.DatasetUtils.DomusRecord;
import Domus.DomusWord;
import net.automatalib.automata.fsa.DFA;

import java.util.ArrayList;
import java.util.List;

public class PerformanceEvaluator {
    private final List<DomusWord> positiveSequences;
    private final List<DomusWord> negativeSequences;
    private final DFA<?, DomusRecord> dfa;
    private double accuracy; // fraction of the samples correctly classified in the dataset
    private double precision; // fraction of samples correctly classified in the positive class
                                // among the ones classified in the positive class
    private double recall; // fraction of samples correctly classified in the positive class
                            // among the ones belonging to the positive class
    private double F1score; // harmonic mean of the precision and recall

    public PerformanceEvaluator(DFA<?, DomusRecord> dfa) {
        this.dfa = dfa;
        this.positiveSequences = new ArrayList<>();
        this.negativeSequences = new ArrayList<>();
    }

    public PerformanceEvaluator(DFA<?, DomusRecord> dfa, List<DomusWord> positiveSequences, List<DomusWord> negativeSequences) {
        this.dfa = dfa;
        this.positiveSequences = positiveSequences;
        this.negativeSequences = negativeSequences;
    }

    public void run() {
        int samples = positiveSequences.size() + negativeSequences.size();
        int truePos = 0;
        int trueNeg = 0;
        int falsePos = 0;
        int falseNeg = 0;
        for (DomusWord seq : positiveSequences) {
            boolean accepted = false;
            try {
                accepted = dfa.accepts(seq);
            } catch (ArrayIndexOutOfBoundsException e) {

            }
            if (accepted) {
                truePos++;
            } else {
                falseNeg++;
            }
        }

        for (DomusWord seq : negativeSequences) {
            boolean accepted = false;
            try {
                accepted = dfa.accepts(seq);
            } catch (ArrayIndexOutOfBoundsException e) {

            }
            if (!accepted) {
                trueNeg++;
            } else {
                falsePos++;
            }
        }

        accuracy = samples > 0 ? 100.0*(truePos + trueNeg)/samples : 0;
        precision = (truePos + falsePos) > 0 ? 100.0*truePos/(truePos + falsePos) : 0;
        recall = (truePos + falseNeg) > 0 ? 100.0*truePos/(truePos + falseNeg): 0;
        F1score = (precision + recall) > 0 ? 2 * precision * recall / (precision + recall) : 0;
    }

    public void addToPositive(DomusWord sequence) {
        positiveSequences.add(sequence);
    }

    public void addToPositive(List<DomusWord> sequences) {
        positiveSequences.addAll(sequences);
    }

    public void addToNegative(DomusWord sequence) {
        negativeSequences.add(sequence);
    }

    public void addToNegative(List<DomusWord> sequences) {
        negativeSequences.addAll(sequences);
    }

    public double getAccuracy() {
        return accuracy;
    }

    public double getPrecision() {
        return precision;
    }

    public double getRecall() {
        return recall;
    }

    public double getF1score() {
        return F1score;
    }
}
