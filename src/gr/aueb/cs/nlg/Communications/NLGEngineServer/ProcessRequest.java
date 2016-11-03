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
import org.w3c.dom.*;
import gr.aueb.cs.nlg.NLGEngine.*;
import gr.aueb.cs.nlg.Utils.*;

import org.apache.log4j.Logger;            
import gr.aueb.cs.nlg.Languages.Languages;

public class ProcessRequest
{
    private XmlDocumentCreator xmlDocCreator ;
    
    public static String REQ_TYPE = "Type";
    public static String REQ_ResURI = "ResourceURI";
    public static String REQ_UserType = "UserType";    
    public static String REQ_UserId = "UserId";
    public static String REQ_Depth = "Depth";
    public static String REQ_WithComp = "WithComp";
    public static String REQ_Lang = "Lang";
    public static String REQ_Personality = "Personality";
    
    public static String Request = "Request";
    public static String Response = "Response";
    
    public static String RequestType = "RequestType";
    public static String REQ_GET_TEXT = "GetText";
    public static String REQ_CHI = "ClearHistoryInteraction";
    public static String REQ_CREATE_USER = "CreateUser";
    public static String REQ_SET_LANG = "SetLang";
    
    public static final int PACKETCODE_NLG_Req = 871;
    public static final int PACKETCODE_NLG_Res = 872;
    
    
    Logger logger = Logger.getLogger(ProcessRequest.class.getName());
    
    public ProcessRequest()
    {   
        xmlDocCreator = new XmlDocumentCreator();
    }
    
    public String terms = "";
    
    
    public String processRequest(String request, NLGEngineServer nlgserv,NLGEngine myEngine)
    {
        return processRequest( request, nlgserv, myEngine, null);
    }
    
