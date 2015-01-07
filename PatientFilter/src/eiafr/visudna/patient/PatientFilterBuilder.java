/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eiafr.visudna.patient;

import javax.swing.Icon;
import javax.swing.JPanel;
import org.gephi.filters.spi.Category;
import org.gephi.filters.spi.Filter;
import org.gephi.filters.spi.FilterBuilder;
import org.openide.util.lookup.ServiceProvider;

/**
 * 
 * @author Maria Sisto
 */
@ServiceProvider(service = FilterBuilder.class)
public class PatientFilterBuilder implements FilterBuilder {
    
    private Filter myFilter = new PatientFilter();
    

    @Override
    public Category getCategory() {
        return new Category("VisuDNA Filters");
    }

    @Override
    public String getName() {
        return myFilter.getName();
    }

    @Override
    public Icon getIcon() {
        return null;
    }

    @Override
    public String getDescription() {
        return "A filter example";
    }

    @Override
     public Filter getFilter() {
        return myFilter;
    }

    @Override
    public JPanel getPanel(Filter filter) {
        return new PatientFilterPanel((PatientFilter) myFilter);
    }

    @Override
   public void destroy(Filter filter) {
       return;
    }
 
    
}
