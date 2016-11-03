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

import java.io.*;
import java.util.*;

import gr.aueb.cs.nlg.Languages.*;
import gr.aueb.cs.nlg.NLFiles.*;
import gr.aueb.cs.nlg.Utils.*;
import gr.aueb.cs.nlg.Utils.XmlMsgs;

public class Pronouns{
    
    // third person english pronouns
    public static final String sing_Nom_masc = "he";
    public static final String sing_Accu_masc = "him";
    public static final String sing_Gen_masc ="his";
    
    public static final String sing_Nom_fem = "she";
    public static final String sing_Accu_fem = "her";
    public static final String sing_Gen_fem = "hers";
    
    public static final String sing_Nom_neuter = "it";
    public static final String sing_Accu_neuter = "it";
    public static final String sing_Gen_neuter = "its";
    
    public static final String plur_Nom = "they";
    public static final String plur_Accu = "them";
    public static final String plur_Gen = "theirs";
    
    //Demonstrative pronouns
    
    public static final String THIS = "this";
    public static final String THESE = "these";
    
    public static String getPronoun(String Case, String number, String gender){
        String prn = "NOT FOUND PRONOUN";
        
        if(number.compareTo(XmlMsgs.SINGULAR)== 0)
        {            
            if(Case.compareTo(XmlMsgs.NOMINATIVE_TAG)==0)
            {
                if(gender.compareTo(XmlMsgs.GENDER_MASCULINE)==0)
                {
                        prn = sing_Nom_masc;
                }
                else if(gender.compareTo(XmlMsgs.GENDER_FEMININE)==0)
                {
                        prn = sing_Nom_fem;
                }            
                else if(gender.compareTo(XmlMsgs.GENDER_NONPESRSONAL)==0)
                {
                        prn = sing_Nom_neuter;
                }     
            }
            else if(Case.compareTo(XmlMsgs.ACCUSATIVE_TAG)==0)
            {
                if(gender.compareTo(XmlMsgs.GENDER_MASCULINE)==0)
                {
                        prn = sing_Accu_masc;
                }
                else if(gender.compareTo(XmlMsgs.GENDER_FEMININE)==0)
                {
                        prn = sing_Accu_fem;
                }            
                else if(gender.compareTo(XmlMsgs.GENDER_NONPESRSONAL)==0)
                {
                        prn = sing_Accu_neuter;
                }                   
            }            
            else if(Case.compareTo(XmlMsgs.GENITIVE_TAG)==0)
            {
                if(gender.compareTo(XmlMsgs.GENDER_MASCULINE)==0)
                {
                        prn = sing_Gen_masc;
                }
                else if(gender.compareTo(XmlMsgs.GENDER_FEMININE)==0)
                {
                        prn = sing_Gen_fem;
                }            
                else if(gender.compareTo(XmlMsgs.GENDER_NONPESRSONAL)==0)
                {
                        prn = sing_Gen_neuter;
                }                   
            }                
        }
        else if(number.compareTo(XmlMsgs.PLURAL)== 0)
        {            
            if(Case.compareTo(XmlMsgs.NOMINATIVE_TAG)==0)
            {
                prn = plur_Nom;
            }
            else if(Case.compareTo(XmlMsgs.ACCUSATIVE_TAG)==0)
            {
                prn = plur_Accu;
            }            
            else if(Case.compareTo(XmlMsgs.GENITIVE_TAG)==0)
            {
                prn = plur_Gen;
            }                 
        }
    
        return prn;
    }
}
//-----------------------------------------------------------------------------------