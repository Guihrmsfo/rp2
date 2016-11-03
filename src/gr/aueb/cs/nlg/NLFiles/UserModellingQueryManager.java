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



import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.vocabulary.*;
import com.hp.hpl.jena.ontology.*;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;

import com.hp.hpl.jena.util.iterator.*;


import java.util.*;
import java.util.Hashtable;


import org.w3c.dom.*;


import gr.aueb.cs.nlg.*;
import gr.aueb.cs.nlg.NLGEngine.NLGEngine;
import gr.aueb.cs.nlg.Utils.*;
        
import org.apache.log4j.Logger;


public class UserModellingQueryManager
{     
    public String NLGUserModellingNS = "http://www.aueb.gr/users/ion/owlnl/UserModelling#";
    static Logger logger = Logger.getLogger(UserModellingQueryManager.class);
    //appropriateness
    private Hashtable<String, Approps> MicroplansAppropriateness; 
    
    /*************************************************/
    //interest hashtables
    
    private Hashtable<String, Parameters> DPropertyInterests; // default property interests
    private Hashtable<CDPInterestKey, Parameters> CDPropertyInterests; // class property interests
    private Hashtable<IPInterestKey, Parameters> IPropertyInterests; // instance property interests

    private One2ManyMapping Properties2Cls_forInter;
    private One2ManyMapping Cls2Properties_forInter;

    private One2ManyMapping Properties2Instances_forInter;
    private One2ManyMapping Instances2Properties_forInter;
    /*************************************************/
    
    //repetitions hashtables
    private Hashtable<String, Parameters> DPropertyRepetitions; // default property repetitions
    private Hashtable<CDPInterestKey, Parameters> CDPropertyRepetitions; // class property repetitions
    private Hashtable<IPInterestKey, Parameters> IPropertyRepetitions; // instance property repetitions
    
    private One2ManyMapping Properties2Cls_forRep;
    private One2ManyMapping Cls2Properties_forRep;
    
    private One2ManyMapping Properties2Instances_forRep;
    private One2ManyMapping Instances2Properties_forRep;
    /*************************************************/
    
    private Hashtable<String, Parameters> DClsInterests;
    private Hashtable<ClassInstanceKey, Parameters> ClsInterests;

    private Hashtable<String, Parameters> DClsRepetitions;
    private Hashtable<ClassInstanceKey, Parameters> ClsRepetitions;
        
    /*************************************************/
    
    //user types
    private Hashtable<String, UserTypeParameters> UserTypes;
    /*************************************************/
    
    private String UserModellingFileName;    
    public OntModel model;
    
    public UserModellingQueryManager (OntModel model)
    {        
        this.model = model;	
    }
    
        
    // initialize all hashtables
    public void init()
    {
        NLGUserModellingNS = "http://www.aueb.gr/users/ion/owlnl/UserModelling#";
        MicroplansAppropriateness = new  Hashtable<String, Approps>();
        
        DPropertyInterests = new Hashtable<String, Parameters>(); 
        CDPropertyInterests = new Hashtable<CDPInterestKey, Parameters>(); 
        IPropertyInterests = new Hashtable<IPInterestKey, Parameters>(); 
                       
        DPropertyRepetitions = new Hashtable<String, Parameters>(); 
        CDPropertyRepetitions = new Hashtable<CDPInterestKey, Parameters>(); 
        IPropertyRepetitions = new Hashtable<IPInterestKey, Parameters>(); 
    
        DClsInterests = new Hashtable<String, Parameters>();
        ClsInterests = new Hashtable<ClassInstanceKey, Parameters>();
        
        DClsRepetitions = new Hashtable<String, Parameters>();
        ClsRepetitions = new Hashtable<ClassInstanceKey, Parameters>();
        
        UserTypes = new Hashtable<String, UserTypeParameters>(); // user types   
        
        Properties2Cls_forInter = new One2ManyMapping();
        Cls2Properties_forInter = new One2ManyMapping();

        Properties2Instances_forInter  = new One2ManyMapping();
        Instances2Properties_forInter  = new One2ManyMapping();
        
        Properties2Cls_forRep = new One2ManyMapping();
        Cls2Properties_forRep = new One2ManyMapping();

        Properties2Instances_forRep  = new One2ManyMapping();
        Instances2Properties_forRep  = new One2ManyMapping();        
    }
    
    /**********************************************************/
    // delete a user type
    public void DeleteUserType(String UserType)
    {
        // delete user type and its parameters
        UserTypes.remove(NLGUserModellingNS + UserType);
        
        
        // delete and all the UM entries which are related
        // to this user type
        
        deleteUserTypeEntries( this.DPropertyInterests, UserType);
        deleteUserTypeEntries( this.CDPropertyInterests, UserType);
        deleteUserTypeEntries( this.IPropertyInterests, UserType);
        
        deleteUserTypeEntries( this.DPropertyRepetitions, UserType);        
        deleteUserTypeEntries( this.CDPropertyRepetitions, UserType);
        deleteUserTypeEntries( this.IPropertyRepetitions, UserType);
     
    }
    
    public void deleteUserTypeEntries(Hashtable AA, String ut)
    {        
        
        Hashtable<String, Parameters> A = (Hashtable<String, Parameters>) AA ;
        
        Iterator iter = A.keySet().iterator();       
       
        while(iter.hasNext())
        {
            Object key = iter.next();
            
            A.get(key).getParameters().remove(ut);
        }
    }
    
    public void renameUserTypeEntries(Hashtable AA, String oldUT, String newUT)
    {        
        logger.debug("renameUserTypeEntries " + oldUT + " " + newUT);
        Hashtable<String, Parameters> A = (Hashtable<String, Parameters>) AA ;
        
        Iterator iter = A.keySet().iterator();       
       
        while(iter.hasNext())
        {
            
            Object key = iter.next();
            
            logger.debug("xxxxx->>>>" + key.toString());
            
            ParameterNode PN = A.get(key).getParameters().remove(oldUT); // remove it
            
            if(PN!=null)
            {
                PN.setforUserType(newUT); // change ut;
                A.get(key).getParameters().put(newUT, PN); // put it back
            }
        }
    }
    
    // add a new user type
    public void AddUserType(String UT)
    {

       UserTypes.put(NLGUserModellingNS + UT, new UserTypeParameters(1 ,"male", 5));
    }
    
    // rename user type
    public void RenameUserType(String oldUT, String newUT)
    {
        logger.debug("RenameUserType " + oldUT + " " + newUT);
        
        UserTypeParameters UTParams = UserTypes.remove(oldUT); // remove ut
        UserTypes.put(newUT,UTParams); // add ut with new name
        
        renameUserTypeEntries(this.DPropertyInterests,  oldUT,  newUT);
        renameUserTypeEntries(this.CDPropertyInterests,  oldUT,  newUT);
        renameUserTypeEntries(this.IPropertyInterests,  oldUT,  newUT);
        
        
        renameUserTypeEntries(this.DPropertyRepetitions,  oldUT,  newUT);
        renameUserTypeEntries(this.CDPropertyRepetitions,  oldUT,  newUT);
        renameUserTypeEntries(this.IPropertyRepetitions,  oldUT,  newUT);
        
    }
    /**********************************************************/
        
