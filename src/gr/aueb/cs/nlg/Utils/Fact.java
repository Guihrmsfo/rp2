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
 

public class Fact 
{
    
    private String sub;
    private String predicate;
    private String obj;
            
    public Fact(String s, String p,String o) {
        sub = s;
        predicate = p;
        obj = o;
    }
    
    
    public Fact(String fact)
    {
        int index1 = fact.indexOf("," , 0);
        int index2 = fact.indexOf("," , index1 +2);
        //System.out.println("index1:" + index1 + " index2:" + index2);
        
        sub = fact.substring(1, index1);             
        predicate = fact.substring(index1 + 2, index2);
        obj = fact.substring(index2 + 2, fact.length()-1);          
    }
    
    public String getFact(){
        return FactFactory.getFact(sub, predicate, obj);
    }
    
    public String getSubject()
    {
        return sub;
    }
    
    public String getPredicate()
    {
        return predicate;
    }
    
    public String getObject()
    {
        return obj;
    }
    
    public static String getSubject(String fact)
    {
        Fact fct = new Fact(fact);
        return fct.getSubject();
    }
    
    public static String getPredicate(String fact)
    {
        Fact fct = new Fact(fact);
        return fct.getPredicate();
    }
    
    public static String getObject(String fact)
    {
        Fact fct = new Fact(fact);
        return fct.getObject();
    }    
}
