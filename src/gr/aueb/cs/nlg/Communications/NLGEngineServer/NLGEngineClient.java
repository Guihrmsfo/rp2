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


import java.io.*;

import gr.aueb.cs.nlg.Utils.*;

import org.apache.log4j.Logger;            


import org.w3c.dom.*;
import gr.aueb.cs.nlg.Communications.*;
import gr.aueb.cs.nlg.Languages.*;
        
public class NLGEngineClient extends gr.aueb.cs.nlg.Communications.CommunicationModule
{    
    public static Logger logger = Logger.getLogger(NLGEngineClient.class.getName());
    

    
    public NLGEngineClient(String host, int port)
    {      
        super(host, port);
    }
        
    public String getText(int type, String ResourceURI, String UserType, String UserID, int Depth, boolean withComp, String personality) throws Exception
    {
        Document doc = this.getDocCreator().getNewDocument();
        
        Element req = doc.createElement(ProcessRequest.Request);
        req.setAttribute("RequestType" , ProcessRequest.REQ_GET_TEXT);
        doc.appendChild(req);
        
        Element TypeNode = doc.createElement(ProcessRequest.REQ_TYPE);
        TypeNode.setTextContent(type+"");
        req.appendChild(TypeNode);
        
        Element PersonalityNode = doc.createElement(ProcessRequest.REQ_Personality);
        PersonalityNode.setTextContent(personality);
        req.appendChild(PersonalityNode);
        
        Element ResourceURINode = doc.createElement(ProcessRequest.REQ_ResURI);
        ResourceURINode.setTextContent(ResourceURI);
        req.appendChild(ResourceURINode);
        
        Element UserTypeNode = doc.createElement(ProcessRequest.REQ_UserType);
        UserTypeNode.setTextContent(UserType);
        req.appendChild(UserTypeNode);
        
        Element UserIDNode = doc.createElement(ProcessRequest.REQ_UserId);
        UserIDNode.setTextContent(UserID);
        req.appendChild(UserIDNode);
                
        Element DepthNode = doc.createElement(ProcessRequest.REQ_Depth);
        DepthNode.setTextContent(Depth+"");
        req.appendChild(DepthNode);
        
        Element withCompNode = doc.createElement(ProcessRequest.REQ_WithComp);
        withCompNode.setTextContent(withComp +"");
        req.appendChild(withCompNode);
        
        
        String result = XmlUtils.getStringDescription(doc.getElementsByTagName("Request").item(0), true, "UTF-8");
        
        
        send(result, ProcessRequest.PACKETCODE_NLG_Req);
        Message msg = receive();        
        
         
        return msg.getXmlContent();
    }
    
    
    public String getText2(int type, String ResourceURI, String UserType, String UserID, int Depth, boolean withComp, String personality) throws Exception
    {
        Document doc = this.getDocCreator().getNewDocument();
        
        Element req = doc.createElement(ProcessRequest.Request);
        req.setAttribute("RequestType" , ProcessRequest.REQ_GET_TEXT);
        doc.appendChild(req);
        
        req.setAttribute(ProcessRequest.REQ_TYPE,type+"");

        req.setAttribute(ProcessRequest.REQ_Personality,personality);
        
        req.setAttribute(ProcessRequest.REQ_ResURI,ResourceURI);
        
        req.setAttribute(ProcessRequest.REQ_UserType,UserType);
        
        req.setAttribute(ProcessRequest.REQ_UserId,UserID);
        
        req.setAttribute(ProcessRequest.REQ_Depth, Depth+"");                
        
        req.setAttribute(ProcessRequest.REQ_WithComp,withComp +"");                
                        
        
        String result = XmlUtils.getStringDescription(doc.getElementsByTagName("Request").item(0), true);
        
        
        send(result, ProcessRequest.PACKETCODE_NLG_Req);
        Message msg = receive();        
                 
        return msg.getXmlContent();
    }
    
    public String ResetHistoryInteraction() throws Exception
    { 
        Document doc = this.getDocCreator().getNewDocument();
        
        Element req = doc.createElement(ProcessRequest.Request);
        req.setAttribute("RequestType" , ProcessRequest.REQ_CHI);
        doc.appendChild(req);
        
        
        String result = XmlUtils.getStringDescription(doc.getElementsByTagName(ProcessRequest.Request).item(0), true);
        
        send(result, ProcessRequest.PACKETCODE_NLG_Req);
        Message msg = receive();        
        
         
        return msg.getXmlContent();
    }
        
    
    public String createUser(String id) throws Exception
    {
        Document doc = this.getDocCreator().getNewDocument();
        
        Element req = doc.createElement(ProcessRequest.Request);
        req.setAttribute("RequestType" , ProcessRequest.REQ_CREATE_USER);
        doc.appendChild(req);
        
        
        String result = XmlUtils.getStringDescription(doc.getElementsByTagName(ProcessRequest.Request).item(0), true);
               
        send(result, ProcessRequest.PACKETCODE_NLG_Req);
        Message msg = receive();        
        
         
        return msg.getXmlContent();
    }

