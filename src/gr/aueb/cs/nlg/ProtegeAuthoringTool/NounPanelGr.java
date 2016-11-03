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

//import com.ibm.icu.impl.data.LocaleElements_ar_BH;
import javax.swing.*;
import java.awt.event.*;

import gr.aueb.cs.nlg.Utils.XmlMsgs;
import gr.aueb.cs.nlg.NLFiles.Lex_Entry_GR;
        
public class NounPanelGr extends JPanel implements ActionListener, FocusListener
{
    
    LexiconTab LBT;
    
    public NounPanelGr(LexiconTab LBT) 
    {
        this.LBT = LBT;
        initComponents();
        set_Visibility_of_AdnvancedSpellingOptions(true);       
        
        
        // text field
                        
        SN_TEXT.addFocusListener(NounPanelGr.this);                
        SG_TEXT.addFocusListener(NounPanelGr.this);
        SA_TEXT.addFocusListener(NounPanelGr.this);        
        
        PN_TEXT.addFocusListener(NounPanelGr.this);                
        PG_TEXT.addFocusListener(NounPanelGr.this);
        PA_TEXT.addFocusListener(NounPanelGr.this);                
                
        uniflected_form_text.addFocusListener(NounPanelGr.this);
                
        // radio buttons
                
        this.Masc.addActionListener(NounPanelGr.this);
        this.Neuter.addActionListener(NounPanelGr.this);
        this.Feminine.addActionListener(NounPanelGr.this);
                
        this.countable_false_RB.addActionListener(NounPanelGr.this);
        this.countable_true_RB.addActionListener(NounPanelGr.this);
                
        this.def_num_sing.addActionListener(NounPanelGr.this);
        this.def_num_plur.addActionListener(NounPanelGr.this);
     
        this.infl_false.addActionListener(NounPanelGr.this);
        this.infl_true.addActionListener(NounPanelGr.this);
    }
    
    
      public void focusGained(FocusEvent e) 
      {
                    
      }
          
      public void focusLost(FocusEvent e) 
      {

          if(e.getSource() == uniflected_form_text)
          {
              LBT.SomethingChanged();
          }

          if (e.getSource() == SN_TEXT) 
          {            
            LBT.SomethingChanged();
          }

          if (e.getSource() == SG_TEXT) 
          {
            LBT.SomethingChanged();
          }
        
          if (e.getSource() == SA_TEXT) 
          {
            LBT.SomethingChanged();
          }
        
          if (e.getSource() == PN_TEXT) 
          {
            LBT.SomethingChanged();
          }
        
          if (e.getSource() == PG_TEXT) 
          {
            LBT.SomethingChanged();
          }        

          if (e.getSource() == PA_TEXT) 
          {
            LBT.SomethingChanged();
          }         
      }

      public void actionPerformed(ActionEvent e) 
      {

          if(e.getSource() == infl_true)
          {
              setInflectedView(infl_true.isSelected());
             //System.err.println("@@@@");
          }

          if(e.getSource() == infl_false)
          {
             setInflectedView(false);
             //System.err.println("!!!!");
          }
          
          if (e.getSource() == Masc) 
          {
            //System.err.println("Noun Panel GR Masc");  
            LBT.SomethingChanged();
          }

          if (e.getSource() == Neuter) 
          {
            //System.err.println("Noun Panel GR Neuter");  
            LBT.SomethingChanged();
          }

          if (e.getSource() == Feminine) 
          {
            //System.err.println("Noun Panel GR Feminine");               
            LBT.SomethingChanged();
          }

          if (e.getSource() == countable_false_RB) 
          {
            //System.err.println("Noun Panel GR countable_false_RB" ); 
            LBT.SomethingChanged();
          }

          if (e.getSource() == countable_true_RB) 
          {
            //System.err.println("Noun Panel GR countable_true_RB");
            LBT.SomethingChanged();
          }

          if (e.getSource() == def_num_sing) 
          {
            //System.err.println("Noun Panel GR def_num_sing");
            LBT.SomethingChanged();
          }

          if (e.getSource() == def_num_plur) 
          {
            //System.err.println("Noun Panel gr def_num_plur");
            LBT.SomethingChanged(); 
          }
          
      } // actionPerformed    
      
    public void setNoun(String n)
    {
        this.Noun_TEXT.setText(n);    
    }

    // get default number
    public String  get_def_num()
    {
        if(def_num_plur.isSelected())
        {
            return XmlMsgs.PLURAL;
        }
        else if(def_num_sing.isSelected())
        {
            return XmlMsgs.SINGULAR;
        }
        return "error";
    }
    
