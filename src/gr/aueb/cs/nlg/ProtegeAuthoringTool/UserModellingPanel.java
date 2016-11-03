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

import javax.swing.table.*;
                
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
import edu.stanford.smi.protegex.owl.model.event.*;
import edu.stanford.smi.protegex.owl.model.*;
import edu.stanford.smi.protegex.owl.model.NamespaceManager;
import edu.stanford.smi.protegex.owl.model.NamespaceManagerAdapter;

import gr.aueb.cs.nlg.Languages.Languages;
import gr.aueb.cs.nlg.Utils.OntResourcesArray;
import gr.aueb.cs.nlg.NLFiles.*;

import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.vocabulary.*;
import com.hp.hpl.jena.ontology.*;
import com.hp.hpl.jena.util.iterator.*;
import com.hp.hpl.jena.datatypes.*; 
import com.hp.hpl.jena.shared.PrefixMapping;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.graph.TripleMatchFilter;

import org.apache.log4j.Logger;
import org.apache.log4j.BasicConfigurator;

public class UserModellingPanel extends javax.swing.JPanel 
{
    static Logger logger = Logger.getLogger(UserModellingPanel.class.getName());

    private Vector myDATA; 
    private Vector names;
    private JTable propertiesTable;
    private DefaultTableModel dataModel;
    private JScrollPane scrollpane;
          
    private String EditInterestRepetitionsLabel = "Edit Interest-Repetitions";
    private String EditDefaultInterestRepetitionsLabel = "Edit Default interest-Repetitions";
    
    private ComboHeaderComponent header;
            
    private JLabel Resourcelabel;
           
    private JPopupMenu TablePopup;
    private JPopupMenu TablePopup2;
            
    private UserModellingQueryManager UMQM;
    
    private String ResourceURI = "";
    private String ClassURI = "";
    
    private boolean isClass = false;
    
    private JMenuItem EditInterestRepetitionsMI;
    private JMenuItem EditDefaultInterestRepetitionsMI;
            
    public UserModellingPanel(UserModellingQueryManager UMQM) 
    {
        this.UMQM = UMQM;
        
        initComponents();
        myDATA  = new Vector(); 
        names = new Vector();
        
        // add test data
        names.add("Property");
        names.add("Filler");
        
        for(int i = 0; i < 20; i++ )
        {
            Vector row = new Vector();
            row.add("Mitsos");
            row.add("Maria");
            
            myDATA.add(row);
                    
        }
        
        TablePopup = new JPopupMenu();
        TablePopup2 = new JPopupMenu();
        
        EditInterestRepetitionsMI = TablePopup.add(EditInterestRepetitionsLabel);    
        EditDefaultInterestRepetitionsMI = TablePopup.add(EditDefaultInterestRepetitionsLabel);    
        
        //JMenuItem EditInterestRepetitionsMI2  = TablePopup2.add(EditInterestRepetitionsLabel);
         
        // edit interest repetitions parameters
        EditInterestRepetitionsMI.addActionListener(new java.awt.event.ActionListener() 
        {
            public void actionPerformed(java.awt.event.ActionEvent evt) 
            {
               showParametersActionPerformed(evt, 1);
            }
        });
        
        /*
        // edit interest repetitions parameters
        EditInterestRepetitionsMI2.addActionListener(new java.awt.event.ActionListener() 
        {
            public void actionPerformed(java.awt.event.ActionEvent evt) 
            {
               showParametersActionPerformed(evt, 1);
            }
        });
          */
        
        // edit default interest repetitions parameters
        EditDefaultInterestRepetitionsMI.addActionListener(new java.awt.event.ActionListener() 
        {
            public void actionPerformed(java.awt.event.ActionEvent evt) 
            {
               showParametersActionPerformed(evt, 2);
            }
        });
                
        dataModel = new DefaultTableModel(myDATA,names)  
        { 
             public boolean isCellEditable(int row, int col) 
             {                 
                 return false;
             } 
       };
       
       propertiesTable = new JTable(dataModel);
       propertiesTable.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
       
       propertiesTable.setRowSelectionAllowed(true);
       propertiesTable.setRowSelectionInterval(1,1);
       
       propertiesTable.addMouseListener(new  java.awt.event.MouseAdapter()
       {
            public void mouseClicked(MouseEvent e)
            {
                                  
                 if (SwingUtilities.isRightMouseButton(e)) 
                 { 
                    propertiesTable.setColumnSelectionAllowed(false);
                    propertiesTable.setRowSelectionAllowed(true);
                    
                    //int row = propertiesTable.getSelectedRow();
                    int row = propertiesTable.rowAtPoint(new Point(e.getX(), e.getY()));
                         
                    
                    //System.err.println(propertiesTable.getComponentAt(e.getX(), e.getY()).toString());
                    
                    //propertiesTable.setColumnSelectionInterval(0,0);
                    propertiesTable.setRowSelectionInterval(row, row);
                                        
                    String property = dataModel.getValueAt(row, 0).toString();
                    
                    if(property.equals(RDF.type.getURI())) 
                    {
                        EditInterestRepetitionsMI.setText( "Interest-repetitions of this property for this instance" ) ;
                        EditDefaultInterestRepetitionsMI.setText("Interest-repetitions of this property for all instances of the selected class");            
                        
                        EditInterestRepetitionsMI.setVisible(true);
                        EditDefaultInterestRepetitionsMI.setVisible(true);
                    }
                    else if(property.equals(RDFS.subClassOf.getURI()))
                    {
                        EditInterestRepetitionsMI.setText( "Interest-repetitions of this property for this class" ) ;
                        EditDefaultInterestRepetitionsMI.setText("Default interest-repetitions of this property for all superclasses of this class");            
                        
                        EditInterestRepetitionsMI.setVisible(true);
                        EditDefaultInterestRepetitionsMI.setVisible(true);                        
                    }
                    else
                    {
                        if(isClass)
                        {
                            EditInterestRepetitionsMI.setText( "Default interest-repetitions of this property for all instances of this class" ) ;
                            EditDefaultInterestRepetitionsMI.setText("Default interest-repetitions of this property for all instances of any class");            
                        
                            EditInterestRepetitionsMI.setVisible(true);
                            EditDefaultInterestRepetitionsMI.setVisible(true);     
                        }
                        else
                        {
                            EditInterestRepetitionsMI.setText( "Interest-repetitions of this property for this instance" ) ;
                            //EditDefaultInterestRepetitionsMI.setText("Default interest-repetitions of this property for all instances of this class");            
                        
                            EditInterestRepetitionsMI.setVisible(true);
                            EditDefaultInterestRepetitionsMI.setVisible(false);                                
                        }
                    }
                    
                    TablePopup.show(propertiesTable, e.getX(), e.getY());                    
                 }                
            }
      
       });
        
       header = createUserModellingBrowserHeader();
       this.add(header);
             

       scrollpane = new JScrollPane(propertiesTable);
       this.add(scrollpane);
    }//UserModellingPanel
    
