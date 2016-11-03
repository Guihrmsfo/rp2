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

import gr.aueb.cs.nlg.*;
        
        
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.util.iterator.*;



import gr.aueb.cs.nlg.Languages.*;

public class MicroplansAndOrderingManager extends NLFileManager
{
        
    //public String NLGMicroplansAndOrderingNS = "http://www.aueb.gr/users/ion/owlnl/Microplans#";
         

    // resources
    public static final String MicroplansAndOrderingRes = "MicroplansAndOrdering";
    public static final String PropertyRes = "Property";
    public static final String MicroplanRes = "Microplan";
    
    public static final String OwnerRes = "Owner";
    public static final String FillerRes = "Filler";
    public static final String VerbRes = "Verb";
    public static final String TextRes = "Text";
    public static final String PrepRes = "Prep";
    public static final String AdverbRes = "Adverb";
    
    // properties
    public static final String OntologyPrp = "Ontology";
    public static final String PropertiesPrp = "Properties";
    public static final String OrderPrp = "Order";
    public static final String MicroplanNamePrp = "MicroplanName";
    public static final String UsedForComparisonsPrp = "UsedForComparisons";
    
    public static final String MicroplanSlotsPrp = "Slots";
    public static final String MicroplanUsedPrp = "Used";
        
    public static final String LanguagesMicroplansPrp [] = { "GreekMicroplans", "EnglishMicroplans"};
    
    public static final String ValPrp = "Val";
    public static final String pluralValPrp = "pluralVal";
    
    public static final String tensePrp = "tense";
    public static final String voicePrp = "voice";
    public static final String casePrp = "case";
    
    public static final String RETYPEPrp = "RETYPE";
    public static final String AggrAllowedPrp = "AggrAllowed";
    
    // resources
    public static Resource MicroplansAndOrderingResourceType = null;
    public static Resource MicroplanResourceType = null;
    public static Resource PropertyResourceType = null;
    public static Resource MicroplansResourceType = null;
    
    // properties
    public static Property OntologyProperty = null;
    public static Property PropertiesProperty = null;
    public static Property OrderProperty = null;
    
    public static Property GreekMicroplansProperty = null;
    public static Property EnglishMicroplansProperty = null;
    
    public static Property MicroplanNameProperty = null;
    public static Property MicroplanSlotsProperty = null;    
    public static Property MicroplanUsedProperty = null;
    public static Property UsedForComparisonsProperty = null;    
            
    public static Property ValProperty = null;
    public static Property pluralValProperty = null;
    
    public static Property tenseProperty = null;
    public static Property voiceProperty = null;
    public static Property caseProperty = null;
    
    public static Property RETYPEProperty = null;
    public static Property AggrAllowedProperty = null;
            
    public static Resource  OwnerResourseType = null;
    public static Resource  FillerResourseType = null;
    public static Resource  VerbResourseType = null;
    public static Resource  TextResourseType = null;
    public static Resource  PrepResourseType = null;
    public static Resource  AdverbResourseType = null;
    
