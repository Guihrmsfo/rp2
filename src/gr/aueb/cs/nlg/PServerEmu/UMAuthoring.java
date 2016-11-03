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
import gr.demokritos.iit.PServer.*;
 
/** 
* Interface specifying the API that M-PIRO's user modeling subsystem (aka "personalisation server")
* supports for authoring purposes. Unlike UMVisit, this interface assumes that certain user 
* modelling parameters will be handled via user types. 
* This interface is to be implemented by a class whose objects will all act as pointers to the 
* same (a single) personalisation server. 
* For simplicity, the initial assimilation of all facts and entities is always 0. This can 
* change in next versions of the UM subsystem. At the moment, we can set the interest or 
* importance of facts/entities to low values to avoid mentioning things users already know.
* Methods for spatial parameters, parameters to control how much we try to generate comparisons, 
* and decay models to be included in future versions. 
* @author Ion Androutsopoulos (ionandr@iit.demokritos.gr)
* @version 0.6
**/
public interface UMAuthoring 
{ 

// ************* methods to initialise the UM subsystem and to create user types ***********
         
   /**
   * Erase all the information in the personalisation server (both stereotypes and user
   * profiles), and prepare it to receive new information via the UMAuthoring interface.
   * @param entityNames An array containing the identifiers of all the entities.
   * @param factIds An array containing the identifiers of all the facts.
   * @param microPlanIds An array containing the identifiers of all the micro plans.
   * @param wordIds An array containing the identifiers of all the words.
   * @throws UMException() if anything goes wrong.
   **/
   public void initialize(String[] entityNames, String[] factIds, String[] microPlanIds) throws UMException;
   
   /**
   * Create a new user type.
   * @param userType The name of the new user type.
   * @throws UMException() if anything goes wrong.   
   **/
   public void newUserType(String userType) throws UMException;
   

// ************************* interest, importance, assimilation rate ***********************
// These are all parts of the stereotypes, and they are set for individual facts or functors.

   public void setDefaultInterest(String propertyURI, float interest, String userType);
   
   public void setClassInterest(String propertyURI,String ClassURI ,float interest, String userType);
      
   public void setInstanceInterest(String propertyURI,String InstanceURI ,float interest, String userType);
   
   
   public void setAssimilationRateFact(String factId, float assimilationRate, String userType) throws UMException;   
   
   /** 
   * Set the assimilation rate of all the facts with a particular functor for a user type.
   * @param functor The functor of the facts we want to set the assimilation rate of.
   * @param assimilationRate The new assimilation rate of the facts. This must be a non-negative float.
   * Larger values indicate larger degrees of assimilation. 
   * @param userType The user type. 
   * @throws UMException() if anything goes wrong.   
   **/
   public void setAssimilationRateFunctor(String functor, float assimilationRate, String userType) throws UMException;        

   /**
   * Set the initial assimilation of a fact for the specified user type.
   * @param factId The identifier of the fact we want to set the assimilation of.
   * @param initAssimilation The new initial assimilation of the fact. This must be a non-negative float.
   * Larger values indicate larger degrees of assimilation.
   * @param userType The user type.
   * @throws UMException() if anything goes wrong.
   **/
   public void setInitialAssimilationFact(String factId, float initAssimilation, String userType) throws UMException;

   /**
   * Set the initial assimilation of all the facts with a particular functor
   * for the specified user type.
   * @param functor The functor of the facts we want to set the assimilation of.
   * @param initAssimilation The new initial assimilation of the facts. This must be a non-negative float.
   * Larger values indicate larger degrees of assimilation.
   * @param userType The user type.
   * @throws UMException() if anything goes wrong.
   **/
   public void setInitialAssimilationFunctor(String functor, float initAssimilation, String userType) throws UMException;


// ************************* assimilation: direct assignment ***********************
// Override UMVisit.update() and assign a value for the assimilation of fact(s)
// directly, in the personal profiles of users (not the stereotypes).

   /**
   * Set the assimilation of specified fact(s) for all users of the specified user type.
   * @param factIds The identifier(s) of the fact(s) we want to set the assimilation of.
   * @assimilation The new assimilation of the fact(s). This must be a non-negative float.
   * Larger values indicate larger degrees of assimilation.
   * @param userType The user type.
   * @throws UMException() if anything goes wrong.
   **/
   public void setAssimilationFact(String[] factIds, float assimilation, String userType) throws UMException;


// *************** appropriateness of words, micro-planning expressions, schemata ***********************
// These are all parts of the stereotypes, and they are set for individual words, micro-planning 
// expressions or schemata. (Unlike interest, importance, and assimilation rate, here there is no 
// equivalent of setting the values for entire entity types or functors.)
     
   /** 
   * Set the appropriateness of a micro-planning expression for a user type.  
   * @param microPlanningId The identifier of the micro-planning expression. 
   * @param appropriateness The new appropriateness of the micro-planning expression. 
   * Positive values indicate appropriate micro-planning expressions, with 
   * greater values indicating more appropriate expressions. Negative values indicate 
   * inappropriate micro-planning expressions, with smaller values indicating more 
   * inappropriate expressions. For the sake of variation, the generator may not always 
   * choose the applicable micro-planning expressions with the highest appropriateness 
   * score, but it will generally try to use expressions whose appropriateness is positive 
   * (unless there is none of them), and from those, preference will be given to the 
   * expressions with the highest appropriateness scores. 
   * @param userType The user type. 
   * @throws UMException() if anything goes wrong.   
   **/
   public void setMicroPlanningAppropriateness(String microPlanningId, float appropriateness, String userType) throws UMException;    
     
// ***************** numberOfFacts, maxFactsPerSentence, numberOfForwardPointers, voice *******************
// These are all parts of the stereotypes. Unlike other values, they are not associated with entities,
// facts, words, micro-planning expressions, or schemata. 

   /** 
   * Set the number of facts to include in each description for a user type. 
   * @param numberOfFacts The new number of facts. This must be a non-negative integer.
   * @param userType The user type.
   * @throws UMException() if anything goes wrong.   
   **/
   public void setNumberOfFacts(int numberOfFacts, String userType) throws UMException;

   /** 
   * Set the maximum number of facts to aggregate per sentence for a user type.
   * @param factsPerSentence The maximum number of facts to aggregate per sentence (a positive integer).
   * @param userType The user type.
   * @throws UMException() if anything goes wrong.   
   **/
   public void setMaxFactsPerSentence(int factsPerSentence, String userType) throws UMException;
   
   /**
   * Set the total number of forward pointers to include in each description for a user type. 
   * @param NumberOfForwardPointers The number of forward pointers to include in each 
   * description. This must be a non-negative integer. It includes both pointers generated by 
   * log analysis (as reported by getForwardPointers()), and pointers that the text generator 
   * may produce by looking up the database (e.g. exhibits belonging to the same historical period).
   * @param userType The user type. 
   * @throws UMException() if anything goes wrong.   
   **/
   public void setNumberOfForwardPointers(int numberOfForwardPointers, String userType) throws UMException;

   /** 
   * Set the desired voice (e.g. male voice, female voice) for a user type.
   * @param voice The identifier of the new voice.  
   * @param userType The user type. 
   * @throws UMException() if anything goes wrong.   
   **/
   public void setVoice(String voice, String userType) throws UMException;
}

//  **************************************************************************************************
