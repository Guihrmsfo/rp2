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
import java.awt.*; 
import javax.swing.JTable;
import javax.swing.table.*;

import java.util.*;

import gr.aueb.cs.nlg.NLFiles.*;
import gr.aueb.cs.nlg.Languages.*;
        
import org.apache.log4j.Logger;


public class CannedTextsPanel extends javax.swing.JPanel 
{
    
    static Logger logger = Logger.getLogger(CannedTextsPanel.class.getName());
    //Object [][] myDATA2  = { {"A" , "1"}, {"B" , "2"}}; 
     
    Vector myDATA  = new Vector();     
    Vector names = new Vector(); 
            
    JTable CannedTextsTable;
    JScrollPane scrollpane;
    DefaultTableModel dataModel;
    
    String language = "";
    String owlResourceNamespase = "";
    String owlResourceLocalName = "";
    
    
    Vector English_data = new Vector();
    Vector Greek_data = new Vector();
    
    public CannedTextsPanel(LexiconTab LBT) 
    {
        initComponents();
        
        // column names
        names.add("User Type");
        names.add("Canned Text");
        names.add("Focus Lost");
        names.add("Aggregation Allowed");
        
        // add test data
        Vector row1 = new Vector();
        row1.add("UT1");
        row1.add("Cannet Text1");
        row1.add(true);
        row1.add(true);
        
        Vector row2 = new Vector();
        row2.add("UT2");
        row2.add("Cannet Text2");
        row2.add(true);
        row2.add(true);
        
        myDATA.add(row1);
        myDATA.add(row2);
        
        // create the data model
        dataModel = new DefaultTableModel(myDATA,names)  
        { 
             public boolean isCellEditable(int row, int col) 
             {                 
                 if(col == 0)
                 return false;
                 else
                 return true;
             } 

       };
          
      dataModel.addTableModelListener(new TableModelListener()
      {
             public void tableChanged(TableModelEvent e)
             {   
                 
                 //logger.debug("table changed");
                 //logger.debug("@" + e.getFirstRow() + e.getLastRow() + e.getColumn());
                 
                 if(e.getFirstRow()  != -1 && e.getLastRow() !=-1 && e.getColumn() !=-1)
                 {  
                    //logger.debug("saving table"); 
                    saveToLexicon();
                 }
             }
      });
          
      
      CannedTextsTable = new JTable(dataModel);    
      
      TableColumn FocusColumn = CannedTextsTable.getColumnModel().getColumn(2);
      FocusColumn.setCellEditor(new DefaultCellEditor(new JCheckBox()));
      FocusColumn.setCellRenderer(new MyTableCellRenderer());
      
      TableColumn AggreagationColumn = CannedTextsTable.getColumnModel().getColumn(3);
      AggreagationColumn.setCellEditor(new DefaultCellEditor(new JCheckBox()));
      AggreagationColumn.setCellRenderer(new MyTableCellRenderer());
              
      scrollpane = new JScrollPane(CannedTextsTable);
      this.add(scrollpane);
    }
    
   public void setLanguage(String lang)
   {
        this.language = lang;
   }

   public void setResource(String ns, String local)
   {
        owlResourceNamespase = ns;
        owlResourceLocalName = local;
   }
   
   //  a new table renderer ...
   static class MyTableCellRenderer extends DefaultTableCellRenderer 
   {
      JCheckBox jcb;
      Color selectionBackground = (Color) UIManager.get("Table.selectionBackground");
      Color normalBackground = (Color) UIManager.get("Table.background");
      
