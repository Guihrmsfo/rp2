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

package gr.aueb.cs.nlg.ProtegeAuthoringTool;

import gr.aueb.cs.nlg.Languages.*;
import gr.aueb.cs.nlg.NLFiles.UserModellingQueryManager;

public class Utils 
{

    public static boolean isLangSuitable(UserModellingQueryManager UMQM, String ut, String llllang)
    {
        if (llllang.equals(Languages.ENGLISH))
        {
            llllang = "English";
        }
        else if (llllang.equals(Languages.GREEK))
        {
            llllang = "Greek";
        }
                
        if(UMQM.getParametersForUserType(ut).getLang().equals(llllang)|| UMQM.getParametersForUserType(ut).getLang().equals("All"))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}