    public void RenameClass(String old_classURI, String new_classURI)
    {
        
        Iterator iter = CDPropertyInterests.keySet().iterator();
        
        while(iter.hasNext())
        {
            CDPInterestKey key = (CDPInterestKey)iter.next();
                    
            if(key.forOwlClass.equals(old_classURI))
            {
                Parameters params = (Parameters)CDPropertyInterests.remove(key); // remove
                key.forOwlClass = new_classURI; //rename
                CDPropertyInterests.put(key, params);
            }            
        }
        
        iter = CDPropertyRepetitions.keySet().iterator();
        
        while(iter.hasNext())
        {
            CDPInterestKey key = (CDPInterestKey)iter.next();
                    
            if(key.forOwlClass.equals(old_classURI))
            {
                Parameters params = CDPropertyRepetitions.remove(key); // remove
                key.forOwlClass = new_classURI; //rename
                CDPropertyInterests.put(key, params);
            }            
        }        
        
        if(DClsInterests.containsKey(old_classURI))
        {
            Parameters inters = DClsInterests.remove(old_classURI);
            DClsInterests.put(new_classURI, inters);
        }
        
        if(DClsRepetitions.containsKey(old_classURI))
        {
            Parameters reps = DClsRepetitions.remove(old_classURI);
            DClsRepetitions.put(new_classURI, reps);
        }
        
        iter = ClsInterests.keySet().iterator();
        
        while(iter.hasNext())
        {
            ClassInstanceKey CIK = (ClassInstanceKey)iter.next();
            
            if(CIK.ClassURI.equals(old_classURI))
            {
                Parameters params = ClsInterests.remove(CIK);
                CIK.ClassURI = new_classURI;
                ClsInterests.put(CIK, params);
            }
            
            if(CIK.forInstance.equals(old_classURI))
            {
                Parameters params = ClsInterests.remove(CIK);
                CIK.forInstance = new_classURI;
                ClsInterests.put(CIK, params);
            }            
                
        }
        
        iter = ClsRepetitions.keySet().iterator();
        
        while(iter.hasNext())
        {
            ClassInstanceKey CIK = (ClassInstanceKey)iter.next();
            
            if(CIK.ClassURI.equals(old_classURI))
            {
                Parameters params = ClsRepetitions.remove(CIK);
                CIK.ClassURI = new_classURI;
                ClsRepetitions.put(CIK, params);                
            }
            
            if(CIK.forInstance.equals(old_classURI))
            {
                Parameters params = ClsRepetitions.remove(CIK);
                CIK.forInstance = new_classURI;
                ClsRepetitions.put(CIK, params);
            }                        
                
        }
                
    }
        
    
    public void RenameIntance(String old_instanceURI, String new_instanceURI)
    {
        Iterator iter = IPropertyInterests.keySet().iterator();
        
        while(iter.hasNext())
        {
            IPInterestKey key = (IPInterestKey)iter.next();
                    
            if(key.forInstance.equals(old_instanceURI))
            {
                Parameters params = IPropertyInterests.remove(key); // remove
                key.forInstance = new_instanceURI; //rename
                IPropertyInterests.put(key, (Interests)params);
            }            
        }
        
        iter = IPropertyRepetitions.keySet().iterator();
        
        while(iter.hasNext())
        {
            IPInterestKey key = (IPInterestKey)iter.next();
                    
            if(key.forInstance.equals(old_instanceURI))
            {
                Parameters params = IPropertyRepetitions.remove(key); // remove
                key.forInstance = new_instanceURI; //rename
                IPropertyRepetitions.put(key, (Repetitions)params);
            }            
        }        
                        
        iter = ClsInterests.keySet().iterator();
        
        while(iter.hasNext())
        {
            ClassInstanceKey CIK = (ClassInstanceKey)iter.next();
            
            if(CIK.forInstance.equals(old_instanceURI))
            {
                Parameters params = ClsInterests.remove(CIK);
                CIK.forInstance = new_instanceURI;
                ClsInterests.put(CIK, params);
            }
                
        }
        
        iter = ClsRepetitions.keySet().iterator();
        
        while(iter.hasNext())
        {
            ClassInstanceKey CIK = (ClassInstanceKey)iter.next();
            
            if(CIK.forInstance.equals(old_instanceURI))
            {
                Parameters params = ClsRepetitions.remove(CIK);
                CIK.forInstance = new_instanceURI;
                ClsRepetitions.put(CIK, params);                
            }
                
        }        
    }    
    
    public void RenameProperty(String old_PrpURI, String new_PrpURI)
    {
        if(DPropertyInterests.containsKey(old_PrpURI))
        {
            Parameters inters = DPropertyInterests.remove(old_PrpURI);
            DPropertyInterests.put(new_PrpURI , inters); 
        }
        
        if(DPropertyRepetitions.containsKey(old_PrpURI))
        {
            Parameters reps = DPropertyRepetitions.remove(old_PrpURI);
            DPropertyRepetitions.put(new_PrpURI , reps); 
        }        
        
        Iterator iter = CDPropertyInterests.keySet().iterator();
        
        while(iter.hasNext())
        {
            CDPInterestKey key = (CDPInterestKey)iter.next();
                    
            if(key.PropertyURI.equals(old_PrpURI))
            {
                Parameters params = CDPropertyInterests.remove(key); // remove
                key.PropertyURI = new_PrpURI; //rename
                CDPropertyInterests.put(key, (Interests)params);
            }            
        }
        
        iter = CDPropertyRepetitions.keySet().iterator();
        
        while(iter.hasNext())
        {
            CDPInterestKey key = (CDPInterestKey)iter.next();
                    
            if(key.PropertyURI.equals(old_PrpURI))
            {
                Parameters params = CDPropertyRepetitions.remove(key); // remove
                key.PropertyURI = new_PrpURI; //rename
                CDPropertyInterests.put(key, (Interests)params);
            }            
        }        
        
        iter = IPropertyInterests.keySet().iterator();
        
        while(iter.hasNext())
        {
            IPInterestKey key = (IPInterestKey)iter.next();
                    
            if(key.PropertyURI.equals(old_PrpURI))
            {
                Parameters params = IPropertyInterests.remove(key); // remove
                key.PropertyURI = new_PrpURI; //rename
                IPropertyInterests.put(key, (Interests)params);
            }            
        }
        
        iter = IPropertyRepetitions.keySet().iterator();
        
        while(iter.hasNext())
        {
            IPInterestKey key = (IPInterestKey)iter.next();
                    
            if(key.PropertyURI.equals(old_PrpURI))
            {
                Parameters params = IPropertyRepetitions.remove(key); // remove
                key.PropertyURI = new_PrpURI; //rename
                IPropertyRepetitions.put(key, (Repetitions)params);
            }            
        }        
            
    }
    
    
    public void DeleteProperty(String PrpURI)
    {
        // delete default property interest
        DPropertyInterests.remove(PrpURI);
        
        // delete default class property interest
        Iterator iter = Properties2Cls_forInter.getValues(PrpURI);
        
        while(iter.hasNext())
        {
            String Cls = iter.next().toString();  
            CDPropertyInterests.remove(new CDPInterestKey(PrpURI , Cls));
        }
        
        //update mappings
        Properties2Cls_forInter.removeKey(PrpURI);
        Cls2Properties_forInter.removeValue(PrpURI);
                
        // delete instances property interest
        iter = this.Properties2Instances_forInter.getValues(PrpURI);
        
        while(iter.hasNext())
        {
            String inst = iter.next().toString();  
            CDPropertyInterests.remove(new IPInterestKey(PrpURI , inst));
        }        
        
        //update mappings
        Properties2Instances_forInter.removeKey(PrpURI);
        Instances2Properties_forInter.removeValue(PrpURI);     
        
        //!!!!!!!!!!!!!!!!
        
        // delete default property rep
        DPropertyRepetitions.remove(PrpURI);
        
        // delete default class property rep
        iter = Properties2Cls_forRep.getValues(PrpURI);
        
        while(iter.hasNext())
        {
            String Cls = iter.next().toString();  
            CDPropertyRepetitions.remove(new CDPInterestKey(PrpURI , Cls));
        }
        
        //update mappings
        Properties2Cls_forRep.removeKey(PrpURI);
        Cls2Properties_forRep.removeValue(PrpURI);
                
        // delete instances property interest
        iter = Properties2Instances_forRep.getValues(PrpURI);
        
        while(iter.hasNext())
        {
            String inst = iter.next().toString();  
            CDPropertyRepetitions.remove(new IPInterestKey(PrpURI , inst));
        }        
        
        //update mappings
        Properties2Instances_forRep.removeKey(PrpURI);
        Instances2Properties_forRep.removeValue(PrpURI);            
    }
    
    public void DeleteClass(String ClassUri)
    {
        // delete default class property ****inter*****
        Iterator iter = Cls2Properties_forInter.getValues(ClassUri);
        
        while(iter.hasNext())
        {
            String PrpURI = iter.next().toString();  
            this.CDPropertyInterests.remove(new CDPInterestKey(PrpURI , ClassUri));
        }
        
        //update mappings
        Cls2Properties_forInter.removeKey(ClassUri);
        Properties2Cls_forInter.removeValue(ClassUri);
                
        // delete default class property *****repetitions*******
        iter = Cls2Properties_forRep.getValues(ClassUri);
        
        while(iter.hasNext())
        {
            String PrpURI = iter.next().toString();  
            this.CDPropertyRepetitions.remove(new CDPInterestKey(PrpURI , ClassUri));
        }
        
        //update mappings
        Cls2Properties_forRep.removeKey(ClassUri);
        Properties2Cls_forRep.removeValue(ClassUri);    
    }
    
    public void DeleteInstance(String InstUri)
    {
                
        // delete default class property *****inter*****
        Iterator iter = Instances2Properties_forInter.getValues(InstUri);
        
        while(iter.hasNext())
        {
            String PrpURI = iter.next().toString();  
            CDPropertyInterests.remove(new IPInterestKey(PrpURI , InstUri));
        }
        
        //update mappings
        Instances2Properties_forInter.removeKey(InstUri);
        Properties2Instances_forInter.removeValue(InstUri);
                
        // delete default class property *****rep*****
        iter = Instances2Properties_forRep.getValues(InstUri);
        
        while(iter.hasNext())
        {
            String PrpURI = iter.next().toString();  
            this.CDPropertyRepetitions.remove(new CDPInterestKey(PrpURI , InstUri));
        }
        
        //update mappings
        Instances2Properties_forRep.removeKey(InstUri);
        Properties2Instances_forRep.removeValue(InstUri);          
    }
    /**********************************************************/
    public void setDInterestForProperty(String property, String userType, String value)
    {        
        update(DPropertyInterests,property,userType,value, isNullvalue(value), false, null, null, null, null);
    }
    
    public void setDRepetitionsForProperty(String property, String userType, String value)
    {
        update(DPropertyRepetitions,property,userType,value, isNullvalue(value), false, null, null, null, null);
    }
    
    public void setClassInterestForProperty(String property, String Cls,  String userType, String value)
    {                         
        update(CDPropertyInterests,new CDPInterestKey(property,Cls), userType,value, isNullvalue(value), true, Properties2Cls_forInter, Cls2Properties_forInter, property, Cls);
    }

    public void setClassRepetitionsForProperty(String property, String Cls,  String userType, String value)
    {
         update(CDPropertyRepetitions,new CDPInterestKey(property,Cls), userType,value, isNullvalue(value), true, Properties2Cls_forRep, Cls2Properties_forRep, property, Cls);
    }
    
