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

import javax.xml.parsers.*;
import org.w3c.dom.*;
import java.io.*;
import java.util.*;


import gr.aueb.cs.nlg.Languages.*;
import gr.aueb.cs.nlg.NLFiles.*;
import gr.aueb.cs.nlg.Utils.*;
import gr.aueb.cs.nlg.Utils.XmlMsgs;

import org.apache.log4j.Logger;            

public class Aggregation extends NLGEngineComponent
{		
        static Logger logger = Logger.getLogger(Aggregation.class);
         
	private int MAX_FACTS_PER_SENTENCE;
        private int Internal_MAX_FACTS_PER_SENTENCE = 4;
        
	private NLGLexiconQueryManager Lexicon;
	 
	public	final static  String GREEK_MASCULINE_RELATIVE_PRONOUN = "ο οποίος";		
	public	final static  String GREEK_FEMININE_RELATIVE_PRONOUN = "η οποία";
	public	final static  String GREEK_NEUTER_RELATIVE_PRONOUN = "το οποίο";
	
	public	final static  String GREEK_GENDER_INSENSITIVE_PRONOUN = "που";
	
	public	final static  String GREEK_CONNECTIVE = "και";
	public	final static  String ENGLISH_CONNECTIVE = "and";

	public	final static  String GREEK_DISJUNCTION = "ή";
	public	final static  String ENGLISH_DISJUNCTION = "or";
			
	// constructor		
	public Aggregation(int MFPS,String Language, NLGLexiconQueryManager Lexicon)
        {
                super(Language);
		MAX_FACTS_PER_SENTENCE = MFPS;		
		this.Lexicon = Lexicon;
	}
	        
	public void set_MAX_FACTS_PER_SENTENCE(int value)
        {	
		MAX_FACTS_PER_SENTENCE = value;
		//logger.debug("MFPS: " + MAX_FACTS_PER_SENTENCE);
	}

	public XmlMsgs Aggregate(XmlMsgs MyXmlMsgs)
        {
		int type = MyXmlMsgs.getType();
		logger.debug("type" + type);
		
		Document doc = null;		
		if(type == 1)
                {// aggregation for classes
			logger.debug("Classes Aggregation");
			doc = MyXmlMsgs.getXMLTree();
			//Agreggate_enumerations(doc);	//aggregate enumerations
			//Agreggate_allValues_and_someValues(doc);
			//Agreggate_Unions(doc);
			//Agreggate_Intersections(doc);
	                        
			Vector msgs = MyXmlMsgs.ReturnMatchedNodes(XmlMsgs.prefix, "owlMsg");						
			Node root = MyXmlMsgs.getRoot();
			
			logger.debug(" i have found " + msgs.size() + "Msg(s)");
                        
                        //remove msgs from xml tree
                        MyXmlMsgs.removeMsgs(msgs);
                        
                       
			for (int i = 0; i < msgs.size(); i++)
                        {
                            Node Msg = (Node)msgs.get(i);                            
                            Node child = Msg.getChildNodes().item(0);
                            
                            logger.debug(""  + Msg.toString());	
                            logger.debug(""  + child.toString());		
                            logger.debug(""  + child.getChildNodes().item(0));
                         
                            if(child.getChildNodes().item(0) != null)
                            {
                                if(child.getNodeName().equals(XmlMsgs.prefix + ":"+ XmlMsgs.SUBCLASS_OF_TAG))
                                {
                                   root.appendChild(child.getChildNodes().item(0));
                                   logger.debug("appending" + child.getChildNodes().item(0));
                                }
                                else if (child.getNodeName().equals(XmlMsgs.prefix + ":" + XmlMsgs.EQUIVALENT_CLASS_TAG))
                                {
                                    root.appendChild(child.getChildNodes().item(0));
                                    logger.debug("appending" + child.getChildNodes().item(0));
                                }
                                else
                                {

                                }
                            }
			}

                        logger.debug(" extraction completed...");		
			//Aggregate(MyXmlMsgs.getXMLTree(),msgs ,root, false);
		}//if
		else if (type == 2)
                {//aggregation for instances			
			logger.debug("Instances Aggregation");
                        			
                        Node root = MyXmlMsgs.getRoot();
                        Vector msgs = MyXmlMsgs.getMsgs();
                                
			//remove msgs from xml tree
                        MyXmlMsgs.removeMsgs(msgs);			
			
			//aggregate msgs using the aggregation rules
			Aggregate(MyXmlMsgs.getXMLTree(), msgs ,root, false);
				
		}//if type==2	
		
		return MyXmlMsgs;
	}//Aggregate
        
        //comparisons
        public boolean exist(String name, NamedNodeMap map) 
        {
            for (int i=0; i<map.getLength(); i++) {
                if (map.item(i).getLocalName().equalsIgnoreCase(name))
                    return true;
            }
            return false;
        }
        
