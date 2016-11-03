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
import javax.swing.Action;

import java.util.Properties;

import gr.aueb.cs.nlg.NLFiles.*;

import edu.stanford.smi.protege.model.*;
import edu.stanford.smi.protege.plugin.*;
import edu.stanford.smi.protege.ui.*;
import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;

import com.hp.hpl.jena.ontology.*;

import org.apache.log4j.Logger;


public class NLPlugin extends AbstractProjectPlugin 
{
    static Logger logger = Logger.getLogger(NLPlugin.class.getName());		
    
    private Action a;
    private final static int MENU_POS = 5;
    
    // lexicon microplans user-modelling structures
    private static NLGLexiconQueryManager Lexicon;
    private static MicroplansAndOrderingQueryManager MicroAndOrder;
    private static UserModellingQueryManager UMQM;
    
    // the path of nl resourses (lexicon.rdf ...)                      
    private static String NLFilesPath="";
    
    private static ProjectMenuBar menuBar;                   
    private static JMenu NLGMenu;
    
    private static JMenuItem resetMenu;
    
    // get the path of nl resourses (lexicon.rdf ...)  
    public static String getNLFilesPath()
    {
        return NLFilesPath;
    }
    
    // set the path of nl resourses (lexicon.rdf ...)  
    public static void setNLFilesPath(String path)
    {
        NLFilesPath = path;
    }
    
    // set the path of nl resourses (lexicon.rdf ...)  
    public static void setNLFilesPath(java.net.URI myURI)
    {
        File f = new File(myURI);
        String RDFpath = f.getAbsolutePath();
        setNLFilesPath(RDFpath);
    }
            
    public void afterCreate(Project p) 
    {
        logger.debug("After create");   
    }

    public void afterLoad(Project p) 
    {
        init();        
        NLGMenu = null;
        
        logger.debug("After load");
    }
   
    // after the project is shown
    // the NL plugin must be loaded
    // and displayed
    public void afterShow(ProjectView view, ProjectToolBar toolBar, ProjectMenuBar menuBar) 
    {
        init();
        this.menuBar = menuBar;
        NLGMenu = null;
               
        if(!AllTabsAreHidden())
        {
            readInfoAndLoad();
        }
        
        logger.info("--" + ProjectManager.getProjectManager().getCurrentProject().getProjectDirectoryURI());
        
        setNLFilesPath(ProjectManager.getProjectManager().getCurrentProject().getProjectDirectoryURI());
        
        logger.info("After Show");
        
    }

    public ProjectMenuBar getMenuBar()
    {
        return menuBar;
    }

    // unload the nl resourses (lexicon.rdf ...)  
    public static void UnloadNLResources()
    {
        init();
        // get projectView
        ProjectView projectView = ProjectManager.getProjectManager().getCurrentProjectView();

        //get tabs
        UserModellingTab UMTab = (UserModellingTab)projectView.getTabByClassName(UserModellingTab.class.getName());
        LexiconTab  LexTab = (LexiconTab)projectView.getTabByClassName(LexiconTab.class.getName());
        MicroplansAndOrderingTab MicroPlansTab = (MicroplansAndOrderingTab)projectView.getTabByClassName(MicroplansAndOrderingTab.class.getName());
        TextPreviewsTab NLGTab = (TextPreviewsTab)projectView.getTabByClassName(TextPreviewsTab.class.getName());

        RefreshTabs(UMTab, MicroPlansTab, LexTab, NLGTab);
    }
          
    // show the "NLG" menu on the top Menu Bar of Protege
    public synchronized static void showMenu()
    {
        //if(NLGMenu == null && showNLGMenu)
        //{
            logger.debug("creating nlg menu");
            NLGMenu = new JMenu("NLG");
            MyAction myAct = new MyAction();
            NLGMenu.add(myAct);
            
            /*resetMenu = NLGMenu.add("reset");
            
            resetMenu.addActionListener (new ActionListener() 
            {
                public void actionPerformed (ActionEvent e) 
                {
                    
                }
            });
            */
                        
            boolean already_there = false;            
            for(int i = 0; i < ProjectManager.getProjectManager().getCurrentProjectMenuBar().getMenuCount(); i++)
            {
                if(ProjectManager.getProjectManager().getCurrentProjectMenuBar().getMenu(i).getText().equals("NLG"))
                {
                    already_there = true;
                }
            }
            
            // !!!!
            //logger.debug("creating nlg menu" + "trying to add it" + already_there);
            //if(!already_there)
            //ProjectManager.getProjectManager().getCurrentProjectMenuBar().add(NLGMenu);
            
            //menuBar.add(NLGMenu,MENU_POS);
        //}
    }
    
