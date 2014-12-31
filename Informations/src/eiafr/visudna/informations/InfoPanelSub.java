/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eiafr.visudna.informations;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author maria
 */
public class InfoPanelSub extends JPanel{
    private JTextField jtf;
    private JLabel label;
    
    public InfoPanelSub() {
        this.jtf = new JTextField("pouet");
        this.label = new JLabel("Hello");
        
        this.add(jtf);
        this.add(label);
    }
    
}
