/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eiafr.visudna.plugin;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.Node;
import org.openide.util.Lookup;

/**
 *
 * @author maria
 */
public class InfoPanel extends JPanel{
    GridBagConstraints c = new GridBagConstraints();
    
    public InfoPanel() {
        this.setLayout(new GridBagLayout());
        
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        this.add(new JLabel("Informations: \n"), c);
        
    }
    
    public void displayInfos() {
        System.out.println("displaying nodes...");
        try {
            GraphModel gm = Lookup.getDefault().lookup(GraphController.class).getModel();
            Graph graph = gm.getUndirectedGraph();
            
            int nodeTot = graph.getNodeCount();
            
            c.gridy = 1;
            this.add(new JLabel("Number of nodes: " + nodeTot), c);
            
            c.gridy = 2;
            this.add(new JLabel("Number of edges: " + graph.getEdgeCount()), c);
            
            int highDegree = 0; // degree higher thant 200
            int midDegree = 0; // degree between 100 and 200
            int lowDegree = 0; // degree lower thant 100
            
            Map<Integer, Integer> nodeDegree = nodeDegree(graph);
            
            for (int degree: nodeDegree.keySet()) {
                if (degree > 200) {
                    highDegree += nodeDegree.get(degree);
                } else if (degree < 100) {
                    lowDegree += nodeDegree.get(degree);
                } else {
                    midDegree += nodeDegree.get(degree);
                }
            }
            
            c.gridy = 3;
            this.add(new JLabel("Number of nodes with degree > 200: " + highDegree), c);
            c.gridy = 4;
            this.add(new JLabel("Number of nodes with degree between 100 and 200: " + midDegree), c);
            c.gridy = 5;
            this.add(new JLabel("Number of nodes with degree < 100: " + lowDegree), c);
            
        } catch (NullPointerException e) {
            System.out.println("Warning: No graph is loaded");
            //TODO: show a pop-up for that
        }
    }
    
    public Map<Integer, Integer> nodeDegree(Graph graph) {
        Map<Integer, Integer> nodesDegree= new HashMap<Integer, Integer>(); // <Degré, nombre de noeuds ayant de degré>
        
        for(Node node : graph.getNodes()) {
            int deg = graph.getDegree(node);
            if (!nodesDegree.containsKey(deg)) {
                nodesDegree.put(deg, 1);
            } else {
                nodesDegree.put(deg, (nodesDegree.get(deg) + 1));
            }
        }
        
        return nodesDegree;
    }
}
