/*
    NaturalOWL
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

import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.vocabulary.*;
import com.hp.hpl.jena.ontology.*;
import com.hp.hpl.jena.util.iterator.*;

import com.hp.hpl.jena.ontology.OntProperty;
import gr.aueb.cs.nlg.Communications.NLGEngineServer.NLGEngineServer;
        
import org.w3c.dom.*;	
import java.util.*;

import gr.aueb.cs.nlg.Languages.*;
import gr.aueb.cs.nlg.NLFiles.*;
import gr.aueb.cs.nlg.Utils.*;
import gr.aueb.cs.nlg.Utils.XmlMsgs;
import gr.aueb.cs.nlg.Utils.XmlDocumentCreator;
import gr.aueb.cs.nlg.PServerEmu.*;
import gr.demokritos.iit.PServer.*;

import org.apache.log4j.Logger;

public class ContentSelection extends NLGEngineComponent
{    
    static Logger logger = Logger.getLogger("gr.aueb.cs.nlg.NLGEngine.ContentSelection");
    
    private XmlDocumentCreator XmlDocCreator;
    private int counter;
    private MicroplansAndOrderingQueryManager MAOQM; 
    private OntModel ontModel;
    private UserModellingQueryManager UMQM;   
    private gr.aueb.cs.nlg.PServerEmu.UMVisitImpEmu UMVisEmu;
    
    private HashSet mentionedEntities;
    private HashSet conveyedFacts;
    private HashSet usedMicroPlanningExpressions;
           
    private HashMap<String, String> FoundMicroplans ;
    private Vector<memNode> ListOfFacts;
            
    private String PreviousUserId;
    private UserTypeParameters UTP;
    private UMVisit UMVis;
    exhibitsPositions exhibitsPos;
 //   List<String> ComparisonChar;
    
    private String personality = "";
    //-----------------------------------------------------------------------------------
    //constructor	
    ContentSelection(MicroplansAndOrderingQueryManager MAOQM,UserModellingQueryManager UMQM,UMVisit UMVis, OntModel ontModel,String Language, exhibitsPositions exhibitsPos)
    {
        
            super(Language);
            
            this.exhibitsPos = exhibitsPos;
            this.MAOQM = MAOQM;
            
            XmlDocCreator = new XmlDocumentCreator();
            //MyXmlMsgs = null;
            this.ontModel = ontModel;
            this.UMQM = UMQM;
            UMVisEmu = new UMVisitImpEmu(new Structures(UMQM));
            
            this.UMVis = UMVis;
                    
            String uriForNullPrefix = ontModel.getNsPrefixURI(""); // get the uri of the default ns (xmlns="")
            ontModel.removeNsPrefix("");                           // remove ""[prefix] http://.../[ns URI]
            
            //if(uriForNullPrefix != null)
            //{
                if (ontModel.getNsURIPrefix(uriForNullPrefix)!=null)
                {
                    // ok
                }
                else
                {
                    ontModel.setNsPrefix("p1", uriForNullPrefix);
                    
                }
            //}
            //else
            //{
                
            //}
            
            logger.debug("Content Selection initialized..." + uriForNullPrefix);
    }
        
    public void setPersonality(String personality)
    {
        if(personality.equals(""))
            this.personality = "1";
        else
            this.personality = personality;
    }
            
    public void setModel(OntModel m)
    {
        this.ontModel = m;
    }
    
    public void setNamespaces(XmlMsgs MyXmlMsgs)
    {
        
        Map prefixesToNSMap = ontModel.getNsPrefixMap();

        Iterator iter = prefixesToNSMap.keySet().iterator();
        
        while(iter.hasNext())
        {
            String pref = iter.next().toString();            
            String uriForPref = prefixesToNSMap.get(pref).toString();
            
            logger.debug("uriForPref" + uriForPref + " " + pref);
            MyXmlMsgs.setNamespace(uriForPref, pref);
        }
    }
    
    //returns the messages of a Class
    public XmlMsgs getMsgsOfAClass(String strURI, XmlMsgs MyXmlMsgs, int depth, int Level ,Node r, String UserType, String userID)
    {
    	OntClass cls =  ontModel.getOntClass(strURI);    		
				
    	if(cls.isIntersectionClass())
        { //if cls is IntersectionClass
    		Element msgElem = MyXmlMsgs.createNewMsg();
    		Element t = MyXmlMsgs.AddNewElement(msgElem,XmlMsgs.owlnlNS,XmlMsgs.prefix,XmlMsgs.INTERSECTION_OF_TAG);
    		describeBoolCls(MyXmlMsgs, cls.asIntersectionClass(), t, cls, UserType,  userID); 
 			    		    		   		
    	}
    	if (cls.isUnionClass())
        { //if cls is UnionClass    		
    		Element msgElem = MyXmlMsgs.createNewMsg();
    		Element t = MyXmlMsgs.AddNewElement(msgElem,XmlMsgs.owlnlNS,XmlMsgs.prefix,XmlMsgs.UNION_OF_TAG);
    		describeBoolCls(MyXmlMsgs, cls.asUnionClass(),t , cls, UserType,  userID);    		
    	}
    	if (cls.isComplementClass())
        { //if cls is ComplementClass    		
    		Element msgElem = MyXmlMsgs.createNewMsg();
    		Element t = MyXmlMsgs.AddNewElement(msgElem,XmlMsgs.owlnlNS,XmlMsgs.prefix,XmlMsgs.COMPLEMENT_OF_TAG);
    		describeBoolCls(MyXmlMsgs,cls.asComplementClass(),t , cls, UserType,  userID);    		
    	}
    	if (cls.isEnumeratedClass())
        { //if cls is EnumeratedClass    		
    		Element msgElem = MyXmlMsgs.createNewMsg();
    		Element t = MyXmlMsgs.AddNewElement(msgElem,XmlMsgs.owlnlNS,XmlMsgs.prefix,XmlMsgs.ENUMERATION_OF_TAG);
    		describeEnumCls(MyXmlMsgs,cls.asEnumeratedClass(), t, cls);    		
    	}
    	else
        {
    		logger.debug("what happened???");
    	}
    	 
    	for(ExtendedIterator i = cls.listSuperClasses(); i.hasNext();)
        {
    		Element msgElem = MyXmlMsgs.createNewMsg();
			Element t = MyXmlMsgs.AddNewElement(msgElem,XmlMsgs.owlnlNS,XmlMsgs.prefix,XmlMsgs.SUBCLASS_OF_TAG);
			OntClass c = (OntClass)i.next();
			describeCls(MyXmlMsgs,c,t,cls, UserType, userID);
    	}    
    	/*
    	for(ExtendedIterator i = cls.listSubClasses(); i.hasNext();){
    		Element msgElem = MyXmlMsgs.createNewMsg();
			Element t = MyXmlMsgs.AddNewElement(msgElem,"SuperClassOf");
			OntClass c = (OntClass)i.next();
			describeCls(c,t,cls);
    	} */ 
    	
    	for(ExtendedIterator i = cls.listEquivalentClasses(); i.hasNext();){
    		Element msgElem = MyXmlMsgs.createNewMsg();
			Element t = MyXmlMsgs.AddNewElement(msgElem,XmlMsgs.owlnlNS,XmlMsgs.prefix,XmlMsgs.EQUIVALENT_CLASS_TAG);
			OntClass c = (OntClass)i.next();
			describeCls(MyXmlMsgs,c,t,cls, UserType, userID);
    	}    
    	/*
            for(ExtendedIterator i = cls.listDisjointWith(); i.hasNext();){
            Element msgElem = MyXmlMsgs.createNewMsg();
                    Element t = MyXmlMsgs.AddNewElement(msgElem,XmlMsgs.DISJOINT_WITH_TAG);
                    OntClass c = (OntClass)i.next();
                    describeCls(c,t,cls);
    	}
    	*/

    	Element msgElem;
        return MyXmlMsgs;
    }//getMsgsOfAClass
    //-----------------------------------------------------------------------------------
    public void ClearBuffers()
    {
        ListOfFacts = new Vector<memNode> ();
        
        mentionedEntities = new HashSet();
        conveyedFacts = new HashSet();
        usedMicroPlanningExpressions = new HashSet();   
        
        FoundMicroplans = new HashMap<String, String>();
    }
    
    public void setMentionedEntity(String entityURI)
    {
        mentionedEntities.add(entityURI);
    }
    
    public void UpdatePServer(String userID,String userType)
    {
        try
        { 
            logger.debug("Content Selection: " + "UpdatePServer:" + userID + ":" + userType);
            logger.debug("Content Selection: " + mentionedEntities.size() +  ":" + usedMicroPlanningExpressions.size() + ":" + conveyedFacts.size());
            
            String mentionedEntitiesTable [] = new String[mentionedEntities.size()];                
            mentionedEntities.toArray(mentionedEntitiesTable);

            String usedMicroPlanningExpressionsTable [] = new String[usedMicroPlanningExpressions.size()];
            usedMicroPlanningExpressions.toArray(usedMicroPlanningExpressionsTable);

            String conveyedFactsTable [] = new String [conveyedFacts.size()]; 
            conveyedFacts.toArray(conveyedFactsTable);
                
            HashSet<String> usedPredicates = new HashSet<String>();
            Vector<String> conveyedFactsTableAfterFiltering = new Vector<String>();
                    
            for(int i = 0; i < conveyedFactsTable.length; i++)
            {
		if( Fact.getPredicate(conveyedFactsTable[i]).equals(RDF.type.toString()) ||
		    Fact.getPredicate(conveyedFactsTable[i]).equals(RDFS.subClassOf.toString()))
		{
		    conveyedFactsTableAfterFiltering.add(conveyedFactsTable[i]);
		}
		else
		{
		    if( !usedPredicates.contains(Fact.getPredicate(conveyedFactsTable[i])))
		    {
			conveyedFactsTableAfterFiltering.add(conveyedFactsTable[i]);
			usedPredicates.add(Fact.getPredicate(conveyedFactsTable[i]));
		    }
		}
                
            }
                
            conveyedFactsTable  = new String [conveyedFactsTableAfterFiltering.size()]; 
            conveyedFactsTableAfterFiltering.toArray(conveyedFactsTable);
            
            if(!all_Facts_Are_Assimilated)
                UMVis.update(mentionedEntitiesTable, conveyedFactsTable, usedMicroPlanningExpressionsTable, userID, userType);
            else
                UMVis.update(mentionedEntitiesTable, new String[0], usedMicroPlanningExpressionsTable, userID, userType);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    // find the msgs of an instance of the ontology
    // 
     
    public XmlMsgs getMsgsOfAnInstance(String strURI, XmlMsgs MyXmlMsgs, int depth, int Level ,Node r, String UserType, String userID, ComparisonTree tree, ComparisonTree UTree, boolean firstTime, NLGEngineServer nav) throws InvalidLanguageException
    {
        try
        {
            if(nav==null)
            {
                nav = new NLGEngineServer(null);
                nav.setDefaultValues();
            }
                    
            logger.debug("depth:" + depth);
            Individual MyInstance = ontModel.getIndividual(strURI);
                        
            if (MyInstance == null)// periptwsh twn datatype properties
                return MyXmlMsgs;    
                
            int counter = 0;
            Element msgElem = null;

            //logger.debug("URI: " + MyInstance.getRDFType().getURI());
            OntClass c = ontModel.getOntClass(MyInstance.getRDFType().getURI());
            //logger.debug("URI: " +c.getURI());

            if(c==null)
                logger.debug("URI: " +c.getURI() + "null");

            
            // me kapoion magiko (pros to paron) tropo exw brei me poio 8a sygkrinw
            // kai poio xarakthritistiko.
            boolean multyComp = false;
            String isA="";
            boolean proste8hke = false;
            boolean most = false;
            int randomFeature = 0;
            int randomUnique = 0;
            int randomCommon = 0;
            int randomCommonBlured = 0;
            
            ComparisonNode commonBlured = null;
            ComparisonNode comparator = null;
            ComparisonNode unique = null;
            ComparisonNode common = null;
            
            String location="";
            
            if (firstTime && tree.findNode(MyInstance.getURI())==null)
            {
                logger.debug("JEKINAmE!!!!!");

                //an de 8eloyme sygkriseis me bash to Melegkogloy apla "sxoliazoyme" thn epomenh entolh
                comparator = tree.performComparatorSelection(MyInstance.getURI(),ontModel,UMQM, UserType, MAOQM.getPropertiesUsedForComparisons());
                if (comparator!=null)
                    comparator.removeEmptyFeatures();

                if (comparator != null &&comparator.getAttributeNames().size()>0)
                {
                    randomFeature = ((int)(Math.random()*1000))%comparator.getAttributeNames().size();
                }


                if (comparator==null)
                {
                //   comparator = tree.performComparatorSelectionBlured(MyInstance.getURI(),ontModel, 80,UMQM, UserType);
                    if (comparator != null &&comparator.getAttributeNames().size()>0)
                    {
                        randomFeature = ((int)(Math.random()*1000))%comparator.getAttributeNames().size();
                        most = true;
                    }
                }



                if (comparator!=null)
                {
                    int plh8osOmoiothtwn = comparator.omoiothtes(MyInstance.getURI(), ontModel);
                    // exei panw apo mia omoiothta
                    if (plh8osOmoiothtwn > 1)
                        //exei mono omoiothtes
                        if (plh8osOmoiothtwn == comparator.getAttributeNames().size())
                        {
                            multyComp = true;
                        }
                    //exei kai kapoies diafores
                        else
                        {
                        //dialegei tyxaia an 8a kanei sygkrish omoiothtas h diaforas
                            if(Math.random()>=0.5)
                            {
                                //krata omoiothtes
                                comparator.removeDiafores(MyInstance.getURI(), ontModel);
                                multyComp = true;
                            }
                            else
                            {
                                //krata th mia diafora
                                comparator.removeOmoiothtes(MyInstance.getURI(), ontModel, this.MAOQM.PropertiesUsedForComparisons);
                                randomFeature = ((int)(Math.random()*1000))%comparator.getAttributeNames().size();
                            }
                        }
                }


                //an de 8eloyme sygkriseis me ta ek8emata ths sylloghs sxoliazoyme olo to epomeno if
                if (comparator==null)
                {
                    //briskw to interest
                    UTree.sortAttributes(UMQM, UserType);
                    //UTree.print();

                    common = UTree.performCommonSelection(MyInstance.getURI(),ontModel,MAOQM.getPropertiesUsedForComparisons());
                    if (common != null&&common.getAttributeNames().size()>0)
                    {
                        randomCommon = ((int)(Math.random()*1000))%common.getAttributeNames().size();
                        UTree.updateChoice(common, randomCommon);
                    }

                    if (common == null)
                    {
                        unique = UTree.performUniqueSelection(MyInstance.getURI(),ontModel,MAOQM.getPropertiesUsedForComparisons());
                        if (unique != null&&unique.getAttributeNames().size()>0)
                            randomUnique = ((int)(Math.random()*1000))%unique.getAttributeNames().size();
                    }



                   // if (unique == null)
                   //     commonBlured = UTree.performCommonSelectionBlured(MyInstance.getURI(),ontModel);
                    if (commonBlured != null&&commonBlured.getAttributeNames().size()>0)
                    {
                        randomCommonBlured = ((int)(Math.random()*1000))%commonBlured.getAttributeNames().size();
                        UTree.updateChoice(commonBlured, randomCommonBlured);
                    }
                }
                       
   			   		
            // molis brw to xarakthristiko ayto (otan dhladh paei na to perigrapsei me ton klassiko tropo
            // o algori8mos) peirazw ligo to mhnyma wste na enhmerwsw ta epomena stadia
            // oti 8a ginei sygkrish Anti8eshs h omoiothtas me to tade gia to xarakthristiko ayto
            // pros8etw 2 akoma attributes sto antistoixo tag ta
            // comparator: opoy 8a krata to onoma toy typoy/ontothtas me thn opoia ginetai h sygkrish
            // feature: thn timh me thn opoia ginetai h sygkrish
            // an prokeitai gia sygkrish omoiothtas tote to feature paraleipetai
            gr.aueb.cs.nlg.NLGEngine.spaceCalc.fromTo dir = null;
            
            if (comparator!=null)
            {
		Vector<String> exhibits = tree.membersOf(comparator.getName());
                
		if (exhibitsPos.positions.get(MyInstance.getURI()) != null)
                {
                    if (spaceCalc.inSameRoom(exhibits, exhibitsPos.positions.get(MyInstance.getURI()).rooms, exhibitsPos)&&spaceCalc.areInMySight(exhibits,exhibitsPos,5,nav))
                    {
                        location = spaceCalc.places(exhibits, exhibitsPos, nav, 0);
                    }
                }
                if (!location.equalsIgnoreCase(""))
                    dir = spaceCalc.direction(exhibits,exhibitsPos,nav, 0);
					
                tree.updateComparHistory(comparator);
		if (nav.oldX == nav.X && nav.oldY == nav.Y && nav.oldDirection == nav.direction)
		{
		    if ((!location.equalsIgnoreCase(""))&&location.equalsIgnoreCase(spaceCalc.placeOfExhibit(MyInstance.getURI(), exhibitsPos, nav, 0)))
		    {
			location+="-Also";
		    }
		}
		else
		{
		    if ((!location.equalsIgnoreCase(""))&&location.equalsIgnoreCase(spaceCalc.placeOfExhibit(MyInstance.getURI(), exhibitsPos, nav, 0)))
		    {
			location+="-Also";
			if ((!location.equalsIgnoreCase(""))&&!location.equalsIgnoreCase(spaceCalc.oldPlaces(exhibits, exhibitsPos, nav, 0)))
			{
			    location+="-Now";
			}
		    }
		    else
		    {
			if ((!location.equalsIgnoreCase(""))&&location.equalsIgnoreCase(spaceCalc.oldPlaces(exhibits, exhibitsPos, nav, 0)))
			{
			    location+="Still";
			}
			else
			{
			    location+="-Now";
			}
		    }
		}
            }
            
            //location = "left";
            }//firstTime
            
            for(StmtIterator i = MyInstance.listProperties();  i.hasNext();)
            {						   		
 		Statement t = (Statement)i.next();
                RDFNode node = t.getObject();
            
                String pred = t.getPredicate().getLocalName();
            
                if( pred.compareTo("comment")!=0 
            	&& pred.compareTo("label")!=0
            	&& pred.compareTo("sameAs")!=0)
            	{
            		
                    if(t.getPredicate().getLocalName().compareTo("type")==0)
                        isA=node.toString();
                }
            }
	    
	    nav.oldX = nav.X;
	    nav.oldY = nav.Y;
	    nav.oldDirection = nav.direction;
            
                                    
            for(StmtIterator i = MyInstance.listProperties();  i.hasNext();)
            {
						   		
 		Statement t = (Statement)i.next();
                RDFNode node = t.getObject();
            
                String pred = t.getPredicate().getLocalName();
            
                if( pred.compareTo("comment")!=0 
            	&& pred.compareTo("label")!=0
            	&& pred.compareTo("sameAs")!=0
                && pred.compareTo("differentFrom")!=0)
            	{
            		
                    if(t.getPredicate().getURI().compareTo("http://www.w3.org/1999/02/22-rdf-syntax-ns#type")==0)
                    {
                            //msgElem = MyXmlMsgs.createNewMsg();
                            Element msg = MyXmlMsgs.AddNewElement( (Element)r , XmlMsgs.owlnlNS, XmlMsgs.prefix, t.getPredicate().getLocalName()); 

                            //add is-a msg
                            MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix,"Val", node.toString());
                            MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix,XmlMsgs.ORDER_TAG,0+"");
                            MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix, XmlMsgs.REF, strURI);
             
                                                        
                            if (unique!=null && unique.getNumberOfObjects() == 1)
                                MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix, "Unique", unique.getName());
             
                            
                            
                            MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix, XmlMsgs.AGGREG_ALLOWED, "true");
                            MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix, XmlMsgs.FOCUS_LOST, "undefined");
                            MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix, XmlMsgs.LEVEL, Level+"");

                           
                            String InterestValue = "";
                            String assimScore = "";
                            
                            String fact = FactFactory.getFact(strURI, t.getPredicate().getURI(), node.toString()); 
                            String ClassType = NLGEngine.getClassType(ontModel, strURI);
                                    
                            if(userID!=null)
                            {
                                InterestValue = "" + (int)UMVis.getInterestFact(fact, ClassType , userID, UserType, personality);
                                assimScore = "" + UMVis.getAssimilationScore(fact, ClassType, userID, UserType); 
                            }
                            else if(UserType !=null)
                            {
                                Fact fct = new Fact(fact);   
                                InterestValue = "" + UMQM.getCInterest(ClassType, strURI, UserType);
                                
                                assimScore = "0";
                            }
                            else
                            {
                                InterestValue = "1";
                                assimScore = "0";
                            }   
                                                        
                            if(Level == 1)
                            {                                                                                            
                                MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix, XmlMsgs.INTEREST, InterestValue);
                                MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix, XmlMsgs.ASSIMIL_SCORE, assimScore); 
                            }
                            else
                            {
                                MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix, XmlMsgs.INTEREST, 0 + "");
                                MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix, XmlMsgs.ASSIMIL_SCORE, "0");
                            }
                                                       
                            
                            AddKindOfMsg(depth, 0, strURI,  t, MyXmlMsgs,  msg, userID , UserType, nav);
                            AddMsgsFromUpperCls(depth, 0, strURI,   MyXmlMsgs,  msg,  userID ,  UserType, nav);                          
                    }//if "http://www.w3.org/1999/02/22-rdf-syntax-ns#type"
                    else
                    {   
                        //an einai unique xarakthrstiko
                        if (unique != null && unique.getNumberOfObjects()>1 && t.getPredicate().getLocalName().compareTo(unique.getAttributeNames().get(randomUnique))==0)
                        {
                            AddUniqueMsg(depth, Level, t.getSubject().getURI(), t.getPredicate().getNameSpace(), t.getPredicate().getLocalName(), node.toString(), (OntProperty)t.getPredicate().as(OntProperty.class),  MyXmlMsgs,  r,  userID ,  UserType, tree, UTree, unique, proste8hke, MyInstance, isA, nav);
                            proste8hke = true;
                        }
                        else
                        //an prokeitai gia koino xarakthristiko
                        if (common != null && common.getNumberOfObjects()>1 && t.getPredicate().getLocalName().compareTo(common.getAttributeNames().get(randomCommon))==0)
                        {
                            
                            AddCommonMsg(depth, Level, t.getSubject().getURI(), t.getPredicate().getNameSpace(), t.getPredicate().getLocalName(), node.toString(), (OntProperty)t.getPredicate().as(OntProperty.class),  MyXmlMsgs,  r,  userID ,  UserType, tree, UTree, common, proste8hke, MyInstance, isA, nav);
                            proste8hke = true;
                        }else
                        //an prokeitai gia sygkrish me pollapla xarakthristika
                        if (comparator != null && multyComp&& comparator.hasAttribute(t.getPredicate().getLocalName()))
                        {
                            if (comparator != null && comparator.hasAttribute(t.getPredicate().getLocalName()))
                            {
                                //String location = "right";
                                AddMultiComparistonMsg(depth, Level, t.getSubject().getURI(), t.getPredicate().getNameSpace(), t.getPredicate().getLocalName(), node.toString(), (OntProperty)t.getPredicate().as(OntProperty.class),  MyXmlMsgs,  r,  userID ,  UserType, tree, UTree, comparator, proste8hke, MyInstance, isA, most, t, node, location, nav);
                                proste8hke = true;
                            }
                        }else                            
                        //an prokeitai na ginei aplh sygkrish
                        if (comparator != null && t.getPredicate().getLocalName().compareTo(comparator.getAttributeNames().get(randomFeature))==0)
                        {
                            //String location = "right";
                            AddComparisonMsg(depth, Level, t.getSubject().getURI(), t.getPredicate().getNameSpace(), t.getPredicate().getLocalName(), node.toString(), (OntProperty)t.getPredicate().as(OntProperty.class),  MyXmlMsgs,  r,  userID ,  UserType, tree, UTree, comparator, proste8hke, MyInstance, isA, most, t, node, randomFeature, location, nav);
                            proste8hke = true;
                        }
                        else
                        //an prokeitai gia common blured
                        if (commonBlured != null && t.getPredicate().getLocalName().compareTo(commonBlured.getAttributeNames().get(randomCommonBlured))==0)
                        {
                            AddCommonBluredMsg(depth, Level, t.getSubject().getURI(), t.getPredicate().getNameSpace(), t.getPredicate().getLocalName(), node.toString(), (OntProperty)t.getPredicate().as(OntProperty.class),  MyXmlMsgs,  r,  userID ,  UserType, tree, UTree, commonBlured, proste8hke, MyInstance, isA, most, t, node, randomCommonBlured, nav);
                            proste8hke = true;
                        }
                        else
                            AddPropertyMsg(depth, Level, t.getSubject().getURI(), t.getPredicate().getNameSpace(), t.getPredicate().getLocalName(), node.toString(), (OntProperty)t.getPredicate().as(OntProperty.class),  MyXmlMsgs,  r,  userID ,  UserType, tree, UTree, nav);
           
                        
                    }//else
                                
                }// for
   			
            }//for
   		   	
            if (firstTime)
            // pros8etoyme thn ontothta poy molis perigrapsame ston dendro
            if (!proste8hke)
            {
                tree.addElement(MyInstance.getURI(),ontModel,MAOQM.getPropertiesUsedForComparisons());
                proste8hke = true;
            }
            

            return MyXmlMsgs;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return MyXmlMsgs;
        }
   }
    
