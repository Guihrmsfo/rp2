package gr.aueb.cs.nlg.NLFiles;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import java.util.*;
import gr.aueb.cs.nlg.*;

import org.apache.log4j.Logger;
import org.apache.log4j.Level;

public class NLI
{
        private NLIManager NLIMan;
        private LexiconManager Lex;
        private MicroplansAndOrderingManager Micros;      
        
        public String NLI_NS = "http://www.aueb.gr/users/ion/owlnl/NLI#";
        static Logger logger = Logger.getLogger("gr.aueb.cs.nlg.NLFiles.NLI");
        
	private Hashtable<String, HashSet> wh_Mapping; //[owl resource => list of wh questions]
        private Hashtable<String, String> wh_words; //[owl resource => list of wh questions]                
        
	public NLI()
        {               
            
	}
	
        public void init()
        {
            NLI_NS = "http://www.aueb.gr/users/ion/owlnl/NLI#";
            wh_Mapping = null;
            
            wh_words = new Hashtable<String, String>();
            wh_Mapping = new Hashtable<String, HashSet>();	

        }

        //-----------------------------------------------------------------------------------

        public void Load(String path, String file)
        {
            init();
            
            NLIMan = new NLIManager("");
            Micros = new MicroplansAndOrderingManager("");
            Lex = new LexiconManager("");
                    
            NLIMan.read(path, file);
            NLI_NS = NLIMan.model.getNsPrefixURI("");
            
            if(NLI_NS == null)
            {
                NLI_NS = "http://www.aueb.gr/users/ion/owlnl/NLI#";
            }
            
            Model m = NLIMan.getModel();   
            
            StmtIterator StmtIter = m.listStatements(null, NLIManager.WhLinkProperty, (RDFNode)null);
                                                        
            while(StmtIter.hasNext())
            {
                Statement stmt = StmtIter.nextStatement();
                String SubResURI = stmt.getSubject().asNode().getURI();
                String ObjResURI = stmt.getObject().asNode().getURI();
                
                if(wh_Mapping.containsKey(SubResURI))
                {
                    HashSet set = wh_Mapping.get(SubResURI);
                    set.add(ObjResURI);
                    wh_Mapping.put(SubResURI, set);
                }
                else
                {
                    HashSet set = new HashSet();
                    set.add(ObjResURI);
                    wh_Mapping.put(SubResURI, set);                    
                }
            }
            
            StmtIter = m.listStatements(null, NLIManager.whTextProperty , (RDFNode)null);                
                    
            while(StmtIter.hasNext())
            {
                Statement stmt = StmtIter.nextStatement();
                String SubResURI = stmt.getSubject().asNode().getURI();
                String ObjRes = stmt.getObject().asNode().getLiteral().toString();
                
                wh_words.put(SubResURI, ObjRes);
   
            }            
        }
        
        public Iterator getWh(String owlURI)
        {
            if(wh_Mapping.containsKey(owlURI))
            {                
                return wh_Mapping.get(owlURI).iterator();
            }
            else
                return null;
        }
                   
        public String getWhText(String URI)
        {
            if(wh_words.containsKey(URI))
            {                
                return wh_words.get(URI);
            }
            else
                return null;
        }
        
	//-----------------------------------------------------------------------------------
	public static void main(String args[])
        {    
             Logger loGGer = Logger.getLogger("gr.aueb.cs.nlg.NLFiles");
             loGGer.setLevel(Level.DEBUG);
             //BasicConfigurator.configure();
             logger.setLevel(Level.DEBUG);
             logger.debug("logger is working");
             System.err.println(logger.isDebugEnabled());
             //logger.setLevel(Level.INFO);
             logger.debug("logger is working");

             NLI nli = new NLI();
             
             
             nli.Load("C:\\NaturalOWL\\NLFiles-MPIRO\\" , "NLI.rdf");
             
             //String myuri = "http://www.aueb.gr/users/ion/mpiro.owl#sculptor";
             String myuri = "http://www.aueb.gr/users/ion/mpiro.owl#exhibit1";
             
             Iterator it = nli.getWh(myuri);
                 
             while(it !=null && it.hasNext() )
             {
                 String uri = it.next().toString();
                 System.err.println(uri + " #### " + nli.getWhText(uri));
                                  
             }

             
             //LQM.writeLexicon(null, "C:\\NLG_Project\\NLFiles-MPIRO\\test\\Lexicon.rdf");
             
             //LQM.LoadLexicon("C:\\NLG_Project\\NLFiles-MPIRO\\test\\" , "Lexicon.rdf");
	}
    
}
