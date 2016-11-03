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
import java.io.*;

import org.apache.log4j.Logger;

public class NLFilesPanel extends JPanel 
{
    static Logger logger = Logger.getLogger(NLFilesPanel.class.getName());
    
    private File NLFile ;    
    private WaitDialog wdlg;
            
    public NLFilesPanel() 
    {        
        initComponents(); 
        //System.err.println("NLFilesPanel:" + NLPlugin.getNLFilesPath());
        NLResourcesPath.setText(NLPlugin.getNLFilesPath());
        
        ReadModelAndNLInfoBtn.setEnabled(false);
        SaveNLResourcesBtn.setEnabled(false);
        jProgressBar1.setVisible(false);
        

    }
    
    public void setDialog(WaitDialog wdlg)
    {
        this.wdlg = wdlg;
    }
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel2 = new javax.swing.JPanel();
        NLResources_lbl = new javax.swing.JLabel();
        NLResourcesPath = new javax.swing.JTextField();
        SelectNLFilesPath = new javax.swing.JButton();
        SaveNLResourcesBtn = new javax.swing.JButton();
        ReadModelAndNLInfoBtn = new javax.swing.JButton();
        LexCB = new javax.swing.JCheckBox();
        MicroCB = new javax.swing.JCheckBox();
        UmCB = new javax.swing.JCheckBox();
        jProgressBar1 = new javax.swing.JProgressBar();
        UnloadBtn = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        setMaximumSize(new java.awt.Dimension(143, 403));
        setMinimumSize(new java.awt.Dimension(143, 403));
        setPreferredSize(new java.awt.Dimension(143, 303));
        jPanel2.setLayout(new java.awt.GridBagLayout());

        NLResources_lbl.setFont(new java.awt.Font("Dialog", 0, 11));
        NLResources_lbl.setText("NL Resources path");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(17, 5, 5, 5);
        jPanel2.add(NLResources_lbl, gridBagConstraints);

        NLResourcesPath.setEditable(false);
        NLResourcesPath.setPreferredSize(new java.awt.Dimension(300, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(NLResourcesPath, gridBagConstraints);

        SelectNLFilesPath.setFont(new java.awt.Font("Dialog", 0, 11));
        SelectNLFilesPath.setText("Select NL Files path");
        SelectNLFilesPath.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SelectNLFilesPathActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(SelectNLFilesPath, gridBagConstraints);

        SaveNLResourcesBtn.setFont(new java.awt.Font("Dialog", 0, 11));
        SaveNLResourcesBtn.setText("Save NL Resources");
        SaveNLResourcesBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SaveNLResourcesBtnActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(SaveNLResourcesBtn, gridBagConstraints);

        ReadModelAndNLInfoBtn.setFont(new java.awt.Font("Dialog", 0, 11));
        ReadModelAndNLInfoBtn.setText("Load  NL Resources");
        ReadModelAndNLInfoBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ReadModelAndNLInfoBtnActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(ReadModelAndNLInfoBtn, gridBagConstraints);

        LexCB.setFont(new java.awt.Font("Dialog", 0, 11));
        LexCB.setText("Lexicon");
        LexCB.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        LexCB.setMargin(new java.awt.Insets(0, 0, 0, 0));
        LexCB.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                LexCBItemStateChanged(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(LexCB, gridBagConstraints);

        MicroCB.setFont(new java.awt.Font("Dialog", 0, 11));
        MicroCB.setText("Microplans");
        MicroCB.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        MicroCB.setMargin(new java.awt.Insets(0, 0, 0, 0));
        MicroCB.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                MicroCBItemStateChanged(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(MicroCB, gridBagConstraints);

        UmCB.setFont(new java.awt.Font("Dialog", 0, 11));
        UmCB.setText("UserModelling");
        UmCB.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        UmCB.setMargin(new java.awt.Insets(0, 0, 0, 0));
        UmCB.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                UmCBItemStateChanged(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(UmCB, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jProgressBar1, gridBagConstraints);

        UnloadBtn.setText("Unload NL Resources");
        UnloadBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UnloadBtnActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(UnloadBtn, gridBagConstraints);

        add(jPanel2, java.awt.BorderLayout.NORTH);

    }// </editor-fold>//GEN-END:initComponents

    private void UnloadBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UnloadBtnActionPerformed
        NLPlugin.UnloadNLResources();
    }//GEN-LAST:event_UnloadBtnActionPerformed

    private void UmCBItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_UmCBItemStateChanged
        foo();
    }//GEN-LAST:event_UmCBItemStateChanged

    private void MicroCBItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_MicroCBItemStateChanged
        foo();
    }//GEN-LAST:event_MicroCBItemStateChanged

    private void LexCBItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_LexCBItemStateChanged
        foo();
    }//GEN-LAST:event_LexCBItemStateChanged


