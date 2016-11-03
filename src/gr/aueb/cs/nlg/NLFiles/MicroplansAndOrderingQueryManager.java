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

import java.util.*; 

import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.vocabulary.*;
import com.hp.hpl.jena.util.iterator.*;

import org.w3c.dom.*;

import gr.aueb.cs.nlg.*;
import gr.aueb.cs.nlg.Languages.*;
import gr.demokritos.iit.PServer.*;
import gr.aueb.cs.nlg.Utils.*;
       
import org.apache.log4j.Logger;


public class MicroplansAndOrderingQueryManager
{       
    static Logger logger = Logger.getLogger("gr.aueb.cs.nlg.NLFiles.MicroplansAndOrderingQueryManager");
    
    public  String NLGMicroplansAndOrderingNS = "http://www.aueb.gr/users/ion/owlnl/Microplans#";
    public  List<String> PropertiesUsedForComparisons;
            
    private String MicroplansAndOrderingFileName;
    private Hashtable<String, MicroplansAndOrderingNode> MicroplansAndOrderingHashtable;
    //  MicroplansAndOrderingHashtable  [ key: property URI |value: MicroplansAndOrderingNode]
   
    MicroplansAndOrderingManager MicroAndOrdManager;
            
    public final static int NumOfMicroplan = 5;    
        
    //constructor
    public MicroplansAndOrderingQueryManager (){        
           		
    }
    
    //Load microplans and ordering info
    public void init()
    {
         NLGMicroplansAndOrderingNS = "http://www.aueb.gr/users/ion/owlnl/Microplans#";
         MicroplansAndOrderingHashtable = new Hashtable<String, MicroplansAndOrderingNode>();    
         PropertiesUsedForComparisons = new LinkedList<String>();
    }
    
     public void LoadMicroplansAndOrdering(String path, String file)
     {
        //LoadMicroplansAndOrdering
        init();
         
        MicroAndOrdManager = new MicroplansAndOrderingManager("");
        MicroAndOrdManager.read(path, file);
        NLGMicroplansAndOrderingNS = MicroAndOrdManager.model.getNsPrefixURI("");
            
        if(NLGMicroplansAndOrderingNS == null)
            NLGMicroplansAndOrderingNS = "http://www.aueb.gr/users/ion/owlnl/Microplans#";
        
        ExtendedIterator PropertiesIter = MicroAndOrdManager.get(MicroAndOrdManager.PropertiesProperty);
        Model m = MicroAndOrdManager.getModel();
                
        if(PropertiesIter != null)
        {
             while(PropertiesIter.hasNext())
             {//for each property
                Resource prp = (Resource)PropertiesIter.next();
                String prpURI = prp.getURI();
                
                logger.debug("Property URI:" + prpURI);
                
                String order = prp.getProperty(MicroAndOrdManager.OrderProperty).getLiteral().getString(); // !!!
                
                String usedForComparisons = "false";
                if(prp.getProperty(MicroAndOrdManager.UsedForComparisonsProperty) != null)
                usedForComparisons = prp.getProperty(MicroAndOrdManager.UsedForComparisonsProperty).getLiteral().getString(); // !!!
                
                MicroplansList GreekMicroplanList = new MicroplansList(Languages.GREEK); //!!!
                MicroplansList EnglishMicroplanList = new MicroplansList(Languages.ENGLISH); //!!!
                
                StmtIterator GreekMicropIter = m.listStatements(prp, MicroAndOrdManager.GreekMicroplansProperty, (RDFNode)null);

                if(GreekMicropIter.hasNext())
                {
                    Statement t = GreekMicropIter.nextStatement();
                    ExtendedIterator GreekMicropListIter = ((RDFList)t.getObject().as(RDFList.class)).iterator();       
                    
                    while(GreekMicropListIter != null && GreekMicropListIter.hasNext()){
                        Resource microplan = (Resource)GreekMicropListIter.next();               
                        GreekMicroplanList.add(createMicroplan(microplan, Languages.GREEK));
                    }
                }
                
                
                StmtIterator EnglishMicropIter = m.listStatements(prp, MicroAndOrdManager.EnglishMicroplansProperty, (RDFNode)null);
                
                if(EnglishMicropIter.hasNext())
                {
                    Statement t = EnglishMicropIter.nextStatement();
                    ExtendedIterator EnglishMicropListIter = ((RDFList)t.getObject().as(RDFList.class)).iterator();  
                    
                    while(EnglishMicropListIter != null && EnglishMicropListIter.hasNext()){
                       Resource microplan = (Resource)EnglishMicropListIter.next();
                       EnglishMicroplanList.add(createMicroplan(microplan, Languages.ENGLISH));
                    }                    
                }

                if(usedForComparisons.equals("true"))
                PropertiesUsedForComparisons.add(prp.getLocalName()); 
                
                MicroplansAndOrderingNode MAON = new MicroplansAndOrderingNode(Integer.parseInt(order), usedForComparisons.equals("true")?true:false,GreekMicroplanList,EnglishMicroplanList );
                MicroplansAndOrderingHashtable.put(prpURI, MAON);

            }//for each property
        }
        else{
            logger.debug("ERROR: i have not found properties");
        }

     }//LoadMicroplansAndOrdering
     
