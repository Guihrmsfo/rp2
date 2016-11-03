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

package gr.aueb.cs.nlg.Communications.OntologyServer;

import gr.aueb.cs.nlg.NLFiles.*;
import java.util.*;

import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.util.iterator.*;

import org.apache.log4j.Logger;

public class DialogueManagerInfo extends NLFileManager
{
    Logger logger = Logger.getLogger(DialogueManagerInfo.class.getName());
    
    private Hashtable<String, Integer> Classes;
    private Hashtable<String, Integer> Individuals;
           
    private static final String ClassesInfoRes = "ClassesInfo";
    private static final String IndividualsInfoRes = "IndividualsInfo";

    private static final String ClassesPrp = "Classes";
    private static final String IndividualsPrp = "Individuals";
    private static final String PriorityPrp = "Priority";
    
    private Resource ClassesInfoType ;
    private Resource IndividualsInfoType ;

    private Property ClassesProperty ;
    private Property IndividualsProperty ;
    private Property PriorityProperty ;
    
    public DialogueManagerInfo(String xb)
    {        
        super(xb);
    }
 
    public DialogueManagerInfo()
    {        
        super("");
        
        ClassesInfoType = model.createResource(owlnlNS + ClassesInfoRes);
        IndividualsInfoType = model.createResource(owlnlNS + IndividualsInfoRes);
        
        ClassesProperty = model.createProperty(owlnlNS + ClassesPrp);
        IndividualsProperty = model.createProperty(owlnlNS + IndividualsPrp);
        PriorityProperty = model.createProperty(owlnlNS + PriorityPrp);
    }
        
    public void Load(String path)
    {
        try
        {
            init();
            read(path);

            ExtendedIterator ClassesIter = this.get(this.ClassesProperty);

            while(ClassesIter != null && ClassesIter.hasNext())
            {
                String classURI = ClassesIter.next().toString();
                logger.info(classURI);

                Resource res = model.getResource(classURI);
                int priority = res.getProperty(PriorityProperty).getLiteral().getInt();
                
                Classes.put(classURI, priority);
            }

            ExtendedIterator IndividualsIter = this.get(this.IndividualsProperty);

            while(IndividualsIter != null && IndividualsIter.hasNext())
            {
                String IndividualURI = IndividualsIter.next().toString();
                logger.info(IndividualURI);
                Resource res = model.getResource(IndividualURI);
                int priority = res.getProperty(PriorityProperty).getLiteral().getInt();
                
                Classes.put(IndividualURI, priority);
            }        
        }
        catch(Exception e)
        {
            e.printStackTrace();
            
            logger.info("ERORR!!!");
        }
    }
    
    public int getClassPriority(String ResourceURI)
    {
        if(Classes.containsKey(ResourceURI))
            return Classes.get(ResourceURI).intValue();
        else
            return -1;
    }

    public int getIndividualPriority(String ResourceURI)
    {
        if(Classes.containsKey(ResourceURI))
            return Classes.get(ResourceURI).intValue();
        else
            return -1;        
    }
        
    public void init()
    {
        Classes = new Hashtable<String, Integer>();
        Individuals = new Hashtable<String, Integer>();
    }
    
    public static void main(String args[])
    {
        DialogueManagerInfo DMI = new DialogueManagerInfo();
        DMI.Load("C:\\NLG_Project\\NLFiles-MPIRO\\DM.rdf");
    }
}
