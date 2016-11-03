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

import javax.swing.event.TableModelListener;
import javax.swing.event.TableModelEvent;
import javax.swing.*;
import javax.swing.table.*;

import edu.stanford.smi.protege.ui.*;
import edu.stanford.smi.protege.util.*;
import edu.stanford.smi.protege.resource.*;

import gr.aueb.cs.nlg.NLFiles.*;
import java.util.*;

import org.apache.log4j.Logger;

public class OrderingPanel extends JPanel 
{    
    static Logger logger = Logger.getLogger(OrderingPanel.class.getName());
    //Object [][] myDATA2  = { {"A" , "1"}, {"B" , "2"}}; 
    Vector myDATA  = new Vector();     
    Vector names = new Vector(); 
            
    JTable PropertiesOrderTable;
    JScrollPane scrollpane;
    DefaultTableModel dataModel;
    MicroplansAndOrderingTab TAOT;
                 
    String projectName;
    
    MicroplansAndOrderingQueryManager MAOQM;
            
    protected JComponent createHeaderComponent() 
    {
        JLabel label = ComponentFactory.createLabel(Icons.getSlotIcon());
        label.setText(this.projectName);        
                
        String OrderingTable = "ORDERING OF PROPERTIES";
        String forProjectLabel = "For Project";
        HeaderComponent header = new HeaderComponent(OrderingTable, forProjectLabel, label);
        header.setColor(Colors.getSlotColor());
        return header;
    }
          
    public OrderingPanel(MicroplansAndOrderingTab TAOT, MicroplansAndOrderingQueryManager MAOQM) 
    {
        
        this.TAOT = TAOT;
        this.MAOQM = MAOQM;
                
            
        names.add("Property");
        names.add("Order");
    
        if(TAOT != null)
        {
            this.projectName = TAOT.getProj().getName();
        }
        else
        {// this is used only for testing
            this.projectName = "Test Project";
            Vector row = new Vector();
            row.add("A");
            row.add("1");
            
            myDATA.add(row);
            
            row = new Vector();
            row.add("B");
            row.add("2");
            
            myDATA.add(row);
        }
        
        initComponents();
        HeaderPanel.add(createHeaderComponent());
                          
        Load();
        
        dataModel = new DefaultTableModel(myDATA,names)  
        { 
             public boolean isCellEditable(int row, int col) 
             {
                 logger.debug("isCellEditable");
                 if(col!=0)
                 return true;
                 else
                 return false;
             } 

       };
          
      dataModel.addTableModelListener(new TableModelListener()
      {
             public void tableChanged(TableModelEvent e)
             {
                 
                 logger.debug("table changed");
                 logger.debug("@" + e.getFirstRow() + e.getLastRow() + e.getColumn());
                 
                 if(e.getFirstRow()  != -1 && e.getLastRow() !=-1 && e.getColumn() !=-1)
                 {
                    String prpURI = dataModel.getValueAt(e.getFirstRow() ,0).toString();
                    String order =  dataModel.getValueAt(e.getFirstRow() ,1).toString();       
                 
                    logger.debug("@" + prpURI + " "  + order);
                    NLPlugin.getMicroplansAndOrderingQM().setOrder(prpURI, Integer.parseInt(order));
                    //sortTableByOrder();               
                 }
             }
      });
          
      PropertiesOrderTable = new JTable(dataModel);         

      TableColumn OrderColumn = PropertiesOrderTable.getColumn("Order");                                      

      //OrderColumn.setCellEditor(new DefaultCellEditor(comboBox));
      //OrderColumn.setCellRenderer(NumberRenderer);
      OrderColumn.setCellEditor(new SpinnerEditor());
      scrollpane = new JScrollPane(PropertiesOrderTable);
      TablePanel.add(scrollpane/*, java.awt.BorderLayout.CENTER*/);          
    }
         
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        TablePanel = new javax.swing.JPanel();
        ButtonPanel = new javax.swing.JPanel();
        sort = new javax.swing.JButton();
        HeaderPanel = new javax.swing.JPanel();

        setLayout(new java.awt.BorderLayout());

        TablePanel.setLayout(new javax.swing.BoxLayout(TablePanel, javax.swing.BoxLayout.X_AXIS));

        add(TablePanel, java.awt.BorderLayout.CENTER);