    // hide the "NLG" menu on the top Menu Bar of Protege
    public static void hideMenu()
    {
        logger.debug("@@ hide menu");
        if(AllTabsAreHidden() && NLGMenu != null)
        {        
            //menuBar.remove(menuBar.getMenu(MENU_POS));
            logger.debug("@@ hide menu" + "trying...");
            
            for(int i = 0; i < ProjectManager.getProjectManager().getCurrentProjectMenuBar().getMenuCount(); i++)
            {
                logger.debug("@@" + ProjectManager.getProjectManager().getCurrentProjectMenuBar().getMenu(i).getText());
                
                if(ProjectManager.getProjectManager().getCurrentProjectMenuBar().getMenu(i).getText().equals("NLG"))
                {
                    
                    ProjectManager.getProjectManager().getCurrentProjectMenuBar().remove(ProjectManager.getProjectManager().getCurrentProjectMenuBar().getMenu(i));
                }
            }
            
            NLGMenu = null;
            //ProjectManager.getProjectManager().getCurrentProjectMenuBar().repaint();
        }
    }
    
    /************  *************/
    public static OntModel getModel()
    {        
        if(ProjectManager.getProjectManager()!=null)
        {
            if(ProjectManager.getProjectManager().getCurrentProject()!=null)
            {
                if(ProjectManager.getProjectManager().getCurrentProject().getKnowledgeBase()!=null)
                {
                    JenaOWLModel owlModel = (JenaOWLModel) ProjectManager.getProjectManager().getCurrentProject().getKnowledgeBase(); 
                    OntModel m = owlModel.getOntModel(); 
                    return m;   
                }
            }
        }
        
        return null;
    }
            
    public static NLGLexiconQueryManager getLexicon()
    {
        return Lexicon;
    }
        
    public static MicroplansAndOrderingQueryManager getMicroplansAndOrderingQM()
    {
        return MicroAndOrder;
    }
            
    public static UserModellingQueryManager getUserModellingQueryManager()
    {
        return UMQM;
    }
     