    //get singular form
    public String get_singular_form()
    {
        return Nom_sing.getText();
    }
    
    //set plural form
    public String get_plural_form()
    {
       return Nom_plural.getText(); 
    }
    
    //get gender
    public String get_gender()
    {
        if(Masc.isSelected())
        {
            return XmlMsgs.GENDER_MASCULINE;
        }
        else if(Feminine.isSelected())
        {
            return XmlMsgs.GENDER_FEMININE;
        }
        else if (Neuter.isSelected())
        {
            return XmlMsgs.GENDER_NEUTER;
        }
        
        return "error";
    }
    
    //get countable
    public boolean get_countable()
    {
        if(countable_true_RB.isSelected())
        {
            return true;
        }
        else
            return false;
       
    }
    
    //===============================================================
    
    // set default number
    public void set_def_num(String val){
        if(val.compareTo(XmlMsgs.PLURAL)==0)
        {
            this.def_num_plur.setSelected(true);
        }
        else if(val.compareTo(XmlMsgs.SINGULAR)==0)
        {
            this.def_num_sing.setSelected(true);
        }
        else{
        }
    }
    
    //set singular form
    public void set_singular_form(String val){
        Nom_sing.setText(val);
    }
    
    //set plural form
    public void set_plural_form(String val){
       Nom_plural.setText(val); 
    }
    
    //set gender
    public void set_gender(String val)
    {
        if(val.compareTo(XmlMsgs.GENDER_MASCULINE)==0)
        {
            Masc.setSelected(true);
        }
        else if(val.compareTo(XmlMsgs.GENDER_FEMININE)==0)
        {
            Feminine.setSelected(true);
        }
        else if(val.compareTo(XmlMsgs.GENDER_NEUTER)==0)
        {
            Neuter.setSelected(true);
        }
    }
    
    //set countable
    public void set_countable(boolean c)
    {
        if(c == true)
        {
            this.countable_true_RB.setSelected(true);
        }
        else if(c == false)
        {
            this.countable_false_RB.setSelected(true);
        }
    }
    
    public void setAdvnancedSpellingOptions(Lex_Entry_GR entry)
    {
        if(this.infl_true.isSelected())
        {
            SN_TEXT.setText(entry.get(XmlMsgs.NOMINATIVE_TAG, XmlMsgs.SINGULAR));
            SG_TEXT.setText(entry.get(XmlMsgs.GENITIVE_TAG, XmlMsgs.SINGULAR));
            SA_TEXT.setText(entry.get(XmlMsgs.ACCUSATIVE_TAG, XmlMsgs.SINGULAR));

            PN_TEXT.setText(entry.get(XmlMsgs.NOMINATIVE_TAG, XmlMsgs.PLURAL));
            PG_TEXT.setText(entry.get(XmlMsgs.GENITIVE_TAG, XmlMsgs.PLURAL));
            PA_TEXT.setText(entry.get(XmlMsgs.ACCUSATIVE_TAG, XmlMsgs.PLURAL));        
        }
        else
        {
            uniflected_form_text.setText(entry.get(XmlMsgs.NOMINATIVE_TAG, XmlMsgs.SINGULAR));
        }
        
    }
    
    public String get_SN()
    {
        return SN_TEXT.getText();
    }
    
    public String get_SG()
    {
        return SG_TEXT.getText();
    }
    
    public String get_SA()
    {
        return SA_TEXT.getText();
    }
    
    public String get_PN()
    {
        return PN_TEXT.getText();
    }
    
    public String get_PG()
    {
        return PG_TEXT.getText();
    }    
    
    public String get_PA()
    {
        return PA_TEXT.getText();
    }
    
    public String getInflected()
    {
        return uniflected_form_text.getText();
    }
    
