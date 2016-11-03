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

import java.io.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;

import java.util.*;

import gr.aueb.cs.nlg.Languages.*;
import gr.aueb.cs.nlg.NLFiles.*;
import gr.aueb.cs.nlg.Utils.*;
import gr.aueb.cs.nlg.Utils.XmlMsgs;

import org.apache.log4j.Logger;            

public class Lexicalisation extends NLGEngineComponent
{	
            
        static Logger logger = Logger.getLogger(Lexicalisation.class);
        
        private MicroplansAndOrderingQueryManager MAOQM;
                    
	public Lexicalisation(String Language, MicroplansAndOrderingQueryManager MAOQM)
        {
            super(Language);            
            this.MAOQM = MAOQM; 
	}
        
	//-----------------------------------------------------------------------------------
        
	public XmlMsgs lexClasses(XmlMsgs MyXmlMsgs)
        {
            
            Vector CLS_MSGS = MyXmlMsgs.ReturnMatchedNodes(XmlMsgs.prefix, XmlMsgs.CLASS_TAG);
            
            for(int i = 0; i < CLS_MSGS.size(); i++)//for
            { 
                Node CLS_MSG = (Node)CLS_MSGS.get(i);
                MyXmlMsgs.AddPropertySlot(CLS_MSG,XmlMsgs.NOMINATIVE_TAG,PropertySlot.OWNER, XmlMsgs.RE_AUTO);
                MyXmlMsgs.Add_IS_A_Slot(CLS_MSG);
                MyXmlMsgs.AddPropertySlot(CLS_MSG,XmlMsgs.NOMINATIVE_TAG,PropertySlot.FILLER, XmlMsgs.RE_AUTO);
            }
            
            Vector HAS_VALUE_MSGS = MyXmlMsgs.ReturnMatchedNodes(XmlMsgs.prefix, XmlMsgs.HAS_VALUE_RESTRICTION_TAG);
            
            for(int i = 0; i < HAS_VALUE_MSGS.size(); i++)//for
            {
                Node HAS_VALUE_MSG = (Node)HAS_VALUE_MSGS.get(i);
                String MicroName = XmlMsgs.getAttribute(HAS_VALUE_MSG, XmlMsgs.prefix, XmlMsgs.TEMPLATE_TAG);
                String propertyURI = XmlMsgs.getAttribute(HAS_VALUE_MSG, XmlMsgs.prefix, "OnProperty");
                 
                Vector<NLGSlot> slots = MAOQM.getSlots(propertyURI, MicroName, getLanguage());
                ApplyMicroplan(MyXmlMsgs,HAS_VALUE_MSG, slots, "");                    
            }            
            
            String CardRestrictions [] = {XmlMsgs.MAX_CARDINALITY_RESTRICTION_TAG, 
                                       XmlMsgs.MIN_CARDINALITY_RESTRICTION_TAG,
                                       XmlMsgs.CARDINALITY_RESTRICTION_TAG};
                        
            for(int i = 0; i < CardRestrictions.length; i++)//for
            {
                Vector CARD_MSGS = MyXmlMsgs.ReturnMatchedNodes(XmlMsgs.prefix, CardRestrictions[i]);
                
                for(int j = 0; j < CARD_MSGS.size(); j++) //for
                {
                    Node CARD_MSG = (Node)CARD_MSGS.get(j);
                    String MicroName = XmlMsgs.getAttribute(CARD_MSG, XmlMsgs.prefix, XmlMsgs.TEMPLATE_TAG);
                    String propertyURI = XmlMsgs.getAttribute(CARD_MSG, XmlMsgs.prefix, "OnProperty");

                    Vector<NLGSlot> slots = MAOQM.getSlots(propertyURI, MicroName, getLanguage());
                    ApplyMicroplan(MyXmlMsgs,CARD_MSG, slots, "");                    
                }//for
            }//for
                                    
            
            return MyXmlMsgs;
	}// lexClasses
         
