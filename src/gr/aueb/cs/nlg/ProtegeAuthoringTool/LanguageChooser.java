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

public class LanguageChooser extends JPanel 
{
    public LanguageChooser() 
    {
        initComponents();
    }

    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        LangButtonGroup = new javax.swing.ButtonGroup();
        chooseLang_label = new javax.swing.JLabel();
        English = new javax.swing.JRadioButton();
        Greek = new javax.swing.JRadioButton();

        setLayout(new java.awt.GridBagLayout());

        chooseLang_label.setFont(new java.awt.Font("Verdana", 1, 10));
        chooseLang_label.setText("Choose Language");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 15);
        add(chooseLang_label, gridBagConstraints);

        English.setFont(new java.awt.Font("Verdana", 1, 10));
        English.setSelected(true);
        English.setText("English");
        LangButtonGroup.add(English);
        English.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                EnglishItemStateChanged(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 15);
        add(English, gridBagConstraints);

        Greek.setFont(new java.awt.Font("Verdana", 1, 10));
        Greek.setText("Greek");
        LangButtonGroup.add(Greek);
        Greek.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                GreekItemStateChanged(evt);
            }
        });

        add(Greek, new java.awt.GridBagConstraints());

    }//GEN-END:initComponents

    private void EnglishItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_EnglishItemStateChanged
        if(English.isSelected()){
            transmitChange("en");
        }
        else if(Greek.isSelected()){
            transmitChange("gr");
        }
    }//GEN-LAST:event_EnglishItemStateChanged

    private void GreekItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_GreekItemStateChanged
        if(English.isSelected()){
            transmitChange("en");
        }
        else if(Greek.isSelected()){
            transmitChange("gr");
        }
    }//GEN-LAST:event_GreekItemStateChanged
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton English;
    private javax.swing.JRadioButton Greek;
    private javax.swing.ButtonGroup LangButtonGroup;
    private javax.swing.JLabel chooseLang_label;
    // End of variables declaration//GEN-END:variables
    
    public void transmitChange(String langSelected){

    }

}