    public void setValues(boolean lex, boolean micro, boolean um, String path)
    {
        LexCB.setSelected(true);
        MicroCB.setSelected(true);
        UmCB.setSelected(true);
        
        NLResourcesPath.setText(path);
        logger.debug("@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        
        this.repaint();
        this.revalidate();
    }
            
    private void foo()
    {
                    
        if(LexCB.isSelected() || MicroCB.isSelected()|| UmCB.isSelected())
        {
            if(ReadModelAndNLInfoBtn_act)
            ReadModelAndNLInfoBtn.setEnabled(true);
            
            if(SaveNLResourcesBtn_act)
            SaveNLResourcesBtn.setEnabled(true);
        }
        else
        {
            ReadModelAndNLInfoBtn.setEnabled(false);
            SaveNLResourcesBtn.setEnabled(false);
        }
            
    }

    
    public void LoadingCompleted(boolean success, String success_msg, String error_msg, boolean showMessage)
    {
        if(showMessage)
        {
            if(success)
                JOptionPane.showMessageDialog(this, success_msg, success_msg + "", JOptionPane.PLAIN_MESSAGE); 
            else
                JOptionPane.showMessageDialog(this, error_msg, error_msg + "", JOptionPane.ERROR_MESSAGE); 
        }
    }
    

    // 
    public void EnableButtonsBeforeLoadingSaving(boolean b)
    {
        EnableButtons(b);
        
        wdlg.setWaitCursor(!b);        
        jProgressBar1.setVisible(!b);
        jProgressBar1.setIndeterminate(!b);                       
    }
    
    public void EnableButtons(boolean b)
    {
        LexCB.setEnabled(b);;
        MicroCB.setEnabled(b);;
        UmCB.setEnabled(b);
        
        SelectNLFilesPath.setEnabled(b);    
        
        if(b == false)
        {
            ReadModelAndNLInfoBtn.setEnabled(b);
            SaveNLResourcesBtn.setEnabled(b);
        }
        else
        {
           foo();           
        }
        
    }
    
    boolean SelectNLFilesPath_act;
    boolean ReadModelAndNLInfoBtn_act;
    boolean UnloadBtn_act;
    boolean SaveNLResourcesBtn_act;
        
    public void EnableButtons(boolean lex, boolean micro, boolean um, boolean select, boolean load, boolean unload, boolean save)
    {
        LexCB.setEnabled(lex);
        MicroCB.setEnabled(micro);
        UmCB.setEnabled(um);
        
        SelectNLFilesPath_act = select;
        ReadModelAndNLInfoBtn_act = load;
        UnloadBtn_act = unload;
        SaveNLResourcesBtn_act = save;
                
        SelectNLFilesPath.setEnabled(select);
        ReadModelAndNLInfoBtn.setEnabled(load);
        UnloadBtn.setEnabled(unload);
        SaveNLResourcesBtn.setEnabled(save);
        
        foo();
    }
    
    private void SaveNLResourcesBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SaveNLResourcesBtnActionPerformed
        SaveNLResources(true);
    }//GEN-LAST:event_SaveNLResourcesBtnActionPerformed

    public void SaveNLResources(boolean showMessage)
    {
       if(NLPlugin.getModel() != null)
       {
            //String path = NLFile.getAbsolutePath() + System.getProperty("file.separator");
                       
            EnableButtonsBeforeLoadingSaving(false);
            
            //System.err.println("@@@@" + path + " " + NLPlugin.getNLFilesPath() + System.getProperty("file.separator"));
            LoaderSaver LoaderSaver = new LoaderSaver(NLPlugin.getNLFilesPath() + System.getProperty("file.separator"), this ,LexCB.isSelected(), MicroCB.isSelected(), UmCB.isSelected(), 2 , showMessage);
            LoaderSaver.start(); 

       }
       else
       {
           logger.debug("NLPlugin.getModel() is null");
       }
    }
    private void ReadModelAndNLInfoBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ReadModelAndNLInfoBtnActionPerformed
        