    public Vector getUMParametersForIsA(String subjectURI, String objectURI, int type)
    {
        if(UMQM == null)
        {
            Vector v = new Vector();
            
            Vector row = new Vector();
            row.add("1");
            row.add("2");
            row.add("3");
            
            v.add(row);
            return v;
        }
        
        logger.debug("getUMParametersForIsA " + subjectURI + " " + objectURI);
        Iterator<String> userTypesIter = UMQM.getUserTypes();
        Vector parameters = new Vector();
                
        //if(isClass)
        //{// if isClass
        while(userTypesIter.hasNext() )
        {

            String userType = userTypesIter.next();
            logger.debug(userType);

            String inter = ""; 
            String rep = ""; 

            if( type == 1)
            {
                inter = inter + UMQM.getClassInterest(objectURI, subjectURI, userType);
                rep = rep +  UMQM.getClassRepetitions(objectURI, subjectURI, userType);
            }
            else
            {
                inter = inter + UMQM.getDefaultClassInterest(objectURI, userType);
                rep = rep + + UMQM.getDefaultClassRepetitions(objectURI, userType);              
            }

            Vector row = new Vector();
            
            row.add(userType);
            row.add(inter);
            row.add(rep);

            parameters.add(row);
        }
        //}// if isClass
        
        return parameters;
    }  
        