    public void setInstanceInterestForProperty(String property, String instance,  String userType, String value)
    {
        logger.debug("saving..." + property  + " " + instance + " " + userType + " " + value);
                
        update(IPropertyInterests,new IPInterestKey(property, instance), userType,value, isNullvalue(value), true, Properties2Instances_forInter, Instances2Properties_forInter, property, instance);
                           
    }
    
    public void setInstanceRepetitionsForProperty(String property, String instance,  String userType, String value)
    {
        logger.debug("saving..." + property  + " " + instance + " " + userType + " " + value);
       
        update(IPropertyRepetitions,new IPInterestKey(property, instance), userType,value, isNullvalue(value), true, Properties2Instances_forRep, Instances2Properties_forRep, property, instance);

    }    
    
    private void update(Hashtable hst, Object key, String userType, String value, boolean isNullvalue, boolean UpdateMapping, One2ManyMapping A, One2ManyMapping invA, String keyA, String invkeyA)
    {
        Parameters params = null;
                 
        if(hst.containsKey(key))
        {
             params = (Parameters)hst.get(key);
        }
        else
        {
             params = new Parameters();
        }
        
       
        HashMap<String, ParameterNode> map = params.getParameters();
        
        if(!isNullvalue)            
        {
            map.put(userType, new ParameterNode(userType, value));
            
            if(UpdateMapping)
            {
                A.AddMapping(keyA, invkeyA);
                invA.AddMapping(invkeyA, keyA);
            }
        }
        else
        {
            map.remove(userType);
            
            if(UpdateMapping)
            {            
                A.removeValue(keyA, invkeyA);
                invA.removeValue(invkeyA, keyA);            
            }
        }
        
        hst.put(key, params);        
    }
    
    private boolean isNullvalue(String value)
    {
        if(value != null && value.compareTo("")!=0)
            return false;
        else
            return true;
                    
    }
    
    public UserModellingManager UMM;
    /**********************************************************/
    // load user modelling info from rdf file
    public void LoadUserModellingInfo(String path, String file)
    {        
        init();
        
        // read file
        UMM = new UserModellingManager("");
        UMM.read(path,file);
        this.NLGUserModellingNS = UMM.model.getNsPrefixURI("") ;
        
        if(NLGUserModellingNS == null)
            NLGUserModellingNS = "http://www.aueb.gr/users/ion/owlnl/UserModelling#";
        
        // load user types parameters
        ExtendedIterator userTypes = UMM.get(UMM.UserTypesProperty);
        
        logger.debug("Reading User Types...");
        
        while(userTypes!=null && userTypes.hasNext())
        {
            Resource currentUserType = (Resource)userTypes.next();
            
            String UserType = currentUserType.getURI();
            
            logger.debug("UserType..." + UserType);
            
            String MFPS = currentUserType.getProperty(UMM.MaxFactsPerSentenceProperty).getLiteral().getString();
            String SV = currentUserType.getProperty(UMM.SynthesizerVoiceProperty).getLiteral().getString();
            String FPS = currentUserType.getProperty(UMM.FactsPerPageProperty).getLiteral().getString();
            String Lang = "All";
            
            if(currentUserType.getProperty(UMM.LangProperty) !=null)
            {
                Lang = currentUserType.getProperty(UMM.LangProperty).getLiteral().getString();
            }
                
            UserTypes.put(UserType, new UserTypeParameters(Integer.parseInt(MFPS),SV, Integer.parseInt(FPS), Lang));
        }
        
        // finished loading user types parameters
        
         // load repetitions and interests
        
         ExtendedIterator prps = UMM.get(UMM.PropertiesInterestsRepetitionsProperty);
         Model m = UMM.getModel();
         
         Parameters repetitions;
         Parameters inters;
         
         logger.debug("Reading Properties Interest/Repetitions...");
         
         while(prps!=null && prps.hasNext())
         {//for each property
             Resource currentProp = (Resource)prps.next();
             
             String currentPropURI = currentProp.getURI();             
             logger.debug("Property..." + currentPropURI);
             
             // load default property repetitions-interests 
             StmtIterator iter = null;
             iter = m.listStatements(currentProp, UMM.DPInterestRepetitionsProperty, (RDFNode)null);
             
             repetitions = new Parameters(); 
             inters = new Parameters();
             
             while(iter != null && iter.hasNext())
             {                                 
                 Statement t = iter.nextStatement();
                 Resource bNode = (Resource)t.getObject().as(Resource.class);
                 
                 String UT = ((Resource)bNode.getProperty(UMM.forUserTypeProperty).getObject().as(Resource.class)).getURI();
                 String rep = bNode.getProperty(UMM.RepetitionsValueProperty).getLiteral().getString();
                 String inter = bNode.getProperty(UMM.InterestValueProperty).getLiteral().getString();
                 
                 repetitions.add(new ParameterNode(UT,rep));
                 inters.add(new ParameterNode(UT, inter));
             }
             
             DPropertyRepetitions.put(currentPropURI , repetitions);
             DPropertyInterests.put(currentPropURI , inters);
             
             //logger.debug(currentPropURI); 
             //repetitions.print();
             
             // load default class repetitions-interests
             
             iter =  m.listStatements(currentProp , UMM.CDPInterestRepetitionsProperty, (RDFNode)null);
                          
             while(iter != null && iter.hasNext())
             {                 
                 Statement t = iter.nextStatement();
                 Resource bNode = (Resource)t.getObject().as(Resource.class);
                 
                 String forOwlClass =  ((Resource)bNode.getProperty(UMM.forOwlClassProperty).getObject().as(Resource.class)).getURI();
                 String UT = ((Resource)bNode.getProperty(UMM.forUserTypeProperty).getObject().as(Resource.class)).getURI();
                 
                 String rep = bNode.getProperty(UMM.RepetitionsValueProperty).getLiteral().getString();                                  
                 String inter = bNode.getProperty(UMM.InterestValueProperty).getLiteral().getString();                                  
                 
                 CDPInterestKey CDPIKey = new CDPInterestKey(currentPropURI, forOwlClass);
                 //CDPIKey.print();
                         
                 if(CDPropertyRepetitions.containsKey(CDPIKey))
                 {
                     Parameters reps = CDPropertyRepetitions.get(CDPIKey);
                     reps.add(new ParameterNode(UT,rep));
                     CDPropertyRepetitions.put(CDPIKey, reps);
                     
                     //logger.debug(currentPropURI); 
                     //inters.print();
                 }
                 else
                 {
                     Parameters reps = new Repetitions();
                     reps.add(new ParameterNode(UT,rep));
                     CDPropertyRepetitions.put(CDPIKey, reps);
                     
                     //logger.debug(currentPropURI); 
                     //inters.print();
                 }
                 
                 if(CDPropertyInterests.containsKey(CDPIKey))
                 {
                     Parameters interests = CDPropertyInterests.get(CDPIKey);
                     interests.add(new ParameterNode(UT,inter));
                     CDPropertyInterests.put(CDPIKey, interests);
                     
                     //logger.debug(currentPropURI); 
                     //inters.print();
                 }
                 else
                 {
                     Parameters interests = new Interests();
                     interests.add(new ParameterNode(UT,inter));
                     CDPropertyInterests.put(CDPIKey, interests);
                     
                     //logger.debug(currentPropURI); 
                     //inters.print();
                 }             
                 
                 Properties2Cls_forInter.AddMapping(currentPropURI, forOwlClass);
                 Cls2Properties_forInter.AddMapping(forOwlClass, currentPropURI);
                 
                 Properties2Cls_forRep.AddMapping(currentPropURI, forOwlClass);
                 Cls2Properties_forRep.AddMapping(forOwlClass, currentPropURI);                 
             }
             
             // load instances repetitions-interests
             iter =  m.listStatements(currentProp , UMM.IPInterestRepetitionsProperty, (RDFNode)null);
             
             while(iter != null && iter.hasNext())
             {
                 
                 Statement t = iter.nextStatement();
                 Resource bNode = (Resource)t.getObject().as(Resource.class);
                 
                 String forInstance =  ((Resource)bNode.getProperty(UMM.forInstanceProperty).getObject().as(Resource.class)).getURI();
                 String UT = ((Resource)bNode.getProperty(UMM.forUserTypeProperty).getObject().as(Resource.class)).getURI();
                 String rep = bNode.getProperty(UMM.RepetitionsValueProperty).getLiteral().getString();
                 String inter = bNode.getProperty(UMM.InterestValueProperty).getLiteral().getString();

                 IPInterestKey IPIKey = new IPInterestKey(currentPropURI, forInstance);
                 //IPIKey.print();
                         
                 if(IPropertyRepetitions.containsKey(IPIKey))
                 {
                     Parameters reps = IPropertyRepetitions.get(IPIKey);
                     reps.add(new ParameterNode(UT,rep));
                     IPropertyRepetitions.put(IPIKey, reps);
                     
                     //logger.debug(currentPropURI); 
                     //inters.print();
                 }
                 else
                 {
                     //logger.debug("boooooo");
                     Parameters reps = new Repetitions();
                     reps.add(new ParameterNode(UT,rep));
                     IPropertyRepetitions.put(IPIKey, reps);
                     
                     //logger.debug(currentPropURI); 
                     //inters.print();                     
                 }     
                 
                 if(IPropertyInterests.containsKey(IPIKey))
                 {
                     Parameters interests = IPropertyInterests.get(IPIKey);
                     interests.add(new ParameterNode(UT,inter));
                     IPropertyInterests.put(IPIKey, interests);
                     
                     //logger.debug(currentPropURI); 
                     //inters.print();
                 }
                 else
                 {
                     //logger.debug("boooooo");
                     Parameters interests = new Interests();
                     interests.add(new ParameterNode(UT,inter));
                     IPropertyInterests.put(IPIKey, interests);
                     
                     //logger.debug(currentPropURI); 
                     //inters.print();                     
                 }            
                 
                 Instances2Properties_forInter.AddMapping(forInstance, currentPropURI);
                 Properties2Instances_forInter.AddMapping(currentPropURI, forInstance);
                 
                 Instances2Properties_forRep.AddMapping(forInstance, currentPropURI);
                 Properties2Instances_forRep.AddMapping(currentPropURI, forInstance);                 
             }
                           
          }//for each property
                           
          // !!!! load Approp
         
          ExtendedIterator Approps = UMM.get(UMM.AppropriatenessProperty);          
          
          while(Approps != null && Approps.hasNext())
          {
             Resource microplan = (Resource)Approps.next();             
             
             String micropanURI = microplan.getURI();
             Vector<AppropriatenessNode> AppropList = new Vector<AppropriatenessNode>();
                         
             StmtIterator iter =  m.listStatements( microplan , UMM.AppropProperty, (RDFNode)null);
             AppropriatenessNode AN = null;
             
             Approps tempApprops = new Approps();
             while(iter.hasNext())
             {
                 Statement t = iter.nextStatement();
                 Resource bNode = (Resource)t.getObject().as(Resource.class);
                                  
                 String UT = ((Resource)bNode.getProperty(UMM.forUserTypeProperty).getObject().as(Resource.class)).getURI();
                 String appr = bNode.getProperty(UMM.AppropValueProperty).getLiteral().getString();
                 AN = new AppropriatenessNode(appr, UT);
                 
                 //logger.debug(micropanURI);
                 //AN.print();
                 
                 tempApprops.add(AN);
             }
             
             MicroplansAppropriateness.put(micropanURI, tempApprops);  
                        
          }
          // !!!! load Approp
          
         //load class interests repetitions
         ExtendedIterator ClassInterIter = UMM.get(UMM.ClassInterestsRepetitionsProperty);
         
         while(ClassInterIter!=null &&  ClassInterIter.hasNext())
         {
             Resource classRes = (Resource)ClassInterIter.next();
                    
             String classResURI = classRes.getURI();
             
             // load defaults
             StmtIterator iter =  m.listStatements( classRes , UMM.DInterestRepetitionsProperty, (RDFNode)null);
             inters = new Parameters();
             repetitions = new Parameters();
                     
             while(iter!=null && iter.hasNext())
             {                              
                 Statement t = iter.nextStatement();
                 Resource bNode = (Resource)t.getObject().as(Resource.class);
                                  
                 String UT = ((Resource)bNode.getProperty(UMM.forUserTypeProperty).getObject().as(Resource.class)).getURI();
                 String Interest = bNode.getProperty(UMM.InterestValueProperty).getLiteral().getString();
                 String rep = bNode.getProperty(UMM.RepetitionsValueProperty).getLiteral().getString();
                 
                 inters.add(new ParameterNode(UT,Interest ));  
                 repetitions.add(new ParameterNode(UT, rep));
             }
             
             DClsInterests.put(classResURI, inters);
             DClsRepetitions.put(classResURI, repetitions);
                     
             // load 
             iter =  m.listStatements( classRes , UMM.IInterestRepetitionsProperty, (RDFNode)null);
             
             while(iter!=null && iter.hasNext())
             {                              
                 Statement t = iter.nextStatement();
                 Resource bNode = (Resource)t.getObject().as(Resource.class);
                                  
                 String UT = ((Resource)bNode.getProperty(UMM.forUserTypeProperty).getObject().as(Resource.class)).getURI();
                 
                 String Interest = bNode.getProperty(UMM.InterestValueProperty).getLiteral().getString();
                 String rep = bNode.getProperty(UMM.RepetitionsValueProperty).getLiteral().getString();
                 
                 String forInstance = ((Resource)bNode.getProperty(UMM.forInstanceProperty).getObject().as(Resource.class)).getURI();
                 
                 ClassInstanceKey CIKey = new ClassInstanceKey(classResURI, forInstance);
                         
                 if(ClsInterests.containsKey(CIKey))
                 {
                     Parameters temp_inters = ClsInterests.get(CIKey);
                     temp_inters.add(new ParameterNode(UT,Interest));
                     ClsInterests.put(CIKey, temp_inters);
                 }
                 else
                 {
                     Parameters temp_inters = new Interests();
                     temp_inters.add(new ParameterNode(UT,Interest));
                     ClsInterests.put(CIKey, temp_inters);
                 }
                 
                 if(ClsRepetitions.containsKey(CIKey))
                 {
                     Parameters temp_reps = ClsRepetitions.get(CIKey);
                     temp_reps.add(new ParameterNode(UT,rep));
                     ClsRepetitions.put(CIKey, temp_reps);
                 }
                 else
                 {
                     Parameters temp_reps = new Parameters();
                     temp_reps.add(new ParameterNode(UT,rep));
                     ClsRepetitions.put(CIKey, temp_reps);
                 }                 
             }
             
         }
                                                              
    }//LoadUserModellingInfo
       
