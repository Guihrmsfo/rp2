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


import gr.aueb.cs.nlg.Communications.NLGEngineServer.NLGEngineServer;
import java.util.*;

/**
 *
 * @author Erevodifwntas
 */
public class spaceCalc {
    
    static fromTo dir;
    
    /** Creates a new instance of spaceCalc */
    public spaceCalc() {
                dir = new fromTo();
    }
    
    public static String positionRel(double x, double y, double Rx, double Ry, int dir, int frontView, int sideView, double arxh0)
    {
        //metatroph sto neo xwro
        double X = x-Rx;
        double Y = y-Ry;
                
        double dirEx;
        
        dirEx = Math.atan(Y/X);
        
        if (X<0)
        {
           
            dirEx =Math.toDegrees(dirEx)+180;
        }
        else
            dirEx =Math.toDegrees(dirEx);
        
        dirEx = (dirEx+arxh0)%360;
        
        
	double realDir = dirEx - dir;//Math.toDegrees(dir);
        
        if(!(realDir<frontView/2 && realDir>-frontView/2))
        if (realDir>frontView/2 && realDir<((frontView/2)+sideView))
        {
            return "left";
        }
        else
        {
            if (realDir<-frontView/2 && realDir>(-((frontView/2)+sideView)))
            {
                return "right";
            }
            else
                return "behind";
        }
        
        return "noWhere";
    }
    
    public static boolean inSameRoom(Vector<String> exhibits, Vector <String> rooms, exhibitsPositions positions)
    {
        boolean inTheRoom = false;
        for (int j=0; j<exhibits.size(); j++)
        {
            inTheRoom = false;
            for (int i=0; i<rooms.size(); i++)
            {
                try{
                if (inRoom(rooms.get(i),positions.positions.get(exhibits.get(j)).rooms))
                {
                    inTheRoom = true;
                    break;
                }
                }
                catch(Exception ignored)
                {
                    return false;
                }
            }
            if (!inTheRoom)
                return false;
        }

        
        return true;
    }
    
    public static boolean inRoom(String roomName, Vector <String> rooms)
    {
        for (int i=0; i<rooms.size(); i++)
        {
            if (rooms.get(i).equalsIgnoreCase(roomName))
                return true;
        }
        
        return false;
    }
    
    public static String places(Vector<String> exhibits, exhibitsPositions positions, NLGEngineServer nav, double arxh0)
    {
        String pos="";
        int []position = {0,0,0};
        String []posText = {"behind","right","left"};
        
        for (int i =0; i<exhibits.size(); i++)
        {
            if (lineOfSight(nav.X,nav.Y,positions.positions.get(exhibits.get(i)),positions, exhibits))
            {
                return "";
            }
        }
        
        for (int i =0; i<exhibits.size(); i++)
        {
            if (positions.positions.get(exhibits.get(i))==null)
            {
                return "";
            }
            String loc = positionRel(positions.positions.get(exhibits.get(i)).x, positions.positions.get(exhibits.get(i)).y, nav.X, nav.Y, nav.direction,100,100, arxh0);
            boolean breaks = false;
            for (int j=0; j<posText.length; j++)
            {
                if (loc.equalsIgnoreCase(posText[j]))
                {
                    position[j]+=1;
                    breaks = true;
                    break;
                }
            }
            if (!breaks)
            {
                return "";
            }
        }

        for (int i=0; i<position.length; i++)
        {
            if (position[i]>0)
            {
                pos+=posText[i];
            }
        }
        
        if (pos.equalsIgnoreCase(posText[0]+posText[1])||pos.equalsIgnoreCase(posText[0]+posText[2]))
            return "";
        
        return pos;
    }
    
    
    public static String oldPlaces(Vector<String> exhibits, exhibitsPositions positions, NLGEngineServer nav, double arxh0)
    {
        String pos="";
        int []position = {0,0,0};
        String []posText = {"behind","right","left"};
        
        for (int i =0; i<exhibits.size(); i++)
        {
            if (lineOfSight(nav.oldX,nav.oldY,positions.positions.get(exhibits.get(i)),positions, exhibits))
            {
                return "";
            }
        }
        
        for (int i =0; i<exhibits.size(); i++)
        {
            if (positions.positions.get(exhibits.get(i))==null)
            {
                return "";
            }
            String loc = positionRel(positions.positions.get(exhibits.get(i)).x, positions.positions.get(exhibits.get(i)).y, nav.oldX, nav.oldY, nav.oldDirection,100,100, arxh0);
            boolean breaks = false;
            for (int j=0; j<posText.length; j++)
            {
                if (loc.equalsIgnoreCase(posText[j]))
                {
                    position[j]+=1;
                    breaks = true;
                    break;
                }
            }
            if (!breaks)
            {
                return "";
            }
        }

        for (int i=0; i<position.length; i++)
        {
            if (position[i]>0)
            {
                pos+=posText[i];
            }
        }
        
        if (pos.equalsIgnoreCase(posText[0]+posText[1])||pos.equalsIgnoreCase(posText[0]+posText[2]))
            return "";
        
        return pos;
    }
    
    public static String placeOfExhibit(String exhibit, exhibitsPositions positions, NLGEngineServer nav, double arxh0)
    {
        String pos="";
        int []position = {0,0,0};
        String []posText = {"behind","right","left"};
        

	if (positions.positions.get(exhibit)==null)
	{
	    return "";
	}
	
	String loc = positionRel(positions.positions.get(exhibit).x, positions.positions.get(exhibit).y, nav.X, nav.Y, nav.direction,100,100, arxh0);
        
	return loc;
    }
    