private void AddPropertyMsg(int depth, int Level ,String subURI, String predicateNS, String predicateLocalName, String objURI, OntProperty ontPrp, XmlMsgs MyXmlMsgs, Node r, String userID , String UserType, ComparisonTree tree, ComparisonTree UTree, NLGEngineServer navigation)
{
    try
    {

        String predicateURI = predicateNS + predicateLocalName;
        String pref = ontModel.getNsURIPrefix(predicateNS);

        if(pref == null)
        {
            pref = "";
        }

        //create new msg
        Element msg = MyXmlMsgs.AddNewElement( (Element)r , predicateNS, pref, predicateLocalName);                   

        //choose microplan
        String microplanSelected = chooseMicroplan(predicateURI, UMVis, userID, UserType);

        MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix, XmlMsgs.TEMPLATE_TAG, microplanSelected);
        MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix, XmlMsgs.REF, subURI);
        MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix, "Val", objURI);
        MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix, XmlMsgs.LEVEL, Level+"");            
        MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix, XmlMsgs.ORDER_TAG, MAOQM.getOrder(predicateURI) + "");                            

        String AggAllowed = MAOQM.getisAggAllowed(predicateURI,
                predicateURI + "-" + microplanSelected + "-" + getLanguage(), getLanguage()) + "";

        MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix, XmlMsgs.AGGREG_ALLOWED, AggAllowed);
        MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix, XmlMsgs.FOCUS_LOST, "undefined");

        String InterestValue = "";
        String assimScore = "";

        if(userID != null)
        {
            //ListOfFacts.add(new memNode())
            String myfoo = NLGEngine.getClassType(ontModel, subURI);

            InterestValue = "" + (int)UMVis.getInterestFact(FactFactory.getFact(subURI, predicateURI, objURI), myfoo,  userID, UserType, personality);
            assimScore = "" + UMVis.getAssimilationScore(FactFactory.getFact(subURI, predicateURI, objURI), myfoo, userID, UserType);     

        }
        else
        {
            if(UserType !=null)
            {
                InterestValue = String.valueOf(UMQM.getInterest(predicateURI, subURI, UserType));
            }
            else
            {
                InterestValue = "1";
            }

            assimScore = "0";
        }

        MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix, XmlMsgs.INTEREST, InterestValue);
        MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix, XmlMsgs.ASSIMIL_SCORE, assimScore);


        if(ontPrp.isObjectProperty())   
        {
            MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix,XmlMsgs.prpType, XmlMsgs.ObjectProperty);
        }
        else if(ontPrp.isDatatypeProperty())     
        {
            MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix,XmlMsgs.prpType, XmlMsgs.DatatypeProperty);
        }

        // recursively call getMsgsOfAnInstance
        if(depth > 1)
        {                           
            Level++;
            int d = depth - 1;
            getMsgsOfAnInstance( objURI, MyXmlMsgs, d , Level, msg, UserType, userID,tree,UTree,false, navigation);
        }
    }
    catch(Exception e)
    {
        e.printStackTrace();
    }
}
    
 
    
    /*  choose microplan for the specified predicate.
     *  if this predicate has already appeared in the description
     *  choose the same microplan as before.
     */
    private String chooseMicroplan(String predicateURI, UMVisit UMVis, String userID, String userType)
    {
        try
        {
            String microplanSelected = "";
            if(!FoundMicroplans.containsKey(predicateURI))
            {
                microplanSelected = MAOQM.chooseMicroplan(predicateURI, getLanguage(), UMVis, userID, userType);
                FoundMicroplans.put(predicateURI, microplanSelected);
            }
            else
            {
                microplanSelected = FoundMicroplans.get(predicateURI);
            }

            return microplanSelected;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return "";
    }
    
   private void AddMsgsFromUpperCls(int depth, int Level,String strURI,  XmlMsgs MyXmlMsgs, Element msg, String userID , String UserType, NLGEngineServer navigation)
   {
       try
       {
            String InterestValue = "";
            String assimScore = "";
            
            if(depth >= 2)
            {
                String ClassType = NLGEngine.getClassType(ontModel, strURI);
                
                OntClass c = ontModel.getOntClass(ClassType);
                ExtendedIterator it = c.listSuperClasses();
                
                while(it.hasNext())
                {
                    OntClass superClass = (OntClass)it.next();
                    if(superClass.isRestriction())
                    {
                        Restriction r = superClass.asRestriction();
                        if(r.isHasValueRestriction())
                        {
                            HasValueRestriction HVR = r.asHasValueRestriction();
                            
                            String predicateNS = HVR.getOnProperty().getNameSpace();
                            String predicateLN = HVR.getOnProperty().getLocalName();
                            
                            String predicateURI = predicateNS + predicateLN;
                            String subURI = ((OntResource)HVR.getHasValue().as(OntResource.class)).getURI() ;                        
                            
                            logger.debug("Add HasValue");
                            AddPropertyMsg(0, 0,ClassType, predicateNS, predicateLN, HVR.getHasValue().toString(), HVR.getOnProperty(),  MyXmlMsgs,  msg,  userID ,  UserType, null, null, navigation);
                        }
                        
                    }
                }
            }
       }
       catch(Exception e)
       {
           e.printStackTrace();
       }
               
   }
   
   private void AddKindOfMsg(int depth, int Level, String strURI, Statement t, XmlMsgs MyXmlMsgs, Element msg, String userID , String userType, NLGEngineServer nav)
   {
       try
       {
            logger.debug("AddKindOfMsg " + strURI);
            
            String InterestValue = "";
            String assimScore = "";
            
            if(depth >= 2)
            {
                String ClassType = NLGEngine.getClassType(ontModel, strURI);
                
                String fact = t.toString();
                        
                if(userID!=null)
                {
                    InterestValue = "" + (int)UMVis.getInterestFact(fact, ClassType, userID, userType, personality);
                    assimScore = "" + UMVis.getAssimilationScore(fact, ClassType, userID, userType); 
                }
                else if(userType !=null)
                {
                    Fact fct = new Fact(fact);   
                    InterestValue = "" + UMQM.getInterest(fct.getPredicate(), fct.getSubject(), userType);
                    assimScore = "0";
                }
                else
                {
                    InterestValue = "1";
                    assimScore = "0";
                }
                                
                String CT1 = ClassType;
                String CT2 = NLGEngine.getClassType(ontModel, CT1);
                String preCT2 = "";

                fact = FactFactory.getFact(CT1, RDFS.subClassOf.toString(), CT2);

                boolean found = false;
                int currentFactInterest = 0;
                
                while(!found &&  CT2!=null && CT2.compareTo("http://www.w3.org/2002/07/owl#Thing")!=0 )
                {
                          
                    logger.debug("fact:" + fact);
                    
                    if(userID!=null)
                    {
                        currentFactInterest = ((int)UMVis.getInterestFact(fact, NLGEngine.getClassType(ontModel, CT1), userID, userType, personality)); 
                    }
                    else if(userType !=null)
                    {
                        Fact fct = new Fact(fact);
                        currentFactInterest =  UMQM.getCInterest(fct.getObject(),fct.getSubject(),userType);
                    }
                    else
                    {
                        currentFactInterest = 1;   
                    }                    
                    
                    if( currentFactInterest == 0 )
                    {
                        CT1 = CT2;
                        preCT2 = CT2;
                        CT2 = NLGEngine.getClassType(ontModel, CT2);
                        fact = FactFactory.getFact(CT1, RDFS.subClassOf.toString(), CT2);
                    }
                    else
                    {
                        found = true;
                    }
                    
                    logger.debug("fact:" + fact);
                }

                if(found)
                {
                    Element msg2 = MyXmlMsgs.AddNewElement( msg , XmlMsgs.owlnlNS, XmlMsgs.prefix, "type"); 

                    MyXmlMsgs.SetAttr(msg2, XmlMsgs.owlnlNS, XmlMsgs.prefix, "Val", CT2);
                    MyXmlMsgs.SetAttr(msg2, XmlMsgs.owlnlNS, XmlMsgs.prefix, XmlMsgs.ORDER_TAG, 0+"");
                    MyXmlMsgs.SetAttr(msg2, XmlMsgs.owlnlNS, XmlMsgs.prefix, XmlMsgs.REF, ClassType);
                    MyXmlMsgs.SetAttr(msg2, XmlMsgs.owlnlNS, XmlMsgs.prefix, XmlMsgs.AGGREG_ALLOWED, "true");
                    MyXmlMsgs.SetAttr(msg2, XmlMsgs.owlnlNS, XmlMsgs.prefix, XmlMsgs.FOCUS_LOST, "undefined");
                    MyXmlMsgs.SetAttr(msg2, XmlMsgs.owlnlNS, XmlMsgs.prefix, XmlMsgs.LEVEL, Level+"");
                    
                    String fact2 = "[" + ClassType + ", " + RDF.type.toString() + ", " + CT2 +"]";
                    
                    String assimScore2 = "0";
                    
                    if(userID!=null)
                    {                    
                        assimScore2 = "" + UMVis.getAssimilationScore(fact2, preCT2, userID, userType);
                    }

                    //logger.debug("assimScore2" + assimScore2);
                    
                    MyXmlMsgs.SetAttr(msg2, XmlMsgs.owlnlNS, XmlMsgs.prefix, XmlMsgs.INTEREST, 1 + "");
                    MyXmlMsgs.SetAttr(msg2, XmlMsgs.owlnlNS, XmlMsgs.prefix, XmlMsgs.ASSIMIL_SCORE, assimScore2);
                }
            }// depth > 2     
            

            //MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix, XmlMsgs.INTEREST, InterestValue);
            //MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix, XmlMsgs.ASSIMIL_SCORE, assimScore);            
       }
       catch(Exception e)
       {
           e.printStackTrace();
       }
   }
   
   private boolean all_Facts_Are_Assimilated = false;
   
   public boolean AllFactsAreAssimilated()
   {
       return all_Facts_Are_Assimilated;
   }
           
   public XmlMsgs getTheMostInterestingUnassimilatedFacts (XmlMsgs MyXmlMsgs, int depth, String UserType, String UserID)
   {
       try
       {
           if(UserType != null || UserID !=null)
           {

               int levelsSizes[] = new int [depth];   

               for(int i =0; i < levelsSizes.length; i++)
               {
                   levelsSizes[i] = 0;
               }

               Node r = MyXmlMsgs.getRoot();
               Vector Facts = XmlMsgs.ReturnChildNodes(r); // add first level's facts

               levelsSizes[0] = Facts.size();

               int Levelbegin = 0;       

               // get all facts from xml tree and put them into an array
               int d = 1;
               while(d < depth) // while there is a level to explore
               {
                   int level_size = 0;
                   for(int i = Levelbegin; i < levelsSizes[d-1]; i++) // for each node of the d-1 level
                   {
                       Node currNode = (Node)Facts.get(i);
                       Vector currNode_NextLevelFacts = XmlMsgs.ReturnChildNodes(currNode);   

                       level_size = level_size + currNode_NextLevelFacts.size();
                       Facts.addAll(currNode_NextLevelFacts); // add their childs to Facts

                   }

                   levelsSizes[d] = level_size;
                   Levelbegin = Levelbegin + level_size;
                   d++;
               }

               //logger.debug("Facts size:" + Facts.size());
               Object Facts_Array [] = Facts.toArray();

               // sort each level by interest
               Levelbegin = 0;  
               for(int i = 0; i < depth; i++)
               { 
                    if(i == 0)
                    {
                        Arrays.sort(Facts_Array,Levelbegin, Levelbegin + levelsSizes[i]  ,new InterestComparatorImpl(false));
                    }
                    else
                    {                
                        Levelbegin = Levelbegin +  levelsSizes[i-1];
                        int LevelEnd = Levelbegin + levelsSizes[i] ;
                        //logger.debug("Levelbegin:" + Levelbegin + " LevelEnd" + LevelEnd);
                        Arrays.sort(Facts_Array, Levelbegin, LevelEnd  ,new InterestComparatorImpl(false));

                    }
               }

               //logger.debug("Facts array size:" + Facts_Array.length);

              
               int max_facts = 0;

               if(UserID != null)
               {
                   max_facts = UMVis.getNumberOfFacts(UserID, UserType);
               }
               else if(UserType!=null)
               {
                    UserTypeParameters UTP = UMQM.getParametersForUserType(UserType);
                    max_facts = UTP.getFactsPerPage();
               }

               boolean AllFirstLevelFactsAssimilated = true;

               // check if all first level facts are assimilated
               for(int i = 0; i < levelsSizes[0]; i++)
               {
                   Node currNode = (Node)Facts_Array[i];
                   if( Float.parseFloat(XmlMsgs.getAttribute(currNode,XmlMsgs.prefix,XmlMsgs.ASSIMIL_SCORE)) <  1.0F 
                       && Integer.parseInt(XmlMsgs.getAttribute(currNode,XmlMsgs.prefix,XmlMsgs.INTEREST)) != 0)
                   {
                       AllFirstLevelFactsAssimilated = false;
                   }
               }

               all_Facts_Are_Assimilated = AllFirstLevelFactsAssimilated; // !!!!!
                       
               // if are not assimilated
               if (!AllFirstLevelFactsAssimilated)
               {// !AllFirstLevelFactsAssimilated
                   int count_facts = 0;

                   for(int i = 0; i < Facts_Array.length; i++)
                   {
                       Node currNode = (Node)Facts_Array[i];
                       //logger.debug(XmlMsgs.getStringDescription(currNode,true));

                       logger.debug(i +  " " + Integer.parseInt(XmlMsgs.getAttribute(currNode,XmlMsgs.prefix,XmlMsgs.INTEREST))
                       + " " + Float.parseFloat(XmlMsgs.getAttribute(currNode,XmlMsgs.prefix,XmlMsgs.ASSIMIL_SCORE)) ) ;

                       //keep the facts which are not assimilated and have positive interest 
                       if(count_facts < max_facts 
                           && Integer.parseInt(XmlMsgs.getAttribute(currNode,XmlMsgs.prefix,XmlMsgs.INTEREST)) != 0
                           && Float.parseFloat(XmlMsgs.getAttribute(currNode,XmlMsgs.prefix,XmlMsgs.ASSIMIL_SCORE)) < 1.0F)
                       {
                           // keep it and
                           // increase its assimilation score!!!!    
                           count_facts++;

                           if(!XmlMsgs.compare(currNode, XmlMsgs.prefix , XmlMsgs.type))
                           {
                               String factID = "[" + XmlMsgs.getAttribute(currNode,XmlMsgs.prefix, XmlMsgs.REF) + ", " + currNode.getNamespaceURI() + currNode.getLocalName() + ", " + XmlMsgs.getAttribute(currNode,XmlMsgs.prefix, "Val") + "]";                   
                               conveyedFacts.add(factID);

                               String microplanID = currNode.getNamespaceURI() + currNode.getLocalName() + "-" +  XmlMsgs.getAttribute(currNode,XmlMsgs.prefix, XmlMsgs.TEMPLATE_TAG)+ "-" + getLanguage();                                              
                               usedMicroPlanningExpressions.add(microplanID);
                               //mentionedEntities.add(XmlMsgs.getAttribute(currNode,XmlMsgs.prefix, "Val"));
                           }
                           else
                           {
                               String factID = "[" + XmlMsgs.getAttribute(currNode,XmlMsgs.prefix, XmlMsgs.REF) + ", " + "http://www.w3.org/1999/02/22-rdf-syntax-ns#type" + ", " + XmlMsgs.getAttribute(currNode,XmlMsgs.prefix, "Val") + "]";                   
                               conveyedFacts.add(factID);                       
                               //mentionedEntities.add( XmlMsgs.getAttribute(currNode,XmlMsgs.prefix, "Val"));
                           }
                       }
                       else
                       {
                           //logger.debug("deleting from the xml tree");
                           // delete from the xml tree
                           currNode.getParentNode().removeChild(currNode);                
                       }
                    }
               }//!AllFirstLevelFactsAssimilated
               else
               {//AllFirstLevelFactsAssimilated
                   int count_facts = 0;

                   for(int i = 0; i < Facts_Array.length; i++)
                   {
                       Node currNode = (Node)Facts_Array[i];
                       //logger.debug(XmlMsgs.getStringDescription(currNode,true));

                       logger.debug(i +  " " + Integer.parseInt(XmlMsgs.getAttribute(currNode,XmlMsgs.prefix,XmlMsgs.INTEREST))
                       + " " + Float.parseFloat(XmlMsgs.getAttribute(currNode,XmlMsgs.prefix,XmlMsgs.ASSIMIL_SCORE)) ) ;

                       if(Integer.parseInt(XmlMsgs.getAttribute(currNode,XmlMsgs.prefix,XmlMsgs.INTEREST)) != 0)
                       {// interest != 0
                           // keep it 
                           count_facts++;
                       }
                       else
                       {
                           //logger.debug("deleting fro the xml tree");
                           // delete from the xml tree
                           currNode.getParentNode().removeChild(currNode);
                       }
                    }               
               }//AllFirstLevelFactsAssimilated


               return MyXmlMsgs;
           }
           else
           {
               return MyXmlMsgs;
           }           
       }
       catch(Exception e)
       {    
           e.printStackTrace();
       }
       
       return MyXmlMsgs;
   }
   
   private void describeBoolCls(XmlMsgs MyXmlMsgs, BooleanClassDescription boolClsDesc,Element Elem,OntClass parent,String userType, String UserID)
   {    	
    	ExtendedIterator i = boolClsDesc.listOperands();    	
    		    			
    	while( i.hasNext())
        { // for each operand
    		    		    		
            OntClass c = (OntClass)i.next();
    		
            if(c.isRestriction())
            { //if c is a Restriction    			
                describeRestriction(MyXmlMsgs, c.asRestriction(),Elem,  userType, UserID);    			
            }    		
            else 
            {
                describeCls(MyXmlMsgs, c,Elem,parent, userType,  UserID);
            }    		
    		
    	}//while
    	    	
    }//describeCls
//-----------------------------------------------------------------------------------    
    private void describeCls(XmlMsgs MyXmlMsgs, OntClass cls,Element Elem,OntClass parent, String userType, String UserID)
    {
    	
    	if (cls.isRestriction())
        {
            describeRestriction(MyXmlMsgs, cls.asRestriction(),Elem,  userType, UserID);    				    		
    	}
    	else if (!cls.isAnon())
        {    		   		
            Element t = MyXmlMsgs.AddNewElement(Elem,XmlMsgs.owlnlNS,XmlMsgs.prefix,XmlMsgs.CLASS_TAG);
            Node parentNode = t.getParentNode();   
            
            MyXmlMsgs.SetAttr(t,XmlMsgs.owlnlNS,XmlMsgs.prefix, XmlMsgs.REF, parent.getURI());
            MyXmlMsgs.SetAttr(t,XmlMsgs.owlnlNS,XmlMsgs.prefix, "Val", cls.getURI());
            MyXmlMsgs.SetAttr(t, XmlMsgs.owlnlNS, XmlMsgs.prefix,XmlMsgs.ORDER_TAG,0 + "");
    	}
    	else if (cls.isAnon())
        {   			

            if(cls.isIntersectionClass())
            { //if cls is IntersectionClass	    		
                    Element t = MyXmlMsgs.AddNewElement(Elem,XmlMsgs.owlnlNS,XmlMsgs.prefix,XmlMsgs.INTERSECTION_OF_TAG);
                    describeBoolCls(MyXmlMsgs, cls.asIntersectionClass(),t,cls, userType, UserID);    		
            }
            else if (cls.isUnionClass())
            { //if cls is UnionClass    			    		
                    Element t = MyXmlMsgs.AddNewElement(Elem,XmlMsgs.owlnlNS,XmlMsgs.prefix,XmlMsgs.UNION_OF_TAG);
                    describeBoolCls(MyXmlMsgs, cls.asUnionClass(),t,cls, userType, UserID);
            }
            else if (cls.isComplementClass())
            { //if cls is ComplementClass    			    		
                    Element t = MyXmlMsgs.AddNewElement(Elem,XmlMsgs.owlnlNS,XmlMsgs.prefix,XmlMsgs.COMPLEMENT_OF_TAG);
                    describeBoolCls(MyXmlMsgs, cls.asComplementClass(),t,cls, userType, UserID);
            }
            else if (cls.isEnumeratedClass()){ //if cls is EnumeratedClass    			    		
                    Element t = MyXmlMsgs.AddNewElement(Elem,XmlMsgs.owlnlNS,XmlMsgs.prefix,XmlMsgs.ENUMERATION_OF_TAG);
                    describeEnumCls(MyXmlMsgs, cls.asEnumeratedClass(),t,cls);
            }
            else
            {

            }   		
    	}
    	else{
    		
    	}
    }
    //----------------------------------------------------------------------------------- 
    // describes an enumeration class 
    private void describeEnumCls(XmlMsgs MyXmlMsgs, EnumeratedClass EnumCls,Element Elem,OntClass parent)
    {
        for(ExtendedIterator i = EnumCls.listOneOf(); i.hasNext();)
        {
                OntResource ind = (OntResource)i.next(); 			
                Element t = MyXmlMsgs.AddNewElement(Elem,XmlMsgs.owlnlNS,XmlMsgs.prefix,"Individual");
                MyXmlMsgs.setText(t,ind.getURI());
        }//for 	 	
    }
		
    //describe restriction
    private void describeRestriction(XmlMsgs MyXmlMsgs, Restriction r,Element Elem, String userType, String UserID)
    {
        try
        {
            if(r.isHasValueRestriction())
            { // if r is a HasValueRestriction
                    Element t = MyXmlMsgs.AddNewElement(Elem,XmlMsgs.owlnlNS,XmlMsgs.prefix,XmlMsgs.HAS_VALUE_RESTRICTION_TAG);
                    
                    HasValueRestriction hvr = r.asHasValueRestriction();
                    
                    String predicateURI = hvr.getOnProperty().getURI();
                    //choose microplan
                    String microplanSelected = chooseMicroplan(predicateURI, UMVis, UserID, userType);
                    
                    MyXmlMsgs.SetAttr(t, XmlMsgs.owlnlNS, XmlMsgs.prefix ,"OnProperty",predicateURI);
                    MyXmlMsgs.SetAttr(t, XmlMsgs.owlnlNS,XmlMsgs.prefix, "Val",hvr.getHasValue().toString());
                    MyXmlMsgs.SetAttr(t, XmlMsgs.owlnlNS,XmlMsgs.prefix, XmlMsgs.ORDER_TAG, MAOQM.getOrder(hvr.getOnProperty().getURI()) + "");
                    MyXmlMsgs.SetAttr(t, XmlMsgs.owlnlNS,XmlMsgs.prefix, XmlMsgs.TEMPLATE_TAG, MAOQM.chooseMicroplan(hvr.getOnProperty().getURI(), getLanguage()));
            }
            else if (r.isMaxCardinalityRestriction())
            { // if r is a MaxCardinalityRestriction
                    /*
                    Element t = MyXmlMsgs.AddNewElement(Elem,XmlMsgs.owlnlNS,XmlMsgs.prefix,XmlMsgs.MAX_CARDINALITY_RESTRICTION_TAG);

                    MaxCardinalityRestriction MaxCR = r.asMaxCardinalityRestriction();
                    MyXmlMsgs.SetAttr(t,XmlMsgs.owlnlNS,XmlMsgs.prefix,"OnProperty",MaxCR.getOnProperty().getURI());
                    MyXmlMsgs.SetAttr(t,XmlMsgs.owlnlNS,XmlMsgs.prefix,"Val",MaxCR.getMaxCardinality()+"");
                    MyXmlMsgs.SetAttr(t,XmlMsgs.owlnlNS,XmlMsgs.prefix,XmlMsgs.ORDER_TAG,MAOQM.getOrder(MaxCR.getOnProperty().getURI()) + "");
                    MyXmlMsgs.SetAttr(t,XmlMsgs.owlnlNS,XmlMsgs.prefix,XmlMsgs.TEMPLATE_TAG, MAOQM.chooseMicroplan(MaxCR.getOnProperty().getURI(), getLanguage()));
                     */
            }
            else if (r.isMinCardinalityRestriction())
            { // if r is a MinCardinalityRestriction
                   /*
                    Element t = MyXmlMsgs.AddNewElement(Elem,XmlMsgs.owlnlNS,XmlMsgs.prefix,XmlMsgs.MIN_CARDINALITY_RESTRICTION_TAG);

                    
                    MinCardinalityRestriction MinCR = r.asMinCardinalityRestriction();
                    MyXmlMsgs.SetAttr(t,XmlMsgs.owlnlNS,XmlMsgs.prefix,"OnProperty",MinCR.getOnProperty().getURI());
                    MyXmlMsgs.SetAttr(t,XmlMsgs.owlnlNS,XmlMsgs.prefix, "Val",MinCR.getMinCardinality()+"");	
                    MyXmlMsgs.SetAttr(t,XmlMsgs.owlnlNS,XmlMsgs.prefix, XmlMsgs.ORDER_TAG, MAOQM.getOrder(MinCR.getOnProperty().getURI()) + "");
                    MyXmlMsgs.SetAttr(t,XmlMsgs.owlnlNS,XmlMsgs.prefix, XmlMsgs.TEMPLATE_TAG,MAOQM.chooseMicroplan(MinCR.getOnProperty().getURI(), getLanguage()));
                     */
            }
            else if (r.isCardinalityRestriction())
            { // if r is a CardinalityRestriction
                    /*
                    Element t = MyXmlMsgs.AddNewElement(Elem,XmlMsgs.owlnlNS,XmlMsgs.prefix,XmlMsgs.CARDINALITY_RESTRICTION_TAG);			

                    CardinalityRestriction CR = r.asCardinalityRestriction();

                    MyXmlMsgs.SetAttr(t,XmlMsgs.owlnlNS, XmlMsgs.prefix, "OnProperty",CR.getOnProperty().getURI());
                    MyXmlMsgs.SetAttr(t,XmlMsgs.owlnlNS, XmlMsgs.prefix, "Val", CR.getCardinality()+"");	
                    MyXmlMsgs.SetAttr(t,XmlMsgs.owlnlNS, XmlMsgs.prefix, XmlMsgs.ORDER_TAG, MAOQM.getOrder(CR.getOnProperty().getURI()) + "");
                    MyXmlMsgs.SetAttr(t,XmlMsgs.owlnlNS, XmlMsgs.prefix, XmlMsgs.TEMPLATE_TAG, MAOQM.chooseMicroplan(CR.getOnProperty().getURI(), getLanguage()));		
                     */
            }        
            else if (r.isAllValuesFromRestriction())
            { // if r is a AllValuesFromRestriction
                    
                    Element t = MyXmlMsgs.AddNewElement(Elem,XmlMsgs.owlnlNS,XmlMsgs.prefix,XmlMsgs.ALL_VALUES_FROM_RESTRICTION_TAG);

                    AllValuesFromRestriction AVFR = r.asAllValuesFromRestriction();
                    MyXmlMsgs.SetAttr(t,XmlMsgs.owlnlNS,XmlMsgs.prefix,XmlMsgs.ORDER_TAG, MAOQM.getOrder(AVFR.getOnProperty().getURI()) + "");
                    MyXmlMsgs.SetAttr(t,XmlMsgs.owlnlNS,XmlMsgs.prefix,XmlMsgs.TEMPLATE_TAG, MAOQM.chooseMicroplan(AVFR.getOnProperty().getURI(), getLanguage()));
                    
                    MyXmlMsgs.SetAttr(t,XmlMsgs.owlnlNS, XmlMsgs.prefix, "OnProperty",AVFR.getOnProperty().getURI());
                    Element from = MyXmlMsgs.AddNewElement(t,XmlMsgs.owlnlNS,XmlMsgs.prefix,"From");
                    describeCls(MyXmlMsgs, (OntClass)AVFR.getAllValuesFrom().as(OntClass.class), from, r, userType, UserID);

            }
            else if (r.isSomeValuesFromRestriction())
            { // if r is a SomeValuesFromRestriction
                    Element t = MyXmlMsgs.AddNewElement(Elem,XmlMsgs.owlnlNS,XmlMsgs.prefix,XmlMsgs.SOME_VALUES_FROM_RESTRICTION_TAG);

                    SomeValuesFromRestriction SVFR = r.asSomeValuesFromRestriction();
                    MyXmlMsgs.SetAttr(t,XmlMsgs.owlnlNS,XmlMsgs.prefix, XmlMsgs.ORDER_TAG, MAOQM.getOrder(SVFR.getOnProperty().getURI()) + "");
                    MyXmlMsgs.SetAttr(t,XmlMsgs.owlnlNS,XmlMsgs.prefix, XmlMsgs.TEMPLATE_TAG, MAOQM.chooseMicroplan(SVFR.getOnProperty().getURI(), getLanguage()));
                    
                    MyXmlMsgs.SetAttr(t,XmlMsgs.owlnlNS, XmlMsgs.prefix, "OnProperty",SVFR.getOnProperty().getURI());
                    Element from = MyXmlMsgs.AddNewElement(t,XmlMsgs.owlnlNS,XmlMsgs.prefix,"From");
                    describeCls(MyXmlMsgs,(OntClass)SVFR.getSomeValuesFrom().as(OntClass.class), from , r, userType, UserID);	

            }
            else
            { // ERROR something bad happened
                    logger.debug("ERRoR!!!!!");
            }			
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }//describe restriction
//----------------------------------------------------------------------------------- 	
    
 /**************** Functions used for the generation of comparisons ***********************/
    
    
public void AddUniqueMsg(int depth, int Level ,String subURI, String predicateNS, String predicateLocalName, String objURI, OntProperty ontPrp, XmlMsgs MyXmlMsgs, Node r, String userID , String UserType, ComparisonTree tree, ComparisonTree UTree, ComparisonNode unique, boolean proste8hke, Individual MyInstance, String isA, NLGEngineServer nav)
{
    try
    {

        String predicateURI = predicateNS + predicateLocalName;
        String pref = ontModel.getNsURIPrefix(predicateNS);



        if(pref == null)
        {
            pref = "";
        }

        //create new msg
        Element msg = MyXmlMsgs.AddNewElement( (Element)r , predicateNS, pref, predicateLocalName);                   

        //choose microplan
        String microplanSelected = chooseMicroplan(predicateURI, UMVis, userID, UserType);

        MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix, XmlMsgs.TEMPLATE_TAG, microplanSelected);
        MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix, XmlMsgs.REF, subURI);
        MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix, "Val", objURI);
        MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix, XmlMsgs.LEVEL, Level+"");    
        MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix, "Unique", unique.getName());
        if(tree.findNode(isA) != null)
            MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix,"akoma", "true");
        if (!proste8hke)
        {
            tree.addElement(MyInstance.getURI(),ontModel,MAOQM.getPropertiesUsedForComparisons());
            proste8hke = true;
        }
        MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix, XmlMsgs.ORDER_TAG, MAOQM.getOrder(predicateURI) + "");                            

        String AggAllowed = MAOQM.getisAggAllowed(predicateURI,
                predicateURI + "-" + microplanSelected + "-" + getLanguage(), getLanguage()) + "";

        MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix, XmlMsgs.AGGREG_ALLOWED, AggAllowed);
        MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix, XmlMsgs.FOCUS_LOST, "undefined");

        String InterestValue = "";
        String assimScore = "";

        if(userID != null)
        {
            //ListOfFacts.add(new memNode())
            String myfoo = NLGEngine.getClassType(ontModel, subURI);

            InterestValue = "" + (int)UMVis.getInterestFact(FactFactory.getFact(subURI, predicateURI, objURI), myfoo,  userID, UserType, personality);
            assimScore = "" + UMVis.getAssimilationScore(FactFactory.getFact(subURI, predicateURI, objURI), myfoo, userID, UserType);     

        }
        else
        {
            if(UserType !=null)
            {
                InterestValue = String.valueOf(UMQM.getInterest(predicateURI, subURI, UserType));
            }
            else
            {
                InterestValue = "1";
            }

            assimScore = "0";
        }

        MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix, XmlMsgs.INTEREST, InterestValue);
        MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix, XmlMsgs.ASSIMIL_SCORE, assimScore);


        if(ontPrp.isObjectProperty())   
        {
            MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix,XmlMsgs.prpType, XmlMsgs.ObjectProperty);
        }
        else if(ontPrp.isDatatypeProperty())     
        {
            MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix,XmlMsgs.prpType, XmlMsgs.DatatypeProperty);
        }

        // recursively call getMsgsOfAnInstance
        if(depth > 1)
        {                           
            Level++;
            int d = depth - 1;
            getMsgsOfAnInstance( objURI, MyXmlMsgs, d , Level, msg, UserType, userID,tree,UTree,false, nav);
        }
    }
    catch(Exception e)
    {
        e.printStackTrace();
    }
}
    