	//-----------------------------------------------------------------------------------	
	public XmlMsgs lexInstances(XmlMsgs MyXmlMsgs)
        {
            
            logger.debug("lexInstances...");

            Vector Msgs_vec = MyXmlMsgs.getMsgs();

            for(int i = 0; i < Msgs_vec.size(); i++)
            { // for each msg 
                    Node currNode = (Node)Msgs_vec.get(i);

                    if(XmlMsgs.compare(currNode, XmlMsgs.prefix, XmlMsgs.type)) 
                    {
                        MyXmlMsgs.AddPropertySlot(currNode,XmlMsgs.NOMINATIVE_TAG,PropertySlot.OWNER, XmlMsgs.RE_AUTO);
                        MyXmlMsgs.Add_IS_A_Slot(currNode);                                                               
                        
                        if(!XmlMsgs.getAttribute(currNode, XmlMsgs.prefix , "Unique").equalsIgnoreCase(""))
                        {
                            MyXmlMsgs.AddUniqueSlot(currNode);
                            MyXmlMsgs.AddPropertySlot(currNode,XmlMsgs.NOMINATIVE_TAG,PropertySlot.FILLER,XmlMsgs.RE_AUTO);
                            if (getLanguage().equalsIgnoreCase("El"))
                                MyXmlMsgs.AddStringSlot(currNode, "της αίθουσας");
                            else
                                MyXmlMsgs.AddStringSlot(currNode, "of the collection");
                        }
                        else
                        {
                            MyXmlMsgs.AddPropertySlot(currNode,XmlMsgs.NOMINATIVE_TAG,PropertySlot.FILLER, XmlMsgs.RE_AUTO);
                        }
                    }
                    else if(XmlMsgs.compare(currNode, XmlMsgs.prefix, XmlMsgs.DIFFERENT_FROM_TAG)){
                        //....
                    }
                    else if(XmlMsgs.compare(currNode, XmlMsgs.prefix, XmlMsgs.SAME_AS_TAG)){
                        //....
                    }
                    else
                    {// 
                         String microName = XmlMsgs.getAttribute(currNode, XmlMsgs.prefix , XmlMsgs.TEMPLATE_TAG);
                         String propertyURI =  currNode.getNamespaceURI() + currNode.getLocalName();
                         logger.debug("propertyURI:" + propertyURI);
                                         
                         //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////                         
                         String comparatorName = XmlMsgs.getAttribute(currNode, XmlMsgs.prefix , "Comparator");
                         
                         if (!comparatorName.equalsIgnoreCase(""))
                         {
                             MyXmlMsgs.AddComparatorSlot(currNode,comparatorName, this, MAOQM, getLanguage());
                         }
                         
                         String uniqueName = XmlMsgs.getAttribute(currNode, XmlMsgs.prefix , "Unique");
                         
                         if (!uniqueName.equalsIgnoreCase(""))
                         {
                            MyXmlMsgs.AddPropertySlot(currNode,XmlMsgs.NOMINATIVE_TAG,PropertySlot.OWNER,XmlMsgs.RE_AUTO);
                            MyXmlMsgs.Add_IS_A_Slot(currNode);
                            //MyXmlMsgs.AddUniqueSlot(currNode);
                            if(!XmlMsgs.getAttribute(currNode, XmlMsgs.prefix , "Unique").equalsIgnoreCase(""))
                            {
                                MyXmlMsgs.AddUniqueSlot(currNode);
                                MyXmlMsgs.AddPropertySlot(currNode,"",XmlMsgs.NOMINATIVE_TAG,PropertySlot.FILLER,false, true);
                                if (getLanguage().equalsIgnoreCase("El"))
                                    MyXmlMsgs.AddStringSlot(currNode, "της αίθουσας που");
                                else
                                    MyXmlMsgs.AddStringSlot(currNode, "of the collection which");
                                }
                                else
                                    MyXmlMsgs.AddPropertySlot(currNode,XmlMsgs.NOMINATIVE_TAG,PropertySlot.FILLER,XmlMsgs.RE_AUTO);
                         }
                         
                         String commonName = XmlMsgs.getAttribute(currNode, XmlMsgs.prefix , "Common");
                         if (!commonName.equalsIgnoreCase(""))
                         {
                             //Όπως και <ολες οι><ονομα> της συλλογης, αυτό....
                             // όπως και text tag
                             if (getLanguage().equalsIgnoreCase("El"))
                                MyXmlMsgs.AddStringSlot(currNode, "όπως");
                             else
                                MyXmlMsgs.AddStringSlot(currNode, "like"); 
                             // όλες οι (κλασσικό)
                             MyXmlMsgs.AddCommonSlot(currNode);
                              // όνομα
                             MyXmlMsgs.AddPropertySlot(currNode,"",XmlMsgs.NOMINATIVE_TAG,PropertySlot.FILLER,true, true);
                             // <της συλλογής,>
                             if (getLanguage().equalsIgnoreCase("El"))
                                MyXmlMsgs.AddStringSlot(currNode, "της αίθουσας,");
                             else
                                 MyXmlMsgs.AddStringSlot(currNode, "of the collection,");
                             // ???
                             /*if(microName.compareTo("")!=0 ){                            

                                logger.debug("microName:" + microName);
                                Vector<NLGSlot> slots = MAOQM.getSlots(propertyURI, microName, getLanguage());
                                ApplyMicroplan(MyXmlMsgs,currNode, slots);
                             }*/
                         }
                         ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                         
                         if(microName.compareTo("")!=0 )
                         {                            

                            logger.debug("microName:" + microName);
                            if(microName.compareTo("MICROPLAN NOT FOUND") != 0)
                            {
                                Vector<NLGSlot> slots = MAOQM.getSlots(propertyURI, microName, getLanguage());
                                ApplyMicroplan(MyXmlMsgs,currNode, slots, "");
                            }
                            else
                            {
                                MyXmlMsgs.AddPropertySlot(currNode,XmlMsgs.NOMINATIVE_TAG,PropertySlot.OWNER, XmlMsgs.RE_AUTO);
                                MyXmlMsgs.AddVerbSlot(currNode, "@"+currNode.getLocalName()+"@", "@"+currNode.getLocalName()+"@",XmlMsgs.ACTIVE_VOICE, XmlMsgs.TENSE_PRESENT);
                                MyXmlMsgs.AddPropertySlot(currNode,XmlMsgs.ACCUSATIVE_TAG,PropertySlot.FILLER, XmlMsgs.RE_AUTO);
                            }
                         }
                    }//else
            }// for each msg 

            return MyXmlMsgs;	
	}
	//-----------------------------------------------------------------------------------