     private Microplan createMicroplan(Resource microplan, String lang)
     {
         String MicroName = microplan.getProperty(MicroplansAndOrderingManager.MicroplanNameProperty).getLiteral().getString();
         String IsUsed = microplan.getProperty(MicroplansAndOrderingManager.MicroplanUsedProperty).getLiteral().getString();
         
         String AggAllowed="";
         if(microplan.getProperty(MicroplansAndOrderingManager.AggrAllowedProperty)!=null)
         {
            AggAllowed = microplan.getProperty(MicroplansAndOrderingManager.AggrAllowedProperty).getLiteral().getString();
         }
         else
         {
             AggAllowed = "true";
         }
         
         Vector<NLGSlot> slots = createSlots(microplan);
         
         Microplan m = new Microplan(slots,MicroName, (IsUsed.compareTo("true")==0) ? true : false , microplan.getURI(), (AggAllowed.compareTo("true")==0) ? true : false, lang);
         m.print();
         return m;
     }
     
     public Vector<NLGSlot> createSlots(Resource microplan)
     {
         Vector<NLGSlot> slots = new Vector<NLGSlot>();
         RDFList list = (RDFList)microplan.getProperty(MicroplansAndOrderingManager.MicroplanSlotsProperty).getObject().as(RDFList.class);
         
         ExtendedIterator slotIter = list.iterator();
         
         while(slotIter.hasNext())
         {
             //logger.debug("adding Slot");
             Resource slot = (Resource)slotIter.next();
             Resource slotType = ((Resource)slot.getProperty(RDF.type).getObject().as(Resource.class));      
            
             if(slotType.getURI().compareTo(MicroplansAndOrderingManager.OwnerResourseType.getURI())==0){
                //logger.debug("adding PropertySlot");
                PropertySlot PS = new PropertySlot( slot.getProperty(MicroplansAndOrderingManager.caseProperty).getLiteral().getString(), PropertySlot.OWNER, slot.getProperty(MicroplansAndOrderingManager.RETYPEProperty).getLiteral().getString());
                
                if(slot.getProperty(MicroplansAndOrderingManager.RETYPEProperty) !=null)
                    PS.setPropertyREType(slot.getProperty(MicroplansAndOrderingManager.RETYPEProperty).getLiteral().getString());
                else
                    PS.setPropertyREType(XmlMsgs.RE_AUTO);
                slots.add(PS);
             }
             else if(slotType.getURI().compareTo(MicroplansAndOrderingManager.FillerResourseType.getURI())==0){
                //logger.debug("adding PropertySlot"); 
                PropertySlot PS = new PropertySlot( slot.getProperty(MicroplansAndOrderingManager.caseProperty).getLiteral().getString(), PropertySlot.FILLER, slot.getProperty(MicroplansAndOrderingManager.RETYPEProperty).getLiteral().getString()); 
                
                if(slot.getProperty(MicroplansAndOrderingManager.RETYPEProperty) !=null)
                    PS.setPropertyREType(slot.getProperty(MicroplansAndOrderingManager.RETYPEProperty).getLiteral().getString());
                else
                    PS.setPropertyREType(XmlMsgs.RE_AUTO);
                slots.add(PS);
             }
             else if(slotType.getURI().compareTo(MicroplansAndOrderingManager.VerbResourseType.getURI())==0){
                 //logger.debug("adding VerbSlot");
                 
                 String plurarV = "@VERB@";
                 if(slot.getProperty(MicroplansAndOrderingManager.pluralValProperty)!=null)
                 {
                     plurarV = slot.getProperty(MicroplansAndOrderingManager.pluralValProperty).getLiteral().getString();
                 }
                 
                 VerbSlot VS = new VerbSlot(slot.getProperty(MicroplansAndOrderingManager.ValProperty).getLiteral().getString(),
                         plurarV,
                         slot.getProperty(MicroplansAndOrderingManager.voiceProperty).getLiteral().getString(),
                         slot.getProperty(MicroplansAndOrderingManager.tenseProperty).getLiteral().getString());
                 slots.add(VS);
             }
             else if(slotType.getURI().compareTo(MicroplansAndOrderingManager.TextResourseType.getURI())==0){
                 //logger.debug("adding TextSlot");
                 StringSlot SS = new StringSlot(slot.getProperty(MicroplansAndOrderingManager.ValProperty).getLiteral().getString()); 
                 slots.add(SS);
             }
             else if(slotType.getURI().compareTo(MicroplansAndOrderingManager.PrepResourseType.getURI())==0){
                 //logger.debug("adding PrepSlot");
                 StringSlot PS = new StringSlot(slot.getProperty(MicroplansAndOrderingManager.ValProperty).getLiteral().getString()); 
                 PS.isPreposition = true;
                 slots.add(PS);                 
             }
             else if(slotType.getURI().compareTo(MicroplansAndOrderingManager.AdverbResourseType.getURI())==0){
                 //logger.debug("adding PrepSlot");
                 StringSlot PS = new StringSlot(slot.getProperty(MicroplansAndOrderingManager.ValProperty).getLiteral().getString()); 
                 PS.isAdverb = true;
                 slots.add(PS);                 
             }             
         }
         
         return slots;
         
     }
    //-----------------------------------------------------------------------------------
    
