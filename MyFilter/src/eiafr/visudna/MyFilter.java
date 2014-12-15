/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eiafr.visudna;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.gephi.filters.spi.FilterProperty;
import org.gephi.filters.spi.NodeFilter;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.Node;

/**
 *
 * @author maria
 */
public class MyFilter implements NodeFilter {
    
    private int maxDegree = 1;

    @Override
    public boolean init(Graph graph) {
        return true;
    }

    @Override
    public boolean evaluate(Graph graph, Node node) {
        
        return graph.getDegree(node) <= maxDegree;
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
                FilterProperty.createProperty(this, int.class, "maxDegree")
            };
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(MyFilter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public int getMaxDegree() {
        return maxDegree;
    }
 
    public void setMaxDegree(int maxDegree) {
        this.maxDegree = maxDegree;
    }
 
}