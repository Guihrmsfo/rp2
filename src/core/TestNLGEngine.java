/*
    NaturalOWL. 
    Copyright (C) 2008  Dimitrios Galanis and Giorgos Karakatsiotis
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


package core;

import gr.aueb.cs.nlg.Languages.*;
import gr.aueb.cs.nlg.NLGEngine.*;

public class TestNLGEngine
{
    
    public static void main(String args[])
    {
        try
        {
        	
            String owlPath = "C:\\Users\\Guilherme\\Downloads\\OWL\\NaturalOWL\\NLFiles-MPIRO\\mpiro.owl";
            String NLResourcePath = "C:\\Users\\Guilherme\\Downloads\\OWL\\NaturalOWL\\NLFiles-MPIRO\\";
            
            boolean useEmulator = true;   
            
            boolean Load_PServer_Databases = false;        
            
            String Navigation_Server_IP = "";
            int Navigation_Server_port = -1;
            
            String PServer_Database_Username = "";
            String PServer_Database_password = "";
            
            String PServer_IP = "";
            int Pserver_port = -1;
                    
            NLGEngine myEngine =  new NLGEngine(owlPath,
                                                NLResourcePath,
                                                Languages.ENGLISH, // Set language
                                                useEmulator,   // Use NaturalOWL’s emulator of the Pers. Server. A false value would signal that the real Pers. server is to be used
                                                Load_PServer_Databases,  // load the databases of PServer 				 		            								
                                                null,   // Objects representing the lexicon, user modelling, microplans, and 
                                                null,   // ontology. If already available, they 
                                                null,   // may be passed to the engine; 
                                                null,   // otherwise null values are passed.
                                                Navigation_Server_IP,     // navigation server IP
                                                Navigation_Server_port,     // navigation server port (-1 means to use the default port 53000)  
                                                PServer_Database_Username,     // PServer's database username
                                                PServer_Database_password,     // PServer's database password
                                                PServer_IP,     // PServer's IP
                                                Pserver_port);    // PServer's port
            
            // initialize PServer
            myEngine.initPServer();
                                 
            // initialize the statistical tree used
            // in the generation of comparisons
            myEngine.initStatisticalTree();
            
            
            String UT = "http://www.aueb.gr/users/ion/owlnl/UserModelling#Adult";
            String objectURI = "http://www.aueb.gr/users/ion/mpiro.owl#apollo-info";
            String userID = "123";
            boolean GenerateComparisons = false;
            int depth = 1;
            String robotPersonality = "";
            
            
            // create a new user in PServer
            myEngine.getUMVisit().newUser(userID, UT);
            boolean userExists = myEngine.getUMVisit().checkUserExists(userID);
            
            // generate a new text ...
            String result [] = myEngine.GenerateDescription(0, // 0 means we are describing an instance; 1 is for classes. 
                                                            objectURI, // The URI of the instance or class to describe.
                                                            UT,     // String specifying the user type.
                                                            userID, // String specifying the userID of the user.
                                                            depth,  // The depth in the RDF graph of the ontology we 
                                                                    // are allowed to go in content selection when 
                                                                    // describing instances. A depth of 1 will produce 
                                                                    // a text conveying only properties of the instance // being described. Larger depth values will 
                                                                    // produce texts conveying also properties of 
                                                                    // other related instances (e.g., “This lekythos 
                                                                    // was created in the classical period. The 
                                                                    // classical period was…”).  
                                                            -1,  // Maximum number of facts per sentence (how long
                                                                 // an aggregated sentence can be); only used when 
                                                                 // userType is null. 
                                                            GenerateComparisons, // A Boolean specifying whether or not we 
                                                                                 // want comparisons
                                                            robotPersonality);   // this is usefull only the communicates with Pers. Server
                                                                                 // if not use  "" as default value

                                             
                System.out.println("---------------------------------------------------");
                // the result array consists of 3 strings
                // the first string contains the produced text along with
                // the outputs of the intermediate stages of the NLG engine
                //System.out.println ( result[0] );

                System.out.println("---------------------------------------------------");
                // the second string contains only the produced text 
                System.out.println ( result[1] );
                
                
                System.out.println("---------------------------------------------------");
                // also the engine produces semantic-linguistic annotations
                // of the produced text ...
                //System.out.println(result[2]);
            
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
    }
}
