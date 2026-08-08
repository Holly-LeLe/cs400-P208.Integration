import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class BackendTests {

    public static void main(String[] args) {
        BackendTests tests = new BackendTests();
        tests.roleTest1();
        tests.roleTest2();
        tests.roleTest3();
    }

    /**
     * Tests findLocationsOnShortestPath and findTimesOnShortestPath 
     * and edge case if there is a not exist path.
     * use Graph_Placeholder: Union South - CS&Stats -  Weeks Hall
     */
    @Test
    public void roleTest1() {
        // GraphADT<NodeType, EdgeType extends Number>
        GraphADT<String, Double> graph = new Graph_Placeholder();
        Backend backend = new Backend(graph);

        String start = "Union South";
        String end = "Weeks Hall for Geological Sciences";

        List<String> locations = backend.findLocationsOnShortestPath(
            start, end);
        // Correct path is: Union South - CS&Stats -  Weeks Hall
        List<String> expect = List.of("Union South", "Computer Sciences and Statistics", "Weeks Hall for Geological Sciences");
        assertEquals(expect, locations);

        List<Double> times = backend.findTimesOnShortestPath(
            start, end);
        assertEquals(List.of(1.0, 2.0), times);
        
        // no path exists for a location that isn't in the graph
        List<String> empty = backend.findLocationsOnShortestPath(
            "My_home", end);
        assertTrue(empty.isEmpty());
    }

    /**
     * Tests loadGraphData and getListOfAll by loading europeanRail.dot.
     * 
     * Note: Graph_Placeholder caps out at 4 nodes, so only the very first new
     * location successfully gets inserted.
     */
    @Test
    public void roleTest2() {
        Backend backend = new Backend(new Graph_Placeholder());

        try {
            backend.loadGraphData("europeanRail.dot");
        } catch (Exception e) {
            System.out.println("loadGraphData exception in roleTest2() : " + e.getMessage());
        }
        // path.size() < 4，so there will only first node be added to the graph.
        // getListOfAll return only the first node in the graph. 
        // "Amsterdam" -> "Cologne" [minutes=157];
        List<String> all = backend.getListOfAll();

        // System.out.println("roleTest2() list" + all);
        assertEquals(List.of("Amsterdam"), all);

    }

    /**
     * Tests getReachableFromWithin: after loading the graph, checks that
     * a known location is in reachable list.
     * And a nonexistent start location throws NoSuchElementException.
     */
    @Test
    public void roleTest3() {
        Backend backend = new Backend(new Graph_Placeholder());
        // load the graph data
        try {
            backend.loadGraphData("europeanRail.dot");
        } catch (Exception e) {
            System.out.println("loadGraphData exception in roleTest3() : " + e.getMessage());
        }
        
        // only "Amsterdam" is reachable. as only one node is added to the graph.
        List<String> reachable = backend.getReachableFromWithin("Amsterdam", 5.0);
        assertEquals(List.of("Amsterdam"), reachable);

        // should throw NoSuchElementException for a nonexistent start location
        assertThrows(NoSuchElementException.class, () ->
            backend.getReachableFromWithin("Nonexistent Place", 100.0));
    }
}

