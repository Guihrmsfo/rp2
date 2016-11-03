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

package gr.aueb.cs.nlg.Utils;

import org.w3c.dom.*;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;

import java.io.*;
import java.util.*;

import gr.aueb.cs.nlg.*;
import gr.aueb.cs.nlg.NLFiles.*;
  
import gr.aueb.cs.nlg.NLGEngine.Lexicalisation;

import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import org.apache.log4j.Logger;


public class XmlMsgs extends XmlUtils
{	
        static Logger logger = Logger.getLogger(XmlMsgs.class);
	private Document doc;	
	private Element MsgsElement;	 
	//private String Language;
        
	//-----------------------------------------------------------------------------------
	//tags definition
	public	final static  String INTERSECTION_OF_TAG = "intersectionOf";
	public	final static  String SUBCLASS_OF_TAG = "subclassOf";
	public	final static  String UNION_OF_TAG = "unionOf";
	public	final static  String COMPLEMENT_OF_TAG = "complementOf"; 
	public	final static  String CLASS_TAG = "Cls";
	public	final static  String HAS_VALUE_RESTRICTION_TAG = "hasValueRestriction";
	public	final static  String SOME_VALUES_FROM_RESTRICTION_TAG = "someValuesFromRestriction";  
	public	final static  String ALL_VALUES_FROM_RESTRICTION_TAG = "allValuesFromRestriction";
	public	final static  String CARDINALITY_RESTRICTION_TAG = "CardRestriction";
	public	final static  String MIN_CARDINALITY_RESTRICTION_TAG = "MinCardRestriction";
	public	final static  String MAX_CARDINALITY_RESTRICTION_TAG = "MaxCardRestriction";
	public	final static  String ENUMERATION_OF_TAG = "enumerationOf";
	public	final static  String EQUIVALENT_CLASS_TAG = "equivalentClass";
	public	final static  String DISJOINT_WITH_TAG = "disjointWith";
	public	final static  String INSTANCE_TAG = "Instance";
	public	final static  String DIFFERENT_FROM_TAG = "differentFrom";
	public	final static  String SAME_AS_TAG = "sameAs";
	
        //-----------------------------------------------------------------------------------
	public	final static  String TEMPLATE_TAG = "Templ";
	public	final static  String ORDER_TAG = "Order";
	//-----------------------------------------------------------------------------------
	public	final static  int CLS_TYPE = 0;
	public	final static  int INST_TYPE = 1;
	//-----------------------------------------------------------------------------------	
        //slot tags        
	public	final static  String VERB_TAG = "Verb";
        
        public	final static  String singular_VERB_TAG = "singularVerb";
        public	final static  String plural_VERB_TAG = "pluralVerb";
        
	public	final static  String FILLER_TAG = "Filler";
	public	final static  String OWNER_TAG = "Owner";
	public	final static  String TEXT_TAG = "Text";
	public	final static  String CLSDESC_TAG = "ClsDesc";
                
        //from here
        public	final static  String RETYPE = "RE_TYPE";
        
        public	final static  String RE_AUTO = "RE_AUTO";
                
        public	final static  String RE_DEF_ART = "RE_DEF_ART";
        public	final static  String RE_INDEF_ART = "RE_INDEF_ART";
        public	final static  String RE_PRONOUN = "RE_PRONOUN";
        public	final static  String RE_DEMONSTRATIVE = "RE_DEMONSTRATIVE";
        public  final static  String RE_FULLNAME = "RE_FULLNAME";
        //to here
        
        public	final static String IS_A_TAG = "IS_A";
        public	final static String A_TAG = "A_OR_AN"; 
        
        //slots attributes
	public	final static  String CASE_TAG = "case";
	
	public	final static  String TENSE_TAG = "tense";
	public	final static  String VOICE_TAG = "voice";
		
	public	final static  String PASSIVE_VOICE = "passive";
	public	final static  String ACTIVE_VOICE = "active";
	
	public	final static  String TENSE_PRESENT = "present";
	public	final static  String TENSE_PAST = "past";
	public	final static  String TENSE_FUTURE = "future";
			
	public	final static String NOMINATIVE_TAG = "nominative";
        public	final static String GENITIVE_TAG = "genitive";
        public	final static String ACCUSATIVE_TAG = "accusative";
                                      
        public  final static String GENDER_MASCULINE = "masculine";
        public  final static String GENDER_FEMININE = "feminine";
        public  final static String GENDER_NONPESRSONAL= "nonpersonal";
                
        public  final static String GENDER_NEUTER= "neuter";
         
        public  final static String SINGULAR = "singular";
        public  final static String PLURAL = "plural";
                
        public  final static String type = "type"; 
        
        public  final static String PRONOUN_TAG = "Pronoun";
        
        public  final static String REF = "ref";
        public  final static String INTEREST = "interest";
        public  final static String ASSIMIL_SCORE = "AssimilationScore";
        
        public  final static String TYPE_OF ="typeOf";
        
        public final static String AGGREG_ALLOWED = "AGGREG_ALLOWED";
        
        public final static String LEVEL = "LEVEL";    
        public final static String RE_FOCUS = "RE_FOCUS";
        
        public final static String FOCUSLevel4 = "FOCUSLevel4";
        public final static String FOCUSLevel3 = "FOCUSLevel3";
        public final static String FOCUSLevel2 = "FOCUSLevel2";
        public final static String FOCUSLevel1 = "FOCUSLevel1";
        
        public final static String FOCUS_LOST = "FOCUS_LOST";
                
        //-----------------------------------------------------------------------------------
        public static String owlnlNS = "http://www.aueb.gr/users/ion/owlnl#";
        public static String prefix = "owlnl";
         
        public  final static String prpType= "prpType";
        public  final static String ObjectProperty= "ObjPrp";
        public  final static String DatatypeProperty= "DPrp";
        
        /** Comparisons **/
        public	final static String COMPARATOR_TAG = "Comparator";
        public	final static String PREVIOUS_TAG = "Previous";
        public	final static String ARTICLE_TAG = "Article";
        public	final static String UNIQUE_TAG = "Unique";
        public	final static String COMMON_TAG = "Common";
        public	final static String AYTOS_TAG = "Aytos";
        
	public XmlMsgs(String idName,int type, Document d)
        {								
            doc = d;										
            MsgsElement = doc.createElementNS(owlnlNS, prefix + ":" +"owlMsgs"); 

            if(type == CLS_TYPE){
                SetAttr(MsgsElement,owlnlNS, prefix, "CLS-Name",idName);
            }

            else if(type == INST_TYPE){
                SetAttr(MsgsElement,owlnlNS, prefix ,"INST-Name",idName);
            }					
            else{
                    logger.debug("ERROR: XmlMsgs(String idName,int type) type!=0 && type!=1");
            }	

            doc.appendChild(MsgsElement);

            setNamespace(owlnlNS, prefix);
            //setDefaultNamespace();
	}
	//-----------------------------------------------------------------------------------	
        public void setNamespace(String ns, String myprefix)
        {
            NodeList AllNodesList = doc.getElementsByTagName(prefix + ":" + "owlMsgs");
            //((Element)AllNodesList.item(0)).setAttributeNS( "http://www.w3.org/2000/xmlns/", "xmlns:" + prefix, ns);
            ((Element)AllNodesList.item(0)).setAttribute("xmlns:" + myprefix, ns);            
        }
        
        public void setDefaultNamespace(){
            NodeList AllNodesList = doc.getElementsByTagName(prefix + ":" + "owlMsgs");
            ((Element)AllNodesList.item(0)).setAttributeNS( "http://www.w3.org/2000/xmlns/", "xmlns" , owlnlNS);            
        }
        //-----------------------------------------------------------------------------------	
	public int getType(){
		NodeList AllNodesList = doc.getElementsByTagName(prefix + ":" + "owlMsgs");
                
                //logger.debug("size:" + AllNodesList.getLength());
		if(getAttribute(AllNodesList.item(0), XmlMsgs.prefix , "CLS-Name").compareTo("")!=0)
			return 1;
		else if(getAttribute(AllNodesList.item(0), XmlMsgs.prefix , "INST-Name").compareTo("")!=0)
			return 2;
                
		else return 0;
            
                //return 2;
		
	}
	//-----------------------------------------------------------------------------------	
	public String getOwner()
        {
            Node root = getRoot();

            if(getType()==1)
                return getAttribute(root, prefix ,"CLS-Name");
            else if(getType()==2)
                return getAttribute(root, prefix ,"INST-Name");
            else return "";		
	}

        public Node getRoot(){
            return doc.getElementsByTagName(XmlMsgs.prefix + ":" + "owlMsgs").item(0);            
        }
        
        public Vector getMsgs(){
            
            // prepei na valw if gia an eina istance h class
            Node root = getRoot();
            return XmlMsgs.ReturnChildNodes(root);
        }
        
        public void removeMsgs(Vector msgs){
            //remove msgs from xml tree
            Node root = getRoot();
                    
            for (int i = 0; i < msgs.size(); i++){				
                    root.removeChild((Node)msgs.get(i));
            }
        }
        
        // returns true if currNode name == pref + ":" + tag
        public static boolean compare(Node currNode,String pref,String tag){
                        
            String NodeName = currNode.getNodeName();
            String prefixedName = (pref.compareTo("") == 0) ? tag : pref + ":" + tag;
            
            //logger.debug("-" + NodeName + "," + prefixedName +"-");
            
            if(NodeName.compareTo(prefixedName)==0)
                return true;
            else 
                return false;
        }
        
	//return xml tree
	public Document getXMLTree(){
		return doc;
	}
	//-----------------------------------------------------------------------------------
	public void setXMLTree(Document d){
		doc = d; 
	}
	//-----------------------------------------------------------------------------------
	//create  a new Msg 
	public Element createNewMsg(){
		if(getType()==1){
			Element MsgElement = doc.createElementNS(owlnlNS, prefix + ":" + "owlMsg");
			MsgsElement.appendChild(MsgElement);
                        
                        //logger.debug("111111 !!!!!!!!!!!!!!!!!");
                        
			return MsgElement;
		}
		else if(getType()==2){
                        //logger.debug("222222 !!!!!!!!!!!!!!!!!");
                    
			return (Element)doc.getElementsByTagNameNS(owlnlNS, "owlMsgs").item(0);
		}

                logger.debug("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! !!!!!!!!!!!!!!!!!");
                return null;
                
	}
        
	public Element createNewMsg(Element parentMsg){
		if(getType()==1){
			Element MsgElement = doc.createElementNS(owlnlNS, prefix + ":" + "owlMsg");
			parentMsg.appendChild(MsgElement);
                        
                        //logger.debug("111111 !!!!!!!!!!!!!!!!!");
                        
			return MsgElement;
		}
		else if(getType()==2){
                        //logger.debug("222222 !!!!!!!!!!!!!!!!!");
                    
			return (Element)doc.getElementsByTagNameNS(owlnlNS, "owlMsgs").item(0);
		}

                logger.debug("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! !!!!!!!!!!!!!!!!!");
                return null;
                
	}
        
