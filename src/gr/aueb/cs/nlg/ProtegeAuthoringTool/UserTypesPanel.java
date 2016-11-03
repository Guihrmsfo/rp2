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

import javax.swing.*; 
import javax.swing.event.*; 
import javax.swing.tree.*; 
import javax.accessibility.*; 
import java.awt.*; 
import java.awt.event.*; 
import java.util.*; 
import java.io.*; 
import java.applet.*; 
import javax.swing.JPopupMenu;
 
import edu.stanford.smi.protege.model.*;
import edu.stanford.smi.protege.ui.*;
import edu.stanford.smi.protege.util.*;
import edu.stanford.smi.protege.widget.*;
import edu.stanford.smi.protege.resource.*;

import edu.stanford.smi.protege.resource.Icons;
//import edu.stanford.smi.protege.ui.FrameRenderer;
//import edu.stanford.smi.protege.util.LabeledComponent;
//import edu.stanford.smi.protege.widget.AbstractTabWidget;
//import edu.stanford.smi.protegex.owl.model.OWLModel;
//import edu.stanford.smi.protegex.owl.model.RDFSClass;
//import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;
//import edu.stanford.smi.protegex.owl.model.event.ModelAdapter;
//import edu.stanford.smi.protegex.owl.ProtegeOWL;
import gr.aueb.cs.nlg.NLFiles.UserModellingQueryManager;

import gr.aueb.cs.nlg.NLFiles.*;

import com.hp.hpl.jena.rdf.model.*;

import org.apache.log4j.Logger;


public class UserTypesPanel extends JPanel 
{    
    static Logger logger = Logger.getLogger(UserTypesPanel.class.getName());
    
    private JTree UserTypesTree;
    private JSplitPane splitPane;
    private DefaultMutableTreeNode root ;
                
    private String rootNodeName = "UserTypes";
    private TreePath treePath = null;
        
    private JPopupMenu NewUserTypePopUp = null;
    private JPopupMenu RenameDeleteUserTypePopUp = null;
    
    private JMenuItem NewUserTypeMenuItem = null;    
    private JMenuItem RenameUserTypeMenuItem = null;
    private JMenuItem DeleteUserTypeMenuItem = null;
        
    private UserTypesParametersPanel UTPP;
            
    private UserModellingQueryManager UMQM;
    
    private ComboHeaderComponent header ;
    private JLabel Resourcelabel;
    
    public UserTypesPanel(UserModellingQueryManager UMQM) 
    {
        this.UMQM = UMQM;
        initComponents();
                                
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, createUserTypesTree(), createUserTypesParametersPanel()); 
        
        splitPane.setContinuousLayout(true); 
 	splitPane.setOneTouchExpandable(true); 
  
        splitPane.setDividerLocation(200);
        
        UserTypesHeaderPanel.add(createHeader());
        UserTypesViewer.add(splitPane, BorderLayout.CENTER);
        
        UserTypesTree.addTreeSelectionListener(new TreeSelectionListener ()
        {            
            public void valueChanged(TreeSelectionEvent e) 
            {
                 if(treePath != null)
                 {                     
                     treePath = e.getPath();
                     transmitSelection();
                 }                
            }
        });
                
        //popup menus
        NewUserTypePopUp = new JPopupMenu() ; 
        RenameDeleteUserTypePopUp = new JPopupMenu() ; 
    
        // new user type 
        JMenuItem NewUserTypeMenuItem = NewUserTypePopUp.add("Create User Type");    
                
        NewUserTypeMenuItem.addActionListener(new java.awt.event.ActionListener() 
        {
            public void actionPerformed(java.awt.event.ActionEvent evt) 
            {
               NewUserTypeMenuItemActionPerformed(evt);
            }
        });
        
        //rename user type
        JMenuItem RenameUserTypeMenuItem = RenameDeleteUserTypePopUp.add("Rename");
        
