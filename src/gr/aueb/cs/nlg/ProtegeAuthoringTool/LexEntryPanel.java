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

import gr.aueb.cs.nlg.NLFiles.*;
import gr.aueb.cs.nlg.Languages.Languages;

import java.awt.*;
import javax.swing.*;


import org.apache.log4j.Logger;

public class LexEntryPanel extends JPanel 
{
    
    static Logger logger = Logger.getLogger(LexEntryPanel.class.getName());

    public JPanel P;  
    public JPanel GeneralPanel;  
    public JPanel InternalPanel;
    public LanguageChooser LC; 
    public String projectName;
    
    LexiconTab LBT;    
    
    public LexEntryPanel(String projectName, JPanel headerComp, LexiconTab LBT) 
    {
        try
        {             
            this.LBT = LBT;
            initComponents();    
            this.projectName = projectName;

            //NLPlugin.loadNLResources(projectName);
            
            add(headerComp, BorderLayout.NORTH);
            InternalPanel = new JPanel();
            InternalPanel.setLayout(new BorderLayout());
            //!add(InternalPanel, BorderLayout.CENTER);
            add(InternalPanel, BorderLayout.CENTER);
                        
            GeneralPanel = new NounPanelEn(this.LBT);            
            InternalPanel.add(GeneralPanel);                            
                      
        }
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }    
    
    public JPanel getGeneralPanel()
    {        
        return GeneralPanel;        
    }
    
    public void setGeneralPanel(JPanel GP)
    {        
         GeneralPanel = GP;        
    }
      
    public void setInternalPanel(JPanel IP)
    {        
         InternalPanel = IP;        
    }
    
    public JPanel getInternalPanel()
    {        
        return InternalPanel;        
    }
    
    public LanguageChooser getLanguageChooser()
    {        
        return LC;        
    }
    
    public void saveNPToLexicon(String namespace, String selectedRes)
    {
        if(GeneralPanel instanceof NounPanelGr)
        {
            
            NounPanelGr NPGR = (NounPanelGr)GeneralPanel;
            NPGR.setNoun(selectedRes);      
            
            Lex_Entry_GR entry = new Lex_Entry_GR(NPGR.get_gender(), NPGR.get_def_num() ,NPGR.get_countable(), type);
            
            if(NPGR.isInflected())
            {   
                //System.err.println("*********************");
                entry.setInflected(true);
                entry.set_singular_cases(NPGR.get_SN(), NPGR.get_SG(), NPGR.get_SA());
                entry.set_plural_cases(NPGR.get_PN(), NPGR.get_PG(), NPGR.get_PA());
            }
            else
            {
                //System.err.println("+++++++++++++++++++++++");
                entry.setInflected(false);
                entry.set_singular_cases(NPGR.getInflected(), "", "");
                entry.set_plural_cases("", "", "");
            }
            
            NLPlugin.getLexicon().saveNPToLexicon(namespace, selectedRes, entry);
            //NLPlugin.getGreekLexicon().saveLexicon(namespace + selectedNoun, entry);            
            logger.debug("save -- " + namespace + selectedRes);
        }
        else if(GeneralPanel instanceof NounPanelEn)
        {
            
            NounPanelEn NPEN = (NounPanelEn)GeneralPanel;
            NPEN.setNoun(selectedRes);
            
            Lex_Entry_EN entry = new Lex_Entry_EN(NPEN.get_gender(), NPEN.get_def_num(),NPEN.get_countable(), type );
            entry.set_sing_plural(NPEN.get_singular_form(),NPEN.get_plural_form());
            
            NLPlugin.getLexicon().saveNPToLexicon(namespace,selectedRes,entry);
            //NLPlugin.getEnglishLexicon().saveLexicon(namespace + selectedNoun, entry);
            logger.debug("save -- " + namespace + selectedRes);
        }   

    }
     
    
    public void saveCTToLexicon(String namespace, String selectedRes, String lang)
    {
        if(GeneralPanel instanceof CannedTextsPanel)
        {
            /*
            CannedTextPanel CTP = (CannedTextPanel)GeneralPanel;
            
            String CannedTextID = CTP.getSelectedCannedID();
            
            if(CannedTextID != null)
            {
                CannedList CL = (CannedList)NLPlugin.getLexicon().getNLRes(CannedTextID, 2);

                CL.setFOCUS_LOST(CTP.getFocusLost());   // set focus
                CL.setFillerAggregationAllowed(CTP.getAggregationAllowed()); // set aggregation allowed

                CL.setCannedText(CTP.getCannedText().replaceAll("\n",""), lang); // set canned
                CL.removeUserTypes();

                Vector<String> utypes = CTP.getUserTypes();
                
                for(int i = 0; i < utypes.size(); i++)
                {
                    CL.addUserType(utypes.get(i));
                }
                
                NLPlugin.getLexicon().saveCTToLexicon(namespace, selectedRes, CL, CannedTextID);
            }
             */
            
        }        
    }

    
    private void initComponents() {//GEN-BEGIN:initComponents
        LanguagesButtonGroup = new javax.swing.ButtonGroup();

        setLayout(new java.awt.BorderLayout());

    }//GEN-END:initComponents
    
