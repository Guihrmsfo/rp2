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
import java.util.Hashtable;        
import java.util.HashMap;    

import org.apache.log4j.Logger;


import com.hp.hpl.jena.vocabulary.*;

public class Structures
{ 
    static Logger logger = Logger.getLogger("gr.aueb.cs.nlg.PServerEmu.Structures");
    private UserModellingQueryManager UMQM;
    private Hashtable Users;            // [userName --> UserType]
    
    private Hashtable<String, HashMap> mentionedEntitiesCount;    // [resourceID  --- > counter]
    private Hashtable<String, HashMap> MicrplansCount;            // [microID  --- > counter] 
    private Hashtable<String, HashMap> assimilationScores;        // [factID  --- > counter] 
    
    public Structures(UserModellingQueryManager UMQM)
    {
        this.UMQM = UMQM;
        Users = new Hashtable();
        mentionedEntitiesCount = new Hashtable<String, HashMap>();
        MicrplansCount = new Hashtable<String, HashMap>();
        assimilationScores = new Hashtable<String, HashMap>();
    }
    
    public void newUser(String userName, String userType)
    {
        if(UMQM.checkUserTypeExists(userType))
        {
            if(!Users.containsKey(userName))
            {
                Users.put(userName, userType);
                logger.debug("User:" + userName  + " with userType " + userType);
            }
        }
        
    }
    
    public boolean checkUserExists(String userName)
    {
        return Users.containsKey(userName);        
    }
    
    public void deleteUser(String userName)
    {
        Users.remove(userName);
    }
        
    public String getUserTypeForUser(String UserID)
    {
        if(checkUserExists(UserID))
        {
            return Users.get(UserID).toString();
        }
        else
        {
            return null;
        }
    }
    
    public int getMentionedCount(String entityName, String userName)
    {
        HashMap userName2counter = mentionedEntitiesCount.get(entityName);
        
        if(userName2counter == null) return -1;
        
        if(userName2counter.containsKey(userName))
        {
            return Integer.parseInt(userName2counter.get(userName).toString());        
        }
        else
        {
            return 0;
        }
    }
    
    public void mentionedEntitiesCountUpdate(String entityName, String userName)
    {
        if(mentionedEntitiesCount.containsKey(entityName))
        {
            HashMap userName2counter = mentionedEntitiesCount.get(entityName);
             
            if(userName2counter.containsKey(userName))
            {                
                int c = Integer.parseInt(userName2counter.get(userName).toString());            
                c++;
                userName2counter.put(userName, c);
                mentionedEntitiesCount.put(entityName, userName2counter);                            
            }
            else
            {                
                userName2counter.put(userName, 1);
                mentionedEntitiesCount.put(entityName, userName2counter);                
            }
        }
    }
        
    public int getMicroPlanningCount(String microPlanningId, String userName)
    {
        HashMap userName2counter = MicrplansCount.get(microPlanningId);
        
        if(userName2counter == null) return 0;
        
        if(userName2counter.containsKey(userName))
        {
            return Integer.parseInt(userName2counter.get(userName).toString());
        }
        else
        {
            return 0;
        }
    }
    
    public void MicroPlanningCountUpdate(String microPlanningId, String userName) 
    {
        if(MicrplansCount.containsKey(microPlanningId))
        {
            HashMap userName2counter = MicrplansCount.get(microPlanningId);
            
            if(userName2counter.containsKey(userName))
            {
                int c = Integer.parseInt(userName2counter.get(userName).toString());        
                c++;
                userName2counter.put(userName, c);            
                MicrplansCount.put(microPlanningId, userName2counter);
            }
            else
            {          
                userName2counter.put(userName, 1); 
                MicrplansCount.put(microPlanningId, userName2counter);
            }
                          
        }
        else
        {
            logger.debug("@" + microPlanningId + " :Microplan ID is not FOUND");
        }
    }
    
    public float getAssimilationFact(String factId, String ClassURI, String userName)
    {
        logger.debug("getAssimilationFact" + factId);
        
        HashMap userName2counter = assimilationScores.get(factId);
        
        if(userName2counter == null) 
        {
            logger.debug("getAssimilationFact not found " + factId);
            return 0;
        }
        
        if(userName2counter.containsKey(userName))
        {            
            return Float.parseFloat(userName2counter.get(userName).toString());
        }
        else
        {            
            return 0;
        }        
    }
    
