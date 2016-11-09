package core;


import java.io.FileWriter;
import java.io.IOException;

import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.*;

public class OntologyModel {

	public static void main(String[] args) throws IOException {
		String SOURCE = "http://www.eswc2006.org/technologies/ontology";
		String NS = SOURCE + "#";
		
		OntModel model = ModelFactory.createOntologyModel();
		OntClass trendingTopic = model.createClass( NS + "TrendingTopic" );
		OntClass title = model.createClass( NS + "Title" );
		
		ObjectProperty hasTitle = model.createObjectProperty( NS + "hasTitle");
		hasTitle.addDomain(trendingTopic);
		
		FileWriter fw = new FileWriter("twitter.owl");
		model.write(fw);
		fw.close();

	}

}