	//-----------------------------------------------------------------------------------
	//add a new element with name "tag" with root Elem
	public Element AddNewElement(Element Elem,String ns, String prefix, String tag){
            if(prefix.compareTo("") != 0)
            {
                Element NewElement = doc.createElementNS(ns, prefix + ":" + tag);

                //logger.debug("AddNewElement " +  Elem.getNodeName());
                Elem.appendChild(NewElement);
                return NewElement;
            }
            else
            {// not use namespace
                Element NewElement = doc.createElement(tag);

                //logger.debug("AddNewElement " +  Elem.getNodeName());
                Elem.appendChild(NewElement);
                return NewElement;                
            }
                
	}
	//-----------------------------------------------------------------------------------
	// add an attribute with name "Attr" and value "value" to element Elem
	public void SetAttr(Element Elem,String ns,String pref, String Attr,String value)
        {		
		Elem.setAttributeNS(ns ,pref + ":" + Attr, value);	
	}
        
	//-----------------------------------------------------------------------------------
	// set text content of an element
	public void setText(Element Elem,String text)
        {		
		Elem.setTextContent(text);
	}
	
	//-----------------------------------------------------------------------------------
        
        private String getPropertyURI(Node nd, String prop)
        {
            
            if(prop.compareTo("")==0 || prop == null)
            {
                return nd.getNamespaceURI() + nd.getLocalName();
            }
            else
            {
                return prop;
            }
        }
                
        public void AddPropertySlot(Node nd, String Case, int type, String  RE_TYPE){
            if(type == PropertySlot.OWNER)
            {
                Element owner = doc.createElementNS( this.owlnlNS,  prefix + ":" + OWNER_TAG);
                SetAttr(owner,this.owlnlNS, this.prefix,this.CASE_TAG,Case);                
                SetAttr(owner,this.owlnlNS, this.prefix,this.RETYPE,RE_TYPE);                
                                
                SetAttr(owner,this.owlnlNS, this.prefix,"forProperty", nd.getNamespaceURI() + nd.getLocalName());
                SetAttr(owner,this.owlnlNS, this.prefix, XmlMsgs.INTEREST, XmlMsgs.getAttribute(nd, XmlMsgs.prefix, XmlMsgs.INTEREST));
                SetAttr(owner,this.owlnlNS, this.prefix, XmlMsgs.ASSIMIL_SCORE, XmlMsgs.getAttribute(nd, XmlMsgs.prefix, XmlMsgs.ASSIMIL_SCORE));
                
                nd.appendChild(owner);
            }
            else if(type == PropertySlot.FILLER)
            {
                Element filler = doc.createElementNS( this.owlnlNS,  prefix + ":" + FILLER_TAG);
                
                SetAttr(filler,this.owlnlNS, this.prefix,this.RETYPE,RE_TYPE);
                
                if(XmlMsgs.getAttribute(nd,XmlMsgs.prefix, XmlMsgs.prpType).compareTo(XmlMsgs.DatatypeProperty)==0)
                {
                    //SetAttr(filler,this.owlnlNS, this.prefix,this.CASE_TAG,Case);
                }
                else
                {
                    SetAttr(filler,this.owlnlNS, this.prefix,this.CASE_TAG,Case);
                }
                                                                    
                if(compare(nd, this.prefix, XmlMsgs.CLASS_TAG))
                {
                    filler.setTextContent(this.getAttribute(nd, prefix , "Val"));
                }
                else if(compare(nd, this.prefix, XmlMsgs.COMPARATOR_TAG))
                {
                    if (this.getAttribute(nd,prefix,"Val").equalsIgnoreCase(""))
                        filler.setTextContent(this.getAttribute(nd, prefix , "entity"));
                    else
                        filler.setTextContent(this.getAttribute(nd, prefix , "Val"));
                }
                else
                {
                    filler.setTextContent(this.getAttribute(nd, prefix , "Val"));
                }

                SetAttr(filler,this.owlnlNS, this.prefix,"forProperty", nd.getNamespaceURI() + nd.getLocalName());
                SetAttr(filler,this.owlnlNS, this.prefix, XmlMsgs.INTEREST, XmlMsgs.getAttribute(nd, XmlMsgs.prefix, XmlMsgs.INTEREST));
                SetAttr(filler,this.owlnlNS, this.prefix, XmlMsgs.ASSIMIL_SCORE, XmlMsgs.getAttribute(nd, XmlMsgs.prefix, XmlMsgs.ASSIMIL_SCORE));
                
                nd.appendChild(filler);
            }
            else
            {
                logger.debug("ERROR: ADDING PROPERTY SLOT");
            }
        }
        
        
        
        public void AddPropertySlotComp(Node nd, String propURI, String Case, int type, String  RE_TYPE){
            if(type == PropertySlot.OWNER)
            {
                Element owner = doc.createElementNS( this.owlnlNS,  prefix + ":" + OWNER_TAG);
                SetAttr(owner,this.owlnlNS, this.prefix,this.CASE_TAG,Case);                
                SetAttr(owner,this.owlnlNS, this.prefix,this.RETYPE,RE_TYPE);                
                                
                SetAttr(owner,this.owlnlNS, this.prefix,"forProperty", propURI);
                SetAttr(owner,this.owlnlNS, this.prefix, XmlMsgs.INTEREST, XmlMsgs.getAttribute(nd, XmlMsgs.prefix, XmlMsgs.INTEREST));
                SetAttr(owner,this.owlnlNS, this.prefix, XmlMsgs.ASSIMIL_SCORE, XmlMsgs.getAttribute(nd, XmlMsgs.prefix, XmlMsgs.ASSIMIL_SCORE));
                
                nd.appendChild(owner);
            }
            else if(type == PropertySlot.FILLER)
            {
                Element filler = doc.createElementNS( this.owlnlNS,  prefix + ":" + FILLER_TAG);
                
                SetAttr(filler,this.owlnlNS, this.prefix,this.RETYPE,RE_TYPE);
                
                if(XmlMsgs.getAttribute(nd,XmlMsgs.prefix, XmlMsgs.prpType).compareTo(XmlMsgs.DatatypeProperty)==0)
                {
                    //SetAttr(filler,this.owlnlNS, this.prefix,this.CASE_TAG,Case);
                }
                else
                {
                    SetAttr(filler,this.owlnlNS, this.prefix,this.CASE_TAG,Case);
                }
                                                                    
                if(compare(nd, this.prefix, XmlMsgs.CLASS_TAG))
                {
                    filler.setTextContent(this.getAttribute(nd, prefix , "Val"));
                }
                else if(compare(nd, this.prefix, XmlMsgs.COMPARATOR_TAG))
                {
                    if (this.getAttribute(nd,prefix,"Val").equalsIgnoreCase(""))
                        filler.setTextContent(this.getAttribute(nd, prefix , "entity"));
                    else
                        filler.setTextContent(this.getAttribute(nd, prefix , "Val"));
                }
                else
                {
                    filler.setTextContent(this.getAttribute(nd, prefix , "Val"));
                }

                SetAttr(filler,this.owlnlNS, this.prefix,"forProperty", propURI);
                SetAttr(filler,this.owlnlNS, this.prefix, XmlMsgs.INTEREST, XmlMsgs.getAttribute(nd, XmlMsgs.prefix, XmlMsgs.INTEREST));
                SetAttr(filler,this.owlnlNS, this.prefix, XmlMsgs.ASSIMIL_SCORE, XmlMsgs.getAttribute(nd, XmlMsgs.prefix, XmlMsgs.ASSIMIL_SCORE));
                
                nd.appendChild(filler);
            }
            else
            {
                logger.debug("ERROR: ADDING PROPERTY SLOT");
            }
        }
        
        
        
