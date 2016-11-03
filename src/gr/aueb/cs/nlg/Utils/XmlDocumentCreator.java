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

import org.w3c.dom.*;
import javax.xml.parsers.*;

import java.io.*;

public class XmlDocumentCreator
{        
	private DocumentBuilder docBuilder;
	
	public XmlDocumentCreator()
        {
            try
            {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                factory.setNamespaceAware(true);
                docBuilder = factory.newDocumentBuilder();                        
            }
            catch(ParserConfigurationException e)
            {
                e.printStackTrace();
            }			
	}
	
	// return a new XML document
	public Document getNewDocument()
        {
            Document doc = docBuilder.newDocument();
            return doc;
	}	
    
	// parse the file
	public Document parse(File f)
        {
            try
            {
                return docBuilder.parse(f);
            }
            catch(org.xml.sax.SAXException e)
            {
                e.printStackTrace();
            }	
            catch(IOException e)
            {
                e.printStackTrace();
            }
            return null;	
	}	
        
	// parse the file
	public Document parse(InputStream IS)
        {
            try
            {
                return docBuilder.parse(IS);
            }
            catch(org.xml.sax.SAXException e)
            {
                e.printStackTrace();
            }	
            catch(IOException e)
            {
                e.printStackTrace();
            }
            return null;	
	}        
}