        ReadModelAndNLInfo(true);
    }//GEN-LAST:event_ReadModelAndNLInfoBtnActionPerformed
        
    public void ReadModelAndNLInfo(boolean showMessage)
    {
        try
        {
            //if(NLFile.exists())
            //{                   
                //String path = NLFile.getAbsolutePath() + System.getProperty("file.separator"); 
                EnableButtonsBeforeLoadingSaving(false);
                
                //System.err.println("@@@@" + path + " " + NLPlugin.getNLFilesPath());
                LoaderSaver myLoaderSaver = new LoaderSaver( NLPlugin.getNLFilesPath() + System.getProperty("file.separator"), this ,LexCB.isSelected(), MicroCB.isSelected(), UmCB.isSelected(), 1, showMessage);
                myLoaderSaver.start();                
            //}
        }
        catch(Exception e)
        {            
            e.printStackTrace();
        }
    }
    
    private void SelectNLFilesPathActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SelectNLFilesPathActionPerformed
        JFileChooser chooser = new JFileChooser("../");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        
        int returnVal = chooser.showOpenDialog(this);
        
        String fileName = "";
        File myfile = null;
        
        if(returnVal == JFileChooser.APPROVE_OPTION) 
        {
            fileName = chooser.getSelectedFile().getName();
            myfile = chooser.getCurrentDirectory();
            
            NLResourcesPath.setText(myfile.getAbsolutePath()+ System.getProperty("file.separator") + fileName);
            NLPlugin.setNLFilesPath(myfile.getAbsolutePath()+ System.getProperty("file.separator") + fileName );
        }
        
        if(fileName.compareTo("")!=0)
        {
            NLFile = new File(myfile.getAbsolutePath()+ System.getProperty("file.separator") + fileName);
        }
        else
        {
            NLFile = null;
            NLResourcesPath.setText(NLPlugin.getNLFilesPath());
        }        
        
    }//GEN-LAST:event_SelectNLFilesPathActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox LexCB;
    private javax.swing.JCheckBox MicroCB;
    private javax.swing.JTextField NLResourcesPath;
    private javax.swing.JLabel NLResources_lbl;
    private javax.swing.JButton ReadModelAndNLInfoBtn;
    private javax.swing.JButton SaveNLResourcesBtn;
    private javax.swing.JButton SelectNLFilesPath;
    private javax.swing.JCheckBox UmCB;
    private javax.swing.JButton UnloadBtn;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JProgressBar jProgressBar1;
    // End of variables declaration//GEN-END:variables
   
           
}

class LoaderSaver extends Thread 
{
    static Logger logger2 = Logger.getLogger(LoaderSaver.class.getName());
    
    public String path;
    public boolean b;

    public boolean LexCB;
    public boolean MicroCB;
    public boolean UmCB;
    public NLFilesPanel p;
    public int mode ;
    public boolean showMessage;
        
    LoaderSaver( String path, NLFilesPanel p, boolean LexCB,boolean MicroCB,boolean UmCB, int mode, boolean showMessage ) 
    {        
        this.showMessage = showMessage;
        this.path = path;
        this.LexCB = LexCB;
        this.MicroCB = MicroCB;
        this.UmCB = UmCB;
        this.p = p;
        this.mode = mode; // mode -> 1 
    }

    public void run() 
    {        
        try
        { 
            
            logger2.debug("start thread!!!!!!!!!!!!!");
            
            if(mode == 1)
            {
                logger2.info("Start loading files from directory: " + path);
                
                b = NLPlugin.loadNLResources(path, LexCB, MicroCB, UmCB);
                p.EnableButtonsBeforeLoadingSaving(true);            
                p.LoadingCompleted(b, "Finished Loading" ,"ERROR On Loading", showMessage);
                //logger2.info("Finished Loading " + b);
            }
            else if(mode == 2)
            {
                logger2.info("Start saving files to directory: " + path);
                
                b = NLPlugin.savingNLResources(path, LexCB, MicroCB, UmCB);
                p.EnableButtonsBeforeLoadingSaving(true);            
                p.LoadingCompleted(b, "Finished Saving", "ERROR on Saving", showMessage);                
                //logger2.info("Finished Saving" + b);
            }


            logger2.debug("stop thread!!!!!!!!!!!!!");
        }
        catch(Exception e)
        {
            p.EnableButtonsBeforeLoadingSaving(b);
            p.LoadingCompleted(false, "" , "ERROR", showMessage);
            logger2.debug("stop thread");    
            
            e.printStackTrace();
        }
    }
}