        sort.setText("sort");
        sort.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sortActionPerformed(evt);
            }
        });

        ButtonPanel.add(sort);

        add(ButtonPanel, java.awt.BorderLayout.SOUTH);

        HeaderPanel.setLayout(new java.awt.BorderLayout());

        add(HeaderPanel, java.awt.BorderLayout.NORTH);

    }// </editor-fold>//GEN-END:initComponents

    
    /*
        Vector localdata = dataModel.getDataVector();
                
        for(int i = 0; i < localdata.size(); i ++) {
            Vector row = (Vector)localdata.get(i);
            String prpURI = row.get(0).toString();
            int order = Integer.parseInt(row.get(1).toString());
            
            NLPlugin.getMicroplansAndOrderingQM().setOrder(prpURI, order);
        }  
     */
     
    int c = 0;
    private void sortActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sortActionPerformed
       sortTableByOrder();
       //dataModel.setDataVector(myDATA2,names);
    }//GEN-LAST:event_sortActionPerformed
   
    public void Load()
    {
        if(TAOT != null && NLPlugin.getMicroplansAndOrderingQM()!=null)
        {
            Vector prps = TAOT.getProperties();

            for(int i = 0; i < prps.size(); i ++)
            {
                String prp = prps.get(i).toString();
                int ord = NLPlugin.getMicroplansAndOrderingQM().getOrder(prp);
                Vector row = new Vector();

                row.add(prp);
                row.add(ord);

                myDATA.add(row);
            }
        }   
    }
            
    public void sortColumn(DefaultTableModel model, int colIndex, boolean ascending) 
    {        
        Vector localdata = model.getDataVector();
        
        for(int i = 0; i < localdata.size(); i ++){
            logger.debug(localdata.get(i).toString());
        }
        
        Collections.sort(localdata, new ColumnSorter(ascending));
        model.fireTableStructureChanged();        
    }      
    //-------------------------------------------------
    public void addProperty(String prp)
    {
        Object temp[] = {prp , "1"}; 
        dataModel.addRow(temp);                      
    }
    //-------------------------------------------------    
    public void removeProperty(String prp)
    {                           
        Vector localdata = dataModel.getDataVector();
        
        Vector v = TAOT.getProperties();
        
        for(int i = 0; i < localdata.size(); i ++)
        {
            Vector  rowColVec = (Vector)localdata.get(i);            
            logger.debug("--- " + rowColVec.get(0).toString());
            
            if(!v.contains(rowColVec.get(0)))
            {
                dataModel.removeRow(i);
            }
        }  
        
                                    
    }    
    //-------------------------------------------------    
     public void renameProperty(String oldName, String newName)
     {
        Vector localdata = dataModel.getDataVector();
        
        logger.debug("--- looking for .... " + oldName);  
        
        for(int i = 0; i < localdata.size(); i ++)
        {
            Vector  rowColVec = (Vector)localdata.get(i);
            
            logger.debug("--- " +rowColVec.get(0).toString());
            
            if(rowColVec.get(0).toString().compareTo(oldName) == 0)
            {
               logger.debug("--- renaming .... " + oldName);
               dataModel.setValueAt(newName, i , 0);
            }
        }  
     }    
    //-------------------------------------------------    
    public void sortTableByOrder()
    {
        int mColIndex = 1;
        boolean ascending = false;
        PropertiesOrderTable.setAutoCreateColumnsFromModel(false);
        TableColumn TC = PropertiesOrderTable.getColumn("Order");
        
        sortColumn(dataModel, mColIndex, ascending);
        
        //PropertiesOrderTable.setEditor(new SpinnerEditor());     
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel ButtonPanel;
    private javax.swing.JPanel HeaderPanel;
    private javax.swing.JPanel TablePanel;
    private javax.swing.JButton sort;
    // End of variables declaration//GEN-END:variables
    public static void main(String args[])
    {        
        JFrame testFrame = new JFrame();
        
        testFrame.addWindowListener(new java.awt.event.WindowAdapter() 
        {
            public void windowClosing(java.awt.event.WindowEvent evt) 
            {                
                    System.exit(0);                
            }
        });
        
        OrderingPanel OP = new OrderingPanel(null, null);
        testFrame.setLayout(new java.awt.BorderLayout());
        testFrame.add(OP);
        testFrame.show();
        
    }
}


