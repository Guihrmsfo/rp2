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

import java.io.*;
import java.util.*;
import org.w3c.dom.*;

public  class OrderComparatorImpl extends OrderComparator
{//OrderComparator
	private boolean asc;		 
	
                
	public OrderComparatorImpl(boolean a)
        {
            asc = a;
	}
	
	public void setAsc(boolean a)
        {
            asc = a;
	}
	
	public boolean getAsc()
        {
            return asc;
	}
	
	public int compare(Object o1, Object o2)
        {
            return compare((Node)o1,(Node)o2);
	} 
	
	public int compare(Node a,Node b)
        {
            NamedNodeMap NNM_a = a.getAttributes();
            NamedNodeMap NNM_b = b.getAttributes();

            Node Order_a = NNM_a.getNamedItem(XmlMsgs.prefix + ":" + XmlMsgs.ORDER_TAG);
            Node Order_b = NNM_b.getNamedItem(XmlMsgs.prefix + ":" + XmlMsgs.ORDER_TAG);

            String property_a = a.getNodeName();
            String property_b = b.getNodeName();
		
            boolean sameProperty = false;

            if(property_a != null && property_b!=null)
            {
                    if(property_a.compareTo(property_b)==0)
                    {
                            sameProperty = true;
                    }

            }
		
            //Node restr_type_a = NNM_a.getNamedItem("RestrType");
            //Node restr_type_b = NNM_b.getNamedItem("RestrType");

            int a_order = 0;
            int b_order = 0;
		
            if(Order_a==null)
            {
                a_order = 10000;
            }
            else
            {
                a_order = 100* Integer.parseInt(Order_a.getTextContent());


                if(/*restr_type_a!=null &&*/ sameProperty)
                {
                /*
                        if(restr_type_a.getTextContent().compareTo(XmlMsgs.HAS_VALUE_RESTRICTION_TAG)==0)
                        a_order = a_order + 2;

                        if(restr_type_a.getTextContent().compareTo(XmlMsgs.SOME_VALUES_FROM_RESTRICTION_TAG)==0)
                        a_order = a_order + 2;

                        if(restr_type_a.getTextContent().compareTo(XmlMsgs.ALL_VALUES_FROM_RESTRICTION_TAG)==0)
                        a_order = a_order + 2;

                        if(restr_type_a.getTextContent().compareTo(XmlMsgs.CARDINALITY_RESTRICTION_TAG)==0)
                        a_order = a_order + 1;

                        if(restr_type_a.getTextContent().compareTo(XmlMsgs.MIN_CARDINALITY_RESTRICTION_TAG)==0)
                        a_order = a_order + 1;

                        if(restr_type_a.getTextContent().compareTo(XmlMsgs.MAX_CARDINALITY_RESTRICTION_TAG)==0)					
                        a_order = a_order + 1; 		
                 */                            
                    a_order = a_order + (property_a.hashCode()/Integer.MAX_VALUE);
                }
            }
						
            if(Order_b==null)
            {
                    b_order = 10000;
            }
            else
            {
                    b_order = 100* Integer.parseInt(Order_b.getTextContent());						

                    if(/*restr_type_b!=null && */sameProperty)
                    {
                    /*
                            if(restr_type_b.getTextContent().compareTo(XmlMsgs.HAS_VALUE_RESTRICTION_TAG)==0)
                            b_order = b_order + 2;

                            if(restr_type_b.getTextContent().compareTo(XmlMsgs.SOME_VALUES_FROM_RESTRICTION_TAG)==0)
                            b_order = b_order + 2;

                            if(restr_type_b.getTextContent().compareTo(XmlMsgs.ALL_VALUES_FROM_RESTRICTION_TAG)==0)
                            b_order = b_order + 2;

                            if(restr_type_b.getTextContent().compareTo(XmlMsgs.CARDINALITY_RESTRICTION_TAG)==0)
                            b_order = b_order + 1;

                            if(restr_type_b.getTextContent().compareTo(XmlMsgs.MIN_CARDINALITY_RESTRICTION_TAG)==0)
                            b_order = b_order + 1;

                            if(restr_type_b.getTextContent().compareTo(XmlMsgs.MAX_CARDINALITY_RESTRICTION_TAG)==0)					
                            b_order = b_order + 1;			
                     */

                        b_order = b_order + 1;
                    }
            }
										 
            int ret =  a_order - b_order;

            if(asc)
            return ret;
            else
            return (-ret); 
	}
}