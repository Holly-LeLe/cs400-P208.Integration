import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Backend implements BackendInterface {


    private GraphADT<String, Double> graph;
    private List<String> Nodes = new ArrayList<>();

    /*
    * constructor.
    * @param graph object to store the backend's graph data
    */
    public Backend(GraphADT<String,Double> graph) {
        this.graph = graph;
    }

    /**
     * Loads graph data from a dot file. If a graph was previously loaded, this
     * method should first delete the contents (nodes and edges) of the existing
     * graph before loading a new one.
     * @param filename the path to a dot file to read graph data from
     * @throws IOException if there was any problem reading from this file
     */
    public void loadGraphData(String filename) throws IOException {

        String filePath = filename;

        cleanGraph();

        // "Amsterdam" -> "Cologne" [minutes=157];
        String regex = "\"([^\"]+)\"\\s*->\\s*\"([^\"]+)\"\\s*\\[minutes=(\\d+)\\]";
        // compile the regex
        Pattern pattern = Pattern.compile(regex);

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Matcher matcher = pattern.matcher(line);


                if(matcher.find()) {
                    String start = matcher.group(1);
                    String end = matcher.group(2);
                    double time = Double.parseDouble(matcher.group(3));

                    // Insert the nodes and edge

                    // bug: I forget that one node may added multiple times.
                    if(graph.insertNode(start)) {
                        Nodes.add(start);
                    }

                    if(graph.insertNode(end)) {
                        Nodes.add(end);
                    }

                    // edge
                    graph.insertEdge(start, end, time);
                }
            }
        } catch (IOException e) {
            throw new IOException("Can't read from file: " + e.getMessage());
        }
    }

    /**
     * removing all nodes and edges to clear graph.
     */
    public void cleanGraph() {
        for (String node : Nodes) {
            graph.removeNode(node);
        }
        // clear the list of nodes
        this.Nodes = new ArrayList<>();
        // this can't clean the original node in the graph like what happens in placeholder
        // only clean what Backend has added to the graph. It is not a bug.
    }


    /**
     * Returns a list of all locations in the graph.
     * @return list of all location names
     */
    public List<String> getListOfAll() {
        return Nodes;
    }

    /**
     * Return the sequence of locations along the shortest path from start to 
     * end, or an empty list if no such path exists.
     * @param start the start of the path
     * @param end the end of the path
     * @return a list with the nodes along the shortest path from start to end,
     *         or an empty list if no such path exists
     */
    public List<String> findLocationsOnShortestPath(String start, String end) { 
        try {
            List<String> Locations = graph.shortestPathData(start, end);
            return Locations;
        } catch (NoSuchElementException e) {
            return new ArrayList<>(); 
            // return an empty list
        }
    } 

    /**
     * Return the times in minutes between each two nodes on the shortest path 
     * from start to end, or an empty list if no such path exists.
     * @param start the start of the path
     * @param end the end of the path
     * @return a list with the times in minutes between two nodes along the 
     * shortest path from start to end, or an empty list if no such path exists
     */
    public List<Double> findTimesOnShortestPath(String start, String end)   {
        // create a list to store
        List<Double> times = new ArrayList<>();
        try {
            List<String> Locations = graph.shortestPathData(start, end);
            // loop through and get every weight between this node and the next node.
            for(int i = 0; i < Locations.size() - 1; i++) {

                String node = Locations.get(i);
                String nextNode = Locations.get(i + 1);
                double time = graph.getEdge(node, nextNode);
                times.add(time);
            }
        
        } catch (NoSuchElementException e) {
            return new ArrayList<>();
             // return an empty
        }

        return times;

        
    };

    /**
     * Returns the list of locations that can be reached when starting from the 
     * provided start, and travelling maxTime in minutes.
     * @param start the location to find the reachable locations from
     * @param maxTime is the maximum time it can take to get from from the 
     * start to report a location
     * @return the list of locations that can be reached from start in maxTime 
     * or fewer minutes
     * @throws NoSuchElementException if start does not exist
     */

    // bug: if start not in the graph:
    // graph.shortestPathCost(start, node) will still throw NoSuchElementException
    // which makes it continue the loop, and return still return an empty list like other normal case.
    // so we need to check if start is in the graph first.
    public List<String> getReachableFromWithin(String start, double maxTime) throws NoSuchElementException {
        if (!graph.containsNode(start)) {
            throw new NoSuchElementException("Start node does not exist in the graph.");
        }


        List<String> reachable = new ArrayList<>(); 
        for (String node : Nodes) {
            try {
                // get time for every node.
                double time = graph.shortestPathCost(start, node);
                if (time <= maxTime) {
                    reachable.add(node);
                }
            } catch (NoSuchElementException e) {
                // if not, simply skip it.
            }
        }
        return reachable;
    };

}
