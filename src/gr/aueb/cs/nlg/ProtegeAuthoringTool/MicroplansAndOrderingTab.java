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

import java.util.*; 

import javax.swing.*;

import edu.stanford.smi.protege.model.*;
import edu.stanford.smi.protege.util.*;
import edu.stanford.smi.protegex.owl.ui.properties.*;

import edu.stanford.smi.protege.resource.Icons;
//import edu.stanford.smi.protege.ui.FrameRenderer;
//import edu.stanford.smi.protege.util.LabeledComponent;
import edu.stanford.smi.protege.widget.AbstractTabWidget;
//import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;
import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;
import edu.stanford.smi.protegex.owl.model.event.*;
import edu.stanford.smi.protegex.owl.model.*;
 
//import edu.stanford.smi.protegex.owl.model.NamespaceManager;
//import edu.stanford.smi.protegex.owl.model.NamespaceManagerAdapter;

import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.ontology.*;
import com.hp.hpl.jena.util.iterator.*;


import org.apache.log4j.Logger;


public class MicroplansAndOrderingTab extends AbstractTabWidget
{
    static Logger logger = Logger.getLogger(MicroplansAndOrderingTab.class.getName());

    private OWLSubpropertyPane slotsPanel;
    private JPanel templatesPanel;
    private TemplatesPanel TP;
    private JSplitPane _mainSplitter;
    private JSplitPane TopBottomSplitter;
    private OrderingPanel OP;
    private Project P;
    
    public MicroplansAndOrderingTab() 
    {
        //logger.debug("constructor " + "PropertiesAndOrdering");    
    }
    
    public Project getProj(){
        return P;
    }
    
    private JComponent createMainSplitter() 
    {
        _mainSplitter = createLeftRightSplitPane("SlotsTab.left_right", 250);
        _mainSplitter.setLeftComponent(createSlotsPanel());
        templatesPanel = new JPanel();
        TopBottomSplitter = createTopBottomSplitPane("SlotTab.left.top_bottom", 200);
        
        OP = new OrderingPanel(this, NLPlugin.getMicroplansAndOrderingQM()); // create ordering panel
        TP = new TemplatesPanel(); // create templates panel
        
        TopBottomSplitter.setContinuousLayout(true); 
 	TopBottomSplitter.setOneTouchExpandable(true); 
        
        TopBottomSplitter.setTopComponent(OP);        
        TopBottomSplitter.setBottomComponent(TP);
        
        
        _mainSplitter.setRightComponent(TopBottomSplitter);
        return _mainSplitter;
    }
     
    
     public void RefreshMicroPComponents()
     {
        OP = new OrderingPanel(this, NLPlugin.getMicroplansAndOrderingQM()); // create ordering panel
        TP = new TemplatesPanel(); // create templates panel
        
        TopBottomSplitter.setTopComponent(OP);        
        TopBottomSplitter.setBottomComponent(TP);
                           
        TopBottomSplitter.setDividerLocation(200);
        transmitSelection();
     }
     
    private JComponent createSlotsPanel() 
    {   
        P = getProject();       
        
        slotsPanel = new OWLSubpropertyPane(P);
        
        slotsPanel.addSelectionListener(new SelectionListener() 
        {
            public void selectionChanged(SelectionEvent event) 
            {
                transmitSelection();
            }
        });
        
        return slotsPanel;                      
    }
     
    private void transmitSelection() 
    {
        Slot selection = (Slot) CollectionUtilities.getFirstItem(slotsPanel.getSelection());        
        
        if (!slotsPanel.getSelection().isEmpty()) 
        {
            String ns = owlModel.getNamespaceForResourceName(selection.getBrowserText()); // get namespace
            String prp = owlModel.getLocalNameForResourceName(selection.getBrowserText());
            
            TP.set_property(ns, prp);
            
            previous_property = current_property;
            current_property = ns + prp;
        }
    } 
       
    private String current_property = "";
    private String previous_property ="";
    
    public void initialize() 
    {
                
        NLPlugin.showMenu();
        logger.info("initialize " + "PropertiesAndOrdering");
        
        owlModel = (JenaOWLModel) getKnowledgeBase();        
        owlModel2 = (OWLModel) getKnowledgeBase();        
        
        setIcon(Icons.getSlotIcon());
        setLabel("Micro-plans & Ordering");
        add(createMainSplitter());
        transmitSelection();  
        
        
        owlModel.addModelListener(new ModelAdapter() 
        {
            public void propertyCreated(RDFProperty property) 
            {
                logger.debug("property created" + property.getURI());
                OP.addProperty(property.getURI());
            }

            public void propertyDeleted(RDFProperty property) 
            {
                
                logger.debug("property deleted" + property.toString());
                OP.removeProperty(previous_property); 
                NLPlugin.getMicroplansAndOrderingQM().removeProperty(property.toString());
            }
            
            public void resourceNameChanged(RDFResource resource, String oldName) 
            {
                logger.debug("property changed" + resource.getLocalName() +  "  "  + oldName);
                OP.renameProperty(resource.getNamespace() + oldName, resource.getURI());
                NLPlugin.getMicroplansAndOrderingQM().renameProperty(resource.getNamespace() + oldName, resource.getURI());
                transmitSelection();
            }                       
        });                                 
    }
    
    public OWLModel getModel()
    {
        return owlModel2;
    }
        
    JenaOWLModel owlModel;
    OWLModel owlModel2;
    
    public Vector getProperties()
    {        
        Vector v = new Vector();
        OntModel OM = owlModel.getOntModel();
        
        // add DatatypeProperties to v
        for(ExtendedIterator i = OM.listDatatypeProperties(); i.hasNext();){
           Property p= (Property)i.next();
           String str = p.getURI();
           v.add(str);           
        }
        
        // add ObjectProperties to v
        for(ExtendedIterator i = OM.listObjectProperties(); i.hasNext();){
           Property p= (Property)i.next();
           String str = p.getURI();
           v.add(str);         
        }
        
        return v;
    }
       
    public void dispose()
    {
        //logger.debug("dispose " + "PropertiesAndOrdering");
        NLPlugin.hideMenu();
    }
        
    public static void main(String args[])
    {
        //edu.stanford.smi.protege.Application.main(args);
        //PropertiesAndOrdering PO = new PropertiesAndOrdering();
        //PO.initialize();        
    }
}//
