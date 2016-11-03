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
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

public class XmlUtils
{
    
    /** Creates a new instance of XmlUtils */
    public XmlUtils()
    {
    }
    
   
    //-----------------------------------------------------------------------------------
    // return a string representation of the xml document 
    public static String getStringDescription(Node nd, boolean indent)
    {
       return getStringDescription(nd, indent, "UTF-8");
    }//getStringDescription	
    
    public static String getStringDescription(Node nd, boolean indent, String encoding)
    {
        try
        {
            OutputFormat OutFrmt = null;
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            XmlDocumentCreator xml_doc_crtr = new XmlDocumentCreator();
            Document doc = xml_doc_crtr.getNewDocument();
            Node copy_of_nd = doc.importNode(nd,true);
            doc.appendChild(copy_of_nd);

            OutFrmt = new OutputFormat(doc, encoding, indent);
            
            OutFrmt.setIndenting(indent); 
            
            OutFrmt.setEncoding(encoding);             
            XMLSerializer xmlsrz = new XMLSerializer(os,OutFrmt);            
            xmlsrz.serialize(doc);            
            return new String(os.toByteArray(),Charset.forName(encoding));
        }//try
        catch(UnsupportedCharsetException e)
        {
            e.printStackTrace();
            return "ERRoR: UnsupportedCharsetException";                
        }        
        catch(Exception e)
        {
            e.printStackTrace();
            return "ERRoR";
        }//catch
    }//getStringDescription
    
}