    /*  Query functions */
    
    // get the order for the specified property
    public int getOrder(String PrpURI)
    {
        //logger.debug("Looking order for property: " + PrpURI);
        
        MicroplansAndOrderingNode MAON = MicroplansAndOrderingHashtable.get(PrpURI);
        if(MAON != null)
        {
            return MAON.getOrder();
        }
        else
        { 
            return 100;
        }
    }

    // get the UsedForComparisons for the specified property
    public boolean getUsedForComparisons(String PrpURI)
    {
            MicroplansAndOrderingNode MAON = MicroplansAndOrderingHashtable.get(PrpURI);                
            
            if(MAON != null)
            {
                return MAON.getUsedForComparisons();
            }
            else
            {
                return false;
            }
    }
    
    
    // set the UsedForComparisons for the specified property
    public void setUsedForComparisons(String PrpURI, boolean b)
    {
            MicroplansAndOrderingNode MAON = MicroplansAndOrderingHashtable.get(PrpURI);                
            
            if(MAON != null)
            {
                MAON.setUsedForComparisons(b);
            }
    }
    
    public boolean getisAggAllowed(String PrpURI, String MicroplanURI, String language)
    {
        try
        {
            MicroplansAndOrderingNode MAON = MicroplansAndOrderingHashtable.get(PrpURI);                
            
            if(MAON != null)
            {
                MicroplansList ML = MAON.getMicroplans(language);

                for(int i = 0; i < ML.size(); i++)
                {
                    if(ML.getMicroplan(i).getMicroplanURI().compareTo(MicroplanURI) == 0)
                    {
                        return ML.getMicroplan(i).getAggAllowed();
                    }
                }
            }
            else 
            {
                return false;
            }
                        
        }//try
        catch(Exception e)
        {
            e.printStackTrace();            
        }   
        return false;         
    }
    
    public void setOrder(String PrpURI, int ord)
    {
        MicroplansAndOrderingNode MAON = MicroplansAndOrderingHashtable.get(PrpURI);
        if(MAON != null)
        {
             logger.debug(PrpURI + " " + ord);
             MAON.setOrder(ord);        
        }
    }
    