    public int type ;
    
    public void setEntry(String namespace, String LexResource, int t, String lang)
    {
        type = t;
        
        if(GeneralPanel instanceof NounPanelGr)
        {
            logger.debug("NounPanelGr" + LexResource);
            NounPanelGr NPGR = (NounPanelGr)GeneralPanel;
            //NPGR.setNoun(LexResource);    
           
            
            if(NLPlugin.getLexicon() == null)
            {
                logger.debug("Lexicon is not found!!!");
                return;
            }
            else
            {
                logger.debug("Lexicon is  found!!!");
            }
            
            Object obj = NLPlugin.getLexicon().getEntry(namespace + LexResource,Languages.GREEK, null, 1);
            Lex_Entry_GR entry = null;
                    
            if(obj instanceof CannedList)
            {
                entry = null;
            }
            else
            {
                entry = (Lex_Entry_GR)obj;
            }
                    
            if(entry!=null)
            {
                logger.debug("Lexicon is  found!!!");
                NPGR.clear();
                //logger.debug("@" + entry.get(XmlMsgs.NOMINATIVE_TAG, XmlMsgs.SINGULAR));
                //NPGR.set_singular_form(entry.get(XmlMsgs.NOMINATIVE_TAG, XmlMsgs.SINGULAR));
                
                //logger.debug("@" + entry.get(XmlMsgs.NOMINATIVE_TAG, XmlMsgs.PLURAL));
                //NPGR.set_plural_form(entry.get(XmlMsgs.NOMINATIVE_TAG, XmlMsgs.PLURAL));
                NPGR.setInflectedView(entry.getInflected());
               
                logger.debug("@" + entry.get_Gender());
                NPGR.set_gender(entry.get_Gender());
                
                logger.debug("@" + entry.get_num());
                NPGR.set_def_num(entry.get_num());
                
                logger.debug("@" + entry.getCountable());
                NPGR.set_countable(entry.getCountable());
                
                
                NPGR.setAdvnancedSpellingOptions(entry);                
               
            } 
            else
            {
                logger.debug("Lexicon is  found but the entry was not found!!!");
                NPGR.clear();
            }
                        
        }
        else if(GeneralPanel instanceof NounPanelEn)
        {
            logger.debug("NounPanelEn" + LexResource);
            NounPanelEn NPEN = (NounPanelEn)GeneralPanel;
            //NPEN.setNoun(LexResource);            
            
            if(NLPlugin.getLexicon() == null)
            {
                logger.debug("Lexicon is not found!!!");
                return;
            }
            else
            {
                logger.debug("Lexicon is  found!!!");
            }
            
            Object obj = NLPlugin.getLexicon().getEntry(namespace + LexResource,Languages.ENGLISH, null ,1);
            Lex_Entry_EN entry = null;
                    
            if(obj instanceof CannedList)
            {
                entry = null;
            }
            else
            {
                entry = (Lex_Entry_EN)obj;
            }
                    
                                             
            if(entry!=null)
            {
                logger.debug("Lexicon is  found!!!");
                NPEN.clear();
                
                logger.debug("@" + entry.get_Singular());
                NPEN.set_singular_form(entry.get_Singular());
                
                logger.debug("@" + entry.get_Plural());
                NPEN.set_plural_form(entry.get_Plural());
                
                logger.debug("@" + entry.get_Gender());
                NPEN.set_gender(entry.get_Gender());
                
                logger.debug("@" + entry.get_num());
                NPEN.set_def_num(entry.get_num());
                
                logger.debug("@" + entry.getCountable());
                NPEN.set_countable(entry.getCountable());
                                             
            }
            else
            {
                logger.debug("Lexicon is found but the entry was not found!!!");
                NPEN.clear();
            }
        }
        else if(GeneralPanel instanceof CannedTextsPanel)
        {
            logger.debug("CannedPanel" + LexResource);
            CannedTextsPanel CTP = (CannedTextsPanel)GeneralPanel; 
            
            
            CTP.setLanguage(lang);     
            CTP.setResource(namespace, LexResource);
                               
            CTP.clear();
            CTP.setCannedTextsInfo(namespace + LexResource, lang);           

        }
    }
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup LanguagesButtonGroup;
    // End of variables declaration//GEN-END:variables
    public static void main(String args[]){
        /*
        JPanel headerPanel = new JPanel();
        JLabel jlbl = new JLabel("Header");
        headerPanel.add(jlbl);
       
        NounPanel NP = new NounPanel(headerPanel);
        JFrame JF = new JFrame();
        //JF.setLayout(new java.awt.BorderLayout());
        JF.add(NP);
        JF.show();
         */
    }
}