    public void AssimilationUpdate(String factId, String userName) 
    {        
        //logger.debug("Assimilation Update:" + factId);
        
        HashMap userName2counter = null;
        float c = 0;
                         
        if(assimilationScores.containsKey(factId))
        {
            userName2counter = assimilationScores.get(factId);
            
            if(userName2counter.containsKey(userName))
            {                
                c = Float.parseFloat(userName2counter.get(userName).toString()); 
            }
            else
            {                    
                c = 0; 
            }
            
            int index1 = factId.indexOf("," , 0);
            int index2 = factId.indexOf("," , index1 +2);

            //logger.debug("index1:" + index1 + " index2:" + index2);

            String subURI = factId.substring(1, index1);             
            String PrpURI = factId.substring(index1 + 2, index2);
            String objURI = factId.substring(index2 + 2, factId.length()-1);       

            int reps  = 0;
                    
            if(PrpURI.compareTo("http://www.w3.org/1999/02/22-rdf-syntax-ns#type") == 0 
            || PrpURI.compareTo(RDFS.subClassOf.toString()) == 0)
            {
                reps = UMQM.getCRepetitions(objURI, subURI, getUserTypeForUser(userName)); 
            }
            else
            {
                reps = UMQM.getRepetitions(PrpURI, subURI , getUserTypeForUser(userName));
            }
            
                        
            //if reps == 0 then the assimlilation is not increased
            
            float assimRate = 0;
            
            if(reps != 0)
            assimRate = (1.0F / reps);             
            
            //logger.debug("reps: " +  reps + "Assimilation Rate: " + assimRate);

            c = c + assimRate;

            userName2counter.put(userName, c);        
            assimilationScores.put(factId, userName2counter);
            //logger.debug("Assimilation Update1: " + userName + " " + c + " " + factId);
              
        }
        else        
        {
            int index1 = factId.indexOf("," , 0);
            int index2 = factId.indexOf("," , index1 +2);

            //logger.debug("index1:" + index1 + " index2:" + index2);

            String subURI = factId.substring(1, index1);             
            String PrpURI = factId.substring(index1 + 2, index2);
            String objURI = factId.substring(index2 + 2, factId.length()-1);   
            
            c = 1;
            
            userName2counter = new HashMap();
            
            userName2counter.put(userName, c);        
            assimilationScores.put(factId, userName2counter);
            
            logger.debug("Assimilation Update2: " + userName + " " + c + " " + factId);
            
            logger.debug("Assimilation Update2: " + assimilationScores.get(factId).get(userName).toString());
            
        }
        
    }
    
    public UserModellingQueryManager getUserModellingQueryManager()
    {
        return UMQM;
    }
    
    public void initialize(String[] entityNames, String[] factIds, String[] microPlanIds) 
    {
        mentionedEntitiesCount = new Hashtable<String, HashMap>();
        MicrplansCount = new Hashtable<String, HashMap>();
        assimilationScores = new Hashtable<String, HashMap>();
                
       for(int i = 0; i < entityNames.length; i++)
       {
           HashMap HM = new HashMap();          
           //logger.debug("initialize:" + entityNames[i]);
           mentionedEntitiesCount.put(entityNames[i], HM);
       }
       
       for(int i = 0; i < factIds.length; i++)
       {
           HashMap HM = new HashMap();          
           logger.debug("initialize:" + factIds[i]);
           assimilationScores.put(factIds[i], HM);                      
       }
       
       for(int i = 0; i < microPlanIds.length; i++)
       {
           HashMap HM = new HashMap();          
           //logger.debug("initialize:" + microPlanIds[i]);
           MicrplansCount.put(microPlanIds[i], HM);                      
       }       
    }
    
    public void  resetPServer(String resourceURI, String facts[][], String userID)
    {
        if(mentionedEntitiesCount.containsKey(resourceURI)) // 
        {
            HashMap userName2counter = mentionedEntitiesCount.get(resourceURI);
            if(userName2counter != null)
            {
                if(userName2counter.containsKey(userID))
                {
                    int c = Integer.parseInt(userName2counter.get(userID).toString());                    
                    if(c > 1)
                    {   
                        c--;                    
                        userName2counter.put(userID, c);
                        mentionedEntitiesCount.put(resourceURI, userName2counter);
                    }
                }
            }
        }
        
        for(int i = 0; i < facts.length; i++)
        {
            String fact = facts[i][0];
            String Assimscore = facts[i][1];
               
            logger.debug(resourceURI +" reset: " + fact + " " + Assimscore);
            
            if(assimilationScores.containsKey(fact))
            {
                HashMap userName2counter = assimilationScores.get(fact);
                if(userName2counter != null)
                {
                    if(userName2counter.containsKey(userID))
                    {
                        Float c = Float.parseFloat(userName2counter.get(userID).toString());    
                        c = Float.parseFloat(Assimscore);

                        userName2counter.put(userID, c);
                        assimilationScores.put(fact, userName2counter);                        
                        logger.debug(resourceURI +" reset: " + fact + " " + Assimscore);
                    }
                }
            }
        }
                
    }
             
}
