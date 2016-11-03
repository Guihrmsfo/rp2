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

import java.io.*;
import org.w3c.dom.*;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import java.io.File;


public class XMLDocWriter
{    
    // this function is writing a xml doc to a file 
     public static void saveDocToFile(Document doc, String fileName)
     {
        try
        {
            File file = new File(fileName);
            OutputFormat OutFrmt = null;
            
            FileOutputStream fos = new FileOutputStream(fileName);
            
            OutFrmt = new OutputFormat(doc, "UTF-8", true);

            //OutFrmt.setLineWidth(30); 
            OutFrmt.setIndenting(true);  
            //OutFrmt.setEncoding("UTF-8");             

            XMLSerializer xmlsrz = new XMLSerializer(fos,OutFrmt );            
            xmlsrz.asDOMSerializer();
            
            xmlsrz.serialize( doc.getDocumentElement() );
            fos.close();
                        
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }        
}
