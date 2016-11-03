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

package gr.aueb.cs.nlg.NLFiles;

import java.io.*;
import java.io.File;
import java.util.*;

import org.w3c.dom.*;

import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.vocabulary.*;
import com.hp.hpl.jena.ontology.*;
import com.hp.hpl.jena.util.iterator.*;


import gr.aueb.cs.nlg.*;
import gr.aueb.cs.nlg.Languages.*;
import gr.aueb.cs.nlg.Utils.*;

import org.apache.log4j.Logger;
import org.apache.log4j.Level;

public class NLGLexiconQueryManager
{
        public String NLGLexiconNS = "http://www.aueb.gr/users/ion/owlnl/Lexicon#";
        static Logger logger = Logger.getLogger("gr.aueb.cs.nlg.NLFiles.NLGLexiconQueryManager");
        
	private Hashtable Lexicon; //[NPuri => NP]
        private Hashtable CannedTextLexicon; // [CannedURI => CannedText]
        private LexiconManager LexManager;
        
        private Hashtable<String, HashSet> LexiconMapping; //[owlResourceURI, list of NPuri and canned uri]
        //Gerasimos Lampouras
        private Hashtable<String, String> ReverseLexiconMapping; //[NPuri or canned uri, owlResourceURI]
        
	private String language = "";
                
        public static int NP_ENTRY = 1;
        public static int CANNED_TEXT_ENTRY = 1;
        public static int UNDEFINED_ENTRY = -1;
                
	public NLGLexiconQueryManager()
        {   
            //BasicConfigurator.configure();
            init();	
            
	}
	
