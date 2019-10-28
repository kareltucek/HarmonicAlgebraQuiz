package cz.ktweb.harmonicalgebraquiz;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class RandUtils {
    static Random rnd = new Random();
    static Map<String, Integer> lastInt = new HashMap();

    static int NextInt(String id, int bound) {
        if(!lastInt.containsKey(id)) {
            lastInt.put(id, bound);
        }
        int last = lastInt.get(id);
        int current = last;
        while ( current == last ) {
            current = rnd.nextInt(bound);
        }
        lastInt.put(id, current);
        return current;
    }
}
