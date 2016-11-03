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

package gr.aueb.cs.nlg.NLGEngine;

//import java.util.logging.Level;
import gr.aueb.cs.nlg.Communications.OntologyServer.DialogueManagerInfo;
import gr.aueb.cs.nlg.Communications.OntologyServer.OntologyServer;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.io.*;
import javax.swing.UIManager;
import javax.swing.JFrame;

import com.hp.hpl.jena.ontology.*;
import com.hp.hpl.jena.util.iterator.*;



import gr.aueb.cs.nlg.Communications.NLGEngineServer.NLGEngineServer;
import java.util.*;

import gr.aueb.cs.nlg.Languages.*;

import gr.demokritos.iit.PServer.*;




import org.apache.log4j.*;

public class NLGS_GUI extends JFrame implements ActionListener
{
            static Logger logger = Logger.getLogger("gr.aueb.cs.nlg.NLGEngine.NLGS_GUI");
            
            
            static Logger TheLogger  = Logger.getLogger("gr.aueb.cs.nlg");
            
            static Logger NLFiles_logger  = Logger.getLogger("gr.aueb.cs.nlg.NLFiles");
            static Logger Utils_Loggers  = Logger.getLogger("gr.aueb.cs.nlg.Utils");
            static Logger PServerEmu_loggers  = Logger.getLogger("gr.aueb.cs.nlg.PServerEmu");
            static Logger NLGEngine_loggers  = Logger.getLogger("gr.aueb.cs.nlg.NLGEngine");

            public OntModel ontModel = null;
                                    
            public File owlFile ;
            public File NLFile ;
            
            public Vector AllClasses = null;
            public DefaultListModel sampleModel;
            public JScrollPane scrollPane;
            public JScrollPane scrollPane2;
            public JScrollPane scrollPane3;
            
            public JList ClsList;
            public boolean showingIns = false;
            public MyListListener listener;
                                  
            private Vector v;
            Object Instances [];
            Object Classes [];
            private StringBuffer sb;
            
            private int depth = 1;
            private String UserTypeSelected;
            private int MFPS = 1;
            
            private NLGEngine myEngine =  null;
            private OntologyServer onto_server = null;
                    
            private UMVisit UMVis; 
            private Hashtable users;
            
            private int user_counter = 0;
                        
            public String  DimokritosPServer = "Dimokritos PServer";;
            public String  AUEBPServerEmu = "AUEB Emulator";
                    
            private FileWriter exportWriter;
            
            private JScrollPane logScrollPane1;            
            private LogTextArea myLogTextArea;
            private String[][] factsAndAssimilations;
            
            private JPopupMenu LoggerWindowPopup; 
                    
            public NLGS_GUI() 
            {
                try
                {
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
                    initComponents();
                    
                    myLogTextArea = new LogTextArea();
                    //myLogTextArea.setRows(100);
                    //myLogTextArea.setColumns(20);
                    myLogTextArea.setFont(new java.awt.Font("Courier New", 0, 12));
                    
                    logScrollPane1 = new JScrollPane(myLogTextArea);                                         
                    statusBarPanel.add(logScrollPane1);                    
                    
                    LoggerWindowPopup = new JPopupMenu();
                    JMenuItem mmmm = LoggerWindowPopup.add("Clear Log");
                          
                    myLogTextArea.addMouseListener(new  java.awt.event.MouseAdapter()
                    {
                        public void mouseClicked(MouseEvent e)
                        {                            
                            if (SwingUtilities.isRightMouseButton(e)) 
                            { 
                                LoggerWindowPopup.show(myLogTextArea, e.getX(), e.getY());
                            }
                        }                        
                    });
                    
                    mmmm.addActionListener(new java.awt.event.ActionListener() 
                    {
                        public void actionPerformed(java.awt.event.ActionEvent evt) 
                        {
                           myLogTextArea.setText("");
                        }
                    });
                   
                    myLogTextArea.start();
                    myLogTextArea.add(LoggerWindowPopup);
                    ShowInterOuputsCB.setSelected(true);
                            
                    EnglishRBtn.setSelected(true);
                    sampleModel = new DefaultListModel();
                    //for(int i = 0; i < 200; i++)
                    //sampleModel.addElement("" +i );
                    ClsList = new JList(sampleModel);
                    ClsList.setVisibleRowCount(1);
                     
                    JLabel chooseObjectLbl = new JLabel();
                    
                    chooseObjectLbl.setText(" Click an object ");
                            
                    scrollPane = new JScrollPane(ClsList);
                    jSplitPane1.setLeftComponent(scrollPane);
                                       
                    
                    Msgs.setRows(1);
                    scrollPane2 = new JScrollPane(Msgs);
		    jSplitPane1.setRightComponent(scrollPane2);		                         
                    
		    
                    listener = new MyListListener();
                    MyKeyListener k_listener = new MyKeyListener();
                    
                    ClsList.addListSelectionListener(listener);
                    ClsList.addKeyListener(k_listener);
                                        
                    //scrollPane3 = new JScrollPane(Info);
                    //InfoPanel.add(scrollPane3);
                            
                    // DEFAULTS
                    //NLFile = new File("../NLFiles-MPIRO");
                    //NLFile = new File("../NLFiles");
                    NLFile = new File("../authoring-tool/XENIOS/");
                    
                    //owlFile = new File("../NLFiles-MPIRO/mpiro.owl");
                    //owlFile = new File("../NLFiles/wine.owl");
                    owlFile = new File("../authoring-tool/XENIOS/OwlTemp.owl");
                    
                    setPaths();         
                    
                    ChooseServerCmb.addItem(AUEBPServerEmu);
                    ChooseServerCmb.addItem(DimokritosPServer);                    
                    ChooseServerCmb.setEnabled(true);
                    
                    // dhmioyriga toy dendroy poy 8a krata osa exw perigrapsei hdh
                    //comparTree = new ComparisonTree();
                    //statisticalTree = new ComparisonTree();                    
                    
                }
                catch (Exception e) 
                {
                    e.printStackTrace();
                }
            }
            
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        ModelSpec = new javax.swing.ButtonGroup();
        LangSelection = new javax.swing.ButtonGroup();
        ConfigPanel = new javax.swing.JPanel();
        WestConfigPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        path = new javax.swing.JTextField();
        SelectOntologyBtn = new javax.swing.JButton();
        NLResourcesPath = new javax.swing.JTextField();
        NLResources_lbl = new javax.swing.JLabel();
        SelectNLFilesPath = new javax.swing.JButton();
        UserType_lbl = new javax.swing.JLabel();
        Depth_ComBox = new javax.swing.JComboBox();
        Language_lbl = new javax.swing.JLabel();
        Depth_lbl = new javax.swing.JLabel();
        MAX_FACTS_PER_SENT_LABEL = new javax.swing.JLabel();
        MAX_FACTS_PER_SENT_COMBO = new javax.swing.JComboBox();
        UserType_ComBox = new javax.swing.JComboBox();
        GreekRBtn = new javax.swing.JRadioButton();
        EnglishRBtn = new javax.swing.JRadioButton();
        Userlbl = new javax.swing.JLabel();
        UsersCmb = new javax.swing.JComboBox();
        ChooseServerlbl = new javax.swing.JLabel();
        ChooseServerCmb = new javax.swing.JComboBox();
        ShowInterOuputsCB = new javax.swing.JCheckBox();
        ComparisonsCB = new javax.swing.JCheckBox();
        PersonalityTxt = new javax.swing.JTextField();
        PersonalityLbl = new javax.swing.JLabel();
        ProdGrammarsCB = new javax.swing.JCheckBox();
        ControlPanel = new javax.swing.JPanel();
        ReadModelAndNLInfoBtn = new javax.swing.JButton();
        RefreshBtn = new javax.swing.JButton();
        ShowInstances = new javax.swing.JButton();
        ShowClasses = new javax.swing.JButton();
        ClearPServer = new javax.swing.JButton();
        CreateUser = new javax.swing.JButton();
        DeleteUser = new javax.swing.JButton();
        InitStatisticalTree = new javax.swing.JButton();
        ClearPserveronlyforSelInstance = new javax.swing.JButton();
        StopOntologyServer = new javax.swing.JButton();
        StartOntologyServer = new javax.swing.JButton();
        Viewer = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        MsgsPanel = new javax.swing.JPanel();
        Msgs = new javax.swing.JTextArea();
        jSplitPane1 = new javax.swing.JSplitPane();
        Search = new javax.swing.JTextField();
        statusBarPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        IP = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        port = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        PserverIP = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        PserverPort = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        PServerUserId = new javax.swing.JTextField();
        loadDBs = new javax.swing.JCheckBox();
        PserverPass = new javax.swing.JPasswordField();
        MainToolbar = new javax.swing.JMenuBar();
        Basic = new javax.swing.JMenu();
        wines = new javax.swing.JMenuItem();
        MPIRO = new javax.swing.JMenuItem();
        xenios = new javax.swing.JMenuItem();
        exit = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        SaveTexts = new javax.swing.JMenuItem();

