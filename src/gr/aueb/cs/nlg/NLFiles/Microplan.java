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

import java.util.*;

import org.apache.log4j.Logger;

public class Microplan
{
    static Logger logger = Logger.getLogger("gr.aueb.cs.nlg.NLFiles.Microplan");
    
    private Vector<NLGSlot> SlotsList;
    private String MicroplanName;
    private boolean IsUsed;
    private String MicroplanURI;
    private boolean  AggAllowed;
    private String language;
                     
    public Microplan(Vector<NLGSlot> SL, String MN, boolean IU, String mURi, boolean AggAllowed, String language)
    {
        this.SlotsList = SL;
        this.MicroplanName = MN;
        this.IsUsed = IU;
        this.MicroplanURI = mURi;
        this.AggAllowed = AggAllowed;
        this.language = language;
    }
        
    public void print()
    {
        logger.debug("MicroplanURI:" + MicroplanURI);
        logger.debug("MicroplanName:" + MicroplanName);
        logger.debug("IsUsed:" + IsUsed);        
        logger.debug("AggAllowed:" + AggAllowed);  
        logger.debug("language:" + language);  
        
        for(int i = 0; i < SlotsList.size(); i++)
        {
            logger.debug("i :" + i);
        }
    }//print
    
    public Microplan()
    {
        SlotsList = null;
        MicroplanName = "";
        IsUsed = true;
    }
    
    public void setLanguage(String lang)
    {
        language = lang;
    }

    public String getLanguage()
    {
        return language;
    }
        
    public void setSlotslist(Vector<NLGSlot> SL)
    {
        this.SlotsList = SL;
    }
    
    public Vector<NLGSlot> getSlotsList(){
        return this.SlotsList;
    }
    
    public void setMicroplanName(String MN){
        this.MicroplanName = MN;
    }
    
    public String getMicroplanName(){ 
        return this.MicroplanName;
    }
    
    public void setIsUsed(boolean IU){
        this.IsUsed = IU;
    }
    
    public boolean getIsUsed(){
        return this.IsUsed;
    }
           
    
    public void setAggAllowed(boolean AggAllowed)
    {
        this.AggAllowed = AggAllowed;
    }
    
     public boolean getAggAllowed()
    {
        return this.AggAllowed ;
    }
    
    public String getMicroplanURI(){
        return this.MicroplanURI;
    }
    
    public void setMicroplanURI(String mURi){
        this.MicroplanURI = mURi;
    }
}
