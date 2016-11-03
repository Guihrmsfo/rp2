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

import gr.aueb.cs.nlg.Utils.XmlMsgs;

public class GreekArticles{
	public GreekArticles(){
	}
	
	public static final String Neuter_singular_nominative = "το";
	public static final String Neuter_singular_genitive = "του";
	public static final String Neuter_singular_accusative = "το";
	
	public static final String Neuter_plural_nominative = "τα";
	public static final String Neuter_plural_genitive = "των";
	public static final String Neuter_plural_accusative = "τα";	
	
	
	public static final String masculine_singular_nominative = "o";
	public static final String masculine_singular_genitive = "του";
	public static final String masculine_singular_accusative = "τον";
	
	public static final String masculine_plural_nominative = "οι";
	public static final String masculine_plural_genitive = "των";
	public static final String masculine_plural_accusative = "τους";	
	
	public static final String feminine_singular_nominative = "η";
	public static final String feminine_singular_genitive = "της";
	public static final String feminine_singular_accusative = "την";
	
	public static final String feminine_plural_nominative = "οι";
	public static final String feminine_plural_genitive = "των";
	public static final String feminine_plural_accusative = "τις";	
	
	public static String get_prepositional_phrase(String gender, String num, String Case,String next_text){
            return "σ" + getArticle(gender, num, Case, next_text);
        }
        
