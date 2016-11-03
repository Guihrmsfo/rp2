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

package gr.aueb.cs.nlg.NLFiles;

import java.io.*;

import gr.aueb.cs.nlg.Languages.*;

public class InterestNode extends ParameterNode
{    
                
    public InterestNode (String UT,String  IV)
    {
        super(UT, IV);         
    }
        
    public String getInterestValue()
    {
        return this.getParameter();
    }
    
    
    public void setInterestValue(String s)
    {
        this.setParameter(s);
    }
    
    public void print()
    {
        System.out.println("\t===InterestNode printing===");
        System.out.println("\t\tforUserType:" + getforUserType());
        System.out.println("\t\tInterestValue:" + getInterestValue());
        System.out.println("\t===/InterestNode printing===");
    }
}


     