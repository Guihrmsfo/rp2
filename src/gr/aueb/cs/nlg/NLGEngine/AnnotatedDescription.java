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

import org.w3c.dom.*;
import java.util.*;
import gr.aueb.cs.nlg.Utils.*;

import org.apache.log4j.Logger;


public class AnnotatedDescription 
{
    static Logger logger = Logger.getLogger(AnnotatedDescription.class);
            
    private XmlDocumentCreator XmlDocCreator;
    private Document AnnotatedText;
    private Element AnnTExt ; 
    private Element currentPeriod;
    private Element currentSentence;
    
    public static String AnnotText = "AnnotatedText";
    public static String RE = "RE";
    public static String CANNEDTEXT = "CANNEDTEXT";
    public static String RELATIVE_PRONOUN = "RelativePronoun";
    
    public static String forProperty = "forProperty";
    
    public static String TEXT = "TEXT";
    public static String VERB = "VERB";
    
    
    public static String PERIOD = "Period";
    public static String SENTENCE = "Sentence";
    public static String PUNCTUATION = "Punct";
    
    public static String REF = "ref";
    public static String ROLE = "role";
    
    public static String Interest = "Interest";
    public static String Assim = "Assim";
    
    public static String RE_TYPE = "RE_TYPE";
    public static String WSPACE = "WSPACE";
    public static String EmptyRef = "EmptyRef";
    
    public static String Comparator = "Comparator";
    
    public static String TURN = "Turn";
    
    /** Creates a new instance of AnnotatedDescription */
    public AnnotatedDescription() 
    {
        XmlDocCreator = new XmlDocumentCreator();        
    }
    
     
    public Document getXmlDoc()
    {
        logger.debug("Annotated Description!!!");      
                
        return AnnotatedText;
    }
       
    public Node getRoot()
    {
        logger.debug("Annotated Description!!!");      
                
        return this.AnnTExt;
    }
        
    private Node ComparatorSlot;
    private Element ComparatorSlotInAnnotationsTree;
    private String StringCompEntities = "";
    private String TurnToSee = "";

    private boolean InComparator = false;
    
    
    public void setStringCompEntities(String str)
    {
        StringCompEntities = str;
    }
    
    public void setTurnToSee(String str)
    {
        TurnToSee = str;
    }
    
    public void startComparator(Node Slot)
    {
        ComparatorSlot = Slot;
        InComparator = true;
        
         String turn = XmlMsgs.getAttribute(Slot, XmlMsgs.prefix, "ComparatorEntitiesTurn");

         if(turn!=null && !turn.equals(""))
         this.setTurnToSee(turn);                                     
    }
            
    public void finishComparator()
    {
        reformComparator();
        ComparatorSlot = null;
        InComparator = false;
        
    }
    
    public void print()
    {
        logger.debug(XmlMsgs.getStringDescription(AnnTExt, true));
        //this.getXmlDoc();
        logger.debug("Annotated Description");
        System.out.println(XmlMsgs.getStringDescription(AnnTExt, true));
    }
    
    public String getAnnotatedXml()
    {
        return XmlMsgs.getStringDescription(AnnTExt, true);
    }
        
    public void GenerateAnnotatedDescritption()
    {        
        AnnotatedText = XmlDocCreator.getNewDocument();
        AnnTExt = AnnotatedText.createElement(AnnotText);
        AnnotatedText.appendChild(AnnTExt);
        
        add_startPeriod();
        add_startSentence();
    }
    
    public void setEntityId(String id)
    {        
        AnnTExt.setAttribute("ref", id);
    }
    
    
            
