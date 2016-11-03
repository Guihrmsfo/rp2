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

package gr.aueb.cs.nlg.Communications;

import java.nio.charset.Charset;


public class Message 
{
    
    public tnavframe frame;
    
    public Message(tnavframe f) 
    {
        this.frame = f;
    }
    
    public int getPacketCode()
    {
        return this.frame.packetcode;
    }
    
    public String getXmlContent()
    {
        byte xml_in_bytes [] = new byte[frame.data.length-4];
        System.arraycopy(frame.data, 4, xml_in_bytes, 0,xml_in_bytes.length);
        String temp = new String(xml_in_bytes, Charset.forName("UTF-8"));
        
        if(temp.endsWith("\0"))
        {           
            return temp.substring(0, temp.length()-1);
        }
        else
        {
            return temp;
        }
    }    
}
