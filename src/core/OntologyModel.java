package core;


import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.hp.hpl.jena.ontology.*;
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.vocabulary.VCARD;

public class OntologyModel {

	public static void main(String[] args) throws IOException {
		String SOURCE = "http://www.eswc2006.org/technologies/ontology";
		String NS = SOURCE + "#";
		
		OntModel model = ModelFactory.createOntologyModel();
		OntClass trendingTopic = model.createClass( NS + "TrendingTopic" );
		Property date = model.createProperty( NS + "date" );
		
		Individual tt = trendingTopic.createIndividual(NS + "tt1");
		tt.addProperty(VCARD.TITLE, "#Trump");
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		tt.addProperty(date, dateFormat.format(cal.getTime()));
		
		FileWriter fw = new FileWriter("twitter.owl");
		model.write(fw);
		fw.close();

	}

}