        //comparisons
        public boolean cont(Vector<Node> Msg_list) 
        {
            int counter=0;
            for (int k=0; k<Msg_list.size(); k++) 
            {
                Node current_MsgTmp = Msg_list.get(k);
                if (current_MsgTmp.getFirstChild()==null || (current_MsgTmp.getFirstChild()!=null &&current_MsgTmp.getFirstChild().getLocalName().equalsIgnoreCase("comparator")&& exist("multyComp",current_MsgTmp.getFirstChild().getAttributes())))
                counter++;
            }
            
            if (counter>1)
                return true;
            else
                return false;
        }        
	//-----------------------------------------------------------------------------------		
	private void Aggregate(Document doc, Vector<Node> Msg_list, Node root, boolean internalMsgs) 
        {
            
            logger.debug("i am aggregating " + Msg_list.size() + " msgs" );
            int i = 0;

            int MPFS = 0;
            if(internalMsgs)
            {
                MPFS = Internal_MAX_FACTS_PER_SENTENCE;
                RemoveMsgsFromDoc(Msg_list);
            }
            else
            {
                MPFS = MAX_FACTS_PER_SENTENCE;
            }

            Vector<Node> Msg_List_After = new Vector<Node>();													
            Vector<Node> Msg_list_copy = new Vector<Node>();

            // filler aggregation + focus lost
            i  = 0; 
            while(i < Msg_list.size()-1)
            {// gia kathe msg
                
                String filler = XmlMsgs.getAttribute(Msg_list.get(i),XmlMsgs.prefix, "Val");
                Object obj = Lexicon.getEntry(filler, getLanguage(), null);
                
                
		if(obj instanceof CannedList)
                {
                    
                    boolean b = ((CannedList)obj).getFillerAggregationAllowed();
                    
                    logger.debug(filler + " FillerAggregationAllowed" + b);
                    
		    if(b)
                    {
                        ((Element)Msg_list.get(i)).setAttribute("owlnl:" + XmlMsgs.AGGREG_ALLOWED, "true");
                    }
                    else
                    {
                        ((Element)Msg_list.get(i)).setAttribute("owlnl:" + XmlMsgs.AGGREG_ALLOWED, "false");
                    }
                    
                    
                    b = ((CannedList)obj).getFOCUS_LOST();
                    
                    logger.debug(filler + " FOCUS_LOST" + b);
                    
                    if(b)
                    {
                        ((Element)Msg_list.get(i)).setAttribute("owlnl:" + XmlMsgs.FOCUS_LOST, "true");
                    }
                    else
                    {
                        ((Element)Msg_list.get(i)).setAttribute("owlnl:" + XmlMsgs.FOCUS_LOST, "false");
                    }                    
                }
                i++;
            }// gia kathe msg
            
            // create a copy of the Msg_list
            for(int j = 0; j < Msg_list.size(); j++)
            {
                    Msg_list_copy.add(Msg_list.get(j).cloneNode(true));
            }

            
            // apply first the syntactic embedding rule
            i  = 0 ;
            while(i < Msg_list.size()-1)
            {
                Node current_Msg = Msg_list.get(i);
                Node current_Msg_copy = Msg_list_copy.get(i);

                Node next_Msg = Msg_list.get(i + 1);
                Node next_Msg_copy = Msg_list_copy.get(i + 1);	

                //if syntactic embedding rule can be applied	
                if(syntactic_embedding(current_Msg_copy, next_Msg_copy))
                {
                    apply_syntactic_embedding(doc,current_Msg, next_Msg);
                    Msg_list.remove(i + 1);
                    Msg_list_copy.remove(i + 1);
                    i--;
                }										

                i++;
            }// apply first the syntactic embedding rule

            i = 0;
            // apply first the multiple filler rule
            //
            while(i < Msg_list.size()-1)
            {// apply first the multiple filler rule
                Node current_Msg = Msg_list.get(i);
                Node current_Msg_copy = Msg_list_copy.get(i);

                Node next_Msg = Msg_list.get(i + 1);
                Node next_Msg_copy = Msg_list_copy.get(i + 1);	
                
                logger.debug("trying to #### apply shared subject rule");
                               
                //if syntactic embedding rule can be applied	
                if(shared_subject_predicate(doc,current_Msg_copy, next_Msg_copy))
                {
                    logger.debug("#### apply shared subject rule");
                        
                    if( i + 2 < Msg_list.size()-1)
                    {
                        if( shared_subject_predicate(doc,current_Msg_copy, Msg_list_copy.get(i + 2)))
                        {
                            Apply_shared_subject_predicate_RULE(doc, current_Msg, next_Msg, false);
                        }
                        else
                        {
                            Apply_shared_subject_predicate_RULE(doc, current_Msg, next_Msg, true);
                        }
                    }
                    else
                    {
                        Apply_shared_subject_predicate_RULE(doc, current_Msg, next_Msg, true);
                    }
                    
                    Msg_list.remove(i + 1);
                    Msg_list_copy.remove(i + 1);
                    i--;
                }										
		
		i++;
            }// apply first the multiple filler rule
            
            // apply the other aggregation rules !!!!!!!!!!!!!!!!!!
            i = 0;
	    int j = 1;
            
	    while(i < Msg_list.size())
            { // apply the other aggregation rules !!!!!!!!!!!!!!!!!!
                
                Node current_Msg = Msg_list.get(i);
                Node current_Msg_copy = Msg_list_copy.get(i);

                String start_REF = XmlMsgs.getAttribute(current_Msg, XmlMsgs.prefix, XmlMsgs.REF); // new line
                String AggAllowed1 = XmlMsgs.getAttribute(current_Msg, XmlMsgs.prefix, XmlMsgs.AGGREG_ALLOWED); // new line
                
                if(AggAllowed1.compareTo("true") == 0)
                {
                
                    boolean breaking = false; // comparisons
                    int count = 0; // comparisons
            
                    for (int k = i; k < Msg_list.size(); k++) // comparisons
                    {
                        Node current_MsgTmp = Msg_list.get(k);
                        if ((current_MsgTmp.getFirstChild()!=null &&current_MsgTmp.getFirstChild().getLocalName().equalsIgnoreCase("comparator")&& exist("multyComp",current_MsgTmp.getFirstChild().getAttributes())))
                            count++;
                    }
                    
                    
                    if (count > 1 && (current_Msg.getFirstChild()!=null && current_Msg.getFirstChild().getLocalName().equalsIgnoreCase("comparator") && exist("multyComp",current_Msg.getFirstChild().getAttributes()) && cont(Msg_list))) 
                    {// if  /* comparisons */
                        logger.debug("swstos!!!!!!!!!!!!!");

                        //briskw posa einai ta koina xarakthristika
                        int counter = 0;
                        // 8a mporoysa na jekinw apo to i-osto afoy jerw oti ayto einai to prwto
                        for (int k=0; k<Msg_list.size(); k++) 
                        {
                            Node current_MsgTmp = Msg_list.get(k);
                            if ((current_MsgTmp.getFirstChild()!=null &&current_MsgTmp.getFirstChild().getLocalName().equalsIgnoreCase("comparator")&& exist("multyComp",current_MsgTmp.getFirstChild().getAttributes())))
                                counter++;
                        }
                        
                        int c = 0;
                        
                        for (int k=i; k<Msg_list.size(); k++) 
                        {
                            Node current_MsgTmp = Msg_list.get(k);
                            if ((current_MsgTmp.getFirstChild()!=null &&current_MsgTmp.getFirstChild().getLocalName().equalsIgnoreCase("comparator")&& exist("multyComp",current_MsgTmp.getFirstChild().getAttributes()))) 
                            {
                                //to bazw ekei poy prepei
                                if (c == counter-1) {
                                    //bazw and
                                    Apply_Simple_Conjuction_RULE2(doc, current_Msg,current_MsgTmp);
                                    Msg_list.removeElementAt(k);
                                    k--;
                                    //Msg_list.removeElementAt(k);
                                    break;
                                } else if (c>0){
                                    //bazw komma
                                    Apply_Type_COMMA_RULE3(doc,current_Msg, current_MsgTmp);
                                    Msg_list.removeElementAt(k);
                                    k--;
                                }
                                /*else
                                {
                                    addCommmonFeatureText(doc,current_Msg);
                                }*/

                                //to afairw apo th lista

                                c++;
                            }
                        }
                        
                        breaking = true;
                        root.appendChild(current_Msg);
                        i++;                        
                        //  removeItem("multyComp",current_Msg.getFirstChild().getAttributes());
                
                    }// if /* comparisons */
                    else
                    {                    
                        if (current_Msg.getFirstChild()==null || (current_Msg.getFirstChild()!=null && !current_Msg.getFirstChild().getLocalName().equalsIgnoreCase("comparator")))
                        {
                            if (current_Msg.getAttributes().item(2)==null|| (current_Msg.getAttributes().item(3)!=null && !(exist("unique",current_Msg.getAttributes())||exist("Common",current_Msg.getAttributes()) ||exist("mostCommon",current_Msg.getAttributes())))) 
                            {   // @@@@@@                 
                    
                                j = 1;					

                                int Aggr_Msg_counter = 0;
                                boolean subject_changed = false;
                                boolean found_msg_not_Agg_msg = false;
                                String pre_VAL = XmlMsgs.getAttribute(current_Msg, XmlMsgs.prefix, "Val"); // new line

                                while(j <= MPFS - 1 && i + j < Msg_list.size() && !subject_changed && ! found_msg_not_Agg_msg) // new line (changed)
                                {//j <= MPFS - 1 && i + j < Msg_list.size()

                                    Node next_Msg = Msg_list.get(i + j);
                                    Node next_Msg_copy = Msg_list_copy.get(i + j);
                        
                                    if (!(next_Msg.getFirstChild()==null || (next_Msg.getFirstChild()!=null && !next_Msg.getFirstChild().getLocalName().equalsIgnoreCase("comparator")))) 
                                    {// comparisons
                                        breaking = true;
                                        //if(!exist("multyComp",next_Msg.getFirstChild().getAttributes()))
                                        if (this.exist("akoma", next_Msg.getFirstChild().getAttributes()))
                                        if (i==0)
                                        {
                                            Element TEXT = createTextNode(doc);
                            
                                            TEXT.setTextContent("ακόμα");
                            
                                            Vector Msgs_Slots = XmlMsgs.ReturnChildNodes(Msg_list.get(i));
                                            Node current_slot =(Node)Msgs_Slots.get(Msgs_Slots.size()-1);

                                            Msg_list.get(i).insertBefore(TEXT,current_slot);   
                            
                                        }
                                        i = i + j;
                                        break;
                                    } // comparisons
                        
                                    if (!(next_Msg.getAttributes().item(3)==null|| (next_Msg.getAttributes().item(3)!=null && !(exist("unique",next_Msg.getAttributes()))&&!(exist("Common",next_Msg.getAttributes()))&&!(exist("mistCommon",next_Msg.getAttributes()))))) 
                                    { // comparisons
                                        breaking = true;

                                        if (next_Msg.getAttributes()!=null&&this.exist("akoma", next_Msg.getAttributes()))
                                        if (i==0)
                                        {
                                            Element TEXT = createTextNode(doc);

                                            TEXT.setTextContent("ακόμα");

                                            Vector Msgs_Slots = XmlMsgs.ReturnChildNodes(Msg_list.get(i));
                                            Node current_slot =(Node)Msgs_Slots.get(Msgs_Slots.size()-1);

                                            Msg_list.get(i).insertBefore(TEXT,current_slot);   

                                            }
                                            //Apply_Simple_Conjuction_RULE(doc, current_Msg,next_Msg);
                                            i = i + j;

                                            break;
                                    }   // comparisons 
                        
                        
                                    //if(XmlMsgs.getNodeChild(current_Msg,"")) 
                                    String current_REF = XmlMsgs.getAttribute(next_Msg, XmlMsgs.prefix, XmlMsgs.REF); // new line
                                    String current_VAL = XmlMsgs.getAttribute(next_Msg, XmlMsgs.prefix, "Val"); // new line

                                    String AggAllowed2 = XmlMsgs.getAttribute(next_Msg, XmlMsgs.prefix, XmlMsgs.AGGREG_ALLOWED); // new line

                                    if(AggAllowed2.compareTo("true") == 0)
                                    {
                                        // !!!!!!!!!!!!!!!!!
                                        if(XmlMsgs.compare(next_Msg, XmlMsgs.prefix, XmlMsgs.TEXT_TAG))
                                        {
                                            String text = next_Msg.getTextContent();
                                            if(Character.isUpperCase(text.charAt(0)))
                                            {
                                                char ch = Character.toUpperCase(text.charAt(0));
                                                next_Msg.setTextContent(ch + "" + text.substring(1));
                                                next_Msg_copy.setTextContent(ch + "" + text.substring(1));
                                            }
                                        }                    

                                        if(start_REF.compareTo(current_REF) ==0) //new line!!!
                                        {//start_REF.compareTo(current_REF) ==0
                                
                                            Aggr_Msg_counter++; // new line !!!!
                                        
                                            // if current sentence (message) is an IS A sentence
                                            if(XmlMsgs.getNodeChild(current_Msg_copy, XmlMsgs.prefix, XmlMsgs.IS_A_TAG)!=null)
                                            {
                                                logger.debug("IS_A" + j);		

                                                if(XmlMsgs.getNodeChild(next_Msg_copy,XmlMsgs.prefix,XmlMsgs.VERB_TAG)!=null)
                                                {// if the next sentence contains a verb 
                                                    logger.debug("IN j" + j);
                                                    Node verb_node = XmlMsgs.getNodeChild(next_Msg_copy, XmlMsgs.prefix,XmlMsgs.VERB_TAG);
                                                    String voice = XmlMsgs.getAttribute(verb_node, XmlMsgs.prefix , XmlMsgs.VOICE_TAG);

                                                    if(voice.compareTo(XmlMsgs.PASSIVE_VOICE)==0 && j==1)
                                                    {// if next's msg verb is passive && this is the first msg
                                                        //if language is english and MPFS > 2 and there exist a next msg (i+j+1)
                                                        if(MPFS > 2 && i + j + 1 < Msg_list.size() && Languages.isEnglish(getLanguage()) 
                                                        && XmlMsgs.getAttribute(Msg_list.get(i + j + 1), XmlMsgs.prefix, XmlMsgs.REF).compareTo(current_REF)==0
                                                        && XmlMsgs.getAttribute(Msg_list.get(i + j + 1), XmlMsgs.prefix, XmlMsgs.AGGREG_ALLOWED).compareTo("true")==0) // new line
                                                        {
                                                            Apply_Type_SEMICOLON_RULE(doc, current_Msg,next_Msg); // apply semicolon rule
                                                        }
                                                        else
                                                        {
                                                            Apply_Type_COMMA_RULE(doc, current_Msg,next_Msg); //else apply type comma rule
                                                        }
                                                    }
                                                    else
                                                    {//  if next's msg verb is active

                                                        if(j==1)
                                                        {//
                                                            Apply_IS_A_before_ACTIVE_VERB_RULE(doc, current_Msg, next_Msg);
                                                            //Apply_Simple_Conjuction_RULE(doc, current_Msg, next_Msg);
                                                        }
                                                        else
                                                        {
                                                            Apply_Type_COMMA_RULE2(doc, current_Msg,next_Msg);
                                                        }

                                                    }//  if next's msg verb is active

                                                }// if the next sentence contains a verb 
                                                else if(XmlMsgs.getNodeChild(next_Msg_copy, XmlMsgs.prefix, XmlMsgs.IS_A_TAG)!=null)
                                                {//if we have two "is a" Msgs in the row

                                                    int index_of_last_available_msg_for_aggr = countSameSubjectFacts(start_REF,Msg_list, i ) - 1; // new line
                                                    if( j == MPFS - 1 || index_of_last_available_msg_for_aggr == j|| i + j == Msg_list.size()-1
                                                    /*|| XmlMsgs.getAttribute(Msg_list.get(i + j), XmlMsgs.prefix, XmlMsgs.AGGREG_ALLOWED).compareTo("false")==0)*/)
                                                        Apply_IS_A_Agg_Rule(doc, current_Msg,next_Msg, true); 
                                                    else
                                                        Apply_IS_A_Agg_Rule(doc, current_Msg,next_Msg , false);

                                                }//if we have two "is a" Msgs in the row

                                            }// if current sentence (message) is an IS A sentence
                                            else// else if current Msg is not a IS_A message
                                            {
                                                if(shared_subject_predicate(doc,current_Msg_copy, next_Msg_copy))
                                                {
                                                    Apply_shared_subject_predicate_RULE(doc, current_Msg, next_Msg, false);
                                                }
                                                else
                                                {
                                                    int index_of_last_available_msg_for_aggr = countSameSubjectFacts(start_REF,Msg_list, i ) - 1; // new line
                                                    if( j == MPFS - 1 
                                                        || index_of_last_available_msg_for_aggr == j
                                                        || i + j == Msg_list.size()-1
                                                        || ( (i + j + 1 <= Msg_list.size()-1) && XmlMsgs.getAttribute(Msg_list.get(i + j + 1), XmlMsgs.prefix, XmlMsgs.AGGREG_ALLOWED).compareTo("false")==0)
                                                        )
                                                    {
                                                        Apply_Simple_Conjuction_RULE(doc, current_Msg, next_Msg);
                                                    }
                                                    else
                                                    {
                                                        Apply_Type_COMMA_RULE2(doc, current_Msg,next_Msg);						
                                                    }
                                                }
                                            }// else if current Msg is not a IS_A message

                                            current_Msg_copy = Msg_list_copy.get(i + j);
                                            //logger.debug(XmlMsgs.getStringDescription((Node)Msg_list_copy.get(i + j),true));
                                            j++;                                                
                                        }//start_REF.compareTo(current_REF) ==0
                                        else
                                        {// start_REF != current_REF
                                            int index_of_last_available_msg_for_aggr = countSameSubjectFacts(current_REF, Msg_list, i + j ) - 1; // new line

                                            if (/*index_of_last_available_msg_for_aggr == 0 &&*/ pre_VAL.compareTo(current_REF)==0) 
                                            {
                                                if(XmlMsgs.getNodeChild(current_Msg_copy, XmlMsgs.prefix, XmlMsgs.IS_A_TAG) !=null
                                                    &&XmlMsgs.getNodeChild(next_Msg_copy, XmlMsgs.prefix, XmlMsgs.IS_A_TAG) !=null)
                                                {
                                                    apply_Akindof_Rule(doc,current_Msg, next_Msg);
                                                    Aggr_Msg_counter++;
                                                    j++;
                                                }
                                                else if(index_of_last_available_msg_for_aggr == 0)
                                                {
                                                    apply_Relative_Pronoun(doc,current_Msg, next_Msg);
                                                    Aggr_Msg_counter++;
                                                    j++;
                                                }

                                                subject_changed = true; // new line
                                                breaking = true; // comparisons

                                            }
                                            else
                                            {
                                                subject_changed = true; // new line
                                                breaking = true; // comparisons
                                            }
                                            
                                        }// start_REF != current_REF

                                        pre_VAL = current_VAL; // new line
                                    }//Aggregation 2
                                    else
                                    {
                                        found_msg_not_Agg_msg = true;
                                        //breaking = true; // comparisons
                                    }

                                }//j <= MPFS - 1 && i + j < Msg_list.size()
                    
                                root.appendChild(current_Msg);
                    
                                if(found_msg_not_Agg_msg)
                                {
                                    //if((i + j + 1) <= Msg_list.size())// 14/2/2008
                                    //{
                                        root.appendChild(Msg_list.get(i + j));
                                        found_msg_not_Agg_msg = false; // to vale Giorgos. Einai swsto?
                                        i = i + j;
                                        if (j<MPFS)
                                            breaking = true;
                                    //}
                                }
                    
                                if (subject_changed)
                                {
                                    //if((i + j + 1) <= Msg_list.size())// 14/2/2008
                                    //{
                                        root.appendChild(Msg_list.get(i + j));
                                        subject_changed = false; // to vale Giorgos. Einai swsto?
                                        i = i + j;
                                        if (j<MPFS)
                                            breaking = true;
                                    //}
                                }                    
                            } // @@@@@@
                            else 
                            {
                                breaking = true;
                                i++;
                                root.appendChild(current_Msg);
                            }
                    }
                        else
                        {
                            breaking = true;
                            root.appendChild(current_Msg);
                            i++;
                        }
           // }
                    }//while j <= MPFS - 1 && i + j < Msg_list.size()  -----------> else
                    
                    //root.appendChild(current_Msg);
                    //if(found_msg_not_Agg_msg)
                    //{
                    //    root.appendChild(Msg_list.get(i + j));
                    //}
                    /////////////////////???????????????????????////////////////////////////////
                    /*
                    if(found_msg_not_Agg_msg)
                    {
                        root.appendChild(Msg_list.get(i + j));
                    }
                    */
                    //i = i + Aggr_Msg_counter +1; // new line (changed) Aggr_Msg_counter+1 <-- MFPS
                    if (!breaking)
                        // den jerw edw ti gientai...
                        //i = i + Aggr_Msg_counter +1; // new line (changed) Aggr_Msg_counter+1 <-- MFPS
                        i = i + j;
                    //    else
                    //    {
                    //if (subject_changed)
                    //    i++;
                    //subject_changed = false;
                    //    }

                }//Aggregation 1
                else
                {
                    root.appendChild(current_Msg);
                    i = i + 1;
                }
                
            }// apply the other aggregation rules !!!!!!!!!!!!!!!!!!
	}

