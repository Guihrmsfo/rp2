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

package gr.aueb.cs.nlg.NLGEngine;

import gr.aueb.cs.nlg.Utils.*;

public class SurfaceRealizationHelper
{
    
    /** Creates a new instance of SurfaceRealizationHelper */
    public SurfaceRealizationHelper()
    {
    }
 
    public static String getfoo(String Gender, String number, String Case, boolean withArticle)
    {
        if(number.compareTo(XmlMsgs.SINGULAR)==0)
        {
             if(Gender.compareTo(XmlMsgs.GENDER_MASCULINE)==0)
             {
                  if(!withArticle)
                  {
                    if(Case.compareTo(XmlMsgs.NOMINATIVE_TAG)==0){
                        return "αυτός";
                    }
                    else if(Case.compareTo(XmlMsgs.GENITIVE_TAG)==0){
                        return "αυτού";
                    }
                    else if(Case.compareTo(XmlMsgs.ACCUSATIVE_TAG)==0){
                        return "αυτόν";
                    }
                  }
                  else
                  {
                    if(Case.compareTo(XmlMsgs.NOMINATIVE_TAG)==0){
                        return "αυτός" + " " + "ο";
                    }
                    else if(Case.compareTo(XmlMsgs.GENITIVE_TAG)==0){
                        return "αυτού" + " " + "του";
                    }
                    else if(Case.compareTo(XmlMsgs.ACCUSATIVE_TAG)==0){
                        return "αυτόν"+ " " + "τον";
                    }        
                  }
             }
             else if(Gender.compareTo(XmlMsgs.GENDER_FEMININE)==0)
             {
                  if(!withArticle)
                  {
                    if(Case.compareTo(XmlMsgs.NOMINATIVE_TAG)==0){
                        return "αυτή";
                    }
                    else if(Case.compareTo(XmlMsgs.GENITIVE_TAG)==0){
                        return "αυτής";
                    }
                    else if(Case.compareTo(XmlMsgs.ACCUSATIVE_TAG)==0){
                        return "αυτήν";
                    }
                  }
                  else
                  {
                    if(Case.compareTo(XmlMsgs.NOMINATIVE_TAG)==0){
                        return "αυτή" + " " + "η";
                    }
                    else if(Case.compareTo(XmlMsgs.GENITIVE_TAG)==0){
                        return "αυτής" + " " + "της";
                    }
                    else if(Case.compareTo(XmlMsgs.ACCUSATIVE_TAG)==0){
                        return "αυτήν" +  " " + "την";
                    }
                  }
             }
             else if(Gender.compareTo("neuter")==0)
             {
                 if(!withArticle)
                 {
                    if(Case.compareTo(XmlMsgs.NOMINATIVE_TAG)==0){
                        return "αυτό";
                    }
                    else if(Case.compareTo(XmlMsgs.GENITIVE_TAG)==0){
                        return "αυτού του";
                    }
                    else if(Case.compareTo(XmlMsgs.ACCUSATIVE_TAG)==0){
                        return "αυτού του";
                    }
                 }
                 else
                 {
                    if(Case.compareTo(XmlMsgs.NOMINATIVE_TAG)==0){
                        return "αυτό" + " " + "το";
                    }
                    else if(Case.compareTo(XmlMsgs.GENITIVE_TAG)==0){
                        return "αυτού" + " " +"του";
                    }
                    else if(Case.compareTo(XmlMsgs.ACCUSATIVE_TAG)==0){
                        return "αυτό" + " "+ "το";
                    }
                 }
             }
             else
             {
                 if(!withArticle)
                 {
                    return "αυτός/αυτή/αυτό";
                 }
                 else
                 {
                    return "αυτός το/αυτή η/αυτό το";
                 }
                     
             }        
        }
        else if(number.compareTo(XmlMsgs.PLURAL)==0)
        {
             if(Gender.compareTo(XmlMsgs.GENDER_MASCULINE)==0)
             {
                 if(!withArticle)
                 {
                    if(Case.compareTo(XmlMsgs.NOMINATIVE_TAG)==0){
                        return "αυτοί";
                    }
                    else if(Case.compareTo(XmlMsgs.GENITIVE_TAG)==0){
                        return "αυτών";
                    }
                    else if(Case.compareTo(XmlMsgs.ACCUSATIVE_TAG)==0){
                        return "αυτοί";
                    }
                 }
                 else
                 {
                    if(Case.compareTo(XmlMsgs.NOMINATIVE_TAG)==0){
                        return "αυτοί" + " " + "οι";
                    }
                    else if(Case.compareTo(XmlMsgs.GENITIVE_TAG)==0){
                        return "αυτών" + " " + "των";
                    }
                    else if(Case.compareTo(XmlMsgs.ACCUSATIVE_TAG)==0){
                        return "αυτοί"+  " " + "οι";
                    }
                 }
             }
             else if(Gender.compareTo(XmlMsgs.GENDER_FEMININE)==0)
             {
                 if(!withArticle)
                 {
                    if(Case.compareTo(XmlMsgs.NOMINATIVE_TAG)==0){
                        return "αυτές";
                    }
                    else if(Case.compareTo(XmlMsgs.GENITIVE_TAG)==0){
                        return "αυτών";
                    }
                    else if(Case.compareTo(XmlMsgs.ACCUSATIVE_TAG)==0){
                        return "αυτές";
                    }
                 }
                 else
                 {
                    if(Case.compareTo(XmlMsgs.NOMINATIVE_TAG)==0){
                        return "αυτές" + " " + "οι";
                    }
                    else if(Case.compareTo(XmlMsgs.GENITIVE_TAG)==0){
                        return "αυτών" + " " + "των";
                    }
                    else if(Case.compareTo(XmlMsgs.ACCUSATIVE_TAG)==0){
                        return "αυτές" + " " + "οι";
                    }                     
                 }
             }
             else if(Gender.compareTo("neuter")==0)
             {
                 if(!withArticle)
                 {
                    if(Case.compareTo(XmlMsgs.NOMINATIVE_TAG)==0){
                        return "αυτό" ;
                    }
                    else if(Case.compareTo(XmlMsgs.GENITIVE_TAG)==0){
                        return "αυτού";
                    }
                    else if(Case.compareTo(XmlMsgs.ACCUSATIVE_TAG)==0){
                        return "αυτό";
                    }
                 }
                 else
                 {
                    if(Case.compareTo(XmlMsgs.NOMINATIVE_TAG)==0){
                        return "αυτό" + " " + "το";
                    }
                    else if(Case.compareTo(XmlMsgs.GENITIVE_TAG)==0){
                        return "αυτού" + " " + "του";
                    }
                    else if(Case.compareTo(XmlMsgs.ACCUSATIVE_TAG)==0){
                        return "αυτό" + " " + "το";
                    }                     
                 }
             }
             else
             {
                 if(!withArticle)
                    return "αυτοί/αυτές/αυτά";
                 else
                    return "αυτοί οι/αυτές οι/αυτά τα";
             }              
        }
        else
        {
            return "αυτοί/αυτές/αυτά";
        }
        
        return "ERROR";
    }       
    
