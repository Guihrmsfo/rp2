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

package gr.aueb.cs.nlg.Communications;

import java.net.*;
import java.io.*;

import gr.aueb.cs.nlg.Utils.*;

// this class is an interface
// to communicate with ORCA server
public class CommunicationModule
{

    private Socket clientSocket;
    private DataOutputStream out;
    private DataInputStream in;
    
    private XmlDocumentCreator xmlDocCreator;
    private String host;
    private int port;
    
     public CommunicationModule()
    {     
        this.host = "127.0.0.1";
        this.port = 53000;
        xmlDocCreator = new XmlDocumentCreator();            
    }
        
    public CommunicationModule(String h, int p)
    {     
        if(h != null && p !=-1)
        {
            this.host = h;
            this.port = p;
            xmlDocCreator = new XmlDocumentCreator();            
        }
        else
        {
            this.host = "127.0.0.1";
            this.port = 53000;
            xmlDocCreator = new XmlDocumentCreator();               
        }
            
    }
        
    public Socket getSocket()
    {
        return this.clientSocket;
    }

    public DataInputStream getInputStream()
    {
        return this.in;
    }
      
    public DataOutputStream getOutputStream()
    {
        return this.out;
    }
    
    public XmlDocumentCreator getDocCreator()
    {
        return this.xmlDocCreator;
    }
    
    // connects to the communication
    public void connect() throws Exception
    {
        try
        {
            clientSocket = new Socket(host, port);
            openStreams();
        }
        catch(Exception e)
        {
            throw new Exception();
            //e.printStackTrace();
        }        
    }
            
    public void disconnect() throws Exception
    {
        try
        {        
            if(clientSocket.isConnected())
            {
                clientSocket.close();
                this.CloseStreams();
            }
        }
        catch(Exception e)
        {
            throw new Exception();
            //e.printStackTrace();
        }
        
    }
    
    private void openStreams() throws Exception
    {
        try
        {           
            out = new DataOutputStream( clientSocket.getOutputStream());
            in = new DataInputStream( clientSocket.getInputStream());
        }
        catch(Exception e)
        {
            throw new Exception();
            //e.printStackTrace();
        }
    }
    
    private void CloseStreams() throws Exception
    {
        try
        {
            out.close();
            in.close();
        }
        catch(Exception e)
        {
            throw new Exception();
            //e.printStackTrace();
        }
    }
    
    // send a packet with an xml request/response
    // packet_code is the packet code of the packet we are sending
    public void send(String request, int packet_code) throws Exception
    {
        try
        {             
            Utils.writeframeXML(out , packet_code, request);    
        }
        catch(Exception e)
        {
            throw new Exception();
            //e.printStackTrace();
        }
    }
                
    // receive a packet and return the xml request
    public Message receive() throws Exception
    {
        try 
        {            
            tnavframe framein = new tnavframe();            
            Utils.readframeXML(in, framein);            
                    
            return new Message(framein);            
        }
        catch(Exception e)
        {
            throw new Exception();
            //e.printStackTrace();
            //return "";
        }
        
        
    }    
    
    // p -> num of produces packets
    // c -> num of consumed packets
    // prod - > an array containing the produced packets codes
    // cons - > an array containing the consumed packets codes

    
    public void declarePackets(int p, int c, int [] prod, int[] cons, String module_name) throws Exception
    {
        try
        {            
            Utils.declareProducedConsumedPackets(out, p, c, prod, cons,module_name);
        }
        catch(Exception e)
        {
            throw new Exception();
            //e.printStackTrace();
        }
    }    
}