        setTitle("NaturalOWL");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.Y_AXIS));

        ConfigPanel.setMaximumSize(new java.awt.Dimension(2147483647, 250));
        ConfigPanel.setMinimumSize(new java.awt.Dimension(450, 150));
        ConfigPanel.setPreferredSize(new java.awt.Dimension(640, 250));
        ConfigPanel.setLayout(new javax.swing.BoxLayout(ConfigPanel, javax.swing.BoxLayout.LINE_AXIS));

        WestConfigPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        WestConfigPanel.setMaximumSize(new java.awt.Dimension(2147483647, 300));
        WestConfigPanel.setPreferredSize(new java.awt.Dimension(650, 250));
        WestConfigPanel.setLayout(new java.awt.GridBagLayout());

        jLabel1.setText("Ontology path");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 5);
        WestConfigPanel.add(jLabel1, gridBagConstraints);

        path.setEditable(false);
        path.setMinimumSize(new java.awt.Dimension(300, 20));
        path.setPreferredSize(new java.awt.Dimension(300, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 6;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 3, 5);
        WestConfigPanel.add(path, gridBagConstraints);

        SelectOntologyBtn.setText("Select Ontology");
        SelectOntologyBtn.setToolTipText("choose the .owl file");
        SelectOntologyBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SelectOntologyBtnActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        WestConfigPanel.add(SelectOntologyBtn, gridBagConstraints);

        NLResourcesPath.setEditable(false);
        NLResourcesPath.setPreferredSize(new java.awt.Dimension(300, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 3, 5);
        WestConfigPanel.add(NLResourcesPath, gridBagConstraints);

        NLResources_lbl.setText("NL Resources path");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 5);
        WestConfigPanel.add(NLResources_lbl, gridBagConstraints);

        SelectNLFilesPath.setText("Select NL Files path");
        SelectNLFilesPath.setToolTipText("choose the RDF files folder");
        SelectNLFilesPath.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SelectNLFilesPathActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        WestConfigPanel.add(SelectNLFilesPath, gridBagConstraints);

        UserType_lbl.setText("User Type:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 5);
        WestConfigPanel.add(UserType_lbl, gridBagConstraints);

        Depth_ComBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3" }));
        Depth_ComBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                Depth_ComBoxItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        WestConfigPanel.add(Depth_ComBox, gridBagConstraints);

        Language_lbl.setText("Language");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 5);
        WestConfigPanel.add(Language_lbl, gridBagConstraints);

        Depth_lbl.setText("Depth:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 5);
        WestConfigPanel.add(Depth_lbl, gridBagConstraints);

        MAX_FACTS_PER_SENT_LABEL.setText("MAX FACTS PER SENTENCE");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 5);
        WestConfigPanel.add(MAX_FACTS_PER_SENT_LABEL, gridBagConstraints);

        MAX_FACTS_PER_SENT_COMBO.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "none", "1", "2", "3", "4" }));
        MAX_FACTS_PER_SENT_COMBO.setMinimumSize(new java.awt.Dimension(100, 20));
        MAX_FACTS_PER_SENT_COMBO.setPreferredSize(new java.awt.Dimension(100, 20));
        MAX_FACTS_PER_SENT_COMBO.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                MAX_FACTS_PER_SENT_COMBOItemStateChanged(evt);
            }
        });
        MAX_FACTS_PER_SENT_COMBO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MAX_FACTS_PER_SENT_COMBOActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        WestConfigPanel.add(MAX_FACTS_PER_SENT_COMBO, gridBagConstraints);

        UserType_ComBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "none" }));
        UserType_ComBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                UserType_ComBoxItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        WestConfigPanel.add(UserType_ComBox, gridBagConstraints);

        LangSelection.add(GreekRBtn);
        GreekRBtn.setText("GREEK");
        GreekRBtn.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                GreekRBtnItemStateChanged(evt);
            }
        });
        GreekRBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GreekRBtnActionPerformed(evt);
            }
        });
        GreekRBtn.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                GreekRBtnStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        WestConfigPanel.add(GreekRBtn, gridBagConstraints);

        LangSelection.add(EnglishRBtn);
        EnglishRBtn.setText("ENGLISH");
        EnglishRBtn.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                EnglishRBtnItemStateChanged(evt);
            }
        });
        EnglishRBtn.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                EnglishRBtnStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        WestConfigPanel.add(EnglishRBtn, gridBagConstraints);

        Userlbl.setText("Users:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 5);
        WestConfigPanel.add(Userlbl, gridBagConstraints);

        UsersCmb.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "none" }));
        UsersCmb.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                UsersCmbItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        WestConfigPanel.add(UsersCmb, gridBagConstraints);

        ChooseServerlbl.setText("Choose PServer");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 5);
        WestConfigPanel.add(ChooseServerlbl, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        WestConfigPanel.add(ChooseServerCmb, gridBagConstraints);

        ShowInterOuputsCB.setText("Show intermediate Outputs");
        ShowInterOuputsCB.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        ShowInterOuputsCB.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        WestConfigPanel.add(ShowInterOuputsCB, gridBagConstraints);

        ComparisonsCB.setText("Comparisons");
        ComparisonsCB.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        ComparisonsCB.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        WestConfigPanel.add(ComparisonsCB, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        WestConfigPanel.add(PersonalityTxt, gridBagConstraints);

        PersonalityLbl.setText("Personality");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 5);
        WestConfigPanel.add(PersonalityLbl, gridBagConstraints);

        ProdGrammarsCB.setText("Produce Follow Up Grammars");
        ProdGrammarsCB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ProdGrammarsCBActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        WestConfigPanel.add(ProdGrammarsCB, gridBagConstraints);

        ConfigPanel.add(WestConfigPanel);

        ControlPanel.setMinimumSize(new java.awt.Dimension(0, 0));
        ControlPanel.setPreferredSize(new java.awt.Dimension(300, 373));
        ControlPanel.setLayout(new java.awt.GridBagLayout());

        ReadModelAndNLInfoBtn.setText("Load...");
        ReadModelAndNLInfoBtn.setToolTipText("loads ontology, nl resourcses and starts NLG server");
        ReadModelAndNLInfoBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ReadModelAndNLInfoBtnActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 54;
        gridBagConstraints.ipady = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        ControlPanel.add(ReadModelAndNLInfoBtn, gridBagConstraints);

        RefreshBtn.setText("Refresh Text");
        RefreshBtn.setMaximumSize(new java.awt.Dimension(67, 23));
        RefreshBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RefreshBtnActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 25;
        gridBagConstraints.ipady = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 74, 10, 74);
        ControlPanel.add(RefreshBtn, gridBagConstraints);

        ShowInstances.setText("Instances");
        ShowInstances.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ShowInstancesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 41;
        gridBagConstraints.ipady = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        ControlPanel.add(ShowInstances, gridBagConstraints);

        ShowClasses.setText("Classes");
        ShowClasses.setMaximumSize(new java.awt.Dimension(67, 23));
        ShowClasses.setMinimumSize(new java.awt.Dimension(67, 23));
        ShowClasses.setPreferredSize(new java.awt.Dimension(67, 23));
        ShowClasses.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ShowClassesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 53;
        gridBagConstraints.ipady = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        ControlPanel.add(ShowClasses, gridBagConstraints);

        ClearPServer.setText("Clear PServer");
        ClearPServer.setToolTipText("resets the pserver");
        ClearPServer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ClearPServerActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 21;
        gridBagConstraints.ipady = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        ControlPanel.add(ClearPServer, gridBagConstraints);

        CreateUser.setText("New User");
        CreateUser.setToolTipText("creates a new user of the selected user type");
        CreateUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CreateUserActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 41;
        gridBagConstraints.ipady = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        ControlPanel.add(CreateUser, gridBagConstraints);

        DeleteUser.setText("Delete User");
        DeleteUser.setToolTipText("deletes the selected user");
        DeleteUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DeleteUserActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 31;
        gridBagConstraints.ipady = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        ControlPanel.add(DeleteUser, gridBagConstraints);

        InitStatisticalTree.setText("Init Comparison");
        InitStatisticalTree.setToolTipText("resets the statistical tree (it is used in comparisons)");
        InitStatisticalTree.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                InitStatisticalTreeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 13;
        gridBagConstraints.ipady = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        ControlPanel.add(InitStatisticalTree, gridBagConstraints);

        ClearPserveronlyforSelInstance.setText("Clear Selected");
        ClearPserveronlyforSelInstance.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ClearPserveronlyforSelInstanceActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 19;
        gridBagConstraints.ipady = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        ControlPanel.add(ClearPserveronlyforSelInstance, gridBagConstraints);

        StopOntologyServer.setText(" Stop Servers");
        StopOntologyServer.setToolTipText("disconnects the servers from the communication servers");
        StopOntologyServer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                StopOntologyServerActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.ipadx = 23;
        gridBagConstraints.ipady = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        ControlPanel.add(StopOntologyServer, gridBagConstraints);

        StartOntologyServer.setText("Start Ont Server");
        StartOntologyServer.setToolTipText("starts the ontology server");
        StartOntologyServer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                StartOntologyServerActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.ipadx = 7;
        gridBagConstraints.ipady = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        ControlPanel.add(StartOntologyServer, gridBagConstraints);

        ConfigPanel.add(ControlPanel);

        getContentPane().add(ConfigPanel);

        Viewer.setMinimumSize(new java.awt.Dimension(300, 300));
        Viewer.setLayout(new javax.swing.BoxLayout(Viewer, javax.swing.BoxLayout.Y_AXIS));

        MsgsPanel.setLayout(new java.awt.BorderLayout());

        Msgs.setEditable(false);
        Msgs.setFont(new java.awt.Font("Courier New", 0, 14));
        MsgsPanel.add(Msgs, java.awt.BorderLayout.CENTER);
        MsgsPanel.add(jSplitPane1, java.awt.BorderLayout.CENTER);

        Search.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SearchActionPerformed(evt);
            }
        });
        MsgsPanel.add(Search, java.awt.BorderLayout.SOUTH);

        jTabbedPane1.addTab("NLG", MsgsPanel);

        statusBarPanel.setMinimumSize(new java.awt.Dimension(36, 12));
        statusBarPanel.setPreferredSize(new java.awt.Dimension(36, 30));
        statusBarPanel.setLayout(new javax.swing.BoxLayout(statusBarPanel, javax.swing.BoxLayout.Y_AXIS));
        jTabbedPane1.addTab("Logging", statusBarPanel);

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setText("Communication Server IP");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 130, -1));

        IP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                IPActionPerformed(evt);
            }
        });
        jPanel1.add(IP, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 20, 100, -1));

        jLabel3.setText("Communication Server port");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, -1, -1));
        jPanel1.add(port, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 50, 100, -1));

        jLabel4.setText("Pserver IP");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 130, -1));
        jPanel1.add(PserverIP, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 80, 100, -1));

        jLabel5.setText("Pserver port");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, 130, -1));

        PserverPort.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PserverPortActionPerformed(evt);
            }
        });
        jPanel1.add(PserverPort, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 110, 100, -1));

        jLabel7.setText("Pserver password");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 170, 145, -1));

        jLabel6.setText("Pserver User Name");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, 145, -1));
        jPanel1.add(PServerUserId, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 140, 100, -1));

        loadDBs.setText("Load Databases");
        loadDBs.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        loadDBs.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jPanel1.add(loadDBs, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 220, -1, -1));
        jPanel1.add(PserverPass, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 170, 100, -1));

        jTabbedPane1.addTab("Servers Config", jPanel1);

        Viewer.add(jTabbedPane1);

        getContentPane().add(Viewer);

        Basic.setText("Choose Ontology");
        Basic.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BasicActionPerformed(evt);
            }
        });

        wines.setText("wines");
        wines.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                winesActionPerformed(evt);
            }
        });
        Basic.add(wines);

        MPIRO.setText("M-PIRo");
        MPIRO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MPIROActionPerformed(evt);
            }
        });
        Basic.add(MPIRO);

        xenios.setText("xenios");
        xenios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                xeniosActionPerformed(evt);
            }
        });
        Basic.add(xenios);

        exit.setText("Exit");
        exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitActionPerformed(evt);
            }
        });
        Basic.add(exit);

        MainToolbar.add(Basic);

        jMenu1.setText("Save");

        SaveTexts.setText("Save texts");
        SaveTexts.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SaveTextsActionPerformed(evt);
            }
        });
        jMenu1.add(SaveTexts);

        MainToolbar.add(jMenu1);

        setJMenuBar(MainToolbar);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-956)/2, (screenSize.height-642)/2, 956, 642);
    }// </editor-fold>//GEN-END:initComponents

    private void PserverPortActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PserverPortActionPerformed