    public boolean isInflected()
    {
        return this.infl_true.isSelected();
    }
    private void initComponents() 
    {
        java.awt.GridBagConstraints gridBagConstraints;

        // button groups
        GrammaticalGenderButtonGroup = new javax.swing.ButtonGroup();
        CountableButtonGroup = new javax.swing.ButtonGroup();
        InflectedButtonGroup = new javax.swing.ButtonGroup();
        
        Nom_sing_label = new javax.swing.JLabel();
        Nom_plural_label = new javax.swing.JLabel();
        Nom_sing = new javax.swing.JTextField();
        Nom_plural = new javax.swing.JTextField();
        
        GrammaticalGender = new javax.swing.JLabel();
        Masc = new javax.swing.JRadioButton();
        Feminine = new javax.swing.JRadioButton();
        Neuter = new javax.swing.JRadioButton();
        
        //AdvancedSpellOptions = new javax.swing.JCheckBox();
        NounLabel = new javax.swing.JLabel();
        Noun_TEXT = new javax.swing.JLabel();
        
        
        
        count_lbl = new javax.swing.JLabel();
        countable_true_RB = new javax.swing.JRadioButton();
        countable_false_RB = new javax.swing.JRadioButton();
        
        
        infl_label = new javax.swing.JLabel();
        infl_true = new javax.swing.JRadioButton();
        infl_false = new javax.swing.JRadioButton();
        InflectedButtonGroup.add(infl_true);
        InflectedButtonGroup.add(infl_false);
                
        infl_true.setText("Yes");
        infl_false.setText("No");
        
        infl_true.setBounds(200, 38, 80, 23);
        infl_false.setBounds(300, 38, 80, 23);
        
        add(infl_true);
        add(infl_false);

        def_num_lbl = new javax.swing.JLabel();
        def_num_sing = new javax.swing.JRadioButton();
        def_num_plur = new javax.swing.JRadioButton();
        
        
        DefNumButtonGroup = new ButtonGroup();
        DefNumButtonGroup.add(def_num_plur);
        DefNumButtonGroup.add(def_num_sing); 
        
        SN = new javax.swing.JLabel();
        SN_TEXT = new javax.swing.JTextField();
        SG = new javax.swing.JLabel();
        SG_TEXT = new javax.swing.JTextField();
        SA = new javax.swing.JLabel();
        SA_TEXT = new javax.swing.JTextField();
        PN = new javax.swing.JLabel();
        PN_TEXT = new javax.swing.JTextField();
        PG = new javax.swing.JLabel();
        PG_TEXT = new javax.swing.JTextField();
        PA = new javax.swing.JLabel();
        PA_TEXT = new javax.swing.JTextField();

        setLayout(null);

        inflectedForms = new javax.swing.JPanel();
        nounProps = new javax.swing.JPanel();
        
        inflectedForms.setLayout(null);
        nounProps.setLayout(null);
        
        nounProps.setBounds(50, 248, 1000, 300);
        add(nounProps);
        
        GrammaticalGender.setFont(new java.awt.Font("Dialog", 1, 11));
        GrammaticalGender.setText("Grammatical Gender");
        nounProps.add(GrammaticalGender);
        GrammaticalGender.setBounds(0, 5, 170, 23);

        Masc.setFont(new java.awt.Font("Dialog", 1, 11));
        Masc.setText("Masculine");
        GrammaticalGenderButtonGroup.add(Masc);
        nounProps.add(Masc);
        Masc.setBounds(150, 5, 81, 23);

        Feminine.setFont(new java.awt.Font("Dialog", 1, 11));
        Feminine.setText("Feminine");
        GrammaticalGenderButtonGroup.add(Feminine);
        nounProps.add(Feminine);
        Feminine.setBounds(250, 5, 77, 23);

        Neuter.setFont(new java.awt.Font("Dialog", 1, 11));
        Neuter.setText("Neuter");
        GrammaticalGenderButtonGroup.add(Neuter);        
        nounProps.add(Neuter);
        Neuter.setBounds(350, 5, 142, 23);


        count_lbl.setFont(new java.awt.Font("Dialog", 1, 11));
        count_lbl.setText("Countable");
        nounProps.add(count_lbl);
        count_lbl.setBounds(0, 40, 172, 15);
        
        countable_true_RB.setFont(new java.awt.Font("Dialog", 1, 11));
        countable_true_RB.setText("Yes");
        CountableButtonGroup.add(countable_true_RB);
        nounProps.add(countable_true_RB);
        countable_true_RB.setBounds(150, 40, 81, 23);

        countable_false_RB.setFont(new java.awt.Font("Dialog", 1, 11));
        countable_false_RB.setText("No");
        CountableButtonGroup.add(countable_false_RB);
        nounProps.add(countable_false_RB);
        countable_false_RB.setBounds(250, 40, 77, 23);
        
        def_num_lbl.setFont(new java.awt.Font("Dialog", 1, 11));
        def_num_lbl.setText("Default number");
        nounProps.add(def_num_lbl);
        def_num_lbl.setBounds(0, 70, 171, 15);

        def_num_sing.setFont(new java.awt.Font("Dialog", 1, 11));
        def_num_sing.setText("Singular");
        nounProps.add(def_num_sing);
        def_num_sing.setBounds(150, 70, 80, 23);

        def_num_plur.setFont(new java.awt.Font("Dialog", 1, 11));
        def_num_plur.setText("Plural");
        nounProps.add(def_num_plur);
        def_num_plur.setBounds(250, 70, 77, 23);

        /**************/
        infl_label.setFont(new java.awt.Font("Dialog", 1, 11));
        infl_label.setText("Inflected");
        add(infl_label);
        infl_label.setBounds(50, 38, 172, 15);
               
        //inflectedForms.setBorder(new javax.swing.border.LineBorder(java.awt.Color.WHITE));
        
        inflectedForms.setBounds(50, 68, 600, 180);
        add(inflectedForms);
        
        
        SN.setFont(new java.awt.Font("Dialog", 1, 11));
        SN.setText("Singular nominative");
        inflectedForms.add(SN);
        SN.setBounds(0, 5, 172, 15);

        SN_TEXT.setMinimumSize(new java.awt.Dimension(300, 20));
        SN_TEXT.setPreferredSize(new java.awt.Dimension(300, 20));
        inflectedForms.add(SN_TEXT);
        SN_TEXT.setBounds(150, 5, 330, 20);

        SG.setFont(new java.awt.Font("Dialog", 1, 11));
        SG.setText("Singular genitive");
        inflectedForms.add(SG);
        SG.setBounds(0, 35, 172, 15);

        SG_TEXT.setMinimumSize(new java.awt.Dimension(300, 20));
        SG_TEXT.setPreferredSize(new java.awt.Dimension(300, 20));
        inflectedForms.add(SG_TEXT);
        SG_TEXT.setBounds(150, 35, 330, 20);

        SA.setFont(new java.awt.Font("Dialog", 1, 11));
        SA.setText("Singular accusative");
        inflectedForms.add(SA);
        SA.setBounds(0, 65, 170, 15);

        SA_TEXT.setMinimumSize(new java.awt.Dimension(300, 20));
        SA_TEXT.setPreferredSize(new java.awt.Dimension(300, 20));
        inflectedForms.add(SA_TEXT);
        SA_TEXT.setBounds(150, 65, 330, 20);

        PN.setFont(new java.awt.Font("Dialog", 1, 11));
        PN.setText("Plural nominative");
        inflectedForms.add(PN);
        PN.setBounds(0, 95, 169, 15);

        PN_TEXT.setMinimumSize(new java.awt.Dimension(300, 20));
        PN_TEXT.setPreferredSize(new java.awt.Dimension(300, 20));
        inflectedForms.add(PN_TEXT);
        PN_TEXT.setBounds(150, 95, 330, 20);

        PG.setFont(new java.awt.Font("Dialog", 1, 11));
        PG.setText("Plural genitive");
        inflectedForms.add(PG);
        PG.setBounds(0, 125, 170, 15);

        PG_TEXT.setMinimumSize(new java.awt.Dimension(300, 20));
        PG_TEXT.setPreferredSize(new java.awt.Dimension(300, 20));
        inflectedForms.add(PG_TEXT);
        PG_TEXT.setBounds(150, 125, 330, 20);

        PA.setFont(new java.awt.Font("Dialog", 1, 11));
        PA.setText("Plural accusative");
        inflectedForms.add(PA);
        PA.setBounds(0, 155, 169, 15);

        PA_TEXT.setMinimumSize(new java.awt.Dimension(300, 20));
        PA_TEXT.setPreferredSize(new java.awt.Dimension(300, 20));
        inflectedForms.add(PA_TEXT);
        PA_TEXT.setBounds(150, 155, 330, 20);

        uniflectedPanel = new javax.swing.JPanel();
        uniflectedPanel.setLayout(null);
        uniflectedPanel.setBounds(50, 68, 600, 180);
                
        add(uniflectedPanel);
        
        uniflected_form = new javax.swing.JLabel();
        uniflected_form_text = new javax.swing.JTextField();
        
        uniflected_form.setText("Uninflected form");   
        uniflected_form.setFont(new java.awt.Font("Dialog", 1, 11));
        uniflectedPanel.add(uniflected_form);
        uniflected_form.setBounds(0, 5, 169, 15);
                
        uniflected_form_text.setMinimumSize(new java.awt.Dimension(300, 20));
        uniflected_form_text.setPreferredSize(new java.awt.Dimension(300, 20));        
        uniflected_form_text.setBounds(150, 5, 330, 20);
        
        uniflectedPanel.add(uniflected_form_text);        
        
        
        infl_true.setSelected(true);
        setInflectedView(true);
    }

