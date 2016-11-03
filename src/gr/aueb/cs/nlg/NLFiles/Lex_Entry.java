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

import gr.aueb.cs.nlg.Languages.*;
import gr.aueb.cs.nlg.Utils.*;

import java.io.*;
import java.util.StringTokenizer;

public abstract class Lex_Entry 
{
    private boolean countable ;
    private String shortname ;
    
    public Lex_Entry() 
    {
    }
              
    public boolean getCountable()
    {
        return countable;
    } 
    
    public void setCountable(boolean c)
    {
        countable = c;
    } 

    public void setShortname(String s)
    {
        this.shortname = s;
    }
    
    public String getShortname()
    {
        return shortname;
    }
    
    public int [] getShortnameBounds()
    {
        try
        {
            int index = shortname.charAt('-');
            int ret[] = new int[2];
            ret[0] = Integer.parseInt(shortname.substring(0,index));
            ret[1] = Integer.parseInt(shortname.substring(index + 1, shortname.length()));
        }
        catch(Exception e)
        {
            e.printStackTrace();            
        }
            
        return null;
    }
    
    public static String getShortname(String str, int bounds [])
    {
        if(bounds.length == 2)
        {
            String ret = "";
            StringTokenizer ST = new StringTokenizer(str);
            int i = 1;
            while(ST.hasMoreTokens())
            {
                if(i <= bounds[1] && i>=bounds[0])
                {
                    ret = ret + " " + ST.nextToken();
                }
                    i++;
            }
            
            return ret;
        }
        else
        {
            return str;
        }
    }
            
    public abstract void print();
    
    public abstract String get(String Case);
    public abstract String get_Gender();
    public abstract String get_num();
}
