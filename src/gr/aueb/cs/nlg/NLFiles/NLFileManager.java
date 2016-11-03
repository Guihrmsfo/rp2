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

import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.util.iterator.*;


import java.io.*;

import gr.aueb.cs.nlg.*;

import org.apache.log4j.Logger;


public class NLFileManager
{
    public Model model = null;
    public Resource [] prettyTypes;
    public RDFWriter NLGWriter;
    public RDFReader NLGReader;
    
    static Logger logger = Logger.getLogger("gr.aueb.cs.nlg.NLFiles.NLFileManager");
    public static String owlnlNS = "http://www.aueb.gr/users/ion/owlnl#";
    
    public String xmlbase = "";
    public String namespace = "";
    //public String path = "file:C:/Documents and Settings/galanisd/Επιφάνεια εργασίας/rdf_Files/";
    //----------------------------------------------------------------------------        
    public NLFileManager(String xb)
    {
        model = ModelFactory.createDefaultModel();
        xmlbase = xb;
        namespace = xb + "#";
        NLGReader = model.getReader("RDF/XML-ABBREV");
        NLGWriter = model.getWriter("RDF/XML-ABBREV");
                
    }
    
    //public String getPath(){
    //    return this.path;
    //}
    
    public Model getModel()
    {
        return this.model;
    }
    
    public ExtendedIterator get(Property p )
    {
        StmtIterator iter = model.listStatements(null, p, (RDFNode)null);
        
        if(iter != null && iter.hasNext())
        {
            RDFList userTypesList = (RDFList)iter.nextStatement().getObject().as(RDFList.class);
            return userTypesList.iterator();
        }
        else
        {
            return null;
        }
    }
    //----------------------------------------------------------------------------    
    public void setPrettyTypes(Resource [] pT)
    {
        prettyTypes = pT;
    }
    //----------------------------------------------------------------------------    
    public void read(String path, String RDFfile)
    {          
        model = ModelFactory.createDefaultModel();
        File f = new File(path + RDFfile);
        
        if(f.exists())
        {
            model.read("file:" + path + RDFfile);      
            logger.info("Loading:" + "file:" + path + RDFfile);
        }
        else
        {
          logger.info("file:" + path + RDFfile + " NOT FOUND");
        }                
    }
    
    public void read(InputStream is)
    {          
        model = ModelFactory.createDefaultModel();
        
        model.read(is,"");      
        logger.debug("input stream.......................");        
    }    
    
    public void read(String RDFfileAbsolutePath)
    {  
        //NLGReader.read(model, RDFfile);
        model = ModelFactory.createDefaultModel();
        
        if(RDFfileAbsolutePath.startsWith("file:"))
            model.read(RDFfileAbsolutePath);
        else
            model.read("file:" + RDFfileAbsolutePath);
        
        logger.debug("file:" + RDFfileAbsolutePath);
    }    
    //----------------------------------------------------------------------------    
    public void write()
    {
                
        NLGWriter.setProperty("xmlbase", xmlbase);
        NLGWriter.setProperty("tab","4");
        //NLGWriter.setProperty("relativeURIs","");
        //NLGWriter.setProperty("blockRules", "propertyAttr");
        NLGWriter.setProperty("showXMLDeclaration","true");
        NLGWriter.setProperty("prettyTypes", prettyTypes);
        
        System.out.println("==RDF/XML serialization==");
        //NLGWriter.write(model,System.out,xmlbase);
        NLGWriter.write(model,System.out, xmlbase);
        
    }
        
    //----------------------------------------------------------------------------
    public void CloseModel()
    {
        model.close();
    }      
}