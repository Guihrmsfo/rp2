package gr.demokritos.iit.PServer;

import java.net.*;
import java.sql.*;
import java.util.*;

/** 
* This interface specifying the API between the Natular-OWL (the Natural-Language-Machine of A.U.E.B.) and the Personalization-Server.
* The Personalization-Server is a system that parses and processes the contents of the UserModeling.rdf file  (which is a file that 
* the ELEON tool exports) and then stores the information into a DataBase. These files contain information which is about the user
* modeling. More specifically these files have the following information:
* 	
* 	-information about the User-Type/ Robot-type for each user-type(adult,chid,..), the max facts
* 	per sentence, the max fact per page and the type of synthesizer(male,female,child).
* 	
* 	-information about the Properties-Interest for each user-type, how interest every
* 	property in the ontology is.
* 	
* 	-information about the Properties-Repetitions for each user-type, how many times
* 	each property should the machine talk about.(e.g If for property "has-age" the 
* 	repetion-number is 2, the machine can use at most two times that property,
* 	in order to produce text)
* 	
* 	-information about the Appropriateness of Microplans for each user-type, how
* 	appropriate is each microplan.
* 	
* 	-information about the Class-Interest for each user-type, how interest is to
* 	mention in the produced text for the X instance, that is type-of class (e.g.
* 	for the instance "building_1", to mention that "...building1 is a building..."
* 	
* 	-information about the Class-Repetion for each user-type, how many times can mention
* 	in the produced text that Instance-X is type-of Class-Y.
* 	
* 
* The following interface are the functions of the API through ORCA Server between the Personalization-Server and the Natural-OWL machine.
* 
* 
* @author Tegos Athnasios
* @version 2
* 
**/


public interface UMVisit {
	
	
	public void newUser(String userName, String userType) throws UMException;
	 /**
	 * Register a new user to Personalization-Server.
	 * @param userName, The identifier of the new user, like "1001"
	 * @param userType, The type of the user, like "http://www.aueb.gr/users/ion/owlnl/UserModelling#Adult", ... 
	 * @throws UMException() if anything goes wrong.   
	 */
	
	public boolean checkUserExists(String userName) throws UMException;
	 /**
	 * Check if User Exists
	 * @param userName, The identifier of the new user.
	 * @throws UMException() if anything goes wrong.   
	 */
	
	
		
	public float getInterestFact(String factId, String ClassURI, String userName, String userType,String Robot_Personality) throws UMException;
	 /**
	 * Returns the Interest of the factId which mean, how interest for the user:"userName", whose type is:"userType" depending also to the robot personality: "Robot1" interest for this fact,
	 * the Instance X for the Property Y is or the Instance X of the Class Y is.
	 * With the term factId we mean a string that has the above information in the following form. 
	 * 
	 * @param factId, is a string which has the following two forms (one for the interest of Instance for Property and 
	 * 	one for the interest of Instance of Class).More specifically:
	 * 	
	 * 	When you want the interest of an Instance for a Property, factId must be:	
	 * 	[InstanceURI, PropertyURI, Value_of_PropertyURI]
	 * 	
	 * 	When you want the interest of an Instance of a Class factId must be:
	 * 	[InstanceURI, typeOfClass, ClassURI]
	 * 	
	 * 	NOTE-1: when you want the interest of an Instance of a Class you should have in second place the string which indicates type-of "http://www.w3.org/1999/02/22-rdf-syntax-ns#type",
	 * 	just like above.
	 * 	
	 * 	NOTE-2: the URI should be EXACTLY-LIKE!!! the URI in the UserModelind.rdf file.
	 * 
	 * @param ClassURI, is the URI of the Class that the instance belongs to
	 * @param userName, The identifier of the new user.
	 * @param userType, The type of the user, like "http://...#Adult", etc...
	 * @throws UMException() if anything goes wrong.
	 */

	
	
	public float getMicroPlanningAppropriateness(String microPlanningId, String userName, String userType) throws UMException;
	 /**
	 * Get the MicroPlanningAppropriateness, returns how interest for the user:"userName", whose type is:"userType"
	 * the microPlanningId is.
	 *
	 * @param microPlanningId, is the microPlanning URI from the UserModeling.rdf file.
	 * @param userName, The identifier of the new user.
	 * @param userType, The type of the user, like "Adult", "Child" or "Group"
	 * @throws UMException() if anything goes wrong.   
	 */

	
	public int getNumberOfFacts(String userName, String userType) throws UMException;
	 /**
	 * Get the max-Number Of Facts per sentence (FPS) for the user:"userName", whose type is:"userType"
	 * 
	 * @param userName, The identifier of the new user.
	 * @param userType, The type of the user, like "Adult", "Child" or "Group"
	 * @throws UMException() if anything goes wrong.   
	 */
	
	
	public int getMaxFactsPerSentence(String userName, String userType) throws UMException;
	 /**
	 * Get the Max Number Of Facts per text that the Natural-OWL machine produces (MFPS)
	 * for the user:"userName", whose type is:"userType"
	 * @param userName, The identifier of the new user.
	 * @param userType, The type of the user, like "Adult", "Child" or "Group"
	 * @throws UMException() if anything goes wrong.   
	 */
	
	
	
