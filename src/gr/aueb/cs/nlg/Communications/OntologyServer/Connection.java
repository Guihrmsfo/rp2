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

import java.net.*;
import java.io.*;

import org.apache.log4j.Logger;            
import org.apache.log4j.Level;

class Connection extends Thread
{
    DataInputStream in;
    DataOutputStream out;
    Socket clientSocket;
    OntologyModel OM;
    DialogueManagerInfo DMI;
            
    Logger logger = Logger.getLogger(Connection.class.getName());
    
    public Connection(Socket aClientSocket, OntologyModel OM, DialogueManagerInfo DMI)
    {
        try
        {
            
            logger.setLevel(Level.DEBUG);
            this.OM = OM;
            this.DMI = DMI;
            clientSocket = aClientSocket;
            in = new DataInputStream( clientSocket.getInputStream());
            out =new DataOutputStream( clientSocket.getOutputStream());
            this.start();
        }
        catch(IOException e)
        {
            System.out.println("Connection:"+e.getMessage());
        }
    }
    
    public void run()
    {
        try
        {			                
            
            logger.info(  this.getId()  + " thread started...." ); 
            
            String data = in.readUTF();	        // read request          
            
            logger.info(this.getId() + " ECHO request...." + "\n" +  data + "\n" + "++++");                            
           
            String result = "";
            //String result = processRequest(data, OM, DMI);
            
            logger.info(this.getId() + " ECHO result...." + "\n" + result + "\n" + "++++");                
            out.writeUTF(result);
            
            in.close();
            out.close();
            clientSocket.close();
            
            logger.info(this.getId() + " thread terminated...."); 
        }
        catch (EOFException e)
        {
            System.out.println("EOF:"+e.getMessage());        
        }
        catch(IOException e)
        {
            System.out.println("readline:"+e.getMessage());
        }        
        finally
        { 
          try        
          {
              clientSocket.close();
          }        
          catch (IOException e)
          {
              logger.info("close failed");
          }
        }                
    }
}