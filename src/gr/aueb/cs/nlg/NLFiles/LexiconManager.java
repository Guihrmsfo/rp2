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

import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.vocabulary.*;
import com.hp.hpl.jena.util.iterator.*;


import gr.aueb.cs.nlg.Languages.*;


import org.apache.log4j.Logger;


public class LexiconManager extends NLFileManager 
{        
    static Logger logger = Logger.getLogger("gr.aueb.cs.nlg.NLFiles.LexiconManager");
           
    // resources
    public static final String LexiconRes = "Lexicon";
    
    public static final String owlClassRes = "owlClass";
    public static final String owlInstanceRes = "owlInstance";
    
    public static final String NPRes = "NP";
    
    public static final String GreekNPRes = "GreekNP";
    public static final String EnglishNPRes = "EnglishNP";
    
    public static final String singularFormsRes = "singularForms";
    public static final String pluralFormsRes = "pluralForms";
    
    public static final String MappingRes = "Mapping";
    
    public static final String CannedTextRes = "CannedText";
    
    
    // properties
    public static final String entriesPrp = "Entries";
    
    public static final String countablePrp = "countable";
    public static final String inflectedPrp = "inflected";
    
    public static final String numPrp = "num";
    public static final String genderPrp = "gender";
    
    public static final String singularPrp = "singular";
    public static final String pluralPrp = "plural";
    
    public static final String hasNPPrp = "hasNP";
    public static final String LanguagesNPPrp = "LanguagesNP";
    public static final String NPListPrp = "NPList";
    
    public static final String nominativePrp = "nominative";
    public static final String genitivePrp = "genitive";
    public static final String accusativePrp = "accusative";
    
    public static final String hasCannedTextPrp = "hasCannedText";
    
    public static final String ShortnamePrp = "Shortname";
    public static final String FillerAggrAllowedPrp = "FillerAggrAllowed";
    public static final String FOCUS_LOSTPrp = "FOCUS_LOST";
    public static final String PrepPrp = "Prep";
    
    // resources
    public static Resource LexiconResourceType = null;
    public static Resource owlClassResourceType= null;
    public static Resource owlInstanceResourceType = null;
    public static Resource NPResourceType = null;
    public static Resource GreekNPResourceType = null;
    public static Resource EnglishNPResourceTypes = null;
    public static Resource singularFormsResourceType = null;
    public static Resource pluralFormsResourceType = null;
    public static Resource MappingResourceType = null;
    
    public static Resource CannedTextResourseType = null;
    
    
    // properties
    public static Property entriesProperty = null;
    public static Property countableProperty = null;
    public static Property inflectedProperty = null;
    
    public static Property numProperty = null;
    public static Property genderProperty = null;
    public static Property singularProperty = null;
    public static Property pluralProperty = null;
    public static Property hasNPProperty = null;
    public static Property LanguagesNPProperty = null;
    public static Property NPListProperty = null;
    
    public static Property nominativeProperty = null;
    public static Property genitiveProperty = null;
    public static Property accusativeProperty = null;
    
    public static Property hasCannedTextProperty = null;
    
    public static Property ShortnameProperty = null;
    public static Property FillerAggrAllowedProperty = null;
    public static Property FOCUS_LOSTProperty = null;
    public static Property PrepProperty = null;
    