	//-----------------------------------------------------------------------------------	
        
	public void  ApplyMicroplan(XmlMsgs msgs, Node nd, Vector<NLGSlot> slots, String PropURI) 
        {
            try{

                //logger.debug("Apply microplans");

                if(slots != null)
                {

                    for(int i = 0; i < slots.size(); i++)
                    {// for each slot
                        NLGSlot slot = slots.get(i);

                        if(slot instanceof PropertySlot)
                        {
                            PropertySlot PS = (PropertySlot)slot;
                            
                            if (nd.getLastChild()==null || nd.getLastChild().getPreviousSibling()==null)
                            {
                                //logger.debug("PropertySlot");
                                if(PropURI.compareTo("")==0 || PropURI ==null)
                                    msgs.AddPropertySlot(nd,PS.CASE, PS.type, PS.re_type);
                                else
                                    msgs.AddPropertySlotComp(nd, PropURI, PS.CASE, PS.type, PS.re_type);
                            }
                            else
                            {
                                if(!(nd.getLastChild().getTextContent().endsWith("which")&&PS.type == PropertySlot.PROPERTY_OWNER))
                                {
                                    if(!(nd.getLastChild().getPreviousSibling().getTextContent().endsWith("which")&&PS.type == PropertySlot.PROPERTY_OWNER))
                                    {
                                        logger.debug("PropertySlot");
                                        if(PropURI.compareTo("")==0 || PropURI ==null)
                                            msgs.AddPropertySlot(nd,PS.CASE, PS.type, PS.re_type); // XmlMsgs.RE_AUTO --> PS.re_type
                                        else
                                            msgs.AddPropertySlotComp(nd, PropURI, PS.CASE, PS.type, PS.re_type); // XmlMsgs.RE_AUTO --> PS.re_type
                                    }
                                }
                            }
                                 
                        }                            
                        else if(slot instanceof VerbSlot)
                        {
                            VerbSlot VS = (VerbSlot)slot;
                            //logger.debug("VerbSlot");
                            if(PropURI.compareTo("")==0 || PropURI ==null)                                
                                msgs.AddVerbSlot(nd,VS.VERB,VS.pluralVERB,VS.VOICE, VS.TENSE);
                            else
                                msgs.AddVerbSlotComp(nd, PropURI, VS.VERB,VS.pluralVERB,VS.VOICE, VS.TENSE);
                        }
                        else if(slot instanceof StringSlot)
                        {
                            StringSlot SS = (StringSlot)slot;
                            //logger.debug("StringSlot");
                            if(PropURI.compareTo("")==0 || PropURI ==null)
                                msgs.AddStringSlot(nd,SS.STRING, SS.isPreposition);
                            else
                                msgs.AddStringSlotComp(nd,PropURI,SS.STRING, SS.isPreposition);
                        }
                        else if(slot instanceof PrepSlot)
                        {
                            PrepSlot SS = (PrepSlot)slot;
                            //logger.debug("StringSlot");
                            if(PropURI.compareTo("")==0 || PropURI ==null)
                                msgs.AddStringSlot(nd, SS.prep);
                            else
                                msgs.AddStringSlotComp(nd, PropURI,SS.prep);
                        }                        
                    }// for each slot
                }

            }
            catch(Exception e){
                e.printStackTrace();
            }			
	}//transform
        
