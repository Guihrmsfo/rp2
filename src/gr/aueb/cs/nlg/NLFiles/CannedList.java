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

import gr.aueb.cs.nlg.Languages.Languages;
import java.util.HashSet;        
import java.util.*;

public class CannedList 
{
    
    private String GreekCanned;
    private String EnglishCanned;
    private HashSet UserTypes;
    private boolean FillerAggregationAllowed;  
    private boolean FOCUS_LOST;
    
    public CannedList() 
    {
        GreekCanned = "";
        EnglishCanned = "";
        UserTypes = new HashSet();
    }
    
    public void setFillerAggregationAllowed(boolean b)
    {
        FillerAggregationAllowed = b;
    }
    
    public boolean getFillerAggregationAllowed()
    {
        return FillerAggregationAllowed;
    }

    public void setFOCUS_LOST(boolean b)
    {
        FOCUS_LOST = b;
    }
    
    public boolean getFOCUS_LOST()
    {
        return FOCUS_LOST;
    }
    
    public void setCannedText(String str, String lang)
    {
        if(Languages.isEnglish(lang))
            EnglishCanned = str;
        else if(Languages.isGreek(lang))
            GreekCanned = str;
    }

    public String getCannedText(String lang)
    {
        if(Languages.isEnglish(lang))
            return EnglishCanned;
        else if(Languages.isGreek(lang))
            return GreekCanned;
        else
            return "";
                
    }
        
    public void addUserType(String UserType)
    {
        UserTypes.add(UserType);
    }
    
    public void DeleteUserType(String UserType)
    {
         UserTypes.remove(UserType);
    }
    
    public void removeUserTypes()
    {
        UserTypes.clear();
    }
    
    public boolean isSuitablefor(String UserType) 
    {
        return UserTypes.contains(UserType);
    }
    
    public Vector<String> getUserTypes()
    {
        Vector<String> vec = new Vector<String>();
                
        Iterator<String> iter = UserTypes.iterator();
        while(iter.hasNext())
        {
            vec.add(iter.next());
        }
        
        return vec;
    }
}