    //----------------------------------------------------------------------------
    public LexiconManager(String xb) {
        super(xb);        
        
        // define the rdf:type resources
        LexiconResourceType = model.createResource(owlnlNS + this.LexiconRes); // Lexicon
        owlClassResourceType = model.createResource(owlnlNS + this.owlClassRes); //owlClass
        owlInstanceResourceType = model.createResource(owlnlNS + this.owlInstanceRes); //owlInstance
        NPResourceType = model.createResource(owlnlNS + this.NPRes); //NPResource
        GreekNPResourceType =  model.createResource(owlnlNS + this.GreekNPRes); //GreekNP
        EnglishNPResourceTypes =  model.createResource(owlnlNS + this.EnglishNPRes); //EnglishNP
        singularFormsResourceType =  model.createResource(owlnlNS + this.singularFormsRes);
        pluralFormsResourceType =  model.createResource(owlnlNS + this.pluralFormsRes);
        MappingResourceType = model.createResource(owlnlNS + this.MappingRes); //Mapping
        CannedTextResourseType = model.createResource(owlnlNS + this.CannedTextRes); //
        
        
        //define properties
        entriesProperty = model.createProperty(owlnlNS + this.entriesPrp); //Entries
        countableProperty = model.createProperty(owlnlNS + this.countablePrp); //countable
        inflectedProperty = model.createProperty(owlnlNS + this.inflectedPrp); // inflected
        numProperty = model.createProperty(owlnlNS + this.numPrp); //num
        genderProperty = model.createProperty(owlnlNS + this.genderPrp); //gender
        singularProperty = model.createProperty(owlnlNS + this.singularPrp); //singular
        pluralProperty = model.createProperty(owlnlNS + this.pluralPrp); //plural
        hasNPProperty = model.createProperty(owlnlNS + this.hasNPPrp); //hasNP
        LanguagesNPProperty = model.createProperty(owlnlNS + this.LanguagesNPPrp); //LanguagesNP
        NPListProperty = model.createProperty(owlnlNS + this.NPListPrp);
        nominativeProperty = model.createProperty(owlnlNS + this.nominativePrp); //nominative
        genitiveProperty = model.createProperty(owlnlNS + this.genitivePrp); //genitive
        accusativeProperty = model.createProperty(owlnlNS + this.accusativePrp); // accusative
        ShortnameProperty = model.createProperty(owlnlNS + this.ShortnamePrp); //
        
        hasCannedTextProperty = model.createProperty(owlnlNS + this.hasCannedTextPrp);
        FillerAggrAllowedProperty = model.createProperty(owlnlNS + this.FillerAggrAllowedPrp);
        FOCUS_LOSTProperty = model.createProperty(owlnlNS + this.FOCUS_LOSTPrp);
        PrepProperty = model.createProperty(owlnlNS + this.PrepPrp);
        //this.setPrettyTypes(new Resource[]{LexiconResourceType, model.createResource(owlnlNS + "Mapping"),});
    }
    
    //----------------------------------------------------------------------------
    // get lexicon entries
    public ExtendedIterator getEntries(){
        
        StmtIterator iter = model.listStatements(
                new SimpleSelector(null, this.entriesProperty , (RDFNode)null ));
        
        if(iter.hasNext()){
            Statement stmt = iter.nextStatement();
            RDFList EntriesList  = (RDFList)stmt.getObject().as(RDFList.class);
            
            //logger.debug(stmt);
            //logger.debug(stmt.getSubject().getProperty(RDF.type).toString());
            //logger.debug( ((Resource)stmt.getObject().as(Resource.class)).toString() );
            
            return EntriesList.iterator();
        } else
            return null;
    }
    
    //----------------------------------------------------------------------------
    public ExtendedIterator getNLResourceList()
    {
        StmtIterator iter = model.listStatements(null, this.NPListProperty , (RDFNode)null );
        
        if(iter.hasNext())
        {
            Statement stmt = iter.nextStatement();
            RDFList NPList  = (RDFList)stmt.getObject().as(RDFList.class);
            
            //logger.debug(stmt);
            //logger.debug(stmt.getSubject().getProperty(RDF.type).toString());
            //logger.debug( ((Resource)stmt.getObject().as(Resource.class)).toString() );
            
            return NPList.iterator();
        }
        else
        {
            return null;
        }
    }
    