    //----------------------------------------------------------------------------
    public MicroplansAndOrderingManager(String xb){ //
        super(xb);             
        
            
        // define the rdf:type resources
        MicroplansAndOrderingResourceType = model.createResource(owlnlNS + MicroplansAndOrderingRes);
        MicroplanResourceType = model.createResource(owlnlNS + MicroplanRes);
        PropertyResourceType = model.createResource(owlnlNS + PropertyRes);
        MicroplansResourceType = model.createResource(owlnlNS + MicroplanRes);
        
        OwnerResourseType = model.createResource(owlnlNS + OwnerRes);
        FillerResourseType = model.createResource(owlnlNS + FillerRes);
        VerbResourseType = model.createResource(owlnlNS + VerbRes);
        TextResourseType = model.createResource(owlnlNS + TextRes);
        PrepResourseType = model.createResource(owlnlNS + PrepRes);
        AdverbResourseType = model.createResource(owlnlNS + AdverbRes);
        
        //define properties
        OntologyProperty = model.createProperty(owlnlNS  + OntologyPrp);
        PropertiesProperty = model.createProperty(owlnlNS  + PropertiesPrp);
        OrderProperty = model.createProperty(owlnlNS  + OrderPrp);
        
        GreekMicroplansProperty = model.createProperty(owlnlNS  + LanguagesMicroplansPrp[0]);
        EnglishMicroplansProperty = model.createProperty(owlnlNS  + LanguagesMicroplansPrp[1]);
        
        MicroplanNameProperty = model.createProperty(owlnlNS  + MicroplanNamePrp);
        MicroplanSlotsProperty = model.createProperty(owlnlNS  + MicroplanSlotsPrp);   
        MicroplanUsedProperty = model.createProperty(owlnlNS + MicroplanUsedPrp);        
        UsedForComparisonsProperty = model.createProperty(owlnlNS + UsedForComparisonsPrp);
                
        ValProperty = model.createProperty(owlnlNS  + ValPrp);
        pluralValProperty = model.createProperty(owlnlNS  + pluralValPrp);
        
        tenseProperty = model.createProperty(owlnlNS  + tensePrp);
        voiceProperty = model.createProperty(owlnlNS  + voicePrp);
        caseProperty = model.createProperty(owlnlNS  + casePrp);
        
        RETYPEProperty = model.createProperty(owlnlNS  + RETYPEPrp);
        AggrAllowedProperty = model.createProperty(owlnlNS + AggrAllowedPrp);
        
        this.setPrettyTypes(new Resource[]{MicroplansAndOrderingResourceType,});
    }      
    //----------------------------------------------------------------------------
    public String getOrderForProperty(String ForProperty)
    {
        Resource PropertyResource = model.createResource(ForProperty);
        StmtIterator stmtIter = model.listStatements(PropertyResource, this.OrderProperty , (RDFNode)null);
        
        String ret ="";
        
        if(stmtIter.hasNext()){
            Statement stmt = stmtIter.nextStatement();
            ret = stmt.getObject().toString();
        }
        else
            ret = "";
        
        return ret;        
    }
    //----------------------------------------------------------------------------
    public int getNumOfMicroplans(String ForProperty,  String language){
        Resource PropertyResource = model.getResource(ForProperty);
        StmtIterator stmtIter = null;
        Statement stmt = null;
        String ret = "";
        String lang = "";
        
        if(true){ // greek
            stmtIter = model.listStatements(PropertyResource,this.GreekMicroplansProperty , (RDFNode)null);
            lang = "el";
        }
        else{
            stmtIter = model.listStatements(PropertyResource,this.EnglishMicroplansProperty , (RDFNode)null);
            lang = "en";
        }
        
        stmt = stmtIter.nextStatement();
        RDFList MicroplansList = (RDFList)stmt.getObject().as(RDFList.class);        
        return MicroplansList.size();
    }
    //----------------------------------------------------------------------------
    public Resource getMicroplan(String ForProperty, String templName, String language){
        
        Resource MicroplanResource = model.getResource(ForProperty);
        StmtIterator stmtIter = null;
        Statement stmt = null;
        String ret = "";
        
        if(Languages.isValid(language)){
        
            String MicroplanURI = ForProperty + "-" + templName + "-" + language;        
            System.out.println("looking for:" + ForProperty + "-" + templName + "-" + language);

            Resource microplan = model.getResource(MicroplanURI);
            return microplan;
        }
        
        return null;
    }
    
    //----------------------------------------------------------------------------
    public String getUsed(Resource microplan){
        
        StmtIterator StmtIter = model.listStatements(microplan, this.MicroplanUsedProperty , (RDFNode)null);
        
        if(StmtIter.hasNext()){
            Statement stmt = StmtIter.nextStatement();
            return stmt.getObject().toString();      
        }
        else
            return "";
    }
    //----------------------------------------------------------------------------
    public String getUsed(String ForProperty, String templName, String language){
        Resource microplan = getMicroplan(ForProperty,templName,language);
               
        StmtIterator StmtIter = model.listStatements(microplan, this.MicroplanUsedProperty , (RDFNode)null);
        
        if(StmtIter.hasNext()){
            Statement stmt = StmtIter.nextStatement();
            return stmt.getObject().toString();      
        }
        else
            return "";        
    }
    //----------------------------------------------------------------------------
    public String getMicroplanName(String ForProperty, String templName, String language){  
         Resource microplan = getMicroplan(ForProperty,templName,language);
        
        StmtIterator StmtIter = model.listStatements(microplan, model.getProperty(owlnlNS  + MicroplanNamePrp) , (RDFNode)null);
        if(StmtIter.hasNext()){
            Statement stmt = StmtIter.nextStatement();
            return stmt.getObject().toString();      
        }
        else
            return "";        
    }
    //----------------------------------------------------------------------------    
    public ExtendedIterator getSlots(Resource microplan){
        StmtIterator StmtIter = model.listStatements(microplan, model.getProperty(owlnlNS  + MicroplanSlotsPrp) , (RDFNode)null);
        Statement stmt = StmtIter.nextStatement();
        RDFList SlotsList = (RDFList)stmt.getObject().as(RDFList.class);
        
        return SlotsList.iterator();               
    }

    //----------------------------------------------------------------------------    
    public static void main(String args[]){//main
        MicroplansAndOrderingManager MaO = new MicroplansAndOrderingManager("http://www.owlnl.com/NLG");
        MaO.read("wine-microplans-corrected.rdf");
        //MaO.createMicroplansAndOrderingInfo();
        //MaO.write();
               
        String prp = "http://www.w3.org/TR/2003/CR-owl-guide-20030818/wine#hasColor";
        System.out.println("Order: " + MaO.getOrderForProperty(prp));
        
        Resource microplan = MaO.getMicroplan(prp, "templ1", Languages.GREEK);
        System.out.println(microplan.getURI());
        
        System.out.println("Used:" + MaO.getUsed(prp, "templ1", Languages.GREEK));
        System.out.println("MicroplanName:" + MaO.getMicroplanName(prp, "templ1", Languages.GREEK) );
        //System.out.println("num:" + MaO.getNumOfMicroplans(prp, "el"));
        //MaO.getSlots(microplan);

        MaO.CloseModel();
    }
}