// TODO add your handling code here:
    }//GEN-LAST:event_PserverPortActionPerformed

    private void IPActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_IPActionPerformed
    {//GEN-HEADEREND:event_IPActionPerformed
// TODO add your handling code here:
    }//GEN-LAST:event_IPActionPerformed

    private void StopOntologyServerActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_StopOntologyServerActionPerformed
    {//GEN-HEADEREND:event_StopOntologyServerActionPerformed
        
        try
        {
            if(onto_server!=null)
            onto_server.die(); 
            
            if(myEngine !=null)
            this.nlgServer.die();
        }
        catch(Exception e)
        { 
            e.printStackTrace();
        }
    }//GEN-LAST:event_StopOntologyServerActionPerformed

    
    private DialogueManagerInfo DMI;
            
    private void StartOntologyServerActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_StartOntologyServerActionPerformed
    {//GEN-HEADEREND:event_StartOntologyServerActionPerformed
        start_onto_server();
    }//GEN-LAST:event_StartOntologyServerActionPerformed

    public void start_onto_server()
    {
         logger.info("trying to load DM info..."); 
        
        DMI = new DialogueManagerInfo();
        DMI.Load(NLFile.getAbsolutePath() + System.getProperty("file.separator") + "DM.rdf");
        
        logger.info("DM info loaded..."); 
            
        onto_server = new OntologyServer(this.GetCommunicationIP(),this.GetCommunicationPort());
        onto_server.setOntology(this.ontModel);
        onto_server.setDialogueManagerInfo(DMI);
        onto_server.start();
    }
    
    private void ClearPserveronlyforSelInstanceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ClearPserveronlyforSelInstanceActionPerformed
        if(UMVis!=null)
        {
            int index = ClsList.getSelectedIndex();
            
            
            if(index != -1 && getUserId() != null )
            {
                String objectURI = ((OntObject)Instances[index]).getURI();
                
                if(UMVis instanceof gr.aueb.cs.nlg.PServerEmu.UMVisitImpEmu)
                ((gr.aueb.cs.nlg.PServerEmu.UMVisitImpEmu)UMVis).resetPServer(objectURI, factsAndAssimilations, getUserId());
                logger.info("PServer was reseted the info about "+ objectURI + "!!!!! ");
            }
            else
            {
                logger.info("select an instance and a user id before reset PServer!!!!! ");
            }
        }
        else
        {
            logger.info("UMVis Null");
        }
    }//GEN-LAST:event_ClearPserveronlyforSelInstanceActionPerformed

    private void InitStatisticalTreeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_InitStatisticalTreeActionPerformed
        this.myEngine.initStatisticalTree();
        logger.info("Initialized the statistical tree for the comparisons....");
    }//GEN-LAST:event_InitStatisticalTreeActionPerformed

    private void GreekRBtnItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_GreekRBtnItemStateChanged
        refresh();
    }//GEN-LAST:event_GreekRBtnItemStateChanged
       
    private void EnglishRBtnItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_EnglishRBtnItemStateChanged
        refresh();
    }//GEN-LAST:event_EnglishRBtnItemStateChanged

    private void UserType_ComBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_UserType_ComBoxItemStateChanged
        refresh();
    }//GEN-LAST:event_UserType_ComBoxItemStateChanged
    
    String namespace = "http://www.aueb.gr/users/ion/mpiro.owl#";
    
   
    String [] exhibits = {"exhibit8", "exhibit11", "exhibit5", "exhibit46",
                              "exhibit6", "exhibit39", "exhibit49", "exhibit32",
                              "exhibit45", "exhibit38","exhibit40", "exhibit33",
                              "exhibit30", "exhibit21", "exhibit29", "exhibit35",
                              "exhibit16", "exhibit25", "exhibit26", "exhibit31",
                              "exhibit34", "exhibit19", "exhibit17", "exhibit7",
                              "exhibit2", "exhibit12", "exhibit14", "exhibit23",
                              "exhibit4", "exhibit50", "exhibit18", "exhibit44",
                              "exhibit1", "exhibit24", "exhibit10", "exhibit13",
                              "exhibit3", "exhibit20", "exhibit22", "exhibit36",
                              "exhibit43", "exhibit37", "exhibit15", "exhibit41",
                              "exhibit42", "exhibit28", "exhibit48", "exhibit9",
                              "exhibit47"};
        
    /* 
    String [] exhibits = {"exhibit15", "exhibit41", "exhibit42"};
     */ 
    
    private void SaveTextsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SaveTextsActionPerformed
                        
        try
        {
            exportWriter = new FileWriter("../texts.txt");            
                    
            Vector<String> MyUserIDs = new Vector<String>();
            Vector<String> MyUserTypes = new Vector<String>(); 
            
            if(Languages.isEnglish(getLanguage()))
            {
                MyUserTypes.add("http://www.owlnl.com/NLG/UserModelling#Child");
                MyUserTypes.add("http://www.owlnl.com/NLG/UserModelling#Adult");
                MyUserTypes.add("http://www.owlnl.com/NLG/UserModelling#ExpertAdult");
            }
            else
            {
                MyUserTypes.add("http://www.owlnl.com/NLG/UserModelling#ChildG");
                MyUserTypes.add("http://www.owlnl.com/NLG/UserModelling#AdultG");
                MyUserTypes.add("http://www.owlnl.com/NLG/UserModelling#ExpertAdultG");                
            }
            
            MyUserIDs.add("User" + (++user_counter));
            MyUserIDs.add("User" + (++user_counter)); 
            MyUserIDs.add("User" + (++user_counter));  
                    
            UMVis.newUser(MyUserIDs.get(0).toString() , MyUserTypes.get(0).toString());
            UMVis.newUser(MyUserIDs.get(1).toString() , MyUserTypes.get(1).toString());
            UMVis.newUser(MyUserIDs.get(2).toString() , MyUserTypes.get(2).toString());
             
            for(int i =0; i < exhibits.length; i++)
            {

                String objectURI = "http://www.aueb.gr/users/ion/mpiro.owl#" + exhibits[i];

                if(ontModel.getIndividual(objectURI) != null)
                {

                    //exportWriter.write("-----" + exhibits[i] + "-----");
                    exportWriter.write("\n\n");                                        
                    
                    for(int j = 0; j < MyUserIDs.size(); j++)
                    {
                                                
                        String result [] = myEngine.GenerateDescription(0, objectURI,  MyUserTypes.get(j).toString() , MyUserIDs.get(j).toString(), getDepth(), getMFPS(), false,getPersonality());
                        
                        exportWriter.write("UT:" +  MyUserTypes.get(j).toString());
                        exportWriter.write("\n\n");                    
                        exportWriter.write(result[1]);
                        exportWriter.write("\n\n");

                        exportWriter.flush();
                    }
                }
            }       
            
            exportWriter.close();
            
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
                
    }//GEN-LAST:event_SaveTextsActionPerformed

    private void xeniosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_xeniosActionPerformed
            // DEFAULTS
            NLFile = new File("../authoring-tool/XENIOS/");
            owlFile = new File("../authoring-tool/XENIOS/OwlTemp.owl");
            setPaths();
    }//GEN-LAST:event_xeniosActionPerformed

    private void MPIROActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MPIROActionPerformed
            // DEFAULTS
            NLFile = new File("../NLFiles-MPIRO");            
            owlFile = new File("../NLFiles-MPIRO/mpiro.owl");
            setPaths();
    }//GEN-LAST:event_MPIROActionPerformed

    private void winesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_winesActionPerformed
            // DEFAULTS

            NLFile = new File("../NLFiles");            
            owlFile = new File("../NLFiles/wine.owl");
            setPaths();
    }//GEN-LAST:event_winesActionPerformed

    private void ClearPServerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ClearPServerActionPerformed
        if(myEngine != null)
        {
            myEngine.initPServer();
            UsersCmb.removeAllItems();
            UsersCmb.addItem("none");            
            logger.info("PServer was initialized");
        }
        else
        {
            logger.info("NLG Engine has not been initialized");
        }
        
    }//GEN-LAST:event_ClearPServerActionPerformed

    private void DeleteUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DeleteUserActionPerformed
        try
        {
            if( UsersCmb.getSelectedItem().toString().compareTo("none") != 0)
            {
                String userName = UsersCmb.getSelectedItem().toString();
                //String userName = userNameWithUserType.substring(0, userNameWithUserType.indexOf("-"));

                if(UMVis instanceof gr.aueb.cs.nlg.PServerEmu.UMVisitImpEmu)
                ((gr.aueb.cs.nlg.PServerEmu.UMVisitImpEmu)UMVis).deleteUser(userName);
                
                UsersCmb.removeItem(UsersCmb.getSelectedItem());
                logger.info("User " + userName + " was deleted" );
            }            
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }        
    }//GEN-LAST:event_DeleteUserActionPerformed

    private void UsersCmbItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_UsersCmbItemStateChanged
        //userTypeForUser.setText();
    }//GEN-LAST:event_UsersCmbItemStateChanged

    private void CreateUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CreateUserActionPerformed
        
        try
        {
            if(getUserTypeSelected() != null)
            {
                String userName = /*"User" + */  "" +(user_counter++);
                
                Random generator=new Random();
                userName = String.valueOf(generator.nextInt(100000000));

                if(UMVis!=null)
                {
                    String ut = this.getUserTypeSelected();
                    //ut = ut.substring(ut.indexOf('#')+1,ut.length()); 
                    String un = userName;
                    
                    //if this user already exists
                    if(UMVis.checkUserExists(un))
                    {
                        UsersCmb.addItem(un);
                    }
                    else
                    {
                        UMVis.newUser(un, this.getUserTypeSelected());
                        UsersCmb.addItem(un);
			logger.info("New User: "+ un);
                    }
                }
                else
                {
                    logger.info("UMVis Null");
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
                    
    }//GEN-LAST:event_CreateUserActionPerformed

    private void Depth_ComBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_Depth_ComBoxItemStateChanged
        //this.depth = getDepth();
        refresh();
    }//GEN-LAST:event_Depth_ComBoxItemStateChanged

    private void RefreshBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RefreshBtnActionPerformed
                       
        refresh();
        
    }//GEN-LAST:event_RefreshBtnActionPerformed

    private void MAX_FACTS_PER_SENT_COMBOActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MAX_FACTS_PER_SENT_COMBOActionPerformed
