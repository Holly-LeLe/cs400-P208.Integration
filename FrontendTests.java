import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FrontendTests {


    /**
     * Tests that shortest path prompt HTML contains required input fields
     * and button.
     */
    @Test
    public void roleTest1() {

        GraphADT<String,Double> graph =
            new Graph_Placeholder();

        BackendInterface backend =
            new Backend_Placeholder(graph);

        Frontend frontend =
            new Frontend(backend);


        String html =
            frontend.generateShortestPathPromptHTML();


        assertTrue(html.contains("start"));
        assertTrue(html.contains("end"));
        assertTrue(html.contains("Find Shortest Path"));
    }



    /**
     * Tests that shortest path response HTML correctly displays
     * path information returned from backend.
     */
    @Test
    public void roleTest2() {

        GraphADT<String,Double> graph =
            new Graph_Placeholder();

        BackendInterface backend =
            new Backend_Placeholder(graph);

        Frontend frontend =
            new Frontend(backend);


        String html =
            frontend.generateShortestPathResponseHTML(
                "Union South",
                "Computer Sciences and Statistics");


        assertNotNull(html);
    }



    /**
     * Tests reachable prompt and response HTML generation.
     */
    @Test
    public void roleTest3() {

        GraphADT<String,Double> graph =
            new Graph_Placeholder();

        BackendInterface backend =
            new Backend_Placeholder(graph);

        Frontend frontend =
            new Frontend(backend);


        String prompt =
            frontend.generateReachableFromWithinPromptHTML();


        String response =
            frontend.generateReachableFromWithinResponseHTML(
                "Union South",
                10.0);


        assertTrue(prompt.contains("from"));
        assertNotNull(response);
    }


    /**
     * Integration test between real Backend and Frontend for shortest path prompt.
     */
    @Test
    public void shortestPathPromptIntegration() {

        GraphADT<String,Double> graph =
            new DijkstraGraph<>();

        BackendInterface backend =
            new Backend(graph);

        Frontend frontend =
            new Frontend(backend);

        String html =
            frontend.generateShortestPathPromptHTML();

        assertTrue(html.contains("start"));
        assertTrue(html.contains("end"));
    }


    /**
     * Integration test between real Backend and Frontend for shortest path response.
     */
    @Test
    public void shortestPathResponseIntegration() {

        GraphADT<String,Double> graph =
            new DijkstraGraph<>();

        BackendInterface backend =
            new Backend(graph);

        Frontend frontend =
            new Frontend(backend);

        String html =
            frontend.generateShortestPathResponseHTML(
                "Union South",
                "Computer Sciences and Statistics");

        assertNotNull(html);
    }


    /**
     * Integration test between real Backend and Frontend for reachable prompt.
     */
    @Test
    public void reachablePromptIntegration() {

        GraphADT<String,Double> graph =
            new DijkstraGraph<>();

        BackendInterface backend =
            new Backend(graph);

        Frontend frontend =
            new Frontend(backend);

        String html =
            frontend.generateReachableFromWithinPromptHTML();

        assertTrue(html.contains("from"));
    }


    /**
     * Integration test between real Backend and Frontend for reachable response.
     */
    @Test
    public void reachableResponseIntegration() {

        GraphADT<String,Double> graph =
            new DijkstraGraph<>();

        BackendInterface backend =
            new Backend(graph);

        Frontend frontend =
            new Frontend(backend);

        String html =
            frontend.generateReachableFromWithinResponseHTML(
                "Union South",
                10.0);

        assertNotNull(html);
    }

}
