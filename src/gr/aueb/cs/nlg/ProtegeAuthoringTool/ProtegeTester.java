package gr.aueb.cs.nlg.ProtegeAuthoringTool;


public class ProtegeTester 
{

  // before running
  // this tester
  // you must change the working dir to C:\Program Files\Protege_3.3.1
   public static void main(String[] args) 
   {
        int a  = 1;
        System.err.println("boooo");
        a = 7;
        String myargs  [] ={"C:\\NaturalOWL\\NLFiles-MPIRO\\mpiro.pprj"};
        edu.stanford.smi.protege.Application.main(myargs);
        a = 9;
        
   }

}