	public static String getArticle(String gender, String num, String Case, String next_text)
        {
		
		String ret = "";
		if(num.compareTo(XmlMsgs.SINGULAR)==0)
                {
			if(gender.compareTo(XmlMsgs.GENDER_MASCULINE)==0)
                        {				
				if(Case.compareTo(XmlMsgs.NOMINATIVE_TAG)==0)
                                {
					ret = GreekArticles.masculine_singular_nominative;
				}
				else if(Case.compareTo(XmlMsgs.GENITIVE_TAG)==0)
                                {
					ret = GreekArticles.masculine_singular_genitive;
				}
				else if(Case.compareTo(XmlMsgs.ACCUSATIVE_TAG)==0)
                                {
					ret = GreekArticles.masculine_singular_accusative;
				}
			}
			else if(gender.compareTo(XmlMsgs.GENDER_FEMININE)==0)
                        {
				if(Case.compareTo(XmlMsgs.NOMINATIVE_TAG)==0)
                                {
					ret = GreekArticles.feminine_singular_nominative;
				}
				else if(Case.compareTo(XmlMsgs.GENITIVE_TAG)==0)
                                {
					ret = GreekArticles.feminine_singular_genitive;
				}
				else if(Case.compareTo(XmlMsgs.ACCUSATIVE_TAG)==0)
                                {
					ret = GreekArticles.feminine_singular_accusative;
				}				
			}
			else if(gender.compareTo("neuter")==0)
                        {
				if(Case.compareTo(XmlMsgs.NOMINATIVE_TAG)==0)
                                {
					ret = GreekArticles.Neuter_singular_nominative;
				}
				else if(Case.compareTo(XmlMsgs.GENITIVE_TAG)==0)
                                {
					ret = GreekArticles.Neuter_singular_genitive;
				}
				else if(Case.compareTo(XmlMsgs.ACCUSATIVE_TAG)==0)
                                {
					ret = GreekArticles.Neuter_singular_accusative;
				}				
			}
			
		}
		else if(num.compareTo(XmlMsgs.PLURAL)==0)
                {
			if(gender.compareTo(XmlMsgs.GENDER_MASCULINE)==0)
                        {
				if(Case.compareTo(XmlMsgs.NOMINATIVE_TAG)==0)
                                {
					ret = GreekArticles.masculine_plural_nominative;
				}
				else if(Case.compareTo(XmlMsgs.GENITIVE_TAG)==0)
                                {
					ret = GreekArticles.masculine_plural_genitive;
				}
				else if(Case.compareTo(XmlMsgs.ACCUSATIVE_TAG)==0)
                                {
					ret = GreekArticles.masculine_plural_accusative;
				}				
			}
			else if(gender.compareTo(XmlMsgs.GENDER_FEMININE)==0)
                        {
				if(Case.compareTo(XmlMsgs.NOMINATIVE_TAG)==0)
                                {
					ret = GreekArticles.feminine_plural_nominative;
				}
				else if(Case.compareTo(XmlMsgs.GENITIVE_TAG)==0)
                                {
					ret = GreekArticles.feminine_plural_genitive;
				}
				else if(Case.compareTo(XmlMsgs.ACCUSATIVE_TAG)==0)
                                {
					ret = GreekArticles.feminine_plural_accusative;
				}				
			}
			else if(gender.compareTo("neuter")==0)
                        {
				if(Case.compareTo(XmlMsgs.NOMINATIVE_TAG)==0)
                                {
					ret = GreekArticles.Neuter_plural_nominative;
				}
				else if(Case.compareTo(XmlMsgs.GENITIVE_TAG)==0)
                                {
					ret = GreekArticles.Neuter_plural_genitive;
				}
				else if(Case.compareTo(XmlMsgs.ACCUSATIVE_TAG)==0)
                                {
					ret =  GreekArticles.Neuter_plural_accusative;
				}				
			}						                                
		}
		else
                {
                    System.out.println("ERROR + ARTICLES");
                }
		
                if(ret.compareTo("των") != 0 && ret.endsWith("ν"))
                {
                    if( 
                    !(
                    next_text.startsWith("κ") || next_text.startsWith("π") || next_text.startsWith("τ")
                    || next_text.startsWith("γκ") || next_text.startsWith("μπ") || next_text.startsWith("ντ")
                    || next_text.startsWith("τσ") || next_text.startsWith("τζ") || next_text.startsWith("ξ")
                    || next_text.startsWith("ψ")  || next_text.startsWith("Κ") || next_text.startsWith("Π") 
                    || next_text.startsWith("Τ")  || next_text.startsWith("Γκ") || next_text.startsWith("Μπ") 
                    || next_text.startsWith("Ντ") || next_text.startsWith("Τσ") || next_text.startsWith("Τζ") 
                    || next_text.startsWith("Ξ")  || next_text.startsWith("Ψ")  || next_text.startsWith("ΓΚ") 
                    || next_text.startsWith("ΜΠ") || next_text.startsWith("ΝΤ") || next_text.startsWith("ΤΣ") 
                    || next_text.startsWith("ΤΖ") 
                    || next_text.startsWith("α") || next_text.startsWith("ε") 
                    || next_text.startsWith("ο") || next_text.startsWith("ω") || next_text.startsWith("ι")
                    || next_text.startsWith("η") || next_text.startsWith("υ") 
                    || next_text.startsWith("Α") || next_text.startsWith("Ε") 
                    || next_text.startsWith("Ο") || next_text.startsWith("Ω") || next_text.startsWith("Ι")
                    || next_text.startsWith("Η") || next_text.startsWith("Υ")                      
                    || next_text.startsWith("ά") || next_text.startsWith("έ") 
                    || next_text.startsWith("ό") || next_text.startsWith("ώ") || next_text.startsWith("ί")
                    || next_text.startsWith("ή") || next_text.startsWith("ύ") 
                    || next_text.startsWith("Ά") || next_text.startsWith("Έ") 
                    || next_text.startsWith("Ό") || next_text.startsWith("Ώ") || next_text.startsWith("Ί")
                    || next_text.startsWith("Ή") || next_text.startsWith("Ύ") 
                    ))
                    
                    {
                       ret = ret.substring(0,ret.length() -1);
                    }                        
                }
                
		//System.out.println("Articles: " + ret);
		return ret;
	}
        
        public static void main(String args[]){
            String testStr = GreekArticles.getArticle("masculine", XmlMsgs.SINGULAR, XmlMsgs.ACCUSATIVE_TAG, "Δημήτρη");
            System.out.println(testStr);
        }
}