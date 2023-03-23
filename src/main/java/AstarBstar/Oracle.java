package AstarBstar;

import de.learnlib.api.oracle.MembershipOracle;
import de.learnlib.api.query.Query;
import net.automatalib.words.WordBuilder;

import java.util.Collection;

public class Oracle implements MembershipOracle.DFAMembershipOracle<Character> {

    TestDriver testDriver= new TestDriver();
    @Override
    public void processQueries(Collection<? extends Query<Character, Boolean>> queries) {
        for(Query<Character, Boolean> query : queries)
        {
            testDriver.reset();
            //empty string ok
            boolean out = true;
            for (Character input : query.getInput()) {
                out = testDriver.executeSymbol(input);
            }

            query.answer(out);
        }
    }
}
