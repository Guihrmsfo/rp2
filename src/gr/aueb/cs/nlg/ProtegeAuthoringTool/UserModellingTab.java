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
import java.awt.event.*;
import java.awt.dnd.*;
import java.util.*;
import javax.swing.*;

import javax.swing.SwingConstants.*;

import edu.stanford.smi.protege.model.*;
import edu.stanford.smi.protege.ui.*;
import edu.stanford.smi.protege.util.*;
import edu.stanford.smi.protege.widget.*;
import edu.stanford.smi.protege.resource.*;

import edu.stanford.smi.protege.resource.Icons;
import edu.stanford.smi.protege.ui.FrameRenderer;
import edu.stanford.smi.protege.util.LabeledComponent;
import edu.stanford.smi.protege.widget.AbstractTabWidget;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;
import edu.stanford.smi.protegex.owl.model.event.ModelAdapter;
import edu.stanford.smi.protegex.owl.ProtegeOWL;

import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;
import edu.stanford.smi.protegex.owl.jena.*;
        
import edu.stanford.smi.protegex.owl.model.event.*;
import edu.stanford.smi.protegex.owl.model.*;
import edu.stanford.smi.protegex.owl.model.NamespaceManager;
import edu.stanford.smi.protegex.owl.model.NamespaceManagerAdapter;

import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.vocabulary.*;
import com.hp.hpl.jena.ontology.*; 
import com.hp.hpl.jena.util.iterator.*;
import com.hp.hpl.jena.datatypes.*; 


import gr.aueb.cs.nlg.NLFiles.*;

import org.apache.log4j.Logger;


public class UserModellingTab extends AbstractTabWidget
{
     static Logger logger = Logger.getLogger(UserModellingTab.class.getName());
     // the component that contains the class hierarchy tree
     private ClsesPanel _clsesPanel; 
     // the component that contains a list of Instances of the selected class 
     private DirectInstancesList _directInstancesList;   
     // A Panel for inserting the Lexicon data
    
     private boolean _isUpdating;
     //private DirectTypesList _directTypesList;
     //private InstanceDisplay _instanceDisplay;
     private NounPanelEn NPEn;
     boolean ClsSelected = true;
     private ComboHeaderComponent header;
     private String LangSelected = "English";
     private Project Proj;
     private NamespaceManager nsm;
    
     private String namespace = "";
     private String selectedResource = "";
        
     private JButton SaveButton;
     private JButton DiscardButton;    
    
     private JenaOWLModel owlModel;      
       
     private JSplitPane TopBottomSplitter;
     
     private UserModellingPanel UMP;
     private UserTypesPanel UTP; 
     
     private UserModellingQueryManager UMQM;
     
     public void RefreshUMComponents(UserModellingQueryManager UMQM)
     {
        this.UMQM = UMQM;
        //TopBottomSplitter.removeAll();
        
        //TopBottomSplitter = createTopBottomSplitPane("SlotTab.left.top_bottom", 200);
        
        
        // refresh top copmonent        
        UTP = new UserTypesPanel(UMQM); 
        TopBottomSplitter.setTopComponent(UTP);
        
        // refresh bottom component
        UMP  = new UserModellingPanel(UMQM); 
        TopBottomSplitter.setBottomComponent(UMP);
                           
        
        transmitClsSelection();
     }
             