    //----------------------------------------------------------------------------
    public Resource getNPForLanguage(Resource NP, String lang){
        logger.debug("-->" + NP.toString());
        
        StmtIterator iter = model.listStatements(NP,  this.LanguagesNPProperty , (RDFNode)null);
        Statement stmt = iter.nextStatement();
        
        RDFList NPList = (RDFList)stmt.getObject().as(RDFList.class);
        
        String NPRes = "";
        if(lang.compareTo(Languages.GREEK)==0){
            NPRes = GreekNPRes;
        } else if (lang.compareTo(Languages.ENGLISH)==0){
            NPRes = EnglishNPRes;
        }
        
        for(int i = 0; i < NPList.size(); i++){
            Resource res = (Resource)NPList.get(i).as(Resource.class);
            String locName = ((Resource)res.getProperty(RDF.type).getObject().as(Resource.class)).getLocalName();
            
            //logger.debug("locName:" + locName);
            
            if(locName.compareTo(NPRes)==0){
                //logger.debug("returned");
                return (Resource)NPList.get(i).as(Resource.class);
            }
        }
        
        return null;
        
    }
    //----------------------------------------------------------------------------
    public String getCountable(String entryURI, String lang)
    {
        Resource NP = model.getResource(entryURI);
        
        if(NP != null) 
        {
            if(Languages.isGreek(lang)) 
            {
                Resource greekNP = getNPForLanguage(NP, Languages.GREEK);
                return greekNP.getProperty(this.countableProperty).getLiteral().getString();
            } 
            else if(Languages.isEnglish(lang)) 
            {
                Resource englishNP = getNPForLanguage(NP, Languages.ENGLISH);
                return englishNP.getProperty(this.countableProperty).getLiteral().getString();
            }
        }
        
        return "";
    }
    //----------------------------------------------------------------------------
    public String getNum(String entryURI, String lang) {
        Resource NP = model.getResource(entryURI);
        
        if(NP != null){
            if(Languages.isGreek(lang)) {
                Resource greekNP = getNPForLanguage(NP, Languages.GREEK);
                return greekNP.getProperty(this.numProperty).getLiteral().getString();
            } else if(Languages.isEnglish(lang)) {
                Resource englishNP = getNPForLanguage(NP, Languages.ENGLISH);
                return englishNP.getProperty(this.numProperty).getLiteral().getString();
            }
        }
        
        return "";
    }
    //----------------------------------------------------------------------------
    public String getGender(String entryURI, String lang) {
        Resource NP = model.getResource(entryURI);
        
        if(NP != null) {
            if(Languages.isGreek(lang)) {
                Resource greekNP = getNPForLanguage(NP, Languages.GREEK);
                return greekNP.getProperty(this.genderProperty).getLiteral().getString();
            } else if(Languages.isEnglish(lang)) {
                Resource englishNP = getNPForLanguage(NP, Languages.ENGLISH);
                return englishNP.getProperty(this.genderProperty).getLiteral().getString();
            }
        }
        
        return "";
    }
    
    public boolean getInflected(String entryURI, String lang) {
        Resource NP = model.getResource(entryURI);
        
        if(NP != null) 
        {
            if(Languages.isGreek(lang)) 
            {
                Resource greekNP = getNPForLanguage(NP, Languages.GREEK);
                
                if(greekNP.getProperty(this.inflectedProperty) != null)
                {
                    if(greekNP.getProperty(this.inflectedProperty).getLiteral().getString().equals("true"))
                        return true;
                    else
                        return false;
                }
                
            } 
        }
        
        return true;
    }
    
    public String getShortname(String entryURI, String lang) {
        Resource NP = model.getResource(entryURI);
        
        if(NP != null) {
            if(Languages.isGreek(lang)) {
                Resource greekNP = getNPForLanguage(NP, Languages.GREEK);
                Statement stmt = greekNP.getProperty(this.ShortnameProperty);
                
                if(stmt != null)
                    return stmt.getLiteral().getString();
            } else if(Languages.isEnglish(lang)) {
                Resource englishNP = getNPForLanguage(NP, Languages.ENGLISH);
                Statement stmt = englishNP.getProperty(this.ShortnameProperty);
                
                if(stmt != null)
                    return stmt.getLiteral().getString();
            }
        }
        
        return "";
    }
    
