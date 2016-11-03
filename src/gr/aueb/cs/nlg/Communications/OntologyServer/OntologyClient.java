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


import gr.aueb.cs.nlg.Utils.*;


import org.w3c.dom.*;

import org.apache.log4j.Logger;            
import gr.aueb.cs.nlg.Communications.*;

public class OntologyClient extends gr.aueb.cs.nlg.Communications.CommunicationModule
{    
    public static Logger logger = Logger.getLogger(OntologyClient.class.getName());
        

    
    public OntologyClient(String host, int port)
    {      
       super(host, port);
    }
    
    /*
        
            <Request>
                <GetSubClassesOf>http://www.....#vessel</GetSubClassesOf>
            </Request>
     
     
     */
    public String getSubClasses(String classURI) throws Exception
    {
        
        Document doc = this.getDocCreator().getNewDocument();
        
        Node req = doc.createElement(ProcessRequest.Request);
        doc.appendChild(req);
        
        Node req1 = doc.createElement(ProcessRequest.GetSubClassesOf);
        req1.setTextContent(classURI);
        req.appendChild(req1);
        
        String request = XmlUtils.getStringDescription(doc.getElementsByTagName("Request").item(0), true);
        
        this.send(request, ProcessRequest.PACKETCODE_Ontology_Req);
        Message msg = receive();        
                 
        return msg.getXmlContent();
    }
    
    /*
        <Request>
            <GetSubClassesOf>http://www.....#exhibit8</GetSubClassesOf>
        </Request>    
     */
    
    public String getInstances(String classURI) throws Exception
    {
        Document doc =  this.getDocCreator().getNewDocument();
        
        Node req = doc.createElement(ProcessRequest.Request);
        doc.appendChild(req);
        
        Node req1 = doc.createElement(ProcessRequest.GetInstancesOf);
        req1.setTextContent(classURI);
        req.appendChild(req1);
        
        String request = XmlUtils.getStringDescription(doc.getElementsByTagName("Request").item(0), true);
        
        
        this.send(request, ProcessRequest.PACKETCODE_Ontology_Req);
        Message msg = receive();        
        
         
        return msg.getXmlContent();
    }
   
    public String executeSPARQLQuery(String query) throws Exception
    {
     
        Document doc =  this.getDocCreator().getNewDocument();
        
        Node req = doc.createElement(ProcessRequest.Request);
        req.setTextContent(query);
        
        doc.appendChild(req);
        
        String request = XmlUtils.getStringDescription(doc.getElementsByTagName("Request").item(0), true);
        
        this.send(request, ProcessRequest.PACKETCODE_Ontology_Req);
        Message msg = receive();        
                 
        return msg.getXmlContent();
    }
    
