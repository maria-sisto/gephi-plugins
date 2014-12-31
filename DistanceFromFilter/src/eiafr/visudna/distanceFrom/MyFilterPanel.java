/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eiafr.visudna.distanceFrom;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.gephi.filters.spi.FilterProperty;

/**
 *
 * @author maria
 */
public class MyFilterPanel extends JPanel implements DocumentListener {
 
    private MyFilter filter;
    private JTextField geneNameField;
    private JTextField distanceFiled;
 
    public MyFilterPanel(MyFilter filter) {
        super();
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        this.filter = filter;
 
        this.geneNameField = new JTextField(filter.getNodeName());
        this.geneNameField.getDocument().addDocumentListener(this);
        
        this.distanceFiled = new JTextField("" + filter.getDistance());
        this.distanceFiled.getDocument().addDocumentListener(this);
        c.fill = GridBagConstraints.HORIZONTAL;
        
        c.gridx = 0;
        c.gridy = 0;
        this.add(new JLabel("Gene Name: "), c);
        
        c.gridx = 1;
        geneNameField.setColumns(8);
        this.add(this.geneNameField, c);
        
        c.gridx = 0;
        c.gridy = 1;
        this.add(new JLabel("Distance: "), c);
        
        c.gridx = 1;
        this.add(this.distanceFiled, c);
        this.setVisible(true);
    }

    @Override
    public void insertUpdate(DocumentEvent de) {
        changedUpdate(de);
    }

    @Override
    public void removeUpdate(DocumentEvent de) {
        return;
    }

    @Override
    public void changedUpdate(DocumentEvent de) {
        FilterProperty geneName = filter.getProperties()[0];
        geneName.setValue(geneNameField.getText());
        
        FilterProperty distance = filter.getProperties()[1];
        int newDist = 0;
        try {
            newDist =  Integer.parseInt(distanceFiled.getText());   
        } catch (Exception e) {
            System.err.println("Invalid distance value");
        }
        distance.setValue(newDist);
    }
}
