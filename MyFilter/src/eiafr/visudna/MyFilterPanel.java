/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eiafr.visudna;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.gephi.filters.spi.FilterProperty;

/**
 *
 * @author maria
 */
public class MyFilterPanel extends JPanel implements DocumentListener { //ActionListener {
 
    private MyFilter filter;
    private JTextField maxDegFiled;
 
    public MyFilterPanel(MyFilter filter) {
        super();
        this.filter = filter;
 
        this.maxDegFiled = new JTextField(filter.getMaxDegree());
 
 //       maxDegFiled.addActionListener(this);
        maxDegFiled.getDocument().addDocumentListener(this);
        
        this.add(maxDegFiled);
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
        FilterProperty maxDeg = filter.getProperties()[0];
        System.out.println(maxDegFiled.getText());
        maxDeg.setValue(Integer.parseInt(maxDegFiled.getText()));
    }
}
