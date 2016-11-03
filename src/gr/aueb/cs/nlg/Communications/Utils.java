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

import java.io.*;
import java.nio.*;

// this class implements
// the ORCA protocol
public class Utils
{        
    public Utils()
    {
    }
 
    public static void readframe(DataInputStream ais, tnavframe aframe) throws IOException
    {
        try
        {
            
            byte[] ba = new byte[100];
            ais.read(ba, 0, 100);
            ByteBuffer bb = ByteBuffer.wrap(ba);
            bb.order(ByteOrder.LITTLE_ENDIAN);

            byte b1 = bb.get(); //211
            byte b2 = bb.get(); //222
            byte b3 = bb.get(); //233
            byte b4 = bb.get(); //244
            
            aframe.framecontents = bb.getInt();
            aframe.packetcode = bb.getInt();
            aframe.packetid = bb.getInt();
            aframe.ts_sec = bb.getInt();
            aframe.ts_usec = bb.getInt();
            
            aframe.i[0] = bb.getInt();
            aframe.i[1] = bb.getInt();
            aframe.i[2] = bb.getInt();
            aframe.i[3] = bb.getInt();
            aframe.i[4] = bb.getInt();
            aframe.i[5] = bb.getInt();
            aframe.d[0] = bb.getDouble();
            aframe.d[1] = bb.getDouble();
            aframe.d[2] = bb.getDouble(); 
            aframe.d[3] = bb.getDouble();
            aframe.d[4] = bb.getDouble();
            aframe.d[5] = bb.getDouble();
            
            // get the datalen field
            int datalen = bb.getInt(); 

            
            if (datalen != 0)
            {
                aframe.data = new byte[datalen];
                ais.read(aframe.data,0,datalen);
            }
            else
            {
                aframe.data = null;
            }
        }
        catch(Exception e)
        {
            //
        }
    } //readframe
    

        
    public static void readframeXML(DataInputStream ais, tnavframe aframe) throws IOException
    {
        try
        {
            byte[] ba = new byte[100];
            ais.readFully(ba, 0 , 100);
            ByteBuffer bb = ByteBuffer.wrap(ba);
            bb.order(ByteOrder.LITTLE_ENDIAN);

            byte b1 = bb.get(); //211
            byte b2 = bb.get(); //222
            byte b3 = bb.get(); //233
            byte b4 = bb.get(); //244
            
            aframe.framecontents = bb.getInt();
            aframe.packetcode = bb.getInt();
            aframe.packetid = bb.getInt();
            aframe.ts_sec = bb.getInt();
            aframe.ts_usec = bb.getInt();
            
            aframe.i[0] = bb.getInt();
            aframe.i[1] = bb.getInt();
            aframe.i[2] = bb.getInt();
            aframe.i[3] = bb.getInt();
            aframe.i[4] = bb.getInt();
            aframe.i[5] = bb.getInt();
            
            aframe.d[0] = bb.getDouble();
            aframe.d[1] = bb.getDouble();
            aframe.d[2] = bb.getDouble();
            aframe.d[3] = bb.getDouble();
            aframe.d[4] = bb.getDouble();
            aframe.d[5] = bb.getDouble();
            
            int datalen = bb.getInt();
            
            //System.err.println("aframe.packetcode:" + aframe.packetcode);
            //System.err.println("datalen received:" + datalen);
            
            aframe.datalen = datalen;
            //int datalen = bb.getInt();

            if (aframe.datalen!=0)
            {
                //if (aframe.datalen > 10000) aframe.datalen =  10000;
                
                aframe.data = new byte[aframe.datalen];
                
                ais.readFully(aframe.data, 0, aframe.datalen);
                //System.err.println("data received:" + new String(aframe.data) + "$$");        
            }
            else
            {
                aframe.data = null;
            }
        }
        catch(Exception e)
        {
            throw new IOException();
        }
    } //readframe    
    
    
    public static void writeframe(DataOutputStream aos, tnavframe aframe) throws IOException
    {
        byte[] ba = new byte[100];
        
        ByteBuffer bb=ByteBuffer.wrap(ba);
        bb.order(ByteOrder.LITTLE_ENDIAN );
        
        bb.put((byte)211);
        bb.put((byte)222);
        bb.put((byte)233);
        bb.put((byte)244);
        
        bb.putInt(aframe.framecontents);
        bb.putInt(aframe.packetcode);
        bb.putInt(aframe.packetid);
        bb.putInt(aframe.ts_sec);
        bb.putInt(aframe.ts_usec);
        bb.putInt(aframe.i[0]);
        bb.putInt(aframe.i[1]);
        bb.putInt(aframe.i[2]);
        bb.putInt(aframe.i[3]);
        bb.putInt(aframe.i[4]);
        bb.putInt(aframe.i[5]);
        bb.putDouble(aframe.d[0]);
        bb.putDouble(aframe.d[1]);
        bb.putDouble(aframe.d[2]);
        bb.putDouble(aframe.d[3]);
        bb.putDouble(aframe.d[4]);
        bb.putDouble(aframe.d[5]);
        
        if (aframe.data!=null) 
        {
            bb.putInt(aframe.data.length);
        }
        else 
        {
            bb.putInt(0);
        }
        
        aos.write(ba, 0, 100);
        
        if (aframe.data!=null) 
        {
            aos.write(aframe.data,0,aframe.data.length);
        }
        
    } //writeframe    \
    
