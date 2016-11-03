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

import java.util.Vector;

public class MicroplansList
{
    
    private String language;
    private Vector<Microplan> MicroplansList;
    
    MicroplansList(String language, Vector<Microplan> ML)
    {
        this.language = language;
        this.MicroplansList = ML;
    }
    
    MicroplansList(String language)
    {
        this.language = language;
        this.MicroplansList = new Vector<Microplan>();
    }
    
    public void print()
    {
        for(int i = 0; i < MicroplansList.size(); i++)
        {
            MicroplansList.get(i).print();
        }
    }
    
    public int size()
    {
        return MicroplansList.size();
    }
        
    public void add(Microplan m)
    {
        this.MicroplansList.add(m);
    }
    
    public void setLanguage(String language)
    {
        this.language = language;
    }
    
    public String getLanguage()
    {
        return this.language;
    }
        
    public void setMicroplansList(Vector<Microplan> ML)
    {
        this.MicroplansList = ML;
    }
        
    public Vector<Microplan> getMicroplansList()
    {
        return this.MicroplansList;
    }
    
    public Microplan getMicroplan(int i)
    {
        return this.MicroplansList.get(i);
    }
    
    public void setMicroplan(int i, Microplan M)
    {
        this.MicroplansList.set(i,M);
    }
}//MicoplansList