    public void add_RE(String text, String ontID, String re_type, String prop, String produced_re, String role,  String interest, String assimScore)
    {
        if(currentSentence!=null)
        {
            make_it_Comparator();
            
            String re_tag = RE;
                                        
            if(produced_re.equals(SurfaceRealization.PROD_RE_NULL))
            {
                re_tag = EmptyRef;
            }
            else
            { 
                re_tag = produced_re;
            }
            
            Element ELEM = AnnotatedText.createElement(re_tag);
            currentSentence.appendChild(ELEM);
            
            ELEM.setAttribute(REF, ontID);
            //ELEM.setAttribute(RE_TYPE, re_type);           
            ELEM.setAttribute(ROLE, role);  
            
            addForProperty(ELEM, prop);                        
            addInterest(ELEM, interest);
            addAssim(ELEM, assimScore);
            
            ELEM.setTextContent(text);   
            
            SlotInComparator(ELEM, prop);
        }        
    }
    
    
    public void add_RE(String ontID, String role)
    {             
        if(currentSentence!=null)
        {
            make_it_Comparator();
            
            String re_tag = EmptyRef;
                        
            Element ELEM = AnnotatedText.createElement(re_tag);
            currentSentence.appendChild(ELEM);
            
            ELEM.setAttribute(REF, ontID);
            //ELEM.setAttribute(RE_TYPE, re_type);           
            ELEM.setAttribute(ROLE, role);  
                        
            ELEM.setTextContent("");                        
        }
        
    }
    
    
    public void add_CANNED_TEXT(String text, String ontID, String prop, String role,String interest, String assimScore)
    {        
        if(currentSentence!=null)
        {
            make_it_Comparator();
            
            Element ELEM = AnnotatedText.createElement(CANNEDTEXT); 
            currentSentence.appendChild(ELEM);
            
            ELEM.setAttribute(REF, ontID);            
            ELEM.setAttribute(ROLE, role);  
            ELEM.setTextContent(text);
 
            addForProperty(ELEM, prop);                        
            addInterest(ELEM, interest);
            addAssim(ELEM, assimScore);
            
            SlotInComparator(ELEM, prop);
        }
    }

    public void add_TEXT(String text)
    {
        add_TEXT( text,  "",  "", "", "",  "", "false", false);
    }
         
    public void add_TEXT(String text, String prop, String role, String ref, String interest, String assimScore, String isPrep)
    {
        add_TEXT( text,  prop, role, ref, interest,  assimScore, isPrep , true);
    }
     
