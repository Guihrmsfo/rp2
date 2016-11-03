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
import java.util.*;
import java.util.Comparator;
import org.w3c.dom.*;

public  class NameComparatorImpl extends NameComparator{//NameComparator
	private boolean asc;
	
	public NameComparatorImpl(boolean a){
		asc = a;
	}
	
	public void setAsc(boolean a){
		asc = a;
	}
	
	public boolean getAsc(){
		return asc;
	}
	
	public int compare(Object o1, Object o2){
		return compare((OntObject)o1, (OntObject)o2);
	} 
	
	public int compare(OntObject a, OntObject b){
		int ret = a.getLocalName().compareTo(b.getLocalName());
		if(asc)
		return ret;
		else
		return (-ret); 
	}
}