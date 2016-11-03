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


public class UserTypeParameters
{
    
    private int MaxFactsPerSentence;
    private String SynthesizerVoice;
    private int FactsPerPage;
    private String Lang;
    
    public UserTypeParameters(int MFPS, String SynthesizerVoice,int FactsPerPage)
    {
        this.MaxFactsPerSentence = MFPS;
        this.SynthesizerVoice = SynthesizerVoice;
        this.FactsPerPage = FactsPerPage;
    }        

     public UserTypeParameters(int MFPS, String SynthesizerVoice,int FactsPerPage, String lang)
    {
        this.MaxFactsPerSentence = MFPS;
        this.SynthesizerVoice = SynthesizerVoice;
        this.FactsPerPage = FactsPerPage;
        this.Lang = lang;
    }        
        
    public String getLang()
    {
        return Lang;
    }
             
    public void setLang(String lang)
    {                
        this.Lang = lang;
    }
    
    public int getMaxFactsPerSentence()
    {
        return MaxFactsPerSentence;
    }
    
    public void setMaxFactsPerSentence(int MFPS)
    {
        MaxFactsPerSentence = MFPS;               
    } 
    
    public String getSynthesizerVoice()
    {
        return SynthesizerVoice;
    }
    
    public void setSynthesizerVoice(String SV)
    {
        SynthesizerVoice = SV;
    }
    
    public void setFactsPerPage(int FactsPerPage)
    {
        this.FactsPerPage = FactsPerPage;
    }
    
    public int getFactsPerPage()
    {
        return this.FactsPerPage;
    }    
}

