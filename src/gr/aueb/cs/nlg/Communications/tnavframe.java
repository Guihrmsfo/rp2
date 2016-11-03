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

public class tnavframe
{
    
    public int      framecontents;  // 0:moduleinfo, 1:data, 2:request for data
    public int      packetcode;    // a code that identyfies the packet type (i.e odometry, laser, speed, etc)
    public int      packetid;      // a unique packet id or the id of the request
    public int      ts_sec;         // timestamp sec (unix timestamp sec)
    public int      ts_usec;        // timestamp usec (unix timestamp usec)
    public int      i[];          // six integer fields
    public double   d[];          // six double fields
    public int      datalen; 
    public byte     data[];
    
    // 0-3 4-99 99 to the end
    // tnavframe is from 4 to the end
    public tnavframe()
    {
        this.framecontents = 0; 
        this.packetcode = 0;
        this.packetid = 0;
        this.ts_sec = 0;
        this.ts_usec = 0;
        this.i = new int[6];
        this.i[0] = 0;
        this.i[1] = 0;
        this.i[2] = 0;
        this.i[3] = 0;
        this.i[4] = 0;
        this.i[5] = 0;
        this.d = new double[6];
        this.d[0] = 0;
        this.d[1] = 0;
        this.d[2] = 0;
        this.d[3] = 0;
        this.d[4] = 0;
        this.d[5] = 0;        
        this.datalen = 0;
        data = null;
    }                       
} 