        public void init()
        {
            NLGLexiconNS = "http://www.aueb.gr/users/ion/owlnl/Lexicon#";
            Lexicon = null;
            CannedTextLexicon = null;
            LexiconMapping = null; 
            EntriesLoadedFromCompLexicon = new Vector();
            
            Lexicon = new Hashtable();	// NP lexicon
            CannedTextLexicon = new Hashtable(); //canned text  lexicon
            LexiconMapping = new Hashtable(); // mapping 
        }
	//-----------------------------------------------------------------------------------
        //return a Vector that contains  the lexicon
        public Vector getEntries()
        {
            Enumeration enumer = Lexicon.keys();
            Vector v = new Vector();
            while(enumer.hasMoreElements()){
                v.add((String)enumer.nextElement());
            }
            
            return v;
        }
        //-----------------------------------------------------------------------------------
	public String getEntry(String OntResURI, String Case, boolean plural, String lang ) 
        {
            try
            {
                Object obj = getEntry(OntResURI, lang, null, 1);

                
                if(obj == null)
                {
                    return null;
                }                
                else if(obj instanceof Lex_Entry_GR)
                {
                    if(plural)
                        return ((Lex_Entry_GR)obj).get(Case, XmlMsgs.PLURAL );
                    else
                        return ((Lex_Entry_GR)obj).get(Case, XmlMsgs.SINGULAR );
                        
                }
                else if(obj instanceof Lex_Entry_EN)
                {
                    if(plural)
                        return ((Lex_Entry_EN)obj).get(Case, XmlMsgs.PLURAL );
                    else
                        return ((Lex_Entry_EN)obj).get(Case, XmlMsgs.SINGULAR );                    
                }
                else
                {
                    return "ERROR@@@@@";
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            
            return "ERROR@@@@";
	}
        
	public String getEntry(String OntResURI, String Case, String lang, String userType, int entry_type) 
        {
            try
            {
                Object obj = null; 
                        
                if( entry_type == -1)
                    obj = getEntry(OntResURI, lang, userType, entry_type);
                else
                    obj = getEntry(OntResURI, lang, userType);
                
                if(obj == null)
                {
                    return null;
                }
                else if(obj instanceof CannedList)
                {
                    ((CannedList)obj).getCannedText(lang);
                }
                else if(obj instanceof Lex_Entry_GR)
                {
                    return ((Lex_Entry_GR)obj).get(Case);
                }
                else if(obj instanceof Lex_Entry_EN)
                {
                    return ((Lex_Entry_EN)obj).get(Case);
                }
                else
                {
                    return "ERROR";
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            
            return "ERROR";
	}

	//-----------------------------------------------------------------------------------
        public Object getEntry(String OntResURI, String lang, String userType)
        {
            try
            {
		//logger.debug("looking for:" + OntResURI+ "...");
                Iterator iter;
                String NLResource_URI ="";
                boolean RelatedToCanned = false;
                
		if(LexiconMapping.containsKey(OntResURI))
                {// contains key
                      
                    iter = LexiconMapping.get(OntResURI).iterator();

                    // decide if is related to a canned
                    while(iter.hasNext() && !RelatedToCanned)
                    {
                        String current_URI = iter.next().toString();

                        if(CannedTextLexicon.containsKey(current_URI))
                        RelatedToCanned = true;

                    }
                    
                    if (RelatedToCanned)
                        return getEntry(OntResURI, lang, userType, 2);
                    else
                        return getEntry(OntResURI, lang, userType, 1);
                }
                else
                {
                    return null;
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            
            return null;
        }
        
	public Object getEntry(String OntResURI, String lang, String userType, int entry_type)
        {
            try
            {
		//logger.debug("looking for:" + OntResURI+ "...");
                Iterator iter;
                String NLResource_URI ="";
                boolean RelatedToCanned = false;
                
		if(LexiconMapping.containsKey(OntResURI))
                {// contains key
                      
                    iter = LexiconMapping.get(OntResURI).iterator();

                    // decide if is related to a canned
                    while(iter.hasNext() && !RelatedToCanned)
                    {
                        String current_URI = iter.next().toString();

                        if(CannedTextLexicon.containsKey(current_URI))
                        RelatedToCanned = true;

                    }

                    if(entry_type == 2)// canned
                    {
                        iter = LexiconMapping.get(OntResURI).iterator();

                        if(userType !=null)
                        {    
                            while(iter.hasNext())
                            {                                
                                String current_NLResource_URI = iter.next().toString();
                                logger.debug("current_NLResource_URI:" + current_NLResource_URI );
                                Object obj = CannedTextLexicon.get(current_NLResource_URI);

                                if(obj instanceof CannedList)
                                {
                                   if(((CannedList)obj).isSuitablefor(userType)) // find canned suitable for the specified user type
                                   {
                                       if(! (((CannedList)obj).getCannedText(lang).equals("")) ) // find the first suitable not empty canned
                                       NLResource_URI = current_NLResource_URI;
                                   }
                                }                            
                            }                             
                        }
                        else
                        {
                            NLResource_URI = iter.next().toString();
                        }
                        
                        Object obj2 = CannedTextLexicon.get(NLResource_URI);
                        return obj2;
                    }
                    else if(entry_type == 1) // NP
                    {
                        
                      iter = LexiconMapping.get(OntResURI).iterator();

                      while(iter.hasNext())
                      {                                
                          String current_NLResource_URI = iter.next().toString();
                          logger.debug("current_NLResource_URI:" + current_NLResource_URI );
                          Object obj = Lexicon.get(current_NLResource_URI);

                          if(obj instanceof NPList)
                          {       
                              NLResource_URI = current_NLResource_URI;      
                              Object obj2 = Lexicon.get(NLResource_URI);
                              return ((NPList)obj2).getEntry(lang);                          
                          }                            
                      }
                      
                      return null;
                    }
                                        
                    logger.debug("NLResource_URI:" + NLResource_URI);
                    
        		
		}// contains key
		else
                {
                    return null;		
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            
            return null;
	}	
	//-----------------------------------------------------------------------------------
        private void AddMapping(String owlResourceURI,String NLResURI)
        {
            logger.debug("Mapping:" + owlResourceURI + "-" + NLResURI);

            if(LexiconMapping.containsKey(owlResourceURI))
            {
                HashSet set = LexiconMapping.get(owlResourceURI);
                set.add(NLResURI);
                LexiconMapping.put(owlResourceURI, set);
            }
            else
            {
                HashSet set = new HashSet();
                set.add(NLResURI);
                LexiconMapping.put(owlResourceURI, set);                        
            }
        }
        
        
        public void LoadLexicon(String path, String file)
        {
            init();
            EntriesLoadedFromCompLexicon = new Vector();
            LoadLexiconFromFile(path, file);    
            
            File compLexiconFile = new File(path + "comparisonsLexicon.rdf");
            
            if(compLexiconFile.exists())
            {
                EntriesLoadedFromCompLexicon = LoadLexiconFromFile(path, "comparisonsLexicon.rdf"); /// 
                logger.debug("@@@@" + "found comparisonsLexicon");
            }
            else
            {
                        
                try
                {
                    java.net.URL compLexiconURL = NLGLexiconQueryManager.class.getResource("/comparisonsLexicon.rdf");
                                  
                    InputStream is = NLGLexiconQueryManager.class.getResourceAsStream("/comparisonsLexicon.rdf");
                    
                    
                    logger.debug("Loading comparisons' Lexicon: " + compLexiconURL.getPath());
                                                           
                    EntriesLoadedFromCompLexicon = LoadLexiconFromFile(is); ///
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
            
        }
        
        private Vector EntriesLoadedFromCompLexicon = new Vector();
        
        private Vector LoadLexiconFromFile(InputStream is)
        {
            Vector loadedResources = new Vector();
                                
            LexManager = new LexiconManager("");
            MicroplansAndOrderingManager MAOM = new MicroplansAndOrderingManager ("");
            UserModellingManager UMM = new UserModellingManager("");
            
            LexManager.read(is);
            NLGLexiconNS = LexManager.model.getNsPrefixURI("");
                     
            return ParseFile( LexManager,  MAOM,  UMM) ;   
        }
        
        private Vector LoadLexiconFromFile(String path, String file)
        {
            
            //init();
            Vector loadedResources = new Vector();
                                
            LexManager = new LexiconManager("");
            MicroplansAndOrderingManager MAOM = new MicroplansAndOrderingManager ("");
            UserModellingManager UMM = new UserModellingManager("");
            
            LexManager.read(path,file);
            NLGLexiconNS = LexManager.model.getNsPrefixURI("");
                    
 
            return ParseFile( LexManager,  MAOM,  UMM) ;                     
            
        }
        
        
        private Vector ParseFile(LexiconManager LexManager, MicroplansAndOrderingManager MAOM, UserModellingManager UMM)
        {
            Vector loadedResources = new Vector();
            
            HashSet<String> NLResourcesLoaded = new HashSet<String>();
            HashSet<String> OWLResourcesLoaded = new HashSet<String>();
            
           // load mapping 
            ExtendedIterator iter = (ExtendedIterator)LexManager.getEntries();

            while(iter!=null && iter.hasNext())
            {
                //logger.debug("-@->");
                Resource r = (Resource)iter.next();
                
                String owlResourceURI = r.getURI();
                                
                OWLResourcesLoaded.add(owlResourceURI);
                        
                if(r.getProperty(LexManager.hasNPProperty) !=null)
                {
                    //String NPuri = ((Resource)r.getProperty(LexManager.hasNPProperty).getObject().as(Resource.class)).getURI();
                    
                    StmtIterator it = r.listProperties(LexManager.hasNPProperty);
                    
                    while(it.hasNext())
                    {
                        Statement t = it.nextStatement();
                        String NPuri = ((Resource)t.getObject().as(Resource.class)).getURI();
                        AddMapping(owlResourceURI, NPuri);
                    }
                                        
                }
                
                if(r.getProperty(LexManager.hasCannedTextProperty) !=null)
                {
                    //String NPuri = ((Resource)r.getProperty(LexManager.hasNPProperty).getObject().as(Resource.class)).getURI();
                    
                    StmtIterator it = r.listProperties(LexManager.hasCannedTextProperty);
                    
                    while(it.hasNext())
                    {
                        Statement t = it.nextStatement();
                        String CTuri = ((Resource)t.getObject().as(Resource.class)).getURI();
                        AddMapping(owlResourceURI, CTuri);
                    }             
                }
                    
                //logger.debug(NPuri);
                
                
            }
            
            // load NP and canned texts list
            iter = (ExtendedIterator)LexManager.getNLResourceList();

            while(iter!=null && iter.hasNext())
            {                
                Resource r = (Resource)iter.next();
                
                String NLResURI = r.getURI(); 
                NLResourcesLoaded.add(NLResURI);
                //logger.debug("-@->" + NPuri);
                
                String type = r.getProperty(RDF.type).getObject().as(Resource.class).toString();
                
                if(type.compareTo(LexManager.NPResourceType.getURI())==0 ) // NP
                {
                    Lex_Entry_EN LE_EN = new Lex_Entry_EN(LexManager.getGender(NLResURI,Languages.ENGLISH), LexManager.getNum(NLResURI,Languages.ENGLISH), (LexManager.getCountable(NLResURI,Languages.ENGLISH).compareTo("true")==0) ? true: false  , 1);

                    LE_EN.set_sing_plural(LexManager.getN(NLResURI, LexManager.singularPrp, "" ,Languages.ENGLISH)
                                          ,LexManager.getN(NLResURI, LexManager.pluralPrp, "" ,Languages.ENGLISH));
                    
                    if(LexManager.getShortname(NLResURI, Languages.ENGLISH).compareTo("")!=0)
                    LE_EN.setShortname(LexManager.getShortname(NLResURI, Languages.ENGLISH));
                            
                    Lex_Entry_GR LE_GR = new Lex_Entry_GR(LexManager.getGender(NLResURI,Languages.GREEK), LexManager.getNum(NLResURI,Languages.GREEK), (LexManager.getCountable(NLResURI,Languages.GREEK).compareTo("true")==0) ? true: false  , 1);

                    LE_GR.set_singular_cases(LexManager.getN(NLResURI, LexManager.singularPrp, LexManager.nominativePrp ,Languages.GREEK), 
                                             LexManager.getN(NLResURI, LexManager.singularPrp, LexManager.genitivePrp ,Languages.GREEK) ,
                                             LexManager.getN(NLResURI, LexManager.singularPrp, LexManager.accusativePrp ,Languages.GREEK));
                            
                    LE_GR.set_plural_cases(LexManager.getN(NLResURI, LexManager.pluralPrp, LexManager.nominativePrp ,Languages.GREEK), 
                                             LexManager.getN(NLResURI, LexManager.pluralPrp, LexManager.genitivePrp ,Languages.GREEK) ,
                                             LexManager.getN(NLResURI, LexManager.pluralPrp, LexManager.accusativePrp ,Languages.GREEK));
                    
                    LE_GR.setInflected(LexManager.getInflected(NLResURI, Languages.GREEK));
                    
                    if(LexManager.getShortname(NLResURI, Languages.GREEK).compareTo("")!=0)
                    LE_GR.setShortname(LexManager.getShortname(NLResURI, Languages.GREEK));
                    
                    logger.debug("********************************");
                    LE_EN.print();
                    LE_GR.print();

                    NPList npList = new NPList(LE_EN, LE_GR);
                    //logger.debug(NPuri);
                    Lexicon.put(NLResURI, npList);
                }
                else if (type.compareTo(LexManager.CannedTextResourseType.getURI())==0) // CT
                {
                    StmtIterator it = r.listProperties(MAOM.ValProperty);
                    CannedList cannList = new CannedList();
                    
                    while(it.hasNext())
                    {                        
                        Statement stmt = it.nextStatement();                        
                        cannList.setCannedText(stmt.getLiteral().getString(), stmt.getLanguage());
                    }
                    
                    it = r.listProperties(UMM.forUserTypeProperty);
                    
                    while(it.hasNext())
                    {
                        Statement stmt = it.nextStatement();                        
                        cannList.addUserType( ((Resource)stmt.getObject().as(Resource.class)).getURI());                                                
                    }
                    
                    it = r.listProperties(LexManager.FillerAggrAllowedProperty);
                    
                    while(it.hasNext())
                    {
                        Statement stmt = it.nextStatement();                        
                        String FillAggrAllowed = stmt.getLiteral().getString();
                        
                        logger.debug(stmt.toString() + "FillAggrAllowed:" + FillAggrAllowed);
                        
                        if(FillAggrAllowed.compareTo("true")==0)
                            cannList.setFillerAggregationAllowed(true);
                        else
                            cannList.setFillerAggregationAllowed(false);
                        
                    }
                    
                    it = r.listProperties(LexManager.FOCUS_LOSTProperty);
                    
                    while(it.hasNext())
                    {
                        Statement stmt = it.nextStatement();                        
                        String foc_lost = stmt.getLiteral().getString();
                        
                        logger.debug(stmt.toString() + "foc_lost:" + foc_lost);
                        
                        if(foc_lost.compareTo("true")==0)
                        {
                            logger.debug("true");
                            cannList.setFOCUS_LOST(true);
                        }
                        else
                        {
                            logger.debug("false");
                            cannList.setFOCUS_LOST(false);
                        }
                        
                    }            
                    
                    CannedTextLexicon.put(NLResURI, cannList);
                }
                                
            }            
            
            loadedResources = new Vector();
                   
            loadedResources.add(NLResourcesLoaded);
            loadedResources.add(OWLResourcesLoaded);
            
             return loadedResources;
        }

        //-----------------------------------------------------------------------------------
        // for here
        public Iterator<String> getMappings(String owlResourceURi)
        {
            logger.debug("Mappings:" + owlResourceURi);
            if(LexiconMapping.containsKey(owlResourceURi))
            {
                return LexiconMapping.get(owlResourceURi).iterator();
            }
            else
            {
                return null;
            }
        }
        
        //Gerasimos Lampouras
        public void createReverseMappings()
        {
            ReverseLexiconMapping = new Hashtable();
                    
            for (String key : LexiconMapping.keySet())
            {
                HashSet<String> values = LexiconMapping.get(key);
                
                for (String value : values)
                {
                    if ((value!=null)&&(key!=null))
                    {
                        ReverseLexiconMapping.put(value, key);
                    }
                }
            }
        }
        
        public String getReverseMappings(String npURi)
        {
            logger.debug("ReverseMappings:" + npURi);
            if(ReverseLexiconMapping.containsKey(npURi))
            {
                return ReverseLexiconMapping.get(npURi);
            }
            else
            {
                return null;
            }
        }
        //Gerasimos Lampouras - over
        
        public Object getNLRes(String NLResID, int type)
        {
            if(type == 1)
            {
                return Lexicon.get(NLGLexiconNS + NLResID);
            }
            else if(type == 2)
            {                
                return CannedTextLexicon.get(NLGLexiconNS + NLResID);
            }
            else
            {
                return null;
            }
        }
        
        
        public Object getNLResByURI(String NLResURI, int type)
        {
            if(type == 1)
            {
                return Lexicon.get(NLResURI);
            }
            else if(type == 2)
            {                
                return CannedTextLexicon.get(NLResURI);
            }
            else
            {
                return null;
            }
        }
        
        public Iterator getNPs()
        {
            return Lexicon.keySet().iterator();
        }
        
        public void createCannedTextID(String ns, String resourcelocalName, String CannedTextID)
        {
            logger.debug("creating canned" + ns + " " + resourcelocalName + " " + CannedTextID);
            String owlResourceURI = ns + resourcelocalName;
            String NLResourceURI = NLGLexiconNS + CannedTextID;
            AddMapping(owlResourceURI, NLResourceURI);
            
            CannedList CL = new CannedList();
            CannedTextLexicon.put(NLResourceURI, CL);
        }
                
        public boolean containsNLResource(String NLResourceID, int t)
        {
            String NLResourceURI = NLGLexiconNS + NLResourceID;
            
            if(t == 1)
            {
                if(Lexicon.containsKey( NLResourceURI))
                    return true;
                else
                    return false;
            }
            else
            {
                if(CannedTextLexicon.containsKey( NLResourceURI))
                    return true;
                else
                    return false;                
            }        
        }
        
        public void DeleteNLResource(String owlResourceURI, String NLResourceID)
        {
            String NLResourceURI = NLResourceID;
               
            //remove mapping 
            if(LexiconMapping.containsKey(owlResourceURI))
            {
                HashSet set = LexiconMapping.get(owlResourceURI);
                
                if(set!=null)
                set.remove(NLResourceURI);
            }
                    
            //remove lexicon entry
            if(Lexicon.containsKey( NLResourceURI))
            {
                Lexicon.remove(NLResourceURI);      
            }
            
            //remove canned text lexicon entry
            if(CannedTextLexicon.containsKey( NLResourceURI))
            {
                CannedTextLexicon.remove(NLResourceURI);      
            }
        }
        
        public void RenameNLResource(String ns, String resourceName,String oldCannedTextID ,String CannedTextID)
        {
            String oldNLResourceURI = NLGLexiconNS + oldCannedTextID;
            String NLResourceURI = NLGLexiconNS + CannedTextID;
            
            String owlResourceURI = ns + resourceName;
               
            //update mapping 
            if(LexiconMapping.containsKey(owlResourceURI))
            {
                HashSet set = LexiconMapping.get(owlResourceURI);
                
                if(set != null)
                {
                    set.remove(oldNLResourceURI);
                    set.add(NLResourceURI);
                }
            }
                    
            //remove lexicon entry
            if(Lexicon.containsKey( NLResourceURI))
            {
                Object value = Lexicon.get(NLResourceURI);
                Lexicon.remove(oldNLResourceURI);      
                Lexicon.put(NLResourceURI, value);
            }
        }
        
        public void RenameUserType(String oldUT,String newUT)
        {
            Iterator<String> iter = CannedTextLexicon.keySet().iterator();
            while(iter.hasNext())
            {
                String key = iter.next();
                
                CannedList CL = (CannedList)CannedTextLexicon.get(key);
                
                if(CL!=null)
                {
                    CL.DeleteUserType(oldUT);
                    CL.addUserType(newUT);
                    CannedTextLexicon.put(key ,CL);
                }
            }                              
        }
               
        public void revoveCannedTexts(String ns, String objectLocalName)
        {
             String owlResourceURI = ns + objectLocalName;
            // get all the nl resources uris which are connected with the specified owlResourceURI         
             
            Iterator<String> nlresourcesIter = getMappings(owlResourceURI);
            Vector<String> v = new Vector<String>();
            
            //remove all canned texts
            if(nlresourcesIter != null)
            {
                while(  nlresourcesIter.hasNext())
                {
                    String nlResourceURI = nlresourcesIter.next();
                    v.add(nlResourceURI);
                }
            }
            
           for(int i = 0; i < v.size(); i++)
           {
                String nlResourceURI = v.get(i);
                
                if(CannedTextLexicon.containsKey(nlResourceURI))
                {
                    CannedTextLexicon.remove(nlResourceURI);
                    LexiconMapping.get(owlResourceURI).remove(nlResourceURI);         
                }   
           }
                       
        }
        
        public void saveCTToLexicon(String ns, String objectLocalName, Vector<CannedList> entries, Vector<String> CannedTextIDs)
        {            
            revoveCannedTexts( ns,  objectLocalName);
            
            String owlResourceURI = ns + objectLocalName;
            String suffix = "CT";
                                                
            for(int i = 0; i < CannedTextIDs.size(); i++)
            {      
                
                String NLResourceURI = NLGLexiconNS + objectLocalName + "-" + suffix + CannedTextIDs.get(i).toString();
                //System.err.println("owlResourceURI:" + owlResourceURI);
                //System.err.println("NLResourceURI:" + NLResourceURI);
                
                // update lexicon mapping
                if(LexiconMapping.containsKey(owlResourceURI))
                {
                    HashSet set = LexiconMapping.get(owlResourceURI);
                    set.add(NLResourceURI);
                    LexiconMapping.put(owlResourceURI, set);                
                }
                else
                {
                    HashSet set = new HashSet();
                    set.add(NLResourceURI);
                    LexiconMapping.put(owlResourceURI, set);
                }

                CannedTextLexicon.put( NLResourceURI, entries.get(i));               
            }
            
        }
                
        public void DeleteOwlResource(String ns, String objectLocalName)
        {
            String owlResourceURI = ns + objectLocalName;
                      
            // remove lexicon mapping
            if(LexiconMapping.containsKey(owlResourceURI))
            {
                HashSet set = LexiconMapping.get(owlResourceURI);
                
                if(!set.isEmpty())
                {
                    Iterator iter = set.iterator();
                    
                    while(iter.hasNext())
                    {
                        String NLlResourceURI = iter.next().toString();
                        Lexicon.remove(NLlResourceURI);
                    }
                }
                
                LexiconMapping.remove(owlResourceURI);                
            }
            
        }
        
        
        public void RenameOwlResource(String OLD_owlResourceURI,String  NEW_owlResourceURI)
        {
            
            // remove lexicon mapping
            if(LexiconMapping.containsKey(OLD_owlResourceURI))
            {
                HashSet set = LexiconMapping.get(OLD_owlResourceURI);                                
                
                //rename nl resources
                if(!set.isEmpty())
                {
                    Iterator iter = set.iterator();
                    while(iter.hasNext())
                    {
                        String NLResourceURI = iter.next().toString();
                        
                        if(Lexicon.containsKey(NLResourceURI))
                        {
                            Object p = Lexicon.get(NLResourceURI);
                            String NewOwlResourceLocalName = NEW_owlResourceURI.substring(NEW_owlResourceURI.indexOf("#")+1);
                                    
                            String newNLResourceURI = NLGLexiconNS + NewOwlResourceLocalName + "-" + "NP";     
                            
                            set.remove(NLResourceURI);
                            set.add(newNLResourceURI);
                            
                            Object t = Lexicon.remove(NLResourceURI);
                            Lexicon.put(newNLResourceURI, t);
                        }

                        if(CannedTextLexicon.containsKey(NLResourceURI))
                        {
                            Object p = CannedTextLexicon.get(NLResourceURI);
                            String suffix = NLResourceURI.substring(NLResourceURI.lastIndexOf("-")+1);
                            String NewOwlResourceLocalName = NEW_owlResourceURI.substring(NEW_owlResourceURI.indexOf("#")+1);
                            
                            String newNLResourceURI = NLGLexiconNS + NewOwlResourceLocalName + "-" + suffix; 
                        }
                        
                    }
                    
                }
                
                LexiconMapping.remove(OLD_owlResourceURI);                
                LexiconMapping.put(NEW_owlResourceURI, set);           
                
            }
            
        }        
        
        // save NP to lexicon
	public void saveNPToLexicon(String ns, String objectLocalName, Object entry)
        {
            logger.debug(ns + objectLocalName + "------>" + entry.toString());

            String owlResourceURI = ns + objectLocalName;
            String suffix = "NP";

            // update lexicon mapping
            if(LexiconMapping.containsKey(owlResourceURI))
            {
                HashSet set = LexiconMapping.get(owlResourceURI);
                set.add(NLGLexiconNS  + objectLocalName + "-" + suffix);
                LexiconMapping.put(owlResourceURI, set);                
            }
            else
            {
                HashSet set = new HashSet();
                set.add(NLGLexiconNS  + objectLocalName + "-" + suffix);
                LexiconMapping.put(owlResourceURI, set);
            }

            //update lexicon

            String NLResourceURI = NLGLexiconNS  + objectLocalName + "-" + suffix;

            if(entry instanceof Lex_Entry_EN || entry instanceof Lex_Entry_GR)
            {
                NPList npList = null;

                if(Lexicon.containsKey(NLResourceURI))
                {
                    npList = (NPList)Lexicon.get(NLResourceURI);
                }
                else
                {
                    npList = new  NPList ();
                }

                if(entry instanceof Lex_Entry_EN)
                    npList.setEntry(Languages.ENGLISH,entry);
                else
                    npList.setEntry(Languages.GREEK, entry);

                Lexicon.put( NLResourceURI, npList);
            }
                                
	}       


        //to here
        //-----------------------------------------------------------------------------------
        
        private void fooo()
        {
            if(EntriesLoadedFromCompLexicon.size() == 0)
            {
                EntriesLoadedFromCompLexicon.add(new HashSet<String>());
                EntriesLoadedFromCompLexicon.add(new HashSet<String>());
            }
        }
        
	public void writeLexicon(OntModel m, String path)
        {
            XMLDocWriter writer = new XMLDocWriter();
            
            XmlDocumentCreator docCreator = new XmlDocumentCreator();
                        
            Document lexDoc = docCreator.getNewDocument();
            Element rootRDF = lexDoc.createElement("rdf:RDF");
                        
            rootRDF.setAttribute( "xmlns:" + XmlMsgs.prefix , XmlMsgs.owlnlNS);
            rootRDF.setAttribute( "xmlns:" + "rdf" , "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
            rootRDF.setAttribute( "xmlns" ,  NLGLexiconNS);
            rootRDF.setAttribute( "xml:base" , NLGLexiconNS.substring(0, NLGLexiconNS.length()-1));
            
            lexDoc.appendChild(rootRDF);
                    
            Iterator keysIter = Lexicon.keySet().iterator();
            
            
            Element lexicon = lexDoc.createElement("owlnl"+ ":" + LexiconManager.LexiconRes);            
            rootRDF.appendChild(lexicon);
            Element NPListEl = lexDoc.createElement("owlnl"+ ":" + LexiconManager.NPListPrp);
            NPListEl.setAttribute("rdf:parseType", "Collection");
            lexicon.appendChild(NPListEl);
            
            while(keysIter.hasNext())
            {
                String NLResourceURI = keysIter.next().toString();
                
                fooo();
                        
                if( !((HashSet<String>)this.EntriesLoadedFromCompLexicon.get(0)).contains(NLResourceURI))
                {
                
                    logger.debug("NLResourceURI:" + NLResourceURI);
                    String localID = NLResourceURI.substring( NLResourceURI.indexOf("#") + 1);
                
                    Object obj = Lexicon.get(NLResourceURI);

                    if(obj instanceof NPList)   
                    {
                        NPList currentNPList = (NPList)obj;

                        Element NP = lexDoc.createElement("owlnl"+ ":" + LexiconManager.NPRes);
                        NPListEl.appendChild(NP);

                        NP.setAttribute("rdf:ID", localID);
                        Element LanguagesNP = lexDoc.createElement("owlnl"+ ":" + LexiconManager.LanguagesNPPrp);
                        LanguagesNP.setAttribute("rdf:parseType", "Collection");

                        NP.appendChild(LanguagesNP);

                        // greek
                        Element GreekNP = lexDoc.createElement("owlnl"+ ":" + LexiconManager.GreekNPRes);

                        Lex_Entry_GR lex_entry_gr = (Lex_Entry_GR)currentNPList.getEntry(Languages.GREEK);

                        if(lex_entry_gr==null)
                            lex_entry_gr = new Lex_Entry_GR(XmlMsgs.GENDER_NEUTER, XmlMsgs.PLURAL, false ,1);

                        Element countable = lexDoc.createElement("owlnl"+ ":" + LexiconManager.countablePrp);
                        countable.setTextContent(lex_entry_gr.getCountable() + "");
                        Element num = lexDoc.createElement("owlnl"+ ":" + LexiconManager.numPrp);
                        num.setTextContent(lex_entry_gr.get_num());
                        Element gender = lexDoc.createElement("owlnl"+ ":" + LexiconManager.genderPrp);
                        gender.setTextContent(lex_entry_gr.get_Gender());
                        Element infl = lexDoc.createElement("owlnl"+ ":" + LexiconManager.inflectedPrp);
                        infl.setTextContent(lex_entry_gr.getInflected() + "");
                        
                        GreekNP.appendChild(countable);
                        GreekNP.appendChild(num);
                        GreekNP.appendChild(gender);
                        GreekNP.appendChild(infl);
                        
                        if(!lex_entry_gr.get(XmlMsgs.NOMINATIVE_TAG, XmlMsgs.SINGULAR).equals("")
                        || !lex_entry_gr.get(XmlMsgs.GENITIVE_TAG, XmlMsgs.SINGULAR).equals("")
                        || !lex_entry_gr.get(XmlMsgs.ACCUSATIVE_TAG, XmlMsgs.SINGULAR).equals(""))
                        {                    
                            Element singular = lexDoc.createElement("owlnl"+ ":" + LexiconManager.singularPrp);                    
                            Element singForms = lexDoc.createElement("owlnl" + ":" + LexiconManager.singularFormsRes);

                            Element nomS = lexDoc.createElement("owlnl"+ ":" + LexiconManager.nominativePrp);
                            nomS.setTextContent(lex_entry_gr.get(XmlMsgs.NOMINATIVE_TAG, XmlMsgs.SINGULAR));
                            Element genS = lexDoc.createElement("owlnl"+ ":" + LexiconManager.genitivePrp);
                            genS.setTextContent(lex_entry_gr.get(XmlMsgs.GENITIVE_TAG, XmlMsgs.SINGULAR));
                            Element accuS = lexDoc.createElement("owlnl"+ ":" + LexiconManager.accusativePrp);
                            accuS.setTextContent(lex_entry_gr.get(XmlMsgs.ACCUSATIVE_TAG, XmlMsgs.SINGULAR));


                            singForms.appendChild(nomS);
                            singForms.appendChild(genS);
                            singForms.appendChild(accuS);

                            singular.appendChild(singForms);
                            GreekNP.appendChild(singular);
                        }

                        if(!lex_entry_gr.get(XmlMsgs.NOMINATIVE_TAG, XmlMsgs.PLURAL).equals("")
                        || !lex_entry_gr.get(XmlMsgs.GENITIVE_TAG, XmlMsgs.PLURAL).equals("")
                        || !lex_entry_gr.get(XmlMsgs.ACCUSATIVE_TAG, XmlMsgs.PLURAL).equals(""))
                        {                      
                            Element plural = lexDoc.createElement("owlnl"+ ":" + LexiconManager.pluralPrp);
                            Element plurForms = lexDoc.createElement("owlnl" + ":" + LexiconManager.pluralFormsRes);

                            Element nomP = lexDoc.createElement("owlnl"+ ":" + LexiconManager.nominativePrp);
                            nomP.setTextContent(lex_entry_gr.get(XmlMsgs.NOMINATIVE_TAG, XmlMsgs.PLURAL));
                            Element genP = lexDoc.createElement("owlnl"+ ":" + LexiconManager.genitivePrp);
                            genP.setTextContent(lex_entry_gr.get(XmlMsgs.GENITIVE_TAG, XmlMsgs.PLURAL));
                            Element accuP = lexDoc.createElement("owlnl"+ ":" + LexiconManager.accusativePrp);
                            accuP.setTextContent(lex_entry_gr.get(XmlMsgs.ACCUSATIVE_TAG, XmlMsgs.PLURAL));

                            plurForms.appendChild(nomP);
                            plurForms.appendChild(genP);
                            plurForms.appendChild(accuP);

                            plural.appendChild(plurForms);

                            GreekNP.appendChild(plural);
                        }

                        //english NP
                        Element EnglishNP = lexDoc.createElement("owlnl"+ ":" + LexiconManager.EnglishNPRes);

                        Lex_Entry_EN lex_entry_en = (Lex_Entry_EN)currentNPList.getEntry(Languages.ENGLISH);

                        if(lex_entry_en==null)
                            lex_entry_en = new Lex_Entry_EN(XmlMsgs.GENDER_NONPESRSONAL, XmlMsgs.PLURAL, false ,1);

                        Element countable2 = lexDoc.createElement("owlnl"+ ":" + LexiconManager.countablePrp);
                        countable2.setTextContent(lex_entry_en.getCountable() + "");
                        Element num2 = lexDoc.createElement("owlnl"+ ":" + LexiconManager.numPrp);
                        num2.setTextContent(lex_entry_en.get_num());
                        Element gender2 = lexDoc.createElement("owlnl"+ ":" + LexiconManager.genderPrp);
                        gender2.setTextContent(lex_entry_en.get_Gender());

                        EnglishNP.appendChild(countable2);
                        EnglishNP.appendChild(num2);
                        EnglishNP.appendChild(gender2);

                        Element singularEn = lexDoc.createElement("owlnl"+ ":" + LexiconManager.singularPrp);
                        singularEn.setTextContent(lex_entry_en.get(XmlMsgs.NOMINATIVE_TAG, XmlMsgs.SINGULAR));
                        Element pluralEn = lexDoc.createElement("owlnl"+ ":" + LexiconManager.pluralPrp);
                        pluralEn.setTextContent(lex_entry_en.get(XmlMsgs.NOMINATIVE_TAG, XmlMsgs.PLURAL));

                        EnglishNP.appendChild(singularEn);                            
                        EnglishNP.appendChild(pluralEn);                            

                        LanguagesNP.appendChild(GreekNP);
                        LanguagesNP.appendChild(EnglishNP);
                    }
                }
            }
            
            keysIter = CannedTextLexicon.keySet().iterator();
            
            while(keysIter.hasNext())
            {   
                String NLResourceURI = keysIter.next().toString();
                logger.debug("NLResourceURI:" + NLResourceURI);
                String localID = NLResourceURI.substring( NLResourceURI.indexOf("#") + 1);
                
                fooo();
                
                if( !((HashSet<String>)this.EntriesLoadedFromCompLexicon.get(0)).contains(NLResourceURI))
                {
                    Object obj = CannedTextLexicon.get(NLResourceURI);

                    if(obj instanceof CannedList)
                    {
                        CannedList Cl = (CannedList)obj;
                        Element CT_Node = lexDoc.createElement("owlnl"+ ":" + LexiconManager.CannedTextRes);                    
                        NPListEl.insertBefore(CT_Node, null);
                        CT_Node.setAttribute("rdf:ID", localID);

                        Element GreekCT = lexDoc.createElement("owlnl"+ ":" + "Val");                    
                        GreekCT.setAttribute("xml:lang", "el");
                        GreekCT.setTextContent(Cl.getCannedText(Languages.GREEK));

                        Element EnglishCT = lexDoc.createElement("owlnl"+ ":" + "Val");                    
                        EnglishCT.setAttribute("xml:lang", "en");
                        EnglishCT.setTextContent(Cl.getCannedText(Languages.ENGLISH));

                        CT_Node.appendChild(GreekCT);
                        CT_Node.appendChild(EnglishCT);

                        Vector v = Cl.getUserTypes();
                        for(int i = 0; i < v.size(); i++)
                        {
                            String ut = v.get(i).toString();
                            Element forUserType = lexDoc.createElement("owlnl"+ ":" + UserModellingManager.forUserTypePrp);
                            forUserType.setAttribute("rdf:resource", ut);

                            CT_Node.appendChild(forUserType);
                        }

                        Element FocLost = lexDoc.createElement("owlnl"+ ":" + LexiconManager.FOCUS_LOSTPrp);   
                        FocLost.setTextContent(Cl.getFOCUS_LOST()+"");
                        Element AggAllowed = lexDoc.createElement("owlnl"+ ":" + LexiconManager.FillerAggrAllowedPrp);
                        AggAllowed.setTextContent(Cl.getFillerAggregationAllowed()+"");

                        CT_Node.appendChild(FocLost);
                        CT_Node.appendChild(AggAllowed);
                    }                
                    
                }
               
            }
            
             keysIter = LexiconMapping.keySet().iterator();
             
             Element mapping = lexDoc.createElement("owlnl"+ ":" + LexiconManager.MappingRes);            
             rootRDF.appendChild(mapping);
             Element entries = lexDoc.createElement("owlnl"+ ":" + LexiconManager.entriesPrp);            
             entries.setAttribute("rdf:parseType", "Collection");
             mapping.appendChild(entries);
             
             while(keysIter.hasNext())
             {  
                String OWLResourceURI = keysIter.next().toString();             
                logger.debug("OWLResourceURI:" + OWLResourceURI);
                
                fooo();
                
                if( !((HashSet<String>)this.EntriesLoadedFromCompLexicon.get(1)).contains(OWLResourceURI))
                {
                
                    Individual ins = null;
                    if(m != null)
                    {
                        ins = m.getIndividual(OWLResourceURI);
                    }


                    Element OwlRes = null;

                    if(m.getOntClass(OWLResourceURI)==null)
                    {
                        OwlRes = lexDoc.createElement("owlnl"+ ":" + LexiconManager.owlInstanceRes); 
                    }
                    else
                    {
                        OwlRes = lexDoc.createElement("owlnl"+ ":" + LexiconManager.owlClassRes); 
                    }

                    OwlRes.setAttribute("rdf:about", OWLResourceURI);

                    entries.appendChild(OwlRes);

                    Iterator NlResourceIter = LexiconMapping.get(OWLResourceURI).iterator();

                    while(NlResourceIter.hasNext())
                    {
                        String NLResourceURi = NlResourceIter.next().toString();
                        String localID = NLResourceURi.substring( NLResourceURi.indexOf("#") + 1);

                        if(Lexicon.containsKey(NLResourceURi))
                        {
                            Element hasNP = lexDoc.createElement("owlnl"+ ":" + LexiconManager.hasNPPrp);
                            hasNP.setAttribute("rdf:resource",  "#"+localID);
                            OwlRes.appendChild(hasNP);
                        }
                        else
                        {
                            Element hasCanned = lexDoc.createElement("owlnl"+ ":" + LexiconManager.hasCannedTextPrp);
                            hasCanned.setAttribute("rdf:resource", "#"+localID);
                            OwlRes.appendChild(hasCanned);
                        }
                    }
                }
             }
            
            writer.saveDocToFile(lexDoc, path);
            //writer.saveDocToFile(lexDoc, "C:\\NLG_Project\\NLFiles-MPIRO\\moufaLexicon.rdf");
            
	} // write lexicon        
               
	//-----------------------------------------------------------------------------------
	public static void main(String args[])
        {    
             Logger loGGer = Logger.getLogger("gr.aueb.cs.nlg.NLFiles");
             loGGer.setLevel(Level.DEBUG);
             //BasicConfigurator.configure();
             logger.setLevel(Level.DEBUG);
             logger.debug("logger is working");
             System.err.println(logger.isDebugEnabled());
             //logger.setLevel(Level.INFO);
             logger.debug("logger is working");

             NLGLexiconQueryManager LQM = new NLGLexiconQueryManager();
             
             
             LQM.LoadLexicon("C:\\NaturalOWL\\NLFiles-MPIRO\\" , "Lexicon.rdf");
                

             LQM.printingNPs();
             //LQM.writeLexicon(null, "C:\\NLG_Project\\NLFiles-MPIRO\\test\\Lexicon.rdf");
             
             //LQM.LoadLexicon("C:\\NLG_Project\\NLFiles-MPIRO\\test\\" , "Lexicon.rdf");
	}	
        
        public void printingNPs()
        {
             Iterator NPIterator = getNPs();
             
             while(NPIterator.hasNext())
             {
                 String uri = NPIterator.next().toString();
                 System.err.println("uri:" + uri);
                 
                 //Lex_Entry_EN entry_en = (Lex_Entry_EN)((NPList)getNLResByURI(uri, 1)).getEntry(Languages.ENGLISH);
                 Lex_Entry_GR entry_gr = (Lex_Entry_GR)((NPList)getNLResByURI(uri, 1)).getEntry(Languages.GREEK);
                 
                 //System.err.println(entry_en.get(XmlMsgs.NOMINATIVE_TAG));
                 System.err.println(entry_gr.get(XmlMsgs.NOMINATIVE_TAG));
                 
             }
        }
}

