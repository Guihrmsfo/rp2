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
import gr.aueb.cs.nlg.Utils.XmlMsgs;
import java.awt.event.*;

public class NounPanelEn extends JPanel implements ActionListener, FocusListener 
{
    LexiconTab LBT;
    /** Creates new form NounPanelGr */
    public NounPanelEn(LexiconTab LBT) 
    {
        this.LBT = LBT;
        
        initComponents();   
        
        // text field
                        
        Nom_sing.addFocusListener(NounPanelEn.this);                
        Nom_plural.addFocusListener(NounPanelEn.this);
                
        // radio buttons
                
        this.Masc.addActionListener(NounPanelEn.this);
        this.Neuter.addActionListener(NounPanelEn.this);
        this.Feminine.addActionListener(NounPanelEn.this);
                
        this.countable_false_RB.addActionListener(NounPanelEn.this);
        this.countable_true_RB.addActionListener(NounPanelEn.this);
                
        this.def_num_sing.addActionListener(NounPanelEn.this);
        this.def_num_plur.addActionListener(NounPanelEn.this);
                
    }
    
      public void focusGained(FocusEvent e) 
      {
                    
      }
          
      public void focusLost(FocusEvent e) 
      {

          //System.err.println("Noun en focusLost");

          if (e.getSource() == Nom_sing) 
          {
            LBT.SomethingChanged();
          }

          if (e.getSource() == Nom_plural) 
          {
            LBT.SomethingChanged();
          }

      }

      public void actionPerformed(ActionEvent e) 
      {

          //System.err.println("Noun Panel En actionPerformed");

          if (e.getSource() == Masc) 
          {
            //System.err.println("Noun Panel En voice_chooser");  
            LBT.SomethingChanged();
          }

          if (e.getSource() == Neuter) 
          {
            //System.err.println("Noun Panel En tense_chooser");  
            LBT.SomethingChanged();
          }

          if (e.getSource() == Feminine) 
          {
            //System.err.println("Noun Panel En TypeCmb");               
            LBT.SomethingChanged();
          }

          if (e.getSource() == countable_false_RB) 
          {
            //System.err.println("Noun Panel En case_chooser_CB"); 
            LBT.SomethingChanged();
          }

          if (e.getSource() == countable_true_RB) 
          {
            //System.err.println("Noun Panel En Referring_Expr_To_Owner_RB");
            LBT.SomethingChanged();
          }

          if (e.getSource() == def_num_sing) 
          {
            //System.err.println("Noun Panel En Filler_RB");
            LBT.SomethingChanged();
          }

          if (e.getSource() == def_num_plur) 
          {
            //System.err.println("Noun Panel En String_RB");
            LBT.SomethingChanged();
          }
          
      } // actionPerformed    
    
    public boolean get_countable()
    {
        if(countable_true_RB.isSelected())
            return true;
        else
            return false;
       
    }
    
    //set noun
    public void setNoun(String n){
        Noun_TEXT.setText(n);    
    }

    // get default number
    public String  get_def_num(){
        if(def_num_plur.isSelected()){
            return XmlMsgs.PLURAL;
        }
        else if(def_num_sing.isSelected()){
            return XmlMsgs.SINGULAR;
        }
        return "error";
    }
    
    //get singular form
    public String get_singular_form(){
        return Nom_sing.getText();
    }
    
    //set plural form
    public String get_plural_form(){
       return Nom_plural.getText(); 
    }
    
    //get gender
    public String get_gender(){
        if(Masc.isSelected()){
            return XmlMsgs.GENDER_MASCULINE;
        }
        else if(Feminine.isSelected()){
            return XmlMsgs.GENDER_FEMININE;
        }
        else if (Neuter.isSelected()){
            return XmlMsgs.GENDER_NONPESRSONAL;
        }
        
        return "error";
    }
    
    
    //===============================================================
    
