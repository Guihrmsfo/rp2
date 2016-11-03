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

import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import edu.stanford.smi.protege.model.*;
import edu.stanford.smi.protege.ui.*;
import edu.stanford.smi.protege.util.*;

import edu.stanford.smi.protege.resource.Icons;
import edu.stanford.smi.protege.ui.FrameRenderer;
import edu.stanford.smi.protege.util.LabeledComponent;
import edu.stanford.smi.protege.widget.AbstractTabWidget;
//import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;
import edu.stanford.smi.protegex.owl.model.event.*;
import edu.stanford.smi.protegex.owl.model.*;
import edu.stanford.smi.protegex.owl.model.NamespaceManager;


import gr.aueb.cs.nlg.Languages.Languages;

import org.apache.log4j.Logger;

public class LexiconTab extends AbstractTabWidget 
{
    static Logger logger = Logger.getLogger(LexiconTab.class.getName());
   
    // the component that contains the class hierarchy tree
    private ClsesPanel _clsesPanel; 
    // the component that contains a list of Instances of the selected class 
    private DirectInstancesList _directInstancesList;   
    
    // A Panel for inserting the Lexicon data (NPs and canned texts)
    
    private LexEntryPanel LEP;
    private boolean _isUpdating;
    
    //private DirectTypesList _directTypesList;
    //private InstanceDisplay _instanceDisplay;
    
    private NounPanelEn NPEn;
    boolean ClsSelected = true;
    private ComboHeaderComponent header;
    //private String LangSelected = "English";
    
    private Project Proj;
    private NamespaceManager nsm;
    
    private String namespace = "";
    private String selectedResource = "";
        
 
    private JCheckBox CannedTextCB;
    private JComboBox LangChooserCombo;
            
    private JLabel Resourcelabel;
            
    private JenaOWLModel owlModel;      
        
     public void initialize() 
     {        
        NLPlugin.showMenu();
                
        logger.info("initialize " + "LexiconTab");
        setIcon(Icons.getSlotIcon());
        setLabel("Lexicon");
        add(createClsSplitter());
        //setupDragAndDrop();
        
        // this must be done before transmit selection
        owlModel = (JenaOWLModel) getKnowledgeBase(); 
        this.nsm = owlModel.getNamespaceManager();
        transmitClsSelection(false);
        setClsTree(_clsesPanel.getClsesTree());  
        

        
        owlModel.addModelListener(new ModelAdapter() 
        {
            public void classCreated(RDFSClass cls) 
            {
                logger.debug("classCreated " + cls.getURI());
                //do nothing
            }

            public void classDeleted(RDFSClass cls) 
            {
                logger.debug("classDeleted " +  cls.getURI());
                
                Iterator<String> it = NLPlugin.getLexicon().getMappings(cls.getURI());
                
                while(it.hasNext())
                NLPlugin.getLexicon().DeleteNLResource(cls.getURI(), it.next());
            }               
            
            public void individualCreated(RDFResource resource) 
            {
                logger.debug("individualCreated " + resource.getURI());
                //do nothing
            }


            public void individualDeleted(RDFResource resource) 
            {
                logger.debug("individualDeleted " + resource.getURI());
                Iterator<String> it = NLPlugin.getLexicon().getMappings(resource.getURI());
                
                while(it.hasNext())
                NLPlugin.getLexicon().DeleteNLResource(resource.getURI(), it.next());                
            }


            public void resourceNameChanged(RDFResource resource, String oldName) 
            {
                logger.debug("resourceNameChanged " + oldName + " " + resource.getURI());
                
                String oldresourceURI = namespace + oldName;
                String newresourceURI = resource.getURI();
                
                NLPlugin.getLexicon().RenameOwlResource(oldresourceURI, newresourceURI);                
            }            
        });                                        
    }
     /*
    private void setupDragAndDrop() {
        DragSource.getDefaultDragSource().createDefaultDragGestureRecognizer(_directInstancesList,
                DnDConstants.ACTION_COPY_OR_MOVE, new ClsesAndInstancesTabDirectInstancesListDragSourceListener());
        new DropTarget(_clsesPanel.getDropComponent(), DnDConstants.ACTION_COPY_OR_MOVE, new InstanceClsesTreeTarget());
    }*/
        
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
        