    public String getMicroplanName()
    {
        return "";
    }    
    
    
    //return 
    public boolean getIsUsed(String PrpURI, String MicroplanURI, String language)
    {
        try
        {
            MicroplansAndOrderingNode MAON = MicroplansAndOrderingHashtable.get(PrpURI);                
            
            if(MAON != null)
            {
                MicroplansList ML = MAON.getMicroplans(language);

                for(int i = 0; i < ML.size(); i++)
                {
                    if(ML.getMicroplan(i).getMicroplanURI().compareTo(MicroplanURI) == 0)
                    {
                        return ML.getMicroplan(i).getIsUsed();
                    }
                }
            }
            else 
            {
                return false;
            }
                        
        }//try
        catch(Exception e)
        {
            e.printStackTrace();            
        }   
        return false;            
        
    }
    
    // get a list of slots for the specified microplan
    public Microplan getMicroplan(String PrpURI, String MicroplanName, String language)
    {
        try
        {            
            logger.debug(PrpURI + " , " +  MicroplanName + " , " + language);
            MicroplansAndOrderingNode MAON = MicroplansAndOrderingHashtable.get(PrpURI);                
            
            if(MAON != null)
            {
                MicroplansList ML = MAON.getMicroplans(language);

                for(int i = 0; i < ML.size(); i++)
                {
                    if(ML.getMicroplan(i).getMicroplanName().compareTo(MicroplanName) == 0)
                    {
                        return ML.getMicroplan(i);
                    }
                }
            }
            else
            { 
                logger.debug(PrpURI + "not found");
                return null;
            }
                        
        }//try
        catch(Exception e){
            e.printStackTrace();            
        }   
        return null;        
    }
    
    // get a list of slots for the specified microplan
    public Vector<NLGSlot> getSlots(String PrpURI, String MicroplanName, String language){
        try{
            
            logger.debug(PrpURI + " , " +  MicroplanName + " , " + language);
            MicroplansAndOrderingNode MAON = MicroplansAndOrderingHashtable.get(PrpURI);                
            
            if(MAON != null){
                MicroplansList ML = MAON.getMicroplans(language);

                for(int i = 0; i < ML.size(); i++){
                    if(ML.getMicroplan(i).getMicroplanName().compareTo(MicroplanName) == 0){
                        return ML.getMicroplan(i).getSlotsList();
                    }
                }
            }
            else{ 
                logger.debug(PrpURI + "not found");
                return null;
            }
                        
        }//try
        catch(Exception e){
            e.printStackTrace();            
        }   
        return null;        
    }
       
    // chooces  a microplan randomly
    public String chooseMicroplan(String prpURI, String language) throws InvalidLanguageException
    {//chooseMicroplan    
        
        try
        {
            MicroplansAndOrderingNode MAON = MicroplansAndOrderingHashtable.get(prpURI);                

            if(MAON != null){
                MicroplansList ML = MAON.getMicroplans(language);

                int size = ML.size();  // size of microplan list 
                int used_templates = 0;

                // count active microplans	
                for(int i = 0; i < size; i++){                         
                    Microplan microp = ML.getMicroplan(i);                                
                        if(microp.getIsUsed()){
                            used_templates++;
                        }
                }

                // calculate its index randomly
                int index  = (int)Math.floor( 1 + (Math.random()* used_templates) ); // [0.0 , 1.0) -
                //logger.debug("index :" +index + " used templates " + used_templates + " size " + size);

                int used_templates_index = 0;

                for(int i = 0; i < size; i++){                         
                    Microplan microp = ML.getMicroplan(i);                                
                        if(microp.getIsUsed()){                            
                            used_templates_index ++;                                

                            if(used_templates_index == index){
                                return microp.getMicroplanName();
                            }
                        }
                }
        }
        else 
            return "MICROPLAN NOT FOUND";    
          
        }
        catch(InvalidLanguageException e){
            e.printStackTrace();
        }
        
        return "MICROPLAN NOT FOUND";  
    }//chooseMicroplan
    
