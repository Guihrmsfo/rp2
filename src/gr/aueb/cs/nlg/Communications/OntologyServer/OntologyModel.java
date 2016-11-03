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

import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.vocabulary.*;
import com.hp.hpl.jena.ontology.*;


import org.apache.log4j.Logger;
import org.apache.log4j.Level;

public class OntologyModel
{
    Logger logger = Logger.getLogger(OntologyModel.class.getName());
    private OntModel ontModel;

    
    private DialogueManagerInfo DMI;
            
    public OntologyModel(String path)
    {
        logger.setLevel(Level.DEBUG);
                
        ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM ,null);
        ontModel.add(OWL.Thing, RDF.type, OWL.Class);
        
        ontModel.read(path);
        logger.info("Ontology Loaded");
    }

    public OntologyModel(OntModel ontModel)
    {
        logger.setLevel(Level.DEBUG);
        
        this.ontModel = ontModel;
        logger.info("Ontology Loaded");
    }
    
    
  
}