        private int countSameSubjectFacts(String ref, Vector<Node> Msg_list, int index)
        {
            int counter = 0;
            for(int i = index; i < Msg_list.size(); i++)
            {
                Node current = Msg_list.get(i);
                if(XmlMsgs.getAttribute(current, XmlMsgs.prefix, XmlMsgs.REF).compareTo(ref)!=0)
                {// if <current's node ref> != <ref>
                    return counter;
                }
                else
                {
                    logger.debug("same :" + ref + XmlMsgs.getAttribute(current, XmlMsgs.prefix, XmlMsgs.REF).compareTo(ref));
                    counter++; // if <current's node ref> == <ref>
                }
            }
            
            return counter;
        }
        
	//-----------------------------------------------------------------------------------
	private int getType(Node curr_slot)
        {
            if(curr_slot.getNodeName().compareTo("verb")==0)
                return 1;
            else if(curr_slot.getNodeName().compareTo("IS_A")==0)
                return 2;
            else if(curr_slot.getNodeName().compareTo("Owner")==0)
                return 3;
            else
                return 4;		
	}
	
        // comparisons
        public void Apply_Type_COMMA_RULE3(Document doc, Node first, Node second){
        //logger.debug("Apply_Simple_Conjuction_RULE");
        
        Element TEXT = createTextNode(doc);
        
                /*if(Languages.isGreek(getLanguage()))
                        TEXT.setTextContent(GREEK_CONNECTIVE);
                else if(Languages.isEnglish(getLanguage()))
                        TEXT.setTextContent(ENGLISH_CONNECTIVE);
                 */
        //TEXT.setTextContent(", ");
        //first.appendChild(TEXT);
        
        
        //Node removed_Msg = second.getParentNode().removeChild(second);
        
        Element COMMA = doc.createElement("COMMA");
        first.appendChild(COMMA);
        
        if(Languages.isGreek(getLanguage()))
        {
        
            Vector Msgs_Slots = XmlMsgs.ReturnChildNodes(second);
            
            for(int k = 0; k < Msgs_Slots.size(); k ++)
            {
                Node current_slot =(Node)Msgs_Slots.get(k);
                if (current_slot.getLocalName()!=null&&!(current_slot.getLocalName().equalsIgnoreCase("owner") || current_slot.getLocalName().equalsIgnoreCase("comparator")))
                first.appendChild(current_slot);
            }//for
        }
        else
        {
            Vector Msgs_Slots = XmlMsgs.ReturnChildNodes(second);
            
            for(int k = 0; k < Msgs_Slots.size(); k ++)
            {
                Node current_slot =(Node)Msgs_Slots.get(k);
                
                if (current_slot.getLocalName()!=null&&!(current_slot.getLocalName().equalsIgnoreCase("comparator")))
                first.appendChild(current_slot);
            }
        }
    }
    