    // chooces  a microplan randomly
    public String chooseMicroplan(String prpURI, String language, UMVisit UMVis, String UserID, String UserType) throws InvalidLanguageException
    {//chooseMicroplan    
        
        try
        {
            if( UserID != null && UMVis != null)
            {
                MicroplansAndOrderingNode MAON = MicroplansAndOrderingHashtable.get(prpURI);                

                if(MAON != null)
                {
                    MicroplansList ML = MAON.getMicroplans(language);

                    int size = ML.size();  // size of microplan list 
                    int used_templates = 0;
                    float MicroplansInfo  [][] = new float [4][5];

                    for(int i = 0; i < 4; i++)
                    {
                        for(int j = 0; j < 5; j++)
                        {
                            MicroplansInfo[0][j] = -1;    
                            MicroplansInfo[1][j] = 0;
                            MicroplansInfo[2][j] = 0;
                            MicroplansInfo[3][j] = 0;
                        }
                    }

                    for(int j = 0; j < size; j++)
                    {
                        Microplan microp = ML.getMicroplan(j);
                        if(microp.getIsUsed())
                        {
                            used_templates++;
                            MicroplansInfo[0][j] = 1;      // is used
                            String microID = prpURI + "-" + microp.getMicroplanName() + "-" + language;
                            
                            logger.debug("microID:" + microID);
                            
                            MicroplansInfo[1][j] = UMVis.getMicroPlanningCount(microID, UserID, UserType);
                            MicroplansInfo[2][j] = UMVis.getMicroPlanningAppropriateness(microID, UserID, UserType);  
                            
                            logger.debug(MicroplansInfo[1][j] + "###" + MicroplansInfo[2][j]);
                            MicroplansInfo[3][j] = ((1 + MicroplansInfo[2][j]) / (1 + MicroplansInfo[1][j] ));
                        }
                        else
                        {
                            MicroplansInfo[0][j] = 0;   
                        }
                    }

                    float max_score = 0;
                    String templ_name = "MICROPLAN NOT FOUND";
                    
                    for(int j = 0; j < size; j++)
                    {
                        Microplan microp = ML.getMicroplan(j);                                
                        if(microp.getIsUsed())
                        {
                            logger.debug("Microplan Score [" + j + "]:" + MicroplansInfo[3][j]);
                            if(MicroplansInfo[3][j] >= max_score)
                            {
                                max_score = MicroplansInfo[3][j];
                                templ_name = microp.getMicroplanName();
                            }
                        }
                    }
                    
                    return templ_name;

                }
                else
                {                 
                    return "MICROPLAN NOT FOUND";    
                }
            }
            else
            {
                return chooseMicroplan(prpURI, language);
            }
        }
        catch(InvalidLanguageException e)
        {
            e.printStackTrace();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        
        return "MICROPLAN NOT FOUND";  
    }//chooseMicroplan
    
    public void removeProperty(String PrpURI)
    {
        MicroplansAndOrderingHashtable.remove(PrpURI);
    }
    
    public void renameProperty(String oldPrpURI,String newPrpURI)
    {

        try
        {
            MicroplansAndOrderingNode MAON = (MicroplansAndOrderingNode)MicroplansAndOrderingHashtable.get(oldPrpURI);

            if(MAON != null)
            {
                String langs [] ={Languages.ENGLISH, Languages.GREEK};

                 for(int j = 0; j < langs.length; j++)
                 {
                    MicroplansList ML = MAON.getMicroplans(langs[j]);                

                    for(int i = 0; i < ML.size(); i++)
                    {
                        Microplan m = ML.getMicroplan(i);
                        m.setMicroplanURI(newPrpURI + "-" + m.getMicroplanName() + "-" + langs[j]);
                    }
                 }
                
                removeProperty(oldPrpURI);
                MicroplansAndOrderingHashtable.put(newPrpURI,MAON);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void saveMicroplan(String PrpURI,  Microplan microp, String language)
    {
        try
        {
            
            String MicroplanName = microp.getMicroplanName();

            logger.debug("save [" + PrpURI + "," + MicroplanName + "," + language +"]");
            
            MicroplansAndOrderingNode MAON = (MicroplansAndOrderingNode)MicroplansAndOrderingHashtable.get(PrpURI);                

            if(MAON != null)
            {
                MicroplansList ML = MAON.getMicroplans(language);

                boolean found = false;
                for(int i = 0; i < ML.size(); i++)
                {
                    // an yparxei microplan me to idio onoma adikatesthse to
                    if(ML.getMicroplan(i).getMicroplanName().compareTo(MicroplanName) == 0)
                    {
                        ML.setMicroplan(i, microp);
                        found = true;
                    }
                }
                
                if(!found)
                ML.add(microp);
            }
            else
            {    
                logger.info(PrpURI + "!not found!");   
                MicroplansAndOrderingNode temp = new MicroplansAndOrderingNode();
                
                MicroplansList ML = new MicroplansList(language);
                ML.add(microp);
                        
                temp.setMicroplans(ML, language);
                  
                MicroplansAndOrderingHashtable.put(PrpURI, temp);
            }

        }//try
        catch(Exception e)
        {
            e.printStackTrace();            
        }           
    }
    
    public Iterator<String> getMicroplansIDs()
    {
    	
       try
       {
           Iterator<String> iter = MicroplansAndOrderingHashtable.keySet().iterator();

           Vector<String> microplns = new Vector<String>();
                   
           while(iter.hasNext())
           {
               String microURI = iter.next().toString();
               MicroplansAndOrderingNode MAON = MicroplansAndOrderingHashtable.get(microURI);

               MicroplansList ML = null;

               ML = MAON.getMicroplans(Languages.GREEK);

               for(int i = 0; i < ML.size(); i++)
               {
                    Microplan m = ML.getMicroplan(i);
                    
                    microplns.add(microURI + "-" + m.getMicroplanName() + "-" + Languages.GREEK);
               }

               ML = MAON.getMicroplans(Languages.ENGLISH);

               for(int i = 0; i < ML.size(); i++)
               {
                    Microplan m = ML.getMicroplan(i);
                    
                    microplns.add(microURI + "-" + m.getMicroplanName() + "-" + Languages.ENGLISH);
               }           
           }
           
           return microplns.iterator();
       }
       catch(Exception e)
       {
           e.printStackTrace();
           return null;
       }
    }
    
    public void writeMicroplans(String path)
    {
        try
        {
                    
            XMLDocWriter writer = new XMLDocWriter();

            XmlDocumentCreator docCreator = new XmlDocumentCreator();

            Document lexDoc = docCreator.getNewDocument();
            
            Element rootRDF = lexDoc.createElement("rdf:RDF");

            rootRDF.setAttribute( "xmlns:" + XmlMsgs.prefix , XmlMsgs.owlnlNS);
            rootRDF.setAttribute( "xmlns:" + "rdf" , "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
            rootRDF.setAttribute( "xmlns" ,  NLGMicroplansAndOrderingNS);
            
            logger.info("NLGMicroplansAndOrderingNS: " + NLGMicroplansAndOrderingNS);
            rootRDF.setAttribute( "xml:base" , NLGMicroplansAndOrderingNS.substring(0,NLGMicroplansAndOrderingNS.length()-1));
            
            lexDoc.appendChild(rootRDF); 

            Element MicroAndOrdNode = lexDoc.createElement("owlnl:" + MicroplansAndOrderingManager.MicroplansAndOrderingRes);

            Element PropertiesNode = lexDoc.createElement("owlnl:" + MicroplansAndOrderingManager.PropertiesPrp);
            PropertiesNode.setAttribute("rdf:parseType", "Collection");
            MicroAndOrdNode.appendChild(PropertiesNode);
                    
            Iterator<String> keysIter = MicroplansAndOrderingHashtable.keySet().iterator();

            while(keysIter.hasNext())
            {
                String microPlanUri = keysIter.next();
                logger.debug(microPlanUri);
                MicroplansAndOrderingNode MAON = MicroplansAndOrderingHashtable.get(microPlanUri);

                Element PropertyNode = lexDoc.createElement("owlnl:" + MicroplansAndOrderingManager.PropertyRes);
                PropertyNode.setAttribute("rdf:about", microPlanUri);
                
                //write order    
                Element OrderNode = lexDoc.createElement("owlnl:" + MicroplansAndOrderingManager.OrderPrp);
                OrderNode.setTextContent(MAON.getOrder()+"");
                PropertyNode.appendChild(OrderNode);

                //write used for comp
                Element UsedForComparisonsNode = lexDoc.createElement("owlnl:" + MicroplansAndOrderingManager.UsedForComparisonsPrp);
                UsedForComparisonsNode.setTextContent(MAON.getUsedForComparisons()+"");
                PropertyNode.appendChild(UsedForComparisonsNode);
                
                Element GreekMicroplansNode = lexDoc.createElement("owlnl:" + MicroplansAndOrderingManager.LanguagesMicroplansPrp[0]);
                GreekMicroplansNode.setAttribute("rdf:parseType", "Collection");
                PropertyNode.appendChild(GreekMicroplansNode);

                int size1 = MAON.getMicroplans(Languages.GREEK).size();
                for(int j = 0; j < size1; j++)
                {
                    Microplan m = MAON.getMicroplans(Languages.GREEK).getMicroplan(j);
                    addMicroplan(GreekMicroplansNode, m, lexDoc,  "el");
                }

                Element EnglishMicroplansNode = lexDoc.createElement("owlnl:" + MicroplansAndOrderingManager.LanguagesMicroplansPrp[1]);
                EnglishMicroplansNode.setAttribute("rdf:parseType", "Collection");
                PropertyNode.appendChild(EnglishMicroplansNode);
                
                int size2 = MAON.getMicroplans(Languages.ENGLISH).size();

                for(int j = 0; j < size2; j++)
                {
                    Microplan m = MAON.getMicroplans(Languages.ENGLISH).getMicroplan(j);
                    addMicroplan(EnglishMicroplansNode, m, lexDoc,  "en");
                }         
                
                PropertiesNode.appendChild(PropertyNode);
            }
            
            rootRDF.appendChild(MicroAndOrdNode);
            writer.saveDocToFile(lexDoc, path);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    private void addMicroplan(Element MicroplansNode, Microplan m, Document lexDoc, String Lang)
    {
        Element MicroplanNode = lexDoc.createElement("owlnl:" + MicroplansAndOrderingManager.MicroplanRes);
        MicroplanNode.setAttribute("rdf:about", m.getMicroplanURI());
                
        Element MicroplanNameNode = lexDoc.createElement("owlnl:" + MicroplansAndOrderingManager.MicroplanNamePrp);
        MicroplanNameNode.setTextContent(m.getMicroplanName());
        MicroplanNode.appendChild(MicroplanNameNode);
        
        Element MicroplanUsedNode = lexDoc.createElement("owlnl:" + MicroplansAndOrderingManager.MicroplanUsedPrp);
        MicroplanUsedNode.setTextContent(m.getIsUsed()+ "");
        MicroplanNode.appendChild(MicroplanUsedNode);
        
        Element AggrAllowedNode = lexDoc.createElement("owlnl:" + MicroplansAndOrderingManager.AggrAllowedPrp);
        AggrAllowedNode.setTextContent(m.getAggAllowed()+ "");        
        MicroplanNode.appendChild(AggrAllowedNode);
        
        Element SlotsNode = lexDoc.createElement("owlnl:" + MicroplansAndOrderingManager.MicroplanSlotsPrp);
        SlotsNode.setAttribute("rdf:parseType", "Collection");
        Vector<NLGSlot> slots = m.getSlotsList();
        
        for(int i = 0; i < slots.size(); i++)
        {
            NLGSlot slot = slots.get(i);
            
            if(slot instanceof PropertySlot)
            { // owner or filler
                 PropertySlot PS = (PropertySlot)slot;

                 if(PS.type ==0)
                 {
                     Element OWNERNode = lexDoc.createElement("owlnl:" + MicroplansAndOrderingManager.OwnerRes);
                     
                     Element Case = lexDoc.createElement("owlnl:" + MicroplansAndOrderingManager.casePrp);
                     Case.setTextContent(((PropertySlot)slot).CASE);
                     OWNERNode.appendChild(Case);
                     
                     Element Type = lexDoc.createElement("owlnl:" + MicroplansAndOrderingManager.RETYPEPrp);
                     Type.setTextContent(((PropertySlot)slot).re_type);
                     OWNERNode.appendChild(Type);
                     
                     SlotsNode.appendChild(OWNERNode);
                     
                 }                         
                 else if(PS.type ==1)
                 {
                     Element FillerNode = lexDoc.createElement("owlnl:" + MicroplansAndOrderingManager.FillerRes);
                     
                     Element Case = lexDoc.createElement("owlnl:" + MicroplansAndOrderingManager.casePrp);
                     Case.setTextContent(((PropertySlot)slot).CASE);
                     FillerNode.appendChild(Case);
                     
                     Element Type = lexDoc.createElement("owlnl:" + MicroplansAndOrderingManager.RETYPEPrp);
                     Type.setTextContent(((PropertySlot)slot).re_type); 
                     FillerNode.appendChild(Type);
                     
                     SlotsNode.appendChild(FillerNode);
                 }

            }
            else if(slot instanceof VerbSlot)
            { // verb
                Element VerbNode = lexDoc.createElement("owlnl:" + MicroplansAndOrderingManager.VerbRes);
                
                Element voice = lexDoc.createElement("owlnl:" + MicroplansAndOrderingManager.voicePrp);
                voice.setTextContent(((VerbSlot)slot).VOICE);
                VerbNode.appendChild(voice);
                
                Element tense = lexDoc.createElement("owlnl:" + MicroplansAndOrderingManager.tensePrp);
                tense.setTextContent(((VerbSlot)slot).TENSE);
                VerbNode.appendChild(tense);
                
                Element Val = lexDoc.createElement("owlnl:" + "Val");
                Val.setTextContent(((VerbSlot)slot).VERB);
                Val.setAttribute("xml:lang", Lang);                
                VerbNode.appendChild(Val);

                Element pluralVal = lexDoc.createElement("owlnl:" + "pluralVal");
                pluralVal.setTextContent(((VerbSlot)slot).pluralVERB);
                pluralVal.setAttribute("xml:lang", Lang);                
                VerbNode.appendChild(pluralVal);
                
                SlotsNode.appendChild(VerbNode);
            }
            else if(slot instanceof StringSlot){ // String slot 
                
                Element TextNode = null;
                
                if( ((StringSlot)slot).isPreposition)
                    TextNode = lexDoc.createElement("owlnl:" + MicroplansAndOrderingManager.PrepRes);
                else if ( ((StringSlot)slot).isAdverb)
                    TextNode = lexDoc.createElement("owlnl:" + MicroplansAndOrderingManager.AdverbRes);
                else
                    TextNode = lexDoc.createElement("owlnl:" + MicroplansAndOrderingManager.TextRes);
                
                Element Val = lexDoc.createElement("owlnl:" + "Val");
                Val.setTextContent(((StringSlot)slot).STRING);
                Val.setAttribute("xml:lang", Lang);
                TextNode.appendChild(Val);
                
                SlotsNode.appendChild(TextNode);
                
            } 
            else if(slot instanceof ClsDescSlot){ // String slot
                     
            } 
          }//for
        
          MicroplanNode.appendChild(SlotsNode);
          MicroplansNode.appendChild(MicroplanNode);
    }    
    
    public List<String> getPropertiesUsedForComparisons()
    {
        return this.PropertiesUsedForComparisons;
    }
    
    //main
    public static void main(String args[])
    { 
        try
        {
            MicroplansAndOrderingQueryManager MAOQM = new MicroplansAndOrderingQueryManager();
            MAOQM.LoadMicroplansAndOrdering("C:\\NLG_Project\\NLFiles-MPIRO\\" , "microplans.rdf");
            MAOQM.writeMicroplans("C:\\NLG_Project\\NLFiles-MPIRO\\test\\microplans.rdf");
            
            //MAOQM.LoadMicroplansAndOrdering("C:\\NLG_Project\\NLFiles-MPIRO\\test\\" , "microplans.rdf");
            
            //logger.debug(MAOQM.getOrder("http://www.w3.org/TR/2003/CR-owl-guide-20030818/wine#locatedIn"));
            //logger.debug(MAOQM.chooseMicroplan("http://www.w3.org/TR/2003/CR-owl-guide-20030818/wine#locatedIn", Languages.ENGLISH));
            List<String> v = new LinkedList<String>();
            v = MAOQM.getPropertiesUsedForComparisons(); 
            
            for(int i = 0; i < v.size(); i++)
            {
                logger.debug(v.get(i));
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }        
    }
}//class

