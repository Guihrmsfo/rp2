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

import java.io.*;

import gr.aueb.cs.nlg.Languages.*;
import gr.aueb.cs.nlg.Utils.*;
        
public class NPList{
    private Lex_Entry_EN LE_EN;
    private Lex_Entry_GR LE_GR;
    
    public NPList(Lex_Entry_EN LE_EN, Lex_Entry_GR LE_GR){
        this.LE_EN = LE_EN;
        this.LE_GR = LE_GR;
    }
    
    public NPList(){
        this.LE_EN = null;
        this.LE_GR = null;
    }    
    
    public void setEntry(String lang, Object b){
        if(Languages.isEnglish(lang)){
              this.LE_EN = (Lex_Entry_EN)b;
       
        }
        else if(Languages.isGreek(lang)){
             this.LE_GR = (Lex_Entry_GR)b;
        }
    }
    
    public Object getEntry(String lang){
        if(Languages.isEnglish(lang)){
              return this.LE_EN;
       
        }
        else if(Languages.isGreek(lang)){
             return this.LE_GR;
        }
        return null;
    }    
}