// TODO add your handling code here:
    }//GEN-LAST:event_MAX_FACTS_PER_SENT_COMBOActionPerformed

    private void MAX_FACTS_PER_SENT_COMBOItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_MAX_FACTS_PER_SENT_COMBOItemStateChanged
        refresh();
    }//GEN-LAST:event_MAX_FACTS_PER_SENT_COMBOItemStateChanged

    private void GreekRBtnStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_GreekRBtnStateChanged
        
    }//GEN-LAST:event_GreekRBtnStateChanged

    private void EnglishRBtnStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_EnglishRBtnStateChanged

    }//GEN-LAST:event_EnglishRBtnStateChanged

    private void SelectNLFilesPathActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SelectNLFilesPathActionPerformed
        JFileChooser chooser = new JFileChooser("../");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        
        int returnVal = chooser.showOpenDialog(this);
        
        String fileName = "";
        File myfile = null;
        
        if(returnVal == JFileChooser.APPROVE_OPTION) 
        {
            fileName = chooser.getSelectedFile().getName();
            myfile = chooser.getCurrentDirectory();
            NLResourcesPath.setText(myfile.getAbsolutePath()+ System.getProperty("file.separator") + fileName);
        }
        
        if(fileName.compareTo("")!=0)
        {
            NLFile = new File(myfile.getAbsolutePath()+ System.getProperty("file.separator") + fileName);
        }
        else
        {
            NLFile = null;
            NLResourcesPath.setText("");
        }
    }//GEN-LAST:event_SelectNLFilesPathActionPerformed

    private void exitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitActionPerformed
        System.exit(0);
    }//GEN-LAST:event_exitActionPerformed

    private void BasicActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BasicActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_BasicActionPerformed

    private void SelectOntologyBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SelectOntologyBtnActionPerformed
        JFileChooser chooser = new JFileChooser("../ontologies/");
        
        ExampleFileFilter filter = new ExampleFileFilter();
        
        filter.addExtension("owl");
        filter.addExtension("rdf");
        filter.addExtension("xml");
        filter.setDescription(".owl , .rdf & .xml");
        chooser.setFileFilter(filter);
        
        int returnVal = chooser.showOpenDialog(this);
        
        String fileName = "";
        File myfile = null;
        if(returnVal == JFileChooser.APPROVE_OPTION) 
        {
            fileName = chooser.getSelectedFile().getName();
            myfile = chooser.getCurrentDirectory();
            path.setText(myfile.getAbsolutePath()+ System.getProperty("file.separator") + fileName);
            
            NLFile = chooser.getCurrentDirectory();
            this.NLResourcesPath.setText(NLFile.getAbsolutePath());
        }
        
        if(fileName.compareTo("")!=0)
        {
            owlFile = new File(myfile.getAbsolutePath()+ System.getProperty("file.separator") + fileName);
            NLFile = new File(myfile.getAbsolutePath());
        }
        else
        {
            owlFile = null;
            path.setText("");
        }
    }//GEN-LAST:event_SelectOntologyBtnActionPerformed

    private void GreekRBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GreekRBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_GreekRBtnActionPerformed

    
    
    private void ShowInstancesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ShowInstancesActionPerformed
        if(ontModel!=null)
        {
            showingIns = true;
            sampleModel = new DefaultListModel();
            
            v = new Vector();
            // load
            
            
            for(ExtendedIterator i = ontModel.listIndividuals(); i.hasNext();)
            {
            	Individual temp_ind= (Individual)i.next();
            	v.add(new OntObject(temp_ind.getLocalName(), temp_ind.getURI()));              
            }
           
            
           /*             
            for(int i = 0; i < exhibits.length; i++)
            {
                v.add(new OntObject(exhibits[i], namespace + exhibits[i]));
            }
              */
                        
            Instances = v.toArray();            

                                    
            for(int i = 0; i < Instances.length; i++)
            {
                sampleModel.addElement(((OntObject)Instances[i]).getURI());                     
            }
           
            //statisticalTree.print();
            //statisticalTree.sortAttributes();
                        
            //logger.debug("size " + sampleModel.getSize());
            ClsList.setModel(sampleModel);
            getContentPane().invalidate();
            getContentPane().validate();
	    
	    this.jTabbedPane1.setSelectedIndex(0);
        }
    }//GEN-LAST:event_ShowInstancesActionPerformed
    

                        
    private void ShowClassesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ShowClassesActionPerformed
        
        if(ontModel!=null)
        {
            showingIns= false;
            //AllClasses = new Vector();
            sampleModel = new DefaultListModel();
            v = new Vector();
            
            OntClass cls = null;
            int n = 0;
            for (Iterator i = ontModel.listClasses();  i.hasNext(); ){
                cls = (OntClass)i.next();
                if (!cls.isAnon()){
                    //logger.debug(++n + " " + cls.getURI());
                    //AllClasses.add(cls.getURI());
                    v.add(new OntObject(cls.getLocalName(),cls.getURI()));
                }
            }
            
            Classes = v.toArray();
            Arrays.sort(Classes,new NameComparatorImpl(true));
            
            for(int i = 0; i < Classes.length; i++)	
            sampleModel.addElement(((OntObject)Classes[i]).getURI());
            
            logger.debug("size " + sampleModel.getSize());
            
            ClsList.setModel(sampleModel);
            //ClsList.setVisibleRowCount(10);
            getContentPane().invalidate();
            getContentPane().validate();
	    this.jTabbedPane1.setSelectedIndex(0);
        }//if
        else
        {
            logger.info("Ontology Model is null");
        }
    }//GEN-LAST:event_ShowClassesActionPerformed
    
    public String GetCommunicationIP()
    {
        if(this.IP.getText().equals(""))
            return null;
        else
            return this.IP.getText();
    }
    
    public int GetCommunicationPort()
    {
        if(this.port.getText().equals(""))
            return -1;
        else            
            return Integer.parseInt(this.port.getText());
    }
    
    
    public String getPersonality()
    {
        return this.PersonalityTxt.getText();
    }
    
    
    public String GetPserverIP()
    {
        if(this.PserverIP.getText().equals(""))
            return "127.0.0.1";
        else
            return this.PserverIP.getText();
    }
    
    public int GetPserverPort()
    {
        if(this.PserverPort.getText().equals(""))
            return 1111;
        else            
            return Integer.parseInt(this.PserverPort.getText());
    }    
    
    public String GetPserverUserID()
    {
        if(this.PServerUserId.getText().equals(""))
            return "root";
        else
            return this.PServerUserId.getText();
    }
    
    public String GetPserverPass()
    {
        if(this.PserverPass.getText().equals(""))
            return "indigo";
        else            
            return PserverPass.getText();
    }
    
    private void ReadModelAndNLInfoBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ReadModelAndNLInfoBtnActionPerformed
        
        LoadNLG();        
        LoadNLI();
        
        intializeServer(true);
    }//GEN-LAST:event_ReadModelAndNLInfoBtnActionPerformed
    
    public void LoadNLI()
    {
        if(this.ProdGrammarsCB.isSelected())
        {
            // load the additional  rdfs
            
        }
    }
    
    NLGEngineServer nlgServer;
      
    
    public void intializeServer(boolean connecttoORCA)
    {
        try
        {
            if(connecttoORCA)
            {
                nlgServer = new NLGEngineServer(this.GetCommunicationIP(), this.GetCommunicationPort(), myEngine); // connect to navigation server
                nlgServer.setProduceFollowUpGrammars(this.ProdGrammarsCB.isSelected());                
                
                //navigation.setPriority(Thread.MIN_PRIORITY); // set low priority
                nlgServer.start();   // connect to navigation server
                myEngine.setServer(nlgServer);
                
            }
        }
        catch(Exception e)
        {
            logger.debug(" NAVIGATION SERVER NOT FOUND!!!!!");
        }
    }
            
    public void LoadNLG()
    {
        try
        {           
            myEngine = new NLGEngine(owlFile.getAbsolutePath(), 
                                     NLFile.getAbsolutePath(),  
                                     Languages.ENGLISH, 
                                     getServerType(), 
                                     getLoadDatabases(), // load Databases
                                     null, // microplans 
                                     null, // lexicon
                                     null, // user modelling 
                                     null, // ont model
                                     GetCommunicationIP(), // navigation_IP (String) (e.g. "127.0.0.1")
                                     GetCommunicationPort(),  // navigation_port (port) (e.g. 53000)
                                     this.GetPserverUserID(), // (String) database username (e.g. root)
                                     this.GetPserverPass(), // database password
                                     this.GetPserverIP(),  // (String) Pserver IP (e.g. "127.0.0.1")
                                     this.GetPserverPort() //(int) Pserver port (e.g. "1111")
                                     );  
            
            myEngine.initStatisticalTree();
            
            myEngine.initPServer();
                        
            UsersCmb.removeAllItems();
            UsersCmb.addItem("none"); 
            
            this.ontModel = myEngine.getModel();
            this.showUserTypes(myEngine.getUserTypes());
            this.UMVis = myEngine.getUMVisit();
            
            
            logger.info("Finished Loading Ontology & NLResources");
            System.out.println("Finished Loading Ontology & NLResources");
	    this.jTabbedPane1.setSelectedIndex(1);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            logger.info("ERROR");
        }        
    }
    
    
    public void showUserTypes(Iterator<String> iter)
    {        
        UserType_ComBox.removeAllItems();
        UserType_ComBox.addItem("none");
                
        while(iter.hasNext())
        {
            this.UserType_ComBox.addItem(iter.next());
        }
    }
    
    public String getLanguage()
    {
        if(EnglishRBtn.isSelected())
        {
            return Languages.ENGLISH;
        }
        else
        {
            return Languages.GREEK;
        }
    }
    
    public int getDepth()
    {
        return Integer.parseInt(Depth_ComBox.getSelectedItem().toString());
    }
    
    public String getUserTypeSelected()
    {
        if(UserType_ComBox.getSelectedItem() ==null)
            return null;
        
        String str = UserType_ComBox.getSelectedItem().toString();
                
        if(str.compareTo("none")==0)
        {
            return null;
        }
        else
        {
            return str;
        }        
    }
    
    public String getUserId()
    {
        if (UsersCmb == null)
            return null;
        
        if( UsersCmb.getSelectedItem() == null)
            return null;
            
        String str = UsersCmb.getSelectedItem().toString();
        
        if(str.compareTo("none")==0)
        {
            return null;
        }
        else
        {
            return str;
        }
    }
        
    public int getMFPS()
    {
        String str = MAX_FACTS_PER_SENT_COMBO.getSelectedItem().toString();
        
        if(str.compareTo("none")==0)
        {
            return -1;
        }
        else
        {
            return Integer.parseInt(str);
        }        
    }
    
    public boolean getServerType()
    {
        String str = ChooseServerCmb.getSelectedItem().toString();
        
        if(str.compareTo(this.AUEBPServerEmu) ==0 )
        {
            return true;
        }
        else
        {
            return false;
        }
        
    }
    
    boolean getLoadDatabases()
    {
        return this.loadDBs.isSelected();
    }
    
    public void refresh()
    {        
        //AGGRGT.set_MAX_FACTS_PER_SENTENCE(Integer.parseInt(sel_item));
        if(myEngine!=null)
        {
            String sel_item = (String)MAX_FACTS_PER_SENT_COMBO.getSelectedItem();
            myEngine.setLanguage(this.getLanguage()); 

            int a = ClsList.getSelectedIndex();

            if(a != -1)
            {
                ClsList.clearSelection();
                ClsList.setSelectedIndex(a);        
            }
        }
    }        
    
    public void setPaths()
    {
        try
        {
            if(NLFile.exists())
                NLResourcesPath.setText(NLFile.getCanonicalPath());                    
            else
                NLResourcesPath.setText("default NL Files not found");                    

            if(owlFile.exists())
                path.setText(owlFile.getCanonicalPath());                    
            else
                path.setText("default owl file not found"); 
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        System.exit(0);
    }//GEN-LAST:event_exitForm

    private void SearchActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_SearchActionPerformed
    {//GEN-HEADEREND:event_SearchActionPerformed
        System.err.println("!!!!!");
        String search_str = Search.getText();
        
       if(showingIns)
        {
            sampleModel.clear();;
                    
            for(int i = 0; i < Instances.length; i++)
            {
                    String curr_inst = ((OntObject)Instances[i]).getLocalName();

                    if(curr_inst.toLowerCase().startsWith(search_str.toLowerCase()))
                    {
                        sampleModel.addElement(((OntObject)Instances[i]).getURI());
                    }
            }
            
             
        }
        else
        {            
            for(int i = 0; i < Classes.length; i++)
            {
                    String curr_cls = ((OntObject)Classes[i]).getLocalName();
                    if(curr_cls.toLowerCase().startsWith(search_str.toLowerCase()))
                    {
                        sampleModel.addElement(((OntObject)Classes[i]).getURI());
                    }
            }//for
        }
        
        ClsList.setModel(sampleModel);
}//GEN-LAST:event_SearchActionPerformed

private void ProdGrammarsCBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ProdGrammarsCBActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_ProdGrammarsCBActionPerformed
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) 
    {
    
        try
        {
            //logger.setLevel(Level.INFO);
            //TheLogger.setLevel(Level.INFO);

            //NLFiles_logger.setLevel(Level.DEBUG);
            //Utils_Loggers.setLevel(Level.INFO);
            //PServerEmu_loggers.setLevel(Level.INFO);
            //NLGEngine_loggers.setLevel(Level.INFO);

            PropertyConfigurator logConf = new PropertyConfigurator();
            String log4jpath = "log4j.properties";

            File ffff = new File(log4jpath);
            System.err.println("log4jpath" + ffff.getAbsolutePath());

            if(ffff.exists())
            {
                logConf.configure(ffff.getAbsolutePath());
            }


            NLGS_GUI nlgs = new NLGS_GUI();

            if(args.length ==4)
            {

                if(args[0]!=null)
                {
                  File f = new File(args[0]);

                  if(f.exists())
                  {
                      System.err.println("exists");
                      nlgs.path.setText(f.getCanonicalPath());
                      nlgs.owlFile = f;
                  }
                  
                }


                if(args[1]!=null)
                {
                  File f = new File(args[1]);

                  if(f.exists())
                  {
                      System.err.println("exists");
                      nlgs.NLResourcesPath.setText(f.getCanonicalPath());  
                      nlgs.NLFile = f;
                  }
                  
                }


                if(args[2]!=null)
                {
                    if(args[2].equals("el"))
                    {
                        nlgs.GreekRBtn.setSelected(true);
                    }
                    else
                    {
                        nlgs.GreekRBtn.setSelected(false);
                    }
                }


                if(args[3]!=null)
                {
                    if(args[3].equals("true"))
                    {
                        nlgs.ChooseServerCmb.setSelectedItem("AUEB Emulator");
                    }
                    else
                    {
                        nlgs.ChooseServerCmb.setSelectedItem("Dimokritos PServer");
                    }
                }
                
                
                nlgs.LoadNLG();
                nlgs.LoadNLI();
                
                nlgs.intializeServer(true);
                
                nlgs.start_onto_server();
            }        


            nlgs.show();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
        /*SelectedModelSpec = actionEvent.getActionCommand();
        logger.info(SelectedModelSpec);
        for(int i = 0; i < ontModelChoicesStr.length; i++){
            if(ontModelChoicesStr[i].compareTo(SelectedModelSpec)==0){
                ModelSpecIndex = i;
                logger.info(SelectedModelSpec + "(" + ModelSpecIndex + ")" );
                logger.debug("ModelSpecIndex " + ModelSpecIndex);
            }
        }*/
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu Basic;
    private javax.swing.JComboBox ChooseServerCmb;
    private javax.swing.JLabel ChooseServerlbl;
    private javax.swing.JButton ClearPServer;
    private javax.swing.JButton ClearPserveronlyforSelInstance;
    private javax.swing.JCheckBox ComparisonsCB;
    private javax.swing.JPanel ConfigPanel;
    private javax.swing.JPanel ControlPanel;
    private javax.swing.JButton CreateUser;
    private javax.swing.JButton DeleteUser;
    private javax.swing.JComboBox Depth_ComBox;
    private javax.swing.JLabel Depth_lbl;
    private javax.swing.JRadioButton EnglishRBtn;
    private javax.swing.JRadioButton GreekRBtn;
    private javax.swing.JTextField IP;
    private javax.swing.JButton InitStatisticalTree;
    private javax.swing.ButtonGroup LangSelection;
    private javax.swing.JLabel Language_lbl;
    private javax.swing.JComboBox MAX_FACTS_PER_SENT_COMBO;
    private javax.swing.JLabel MAX_FACTS_PER_SENT_LABEL;
    private javax.swing.JMenuItem MPIRO;
    private javax.swing.JMenuBar MainToolbar;
    private javax.swing.ButtonGroup ModelSpec;
    private javax.swing.JTextArea Msgs;
    private javax.swing.JPanel MsgsPanel;
    private javax.swing.JTextField NLResourcesPath;
    private javax.swing.JLabel NLResources_lbl;
    private javax.swing.JTextField PServerUserId;
    private javax.swing.JLabel PersonalityLbl;
    private javax.swing.JTextField PersonalityTxt;
    private javax.swing.JCheckBox ProdGrammarsCB;
    private javax.swing.JTextField PserverIP;
    private javax.swing.JPasswordField PserverPass;
    private javax.swing.JTextField PserverPort;
    private javax.swing.JButton ReadModelAndNLInfoBtn;
    private javax.swing.JButton RefreshBtn;
    private javax.swing.JMenuItem SaveTexts;
    private javax.swing.JTextField Search;
    private javax.swing.JButton SelectNLFilesPath;
    private javax.swing.JButton SelectOntologyBtn;
    private javax.swing.JButton ShowClasses;
    private javax.swing.JButton ShowInstances;
    private javax.swing.JCheckBox ShowInterOuputsCB;
    private javax.swing.JButton StartOntologyServer;
    private javax.swing.JButton StopOntologyServer;
    private javax.swing.JComboBox UserType_ComBox;
    private javax.swing.JLabel UserType_lbl;
    private javax.swing.JLabel Userlbl;
    private javax.swing.JComboBox UsersCmb;
    private javax.swing.JPanel Viewer;
    private javax.swing.JPanel WestConfigPanel;
    private javax.swing.JMenuItem exit;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JCheckBox loadDBs;
    private javax.swing.JTextField path;
    private javax.swing.JTextField port;
    private javax.swing.JPanel statusBarPanel;
    private javax.swing.JMenuItem wines;
    private javax.swing.JMenuItem xenios;
    // End of variables declaration//GEN-END:variables

    //--------------------------------------------------------------------
    private class MyListListener implements ListSelectionListener 
    {
       public void valueChanged(ListSelectionEvent event) 
       {       		
            if (!event.getValueIsAdjusting()) 
            {
                if (!ClsList.isSelectionEmpty())
                {
                    try
                    {
                        int index = ClsList.getSelectedIndex();
                        String selection; 

                            if (showingIns)
                            {
                                //logger.debug(" *****:" +Instances.length);
                                if (Instances== null)
                                logger.debug(" *****:null" + index);

                                String objectURI = ClsList.getSelectedValue().toString();
                                
                                logger.info("semantic-syntactic annotations...\n");
                                logger.info(objectURI + "\n");
                                                                
                                String result [] = myEngine.GenerateDescription(0, objectURI, getUserTypeSelected(), getUserId(), getDepth(), getMFPS(),  ComparisonsCB.isSelected(),getPersonality());
                                
                                logger.info(result[0]);                     
                                logger.info(result[1]);                                   
                                logger.info(result [2]);
                                
                                factsAndAssimilations = myEngine.getFactsAndAssimilations();
                                
                                if(ShowInterOuputsCB.isSelected())
                                    Msgs.setText(result[0]);
                                else
                                    Msgs.setText("\n"  +result[1]);

                            }//if			                        
                            else
                            {
                                //logger.debug(" *****:" +Instances.length);
                                if (Instances== null)
                                logger.debug(" *****:null" + index);

                                String objectURI = ((OntObject)Classes[index]).getURI();
                                logger.info(objectURI);
                                
                                String result [] = myEngine.GenerateDescription(1, objectURI, getUserTypeSelected() , getUserId(), getDepth(), getMFPS(), ComparisonsCB.isSelected(),getPersonality());
                                Msgs.setText(result[0]);                       
                            }//else
                    }//try
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }
                }//if ! selection empty
            }//if
        }//valueChanged    	
    }//MyListListener
 	//--------------------------------------------------------------------    
    private class MyKeyListener implements KeyListener 
    {
        public void keyPressed(KeyEvent event) 
        {

                char ch = event.getKeyChar();
                int keyCode = event.getKeyCode();
                boolean valid = false;
                /*
                if( keyCode == KeyEvent.VK_A ||
                        keyCode == KeyEvent.VK_B ||
                        keyCode == KeyEvent.VK_C ||
                        keyCode == KeyEvent.VK_D ||
                        keyCode == KeyEvent.VK_E ||
                        keyCode == KeyEvent.VK_F ||
                        keyCode == KeyEvent.VK_G ||
                        keyCode == KeyEvent.VK_H ||
                        keyCode == KeyEvent.VK_I ||
                        keyCode == KeyEvent.VK_J ||
                        keyCode == KeyEvent.VK_K ||
                        keyCode == KeyEvent.VK_L ||
                        keyCode == KeyEvent.VK_M ||
                        keyCode == KeyEvent.VK_N ||
                        keyCode == KeyEvent.VK_O ||
                        keyCode == KeyEvent.VK_P ||
                        keyCode == KeyEvent.VK_Q ||
                        keyCode == KeyEvent.VK_R ||
                        keyCode == KeyEvent.VK_S ||
                        keyCode == KeyEvent.VK_T ||
                        keyCode == KeyEvent.VK_U ||
                        keyCode == KeyEvent.VK_V ||
                        keyCode == KeyEvent.VK_W ||
                        keyCode == KeyEvent.VK_X ||
                        keyCode == KeyEvent.VK_Y ||
                        keyCode == KeyEvent.VK_Z ||
                        keyCode == KeyEvent.VK_BACK_SPACE ||
                        keyCode == KeyEvent.VK_ENTER)
                {
                        valid = true;
                }
                */
                if( keyCode != KeyEvent.VK_ESCAPE &&
                        keyCode != KeyEvent.VK_SHIFT &&
                        keyCode != KeyEvent.VK_CAPS_LOCK &&
                        keyCode != KeyEvent.VK_CONTROL &&
                        keyCode != KeyEvent.VK_ALT &&
                        keyCode != KeyEvent.VK_HOME &&
                        keyCode != KeyEvent.VK_END &&
                        keyCode != KeyEvent.VK_PAGE_DOWN &&
                        keyCode != KeyEvent.VK_PAGE_UP &&
                        keyCode != KeyEvent.VK_F1 &&
                        keyCode != KeyEvent.VK_F2 &&
                        keyCode != KeyEvent.VK_F3 &&
                        keyCode != KeyEvent.VK_F4 &&
                        keyCode != KeyEvent.VK_F5 &&
                        keyCode != KeyEvent.VK_F6 &&
                        keyCode != KeyEvent.VK_F7 &&
                        keyCode != KeyEvent.VK_F8 &&
                        keyCode != KeyEvent.VK_F9 &&
                        keyCode != KeyEvent.VK_F10 &&
                        keyCode != KeyEvent.VK_F11 &&
                        keyCode != KeyEvent.VK_F12)
                {
                        valid = true;
                }			
                if(!event.isActionKey() && valid)
                {
                    logger.debug("pressed:" + ch + (int)ch);
                    if(sb == null)
                    {
                        if(!Character.isWhitespace(ch))
                        {
                                sb = new StringBuffer();
                                sb.append(ch);
                                logger.info("Looking for:" + new String(sb));
                        }
                    }
                    else
                    {
                            if(Character.isWhitespace(ch))
                            {
                                String srch_str = new String(sb);

                                sb = null;
                                if(showingIns)
                                {
                                    //logger.debug("Looking for " + srch_str + "in instances");
                                    for(int i = 0; i < Instances.length; i++)
                                    {
                                            String curr_inst = ((OntObject)Instances[i]).getLocalName();

                                            if(curr_inst.toLowerCase().startsWith(srch_str.toLowerCase()))
                                            {
                                                    ClsList.setSelectedIndex(i); 
                                                    ClsList.ensureIndexIsVisible(i);
                                                    logger.info("found:" + i);
                                                    i =  Instances.length;
                                            }					
                                    }
                                }
                                else
                                {
                                    logger.debug("Looking for " + srch_str + "in classes");
                                    for(int i = 0; i < Classes.length; i++)
                                    {
                                            String curr_cls = ((OntObject)Classes[i]).getLocalName();
                                            if(curr_cls.toLowerCase().startsWith(srch_str.toLowerCase()))
                                            {
                                                    ClsList.setSelectedIndex(i); 
                                                    ClsList.ensureIndexIsVisible(i);
                                                    logger.info("found:" + i);
                                                    i =  Classes.length;

                                            }
                                    }//for
                                }

                            }//if
                            else
                            {
                                    if((int)ch!=8 && (int)ch!= KeyEvent.VK_DELETE)
                                    {
                                        sb.append(ch);	 					
                                    }
                                    else
                                    {
                                        if(sb.length()>=1)
                                        sb.deleteCharAt(sb.length()-1);
                                    }
                                    
                                    logger.info("Looking for:" + new String(sb));
                            }

                    }//else	
                }			 
        }//keypressed

        public void keyReleased(KeyEvent event) {
        }

        public void keyTyped(KeyEvent event) {
        } 		
    }//MyKeyListener class

}//GUI