    public void add_TEXT(String text, String prop,String role, String ref, String interest, String assimScore, String isPrep, boolean b)
    {        
        
        if(currentSentence!=null)
        {        
            make_it_Comparator();
                    
            boolean end_of_sentence = false;
            boolean end_of_period = false;                    
            
            if(text.equals("."))
            {
                logger.debug("......................");
                Element Punct = AnnotatedText.createElement(PUNCTUATION);
                Punct.setTextContent(text);

                end_of_sentence = true;
                end_of_period = true;
                
                add_startPeriod(end_of_period, Punct);
                add_startSentence(end_of_sentence);
                
                SlotInComparator(Punct,  prop);
               
            }
            else if(text.equals("and") || text.equals("και"))
            {
                logger.debug("and and and");
                Element TextEl = AnnotatedText.createElement("Text");
                TextEl.setTextContent(text);

                end_of_sentence = true;
                add_startSentence(end_of_sentence, TextEl);
                
                SlotInComparator(TextEl,  prop);
            }
            else if (text.equals(";"))
            {
                logger.debug(";;;;;;;;;;");
                Element Punct = AnnotatedText.createElement(PUNCTUATION);
                Punct.setTextContent(text);                    

                end_of_sentence = true;
                add_startSentence(end_of_sentence, Punct);
                
                SlotInComparator(Punct,  prop);
            }
            else if(text.equals(","))
            {
                logger.debug(",,,,,,,");
                Element Punct = AnnotatedText.createElement(PUNCTUATION);
                Punct.setTextContent(text);                    

                end_of_sentence = true;       
                add_startSentence(end_of_sentence, Punct);
                
                SlotInComparator(Punct,  prop);
                                
            }
            else if(text.equals(Aggregation.GREEK_FEMININE_RELATIVE_PRONOUN) ||
                    text.equals(Aggregation.GREEK_MASCULINE_RELATIVE_PRONOUN) ||
                    text.equals(Aggregation.GREEK_NEUTER_RELATIVE_PRONOUN) ||
                    text.equals(Aggregation.GREEK_GENDER_INSENSITIVE_PRONOUN)
                    )
            {
                logger.debug("GREEK_FEMININE_RELATIVE_PRONOUN | GREEK_MASCULINE_RELATIVE_PRONOUN | GREEK_NEUTER_RELATIVE_PRONOUN | GREEK_GENDER_INSENSITIVE_PRONOUN");
                Element TextEl = AnnotatedText.createElement(RELATIVE_PRONOUN);
                TextEl.setTextContent(text);
                
                TextEl.setAttribute(REF, ref);
                TextEl.setAttribute(ROLE, role);
                        
                end_of_sentence = true;
                
                if(currentSentence.getLastChild()!=null && currentSentence.getLastChild().getTextContent().compareTo(",")==0)
                {
                    
                }
                else
                {
                    add_startSentence(end_of_sentence);
                }                    
                
                currentSentence.appendChild(TextEl);
                
                SlotInComparator(TextEl,  prop);
            }        
            else if (text.equals("which"))
            {
                Element TextEl = AnnotatedText.createElement(RELATIVE_PRONOUN);
                TextEl.setTextContent(text);    
                
                TextEl.setAttribute(REF, ref);
                TextEl.setAttribute(ROLE, role);
                
                currentSentence.appendChild(TextEl);
                
                SlotInComparator(TextEl,  prop);
            }
            else if(text.equals("that") )
            {
                logger.debug("that that that");
                Element TextEl = AnnotatedText.createElement(TEXT);
                TextEl.setTextContent(text);

                currentSentence.appendChild(TextEl);
                
                SlotInComparator(TextEl,  prop);
            }                         
            else
            {
                Element ELEM = AnnotatedText.createElement(TEXT);  
                
                if(isPrep.equals("true"))
                {
                    ELEM = AnnotatedText.createElement("Prep");  
                }
                else
                {
                    ELEM = AnnotatedText.createElement(TEXT);  
                }
                
                
                currentSentence.appendChild(ELEM);
                ELEM.setTextContent(text);            
                
                
                if(b)
                {
                    addForProperty(ELEM, prop);                        
                    addInterest(ELEM, interest);
                    addAssim(ELEM, assimScore);    
                }
                
                SlotInComparator(ELEM,  prop);
            }
            
            
        }
    }
    
    
    public void add_VERB(String text, String prop, String interest, String assimScore)
    {         
        if(currentSentence !=null)
        {  
            make_it_Comparator();
            Element ELEM = AnnotatedText.createElement(VERB);        
            currentSentence.appendChild(ELEM);
            
            ELEM.setTextContent(text);
            
            addForProperty(ELEM, prop);                        
            addInterest(ELEM, interest);
            addAssim(ELEM, assimScore);
            
            SlotInComparator(ELEM,  prop);
        }
    }
        
    public void add_Whitespace()
    {
        if(currentSentence !=null)
        {  
            Element ELEM = AnnotatedText.createElement(WSPACE);                       
            currentSentence.appendChild(ELEM);        
        }
    }
    
    public void add_startPeriod()
    {        
        Element ELEM = AnnotatedText.createElement(AnnotatedDescription.PERIOD);                       
        currentPeriod = ELEM;
        AnnTExt.appendChild(ELEM);   
    }               

    private void add_startPeriod(boolean end_of_period, Element punct )
    {        
        if(end_of_period)
        {
            // create a new period
            Element ELEM = AnnotatedText.createElement(AnnotatedDescription.PERIOD);                       
            
            currentPeriod.appendChild(punct);
            currentPeriod = ELEM;
            
            //AnnTExt.appendChild(connector);
            AnnTExt.appendChild(ELEM);   
        }
    } 
    
    public void add_startSentence()
    {        
        //makeitComparator();
        
        if(!InComparator)
        {
            // create a new sentence tag
            Element SENTENCE_ELEM = AnnotatedText.createElement(AnnotatedDescription.SENTENCE);                       
            // set the new sentence tag as the current sentence
            this.currentSentence = SENTENCE_ELEM;
        
            // add it
            if(currentPeriod != null)
            currentPeriod.appendChild(SENTENCE_ELEM);   
        }

    }      
                   