    public void addCommmonFeatureText(Document doc, Node first)
    {
        logger.debug("addCommmonFeatureText");
        
        Element TEXT = createTextNode(doc);
        
        if(Languages.isGreek(getLanguage()))
            TEXT.setTextContent("εχει τα εξής χαρακτηριστικά::");
        else if(Languages.isEnglish(getLanguage()))
            TEXT.setTextContent(";");
                        
        //Node removed_Msg = second.getParentNode().removeChild(second);
        
        Vector Msgs_Slots = XmlMsgs.ReturnChildNodes(first);
        for(int k = 0; k < Msgs_Slots.size(); k ++){
            Node current_slot =(Node)Msgs_Slots.get(k);
            if (current_slot.getLocalName().equalsIgnoreCase("owner"))
            {
                
                first.insertBefore(TEXT,(Node)Msgs_Slots.get(k+1));
                break;
            }
        }//for
        
    }
    //-----------------------------------------------------------------------------------
    public void Apply_Simple_Conjuction_RULE2(Document doc, Node first, Node second){
        logger.debug("Apply_Simple_Conjuction_RULE");
        
        Element TEXT = createTextNode(doc);
        
        if(Languages.isGreek(getLanguage()))
            TEXT.setTextContent(GREEK_CONNECTIVE);
        else if(Languages.isEnglish(getLanguage()))
            TEXT.setTextContent(ENGLISH_CONNECTIVE);
        
        first.appendChild(TEXT);
        
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //!!!!!!!!kapoy edw 8a prepei na bazw ton owner
        
        //Node removed_Msg = second.getParentNode().removeChild(second);
        
        if(Languages.isGreek(getLanguage()))
        {
            boolean once =true;
            boolean put = false;
            Element aytos = createAytosNode(doc);
            aytos.setTextContent("http://www.aueb.gr/users/ion/mpiro.owl#aytos");
        
            Vector Msgs_Slots = XmlMsgs.ReturnChildNodes(second);
            for(int k = 0; k < Msgs_Slots.size(); k ++)
            {
                Node current_slot =(Node)Msgs_Slots.get(k);
                if (!(current_slot.getLocalName().equalsIgnoreCase("owner") || current_slot.getLocalName().equalsIgnoreCase("comparator")))
                {
                    first.appendChild(current_slot);
                    if (put && current_slot.getLocalName().equalsIgnoreCase("Verb"))
                    {
                        first.appendChild(aytos);
                        put = false;
                    }
                }
                else
                {
                    if (once)
                    {
                        once = false;
                        put = true;
                    }
                }     
            }
        }
        else
        {
            Vector Msgs_Slots = XmlMsgs.ReturnChildNodes(second);
            for(int k = 0; k < Msgs_Slots.size(); k ++)
            {
                
            Node current_slot =(Node)Msgs_Slots.get(k);
            if (!(current_slot.getLocalName().equalsIgnoreCase("comparator")))
                first.appendChild(current_slot);
            }
        }        
    }        
	//-----------------------------------------------------------------------------------	
	public void Apply_Type_COMMA_RULE(Document doc, Node IS_A, Node PASS_VERB_MSG) 
        {
		logger.debug("Apply_Type_COMMA_RULE");
		
		                
                if(Languages.isEnglish(getLanguage()))
                {
                    Element COMMA = doc.createElement("COMMA");
                    IS_A.appendChild(COMMA);
                }
		
                
                // if greek
		if(Languages.isGreek(getLanguage()))
                {
                    String ref = XmlMsgs.getAttribute(IS_A, XmlMsgs.prefix, "Val");
                    Element TEXT = createTextNode(doc);

                    if(Math.random() >= 0.5)
                    {
                        Vector slots = XmlMsgs.ReturnChildNodes(IS_A); // return all slots

                        for(int i = 0; i < slots.size(); i++)
                        {
                                Node slt = (Node)slots.get(i);
                                //find the filler of IS_A msg
                                if(XmlMsgs.compare(slt,XmlMsgs.prefix,XmlMsgs.FILLER_TAG))
                                {
                                    try
                                    {
                                       
                                        Object obj = Lexicon.getEntry(slt.getTextContent(), getLanguage(), null);
                                        
                                        if(obj instanceof Lex_Entry_GR)
                                        {
                                            Lex_Entry_GR lex_entry_gr = (Lex_Entry_GR)obj;

                                            if(lex_entry_gr !=null)
                                            {
                                                String Gen = lex_entry_gr.get_Gender();

                                                if(Gen.compareTo(XmlMsgs.GENDER_MASCULINE)==0)
                                                {
                                                        TEXT.setAttribute("owlnl:role", XmlMsgs.OWNER_TAG);
                                                        TEXT.setAttribute("owlnl:ref", ref);
                                                        TEXT.setTextContent(GREEK_MASCULINE_RELATIVE_PRONOUN);
                                                }
                                                else if(Gen.compareTo(XmlMsgs.GENDER_FEMININE)==0)
                                                {
                                                        TEXT.setAttribute("owlnl:role", XmlMsgs.OWNER_TAG);
                                                        TEXT.setAttribute("owlnl:ref", ref);
                                                        TEXT.setTextContent(GREEK_FEMININE_RELATIVE_PRONOUN);
                                                }
                                                else if(Gen.compareTo("neuter")==0)
                                                {
                                                        TEXT.setAttribute("owlnl:role", XmlMsgs.OWNER_TAG);
                                                        TEXT.setAttribute("owlnl:ref", ref);
                                                        TEXT.setTextContent(GREEK_NEUTER_RELATIVE_PRONOUN);
                                                }
                                            }
                                            else
                                            {
                                                TEXT.setAttribute("owlnl:role", XmlMsgs.OWNER_TAG);
                                                TEXT.setAttribute("owlnl:ref", ref);
                                                TEXT.setTextContent(GREEK_GENDER_INSENSITIVE_PRONOUN);    
                                            }
                                            
                                        }
                                        else if(obj instanceof CannedList)
                                        {
                                            TEXT.setAttribute("owlnl:role", XmlMsgs.OWNER_TAG);
                                            TEXT.setAttribute("owlnl:ref", ref);
                                            TEXT.setTextContent(GREEK_GENDER_INSENSITIVE_PRONOUN);
                                        }
                                    }
                                    catch(Exception e)
                                    {
                                        e.printStackTrace();
                                    }
                                }
                        }
                    }
                    else
                    {
                            TEXT.setAttribute("owlnl:role", XmlMsgs.OWNER_TAG);
                            TEXT.setAttribute("owlnl:ref", ref);
                            TEXT.setTextContent(GREEK_GENDER_INSENSITIVE_PRONOUN);
                    }

                    IS_A.appendChild(TEXT);
		}//// if greek
		
		//Node verb = XmlMsgs.getNodeChild(PASS_VERB_MSG,XmlMsgs.VERB_TAG);
					
		boolean FOUND = false;
		Vector Msgs_Slots = XmlMsgs.ReturnChildNodes(PASS_VERB_MSG);
                
		for(int k = 0; k < Msgs_Slots.size(); k ++)
                {
			Node current_slot =(Node)Msgs_Slots.get(k);
                                                
			if(XmlMsgs.compare(current_slot , XmlMsgs.prefix, XmlMsgs.VERB_TAG)){
				if(Languages.isEnglish(getLanguage()))
				current_slot = remove_auxiliary_verb(current_slot);				
				FOUND = true;
			}//if
			
			if(FOUND)
			IS_A.appendChild(current_slot);
		}//for
			

	}//type comma rule
        //-----------------------------------------------------------------------------------        
	public void Apply_IS_A_before_ACTIVE_VERB_RULE(Document doc, Node IS_A, Node ACTIVE_VERB_MSG){
		logger.debug("Apply_IS_A_before_ACTIVE_VERB_RULE");
		
		Element CONNECTOR = createTextNode(doc);
                
                if(Languages.isGreek(getLanguage()))
                {
                    CONNECTOR.setTextContent("που");                    
                }
                else if (Languages.isEnglish(getLanguage()))
                {
                    CONNECTOR.setTextContent("that");
                }
                
		IS_A.appendChild(CONNECTOR);
		
                CONNECTOR.setAttribute("owlnl:role" , XmlMsgs.OWNER_TAG);
                String ref = XmlMsgs.getAttribute(IS_A, XmlMsgs.prefix, "Val");
                CONNECTOR.setAttribute("owlnl:ref",ref);
                
		boolean FOUND = false;
		Vector Msgs_Slots = XmlMsgs.ReturnChildNodes(ACTIVE_VERB_MSG);
                
		for(int k = 0; k < Msgs_Slots.size(); k ++)
                {
			Node current_slot =(Node)Msgs_Slots.get(k);
                        
			if(XmlMsgs.compare(current_slot, XmlMsgs.prefix, XmlMsgs.VERB_TAG))
                        {
				FOUND = true;
			}//if
			
			if(FOUND)
                            IS_A.appendChild(current_slot);                        
                            
		}//for
			

	}//       
	//-----------------------------------------------------------------------------------	
	public Node remove_auxiliary_verb(Node verb)
        {
		
		String verb_str1 = XmlMsgs.getAttribute(verb, XmlMsgs.prefix, XmlMsgs.singular_VERB_TAG);                
                		
                
		if(verb_str1.startsWith("is "))
                {
			verb_str1 = verb_str1.substring(3,verb_str1.length());
			((Element)verb).setAttribute("owlnl:" + XmlMsgs.singular_VERB_TAG, verb_str1);
		}
		else if(verb_str1.startsWith("was "))
                {
			verb_str1 = verb_str1.substring(4,verb_str1.length());
			((Element)verb).setAttribute("owlnl:" + XmlMsgs.singular_VERB_TAG, verb_str1);
		}

                
		String verb_str2 = XmlMsgs.getAttribute(verb, XmlMsgs.prefix, XmlMsgs.plural_VERB_TAG);                
                		
                
		if(verb_str2.startsWith("is "))
                {
			verb_str2 = verb_str2.substring(3,verb_str1.length());
			((Element)verb).setAttribute("owlnl:" + XmlMsgs.plural_VERB_TAG, verb_str2);
		}
		else if(verb_str1.startsWith("was "))
                {
			verb_str2 = verb_str2.substring(4,verb_str1.length());
			((Element)verb).setAttribute("owlnl:" + XmlMsgs.plural_VERB_TAG, verb_str2);
		}
		return verb;
	}
	//-----------------------------------------------------------------------------------	
        public void Apply_IS_A_Agg_Rule(Document doc,Node current_Msg,Node next_Msg, boolean last){
                logger.debug("Apply_IS_A_Agg_Rule");
                Element TEXT = createTextNode(doc);		

                if(last){
                     if(Languages.isGreek(getLanguage())){
                        TEXT.setTextContent(this.GREEK_CONNECTIVE);
                     }
                     else if(Languages.isEnglish(getLanguage())){
                         TEXT.setTextContent(this.ENGLISH_CONNECTIVE);
                     }                                         
                }
                else
                    TEXT.setTextContent(", ");
                
                current_Msg.appendChild(TEXT);
                boolean FOUND = false;
                Vector Msgs_Slots = XmlMsgs.ReturnChildNodes(next_Msg);
                for(int k = 0; k < Msgs_Slots.size(); k ++){
                        Node current_slot =(Node)Msgs_Slots.get(k);

                        if(FOUND)
                        current_Msg.appendChild(current_slot);
                        
                        if(XmlMsgs.compare(current_slot,XmlMsgs.prefix, XmlMsgs.IS_A_TAG)){
                                FOUND = true;
                                Element A_OR_AN = doc.createElement(XmlMsgs.A_TAG);
                                current_Msg.appendChild(A_OR_AN);  
                        }//if
                        

                }//for			

        }
	//-----------------------------------------------------------------------------------	
	public void Apply_Type_COMMA_RULE2(Document doc, Node first, Node second)
        {
		logger.debug("Apply_Type_COMMA_RULE2");
		Element COMMA = doc.createElement("COMMA");
		first.appendChild(COMMA);
			
		//Node removed_Msg = second.getParentNode().removeChild(second);
					
		Vector Msgs_Slots = XmlMsgs.ReturnChildNodes(second);
                
		for(int k = 0; k < Msgs_Slots.size(); k ++)
                {
			Node current_slot =(Node)Msgs_Slots.get(k);				
			first.appendChild(current_slot);
		}//for
			
		
	}//type comma rule	
	//-----------------------------------------------------------------------------------
	public void Apply_Simple_Conjuction_RULE(Document doc, Node first, Node second){
		logger.debug("Apply_Simple_Conjuction_RULE");
		
		Element TEXT = createTextNode(doc);
		
		if(Languages.isGreek(getLanguage()))
			TEXT.setTextContent(GREEK_CONNECTIVE);
		else if(Languages.isEnglish(getLanguage()))
			TEXT.setTextContent(ENGLISH_CONNECTIVE);
		
		first.appendChild(TEXT);	
		
		//Node removed_Msg = second.getParentNode().removeChild(second);
		
		Vector Msgs_Slots = XmlMsgs.ReturnChildNodes(second);
                for(int k = 0; k < Msgs_Slots.size(); k ++){
                        Node current_slot =(Node)Msgs_Slots.get(k);
                        first.appendChild(current_slot);
                }//for	
		
	}//type comma rule
        //-----------------------------------------------------------------------------------
        public void apply_Relative_Pronoun(Document doc, Node first, Node second)
        {
            logger.debug("apply_Relative_Pronoun");
		
            String ref = XmlMsgs.getAttribute(first, XmlMsgs.prefix, "Val"); 
            
            Element TEXT = createTextNode(doc);

            if(Languages.isGreek(getLanguage()))
            {
                    TEXT.setAttribute("owlnl:role", XmlMsgs.OWNER_TAG);
                    TEXT.setAttribute("owlnl:ref", ref);
                    TEXT.setTextContent(this.GREEK_GENDER_INSENSITIVE_PRONOUN);
            }
            else if(Languages.isEnglish(getLanguage()))
            {
                    Element TEXT1 = createTextNode(doc);
                    TEXT1.setTextContent(",");
                    first.appendChild(TEXT1);
                    
                    TEXT.setAttribute("owlnl:role", XmlMsgs.OWNER_TAG);
                    TEXT.setAttribute("owlnl:ref", ref);
                    TEXT.setTextContent("which");
            }
            
            first.appendChild(TEXT);	
            
            Vector Msgs_Slots = XmlMsgs.ReturnChildNodes(second);
            
            for(int k = 0; k < Msgs_Slots.size(); k ++)
            {
                    Node current_slot =(Node)Msgs_Slots.get(k);
                    
                    if( !XmlMsgs.compare(current_slot,XmlMsgs.prefix, XmlMsgs.OWNER_TAG))
                    {
                        first.appendChild(current_slot);
                    }
                    else
                    {                        
                        //TEXT.setAttribute("owlnl:role", XmlMsgs.OWNER_TAG);
                        //TEXT.setAttribute("owlnl:ref", ref);
                        current_slot.getParentNode().removeChild(current_slot);
                    }
            }//for	            
		            
        }
        