        public void AddPropertySlot(Node nd, String propURI, String Case, int type, boolean plural, boolean unique){
            if(type == PropertySlot.OWNER)
            {
                Element owner = doc.createElementNS( this.owlnlNS,  prefix + ":" + OWNER_TAG);
                SetAttr(owner,this.owlnlNS, this.prefix,this.CASE_TAG,Case);                
                if (plural)
                {
                    SetAttr(owner,this.owlnlNS, this.prefix,"form",this.PLURAL);
                } 
                
                SetAttr(owner,this.owlnlNS, this.prefix,"forProperty", propURI);
                SetAttr(owner,this.owlnlNS, this.prefix, XmlMsgs.INTEREST, XmlMsgs.getAttribute(nd, XmlMsgs.prefix, XmlMsgs.INTEREST));
                SetAttr(owner,this.owlnlNS, this.prefix, XmlMsgs.ASSIMIL_SCORE, XmlMsgs.getAttribute(nd, XmlMsgs.prefix, XmlMsgs.ASSIMIL_SCORE));
                
                nd.appendChild(owner);
            }
            else if(type == PropertySlot.FILLER){
                Element filler = doc.createElementNS( this.owlnlNS,  prefix + ":" + FILLER_TAG);
                SetAttr(filler,this.owlnlNS, this.prefix,this.CASE_TAG,Case);
                if (plural)
                {
                    SetAttr(filler,this.owlnlNS, this.prefix,"form",this.PLURAL);
                } 
                   
                    
                    if(compare(nd, this.prefix, XmlMsgs.CLASS_TAG)){
                        filler.setTextContent(this.getAttribute(nd, prefix , "Cls-URI"));
                    }
                    else if(this.getAttribute(nd, prefix , "Val").equalsIgnoreCase(""))
                    {
                        filler.setTextContent(this.getAttribute(nd.getParentNode(), prefix , "Comparator"));
                    }
                    else if (unique)
                    {
                        if(!this.getAttribute(nd, prefix , "Unique").equalsIgnoreCase(""))
                            filler.setTextContent(this.getAttribute(nd, prefix , "Unique"));
                        else
                            filler.setTextContent(this.getAttribute(nd, prefix , "Common"));
                    }
                    else
                    {
                        filler.setTextContent(this.getAttribute(nd, prefix , "Val"));
                    }
                    
                    SetAttr(filler,this.owlnlNS, this.prefix,"forProperty", propURI);
                    SetAttr(filler,this.owlnlNS, this.prefix, XmlMsgs.INTEREST, XmlMsgs.getAttribute(nd, XmlMsgs.prefix, XmlMsgs.INTEREST));
                    SetAttr(filler,this.owlnlNS, this.prefix, XmlMsgs.ASSIMIL_SCORE, XmlMsgs.getAttribute(nd, XmlMsgs.prefix, XmlMsgs.ASSIMIL_SCORE));
                
                nd.appendChild(filler);
            }
            else{
                logger.debug("ERROR: ADDING PROPERTY SLOT");
            }
        }
        
        
        //kalytera na allaze oloklhrh ayth h synarthsh kai na epairne ws orisma ta periexomena oty filler... 
        // (h na eftiaxna perissotera types)
        public void AddPropertySlot(Node nd, String propURI, String Case, int type, boolean plural, boolean unique, boolean kind){
            if(type == PropertySlot.OWNER){
                Element owner = doc.createElementNS( this.owlnlNS,  prefix + ":" + OWNER_TAG);
                SetAttr(owner,this.owlnlNS, this.prefix,this.CASE_TAG,Case);
                if (plural)
                {
                    SetAttr(owner,this.owlnlNS, this.prefix,"form",this.PLURAL);
                } 
                
                SetAttr(owner,this.owlnlNS, this.prefix,"forProperty", propURI);
                SetAttr(owner,this.owlnlNS, this.prefix, XmlMsgs.INTEREST, XmlMsgs.getAttribute(nd, XmlMsgs.prefix, XmlMsgs.INTEREST));
                SetAttr(owner,this.owlnlNS, this.prefix, XmlMsgs.ASSIMIL_SCORE, XmlMsgs.getAttribute(nd, XmlMsgs.prefix, XmlMsgs.ASSIMIL_SCORE));
                
                nd.appendChild(owner);
            }
            else if(type == PropertySlot.FILLER){
                Element filler = doc.createElementNS( this.owlnlNS,  prefix + ":" + FILLER_TAG);
                SetAttr(filler,this.owlnlNS, this.prefix,this.CASE_TAG,Case);
                if (plural)
                {
                    SetAttr(filler,this.owlnlNS, this.prefix,"form",this.PLURAL);
                } 
                   
                    
                    if(compare(nd, this.prefix, XmlMsgs.CLASS_TAG)){
                        filler.setTextContent(this.getAttribute(nd, prefix , "Cls-URI"));
                    }
                    else if (kind)
                    {
                        filler.setTextContent(this.getAttribute(nd,prefix, "kind"));
                    }
                    else if(this.getAttribute(nd, prefix , "Val").equalsIgnoreCase(""))
                    {
                        filler.setTextContent(this.getAttribute(nd.getParentNode(), prefix , "Comparator"));
                    }
                    else if (unique)
                    {
                        if(!this.getAttribute(nd, prefix , "Unique").equalsIgnoreCase(""))
                            filler.setTextContent(this.getAttribute(nd, prefix , "Unique"));
                        else
                            filler.setTextContent(this.getAttribute(nd, prefix , "Common"));
                    }
                    else
                    {
                        filler.setTextContent(this.getAttribute(nd, prefix , "Val"));
                    }
                    
                    SetAttr(filler,this.owlnlNS, this.prefix,"forProperty", propURI);
                    SetAttr(filler,this.owlnlNS, this.prefix, XmlMsgs.INTEREST, XmlMsgs.getAttribute(nd, XmlMsgs.prefix, XmlMsgs.INTEREST));
                    SetAttr(filler,this.owlnlNS, this.prefix, XmlMsgs.ASSIMIL_SCORE, XmlMsgs.getAttribute(nd, XmlMsgs.prefix, XmlMsgs.ASSIMIL_SCORE));
                
                    nd.appendChild(filler);
            }
            else{
                logger.debug("ERROR: ADDING PROPERTY SLOT");
            }
        }
        
        // add slot functions
        public void AddStringSlot(Node nd, String text)
        {
            Element textNode = doc.createElementNS( this.owlnlNS,  prefix + ":" + TEXT_TAG);
            textNode.setTextContent(text);
            
            SetAttr(textNode,this.owlnlNS, this.prefix,"forProperty", nd.getNamespaceURI() + nd.getLocalName());
            SetAttr(textNode,this.owlnlNS, this.prefix, XmlMsgs.INTEREST, XmlMsgs.getAttribute(nd, XmlMsgs.prefix, XmlMsgs.INTEREST));
            SetAttr(textNode,this.owlnlNS, this.prefix, XmlMsgs.ASSIMIL_SCORE, XmlMsgs.getAttribute(nd, XmlMsgs.prefix, XmlMsgs.ASSIMIL_SCORE));
            
            nd.appendChild(textNode);
            
            
        }
        
        // add slot functions
        public void AddStringSlotComp(Node nd, String propURI, String text)
        {
            Element textNode = doc.createElementNS( this.owlnlNS,  prefix + ":" + TEXT_TAG);
            textNode.setTextContent(text);
            
            SetAttr(textNode,this.owlnlNS, this.prefix,"forProperty", propURI);
            SetAttr(textNode,this.owlnlNS, this.prefix, XmlMsgs.INTEREST, XmlMsgs.getAttribute(nd, XmlMsgs.prefix, XmlMsgs.INTEREST));
            SetAttr(textNode,this.owlnlNS, this.prefix, XmlMsgs.ASSIMIL_SCORE, XmlMsgs.getAttribute(nd, XmlMsgs.prefix, XmlMsgs.ASSIMIL_SCORE));
            
            nd.appendChild(textNode);
            
            
        }
        
        // add slot functions
        public void AddStringSlotComp(Node nd, String propURI, String role, String text)
        {
            Element textNode = doc.createElementNS( this.owlnlNS,  prefix + ":" + TEXT_TAG);
            textNode.setTextContent(text);
            
            SetAttr(textNode,this.owlnlNS, this.prefix,"forProperty", propURI);
            SetAttr(textNode,this.owlnlNS, this.prefix, XmlMsgs.INTEREST, XmlMsgs.getAttribute(nd, XmlMsgs.prefix, XmlMsgs.INTEREST));
            SetAttr(textNode,this.owlnlNS, this.prefix, XmlMsgs.ASSIMIL_SCORE, XmlMsgs.getAttribute(nd, XmlMsgs.prefix, XmlMsgs.ASSIMIL_SCORE));
            
            SetAttr(textNode,this.owlnlNS, this.prefix, "role", role);
            SetAttr(textNode,this.owlnlNS, this.prefix, XmlMsgs.REF, propURI);
            
            nd.appendChild(textNode);
            
            
        }
        
        // add slot functions
        public void AddStringSlot(Node nd, String text,boolean isPrep)
        {
            Element textNode = doc.createElementNS( this.owlnlNS,  prefix + ":" + TEXT_TAG);
            textNode.setTextContent(text);
            
            SetAttr(textNode,this.owlnlNS, this.prefix,"forProperty", nd.getNamespaceURI() + nd.getLocalName());
            SetAttr(textNode,this.owlnlNS, this.prefix, XmlMsgs.INTEREST, XmlMsgs.getAttribute(nd, XmlMsgs.prefix, XmlMsgs.INTEREST));
            SetAttr(textNode,this.owlnlNS, this.prefix, XmlMsgs.ASSIMIL_SCORE, XmlMsgs.getAttribute(nd, XmlMsgs.prefix, XmlMsgs.ASSIMIL_SCORE));
            
            if(isPrep)
                SetAttr(textNode,this.owlnlNS, this.prefix, "Prep", isPrep +"");
            
            nd.appendChild(textNode);
            
            
        }
        
        // add slot functions
        public void AddStringSlotComp(Node nd, String propURI, String text,boolean isPrep)
        {
            Element textNode = doc.createElementNS( this.owlnlNS,  prefix + ":" + TEXT_TAG);
            textNode.setTextContent(text);
            
            SetAttr(textNode,this.owlnlNS, this.prefix,"forProperty", propURI);
            SetAttr(textNode,this.owlnlNS, this.prefix, XmlMsgs.INTEREST, XmlMsgs.getAttribute(nd, XmlMsgs.prefix, XmlMsgs.INTEREST));
            SetAttr(textNode,this.owlnlNS, this.prefix, XmlMsgs.ASSIMIL_SCORE, XmlMsgs.getAttribute(nd, XmlMsgs.prefix, XmlMsgs.ASSIMIL_SCORE));
            
            if(isPrep)
                SetAttr(textNode,this.owlnlNS, this.prefix, "Prep", isPrep +"");
            
            nd.appendChild(textNode);
            
            
        }
        
        public void AddVerbSlot(Node nd, String verb, String pverb, String voice, String tense)
        {
            Element verbNode = doc.createElementNS( this.owlnlNS,  prefix + ":" + VERB_TAG);            
            
            SetAttr(verbNode,this.owlnlNS, this.prefix,this.singular_VERB_TAG, verb); 
            SetAttr(verbNode,this.owlnlNS, this.prefix,this.plural_VERB_TAG, pverb); 
            SetAttr(verbNode,this.owlnlNS, this.prefix,this.VOICE_TAG,voice); 
            SetAttr(verbNode,this.owlnlNS, this.prefix,this.TENSE_TAG,tense); 
                                
            SetAttr(verbNode,this.owlnlNS, this.prefix,"forProperty", nd.getNamespaceURI() + nd.getLocalName());
            SetAttr(verbNode,this.owlnlNS, this.prefix, XmlMsgs.INTEREST, XmlMsgs.getAttribute(nd, XmlMsgs.prefix, XmlMsgs.INTEREST));
            SetAttr(verbNode,this.owlnlNS, this.prefix, XmlMsgs.ASSIMIL_SCORE, XmlMsgs.getAttribute(nd, XmlMsgs.prefix, XmlMsgs.ASSIMIL_SCORE));
            
            nd.appendChild(verbNode);            
        }

        public void AddVerbSlotComp(Node nd, String propURI, String verb, String pverb, String voice, String tense)
        {
            Element verbNode = doc.createElementNS( this.owlnlNS,  prefix + ":" + VERB_TAG);            
            
            SetAttr(verbNode,this.owlnlNS, this.prefix,this.singular_VERB_TAG, verb); 
            SetAttr(verbNode,this.owlnlNS, this.prefix,this.plural_VERB_TAG, pverb); 
            SetAttr(verbNode,this.owlnlNS, this.prefix,this.VOICE_TAG,voice); 
            SetAttr(verbNode,this.owlnlNS, this.prefix,this.TENSE_TAG,tense); 
                                
            SetAttr(verbNode,this.owlnlNS, this.prefix,"forProperty", propURI);
            SetAttr(verbNode,this.owlnlNS, this.prefix, XmlMsgs.INTEREST, XmlMsgs.getAttribute(nd, XmlMsgs.prefix, XmlMsgs.INTEREST));
            SetAttr(verbNode,this.owlnlNS, this.prefix, XmlMsgs.ASSIMIL_SCORE, XmlMsgs.getAttribute(nd, XmlMsgs.prefix, XmlMsgs.ASSIMIL_SCORE));
            
            nd.appendChild(verbNode);            
        }
        
