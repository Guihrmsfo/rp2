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

import javax.swing.JPanel;
import java.awt.event.*;

import gr.aueb.cs.nlg.Utils.*;
        

public class SlotPanel extends JPanel implements ActionListener, FocusListener 
{        
    private TemplatesPanel TP;
    private String re_types [] = { "auto", "pronoun", "name", "definite noun phrase", "indefinite noun phrase", "demonstrative" };
    
    private boolean listenersEnabled = false;
            
    public SlotPanel( TemplatesPanel TP) 
    {     
        
        
        listenersEnabled = true;
                
        this.TP = TP;
        
        initComponents();                
        hideAll(); // hide all 
        this.set_Referring_Expr_To_Owner_RB("nominative","auto");
        
        this.lexic.setVisible(false);
        case_chooser_CB.addItem("nominative");
        case_chooser_CB.addItem("genitive");
        case_chooser_CB.addItem("accusative");
        
        voice_chooser.addItem("active");
        voice_chooser.addItem("passive");
        
        tense_chooser.addItem("present");
        tense_chooser.addItem("past");
        
        TypeCmb.setModel(new javax.swing.DefaultComboBoxModel(re_types));
        // text fields
        String_text_field.addFocusListener(SlotPanel.this);
        String_text_field_plural.addFocusListener(SlotPanel.this);
        
        //combos
        voice_chooser.addActionListener(SlotPanel.this);
        tense_chooser.addActionListener(SlotPanel.this);
        TypeCmb.addActionListener(SlotPanel.this);
        case_chooser_CB.addActionListener(SlotPanel.this);
        
        // radio buttons
        Referring_Expr_To_Owner_RB.addActionListener(SlotPanel.this);
        Filler_RB.addActionListener(SlotPanel.this);
        String_RB.addActionListener(SlotPanel.this);
        Verb_RB.addActionListener(SlotPanel.this);
        preposition.addActionListener(SlotPanel.this);
        adverb.addActionListener(SlotPanel.this);        
    }

    
      public void enableListeners(boolean b)
      {
          this.listenersEnabled = b;
      }
            
      public void focusGained(FocusEvent e) 
      {
                    
      }

      public void focusLost(FocusEvent e) 
      {
          if(listenersEnabled)
          {
              ////System.err.println("Slots Panel focusLost");

              if (e.getSource() == String_text_field) 
              {
                  TP.saveMicroplan();
              }

              if (e.getSource() == String_text_field_plural) 
              {
                  TP.saveMicroplan();
              }
          }
      }

      public void actionPerformed(ActionEvent e) 
      {
          if(listenersEnabled)
          {
              ////System.err.println("Slots Panel actionPerformed");

              if (e.getSource() == voice_chooser) 
              {
                //System.err.println("Slots Panel voice_chooser");  
                TP.saveMicroplan();
              }

              if (e.getSource() == tense_chooser) 
              {
                //System.err.println("Slots Panel tense_chooser");  
                TP.saveMicroplan();
              }

              if (e.getSource() == TypeCmb) 
              {
                //System.err.println("Slots Panel TypeCmb");               
                TP.saveMicroplan();
              }

              if (e.getSource() == case_chooser_CB) 
              {
                //System.err.println("Slots Panel case_chooser_CB"); 
                TP.saveMicroplan();
              }

              if (e.getSource() == Referring_Expr_To_Owner_RB) 
              {
                //System.err.println("Slots Panel Referring_Expr_To_Owner_RB");
                TP.saveMicroplan();
              }

              if (e.getSource() == Filler_RB) 
              {
                //System.err.println("Slots Panel Filler_RB");
                TP.saveMicroplan();
              }

              if (e.getSource() == String_RB) 
              {
                //System.err.println("Slots Panel String_RB");
                TP.saveMicroplan();
              }

              if (e.getSource() == preposition) 
              {
                //System.err.println("Slots Panel preposition");
                TP.saveMicroplan();
              }
              
              if (e.getSource() == adverb) 
              {
                //System.err.println("Slots Panel adverb");
                TP.saveMicroplan();
              }
              
              if (e.getSource() == Verb_RB) 
              {
                //System.err.println("Slots Panel String_RB");
                TP.saveMicroplan();
              }      
          }
      } // actionPerformed
  
    public void set_Slot_lbl(String txt)
    {
        Slot_lbl.setText(txt);
    }
        
    public JRadioButton get_Slot_RB()
    {
        return Slot_RB;
    }
    
