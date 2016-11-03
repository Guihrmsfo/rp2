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

import javax.swing.*;


public class NLGManager extends JFrame
{
    
    private static WaitDialog dlg;
    private NLFilesPanel p;
    
    public NLFilesPanel getNLFilesPanel()
    {
        return p;
    }
    
    public NLGManager() 
    {
         this.setResizable(true);
         this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
         
         p = new NLFilesPanel();
 
                 
         dlg = new WaitDialog(p, this);
         
         p.setDialog(dlg);
    }
    
    public void showDialog()
    {
        dlg.setVisible(true);
    }
    
}