private void AddCommonMsg(int depth, int Level ,String subURI, String predicateNS, String predicateLocalName, String objURI, OntProperty ontPrp, XmlMsgs MyXmlMsgs, Node r, String userID , String UserType, ComparisonTree tree, ComparisonTree UTree, ComparisonNode common, boolean proste8hke, Individual MyInstance, String isA, NLGEngineServer nav)
{
    try
    {

        String predicateURI = predicateNS + predicateLocalName;
        String pref = ontModel.getNsURIPrefix(predicateNS);

        if(pref == null)
        {
            pref = "";
        }

        //create new msg
        Element msg = MyXmlMsgs.AddNewElement( (Element)r , predicateNS, pref, predicateLocalName);                   

        //choose microplan
        String microplanSelected = chooseMicroplan(predicateURI, UMVis, userID, UserType);

        MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix, XmlMsgs.TEMPLATE_TAG, microplanSelected);
        MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix, XmlMsgs.REF, subURI);
        MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix, "Val", objURI);
        MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix, XmlMsgs.LEVEL, Level+"");            
        MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix, "Common", common.getName());

        if(tree.findNode(isA) != null)
            MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix,"akoma", "true");
        if (!proste8hke)
        {
            tree.addElement(MyInstance.getURI(),ontModel,MAOQM.getPropertiesUsedForComparisons());
            proste8hke = true;
        }
        MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix, XmlMsgs.ORDER_TAG, MAOQM.getOrder(predicateURI) + "");                            

        String AggAllowed = MAOQM.getisAggAllowed(predicateURI,
                predicateURI + "-" + microplanSelected + "-" + getLanguage(), getLanguage()) + "";

        MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix, XmlMsgs.AGGREG_ALLOWED, AggAllowed);
        MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix, XmlMsgs.FOCUS_LOST, "undefined");

        String InterestValue = "";
        String assimScore = "";

        if(userID != null)
        {
            //ListOfFacts.add(new memNode())
            String myfoo = NLGEngine.getClassType(ontModel, subURI);

            InterestValue = "" + (int)UMVis.getInterestFact(FactFactory.getFact(subURI, predicateURI, objURI), myfoo,  userID, UserType, personality);
            assimScore = "" + UMVis.getAssimilationScore(FactFactory.getFact(subURI, predicateURI, objURI), myfoo, userID, UserType);     

        }
        else
        {
            if(UserType !=null)
            {
                InterestValue = String.valueOf(UMQM.getInterest(predicateURI, subURI, UserType));
            }
            else
            {
                InterestValue = "1";
            }

            assimScore = "0";
        }

        MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix, XmlMsgs.INTEREST, InterestValue);
        MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix, XmlMsgs.ASSIMIL_SCORE, assimScore);


        if(ontPrp.isObjectProperty())   
        {
            MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix,XmlMsgs.prpType, XmlMsgs.ObjectProperty);
        }
        else if(ontPrp.isDatatypeProperty())     
        {
            MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix,XmlMsgs.prpType, XmlMsgs.DatatypeProperty);
        }

        // recursively call getMsgsOfAnInstance
        if(depth > 1)
        {                           
            Level++;
            int d = depth - 1;
            getMsgsOfAnInstance( objURI, MyXmlMsgs, d , Level, msg, UserType, userID,tree,UTree,false, nav);
        }
    }
    catch(Exception e)
    {
        e.printStackTrace();
    }
}
    