    private void add_startSentence(boolean end_of_sentence)
    {        
        if(end_of_sentence && !InComparator)
        {

            //makeitComparator();
            
            Element SENTENCE_ELEM = AnnotatedText.createElement(AnnotatedDescription.SENTENCE);                       
            this.currentSentence = SENTENCE_ELEM;
        
            if(currentPeriod != null)
            {                        
                currentPeriod.appendChild(SENTENCE_ELEM);   
            }
        }
    } 
    
    private void add_startSentence(boolean end_of_sentence, Element connector)
    {        
        if(end_of_sentence && !InComparator)
        {
            //boolean isComp = makeitComparator();
            
            Element SENTENCE_ELEM = AnnotatedText.createElement(AnnotatedDescription.SENTENCE);                       
            
            //if(isComp)
            //    this.currentSentence.appendChild(connector);
                        
            this.currentSentence = SENTENCE_ELEM;
        
            if(currentPeriod != null)
            {
                //if(!isComp)
                currentPeriod.appendChild(connector);            
                
                currentPeriod.appendChild(SENTENCE_ELEM);   
            }
        }
        else if(end_of_sentence && InComparator)
        {
            this.currentSentence.appendChild(connector);            
            //Element SENTENCE_ELEM = AnnotatedText.createElement(AnnotatedDescription.SENTENCE);
            //this.currentSentence.appendChild(SENTENCE_ELEM);
            //this.currentSentence                    

        }
    }
    
    
    
    public void make_it_Comparator()
    {
        if(InComparator && currentSentence != null 
           && currentSentence.getNodeName().compareTo(this.SENTENCE)== 0 )
        {
            Element comparator = AnnotatedText.createElement(AnnotatedDescription.Comparator);
            currentSentence.getParentNode().replaceChild(comparator, currentSentence);
            
            currentSentence = comparator;
            ComparatorSlotInAnnotationsTree = comparator;
        }                
    }
    
    
            
    private void SlotInComparator(Node createdSlot, String prop)
    {
        // marks some of the comparator slots with
        // the property they are coming from
        if( InComparator && 
            prop != null &&
            prop.compareTo("")!=0 &&            
            prop.compareTo("http://www.aueb.gr/users/ion/owlnl#Comparator") !=0)
        {
           ((Element)createdSlot).setAttribute( AnnotatedDescription.forProperty, prop);
        }
        
        // if there is a "," immediatelly
        // after comparator put it
        // as last slot inside comparator
        if(!InComparator && createdSlot.getTextContent().compareTo(",") == 0)
        {
            if( createdSlot.getPreviousSibling() != null && createdSlot.getPreviousSibling().getNodeName().compareTo("Comparator")==0)
                createdSlot.getPreviousSibling().appendChild(createdSlot);
        }
    }
    