    public Vector getUMParametersForProperty(String PropertyURI, String ResourceURI, boolean isClass, int type)
    {
        /**** this only for testing ***/
        if(UMQM == null)
        {
            Vector v = new Vector();
            
            Vector row = new Vector();
            row.add("1");
            row.add("2");
            row.add("3");
            
            v.add(row);
            return v; 
        }
        
        /**** this only for testing ***/
        
        logger.debug("getUMParametersForProperty " + PropertyURI + " " + ResourceURI);
        Iterator<String> userTypesIter = UMQM.getUserTypes();
        Vector parameters = new Vector();
                
        if(isClass)
        {// if isClass
            while(userTypesIter.hasNext() )
            {
                                
                String userType = userTypesIter.next();
                logger.debug(userType);
                
                String inter = ""; 
                String rep = ""; 
                        
                if( type == 1)
                {
                    inter = inter + UMQM.getCDPInterest(PropertyURI, ResourceURI, userType);                
                    rep = rep + + UMQM.getCDPRepetitions(PropertyURI, ResourceURI, userType);
                }
                else
                {
                    inter = inter + UMQM.getDInterest(PropertyURI, userType);
                    rep = rep + + UMQM.getDRepetitions(PropertyURI, userType);                  
                }
                
                Vector row = new Vector();
                row.add(userType);
                row.add(inter);
                row.add(rep);
                
                parameters.add(row);
            }
        }// if isClass
        else
        {// if !isClass 
            while(userTypesIter.hasNext())
            {
                                
                String userType = userTypesIter.next().toString();
                logger.debug(userType);

                String inter = ""; 
                String rep = ""; 
                
                if( type == 1)
                {                
                     inter = "" + UMQM.getIPInterest(PropertyURI, ResourceURI, userType);
                     rep = "" + UMQM.getIPRepetitions(PropertyURI, ResourceURI, userType);
                }
                else
                {
                    inter = inter + UMQM.getDInterest(PropertyURI, userType);
                    rep = rep + + UMQM.getDRepetitions(PropertyURI, userType);                    
                }                
                
                
                Vector row = new Vector();
                row.add(userType);
                row.add(inter);
                row.add(rep);
                
                parameters.add(row);
            }            
        }// if !isClass 
        
        return parameters;
    }   
        
    public void showParametersActionPerformed(java.awt.event.ActionEvent evt, int t)
    {        
        int row = propertiesTable.getSelectedRow();

        if(row !=-1)
        {
            String property = dataModel.getValueAt(row, 0).toString();
            String objectURI = dataModel.getValueAt(row, 1).toString();
            
            Vector<String> colNames = new Vector<String>();
            colNames.add("Interest");
            colNames.add("Repetitions");
            
            EditUMParametersDialog editDialog = new EditUMParametersDialog(colNames);                                   

            Vector testData = null;
                    
            if(property.equals(RDF.type.getURI()) || property.equals(RDFS.subClassOf.getURI())) 
            {
                testData = getUMParametersForIsA(ResourceURI, objectURI, t);
            }
            else
            {
                testData = getUMParametersForProperty(property, ResourceURI, isClass, t);
            }
                        
            
            editDialog.setData(testData);
            
            boolean ok = false;
            
            //if(t == 1)
            //{ 
            
                String sub = "";
                String pred = "";
                String obj = "";
                                
                if(property.equals(RDF.type.getURI())) 
                {
                    if(t == 1)
                    {
                        sub = this.ResourceURI;
                        pred = property;
                        obj = objectURI;
                    }
                    else if(t == 2)
                    {
                        sub = "*";
                        pred = property;
                        obj = objectURI;                        
                    }
                }
                else if(property.equals(RDFS.subClassOf.getURI()))
                {                  
                    if(t == 1)
                    {
                        sub = this.ResourceURI;
                        pred = property;
                        obj = objectURI;
                    }
                    else if(t == 2)
                    {
                        sub = "*";
                        pred = property;
                        obj = objectURI;                        
                    }                    
                }
                else
                {
                    if(isClass)
                    {
                        if(t == 1)
                        {
                            sub = this.ResourceURI;
                            pred = property;
                            obj = "*";
                        }
                        else if(t == 2)
                        {
                            sub = "*";
                            pred = property;
                            obj = "*";                        
                        }  
                    }
                    else
                    {
                         
                        if(t == 1)
                        {
                            sub = this.ResourceURI;
                            pred = property;
                            obj = objectURI;
                        }
                        else if(t == 2)
                        {
                            sub = "*";
                            pred = property;
                            obj = "*";                        
                        }                          
                    }
                }                
                

                
                ok = editDialog.showDlg(property , "Interest-Repetitions for " + "&lt " + sub + ", " + pred + ", " + obj + " &gt");
                
            //}
            //else
            //{
            //    String sub = "";
            //    String pred = "";
            //    String obj = "";
            //    
            //    ok = editDialog.showDlg(property , "Interest-Repetitions for " + "[" + ClassURI + ", " + property + ", "  + "Filler ]");
            //}
            
            if(ok)// interest - repetitions changed
            {
                Vector data = editDialog.getData();
                
                //
                for(int i = 0; i < data.size(); i++)
                {
                    Vector rowData = (Vector)data.get(i);  
                    
                    for(int j = 0; j < rowData.size(); j++)
                    {
                       // System.err.print("---" + rowData.get(j));
                    }
                    
                    rowData.setElementAt(NLPlugin.getUserModellingQueryManager().NLGUserModellingNS  + rowData.get(0).toString(), 0);
                    logger.debug("\n");
                }
                
                if(property.equals(RDF.type.getURI()) || property.equals(RDFS.subClassOf.getURI())) 
                {
                    saveClassInterestRepetitions(ResourceURI, objectURI, data, t);
                }
                else
                {
                    saveInterestRepetitions(property, ResourceURI, data, t);
                }
                
                
            }
                    
            logger.debug(property);        
        }
    }
    