        public void Add_IS_A_Slot(Node nd)
        {
            Element verbNode = doc.createElementNS( this.owlnlNS,  prefix + ":" + IS_A_TAG);                                                        
            
            SetAttr(verbNode,this.owlnlNS, this.prefix,"forProperty", nd.getNamespaceURI() + nd.getLocalName());
            
            SetAttr(verbNode,this.owlnlNS, this.prefix, XmlMsgs.INTEREST, XmlMsgs.getAttribute(nd, XmlMsgs.prefix, XmlMsgs.INTEREST));
            SetAttr(verbNode,this.owlnlNS, this.prefix, XmlMsgs.ASSIMIL_SCORE, XmlMsgs.getAttribute(nd, XmlMsgs.prefix, XmlMsgs.ASSIMIL_SCORE));
            
            nd.appendChild(verbNode);            
        }
           
        public void Add_IS_A_SlotComp(Node nd, String propURI)
        {
            Element verbNode = doc.createElementNS( this.owlnlNS,  prefix + ":" + IS_A_TAG);                                                        
            
            SetAttr(verbNode,this.owlnlNS, this.prefix,"forProperty", propURI);
            
            SetAttr(verbNode,this.owlnlNS, this.prefix, XmlMsgs.INTEREST, XmlMsgs.getAttribute(nd, XmlMsgs.prefix, XmlMsgs.INTEREST));
            SetAttr(verbNode,this.owlnlNS, this.prefix, XmlMsgs.ASSIMIL_SCORE, XmlMsgs.getAttribute(nd, XmlMsgs.prefix, XmlMsgs.ASSIMIL_SCORE));
            
            nd.appendChild(verbNode);            
        }
        
        /* comparisons*/
        
        public void AddPreviousSlot(Node nd)
        {
            Element textNode = doc.createElementNS( this.owlnlNS,  prefix + ":" + PREVIOUS_TAG);
            if(!this.getAttribute(nd,prefix,"same").equalsIgnoreCase(""))
                textNode.setTextContent("http://www.aueb.gr/users/ion/mpiro.owl#other");
            else
                textNode.setTextContent("http://www.aueb.gr/users/ion/mpiro.owl#previous");
            nd.appendChild(textNode);
        }
        
        public void AddMostSlot(Node nd)
        {
            Element textNode = doc.createElementNS( this.owlnlNS,  prefix + ":" + PREVIOUS_TAG);
            textNode.setTextContent("http://www.aueb.gr/users/ion/mpiro.owl#most");
            nd.appendChild(textNode);
        }
        
        public void AddUniqueSlot(Node nd)
        {
            Element textNode = doc.createElementNS( this.owlnlNS,  prefix + ":" + UNIQUE_TAG);
            textNode.setTextContent("http://www.aueb.gr/users/ion/mpiro.owl#unique");
            nd.appendChild(textNode);
        }
        
        public void AddCommonSlot(Node nd)
        {
            Element textNode = doc.createElementNS( this.owlnlNS,  prefix + ":" + COMMON_TAG);
            textNode.setTextContent("http://www.aueb.gr/users/ion/mpiro.owl#common");
            nd.appendChild(textNode);
        }
        
        public void AddArticleSlot(Node nd)
        {
            Element textNode = doc.createElementNS( this.owlnlNS,  prefix + ":" + ARTICLE_TAG);
            textNode.setTextContent("http://www.aueb.gr/users/ion/mpiro.owl#article");
            nd.appendChild(textNode);
        }
        /* comparisons*/
        
        
        
        
        //-----------------------------------------------------------------------------------
        // return a string representation of the xml document 
        public String getStringDescription(boolean indent)
        {
            try
            {
                OutputFormat OutFrmt = null;
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                OutFrmt = new OutputFormat(doc);

                //OutFrmt.setLineWidth(1000); 
                OutFrmt.setIndenting(indent);  
                //OutFrmt.setEncoding("ISO-8859-7");             
                OutFrmt.setEncoding("UTF-8");             
                XMLSerializer xmlsrz = new XMLSerializer(os,OutFrmt);            
                xmlsrz.serialize(doc);            
                return new String(os.toByteArray(), Charset.forName("UTF-8") ); 
            }//try
            catch(UnsupportedCharsetException e)
            {
                e.printStackTrace();
                return "ERRoR: UnsupportedCharsetException";                
            }
            catch(Exception e)
            {
                e.printStackTrace();
                return "ERRoR";
            }//catch       

        }//getStringDescription
    
	//sort by order
	public void sortByOrder()
        {
		Node root = getRoot();	
				
		Vector Msgs = getMsgs();
		
		Object T [] = Msgs.toArray();
		Arrays.sort(T,new OrderComparatorImpl(true)); // sort  Nodes by the order Attribute
		Vector sorted_Msgs = new Vector();
                
		for(int i = 0; i < T.length; i++)
                {
			Node curr = (Node)T[i];
			//logger.debug("sort" + curr.getNodeName() + "  ,  " +curr.getAttributes().getNamedItem("Order").getTextContent());
			root.appendChild(curr); // to append child prwta afairei enan komvo 
                                                // pou hdh yparxei sto dentro
                        
                        sortMsgs2(curr);
		}
                
                GroupSameProperties();
	}		
	//-----------------------------------------------------------------------------------
	private void sortMsgs(Node n)
        {
            logger.debug("sort---" + n.getNodeName());

            Vector MsgNodes = ReturnChildNodes(n);

            if(MsgNodes.size()>1)
            {// if # Msg childs > 1 sort them  and recursively call sortMsgs 			

                    Object T [] = MsgNodes.toArray();
                    Arrays.sort(T,new OrderComparatorImpl(true));

                    for(int i = 0; i < T.length; i++)
                    {
                            Node curr = (Node)T[i];
                            //logger.debug("sort " + curr.getNodeName() + "  ,  " +curr.getAttributes().getNamedItem("Order").getTextContent());
                    }

                    for(int i = 0; i < T.length; i++)
                    { 
                            Node curr = (Node)T[i];
                            n.appendChild(curr);
                            sortMsgs(curr);
                    }	
            }//if
                        
	}//sortMsgs
        //-----------------------------------------------------------------------------------
	private void sortMsgs2(Node n)
        {
            logger.debug("sort---" + n.getNodeName());

            Vector MsgNodes = ReturnChildNodes(n); // get childs of n

            if(MsgNodes.size() >= 1) // if has childs
            {// if # Msg childs > 1 sort them and  recursively call sortMsgs 			

                    int j = 0;
                    while( j < MsgNodes.size())
                    {
                        Node curr = (Node)MsgNodes.get(j);
                        if(
                          curr.getNodeName().equals(XmlMsgs.prefix + ":" + XmlMsgs.OWNER_TAG)
                        ||curr.getNodeName().equals(XmlMsgs.prefix + ":" + XmlMsgs.FILLER_TAG)
                        ||curr.getNodeName().equals(XmlMsgs.prefix + ":" + XmlMsgs.VERB_TAG)
                        ||curr.getNodeName().equals(XmlMsgs.prefix + ":" + XmlMsgs.TEXT_TAG)
                         ||curr.getNodeName().equals(XmlMsgs.prefix + ":" + XmlMsgs.IS_A_TAG))
                        {
                            MsgNodes.remove(curr);                           
                        }
                        else
                        {
                          j++;
                        }
                    }
                    
                    
                    Object T [] = MsgNodes.toArray();
                    Arrays.sort(T,new OrderComparatorImpl(true)); // sort them by order

                    for(int i = 0; i < T.length; i++)
                    { 
                        Node curr = (Node)T[i];
                        n.getParentNode().appendChild(curr);
                        sortMsgs2(curr);
                    }	
            }//if
	}//sortMsgs        
               
        private void GroupSameProperties()
        {
            HashMap<String, Node> SubjectPredicateSet =  new HashMap<String, Node>();
            
            Node root = getRoot();            
            Vector Msgs = getMsgs();
            
            for(int i = 0; i < Msgs.size(); i++)
            {
                Node currNode = (Node) Msgs.get(i);
                
                String REF = XmlMsgs.getAttribute(currNode, XmlMsgs.prefix, XmlMsgs.REF);
                String Predicate = currNode.getNodeName();
                
                String key = REF + "," + Predicate;
                
                if(SubjectPredicateSet.containsKey(key))
                {
                    Node refChild = SubjectPredicateSet.get(key).getNextSibling();
                    if(refChild!=null)
                    {                        
                        Node ParentNode = refChild.getParentNode();
                        ParentNode.insertBefore(currNode, refChild);
                    }
                    else
                    {
                         Node ParentNode = currNode.getParentNode();
                         ParentNode.appendChild(currNode);
                    }
                }
                else
                {
                    SubjectPredicateSet.put(key, currNode);
                }
            }
        }
        

	//-----------------------------------------------------------------------------------		
	public static String getAttribute(Node node, String prefix, String AttributeName)
        {            
            String prefixedAttrName = "";

            if(prefix.compareTo("")==0){
                prefixedAttrName = AttributeName;
            }
            else{
                prefixedAttrName = prefix + ":" + AttributeName;
            }
            
            String ret = "";

            NamedNodeMap NMM = node.getAttributes();

            if(NMM != null)
            {
                if(NMM.getNamedItem(prefixedAttrName)!=null){
                        ret = NMM.getNamedItem( prefixedAttrName ).getTextContent();	
                        return ret;
                }
                else return ret; 
            }
            else
            return ret;		
	}
	//-----------------------------------------------------------------------------------
	public static String getChild(Node node, String prefix ,String ChildName){
            
                String prefixedChildName = "";
                        
                if(prefix == null){
                    prefixedChildName = ChildName;
                }
                else
                    prefixedChildName = prefix + ":" + ChildName;
                
                
		Node n = null;
		NodeList list_of_templ_childs= node.getChildNodes();
                
		for(int i = 0; i < list_of_templ_childs.getLength(); i++){
			n = list_of_templ_childs.item(i);
                        
			if(n.getNodeName().compareTo(prefixedChildName)==0){
                            
				//logger.debug("Return: " + n.getTextContent());
				return n.getTextContent();
			}
		}
		return "";
	}	
	//-----------------------------------------------------------------------------------
        
	public static Node getNodeChild(Node node,  String prefix , String ChildName){
            
                String prefixedChildName = "";
                        
                if(prefix == null){
                    prefixedChildName = ChildName;
                }
                else
                    prefixedChildName = prefix + ":" + ChildName;
                
		Node n = null;
		NodeList list_of_templ_childs= node.getChildNodes();
		for(int i = 0; i < list_of_templ_childs.getLength(); i++){
			n = list_of_templ_childs.item(i);
			if(n.getNodeName().compareTo(prefixedChildName)==0){
				//logger.debug("Return: " + n.getTextContent());
				return n;
			}
		}
		return null;
	}	
        
