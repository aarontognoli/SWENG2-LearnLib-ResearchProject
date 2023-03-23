package FirstExample;

import java.util.HashSet;
import java.util.Set;

public class Pool {

    private Set<Object> pool = new HashSet<Object>();

    public void add(Object o) {
        pool.add(o);
    }

    public void remove(Object o) {
        pool.remove(o);
    }

    public boolean isEmpty() {
        return pool.isEmpty();
    }
}