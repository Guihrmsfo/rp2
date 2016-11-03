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

//===================================================================
// UMAuthoringImpEmu
//
// Implements the UMAuthoring interface through calls to an emulation
// of PersServer, running on main memory (no persistent storage).
//===================================================================
public class UMAuthoringImpEmu implements UMAuthoring 
{
    
    Structures s;  //the main memory structures holding personilization data

    //initializers
    public UMAuthoringImpEmu(Structures s) 
    {
        this.s = s;
    }


   public void initialize(String[] entityNames, String[] factIds, String[] microPlanIds) throws UMException
   {
        s.initialize(entityNames, factIds, microPlanIds);
   }
   

   public void newUserType(String userType) throws UMException
   {
       
   }
   

   public void setDefaultInterest(String propertyURI, float interest, String userType)
   {
       
   }
   
   public void setClassInterest(String propertyURI,String ClassURI ,float interest, String userType)
   {
       
   }
      
   public void setInstanceInterest(String propertyURI,String InstanceURI ,float interest, String userType)
   {
       
   }
   
   
   public void setAssimilationRateFact(String factId, float assimilationRate, String userType) throws UMException
   {
       
   }
   

   public void setAssimilationRateFunctor(String functor, float assimilationRate, String userType) throws UMException
   {
       
   }


   public void setInitialAssimilationFact(String factId, float initAssimilation, String userType) throws UMException
   {
       
   }

   public void setInitialAssimilationFunctor(String functor, float initAssimilation, String userType) throws UMException
   {
       
   }

   public void setAssimilationFact(String[] factIds, float assimilation, String userType) throws UMException
   {
       
   }

   public void setMicroPlanningAppropriateness(String microPlanningId, float appropriateness, String userType) throws UMException
   {
       
   }    
     
// ***************** numberOfFacts, maxFactsPerSentence, numberOfForwardPointers, voice *******************

   public void setNumberOfFacts(int numberOfFacts, String userType) throws UMException
   {
       
   }


   public void setMaxFactsPerSentence(int factsPerSentence, String userType) throws UMException
   {
       
   }
   
   
   public void setNumberOfForwardPointers(int numberOfForwardPointers, String userType) throws UMException
   {
       
   }


   public void setVoice(String voice, String userType) throws UMException
   {
   }
}