    public static String getfoo2(String Gender, String number, String Case)
    {
        if(number.compareTo(XmlMsgs.SINGULAR)==0)
        {
             if(Gender.compareTo(XmlMsgs.GENDER_MASCULINE)==0)
             {
                if(Case.compareTo(XmlMsgs.NOMINATIVE_TAG)==0){
                    return "συγκεκριμένος";
                }
                else if(Case.compareTo(XmlMsgs.GENITIVE_TAG)==0){
                    return "συγκεκριμένου";
                }
                else if(Case.compareTo(XmlMsgs.ACCUSATIVE_TAG)==0){
                    return "συγκεκριμένο";
                }       
             }
             else if(Gender.compareTo(XmlMsgs.GENDER_FEMININE)==0)
             {
                if(Case.compareTo(XmlMsgs.NOMINATIVE_TAG)==0){
                    return "συγκεκριμένη";
                }
                else if(Case.compareTo(XmlMsgs.GENITIVE_TAG)==0){
                    return "συγκεκριμένης";
                }
                else if(Case.compareTo(XmlMsgs.ACCUSATIVE_TAG)==0){
                    return "συγκεκριμένη";
                }
             }
             else if(Gender.compareTo("neuter")==0)
             {

                if(Case.compareTo(XmlMsgs.NOMINATIVE_TAG)==0){
                    return "συγκεκριμένο";
                }
                else if(Case.compareTo(XmlMsgs.GENITIVE_TAG)==0){
                    return "συγκεκριμένου";
                }
                else if(Case.compareTo(XmlMsgs.ACCUSATIVE_TAG)==0){
                    return "συγκεκριμένο";
                }

             }
             else
             {
                    return "αυτός/αυτή/αυτό";     
             }        
        }
        else if(number.compareTo(XmlMsgs.PLURAL)==0)
        {
             if(Gender.compareTo(XmlMsgs.GENDER_MASCULINE)==0)
             {

                if(Case.compareTo(XmlMsgs.NOMINATIVE_TAG)==0){
                    return "συγκεκριμένοι";
                }
                else if(Case.compareTo(XmlMsgs.GENITIVE_TAG)==0){
                    return "συγκεκριμένων";
                }
                else if(Case.compareTo(XmlMsgs.ACCUSATIVE_TAG)==0){
                    return "συγκεκριμένους";
                }
             }
             else if(Gender.compareTo(XmlMsgs.GENDER_FEMININE)==0)
             {
                    if(Case.compareTo(XmlMsgs.NOMINATIVE_TAG)==0){
                        return "συγκεκριμένες";
                    }
                    else if(Case.compareTo(XmlMsgs.GENITIVE_TAG)==0){
                        return "συγκεκριμένων";
                    }
                    else if(Case.compareTo(XmlMsgs.ACCUSATIVE_TAG)==0){
                        return "συγκεκριμένες";
                    }
             }
             else if(Gender.compareTo("neuter")==0)
             {
                    if(Case.compareTo(XmlMsgs.NOMINATIVE_TAG)==0){
                        return "συγκεκριμένα" ;
                    }
                    else if(Case.compareTo(XmlMsgs.GENITIVE_TAG)==0){
                        return "συγκεκριμένων";
                    }
                    else if(Case.compareTo(XmlMsgs.ACCUSATIVE_TAG)==0){
                        return "συγκεκριμένα";
                    }

             }
             else
             {
                    return "αυτοί/αυτές/αυτά";

             }              
        }
        
        return "ERROR";
    }           
    
}