    public String setLanguage(String Lang) throws Exception
    {
        Document doc = this.getDocCreator().getNewDocument();
        
        Element req = doc.createElement(ProcessRequest.Request);
        req.setAttribute("RequestType" , ProcessRequest.REQ_SET_LANG);
        doc.appendChild(req);
                
        Element LangNode = doc.createElement(ProcessRequest.REQ_Lang);
        LangNode.setTextContent(Lang);
        req.appendChild(LangNode);
        
        String result = XmlUtils.getStringDescription(doc.getElementsByTagName(ProcessRequest.Request).item(0), true);
               
        send(result, ProcessRequest.PACKETCODE_NLG_Req);
        Message msg = receive();        
        
         
        return msg.getXmlContent();
    }    

    
    public static void main(String args[]) throws Exception
    {
        int type = 0;
        
        String ResURi= "http://www.aueb.gr/users/ion/mpiro.owl#exhibit1";
        String userType = "http://www.aueb.gr/users/ion/owlnl/UserModelling#Adult";
        
        //String ResURi= "http://localhost/OwlTemp.owl#temple-of-ares";
        //String userType = "http://www.aueb.gr/users/ion/owlnl/UserModelling#adult";
        
            
        String userID = "1234658";
        int depth = 1;
        boolean withComp = false;      
        
        
        //NLGEngineClient client = new NLGEngineClient("192.168.0.31", 53000);
        NLGEngineClient client = new NLGEngineClient("127.0.0.1", 53000);
        
        //NLGEngineClient client = new NLGEngineClient("192.168.0.157", 53000);
        client.connect();
                               
        int [] produced = {ProcessRequest.PACKETCODE_NLG_Req};
        int [] consumed = {ProcessRequest.PACKETCODE_NLG_Res};
            
        client.declarePackets(1, 1, produced, consumed,"AUEBNLGCLIENT");
        
        DataInputStream stream = client.getInputStream();
        
        boolean flag = true;
        
        /*
        while(flag)
        {
            byte b = stream.read
            System.err.println(b);
        }
            
         
        
        flag = true;
        int x =0;
        
        while(flag)
        {
            Message msg = client.receive();
            x++;
            String str = msg.getXmlContent();
            System.err.println(str);
            if(x==100) flag = false;
        }
        */
        
        //client.setLanguage(Languages.GREEK);
        client.setLanguage(Languages.ENGLISH);
        
        String res = "";
        for(int i = 0; i < 1; i++)
        {
            System.err.println("@@@@\n" + "sending " + i  + "@@@@");
            res = client.getText(0, ResURi, userType, userID, depth, withComp, "");
            System.err.println("@@@@\n" + res + "\n @@@@");
        }
        
        //client.createUser("1");
        
        /*
        <Request RequestType="SetLang">
            <Lang>el</Lang>
        </Request>

        */
        
        /*
        
            <?xml version="1.0" encoding="ISO-8859-7"?>
            <Request RequestType="GetText">
                <Type>0</Type>
                <Personality/>
                <ResourceURI>http://www.aueb.gr/users/ion/mpiro.owl#exhibit1</ResourceURI>
                <UserType>http://www.aueb.gr/users/ion/owlnl/UserModelling#Adult</UserType>
                <UserId/>
                <Depth>1</Depth>
                <WithComp>true</WithComp>
            </Request>
        
        */
        
                
        //System.err.println("@@@@\n" + client.setLanguage(Languages.GREEK) + "\n @@@@");
        //System.err.println("@@@@\n" + client.getText(0, ResURi, userType, userID, depth, withComp, "") + "\n @@@@");
        
        /*
            <?xml version="1.0" encoding="ISO-8859-7"?>
                <Response>
                    <Text>This is the only amphora of the collection. It was created during the archaic period, it was decorated
                 with the red-figure technique and it was painted by the Painter of Kleophrades. It dates from the
                 early 5th century B.C. It depicts a warrior performing splachnoscopy before leaving for battle. Splachnoscopy
                 is the study of animal entrails, through which people tried to predict the future. It was
                 one of the most common divination methods used in the archaic period. Today this amphora is exhibited
                 in the Martin von Wagner Museum. </Text>
                    <AnnotatedText>
                        <Period>
                            <Sentence Assim="0" Interest="1" forProperty="http://www.aueb.gr/users/ion/owlnl#type">
                                <Demonstrative ref="http://www.aueb.gr/users/ion/mpiro.owl#exhibit1" role="Owner">this</Demonstrative>
                                <VERB>is</VERB>
                                <NP ref="http://www.aueb.gr/users/ion/mpiro.owl#unique" role="Filler">the only</NP>
                                <NP ref="http://www.aueb.gr/users/ion/mpiro.owl#amphora" role="Filler">amphora</NP>
                                <TEXT>of the collection</TEXT>
                            </Sentence>
                            <Punct>.</Punct>
                        </Period>
                        ...
                    </AnnotatedText>
                </Response>
         
         */
        
        

        //System.err.println("@@@@\n" + 
        //        client.getText(0, ResURi, userType, userID, depth, withComp) + 
        //        "\n @@@@");        
        
        client.disconnect();
    }
}

