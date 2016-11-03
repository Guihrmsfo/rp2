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

public class RepetitionsNode extends ParameterNode
{       
    private String RepValue ;
                
    public RepetitionsNode (String UT,String  RV)
    {        
        super(UT, RV);                
    }
    
    
    public String getRepValue()
    {
        return getParameter();
    }
    
    public void  setRepValue(String repV)
    {
        setParameter(repV);
    }
    
    public void print(){
        System.out.println("\t===RepetitionsNode printing===");
        System.out.println("\t\tforUserType:" + getforUserType());
        System.out.println("\t\tRepValue:" + getRepValue());
        System.out.println("\t===/RepetitionsNode printing===");
    }
}


     