        public void apply_Akindof_Rule(Document doc, Node first, Node second)
        {
            logger.debug("apply_A_kindof_Rule");
		

            Element TEXT1 = createTextNode(doc);
            Element TEXT2 = createTextNode(doc);

            if(Languages.isGreek(getLanguage()))
            {
                    TEXT1.setTextContent(",");
                    TEXT2.setTextContent("ένα είδος");
            }
            else if(Languages.isEnglish(getLanguage()))
            {
                    TEXT1.setTextContent(",");
                    TEXT2.setTextContent("a kind of");
            }
            
            first.appendChild(TEXT1);	
            first.appendChild(TEXT2);	
            
            Vector Msgs_Slots = XmlMsgs.ReturnChildNodes(second);
            
            for(int k = 0; k < Msgs_Slots.size(); k ++)
            {
                    Node current_slot =(Node)Msgs_Slots.get(k);
                    
                    if( XmlMsgs.compare(current_slot, XmlMsgs.prefix, XmlMsgs.FILLER_TAG))
                    {
                        if(Languages.isGreek(getLanguage()))
                        {
                            ((Element)current_slot).setAttribute("owlnl:" + XmlMsgs.CASE_TAG, XmlMsgs.GENITIVE_TAG);
                        }
                        
                        first.appendChild(current_slot);
                    }
                    else
                    {
                        if(XmlMsgs.compare(current_slot, XmlMsgs.prefix, XmlMsgs.OWNER_TAG))
                        {
                            Element r_owner = doc.createElementNS(XmlMsgs.owlnlNS, XmlMsgs.prefix + ":" + "ROWNER");                        
                            String ref = XmlMsgs.getAttribute(first, XmlMsgs.prefix, "Val"); 
                            r_owner.setAttribute("owlnl:ref", ref);
                            r_owner.setAttribute("owlnl:role", XmlMsgs.OWNER_TAG);                        
                            TEXT2.getParentNode().insertBefore(r_owner, TEXT2);                                                    
                        }
                        
                        // remove "owner" and "is""
                        current_slot.getParentNode().removeChild(current_slot); 


                    }
            }//for	            
		            
        }        
	//-----------------------------------------------------------------------------------
	public void Apply_Type_SEMICOLON_RULE(Document doc, Node first, Node second)
        {
		logger.debug("Apply_Type_SEMICOLON_RULE");
		
		Element TEXT = createTextNode(doc);
		TEXT.setTextContent(";"); // add semicolon
		first.appendChild(TEXT);
		
		//Node removed_Msg = second.getParentNode().removeChild(second);
	
		Vector Msgs_Slots = XmlMsgs.ReturnChildNodes(second);
                
		for(int k = 0; k < Msgs_Slots.size(); k ++)
                {
			Node current_slot =(Node)Msgs_Slots.get(k);
			first.appendChild(current_slot);
		}//for					
	} 
	//-----------------------------------------------------------------------------------
	public boolean syntactic_embedding(Node current_Msg,Node next_Msg){
			if(XmlMsgs.getAttribute(current_Msg,XmlMsgs.prefix ,"property").compareTo(XmlMsgs.getAttribute(next_Msg, XmlMsgs.prefix ,"property"))==0){
				if(XmlMsgs.getAttribute(current_Msg,XmlMsgs.prefix ,"RestrType").compareTo(XmlMsgs.CARDINALITY_RESTRICTION_TAG)==0				
				||XmlMsgs.getAttribute(current_Msg, XmlMsgs.prefix , "RestrType").compareTo(XmlMsgs.MAX_CARDINALITY_RESTRICTION_TAG)==0){
					if(XmlMsgs.getAttribute(next_Msg, XmlMsgs.prefix ,"RestrType").compareTo(XmlMsgs.HAS_VALUE_RESTRICTION_TAG)==0){
						return true;
					}						
				}
			}
			
			return false;
			
	}
	//-----------------------------------------------------------------------------------
	public void apply_syntactic_embedding(Document doc, Node current_Msg,Node next_Msg) 
        {
		
		logger.debug("apply_syntactic_embedding");
		
		Element COMMA = doc.createElement("COMMA");		
		current_Msg.appendChild(COMMA);
		
		if(Languages.isEnglish(getLanguage()))
                {
			
		}
		else if(Languages.isGreek(getLanguage()))
                {
			Vector slots = XmlMsgs.ReturnChildNodes(next_Msg);
			for(int i = 0; i < slots.size(); i++)
                        {
				Node nd = (Node)slots.get(i);
				if(XmlMsgs.compare(nd, XmlMsgs.prefix, XmlMsgs.FILLER_TAG))
                                {
                                        try
                                        {
                                            Object obj = Lexicon.getEntry(nd.getTextContent(), getLanguage(), null);                                         
                                        
                                            if(obj instanceof Lex_Entry_GR)
                                            {
                                                Lex_Entry_GR lex_en_gr = (Lex_Entry_GR)obj;
                                                GreekArticles GA = new GreekArticles();

                                                String article = GA.getArticle(lex_en_gr.get_Gender(),lex_en_gr.get_num(),XmlMsgs.NOMINATIVE_TAG,"ζζζ");
                                                Element artcl = createTextNode(doc);
                                                artcl.setTextContent(article);
                                                current_Msg.appendChild(artcl);
                                            }
                                            else
                                            {

                                            }
                                        }
                                        catch(Exception e)
                                        {
                                            e.printStackTrace();
                                        }
				}
			}
			
			
		}						
		
		Vector Msgs_Slots = XmlMsgs.ReturnChildNodes(next_Msg);
		for(int k = 0; k < Msgs_Slots.size(); k ++){
			Node current_slot =(Node)Msgs_Slots.get(k);
									
			if(XmlMsgs.compare(current_slot,XmlMsgs.prefix,XmlMsgs.FILLER_TAG))
                        {
                                current_Msg.appendChild(current_slot);
			}//if
			
		}//for
			
	}	
	//-----------------------------------------------------------------------------------
	public boolean shared_subject_predicate(Document doc,Node current_Msg,Node next_Msg)
        {
		
		Node verb_1 = get_Verb(current_Msg);
		Node verb_2 = get_Verb(next_Msg);
		
		Node Own1 = get_Owner(current_Msg);
		Node Own2 = get_Owner(next_Msg);

		if(Own1 == null || Own2 ==null)
                    return false;
                
		String case1 = XmlMsgs.getAttribute(Own1, XmlMsgs.prefix ,XmlMsgs.CASE_TAG);
		String case2 = XmlMsgs.getAttribute(Own2, XmlMsgs.prefix ,XmlMsgs.CASE_TAG);
		
		if(verb_1 == null || verb_2 ==null)
                    return false;
		
		if(case1.compareTo(case2)!=0)
                    return false;
		
                logger.debug("verb_1:" + verb_1.getTextContent() + " verb_2:" + verb_2.getTextContent());
                
                int same_text_slots_before_owner = 0;
                if( Own1.getPreviousSibling() != null && Own2.getPreviousSibling()!=null)
                {
                    if (XmlMsgs.compare(Own1.getPreviousSibling(), XmlMsgs.prefix, XmlMsgs.TEXT_TAG)
                    && XmlMsgs.compare(Own2.getPreviousSibling(), XmlMsgs.prefix, XmlMsgs.TEXT_TAG) )
                    {
                        if(Own1.getTextContent().equals(Own2.getTextContent()))
                            same_text_slots_before_owner= 1;
                        else
                            same_text_slots_before_owner = -1;
                    }
                }
                
		if(XmlMsgs.getAttribute(verb_1, XmlMsgs.prefix, XmlMsgs.singular_VERB_TAG)
                    .compareTo(XmlMsgs.getAttribute(verb_2, XmlMsgs.prefix, XmlMsgs.singular_VERB_TAG))==0)
                {
                    if(same_text_slots_before_owner == 0)
                        return true;
                    else 
                    {
                        if (same_text_slots_before_owner == 1)
                            return true;
                        else
                            return false;
                    }
                }
		else
                {
                    return false;
                }
	}
	//-----------------------------------------------------------------------------------
	public void Apply_shared_subject_predicate_RULE(Document doc, Node first, Node second, boolean last)
        {
		logger.debug("shared_subject_predicate");
                Node first_verb = XmlMsgs.getNodeChild(first, XmlMsgs.prefix, XmlMsgs.VERB_TAG);
                Node sec_verb = XmlMsgs.getNodeChild(second, XmlMsgs.prefix, XmlMsgs.VERB_TAG);

                String voice1 = XmlMsgs.getAttribute(first_verb,XmlMsgs.prefix,XmlMsgs.VOICE_TAG);
                String voice2 = XmlMsgs.getAttribute(sec_verb,XmlMsgs.prefix,XmlMsgs.VOICE_TAG); 
                
                //if(!(voice1.compareTo(voice2)==0 && voice1.compareTo(XmlMsgs.PASSIVE_VOICE)==0))
                if(last)
                {
                    Element TEXT = createTextNode(doc);
                   
                    if(Languages.isGreek(getLanguage()))
                            TEXT.setTextContent(GREEK_CONNECTIVE);
                    else if(Languages.isEnglish(getLanguage()))
                            TEXT.setTextContent(ENGLISH_CONNECTIVE);

                    first.appendChild(TEXT);		
                }
		//Node removed_Msg = second.getParentNode().removeChild(second);
		
		boolean FOUND = false;
		Vector Msgs_Slots = XmlMsgs.ReturnChildNodes(second);
		for(int k = 0; k < Msgs_Slots.size(); k ++)
                {
			Node current_slot =(Node)Msgs_Slots.get(k);
			
			if(FOUND)
			first.appendChild(current_slot);
			
			if(XmlMsgs.compare(current_slot, XmlMsgs.prefix, XmlMsgs.VERB_TAG))
                        {
				FOUND = true;
			}//if
		
		}//for
				
	}
	//-----------------------------------------------------------------------------------
	public Node get_Verb(Node Msg){
		Vector Msgs_Slots = XmlMsgs.ReturnChildNodes(Msg);
		for(int k = 0; k < Msgs_Slots.size(); k ++){
			Node current_slot =(Node)Msgs_Slots.get(k);
                        
			if( XmlMsgs.compare(current_slot, XmlMsgs.prefix, XmlMsgs.VERB_TAG)){
				return current_slot;
			}//if
		}//for
		
		return null;		
	}//get_Verb
	//-----------------------------------------------------------------------------------
	public Node get_Owner(Node Msg){
		Vector Msgs_Slots = XmlMsgs.ReturnChildNodes(Msg);
		for(int k = 0; k < Msgs_Slots.size(); k ++){
			Node current_slot =(Node)Msgs_Slots.get(k);
                        
                        if(XmlMsgs.compare(current_slot, XmlMsgs.prefix, XmlMsgs.OWNER_TAG))
			{
				return current_slot;
			}//if
		}//for
		
		return null;		
	}//get_Owner
	//-----------------------------------------------------------------------------------
	public Node get_Filler(Node Msg){
		Vector Msgs_Slots = XmlMsgs.ReturnChildNodes(Msg);
		for(int k = 0; k < Msgs_Slots.size(); k ++){
			Node current_slot =(Node)Msgs_Slots.get(k);
			if(XmlMsgs.compare(current_slot, XmlMsgs.prefix, XmlMsgs.FILLER_TAG)){
				return current_slot;
			}//if
		}//for
		
		return null;		
	}//get_Verb	
	//-----------------------------------------------------------------------------------	
	public Node ancestor(Node nd){
				
		Node preNode = nd;
		Node currNode = nd.getParentNode();
		
		while(currNode.getNodeName().compareTo("owlMsgs")!=0){
			preNode = currNode;
			currNode = currNode.getParentNode();
			logger.debug(preNode.getNodeName() + " , " + currNode.getNodeName());
		}
		
		return preNode;
	}
	//-----------------------------------------------------------------------------------	
	public void Agreggate_enumerations(Document doc){
		
		String disjunction="";
		if(Languages.isEnglish(getLanguage())){
			disjunction = this.ENGLISH_DISJUNCTION;
		}
		else if(Languages.isGreek(getLanguage())){
			disjunction = this.GREEK_DISJUNCTION;
		}
		
		
		NodeList EnumList = doc.getElementsByTagName(XmlMsgs.ENUMERATION_OF_TAG);
		Vector<Node> enums = new Vector<Node>();
		
		for(int i = 0; i < EnumList.getLength(); i++)// for each enumeration
			enums.add(EnumList.item(i));
			
		for(int i = 0; i < enums.size(); i++){// for each enumeration
			Node enum_item = enums.get(i);
			Vector slots =  XmlMsgs.ReturnChildNodes(enum_item);// 
			Vector<Node> individuals = new Vector<Node>();
			
			
			for(int j = 0; j < slots.size(); j++){
				Node slot = (Node)slots.get(j);
				
				if(slot.getNodeName().compareTo("COMMA")!=0 && slot.getTextContent().compareTo(disjunction)!=0){
					individuals.add(slot);
				}
			}
			
			int min = 100;
			for(int j = 0; j < individuals.size()-1; j++){
				Node slot1 = individuals.get(j);
				Node slot2 = individuals.get(j+1);
				int a = f(slot1,slot2);
				if(a < min)
				min = a;
			}//for
			
			logger.debug("min:" + min);
			Node next = enum_item.getNextSibling();
			
			for(int j = 0; j < slots.size(); j++){
				Node slot = (Node)slots.get(j);				
				
				if(slot.getNodeName().compareTo("COMMA")!=0 && slot.getTextContent().compareTo("or")!=0){
					
					if(j!=slots.size()-1){					
						StringTokenizer ST = new StringTokenizer(slot.getTextContent());
						Stack<String> st1 = new Stack<String>();
						
						while(ST.hasMoreTokens()){							
							st1.push(ST.nextToken());
						}//while
						
						String val = "";
						int c = 0;
						
						boolean first = true;
						while(!st1.empty()){
							if(c >= min){
								
								
								if(first){
									val = st1.pop();
									first = false;
								}
								else
								val = st1.pop() + " "  + val;
							}
							else
							{
								st1.pop();							
							}
							c++;
						}
						
						slot.setTextContent(val);
					
					}				
					
					if(next==null)
					enum_item.getParentNode().appendChild(slot);
					else
					enum_item.getParentNode().insertBefore(slot,next);
				}
				else{
					
					if(next==null)
					enum_item.getParentNode().appendChild(slot);
					else
					enum_item.getParentNode().insertBefore(slot,next);
				}
					
			}//for
		}//for
	}
	//-----------------------------------------------------------------------------------
	public void Agreggate_allValues_and_someValues(Document doc){
		NodeList Froms = doc.getElementsByTagName("From");
		Vector<Node> Froms_List = new Vector<Node>();
		
		for(int i = 0; i < Froms.getLength(); i++)// for each from
			Froms_List.add(Froms.item(i));
			
		for(int i = 0; i < Froms_List.size(); i++){// for each from
                    
                }
        }
	
