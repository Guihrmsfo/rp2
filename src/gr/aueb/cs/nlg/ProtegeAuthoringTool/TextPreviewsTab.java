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
import java.util.*;
import java.util.HashMap;
import javax.swing.*;
import edu.stanford.smi.protege.model.*;
import edu.stanford.smi.protege.ui.*;
import edu.stanford.smi.protege.util.*;

import edu.stanford.smi.protege.resource.Icons;
import edu.stanford.smi.protege.ui.FrameRenderer;
import edu.stanford.smi.protege.util.LabeledComponent;
import edu.stanford.smi.protege.widget.AbstractTabWidget;
//import edu.stanford.smi.protegex.owl.model.OWLModel;
//import edu.stanford.smi.protegex.owl.model.RDFSClass;
//import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;
//import edu.stanford.smi.protegex.owl.model.event.ModelAdapter;
//import edu.stanford.smi.protegex.owl.ProtegeOWL;
import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;
import edu.stanford.smi.protegex.owl.model.NamespaceManager;
//import edu.stanford.smi.protegex.owl.model.NamespaceManagerAdapter;

import com.hp.hpl.jena.ontology.*;


import gr.aueb.cs.nlg.NLGEngine.*;
import gr.aueb.cs.nlg.Languages.Languages;



import org.apache.log4j.Logger;


public class TextPreviewsTab extends AbstractTabWidget 
{
    static Logger logger = Logger.getLogger(TextPreviewsTab.class.getName());
    
    private static int userIDgen = 0;
    // the component that contains the class hierarchy tree
    private ClsesPanel _clsesPanel; 
    // the component that contains a list of Instances of the selected class 
    private DirectInstancesList _directInstancesList;   

    private boolean _isUpdating;

    boolean ClsSelected = true;
    private ComboHeaderComponent header;
    private String LangSelected = "English";
    private Project Proj;
    private NamespaceManager nsm;
    
    private String namespace = "";
    private String selectedResource = "";
    
    private NLG_HTML_Viewer NLG_viewer;
    
    private JPanel panel;
    private NLGEngine myEngine;
            
    private JComboBox LangChooserCombo;
    private JCheckBox compCb; // generate texts with comparisons
    private JCheckBox UsePserver; // generate texts for specified users
    private JComboBox UserTypes;
    private JComboBox Depth;
    private JCheckBox ShowAnnotations; 
    
    private HashMap<String,Integer> users;
    
     public void initialize() 
     {
        setIcon(Icons.getSlotIcon());
        setLabel("Text Previews ");
        
        // try to show this tab after it
        // is loaded from protege
        NLPlugin.showMenu();
        
        logger.info("initialize " + "TextPreviewsTab");
        
        add(createClsSplitter());
        //setupDragAndDrop();
        
        // get the jena model from the KB
        owlModel = (JenaOWLModel) getKnowledgeBase(); 
        // get the namespace manager
        this.nsm = owlModel.getNamespaceManager();

        //get the updated OntModel
        OntModel m = owlModel.getOntModel(); 
        
        // create a new engine
        myEngine =  new NLGEngine(  "", "",  
                                    Languages.ENGLISH, 
                                    true, 
                                    false,
                                    NLPlugin.getMicroplansAndOrderingQM(), 
                                    NLPlugin.getLexicon(), 
                                    NLPlugin.getUserModellingQueryManager(),
                                    m, 
                                    "", 
                                    -1, 
                                    ""  ,
                                    "", 
                                    "", 
                                    1111);
        // init the statistical tree which holds
        // the informations needed for generating comparisons
        myEngine.initStatisticalTree();
         
        // initialize the personalization server
        // in this tab we always use the emulator
        myEngine.initPServer();   
        
        // create a new user map. The map holds
        // ids of the users. For each user type we 
        // have one user
        
        users = new HashMap<String, Integer>();
        myEngine.setShapeText(false);
                
        refreshUserTypes();
        refreshEngine();
        
        transmitClsSelection();
        setClsTree(_clsesPanel.getClsesTree());   
        
        
        // if the tab gains the focus
        // then the user types combo box is updated
        // this happens due to
        // the user might have added or deleted a user type
        
        this.addFocusListener(new FocusAdapter() 
        {
            public void focusGained(FocusEvent e) 
            {
                refreshUserTypes();
            }
        });
    }
     
