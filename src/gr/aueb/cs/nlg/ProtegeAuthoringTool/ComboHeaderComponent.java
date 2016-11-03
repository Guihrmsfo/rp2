/*
    NaturalOWL. 
    Copyright (C) 2008  Dimitrios Galanis and Giorgos Karakatsiotis.
    Natural Language Processing Group, Department of Informatics, 
    Athens University of Economics and Business, Greece.

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
*/

package gr.aueb.cs.nlg.ProtegeAuthoringTool;

import java.awt.*;

import javax.swing.*;

import edu.stanford.smi.protege.resource.*;
import edu.stanford.smi.protege.util.*;


public class ComboHeaderComponent extends JPanel 
{
    private JComponent component;
    private JLabel titleLabel;
    private JPanel titlePanel;
    private JLabel componentLabel;
    
    private JPanel Combos = ComponentFactory.createPanel();
    private JPanel Buttons = ComponentFactory.createPanel();
    
    //private JPanel Buttons = ComponentFactory.createPanel();
     
    private JLabel toolbarLabel ;
    private JPanel toolbar = ComponentFactory.createPanel();
    private JPanel xxxxx = ComponentFactory.createPanel();
    
    public ComboHeaderComponent(JComponent component) 
    {
        this("", "", component);
    }
    
    public ComboHeaderComponent(String title, String label, JComponent component) 
    {
            this.component = component;
	    setLayout(new BorderLayout());
            
	    titlePanel = new JPanel(new BorderLayout());
	    titlePanel.setBackground(Colors.getClsColor());
	    titleLabel = ComponentFactory.createTitleFontLabel(title.toUpperCase());
	    titleLabel.setForeground(Color.white);
	    titleLabel.setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 2));
	    titlePanel.add(titleLabel);
            
            xxxxx.setLayout(new javax.swing.BoxLayout(xxxxx, javax.swing.BoxLayout.X_AXIS));
            
            
	    add(titlePanel, BorderLayout.NORTH);
            add(xxxxx, BorderLayout.SOUTH);
            
	    componentLabel = ComponentFactory.createSmallFontLabel("");
	    componentLabel.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 4));
            
	    JPanel subjectPanel = new JPanel(new BorderLayout());
            
	    subjectPanel.add(componentLabel, BorderLayout.WEST);
	    subjectPanel.add(component, BorderLayout.CENTER);
	    add(subjectPanel, BorderLayout.CENTER);
            
            
            Combos.setLayout(new javax.swing.BoxLayout(Combos, javax.swing.BoxLayout.X_AXIS));                        
            //Combos.setLayout(new GridLayout(1,4,5,0));
            Buttons.setLayout(new javax.swing.BoxLayout(Buttons, javax.swing.BoxLayout.X_AXIS));
            Combos.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                        
            add(toolbar, BorderLayout.EAST);
            
            toolbar.setLayout(new javax.swing.BoxLayout(toolbar, javax.swing.BoxLayout.Y_AXIS));
            
            toolbarLabel = ComponentFactory.createTitleFontLabel(title.toUpperCase());
	    toolbarLabel.setForeground(Color.black);
	    toolbarLabel.setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 2));
            toolbarLabel.setText("");
                    
            subjectPanel.add(toolbarLabel, BorderLayout.EAST);
	    toolbar.add(Combos);
            toolbar.add(Buttons);
            
	    setComponentLabel(label);
    }
    
    public JCheckBox addCheckBoxForSelectedResource(String lbl)
    {
            JCheckBox CB = ComponentFactory.createCheckBox(lbl);
            CB.setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 2));            
            xxxxx.add(CB, BorderLayout.WEST);
            
            return CB;
    }
    
    
    
    public void setRightLabel(String txt)
    {
        toolbarLabel.setText(txt);
    }
    
    public JComboBox addCombo(String label, String data[]) 
    {
        JComboBox CB = ComponentFactory.createComboBox();        
        JLabel CB_label = ComponentFactory.createLabel();
        CB_label.setText(label);
                                      
        for(int i = 0; i < data.length; i++){
            CB.addItem(data[i]);
        }
                
        CB_label.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        Combos.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        
        Combos.add(CB_label);  
        Combos.add(CB);
        return CB;
    }
    
    public JCheckBox addCheckBox(String label) 
    {
        JCheckBox CB = ComponentFactory.createCheckBox(label);

        CB.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));                               
        Combos.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        
        Combos.add(CB);
        return CB;
    }
    
    //add a button 
    public JButton addButton(String label,Action act) 
    {
        JButton button = ComponentFactory.createButton(act);
        button.setText(label);
        
        Buttons.add(button);
        return button;
    }
     
    //add a button 
    public JButton addButton(String label) 
    {
        JButton button = new JButton();
        button.setText(label);
        
        Buttons.add(button);
        return button;
    } 
     
    public JPanel getCombosPanel() 
    {
        return Combos;
    }
    
    
    public JComponent getComponent() 
    {
        return component;
    }
    
    public void setTitle(String title) 
    {
        titleLabel.setText(title.toUpperCase());
    }
    
    public void setComponentLabel(String label) 
    {
        componentLabel.setText(label);
    }
    
    public void setColor(Color color) {
        titlePanel.setBackground(color);
    }
}