    /**********************************************************/
    
    // query user modelling info
    
    /**** Appropriateness ***/
    
    public int getAppropriateness(String MicroplanURI, String userType)
    {
        Approps AppropList = MicroplansAppropriateness.get(MicroplanURI);
        
        if(AppropList != null)
        {
            for(int i = 0; i < AppropList.size(); i++)
            {
                AppropriatenessNode AN = AppropList.get(i);
                if(AN.getUserType().compareTo(userType)==0)
                {
                    return Integer.parseInt(AN.geInterestValue());
                }
            }
        }
        else 
        {
            return 1;            
        }
        
        return 10000;
    }
    
    public void setAppropriateness(String MicroplanURI, String userType,String value)
    {
        logger.debug(MicroplanURI + " " + userType + " " + value);
        
        Approps AppropList = MicroplansAppropriateness.get(MicroplanURI);
        boolean found = false;
        
        if(AppropList != null)
        {
            for(int i = 0; i < AppropList.size(); i++)
            {
                AppropriatenessNode AN = AppropList.get(i);
                
                if(AN.getUserType().compareTo(userType)==0) // found ut
                {
                    found = true;
                    if(value.equals("-1"))
                        AppropList.remove(AN);
                    else
                        AN.setInterestValue(value);
                }
            }
            
            if(!found)
            AppropList.add(new AppropriatenessNode(value, userType));
            
            MicroplansAppropriateness.put(MicroplanURI,AppropList);
        }
        else
        {
            if(!value.equals("-1"))
            {
                AppropList = new Approps();
                AppropList.add(new AppropriatenessNode(value, userType));
                MicroplansAppropriateness.put(MicroplanURI,AppropList);
            }
        }
    }
    
    
    /**** Appropriateness ***/
    
    
    
    
    
    /**** Class Interest ***/
    
    // get interest instanceURI is-a ClassURI
    public int getCInterest(String ClassURI,String instanceURI,String UT)
    {
        if(ClassURI.equals("http://www.w3.org/2002/07/owl#Thing"))
        return 0;
        
        logger.debug("getCInterest" + ClassURI + " " + instanceURI);
        int retInterest = -1;
        
        retInterest = getClassInterest(ClassURI, instanceURI, UT); // interest gia ClassURI, instanceURI


        if(retInterest == -1)
        {
            retInterest = this.getDefaultClassInterest(ClassURI, UT); // interest gia ClassURI
        }
        
        if(retInterest == -1)
            retInterest = 1;
        
        return retInterest;        
        
    }//getCInterest
    
    // get Class Interest
    public int getClassInterest(String ClassURI,String instanceURI,String UT)
    {
        
        int retInterest = -1;
        ClassInstanceKey CIK = new ClassInstanceKey (ClassURI,instanceURI);
        
        if(ClsInterests.containsKey(CIK))
        {
            Parameters interests = ClsInterests.get(CIK);
            
            HashMap<String, ParameterNode> interestsHashMap = interests.getParameters();
                    
            if(interestsHashMap.containsKey(UT))
            {                
                retInterest = Integer.parseInt(interestsHashMap.get(UT).getParameter());
                return retInterest;
            }           
        }
        
        return retInterest;
    }
    
    // set Class Interest
    public void setClassInterest(String ClassURI,String instanceURI,String UT, String value)
    {
        logger.debug("setClassInterest" + " " + ClassURI  + " " + instanceURI);
        ClassInstanceKey CIK = new ClassInstanceKey (ClassURI,instanceURI);        
        update(ClsInterests, CIK, UT, value, isNullvalue(value), false, null, null, null, null);        
    }
     