     // it refreshes the language
     // and the ontology model of the NLG engine
     // it must be called before a new text is produced
     public void refreshEngine()
     {
         // update the language of the NLG engine
         myEngine.setLanguage(this.getSelectedLanguage());
         
         // update the ontology model for which the
         // NLG engine produces texts 
                  
         owlModel = (JenaOWLModel) getKnowledgeBase();         
         myEngine.refreshNLGModules(owlModel.getOntModel(), this.getSelectedLanguage());
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
        
        header = createNLGenerationBrowserHeader();
        NLG_viewer = new NLG_HTML_Viewer();
        
        panel = new JPanel();        
        panel.setLayout(new java.awt.BorderLayout());
        
        panel.add(header, BorderLayout.NORTH);
        panel.add(NLG_viewer, BorderLayout.CENTER);
        
        pane.setRightComponent(panel);
        //pane.setRightComponent(createInstanceDisplay());
        return pane;
    }
     
    private JComponent createInstancesPanel() 
    {
        return createDirectInstancesList();
    }
    
    public int getDepth()
    {
        return Integer.parseInt(Depth.getSelectedItem().toString());
    }
        
    protected ComboHeaderComponent createNLGenerationBrowserHeader() 
    {
        JLabel label = ComponentFactory.createLabel(getProject().getName(), Icons.getProjectIcon(), SwingConstants.LEFT);
        String forResource = "For Resource";
        String NLGBrowser = "Text Preview";
        
        String Languages [] = {"English","Greek"};
        
        header = new ComboHeaderComponent(NLGBrowser, forResource, label);
        LangChooserCombo = header.addCombo("For Language", Languages);       
        
        String t [] = { "none" };
        UserTypes = header.addCombo("User Type", t);
        UserTypes.setSelectedIndex(0);
        
                
        String depths [] = { "1", "2" };
        Depth = header.addCombo("Maximum Graph Distance In Content Selection", depths);
        
        JButton preview = header.addButton("Preview");
        //JButton comparisonsButt = header.addButton("Initiliaze Comparisons Statistical Tree");
        JButton ResetHistoryInteraction = header.addButton("Reset interaction history");
         
        compCb = header.addCheckBoxForSelectedResource("Generate Comparisons");
        UsePserver = header.addCheckBoxForSelectedResource("Use Personalization Server");
        ShowAnnotations = header.addCheckBoxForSelectedResource("Show Semantic and Syntactic Annotations");
        ShowAnnotations.setSelected(false);
                
        UsePserver.setSelected(true);
        UsePserver.setVisible(false);
                
        preview.addActionListener(new java.awt.event.ActionListener() 
        {
            public void actionPerformed(java.awt.event.ActionEvent evt) 
            {
                previewBtnActionPerformed(evt);
            }
        });
        
        ResetHistoryInteraction.addActionListener(new java.awt.event.ActionListener() 
        {
            public void actionPerformed(java.awt.event.ActionEvent evt) 
            {
                ResetHistoryInteractionBtnActionPerformed(evt);
            }
        });

        /*comparisonsButt.addActionListener(new java.awt.event.ActionListener() 
        {
            public void actionPerformed(java.awt.event.ActionEvent evt) 
            {
                comparisonsButtBtnActionPerformed(evt);
            }
        });
        */
        
        
        UsePserver.addActionListener(new java.awt.event.ActionListener() 
        {
            public void actionPerformed(java.awt.event.ActionEvent evt) 
            {
                UsePserverActionPerformed(evt);
            }
        });
        
        
        this.UserTypes.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                UserTypesItemStateChanged(evt);
            }
        });
        
        
        this.LangChooserCombo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                LangChooserItemStateChanged(evt);
            }
        });
        
        refreshUserTypes();
        return header;
    }

    private void UserTypesItemStateChanged(java.awt.event.ItemEvent evt) {                                                 
        this.createPserverUSer();
    }    
        
    private void LangChooserItemStateChanged(java.awt.event.ItemEvent evt) 
    {
        this.refreshUserTypes();
    }
    
    private void previewBtnActionPerformed(java.awt.event.ActionEvent evt) 
    {            
        ShowText(this.namespace, this.selectedResource); // get namespace, String ln);
    }
    
    private void comparisonsButtBtnActionPerformed(java.awt.event.ActionEvent evt) 
    {
        myEngine.initStatisticalTree();
    }
            
    private void ResetHistoryInteractionBtnActionPerformed(java.awt.event.ActionEvent evt)
    {
        myEngine.initStatisticalTree();
        myEngine.initPServer();
        users = new HashMap<String, Integer>();
        createPserverUSer();
    }
            
    private void UsePserverActionPerformed(java.awt.event.ActionEvent evt) 
    {
        createPserverUSer();
    }
          
    // create a pserver user for the selected user type
    // if it doesn't exist
    private void createPserverUSer()
    {
        try
        { 
            if(this.UsePserver.isSelected())
            {
                logger.debug("create Pserver USer");

                if(UserTypes.getSelectedItem() != null)
                {
                    String utLocalName = UserTypes.getSelectedItem().toString();
                    String ut = "";

                    if(utLocalName.equals("none"))
                    {
                        ut = null;
                    }
                    else
                    {
                        ut = NLPlugin.getUserModellingQueryManager().NLGUserModellingNS + utLocalName; 
                    }

                    if(ut != null)
                    {
                        if(users.containsKey(ut))
                        {
                            int userId = users.get(ut);
                        }
                        else
                        {
                            int userID = ++userIDgen;
                            logger.debug("user created...");
                            myEngine.getUMVisit().newUser(userID + "", ut);
                            users.put(ut, userID);
                        }

                    }
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    


    private String getSelectedLanguage()
    {    
        
        String sel_item = (String)LangChooserCombo.getSelectedItem();
        logger.debug("lang:" + sel_item);
        transmitChange(sel_item); // 
        LangSelected = sel_item;
        
        if(LangSelected.equals("English"))
        {
            return Languages.ENGLISH;
        }
        else //(LangSelected.equals("Greek"))
        {
            return Languages.GREEK;
        }
        
    }
    
    JenaOWLModel owlModel;        
    
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
                            
                            JLabel label = (JLabel)header.getComponent();
                            label.setText(selectedResource);
        
                            //refresh_NLG_Viewer(namespace, selectedResource); 
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
     
     
    public void refreshNLGTab()
    {
        refreshUserTypes();
         
        OntModel m = owlModel.getOntModel(); 
        
        myEngine =  new NLGEngine(
                "", 
                "",  
                Languages.ENGLISH,
                true,
                false,
                NLPlugin.getMicroplansAndOrderingQM(), 
                NLPlugin.getLexicon(),
                NLPlugin.getUserModellingQueryManager(),
                m, "", -1,"","", "", 1111);
        
        myEngine.initPServer();              
        users = new HashMap<String, Integer>();
        myEngine.initStatisticalTree();
        myEngine.setShapeText(false);
    }
      
    
    public void refreshUserTypes()
    {
        Iterator<String> iter = null;
        
        if( NLPlugin.getUserModellingQueryManager() != null)
        {
            iter = NLPlugin.getUserModellingQueryManager().getUserTypes();
        }
        
        UserTypes.removeAllItems();       

        UserTypes.revalidate();
        UserTypes.repaint();
        
        while(iter !=null && iter.hasNext())            
        {
            String ut = iter.next();
            String ut_lang = NLPlugin.getUserModellingQueryManager().getParametersForUserType(ut).getLang();
                                    
            if(ut_lang.equals("English"))
            {
                ut_lang = Languages.ENGLISH;
            }
            else if(ut_lang.equals("Greek"))
            {
                ut_lang = Languages.GREEK;
            }
                    
            if(getSelectedLanguage().equals(ut_lang) || ut_lang.equals("All"))
            {
                //System.err.println(getSelectedLanguage() + " " + ut_lang + " " + ut);
                UserTypes.addItem(ut.substring(ut.indexOf("#") + 1) );
            }
        }
                
        UserTypes.revalidate();
        UserTypes.repaint();
        
        createPserverUSer();
    }
    
    // it is called when the user asks for new text
    public void ShowText(String ns, String ln)
    { 
        if(myEngine !=null)
        {            
            refreshEngine();                                           
            createPserverUSer();
            
            String utLocalName = UserTypes.getSelectedItem().toString();
            String ut = NLPlugin.getUserModellingQueryManager().NLGUserModellingNS + utLocalName; 
            
            if(utLocalName.equals("none"))
                ut = null;
            
            String resourceURI = ns + ln;
              
            String result [];
            
            if(NLPlugin.getModel().getOntClass(resourceURI) ==null)            
            {
                String userID = null;
                
                if(users.containsKey(ut) && this.UsePserver.isSelected())
                    userID = users.get(ut) + "";
                        
                logger.debug("userID: " + userID);
                
                if(UsePserver.isSelected())
                    result = myEngine.GenerateDescription(0, resourceURI, ut , userID, getDepth(), -1, compCb.isSelected(), "");
                else
                    result = myEngine.GenerateDescription(0, resourceURI, ut , userID, getDepth(), -1, compCb.isSelected(), "");
            }
            else
            {
                
                String userID = null;
                
                if(users.containsKey(ut) && this.UsePserver.isSelected())
                    userID = users.get(ut) + "";
                
                logger.info("userID: " + userID);
                
                if(UsePserver.isSelected())
                    result = myEngine.GenerateDescription(1, resourceURI, ut , userID, getDepth(), -1, compCb.isSelected(), "");
                else
                    result = myEngine.GenerateDescription(1, resourceURI, ut , userID, getDepth(), -1, compCb.isSelected(), "");
            }
            
            if(result [1].equals(""))
            {                
                this.NLG_viewer.setText("*NULL*");
            }
            else
            {
                String userID = users.get(ut) + "";
                
                if(myEngine.AllFactsAreAssimilated())
                {
                    logger.debug("userID: " + userID + " not assimilated");
                    String text = result [1] + "<BR><BR><BR>" + "All facts are assimilated";
                    
                    if(this.ShowAnnotations.isSelected())
                    {
                        text = text + "<BR><BR><BR>" +  "<pre bgcolor='#ffffff' style='font-family: Cambria; font-size: 12px'>" + XML2HTML(result [2]) + "</pre>";
                    }
                    
                    this.NLG_viewer.setText(text);
                }
                else
                {
                    logger.debug("userID: "  + userID + " assimilated");
                    String text = result [1];

                    if(this.ShowAnnotations.isSelected())
                    {
                        text = text + "<BR><BR><BR>" + "<pre bgcolor='#ffffff' style='font-family: Cambria; font-size: 12px'>"  + XML2HTML(result [2]) + "</pre>";
                    }
                    
                    this.NLG_viewer.setText(text);
                }
            }
        } 
        else
        {
            this.NLG_viewer.setText("ERROR");
        }
            
    }
    
    private String XML2HTML(String str)
    {
        str = str.replaceAll(">", "&gt;");
        str = str.replaceAll("<", "&lt;");        
        str = str.replaceAll("\"", "&quot;");
        
        return str;
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
                transmitClsSelection();
            }
        });
        
        _clsesPanel.getClsesTree().addFocusListener(new FocusAdapter() 
        {
            public void focusGained(FocusEvent e) 
            {
                refreshUserTypes();
                //transmitClsSelection();                
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
                
                JLabel label = (JLabel)header.getComponent();
                label.setText(selectedResource);
                            
                logger.debug("namespace:" + namespace + " selected:" + selectedResource);
                //refresh_NLG_Viewer(namespace, selectedResource);  
                
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
    
    public void transmitChange(String langSelected)
    {
 
    }
    
    public void close() 
    {
        
    }
    
    public boolean canClose()
    {
        //logger.debug("can close...");
        return true;
    }
    
    public void dispose() 
    {
        //logger.info("dipose " + "Text Previews");
        NLPlugin.hideMenu();
    }     
    


}