    public void setREType(String type)
    {
        if(type.equals(XmlMsgs.RE_AUTO))
        {
            TypeCmb.setSelectedItem("auto");
        }
        else if(type.equals(XmlMsgs.RE_DEF_ART))
        {
            TypeCmb.setSelectedItem("definite noun phrase");
        }
        else if(type.equals(XmlMsgs.RE_INDEF_ART))
        {
            TypeCmb.setSelectedItem("indefinite noun phrase");
        }
        else if(type.equals(XmlMsgs.RE_PRONOUN))
        {
            TypeCmb.setSelectedItem("pronoun");
        }
        else if(type.equals(XmlMsgs.RE_DEMONSTRATIVE))
        {
            TypeCmb.setSelectedItem("demonstrative");
        }
        else if(type.equals(XmlMsgs.RE_FULLNAME))
        {
            TypeCmb.setSelectedItem("Name");
        }
    }
    
    public String getREType()
    {
        String type = TypeCmb.getSelectedItem().toString();
        
        if(type.equals("auto"))
        {
            return XmlMsgs.RE_AUTO;
        }
        else if(type.equals("definite noun phrase"))
        {
            return XmlMsgs.RE_DEF_ART;
        }
        else if(type.equals("indefinite noun phrase"))
        {
            return XmlMsgs.RE_INDEF_ART;
        }
        else if(type.equals("pronoun"))
        {
            return XmlMsgs.RE_PRONOUN;
        }
        else if(type.equals("demonstrative"))
        {
            return XmlMsgs.RE_DEMONSTRATIVE;
        }
        else if(type.equals("Name"))
        {
            return XmlMsgs.RE_FULLNAME;
        }
        else
        {
            return "";
        }
    }    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {
        java.awt.GridBagConstraints gridBagConstraints;

        optionsGroup = new javax.swing.ButtonGroup();
        buttonGroup1 = new javax.swing.ButtonGroup();
        SlotSelectionPanel = new javax.swing.JPanel();
        Slot_lbl = new javax.swing.JLabel();
        Slot_RB = new javax.swing.JRadioButton();
        SlotChooser = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        String_text_field = new javax.swing.JTextField();
        case_chooser_CB = new javax.swing.JComboBox();
        LexiconChooser = new javax.swing.JButton();
        LexEntry = new javax.swing.JTextField();
        Case_lbl = new javax.swing.JLabel();
        voice_chooser = new javax.swing.JComboBox();
        tense_chooser = new javax.swing.JComboBox();
        voice_lbl = new javax.swing.JLabel();
        tense_lbl = new javax.swing.JLabel();
        TypeCmb = new javax.swing.JComboBox();
        Typelbl = new javax.swing.JLabel();
        String_text_field_plural = new javax.swing.JTextField();
        plural_lbl = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        Filler_RB = new javax.swing.JRadioButton();
        String_RB = new javax.swing.JRadioButton();
        Verb_RB = new javax.swing.JRadioButton();
        Referring_Expr_To_Owner_RB = new javax.swing.JRadioButton();
        lexic = new javax.swing.JRadioButton();
        preposition = new javax.swing.JRadioButton();
        adverb = new javax.swing.JRadioButton();

        setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        setLayout(new java.awt.BorderLayout());

        SlotSelectionPanel.setLayout(new java.awt.GridBagLayout());

        Slot_lbl.setFont(new java.awt.Font("Dialog", 1, 11));
        Slot_lbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Slot_lbl.setText("Slot");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 11, 0, 7);
        SlotSelectionPanel.add(Slot_lbl, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        SlotSelectionPanel.add(Slot_RB, gridBagConstraints);

        add(SlotSelectionPanel, java.awt.BorderLayout.WEST);

        SlotChooser.setLayout(new java.awt.BorderLayout());

        jPanel1.setLayout(new java.awt.GridBagLayout());

        String_text_field.setMinimumSize(new java.awt.Dimension(150, 20));
        String_text_field.setPreferredSize(new java.awt.Dimension(150, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel1.add(String_text_field, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel1.add(case_chooser_CB, gridBagConstraints);

        LexiconChooser.setFont(new java.awt.Font("Dialog", 1, 11));
        LexiconChooser.setText("choose class");
        LexiconChooser.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                LexiconChooserActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel1.add(LexiconChooser, gridBagConstraints);

        LexEntry.setEditable(false);
        LexEntry.setMinimumSize(new java.awt.Dimension(150, 20));
        LexEntry.setPreferredSize(new java.awt.Dimension(150, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        jPanel1.add(LexEntry, gridBagConstraints);

        Case_lbl.setText("Case: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel1.add(Case_lbl, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 3, 4, 3);
        jPanel1.add(voice_chooser, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 3, 4, 3);
        jPanel1.add(tense_chooser, gridBagConstraints);

        voice_lbl.setFont(new java.awt.Font("Dialog", 0, 11));
        voice_lbl.setText("voice");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 3, 4, 3);
        jPanel1.add(voice_lbl, gridBagConstraints);

        tense_lbl.setFont(new java.awt.Font("Dialog", 0, 11));
        tense_lbl.setText("tense");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 3, 4, 3);
        jPanel1.add(tense_lbl, gridBagConstraints);

        TypeCmb.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Auto", "Pronoun", "Name", "definite article", "indefinite article", "demostrative" }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel1.add(TypeCmb, gridBagConstraints);

        Typelbl.setText("Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        jPanel1.add(Typelbl, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel1.add(String_text_field_plural, gridBagConstraints);

        plural_lbl.setText(" plural");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel1.add(plural_lbl, gridBagConstraints);

        SlotChooser.add(jPanel1, java.awt.BorderLayout.CENTER);

        jPanel2.setLayout(new java.awt.GridBagLayout());

        optionsGroup.add(Filler_RB);
        Filler_RB.setFont(new java.awt.Font("Dialog", 1, 11));
        Filler_RB.setText("property value");
        Filler_RB.addItemListener(new java.awt.event.ItemListener()
        {
            public void itemStateChanged(java.awt.event.ItemEvent evt)
            {
                Filler_RBItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 146;
        jPanel2.add(Filler_RB, gridBagConstraints);

        optionsGroup.add(String_RB);
        String_RB.setFont(new java.awt.Font("Dialog", 1, 11));
        String_RB.setText("string");
        String_RB.addItemListener(new java.awt.event.ItemListener()
        {
            public void itemStateChanged(java.awt.event.ItemEvent evt)
            {
                String_RBItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel2.add(String_RB, gridBagConstraints);

        optionsGroup.add(Verb_RB);
        Verb_RB.setFont(new java.awt.Font("Dialog", 1, 11));
        Verb_RB.setText("verb");
        Verb_RB.addItemListener(new java.awt.event.ItemListener()
        {
            public void itemStateChanged(java.awt.event.ItemEvent evt)
            {
                Verb_RBItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel2.add(Verb_RB, gridBagConstraints);

        optionsGroup.add(Referring_Expr_To_Owner_RB);
        Referring_Expr_To_Owner_RB.setFont(new java.awt.Font("Dialog", 1, 11));
        Referring_Expr_To_Owner_RB.setText("property owner");
        Referring_Expr_To_Owner_RB.addItemListener(new java.awt.event.ItemListener()
        {
            public void itemStateChanged(java.awt.event.ItemEvent evt)
            {
                Referring_Expr_To_Owner_RBItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel2.add(Referring_Expr_To_Owner_RB, gridBagConstraints);

        optionsGroup.add(lexic);
        lexic.setFont(new java.awt.Font("Dialog", 1, 11));
        lexic.setText("class descriptor");
        lexic.addItemListener(new java.awt.event.ItemListener()
        {
            public void itemStateChanged(java.awt.event.ItemEvent evt)
            {
                lexicItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel2.add(lexic, gridBagConstraints);

        optionsGroup.add(preposition);
        preposition.setFont(new java.awt.Font("Dialog", 1, 11));
        preposition.setText("preposition");
        preposition.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        preposition.setMargin(new java.awt.Insets(0, 0, 0, 0));
        preposition.addItemListener(new java.awt.event.ItemListener()
        {
            public void itemStateChanged(java.awt.event.ItemEvent evt)
            {
                prepositionItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel2.add(preposition, gridBagConstraints);

        optionsGroup.add(adverb);
        adverb.setFont(new java.awt.Font("Dialog", 1, 11));
        adverb.setText("adverb");
        adverb.addItemListener(new java.awt.event.ItemListener()
        {
            public void itemStateChanged(java.awt.event.ItemEvent evt)
            {
                adverbItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel2.add(adverb, gridBagConstraints);

        SlotChooser.add(jPanel2, java.awt.BorderLayout.WEST);

        add(SlotChooser, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void prepositionItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_prepositionItemStateChanged
        hideAll();
        
        String_text_field.setVisible(true);
        
        this.revalidate();
        this.repaint();
    }//GEN-LAST:event_prepositionItemStateChanged

    public void set_prepositionItemStateChanged(String text)
    {
        hideAll();
        preposition.setSelected(true);
        String_text_field.setVisible(true);
        String_text_field.setText(text);
        
        this.revalidate();
        this.repaint();
    }
    
    private void LexiconChooserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LexiconChooserActionPerformed
       
        //ClsDescChooser CDC = ClsDescChooser.showLexiconChooser(this, TAOT.getProj());
        //System.out.println("CDC===>" + CDC.getSelectedItem());
    }//GEN-LAST:event_LexiconChooserActionPerformed
        
    
    private void lexicItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_lexicItemStateChanged
       hideAll();
       //LexiconCombo.setVisible(true);
       LexiconChooser.setVisible(true);
       LexEntry.setVisible(true);           
       
    }//GEN-LAST:event_lexicItemStateChanged
    public void set_lexicItemStateChanged(String str) {
       hideAll();       
       LexiconChooser.setVisible(true);
       LexEntry.setVisible(true);    
       LexEntry.setText(str);
       
    }
    private void Verb_RBItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_Verb_RBItemStateChanged
        hideAll();
        if(Verb_RB.isSelected())
        {
            String_text_field.setVisible(true);
            tense_chooser.setVisible(true);
            voice_chooser.setVisible(true);

            String_text_field_plural.setVisible(true);
            plural_lbl.setVisible(true);
        
            tense_lbl.setVisible(true);
            voice_lbl.setVisible(true);                       
        }
        
        this.revalidate();
        this.repaint();
    }//GEN-LAST:event_Verb_RBItemStateChanged
    public void set_Verb_RBItemStateChanged(String verb, String verb_plural,String voice, String tense){
        hideAll();
        Verb_RB.setSelected(true);
        
        String_text_field.setVisible(true);
        String_text_field.setText(verb);
        
        String_text_field_plural.setVisible(true);
        plural_lbl.setVisible(true);
        String_text_field_plural.setText(verb_plural);
        
        tense_chooser.setVisible(true);
        tense_chooser.setSelectedItem(tense);
        voice_chooser.setVisible(true);
        voice_chooser.setSelectedItem(voice);
        
        tense_lbl.setVisible(true);
        voice_lbl.setVisible(true);
        
        this.revalidate();
        this.repaint();
    }
        
    private void Referring_Expr_To_Owner_RBItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_Referring_Expr_To_Owner_RBItemStateChanged
        hideAll();
        
        if(Referring_Expr_To_Owner_RB.isSelected())
        {
            case_chooser_CB.setVisible(true);            
            Case_lbl.setVisible(true);
            
            Typelbl.setVisible(true);
            TypeCmb.setVisible(true);
        }
        
        this.revalidate();
        this.repaint();   
    }//GEN-LAST:event_Referring_Expr_To_Owner_RBItemStateChanged
    
    public void set_Referring_Expr_To_Owner_RB(String c, String t)
    {
        hideAll();
        
        Referring_Expr_To_Owner_RB.setSelected(true);
        Case_lbl.setVisible(true);
        case_chooser_CB.setVisible(true);            
        Typelbl.setVisible(true);
        TypeCmb.setVisible(true);
       
         
        case_chooser_CB.setSelectedItem(c);
        setREType(t);
                
        this.revalidate();
        this.repaint();     
    }
    
    private void Filler_RBItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_Filler_RBItemStateChanged
        hideAll();
        
        if(Filler_RB.isSelected())
        {
            case_chooser_CB.setVisible(true);
            Case_lbl.setVisible(true);
            Typelbl.setVisible(true);
            TypeCmb.setVisible(true);
        }
        
        this.revalidate();
        this.repaint();
    }//GEN-LAST:event_Filler_RBItemStateChanged
    
    public void set_Filler_RBItemStateChanged(String c, String t)
    {
        hideAll();

        Filler_RB.setSelected(true);
        
        Case_lbl.setVisible(true);
        case_chooser_CB.setVisible(true);
        Typelbl.setVisible(true);
        TypeCmb.setVisible(true);
            
        case_chooser_CB.setSelectedItem(c);
        setREType(t);
                                
        this.revalidate();
        this.repaint();
    }
    
    private void String_RBItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_String_RBItemStateChanged
        hideAll();
        
        if(String_RB.isSelected())
        {
           String_text_field.setVisible(true);
        }
        
        this.revalidate();
        this.repaint();
    }//GEN-LAST:event_String_RBItemStateChanged
    
    public void set_StringRBItemStateChanged(String text)
    {
        hideAll();
        String_RB.setSelected(true);
        String_text_field.setVisible(true);
        String_text_field.setText(text);
        
        this.revalidate();
        this.repaint();
    }
    
    private void adverbItemStateChanged(java.awt.event.ItemEvent evt)//GEN-FIRST:event_adverbItemStateChanged
    {//GEN-HEADEREND:event_adverbItemStateChanged
        hideAll();
        //adverb.setSelected(true);
        String_text_field.setVisible(true);
        
        this.revalidate();
        this.repaint();
    }//GEN-LAST:event_adverbItemStateChanged
     
    public void set_AdverbItemStateChanged(String text)
    {
        hideAll();
        adverb.setSelected(true);
        String_text_field.setVisible(true);
        String_text_field.setText(text);
        
        this.revalidate();
        this.repaint();
    }
    
    public void hideAll()
    {
        String_text_field.setVisible(false);
        String_text_field_plural.setVisible(false);
        plural_lbl.setVisible(false);
        
        case_chooser_CB.setVisible(false);
        LexiconChooser.setVisible(false);
        LexEntry.setVisible(false);
        Case_lbl.setVisible(false);
        
        tense_chooser.setVisible(false);
        voice_chooser.setVisible(false);
        
        tense_lbl.setVisible(false);
        voice_lbl.setVisible(false);
        
        Typelbl.setVisible(false);
        TypeCmb.setVisible(false);
    } 
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Case_lbl;
    private javax.swing.JRadioButton Filler_RB;
    private javax.swing.JTextField LexEntry;
    private javax.swing.JButton LexiconChooser;
    private javax.swing.JRadioButton Referring_Expr_To_Owner_RB;
    private javax.swing.JPanel SlotChooser;
    private javax.swing.JPanel SlotSelectionPanel;
    private javax.swing.JRadioButton Slot_RB;
    private javax.swing.JLabel Slot_lbl;
    private javax.swing.JRadioButton String_RB;
    private javax.swing.JTextField String_text_field;
    private javax.swing.JTextField String_text_field_plural;
    private javax.swing.JComboBox TypeCmb;
    private javax.swing.JLabel Typelbl;
    private javax.swing.JRadioButton Verb_RB;
    private javax.swing.JRadioButton adverb;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox case_chooser_CB;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JRadioButton lexic;
    private javax.swing.ButtonGroup optionsGroup;
    private javax.swing.JLabel plural_lbl;
    private javax.swing.JRadioButton preposition;
    private javax.swing.JComboBox tense_chooser;
    private javax.swing.JLabel tense_lbl;
    private javax.swing.JComboBox voice_chooser;
    private javax.swing.JLabel voice_lbl;
    // End of variables declaration//GEN-END:variables
    //------------------------------------------------
    
    
    public boolean is_OwnerRB_Selected()
    {
        return Referring_Expr_To_Owner_RB.isSelected();
    }
    
    public boolean is_fillerRB_Selected()
    {
        return this.Filler_RB.isSelected();
    }
    
    public boolean is_StringRB_Selected()
    {
        return this.String_RB.isSelected();
    }
    
    public boolean is_preposition_Selected()
    {
        return this.preposition.isSelected();
    }
    
    public boolean is_Adverb_Selected()
    {
        return this.adverb.isSelected();
    }
    
    public boolean is_verbRB_Selected()
    {
        return this.Verb_RB.isSelected();
    } 
    
    
    public boolean is_ClsDescRB_Selected()
    {
        return this.lexic.isSelected();
    } 
    //------------------------------------------------
    
    public String get_Case()
    {
        return this.case_chooser_CB.getSelectedItem().toString();
    }
    
    public String get_String()
    {
        return this.String_text_field.getText();
    }
    
    public String get_Verb()
    {
        return this.String_text_field.getText();
    }
    
    public String get_Verb_plural()
    {
        return this.String_text_field_plural.getText();
    }
        
    public String get_Voice()
    {
        return this.voice_chooser.getSelectedItem().toString();
    }    
    
    public String get_Tense()
    {
        return this.tense_chooser.getSelectedItem().toString();
    }        
    
    public String get_LexEntry()
    {
        return LexEntry.getText();
    }
        
}
