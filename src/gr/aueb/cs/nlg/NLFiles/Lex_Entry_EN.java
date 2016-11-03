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

public class Lex_Entry_EN extends Lex_Entry
{
        static Logger logger = Logger.getLogger("gr.aueb.cs.nlg.NLFiles.Lex_Entry_EN");
	private String singular;
	private String plural;
	private String Gender;
	private String num;
        private int type;
 
                
        public int getType()
        {
            return type;
        }        
	//-----------------------------------------------------------------------------------
	public void print(){
		logger.debug("_ Lex_Entry_EN Serialization _");
		logger.debug(singular + ", " + plural);
		logger.debug(Gender + ", " + num + ", "  + this.getCountable());
                logger.debug(this.getShortname());
		logger.debug("_______________________________");
	}
	//-----------------------------------------------------------------------------------
	public Lex_Entry_EN(String singular,String plural, String g, String num, int t)
        {
		this.singular = singular;
		this.plural = plural;
		this.Gender = g;
		this.num = num;
                this.type = t;
	}
	//-----------------------------------------------------------------------------------
	public Lex_Entry_EN(String g, String num, boolean c ,int t)
        {
		this.singular = "";
		this.plural = "";
		this.Gender = g;
		this.num = num;
                this.type = t;
                this.setCountable(c);
	}
	//-----------------------------------------------------------------------------------
	public void set_sing_plural(String s, String p)
        {
		singular = s;
		plural = p;
	}		
	//-----------------------------------------------------------------------------------

	public String get_Gender(){
		return Gender;
	}
	
	public String get_Singular(){
		return singular;
	}
	
	public String get_Plural(){
		return plural;
	}
	
	public String get_num(){
		return num;
	}
	//-----------------------------------------------------------------------------------
	public String get(String Case)
        {
            String ret = "";
            if(num.compareTo(XmlMsgs.SINGULAR)==0)
                    ret = this.singular;
            else if (num.compareTo(XmlMsgs.PLURAL)==0)
                    ret = this.plural;

            if(Case.compareTo(XmlMsgs.NOMINATIVE_TAG)==0 || Case.compareTo(XmlMsgs.ACCUSATIVE_TAG)==0)
            {
                    return ret;
            }
            else
            {
                if(ret.endsWith("s"))
                {
                    ret = ret + "'";
                    return ret;
                }
                else
                {
                    ret = ret + "' s";
                    return ret;
                }
            }
	}//get
	//-----------------------------------------------------------------------------------
	public String get(String Case,String numb)
        {
		String ret = "";
		if(numb.compareTo(XmlMsgs.SINGULAR)==0)
                {
                    ret = this.singular;
                }
		else if (numb.compareTo(XmlMsgs.PLURAL)==0)
                {
                    ret = this.plural;
                    ///////edw kanw pagapontia epeidh to lejiko mas den einai plhres
                    if (ret.equalsIgnoreCase(""))
                        ret = this.singular;                    
                }
			
		if(Case.compareTo(XmlMsgs.NOMINATIVE_TAG)==0 || Case.compareTo(XmlMsgs.ACCUSATIVE_TAG)==0)
                {
                    return ret;
                }
		else
                {
                    if(ret.endsWith("s"))
                    {
                        ret = ret + "'";
                        return ret;
                    }
                    else
                    {
                        ret = ret + "' s";
                        return ret;
                    }
		}
	}//get	
}