    public String getN(String entryURI, String Number, String Case,String lang){
        Resource NP = model.getResource(entryURI);
        
        if(NP != null) {
            if(lang.compareTo(Languages.GREEK)==0) {//GREEk
                
                //logger.debug("GREEk");
                
                Resource greekNP = getNPForLanguage(NP, Languages.GREEK);
                Resource Forms = null;
                
                if(Number.compareTo(this.singularPrp) == 0) {
                    Statement stmt = greekNP.getProperty(this.singularProperty);
                    if(stmt!=null)
                        Forms = (Resource)stmt.getObject().as(Resource.class);
                } else if(Number.compareTo(this.pluralPrp) == 0) {
                    Statement stmt = greekNP.getProperty(this.pluralProperty);
                    if(stmt!=null)
                        Forms = (Resource)stmt.getObject().as(Resource.class);
                }
                
                if(Forms!=null) {
                    if(Case.compareTo(this.nominativePrp) == 0) {
                        return Forms.getProperty(this.nominativeProperty).getLiteral().getString();
                    } else if(Case.compareTo(this.genitivePrp) == 0) {
                        return Forms.getProperty(this.genitiveProperty).getLiteral().getString();
                    } else if(Case.compareTo(this.accusativePrp) == 0) {
                        return Forms.getProperty(this.accusativeProperty).getLiteral().getString();
                    }
                }
            }//GREEK
            else if(lang.compareTo(Languages.ENGLISH)==0) {//ENGLISH
                
                //logger.debug("ENGLISh");
                
                Resource englishNP = getNPForLanguage(NP, Languages.ENGLISH);
                
                if(Number.compareTo(this.singularPrp) == 0) {
                    Statement stmt = englishNP.getProperty(this.singularProperty);
                    if(stmt!=null)
                        return stmt.getLiteral().getString();
                } else if(Number.compareTo(this.pluralPrp) == 0) {
                    Statement stmt = englishNP.getProperty(this.pluralProperty);
                    if(stmt!=null)
                        return stmt.getLiteral().getString();
                }
            }//ENGLISH
        }
        
        return "";
    }
    //----------------------------------------------------------------------------
    public static void main(String args[]){//main
        
        LexiconManager LexManager = new LexiconManager("");
        LexManager.read("...." , "Lexicon.rdf");
/*
        String uri = "http://www.w3.org/TR/2003/CR-owl-guide-20030818/wine#Wine";
 
        //Resource entry = LexManager.getEntry(uri);
        //logger.debug(entry.getURI());
 
        logger.debug(LexManager.getNum(uri,Languages.GREEK));
        logger.debug(LexManager.getNum(uri,Languages.ENGLISH));
 
        logger.debug(LexManager.getCountable(uri,Languages.GREEK));
        logger.debug(LexManager.getCountable(uri,Languages.ENGLISH));
 
        logger.debug(LexManager.getGender(uri,Languages.GREEK));
        logger.debug(LexManager.getGender(uri,Languages.ENGLISH));
 
 
        logger.debug(LexManager.getN(uri,LexiconManager.singularPrp,"",Languages.ENGLISH));
 */
        
        ExtendedIterator iter = (ExtendedIterator)LexManager.getEntries();
        
        while(iter.hasNext()){
            logger.debug("-@->");
            Resource m = (Resource)iter.next();
            String NPuri = ((Resource)m.getProperty(LexiconManager.hasNPProperty).getObject().as(Resource.class)).getURI();
            logger.debug(NPuri);
            
            String n = LexManager.getN(NPuri,LexManager.singularPrp, LexManager.nominativePrp,Languages.ENGLISH);
            logger.debug("n" + n);
        }
        
        LexManager.CloseModel();
    }
}
