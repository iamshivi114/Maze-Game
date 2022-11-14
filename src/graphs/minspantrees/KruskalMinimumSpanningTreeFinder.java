package graphs.minspantrees;

import disjointsets.DisjointSets;
//import disjointsets.QuickFindDisjointSets;
import disjointsets.UnionBySizeCompressingDisjointSets;
import graphs.BaseEdge;
import graphs.KruskalGraph;

import java.util.ArrayList;
//import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Computes minimum spanning trees using Kruskal's algorithm.
 * @see MinimumSpanningTreeFinder for more documentation.
 */
public class KruskalMinimumSpanningTreeFinder<G extends KruskalGraph<V, E>, V, E extends BaseEdge<V, E>>
    implements MinimumSpanningTreeFinder<G, V, E> {

    protected DisjointSets<V> createDisjointSets() {

        return new UnionBySizeCompressingDisjointSets<>();

        /*
        Otherwise, do not change this method.
        We override this during grading to test your code using our correct implementation so that
        you don't lose extra points if your implementation is buggy.
         */
    }

    @Override
    public MinimumSpanningTree<V, E> findMinimumSpanningTree(G graph) {
        // Here's some code to get you started; feel free to change or rearrange it if you'd like.

        // sort edges in the graph in ascending weight order
        List<E> edges = new ArrayList<>(graph.allEdges());
        edges.sort(Comparator.comparingDouble(E::weight));

        DisjointSets<V> disjointSets = createDisjointSets();
        Set<E> edgesOfMST = new HashSet<>();

        List<V> listOfVertices = new ArrayList<>(graph.allVertices());
        for (V vertex : listOfVertices) {
            disjointSets.makeSet(vertex);
        }
        for (E edge : edges) {
            int uMST = disjointSets.findSet(edge.from());
            int vMST = disjointSets.findSet(edge.to());
            if (uMST != vMST) {
                edgesOfMST.add(edge);
                disjointSets.union(edge.from(), edge.to());
                // Adding vertices to the final MST.
            }
        }
        if (edgesOfMST.size() == listOfVertices.size() - 1 || listOfVertices.size() == 0) {
            return new MinimumSpanningTree.Success<>(edgesOfMST);
        } else {
            return new MinimumSpanningTree.Failure<>();
        }
    }
}