     public void initialize() 
     {       
        NLPlugin.showMenu();  
        
        logger.info("initialize " + "UserModellingTab");
                
        setIcon(Icons.getSlotIcon());
        setLabel("User Modelling");
        add(createClsSplitter());
        //setupDragAndDrop();
        owlModel = (JenaOWLModel) getKnowledgeBase(); // this must be done before transmit selection
        this.nsm = owlModel.getNamespaceManager();
        //transmitClsSelection();
        setClsTree(_clsesPanel.getClsesTree());           
        
        
        owlModel.addClassListener(new ClassAdapter()
        {
            
            public void instanceAdded(RDFSClass cls, RDFResource instance) 
            {
                logger.debug("instanceAdded" + 
                         "\n" + cls.getURI() +
                         "\n" + instance.getURI());
            }


            public void instanceRemoved(RDFSClass cls, RDFResource instance) 
            {
                logger.debug("instanceRemoved" + 
                         "\n" + cls.getURI() +
                         "\n" + instance.getURI());
            }
    
            public void subclassAdded(RDFSClass cls, RDFSClass subclass) 
            {
                logger.debug("subclassAdded" + 
                        "\n" + cls.getURI() +
                        "\n" + subclass.getURI());                
            }


            public void subclassRemoved(RDFSClass cls, RDFSClass subclass) 
            {
                logger.debug("subclassRemoved" + 
                        "\n" + cls.getURI() +
                        "\n" + subclass.getURI());
            }


            public void superclassAdded(RDFSClass cls, RDFSClass superclass) 
            {
                logger.debug("superclassAdded" + 
                        "\n" + cls.getURI()  + 
                        "\n" + superclass.getURI());
            }


            public void superclassRemoved(RDFSClass cls, RDFSClass superclass) 
            {
                logger.debug("superclassRemoved" + 
                        "\n" + cls.getURI() +
                        "\n" + superclass.getURI());
            }            
        });
         
        owlModel.addModelListener(new ModelAdapter() 
        {
            public void classCreated(RDFSClass cls) 
            {
                logger.debug("classCreated " + cls.getURI());
                // do nothing
            }
            
            public void individualCreated(RDFResource resource) 
            {
                logger.debug("individualCreated " + resource.getURI());
                // do nothing
            }
            
            public void classDeleted(RDFSClass cls) 
            {
                logger.debug("classDeleted " +  cls.getURI());
                UMQM.DeleteClass(cls.getURI());
            }               
            
            public void individualDeleted(RDFResource resource) 
            {
                logger.debug("individualDeleted " + resource.getURI());
                UMQM.DeleteInstance(resource.getURI());
            }
                         

            public void resourceNameChanged(RDFResource resource, String oldName) 
            {                
                logger.debug("resourceNameChanged " + oldName + " " + resource.getURI());
                
                String oldresourceURI = namespace + oldName;
                String newresourceURI = resource.getURI();
                
                if(NLPlugin.getModel().getOntClass(newresourceURI) == null)
                {
                    NLPlugin.getUserModellingQueryManager().RenameIntance(oldresourceURI, newresourceURI);
                }
                else
                {
                    NLPlugin.getUserModellingQueryManager().RenameClass(oldresourceURI, newresourceURI);
                }
            }            
        }); 
        
        transmitClsSelection();
    }//initialize
     
     /*
    private void setupDragAndDrop() 
    {
        DragSource.getDefaultDragSource().createDefaultDragGestureRecognizer(_directInstancesList,
                DnDConstants.ACTION_COPY_OR_MOVE, new ClsesAndInstancesTabDirectInstancesListDragSourceListener());
        new DropTarget(_clsesPanel.getDropComponent(), DnDConstants.ACTION_COPY_OR_MOVE, new InstanceClsesTreeTarget());
    }
      */
        
    private JComponent createClsSplitter() 
    {
        JSplitPane pane = createLeftRightSplitPane("NLG_PLUG_IN.left_right", 50);
        //pane.setDividerLocation(100);
        pane.setLeftComponent(createClsControlPanel());
        pane.setRightComponent(createInstanceSplitter());
        return pane;
    }
    
     private JComponent createClsControlPanel() 
     {        
        return createClsesPanel();
     }
     
     private JComponent createInstanceSplitter() 
     {
        JSplitPane pane = createLeftRightSplitPane("NLG_PLUG_IN.right.left_right", 0);
        //pane.setDividerLocation(100);
        pane.setLeftComponent(createInstancesPanel());
        
        TopBottomSplitter = createTopBottomSplitPane("SlotTab.left.top_bottom", 200);
        
        UTP = new UserTypesPanel(NLPlugin.getUserModellingQueryManager());
        TopBottomSplitter.setTopComponent(UTP);
        
        UMP  = new UserModellingPanel(NLPlugin.getUserModellingQueryManager());
        TopBottomSplitter.setBottomComponent(UMP);
                
        pane.setRightComponent(TopBottomSplitter);
                
        return pane;
    }
     
    private JComponent createInstancesPanel() 
    {
        return createDirectInstancesList();
    }
       

