/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eiafr.visudna.patient;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.gephi.filters.spi.FilterProperty;
import org.gephi.filters.spi.NodeFilter;
import org.gephi.graph.api.Edge;
import org.gephi.graph.api.EdgeIterable;
import org.gephi.graph.api.EdgeIterator;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.Node;
import org.gephi.graph.api.NodeIterator;

/**
 * 
 * @author Maria Sisto
 */
public class PatientFilter implements NodeFilter {

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
        public boolean equals(Object o) {
            NodeDepth d = (NodeDepth) o;
            String name1 = (String) this.node.getAttributes().getValue("name");
            String name2 = (String) d.node.getAttributes().getValue("name");
            return name1.equals(name2);
        }
    }

    private class AffectedNode {

        private Node node;
        private Map<String, NodeDepth> nodesDistance;

        public AffectedNode(Node node) {
            this.node = node;
            this.nodesDistance = new HashMap<String, NodeDepth>();
        }

        private String getNodeName() {
            return (String) getNodeAttr(node, "name");
        }

        @Override
        public boolean equals(Object o) {
            NodeDepth d = (NodeDepth) o;
            String name1 = (String) this.node.getAttributes().getValue("name");
            String name2 = (String) d.node.getAttributes().getValue("name");
            return name1.equals(name2);
        }
    }

    private String patient;
    private String oldPatient;
    private String phenotype;
    private String oldPhenotype;
    private int distance;
    private Map<String, Node> affectedGenes;
    private Map<String, AffectedNode> patientNodes;

    public PatientFilter() {
        this.patient = "";
        this.phenotype = "";
        this.distance = 0;

        this.affectedGenes = new HashMap<String, Node>();
        this.patientNodes = new HashMap<String, AffectedNode>();
    }

    /**
     * Initializes the filter. Is called at every parameter change.
     *
     * @param graph
     * @return true if the filter was successfully initialized, false otherwise
     */
    @Override
    public boolean init(Graph graph) {
        //if (!patient.equals("") && oldPatient.equals(patient)) {
            //if (!phenotype.equals("") && oldPhenotype.equals(phenotype)) {
                //return true;
            //} else {
               // System.out.println("relaod phenotype");
                initPheno(graph); // A faire a chaque changement de patient
            //}
        //} else {
            //System.out.println("reload patient");
            initPatient(graph); // A faire a chanque changement de phénotype
        //}
        
        graph.readUnlockAll();
        return true; // Return false because we could not initialize the distances properly
    }

    /**
     * Initializes the set containing the nodes of the patient.
     *
     * @param graph
     */
    private void initPatient(Graph graph) {
        this.patientNodes = new HashMap<String, AffectedNode>();
        
        NodeIterator nodesItr = graph.getNodes().iterator();

        while (nodesItr.hasNext()) {
            Node node = nodesItr.next();
            int patientNb = (int) Double.parseDouble((String) getNodeAttr(node, "patientNb"));
            for (int i = 0; i < patientNb; i++) {
                String pat = (String) getNodeAttr(node, "patient" + i);
                if (patient.equals(pat)) {
                    AffectedNode aNode = new AffectedNode(node);
                    aNode.nodesDistance = initDist(graph, node);
                    patientNodes.put(aNode.getNodeName(), aNode);
                }
            }
        }
    }
    
    private void colorNode(Graph graph, Node node, float r, float g, float b) {
        node.getNodeData().setColor(r, g, b);
        EdgeIterator edgeItr = graph.getEdges().iterator();
        
        while(edgeItr.hasNext()) {
            Edge e = edgeItr.next();
            e.getEdgeData().setColor(1, 1, 1);
        }
    }

    /**
     * Initializes the set containing the nodes having the given phenotype.
     *
     * @param graph
     */
    private void initPheno(Graph graph) {
        this.affectedGenes = new HashMap<String, Node>();
        NodeIterator nodesItr = graph.getNodes().iterator();

        while (nodesItr.hasNext()) {
            Node node = nodesItr.next();
            int phenoNb = (int) Double.parseDouble((String) getNodeAttr(node, "phenotypeNb"));
            for (int i = 0; i < phenoNb; i++) {
                String attr = "phenotype" + i;
                String pheno = (String) getNodeAttr(node, attr);
                if (pheno != null && pheno.equals(this.phenotype)) {
                    String name = (String) getNodeAttr(node, "name");
                    affectedGenes.put(name, node);
                }
            }
        }
    }

    /**
     * Initializes the distance set. Uses a breadth-first algorithm.
     *
     * @param graph
     * @param rootNode
     */
    private Map<String, NodeDepth> initDist(Graph graph, Node rootNode) {
        Queue<NodeDepth> queue = new LinkedList<NodeDepth>();
        Map<String, NodeDepth> nodesDistance = new HashMap<String, NodeDepth>();
        queue.add(new NodeDepth(rootNode));

        while (!queue.isEmpty()) {
            NodeDepth t = queue.remove();
            int newDepth = t.depth + 1;
            for (Node neighbor : graph.getNeighbors(t.node)) {
                NodeDepth nd = new NodeDepth(neighbor);
                nd.depth = newDepth;
                if (!nodesDistance.containsKey(nd.getNodeName())) {
                    String nName = (String) getNodeAttr(neighbor, "name");
                    nodesDistance.put(nName, nd);
                    queue.add(nd);
                }
            }
        }
        return nodesDistance;
    }

    /**
     * Evaluates if a node satisfies the filter.
     *
     * @param graph
     * @param node
     * @return true if the node satisfies the filter, false otherwise
     */    
    @Override
    public boolean evaluate(Graph graph, Node node) {
        String name = (String) getNodeAttr(node, "name");
        System.out.print(name + ": ");
        
        if(patientNodes.containsKey(name)) { // On affihche tous les gènes variants du patient
            colorNode(graph, node, 1, 0, 0);
            return true;
        }
        
        if (affectedGenes.containsKey(name)) { // On affiche les gènes affectés à une distance d'une gène du patient
            for(AffectedNode an : patientNodes.values()) {
                if (an.nodesDistance.containsKey(name)) {
                    if(an.nodesDistance.get(name).depth <= distance) {
                        colorNode(graph, node, 0, 1, 0);
                        return true;
                    }
                }
            }
        }
        colorNode(graph, node, 0.6f, 0.6f, 0.6f); // reset original color
        return false;
    }

    @Override
    public void finish() {
        return;
    }

    @Override
    public String getName() {
        return "Patient data filter";
    }

    @Override
    public FilterProperty[] getProperties() {
        try {
            return new FilterProperty[]{
                FilterProperty.createProperty(this, String.class, "phenotype"),
                FilterProperty.createProperty(this, String.class, "patient"),
                FilterProperty.createProperty(this, int.class, "distance")
            };
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(PatientFilter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public int getDistance() {
        return distance;
    }

    /**
     * Set new value for distance. If the value is smaller than 0, then the new
     * value is set to 0.
     *
     * @param distance
     */
    public void setDistance(int distance) {
        if (distance >= 0) {
            this.distance = distance;
        } else {
            this.distance = 0;
        }
    }

    public String getPatient() {
        return patient;
    }

    public void setPatient(String patient) {
        this.oldPatient = this.patient;
        this.patient = patient;
    }

    public String getPhenotype() {
        return phenotype;
    }

    public void setPhenotype(String phenotype) {
        this.oldPhenotype = this.phenotype;
        this.phenotype = phenotype;
    }

    /* Returns the value stored int the node "attr" attribute*/
    private Object getNodeAttr(Node node, String attr) {
        return node.getAttributes().getValue(attr);
    }
}
