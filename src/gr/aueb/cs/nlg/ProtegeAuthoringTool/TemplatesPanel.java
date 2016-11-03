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
import java.awt.*;
import java.util.*;
import java.awt.event.*;
import java.text.*;
import javax.swing.border.*;

import edu.stanford.smi.protege.event.*;
import edu.stanford.smi.protege.model.*;
import edu.stanford.smi.protege.resource.*;
import edu.stanford.smi.protege.util.*;
import edu.stanford.smi.protege.widget.*;
import edu.stanford.smi.protege.ui.*;
 

import gr.aueb.cs.nlg.NLFiles.*;
import gr.aueb.cs.nlg.Languages.Languages;

import org.apache.log4j.Logger;


public class TemplatesPanel extends JPanel 
{    
    static Logger logger = Logger.getLogger(TemplatesPanel.class.getName());
    
    private JPanel SlotsPanel = new javax.swing.JPanel();
    private Vector slots_VEC;
    private ComboHeaderComponent header;
    
    public static int SLOT_PANEL_WIDTH = 600;
    public static int SLOT_PANEL_HEIGHT = 150;
    

    
    private String LanguagesArr [] = {"English","Greek"};
    private String Templs [] = {"templ1","templ2", "templ3", "templ4", "templ5"};
    
    //public TemplatesAndOrderingTab TAOT;
    
    private String lang;        
        
    //private MicroplansAndOrderingQueryManager MAOQM;
    //private UserModellingQueryManager UMQM;
    
    private String namespace;            
    private String curr_property;
    
    private JComboBox LangCombo;
    private JComboBox TemplatesCombo;
    private JComboBox RestrTypeBox;
    

    private JButton Appropriateness;
    
    private JCheckBox used;
    private JCheckBox AggregationAllowed;
    private JCheckBox UsedForComparisonsCb ;
    
    private boolean templateButtonsListeneresEnabled = false;
            
    public TemplatesPanel() 
    {

        slots_VEC = new Vector();
        initComponents();       
                
        header = (ComboHeaderComponent)createHeaderComponent();
        templateInfo.add(header);
        
        SlotsPanel = new javax.swing.JPanel();
        SlotsPanel.setLayout(null);
        
        addToolbar();
        

        
        Appropriateness.addActionListener(new java.awt.event.ActionListener() 
        {
            public void actionPerformed(java.awt.event.ActionEvent evt) 
            {
                AppropriatenessActionPerformed(evt);
            }
        });
                
        //this.MAOQM = NLPlugin.getMicroplansAndOrderingQM();
        
        SlotsScrollPane = new JScrollPane(SlotsPanel);
        templates.add(SlotsScrollPane);

    }    
        
    public Vector get_Slots_VEC()
    {
        return slots_VEC;
    }
     
    public MicroplansAndOrderingQueryManager getMicroplansAndOrderingQueryManager()
    {
        return NLPlugin.getMicroplansAndOrderingQM();
    }
    //-------------------------------------------------------------------------------
    
    
    
    protected JComponent createHeaderComponent() 
    {
        JLabel label = ComponentFactory.createLabel(Icons.getSlotIcon());
        label.setText("");                
        
        String templatesHeaderLbl = "Micro-plans";
        String forPropertyLabel = "For Property";
        header = new ComboHeaderComponent(templatesHeaderLbl, forPropertyLabel, label);    
        header.setRightLabel("Micro-plans:");
        UsedForComparisonsCb = header.addCheckBoxForSelectedResource("Property is used for comparisons");
        
        
        UsedForComparisonsCb.addActionListener(new java.awt.event.ActionListener() 
        {
            public void actionPerformed(java.awt.event.ActionEvent evt) 
            {
                UsedForComparisonsCbActionPerformed(evt);
            }
        });
        
        //JButton ChooseLang = header.addButton(new ChooseLangAction());            
        
        header.setColor(Colors.getSlotColor());
        return header;
    }
    //-------------------------------------------------------------------------------   
    private void UsedForComparisonsCbActionPerformed(java.awt.event.ActionEvent evt) 
    {
        
        if(NLPlugin.getMicroplansAndOrderingQM() != null)
        {
            NLPlugin.getMicroplansAndOrderingQM().setUsedForComparisons(this.namespace + this.curr_property, this.UsedForComparisonsCb.isSelected());
        }
    }
    
