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

package gr.aueb.cs.nlg.Communications.NLGEngineServer;

import gr.aueb.cs.nlg.NLGEngine.*;
import java.util.Random;
import java.math.BigDecimal;

import org.apache.log4j.Logger;

     
import gr.aueb.cs.nlg.Communications.*;
import java.nio.charset.Charset;
    
public class NLGEngineServer extends Thread
{
    static Logger logger = Logger.getLogger(NLGEngineServer.class.getName());
    
    public static final int PACKETCODE_localizer = 212;

        
    public double X;
    public double Y;
    public double oldX;
    public double oldY;
    
    
    public int direction;
    public int oldDirection;
    
    boolean flagout;
            
    private gr.aueb.cs.nlg.Communications.NLGEngineServer.ProcessRequest NLG_PR;    
    private NLGEngine myEngine;
            
    private boolean produceFollowUpGrammars;
    
    CommunicationModule client;

    public NLGEngineServer(NLGEngine myEngine/*, NLI nli*/)
    {           
        NLG_PR = new gr.aueb.cs.nlg.Communications.NLGEngineServer.ProcessRequest();
        client = new CommunicationModule();
        this.myEngine = myEngine;
    }
    
    
    public NLGEngineServer(String nav_server_IP, int port, NLGEngine myEngine/*, NLI nli*/)
    {        
        this.myEngine = myEngine;
        NLG_PR = new gr.aueb.cs.nlg.Communications.NLGEngineServer.ProcessRequest();
        client = new CommunicationModule(nav_server_IP, port);        
    }
    
    public void die() throws Exception
    {
        flagout = true;
        client.disconnect();
    }
    
    public void setProduceFollowUpGrammars(boolean b)
    {
        this.produceFollowUpGrammars = b;
    }
    
    public void setDefaultValues()
    {
        this.X = 0;
        this.Y = 0;
        this.oldX = 0;
        this.oldY = 0;
    }
    
    public void  run()
    {        
        logger.info("Trying to connect to Communications Server");
        
        try
        {                        
            flagout = false; 
            client.connect();
        }
        catch (Exception e)
        {
            logger.info("Couldn't get I/O for the connection to server");
            
            this.X = 0;
            this.Y = 0;
            this.direction = 0;
            flagout = true;
        }
        
        this.oldX = 0;
        this.oldY = 0;
        
        // initially sends module information to server
        logger.debug("Sending Module Information to server");
        try
        {            
            int [] produced = {ProcessRequest.PACKETCODE_NLG_Res,998};
            int [] consumed = {ProcessRequest.PACKETCODE_NLG_Req, PACKETCODE_localizer};
            
            client.declarePackets(2, 2, produced, consumed, "NLGSERVER");             
        }
        catch (Exception e)
        {
            logger.info("Couldn't write module info");          
            flagout=true; 
        }
        
              
        int mpstate=0;
        Random random = new Random();
        
        if(!flagout)
        logger.info("NGLEngineServer: I was succesfully connected to Communication Server");
        
        String result = "";
                
        try
        {
                    
            while (!flagout)
            {

                Message msg = null;

                int packetcode = -1;
                // blocks here and tries to read input from server
                // put in a tseparate hread if you dont want to block
                //logger.info("Waiting...");

                try
                {
                    msg = client.receive();
                    //logger.info("a message was read!!!");
                }
                catch (Exception e)
                {
                    flagout = true; 
                    logger.info("Couldn't read frame");
                }
                
                 packetcode = msg.getPacketCode();
                //logger.info("packetcode: " + packetcode);
               

                switch (packetcode)
                {
                    case PACKETCODE_localizer:
                        

                        X = roundDec(msg.frame.d[0], 2);
                        Y = roundDec(msg.frame.d[1], 2);
                        direction = (int)(msg.frame.d[2] * 57.2958);

                        System.err.println("eimai kapoy ekei X: "+ X + " Y: " + Y+ " kai koitw pros " + direction);
                        
                        break;

                    case ProcessRequest.PACKETCODE_NLG_Req:                    
                        logger.info("a NLG Request message was read!!!");
                        
                        String xmlRequest = msg.getXmlContent();
                        
                        if(produceFollowUpGrammars)
                        {
                            String grammar = "gramm";
                            result = NLG_PR.processRequest(xmlRequest, this ,myEngine, grammar);
                        }
                        else
                        {
                            result = NLG_PR.processRequest(xmlRequest, this , myEngine);
                        }
                        
                        logger.info("\n\n\nresult:" + result);
                        client.send(result, ProcessRequest.PACKETCODE_NLG_Res);
                        
                        logger.info("NLG_PR.terms" + NLG_PR.terms);
                        
                         
                        String myTerms = new String( (NLG_PR.terms + "\n").getBytes("UTF-8"), Charset.forName("UTF-8"));
                        client.send(myTerms,998);
                                
                        break;                     

                    default:
                        logger.info("******Something wrong happened. Packet code not recognized!!");
                }//switch
            } // while
        }
        catch(Exception e)
        {
            logger.info("NLG Engine: Problem on sending or receiving");
            logger.info("sending: " + result);
        }
        
        try
        {
            client.disconnect();
            logger.info("NLGEngine server disconnected");
        }
        catch (Exception e)
        {
            logger.info("Couldn't close connection");
        }
    } // main
    
    
    public static double roundDec(double num, int Dec)
    {
        BigDecimal bd = new BigDecimal(num);
        bd = bd.setScale(Dec,BigDecimal.ROUND_UP);
        return bd.doubleValue();
    }
    
} // nav_dm