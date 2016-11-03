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

import java.util.*;
 
import org.apache.log4j.*;
import org.apache.log4j.spi.*;
 

public class TextAreaAppender extends AppenderSkeleton
{
  private Vector listeners;
  private Filter myFilter;
  private PatternLayout layout = new PatternLayout("%-19d{dd/MM hh:mm:ss,SSS} [%t] %-5p %c %x - %m");
 
  public TextAreaAppender()
  {
    super();
    listeners = new Vector (2);
  }
  
  
  public TextAreaAppender(Layout layout, String name) 
  {      
	super();
	this.setLayout(layout);
        this.setName(name);        
  }
  
  
  public void addTextAreaAppenderListener (TextAreaAppenderListener listener)
  {
    listeners.add (listener);
  }
 

  protected void append(LoggingEvent le)
  {
    String msg = getLayout().format(le);
    for (int i = 0; i < listeners.size(); i++)
      ((TextAreaAppenderListener) listeners.get(i)).logItemAdded(msg);
  }
 
  public void close()
  {  
  }
 
  public boolean requiresLayout()
  {
    return true;
  }
}