        /* */
        public boolean findInNodeMap(NamedNodeMap nnm, String name)
        {
            for (int i=0; i<nnm.getLength();i++)
            {
                if (nnm.item(i).getLocalName().equalsIgnoreCase(name))
                    return true;
            }
            return false;
        }
        
	//-----------------------------------------------------------------------------------
	public Vector ReturnMatchedNodes(String pref, String tag){
            
            NodeList Match_List = null;

            if (pref.compareTo("")==0) 
                Match_List = doc.getElementsByTagName(tag);
            else
                
		Match_List = doc.getElementsByTagName(prefix + ":" + tag);
                                         
            Vector Match_List_vec = new Vector();
            for(int i = 0; i < Match_List.getLength(); i++)
	    {        
		Match_List_vec.add(Match_List.item(i));
            }

            return Match_List_vec;
	}//ReturnMatchedNodes
	//-----------------------------------------------------------------------------------
        
        // returns a vector which contains the childs
        // of current
	public static Vector ReturnChildNodes(Node current){
		NodeList Child_List = current.getChildNodes();
		Vector Child_List_vec = new Vector();
		
		for(int i = 0; i < Child_List.getLength(); i++){
			
		    Child_List_vec.add(Child_List.item(i));
		}		
		
		return Child_List_vec;
	}
	//-----------------------------------------------------------------------------------	
        /*
	public static String get_Child_OR_Attr(Node node, String str){
		String ret = "";
		ret = getAttribute(node, str);
		
		if (ret.compareTo("")==0)
		ret = getChild(node, str);
				
		return ret;
		
	}
         */	
        
        //-----------------------------------------------------------------------------------	
        public static Node getFirstNonTextChild(Node parent){
            NodeList childs = parent.getChildNodes();
            for(int i = 0; i < childs.getLength(); i++)
            if (childs.item(i).getNodeType() != Node.TEXT_NODE){
                return childs.item(i);
            } 
            
            return null;
        }
       
        
       public void removeNode(Node node){
           node.getParentNode().removeChild(node);
       }
                
       public void replaceWithPronoun(Node node, String Case)
       {
		Element elmnt = doc.createElementNS(this.owlnlNS, this.prefix + ":" + this.PRONOUN_TAG);
                elmnt.setAttributeNS(this.owlnlNS, this.prefix + ":" + this.CASE_TAG, Case);
                        
		Node parent = node.getParentNode();                
		parent.replaceChild((Node)elmnt,node);
       }
       
       // comparisons
       public void addSeenBefore(Element node, boolean plural)
       {
           double pos = Math.random();
           if (plural)
           {
               if (pos<0.25)
               {
                   AddStringSlot(node,"που είδατε");
               }
               else
               if (pos<0.5)
               {
                   AddStringSlot(node,"που εξετάσατε νωρίτερα");
               }
               else
               if (pos<0.75)
               {
                   AddStringSlot(node,"που εξετάσατε");
               }
               else
               //if (pos<0.3)
               {
                   AddStringSlot(node,"που είδατε προηγουμένως");
               }
           }
           else
           {
               if (pos<0.5)
               {
                   AddStringSlot(node,"που είδατε");
               }
               else
               {
                   AddStringSlot(node,"που εξετάσατε");
               }
           }
       }
             
       public void AddLocationSlot(Node nd, String loc, boolean plural)
        {
           if (!loc.equalsIgnoreCase(""))
           {
                Element textNode = doc.createElementNS( this.owlnlNS,  prefix + ":" + "LOCATION");
                textNode.setTextContent("http://www.aueb.gr/users/ion/mpiro.owl#"+loc);
                nd.appendChild(textNode);
           }
        }
               
