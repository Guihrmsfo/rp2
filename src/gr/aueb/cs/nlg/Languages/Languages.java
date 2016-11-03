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

package gr.aueb.cs.nlg.Languages;

import gr.aueb.cs.nlg.NLGEngine.*;
        
public class Languages
{
    
	public final static String ENGLISH  = "en";
	public final static String GREEK  = "el";
	
	public static boolean isValid(String lang)
        {            
            if(lang.compareTo(ENGLISH)==0 || lang.compareTo(GREEK)==0)
            return true;
            else
            return false;
	}
	
	
	public static boolean isGreek(String str)
        {
            if(str.compareTo(GREEK)==0)
            return true;

            return false;
	}

	public static boolean isEnglish(String str)
        {
            if(str.compareTo(ENGLISH)==0)
            return true;

            return false;		
	}
	
		
        public static void updateLanguages(ContentSelection CS, Lexicalisation LEX,  
                GRE gre, SurfaceRealization SR, Aggregation aggr, String lang)
        {
            if(CS != null && LEX != null && gre != null && SR!=null)
            {
                //System.out.println("update language:" + lang);
                CS.setLanguage(lang);
                LEX.setLanguage(lang);
                gre.setLanguage(lang);
                SR.setLanguage(lang);
                aggr.setLanguage(lang);
            }
        }        
        
}