package gr.aueb.cs.nlg.NLGEngine;
import java.lang.Exception;

public class PronounsFileNotFoundException extends Exception{
	
	public PronounsFileNotFoundException(){
        super("Pronouns File Not Found!!!!");
    }
}