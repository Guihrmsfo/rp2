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

public  class InterestComparatorImpl extends InterestComparator
{//InterestComparatorImpl
	private boolean asc;		 
	
               
	public InterestComparatorImpl(boolean a)
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
            int Interest_a = Integer.parseInt(XmlMsgs.getAttribute(a, XmlMsgs.prefix, XmlMsgs.INTEREST));
            int Interest_b = Integer.parseInt(XmlMsgs.getAttribute(b, XmlMsgs.prefix, XmlMsgs.INTEREST));

            int ret = Interest_a - Interest_b; 

            if(asc)
            return ret;
            else
            return (-ret); 
	}
}//InterestComparatorImpl