    // saving the structures to files
    public static boolean savingNLResources(String NL_FilesPath, boolean savelex, boolean saveMicro,boolean saveUm)
    {        
        logger.debug("NL_FilesPath:" + NL_FilesPath);
        try
        {
            if(savelex)
            {
                NLPlugin.getLexicon().writeLexicon(NLPlugin.getModel(), NL_FilesPath + "Lexicon.rdf");
            }
            
            if(saveMicro)
            {
                NLPlugin.getMicroplansAndOrderingQM().writeMicroplans(NL_FilesPath + "microplans.rdf");                        
            }
            
            if(saveUm)
            {
                NLPlugin.getUserModellingQueryManager().writeUM(NLPlugin.getModel(),NL_FilesPath + "UserModelling.rdf");
            }
            
            // the rdf files have been updated
            
            // now update and nlg.properties            
            //update_NLG_propertiesInfo();
            
            return true;
        }
        catch(Exception e)
        {            
            init();
            
             // get projectView
            ProjectView projectView = ProjectManager.getProjectManager().getCurrentProjectView();
        
            //get tabs
            UserModellingTab UMTab = (UserModellingTab)projectView.getTabByClassName(UserModellingTab.class.getName());
            LexiconTab  LexTab = (LexiconTab)projectView.getTabByClassName(LexiconTab.class.getName());
            MicroplansAndOrderingTab MicroPlansTab = (MicroplansAndOrderingTab)projectView.getTabByClassName(MicroplansAndOrderingTab.class.getName());            
            TextPreviewsTab NLGTab = (TextPreviewsTab)projectView.getTabByClassName(TextPreviewsTab.class.getName());
            
            RefreshTabs(UMTab, MicroPlansTab, LexTab, NLGTab);
            
            e.printStackTrace();
            return false;
        }        
    }
   
    
    private static void readInfoAndLoad()
    {                                
        try 
        {            
            
            logger.info("--" + ProjectManager.getProjectManager().getCurrentProject().getLoadingURI());
            
            setNLFilesPath(ProjectManager.getProjectManager().getCurrentProject().getProjectDirectoryURI());
            
            if(NLFilesPath!=null) // if found
            {
                NLGManager nlgMan = new NLGManager();
                
                
                nlgMan.getNLFilesPanel().setValues(true, true, true, NLFilesPath);
                nlgMan.getNLFilesPanel().EnableButtons(true, true, true,true, true, false, false);
                nlgMan.getNLFilesPanel().ReadModelAndNLInfo(false);
                
                //nlgMan.showDialog();
            }
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
        
    // open the NLG.properties file and write where path of the RDF files
    private static void update_NLG_propertiesInfo()
    {
                        
        Properties properties = new Properties();
        
        try 
        {   
            
            String uri = ProjectManager.getProjectManager().getCurrentProject().getProjectDirectoryURI().toString();
            
            java.net.URI myURI = new java.net.URI(uri + "/nlg.properties") ;
            
            logger.debug("nlg.properties file: " + myURI.toString());
            
            File f = new File(myURI);
                        
            //System.err.println("@" + f.getAbsolutePath() + "@");
            
            if(f.exists()) // if already exists
            {
                 // Read properties file
                properties.load(new FileInputStream(myURI.getPath()));                
                logger.debug("loaded properties from nlg.properties file");
            }

                                                
            logger.debug("::::" + getNLFilesPath());
            
            
            String savePath = "./" ;
            
            //if NLFilesPath is null then 
            if(!getNLFilesPath().equals(""))
            savePath = getNLFilesPath();
                            
            logger.debug("loaded properties : " + savePath + " : " + myURI.getPath());
            
            //change the properties
            properties.setProperty("RDFFilesPath", savePath );

            // write properties file
            properties.store(new FileOutputStream(myURI.getPath()), "this is the directory of the RDF files");        

        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }

        
    // load rdf files to structures
    public static boolean loadNLResources(String NL_FilesPath, boolean loadlex, boolean loadMicro,boolean loadUm)
    {
        logger.debug("NL_FilesPath:" + NL_FilesPath);
        // get projectView
        ProjectView projectView = ProjectManager.getProjectManager().getCurrentProjectView();

        //get tabs
        UserModellingTab UMTab = (UserModellingTab)projectView.getTabByClassName(UserModellingTab.class.getName());
        LexiconTab  LexTab = (LexiconTab)projectView.getTabByClassName(LexiconTab.class.getName());
        MicroplansAndOrderingTab MicroPlansTab = (MicroplansAndOrderingTab)projectView.getTabByClassName(MicroplansAndOrderingTab.class.getName());
        TextPreviewsTab NLGTab = (TextPreviewsTab)projectView.getTabByClassName(TextPreviewsTab.class.getName());

        //get project
        Project proj = ProjectManager.getProjectManager().getCurrentProject();

 
        try
        {            
            if(loadlex) 
            {// load lexicon
                Lexicon = new NLGLexiconQueryManager();
                Lexicon.LoadLexicon(NL_FilesPath, "Lexicon.rdf");
            }
            
            if(loadMicro)
            {// load microplans
                MicroAndOrder = new MicroplansAndOrderingQueryManager();
                MicroAndOrder.LoadMicroplansAndOrdering(NL_FilesPath, "microplans.rdf");
            }
            
            if(loadUm)
            {// load User Modelling
                UMQM = new UserModellingQueryManager(NLPlugin.getModel());
                UMQM.LoadUserModellingInfo(NL_FilesPath, "UserModelling.rdf");       
            }

                    
            RefreshTabs(UMTab, MicroPlansTab, LexTab, NLGTab);
                                
            //update_NLG_propertiesInfo();
            return true;
        }
        catch(Exception e)
        {          
            logger.info("RDF FILES NOT FOUND OR CORRUPTED");
            init();
            RefreshTabs(UMTab, MicroPlansTab, LexTab, NLGTab);
            
            e.printStackTrace();
            return false;
        }
    }
    
    // refresh tabs 
    public static void RefreshTabs(UserModellingTab UMTab,MicroplansAndOrderingTab MicroPlansTab, LexiconTab  LexTab, TextPreviewsTab NLGTab)
    {
        if(UMTab != null)
        {
            UMTab.RefreshUMComponents(UMQM);            
        }

        if(MicroPlansTab != null)
        {
            MicroPlansTab.RefreshMicroPComponents();
        }       

        if(LexTab != null)
        {
            LexTab.Refresh_LexTab();
        }


        if(NLGTab != null)
        {
            NLGTab.refreshNLGTab();
        }
    }
    
    public static boolean AllTabsAreHidden()
    {
        
        // get projectView
        ProjectView projectView = ProjectManager.getProjectManager().getCurrentProjectView();

        //get tabs
        UserModellingTab UMTab = (UserModellingTab)projectView.getTabByClassName(UserModellingTab.class.getName());
        LexiconTab  LexTab = (LexiconTab)projectView.getTabByClassName(LexiconTab.class.getName());
        MicroplansAndOrderingTab MicroPlansTab = (MicroplansAndOrderingTab)projectView.getTabByClassName(MicroplansAndOrderingTab.class.getName());
        TextPreviewsTab NLGTab = (TextPreviewsTab)projectView.getTabByClassName(TextPreviewsTab.class.getName());

        
        if(UMTab==null && LexTab == null && MicroPlansTab==null && NLGTab==null)
        {
            return true;
        }
        
        Project proj = ProjectManager.getProjectManager().getCurrentProject();
         
        WidgetDescriptor LexWidgetDescr = proj.getTabWidgetDescriptor(LexiconTab.class.getName());
        WidgetDescriptor MicroWidgetDescr = proj.getTabWidgetDescriptor(MicroplansAndOrderingTab.class.getName());
        WidgetDescriptor UMWidgetDescr = proj.getTabWidgetDescriptor(UserModellingTab.class.getName());
        WidgetDescriptor NLGWidgetDescr = proj.getTabWidgetDescriptor(TextPreviewsTab.class.getName());
        
        if(   !MicroWidgetDescr.isVisible() && !UMWidgetDescr.isVisible() 
           && !LexWidgetDescr.isVisible()   && !NLGWidgetDescr.isVisible())
        {
            return true;
        }
        else
        {
            return false;
        }
        

    }
        
    // initialize the lexicon, microplans, and user-modelling structures
    public static void init()
    {
        Lexicon = new NLGLexiconQueryManager();
        Lexicon.init();
        
        MicroAndOrder = new MicroplansAndOrderingQueryManager();
        MicroAndOrder.init();
        
        UMQM = new UserModellingQueryManager(NLPlugin.getModel());
        UMQM.init();
    }
      
    
    public void beforeSave(Project p) 
    {
        logger.debug("before save");
        
        if(!AllTabsAreHidden())
        {
            NLGManager nlgmanager = new NLGManager();
            nlgmanager.getNLFilesPanel().setValues(true, true, true, getNLFilesPath());
            
            nlgmanager.getNLFilesPanel().EnableButtons(true, true, true , true, false, false, true);
            nlgmanager.getNLFilesPanel().SaveNLResources(false);
            //nlgmanager.showDialog(); 
        }
    }

    public void afterSave(Project p) 
    {
        System.err.println("after save");
        /*
        if(!AllTabsAreHidden())
        {
            NLGManager nlgmanager = new NLGManager();
            nlgmanager.getNLFilesPanel().setValues(true, true, true, getNLFilesPath());                                
            
            nlgmanager.showDialog();
        }
         */
    }
        
    public void beforeHide(ProjectView view, ProjectToolBar toolBar, ProjectMenuBar menuBar) 
    {
       System.err.println("before hide");
    }

    public void beforeClose(Project p) 
    {
        logger.debug("before close");
    }

    public String getName() 
    {
        return "Menu Item Inserter";
    }

    public void dispose() 
    {
       logger.debug("dispose");
    }
    
    
   public static void main(String[] args) 
   {
      edu.stanford.smi.protege.Application.main(args);
   }
}
//---------------------------------------------