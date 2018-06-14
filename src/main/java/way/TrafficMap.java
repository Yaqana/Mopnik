package way;


import br.zuq.osm.parser.OSMParser;
import br.zuq.osm.parser.model.OSM;
import br.zuq.osm.parser.model.OSMNode;
import br.zuq.osm.parser.model.Way;
import config.AppConfig;
import elements.MainFrame;
import org.apache.commons.lang3.math.NumberUtils;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.painter.Painter;
import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.Waypoint;
import org.jxmapviewer.viewer.WaypointPainter;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.Statement;
import java.util.*;


public class TrafficMap {
    private OSM osm = null;
    private static String tag = "distance";

    public TrafficMap() {
        try {
            osm = OSMParser.parse(new FileInputStream(AppConfig.getFile(AppConfig.getMapFilename())));
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    public void addGeopositions(RoutesMap routesMap) {
        for (OSMNode node : osm.getNodes()) {
            if (node.getAllTags().containsKey("ref")) {
                double distance = getDistance(node);
                if (distance == -1) { continue; }
                String refs[] = NameAdjuster.getWayNames(node);
                for (String ref : refs) {
                    if ((ref.contains("A") || ref.contains("S") && !ref.contains("S3"))) {
                        routesMap.addGeoposition(ref, distance,
                                new GeoPosition(Double.parseDouble(node.lat), Double.parseDouble(node.lon)));
                    }
                }
            }
        }

    }

    public Set<Waypoint> mileages() {
        Set<Waypoint> waypointSet = new HashSet<>();
        WaypointPainter<Waypoint> p = new WaypointPainter<Waypoint>();
        for (OSMNode node : osm.getNodes()) {
            if (node.getAllTags().containsKey("milestone")) {
                waypointSet.add(
                        new DefaultWaypoint(
                                new GeoPosition(Double.parseDouble(node.lat), Double.parseDouble(node.lon))));
            }
        }
        return waypointSet;
    }

    /*
    public List<RoutePainter> routes(RoutesMap routesMap) {
        Map<String, Set<OSMNode>> nodes = new HashMap<>();
        Comparator<OSMNode> osmNodeComparator =
                Comparator.comparingDouble(n -> Integer.parseInt(n.getAllTags().get("milestone")));
        for (Way way : osm.getWays()) {
            for (OSMNode node : way.nodes) {
                if (node.getAllTags().containsKey("milestone") && way.getAllTags().containsKey("ref")) {
                    String refs[] = way.getAllTags().get("ref").replaceAll("\\s+", "").split(";");
                    for (String ref : refs) {
                        if (nodes.containsKey(ref)) {
                            nodes.get(ref).add(node);
                        } else {
                            nodes.put(ref, new TreeSet<>(osmNodeComparator));
                        }
                    }
                }
            }
        }
        List<RoutePainter> res = new ArrayList<>();
        for (Map.Entry<String, Set<OSMNode>> entry : nodes.entrySet()) {
            if (!entry.getKey().equals("S8")) { // Milestones on S8 are incorrect.
                boolean first = true;
                OSMNode last = null;
                double lastMilestone = 0.0;
                for (OSMNode node : entry.getValue()) {
                    double newMilestone = Double.parseDouble(node.getAllTags().get("milestone"));

                    // This condition is to omit nodes that are to close to some other node.
                    // Milestone is an integer and two nodes being less than two kilometers apart
                    // may in fact be placed on the same crossroads.
                    if (!first && Integer.parseInt(last.getAllTags().get("milestone")) <
                            Integer.parseInt(node.getAllTags().get("milestone")) - 2) {
                        List<GeoPosition> track = Arrays.asList(
                                new GeoPosition(Double.parseDouble(last.lat), Double.parseDouble(last.lon)),
                                new GeoPosition(Double.parseDouble(node.lat), Double.parseDouble(node.lon)));
                        Route route = null;
                        if (routesMap != null) {
                            route = routesMap.find(entry.getKey(), lastMilestone);
                        }
                        res.add(new RoutePainter(track, route));
                    }
                    first = false;
                    last = node;
                    lastMilestone = newMilestone;
                }
            }
        }
        return res;
    } */

    private double getDistance(OSMNode node) {
        if (node.getAllTags().containsKey(tag)) {
            if (NumberUtils.isCreatable(node.getAllTags().get(tag))) {
                return Double.parseDouble(node.getAllTags().get(tag).replaceAll(",", "."));
            }
        }
        return -1.;
    }

}
