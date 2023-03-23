package SecondExample;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayDeque;
import java.util.Deque;

/*
 * The BoundedStringQueue is the class of which we are going to
 * infer a model. It wraps an ordinary queue of Strings, limiting
 * its size to MAX_SIZE (3). Once the queue is full, additional
 * offers will be ignored.
 */
public class BoundedStringQueue {

    // capacity
    public static final int MAX_SIZE = 3;
    // storage
    private final Deque<String> data = new ArrayDeque<>(3);

    // add a String to the queue if capacity allows
    public void offer(String s) {
        if (data.size() < MAX_SIZE) {
            data.offerFirst(s);
        }
    }

    // get next element from queue (null for empty queue)
    public @Nullable
    String poll() {
        return data.poll(); //pollFirst
    }
}