private void AddMultiComparistonMsg(int depth, int Level ,String subURI, String predicateNS, String predicateLocalName, String objURI, OntProperty ontPrp, XmlMsgs MyXmlMsgs, Node r, String userID , String UserType, ComparisonTree tree, ComparisonTree UTree, ComparisonNode comparator, boolean proste8hke, Individual MyInstance, String isA, boolean most, Statement t, RDFNode node, String location, NLGEngineServer nav)
{
    try
    {

        String predicateURI = predicateNS + predicateLocalName;
        String pref = ontModel.getNsURIPrefix(predicateNS);

        int randomFeature = comparator.getIndexOfAttribute(t.getPredicate().getLocalName());

        if(pref == null)
        {
            pref = "";
        }

        //create new msg
        Element msg = MyXmlMsgs.AddNewElement( (Element)r , predicateNS, pref, predicateLocalName);                   

        //choose microplan
        String microplanSelected = chooseMicroplan(predicateURI, UMVis, userID, UserType);

        MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix, XmlMsgs.TEMPLATE_TAG, microplanSelected);
        MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix, XmlMsgs.REF, subURI);
        MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix, "Val", objURI);
        MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix,"Comparator", comparator.getName());
        addComparisonEntities(MyXmlMsgs, msg, comparator, tree, nav);
                
        //MyXmlMsgs.SetAttr(tmp, "", XmlMsgs.prefix,"Comparator", "http://www.w3.org/TR/2003/CR-owl-guide-20030818/wine#Region");
        //MyXmlMsgs.SetAttr(tmp, "", XmlMsgs.prefix,"Feature", nodeComparing.toString());
        if (comparator.getNumberOfObjects()>1)
            MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix,"form", "plural");
        else
            MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix,"form", "single");
        if (node.toString().compareTo(comparator.getAttributeValues().get(randomFeature).getDescription().get(0).Value)!=0)
        //if (node.toString().compareTo("http://www.w3.org/TR/2003/CR-owl-guide-20030818/wine#FrenchRegion")!=0)
            //MyXmlMsgs.SetAttr(tmp, "", XmlMsgs.prefix,"Feature", "http://www.w3.org/TR/2003/CR-owl-guide-20030818/wine#FrenchRegion");
            MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix,"Feature", comparator.getAttributeValues().get(randomFeature).getDescription().get(0).Value);

        MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix, XmlMsgs.REF, MyInstance.getURI());
        if(tree.findNode(isA) != null)
            MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix,"akoma", "true");
        if (!proste8hke)
        {
            tree.addElement(MyInstance.getURI(),ontModel,MAOQM.getPropertiesUsedForComparisons());
            proste8hke = true;
        }
        if (most)
            MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix,"most", "true");
        if(tree.isParent(MyInstance.getURI(),comparator.getName()))
            MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix,"previous", "true");

        if(comparator.getName().equalsIgnoreCase(isA))
        {
            MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix,"same", "true");
        }
        
        MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix,"location", location);
        
        // an o comparator den einai paidi ths rizas,
        // kai den einai idio pragma me ayto poy perigrafoyme
        //grapse ti typoy einai
    //    if (!(tree.rootHasChild(comparator.getName())) && !(comparator.getName().equalsIgnoreCase(isA)))
    //    {
    //        MyXmlMsgs.SetAttr(tmp, XmlMsgs.owlnlNS, XmlMsgs.prefix,"kind", tree.kindOf(comparator.getName()));
    //    }

        MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix,"multyComp", "true");

        MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix, XmlMsgs.LEVEL, Level+"");            
        MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix, XmlMsgs.ORDER_TAG, MAOQM.getOrder(predicateURI) + "");                            

        String AggAllowed = MAOQM.getisAggAllowed(predicateURI,
                predicateURI + "-" + microplanSelected + "-" + getLanguage(), getLanguage()) + "";

        MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix, XmlMsgs.AGGREG_ALLOWED, AggAllowed);
        MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix, XmlMsgs.FOCUS_LOST, "undefined");

        String InterestValue = "";
        String assimScore = "";

        if(userID != null)
        {
            //ListOfFacts.add(new memNode())
            String myfoo = NLGEngine.getClassType(ontModel, subURI);

            InterestValue = "" + (int)UMVis.getInterestFact(FactFactory.getFact(subURI, predicateURI, objURI), myfoo,  userID, UserType, personality);
            assimScore = "" + UMVis.getAssimilationScore(FactFactory.getFact(subURI, predicateURI, objURI), myfoo, userID, UserType);     

        }
        else
        {
            if(UserType !=null)
            {
                InterestValue = String.valueOf(UMQM.getInterest(predicateURI, subURI, UserType));
            }
            else
            {
                InterestValue = "1";
            }

            assimScore = "0";
        }

        MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix, XmlMsgs.INTEREST, InterestValue);
        MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix, XmlMsgs.ASSIMIL_SCORE, assimScore);


        if(ontPrp.isObjectProperty())   
        {
            MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix,XmlMsgs.prpType, XmlMsgs.ObjectProperty);
        }
        else if(ontPrp.isDatatypeProperty())     
        {
            MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix,XmlMsgs.prpType, XmlMsgs.DatatypeProperty);
        }

        // recursively call getMsgsOfAnInstance
        if(depth > 1)
        {                           
            Level++;
            int d = depth - 1;
            getMsgsOfAnInstance( objURI, MyXmlMsgs, d , Level, msg, UserType, userID,tree,UTree,false, nav);
        }
    }
    catch(Exception e)
    {
        e.printStackTrace();
    }
}
    
