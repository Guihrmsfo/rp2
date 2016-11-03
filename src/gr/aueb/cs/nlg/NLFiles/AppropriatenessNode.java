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

package gr.aueb.cs.nlg.NLFiles;

public class AppropriatenessNode{
    private String InterestValue;
    private String UserType;
    
    public AppropriatenessNode(String InterestValue, String UserType){
        this.InterestValue = InterestValue;
        this.UserType = UserType;
    }
    
    public String getUserType(){
        return this.UserType;
    }
    
    public void setUserType(String UT){
        this.UserType = UT;
    }
    
    public String geInterestValue(){
        return this.InterestValue;
    }
    
    public void setInterestValue(String IV){
        this.InterestValue = IV;
    }   
    
    public void print(){
        System.out.println("InterestValue " + InterestValue);
        System.out.println("UserType " + UserType);
    }
}//Appropr