     private JComponent createDirectInstancesList() 
     {
        _directInstancesList = new DirectInstancesList(getProject());        
        
        _directInstancesList.addSelectionListener(new SelectionListener() 
        {
            
            public void selectionChanged(SelectionEvent event) 
            {
                if (!_isUpdating) 
                {
                    _isUpdating = true;
                    Collection selection = _directInstancesList.getSelection();
                    Instance selectedInstance;
                    if (selection.size() == 1) 
                    {
                        selectedInstance = (Instance) CollectionUtilities.getFirstItem(selection);
                        if(!ClsSelected)
                        {
                            String BrowserText = selectedInstance.getBrowserText();
                            
                            namespace = owlModel.getNamespaceForResourceName(BrowserText); // get namespace
                            selectedResource = owlModel.getLocalNameForResourceName(BrowserText);
                            
                            logger.debug("namespace:" + namespace + " selected:" + selectedResource);
                            refresh(namespace, selectedResource);  
                            
                        }
                        ClsSelected = false;
                    } 
                    else 
                    {
                        selectedInstance = null;
                    }
                    
                    //_instanceDisplay.setInstance(selectedInstance);
                    //_directTypesList.setInstance(selectedInstance);
                    _isUpdating = false;
                }
            }
        });
        
        setInstanceSelectable((Selectable) _directInstancesList.getDragComponent());
        return _directInstancesList;
    }
     

    
    private JComponent createClsesPanel() 
    {
        _clsesPanel = new ClsesPanel(getProject());        
        LabeledComponent LC = _clsesPanel.getLabeledComponent();
        LC.removeAllHeaderButtons();    // remove all the header buttons
        LC.revalidate();
        
        FrameRenderer renderer = FrameRenderer.createInstance();
        
        renderer.setDisplayDirectInstanceCount(true);
        
        _clsesPanel.setRenderer(renderer);
        
        _clsesPanel.addSelectionListener(new SelectionListener() {
            public void selectionChanged(SelectionEvent event) {
                transmitClsSelection();
            }
        });
        
        _clsesPanel.getClsesTree().addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                transmitClsSelection();
            }
        });
              
        return _clsesPanel;
    }
    
    private void transmitClsSelection() 
    {
        logger.debug("Selection---");
        // Log.enter(this, "transmitSelection");
        Collection selection = _clsesPanel.getSelection();
        Instance selectedInstance = null;
        Cls selectedCls = null;
        Cls selectedParent = null;
        if (selection.size() == 1) 
        {
            selectedInstance = (Instance) CollectionUtilities.getFirstItem(selection);
            if (selectedInstance instanceof Cls) 
            {
                selectedCls = (Cls) selectedInstance;
                
                String BrowserText = selectedCls.getBrowserText();
                
                namespace = owlModel.getNamespaceForResourceName(BrowserText); // get namespace
                selectedResource = owlModel.getLocalNameForResourceName(BrowserText);
                
                logger.debug("namespace:" + namespace + " selected:" + selectedResource);
                refresh(namespace, selectedResource);  
                
                ClsSelected = true;
                selectedParent = _clsesPanel.getDisplayParent();
            }
        }
        //_inverseRelationshipPanel.setCls(selectedCls, selectedParent);
        _directInstancesList.setClses(selection);
        

        /*
         ComponentUtilities.setListValues(_clsList, selection);
         if (!selection.isEmpty()) {
         _clsList.setSelectedIndex(0);
         }
         */
        _directInstancesList.clearSelection();
        //_instanceDisplay.setInstance(selectedCls);
    }   
    
    public void refresh(String namespace,String selectedResource)
    {
        OntModel m = owlModel.getOntModel();
                
        if(m != null)
        {            
            String ResourceURI = namespace + selectedResource;
            OntClass c = m.getOntClass(ResourceURI);
            
            Vector data = FindProperties(m,namespace, selectedResource);  
            
            if(c !=null)
            {
                UMP.refresh(data,  ResourceURI, true); 
            }
            else
            {
                UMP.refresh(data,  ResourceURI, false); 
            }
            
            UMP.setResourceName(selectedResource);
            UTP.setProjectName(this.getProject().getProjectName());
        }              
    }
        
    
            
    private void addProperties(OntClass c, HashMap propertiesMap)
    {
            ExtendedIterator iter = c.listDeclaredProperties();

            while(iter.hasNext())
            {
                String property = iter.next().toString();

                logger.debug(property);
                
                if(property.equals(RDFS.subClassOf.getURI()))
                    propertiesMap.put(property,propertiesMap.get(property));
                else
                    propertiesMap.put(property,"Filler"); 
            }                    
    }
            
    // find the properties which are related with a class or instance
    public Vector FindProperties(OntModel m, String namespace, String selectedResource)
    {        
        Vector properties = new Vector();
        HashMap propertiesMap = new HashMap(); 
        
        OntClass c = m.getOntClass(namespace + selectedResource);
                
        if(c != null) // if c is a class
        {
            logger.debug("Class "+ namespace + selectedResource);
                      
            //foo(c);
                                
            HashSet visitedClasses = new HashSet();                                    
            getProperties(c, visitedClasses, propertiesMap);            
               
            // add direct superclasses
            ExtendedIterator superclassIter = c.listSuperClasses();
            while(superclassIter.hasNext())
            {
                OntClass superClass = (OntClass)superclassIter.next();
                
                if(!superClass.isAnon())
                {
                    propertiesMap.put(RDFS.subClassOf.getURI(), superClass.getURI());
                }
            }
                    
        }// if c is a class
        
        Individual MyInstance = m.getIndividual(namespace + selectedResource);

        if(MyInstance!=null && c == null)// if is an instance
        {               

            logger.debug("Instance "+ namespace + selectedResource);                

            StmtIterator iter = MyInstance.listProperties();

            while(iter.hasNext())
            {
                Vector row = new Vector();

                Statement stmt = iter.nextStatement();
                String property = stmt.getPredicate().getURI();
                logger.debug(property);
                
                StmtIterator stmtIter = m.listStatements(MyInstance, stmt.getPredicate(), (RDFNode)null);
                
                String fillers = "";
                while(stmtIter.hasNext())
                {
                    // prothetw oluys tous fillers
                    String fill = stmtIter.nextStatement().getObject().toString();
                    //fill = fill.substring(fill.indexOf("#") + 1); //
                    fillers = fill ; //+ ";" + fillers;
                }
                
                propertiesMap.put(property,fillers); 
                
            }
                         
        }
        
        Iterator propertiesIter = propertiesMap.keySet().iterator();
        
        while(propertiesIter.hasNext())
        {
            Vector row = new Vector();
            String property = propertiesIter.next().toString();
            
            row.add(property);            
            row.add(propertiesMap.get(property));
            
            properties.add(row);
        }
        
        return properties;
    }
    
    private void getProperties(OntClass c, HashSet visitedClasses, HashMap propertiesMap )
    {        
        if(c.isAnon() || (!c.isAnon() && !visitedClasses.contains(c.getURI())))
        {
            if(!c.isAnon())
            visitedClasses.add(c.getURI());
            
            addProperties(c,propertiesMap);
             
            ExtendedIterator SuperClassesIter = c.listSuperClasses();

            while(SuperClassesIter.hasNext())
            {
               OntClass SuperClass = (OntClass)SuperClassesIter.next();
               getProperties(SuperClass, visitedClasses, propertiesMap);
            }

            ExtendedIterator EquivalentClassesIter = c.listEquivalentClasses();

            while(EquivalentClassesIter.hasNext())
            {
               OntClass EquivalentClass = (OntClass)EquivalentClassesIter.next();
               Vector operands = getOperands(EquivalentClass);
               
               for(int i = 0; i < operands.size(); i++)
               {
                    getProperties((OntClass)operands.get(i), visitedClasses, propertiesMap);
               }
               
               getProperties(EquivalentClass, visitedClasses, propertiesMap);
            }   
            
           Vector operands = getOperands(c);

           for(int i = 0; i < operands.size(); i++)
           {
                getProperties((OntClass)operands.get(i), visitedClasses, propertiesMap);
           }            
                        
          
        }
    }
       
    private Vector getOperands(OntClass c)
    {
        
        Vector operandsVector = new Vector();
        
        if(c.isIntersectionClass())
        {
            IntersectionClass IC = c.asIntersectionClass();
            ExtendedIterator i = IC.listOperands();
            
            while(i.hasNext())
            {
                OntClass operandClass = (OntClass)i.next();
                operandsVector.add(operandClass);
            }
        }
        
        if(c.isUnionClass())
        {
            UnionClass UC = c.asUnionClass();
            ExtendedIterator i = UC.listOperands();
            
            while(i.hasNext())
            {
                OntClass operandClass = (OntClass)i.next();
                operandsVector.add(operandClass);
            }            
        }
        
        if(c.isComplementClass())
        {
            ComplementClass CC = c.asComplementClass();
            ExtendedIterator i = CC.listOperands();
            
            while(i.hasNext())
            {
                OntClass operandClass = (OntClass)i.next();
                operandsVector.add(operandClass);
            }            
        }
        
        return operandsVector;
    }
    
    public void foo(OntClass myClass)
    {
        ExtendedIterator equivIter = myClass.listEquivalentClasses();
        
        while(equivIter.hasNext())
        {
            logger.debug("!@"  + "foo");
            OntClass cls = (OntClass)equivIter.next();
            
            if(cls.isIntersectionClass())
            { //if cls is IntersectionClass
                IntersectionClass IC = cls.asIntersectionClass();
                ExtendedIterator i = IC.listOperands();

                while(i.hasNext())
                {
                    OntClass c = (OntClass)i.next();

                    if(c.isAnon())
                    {
                        logger.debug("!@"  + "Anon");
                        ExtendedIterator ii = c.listDeclaredProperties(true);
                        while(ii.hasNext())
                        {
                            logger.debug("!@"  + ii.next().toString());
                        }
                    }
                    else 
                    {
                        logger.debug("!@"  + "not Anon");
                        ExtendedIterator ii = c.listDeclaredProperties(true);
                        while(ii.hasNext())
                        {
                            logger.debug("@!"  + ii.next().toString());
                        }                    

                    }

                }
            }
            if (cls.isUnionClass())
            { //if cls is UnionClass    		

            }
            if (cls.isComplementClass()){ //if cls is ComplementClass    		

            } 
        }
    }
    
    public void close() 
    {
        
    }
    
    public void dispose()
    {
        logger.info("initialize " + "User Modelling");
        NLPlugin.hideMenu();
    }
       
}