private void AddComparisonMsg(int depth, int Level ,String subURI, String predicateNS, String predicateLocalName, String objURI, OntProperty ontPrp, XmlMsgs MyXmlMsgs, Node r, String userID , String UserType, ComparisonTree tree, ComparisonTree UTree, ComparisonNode comparator, boolean proste8hke, Individual MyInstance, String isA, boolean most, Statement t, RDFNode node, int randomFeature, String location, NLGEngineServer nav)
{
    try
    {

        String predicateURI = predicateNS + predicateLocalName;
        String pref = ontModel.getNsURIPrefix(predicateNS);

        if(pref == null)
        {
            pref = "";
        }

        //create new msg
        Element msg = MyXmlMsgs.AddNewElement( (Element)r , predicateNS, pref, predicateLocalName);                   

        //choose microplan
        String microplanSelected = chooseMicroplan(predicateURI, UMVis, userID, UserType);

        MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix, XmlMsgs.TEMPLATE_TAG, microplanSelected);
        MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix, XmlMsgs.REF, subURI);
        MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix, "Val", objURI);
        MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix,"Comparator", comparator.getName());
        addComparisonEntities(MyXmlMsgs, msg, comparator, tree, nav);
        
        //MyXmlMsgs.SetAttr(tmp, "", XmlMsgs.prefix,"Comparator", "http://www.w3.org/TR/2003/CR-owl-guide-20030818/wine#Region");
        //MyXmlMsgs.SetAttr(tmp, "", XmlMsgs.prefix,"Feature", nodeComparing.toString());
        if (comparator.getNumberOfObjects()>1)
            MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix,"form", "plural");
        else
            MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix,"form", "single");
        if (node.toString().compareTo(comparator.getAttributeValues().get(randomFeature).getDescription().get(0).Value)!=0)
        //if (node.toString().compareTo("http://www.w3.org/TR/2003/CR-owl-guide-20030818/wine#FrenchRegion")!=0)
            //MyXmlMsgs.SetAttr(tmp, "", XmlMsgs.prefix,"Feature", "http://www.w3.org/TR/2003/CR-owl-guide-20030818/wine#FrenchRegion");
            MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix,"Feature", comparator.getAttributeValues().get(randomFeature).getDescription().get(0).Value);

        MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix, XmlMsgs.REF, MyInstance.getURI());

        if(tree.findNode(isA) != null)
            MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix,"akoma", "true");
        if (!proste8hke)
        {
            tree.addElement(MyInstance.getURI(),ontModel,MAOQM.getPropertiesUsedForComparisons());
            proste8hke = true;
        }
        if (most)
            MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix,"most", "true");
        if(tree.isParent(MyInstance.getURI(),comparator.getName()))
            MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix,"previous", "true");

        if(comparator.getName().equalsIgnoreCase(isA))
        {
            MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix,"same", "true");
        }
        
        MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix,"location", location);

        // an o comparator den einai paidi ths rizas, grapse ti typoy einai
        /*if (!tree.rootHasChild(comparator.getName())&& !(comparator.getName().equalsIgnoreCase(isA))&&!tree.findRoot().getName().equalsIgnoreCase(comparator.getName()))
        {
            //logger.debug("lol"+comparator.getName());
            MyXmlMsgs.SetAttr(tmp, XmlMsgs.owlnlNS, XmlMsgs.prefix,"kind", tree.kindOf(comparator.getName()));
        }*/ 
        MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix, XmlMsgs.LEVEL, Level+"");            
        MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix, XmlMsgs.ORDER_TAG, MAOQM.getOrder(predicateURI) + "");                            

        String AggAllowed = MAOQM.getisAggAllowed(predicateURI,
                predicateURI + "-" + microplanSelected + "-" + getLanguage(), getLanguage()) + "";

        MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix, XmlMsgs.AGGREG_ALLOWED, AggAllowed);
        MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix, XmlMsgs.FOCUS_LOST, "undefined");

        String InterestValue = "";
        String assimScore = "";

        if(userID != null)
        {
            //ListOfFacts.add(new memNode())
            String myfoo = NLGEngine.getClassType(ontModel, subURI);

            InterestValue = "" + (int)UMVis.getInterestFact(FactFactory.getFact(subURI, predicateURI, objURI), myfoo,  userID, UserType, personality);
            assimScore = "" + UMVis.getAssimilationScore(FactFactory.getFact(subURI, predicateURI, objURI), myfoo, userID, UserType);     

        }
        else
        {
            if(UserType !=null)
            {
                InterestValue = String.valueOf(UMQM.getInterest(predicateURI, subURI, UserType));
            }
            else
            {
                InterestValue = "1";
            }

            assimScore = "0";
        }

        MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix, XmlMsgs.INTEREST, InterestValue);
        MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix, XmlMsgs.ASSIMIL_SCORE, assimScore);


        if(ontPrp.isObjectProperty())   
        {
            MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix,XmlMsgs.prpType, XmlMsgs.ObjectProperty);
        }
        else if(ontPrp.isDatatypeProperty())     
        {
            MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix,XmlMsgs.prpType, XmlMsgs.DatatypeProperty);
        }

        // recursively call getMsgsOfAnInstance
        if(depth > 1)
        {                           
            Level++;
            int d = depth - 1;
            getMsgsOfAnInstance( objURI, MyXmlMsgs, d , Level, msg, UserType, userID,tree,UTree,false, nav);
        }
    }
    catch(Exception e)
    {
        e.printStackTrace();
    }
}
        