      public MyTableCellRenderer()
      {
          super();
          jcb = new JCheckBox();
          //jcb.setVisible(false);
          jcb.setSelected(false);
      }
      
      
      public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) 
      {
        //logger.debug(value + "" + isSelected);
        Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if (column == 2 || column ==3)
        {
            //jcb = new JCheckBox();
            jcb.setHorizontalAlignment(SwingConstants.LEFT);
            jcb.setBackground( Color.white);
            jcb.setSelected((Boolean)value);
            return jcb;
        }
         
        return cell;
      }
      
   }
   
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
        
        //NLGLexiconQueryManager LQM = new NLGLexiconQueryManager();
        //NLPlugin.getLexicon().LoadLexicon("C:\\Documents and Settings\\galanisd\\Επιφάνεια εργασίας\\NLFiles-MPIRO\\" , "Lexicon.rdf");
        
        //UserModellingQueryManager UMQM = new UserModellingQueryManager(null);
        //UMQM.LoadUserModellingInfo("C:\\Documents and Settings\\galanisd\\Επιφάνεια εργασίας\\NLFiles-MPIRO\\" , "UserModelling.rdf");
        
        CannedTextsPanel CTP = new CannedTextsPanel(null);
        testFrame.setLayout(new java.awt.BorderLayout());
        testFrame.add(CTP);
        testFrame.show();
        
            //CTP.clear();
            //CTP.setLanguage(Languages.ENGLISH);
            //CTP.setCannedTextsIDs();
            //CTP.selectFirstCannedTextId();  
            
    }
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.X_AXIS));

    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    
    // this method clears the table
    // that the canned texts are listed
    public void clear()
    {
        Vector data = new Vector();
                       
        Iterator<String> uts = NLPlugin.getUserModellingQueryManager().getUserTypes();

        //for each user type
        while(uts!=null && uts.hasNext())
        {
            
            String ut = uts.next();
            
            if(Utils.isLangSuitable(NLPlugin.getUserModellingQueryManager(),ut, this.language))
            {
                Vector row = new Vector();
            
                row.add(ut);
                row.add("");
                row.add(false);
                row.add(false);
            
                data.add(row);
            }
        }
        
        dataModel.setDataVector(data, names);
         
        CannedTextsTable.revalidate();
        CannedTextsTable.repaint();
    }
    
    // set the fields of the selected canned text
    public void setCannedTextsInfo(String OWLResourceURI, String lang)
    {        
        clear();
        
        //logger.debug("after clear");
        //logger.debug("OWLResourceURI" + OWLResourceURI);

        English_data = new Vector();
        Greek_data = new Vector();
        
        print();
        
        if(NLPlugin.getLexicon() == null)
        {
            // if the lexicon is null
            // don't do anything
            logger.debug("lexicon is null" );
        }
        else
        {
            
            // one hash map for each lang
            // the hash maps hold the canned texts
            Hashtable<String, Vector> map_GR = new Hashtable<String, Vector>();
            Hashtable<String, Vector> map_EN = new Hashtable<String, Vector>();
    
            Iterator<String> k = NLPlugin.getUserModellingQueryManager().getUserTypes();
            
            // initialize the maps
            // the default entry
            // for each user type is 
            // UT , "" , false, false
            while(k!=null && k.hasNext())
            {                               
                String current_ut = k.next();
                
                if( Utils.isLangSuitable(NLPlugin.getUserModellingQueryManager(), current_ut, Languages.ENGLISH))
                {
                    Vector row = new Vector();
                    row.add("");
                    row.add(false);
                    row.add(false);                                          
                    map_EN.put(current_ut, row);
                }
                
                if( Utils.isLangSuitable(NLPlugin.getUserModellingQueryManager(), current_ut, Languages.GREEK))
                {
                    Vector row = new Vector();
                    row.add("");
                    row.add(false);
                    row.add(false);                                          
                    map_GR.put(current_ut, row);
                    
                }
                
            }
            
            // find all nl resources ids
            Iterator<String> iter = NLPlugin.getLexicon().getMappings(OWLResourceURI);

            //for each nl resource which is a canned text
            while(iter!=null && iter.hasNext())
            {
                String cannedtextID = iter.next();

                //logger.debug("cannedtextID " + cannedtextID);

                Object obj = NLPlugin.getLexicon().getNLResByURI(cannedtextID, 2);

                if(obj instanceof CannedList)
                {
                    CannedList CL = (CannedList)obj;                                            

                    Iterator<String> uts = NLPlugin.getUserModellingQueryManager().getUserTypes();
                    
                    while(uts!=null && uts.hasNext())
                    {
                        String ut = uts.next();

                        if(Utils.isLangSuitable(NLPlugin.getUserModellingQueryManager(),ut, lang))
                        {
                            if(CL.isSuitablefor(ut))
                            {

                                if(map_GR.containsKey(ut))
                                {
                                    Vector row_GR = map_GR.get(ut);

                                    String CT = (String)row_GR.get(0);
                                    if(CT.compareTo("") ==0)
                                    {
                                        row_GR = new Vector();

                                        row_GR.add(CL.getCannedText(Languages.GREEK));
                                        row_GR.add(CL.getFOCUS_LOST());
                                        row_GR.add(CL.getFillerAggregationAllowed());       

                                        map_GR.put(ut, row_GR);                                
                                    }
                                }
                                else
                                {        
                                    Vector row_GR = new Vector();

                                    row_GR.add(CL.getCannedText(Languages.GREEK));
                                    row_GR.add(CL.getFOCUS_LOST());
                                    row_GR.add(CL.getFillerAggregationAllowed());       

                                    map_GR.put(ut, row_GR);
                                }

                                if(map_EN.containsKey(ut))
                                {

                                    Vector row_EN = map_EN.get(ut);

                                    String CT = (String)row_EN.get(0);

                                    if(CT.compareTo("") ==0)
                                    {
                                        row_EN = new Vector();

                                        row_EN.add(CL.getCannedText(Languages.ENGLISH));
                                        row_EN.add(CL.getFOCUS_LOST());
                                        row_EN.add(CL.getFillerAggregationAllowed());       

                                        map_EN.put(ut, row_EN);                                
                                    }                                
                                }
                                else
                                {
                                    Vector row_EN = new Vector();
                                    row_EN.add(CL.getCannedText(Languages.ENGLISH));
                                    row_EN.add(CL.getFOCUS_LOST());
                                    row_EN.add(CL.getFillerAggregationAllowed());                                  

                                    map_EN.put(ut, row_EN);
                                }
                            }// if is suitablr
                        }// if it is the correct lang
                    }
                }
            }//for each nl resource which is a canned text
            
            Iterator<String> map_EN_it = map_EN.keySet().iterator();
            Iterator<String> map_GR_it = map_GR.keySet().iterator();
            
            while(map_EN_it != null && map_EN_it.hasNext())
            {
                String ut = map_EN_it.next();
                Vector v = map_EN.get(ut);
                
                for(int i = 0; i < v.size(); i++)
                {
                    logger.debug("v[" + i +"]" + v.get(i));
                }
                                
                Vector vv = new Vector();
                
                vv.add(0, ut.substring(ut.indexOf("#")+1));
                
                for(int i = 0; i < v.size(); i++)
                vv.add(v.get(i));
                
                for(int i = 0; i < v.size(); i++)
                {
                    logger.debug("vv[" + i +"]" + v.get(i));
                }
                
                this.English_data.add(vv);
            }
            
            while(map_GR_it != null && map_GR_it.hasNext())
            {
                String ut = map_GR_it.next();
                
                Vector v = map_GR.get(ut);
                Vector vv = new Vector();
                
                vv.add(0, ut.substring(ut.indexOf("#")+1));
                
                for(int i = 0; i < v.size(); i++)
                vv.add(v.get(i));
                
                this.Greek_data.add(vv);                
            }
           
        } 
       
        
            
        logger.debug(" before last print");
        print();
        
        if(Languages.isEnglish(language))
        {          
            Collections.sort(English_data, new UTColumnSorter(true));
            dataModel.fireTableStructureChanged(); 
                  
            dataModel.setDataVector(English_data, names);
        }
        else
        {
            
            Collections.sort(Greek_data, new UTColumnSorter(true));
            dataModel.fireTableStructureChanged();             
            
            dataModel.setDataVector(Greek_data, names);
        }
        
        TableColumn FocusColumn = CannedTextsTable.getColumnModel().getColumn(2);
        FocusColumn.setCellEditor(new DefaultCellEditor(new JCheckBox()));
        FocusColumn.setCellRenderer(new MyTableCellRenderer());
      
        TableColumn AggreagationColumn = CannedTextsTable.getColumnModel().getColumn(3);
        AggreagationColumn.setCellEditor(new DefaultCellEditor(new JCheckBox()));
        AggreagationColumn.setCellRenderer(new MyTableCellRenderer());
      
        CannedTextsTable.revalidate();
        //CannedTextsTable.repaint();
        
         logger.debug("last print");
        print();
        logger.debug("end of set canned texts info");
    }
    
    
    public void saveToLexicon()
    {
        logger.debug("saving canned to lexicon" + this.owlResourceNamespase + this.owlResourceLocalName);
        
        // 
        //
        //
        
        if(Languages.isEnglish(language))
        {
            this.English_data = this.dataModel.getDataVector();            
        }
        else
        {
            this.Greek_data  = this.dataModel.getDataVector();            
        }
        
        print();
        
        // a vector containing canned texts
        Vector<CannedList> cannedTexts = new Vector<CannedList>();
        Vector<String> ids = new Vector<String>();

        for(int i = 0; i < English_data.size(); i++)
        {
            Vector row = (Vector)English_data.get(i);
            
            String userType = row.get(0).toString();
            
            String cannedText = "";
            
            if (row.get(1) == null)
            {
                cannedText = "";
            }
            else
            {
                cannedText = row.get(1).toString();
            }
                
            boolean FL = (Boolean)row.get(2);
            boolean AA = (Boolean)row.get(3);
                 
            // create a new canned text
            CannedList CL = new CannedList();
            
            // this canned text is suitable for the user type
            CL.addUserType(NLPlugin.getUserModellingQueryManager().NLGUserModellingNS + userType);
            //set focus
            CL.setFOCUS_LOST(FL);
            //set aggregation allowed
            CL.setFillerAggregationAllowed(AA);
            
            // set the english canned text
            CL.setCannedText(cannedText, Languages.ENGLISH);
            
            // set "" as the greek canned text
            CL.setCannedText("", Languages.GREEK);
            cannedTexts.add(CL);       
            ids.add(ids.size() + "");
            
        }
           
        // the code below is for debugging
        /* 
        for(int i = 0; i < cannedTexts.size(); i++)
        {
            logger.debug("------------------------------------");
            logger.debug(i + "");
            logger.debug("EN" + cannedTexts.get(i).getCannedText(Languages.ENGLISH));
            logger.debug("GR" + cannedTexts.get(i).getCannedText(Languages.GREEK));
            
            Vector<String> v = cannedTexts.get(i).getUserTypes();
            
            for(int j = 0; j < v.size(); j++)
            {
                logger.debug(v.get(j));
            }
                        
            logger.debug("------------------------------------");
        }
         */
        
        print();
         
        for(int i = 0; i < Greek_data.size(); i++)
        {
            Vector row = (Vector)Greek_data.get(i);
            
            String userType = row.get(0).toString();
            
            String cannedText = "";
            
            if (row.get(1) == null)
            {
                cannedText = "";
            }
            else
            {
                cannedText = row.get(1).toString();
            }
                
            boolean FL = (Boolean)row.get(2);
            boolean AA = (Boolean)row.get(3);
                        
            boolean found = false;
            
            // search the already existing canned text
            // and the greek canned if is the parameters are the same 
            for(int j = 0; j < cannedTexts.size(); j++)
            {
                CannedList CL = cannedTexts.get(j);
                
                //logger.debug(CL.getFOCUS_LOST());
                //logger.debug(CL.getFillerAggregationAllowed());
                //logger.debug();
                
                if( 
                    CL.getFOCUS_LOST() == FL 
                    && CL.getFillerAggregationAllowed() == AA
                    && CL.getCannedText(Languages.GREEK).compareTo("")== 0
                    && CL.getUserTypes().contains(NLPlugin.getUserModellingQueryManager().NLGUserModellingNS + userType)
                  )
                {
                    CL.setCannedText(cannedText, Languages.GREEK);
                    //CL.addUserType(userType);
                    found = true;
                    //j = cannedTexts.size() + 1;
                }
            }
            
            // if not a suitable canned is found then create a new one
            if(!found)
            {
                CannedList CL = new CannedList();
                CL.addUserType(NLPlugin.getUserModellingQueryManager().NLGUserModellingNS + userType);
                
                CL.setFOCUS_LOST(FL);
                CL.setFillerAggregationAllowed(AA);
                
                CL.setCannedText("", Languages.ENGLISH);
                CL.setCannedText(cannedText, Languages.GREEK);
                
                cannedTexts.add(CL);       
                ids.add(ids.size() + "");
            }
        }
        
        // the code  below is used for debugging
        /*
        for(int i = 0; i < cannedTexts.size(); i++)
        {
            logger.debug("------------------------------------");
            logger.debug(i + "");
            logger.debug("EN" + cannedTexts.get(i).getCannedText(Languages.ENGLISH));
            logger.debug("GR" + cannedTexts.get(i).getCannedText(Languages.GREEK));
            logger.debug("FL" + cannedTexts.get(i).getFOCUS_LOST());
            logger.debug("AA" + cannedTexts.get(i).getFillerAggregationAllowed());
            
            Vector<String> v = cannedTexts.get(i).getUserTypes();
            
            for(int j = 0; j < v.size(); j++)
            {
                logger.debug("suitable" + v.get(j));
            }
            
            logger.debug("------------------------------------");
        }
        */
        print();
         
        // save it
        NLPlugin.getLexicon().saveCTToLexicon(this.owlResourceNamespase, this.owlResourceLocalName, cannedTexts, ids);            
    }
    
    // it prints the english and then the greek canned texts
    public void print()
    {
        logger.debug("=========ENGLISH=========");
        
        Vector rows = this.English_data;
                
        for(int i = 0; i < rows.size(); i++)
        {
            Vector row = (Vector)rows.get(i);
            
            for(int j = 0; j < row.size(); j++)
            {
                System.err.print(row.get(j) + " ");
            }
            
            logger.debug("\n");
        }
        
        logger.debug("=======GREEK===========");
        rows = this.Greek_data;
        
        for(int i = 0; i < rows.size(); i++)
        {
            Vector row = (Vector)rows.get(i);
            
            for(int j = 0; j < row.size(); j++)
            {
                System.err.print(row.get(j) + " ");
            }
            
            logger.debug("\n");
        }        
    }
    
    //

}
