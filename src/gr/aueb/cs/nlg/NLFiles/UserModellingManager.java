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

package gr.aueb.cs.nlg.NLFiles;

import com.hp.hpl.jena.rdf.model.*;



import gr.aueb.cs.nlg.*;

public class UserModellingManager extends NLFileManager 
{
        
    
    // resources
    public static final String UserModellingRes = "UserModelling";
    public static final String UserTypeRes = "UserType";
    public static final String PropertyRes = "Property";
    public static final String MicroplanRes = "Microplan";
        
    // properties
    public static final String UserTypesPrp = "UserTypes";
        
    public static final String AppropriatenessPrp= "Appropriateness";
    public static final String AppropPrp = "Approp";
    
    public static final String forUserTypePrp = "forUserType";
    public static final String AppropValuePrp = "AppropValue";    
    
    public static final String forInstancePrp = "forInstance";
    public static final String forOwlClassPrp = "forOwlClass";
    
    public static final String InterestValuePrp = "InterestValue";
    public static final String RepetitionsValuePrp = "Repetitions";
    
    
    public static final String PropertiesInterestsRepetitionsPrp = "PropertiesInterestsRepetitions";
    
    public static final String DPInterestRepetitionsPrp = "DPInterestRepetitions";
    public static final String CDPInterestRepetitionsPrp = "CDPInterestRepetitions";
    public static final String IPInterestRepetitionsPrp = "IPInterestRepetitions";
                    
    
    public static final String ClassInterestsRepetitionsPrp = "ClassInterestsRepetitions";   
    
    public static final String MaxFactsPerSentencePrp = "MaxFactsPerSentence";
    public static final String SynthesizerVoicePrp = "SynthesizerVoice";
    public static final String FactsPerPagePrp = "FactsPerPage";
    public static final String LangPrp = "Language";
    
    public static final String DInterestRepetitionsPrp = "DInterestRepetitions";
    public static final String IInterestRepetitionsPrp = "IInterestRepetitions";

    
    // properties
    public Property UserTypesProperty = model.createProperty(owlnlNS + UserModellingManager.UserTypesPrp);
    public Property AppropriatenessProperty = model.createProperty(owlnlNS + UserModellingManager.AppropriatenessPrp);
    public Property AppropProperty = model.createProperty(owlnlNS + UserModellingManager.AppropPrp);
    public Property forUserTypeProperty = model.createProperty(owlnlNS + UserModellingManager.forUserTypePrp);
    public Property AppropValueProperty = model.createProperty(owlnlNS + UserModellingManager.AppropValuePrp);
   
    public Property forInstanceProperty =  model.createProperty(owlnlNS + UserModellingManager.forInstancePrp);
        
    public Property  DPInterestRepetitionsProperty = model.createProperty(owlnlNS + UserModellingManager.DPInterestRepetitionsPrp);
    public Property  CDPInterestRepetitionsProperty = model.createProperty(owlnlNS + UserModellingManager.CDPInterestRepetitionsPrp);
    public Property  IPInterestRepetitionsProperty = model.createProperty(owlnlNS + UserModellingManager.IPInterestRepetitionsPrp);
    
    public Property  InterestValueProperty = model.createProperty(owlnlNS + UserModellingManager.InterestValuePrp);        
    public Property  RepetitionsValueProperty = model.createProperty(owlnlNS + UserModellingManager.RepetitionsValuePrp);
    
    public Property forOwlClassProperty = model.createProperty(owlnlNS + UserModellingManager.forOwlClassPrp);
    public Property PropertiesInterestsRepetitionsProperty = model.createProperty(owlnlNS + UserModellingManager.PropertiesInterestsRepetitionsPrp);
    
    
    public Property MaxFactsPerSentenceProperty = model.createProperty(owlnlNS + UserModellingManager.MaxFactsPerSentencePrp);
    public Property SynthesizerVoiceProperty = model.createProperty(owlnlNS + UserModellingManager.SynthesizerVoicePrp);
    public Property FactsPerPageProperty = model.createProperty(owlnlNS + UserModellingManager.FactsPerPagePrp);
    public Property LangProperty = model.createProperty(owlnlNS + UserModellingManager.LangPrp);
    
    public Property ClassInterestsRepetitionsProperty = model.createProperty(owlnlNS + UserModellingManager.ClassInterestsRepetitionsPrp);
    
    public Property DInterestRepetitionsProperty = model.createProperty(owlnlNS + UserModellingManager.DInterestRepetitionsPrp);
    public Property IInterestRepetitionsProperty = model.createProperty(owlnlNS + UserModellingManager.IInterestRepetitionsPrp);
    
    
    public Resource MicroplansAndOrderingResourceType = null;
    
   
         
    //----------------------------------------------------------------------------
    public UserModellingManager(String xb){
        super(xb);             
    }

    //----------------------------------------------------------------------------

    
    public static void main(String args[]){//main

    }
}