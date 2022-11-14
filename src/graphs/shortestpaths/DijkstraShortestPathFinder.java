package graphs.shortestpaths;

import graphs.BaseEdge;
//import graphs.Edge;
//import graphs.EdgeWithData;
import graphs.Graph;
//import priorityqueues.ArrayHeapMinPQ;
import priorityqueues.DoubleMapMinPQ;
import priorityqueues.ExtrinsicMinPQ;

import java.util.ArrayList;
//import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
//import java.util.Set;

/**
 * Computes shortest paths using Dijkstra's algorithm.
 * @see SPTShortestPathFinder for more documentation.
 */
public class DijkstraShortestPathFinder<G extends Graph<V, E>, V, E extends BaseEdge<V, E>>
    extends SPTShortestPathFinder<G, V, E> {

    protected <T> ExtrinsicMinPQ<T> createMinPQ() {
        return new DoubleMapMinPQ<>();
        //return new ArrayHeapMinPQ<>();

        /*
        Otherwise, do not change this method.
        We override this during grading to test your code using our correct implementation so that
        you don't lose extra points if your implementation is buggy.
         */
    }

    @Override
    protected Map<V, E> constructShortestPathsTree(G graph, V start, V end) {
        ExtrinsicMinPQ<V> priorityQueue = createMinPQ();
        Map<V, E> shortestPathTree = new HashMap<>();
        Map<V, Double> distTo = new HashMap<>();
        priorityQueue.add(start, 0.0);
        distTo.put(start, 0.0);
        while (!priorityQueue.isEmpty()) {
            V minVert = priorityQueue.removeMin();
            if (Objects.equals(minVert, end)) {
                return shortestPathTree;
            }
            for (E edge : graph.outgoingEdgesFrom(minVert)) {
                if (!distTo.containsKey(edge.to())) {
                    distTo.put(edge.to(), Double.POSITIVE_INFINITY);
                }
                double oldDist = distTo.get(edge.to());
                double newDist = distTo.get(edge.from()) + edge.weight();

                if (newDist < oldDist) {
                    distTo.replace(edge.to(), newDist);
                    shortestPathTree.put(edge.to(), edge);
                    if (priorityQueue.contains(edge.to())) {
                        priorityQueue.changePriority(edge.to(), newDist);
                    } else {
                        priorityQueue.add(edge.to(), newDist);
                    }
                }
            }
        }
        return shortestPathTree;
    }

    @Override
    protected ShortestPath<V, E> extractShortestPath(Map<V, E> spt, V start, V end) {
        List<E> path = new ArrayList<>();

        if (Objects.equals(start, end)) {
            return new ShortestPath.SingleVertex<>(start);
        }

        if (!spt.containsKey(end)) {
            return new ShortestPath.Failure<>();
        }
        V curr = end;
        while (!Objects.equals(curr, start)) {
            path.add(spt.get(curr));
            curr = spt.get(curr).from();
        }
        Collections.reverse(path);
        return new ShortestPath.Success<>(path);
    }

}
