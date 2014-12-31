/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eiafr.visudna.distanceFrom;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.gephi.filters.spi.FilterProperty;
import org.gephi.filters.spi.NodeFilter;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.Node;
import org.gephi.graph.api.NodeIterator;

/**
 *
 * @author maria
 */
public class MyFilter implements NodeFilter {
    
    /**
     * Stores a node and its depth
     */
    private class NodeDepth {
        private Node node;
        private int depth;
        
        public NodeDepth(Node node) {
            this.node = node;
            this.depth = 0;
        }
        
        private String getNodeName() {
            return (String) getNodeAttr(node, "name");
        }
        
        @Override
        public boolean equals(Object o){
            NodeDepth d = (NodeDepth) o;
            String name1 = (String) this.node.getAttributes().getValue("name");
            String name2 = (String) d.node.getAttributes().getValue("name");
            return name1.equals(name2);
        }
    }
    
    private String nodeName;
    private String oldNodeName;
    private int distance;
    private Map<String, NodeDepth> nodesDistance;  // Map and not set because we need to access it via the node and not and not the NodeDepth, String because if we change the distance, the referenced node objects may change.
    
    public MyFilter() {
        this.nodeName = "";
        this.oldNodeName = "";
        this.distance = 0;
    }

    static int nbInit = 0;
    
    /**
     * Initializes the filter. Is called at every parameter change.
     * @param graph
     * @return true if the filter was successfully initialized, false otherwise
     */
    @Override
    public boolean init(Graph graph) {
        if (!nodeName.equals("")
                && oldNodeName.equals(nodeName)) // if the nodeName didn't change, no need to re-init.
            return true;
        
        System.out.println("INIT! :" + (++nbInit));
        nodesDistance = new HashMap<String, NodeDepth>();
        
         NodeIterator nodesItr = graph.getNodes().iterator();
         
         while (nodesItr.hasNext()) {
             Node node = nodesItr.next();
             if (getNodeAttr(node, "name").equals(nodeName)) {
                 initDist(graph, node);
                 graph.readUnlockAll(); // Locks used when calling NodeIterator. Do not forget to release them.
                 return true;
             }
         }
        graph.readUnlockAll();
        return false; // Return false because we could not initialize the distances properly
    }
    
    /**
     * Initializes the distance set. Uses a breadth-first algorithm.
     * @param graph
     * @param rootNode 
     */
    private void initDist(Graph graph, Node rootNode) {
        Queue<NodeDepth> queue = new LinkedList<NodeDepth>();
        queue.add(new NodeDepth(rootNode));
        
        while(!queue.isEmpty()) {
            NodeDepth t = queue.remove();
            int newDepth = t.depth + 1;
            for (Node neighbor : graph.getNeighbors(t.node) ) {
                NodeDepth nd = new NodeDepth(neighbor);
                nd.depth = newDepth;
                if (!nodesDistance.containsKey(nd.getNodeName())) {
                    String nName = (String) getNodeAttr(neighbor, "name");
                    nodesDistance.put(nName, nd);
                    queue.add(nd);
                }
            }
        }
    }

    /**
     * Evaluates if a node satisfies the filter. Here if the node is a the right distance from the source node.
     * @param graph
     * @param node
     * @return true if the node satisfies the filter, false otherwise
     */
    @Override
    public boolean evaluate(Graph graph, Node node) {
        if (nodeName.equals("")) return true;
        String name = (String) getNodeAttr(node, "name");
        
        if (nodesDistance.containsKey(name)) { // We have to test this since the graph is disjoint
            return this.nodeName.equals(name) || (nodesDistance.get(name).depth <= distance);
        }

        return false;
    }

    @Override
    public void finish() {
        return;
    }

    @Override
    public String getName() {
        return "Distance Filter";
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
        this.oldNodeName = this.nodeName;
        this.nodeName = nodeName;
    }

    /**
     * Set new value for distance. If the value is smaller than 0, then the new value is set to 0.
     * @param distance 
     */
    public void setDistance(int distance) {
        if (distance >= 0) {
            this.distance = distance;
        } else {
            this.distance = 0;
        }
    }
    
    /* Returns the value stored int the node "attr" attribute*/
    private Object getNodeAttr(Node node, String attr) {
        return node.getAttributes().getValue(attr);
    }
}