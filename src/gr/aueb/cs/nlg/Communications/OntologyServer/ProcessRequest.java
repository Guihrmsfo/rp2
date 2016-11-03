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

import org.w3c.dom.*;
import gr.aueb.cs.nlg.Utils.*;

import com.hp.hpl.jena.ontology.*;
import com.hp.hpl.jena.util.iterator.*;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;

import org.apache.log4j.Logger;

import java.io.*;
import com.hp.hpl.jena.rdf.model.*;



public class ProcessRequest
{
    static Logger logger = Logger.getLogger(ProcessRequest.class.getName());

    private XmlDocumentCreator xmlDocCreator;
    
    public static String GetSubClassesOf = "GetSubClassesOf";
    public static String GetInstancesOf = "GetInstancesOf";
    public static String SubClassesOf = "SubClassesOf";    
    public static String Class = "Class";
    
    public static String Request = "Request";
    public static String Response = "Response";
    
    
    public static final int PACKETCODE_Ontology_Req = 873;
    public static final int PACKETCODE_Ontology_Res = 874;
    
    public ProcessRequest()
    {
        xmlDocCreator = new XmlDocumentCreator();
    }
    
    public String processRequest2(String request,OntModel ontModel, DialogueManagerInfo DMI)
    {
        String ret ="";
        
        try 
        {                   
            logger.info("request:" + request);

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(request.getBytes("UTF-8"));
            Document doc = xmlDocCreator.parse(byteArrayInputStream);

            Node root = doc.getElementsByTagName(Request).item(0);
             
            Model model = ontModel ;
            String queryString = root.getTextContent() ;

            Query query = QueryFactory.create(queryString) ;                
            QueryExecution qexec = QueryExecutionFactory.create(query, model) ;

            logger.info("executing SPARQL query...");
        
            ResultSet results = qexec.execSelect();
            
            for ( ; results.hasNext() ; )
            {
                QuerySolution soln = results.nextSolution() ;
                
                ret = ret + soln.toString() + "\n";
            }
        }
        catch(Exception e)
        { 
            e.printStackTrace();
        }
        
        Document doc =  xmlDocCreator.getNewDocument();
        
        Node res = doc.createElement(ProcessRequest.Response);
        res.setTextContent(ret);
        
        doc.appendChild(res);
        String resp = XmlUtils.getStringDescription(doc.getElementsByTagName("Response").item(0), true);
                
        logger.info(resp); 
         
        return resp;
        
    }
  
    // process the request for the ontology server    
    public String processRequest(String request,OntModel ontModel, DialogueManagerInfo DMI)
    {
        try
        {
        System.err.println();
        
        String result = "";
        Document resultDoc = xmlDocCreator.getNewDocument();
        logger.info("process the request: " + request);
        Node ResponseNode = resultDoc.createElement(Response);
        resultDoc.appendChild(ResponseNode);
                
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(request.getBytes("UTF-8"));
        Document doc = xmlDocCreator.parse(byteArrayInputStream);
        
        Node root = doc.getElementsByTagName(Request).item(0);
        
        NodeList list = root.getChildNodes();
                                
        for(int i = 0; i < list.getLength(); i++)
        {
            
            Node currentRequest = list.item(i);
            String RequestNodeName = currentRequest.getNodeName();
            //logger.debug(RequestNodeName);
             
            if(RequestNodeName.equals(GetSubClassesOf))
            {
                String ClassURI = currentRequest.getTextContent();
                OntClass c = ontModel.getOntClass(ClassURI);
                
                if(c!=null)
                {
                    ExtendedIterator iter = c.listSubClasses();

                    while(iter.hasNext())
                    {
                        OntClass superc = (OntClass)iter.next();

                        if(!c.isAnon())
                        {
                            //result = result + "\n" + superc.getURI();
                            String supercURI = superc.getURI();
                            Element ClassNode = resultDoc.createElement("Class");
                            
                            ClassNode.setAttribute("owlnl:Id", supercURI);
                            ClassNode.setAttribute("owlnl:Priority", DMI.getClassPriority(supercURI) + "");
                            
                            ResponseNode.appendChild(ClassNode);
                        }
                    }
                }
                else
                {
                    result = "CLASS NOT FOUND";
                }
            }
            else if(RequestNodeName.equals(GetInstancesOf))
            {
                String ClassURI = currentRequest.getTextContent();
                OntClass c = ontModel.getOntClass(ClassURI);

                if(c!=null)
                {
                    ExtendedIterator iter = c.listInstances();

                    while(iter.hasNext())
                    {

                        Individual ind = (Individual)iter.next();

                        if(!ind.isAnon())
                        {
                            String indURI = ind.getURI();
                            
                            Element IndividualNode = resultDoc.createElement("Individual");
                            
                            IndividualNode.setAttribute("owlnl:Id", indURI);
                            IndividualNode.setAttribute("owlnl:Priority", DMI.getClassPriority(indURI) + "");
                            
                            ResponseNode.appendChild(IndividualNode);
                        }
                    }
                }
                else
                {
                    result = "CLASS NOT FOUND";
                }
            }
            
        }//for
        
        //logger.debug("processRequest: " + XmlUtils.getStringDescription(ResponseNode, true) );
        return XmlUtils.getStringDescription(ResponseNode, true) ; 
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return "ERROR...";
        }
    }    
    
    
    public static void main(String args[])
    {

    }
}
