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

package gr.aueb.cs.nlg.NLGEngine;
        
import gr.aueb.cs.nlg.Communications.NLGEngineServer.NLGEngineServer;
import java.io.*;
import java.io.InputStream.*;
import java.util.*;
import java.net.*;

import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.vocabulary.*;
import com.hp.hpl.jena.ontology.*;
import com.hp.hpl.jena.util.iterator.*;


import org.w3c.dom.*;
import javax.xml.parsers.*;

import gr.aueb.cs.nlg.Languages.*;
import gr.aueb.cs.nlg.NLFiles.*;
import gr.aueb.cs.nlg.Utils.*;
import gr.aueb.cs.nlg.Utils.XmlMsgs;
import gr.aueb.cs.nlg.PServerEmu.*;
import gr.demokritos.iit.PServer.*;
        
import org.apache.log4j.Logger;


public class NLGEngine
{
    
    static Logger logger = Logger.getLogger(NLGEngine.class.getName());

    String roomsPropertyLocalName = "InRoom";
    String coordPropertyLocalName = "coord";
    
    public OntModel ontModel = null;
    public String OntologyPathURL = "";
    public File owlFile = null;
    public File NLFile = null;
    
    public String owlFilePath = "";
    public String NLResourcesPath; 
    
    public String lang;
    
    private XmlDocumentCreator XmlDocCreator;
    
    private MicroplansAndOrderingQueryManager MAOQM;
    private NLGLexiconQueryManager NLGLQM;
    private UserModellingQueryManager UMQM;
    
    private ContentSelection CS;
    private Lexicalisation LEX;

    private Aggregation AGGRGT;
    
    private GRE genRefExpr;
    private SurfaceRealization SR;
          
    private Structures strcts;
    private gr.aueb.cs.nlg.PServerEmu.UMAuthoring UMAuth;
    public  gr.demokritos.iit.PServer.UMVisit UMVis;        
    
    private boolean useEmulator;
            
    private exhibitsPositions exhibitsPos;
    private XmlMsgs messagesAfterContentSelection;
    
    private NLGEngineServer nlgServer;
    public java.util.List charComparison;
            
    private String DBusername = "";
    private String DBpassword = "";
    private boolean LOAD_DBs;
    
    private String PSERVER_IP = "";
    private int PSERVER_PORT ;

    private String COMMUNICATION_SERVER_IP = "";
    private int COMMUNICATION_SERVER_PORT ;
    
    //private boolean connectToCommunicationsServer = true;
    // to dendro poy apo8hkeyei osa antikeimena exw perigrapsei mexri twra
    private ComparisonTree statisticalTree;
    private ComparisonTree comparTree;
            
    private AnnotatedDescription AD;
    
    public NLGEngine()
    {
        logger.debug("NLGEngine was Initialed...");
    }
    
    /*
    public void createStatisticalTree()
    {
        // dhmioyriga toy dendroy poy 8a krata osa exw perigrapsei hdh
        comparTree = new ComparisonTree();
        statisticalTree = new ComparisonTree(); 
    }
     */
    
    public void setShapeText(boolean b)
    {
        SR.setshapeText(b);
    }
    
    public void initStatisticalTree()
    {            
        // init the structures used for comparisons
        comparTree = new ComparisonTree(this.charComparison);
        statisticalTree = new ComparisonTree(this.charComparison); 
     
        // load the statistical tree
        ExtendedIterator iter = ontModel.listIndividuals();
        
        while(iter.hasNext())
        {
            String IndividualURI = iter.next().toString();
            statisticalTree.addElement(IndividualURI, this.ontModel,this.charComparison);
        }
        
        //for(int i = 0; i < Instances.length; i++)
        //{            
        //    statisticalTree.addElement(((OntObject)Instances[i]).getURI(),ontModel);
        //}

        //statisticalTree.print();
        statisticalTree.sortAttributes();
    }
    
    // disconnect from Navigation server
    public void disconnectFromNavServer() throws Exception
    {
        try
        {
            nlgServer.die();
        }
        catch(Exception e)
        {
            throw new Exception();
        }
    }
         
