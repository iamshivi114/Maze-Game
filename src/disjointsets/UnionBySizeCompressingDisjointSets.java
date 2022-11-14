package disjointsets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
//import java.util.NoSuchElementException;
//import java.util.Objects;

/**
 * A quick-union-by-size data structure with path compression.
 * @see DisjointSets for more documentation.
 */
public class UnionBySizeCompressingDisjointSets<T> implements DisjointSets<T> {
    // Do NOT rename or delete this field. We will be inspecting it directly in our private tests.
    List<Integer> pointers;
    HashMap<T, Integer> ids;
    int size;

    /*
    However, feel free to add more fields and private helper methods. You will probably need to
    add one or two more fields in order to successfully implement this class.
    */

    public UnionBySizeCompressingDisjointSets() {
        this.ids = new HashMap<>();
        this.size = 0;
        this.pointers = new ArrayList<>();
    }

    @Override
    public void makeSet(T item) {
        ids.put(item, size);
        pointers.add(-1);
        size++;
    }

    @Override
    public int findSet(T item) {
        if (!ids.containsKey(item)) {
            throw new IllegalArgumentException();
        }
        Integer index = ids.get(item);
        List<Integer> temp = new ArrayList<>();
        temp.add(index);
        return findSet(index, temp);
        // pathCompression(parent, index);
        // return parent;
    }

    private int findSet(int index, List<Integer> temp) {
        if (pointers.get(index) < 0) {
            for (int i = 0; i < temp.size() - 1; i++) {
                pointers.set(temp.get(i), index);
            }
            return index;
        }
        temp.add(pointers.get(index));
        return findSet(pointers.get(index), temp);
    }

    // private void pathCompression(int parent, int index) {
    //     if (index != parent) {
    //         pathCompression(parent, pointers.get(index));
    //         pointers.set(index, parent);
    //     }
    // }

    @Override
    public boolean union(T item1, T item2) {
        int repOne = findSet(item1);
        int repTwo = findSet(item2);
        if (repOne != repTwo) {
            int parentSet = repOne;
            int childSet = repTwo;
            if (pointers.get(repOne) > pointers.get(repTwo)) {
               parentSet = repTwo;
               childSet = repOne;
            }
            pointers.set(parentSet, pointers.get(repOne) + pointers.get(repTwo));
            pointers.set(childSet, parentSet);
            return true;
        }
        return false;
    }
}
