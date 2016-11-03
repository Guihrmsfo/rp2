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

package gr.aueb.cs.nlg.PServerEmu;

import gr.aueb.cs.nlg.NLFiles.*;

import gr.demokritos.iit.PServer.UMException;

import com.hp.hpl.jena.vocabulary.*;


import org.apache.log4j.Logger;

    
public class UMVisitImpEmu implements gr.demokritos.iit.PServer.UMVisit
{
    static Logger logger = Logger.getLogger("gr.aueb.cs.nlg.PServerEmu.UMVisitImpEmu");
    
    //personalization server emulation variables
    Structures s;  //the main memory structures holding personilization data

    //initializers
    public UMVisitImpEmu(Structures s) 
    { 
        this.s = s;
    }

    public void newUser(String userName, String userType) throws UMException
    {
       s.newUser(userName, userType);
    }

    public void newUser(String userName, String userType, String targetLanguage) throws UMException
    {

    }

    public boolean checkUserExists(String userName) throws UMException
    {
       return s.checkUserExists(userName);
    }

    public void deleteUser(String userName) throws UMException
    {
        s.deleteUser(userName);   
    }

    public float getInterestFact(String factId, String Class, String userName, String userType, String Robot_Personality)
    {
       Robot_Personality = "do nothing";
               
       int index1 = factId.indexOf("," , 0);
       int index2 = factId.indexOf("," , index1 +2);

       logger.debug("index1:" + index1 + " index2:" + index2);

       String subURI = factId.substring(1, index1);             
       String PrpURI = factId.substring(index1 + 2, index2);
       String objURI = factId.substring(index2 + 2, factId.length()-1);   
        
       logger.debug("getInterestFact factId=>" + factId);
       
       if(PrpURI.compareTo("http://www.w3.org/1999/02/22-rdf-syntax-ns#type") == 0 
          || PrpURI.compareTo(RDFS.subClassOf.toString()) == 0)
       {
           return s.getUserModellingQueryManager().getCInterest(Class, subURI, s.getUserTypeForUser(userName)); 
       }
       else
       {
           //logger.debug("PrpURI=>" + PrpURI);
           //logger.debug("subURI=>" + subURI);
           
           return s.getUserModellingQueryManager().getInterest(PrpURI, subURI, s.getUserTypeForUser(userName));
       }
    }

    public float getMicroPlanningAppropriateness(String microPlanningId, String userName, String userType) 
    {
       return s.getUserModellingQueryManager().getAppropriateness(microPlanningId, s.getUserTypeForUser(userName));
    }

    public int[] getMicroPlanningCount(String microPlanningIds[], String userName) throws UMException
    {

       int result[] = new int[microPlanningIds.length];
       for(int i = 0; i < result.length; i++)
       {
           result[i] = s.getMicroPlanningCount( microPlanningIds[i], userName);
       }

       return result;        
    }

    public int [] getMicroPlanningAppropriateness(String microPlanningIds[], String userName)
    {
       UserModellingQueryManager UMQM = s.getUserModellingQueryManager();

       int result[] = new int[microPlanningIds.length];
       for(int i = 0; i < result.length; i++)
       {
           result[i] = UMQM.getAppropriateness(microPlanningIds[i], s.getUserTypeForUser(userName));
       }

       return result;
    }

    public int getNumberOfFacts(String userName, String userType) throws UMException
    {
        return s.getUserModellingQueryManager().getParametersForUserType(s.getUserTypeForUser(userName)).getFactsPerPage();
    }

    public int getMaxFactsPerSentence(String userName, String userType) throws UMException
    {
       return s.getUserModellingQueryManager().getParametersForUserType(s.getUserTypeForUser(userName)).getMaxFactsPerSentence();
    }

    public int getNumberOfForwardPointers(String userName) throws UMException
    {
       return -1;
    }

    public int getVoice(String userName, String userType) throws UMException
    {
       return Integer.parseInt(s.getUserModellingQueryManager().getParametersForUserType(s.getUserTypeForUser(userName)).getSynthesizerVoice());
    }

    public void setTargetLanguage(String targetLanguage, String userName) throws UMException
    {

    }

    public String getTargetLanguage(String userName) throws UMException
    {
       return "NOT SUPPORTED";
    }

    public void changeUserType(String userName, String userType) throws UMException
    {

    }

    public String getUserType(String userName) throws UMException
    {
        return s.getUserTypeForUser(userName);
    }

    public float getAssimilationScore(String factId, String ClassURI, String userName, String userType) throws UMException
    {
       return s.getAssimilationFact(factId, ClassURI, userName);
    }

    public float[] getAssimilationFacts(String factIds[], String Classes[], String userName)
    {
        return new float[1];
    }

    public int getMentionedCount(String entityName, String userName) throws UMException
    {
        return s.getMentionedCount(entityName, userName);
    }

    public int[] getMentionedCount(String entityNames[], String userName)
    {
       int result[] = new int[entityNames.length];
       for(int i = 0; i < result.length; i++)
       {
           result[i] = s.getMentionedCount(entityNames[i], userName);
       }

       return result;           
    }

    public int getMicroPlanningCount(String microPlanningId, String userName, String userType) throws UMException
    {
        return s.getMicroPlanningCount(microPlanningId, userName);
    }

    public void update(String[] mentionedEntities, String[] conveyedFacts,
                      String[] conveyedFunctors, String[] usedMicroPlanningExpressions,
                      String userName) throws UMException
    {

       for(int i = 0; i < mentionedEntities.length; i++)
       {
            s.mentionedEntitiesCountUpdate(mentionedEntities[i], userName);   
       }

       for(int i = 0; i < usedMicroPlanningExpressions.length; i++)
       {
           s.MicroPlanningCountUpdate(usedMicroPlanningExpressions[i], userName);
       }
       
       for(int i = 0; i < conveyedFacts.length; i++)
       {
           s.AssimilationUpdate(conveyedFacts[i],userName);
           logger.debug("Passing" + conveyedFacts[i] + "to AssimilationUpdate");
       }
    }

    public void update(String[] InstanceURI, String[] Facts, String[] MicroplanURI, String userName, String userType) throws UMException
    {
        try
        {
            update(InstanceURI, Facts, new String[1], MicroplanURI, userName);
        }
        catch(Exception e)
        {            
            e.printStackTrace();
        }
    }
    
    
    public void setNewFocus(String entityName, String userName) throws UMException
    {

    }

    public String getCurrentFocus(String userName) throws UMException
    {
       return "NOT SUPPORTED";
    }  

    public String[] getPreviousFoci(String userName) throws UMException
    {
       return new String[3];
    }

    public int getFocusCount(String entityName, String userName) throws UMException
    {
       return -1;
    }

    public String[] getForwardPointers(String userName) throws UMException
    {
       return new String[3];
    }
     
    public void  resetPServer(String resourceURI, String facts [][], String userID)
    {
        s.resetPServer(resourceURI, facts, userID); 
    }
}
