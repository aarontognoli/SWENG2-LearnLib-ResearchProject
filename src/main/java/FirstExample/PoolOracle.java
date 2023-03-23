package FirstExample;

import de.learnlib.api.oracle.MembershipOracle;
import de.learnlib.api.query.Query;
import de.learnlib.examples.mealy.ExampleCoffeeMachine;
import net.automatalib.words.Word;
import net.automatalib.words.WordBuilder;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collection;

public class PoolOracle implements MembershipOracle.MealyMembershipOracle<String, String> {
    private PoolTestDriver testDriver;

    public PoolOracle() {
        testDriver = new PoolTestDriver();
    }



    @Override
    public void processQueries(Collection<? extends Query<String, Word< String>>> queries) {

        for(Query<String, Word<String>> query : queries)
        {
            testDriver.reset();
            WordBuilder< String> output = new WordBuilder<>();
            for (String input : query.getInput()) {
                output.add(testDriver.executeSymbol(input));
            }

            query.answer(output.toWord().suffix(query.getSuffix().size()));
        }
    }


}