    public static String previousPlaceOfExhibit(String exhibit, exhibitsPositions positions, NLGEngineServer nav, double arxh0)
    {
        String pos="";
        int []position = {0,0,0};
        String []posText = {"behind","right","left"};
        

	if (positions.positions.get(exhibit)==null)
	{
	    return "";
	}
	
	String loc = positionRel(positions.positions.get(exhibit).x, positions.positions.get(exhibit).y, nav.oldX, nav.oldY, nav.oldDirection,100,100, arxh0);
        
	return loc;
    }
    
    
    public static boolean areInMySight(Vector<String> exhibits, exhibitsPositions pos, double vision, NLGEngineServer nav)
    {
        boolean ISeeThemAll = true;
        for (int i=0; i<exhibits.size(); i++)
        {
            if (!inMySight(nav.X,nav.Y,pos.positions.get(exhibits.get(i)).x, pos.positions.get(exhibits.get(i)).y,vision))
            {
                ISeeThemAll = false;
                break;
            }
        }
        
        return ISeeThemAll;
    }
    
    public static fromTo direction(Vector<String> exhibits, exhibitsPositions pos, NLGEngineServer nav, double arxh0)
    {
        spaceCalc tmp = new spaceCalc();
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        
        for (int i=0; i<exhibits.size(); i++)
        {
            int position = computeDirection(pos.positions.get(exhibits.get(i)).x, pos.positions.get(exhibits.get(i)).y,nav.X,nav.Y, nav.direction, arxh0);
            
            if (min>position)
                min = position;
            if (max<position)
                max = position;
                        
        }

        if (max > 180 && min > 180)
        {
            tmp.dir.from = max;
            tmp.dir.to = min;
        }
        else
        {
            
	    tmp.dir.from = min;
            tmp.dir.to = max;
        }
        
        return tmp.dir;
    }
    
    public static int computeDirection(double exhibitX, double exhibitY, double robotX, double robotY, int lookingAt, double arxh0)
    {
        //int pos = 0;
        
        //metatroph sto neo xwro
        double X = exhibitX-robotX;
        double Y = exhibitY-robotY;
                System.out.println("X: "+exhibitX+ " Y:" + exhibitY);
                System.out.println("rX: "+robotX+ " rY:" + robotY);
        double dirEx;
        
        dirEx = Math.atan(Y/X);
        System.out.println("degrees: "+dirEx);
        double realDir = 0.0;
        if (dirEx<0)
        {
            dirEx =Math.toDegrees(dirEx)+360;
        }
        
        if (lookingAt>dirEx)
        {
            realDir = lookingAt-dirEx;
        }
        else
        {
            realDir = dirEx - lookingAt;
        }
        
        realDir+=arxh0;
        
        return (int)realDir;
    }
    
    /*
    public static void main(String args[])
    {
        int rX = 1;
        int rY = 1;
        int exhibitX = 1;
        int exhibitY = -5;
        int robotLooking = 180;
        
        int pos = computeDirection(exhibitX, exhibitY,rX,rY,robotLooking);
        System.out.println(pos);
    }*/
    
    
    
    public static boolean inMySight(double iAmX, double iAmY, double itIsX, double itIsY, double visionLength)
    {
        double Dx = iAmX-itIsX;
        double Dy = iAmY-itIsY;
        if( Math.sqrt(Dx*Dx+Dy*Dy) > visionLength)
        {
            return false;
        }
        else
            return true;
    }
    
    public static boolean lineOfSight(double fromX, double fromY, exhibitPosition to, exhibitsPositions pos, Vector<String> comparing)
    {
        //return false;
        
        boolean collision = false;
        
        double minX;
        double minY;
        double maxX;
        double maxY;
        
        if (fromX>to.x)
        {
            maxX = fromX;
            minX = to.x;
        }
        else
        {
            maxX = to.x;
            minX = fromX;
        }
        
        if (fromY>to.y)
        {
            maxY = fromY;
            minY = to.y;
        }
        else
        {
            maxY = to.y;
            minY = fromY;
        }
        
        //y = ax+b
        double a = (to.y - fromY)/(to.x - fromX);
        double b = to.y - a*to.x;
        
        
        for (int i=0; i<pos.positions.size(); i++)
        {
            
            if (!((String)pos.positions.keySet().toArray()[i]).equalsIgnoreCase(to.name))
            if (!comparing.contains(((String)pos.positions.keySet().toArray()[i])))
            {
                Vector<String> tmp = new Vector();
                tmp.add(((String)pos.positions.keySet().toArray()[i]));
                if (inSameRoom(tmp,pos.positions.get(tmp.get(0)).rooms,pos))
                {
                    //an anhkei sthn ey8eia mas
                    if (pos.positions.get(tmp.get(0)).y - a*pos.positions.get(tmp.get(0)).x-b<0.5 && pos.positions.get(tmp.get(0)).y - a*pos.positions.get(tmp.get(0)).x-b>-0.5)
                    {
                        //an einai anamesa mas
                        if (pos.positions.get(tmp.get(0)).x>minX && pos.positions.get(tmp.get(0)).x<maxX && pos.positions.get(tmp.get(0)).y>minY && pos.positions.get(tmp.get(0)).y<maxY)
                        {
                            collision = true;
                            return collision;
                        }
                    }
                    
                }
            }
            else
            {
                System.out.println("leitoyrgei");
            }
        
        }
        return collision;
    }

    class fromTo
    {
        public int from;
        public int to;    
    }
}
