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




import java.util.*;

public class AttributeDesc {

    private List <Attributes>description;
    public boolean chosen;
    public int history;
    /** Creates a new instance of AttributeDesc */
    public AttributeDesc(String value, int plh8os) {
        description = new LinkedList<Attributes>();
        description.add(new Attributes(value,plh8os));
        chosen = false;
        history = 0;
    }
    
    /** Creates a new instance of AttributeDesc same as oldAttr*/
    public AttributeDesc(AttributeDesc oldAttr) {
        chosen = oldAttr.chosen;
        description = new LinkedList<Attributes>();
        for (int i=0; i<oldAttr.description.size(); i++)
        {
            description.add(new Attributes(oldAttr.description.get(i).Value,oldAttr.description.get(i).Plh8os));
        }
        history = oldAttr.history;
    }
    
    /** pros8etei ena attribute*/
    public void addAtribute(String name)
    {
        boolean stopped=false;
        for (int i=0; i<description.size(); i++)
        {
            if(description.get(i).Value.equalsIgnoreCase(name))
            {
                description.get(i).Plh8os++;
                stopped = true;
                break;
            }
        }
        if (!stopped)
        {
            description.add(new Attributes(name,1));
        }
    }
    
    /** Afairei ena atribute*/
    public void removeAtribute(String name)
    {
        for (int i=0; i<description.size(); i++)
        {
            if(description.get(i).Value.equalsIgnoreCase(name))
            {
                if (description.get(i).Plh8os > 0)
                    description.get(i).Plh8os--;
                break;
            }
        }
    }
    
    public List<Attributes> getDescription()
    {
        return this.description;
    }
    
    /**epistrefei th 8esh opoy brisketai to xarakthristiko me onoma attrName. an den to brei epistrefei -1*/
    public int findName(String attrName)
    {
        for (int i=0; i<this.description.size(); i++)
        {
            if (this.description.get(i).Value.equalsIgnoreCase(attrName))
                return i;
        }
        return -1;
    }
    
    public String toString()
    {
        String outPut="";
        for (int i=0; i<description.size(); i++)
        {
            outPut+= "\tValue: "+description.get(i).Value+"\t-\t"+description.get(i).Plh8os +"\r\n";
        }
        if (outPut == "")
            return "empty";
        else
            return outPut;
    }
    
}

class Attributes {
    public String Value;
    public int Plh8os;
    /** Creates a new instance of AttributeDesc */
    public Attributes(String value, int plh8os) {
        Value = value;
        Plh8os = plh8os;
    }
    
}