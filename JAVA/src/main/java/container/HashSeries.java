package container;

import java.util.HashMap;
import java.util.HashSet;

public class HashSeries {
    public void MapAndSet(){
        HashMap<Integer, String> hashMap  = new HashMap<>();
        HashSet<Integer> hashSet = new HashSet<>();

        hashMap.put(1,"one");
        hashMap.put(2,"two");
        hashMap.put(3,"three");

        hashSet.add(1);
        hashSet.add(2);
        hashSet.add(3);
        hashSet.add(1);
        System.out.println(hashSet.size());


    }
}
