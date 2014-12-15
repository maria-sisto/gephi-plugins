/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eiafr.visudna.distanceFrom;

import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.gephi.filters.spi.FilterProperty;
import org.gephi.filters.spi.NodeFilter;
import org.gephi.graph.api.Edge;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.Node;
import org.omg.CORBA.DATA_CONVERSION;

/**
 *
 * @author maria
 */
public class MyFilter implements NodeFilter {
    
    private String nodeName;
    private int distance;
    private Set<NodeDepth> nodesDistance;
    
    public MyFilter() {
        this.nodeName = "";
        this.distance = 0;
    }
    
    private class NodeDepth {
        private Node node;
        private int depth;
        
        public NodeDepth(Node node) {
            this.node = node;
            this.depth = 0;
        }
        
        @Override
        public boolean equals(Object o){
            NodeDepth d = (NodeDepth) o;
            String name1 = (String) this.node.getAttributes().getValue("name");
            String name2 = (String) d.node.getAttributes().getValue("name");;
            return name1.equals(name2);
        }
    }

    @Override
    public boolean init(Graph graph) {
        return true;
    }

    @Override
    public boolean evaluate(Graph graph, Node node) {
        if (nodeName.equals("")) return true;
        String name = (String) node.getAttributes().getValue("name");
        
        return this.nodeName.equals(name) || isAtDistance(graph, node);
    }
    
    // BFS algorithm (because unweighted graph)
    private boolean isAtDistance(Graph graph, Node node) {
        if (distance == 0) return false;
        Queue<NodeDepth> queue = new LinkedList<NodeDepth>();
        Set<NodeDepth> set = new HashSet<NodeDepth>();
        queue.add(new NodeDepth(node));
        
        while(!queue.isEmpty()) {
            NodeDepth t = queue.remove();
            String curName = (String) t.node.getAttributes().getValue("name");
            if (curName != null) {
                if (curName.equals(nodeName)) {
                    return t.depth <= distance;
                }
            }
            
            int newDepth = t.depth + 1;
            if (newDepth <= distance) {
                for (Node neighbor : graph.getNeighbors(t.node) ) {
                    NodeDepth nd = new NodeDepth(neighbor);
                    nd.depth = newDepth;
                    if (!set.contains(nd)) {
                        set.add(nd);
                        queue.add(nd);
                    }
                }
            }
        }
        
        return false;
    }

    @Override
    public void finish() {
        return;
    }

    @Override
    public String getName() {
        return "My Filter";
    }

    @Override
    public FilterProperty[] getProperties() {
        try {
            return new FilterProperty[]{
                FilterProperty.createProperty(this, String.class, "nodeName"),
                FilterProperty.createProperty(this, int.class, "distance")
            };
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(MyFilter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public String getNodeName() {
        return nodeName;
    }
    
    public int getDistance() {
        return distance;
    }
 
    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
}