    // reform comparator
    // when we are done with it.
   
     
    public void reformComparator()
    {        
                        
        if(ComparatorSlotInAnnotationsTree != null)
        {
            // remove unnecessary attributes from comparator tag
            ComparatorSlotInAnnotationsTree.removeAttribute(AnnotatedDescription.Interest);
            ComparatorSlotInAnnotationsTree.removeAttribute(AnnotatedDescription.forProperty);
            ComparatorSlotInAnnotationsTree.removeAttribute(AnnotatedDescription.Assim);
                        
            ComparatorSlotInAnnotationsTree.setAttribute(AnnotatedDescription.REF, StringCompEntities);
            ComparatorSlotInAnnotationsTree.setAttribute(AnnotatedDescription.TURN, this.TurnToSee);
            
            // determine if it is a positive or negative comparison
            Node feat = ComparatorSlot.getAttributes().getNamedItem("owlnl:Feature");

            if(feat != null)
            {
                if(!feat.getTextContent().equals(""))
                {
                    ComparatorSlotInAnnotationsTree.setAttribute("type", "negative");
                }
                else
                {
                    ComparatorSlotInAnnotationsTree.setAttribute("type", "positive");    
                }
            }
            else
            {
                ComparatorSlotInAnnotationsTree.setAttribute("type", "positive");
            }


            // clear roles in some of the comparator slots
            // 

            NodeList CompSlots = ComparatorSlotInAnnotationsTree.getChildNodes();
            for(int i = 0; i < CompSlots.getLength(); i++)
            {
                Node slot = CompSlots.item(i);

                if(slot.getAttributes().getNamedItem(AnnotatedDescription.ROLE) != null
                   && slot.getAttributes().getNamedItem(AnnotatedDescription.forProperty) == null)
                {
                    slot.getAttributes().removeNamedItem(AnnotatedDescription.ROLE);
                }
            }
            

            // group two or more NPs to 1

            CompSlots = ComparatorSlotInAnnotationsTree.getChildNodes();
            Vector<Node> NPs_Delete = new Vector<Node>();
            Vector<Node> CompSlots_VEC = new Vector<Node>();

            int NP_count = 0;
            String NPs_Content="";
            int i = 0;

            for(i = 0; i < CompSlots.getLength(); i++)
            {
                CompSlots_VEC.addElement( CompSlots.item(i) );
            }

            i = 0;
            while( i < CompSlots_VEC.size())
            {
                Node slot = CompSlots_VEC.get(i); 

                //found the first NP
                if(slot.getNodeName().compareTo("NP")==0)
                {
                    NP_count = 1;
                    NPs_Content = slot.getTextContent(); 
                    NPs_Delete.removeAllElements();
                    NPs_Delete.addElement(slot);

                    if(i + 1 < CompSlots.getLength())
                    {
                        i++;
                        slot = CompSlots_VEC.get(i);

                        // find the next NPs
                        while( i < CompSlots.getLength() && CompSlots_VEC.get(i).getNodeName().compareTo("NP")==0)
                        {
                            NP_count++;
                            NPs_Content = NPs_Content + " " + CompSlots_VEC.get(i).getTextContent();
                            NPs_Delete.addElement(CompSlots_VEC.get(i));                                    

                            // go to next  slot
                            i++;

                        }
                    }

                    if(NP_count > 1)
                    {
                        for(int j = 0; j < NPs_Delete.size()-1; j++)
                        {
                            NPs_Delete.get(j).getParentNode().removeChild( NPs_Delete.get(j));
                        }

                        NPs_Delete.get(NPs_Delete.size()-1).setTextContent(NPs_Content);
                    }                            
                }

                i++;
            }// while  
            
            // create a new sentence inside 
            // comparator if necessary
            // there will be at most one sentence in the comparator
            
            CompSlots = ComparatorSlotInAnnotationsTree.getChildNodes();
            CompSlots_VEC = null;
            CompSlots_VEC = new Vector<Node>();
            
            for(i = 0; i < CompSlots.getLength(); i++)
            {
                CompSlots_VEC.addElement( CompSlots.item(i) );
            }
            
            boolean found_owner = false; 
            boolean sentence_created = false;
            
            Node the_comp_sent_slot = null;
                    
            i = 0;
            while( i < CompSlots_VEC.size())
            {
                Node slot = CompSlots_VEC.get(i);
                //String pre_Prop = "";        
                //String current_Prop = "";
                
                if(slot.getAttributes()!= null && slot.getAttributes().getNamedItem(AnnotatedDescription.forProperty) == null || slot.getAttributes().getNamedItem(AnnotatedDescription.forProperty).getNodeName().compareTo("")==0)
                {
                    
                        // do nothing
                }
                else
                {   // create a sentence inside comparator
                    
                    String PropURI = ((Element)slot).getAttribute(AnnotatedDescription.forProperty);
                    sentence_created = true;
                   // create a new sentence tag
                    Element SENTENCE_ELEM = AnnotatedText.createElement(AnnotatedDescription.SENTENCE);                       
                    // set the new sentence tag as the current sentence
                    this.currentSentence = SENTENCE_ELEM;  
                    this.currentSentence.setAttribute(AnnotatedDescription.forProperty, PropURI);
                            
                    while(i < CompSlots_VEC.size())
                    {
                        slot = CompSlots_VEC.get(i);
                        ((Element)slot).removeAttribute(AnnotatedDescription.forProperty);
                        this.currentSentence.appendChild(slot);
                        i++;
                    }
                    
                    ComparatorSlotInAnnotationsTree.appendChild(SENTENCE_ELEM);
                    
                    // we have just added a sentence inside comparator
                    // if this sentence does not contain
                    // an owner then carry the owner 
                    // from the comparator.
                    // the sentence should always have an owner
                    
                    NodeList sentence_slots = SENTENCE_ELEM.getChildNodes();
                                       
                    
                    for(int k = 0; k < sentence_slots.getLength(); k++)
                    {
                        Element sent_slot = ((Element)sentence_slots.item(k));
                        
                        if(sent_slot.getAttribute(AnnotatedDescription.ROLE) != null)
                        {
                            if(sent_slot.getAttribute(AnnotatedDescription.ROLE).compareTo(XmlMsgs.OWNER_TAG)==0)
                            {
                                found_owner = true;
                               
                            }
                        }
                    }
                    
                    the_comp_sent_slot = SENTENCE_ELEM;

                }
                
                i++;
            }// while
            
            if(found_owner)
            {
                // nothing has to be done
            }
            else if(sentence_created)
            {
                CompSlots = ComparatorSlotInAnnotationsTree.getChildNodes();
                CompSlots_VEC = null;
                CompSlots_VEC = new Vector<Node>();

                for(i = 0; i < CompSlots.getLength(); i++)
                {
                    CompSlots_VEC.addElement( CompSlots.item(i) );
                }

                i = 0;
                
                boolean found_NP = false;
                Node node_after_new = null;
                
                while( i < CompSlots_VEC.size())
                {
                    Node slot = CompSlots_VEC.get(i);
                    
                    if(slot.getNodeName().compareTo("NP")==0)
                    {
                        found_NP = true;
                        ((Element)slot).setAttribute(this.ROLE, XmlMsgs.OWNER_TAG);
                        the_comp_sent_slot.insertBefore( slot, the_comp_sent_slot.getFirstChild());
                        node_after_new = slot;
                    }
                    else
                    {
                        if(slot.getNodeName().compareTo(this.SENTENCE)!=0 && found_NP)
                        {
                            the_comp_sent_slot.insertBefore(slot, node_after_new);
                        }
                    }
                    
                    i++;
                }
            }
            
        }   
       
        
    }
    
