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


import org.apache.log4j.Logger;
import org.apache.log4j.Level;

import com.hp.hpl.jena.ontology.*;
        
  
import gr.aueb.cs.nlg.Communications.*;
        
public class OntologyServer extends Thread 
{
    static Logger logger = Logger.getLogger("gr.aueb.cs.nlg.Communications.OntologyServer.OntologyServer");

    boolean flagout;    
        
    private gr.aueb.cs.nlg.Communications.OntologyServer.ProcessRequest ONT_PR;
    private DialogueManagerInfo DMI;
    private OntModel m;    
 
    CommunicationModule client;
            
    public OntologyServer()
    {
        super("OntologyServer");
        client = new CommunicationModule();
    }
        
    public OntologyServer(String nav_server_IP, int port)
    {
        super("OntologyServer");
        logger.setLevel(Level.INFO);
        client = new CommunicationModule(nav_server_IP, port);
    }
    
    public void die() throws Exception
    {
        flagout = true;
        client.disconnect();
    }

    public void setOntology(OntModel m)
    {
        this.m = m;
    }
    
    public void setDialogueManagerInfo(DialogueManagerInfo DMI)
    {
        this.DMI = DMI;
    }
    
    public void  run()
    {
        ONT_PR = new gr.aueb.cs.nlg.Communications.OntologyServer.ProcessRequest();
        
        logger.debug("Initializing Communications with server");
        
        try
        {            
            flagout = false;
            client.connect();
        }
        catch (Exception e)
        {
            logger.info("Couldn't get I/O for the connection to server");
            flagout = true;
        }

        // initially sends module information to server
        logger.debug("Sending Module Information to server");
        
        try
        {
            int [] consumed = {ProcessRequest.PACKETCODE_Ontology_Req};
            int [] produced = {ProcessRequest.PACKETCODE_Ontology_Res};
            client.declarePackets(1, 1, produced, consumed, "OntologyServer");          
        }
        catch (Exception e)
        {
            logger.info("Couldn't write module info");
            flagout = true;
        }
        
        
        //int mpstate=0;
        //Random random = new Random();
        
        if(!flagout)
        logger.info("Ontology Server: I was succesfully connected to Communication Server");
        
        try
        {
            while (!flagout)
            {
                Message msg =null;

                int packetcode = -1;

                // blocks here and tries to read input from server
                // put in a tseparate hread if you dont want to block

                logger.info("Waiting...");
                try
                {
                    msg = client.receive();
                    logger.debug("a message was read!!!");
                }
                catch (Exception e)
                {
                    flagout = true;
                    logger.debug("Couldn't read frame");
                }

                packetcode = msg.getPacketCode();

                switch (packetcode)
                {
                    case ProcessRequest.PACKETCODE_Ontology_Req:

                        String response = ONT_PR.processRequest2(msg.getXmlContent(), m, DMI);                    
                        client.send(response, ProcessRequest.PACKETCODE_Ontology_Res);                    

                        break;       

                    default:
                        logger.debug("There is something wrong!!");
                }//switch
            } // while
        }
        catch(Exception e)
        {
            logger.info("Problem on sending or receiving");
        }
        
        try
        {
            client.disconnect();
            logger.info("disconnected");
        }
        catch (Exception e)
        {
            logger.info("Couldn't close connection");
        }
    } // main
    
} // nav_dm