private void AddCommonBluredMsg(int depth, int Level ,String subURI, String predicateNS, String predicateLocalName, String objURI, OntProperty ontPrp, XmlMsgs MyXmlMsgs, Node r, String userID , String UserType, ComparisonTree tree, ComparisonTree UTree, ComparisonNode commonBlured, boolean proste8hke, Individual MyInstance, String isA, boolean most, Statement t, RDFNode node, int randomCommonBlured, NLGEngineServer nav)
{
    try
    {

        String predicateURI = predicateNS + predicateLocalName;
        String pref = ontModel.getNsURIPrefix(predicateNS);

        if(pref == null)
        {
            pref = "";
        }

        //create new msg
        Element msg = MyXmlMsgs.AddNewElement( (Element)r , predicateNS, pref, predicateLocalName);                   

        //choose microplan
        String microplanSelected = chooseMicroplan(predicateURI, UMVis, userID, UserType);

        MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix, XmlMsgs.TEMPLATE_TAG, microplanSelected);
        MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix, XmlMsgs.REF, subURI);
        MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix, "Val", objURI);
        MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix,"Comparator", commonBlured.getName());
        addComparisonEntities(MyXmlMsgs, msg, commonBlured, tree, nav);
        
        //MyXmlMsgs.SetAttr(tmp, "", XmlMsgs.prefix,"Comparator", "http://www.w3.org/TR/2003/CR-owl-guide-20030818/wine#Region");
        //MyXmlMsgs.SetAttr(tmp, "", XmlMsgs.prefix,"Feature", nodeComparing.toString());
        //if (comparator.getNumberOfObjects()>1)
            MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix,"form", "plural");
            MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix,"MostCommon", "true");
        //else
        //    MyXmlMsgs.SetAttr(tmp, XmlMsgs.owlnlNS, XmlMsgs.prefix,"form", "single");
        if (node.toString().compareTo(commonBlured.getAttributeValues().get(randomCommonBlured).getDescription().get(0).Value)!=0)
        //if (node.toString().compareTo("http://www.w3.org/TR/2003/CR-owl-guide-20030818/wine#FrenchRegion")!=0)
            //MyXmlMsgs.SetAttr(tmp, "", XmlMsgs.prefix,"Feature", "http://www.w3.org/TR/2003/CR-owl-guide-20030818/wine#FrenchRegion");
            MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix,"Feature", commonBlured.getAttributeValues().get(randomCommonBlured).getDescription().get(0).Value);

        MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix, XmlMsgs.REF, MyInstance.getURI());
        if (!proste8hke)
        {
            tree.addElement(MyInstance.getURI(),ontModel,MAOQM.getPropertiesUsedForComparisons());
            proste8hke = true;
        }
        MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix,"most", "true");
        MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix, XmlMsgs.LEVEL, Level+"");            
        MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix, XmlMsgs.ORDER_TAG, MAOQM.getOrder(predicateURI) + "");                            

        String AggAllowed = MAOQM.getisAggAllowed(predicateURI,
                predicateURI + "-" + microplanSelected + "-" + getLanguage(), getLanguage()) + "";

        MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix, XmlMsgs.AGGREG_ALLOWED, AggAllowed);
        MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix, XmlMsgs.FOCUS_LOST, "undefined");

        String InterestValue = "";
        String assimScore = "";

        if(userID != null)
        {
            //ListOfFacts.add(new memNode())
            String myfoo = NLGEngine.getClassType(ontModel, subURI);

            InterestValue = "" + (int)UMVis.getInterestFact(FactFactory.getFact(subURI, predicateURI, objURI), myfoo,  userID, UserType, personality);
            assimScore = "" + UMVis.getAssimilationScore(FactFactory.getFact(subURI, predicateURI, objURI), myfoo, userID, UserType);     

        }
        else
        {
            if(UserType !=null)
            {
                InterestValue = String.valueOf(UMQM.getInterest(predicateURI, subURI, UserType));
            }
            else
            {
                InterestValue = "1";
            }

            assimScore = "0";
        }

        MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix, XmlMsgs.INTEREST, InterestValue);
        MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix, XmlMsgs.ASSIMIL_SCORE, assimScore);


        if(ontPrp.isObjectProperty())   
        {
            MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix,XmlMsgs.prpType, XmlMsgs.ObjectProperty);
        }
        else if(ontPrp.isDatatypeProperty())     
        {
            MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix,XmlMsgs.prpType, XmlMsgs.DatatypeProperty);
        }

        // recursively call getMsgsOfAnInstance
        if(depth > 1)
        {                           
            Level++;
            int d = depth - 1;
            getMsgsOfAnInstance( objURI, MyXmlMsgs, d , Level, msg, UserType, userID,tree,UTree,false, nav);
        }
    }
    catch(Exception e)
    {
        e.printStackTrace();
    }
}    