	//-----------------------------------------------------------------------------------
	public void Agreggate_Unions(Document doc){
		NodeList UNIONS = doc.getElementsByTagName(XmlMsgs.UNION_OF_TAG);
		Vector<Node> UNIONS_List = new Vector<Node>();
		
		for(int i = 0; i < UNIONS.getLength(); i++)// for each union
			UNIONS_List.add(UNIONS.item(i));
			
		for(int i = 0; i < UNIONS_List.size(); i++){// for each union
			Node UNION_item = UNIONS_List.get(i);			
			
			if(UNION_item.getParentNode().getNodeName().compareTo("From")==0){ // 
				Node From = UNION_item.getParentNode();
				Node Msg = From.getParentNode();
				
				From = From.getParentNode().removeChild(From);	
								
				Vector<Node> after_verb = new Vector<Node>();
				after_verb= copy_after_verb(Msg,false,false);
				
				Vector Msgs = XmlMsgs.ReturnChildNodes(UNION_item);
				
				for (int j = 0; j  < Msgs.size(); j++){	
						
					logger.debug("------->" + j);
					
					Node curr_Msg = (Node)Msgs.get(j);				
					if(j==0){
						
						Vector<Node> after_v = new Vector<Node>();
						after_v = copy_after_verb(curr_Msg,false,false);
						append(Msg, after_v);
					}
					else{

						append(Msg, copy_vector(after_verb));
						
						Vector<Node> after_v = new Vector<Node>();
						after_v = copy_after_verb(curr_Msg,false,false);
						append(Msg, after_v);
					}
					
					if(j != Msgs.size()-1){
						Node TEXT = createTextNode(doc);
                                                
                                                if(Languages.isEnglish(getLanguage()))
                                                    TEXT.setTextContent("or");
                                                else if(Languages.isGreek(getLanguage()))
                                                    TEXT.setTextContent("ή");
                                                
						Msg.appendChild(TEXT);
						
					}
					//curr_Msg.getParentNode().removeChild(curr_Msg);
				}
				
				
			}//if
			
		}//for
	}
	//-----------------------------------------------------------------------------------
	public void Agreggate_Intersections(Document doc){
		NodeList Intersections = doc.getElementsByTagName(XmlMsgs.INTERSECTION_OF_TAG);
		Vector<Node> Intersections_List = new Vector<Node>();
		
		for(int i = 0; i < Intersections.getLength(); i++)// for each intersection
			Intersections_List.add(Intersections.item(i));
			
		for(int i = 0; i < Intersections_List.size(); i++){// for each intersection
			Node Intersections_item = Intersections_List.get(i);			
			
                        //if intersection is child of a From tag
			if(Intersections_item.getParentNode().getNodeName().compareTo("From")==0){
                                                            
				Node From = Intersections_item.getParentNode();				
				Vector<Node> Msgs = XmlMsgs.ReturnChildNodes(Intersections_item);
				                                
                                Aggregate(doc, Msgs , From, true);
				Intersections_item.getParentNode().removeChild(Intersections_item);
                                
                                
                                if(At_Least_an_IS_A_Msg(From)){
                                    logger.debug("At_Least_an_IS_A_Msg");
                                    
                                    Node FromMsg = From.getParentNode();
                                    
                                    if(XmlMsgs.getAttribute(FromMsg,XmlMsgs.prefix , "RestrType").compareTo(XmlMsgs.SOME_VALUES_FROM_RESTRICTION_TAG)==0){
                                        Vector<Node> FromMsgs = XmlMsgs.ReturnChildNodes(From);
                                        
                                        for(int j = 0; j < FromMsgs.size(); j++){
                                            Node FromMsgItem = FromMsgs.get(j); // get From Msg
                                            
                                            Vector<Node> FromMsgSlots = XmlMsgs.ReturnChildNodes(FromMsgItem);
                                            
                                            boolean FOUND = false;
                                            for(int k = 0; k < FromMsgSlots.size(); k++){
                                                Node FromMsgSlot = FromMsgSlots.get(k);
                                                
                                                if(!FOUND){
                                                    FromMsgSlot.getParentNode().removeChild(FromMsgSlot);
                                                   
                                                }
                                                
                                                if(FromMsgSlot.getNodeName().compareTo(XmlMsgs.IS_A_TAG)==0){
                                                    FOUND = true;
                                                }
                                                
                                            }
                                            
                                        }
                                        
                                    }
                                    else if(XmlMsgs.getAttribute(FromMsg, XmlMsgs.prefix ,"RestrType").compareTo(XmlMsgs.ALL_VALUES_FROM_RESTRICTION_TAG)==0){
                                        
                                    }
                                    else{
                                        // something bad happened
                                    }
                                }
                                else{ //
                                    Node FromMsg = From.getParentNode();
                                    
                                    if(XmlMsgs.getAttribute(FromMsg, XmlMsgs.prefix ,"RestrType").compareTo(XmlMsgs.SOME_VALUES_FROM_RESTRICTION_TAG)==0){

                                    }
                                    else if(XmlMsgs.getAttribute(FromMsg,XmlMsgs.prefix , "RestrType").compareTo(XmlMsgs.ALL_VALUES_FROM_RESTRICTION_TAG)==0){

                                    }
                                    else{
                                        // something bad happened
                                    }                                       
                                }
                                                                                               				
			}//if
			else{// 
									
			}//else
			
		}//for
	}	
	//-----------------------------------------------------------------------------------	
	public Vector<Node> copy_after_verb(Node Msg, boolean stop, boolean copy_verb ){
		Vector slots = XmlMsgs.ReturnChildNodes(Msg);
		
		Vector<Node> retVector = new Vector<Node>();
		
		boolean found = false;
		for(int i = 0; i < slots.size(); i++){
			Node current = (Node)slots.get(i);
			
			if(current.getNodeName().compareTo("From")==0 && stop){
				i = slots.size();
			}
			
			
			if(found && i!=slots.size()){
				Node nd = current.cloneNode(true);
				logger.debug("nd-->" + nd.getNodeName() + nd.getTextContent());
				
				retVector.add(nd);
			}
					
			if(current.getNodeName().compareTo(XmlMsgs.VERB_TAG)==0 || current.getNodeName().compareTo(XmlMsgs.IS_A_TAG)==0){
				found = true;
				if(copy_verb){
					Node nd = current.cloneNode(true);
					retVector.add(nd);
				}
			}
		}//for
		

		return retVector;
	}
	//-----------------------------------------------------------------------------------	
	public void insert_before(Node ref, Vector<Node> vec){
		Node parent = ref.getParentNode();
		
		for(int i = vec.size()-1; i >=0 ; i--){
				parent.insertBefore(vec.get(i),ref);
				ref = vec.get(i);
		}//for
			
	}
	//-----------------------------------------------------------------------------------	
	public void append(Node parent, Vector<Node> vec){
				
		for(int i = 0; i < vec.size() ; i++){
				parent.appendChild(vec.get(i));
		}//for
			
	}	
	//-----------------------------------------------------------------------------------	
	public Vector<Node> copy_vector(Vector<Node> vec){
		Vector<Node> copy = new Vector<Node>();
		
		for(int i = 0; i < vec.size() ; i++){
				copy.add(vec.get(i).cloneNode(true));
		}//for
			
		return copy;
	}		
	//-----------------------------------------------------------------------------------	
	public int f(Node slot1, Node slot2){
		StringTokenizer ST = new StringTokenizer(slot1.getTextContent());
		Stack<String> st1 = new Stack<String>();
		while(ST.hasMoreTokens()){
			st1.push(ST.nextToken());
		}//while
		
		StringTokenizer ST2 = new StringTokenizer(slot2.getTextContent());
		Stack<String> st2 = new Stack<String>();
		while(ST2.hasMoreTokens()){
			st2.push(ST2.nextToken());
		}//while		
		
		int c = 0;
		while(!st1.empty() && !st2.empty()){
			String str1 = st1.pop();
			String str2 = st2.pop();
			if(str1.compareTo(str2)==0){
				logger.debug("!!!!" +str1 + " , " + str2);
				c++;
			}
			else
			return  c;
		}
		return c;
	}
	//-----------------------------------------------------------------------------------
	public boolean AllChilds_IS_A(Node current){
		NodeList childs = current.getChildNodes();
		
		for(int i = 0; i < childs.getLength(); i++){
			if (XmlMsgs.getNodeChild(childs.item(i),XmlMsgs.prefix,XmlMsgs.IS_A_TAG)==null)
			return false;
		}
		//logger.debug("return true");
		return true;
	}	
        //-----------------------------------------------------------------------------------        
        public boolean At_Least_an_IS_A_Msg(Node current){
		NodeList Msgs = current.getChildNodes();
		
		for(int i = 0; i < Msgs.getLength(); i++){
                    Node Msg = Msgs.item(i);
                    NodeList MsgSlots = Msg.getChildNodes();
                    
                    for(int j = 0; j < MsgSlots.getLength(); j++){
                        Node Slot = MsgSlots.item(j);
                        
                        if(Slot.getNodeName().compareTo(XmlMsgs.IS_A_TAG)==0)
			return true;                        
                    }		
		}

		return false;
	}	        
	//-----------------------------------------------------------------------------------
        public void RemoveMsgsFromDoc(Vector<Node> Msg_list){
            // remove Msgs from doc
            for (int i = 0; i < Msg_list.size(); i++){
                    Node Msg = Msg_list.get(i);
                    //Node ancestor_Node = ancestor(Msg);				
                    Node removed_Msg = Msg.getParentNode().removeChild(Msg);
                    //root.appendChild(removed_Msg);					
            }            
        }
        
       public Element createTextNode(Document doc){
           return doc.createElementNS(XmlMsgs.owlnlNS, XmlMsgs.prefix + ":" + XmlMsgs.TEXT_TAG);
       }        
       
       public Element createAytosNode(Document doc){
        return doc.createElementNS(XmlMsgs.owlnlNS, XmlMsgs.prefix + ":" + XmlMsgs.AYTOS_TAG);
    }
}//Aggregation	