        //
        public void  ApplyMicroplan(XmlMsgs msgs, Node nd, Vector<NLGSlot> slots, boolean doubleLine, String PropURI) 
        {
            try{

                logger.debug("Apply microplans");

                if(slots != null){

                    for(int i = 0; i < slots.size(); i++)
                    {// for each slot
                        NLGSlot slot = slots.get(i);

                        if(slot instanceof PropertySlot)
                        {
                            PropertySlot PS = (PropertySlot)slot;
                            if (nd.getLastChild()==null || nd.getLastChild().getPreviousSibling()==null)
                            {
                                logger.debug("PropertySlot");
                                msgs.AddPropertySlotComp(nd, PropURI, PS.CASE, PS.type,XmlMsgs.RE_AUTO);
                            }
                            else
                            if(!(nd.getLastChild().getTextContent().endsWith("which")&&PS.type == PropertySlot.PROPERTY_OWNER))
                            {
                                if(!(nd.getLastChild().getPreviousSibling().getTextContent().endsWith("which")&&PS.type == PropertySlot.PROPERTY_OWNER))
                                 {
                                     logger.debug("PropertySlot");
                                     msgs.AddPropertySlotComp(nd, PropURI, PS.CASE, PS.type,XmlMsgs.RE_AUTO);
                                 }
                            }
                            else
                            {
                                if(doubleLine)
                                     msgs.AddPropertySlotComp(nd, PropURI, PS.CASE, PS.type, XmlMsgs.RE_AUTO);
                            }
                            /*else
                            {
                                if(nd.getLastChild().getPreviousSibling()!=null&&!(nd.getLastChild().getPreviousSibling().getTextContent().endsWith("which")&&PS.type == PropertySlot.PROPERTY_OWNER))
                                {
                                    logger.debug("PropertySlot");
                                    msgs.AddPropertySlot(nd,PS.CASE, PS.type);
                                }
                            }*/
                        }                            
                        else if(slot instanceof VerbSlot)
                        {
                            VerbSlot VS = (VerbSlot)slot;
                            logger.debug("VerbSlot");
                            msgs.AddVerbSlotComp(nd, PropURI, VS.VERB,VS.pluralVERB,VS.VOICE, VS.TENSE);
                        }
                        else if(slot instanceof StringSlot)
                        {
                            StringSlot SS = (StringSlot)slot;
                            logger.debug("StringSlot");
                            msgs.AddStringSlotComp(nd, PropURI, SS.STRING, SS.isPreposition);
                        }
                        else if(slot instanceof PrepSlot)
                        {
                            PrepSlot SS = (PrepSlot)slot;
                            //logger.debug("StringSlot");
                            msgs.AddStringSlotComp(nd, PropURI,  SS.prep);
                        }   
                    }// for each slot
                }

            }
            catch(Exception e){
                e.printStackTrace();
            }			
	}//transform
 
        
	//-----------------------------------------------------------------------------------	
	public boolean AllChildsCls(Node current){
		NodeList childs = current.getChildNodes();
		
		for(int i = 0; i < childs.getLength(); i++){
			if (childs.item(i).getNodeName().compareTo(XmlMsgs.CLASS_TAG)!=0)
			return false;
		}
		
		return true;
	}
}//Lexicalisation
//-----------------------------------------------------------------------------------	

