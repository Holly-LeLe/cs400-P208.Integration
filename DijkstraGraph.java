import java.util.PriorityQueue;
import java.util.List;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * This class extends the BaseGraph data structure with additional methods for
 * computing the total cost and list of node data along the shortest path
 * connecting a provided starting to ending nodes. This class makes use of
 * Dijkstra's shortest path algorithm.
 */
public class DijkstraGraph<NodeType, EdgeType extends Number>
        extends BaseGraph<NodeType, EdgeType>
        implements GraphADT<NodeType, EdgeType> {

    /**
     * While searching for the shortest path between two nodes, a SearchNode
     * contains data about one specific path between the start node and another
     * node in the graph. The final node in this path is stored in its node
     * field. The total cost of this path is stored in its cost field. And the
     * predecessor SearchNode within this path is referenced by the predecessor
     * field (this field is null within the SearchNode containing the starting
     * node in its node field).
     *
     * SearchNodes are Comparable and are sorted by cost so that the lowest cost
     * SearchNode has the highest priority within a java.util.PriorityQueue.
     */
    protected class SearchNode implements Comparable<SearchNode> {
        public Node node;
        public double cost;
        public SearchNode pred;

        public SearchNode(Node startNode) {
            this.node = startNode;
            this.cost = 0;
            this.pred = null;
        }

        public SearchNode(SearchNode pred, Edge newEdge) {
            this.node = newEdge.succ;
            this.cost = pred.cost + newEdge.data.doubleValue();
            this.pred = pred;
        }

        public int compareTo(SearchNode other) {
            if (cost > other.cost)
                return +1;
            if (cost < other.cost)
                return -1;
            return 0;
        }
    }

    /**
     * Constructor that sets the map that the graph uses.
     */
    public DijkstraGraph() {
        super(new HashTableMap<>());
    }

    /**
     * Insert a new directed edge with a non-negative weight into the graph. If 
     * an edge between pred and succ already exists, update the data stored in 
     * that edge to the new weight.
     * 
     * @param pred is the data contained in the new edge's predecesor node
     * @param succ is the data contained in the new edge's succ node
     * @param weight is the non-negative data to be stored in the new edge
     * @return true if the edge could be inserted or updated, or false if the 
     * pred or succ data are not found in any graph nodes or the weight 
     * specified is negative.
     */
    @Override
    public boolean insertEdge(NodeType pred, NodeType succ, EdgeType weight) {
        if (weight.doubleValue() < 0)
            return false;
        return super.insertEdge(pred, succ, weight);
    }

    /**
     * This helper method creates a network of SearchNodes while computing the
     * shortest path between the provided start and end locations. The
     * SearchNode that is returned by this method represents the end of the
     * shortest path that is found: it's cost is the cost of that shortest path,
     * and the nodes linked together through predecessor references represent
     * all of the nodes along that shortest path (ordered from end to start).
     *
     * @param start the starting node for the path
     * @param end   the destination node for the path
     * @return SearchNode for the final end node within the shortest path
     * @throws NoSuchElementException if either the start or the end node
     * cannot be found, or there is no path from start node to end node
     * @throws NullPointerException if the start or end node are null
     */
    protected SearchNode computeShortestPath(Node start, Node end) {

        if (start == null || end == null)
            throw new NullPointerException();

        PriorityQueue<SearchNode> queue = new PriorityQueue<>();

        HashTableMap<NodeType, Boolean> visited =
            new HashTableMap<>();

        queue.add(new SearchNode(start));

        while (!queue.isEmpty()) {

            SearchNode current = queue.poll();

            if (visited.containsKey(current.node.data))
                continue;

            visited.put(current.node.data, true);

            if (current.node == end)
                return current;

            for (Edge edge : current.node.edgesLeaving) {

                if (!visited.containsKey(edge.succ.data)) {

                    SearchNode next =
                        new SearchNode(current, edge);

                    queue.add(next);
                }
            }
        }

        throw new NoSuchElementException();
    }

    /**
     * Returns the list of data values from nodes along the shortest path
     * from the node with the provided start value through the node with the
     * provided end value. This list of data values starts with the start
     * value, ends with the end value, and contains intermediary values in the
     * order they are encountered while traversing this shortest path. This
     * method uses Dijkstra's shortest path algorithm to find this solution.
     *
     * @param start the data item in the starting node for the path
     * @param end   the data item in the destination node for the path
     * @return list of data item from nodes along this shortest path
     * @throws NoSuchElementException if either the start or the end node
     * cannot be found, or there is no path from start node to end node
     * @throws NullPointerException if the start or end node are null
     */
    public List<NodeType> shortestPathData(NodeType start, NodeType end) {

        SearchNode path =
            computeShortestPath(nodes.get(start), nodes.get(end));

        LinkedList<NodeType> result = new LinkedList<>();

        while (path != null) {
            result.addFirst(path.node.data);
            path = path.pred;
        }

        return result;
    }

    /**
     * Returns the cost of the path (sum over edge weights) of the shortest
     * path from the node containing the start data to the node containing the
     * end data. This method uses Dijkstra's shortest path algorithm to find
     * this solution.
     *
     * @param start the data item in the starting node for the path
     * @param end   the data item in the destination node for the path
     * @return the cost of the shortest path between these nodes
     * @throws NoSuchElementException if either the start or the end node
     * cannot be found, or there is no path from start node to end node
     * @throws NullPointerException if the start or end node are null
     */
    public double shortestPathCost(NodeType start, NodeType end) {

        return computeShortestPath(
            nodes.get(start),
            nodes.get(end)
        ).cost;
    }


    /**
     * Tests shortest path data and cost on a directed graph with multiple paths.
     */
    @Test
    public void testShortestPathLectureExample() {
        DijkstraGraph<String, Integer> graph = new DijkstraGraph<>();

        graph.insertNode("A");
        graph.insertNode("B");
        graph.insertNode("C");
        graph.insertNode("D");
        graph.insertNode("E");

        graph.insertEdge("A", "B", 4);
        graph.insertEdge("A", "C", 2);
        graph.insertEdge("C", "B", 1);
        graph.insertEdge("B", "D", 5);
        graph.insertEdge("C", "D", 8);
        graph.insertEdge("C", "E", 10);
        graph.insertEdge("D", "E", 2);

        assertEquals(10.0, graph.shortestPathCost("A", "E"));
        assertEquals(List.of("A", "C", "B", "D", "E"),
                graph.shortestPathData("A", "E"));
    }

    /**
     * Tests a different start and end node using the same graph structure.
     */
    @Test
    public void testShortestPathDifferentStartEnd() {
        DijkstraGraph<String, Integer> graph = new DijkstraGraph<>();

        graph.insertNode("A");
        graph.insertNode("B");
        graph.insertNode("C");
        graph.insertNode("D");
        graph.insertNode("E");

        graph.insertEdge("A", "B", 4);
        graph.insertEdge("A", "C", 2);
        graph.insertEdge("C", "B", 1);
        graph.insertEdge("B", "D", 5);
        graph.insertEdge("C", "D", 8);
        graph.insertEdge("C", "E", 10);
        graph.insertEdge("D", "E", 2);

        assertEquals(8.0, graph.shortestPathCost("C", "E"));
        assertEquals(List.of("C", "B", "D", "E"),
                graph.shortestPathData("C", "E"));
    }

    /**
     * Tests that a NoSuchElementException is thrown when both nodes exist,
     * but no directed path connects the start node to the end node.
     */
    @Test
    public void testNoDirectedPathThrowsException() {
        DijkstraGraph<String, Integer> graph = new DijkstraGraph<>();

        graph.insertNode("A");
        graph.insertNode("B");
        graph.insertNode("C");

        graph.insertEdge("A", "B", 3);

        assertThrows(NoSuchElementException.class, () -> {
            graph.shortestPathData("B", "C");
        });
    }

}
