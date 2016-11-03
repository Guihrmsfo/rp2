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
	
	public static final String Neuter_singular_nominative = "��";
	public static final String Neuter_singular_genitive = "���";
	public static final String Neuter_singular_accusative = "��";
	
	public static final String Neuter_plural_nominative = "��";
	public static final String Neuter_plural_genitive = "���";
	public static final String Neuter_plural_accusative = "��";	
	
	
	public static final String masculine_singular_nominative = "o";
	public static final String masculine_singular_genitive = "���";
	public static final String masculine_singular_accusative = "���";
	
	public static final String masculine_plural_nominative = "��";
	public static final String masculine_plural_genitive = "���";
	public static final String masculine_plural_accusative = "����";	
	
	public static final String feminine_singular_nominative = "�";
	public static final String feminine_singular_genitive = "���";
	public static final String feminine_singular_accusative = "���";
	
	public static final String feminine_plural_nominative = "��";
	public static final String feminine_plural_genitive = "���";
	public static final String feminine_plural_accusative = "���";	
	
	public static String get_prepositional_phrase(String gender, String num, String Case,String next_text){
            return "�" + getArticle(gender, num, Case, next_text);
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
		
                if(ret.compareTo("���") != 0 && ret.endsWith("�"))
                {
                    if( 
                    !(
                    next_text.startsWith("�") || next_text.startsWith("�") || next_text.startsWith("�")
                    || next_text.startsWith("��") || next_text.startsWith("��") || next_text.startsWith("��")
                    || next_text.startsWith("��") || next_text.startsWith("��") || next_text.startsWith("�")
                    || next_text.startsWith("�")  || next_text.startsWith("�") || next_text.startsWith("�") 
                    || next_text.startsWith("�")  || next_text.startsWith("��") || next_text.startsWith("��") 
                    || next_text.startsWith("��") || next_text.startsWith("��") || next_text.startsWith("��") 
                    || next_text.startsWith("�")  || next_text.startsWith("�")  || next_text.startsWith("��") 
                    || next_text.startsWith("��") || next_text.startsWith("��") || next_text.startsWith("��") 
                    || next_text.startsWith("��") 
                    || next_text.startsWith("�") || next_text.startsWith("�") 
                    || next_text.startsWith("�") || next_text.startsWith("�") || next_text.startsWith("�")
                    || next_text.startsWith("�") || next_text.startsWith("�") 
                    || next_text.startsWith("�") || next_text.startsWith("�") 
                    || next_text.startsWith("�") || next_text.startsWith("�") || next_text.startsWith("�")
                    || next_text.startsWith("�") || next_text.startsWith("�")                      
                    || next_text.startsWith("�") || next_text.startsWith("�") 
                    || next_text.startsWith("�") || next_text.startsWith("�") || next_text.startsWith("�")
                    || next_text.startsWith("�") || next_text.startsWith("�") 
                    || next_text.startsWith("�") || next_text.startsWith("�") 
                    || next_text.startsWith("�") || next_text.startsWith("�") || next_text.startsWith("�")
                    || next_text.startsWith("�") || next_text.startsWith("�") 
                    ))
                    
                    {
                       ret = ret.substring(0,ret.length() -1);
                    }                        
                }
                
		//System.out.println("Articles: " + ret);
		return ret;
	}
        
        public static void main(String args[]){
            String testStr = GreekArticles.getArticle("masculine", XmlMsgs.SINGULAR, XmlMsgs.ACCUSATIVE_TAG, "�������");
            System.out.println(testStr);
        }
}