    public String processRequest(String request, NLGEngineServer nlgserv, NLGEngine myEngine, String grammar)
    {
        try
        {
                    
        logger.info("process the request: " + request);
        
        String result[];
        Document ResponseDoc = xmlDocCreator.getNewDocument();
        
        Node ResponseNode = ResponseDoc.createElement(Response);
        ResponseDoc.appendChild(ResponseNode);
                
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(request.getBytes("UTF-8"));
        Document requestdoc = xmlDocCreator.parse(byteArrayInputStream);
        
        Node requestNode = requestdoc.getElementsByTagName(Request).item(0);
        
        NamedNodeMap NNM  =requestNode.getAttributes();
        String requestType = NNM.getNamedItem(RequestType).getTextContent();
        
        if(requestType.compareTo(REQ_CHI) ==0 )
        {
            logger.info("processing a REQ_CHI");
            //myEngine.initPServer();
            myEngine.initStatisticalTree();
        }
        else if(requestType.compareTo(REQ_GET_TEXT) ==0)
        {
            logger.info("processing a REQ_GET_TEXT");
                 
            int type = 0;
            String ResourceURI = "";
            int Depth = 1;
            String UserType = "";
            String UserId = "";
            String personality = "";
            
            boolean withComp = false;
                
            for(int i = 0; i < requestNode.getChildNodes().getLength(); i++)
            {
                Node param = requestNode.getChildNodes().item(i);

                        
                if(param.getNodeName().equals(REQ_TYPE))
                {
                     if(param.getTextContent().equals(""))
                        type = 0;
                    else
                        type = Integer.parseInt(param.getTextContent());                    
                }
                else if(param.getNodeName().equals(REQ_ResURI))
                {
                   if(param.getTextContent().equals(""))
                        ResourceURI = "";
                    else
                        ResourceURI = param.getTextContent();                    
                }
                else if(param.getNodeName().equals(REQ_UserType))
                {
                    if(param.getTextContent().equals(""))
                        UserType = null;
                    else
                        UserType = param.getTextContent();             
                }                
                else if(param.getNodeName().equals(REQ_UserId))
                {
                    if(param.getTextContent().equals(""))
                        UserId = null;
                    else
                        UserId = param.getTextContent();                      
                }                                
                else if(param.getNodeName().equals(REQ_WithComp))
                {
                    if(param.getTextContent().equals(""))
                        withComp = false;
                    else
                    {
                        if(param.getTextContent().equals("true"))
                            withComp = true;                     
                        else
                            withComp = false;
                    }
                }             
                else if(param.getNodeName().equals(REQ_Depth))
                {
                     if(param.getTextContent().equals(""))
                        Depth = 1;
                    else
                        Depth = Integer.parseInt(param.getTextContent());                       
                }                   
                else if(param.getNodeName().equals(this.REQ_Personality))
                {
                     if(param.getTextContent().equals(""))
                        personality = "";
                    else
                        personality = param.getTextContent();                       
                }                
            }
           
            logger.info("asking a text from the NLG engine");
                           
            if(myEngine.useEmulator() && UserId !=null && !UserId.equals(""))
            {
                
                
                if(!myEngine.getUMVisit().checkUserExists(UserId))
                {
                    logger.info("but before that i have to create a user");
                    myEngine.getUMVisit().newUser(UserId, UserType);                    
                }
            }
                        
            result = myEngine.GenerateDescription(type, ResourceURI, UserType, UserId, Depth, -1, withComp,personality);
            
            logger.info("I got the text...");
            
            logger.info("this the text..\n");
            
            logger.info(result[0]);                       
            logger.info(result[1]);            
            logger.info(result[2]);

            
            Element Text = ResponseDoc.createElement("Text");
            Text.setTextContent(result[1]);
            ResponseNode.appendChild(Text);
            
            Node annText = myEngine.getAnnotatedText().getRoot().cloneNode(true);
            ResponseDoc.adoptNode(annText);
            ResponseNode.appendChild(annText);
            
            /* new code */
            terms = "";
            NodeList listofRefsToThingsThatUserCanAskFor = ResponseDoc.getElementsByTagName("NP");
            
            String HTML_TEXt = result[1];
            int k = 0;
            for(int i = 0; i < listofRefsToThingsThatUserCanAskFor.getLength(); i++)
            {
                Node NP = listofRefsToThingsThatUserCanAskFor.item(i);
                String NPTextContent = NP.getTextContent();
                String uri = ((Element)NP).getAttribute("ref");
                
                String nominativeNP = myEngine.getLexicon().getEntry(uri, XmlMsgs.NOMINATIVE_TAG, myEngine.getLang(), null , 0);
                
                boolean i_got_it = false;
                if(nominativeNP!=null && !nominativeNP.equals(""))
                {
                    i_got_it = true;
                    k++;
                    NPTextContent = nominativeNP;
                    int start = HTML_TEXt.indexOf(NPTextContent);
                    int end = start + NPTextContent.length();
                        
                    if(k == 0)
                    {
                        terms = NPTextContent;
                    }
                    else
                    {
                        terms = terms + "\n"  + NPTextContent;
                    }
                        
                    if(start!=-1)
                    HTML_TEXt = HTML_TEXt.substring(0,start) + "<b>" + NPTextContent + "</b>" + HTML_TEXt.substring(end,HTML_TEXt.length()) ;
                }
                
                String accuNP = myEngine.getLexicon().getEntry(uri, XmlMsgs.ACCUSATIVE_TAG, myEngine.getLang(), null , 0);
                
                if(!i_got_it)
                {
                    
                    if(accuNP!=null && !accuNP.equals(""))
                    {
                        i_got_it = true;
                        k++;
                        NPTextContent = accuNP;
                        int start = HTML_TEXt.indexOf(NPTextContent);
                        int end = start + NPTextContent.length();

                        if(k == 0)
                        {
                            terms = NPTextContent;
                        }
                        else
                        {
                            terms = terms + "\n"  + NPTextContent;
                        }

                        if(start!=-1)
                        HTML_TEXt = HTML_TEXt.substring(0,start) + "<b>" + NPTextContent + "</b>" + HTML_TEXt.substring(end,HTML_TEXt.length()) ;
                    }                    
                }
            }
           
            
            logger.info("<html>" + HTML_TEXt + "</html>");
            String htmlText = "<html>" + HTML_TEXt + "</html>";
            
            //Document html = xmlDocCreator.parse(  new ByteArrayInputStream(htmlText.getBytes("UTF-8")) );
            //Node HTMLNODE = html.getElementsByTagName("html").item(0);
            
            Element HTML_Text = ResponseDoc.createElement("HTML");
            //HTML_Text.setTextContent(HTML_TEXt);
            //ResponseNode.appendChild(HTML_Text);
            
            //ResponseDoc.adoptNode(HTMLNODE);
            //ResponseNode.appendChild(HTMLNODE);
                    
            //Text.setTextContent("<html>" + HTML_TEXt + "</html>");
            
            
             /* new code */
            
            if(grammar!=null)
            {
                Element Grammar = ResponseDoc.createElement("Grammar");
                ResponseNode.appendChild(Grammar);
            }
            
            if(myEngine.AllFactsAreAssimilated())
            {
                Text.setAttribute("AllFactsAssimilated", "true");
            }
            else
            {
                Text.setAttribute("AllFactsAssimilated", "false");
            }            
        }
        else if(requestType.compareTo(REQ_CREATE_USER) ==0)
        {
            logger.info("processing a REQ_CREATE_USER");
        }        
        else if(requestType.compareTo(this.REQ_SET_LANG) ==0)
        {
            logger.info("processing a REQ_SET_LANG");
            
            for(int i = 0; i < requestNode.getChildNodes().getLength(); i++)
            {
                Node param = requestNode.getChildNodes().item(i);

                String lang = Languages.ENGLISH;
                
                if(param.getNodeName().equals(this.REQ_Lang))
                {
                     if(!param.getTextContent().equals(""))
                        lang = param.getTextContent();                    
                     
                     myEngine.setLanguage(lang);
                }
            }
            
            Element Text = ResponseDoc.createElement("STATUS");
            Text.setTextContent("OK");
            ResponseNode.appendChild(Text);            
        }
            
        return XmlUtils.getStringDescription(ResponseNode,true,"UTF-8");
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return "Process Request ERROR......";
        }
    }
    
    
    public String processRequest2(String request, NLGEngine myEngine)
    {
        try
        {                    
            logger.info("processing the request: " + request);

            String result[];
            Document ResponseDoc = xmlDocCreator.getNewDocument();

            Node ResponseNode = ResponseDoc.createElement(Response);
            ResponseDoc.appendChild(ResponseNode);

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(request.getBytes("UTF-8"));
            Document requestdoc = xmlDocCreator.parse(byteArrayInputStream);

            Node requestNode = requestdoc.getElementsByTagName(Request).item(0);

            NamedNodeMap NNM  =requestNode.getAttributes();
            String requestType = NNM.getNamedItem(RequestType).getTextContent();

            if(requestType.compareTo(REQ_CHI) ==0 )
            {
                logger.info("processing a REQ_CHI");
                //myEngine.initPServer();
                myEngine.initStatisticalTree();
            }
            else if(requestType.compareTo(REQ_GET_TEXT) ==0)
            {
                logger.info("processing a REQ_GET_TEXT");

                int type = 0;
                String ResourceURI = "";
                int Depth = 1;
                String UserType = null;
                String UserId = null;
                String personality = "";

                boolean withComp = false;

                Element RequestElement = (Element)requestNode;

                type = Integer.parseInt(RequestElement.getAttribute(REQ_TYPE));
                ResourceURI = RequestElement.getAttribute(REQ_ResURI);
                UserType = RequestElement.getAttribute(REQ_UserType);
                UserId = RequestElement.getAttribute(REQ_UserId);
                personality = RequestElement.getAttribute(REQ_Personality);
                Depth = Integer.parseInt(RequestElement.getAttribute(REQ_Depth));

                if(RequestElement.getAttribute(REQ_WithComp).equals("true"))
                   withComp = true;     


                if(UserId.equals(""))
                    UserId = null;

                logger.info("asking a text from the NLG engine");

                result = myEngine.GenerateDescription(type, ResourceURI, UserType, UserId, Depth, -1, withComp,personality);

                logger.info("I got the text...");

                Element Text = ResponseDoc.createElement("Text");
                Text.setTextContent(result[1]);
                ResponseNode.appendChild(Text);

                logger.info("...");

                Node annText = myEngine.getAnnotatedText().getRoot().cloneNode(true);
                ResponseDoc.adoptNode(annText);
                ResponseNode.appendChild(annText);
                


                if(myEngine.AllFactsAreAssimilated())
                {
                    Text.setAttribute("AllFactsAssimilated", "true");
                }
                else
                {
                    Text.setAttribute("AllFactsAssimilated", "false");
                }

                logger.info("...");
            }
            else if(requestType.compareTo(REQ_CREATE_USER) ==0)
            {
                logger.info("processing a REQ_CREATE_USER");
            }        
            else if(requestType.compareTo(this.REQ_SET_LANG) ==0)
            {
                logger.info("processing a REQ_SET_LANG");

                for(int i = 0; i < requestNode.getChildNodes().getLength(); i++)
                {
                    Node param = requestNode.getChildNodes().item(i);

                    String lang = Languages.ENGLISH;

                    if(param.getNodeName().equals(this.REQ_Lang))
                    {
                         if(!param.getTextContent().equals(""))
                            lang = param.getTextContent();                    

                         myEngine.setLanguage(lang);
                    }
                }

                Element Text = ResponseDoc.createElement("STATUS");
                Text.setTextContent("OK");
                ResponseNode.appendChild(Text);            
            }

            return XmlUtils.getStringDescription(ResponseNode,true);
        }
        catch(java.io.UnsupportedEncodingException e)
        {
            return "ERROR-UnsupportedEncodingException";
        }
        catch(Exception e)
        {
            return "ERROR";
        }        
    }    
}
