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

import gr.aueb.cs.nlg.Languages.*;

public class MicroplansAndOrderingNode
{
    private int Order;
    private boolean UsedForComparisons;
    private MicroplansList GreekMicroplans;
    private MicroplansList EnglishMicroplans;
    
    //constuctor
    public MicroplansAndOrderingNode(int Order,boolean UsedForComparisons,MicroplansList GM, MicroplansList EM)
    {
        this.Order = Order;
        this.UsedForComparisons = UsedForComparisons;
        this.GreekMicroplans = GM;
        this.EnglishMicroplans = EM;
    }
    
    public void print()
    {
        System.out.println("Order:" + Order);
        System.out.println("UsedForComparisons:" + UsedForComparisons);
        System.out.println("===GreekMicroplans===");
        GreekMicroplans.print();
        System.out.println("===EnglishMicroplans===");
        EnglishMicroplans.print();
    }
    
    //constructor 
    public MicroplansAndOrderingNode()
    {
        this.Order = 1;
        this.GreekMicroplans = new MicroplansList(Languages.GREEK);
        this.EnglishMicroplans = new MicroplansList(Languages.ENGLISH);
    }
    
    public void setUsedForComparisons(boolean UsedForComparisons){
        this.UsedForComparisons = UsedForComparisons;
    }
    
    public boolean getUsedForComparisons(){
        return this.UsedForComparisons;
    }
    
     public void setOrder(int Order){
        this.Order = Order;
    }
    
    public int getOrder(){
        return this.Order;
    }
    
    public void setMicroplans(MicroplansList M, String language) throws InvalidLanguageException{
        if(Languages.isEnglish(language))
        {
            this.EnglishMicroplans = M;
        }
        else if(Languages.isGreek(language))
        {
            this.GreekMicroplans = M;            
        }
        else
        {
            throw new InvalidLanguageException();
        }
    }
    
    public MicroplansList getMicroplans(String language) throws InvalidLanguageException{
        if(Languages.isEnglish(language))
        {
            return this.EnglishMicroplans;
        }
        else if(Languages.isGreek(language))
        {
            return this.GreekMicroplans; 
        }
        else
        {            
            throw new InvalidLanguageException();
        }
        
    }
}//MicroplansAndOrderingNode