    private void addToolbar()
    {                
        used = header.addCheckBox("Used");
        AggregationAllowed = header.addCheckBox("Aggregation Allowed");
        
        used.addActionListener(new java.awt.event.ActionListener() 
        {
            public void actionPerformed(java.awt.event.ActionEvent evt) 
            {
                usedActionPerformed(evt);
            }
        });
        
        AggregationAllowed.addActionListener(new java.awt.event.ActionListener() 
        {
            public void actionPerformed(java.awt.event.ActionEvent evt) 
            {
                AggregationAllowedActionPerformed(evt);
            }
        });
        
        LangCombo = header.addCombo("Language  ", LanguagesArr); // Language chooser combo
        TemplatesCombo = header.addCombo("Micro-plan  ", Templs);  // Templates chooser combo
                     
        //SaveButton = header.addButton("Save Template  ");
        Appropriateness = header.addButton("Micro-plan Appropriateness");
        
        // lang changed
        LangCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
               LangComboActionPerformed(evt);
            }
        });
        
        // template changed
        TemplatesCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
               TemplatesComboActionPerformed(evt);
            }
        });        
              
    }
    
    
    private void usedActionPerformed(java.awt.event.ActionEvent evt)
    {
        if(templateButtonsListeneresEnabled)
        saveMicroplan();
    }

    private void AggregationAllowedActionPerformed(java.awt.event.ActionEvent evt)
    {
        if(templateButtonsListeneresEnabled)
        saveMicroplan();
    }
    
    //-------------------------------------------------------------------------------
    private void LangComboActionPerformed(java.awt.event.ActionEvent evt) 
    {        
        String sel_item = (String)LangCombo.getSelectedItem();
        logger.debug("lang:" + sel_item);
        showTemplate(get_property(),(String)TemplatesCombo.getSelectedItem(), getLanguage((String)LangCombo.getSelectedItem()));
    }    
    
    //-------------------------------------------------------------------------------
    
    private void TemplatesComboActionPerformed(java.awt.event.ActionEvent evt) 
    {        
        String sel_item = (String)TemplatesCombo.getSelectedItem();
        logger.debug("templ sel:" + sel_item);
        showTemplate(get_property(),(String)TemplatesCombo.getSelectedItem(), getLanguage((String)LangCombo.getSelectedItem()));
    }  
    //-------------------------------------------------------------------------------
    /*
    private void RestrTypeBoxActionPerformed(java.awt.event.ActionEvent evt) {        
        String sel_item = (String)RestrTypeBox.getSelectedItem();
        logger.debug("templ sel:" + sel_item);
        //showTemplate(get_property(),(String)TemplatesCombo.getSelectedItem(), (String)LangCombo.getSelectedItem());
    }      
    */

    //-------------------------------------------------------------------------------
    public void refreshToolBar(String Templs []){   

    }
    //-------------------------------------------------------------------------------
    public void set_property(String ns, String prop)
    {
        JLabel label = (JLabel)header.getComponent();
        label.setText(prop);
        
        used.setSelected(false);
        AggregationAllowed.setSelected(false);
                
        if(NLPlugin.getMicroplansAndOrderingQM() != null)
        {
            UsedForComparisonsCb.setSelected(NLPlugin.getMicroplansAndOrderingQM().getUsedForComparisons(ns + prop));
        }
        else
        {
            UsedForComparisonsCb.setSelected(false);
        }
                
        namespace = ns;
        this.curr_property = prop;
        
        // TO DO remove namespace !!!
        showTemplate(get_property(),(String)TemplatesCombo.getSelectedItem(), getLanguage((String)LangCombo.getSelectedItem()));
    }
    //-------------------------------------------------------------------------------
    public String get_property()
    {        
        JLabel label = (JLabel)header.getComponent();
        return namespace + label.getText();                        
    }
    //-------------------------------------------------------------------------------
    public String get_Lang()
    {
        return getLanguage((String)LangCombo.getSelectedItem());
    }    
    
    //-------------------------------------------------------------------------------
    public String get_Templ_Name()
    {
        return (String)TemplatesCombo.getSelectedItem();
    }        

    //-------------------------------------------------------------------------------
    
    private Vector<NLGSlot> getSlots()
    {        
        Vector<NLGSlot> slots = new Vector<NLGSlot>();
                
        for(int i = 0; i < slots_VEC.size(); i ++)
        {
            SlotPanel SP = (SlotPanel)slots_VEC.get(i);
            
            if(SP.is_OwnerRB_Selected())
            {
               slots.add(new PropertySlot(SP.get_Case(), PropertySlot.OWNER, SP.getREType())); 
            }
            else if(SP.is_fillerRB_Selected())
            {
                slots.add(new PropertySlot(SP.get_Case(), PropertySlot.FILLER, SP.getREType()));
            }
            else if(SP.is_StringRB_Selected())
            {
                slots.add(new StringSlot(SP.get_String()));
            }
            else if(SP.is_preposition_Selected())
            {
                StringSlot SS = new StringSlot(SP.get_String());
                SS.isPreposition = true;
                slots.add(SS);
            }
            else if(SP.is_Adverb_Selected())
            {
                StringSlot SS = new StringSlot(SP.get_String());
                SS.isAdverb = true;
                slots.add(SS);
            }            
            else if(SP.is_verbRB_Selected())
            {
                slots.add(new VerbSlot(SP.get_Verb(), SP.get_Verb_plural(), SP.get_Voice(),SP.get_Tense()));
            }
        }
        
        return slots;
    }
                
    public void showTemplate(String propertyURI, String templ_name, String lang)
    {
        
        SlotsPanel.removeAll();
        SlotPanel SP = null;
        slots_VEC = new Vector();
        
        templateButtonsListeneresEnabled = false; // very important (disables listeners for a while)
        
        used.setSelected(false);
        AggregationAllowed.setSelected(false);
        
        if(NLPlugin.getMicroplansAndOrderingQM() !=null)
        {
            Microplan micropl = NLPlugin.getMicroplansAndOrderingQM().getMicroplan(propertyURI,templ_name,  getLanguage((String)LangCombo.getSelectedItem()));        

            if(micropl!=null)
            {            
                               
                used.setSelected(micropl.getIsUsed());
                AggregationAllowed.setSelected(micropl.getAggAllowed());
                        
                Vector<NLGSlot> NLGSlots = micropl.getSlotsList();
                
                for(int i = 0; i < NLGSlots.size(); i++)// for each slot of the microplan
                {
                    NLGSlot NLGSlot = NLGSlots.get(i);
                    SP = new SlotPanel(this);

                    SP.enableListeners(false);
                    
                    if(NLGSlot instanceof PropertySlot)
                    { // owner or filler
                         PropertySlot PS = (PropertySlot)NLGSlot;

                         if(PS.type ==0)
                         {
                             SP.set_Referring_Expr_To_Owner_RB(PS.CASE,PS.re_type);
                         }                         
                         else if(PS.type ==1)
                         {
                             SP.set_Filler_RBItemStateChanged(PS.CASE, PS.re_type);
                         }

                    }
                    else if(NLGSlot instanceof VerbSlot)
                    { // verb
                        VerbSlot VS = (VerbSlot)NLGSlot;
                        SP.set_Verb_RBItemStateChanged(VS.VERB, VS.pluralVERB, VS.VOICE, VS.TENSE);                    
                    }
                    else if(NLGSlot instanceof StringSlot)
                    { // String slot
                        StringSlot SS = (StringSlot)NLGSlot;
                        
                        if(SS.isPreposition)
                        {
                            SP.set_prepositionItemStateChanged(SS.STRING); 
                        }
                        else if(SS.isAdverb)
                        {
                            SP.set_AdverbItemStateChanged(SS.STRING);
                        }
                        else
                        {
                            SP.set_StringRBItemStateChanged(SS.STRING);                          
                        }
                    } 
                    else if(NLGSlot instanceof ClsDescSlot)
                    { // String slot
                        ClsDescSlot ClsDesc_S = (ClsDescSlot)NLGSlot;
                        SP.set_lexicItemStateChanged(ClsDesc_S.CLSURI);                      
                    } 

                    SP.set_Slot_lbl("Slot " + i);
                    slots_VEC.add(SP);
                    SlotsGroup.add(SP.get_Slot_RB());                
                    SP.setBounds(10, 10 + i * (SLOT_PANEL_HEIGHT + 10), SLOT_PANEL_WIDTH, SLOT_PANEL_HEIGHT);            
                    SlotsPanel.add(SP);  
                    
                    SP.enableListeners(true);
                }//for

                
            }//micropl!=null
            else
            {
                logger.debug("Slots null");
            }

        }//MAOQM
        
        SlotsPanel.setPreferredSize(new Dimension(500,10 + (slots_VEC.size()+1) * (SLOT_PANEL_HEIGHT + 10)));
        SlotsPanel.setMinimumSize(new Dimension(500,10 + (slots_VEC.size()+1) * (SLOT_PANEL_HEIGHT + 10)));          
        
        repaint();         
        revalidate();
        
        templateButtonsListeneresEnabled = true; // enable the listeners
    }
    //-------------------------------------------------------------------------------
    private void initComponents() {//GEN-BEGIN:initComponents
        SlotsGroup = new javax.swing.ButtonGroup();
        templateInfo = new javax.swing.JPanel();
        ButtonPanel = new javax.swing.JPanel();
        insert_before = new javax.swing.JButton();
        insert_after = new javax.swing.JButton();
        delete = new javax.swing.JButton();
        templates = new javax.swing.JPanel();

        setLayout(new java.awt.BorderLayout());

        templateInfo.setLayout(new java.awt.BorderLayout());

        add(templateInfo, java.awt.BorderLayout.NORTH);

        ButtonPanel.setLayout(new java.awt.GridLayout(1, 0));

        insert_before.setFont(new java.awt.Font("Verdana", 1, 10));
        insert_before.setText("insert slot before");
        insert_before.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                insert_beforeActionPerformed(evt);
            }
        });

        ButtonPanel.add(insert_before);

        insert_after.setFont(new java.awt.Font("Verdana", 1, 10));
        insert_after.setText("insert slot after");
        insert_after.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                insert_afterActionPerformed(evt);
            }
        });

        ButtonPanel.add(insert_after);

        delete.setFont(new java.awt.Font("Verdana", 1, 10));
        delete.setText("delete slot");
        delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteActionPerformed(evt);
            }
        });

        ButtonPanel.add(delete);

        add(ButtonPanel, java.awt.BorderLayout.SOUTH);

        templates.setLayout(new java.awt.BorderLayout());

        templates.setMaximumSize(new java.awt.Dimension(1000, 1000));
        templates.setMinimumSize(new java.awt.Dimension(0, 400));
        templates.setPreferredSize(new java.awt.Dimension(0, 400));
        add(templates, java.awt.BorderLayout.CENTER);

    }//GEN-END:initComponents

    private void insert_beforeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_insert_beforeActionPerformed
        int j = -1;
        
        for(int i = 0;  i < slots_VEC.size(); i++)
        {
            logger.debug(i + "---");
            SlotPanel Sp = (SlotPanel)slots_VEC.get(i); //get SlotPanel

            if(Sp.get_Slot_RB().isSelected())
            {
                logger.debug(i + "is selected" );                
                j = i;
            }            
            //SlotsGroup.remove(Sp.get_Slot_RB()); // remove SlotPanel's RB from SlotsGroup
        }
            
        if( j == -1)
            return;
        
        SlotPanel newSP = new SlotPanel(this);
        
        
        if(j == 0)
        {
            slots_VEC.add(0,newSP);
        }
        else{
            slots_VEC.add(j,newSP);
        }

        SlotPanel SP = null;
        
        for(int i = 0; i < slots_VEC.size(); i++)
        {
            logger.debug(i);                  
            SP = (SlotPanel)slots_VEC.get(i);
            SP.set_Slot_lbl("Slot " + i);
            SlotsGroup.add(SP.get_Slot_RB());
            SP.setBounds(10, 10 + i * (SLOT_PANEL_HEIGHT + 10), SLOT_PANEL_WIDTH, SLOT_PANEL_HEIGHT);            
            SlotsPanel.add(SP);
        }        
        
        SlotsPanel.setPreferredSize(new Dimension(500,10 + (slots_VEC.size()+1) * (SLOT_PANEL_HEIGHT + 10)));
        SlotsPanel.setMinimumSize(new Dimension(500,10 + (slots_VEC.size()+1) * (SLOT_PANEL_HEIGHT + 10)));   
        
        revalidate();
        repaint();  
        
        this.saveMicroplan(); // saves the modified microplan
    }//GEN-LAST:event_insert_beforeActionPerformed

    private void insert_afterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_insert_afterActionPerformed
        int j = -1;
        
        for(int i = 0;  i < slots_VEC.size(); i++)
        {
            logger.debug(i + "---");
            SlotPanel Sp = (SlotPanel)slots_VEC.get(i); //get SlotPanel

            if(Sp.get_Slot_RB().isSelected())
            {
                logger.debug(i + "is selected" );                
                j = i;
            }            
            //SlotsGroup.remove(Sp.get_Slot_RB()); // remove SlotPanel's RB from SlotsGroup
        }
         
        SlotPanel newSP = new SlotPanel(this);
        
        if(j == slots_VEC.size()-1)
        {
            slots_VEC.add(newSP);
        }
        else
        {
            slots_VEC.add(j+1,newSP);
        }

        SlotPanel SP = null;
        for(int i = 0; i < slots_VEC.size(); i++)
        {
            logger.debug(i);                  
            SP = (SlotPanel)slots_VEC.get(i);
            SP.set_Slot_lbl("Slot " + i);
            SlotsGroup.add(SP.get_Slot_RB());
            SP.setBounds(10, 10 + i * (SLOT_PANEL_HEIGHT + 10), SLOT_PANEL_WIDTH, SLOT_PANEL_HEIGHT);            
            SlotsPanel.add(SP);
        }        
                
        SlotsPanel.setPreferredSize(new Dimension(500,10 + (slots_VEC.size()+1) * (SLOT_PANEL_HEIGHT + 10)));
        SlotsPanel.setMinimumSize(new Dimension(500,10 + (slots_VEC.size()+1) * (SLOT_PANEL_HEIGHT + 10)));                
        revalidate();
        repaint();        
        
        this.saveMicroplan(); // saves the modified microplan
    }//GEN-LAST:event_insert_afterActionPerformed

    private void deleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteActionPerformed
        int j =-1;
        //logger.debug(SlotsGroup.getButtonCount() + "count");
        
        for(int i = 0;  i < slots_VEC.size(); i++)
        {
            logger.debug(i + "---");
            SlotPanel Sp = (SlotPanel)slots_VEC.get(i); //get SlotPanel

            if(Sp.get_Slot_RB().isSelected())
            {
                logger.debug(i + "is selected" );
                j = i;
                SlotsPanel.remove(Sp); // remove SlotPanel from SlotsPanel            
            }
            
            //SlotsGroup.remove(Sp.get_Slot_RB()); // remove SlotPanel's RB from SlotsGroup                        
        }
                        
        if(j!=-1)
        slots_VEC.remove(j);
        
        //SlotsGroup = new ButtonGroup();
        
        logger.debug(SlotsGroup.getButtonCount() + " count " + slots_VEC.size());
        
        SlotPanel SP = null;
        for(int i = 0; i < slots_VEC.size(); i++)
        {
            logger.debug(i);                  
            SP = (SlotPanel)slots_VEC.get(i);
            SP.set_Slot_lbl("Slot " + i);
            //SlotsGroup.add(SP.get_Slot_RB());
            SP.setBounds(10, 10 + i * (SLOT_PANEL_HEIGHT + 10), SLOT_PANEL_WIDTH, SLOT_PANEL_HEIGHT);            
            SlotsPanel.add(SP);
        }
        
        SlotsPanel.setPreferredSize(new Dimension(500,10 + (slots_VEC.size()+1) * (SLOT_PANEL_HEIGHT + 10)));
        SlotsPanel.setMinimumSize(new Dimension(500,10 + (slots_VEC.size()+1) * (SLOT_PANEL_HEIGHT + 10)));
        
        revalidate();
        repaint();
        
        this.saveMicroplan(); // saves the modified microplan
    }//GEN-LAST:event_deleteActionPerformed
    
    public String getLanguage(String langSel)
    {
        if(langSel.compareTo(LanguagesArr[0])== 0)
            return Languages.ENGLISH;
        else if(langSel.compareTo(LanguagesArr[1])== 0)
            return Languages.GREEK;
        else
            return "ERROR";            
    }
    
    JScrollPane SlotsScrollPane;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel ButtonPanel;
    private javax.swing.ButtonGroup SlotsGroup;
    private javax.swing.JButton delete;
    private javax.swing.JButton insert_after;
    private javax.swing.JButton insert_before;
    private javax.swing.JPanel templateInfo;
    private javax.swing.JPanel templates;
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
        
        TemplatesPanel TP = new TemplatesPanel();
        
        testFrame.setLayout(new java.awt.BorderLayout());
        testFrame.add(TP);
        testFrame.show();
    }
    
    
    // get the field/values of the specified microplan and save it
    public void saveMicroplan()
    {
        Microplan microP = new Microplan(getSlots(), TemplatesCombo.getSelectedItem().toString(), used.isSelected() ,  get_property() + "-" + TemplatesCombo.getSelectedItem().toString() + "-" + getLanguage((String)LangCombo.getSelectedItem()), AggregationAllowed.isSelected(), getLanguage((String)LangCombo.getSelectedItem()));
        
        logger.debug("**************");
        microP.print();
        logger.debug("**************");
          
        if(NLPlugin.getMicroplansAndOrderingQM() !=null)
        {
            NLPlugin.getMicroplansAndOrderingQM().saveMicroplan(get_property(), microP, getLanguage((String)LangCombo.getSelectedItem()));
        }
    }
    
    private void AppropriatenessActionPerformed(java.awt.event.ActionEvent evt)
    {
        logger.debug("Editing Appropriateness...");       
                
        Vector<String> columnNames = new Vector<String>();
        Vector<String> user_types = new Vector<String>();
        
        //Iterator<String> ut_i = NLPlugin.getUserModellingQueryManager().getUserTypes();
        
        ///while(ut_i!=null && ut_i.hasNext())
        //{
        //    String curr_ut = ut_i.next();
        //    if(Utils.isLangSuitable(NLPlugin.getUserModellingQueryManager(), curr_ut, getLanguage((String)LangCombo.getSelectedItem())))
        //    user_types.add(curr_ut);
        //}
        
        columnNames.add("Greek");
        columnNames.add("English");
        
        EditUMParametersDialog editDialog = new EditUMParametersDialog(columnNames);                                   

        Vector testData = getUMData(get_property(), get_Templ_Name());
        Vector EditData = new Vector();
          
        for(int i = 0; i < testData.size(); i++)
        {
            Vector row = new Vector();
            
            Vector v = (Vector)testData.get(i);
            String ut = v.get(0).toString();
            
            System.out.println("ut:" + ut);
                     
            row.add(ut);
            row.add( Utils.isLangSuitable( NLPlugin.getUserModellingQueryManager(), ut, Languages.GREEK ));
            row.add( Utils.isLangSuitable( NLPlugin.getUserModellingQueryManager(), ut, Languages.ENGLISH ));
            
            EditData.add(row);
        }
        
        editDialog.setData(testData);
        editDialog.setCheckEditableFromTable(true);
        

           
        for(int i = 0; i < EditData.size(); i++)
        {
            Vector row = new Vector();
            
            for(int j = 0; j < row.size(); i++)
            {
                System.err.print( "" + row.get(j).toString());
            }
            
        }
        
        editDialog.setEditable(EditData);
        
        boolean ok = editDialog.showDlg(/*get_property() +  "-" + get_Templ_Name()*/ "Micro-plan appropriateness", 
                "Appropriateness scores for template " + get_property() +  "-" + get_Templ_Name() + " of " + get_property());

        if(ok)// Appropriateness changed
        {
            Vector data = editDialog.getData();

            //
            for(int i = 0; i < data.size(); i++)
            {
                Vector rowData = (Vector)data.get(i);  

                for(int j = 0; j < rowData.size(); j++)
                {
                    //System.err.print("---" + rowData.get(j));
                }

                rowData.setElementAt(NLPlugin.getUserModellingQueryManager().NLGUserModellingNS  + rowData.get(0).toString(), 0);
                //logger.debug();
            } 
            
            saveAppropriatenness(get_property() , get_Templ_Name(), data);
        }
           
    }
    
    private Vector getUMData(String property, String templName)
    {
        String MicroplanURI_EN = property + "-" + templName + "-" + Languages.ENGLISH;
        String MicroplanURI_GR = property + "-" + templName + "-" + Languages.GREEK;
                
        Vector data = new Vector();
        
        Iterator<String> userTypesIter = NLPlugin.getUserModellingQueryManager().getUserTypes();
        
        while(userTypesIter.hasNext())
        {
            Vector row = new Vector();
            String userType = userTypesIter.next().toString();
            
            row.add(userType);
            row.add(NLPlugin.getUserModellingQueryManager().getAppropriateness(MicroplanURI_EN, userType));
            row.add(NLPlugin.getUserModellingQueryManager().getAppropriateness(MicroplanURI_GR, userType));
            
            data.add(row);
        }
                
        return data;
    }
    
    public void saveAppropriatenness(String property, String templName,Vector data)
    {
        for(int i = 0; i < data.size(); i++)
        {
            Vector rowData = (Vector)data.get(i);  

            for(int j = 0; j < rowData.size(); j++)
            {
                logger.debug("---" + rowData.get(j));
            }

            String ut = rowData.get(0).toString();

            String Approp_En = rowData.get(1).toString();                        
            String Approp_Gr = rowData.get(2).toString();

            if(Approp_En.equals("undefined"))
            Approp_En = "-1";

            if(Approp_Gr.equals("undefined"))
            Approp_Gr = "-1";                    
            
            NLPlugin.getUserModellingQueryManager().setAppropriateness(property + "-" + templName + "-" + Languages.ENGLISH , rowData.get(0).toString(),Approp_En);
            NLPlugin.getUserModellingQueryManager().setAppropriateness(property + "-" + templName + "-" + Languages.GREEK, rowData.get(0).toString(),Approp_Gr);           

            //logger.debug();
        }   
    }
}

//-----------------------------------------------------------