    public NLGLexiconQueryManager getLexicon()
    {
        return this.NLGLQM;
    }
    
    
     /*       
    // initialize NLG engine
    public NLGEngine(String owlFilePath, // OWL FILE PATH
                     String NLResourcesPath,  // NL Resources PATH
                     String lang, // 
                     boolean useEmul, // use Emulator
                     boolean load_DBases, // load databases
                     MicroplansAndOrderingQueryManager MAOQMan, // microplans
                     NLGLexiconQueryManager NLGLQMan, // lexicon
                     UserModellingQueryManager UMQMan, // user modelling
                     OntModel m, // ontology
                     String communication_server_IP,int communication_server_port, // navigation server IP and port
                     String DBusername,String DBpassword, // database  username and password
                     String PServerIP, int PserverPort
                     )
    {
        this(owlFilePath, NLResourcesPath, lang, useEmul, load_DBases, MAOQMan, NLGLQMan, UMQMan, m, 
             communication_server_IP, communication_server_port, DBusername, DBpassword, PServerIP, PserverPort);
    }
    */
    
    // initialize NLG engine
    public NLGEngine(String owlFilePath, // OWL FILE PATH
                     String NLResourcesPath,  // NL Resources PATH
                     String lang, // 
                     boolean useEmul, // use Emulator
                     boolean load_DBases, // load databases
                     MicroplansAndOrderingQueryManager MAOQMan, // microplans
                     NLGLexiconQueryManager NLGLQMan, // lexicon
                     UserModellingQueryManager UMQMan, // user modelling
                     OntModel m, // ontology
                     String communication_server_IP,int communication_server_port, // communication server IP and port
                     String DBusername,String DBpassword, // database  username and password
                     String PServerIP, int PserverPort
                     )
    {
                
        try
        {
            this.useEmulator = useEmul;   
            this.DBusername = DBusername;
            this.DBpassword = DBpassword;
            this.PSERVER_IP = PServerIP;
            this.PSERVER_PORT = PserverPort;
                
            this.LOAD_DBs = load_DBases;
                    
            this.COMMUNICATION_SERVER_IP = communication_server_IP;
            this.COMMUNICATION_SERVER_PORT = communication_server_port;
              
            
            this.owlFilePath = owlFilePath;
            this.NLResourcesPath = NLResourcesPath;
            XmlDocCreator = new XmlDocumentCreator();
            
            this.lang = lang;
                    
            if(m == null)
            {// if m is null load it
                owlFile = new File(owlFilePath);
                
                if(owlFile.exists())
                {

                    //parse owl file
                    DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                    Document doc = docBuilder.parse(owlFile);
                    NodeList nodeList = doc.getElementsByTagName("rdf:RDF");

                    //get the Base URL of the ontology
                    String OntologyPathURL = "";
                    NamedNodeMap nameNodeMap = nodeList.item(0).getAttributes();

                    if (nameNodeMap.getNamedItem("xml:base") != null)
                    {
                        OntologyPathURL = nameNodeMap.getNamedItem("xml:base").getNodeValue();
                    }
                    else
                    {
                        OntologyPathURL = nameNodeMap.getNamedItem("xmlns").getNodeValue();
                    }

                    ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM ,null);
                    ontModel.add(OWL.Thing, RDF.type, OWL.Class);  

                    ontModel.getSpecification().getDocumentManager().addAltEntry(OntologyPathURL, "file:" + owlFile.getPath());
                    logger.debug( "[" + OntologyPathURL + ","  +  owlFile.getPath() + "]");                

                    String path = owlFile.getParent();

                    logger.debug("Looking for imports...");
                    // a simple way to import other ontologies that are referenced from current ontology

                    nodeList = doc.getElementsByTagName("owl:imports");
                    
                    for(int i = 0; i < nodeList.getLength(); i++)
                    { // for all imports
                        NamedNodeMap nameNodeMap2 = nodeList.item(i).getAttributes();
                        String importURI = nameNodeMap2.getNamedItem("rdf:resource").getNodeValue();

                        File temp = new File(importURI);
                        String importFileName = path + System.getProperty("file.separator") + temp.getName()+".owl";
                        logger.debug("importFileName :" + importFileName);
                        File localCopy = new File(importFileName);

                        if(localCopy.exists())
                        {
                            logger.debug("localCopy " + localCopy.exists() +  " ok ");

                            logger.debug("import " + "[" + importURI + "," + importFileName + "]" );
                            //ontModel.addLoadedImport(importURI);
                            //if(!ontModel.hasLoadedImport(importURI)){
                                    ontModel.getSpecification().getDocumentManager().addAltEntry(importURI , "file:" + System.getProperty("file.separator") + importFileName);
                                    //ontModel.addLoadedImport(importURI);
                            //}
                        }
                        else
                        logger.debug("localCopy not found");
                    }//for     

                    ontModel.read(OntologyPathURL, "RDF/XML-ABBREV");                                              
                    
                }// if owl file exists
                else
                {
                    throw new java.io.FileNotFoundException();
                }
            }// if m is null load it
            else
            {
                this.ontModel = m;
            }
            
            String ns = ontModel.getNsPrefixURI("");
            exhibitsPos = new exhibitsPositions(ontModel, ns + coordPropertyLocalName, ns + roomsPropertyLocalName);
            
            
            // if lexicon usermodelling and microplans are != null                         
            if(NLGLQMan != null && MAOQMan!=null && UMQMan!=null)
            {
                this.NLGLQM = NLGLQMan;
                this.MAOQM = MAOQMan;
                this.UMQM = UMQMan;
                
                ConnectToPserver(false); 
                        
                initializeNLGModules();              
            }
            else
            { // load them from RDF files               
                NLFile = new File(NLResourcesPath);
                
                
                if(NLFile.exists())
                {//                    
                            
                    String currentNLFilesPath = NLFile.getAbsoluteFile().getAbsolutePath() + System.getProperty("file.separator");
                    
                    String path = NLFile.getAbsolutePath() + System.getProperty("file.separator");
                    
                    File f1 = new File(path + "Lexicon.rdf");
                    File f2 = new File(path + "microplans.rdf");
                    File f3 = new File(path + "UserModelling.rdf");

                    logger.debug("path:" + path);

                    if(f1.exists() && f2.exists() && f3.exists())
                    {
                        // load lexicon
                        NLGLQM = new NLGLexiconQueryManager();
                        NLGLQM.LoadLexicon(path, "Lexicon.rdf");

                        //load microplans and ordering info
                        MAOQM = new MicroplansAndOrderingQueryManager();
                        MAOQM.LoadMicroplansAndOrdering( path ,  "microplans.rdf");

                        //load User Modelling info
                        UMQM = new UserModellingQueryManager(ontModel);
                        UMQM.LoadUserModellingInfo( path ,  "UserModelling.rdf");

                        //UMQM.test();
                        //UMVisEmu = new UMVisitImpEmu(new Structures(UMQM));

                        ConnectToPserver(false);

                        initializeNLGModules();
                    }
                    else
                    {
                        
                        logger.debug("NL Files not FOUND !!!!!!");
                        NLGLQM = new NLGLexiconQueryManager();                    
                        MAOQM = new MicroplansAndOrderingQueryManager();                
                        UMQM = new UserModellingQueryManager(ontModel);

                        NLGLQM.init();
                        MAOQM.init();
                        UMQM.init();

                        //ConnectToPserver(useEmul);

                        initializeNLGModules();                        
                    }

                }
                else
                {
                    logger.debug("NL Files not FOUND !!!!!!");
                    NLGLQM = new NLGLexiconQueryManager();                    
                    MAOQM = new MicroplansAndOrderingQueryManager();                
                    UMQM = new UserModellingQueryManager(ontModel);
                    
                    NLGLQM.init();
                    MAOQM.init();
                    UMQM.init();
                    
                    //ConnectToPserver(useEmul);
                    
                    initializeNLGModules();
                    
                }                
            }       
            
            this.charComparison = this.MAOQM.getPropertiesUsedForComparisons();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        this.exhibitsPos.changePosition("http://www.aueb.gr/users/ion/mpiro.owl#exhibit15",5.0,32.0);
    }
    
