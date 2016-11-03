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

import gr.aueb.cs.nlg.Utils.*;

public class SurfaceRealizationHelper
{
    
    /** Creates a new instance of SurfaceRealizationHelper */
    public SurfaceRealizationHelper()
    {
    }
 
    public static String getfoo(String Gender, String number, String Case, boolean withArticle)
    {
        if(number.compareTo(XmlMsgs.SINGULAR)==0)
        {
             if(Gender.compareTo(XmlMsgs.GENDER_MASCULINE)==0)
             {
                  if(!withArticle)
                  {
                    if(Case.compareTo(XmlMsgs.NOMINATIVE_TAG)==0){
                        return "�����";
                    }
                    else if(Case.compareTo(XmlMsgs.GENITIVE_TAG)==0){
                        return "�����";
                    }
                    else if(Case.compareTo(XmlMsgs.ACCUSATIVE_TAG)==0){
                        return "�����";
                    }
                  }
                  else
                  {
                    if(Case.compareTo(XmlMsgs.NOMINATIVE_TAG)==0){
                        return "�����" + " " + "�";
                    }
                    else if(Case.compareTo(XmlMsgs.GENITIVE_TAG)==0){
                        return "�����" + " " + "���";
                    }
                    else if(Case.compareTo(XmlMsgs.ACCUSATIVE_TAG)==0){
                        return "�����"+ " " + "���";
                    }        
                  }
             }
             else if(Gender.compareTo(XmlMsgs.GENDER_FEMININE)==0)
             {
                  if(!withArticle)
                  {
                    if(Case.compareTo(XmlMsgs.NOMINATIVE_TAG)==0){
                        return "����";
                    }
                    else if(Case.compareTo(XmlMsgs.GENITIVE_TAG)==0){
                        return "�����";
                    }
                    else if(Case.compareTo(XmlMsgs.ACCUSATIVE_TAG)==0){
                        return "�����";
                    }
                  }
                  else
                  {
                    if(Case.compareTo(XmlMsgs.NOMINATIVE_TAG)==0){
                        return "����" + " " + "�";
                    }
                    else if(Case.compareTo(XmlMsgs.GENITIVE_TAG)==0){
                        return "�����" + " " + "���";
                    }
                    else if(Case.compareTo(XmlMsgs.ACCUSATIVE_TAG)==0){
                        return "�����" +  " " + "���";
                    }
                  }
             }
             else if(Gender.compareTo("neuter")==0)
             {
                 if(!withArticle)
                 {
                    if(Case.compareTo(XmlMsgs.NOMINATIVE_TAG)==0){
                        return "����";
                    }
                    else if(Case.compareTo(XmlMsgs.GENITIVE_TAG)==0){
                        return "����� ���";
                    }
                    else if(Case.compareTo(XmlMsgs.ACCUSATIVE_TAG)==0){
                        return "����� ���";
                    }
                 }
                 else
                 {
                    if(Case.compareTo(XmlMsgs.NOMINATIVE_TAG)==0){
                        return "����" + " " + "��";
                    }
                    else if(Case.compareTo(XmlMsgs.GENITIVE_TAG)==0){
                        return "�����" + " " +"���";
                    }
                    else if(Case.compareTo(XmlMsgs.ACCUSATIVE_TAG)==0){
                        return "����" + " "+ "��";
                    }
                 }
             }
             else
             {
                 if(!withArticle)
                 {
                    return "�����/����/����";
                 }
                 else
                 {
                    return "����� ��/���� �/���� ��";
                 }
                     
             }        
        }
        else if(number.compareTo(XmlMsgs.PLURAL)==0)
        {
             if(Gender.compareTo(XmlMsgs.GENDER_MASCULINE)==0)
             {
                 if(!withArticle)
                 {
                    if(Case.compareTo(XmlMsgs.NOMINATIVE_TAG)==0){
                        return "�����";
                    }
                    else if(Case.compareTo(XmlMsgs.GENITIVE_TAG)==0){
                        return "�����";
                    }
                    else if(Case.compareTo(XmlMsgs.ACCUSATIVE_TAG)==0){
                        return "�����";
                    }
                 }
                 else
                 {
                    if(Case.compareTo(XmlMsgs.NOMINATIVE_TAG)==0){
                        return "�����" + " " + "��";
                    }
                    else if(Case.compareTo(XmlMsgs.GENITIVE_TAG)==0){
                        return "�����" + " " + "���";
                    }
                    else if(Case.compareTo(XmlMsgs.ACCUSATIVE_TAG)==0){
                        return "�����"+  " " + "��";
                    }
                 }
             }
             else if(Gender.compareTo(XmlMsgs.GENDER_FEMININE)==0)
             {
                 if(!withArticle)
                 {
                    if(Case.compareTo(XmlMsgs.NOMINATIVE_TAG)==0){
                        return "�����";
                    }
                    else if(Case.compareTo(XmlMsgs.GENITIVE_TAG)==0){
                        return "�����";
                    }
                    else if(Case.compareTo(XmlMsgs.ACCUSATIVE_TAG)==0){
                        return "�����";
                    }
                 }
                 else
                 {
                    if(Case.compareTo(XmlMsgs.NOMINATIVE_TAG)==0){
                        return "�����" + " " + "��";
                    }
                    else if(Case.compareTo(XmlMsgs.GENITIVE_TAG)==0){
                        return "�����" + " " + "���";
                    }
                    else if(Case.compareTo(XmlMsgs.ACCUSATIVE_TAG)==0){
                        return "�����" + " " + "��";
                    }                     
                 }
             }
             else if(Gender.compareTo("neuter")==0)
             {
                 if(!withArticle)
                 {
                    if(Case.compareTo(XmlMsgs.NOMINATIVE_TAG)==0){
                        return "����" ;
                    }
                    else if(Case.compareTo(XmlMsgs.GENITIVE_TAG)==0){
                        return "�����";
                    }
                    else if(Case.compareTo(XmlMsgs.ACCUSATIVE_TAG)==0){
                        return "����";
                    }
                 }
                 else
                 {
                    if(Case.compareTo(XmlMsgs.NOMINATIVE_TAG)==0){
                        return "����" + " " + "��";
                    }
                    else if(Case.compareTo(XmlMsgs.GENITIVE_TAG)==0){
                        return "�����" + " " + "���";
                    }
                    else if(Case.compareTo(XmlMsgs.ACCUSATIVE_TAG)==0){
                        return "����" + " " + "��";
                    }                     
                 }
             }
             else
             {
                 if(!withArticle)
                    return "�����/�����/����";
                 else
                    return "����� ��/����� ��/���� ��";
             }              
        }
        else
        {
            return "�����/�����/����";
        }
        
        return "ERROR";
    }       
    