    // set default number
    public void set_def_num(String val){
        if(val.compareTo(XmlMsgs.PLURAL)==0){
            this.def_num_plur.setSelected(true);
        }
        else if(val.compareTo(XmlMsgs.SINGULAR)==0){
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
    public void set_gender(String val){
        if(val.compareTo(XmlMsgs.GENDER_MASCULINE)==0){
            Masc.setSelected(true);
        }
        else if(val.compareTo(XmlMsgs.GENDER_FEMININE)==0){
            Feminine.setSelected(true);
        }
        else if(val.compareTo(XmlMsgs.GENDER_NONPESRSONAL)==0){
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
    
    private void initComponents() {

        GrammaticalGenderButtonGroup = new javax.swing.ButtonGroup();
        CountableButtonGroup = new javax.swing.ButtonGroup();
        Nom_sing_label = new javax.swing.JLabel();
        Nom_plural_label = new javax.swing.JLabel();
        Nom_sing = new javax.swing.JTextField();
        Nom_plural = new javax.swing.JTextField();
        GrammaticalGender = new javax.swing.JLabel();
        Masc = new javax.swing.JRadioButton();
        Feminine = new javax.swing.JRadioButton();
        Neuter = new javax.swing.JRadioButton();
        
        NounLabel = new javax.swing.JLabel();
        countable_true_RB = new javax.swing.JRadioButton();
        countable_false_RB = new javax.swing.JRadioButton();
        Noun_TEXT = new javax.swing.JLabel();
        infl_label = new javax.swing.JLabel();
        infl_true = new javax.swing.JRadioButton();
        infl_false = new javax.swing.JRadioButton();
        def_num_lbl = new javax.swing.JLabel();
        def_num_sing = new javax.swing.JRadioButton();
        def_num_plur = new javax.swing.JRadioButton();
        count_lbl = new javax.swing.JLabel();
        

        setLayout(null);

        Nom_sing_label.setFont(new java.awt.Font("Dialog", 1, 11));
        Nom_sing_label.setText("Singular form");
        add(Nom_sing_label);
        Nom_sing_label.setBounds(50, 68, 169, 15);

        Nom_plural_label.setFont(new java.awt.Font("Dialog", 1, 11));
        Nom_plural_label.setText("Plural form");
        add(Nom_plural_label);
        Nom_plural_label.setBounds(50, 99, 168, 15);

        Nom_sing.setMinimumSize(new java.awt.Dimension(300, 20));
        Nom_sing.setPreferredSize(new java.awt.Dimension(300, 20));
        add(Nom_sing);
        Nom_sing.setBounds(200, 65, 330, 20);

        Nom_plural.setMinimumSize(new java.awt.Dimension(300, 20));
        Nom_plural.setPreferredSize(new java.awt.Dimension(300, 20));
        add(Nom_plural);
        Nom_plural.setBounds(200, 96, 330, 20);

        GrammaticalGender.setFont(new java.awt.Font("Dialog", 1, 11));
        GrammaticalGender.setText("Natural gender");
        add(GrammaticalGender);
        GrammaticalGender.setBounds(50, 127, 170, 23);

        Masc.setFont(new java.awt.Font("Dialog", 1, 11));
        Masc.setText("Masculine");
        GrammaticalGenderButtonGroup.add(Masc);
        add(Masc);
        Masc.setBounds(200, 127, 81, 23);

        Feminine.setFont(new java.awt.Font("Dialog", 1, 11));
        Feminine.setText("Feminine");
        GrammaticalGenderButtonGroup.add(Feminine);
        add(Feminine);
        Feminine.setBounds(300, 127, 77, 23);

        Neuter.setFont(new java.awt.Font("Dialog", 1, 11));
        Neuter.setText("Neuter");
        GrammaticalGenderButtonGroup.add(Neuter);

        add(Neuter);
        Neuter.setBounds(400, 127, 142, 23);

        /*
        NounLabel.setFont(new java.awt.Font("Dialog", 1, 11));
        NounLabel.setText("Noun");
        add(NounLabel);
        NounLabel.setBounds(50, 36, 172, 15);
         */

        countable_true_RB.setFont(new java.awt.Font("Dialog", 1, 11));
        countable_true_RB.setText("Yes");
        CountableButtonGroup.add(countable_true_RB);
        add(countable_true_RB);
        countable_true_RB.setBounds(200, 187, 81, 23);

        countable_false_RB.setFont(new java.awt.Font("Dialog", 1, 11));
        countable_false_RB.setText("No");
        CountableButtonGroup.add(countable_false_RB);
        add(countable_false_RB);
        countable_false_RB.setBounds(300, 187, 77, 23);

        /*
        Noun_TEXT.setFont(new java.awt.Font("Verdana", 1, 10));
        Noun_TEXT.setText("                            ");
        add(Noun_TEXT);
        Noun_TEXT.setBounds(290, 36, 330, 14);
         */

        def_num_lbl.setFont(new java.awt.Font("Dialog", 1, 11));
        def_num_lbl.setText("Default number");
        add(def_num_lbl);
        def_num_lbl.setBounds(50, 157, 171, 15);

        def_num_sing.setFont(new java.awt.Font("Dialog", 1, 11));
        def_num_sing.setText("Singular");
        add(def_num_sing);
        def_num_sing.setBounds(200, 157, 80, 23);

        def_num_plur.setFont(new java.awt.Font("Dialog", 1, 11));
        def_num_plur.setText("Plural");
        add(def_num_plur);
        def_num_plur.setBounds(300, 157, 77, 23);

        count_lbl.setFont(new java.awt.Font("Dialog", 1, 11));
        count_lbl.setText("Countable");
        add(count_lbl);
        count_lbl.setBounds(50, 187, 172, 15);

 
        DefNumButtonGroup = new ButtonGroup();
        DefNumButtonGroup.add(def_num_plur);
        DefNumButtonGroup.add(def_num_sing);      
    }

    
    private javax.swing.ButtonGroup DefNumButtonGroup;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox AdvancedSpellOptions;
    private javax.swing.ButtonGroup CountableButtonGroup;
    private javax.swing.JRadioButton Feminine;
    private javax.swing.JLabel GrammaticalGender;
    private javax.swing.ButtonGroup GrammaticalGenderButtonGroup;
    private javax.swing.JRadioButton Masc;
    private javax.swing.JRadioButton Neuter;
    private javax.swing.JTextField Nom_plural;
    private javax.swing.JLabel Nom_plural_label;
    private javax.swing.JTextField Nom_sing;
    private javax.swing.JLabel Nom_sing_label;
    private javax.swing.JLabel NounLabel;
    private javax.swing.JLabel Noun_TEXT;


    private javax.swing.JLabel count_lbl;
    private javax.swing.JRadioButton countable_false_RB;
    private javax.swing.JRadioButton countable_true_RB;
    private javax.swing.JLabel def_num_lbl;
    private javax.swing.JRadioButton def_num_plur;
    private javax.swing.JRadioButton def_num_sing;
    private javax.swing.JRadioButton infl_false;
    private javax.swing.JLabel infl_label;
    private javax.swing.JRadioButton infl_true;
    // End of variables declaration//GEN-END:variables
    
     
    public void clear()
    {
        Nom_sing.setText("");
        Nom_plural.setText("");
        
        Masc.setSelected(true);        
        def_num_sing.setSelected(true);   
        countable_true_RB.setSelected(true);

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
        
        
        NounPanelEn p = new NounPanelEn(null);
        testFrame.setLayout(new java.awt.BorderLayout());
        testFrame.add(p);
        testFrame.show();
        
        
    }    
}
