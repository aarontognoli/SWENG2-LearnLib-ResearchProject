package AstarBstar;

import de.learnlib.api.oracle.MembershipOracle;
import de.learnlib.api.query.Query;
import net.automatalib.words.WordBuilder;

import java.util.Collection;

public class Oracle implements MembershipOracle.DFAMembershipOracle<String> {

    TestDriver testDriver= new TestDriver();
    @Override
    public void processQueries(Collection<? extends Query<String, Boolean>> queries) {
        for(Query<String, Boolean> query : queries)
        {
            testDriver.reset();
            boolean out = false;
            for (String input : query.getInput()) {
                out = testDriver.executeSymbol(input);
            }

            query.answer(out);
        }
    }
}