    public static void main(String args[]) throws Exception
    {
        /*
            <?xml version="1.0" encoding="ISO-8859-7"?>
            <Request>
                <GetSubClassesOf>http://www.aueb.gr/users/ion/mpiro.owl#vessel</GetSubClassesOf>
            </Request>

            Waiting...
            <?xml version="1.0" encoding="ISO-8859-7"?>
            <Request>
                <GetSubClassesOf>http://www.aueb.gr/users/ion/mpiro.owl#vessel</GetSubClassesOf>
            </Request>

            Waiting...
            <?xml version="1.0" encoding="ISO-8859-7"?>
            <Request>
                <GetInstancesOf>http://www.aueb.gr/users/ion/mpiro.owl#amphora</GetInstancesOf>
            </Request>         
         */
        try
        {
            OntologyClient OC = new OntologyClient("127.0.0.1", 53000);
            logger.info("trying to connect to communication server");
            OC.connect();                        
            logger.info("connected to communication server");
            
            int [] produced = {ProcessRequest.PACKETCODE_Ontology_Req};
            int [] consumed = {ProcessRequest.PACKETCODE_Ontology_Res};
                        
            OC.declarePackets( 1, 1, produced, consumed, "OntologyClient");
        
           //String queryString = "SELECT ?x ?y WHERE {?x  <http://www.aueb.gr/users/ion/mpiro.owl#painting-techinique-used>  ?y}" ;
           String queryString = "SELECT ?y WHERE {<http://www.aueb.gr/users/ion/mpiro.owl#exhibit1> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ?y}" ;
            
            //for(int i = 0; i < 10; i++)
            //{
                String ret = OC.executeSPARQLQuery(queryString);                
                System.err.println(ret);
                //logger.info("\n\n" + OC.getSubClasses("http://www.aueb.gr/users/ion/mpiro.owl#vessel") + "\n\n");        
                //logger.info("\n\n" + OC.getSubClasses("http://www.aueb.gr/users/ion/mpiro.owl#vessel") + "\n\n");                                                
                //logger.info("\n\n" + OC.getInstances("http://www.aueb.gr/users/ion/mpiro.owl#amphora") + "\n\n");
            //}
            /* 
             
             <?xml version="1.0" encoding="ISO-8859-7"?>
             <Response>
                <Class owlnl:Id="http://www.aueb.gr/users/ion/mpiro.owl#kylix" owlnl:Priority="1"/>
                <Class owlnl:Id="http://www.aueb.gr/users/ion/mpiro.owl#hydria" owlnl:Priority="-1"/>
                <Class owlnl:Id="http://www.aueb.gr/users/ion/mpiro.owl#prochous" owlnl:Priority="-1"/>
                <Class owlnl:Id="http://www.aueb.gr/users/ion/mpiro.owl#aryballos" owlnl:Priority="-1"/>
                <Class owlnl:Id="http://www.aueb.gr/users/ion/mpiro.owl#lekythos" owlnl:Priority="-1"/>
                <Class owlnl:Id="http://www.aueb.gr/users/ion/mpiro.owl#kantharos" owlnl:Priority="-1"/>
                <Class owlnl:Id="http://www.aueb.gr/users/ion/mpiro.owl#cauldron" owlnl:Priority="-1"/>
                <Class owlnl:Id="http://www.aueb.gr/users/ion/mpiro.owl#amphora" owlnl:Priority="1"/>
                <Class owlnl:Id="http://www.aueb.gr/users/ion/mpiro.owl#stamnos" owlnl:Priority="-1"/>
                <Class owlnl:Id="http://www.aueb.gr/users/ion/mpiro.owl#rhyton" owlnl:Priority="-1"/>
            </Response>

             <?xml version="1.0" encoding="ISO-8859-7"?>
             <Response>
                <Class owlnl:Id="http://www.aueb.gr/users/ion/mpiro.owl#kylix" owlnl:Priority="1"/>
                <Class owlnl:Id="http://www.aueb.gr/users/ion/mpiro.owl#hydria" owlnl:Priority="-1"/>
                <Class owlnl:Id="http://www.aueb.gr/users/ion/mpiro.owl#prochous" owlnl:Priority="-1"/>
                <Class owlnl:Id="http://www.aueb.gr/users/ion/mpiro.owl#aryballos" owlnl:Priority="-1"/>
                <Class owlnl:Id="http://www.aueb.gr/users/ion/mpiro.owl#lekythos" owlnl:Priority="-1"/>
                <Class owlnl:Id="http://www.aueb.gr/users/ion/mpiro.owl#kantharos" owlnl:Priority="-1"/>
                <Class owlnl:Id="http://www.aueb.gr/users/ion/mpiro.owl#cauldron" owlnl:Priority="-1"/>
                <Class owlnl:Id="http://www.aueb.gr/users/ion/mpiro.owl#amphora" owlnl:Priority="1"/>
                <Class owlnl:Id="http://www.aueb.gr/users/ion/mpiro.owl#stamnos" owlnl:Priority="-1"/>
                <Class owlnl:Id="http://www.aueb.gr/users/ion/mpiro.owl#rhyton" owlnl:Priority="-1"/>
            </Response>
             
            <?xml version="1.0" encoding="ISO-8859-7"?>
            <Response>
                <Individual owlnl:Id="http://www.aueb.gr/users/ion/mpiro.owl#exhibit1" owlnl:Priority="5"/>
            </Response>

            */
            
            OC.disconnect();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
    }
}
