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

import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.vocabulary.*;
import com.hp.hpl.jena.ontology.*;
import com.hp.hpl.jena.util.iterator.*;
import com.hp.hpl.jena.datatypes.*; 


import java.util.*;
/**
 *
 * @author Erevodifwntas
 */
public class exhibitsPositions
{
    public Hashtable <String, exhibitPosition>  positions;
    
    
    public  exhibitsPositions (OntModel ontModel, String coordPropertyURI, String roomsPropertyURI)
    {
        positions = new Hashtable<String, exhibitPosition>();
        Property coord = ontModel.getProperty(coordPropertyURI);
        Property rooms = ontModel.getProperty(roomsPropertyURI);               
        
        ExtendedIterator iter = ontModel.listIndividuals();
        
        while(iter.hasNext())
        {
            String IndividualURI = iter.next().toString();
            Individual temp = ontModel.getIndividual(IndividualURI);
            
            if(temp.getProperty(coord)!=null && temp.getProperty(rooms)!=null)
            positions.put(IndividualURI, new exhibitPosition(IndividualURI, temp.getProperty(coord).getLiteral().getString(),temp.getProperty(rooms).getLiteral().getString()));
        }    
    }
    
    public void changePosition(String exhibitURI, double newX, double newY)
    {
        
        exhibitPosition tmp = this.positions.get(exhibitURI);
        if(tmp!=null)
        {
            tmp.x =newX;
            tmp.y = newY;
        }
    }
    
}

class exhibitPosition {
   String name;
   double x;
   double y;
   
   Vector <String> rooms;
   
    /** Creates a new instance of exhibitsPositions */
    public exhibitPosition(String name, String coords, String room) {
        this.name = name;
        rooms = new Vector<String>();
        int pos = coords.indexOf(',');
        x = Double.parseDouble(coords.substring(0,pos));
        y = Double.parseDouble(coords.substring(pos+1));
        
        int start = 0;
        while(true)
        {
            pos = room.indexOf(',',start);
            if (pos<0)
            {
                rooms.add(room.substring(start));
                break;
            }
            else
            {
                rooms.add(room.substring(start,pos));
                start = pos+1;
            }
        }
    }
}