    // get Default Class Interest
    public int getDefaultClassInterest(String ClassURI,String UT)
    {        
        int retInterest = -1;    
        
        if(DClsInterests.containsKey(ClassURI))
        {
            Parameters interests = DClsInterests.get(ClassURI);

            HashMap<String, ParameterNode> interestsHashMap = interests.getParameters();

            if(interestsHashMap.containsKey(UT))
            {                
                retInterest = Integer.parseInt(interestsHashMap.get(UT).getParameter());
                return retInterest;
            }
        }

        return retInterest;
    }
    
    // set Class Interest
    public void setDefaultClassInterest(String ClassURI,String UT, String value)
    {      
        logger.debug("setDefaultClassInterest" + " " + ClassURI );
        update(DClsInterests, ClassURI, UT, value, isNullvalue(value), false, null, null, null, null);            
    }
    
    /**** Class Interest ***/
    
    
    
    
        
    /**** Class Repetitions ***/
    public int getCRepetitions(String ClassURI,String instanceURI,String UT)
    {
        
        int retRepetitions = -1;
        retRepetitions = this.getClassRepetitions(ClassURI, instanceURI, UT);

        if(retRepetitions == -1)
        {
            this.getDefaultClassRepetitions(ClassURI, UT);
        }
        
        if(retRepetitions == -1)
            retRepetitions = 1;
        
        return retRepetitions;        
        
    }
    
    public int getClassRepetitions(String ClassURI,String instanceURI,String UT)
    {
        
        int retRepetitions = -1;
        ClassInstanceKey CIK = new ClassInstanceKey (ClassURI,instanceURI);
        
        if(ClsRepetitions.containsKey(CIK))
        {
            Parameters reps = ClsRepetitions.get(CIK);
            
            HashMap<String, ParameterNode> RepetitionsHashMap = reps.getParameters();

            if(RepetitionsHashMap.containsKey(UT))
            {                
                retRepetitions = Integer.parseInt(RepetitionsHashMap.get(UT).getParameter());
                return retRepetitions;
            }           
        }
        
        return retRepetitions;
    }
    
    public int getDefaultClassRepetitions(String ClassURI,String UT)
    {
        int retRepetitions = -1;
        
        if(DClsRepetitions.containsKey(ClassURI))
        {
            Parameters reps = DClsRepetitions.get(ClassURI);

            HashMap<String, ParameterNode> RepetitionsHashMap = reps.getParameters();

            if(RepetitionsHashMap.containsKey(UT))
            {                
                retRepetitions = Integer.parseInt(RepetitionsHashMap.get(UT).getParameter());
                return retRepetitions;
            }  
        }  
        
        return retRepetitions;
    }
    
    public void setClassRepetitions(String ClassURI,String instanceURI,String UT, String value)
    {
        
        ClassInstanceKey CIK = new ClassInstanceKey (ClassURI,instanceURI);
        update(ClsRepetitions, CIK, UT, value, isNullvalue(value), false, null, null, null, null);  
    }
    
    public void setDefaultClassRepetitions(String ClassURI,String UT, String value)
    {
        logger.debug("setDefaultClassRepetitions" + value);
        update(DClsRepetitions, ClassURI, UT, value, isNullvalue(value), false, null, null, null, null);         
    }
    
    /**** Class Repetitions ***/

    
    //----------------------------------------------------------------------------------
    //get default interest
    public int getDInterest(String PrpURI, String UT)
    {
        if(DPropertyInterests.containsKey(PrpURI))
        {
            Parameters interests = (Parameters)DPropertyInterests.get(PrpURI);
            HashMap<String, ParameterNode> interestsHashMap = interests.getParameters();

            if(interestsHashMap.containsKey(UT))
            {                
                return Integer.parseInt(interestsHashMap.get(UT).getParameter());
            }                                
        }    
        
        return -1;
    }
    
    //get default repetitions
     public int getDRepetitions(String PrpURI, String UT)
     {
        if(DPropertyRepetitions.containsKey(PrpURI))
        {
            Parameters reps = (Parameters)DPropertyRepetitions.get(PrpURI);

            HashMap<String, ParameterNode> RepetitionsHashMap = reps.getParameters();

            if(RepetitionsHashMap.containsKey(UT))
            {                
                return Integer.parseInt(RepetitionsHashMap.get(UT).getParameter());
            }                  
        }
        
        return -1;
     }
         
    // get class-property interest
    public int getCDPInterest(String PrpURI,String owlClassURI, String UT)
    {        
        CDPInterestKey CDPIKey = new CDPInterestKey(PrpURI,owlClassURI);

        if(CDPropertyInterests.containsKey(CDPIKey))
        {
            Parameters interests = (Parameters)CDPropertyInterests.get(CDPIKey);                                                  
            HashMap<String, ParameterNode> interestsHashMap = interests.getParameters();

            if(interestsHashMap.containsKey(UT))
            {                
                return Integer.parseInt(interestsHashMap.get(UT).getParameter());
            }
        }
        
        return -1;
    }     
    
    // get class-property reps
    public int getCDPRepetitions(String PrpURI,String owlClassURI, String UT)
    {
        CDPInterestKey CDPIKey = new CDPInterestKey(PrpURI,owlClassURI);

        if(CDPropertyRepetitions.containsKey(CDPIKey))
        {
            Parameters reps = (Parameters)CDPropertyRepetitions.get(CDPIKey);                 

            HashMap<String, ParameterNode> RepetitionsHashMap = reps.getParameters();

            if(RepetitionsHashMap.containsKey(UT))
            {                
                return  Integer.parseInt(RepetitionsHashMap.get(UT).getParameter());
            }  
        }
        return -1;
    }
    
    // get instance-property interest
    public int getIPInterest(String PrpURI,String instanceURI, String UT)
    {
        IPInterestKey IPIKey = new IPInterestKey(PrpURI, instanceURI);            

        if(IPropertyInterests.containsKey(IPIKey))
        {
            //logger.debug("ok IPropertyInterests");
            Parameters interests = (Parameters)IPropertyInterests.get(IPIKey);

            HashMap<String, ParameterNode> interestsHashMap = interests.getParameters();

            if(interestsHashMap.containsKey(UT))
            {                
                return Integer.parseInt(interestsHashMap.get(UT).getParameter());
            }

        }     
        
        return -1;
    }
    
    // get instance-property repetitions
    public int getIPRepetitions(String PrpURI,String instanceURI, String UT)
    {
        IPInterestKey IPIKey = new IPInterestKey(PrpURI, instanceURI);
        if(IPropertyRepetitions.containsKey(IPIKey))
        {

            Parameters reps = (Parameters)IPropertyRepetitions.get(IPIKey);

            HashMap<String, ParameterNode> RepetitionsHashMap = reps.getParameters();

            if(RepetitionsHashMap.containsKey(UT))
            {                
                return Integer.parseInt(RepetitionsHashMap.get(UT).getParameter());
            }       
        }
        
        return -1;
    }
    
    
    //----------------------------------------------------------------------------------
            
    // get interest
    public int getInterest(String PrpURI, String instanceURI, String UT)
    {
        int retInterest = -1;
        
        if(model.getResource(instanceURI).canAs(OntClass.class))
        { 
                retInterest = this.getCDPInterest(PrpURI, instanceURI, UT);
                
                if(retInterest == -1)
                {
                    retInterest = this.getDInterest(PrpURI, UT);
                }                
        }
        else
        {
                retInterest = this.getIPInterest(PrpURI, instanceURI, UT);
                        
                if(retInterest == -1)
                {
                    String owlClassURI = NLGEngine.getClassType(model, instanceURI);
                    retInterest = this.getCDPInterest(PrpURI, owlClassURI, UT);
                }
                
                if(retInterest == -1)
                {
                    retInterest = this.getDInterest(PrpURI, UT);
                }                
        }
        
        if(retInterest == -1)
        retInterest = 1; // change it to 1???????

        return retInterest;
    }

    // get repetitions
    public int getRepetitions(String PrpURI, String instanceURI, String UT)
    {
        
        int retRepetitions = -1;
           
        if(model.getResource(instanceURI).canAs(OntClass.class))
        {
            retRepetitions = this.getCDPRepetitions(PrpURI,instanceURI, UT);

            if(retRepetitions == -1)
            {
                retRepetitions = this.getDRepetitions(PrpURI, UT);
            }            
        }
        else
        {
            retRepetitions = this.getIPRepetitions(PrpURI,instanceURI, UT);
                    
            if(retRepetitions == -1)
            {            
                String owlClassURI = NLGEngine.getClassType(model, instanceURI);
                retRepetitions = this.getCDPRepetitions(PrpURI,owlClassURI, UT);
            }

            if(retRepetitions == -1)
            {
                retRepetitions = this.getDRepetitions(PrpURI, UT);
            }     
        }
        
        if(retRepetitions == -1)
            retRepetitions = 1; // !!!!!!!!!!!!!!!!!
        
        return retRepetitions;
    }
            
    //  get a list of all user types
 
    public  Iterator<String> getUserTypes()
    {
        return this.UserTypes.keySet().iterator();
    }
    
    
   // check if the specified user type exists
  
    public boolean checkUserTypeExists(String UserType)
    {
        return this.UserTypes.containsKey(UserType);
    }
    
    // get user type parameters for the specified userType       
    public UserTypeParameters getParametersForUserType(String userType)
    {        
        if(UserTypes.containsKey(userType))
        {
            return UserTypes.get(userType);
        }
        else
        {
            return null;
        }
    }
    
    public void setParametersForUserType(String userType, UserTypeParameters UTP)
    {
        UserTypes.put(userType, UTP);
    }
    
