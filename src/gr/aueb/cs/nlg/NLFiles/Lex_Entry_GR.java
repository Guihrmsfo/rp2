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

import gr.aueb.cs.nlg.Utils.XmlMsgs;
import org.apache.log4j.Logger;


public class Lex_Entry_GR extends Lex_Entry
{
        static Logger logger = Logger.getLogger("gr.aueb.cs.nlg.NLFiles.Lex_Entry_GR");
	private String singular_nom;
	private String singular_gen;
	private String singular_acc;
	private String plural_nom;
	private String plural_gen;
	private String plural_acc;
	private String Gender;
	private String  num;
        
        private boolean  inflected = true;
        
        private int type;
                
        public int getType()
        {
            return type;
        }
        
        public void setInflected(boolean b)
        {
            this.inflected = b;
        }
        
        public boolean getInflected()
        {
            return inflected;
        }        
	//-----------------------------------------------------------------------------------
	public void print()
        {
            logger.debug("_ Lex_Entry_GR Serialization _");
            logger.debug(singular_nom + ", " + singular_gen + ", " + singular_acc);
            logger.debug(plural_nom + ", " + plural_gen + ", " + plural_acc);
            logger.debug(Gender + ", " + num + ", "  + this.getCountable());
            logger.debug(this.inflected);
            logger.debug(this.getShortname());
            logger.debug("_______________________________");
	}
	//-----------------------------------------------------------------------------------
        public Lex_Entry_GR(String sn,String sg,String sa,String pn,String pg,String pa,String gender, int t)
        {
            singular_nom = sn;
            singular_gen = sg;
            singular_acc = sa;
            plural_nom = pn;
            plural_gen = pg;
            plural_acc = pa;		
            Gender = gender;
            this.type = t;
            this.inflected = true;
		
	}
	//-----------------------------------------------------------------------------------
	public Lex_Entry_GR(String g,String n,boolean c ,int t)
        {
            singular_nom = "";
            singular_gen = "";
            singular_acc = "";
            plural_nom = "";
            plural_gen = "";
            plural_acc = "";	
            Gender = g;
            num = n;
            this.type = t;
            this.setCountable(c);
            this.inflected = true;
	}	
	//-----------------------------------------------------------------------------------
	public void set_singular_cases(String nom,String gen,String acc)
        {
            this.singular_nom = nom;
            this.singular_gen = gen;
            this.singular_acc = acc;
	}
	//-----------------------------------------------------------------------------------
	public void set_plural_cases(String nom,String gen,String acc)
        {
            this.plural_nom = nom;
            this.plural_gen = gen;
            this.plural_acc = acc;
	}
        
	//-----------------------------------------------------------------------------------
	public String get(String Case)
        {
            if(!inflected)
                return this.singular_nom;
            
            if(num.compareTo(XmlMsgs.SINGULAR)==0)
            {
                if(Case.compareTo(XmlMsgs.NOMINATIVE_TAG)==0){
                        return this.singular_nom;
                }
                else if(Case.compareTo(XmlMsgs.GENITIVE_TAG)==0){
                        return this.singular_gen;
                }
                else if(Case.compareTo(XmlMsgs.ACCUSATIVE_TAG)==0){
                        return this.singular_acc;
                }
                else
                {
                }			
            }
            else if(num.compareTo(XmlMsgs.PLURAL)==0)
            {
                if(Case.compareTo(XmlMsgs.NOMINATIVE_TAG)==0){
                        return this.plural_nom;
                }
                else if(Case.compareTo(XmlMsgs.GENITIVE_TAG)==0){
                        return this.plural_gen;
                }
                else if(Case.compareTo(XmlMsgs.ACCUSATIVE_TAG)==0){
                        return this.plural_acc;
                }
                else
                {
                }
            }
            return "";
	}
	//-----------------------------------------------------------------------------------
	public String get(String Case, String numb)
        {
            if(!inflected)
                return this.singular_nom;
                        
            if(numb.compareTo(XmlMsgs.SINGULAR)==0)
            {
                if(Case.compareTo(XmlMsgs.NOMINATIVE_TAG)==0){
                        return this.singular_nom;
                }
                else if(Case.compareTo(XmlMsgs.GENITIVE_TAG)==0){
                        return this.singular_gen;
                }
                else if(Case.compareTo(XmlMsgs.ACCUSATIVE_TAG)==0){
                        return this.singular_acc;
                }
                else
                {
                }			
            }
            else if(numb.compareTo(XmlMsgs.PLURAL)==0)
            {
                if(Case.compareTo(XmlMsgs.NOMINATIVE_TAG)==0)
                {
                        return this.plural_nom;
                }
                else if(Case.compareTo(XmlMsgs.GENITIVE_TAG)==0)
                {
                        return this.plural_gen;
                }
                else if(Case.compareTo(XmlMsgs.ACCUSATIVE_TAG)==0)
                {
                        return this.plural_acc;
                }
                else
                {
                }
            }
            return "";
	}	
	
	public String get_Gender()
        {
		return Gender;
	}
	
        public String get_num()
        {
		return num;
	}
}