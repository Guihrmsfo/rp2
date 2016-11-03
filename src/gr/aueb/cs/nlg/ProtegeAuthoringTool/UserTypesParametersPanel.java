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

import javax.swing.JFrame;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.border.*;
import javax.swing.event.*;

import gr.aueb.cs.nlg.NLFiles.*;

public class UserTypesParametersPanel extends javax.swing.JPanel implements ActionListener
{
    
    private UserModellingQueryManager UMQM;
            
    public UserTypesParametersPanel(UserModellingQueryManager UMQM) 
    {
        this.UMQM = UMQM;        
                
        initComponents();
        
        SynthesizerVoiceCmb.setVisible(false);
        SynthesizerVoiceLbl.setVisible(false);
        LinksPerPageCmb.setVisible(false);
        LinksPerPageLbl.setVisible(false); 
        LangLbl.setVisible(false);
        LangCmb.setVisible(false);
                
        MaxFactsPerSentCmb.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", "4" }));        
        SynthesizerVoiceCmb.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "male", "female" }));
        
        for(int i = 0; i < 50; i++)
        LinksPerPageCmb.addItem(i + 1 +"");
        
        for(int i = 0; i < 100; i++)
        FactsPerPageCmb.addItem(i + 1 +"");
               
        MaxFactsPerSentCmb.addActionListener(UserTypesParametersPanel.this);
        SynthesizerVoiceCmb.addActionListener(UserTypesParametersPanel.this);
        LinksPerPageCmb.addActionListener(UserTypesParametersPanel.this);
        FactsPerPageCmb.addActionListener(UserTypesParametersPanel.this);
        LangCmb.addActionListener(UserTypesParametersPanel.this);
        
       
    }
    
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents()
    {
        UserTypeLbl = new javax.swing.JLabel();
        MaxFactsPerSentCmb = new javax.swing.JComboBox();
        FactsPerPageCmb = new javax.swing.JComboBox();
        LinksPerPageCmb = new javax.swing.JComboBox();
        SynthesizerVoiceCmb = new javax.swing.JComboBox();
        MaxFactsPerSentLbl = new javax.swing.JLabel();
        FactsPerPageLbl = new javax.swing.JLabel();
        LinksPerPageLbl = new javax.swing.JLabel();
        SynthesizerVoiceLbl = new javax.swing.JLabel();
        LangLbl = new javax.swing.JLabel();
        LangCmb = new javax.swing.JComboBox();

        setLayout(null);

        UserTypeLbl.setFont(new java.awt.Font("Tahoma", 1, 11));
        UserTypeLbl.setText("jLabel1");
        add(UserTypeLbl);
        UserTypeLbl.setBounds(20, 20, 137, 14);

        add(MaxFactsPerSentCmb);
        MaxFactsPerSentCmb.setBounds(290, 80, 70, 20);

        add(FactsPerPageCmb);
        FactsPerPageCmb.setBounds(290, 50, 70, 20);

        add(LinksPerPageCmb);
        LinksPerPageCmb.setBounds(290, 180, 70, 20);

        add(SynthesizerVoiceCmb);
        SynthesizerVoiceCmb.setBounds(290, 150, 70, 20);

        MaxFactsPerSentLbl.setText("Maximum facts per sentence ");
        MaxFactsPerSentLbl.setMaximumSize(new java.awt.Dimension(180, 14));
        MaxFactsPerSentLbl.setPreferredSize(new java.awt.Dimension(180, 14));
        add(MaxFactsPerSentLbl);
        MaxFactsPerSentLbl.setBounds(20, 80, 180, 14);

        FactsPerPageLbl.setText("Facts per  page ");
        FactsPerPageLbl.setMaximumSize(new java.awt.Dimension(180, 14));
        FactsPerPageLbl.setPreferredSize(new java.awt.Dimension(180, 14));
        add(FactsPerPageLbl);
        FactsPerPageLbl.setBounds(20, 50, 137, 14);

        LinksPerPageLbl.setText("Links per page ");
        LinksPerPageLbl.setMaximumSize(new java.awt.Dimension(180, 14));
        LinksPerPageLbl.setPreferredSize(new java.awt.Dimension(180, 14));
        add(LinksPerPageLbl);
        LinksPerPageLbl.setBounds(20, 180, 137, 14);

        SynthesizerVoiceLbl.setText("Synthesizer voice ");
        SynthesizerVoiceLbl.setMaximumSize(new java.awt.Dimension(180, 14));
        SynthesizerVoiceLbl.setPreferredSize(new java.awt.Dimension(180, 14));
        add(SynthesizerVoiceLbl);
        SynthesizerVoiceLbl.setBounds(20, 150, 137, 14);

        LangLbl.setText("Language");
        add(LangLbl);
        LangLbl.setBounds(20, 110, 70, 14);

        LangCmb.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "English", "Greek", "All" }));
        add(LangCmb);
        LangCmb.setBounds(290, 110, 70, 20);

    }// </editor-fold>//GEN-END:initComponents

        
    public void actionPerformed(ActionEvent e) 
    {
        if (e.getSource() == MaxFactsPerSentCmb)
        {
            saveUserTypeParameters();
        }
        else if (e.getSource() == FactsPerPageCmb) 
        {
            saveUserTypeParameters();
        }
        else if (e.getSource() == LinksPerPageCmb) 
        {
            saveUserTypeParameters();
        }
        else if (e.getSource() == SynthesizerVoiceCmb) 
        {
            saveUserTypeParameters();
        }     
        else if (e.getSource() == LangCmb) 
        {
            saveUserTypeParameters();
        }
            
    }
    
    public void saveUserTypeParameters()
    {
        //System.err.println("Saving UT Params " + this.getUserType());
        UserTypeParameters UTP = new UserTypeParameters(getMaxFactsPerSent(), getSynthesizerVoice(), getFactsPerPage(), this.getLang() );
        NLPlugin.getUserModellingQueryManager().setParametersForUserType(NLPlugin.getUserModellingQueryManager().NLGUserModellingNS + getUserType(),UTP);     
    }
        
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox FactsPerPageCmb;
    private javax.swing.JLabel FactsPerPageLbl;
    private javax.swing.JComboBox LangCmb;
    private javax.swing.JLabel LangLbl;
    private javax.swing.JComboBox LinksPerPageCmb;
    private javax.swing.JLabel LinksPerPageLbl;
    private javax.swing.JComboBox MaxFactsPerSentCmb;
    private javax.swing.JLabel MaxFactsPerSentLbl;
    private javax.swing.JComboBox SynthesizerVoiceCmb;
    private javax.swing.JLabel SynthesizerVoiceLbl;
    private javax.swing.JLabel UserTypeLbl;
    // End of variables declaration//GEN-END:variables
    
    public int getFactsPerPage()
    {        
        return Integer.parseInt(FactsPerPageCmb.getSelectedItem().toString());
    }
    
    public int getLinksPerPage()
    {        
        return Integer.parseInt(LinksPerPageCmb.getSelectedItem().toString());
    }
    
    public int getMaxFactsPerSent()
    {       
        return Integer.parseInt(MaxFactsPerSentCmb.getSelectedItem().toString());
    }
    
    public String getSynthesizerVoice()
    {
         return SynthesizerVoiceCmb.getSelectedItem().toString();
    }
    
    public String getLang()
    {
         String llllang = LangCmb.getSelectedItem().toString();
         return llllang;
    }
        
    public void setFactsPerPage(int FPP)
    {
        FactsPerPageCmb.setSelectedItem(FPP +"");
    }
    
    public void setLinksPerPage(int LPP)
    {
        LinksPerPageCmb.setSelectedItem(LPP +"");
    }
    
    public void setMaxFactsPerSent(int MFPS)
    {
        MaxFactsPerSentCmb.setSelectedItem(MFPS +"");
    }
    
    public void setSynthesizerVoice(String SV)
    {
        SynthesizerVoiceCmb.setSelectedItem(SV);
    }    

    public void setLang(String lang)
    {
        LangCmb.setSelectedItem(lang);
    } 
        
    public void setUserType(String title)
    {
        UserTypeLbl.setText(title);
    }
     
    public String getUserType()
    {
        return UserTypeLbl.getText();
    }
    
    public void setParameters(int FPP,int LPP, int MFPS,String SV, String lang)
    {
        setFactsPerPage(FPP);
        setMaxFactsPerSent(MFPS);
        setLinksPerPage(LPP);
        setSynthesizerVoice(SV);
        setLang(lang);
    }
    
    public void setVisibleParameters(boolean vis)
    {
        FactsPerPageCmb.setVisible(vis);
        FactsPerPageLbl.setVisible(vis);
        //LinksPerPageCmb.setVisible(vis);
        //LinksPerPageLbl.setVisible(vis);
        MaxFactsPerSentCmb.setVisible(vis);
        MaxFactsPerSentLbl.setVisible(vis);
        //SynthesizerVoiceCmb.setVisible(vis);
        //SynthesizerVoiceLbl.setVisible(vis);    
        LangLbl.setVisible(vis);
        LangCmb.setVisible(vis);
    }
    
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
        
        
                
        UserTypesParametersPanel UTPP = new UserTypesParametersPanel(null);
        testFrame.setLayout(new java.awt.BorderLayout());
        testFrame.add(UTPP);
        testFrame.show();        
    }     
}