    public Iterator<String> getMicroplansIDs()
    {
        return MicroplansAppropriateness.keySet().iterator();
    }
    
    //------------------------------------------------------------------------------------------------
    public  void test()
    {
        String m = "http://www.aueb.gr/users/ion/mpiro.owl#current-location-templ1-en";
         
        String ret = 
            getAppropriateness(m,"http://www.owlnl.com/NLG/UserModelling#Child") + " , "+
            getAppropriateness(m, "http://www.owlnl.com/NLG/UserModelling#Adult") + " , " +
            getAppropriateness(m, "http://www.owlnl.com/NLG/UserModelling#Group");
                                                             
        String prp = "http://www.aueb.gr/users/ion/mpiro.owl#creation-period";
        String prp2 = "http://www.aueb.gr/users/ion/mpiro.owl#made-of";
          
        String ResourceURI = "http://www.aueb.gr/users/ion/mpiro.owl#exhibit1";
        String ResourceURI2 = "http://www.aueb.gr/users/ion/mpiro.owl#exhibit16";
        
              
        String ret2 = 
             getInterest(prp2 , ResourceURI2 ,"http://www.owlnl.com/NLG/UserModelling#Child" ) + " , " +
             getInterest(prp2 , ResourceURI2 ,"http://www.owlnl.com/NLG/UserModelling#Adult" ) + " , " +
             getInterest(prp2 , ResourceURI2 ,"http://www.owlnl.com/NLG/UserModelling#Group" );
        
        String ret3 = 
            getRepetitions(prp , ResourceURI ,"http://www.owlnl.com/NLG/UserModelling#Child" ) + " , " +
            getRepetitions(prp , ResourceURI ,"http://www.owlnl.com/NLG/UserModelling#Adult" ) + " , " +
            getRepetitions(prp , ResourceURI ,"http://www.owlnl.com/NLG/UserModelling#Group" );

        String ret4 = 
            getCRepetitions("http://www.aueb.gr/users/ion/mpiro.owl#amphora" , ResourceURI ,"http://www.owlnl.com/NLG/UserModelling#Child" ) + " , " +
            getCRepetitions("http://www.aueb.gr/users/ion/mpiro.owl#amphora" , ResourceURI ,"http://www.owlnl.com/NLG/UserModelling#Adult" ) + " , " +
            getCRepetitions("http://www.aueb.gr/users/ion/mpiro.owl#amphora" , ResourceURI ,"http://www.owlnl.com/NLG/UserModelling#Group" );

        String ret5 = 
            getCInterest("http://www.aueb.gr/users/ion/mpiro.owl#amphora" , ResourceURI ,"http://www.owlnl.com/NLG/UserModelling#Child" ) + " , " +
            getCInterest("http://www.aueb.gr/users/ion/mpiro.owl#amphora" , ResourceURI ,"http://www.owlnl.com/NLG/UserModelling#Adult" ) + " , " +
            getCInterest("http://www.aueb.gr/users/ion/mpiro.owl#amphora" , ResourceURI ,"http://www.owlnl.com/NLG/UserModelling#Group" );
        
            logger.debug(ret + "\n" + ret2 + "\n" + ret3 + "\n" + ret4 + "\n" + ret5); 
            logger.debug("===/Test user modelling===");                                      
    }   

