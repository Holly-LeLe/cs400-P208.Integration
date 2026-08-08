import java.util.List;
import java.util.NoSuchElementException;

public class Frontend implements FrontendInterface {

    private BackendInterface backend;

    public Frontend(BackendInterface backend) {
        this.backend = backend;
    }

    @Override
    public String generateShortestPathPromptHTML() {
        return "<label>Start Location:</label>"
            + "<input id=\"start\" type=\"text\">"
            + "<label>End Location:</label>"
            + "<input id=\"end\" type=\"text\">"
            + "<button>Find Shortest Path</button>";
    }


    @Override
    public String generateShortestPathResponseHTML(String start, String end) {

        List<String> locations =
            backend.findLocationsOnShortestPath(start, end);

        List<Double> times =
            backend.findTimesOnShortestPath(start, end);


        if (locations == null || locations.size() == 0) {
            return "<p>No path found from "
                + start + " to " + end + "</p>";
        }


        String html = "<p>Shortest path from "
            + start + " to " + end + ":</p>";

        html += "<ol>";

        for (String location : locations) {
            html += "<li>" + location + "</li>";
        }

        html += "</ol>";


        double totalTime = 0;

        for (Double time : times) {
            totalTime += time;
        }

        html += "<p>Total time: " + totalTime + "</p>";

        return html;
    }


    @Override
    public String generateReachableFromWithinPromptHTML() {

        return "<label>From Location:</label>"
            + "<input id=\"from\" type=\"text\">"
            + "<label>Maximum Time:</label>"
            + "<input id=\"time\" type=\"text\">"
            + "<button>Reachable From Within</button>";
    }


    @Override
    public String generateReachableFromWithinResponseHTML(
            String start, double maxTime) {

        try {

            List<String> locations =
                backend.getReachableFromWithin(start, maxTime);


            if (locations == null || locations.size() == 0) {
                return "<p>No locations reachable</p>";
            }


            String html = "<p>Locations reachable from "
                + start + " within "
                + maxTime + " minutes:</p>";

            html += "<ul>";

            for (String location : locations) {
                html += "<li>" + location + "</li>";
            }

            html += "</ul>";

            return html;


        } catch (NoSuchElementException e) {

            return "<p>Start location not found</p>";
        }
    }
}