    public void setInflectedView( boolean b)
    {        
        if(b)
        {
            infl_true.setSelected(true);
        }
        else
        {
            infl_false.setSelected(true);
        }
        
        uniflectedPanel.setVisible(!b);
        uniflectedPanel.repaint();
        uniflectedPanel.revalidate();
        
        inflectedForms.setVisible(b);
        inflectedForms.repaint();
        inflectedForms.revalidate();
        
        if(b)
        {
            nounProps.setBounds(50, 248, 1000, 300);
        }
        else
        {
            nounProps.setBounds(50, 98, 1000, 300);
        }
        
        this.repaint();
        this.revalidate();
    }



    
    private javax.swing.ButtonGroup  DefNumButtonGroup;
  
    // Variables declaration - do not modify
    //private javax.swing.JCheckBox AdvancedSpellOptions;
    
    
    private javax.swing.JPanel inflectedForms = new javax.swing.JPanel();
    private javax.swing.JPanel nounProps = new javax.swing.JPanel();
    private javax.swing.JPanel uniflectedPanel = new javax.swing.JPanel();
    
    private javax.swing.ButtonGroup CountableButtonGroup;
    private javax.swing.ButtonGroup GrammaticalGenderButtonGroup;
    private javax.swing.ButtonGroup InflectedButtonGroup;
    
