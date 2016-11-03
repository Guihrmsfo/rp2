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
import java.awt.event.ActionEvent;


public class MyAction extends AbstractAction
{
    //private NLPlugin nlplug;
            
    public MyAction()
    {        
        super("NLG Manager");        
    }
    
    public void actionPerformed(ActionEvent event) 
    {
        NLGManager nlgmanager = new NLGManager(); 
        nlgmanager.getNLFilesPanel().EnableButtons(true, true, true, true,true,true,true);
        //nlgmanager.getNLFilesPanel().setValues(true,true,true,)
        nlgmanager.showDialog();
    }
    
    public static void main(String args[])
    {
        JFrame f = new JFrame();
        JButton button = new JButton(new MyAction());

        f.getContentPane().add(button);
        f.setVisible(true);
        f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }
}