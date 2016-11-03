package gr.aueb.cs.nlg.NLFiles;

import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;


public class NLIManager extends NLFileManager
{
             
    
    // resources
    public static final String WhPhrasesRes = "WhPhrases";
    public static final String NLIMappingRes = "NLIMapping";
    public static final String WhPhraseRes = "WhPhrase";
            
    // properties
    public static final String WhLinkPrp = "WhLink";
    public static final String whTextPrp = "whText";
   
    
    // resources
    public static Resource WhPhrasesResource = null;
    public static Resource NLIMappingResource = null;
    public static Resource WhPhraseResResource = null;
    
    // properties
    public static Property WhLinkProperty = null;
    public static Property whTextProperty = null;

    
    //----------------------------------------------------------------------------
    public NLIManager(String xb){ //
        super(xb);             
                    
        // define the rdf:type resources
        WhPhrasesResource = model.createResource(owlnlNS + WhPhrasesRes);
        NLIMappingResource = model.createResource(owlnlNS + NLIMappingRes);
        WhPhraseResResource = model.createResource(owlnlNS + WhPhraseRes);

        
        //define properties
        WhLinkProperty = model.createProperty(owlnlNS  + WhLinkPrp);
        whTextProperty = model.createProperty(owlnlNS  + whTextPrp);

        
        //this.setPrettyTypes(new Resource[]{MicroplansAndOrderingResourceType,});
    }      
    //----------------------------------------------------------------------------
 

    //----------------------------------------------------------------------------    
    public static void main(String args[]){//main

    }

}