        header = createLexiconBrowserHeader();
        LEP = new LexEntryPanel(getProject().getName(),header, this);
        pane.setRightComponent(LEP);
        //pane.setRightComponent(createInstanceDisplay());
        return pane;
    }
     
    private JComponent createInstancesPanel() 
    {
        return createDirectInstancesList();
    }
    
    public void setSelectedResourceName(String ResourceName)
    {
        Resourcelabel.setText(ResourceName);
    }
    
    protected ComboHeaderComponent createLexiconBrowserHeader() 
    {
        Resourcelabel = ComponentFactory.createLabel("", Icons.getProjectIcon(), SwingConstants.LEFT);
        String forResource = "Resource";
        String NounBrowser = "Lexicon Browser";
        
        String Languages [] = {"English","Greek"};
        
        // create header component
        header = new ComboHeaderComponent(NounBrowser, forResource, Resourcelabel);
                                        
        // add "languages combo"
        LangChooserCombo = header.addCombo("For Language", Languages);       
        LangChooserCombo.addActionListener(new java.awt.event.ActionListener() 
        {
            public void actionPerformed(java.awt.event.ActionEvent evt) 
            {
                LangChooserComboActionPerformed(evt);
            }
        });
        
        //add CannedText button
        CannedTextCB = header.addCheckBox("Multilingual Personalized Canned Text");
        
        CannedTextCB.addItemListener(new java.awt.event.ItemListener() 
        {
            public void itemStateChanged(java.awt.event.ItemEvent evt) 
            {
                CannedTextCBStateChanged(evt);
            }
        });
        
        return header;
    }
    
    private void CannedTextCBStateChanged(java.awt.event.ItemEvent evt)
    {        
        transmitChange();
    }
            
    private void LangChooserComboActionPerformed(java.awt.event.ActionEvent evt) 
    {                    
        transmitChange();        
    }

    private String getSelectedLanguage()
    {              
        
        return (String)LangChooserCombo.getSelectedItem(); 
    }
          
     private JComponent createDirectInstancesList() 
     {
        _directInstancesList = new DirectInstancesList(getProject());        
        
        _directInstancesList.addSelectionListener(new SelectionListener() 
        {
            
            public void selectionChanged(SelectionEvent event) 
            {                
                //logger.debug("Intance was selected!!!!!!!!!!");
                
                if (!_isUpdating) 
                {
                    _isUpdating = true;
                    Collection selection = _directInstancesList.getSelection();
                    Instance selectedInstance;
                
                    //logger.debug("Intance was selected before!!!!!!!!!!");
                    
                    if (selection.size() == 1) 
                    {
                        selectedInstance = (Instance) CollectionUtilities.getFirstItem(selection);
                        
                        //logger.debug("@@" + "namespace:" + namespace + " selected:" + selectedResource);
                        
                        if(!ClsSelected)
                        {
                            String BrowserText = selectedInstance.getBrowserText();
                            
                            namespace = owlModel.getNamespaceForResourceName(BrowserText); // get namespace
                            selectedResource = owlModel.getLocalNameForResourceName(BrowserText);
                            
                            //logger.debug("@@@" + "namespace:" + namespace + " selected:" + selectedResource);
                            
                            refreshLexEntryPanel(namespace, selectedResource);  
                            CannedTextCB.setEnabled(true);
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
     
    public void refreshLexEntryPanel(String namespace,String selectedResource)
    {
        String lang = "";
        
        if(getSelectedLanguage().compareTo("English")==0)
        {     
            lang = Languages.ENGLISH;
        }
        else if(getSelectedLanguage().compareTo("Greek")==0)
        {
            lang = Languages.GREEK;
        }
         
        
        if(ClsSelected)
        {
            LEP.setEntry(namespace, selectedResource, 0, lang);
        }
        else
        {
            LEP.setEntry(namespace, selectedResource, 1, lang);
        }
        
        
        setSelectedResourceName(selectedResource);
 
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
        
        _clsesPanel.addSelectionListener(new SelectionListener() 
        {
            public void selectionChanged(SelectionEvent event) 
            {
                transmitClsSelection(false);
            }
        });
        
        _clsesPanel.getClsesTree().addFocusListener(new FocusAdapter() 
        {
            public void focusGained(FocusEvent e) 
            {
                transmitClsSelection(true);
            }
        });
              
        return _clsesPanel;
    }
    
    
    public void Refresh_LexTab()
    {
        
        transmitClsSelection(false);
    }
    
    private void transmitClsSelection(boolean Selinstance) 
    {
        //logger.debug("Selection---");
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
                refreshLexEntryPanel(namespace, selectedResource);  
                CannedTextCB.setEnabled(false);
                
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
        
        //if(Selinstance)
        _directInstancesList.clearSelection();
        
        //_instanceDisplay.setInstance(selectedCls);
    }   
    
    public void transmitChange()
    {        
        String langSelected = (String)LangChooserCombo.getSelectedItem();          
                
        JPanel IP = LEP.getInternalPanel();
        JPanel GP = LEP.getGeneralPanel();
        
        JPanel NEWGP = null;
                      
        IP.remove(GP);    
                
        if(langSelected.compareTo("English")==0 )
        {
            if (CannedTextCB.isSelected())
            {
                NEWGP = new CannedTextsPanel(this);
            }
            else
            {
                NEWGP = new NounPanelEn(this); 
            } 
        }
        else if(langSelected.compareTo("Greek")==0 )
        {
            if (CannedTextCB.isSelected())
            {
                NEWGP = new CannedTextsPanel(this);
            }
            else
            {            
                NEWGP = new NounPanelGr(this);
            }
        }
        
        //JScrollPane scrollpane = new JScrollPane(NEWGP);
        LEP.setGeneralPanel(NEWGP);
        IP.add(NEWGP);  
        
        LEP.setInternalPanel(IP);     
        
        LEP.revalidate();
        
        refreshLexEntryPanel(namespace, selectedResource);  
    }    
    
    
    public void close() 
    {
        //logger.debug("Closing tab");
    }

    public void dispose() 
    {
        //logger.debug("dispose " + "Lexicon");
        NLPlugin.hideMenu();
    }
        

    
    public void SomethingChanged()
    {
         saveToLexicon(namespace, selectedResource); 
    }
            
    public void saveToLexicon(String namespace,String selectedResource)
    {
        if(!CannedTextCB.isSelected())
        {
            LEP.saveNPToLexicon(namespace, selectedResource);
        }
        else
        {
            if(getSelectedLanguage().compareTo("English")==0 )
            {
                LEP.saveCTToLexicon(namespace, selectedResource, Languages.ENGLISH);
            }
            else
            {
                LEP.saveCTToLexicon(namespace, selectedResource, Languages.GREEK);
            }
        }
    }
        
    
   public static void main(String[] args) 
   {
        int a  = 1;
        System.err.println("boooo");
        a = 7;
        String myargs  [] ={"C:\\NaturalOWL\\NLFiles-MPIRO\\mpiro.pprj"};
        edu.stanford.smi.protege.Application.main(myargs);
    }

}

