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

import javax.swing.*;
import java.util.*;
 
import org.apache.log4j.*;
import org.apache.log4j.spi.*;

public class LogTextArea extends JTextArea implements TextAreaAppenderListener
{
    
    /** Creates a new instance of LogTextArea */
    public LogTextArea()
    {      
        this.setEditable(false);
    }
    
    public void start()
    {
        Enumeration en = Logger.getRootLogger().getAllAppenders();
        
        while (en.hasMoreElements())
        {
          Appender ap = (Appender)en.nextElement();
          if (ap instanceof TextAreaAppender)
          {
            ((TextAreaAppender) ap).addTextAreaAppenderListener(this);
          }
        }
    }
        
    public void logItemAdded(String msg)
    {
       if(this.getText().length() > 1000000)
           this.setText("");
       else
           append(msg);
    }        
}
