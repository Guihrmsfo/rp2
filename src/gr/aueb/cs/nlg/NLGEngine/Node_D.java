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
import javax.xml.parsers.*;
import java.util.*;
/**
 *
 * @author USER
 */
class Node_D
{
	public Node nd;
	public int depth;
	//-----------------------------------------------------------------------------------
	public Node_D(Node nd,int c){
		this.nd = nd;
		this.depth = c;
	}
	
	public Node get_Node(){
		return nd;
	}
	
	public int get_depth(){
		return depth;
	}	
		
	public static int depth(Node nd){
		int c = 0;
		Node currNode = nd.getParentNode();
		while(currNode!=null){
			c++;
			currNode = currNode.getParentNode();
		}
		System.out.println("c value is:" + c);
		return c;
	}
	//-----------------------------------------------------------------------------------
	public static Object[] sort (Vector All_Nodes_vec ){
		Object T [] = All_Nodes_vec.toArray();	
		Arrays.sort(T,new DComparatorImpl(false));
		return T;
		
	}//sort
}