       // comparisons
       //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
       

       
       public void AddComparatorSlot(Node nd, String entity, Lexicalisation lex, MicroplansAndOrderingQueryManager MAOQM, String language)
       {
	  // This method adds the comparator slot to the xml tree
          // The comparator slot cosists of
          // slots taken from the microplans.
          // These slots some times are changed, for example
          // we change the number attribute (singualar to plural) for some of them.
          // Also, the function adds and some texts slots 
          // like "which" that is referring expression 
          // These slots must be marked with the 
          // appropariate attributes like "owlnl:ref"
          // and "owlnl:role" in order to produce 
          // the semantic and sysntactic annotation correctly.
          // Also, when we use use slots coming from the microplans
          // by using a applyMicroplan method we must pass to the 
          // method the correct PropertyURI for which
          // the slots are used. 
	   
	   Element msgElem = createNewMsg();
	   Element tmp = AddNewElement(msgElem, XmlMsgs.owlnlNS, prefix, COMPARATOR_TAG);
	   this.SetAttr(tmp, this.owlnlNS, this.prefix, "entity", this.getAttribute(nd, prefix, "Comparator"));
	   if (this.getAttribute(nd, prefix, "previous").equalsIgnoreCase("true"))
	       this.SetAttr(tmp, this.owlnlNS, this.prefix, "previous", "true");
	   if (this.getAttribute(nd, prefix, "most").equalsIgnoreCase("true"))
	       this.SetAttr(tmp, this.owlnlNS, this.prefix, "most", "true");
	   
	   if (this.getAttribute(nd, prefix, "form").equalsIgnoreCase("plural"))
	       this.SetAttr(tmp, this.owlnlNS, this.prefix, "form", "plural");
	   if (!this.getAttribute(nd, prefix, "kind").equalsIgnoreCase(""))
	       this.SetAttr(tmp, this.owlnlNS, this.prefix, "kind", this.getAttribute(nd, prefix, "kind"));
	   if (!this.getAttribute(nd, prefix, "multyComp").equalsIgnoreCase(""))
	       this.SetAttr(tmp, this.owlnlNS, this.prefix, "multyComp", this.getAttribute(nd, prefix, "multyComp"));
	   if (!this.getAttribute(nd, prefix, "same").equalsIgnoreCase(""))
	       this.SetAttr(tmp, this.owlnlNS, this.prefix, "same", "true");
	   if (this.getAttribute(nd, prefix, "akoma").equalsIgnoreCase("true"))
	       this.SetAttr(tmp, this.owlnlNS, this.prefix, "akoma", "true");
	   
	   if (!this.getAttribute(nd, prefix, "location").equalsIgnoreCase(""))
	       this.SetAttr(tmp, this.owlnlNS, this.prefix, "location", this.getAttribute(nd, prefix, "location"));
	   
	   
	   //ean einai sygkrish diaforas
	   if (this.getAttribute(nd, prefix, "Feature").compareTo("") != 0)
	   {
	       if (language.equalsIgnoreCase("en"))
	       {
		   boolean comm = false;
		   this.SetAttr(tmp, this.owlnlNS, this.prefix, "Val", this.getAttribute(nd, prefix, "Feature"));
		   //tmp.setAttribute("Val",this.getAttribute(nd, prefix , "Val"));
		   nd.appendChild(tmp);
		   
		   String microName = XmlMsgs.getAttribute(nd, XmlMsgs.prefix, XmlMsgs.TEMPLATE_TAG);
		   
		   boolean doubleLine = false;
		   boolean doubleLineUnlike = false;
		   if (microName.compareTo("") != 0)
		   {
		       
		       int select = (int)(Math.random() * 1000);
		       
		       if (select < 333)
		       {
			   if (this.getAttribute(nd, prefix, "previous").equalsIgnoreCase("true"))
			   {
			       if (!this.getAttribute(nd, prefix, "same").equalsIgnoreCase(""))
			       {
				   if (this.getAttribute(nd, prefix, "most").equalsIgnoreCase("true"))
				   {
				       AddStringSlot(tmp, "while most of the other");
				   }
				   else
				   {
				       if (this.getAttribute(nd, prefix, "form").equalsIgnoreCase("plural"))
					   AddStringSlot(tmp, "while");
				       else
					   AddStringSlot(tmp, "while");
				       AddPreviousSlot(tmp);
				   }
			       }
			       else
			       {
				   
				   if (this.getAttribute(nd, prefix, "most").equalsIgnoreCase("true"))
				   {
				       AddStringSlot(tmp, "while most of the previous");
				   }
				   else
				   {
				       if (this.getAttribute(nd, prefix, "form").equalsIgnoreCase("plural"))
					   AddStringSlot(tmp, "while");
				       else
					   AddStringSlot(tmp, "while");
				       AddPreviousSlot(tmp);
				   }
			       }
			       if (this.getAttribute(nd, prefix, "MostCommon").equalsIgnoreCase("true"))
				   AddStringSlot(tmp, "of the collection");
			       //else
			       //    AddStringSlot(tmp,"that you have seen earlier");
			       //doubleLine = true;
			   }
			   else
			   {
			       if (this.getAttribute(nd, prefix, "most").equalsIgnoreCase("true"))
			       {
				   AddStringSlot(tmp, "while most of the");
			       }
			       else
				   if (this.getAttribute(nd, prefix, "form").equalsIgnoreCase("plural"))
				       AddStringSlot(tmp, "while all the");
				   else
				       AddStringSlot(tmp, "while the");
			       if (this.getAttribute(nd, prefix, "MostCommon").equalsIgnoreCase("true"))
				   AddStringSlot(tmp, "of the collection");
			       else
				   if (this.getAttribute(nd, prefix, "kind").equalsIgnoreCase(""))
				       AddStringSlot(tmp, "that you saw earlier");
								/*else
				     if(this.getAttribute(nd, prefix , "kind").equalsIgnoreCase(""))
					AddStringSlot(tmp,"that you saw earlier");
				    else
				    {
					Element COMMA = doc.createElement("COMMA");
									tmp.appendChild(COMMA);
									AddStringSlot(tmp,"a kind of");
									AddPropertySlot(tmp,"nominative",PropertySlot.FILLER,false,false, true);
									AddStringSlot(tmp,"that you saw earlier");
								}*/
			       doubleLine = true;
			   }
		       }
		       else if (select > 666)
		       {
			   if (this.getAttribute(nd, prefix, "previous").equalsIgnoreCase("true"))
			   {
			       if (!this.getAttribute(nd, prefix, "same").equalsIgnoreCase(""))
			       {
				   if (this.getAttribute(nd, prefix, "most").equalsIgnoreCase("true"))
				   {
				       AddStringSlot(tmp, "unlike most of the other");
				   }
				   else
				   {
				       if (this.getAttribute(nd, prefix, "form").equalsIgnoreCase("plural"))
					   AddStringSlot(tmp, "unlike");
				       else
					   AddStringSlot(tmp, "unlike");
				       AddPreviousSlot(tmp);
				   }
			       }
			       else
				   if (this.getAttribute(nd, prefix, "most").equalsIgnoreCase("true"))
				   {
				   AddStringSlot(tmp, "unlike most of the previous");
				   }
				   else
				   {
				   if (this.getAttribute(nd, prefix, "form").equalsIgnoreCase("plural"))
				       AddStringSlot(tmp, "unlike");
				   else
				       AddStringSlot(tmp, "unlike");
				   AddPreviousSlot(tmp);
				   }
			       if (this.getAttribute(nd, prefix, "MostCommon").equalsIgnoreCase("true"))
				   AddStringSlot(tmp, "of the collection, which");
			       else
				   if (!this.getAttribute(nd, prefix, "location").equalsIgnoreCase(""))
				       AddStringSlot(tmp, "and");
				   else
				   {
				   Element COMMA = doc.createElement("COMMA");
				   tmp.appendChild(COMMA);
				   AddStringSlotComp(tmp, getAttribute(nd, prefix, "ComparatorEntities"), XmlMsgs.OWNER_TAG, "which");
				   }
			       doubleLineUnlike = true;
			       comm = true;
			       doubleLine = true;
			   }
			   else
			   {
			       if (this.getAttribute(nd, prefix, "most").equalsIgnoreCase("true"))
			       {
				   AddStringSlot(tmp, "unlike most of the");
			       }
			       else
				   if (this.getAttribute(nd, prefix, "form").equalsIgnoreCase("plural"))
				       AddStringSlot(tmp, "unlike all the");
				   else
				       AddStringSlot(tmp, "unlike the");
			       if (this.getAttribute(nd, prefix, "MostCommon").equalsIgnoreCase("true"))
				   AddStringSlot(tmp, "of the collection, which");
								/* else
				    if(this.getAttribute(nd, prefix , "kind").equalsIgnoreCase(""))
					AddStringSlot(tmp,"that you saw earlier");
				    else
				    {
					Element COMMA = doc.createElement("COMMA");
					tmp.appendChild(COMMA);
					AddStringSlot(tmp,"a kind of");
					AddPropertySlot(tmp,"nominative",PropertySlot.FILLER,false,false, true);
					AddStringSlot(tmp,"that you saw earlier");
									 }*/
			       doubleLine = true;
			   }
		       }
		       else
		       {
			   if (this.getAttribute(nd, prefix, "previous").equalsIgnoreCase("true"))
			   {
			       if (!this.getAttribute(nd, prefix, "same").equalsIgnoreCase(""))
			       {
				   if (this.getAttribute(nd, prefix, "most").equalsIgnoreCase("true"))
				   {
				       AddStringSlot(tmp, "whereas most of the other");
				   }
				   else
				   {
				       if (this.getAttribute(nd, prefix, "form").equalsIgnoreCase("plural"))
					   AddStringSlot(tmp, "whereas");
				       else
					   AddStringSlot(tmp, "whereas");
				       AddPreviousSlot(tmp);
				   }
			       }
			       else
				   if (this.getAttribute(nd, prefix, "most").equalsIgnoreCase("true"))
				   {
				   AddStringSlot(tmp, "whereas most of the previous");
				   }
				   else
				   {
				   if (this.getAttribute(nd, prefix, "form").equalsIgnoreCase("plural"))
				       AddStringSlot(tmp, "whereas");
				   else
				       AddStringSlot(tmp, "whereas");
				   AddPreviousSlot(tmp);
				   }
			       //AddStringSlot(tmp,"that you have seen earlier");
			       //    doubleLine = true;
			   }
			   else
			   {
			       if (this.getAttribute(nd, prefix, "most").equalsIgnoreCase("true"))
			       {
				   AddStringSlot(tmp, "whereas most of the");
			       }
			       else
				   if (this.getAttribute(nd, prefix, "form").equalsIgnoreCase("plural"))
				       AddStringSlot(tmp, "whereas all the");
				   else
				       AddStringSlot(tmp, "whereas the");
			       if (this.getAttribute(nd, prefix, "MostCommon").equalsIgnoreCase("true"))
				   AddStringSlot(tmp, "of the collection");
								/*else
				    if(this.getAttribute(nd, prefix , "kind").equalsIgnoreCase(""))
					AddStringSlot(tmp,"that you saw earlier");
				    else
				    {
					Element COMMA = doc.createElement("COMMA");
					tmp.appendChild(COMMA);
					AddStringSlot(tmp,"a kind of");
					AddPropertySlot(tmp,"nominative",PropertySlot.FILLER,false,false, true);
					AddStringSlot(tmp,"that you saw earlier");
				    }*/
			       doubleLine = true;
			   }
		       }
		       
		       
		       logger.debug("microName:" + microName);
		       
		       String propertyURI = nd.getNamespaceURI() + nd.getLocalName();
		       //String propertyURI = XmlMsgs.getAttribute(nd, XmlMsgs.prefix, "OnProperty");
		       Vector<NLGSlot> slots = MAOQM.getSlots(propertyURI, microName, language);
		       //Vector<NLGSlot> slots = MAOQM.getSlots("http://www.w3.org/TR/2003/CR-owl-guide-20030818/wine#locatedIn", microName, language);
		       lex.ApplyMicroplan(this, tmp, slots, doubleLine, propertyURI);
		       
		       
		       //  lex.ApplyMicroplan(this,tmp, slots);
		       
		       
						/*                      Node HAS_VALUE_MSG = (Node)HAS_VALUE_MSGS.get(i);
		String MicroName = XmlMsgs.getAttribute(HAS_VALUE_MSG, XmlMsgs.prefix, XmlMsgs.TEMPLATE_TAG);
		String propertyURI = XmlMsgs.getAttribute(HAS_VALUE_MSG, XmlMsgs.prefix, "OnProperty");
						 
		Vector<NLGSlot> slots = MAOQM.getSlots(propertyURI, MicroName, getLanguage());
		ApplyMicroplan(MyXmlMsgs,HAS_VALUE_MSG, slots);
						 */
		       
		       NodeList nodes = tmp.getChildNodes();
		       for (int i = 0; i < nodes.getLength(); i++)
		       {
			   if (nodes.item(i).getLocalName() != null && nodes.item(i).getLocalName().equalsIgnoreCase(this.FILLER_TAG))
			   {
			       if (this.getAttribute(nd, prefix, "form").equalsIgnoreCase("plural"))
			       {
				   tmp.removeChild(nodes.item(i));
				   AddPropertySlot(tmp,propertyURI, XmlMsgs.ACCUSATIVE_TAG, PropertySlot.FILLER, true, false);
			       }
			   }
			   if (this.getAttribute(nodes.item(i), prefix, "case").compareTo(this.NOMINATIVE_TAG) == 0)
			   {
			       NamedNodeMap NMM = nodes.item(i).getAttributes();
			       if (this.getAttribute(nd, prefix, "form").equalsIgnoreCase("plural"))
			       {
				   tmp.removeChild(nodes.item(i));
				   Element owner = doc.createElementNS(this.owlnlNS, prefix + ":" + OWNER_TAG);
				   SetAttr(owner, this.owlnlNS, this.prefix, this.CASE_TAG, this.ACCUSATIVE_TAG);
				   SetAttr(owner, this.owlnlNS, this.prefix, "form", this.PLURAL);
				   tmp.insertBefore(owner, nodes.item(i));
				   
				   if (doubleLineUnlike)
				   {
				       tmp.insertBefore(nodes.item(2), nodes.item(i + 1));
				       
				       if (comm)
				       {
					   //tmp.insertBefore(nodes.item(1),nodes.item(i+1));
					   tmp.insertBefore(nodes.item(2), nodes.item(i + 1));
				       }
				   }
				   else
				       if (doubleLine)
				       {
				       tmp.insertBefore(nodes.item(1), nodes.item(i + 1));
				       
				       if (comm)
					   tmp.insertBefore(nodes.item(2), nodes.item(i + 1));
				       }
				   if (!this.getAttribute(nd, prefix, "location").equalsIgnoreCase(""))
				   {
				       AddLocationSlot(tmp, this.getAttribute(nd, prefix, "location"), this.getAttribute(nd, prefix, "form").equalsIgnoreCase("plural"));
				       if (doubleLineUnlike)
				       {
					   
					   tmp.insertBefore(nodes.item(nodes.getLength() - 1), nodes.item(4));
					   if (comm)
					       tmp.insertBefore(nodes.item(2), nodes.item(5));
				       }
				       else
					   tmp.insertBefore(nodes.item(nodes.getLength() - 1), nodes.item(3));
				       if (select < 666)
				       {
					   Element COMMA = doc.createElement("COMMA");
					   tmp.appendChild(COMMA);
					   tmp.insertBefore(nodes.item(nodes.getLength() - 1), nodes.item(4));
				       }
				   }
				   //AddLocationSlot(tmp, this.getAttribute(nd, prefix , "location"), this.getAttribute(nd,prefix,"form").equalsIgnoreCase("plural"));
			       }
			       else
			       {
				   if (NMM != null)
				   {
				       if (NMM.getNamedItem(prefix + ":case") != null)
				       {
					   NMM.getNamedItem(prefix + ":case").setTextContent(this.ACCUSATIVE_TAG);
					   tmp.insertBefore(nodes.item(i), nodes.item(i + 1));
					   if (doubleLineUnlike)
					   {
					       tmp.insertBefore(nodes.item(2), nodes.item(i + 1));
					       if (comm)
						   tmp.insertBefore(nodes.item(2), nodes.item(i + 1));
					   }
					   else
					       if (doubleLine)
					       {
					       tmp.insertBefore(nodes.item(1), nodes.item(i + 1));
					       }
					   if (!this.getAttribute(nd, prefix, "location").equalsIgnoreCase(""))
					   {
					       AddLocationSlot(tmp, this.getAttribute(nd, prefix, "location"), this.getAttribute(nd, prefix, "form").equalsIgnoreCase("plural"));
					       if (doubleLineUnlike)
					       {
						   tmp.insertBefore(nodes.item(nodes.getLength() - 1), nodes.item(4));
						   if (comm)
						       tmp.insertBefore(nodes.item(2), nodes.item(5));
					       }
					       else
						   tmp.insertBefore(nodes.item(nodes.getLength() - 1), nodes.item(3));
					       if (select < 666)
					       {
						   Element COMMA = doc.createElement("COMMA");
						   tmp.appendChild(COMMA);
						   tmp.insertBefore(nodes.item(nodes.getLength() - 1), nodes.item(4));
					       }
					   }
				       }
				   }
			       }
			       //doc.g
			   }
		       }
		       // elegxe ton ari8mo
		   }
	       }
	       else
	       {
		   this.SetAttr(tmp, this.owlnlNS, this.prefix, "Val", this.getAttribute(nd, prefix, "Feature"));
		   //tmp.setAttribute("Val",this.getAttribute(nd, prefix , "Val"));
		   nd.appendChild(tmp);
		   
		   
		   String microName = XmlMsgs.getAttribute(nd, XmlMsgs.prefix, XmlMsgs.TEMPLATE_TAG);
		   
		   boolean doubleLine = false;
		   boolean comm = false;
		   boolean nomin = false;
		   if (microName.compareTo("") != 0)
		   {
		       
		       int select = (int)(Math.random() * 1000);
		       
		       if (select < 333)
		       {
			   if (this.getAttribute(nd, prefix, "previous").equalsIgnoreCase("true"))
			   {
			       AddStringSlot(tmp, "αντίθετα από");
			       if (this.getAttribute(nd, prefix, "most").equalsIgnoreCase("true"))
			       {
				   AddMostSlot(tmp);
			       }
			       ////////////////////////////////////////////////////////////////////////////
			       //isws na mh 8elei to else. analoga me to ti 8eloyme na ektypwnei///////////
			       else
				   AddPreviousSlot(tmp);
			       if (this.getAttribute(nd, prefix, "MostCommon").equalsIgnoreCase("true"))
				   AddStringSlot(tmp, "της αίθουσας που");
			       else
			       {
				   // AddLocationSlot(tmp, this.getAttribute(nd, prefix , "location"), this.getAttribute(nd,prefix,"form").equalsIgnoreCase("plural"));
				   Element COMMA = doc.createElement("COMMA");
				   tmp.appendChild(COMMA);
				   comm = true;
				   AddStringSlotComp(tmp,getAttribute(nd, prefix, "ComparatorEntities"), XmlMsgs.OWNER_TAG,"που");
			       }
			       doubleLine = true;
			   }
			   else
			   {
			       AddStringSlot(tmp, "αντίθετα από");
			       if (this.getAttribute(nd, prefix, "most").equalsIgnoreCase("true"))
			       {
				   AddMostSlot(tmp);
			       }
			       else
				   AddArticleSlot(tmp);
			       //AddPreviousSlot(tmp);
			       if (this.getAttribute(nd, prefix, "MostCommon").equalsIgnoreCase("true"))
				   AddStringSlot(tmp, "της αίθουσας που");
			       else
				   if (this.getAttribute(nd, prefix, "kind").equalsIgnoreCase(""))
				       addSeenBefore(tmp, true);
				   else
				   {
				   Element COMMA = doc.createElement("COMMA");
				   tmp.appendChild(COMMA);
				   AddStringSlot(tmp, "ένα είδος");
				   AddPropertySlot(tmp, nd.getNamespaceURI() + nd.getLocalName(), "genitive", PropertySlot.FILLER, false, false, true);
				   
				   addSeenBefore(tmp, true);
				   }
			       doubleLine = true;
			   }
		       }
		       
		       else if (select > 666)
		       {
			   if (this.getAttribute(nd, prefix, "previous").equalsIgnoreCase("true"))
			   {
			       AddStringSlot(tmp, "σε αντίθεση με");
			       if (this.getAttribute(nd, prefix, "most").equalsIgnoreCase("true"))
			       {
				   AddMostSlot(tmp);
			       }
			       else
				   AddPreviousSlot(tmp);
			       if (this.getAttribute(nd, prefix, "MostCommon").equalsIgnoreCase("true"))
				   AddStringSlot(tmp, "της αίθουσας που");
			       else
			       {
				   // AddLocationSlot(tmp, this.getAttribute(nd, prefix , "location"), this.getAttribute(nd,prefix,"form").equalsIgnoreCase("plural"));
				   Element COMMA = doc.createElement("COMMA");
				   tmp.appendChild(COMMA);
				   comm = true;
				   AddStringSlotComp(tmp, getAttribute(nd, prefix, "ComparatorEntities"), XmlMsgs.OWNER_TAG,"που");
			       }
			       doubleLine = true;
			   }
			   else
			   {
			       AddStringSlot(tmp, "σε αντίθεση με");
			       if (this.getAttribute(nd, prefix, "most").equalsIgnoreCase("true"))
			       {
				   AddMostSlot(tmp);
			       }
			       else
				   AddArticleSlot(tmp);
			       //AddPreviousSlot(tmp);
			       if (this.getAttribute(nd, prefix, "MostCommon").equalsIgnoreCase("true"))
				   AddStringSlot(tmp, "της αίθουσας, που");
			       else
				   if (this.getAttribute(nd, prefix, "kind").equalsIgnoreCase(""))
				       addSeenBefore(tmp, true);
				   else
				   {
				   Element COMMA = doc.createElement("COMMA");
				   tmp.appendChild(COMMA);
				   AddStringSlot(tmp, "ένα είδος");
				   AddPropertySlot(tmp, nd.getNamespaceURI() + nd.getLocalName(), "genitive", PropertySlot.FILLER, false, false, true);
				   
				   addSeenBefore(tmp, true);
				   }
			       doubleLine = true;
			   }
		       }
		       else
		       {
			   if (this.getAttribute(nd, prefix, "previous").equalsIgnoreCase("true"))
			   {
			       AddStringSlot(tmp, "ενώ");
			       if (this.getAttribute(nd, prefix, "most").equalsIgnoreCase("true"))
			       {
				   AddMostSlot(tmp);
			       }
			       else
				   AddPreviousSlot(tmp);
			       if (this.getAttribute(nd, prefix, "MostCommon").equalsIgnoreCase("true"))
				   AddStringSlot(tmp, "της αίθουσας");
			       //else
			       //    AddStringSlot(tmp,"που είδατε νωρίτερα");
			       //AddLocationSlot(tmp, this.getAttribute(nd, prefix , "location"), this.getAttribute(nd,prefix,"form").equalsIgnoreCase("plural"));
			       doubleLine = true;
			       nomin = true;
			   }
			   else
			   {
			       AddStringSlot(tmp, "ενώ");
			       if (this.getAttribute(nd, prefix, "most").equalsIgnoreCase("true"))
			       {
				   AddMostSlot(tmp);
			       }
			       else
				   AddArticleSlot(tmp);
			       //AddPreviousSlot(tmp);
			       if (this.getAttribute(nd, prefix, "MostCommon").equalsIgnoreCase("true"))
				   AddStringSlot(tmp, "της αίθουσας");
			       else
				   if (this.getAttribute(nd, prefix, "kind").equalsIgnoreCase(""))
				       addSeenBefore(tmp, true);
				   else
				   {
				   Element COMMA = doc.createElement("COMMA");
				   tmp.appendChild(COMMA);
				   AddStringSlot(tmp, "ένα είδος");
				   AddPropertySlot(tmp, nd.getNamespaceURI() + nd.getLocalName(), "genitive", PropertySlot.FILLER, false, false, true);
				   
				   addSeenBefore(tmp, true);
				   }
			       nomin = true;
			       doubleLine = true;
			   }
		       }
		       
		       
		       logger.debug("microName:" + microName);
		       String propertyURI = nd.getNamespaceURI() + nd.getLocalName();
		       //String propertyURI = XmlMsgs.getAttribute(nd, XmlMsgs.prefix, "OnProperty");
		       Vector<NLGSlot> slots = MAOQM.getSlots(propertyURI, microName, language);
		       //Vector<NLGSlot> slots = MAOQM.getSlots("http://www.w3.org/TR/2003/CR-owl-guide-20030818/wine#locatedIn", microName, language);
		       
		       lex.ApplyMicroplan(this, tmp, slots, propertyURI);
		       
		       //AddLocationSlot(tmp, this.getAttribute(nd, prefix , "location"), this.getAttribute(nd,prefix,"form").equalsIgnoreCase("plural"));
						/*                      Node HAS_VALUE_MSG = (Node)HAS_VALUE_MSGS.get(i);
		String MicroName = XmlMsgs.getAttribute(HAS_VALUE_MSG, XmlMsgs.prefix, XmlMsgs.TEMPLATE_TAG);
		String propertyURI = XmlMsgs.getAttribute(HAS_VALUE_MSG, XmlMsgs.prefix, "OnProperty");
						 
		Vector<NLGSlot> slots = MAOQM.getSlots(propertyURI, MicroName, getLanguage());
		ApplyMicroplan(MyXmlMsgs,HAS_VALUE_MSG, slots);
						 */
		       
		       
		       NodeList nodes = tmp.getChildNodes();
		       for (int i = 0; i < nodes.getLength(); i++)
		       {
			   if (this.getAttribute(nodes.item(i), prefix, "case").compareTo(this.NOMINATIVE_TAG) == 0)
			   {
			       NamedNodeMap NMM = nodes.item(i).getAttributes();
			       if (this.getAttribute(nd, prefix, "form").equalsIgnoreCase("plural"))
			       {
				   tmp.removeChild(nodes.item(i));
				   Element owner = doc.createElementNS(this.owlnlNS, prefix + ":" + OWNER_TAG);
				   if (!nomin)
				       SetAttr(owner, this.owlnlNS, this.prefix, this.CASE_TAG, this.ACCUSATIVE_TAG);
				   else
				       SetAttr(owner, this.owlnlNS, this.prefix, this.CASE_TAG, this.NOMINATIVE_TAG);
				   SetAttr(owner, this.owlnlNS, this.prefix, "form", this.PLURAL);
				   
				   tmp.insertBefore(owner, nodes.item(i));
				   if (doubleLine)
				   {
				       tmp.insertBefore(nodes.item(2), nodes.item(i + 1));
				       
				       if (comm)
					   tmp.insertBefore(nodes.item(2), nodes.item(i + 1));
				       
				       
				   }
				   if (!this.getAttribute(nd, prefix, "location").equalsIgnoreCase(""))
				   {
				       AddLocationSlot(tmp, this.getAttribute(nd, prefix, "location"), this.getAttribute(nd, prefix, "form").equalsIgnoreCase("plural"));
				       tmp.insertBefore(nodes.item(nodes.getLength() - 1), nodes.item(3));
				   }
			       }
			       else
			       {
				   if (NMM != null)
				   {
				       if (NMM.getNamedItem(prefix + ":case") != null)
				       {
					   NMM.getNamedItem(prefix + ":case").setTextContent(this.ACCUSATIVE_TAG);
					   tmp.insertBefore(nodes.item(i), nodes.item(i + 1));
					   if (doubleLine)
					   {
					       if (comm)
						   tmp.insertBefore(nodes.item(1), nodes.item(i + 1));
					       tmp.insertBefore(nodes.item(1), nodes.item(i + 1));
					   }
					   if (!this.getAttribute(nd, prefix, "location").equalsIgnoreCase(""))
					   {
					       AddLocationSlot(tmp, this.getAttribute(nd, prefix, "location"), this.getAttribute(nd, prefix, "form").equalsIgnoreCase("plural"));
					       tmp.insertBefore(nodes.item(nodes.getLength() - 1), nodes.item(2));
					   }
					   
				       }
				   }
			       }
			       //doc.g
			   }
		       }
		   }
	       }
	   }
	   //sygkrish omoiothtas
	   else
	   {
	       if (language.equalsIgnoreCase("en"))
	       {
		   //this.SetAttr(tmp, this.owlnlNS, this.prefix,"Val",this.getAttribute(nd, prefix , "Feature"));
		   //tmp.setAttribute("Val",this.getAttribute(nd, prefix , "Val"));
		   nd.appendChild(tmp);
		   
		   String microName = XmlMsgs.getAttribute(nd, XmlMsgs.prefix, XmlMsgs.TEMPLATE_TAG);
		   
		   if (microName.compareTo("") != 0)
		   {
		       if (this.getAttribute(nd, prefix, "previous").equalsIgnoreCase("true"))
			   if (!this.getAttribute(nd, prefix, "same").equalsIgnoreCase(""))
			   {
			   if (this.getAttribute(nd, prefix, "most").equalsIgnoreCase("true"))
			       AddStringSlot(tmp, "like most of the other");
			   else
			   {
			       if (this.getAttribute(nd, prefix, "form").equalsIgnoreCase("plural"))
				   AddStringSlot(tmp, "like");
			       else
				   AddStringSlot(tmp, "like");
			       AddPreviousSlot(tmp);
			   }
			   
			   }
			   else
			       if (this.getAttribute(nd, prefix, "most").equalsIgnoreCase("true"))
				   AddStringSlot(tmp, "like most of the previous");
			       else
			       {
			   if (this.getAttribute(nd, prefix, "form").equalsIgnoreCase("plural"))
			       AddStringSlot(tmp, "like");
			   else
			       AddStringSlot(tmp, "like");
			   AddPreviousSlot(tmp);
			       }
		       else
		       {
			   if (this.getAttribute(nd, prefix, "most").equalsIgnoreCase("true"))
			       AddStringSlot(tmp, "like most of the");
			   else
			       if (this.getAttribute(nd, prefix, "form").equalsIgnoreCase("plural"))
				   AddStringSlot(tmp, "like all the");
			       else
				   AddStringSlot(tmp, "like the");
			   //AddStringSlot(tmp,"like the");
			   //AddStringSlot(tmp,"that you saw");
		       }
		       if (this.getAttribute(nd, prefix, "form").equalsIgnoreCase("plural"))
		       {
			   AddPropertySlot(tmp, nd.getNamespaceURI() + nd.getLocalName(), this.ACCUSATIVE_TAG, PropertySlot.OWNER, true, false);
		       }
		       else
			   ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			   //den jerw ti einai ayto to teleytaio....
			   AddPropertySlot(tmp, this.ACCUSATIVE_TAG, PropertySlot.OWNER, "");
		       //if (!this.getAttribute(nd, prefix , "previous").equalsIgnoreCase("true"))
		       if (this.getAttribute(nd, prefix, "MostCommon").equalsIgnoreCase("true"))
			   AddStringSlot(tmp, "of the collection");
		       else
			   
			   if (this.getAttribute(nd, prefix, "kind").equalsIgnoreCase(""))
			   {
			   if (!this.getAttribute(nd, prefix, "previous").equalsIgnoreCase("true"))
			       AddStringSlot(tmp, "that you saw earlier");
			   }
			   else
			   {
			   Element COMMA = doc.createElement("COMMA");
			   tmp.appendChild(COMMA);
			   AddStringSlot(tmp, "a kind of");
			   AddPropertySlot(tmp, nd.getNamespaceURI() + nd.getLocalName(), "nominative", PropertySlot.FILLER, false, false, true);
			   if (!this.getAttribute(nd, prefix, "previous").equalsIgnoreCase("true"))
			       AddStringSlot(tmp, "that you saw earlier");
			   }
		       if (!this.getAttribute(nd, prefix, "location").equalsIgnoreCase(""))
		       {
			   AddLocationSlot(tmp, this.getAttribute(nd, prefix, "location"), this.getAttribute(nd, prefix, "form").equalsIgnoreCase("plural"));
		       }
		       //  logger.debug("microName:" + microName);
		       //  String propertyURI =  nd.getNamespaceURI() + nd.getLocalName();
		       //String propertyURI = XmlMsgs.getAttribute(nd, XmlMsgs.prefix, "OnProperty");
		       //  Vector<NLGSlot> slots = MAOQM.getSlots(propertyURI, microName, language);
		       //Vector<NLGSlot> slots = MAOQM.getSlots("http://www.w3.org/TR/2003/CR-owl-guide-20030818/wine#locatedIn", microName, language);
		       //  lex.ApplyMicroplan(this,tmp, slots);
		       
		       // elegxe ton ari8mo
		       
		       
		   }
	       }
	       else
	       {
		   //this.SetAttr(tmp, this.owlnlNS, this.prefix,"Val",this.getAttribute(nd, prefix , "Feature"));
		   //tmp.setAttribute("Val",this.getAttribute(nd, prefix , "Val"));
		   nd.appendChild(tmp);
		   
		   String microName = XmlMsgs.getAttribute(nd, XmlMsgs.prefix, XmlMsgs.TEMPLATE_TAG);
		   
		   if (microName.compareTo("") != 0)
		   {
						/*if (Math.random()>0.5)
			{
			    AddStringSlot(tmp,"όπως και");
			    if (this.getAttribute(nd, prefix , "previous").equalsIgnoreCase("true"))
				if (this.getAttribute(nd, prefix , "most").equalsIgnoreCase("true"))
				    AddMostSlot(tmp);
				else
				    AddPreviousSlot(tmp);
			    else
				if (this.getAttribute(nd, prefix , "most").equalsIgnoreCase("true"))
				    AddMostSlot(tmp);
				else
				    AddArticleSlot(tmp);
			    if (this.getAttribute(nd, prefix , "form").equalsIgnoreCase("plural"))
			    {
				AddPropertySlot(tmp, this.NOMINATIVE_TAG, PropertySlot.OWNER,true,false);
			    }
			    else
				AddPropertySlot(tmp, this.NOMINATIVE_TAG, PropertySlot.OWNER);
			  //  if (Math.random()>0.5)
			    if(this.getAttribute(nd, prefix , "MostCommon").equalsIgnoreCase("true"))
				if(nd.getLocalName().equalsIgnoreCase("current-location"))
					AddStringSlot(tmp,"της συλλογής");
				    else
				    AddStringSlot(tmp,"της συλλογής και");
								else
									if(this.getAttribute(nd, prefix , "kind").equalsIgnoreCase(""))
										if(nd.getLocalName().equalsIgnoreCase("current-location"))
										AddStringSlot(tmp,"που είδατε νωρίτερα");
									else
										AddStringSlot(tmp,"που είδατε νωρίτερα και");
									else
									{
										Element COMMA = doc.createElement("COMMA");
										tmp.appendChild(COMMA);
										AddStringSlot(tmp,"ένα είδος");
										AddPropertySlot(tmp,"genitive",PropertySlot.FILLER,false,false, true);
									if(nd.getLocalName().equalsIgnoreCase("current-location"))
										AddStringSlot(tmp,"που είδατε νωρίτερα");
									else
										AddStringSlot(tmp,"που είδατε νωρίτερα και");
									}
						   // else
							 //   AddStringSlot(tmp,"που είδατε προηγουμένως, και");
						}
						else*/
		       {
			   AddStringSlot(tmp, "όπως");
			   if (this.getAttribute(nd, prefix, "previous").equalsIgnoreCase("true"))
			       if (this.getAttribute(nd, prefix, "most").equalsIgnoreCase("true"))
				   AddMostSlot(tmp);
			       else
				   AddPreviousSlot(tmp);
			   else
			       if (this.getAttribute(nd, prefix, "most").equalsIgnoreCase("true"))
				   AddMostSlot(tmp);
			       else
				   AddArticleSlot(tmp);
			   if (this.getAttribute(nd, prefix, "form").equalsIgnoreCase("plural"))
			   {
			       AddPropertySlot(tmp, nd.getNamespaceURI() + nd.getLocalName(), this.NOMINATIVE_TAG, PropertySlot.OWNER, true, false);
			   }
			   else
			       AddPropertySlot(tmp, this.NOMINATIVE_TAG, PropertySlot.OWNER, "");
			   if (!this.getAttribute(nd, prefix, "MostCommon").equalsIgnoreCase("true"))
			       if (this.getAttribute(nd, prefix, "form").equalsIgnoreCase("plural"))
				   addSeenBefore(tmp, true);
			       else
				   addSeenBefore(tmp, false);
			   //if (Math.random()>0.5)
			   //     AddStringSlot(tmp,"που είδατε νωρίτερα");
			   //else
			   if (this.getAttribute(nd, prefix, "MostCommon").equalsIgnoreCase("true"))
			       if (nd.getLocalName().equalsIgnoreCase("current-location"))
				   AddStringSlot(tmp, "της αίθουσας");
			       else
				   AddStringSlot(tmp, "της αίθουσας");
			   else
			       if (!this.getAttribute(nd, prefix, "previous").equalsIgnoreCase("true"))
			       {
			       //if(this.getAttribute(nd, prefix , "kind").equalsIgnoreCase(""))
			       //     if(nd.getLocalName().equalsIgnoreCase("current-location"))
			       //   addSeenBefore(tmp);
			       // else
			       //    addSeenBefore(tmp);
			       }
							/* else
				    {
					Element COMMA = doc.createElement("COMMA");
					tmp.appendChild(COMMA);
					AddStringSlot(tmp,"ένα είδος");
					AddPropertySlot(tmp,"genitive",PropertySlot.FILLER,false,false, true);
				    if(nd.getLocalName().equalsIgnoreCase("current-location"))
					AddStringSlot(tmp,"που είδατε νωρίτερα");
				    else
					AddStringSlot(tmp,"που είδατε νωρίτερα και");
				    }*/
		       }
		       if (!this.getAttribute(nd, prefix, "location").equalsIgnoreCase(""))
		       {
			   AddLocationSlot(tmp, this.getAttribute(nd, prefix, "location"), this.getAttribute(nd, prefix, "form").equalsIgnoreCase("plural"));
		       }
		       //  logger.debug("microName:" + microName);
		       //  String propertyURI =  nd.getNamespaceURI() + nd.getLocalName();
		       //String propertyURI = XmlMsgs.getAttribute(nd, XmlMsgs.prefix, "OnProperty");
		       //  Vector<NLGSlot> slots = MAOQM.getSlots(propertyURI, microName, language);
		       //Vector<NLGSlot> slots = MAOQM.getSlots("http://www.w3.org/TR/2003/CR-owl-guide-20030818/wine#locatedIn", microName, language);
		       //  lex.ApplyMicroplan(this,tmp, slots);
		       
		       // elegxe ton ari8mo
		   }
	       }
	   }
	   
	   
       }
       
}//XmlMsgs