private void addComparisonEntities(XmlMsgs MyXmlMsgs, Element msg, ComparisonNode comparator, ComparisonTree compTree, NLGEngineServer nav)
{
     String entities = "";
     
     List<String> list = compTree.getAllChildren(comparator.getName());
     Vector<String> exhbits = new Vector<String>();
             
     for(int i = 0; i < list.size(); i++)
     {
        String entity = list.get(i);
        if( i == 0)
        {
            entities = entity;
        }
        else
        {
            entities = entities +  "," + entity;
        }
        
        exhbits.add(entity);
     }
     
     MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix,"ComparatorEntities", entities);
     
     gr.aueb.cs.nlg.NLGEngine.spaceCalc.fromTo  fromTo = spaceCalc.direction(exhbits,exhibitsPos, nav, -90);
     MyXmlMsgs.SetAttr(msg, XmlMsgs.owlnlNS, XmlMsgs.prefix,"ComparatorEntitiesTurn", fromTo.from + "," + fromTo.to);
}

/**************** Functions used for the generation of comparisons ***********************/    
    
}//class ContentSelection

class memNode
{
    public Node msg;
    public String propertyURI;
    public String ClassURI;
    public String InstanceURI;
    public String userID;
    
    public memNode(Node msg, String propertyURI, String ClassURI, String InstanceURI, String userID)
    {
        this.msg = msg;
        this.propertyURI = propertyURI;
        this.ClassURI = ClassURI;
        this.InstanceURI = InstanceURI;
        this.userID = userID;
    }

}