    private javax.swing.JRadioButton Feminine;
    private javax.swing.JLabel GrammaticalGender;

    private javax.swing.JRadioButton Masc;
    private javax.swing.JRadioButton Neuter;
    private javax.swing.JTextField Nom_plural;
    private javax.swing.JLabel Nom_plural_label;
    private javax.swing.JTextField Nom_sing;
    private javax.swing.JLabel Nom_sing_label;
    private javax.swing.JLabel NounLabel;
    private javax.swing.JLabel Noun_TEXT;
    
    private javax.swing.JLabel uniflected_form;
    private javax.swing.JTextField uniflected_form_text;
    
    private javax.swing.JLabel PA;
    private javax.swing.JTextField PA_TEXT;    
    private javax.swing.JLabel PG;
    private javax.swing.JTextField PG_TEXT;
    private javax.swing.JLabel PN;
    private javax.swing.JTextField PN_TEXT;
    private javax.swing.JLabel SA;
    private javax.swing.JTextField SA_TEXT;
    private javax.swing.JLabel SG;
    private javax.swing.JTextField SG_TEXT;
    private javax.swing.JLabel SN;
    private javax.swing.JTextField SN_TEXT;
    private javax.swing.JLabel count_lbl;
    private javax.swing.JRadioButton countable_false_RB;
    private javax.swing.JRadioButton countable_true_RB;
    private javax.swing.JLabel def_num_lbl;
    private javax.swing.JRadioButton def_num_plur;
    private javax.swing.JRadioButton def_num_sing;
    private javax.swing.JRadioButton infl_false;
    private javax.swing.JLabel infl_label;
    private javax.swing.JRadioButton infl_true;
   
    public void clear()
    {
        Nom_sing.setText("");
        Nom_plural.setText("");
        
        Masc.setSelected(true);        
        def_num_sing.setSelected(true);   
        countable_true_RB.setSelected(true);
        
        this.infl_true.setSelected(true);
        uniflected_form_text.setText("");
        this.setInflectedView(true);
        SN_TEXT.setText(""); 
        SG_TEXT.setText(""); 
        SA_TEXT.setText(""); 
        
        PN_TEXT.setText(""); 
        PG_TEXT.setText(""); 
        PA_TEXT.setText(""); 
    }
    
    public void set_Visibility_of_AdnvancedSpellingOptions(boolean visible){
       
  
        SN.setVisible(visible); 
        SG.setVisible(visible);
        SA.setVisible(visible);
        
        PN.setVisible(visible);
        PG.setVisible(visible);
        PA.setVisible(visible);
        
        SN_TEXT.setVisible(visible); 
        SG_TEXT.setVisible(visible);
        SA_TEXT.setVisible(visible);
        
        PN_TEXT.setVisible(visible);
        PG_TEXT.setVisible(visible);
        PA_TEXT.setVisible(visible);        

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
        
        
        NounPanelGr p = new NounPanelGr(null);
        testFrame.setLayout(new java.awt.BorderLayout());
        testFrame.add(p);
        testFrame.show();
        
        
    } 
}