   public static void writeframeXML(DataOutputStream aos,  int packet_code, String xmlData) throws IOException
    {
       try
       {
           
        System.err.println("writeframeXML");           
        xmlData = xmlData + "\0";
       
        byte[] ba = new byte[104];
        ByteBuffer bb = ByteBuffer.wrap(ba);
        bb.order(ByteOrder.LITTLE_ENDIAN );
        
        bb.put((byte)211);
        bb.put((byte)222);
        bb.put((byte)233);
        bb.put((byte)244);//4
        bb.putInt(1);
        bb.putInt(packet_code);
        bb.putInt(0);
        bb.putInt(0);
        bb.putInt(0);
        bb.putInt(0);
        bb.putInt(0);
        bb.putInt(0);
        bb.putInt(0);
        bb.putInt(0);
        bb.putInt(0);//48
        bb.putDouble(0);
        bb.putDouble(0);
        bb.putDouble(0);
        bb.putDouble(0);
        bb.putDouble(0);
        bb.putDouble(0);//96
        
        // set the data len field of the header               
        bb.putInt(4 + xmlData.getBytes("UTF-8").length);//100        
                    
        
        // set the data len of the xml
        bb.putInt(xmlData.getBytes("UTF-8").length); //104
                                
        System.err.println("data send:" + xmlData + "$$");        
        System.err.println("datalen send:" + (xmlData.getBytes("UTF-8").length));
          
        //sends the header
        aos.write(ba, 0 , 104);
        
        bb = ByteBuffer.wrap(xmlData.getBytes("UTF-8"));
        bb.order(ByteOrder.LITTLE_ENDIAN );
        
        aos.write(bb.array(), 0,  xmlData.getBytes("UTF-8").length);
        aos.flush();
        
       }
       catch(Exception e)
       {
           throw new IOException();
       }
        
    } //writeframe      
    

    public static void declareProducedConsumedPackets(DataOutputStream aos , int num_of_produced_packets, int num_of_consumed_packets, int [] produced , int[] consumed, String module_name)  throws IOException
    {
        String tmp ="";
        try
        {
            module_name = module_name + "\0";
            int module_name_size = module_name.getBytes("UTF-8").length;
            
            // 100 bytes for the header
            // 4 bytes -- integer -- the length of the module name
            // module name
            // 4 bytes -- integer -- the number of the produced packets
            // 4 bytes * num_of_produced_packets
            // 4 bytes -- integer -- the number of the consumed packets
            // 4 bytes * num_of_consumed_packets
            
            byte[] ba = new byte[100 + 4 + module_name_size + 4 * (1 + num_of_produced_packets + 1 + num_of_consumed_packets)];
            ByteBuffer bb = ByteBuffer.wrap(ba);
            bb.order(ByteOrder.LITTLE_ENDIAN );
                        
            bb.put((byte)211);
            bb.put((byte)222);
            bb.put((byte)233);
            bb.put((byte)244);
            bb.putInt(0);     //framecontents = moduleinfo
            bb.putInt(0);     //packetcode: unused
            bb.putInt(0);     //packetid:unused
            bb.putInt(0);     //ts_sec :unused
            bb.putInt(0);     //ts_usec :unused
            bb.putInt(0);     // i0
            bb.putInt(0);     // i1
            bb.putInt(0);     // i2
            bb.putInt(0);     // i3
            bb.putInt(0);     // i4
            bb.putInt(0);     // i5
            bb.putDouble(0);  //d0
            bb.putDouble(0);  //d1
            bb.putDouble(0);  //d2
            bb.putDouble(0);  //d3
            bb.putDouble(0);  //d4
            bb.putDouble(0);  //d5
            
            // set the data len field of the header
            bb.putInt(4 + module_name_size + 4 * (1 + num_of_produced_packets + 1 + num_of_consumed_packets));    
                                   
            bb.putInt(module_name_size);  // set the data len of the module name                
            bb.put((module_name).getBytes("UTF-8")); // set the module name
            
            bb.putInt(num_of_produced_packets); // set the number of packages the module produces
                                    
            for(int i = 0; i < num_of_produced_packets; i++)
            {
                bb.putInt(produced[i]); // produce 1
            }
            
            bb.putInt(num_of_consumed_packets); // set the number of packages the modules consumes
            
            for(int i = 0; i < num_of_consumed_packets; i++)
            {
                bb.putInt(consumed[i]);  // consumed 1
            }            
            
            //write it the output stream
            aos.write(ba, 0, ba.length);
            aos.flush();
        }
        catch(Exception e)
        {
            throw new IOException();
            //logger.debug(" Problem with connection");
        }
    }      
}