    public exhibitsPositions getExhPos()
    {
        return this.exhibitsPos;
    }
    
    public NLGEngineServer getNav()
    {
        return this.nlgServer;
    }
        
    public void setModel(OntModel model)
    {
        this.ontModel = model;
        CS.setModel(model);
        SR.setModel(model);
    }
    
    private void ConnectToPserver(boolean directly)
    {
        try
        {                    
            if(this.useEmulator)
            {
                strcts =  new Structures(UMQM);
                UMAuth = new UMAuthoringImpEmu(strcts);           
                UMVis = new UMVisitImpEmu(strcts); 
            }
            else
            {
                /*
                logger.debug("Connecting to PServer..");
                String currentNLFilesPath = NLFile.getAbsoluteFile().getAbsolutePath() + System.getProperty("file.separator");                    
                String path = NLFile.getAbsolutePath() + System.getProperty("file.separator");                     

                //String path = "C:\\Documents and Settings\\USER\\Επιφάνεια εργασίας\\AuthoringTool\\";                
                
                //String username="root";
                //String password="xenios";
                
                if(directly)
                {
                    String file = "UserModelling.rdf";

                    String xeniosPath="jdbc:mysql://" + PSERVER_IP +"/indigo";
                    String PerServerPath="jdbc:mysql://" +  PSERVER_IP+ "/pserver";

                    //To palio arxeio me sxolia, einai akribws to idio me ParseAndInitialize mono pou exei sxolia
                    //parseConcatenatedUserModeling a=new parseConcatenatedUserModeling();
                    //a.parseConcatenated(path, file);
 
                    if(this.LOAD_DBs)
                    {
                        logger.info("Loading Databases....");
                        ParseAndInitialize a = new ParseAndInitialize(file, path, DBusername, DBpassword, xeniosPath, PerServerPath);
                        a.parseConcatenated();  
                        logger.info("Loaded Databases....");
                    }

                    InetAddress utopia;
                    int prt = 1111;

                    if(this.PSERVER_IP == null || this.PSERVER_IP.equals(""))
                    {
                        utopia = InetAddress.getByName("localhost");
                    }
                    else
                    {
                        utopia = InetAddress.getByName(PSERVER_IP);
                    }

                    if(prt == -1)
                    {
                        prt = 1111;
                    }

                    logger.info("Address: " + utopia);
                    logger.info("prt: " + prt);
                    logger.info("DBusername: " + DBusername);
                    logger.info("DBpassword: " + DBpassword);
                    logger.info("xeniosPath: " + xeniosPath);
                    logger.info("PerServerPath: " + PerServerPath);
                    
                    logger.info("UMVis connected to Pserver....");
                }
                else
                {
                    logger.info("trying to connect to COMMUNICATION_SERVER with IP: " + COMMUNICATION_SERVER_IP);
                    
                    UMVis = new UMVisitImp(this.COMMUNICATION_SERVER_IP);    
                    logger.info("UMVis connected to communication server....");
                }   
                */
            }       

            
        }
        catch(Exception e)
        {
            logger.info("***NOT CONNECTED*** to PServer....");
            e.printStackTrace();
        }
    }
    