	public int getVoice(String userName, String userType) throws UMException;
	 /**
	 * Get the Voise of the synthesizer (SV) for the user:"userName", whose type is:"userType"
	 * if return: 	1->male
	 * 		2->female	
	 * 		3->child
	 * 
	 * @param userName, The identifier of the new user.
	 * @param userType, The type of the user, like "Adult", "Child" or "Group"
	 * @throws UMException() if anything goes wrong.   
	 */
	

	
	public float getAssimilationScore(String factId, String ClassURI, String userName, String userType) throws UMException;
	 /**
	 * Returns the Assimilation-Score for the factId. Assimilation-Score, is an indicator which reveals how much a user have assimilate
	 * a fact.
	 * 
	 * For example, if the property "has-areas" for the instance "building_1" has repetions=3, the assimilation-score in the begining (for that fact)
	 * will be 0 and whenever the machine produces a text that contain that fact(....building1 has areas...) the score will be increased 
	 * each time at assimilation-rate=1/3.
	 * 
	 * Generally, if a fact has repetition=x but not zero, then the assimilation-score for that fact will be 0 in the begining and whenever
	 * the machine uses that fact, the assimilation-score (for that fact) will be increased each time at assimilation-rate=1/x. But if a fact 
	 * has repetition=0 then the assilation-score for that fact is 1 and the assimilation-rate=0.
	 * 
	 * The Natural-OWL uses the assimilation-Score in order to choose the content for the text that is going to produce.
	 * 
	 * @param factId, is a string which has the following two forms (one for the assimilation-score of Instance for Property and 
	 * 	one for the the assimilation-score of Instance of Class).More specifically:
	 * 	
	 * 	When you want the assimilation-score of an Instance for a Property, factId must be:	
	 * 	[InstanceURI, PropertyURI, Value_of_PropertyURI]
	 * 	
	 * 	When you want the assimilation-score of an Instance of a Class factId must be:
	 * 	[InstanceURI, typeOfClass, ClassURI]
	 * 	
	 * 	NOTE-1: when you want the assimilation-score of an Instance of a Class you should have in second place the word "http://www.w3.org/1999/02/22-rdf-syntax-ns#type",
	 * 	just like above.
	 * 	
	 * 	NOTE-2: the URI should be EXACTLY-LIKE the URI in the UserModelind.rdf file.
	 * 
	 * @param ClassURI, is the URI of the Class that the instance belongs to
	 * @param userName, The identifier of the new user.
	 * @param userType, The type of the user, like "Adult", "Child" or "Group"
	 * @throws UMException() if anything goes wrong.
	 */

	
	public int getMentionedCount(String InstanceURI, String userName) throws UMException;
	 /**
	 * Get how many times the instance:"InstanceURI" for the user:"userName", have been used (how many times the Natural-OWL machine have
	 * produced text for that instance)
	 *
	 * @param InstanceURI, is the instance URI from the UserModeling.rdf file.
	 * @param userName, The identifier of the new user.
	 * @throws UMException() if anything goes wrong.   
	 */
	
	
	public int getMicroPlanningCount(String microPlanningId, String userName, String userType) throws UMException;
	 /**
	 * Get how many times the microplan:"microPlanningId" for the user:"userName", have been used (how many times the Natural-OWL machine have
	 * produced text with that microplan)
	 *
	 * @param microPlanningId, is the microplan URI from the UserModeling.rdf file.
	 * @param userName, The identifier of the new user.
	 * @param userType, The type of the user, like "Adult", "Child" or "Group"
	 * @throws UMException() if anything goes wrong.   
	 */
		
	
	public void update(String[] InstanceURI, String[] Facts, String[] MicroplanURI, String userName, String userType) throws UMException;
	
	 /**
	 * This function updates the databases of Personalization-Server. More specifically, whenever the machine produces a text, then you must call
	 * this function with the things that have been used into the text (the instances that have used, the facts and the microplans) in order to update
	 * the assimalation-scores on the used facts, to increase the number of used microplans and instances and generally to update the databases and
	 * the personalization-server.
	 * 
	 * @param InstanceURI[], is an array which has the used instances (contains Inastance_URI).
	 * @param Facts[], is an array which has the facts that have been used. The facts should be in the same form as above:
	 * 
	 * 	for example, the "n" possition of the Fact will contain: [InstanceURI_1, PropertyURI_1, Value_of_PropertyURI_1]
	 * 		     the "n+1" will contain: [InstanceURI_2, PropertyURI_2, Value_of_PropertyURI_2]
	 * 		     the "n+2" will contain: [InstanceURI_3, typeOfClass, ClassURI_3]
	 * 		     ....	
	 * 
	 * @param MicroplanURI[], is an array which has the used microplans (contains Microplan_URI)
	 * @param userName, The identifier of the new user.
	 * @param userType, The type of the user, like "Adult", "Child" or "Group"
	 * @throws UMException() if anything goes wrong.   
	 */        
	 	
}