    public void saveClassInterestRepetitions(String subjectURI, String objectURI, Vector data, int t)
    {
        logger.debug("subjectURI" + " is-a " + objectURI);
        
        for(int i = 0; i < data.size(); i++)
        {
            Vector rowData = (Vector)data.get(i);  

            for(int j = 0; j < rowData.size(); j++)
            {
                //System.err.print(" @@@@@ " + rowData.get(j));
            }

            String ut = rowData.get(0).toString();

            String interest = rowData.get(1).toString();
            String repetitions = rowData.get(2).toString();

            if(t == 1) 
            {
                UMQM.setClassInterest(objectURI, subjectURI, ut, interest);
                UMQM.setClassRepetitions(objectURI, subjectURI, ut, repetitions);
            }
            else
            {
                UMQM.setDefaultClassInterest(objectURI, ut, interest);
                UMQM.setDefaultClassRepetitions(objectURI, ut, repetitions);
            }

            logger.debug("\n");
        }           
    } 
    
    public void saveInterestRepetitions(String property,String resource,Vector data, int t)
    {
        if(isClass)
        {
            for(int i = 0; i < data.size(); i++)
            {
                Vector rowData = (Vector)data.get(i);  

                for(int j = 0; j < rowData.size(); j++)
                {
                    //System.err.print("---" + rowData.get(j));
                }

                String ut = rowData.get(0).toString();
                
                String interest = rowData.get(1).toString();
                String repetitions = rowData.get(2).toString();
                
                if(t == 1)
                {
                    UMQM.setClassInterestForProperty(property, resource, ut, interest);
                    UMQM.setClassRepetitionsForProperty(property, resource, ut, repetitions);
                }
                else
                {
                    UMQM.setDInterestForProperty(property, ut, interest);
                    UMQM.setDRepetitionsForProperty(property, ut, repetitions);
                }
                
                logger.debug("\n");
            }            
        }
        else
        {
            for(int i = 0; i < data.size(); i++)
            {
                Vector rowData = (Vector)data.get(i);  

                for(int j = 0; j < rowData.size(); j++)
                {
                    //System.err.print("---" + rowData.get(j));
                }

                String ut = rowData.get(0).toString();
                
                String interest = rowData.get(1).toString();
                String repetitions = rowData.get(2).toString();

                if(t == 1)
                {
                    UMQM.setInstanceInterestForProperty(property, resource, ut, interest);
                    UMQM.setInstanceRepetitionsForProperty(property, resource, ut, repetitions);
                }
                else
                {
                    UMQM.setDInterestForProperty(property, ut, interest);
                    UMQM.setDRepetitionsForProperty(property, ut, repetitions);
                }
                
                logger.debug("\n");
            }
        }
    }
    
    public void setResourceName(String resource)
    {
        Resourcelabel.setText(resource);
    }
    
    protected ComboHeaderComponent createUserModellingBrowserHeader() 
    {
        Resourcelabel = ComponentFactory.createLabel("", Icons.getProjectIcon(), SwingConstants.LEFT);
        String forResource = "Resource";
        String UserModellingBrowser = "Interest and Repetitions of Properties (Right-click on a property to change its interest or repetitions)";
        
        
        // create header component
        header = new ComboHeaderComponent(UserModellingBrowser, forResource, Resourcelabel);
        return header;
    }
        
            
    public void refresh(Vector propertiesFillerVector, String ResourceURI, boolean isClass)
    {
        this.ResourceURI = ResourceURI;
        
        if(isClass)
        {
            ClassURI = ResourceURI;
        }
        
        this.isClass = isClass;
                
        dataModel.setDataVector(propertiesFillerVector, names);
    }    
    
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    
    /*A main mathod for testing */
    
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
        
        UserModellingPanel UMP = new UserModellingPanel(null);
        testFrame.setLayout(new java.awt.BorderLayout());
        testFrame.add(UMP);
        testFrame.show();        
    }               
}