    public void writeUM(OntModel m, String path)
    {//write UM
        
        logger.debug("Saving UM parameters....");
        XMLDocWriter writer = new XMLDocWriter();

        XmlDocumentCreator docCreator = new XmlDocumentCreator();

        Document UMDoc = docCreator.getNewDocument();
        Element rootRDF = UMDoc.createElement("rdf:RDF");

        rootRDF.setAttribute( "xmlns:" + XmlMsgs.prefix , XmlMsgs.owlnlNS);
        rootRDF.setAttribute( "xmlns:" + "rdf" , "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
        rootRDF.setAttribute( "xmlns" , NLGUserModellingNS);
        rootRDF.setAttribute( "xml:base" , NLGUserModellingNS.substring(0,NLGUserModellingNS.length()-1));
        
        UMDoc.appendChild(rootRDF);
        
        //write user types
        Iterator<String> utIter = UserTypes.keySet().iterator();
        
        Element UserModellingResNode = UMDoc.createElement("owlnl:" + UserModellingManager.UserModellingRes);
         
        Element UserTypesNode = UMDoc.createElement("owlnl:" + UserModellingManager.UserTypesPrp);
        UserTypesNode.setAttribute("rdf:parseType", "Collection");
                
        while(utIter != null && utIter.hasNext())
        {
            String ut = utIter.next();
            UserTypeParameters UTP = UserTypes.get(ut);

            Element FactsPerPageNode = UMDoc.createElement("owlnl:" + UserModellingManager.FactsPerPagePrp);
            Element MaxFactsPerSentenceNode = UMDoc.createElement("owlnl:" + UserModellingManager.MaxFactsPerSentencePrp);
            Element SynthesizerVoiceNode = UMDoc.createElement("owlnl:" + UserModellingManager.SynthesizerVoicePrp);
            Element LangNode = UMDoc.createElement("owlnl:" + UserModellingManager.LangPrp);

            FactsPerPageNode.setTextContent(UTP.getFactsPerPage() + "");
            MaxFactsPerSentenceNode.setTextContent(UTP.getMaxFactsPerSentence() + "");
            SynthesizerVoiceNode.setTextContent(UTP.getSynthesizerVoice());
            LangNode.setTextContent(UTP.getLang());
                    
            Element UserTypeNode = UMDoc.createElement("owlnl:" + UserModellingManager.UserTypeRes);
            UserTypeNode.setAttribute("rdf:ID", ut.substring(ut.indexOf("#")+1));
                    
            UserTypeNode.appendChild(FactsPerPageNode);                
            UserTypeNode.appendChild(MaxFactsPerSentenceNode);
            UserTypeNode.appendChild(SynthesizerVoiceNode);
            UserTypeNode.appendChild(LangNode);
                    
            UserTypesNode.appendChild(UserTypeNode);
        }

        UserModellingResNode.appendChild(UserTypesNode);
        
        //write appropriateness
        Iterator<String> microplansIter = MicroplansAppropriateness.keySet().iterator();
        Element AppropNode = UMDoc.createElement("owlnl:" + UserModellingManager.AppropriatenessPrp);

        while(microplansIter!= null && microplansIter.hasNext())
        {
            String micropURI = microplansIter.next();
            Element MicropAppropNode = UMDoc.createElement("owlnl:" + "MicroplanApprop");

            Approps apprps = MicroplansAppropriateness.get(micropURI);
            for(int j = 1; j < apprps.size(); j++)
            {
                AppropriatenessNode AN = apprps.get(j);
                Element Approp = UMDoc.createElement("owlnl:" + UserModellingManager.AppropPrp);
                
                Element forUT = UMDoc.createElement("owlnl:" + UserModellingManager.forUserTypePrp);
                forUT.setTextContent(AN.getUserType());
                Element AppVal = UMDoc.createElement("owlnl:" + UserModellingManager.AppropValuePrp);
                AppVal.setTextContent(AN.geInterestValue());    
                
                Approp.appendChild(forUT);
                Approp.appendChild(AppVal);    
                
                MicropAppropNode.appendChild(Approp);    
            }
        }
        
        //write interests repetitions
        
        Element PropertiesInterestsRepetitionsNode = UMDoc.createElement("owlnl:" + UserModellingManager.PropertiesInterestsRepetitionsPrp);
        PropertiesInterestsRepetitionsNode.setAttribute("rdf:parseType" , "Collection");
                        
        ExtendedIterator propertyIter = model.listObjectProperties();        
        propertyIter = propertyIter.andThen(model.listDatatypeProperties());
                         
        while(propertyIter!=null && propertyIter.hasNext())
        {// for each property
            
            boolean d_found = false;
            boolean c_found = false;
            boolean i_found = false;
            
            Element PropertyNode = UMDoc.createElement("owlnl:" + UserModellingManager.PropertyRes);
            String property = propertyIter.next().toString();
            PropertyNode.setAttribute("rdf:about", property);
            
            logger.debug("!!!!!!property:" + property);
            
            //write default interests-repetitions
            Iterator<String> utsIter = getUserTypes();
                       
             
            while(utsIter!= null && utsIter.hasNext())
            {// for each user type
                
                String ut = utsIter.next();
                Element DPInterestRepetitionsNode = UMDoc.createElement("owlnl:" + UserModellingManager.DPInterestRepetitionsPrp);
                DPInterestRepetitionsNode.setAttribute("rdf:parseType", "Resource");
                Element forUserTypeNode = UMDoc.createElement("owlnl:" + UserModellingManager.forUserTypePrp);
                
                forUserTypeNode.setAttribute("rdf:resource", ut);
                DPInterestRepetitionsNode.appendChild(forUserTypeNode);
                    
                Element InterestNode = UMDoc.createElement("owlnl:" + UserModellingManager.InterestValuePrp);
                Element RepetitionsNode = UMDoc.createElement("owlnl:" + UserModellingManager.RepetitionsValuePrp);
                        
                                          
                boolean found = false;
                if(DPropertyInterests.containsKey(property))
                {
                    HashMap<String, ParameterNode> interests = DPropertyInterests.get(property).getParameters();
                    if(interests.containsKey(ut))
                    {
                        String val = interests.get(ut).getParameter();                                        
                        InterestNode.setTextContent(val);
                        DPInterestRepetitionsNode.appendChild(InterestNode);
                        found = true;
                        d_found = true;
                    }
                }                
                
                
                if(DPropertyRepetitions.containsKey(property))
                {
                    HashMap<String, ParameterNode> reps = DPropertyRepetitions.get(property).getParameters();
                    if(reps.containsKey(ut))
                    {
                        String val = reps.get(ut).getParameter();
                        RepetitionsNode.setTextContent(val);
                        DPInterestRepetitionsNode.appendChild(RepetitionsNode);   
                        found = true;
                        d_found = true;
                    }
                }             
                                
                if(found)
                PropertyNode.appendChild(DPInterestRepetitionsNode);
            }// for each user type
                    
            //write class default interests-repetitions            
            
            
            Iterator<String> clsesIter = interesect(Properties2Cls_forInter.getValues(property), Properties2Cls_forRep.getValues(property));
            while(clsesIter!= null && clsesIter.hasNext())
            {
                String owclassURI = clsesIter.next().toString();
                utsIter = getUserTypes();
            
                while(utsIter!= null && utsIter.hasNext())
                {                  
                    String ut = utsIter.next(); 
                    CDPInterestKey key = new CDPInterestKey(property, owclassURI);
                    
                    Element CDPInterestRepetitionsNode = UMDoc.createElement("owlnl:" + UserModellingManager.CDPInterestRepetitionsPrp);
                    CDPInterestRepetitionsNode.setAttribute("rdf:parseType", "Resource");
                            
                    Element forUserTypeNode = UMDoc.createElement("owlnl:" + UserModellingManager.forUserTypePrp);
                    Element forOwlClassNode = UMDoc.createElement("owlnl:" + UserModellingManager.forOwlClassPrp);                    
                    
                    forUserTypeNode.setAttribute("rdf:resource", ut);
                    CDPInterestRepetitionsNode.appendChild(forUserTypeNode);
                                
                    forOwlClassNode.setAttribute("rdf:resource", owclassURI);
                    CDPInterestRepetitionsNode.appendChild(forOwlClassNode);
                    
                    Element InterestNode = UMDoc.createElement("owlnl:" + UserModellingManager.InterestValuePrp);
                    Element RepetitionsNode = UMDoc.createElement("owlnl:" + UserModellingManager.RepetitionsValuePrp);
                
                    boolean found = false;
                    if(CDPropertyInterests.containsKey(key))
                    {
                        HashMap<String, ParameterNode> interests = CDPropertyInterests.get(key).getParameters();
                        if(interests.containsKey(ut))
                        {
                            String val = interests.get(ut).getParameter();   
                            InterestNode.setTextContent(val);
                            CDPInterestRepetitionsNode.appendChild(InterestNode);  
                            found = true;
                            c_found = true;
                        }
                    }


                    if(CDPropertyRepetitions.containsKey(key))
                    {
                        HashMap<String, ParameterNode> reps = CDPropertyRepetitions.get(key).getParameters();
                        if(reps.containsKey(ut))
                        {
                            String val = reps.get(ut).getParameter();   
                            RepetitionsNode.setTextContent(val);
                            CDPInterestRepetitionsNode.appendChild(RepetitionsNode);     
                            found = true;
                            c_found = true;
                        }
                    }

                    if(found)
                    PropertyNode.appendChild(CDPInterestRepetitionsNode);
                }
                
                
            }//while
            
            //write instance default interests-repetitions            
            
            Iterator<String> InstanceIter = interesect(this.Properties2Instances_forInter.getValues(property), Properties2Instances_forRep.getValues(property));
                        
            while(InstanceIter!= null && InstanceIter.hasNext())
            {
                String InstanceURI = InstanceIter.next().toString();
                utsIter = getUserTypes();
            
                while(utsIter!=null && utsIter.hasNext())
                {                  
                    String ut = utsIter.next(); 
                    IPInterestKey key = new IPInterestKey(property, InstanceURI);
                    
                    Element IPInterestRepetitionsNode = UMDoc.createElement("owlnl:" + UserModellingManager.IPInterestRepetitionsPrp);
                    IPInterestRepetitionsNode.setAttribute("rdf:parseType", "Resource");
                            
                    Element forUserTypeNode = UMDoc.createElement("owlnl:" + UserModellingManager.forUserTypePrp);
                    Element forInstanceNode = UMDoc.createElement("owlnl:" + UserModellingManager.forInstancePrp);
                    
                    forUserTypeNode.setAttribute("rdf:resource", ut);
                    IPInterestRepetitionsNode.appendChild(forUserTypeNode);
                    
                    forInstanceNode.setAttribute("rdf:resource", InstanceURI);
                    IPInterestRepetitionsNode.appendChild(forInstanceNode);
                    
                    Element InterestNode = UMDoc.createElement("owlnl:" + UserModellingManager.InterestValuePrp);
                    Element RepetitionsNode = UMDoc.createElement("owlnl:" + UserModellingManager.RepetitionsValuePrp);
                    
                    boolean found  = false;
                    if(IPropertyInterests.containsKey(key))
                    {
                        HashMap<String, ParameterNode> interests = IPropertyInterests.get(key).getParameters();
                        if(interests.containsKey(ut))
                        {
                            String val = interests.get(ut).getParameter();                                        
                            InterestNode.setTextContent(val);
                            IPInterestRepetitionsNode.appendChild(InterestNode);  
                            found = true;
                            i_found = true;
                        }
                    }

                    if(IPropertyRepetitions.containsKey(key))
                    {
                        HashMap<String, ParameterNode> reps = IPropertyRepetitions.get(key).getParameters();
                        if(reps.containsKey(ut))
                        {
                            String val = reps.get(ut).getParameter();                                        
                            RepetitionsNode.setTextContent(val);
                            IPInterestRepetitionsNode.appendChild(RepetitionsNode); 
                            found = true;
                            i_found = true;
                        }
                    }    
                    
                    if(found )
                    PropertyNode.appendChild(IPInterestRepetitionsNode);
                }
            } 
            
            if(d_found || c_found || i_found)
            PropertiesInterestsRepetitionsNode.appendChild(PropertyNode);
        }// for each property
        
        UserModellingResNode.appendChild(PropertiesInterestsRepetitionsNode);
        
        // ****** WRITE  APPROPRIATENESS  *********/
        
        Iterator<String> iter  = MicroplansAppropriateness.keySet().iterator();
        
        Element AppropriatenessPrpNode = UMDoc.createElement("owlnl:" + UserModellingManager.AppropriatenessPrp);
        AppropriatenessPrpNode.setAttribute("rdf:parseType", "Collection");
        
        while(iter!= null && iter.hasNext())
        {
            Element MicroplanAppropNode = UMDoc.createElement("owlnl:" + "MicroplanApprop");
            String microplanURi = iter.next();
            MicroplanAppropNode.setAttribute("rdf:about", microplanURi);
            
            
            Approps approps = MicroplansAppropriateness.get(microplanURi);
                    
            for(int i = 0; i < approps.size(); i++)
            {
                String ut = approps.get(i).getUserType();
                String inter = approps.get(i).geInterestValue();
                
                Element AppropPrpNode = UMDoc.createElement("owlnl:" + UserModellingManager.AppropPrp);
                
                Element forUserTypePrpNode = UMDoc.createElement("owlnl:" + UserModellingManager.forUserTypePrp);
                forUserTypePrpNode.setAttribute("rdf:resource", ut);
                Element AppropValuePrpNode = UMDoc.createElement("owlnl:" + UserModellingManager.AppropValuePrp);
                AppropValuePrpNode.setTextContent(inter);
                
                AppropPrpNode.appendChild(forUserTypePrpNode);
                AppropPrpNode.appendChild(AppropValuePrpNode);
                
                AppropPrpNode.setAttribute("rdf:parseType", "Resource");
                MicroplanAppropNode.appendChild(AppropPrpNode);
                
            }
            
            AppropriatenessPrpNode.appendChild(MicroplanAppropNode);
            
        }
              
        UserModellingResNode.appendChild(AppropriatenessPrpNode);
        // ****** WRITE  CLASS Interests And Repetitions *********/
        
        Element ClassInterestsRepetitionsPrpNode = UMDoc.createElement("owlnl:" + UserModellingManager.ClassInterestsRepetitionsPrp);                             
        ClassInterestsRepetitionsPrpNode.setAttribute("rdf:parseType", "Collection");
                
        ExtendedIterator NamedClassesIter = model.listNamedClasses();
        
        
        boolean added_something1 = false;
        boolean added_something2 = false;
        boolean added_something3 = false;
        
        while(NamedClassesIter!=null && NamedClassesIter.hasNext())
        {// for each class
            String ClassURI = NamedClassesIter.next().toString();
            OntClass c = model.getOntClass(ClassURI);
            
            Element owlClassResNode = UMDoc.createElement("owlnl:" + LexiconManager.owlClassRes);                             
            owlClassResNode.setAttribute("rdf:about", ClassURI);
                    
            added_something1 = addClassInterestRepetitions (UMDoc, DClsInterests, DClsRepetitions, ClassURI, UserModellingManager.DInterestRepetitionsPrp, owlClassResNode );                    
            
                        
            ExtendedIterator SubClassesIter = c.listSubClasses();
            
            while(SubClassesIter!= null && SubClassesIter.hasNext())
            {
                String SubClassURI = SubClassesIter.next().toString();
                OntClass SubClass = model.getOntClass(SubClassURI);
                
                if(!SubClass.isAnon())
                {
                    
                        ClassInstanceKey CIK = new ClassInstanceKey(ClassURI, SubClassURI);
                                        
                        added_something2 = addClassInterestRepetitions (UMDoc, this.ClsInterests, this.ClsRepetitions, CIK, 
                                                    UserModellingManager.IInterestRepetitionsPrp, owlClassResNode );                    
                }// !isAnon
            }
            
            
            StmtIterator stmtIter = model.listStatements(null, RDF.type , c);
            
            while(stmtIter != null && stmtIter.hasNext())
            {
                    String IndividualURI = stmtIter.nextStatement().getSubject().getURI();
                                
                    ClassInstanceKey CIK = new ClassInstanceKey(ClassURI, IndividualURI);

                    added_something3 = addClassInterestRepetitions (UMDoc, ClsInterests, ClsRepetitions, CIK, 
                                                UserModellingManager.IInterestRepetitionsPrp, owlClassResNode );
            }
            
            if(added_something3 || added_something2 || added_something1)
            {
                ClassInterestsRepetitionsPrpNode.appendChild(owlClassResNode);
            }
        }// for each class
         
        UserModellingResNode.appendChild(ClassInterestsRepetitionsPrpNode);    
        
        rootRDF.appendChild(UserModellingResNode);
        writer.saveDocToFile(UMDoc,path);
        
        logger.debug("Saved UM parameters....");
    }//write UM
    
        
    private boolean addClassInterestRepetitions (Document UMDoc, Hashtable  AA, Hashtable BB, Object key, String RootTag,Element owlClassResNode )
    {
    
        Hashtable<String, Parameters> A = (Hashtable<String, Parameters>) AA ;
        Hashtable<String, Parameters> B = (Hashtable<String, Parameters>) BB ;

        boolean added_something = false;

        if(A.containsKey(key) || B.containsKey(key))
        {

            Iterator<String> utsIter = getUserTypes();

            while(utsIter.hasNext())
        {// for each user type

                String ut = utsIter.next();
                Element RootTagNode = UMDoc.createElement("owlnl:" + RootTag);
                RootTagNode.setAttribute("rdf:parseType", "Resource");
                        
                boolean visited  =false;

                if(A.containsKey(key))
                {
                    HashMap<String, ParameterNode> interests = A.get(key).getParameters();

                    if(interests.containsKey(ut))
                    {
                        visited  = true;
                        added_something = true;
                        Element InterestValuePrpNode = UMDoc.createElement("owlnl:" + UserModellingManager.InterestValuePrp);
                        InterestValuePrpNode.setTextContent(interests.get(ut).getParameter());
                        RootTagNode.appendChild(InterestValuePrpNode);                        
                                                
                    }
                }

                if(B.containsKey(key))
                {

                    HashMap<String, ParameterNode> repetitions = B.get(key).getParameters();

                    if(repetitions.containsKey(ut))
                    {
                        visited  = true;
                        added_something = true;

                        Element RepetitionsValuePrpNode = UMDoc.createElement("owlnl:" + UserModellingManager.RepetitionsValuePrp);
                        RepetitionsValuePrpNode.setTextContent(repetitions.get(ut).getParameter());
                        RootTagNode.appendChild(RepetitionsValuePrpNode);
                        
                    
                    }

                }

                if(visited)
                {

                    Element forUT = UMDoc.createElement("owlnl:" + UserModellingManager.forUserTypePrp);
                    forUT.setAttribute("rdf:resource", ut);
                    RootTagNode.appendChild(forUT);
                   
                    
                    if(RootTag.compareTo(UserModellingManager.IInterestRepetitionsPrp)==0)
                    {
                        Element forInstancePrpNode = UMDoc.createElement("owlnl:" + UserModellingManager.forInstancePrp);
                        ClassInstanceKey CIK = (ClassInstanceKey)key;                            
                        forInstancePrpNode.setAttribute("rdf:resource", CIK.forInstance);
                        
                        RootTagNode.appendChild(forInstancePrpNode);
                    }                        
                    
                    owlClassResNode.appendChild(RootTagNode);
                }
            }//// for each user type
        }//if

        return added_something;
    }
    
    private Iterator<String> interesect(Iterator<String> iter1, Iterator<String> iter2)
    {
            HashSet<String> clses = new HashSet<String>();
                        
            while(iter1 !=null && iter1.hasNext() )
            clses.add(iter1.next());
                    
            while(iter2 !=null && iter2.hasNext() )
            clses.add(iter2.next());
            
            return clses.iterator();
                    
    }
    
            
    public static void main(String args[])
    {
        logger.debug("===Test user modelling===");
        UserModellingQueryManager UMQM = new UserModellingQueryManager(null); 
        UMQM.LoadUserModellingInfo("C:\\NaturalOWL\\NLFiles-MPIRO\\", "UserModelling.rdf");
        
        String queryString = "SELECT ?x WHERE {<http://www.aueb.gr/users/ion/owlnl/UserModelling#Child> <http://www.aueb.gr/users/ion/owlnl#SynthesizerVoice> ?x}";
        Query query = QueryFactory.create(queryString);              
        QueryExecution qexec = QueryExecutionFactory.create(query, UMQM.UMM.model) ;

        logger.info("executing SPARQL query...");

        ResultSet results = qexec.execSelect();

        for ( ; results.hasNext() ; )
        {
            QuerySolution soln = results.nextSolution() ;

            logger.info(soln.toString());
        }        

        
            
            
        //UMQM.writeUM(null, "C:\\NLG_Project\\NLFiles-MPIRO\\moufaUserModelling.rdf");
                
        /*
        UserModellingQueryManager UMQM = new UserModellingQueryManager(); 
        UMQM.LoadUserModellingInfo("C:\\Documents and Settings\\galanisd\\Επιφάνεια εργασίας\\NLFiles-MPIRO\\", "UserModellingCopy.rdf");
        
        String m = "http://www.aueb.gr/users/ion/mpiro.owl#current-location-templ1-en";
         
        String ret = 
            UMQM.getAppropriateness(m,"http://www.owlnl.com/NLG/UserModelling#Child") + " , "+
            UMQM.getAppropriateness(m, "http://www.owlnl.com/NLG/UserModelling#Adult") + " , " +
            UMQM.getAppropriateness(m, "http://www.owlnl.com/NLG/UserModelling#Group");
                                                             
        String prp = "http://www.aueb.gr/users/ion/mpiro.owl#creation-period";
        String prp2 = "http://www.aueb.gr/users/ion/mpiro.owl#made-of";
          
        String ResourceURI = "http://www.aueb.gr/users/ion/mpiro.owl#exhibit1";
        String ResourceURI2 = "http://www.aueb.gr/users/ion/mpiro.owl#exhibit16";
        
              
        String ret2 = 
             UMQM.getInterest(prp2 , ResourceURI2 ,"http://www.owlnl.com/NLG/UserModelling#Child" ) + " , " +
             UMQM.getInterest(prp2 , ResourceURI2 ,"http://www.owlnl.com/NLG/UserModelling#Adult" ) + " , " +
             UMQM.getInterest(prp2 , ResourceURI2 ,"http://www.owlnl.com/NLG/UserModelling#Group" );
        
        String ret3 = 
            UMQM.getRepetitions(prp , ResourceURI ,"http://www.owlnl.com/NLG/UserModelling#Child" ) + " , " +
            UMQM.getRepetitions(prp , ResourceURI ,"http://www.owlnl.com/NLG/UserModelling#Adult" ) + " , " +
            UMQM.getRepetitions(prp , ResourceURI ,"http://www.owlnl.com/NLG/UserModelling#Group" );

        String ret4 = 
            UMQM.getCRepetitions("http://www.aueb.gr/users/ion/mpiro.owl#amphora" , ResourceURI ,"http://www.owlnl.com/NLG/UserModelling#Child" ) + " , " +
            UMQM.getCRepetitions("http://www.aueb.gr/users/ion/mpiro.owl#amphora" , ResourceURI ,"http://www.owlnl.com/NLG/UserModelling#Adult" ) + " , " +
            UMQM.getCRepetitions("http://www.aueb.gr/users/ion/mpiro.owl#amphora" , ResourceURI ,"http://www.owlnl.com/NLG/UserModelling#Group" );

        String ret5 = 
            UMQM.getCInterest("http://www.aueb.gr/users/ion/mpiro.owl#amphora" , ResourceURI ,"http://www.owlnl.com/NLG/UserModelling#Child" ) + " , " +
            UMQM.getCInterest("http://www.aueb.gr/users/ion/mpiro.owl#amphora" , ResourceURI ,"http://www.owlnl.com/NLG/UserModelling#Adult" ) + " , " +
            UMQM.getCInterest("http://www.aueb.gr/users/ion/mpiro.owl#amphora" , ResourceURI ,"http://www.owlnl.com/NLG/UserModelling#Group" );
        
            logger.debug(ret + "\n" + ret2 + "\n" + ret3 + "\n" + ret4 + "\n" + ret5); 
            logger.debug("===/Test user modelling===");    
         */        
    }
}//class UserModellingQueryManager