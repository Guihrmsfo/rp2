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
        
public class EnglishArticles{
    public static final String ARTICLE_THE = "the"; //definite_article
    public static final String ARTICLE_A = "a";    //indefinite article
    public static final String ARTICLE_AN = "an";    //indefinite article 
    
    public static String getIndefiniteArticle(String text){
        
        char ch = text.charAt(0);
        if (ch == 'a' || ch == 'e' || ch == 'o' || ch =='i' || ch =='u'
        || ch =='A' || ch =='E' || ch =='O' || ch =='I' || ch =='U'){
                return ARTICLE_AN;
        }
        else{

                return ARTICLE_A;
        }  
    }        
}//EnglishArticles

