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

	private static DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	private String SOURCE = "http://www.eswc2006.org/technologies/ontology";
	private String NS = SOURCE + "#";
	private OntModel model;
	private OntClass trendingTopic;
	private Property date;
	private int trendCount;

	public OntologyModel() {
		model = ModelFactory.createOntologyModel();
		trendingTopic = model.createClass(NS + "TrendingTopic");
		date = model.createProperty(NS + "date");
		trendCount = 0;
	}

	public void createTrendingTopic(TrendingTopic trend) {
		this.trendCount++;
		Individual tt = trendingTopic.createIndividual(NS + "tt" + this.trendCount);
		tt.addProperty(VCARD.TITLE, trend.getName());
		if(trend.getDate() != null){
			tt.addProperty(date, dateFormat.format(trend.getDate().getTime()));
		}
	}

	public void save(String fileName) throws IOException {
		FileWriter fw = new FileWriter(fileName);
		model.write(fw);
		fw.close();
	}

}
