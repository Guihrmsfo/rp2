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
import java.util.*;

import gr.aueb.cs.nlg.Languages.*;
import gr.aueb.cs.nlg.NLFiles.*;
import gr.aueb.cs.nlg.Utils.*;
import gr.aueb.cs.nlg.Utils.XmlMsgs;

//GRE: Generate Referring Expressions

public class GRE extends NLGEngineComponent
{
    
        private HashSet Introduced_Entities;
                
	public GRE(String lang)
        {
            super(lang);     
            Introduced_Entities = new HashSet();
	}
	        
	public XmlMsgs GenerateReferringExpressions(XmlMsgs MyXmlMsgs) throws InvalidLanguageException
        {      
            Introduced_Entities = new HashSet();
            Document doc = MyXmlMsgs.getXMLTree();
            int type = MyXmlMsgs.getType();

            if(Languages.isEnglish(getLanguage()))
            {// english
                //System.out.println("GRE....English");     
                
                String Previous_Owner = "";
                Node Previous_Owner_Msg = null;

                if(type==2 || type == 1)
                {
                    Vector owners = MyXmlMsgs.ReturnMatchedNodes(XmlMsgs.prefix, XmlMsgs.OWNER_TAG);
                    boolean previousAggAllowed = false;
                                                                    
                    for (int i = 0; i < owners.size(); i++)
                    {// for each owner
                        
                        Node current_owner = (Node)owners.get(i);
                        String owner_ref = XmlMsgs.getAttribute(current_owner.getParentNode(), XmlMsgs.prefix, XmlMsgs.REF);
                        String filler_ref = XmlMsgs.getAttribute(current_owner.getParentNode(), XmlMsgs.prefix, "Val");
                        
                        Node current_Owner_Msg = current_owner.getParentNode();
                    
                        
			if(current_Owner_Msg.equals(Previous_Owner_Msg))
                        {
                            previousAggAllowed = true;
                        }
                        else
                        {
                            Node Previous_Msg = current_owner.getParentNode().getPreviousSibling();
                            
                            if(Previous_Msg == null)
                            {
                                previousAggAllowed = true;
                            }
                            else if(XmlMsgs.getAttribute(Previous_Msg, XmlMsgs.prefix, XmlMsgs.AGGREG_ALLOWED).compareTo("true")==0)
                            {
                                previousAggAllowed = true;
                            }
                            else
                            {
                                previousAggAllowed = false;
                            }
                        }
                        
                    
                        if((!Introduced_Entities.contains(owner_ref))  )
                        {
                            MyXmlMsgs.SetAttr((Element)current_owner, XmlMsgs.owlnlNS, XmlMsgs.prefix, XmlMsgs.RE_FOCUS, XmlMsgs.FOCUSLevel3);
                        }//!visited
                        //else if(!previousAggAllowed)
                        //{
                        //    MyXmlMsgs.SetAttr((Element)current_owner, XmlMsgs.owlnlNS, XmlMsgs.prefix, XmlMsgs.RE_FOCUS, XmlMsgs.FOCUSLevel2);                            
                        //}
                        else 
                        {//visited
                            Node Previous_Msg = current_owner.getParentNode().getPreviousSibling();                            
                            int pre_level = 0;

                            
                            if(current_Owner_Msg.equals(Previous_Owner_Msg))
                            {
                                MyXmlMsgs.SetAttr((Element)current_owner, XmlMsgs.owlnlNS, XmlMsgs.prefix, XmlMsgs.RE_FOCUS, XmlMsgs.FOCUSLevel1);
                                //MyXmlMsgs.replaceWithPronoun(current_owner, XmlMsgs.getAttribute(current_owner,XmlMsgs.prefix,XmlMsgs.CASE_TAG));                                
                            }
                            else
                            {
                                
				if(Previous_Msg!=null)
				    pre_level = Integer.parseInt(XmlMsgs.getAttribute(Previous_Msg, XmlMsgs.prefix, XmlMsgs.LEVEL));
                                
                                int level = -1;
                                
                                // 15/4/2008 changed
                                if(XmlMsgs.getAttribute(current_Owner_Msg, XmlMsgs.prefix, XmlMsgs.LEVEL) == null || XmlMsgs.getAttribute(current_Owner_Msg, XmlMsgs.prefix, XmlMsgs.LEVEL).equals(""))
                                {
                                    level = 1;
                                }
                                else
                                {
                                    level = Integer.parseInt(XmlMsgs.getAttribute(current_Owner_Msg, XmlMsgs.prefix, XmlMsgs.LEVEL));
                                }

                                if(pre_level == 0 && level ==1)
                                {
                                    MyXmlMsgs.SetAttr((Element)current_owner, XmlMsgs.owlnlNS, XmlMsgs.prefix, XmlMsgs.RE_FOCUS, XmlMsgs.FOCUSLevel4);                       
                                }
                                else if(pre_level > 0)
                                {
                                    if(level < pre_level)
                                    {
                                        MyXmlMsgs.SetAttr((Element)current_owner, XmlMsgs.owlnlNS, XmlMsgs.prefix, XmlMsgs.RE_FOCUS, XmlMsgs.FOCUSLevel3);
                                    } 
                                    if(level > pre_level)
                                    {
                                        MyXmlMsgs.SetAttr((Element)current_owner, XmlMsgs.owlnlNS, XmlMsgs.prefix, XmlMsgs.RE_FOCUS, XmlMsgs.FOCUSLevel3);
                                    }                                   
                                    else if (level == pre_level)
                                    {
                                        if(!previousAggAllowed)
                                        {  
                                            if(XmlMsgs.getAttribute(Previous_Msg, XmlMsgs.prefix, XmlMsgs.FOCUS_LOST).compareTo("true")==0)
                                            {
                                                MyXmlMsgs.SetAttr((Element)current_owner, XmlMsgs.owlnlNS, XmlMsgs.prefix, XmlMsgs.RE_FOCUS, XmlMsgs.FOCUSLevel3);
                                            }
                                            else
                                            {
                                                MyXmlMsgs.SetAttr((Element)current_owner, XmlMsgs.owlnlNS, XmlMsgs.prefix, XmlMsgs.RE_FOCUS, XmlMsgs.FOCUSLevel1);
                                            }
                                        }
                                        else
                                        {
                                            if(XmlMsgs.getAttribute(Previous_Msg, XmlMsgs.prefix, XmlMsgs.FOCUS_LOST).compareTo("true")==0)
                                            {                                            
                                                MyXmlMsgs.SetAttr((Element)current_owner, XmlMsgs.owlnlNS, XmlMsgs.prefix, XmlMsgs.RE_FOCUS, XmlMsgs.FOCUSLevel1);         
                                            }
                                            else
                                            {
                                                MyXmlMsgs.SetAttr((Element)current_owner, XmlMsgs.owlnlNS, XmlMsgs.prefix, XmlMsgs.RE_FOCUS, XmlMsgs.FOCUSLevel1);
                                            }
                                        }
                                    }
                                }
                            }
                                
                        }

                        Introduced_Entities.add(owner_ref);
                        Introduced_Entities.add(filler_ref);
                                                        
                        Previous_Owner = owner_ref;
                        Previous_Owner_Msg = current_Owner_Msg;
                    }// for each owner

                }//else if
                else
                System.out.println("Generate Referring Expressions + ERROR");

                MyXmlMsgs.setXMLTree(doc);
                return MyXmlMsgs;
            }// english
            else if(Languages.isGreek(getLanguage()))
            {// greek
                    String OwnerURI = MyXmlMsgs.getOwner();		
                    Vector owners = MyXmlMsgs.ReturnMatchedNodes(XmlMsgs.prefix, XmlMsgs.OWNER_TAG);
                    
                    boolean previousAggAllowed = false;
                    String Previous_Owner = "";
                    Node Previous_Owner_Msg = null;
                    
                    for (int i = 0; i < owners.size(); i++)
                    {
                            
                        Node current_owner = (Node)owners.get(i);
                        
                        String owner_ref = XmlMsgs.getAttribute(current_owner.getParentNode(), XmlMsgs.prefix, XmlMsgs.REF);
                        String filler_ref = XmlMsgs.getAttribute(current_owner.getParentNode(), XmlMsgs.prefix, "Val");                            

                        Node current_Owner_Msg = current_owner.getParentNode();

                        if(current_Owner_Msg.equals(Previous_Owner_Msg))
                        {
                            previousAggAllowed = true;
                        }
                        else
                        {
                            Node Previous_Msg = current_owner.getParentNode().getPreviousSibling();
                            
                            if(Previous_Msg == null)
                            {
                                previousAggAllowed = true;
                            }
                            else if(XmlMsgs.getAttribute(Previous_Msg, XmlMsgs.prefix, XmlMsgs.AGGREG_ALLOWED).compareTo("true")==0)
                            {
                                previousAggAllowed = true;
                            }
                            else
                            {
                                previousAggAllowed = false;
                            }
                        }

                        if((!Introduced_Entities.contains(owner_ref))  )
                        {
                            MyXmlMsgs.SetAttr((Element)current_owner, XmlMsgs.owlnlNS, XmlMsgs.prefix, XmlMsgs.RE_FOCUS, XmlMsgs.FOCUSLevel3);
                        }//!visited
                        else 
                        {//visited
                            Node Previous_Msg = current_owner.getParentNode().getPreviousSibling();
                            int pre_level = 0;

                            if(current_Owner_Msg.equals(Previous_Owner_Msg))
                            {
                                MyXmlMsgs.SetAttr((Element)current_owner, XmlMsgs.owlnlNS, XmlMsgs.prefix, XmlMsgs.RE_FOCUS, XmlMsgs.FOCUSLevel1);
                            }
                            else// current_Owner_Msg!=Previous_Owner_Msg
                            {
                                if(Previous_Msg!=null)
                                {
                                    pre_level = Integer.parseInt(XmlMsgs.getAttribute(Previous_Msg, XmlMsgs.prefix, XmlMsgs.LEVEL));
                                }
                                
                                int level = Integer.parseInt(XmlMsgs.getAttribute(current_Owner_Msg, XmlMsgs.prefix, XmlMsgs.LEVEL));

                                if(pre_level == 0 )
                                {
                                    if(level ==1)// epistrofh apo ton geniko typo
                                    {
                                        MyXmlMsgs.SetAttr((Element)current_owner, XmlMsgs.owlnlNS, XmlMsgs.prefix, XmlMsgs.RE_FOCUS, XmlMsgs.FOCUSLevel4);
                                    }
                                    else // synexizoume ston geniko typo
                                    {
                                        MyXmlMsgs.SetAttr((Element)current_owner, XmlMsgs.owlnlNS, XmlMsgs.prefix, XmlMsgs.RE_FOCUS, XmlMsgs.FOCUSLevel1);
                                    }
                                }
                                else if(pre_level > 0)
                                {
                                    if(level < pre_level)// epistrofh apo katwtero level 
                                    {                                            
                                        MyXmlMsgs.SetAttr((Element)current_owner, XmlMsgs.owlnlNS, XmlMsgs.prefix, XmlMsgs.RE_FOCUS, XmlMsgs.FOCUSLevel3);
                                    } 
                                    else if(level > pre_level) // paw se katwtero level
                                    {
                                        MyXmlMsgs.SetAttr((Element)current_owner, XmlMsgs.owlnlNS, XmlMsgs.prefix, XmlMsgs.RE_FOCUS, XmlMsgs.FOCUSLevel2);
                                    }                                   
                                    else if (level == pre_level)
                                    {
                                        if(!previousAggAllowed)
                                        {  
                                            if(XmlMsgs.getAttribute(Previous_Msg, XmlMsgs.prefix, XmlMsgs.FOCUS_LOST).compareTo("true")==0)
                                            {
                                                MyXmlMsgs.SetAttr((Element)current_owner, XmlMsgs.owlnlNS, XmlMsgs.prefix, XmlMsgs.RE_FOCUS, XmlMsgs.FOCUSLevel3);
                                            }
                                            else
                                            {
                                                MyXmlMsgs.SetAttr((Element)current_owner, XmlMsgs.owlnlNS, XmlMsgs.prefix, XmlMsgs.RE_FOCUS, XmlMsgs.FOCUSLevel1);
                                            }
                                        }
                                        else
                                        {
                                            if(XmlMsgs.getAttribute(Previous_Msg, XmlMsgs.prefix, XmlMsgs.FOCUS_LOST).compareTo("true")==0)
                                            {                                            
                                                MyXmlMsgs.SetAttr((Element)current_owner, XmlMsgs.owlnlNS, XmlMsgs.prefix, XmlMsgs.RE_FOCUS, XmlMsgs.FOCUSLevel1);         
                                            }
                                            else
                                            {
                                                MyXmlMsgs.SetAttr((Element)current_owner, XmlMsgs.owlnlNS, XmlMsgs.prefix, XmlMsgs.RE_FOCUS, XmlMsgs.FOCUSLevel1);
                                            }
                                        }
                                    }
                                }
                            }

                        }

                        Introduced_Entities.add(owner_ref);
                        Introduced_Entities.add(filler_ref);

                        Previous_Owner = owner_ref;
                        Previous_Owner_Msg = current_Owner_Msg;
                    }//for
                    return MyXmlMsgs;		
                }// greek
                else
                {
                      throw new InvalidLanguageException();
                }
                 
	}//GenerateReferringExpressions
}//GRE
