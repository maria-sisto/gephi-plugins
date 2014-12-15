/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eiafr.visudna.distanceFrom;

import javax.swing.Icon;
import javax.swing.JPanel;
import org.gephi.filters.spi.Category;
import org.gephi.filters.spi.Filter;
import org.gephi.filters.spi.FilterBuilder;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author maria
 */
@ServiceProvider(service = FilterBuilder.class)
public class MyFilterBuilder implements FilterBuilder {
    
    private Filter myFilter = new MyFilter();
    

    @Override
    public Category getCategory() {
        return new Category("VisuDNA Filters");
    }

    @Override
    public String getName() {
        return "My Filter";
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
        return new MyFilterPanel((MyFilter) myFilter);
    }

    @Override
   public void destroy(Filter filter) {
       return;
    }
 
    
}
