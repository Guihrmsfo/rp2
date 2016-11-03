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

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


/**
 *
 * @author galanisd
 */
public class WaitDialog extends JDialog
{
          
    private JComponent p;
    private JFrame frame;
    boolean waiting = false;
    
    public WaitDialog(JComponent pp, JFrame frame) 
    {
        super(frame, "NLG Manager", true);
        this.frame = frame;
        this.p = pp;
        this.getContentPane().add(p);
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                
               if(!waiting)
               {
                    dispose();
                    disp();
               }
            }
        });
    
        this.setAlwaysOnTop(true);
        
        
        // set size of the dialog
        Dimension d = new Dimension(400, 350);
        this.setPreferredSize(d); 
        //this.setSize(new Dimension(400, 350)); 
        this.setMinimumSize(d);
        this.setMaximumSize(d);
        
        this.setResizable(false);
        
        // set location of the dialog
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension size = this.getSize();
        this.setLocation( (screenSize.width - size.width) / 2, (screenSize.height - size.height) / 2 );         
        
        this.pack();
        
    }
    
    public void disp()
    {
        frame.dispose();
    }
    
    // set wait cursor
    public void setWaitCursor(boolean isWait)
    {
        if (isWait)
        {                        
            this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            waiting = true;            
        }
        else
        {                        
            this.setCursor(Cursor.getDefaultCursor());
            waiting = false;            
        }
    }    
    
    // a main for testing
    public static void main(String args[])
    {
        JFrame f = new JFrame();
           
    
        NLFilesPanel p = new NLFilesPanel();
        WaitDialog wdlg = new WaitDialog(p, f);
        
        p.setDialog(wdlg);
        wdlg.setVisible(true);
        wdlg.setWaitCursor(true);
    }
    
}