        RenameUserTypeMenuItem.addActionListener(new java.awt.event.ActionListener() 
        {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
               RenameUserTypeMenuItemActionPerformed(evt);
            }
        });
        
        // delete user type
        JMenuItem DeleteUserTypeMenuItem = RenameDeleteUserTypePopUp.add("Delete");  
        
        DeleteUserTypeMenuItem.addActionListener(new java.awt.event.ActionListener() 
        {
            public void actionPerformed(java.awt.event.ActionEvent evt) 
            {
               DeleteUserTypeMenuItemActionPerformed(evt);
            }
        });
                                            
        transmitSelection();
        LoadUserTypes();
    }
    
    
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        UserTypesHeaderPanel = new javax.swing.JPanel();
        UserTypesViewer = new javax.swing.JPanel();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

        UserTypesHeaderPanel.setLayout(new java.awt.BorderLayout());

        add(UserTypesHeaderPanel);

        UserTypesViewer.setLayout(new java.awt.BorderLayout());

        add(UserTypesViewer);

    }// </editor-fold>//GEN-END:initComponents

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel UserTypesHeaderPanel;
    private javax.swing.JPanel UserTypesViewer;
    // End of variables declaration//GEN-END:variables
    
    public ComboHeaderComponent createHeader()
    {
        Resourcelabel = ComponentFactory.createLabel("", Icons.getProjectIcon(), SwingConstants.LEFT);
        String forProject = "Project";
        String UserTypesBrowser = "User Types";
                
        // create header component
        header = new ComboHeaderComponent(UserTypesBrowser, forProject, Resourcelabel);
        return header;
    }
    
    public void setProjectName(String resource)
    {
        Resourcelabel.setText(resource);
    }
        
    public void NewUserTypeMenuItemActionPerformed(java.awt.event.ActionEvent evt)
    {
        String newName = AskNewUserType(  "New User type",  "give user type?",  "");
        
        if(newName.compareTo("") !=0)
        {
            DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(newName);
            root.add(newNode);
            ((DefaultTreeModel)UserTypesTree.getModel()).reload();
            
            NLPlugin.getUserModellingQueryManager().AddUserType(newName);
            transmitSelection();
        }
    }
          
    public void RenameUserTypeMenuItemActionPerformed(java.awt.event.ActionEvent evt)
    {
        DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode)  treePath.getLastPathComponent();
        
                
        String oldname = currentNode.getUserObject().toString();
        String newName = AskNewName (currentNode.toString() ,  "Rename User Type",  "New User Type Name",  "");
        
        logger.debug("RenameUserType " + oldname + " " + newName); 
        
        currentNode.setUserObject(newName);
        ((DefaultTreeModel)UserTypesTree.getModel()).reload(currentNode);
        
        if(newName!=null)
        {
            NLPlugin.getUserModellingQueryManager().RenameUserType(NLPlugin.getUserModellingQueryManager().NLGUserModellingNS + oldname, NLPlugin.getUserModellingQueryManager().NLGUserModellingNS + newName);
            NLPlugin.getLexicon().RenameUserType(NLPlugin.getUserModellingQueryManager().NLGUserModellingNS + oldname, NLPlugin.getUserModellingQueryManager().NLGUserModellingNS + newName);
        }
        
        transmitSelection();
    }
            
    public void DeleteUserTypeMenuItemActionPerformed(java.awt.event.ActionEvent evt)
    {
        logger.debug("DeleteUserTypeMenuItemActionPerformed");
        DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode)  treePath.getLastPathComponent();
        
        logger.debug(currentNode.toString());                
        ((DefaultTreeModel)UserTypesTree.getModel()).removeNodeFromParent(currentNode);
        
        treePath = null;
        
        NLPlugin.getUserModellingQueryManager().DeleteUserType(currentNode.toString());
        transmitSelection();
    }
            
    private String AskNewName( String oldName,  String title,  String question,  String response) 
    { 
 	 	
        String result = JOptionPane.showInputDialog(this, question, oldName);
        
        if(result ==null)
        {
           JOptionPane.showMessageDialog(this, "null name");  
           return oldName;            
        }
        else if(result.compareTo(oldName)==0)
        {    
           JOptionPane.showMessageDialog(this, "you have given the same name");                         
        }
        else if(result.compareTo("")==0)
        {
           JOptionPane.showMessageDialog(this, "null name");  
           return oldName;
        }
 	
        Enumeration childs = root.children();
        while(childs.hasMoreElements())
        {
            DefaultMutableTreeNode currentChild = (DefaultMutableTreeNode)childs.nextElement();
            
            if(currentChild.getUserObject().toString().compareTo(result) ==0)
            {
               logger.debug(currentChild.getUserObject().toString());
               JOptionPane.showMessageDialog(this, "this name already exists");  
               return oldName;                
            }
                
        }
        
        return result;
   } 
    
    private String AskNewUserType(  String title,  String question,  String response) 
    { 
 	 	
        String result = JOptionPane.showInputDialog(this, question);
        
        if(result == null)
        {
           JOptionPane.showMessageDialog(this, "null name");  
           return "";            
        }                      
        else if(result.compareTo("")==0)
        {
           JOptionPane.showMessageDialog(this, "null name");  
           return "";
        }
 	
        Enumeration childs = root.children();
        while(childs.hasMoreElements())
        {
            DefaultMutableTreeNode currentChild = (DefaultMutableTreeNode)childs.nextElement();
            
            if(currentChild.getUserObject().toString().compareTo(result) ==0)
            {
               logger.debug(currentChild.getUserObject().toString());
               JOptionPane.showMessageDialog(this, "this name already exists");  
               return "";                
            }
                
        }
        
        logger.debug(result);
        return result;
   } 
    
    public void LoadUserTypes()
    {
        root.removeAllChildren();
     
        if(NLPlugin.getUserModellingQueryManager() != null)
        {
            Iterator<String> userTypes = NLPlugin.getUserModellingQueryManager().getUserTypes();


            while(userTypes.hasNext())
            {
                String  ut = userTypes.next();

                ut = ut.substring(ut.indexOf("#") + 1);
                root.add(new DefaultMutableTreeNode(ut));
            }
        }
    }
    
    public JScrollPane createUserTypesTree() 
    {
        root = new DefaultMutableTreeNode(rootNodeName);             
        
        UserTypesTree = new JTree(root);
                
        UserTypesTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
                  
        
        /*
        UserTypesTree.addFocusListener(new FocusListener()
        {
            java.awt.event.Fo
        });
         */
        
        UserTypesTree.addMouseListener(new  java.awt.event.MouseAdapter()
        {
            public void mouseClicked(MouseEvent e)
            {
                
                 treePath = UserTypesTree.getPathForLocation(e.getX(), e.getY());
                 
                 //if(treePath != null)
                 //{
                 //    transmitSelection();
                 //}
                 
                 if (SwingUtilities.isRightMouseButton(e)) 
                 {                                                                                   
                     if(treePath !=null)
                     {
                        //JOptionPane.showMessageDialog(null,"Right Mouse Button " + treePath.toString() );
                        String Selection = treePath.getLastPathComponent().toString();
                        
                        if(Selection.compareTo(rootNodeName) == 0)
                        {
                            showAddNewUserTypePopup(e);
                        }
                        else
                        {
                            showRenameDeleteUserTypePopup(e);
                        }
                     }
                                          
                 }
                 
            }
      
        });
        
        return new JScrollPane(UserTypesTree); 
    }   
    
    public JScrollPane createUserTypesParametersPanel() 
    {
        UTPP = new UserTypesParametersPanel(NLPlugin.getUserModellingQueryManager());
        return new JScrollPane(UTPP); 
    }
    
    private void showRenameDeleteUserTypePopup(MouseEvent e)
    {
        RenameDeleteUserTypePopUp.show(this,e.getX(), e.getY());
    }
            
    private  void showAddNewUserTypePopup(MouseEvent e)
    {                        
        NewUserTypePopUp.show(this,e.getX(), e.getY());
    }
    
    
    private void transmitSelection()
    {
        String Msg = "Choose a user type";

        if(treePath !=null)
        {            
            String Selection = treePath.getLastPathComponent().toString();
            
            if(Selection.compareTo(rootNodeName) != 0)
            {
                    Msg = Selection;
            }
        } 
     
        UTPP.setUserType(Msg);   
        
        if(Msg.compareTo("Choose a user type")==0)
        {
            UTPP.setVisibleParameters(false);
        }
        else
        {
            // make um parameters combos's visible
            UTPP.setVisibleParameters(true);
            //set parameters
            UserTypeParameters UTParams = NLPlugin.getUserModellingQueryManager().getParametersForUserType(NLPlugin.getUserModellingQueryManager().NLGUserModellingNS + Msg);
            
            if(UTParams !=null)
            UTPP.setParameters(UTParams.getFactsPerPage(), 1, UTParams.getMaxFactsPerSentence(), UTParams.getSynthesizerVoice(), UTParams.getLang());
        }
    }
    
    /*
     *  A main mathod for testing
     */
    public static void main(String args[])
    {        
        JFrame testFrame = new JFrame();
        testFrame.pack();
        
        testFrame.addWindowListener(new java.awt.event.WindowAdapter() 
        {
            public void windowClosing(java.awt.event.WindowEvent evt) 
            {                
                    System.exit(0);                
            }
        });
        
        UserModellingQueryManager UMQM = new UserModellingQueryManager(null);
                
        UserTypesPanel UTP = new UserTypesPanel(UMQM);
        testFrame.setLayout(new java.awt.BorderLayout());
        testFrame.add(UTP);
        testFrame.show();        
    }    
}
