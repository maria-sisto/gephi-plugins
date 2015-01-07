/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eiafr.visudna.patient;

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
 * @author Maria Sisto
 */
public class PatientFilterPanel extends JPanel implements DocumentListener {
 
    private PatientFilter filter;
    private JTextField patientNameField;
    private JTextField phenotypeField;
    private JTextField distanceFiled;
 
    public PatientFilterPanel(PatientFilter filter) {
        super();
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        this.filter = filter;
 
        this.patientNameField = new JTextField(filter.getPatient());
        this.patientNameField.getDocument().addDocumentListener(this);
        
        this.phenotypeField = new JTextField(filter.getPhenotype());
        this.phenotypeField.getDocument().addDocumentListener(this);
        
        this.distanceFiled = new JTextField("" + filter.getDistance());
        this.distanceFiled.getDocument().addDocumentListener(this);
        c.fill = GridBagConstraints.HORIZONTAL;
        
        c.gridx = 0;
        c.gridy = 0;
        this.add(new JLabel("Patient Name: "), c);
        
        c.gridx = 1;
        patientNameField.setColumns(8);
        this.add(this.patientNameField, c);
        
        c.gridx = 0;
        c.gridy = 1;
        this.add(new JLabel("Phenotype: "), c);
        
        c.gridx = 1;
        phenotypeField.setColumns(8);
        this.add(this.phenotypeField, c);
        
        c.gridx = 0;
        c.gridy = 2;
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
        FilterProperty patientName = filter.getProperties()[1];
        patientName.setValue(patientNameField.getText());
        
        FilterProperty phenotype = filter.getProperties()[0];
        phenotype.setValue(phenotypeField.getText());
        
        FilterProperty distance = filter.getProperties()[2];
        int newDist = 0;
        try {
            newDist =  Integer.parseInt(distanceFiled.getText());   
        } catch (Exception e) {
            System.err.println("Invalid distance value");
        }
        distance.setValue(newDist);
    }
}
