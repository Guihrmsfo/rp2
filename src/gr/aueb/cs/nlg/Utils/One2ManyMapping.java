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

import java.util.*;

public class One2ManyMapping 
{ 
    HashMap<String, HashSet> mapping;
    
    public One2ManyMapping() 
    {
        mapping = new HashMap<String, HashSet>();        
    }
    
    public void print()
    {
        Iterator<String> iter = mapping.keySet().iterator();
        
        while(iter.hasNext())
        {
            String key = iter.next();
            Iterator<String> iter2 = mapping.get(key).iterator();
            
            while(iter2.hasNext())
            {
                String val = iter2.next();
                //System.err.println(key + " ---> " + val);
            }
        }
    }
    
    public void AddMapping(String key , String value)
    {
        //System.err.println(key + " is mapped to " + value);
        
        if(mapping.containsKey(key))
        {
             HashSet set = mapping.get(key);
             set.add(value);
             mapping.put(key, set);                        
        }
        else
        {
            HashSet set = new HashSet();
            set.add(value);
            mapping.put(key, set);                        
        }
    }
    
    public void removeKey(String key)
    {
        mapping.remove(key);
    }
    
    public Iterator getValues(String key)
    {
        if(mapping.containsKey(key))
            return mapping.get(key).iterator();
        else
            return null;
    }
    
    public void removeValue(String value)
    {
        Iterator<String> iter = mapping.keySet().iterator();
        
        while(iter.hasNext())
        {
            mapping.get(iter.next()).remove(value);
        }
    }
    
    public void removeValue(String key, String value)
    {
        mapping.get(key).remove(value);
    }
}