    public static String getfoo2(String Gender, String number, String Case)
    {
        if(number.compareTo(XmlMsgs.SINGULAR)==0)
        {
             if(Gender.compareTo(XmlMsgs.GENDER_MASCULINE)==0)
             {
                if(Case.compareTo(XmlMsgs.NOMINATIVE_TAG)==0){
                    return "�������������";
                }
                else if(Case.compareTo(XmlMsgs.GENITIVE_TAG)==0){
                    return "�������������";
                }
                else if(Case.compareTo(XmlMsgs.ACCUSATIVE_TAG)==0){
                    return "������������";
                }       
             }
             else if(Gender.compareTo(XmlMsgs.GENDER_FEMININE)==0)
             {
                if(Case.compareTo(XmlMsgs.NOMINATIVE_TAG)==0){
                    return "������������";
                }
                else if(Case.compareTo(XmlMsgs.GENITIVE_TAG)==0){
                    return "�������������";
                }
                else if(Case.compareTo(XmlMsgs.ACCUSATIVE_TAG)==0){
                    return "������������";
                }
             }
             else if(Gender.compareTo("neuter")==0)
             {

                if(Case.compareTo(XmlMsgs.NOMINATIVE_TAG)==0){
                    return "������������";
                }
                else if(Case.compareTo(XmlMsgs.GENITIVE_TAG)==0){
                    return "�������������";
                }
                else if(Case.compareTo(XmlMsgs.ACCUSATIVE_TAG)==0){
                    return "������������";
                }

             }
             else
             {
                    return "�����/����/����";     
             }        
        }
        else if(number.compareTo(XmlMsgs.PLURAL)==0)
        {
             if(Gender.compareTo(XmlMsgs.GENDER_MASCULINE)==0)
             {

                if(Case.compareTo(XmlMsgs.NOMINATIVE_TAG)==0){
                    return "�������������";
                }
                else if(Case.compareTo(XmlMsgs.GENITIVE_TAG)==0){
                    return "�������������";
                }
                else if(Case.compareTo(XmlMsgs.ACCUSATIVE_TAG)==0){
                    return "��������������";
                }
             }
             else if(Gender.compareTo(XmlMsgs.GENDER_FEMININE)==0)
             {
                    if(Case.compareTo(XmlMsgs.NOMINATIVE_TAG)==0){
                        return "�������������";
                    }
                    else if(Case.compareTo(XmlMsgs.GENITIVE_TAG)==0){
                        return "�������������";
                    }
                    else if(Case.compareTo(XmlMsgs.ACCUSATIVE_TAG)==0){
                        return "�������������";
                    }
             }
             else if(Gender.compareTo("neuter")==0)
             {
                    if(Case.compareTo(XmlMsgs.NOMINATIVE_TAG)==0){
                        return "������������" ;
                    }
                    else if(Case.compareTo(XmlMsgs.GENITIVE_TAG)==0){
                        return "�������������";
                    }
                    else if(Case.compareTo(XmlMsgs.ACCUSATIVE_TAG)==0){
                        return "������������";
                    }

             }
             else
             {
                    return "�����/�����/����";

             }              
        }
        
        return "ERROR";
    }           
    
}