    private void addInterest(Element SentenceSlot, String interest)
    {
        Element SentenceNode = (Element)SentenceSlot.getParentNode();
        
        if(SentenceNode.getAttributes() != null )
        {
            if(SentenceNode.getAttributes().getNamedItem(Interest) == null)
            {
                SentenceNode.setAttribute(Interest, interest);
            }
            else
            {
                if(SentenceNode.getAttributes().getNamedItem(Interest).getTextContent().equals(""))
                {
                    SentenceNode.setAttribute(Interest, interest);
                }
            }              
        }
       
    }
    
    
    private void addAssim(Element SentenceSlot, String assim)
    {
        Element SentenceNode = (Element)SentenceSlot.getParentNode();
        
        if(SentenceNode.getAttributes() != null )
        {
            if(SentenceNode.getAttributes().getNamedItem(Assim) == null)
            {
                SentenceNode.setAttribute(Assim, assim);
            }
            else
            {
                if(SentenceNode.getAttributes().getNamedItem(Assim).getTextContent().equals(""))
                {
                    SentenceNode.setAttribute(Assim, assim);
                }
            }              
        }     
       
    }    
    
    private void addForProperty(Element SentenceSlot, String property)
    {
        Element SentenceNode = (Element)SentenceSlot.getParentNode();
        
        if(SentenceNode.getAttributes() != null )
        {
            if(SentenceNode.getAttributes().getNamedItem(forProperty) == null)
            {
                SentenceNode.setAttribute(forProperty, property);
            }
            else
            {
                if(SentenceNode.getAttributes().getNamedItem(forProperty).getTextContent().equals(""))
                {
                    SentenceNode.setAttribute(forProperty, property);
                }
            }
        }
       
    }      
    
    public void removeLastPeriod()
    {
        if(this.currentPeriod != null)
        {
            currentPeriod.getParentNode().removeChild(currentPeriod);
        }
    }
}