     public boolean AllFactsAreAssimilated()
     {
       return CS.AllFactsAreAssimilated();
     }
       
    private void initializeNLGModules()
    {
        CS = new ContentSelection(MAOQM, UMQM, UMVis ,ontModel, this.lang, this.exhibitsPos);                
        LEX = new Lexicalisation(this.lang, MAOQM);                                                                 
        AGGRGT = new Aggregation(1,this.lang,NLGLQM);
        genRefExpr = new GRE(this.lang);                                
        SR = new SurfaceRealization(100,this.lang, NLGLQM, ontModel);        
    }
    
    public void refreshNLGModules(OntModel new_model, String mylang)
    {
        CS = new ContentSelection(MAOQM, UMQM, UMVis , new_model, mylang, this.exhibitsPos);                
        LEX = new Lexicalisation(mylang, MAOQM);                                                                 
        AGGRGT = new Aggregation(1, mylang, NLGLQM);
        genRefExpr = new GRE(mylang);                                
        SR = new SurfaceRealization(100, mylang, NLGLQM, new_model);        
    }
        
    public void initPServer()
    {
        try
        {
            if(useEmulator)
            {
                Vector entities = new Vector();
                Vector microplanIDs = new Vector();
                Vector factsIDs = new Vector();

                for (Iterator i = ontModel.listClasses();  i.hasNext(); )
                {
                    OntClass cls = (OntClass)i.next();
                    
                    if (!cls.isAnon())
                    {
                        entities.add( cls.getURI()); // add class entities
                        
                        ExtendedIterator j = cls.listSuperClasses();
                        
                        while( j.hasNext() )
                        {
                            OntClass superClass = (OntClass)j.next();
                            
                            if(!superClass.isAnon())
                            {
                                String factID = "[" + cls.getURI() + ", " + RDFS.subClassOf.getURI() + ", " + superClass.getURI() + "]"; 
                                logger.debug("factID:" + factID);
                                factsIDs.add(factID);
                            }
                        }
                    }
                }

                for(ExtendedIterator i = ontModel.listIndividuals(); i.hasNext();)
                {
                    Individual temp_ind = (Individual)i.next();
                    entities.add(temp_ind.getURI());   // add instance entities          

                    for(StmtIterator j = temp_ind.listProperties();  j.hasNext();)
                    {
                        Statement t = (Statement)j.next();
                        RDFNode node = t.getObject();

                        String factID = "[" + t.getSubject().getURI() + ", " + t.getPredicate().getURI() + ", " + t.getObject().toString() + "]"; 

                        logger.debug("factID:" + factID);
                        factsIDs.add(factID);
                    }

                }

                Iterator<String> MicroplansIDsIter = MAOQM.getMicroplansIDs();

                while(MicroplansIDsIter.hasNext())
                {
                    String MicroID = MicroplansIDsIter.next().toString();
                    microplanIDs.add(MicroID);
                    logger.debug("\t\tMicroID:" + MicroID);
                }

                String entitiesTable[] = new String[entities.size()]; 
                entities.toArray(entitiesTable);

                String factIDsTable[] = new String[factsIDs.size()]; 
                factsIDs.toArray(factIDsTable);

                String microplanIDsTable[] = new String[microplanIDs.size()]; 
                microplanIDs.toArray(microplanIDsTable);

                UMAuth.initialize(entitiesTable, factIDsTable, microplanIDsTable);
            }
            else
            {
                // do nothing...
                // we have already loaded everything
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public final static int INSTANCE_TYPE = 0 ;
    public final static int CLS_TYPE = 0 ;
    
    Vector<String> stagesOutputs;
            
    public void setServer (NLGEngineServer nlgS)
    {
        this.nlgServer = nlgS;
    }
            
    public  String[] GenerateDescription(int type, String objectURI, String userType, String userID, int depth, int MFPS, boolean withComp, String personality)
    {
        logger.info("type:" + type);
        logger.info("objectURI: " + objectURI);
        logger.info("userType: " + userType);
        logger.info("userID: " + userID);
        logger.info("depth: " + depth);
        logger.info("MFPS: " + MFPS);
        logger.info("withComp: " + withComp);
        logger.info("personality: " + personality);
        
        
        stagesOutputs = new Vector<String>();
        String result [] = new String[3];
        result[0] = "";
        result[1] = "";
        result[2] = "";
        
        try
        {
            if(MFPS == -1)
            {
                if(userID != null)
                {                    
                     AGGRGT.set_MAX_FACTS_PER_SENTENCE(UMVis.getMaxFactsPerSentence(userID, userType));                                             
                }
                else
                {
                    if(userType != null)
                    {
                        UserTypeParameters UTP = UMQM.getParametersForUserType(userType);
                        AGGRGT.set_MAX_FACTS_PER_SENTENCE(UTP.getMaxFactsPerSentence());                                                
                    }
                    else
                    {
                        AGGRGT.set_MAX_FACTS_PER_SENTENCE(1);  
                    }
                }
            }
            else
            {
                AGGRGT.set_MAX_FACTS_PER_SENTENCE(MFPS);                
            }

                   
            if(type == 0)
            {
                String textResult = "";

                XmlMsgs messages = new XmlMsgs(objectURI, XmlMsgs.INST_TYPE, XmlDocCreator.getNewDocument());
                //XmlMsgs messages2 = new XmlMsgs(objectURI, XmlMsgs.INST_TYPE, XmlDocCreator.getNewDocument());

                CS.ClearBuffers();
                CS.setNamespaces(messages);
                CS.setPersonality(personality);
                messages = CS.getMsgsOfAnInstance(objectURI, messages, depth, 1, messages.getRoot(), userType, userID, comparTree, statisticalTree, withComp, nlgServer);
		
                
                textResult = textResult + "\n" + "---Content Selection---"  + "\n";
                stagesOutputs.add(messages.getStringDescription(true));
                textResult = textResult + " " +  messages.getStringDescription(true);
                

                messages = CS.getTheMostInterestingUnassimilatedFacts(messages, depth, userType, userID);
                                
                textResult = textResult + "\n" + "---Content Selection/Most Interesting Not Assimilated Facts---"  + "\n";
                stagesOutputs.add(messages.getStringDescription(true));
                textResult = textResult + " " +  messages.getStringDescription(true);
                
                
                if(userID != null)
                {
                    CS.setMentionedEntity(objectURI);
                    CS.UpdatePServer(userID, userType);
                }

                messages.sortByOrder();

                textResult = textResult + "\n" + "---Ordering---"  + "\n";
                stagesOutputs.add(messages.getStringDescription(true));
                textResult = textResult + " " +  messages.getStringDescription(true);
                
                
                messagesAfterContentSelection = messages;  // store the messages after Content Selection
                

                messages = LEX.lexInstances(messages);

                textResult = textResult + "\n" + "---Lexicalisation---"  + "\n";
                stagesOutputs.add(messages.getStringDescription(true));
                textResult = textResult + " " +  messages.getStringDescription(true);
                
                
                messages = AGGRGT.Aggregate(messages);

                textResult = textResult + "\n" + "---Aggregation---"  + "\n";
                stagesOutputs.add(messages.getStringDescription(true));
                textResult = textResult + " " +  messages.getStringDescription(true);
                
                messages = genRefExpr.GenerateReferringExpressions( messages);

                textResult = textResult + "\n" + "---Referring expressions---"  + "\n";
                stagesOutputs.add(messages.getStringDescription(true));
                textResult = textResult + " " +  messages.getStringDescription(true);

                String RT = "";
        
                if(userID !=null)                        
                {
                    RT = SR.Realize(messages, userType);
                }
                else
                {
                    RT = SR.Realize(messages, userType);
                }
                
                textResult = textResult + "\n" + "---Surface Realization---"  + "\n";
                stagesOutputs.add(RT);
                textResult = textResult + "\n" + RT + "\n";
                
                result [0] = textResult;
                result [1] = RT;
                
                
                SR.getAnnotatedDescription().removeLastPeriod();
                this.AD = SR.getAnnotatedDescription();
                
                result [2] = AD.getAnnotatedXml();
                                
                return result;
            }
            else if(type == 1)
            {
                String textResult = "";

                XmlMsgs messages = new XmlMsgs(objectURI, XmlMsgs.CLS_TYPE, XmlDocCreator.getNewDocument());
                XmlMsgs messages2 = new XmlMsgs(objectURI, XmlMsgs.CLS_TYPE, XmlDocCreator.getNewDocument());

                CS.ClearBuffers();
                messages = CS.getMsgsOfAClass(objectURI, messages, depth, 1, messages.getRoot(), userType, userID);

                textResult = textResult + "\n" + "---Content Selection---"  + "\n";
                textResult = textResult + " " +  messages.getStringDescription(true);

                   
                    OntClass c = ontModel.getOntClass(objectURI);
                    
                    ExtendedIterator iter = c.listDeclaredProperties(true);
                    
                    while(iter.hasNext())
                    {
                        logger.debug("declared properties " + iter.next().toString());
                    }

                    iter = c.listDeclaredProperties(false);
                    while(iter.hasNext())
                    {
                        logger.debug("declared properties 2" + iter.next().toString());
                    }   
                    
                    iter = c.listDeclaredProperties();
                    while(iter.hasNext())
                    {
                        logger.debug("declared properties --" + iter.next().toString());
                    }   
                    
                 
                    
                //messages = CS.getTheMostInterestingFacts(messages, depth, userType);

                //textResult = textResult + "\n" + "---Content Selection/Most Interesting Facts---"  + "\n";
                //textResult = textResult + " " +  messages.getStringDescription(true);


                //messages.sortByOrder();

                //textResult = textResult + "\n" + "---Ordering---"  + "\n";
                //textResult = textResult + " " +  messages.getStringDescription(true);


                messages = LEX.lexClasses(messages);

                textResult = textResult + "\n" + "---Lexicalisation---"  + "\n";
                textResult = textResult + " " +  messages.getStringDescription(true);

                messages = AGGRGT.Aggregate(messages);

                textResult = textResult + "\n" + "---Aggregation---"  + "\n";
                textResult = textResult + " " +  messages.getStringDescription(true);

                messages.sortByOrder();
                
                textResult = textResult + "\n" + "---Sort---"  + "\n";
                textResult = textResult + " " +  messages.getStringDescription(true);
                                
                        
                messages = genRefExpr.GenerateReferringExpressions( messages);

                textResult = textResult + "\n" + "---Referring expressions---"  + "\n";
                textResult = textResult + " " +  messages.getStringDescription(true);

                
                String RT = "";
                
              
                if(userID !=null)                        
                {
                    RT = SR.Realize(messages, userType);
                }
                else
                {
                    RT = SR.Realize(messages, userType);
                }
                
                textResult = textResult + "\n" + "---Surface Realization---"  + "\n";
                textResult = textResult + "\n" + RT + "\n";
                
                
                result [0] = textResult;
                result [1] = RT;
                
                return result;               
            }
            else
            {
                return result;       
            }
                       
        }
        catch(Exception e)
        {
            logger.info("******** Problem on generating a text... ********");
            e.printStackTrace();      
            result[0] = "";
            result[1] = "<ERROR></ERROR>";
            
            logger.info("******** DEBUGGING ********");
            
            for(int i = 0; i< stagesOutputs.size(); i++)
            {
                logger.info( "*** " + i + " ***");
                logger.info(stagesOutputs.get(i) + "\n\n\n");
            }
            
            return result;
        }
                    
    }
    
   public void setLanguage(String lang)
   {
       this.lang = lang;
       Languages.updateLanguages(CS, LEX, genRefExpr, SR, AGGRGT, lang);
   }
   
    
   public String getLang()
   {
       return this.lang;
   }

  
   
   
   public OntModel getModel()
   {
       return this.ontModel;
   }     
   
   public Iterator<String> getUserTypes()
   {
       return UMQM.getUserTypes();
   }
      
   public UMVisit getUMVisit()
   {
       return this.UMVis;
   }
   
   
   public boolean useEmulator()
   {
       return useEmulator;
   }
           
   public static boolean isClass (OntModel m,  String instanceURI)
   {
        
        Resource ontRes  = m.getResource(instanceURI);
        if(!ontRes.canAs(OntClass.class))
            return false;
        else 
            return true;
                
   }


    public static String getClassType (OntModel m,  String instanceURI)
    {
        if(m == null) return "NULL MODEL";
                
        Resource ontRes  = m.getResource(instanceURI);
        
        if(ontRes == null)
        {
            return "NOT FOUND";
        }
        else if(!ontRes.canAs(OntClass.class))
        {
            Individual ind = m.getIndividual(instanceURI);
            logger.debug("foo:" + instanceURI);
        
            StmtIterator iter = ind.listProperties(RDF.type);

            while(iter.hasNext())
            {
                Statement stmt = (Statement)iter.nextStatement();                
                return ((Resource)stmt.getObject().as(Resource.class)).getURI();
            }
        }
        else
        {
            OntClass c = m.getOntClass(instanceURI);
            
            if(c !=null)
            {
                StmtIterator iter = c.listProperties(RDFS.subClassOf);

                while(iter.hasNext())
                {
                    Statement stmt = (Statement)iter.nextStatement(); 
                    Resource res = ((Resource)stmt.getObject().as(Resource.class));               
                    if(!res.isAnon())
                    return res.getURI();
                }
            }
        }
                    
        return "";
    }
    
    public String[][] getFactsAndAssimilations()
    {
        
        Vector msgs = messagesAfterContentSelection.getMsgs();
        String[][] array = new String[msgs.size()][2];
                
        for(int i = 0; i < msgs.size(); i++)
        {
           
            Node msg = (Node)msgs.get(i);
            
            String sub = XmlMsgs.getAttribute(msg, XmlMsgs.prefix, XmlMsgs.REF);
            String obj = XmlMsgs.getAttribute(msg,XmlMsgs.prefix, "Val");
            String pred = "";
            String score =  XmlMsgs.getAttribute(msg,XmlMsgs.prefix,  XmlMsgs.ASSIMIL_SCORE);
            
            if(msg.getNodeName().equals("owlnl:type"))
            {
                pred = RDF.type.getURI();
            }
            else
            {
                pred =  msg.getNamespaceURI() + msg.getLocalName();
            }
                    
            array[i][0] = FactFactory.getFact(sub, pred, obj);
            array[i][1] = score;      
                         
        }
        
        return array;
    }    
    
    public AnnotatedDescription getAnnotatedText()
    {
        return this.AD;
    }
}
