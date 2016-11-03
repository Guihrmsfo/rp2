package core;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.*;
import edu.stanford.nlp.io.IOUtils;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.sequences.DocumentReaderAndWriter;
import edu.stanford.nlp.util.Triple;

import java.util.List;

public class NERDemo {

	public static void main(String[] args) throws Exception {

		String serializedClassifier = "classifiers/english.all.3class.distsim.crf.ser.gz";

		if (args.length > 0) {
			serializedClassifier = args[0];
		}

		AbstractSequenceClassifier<CoreLabel> classifier = CRFClassifier.getClassifier(serializedClassifier);

		String[] example = { "There is a God. She is a black woman. Beyoncé CMAawards50 ",
		"David Ross!!!! In his final MLB game, the 39-year-old catcher hits a HR in his 1st AB of Game 7. " };

		for (String str : example) {
			System.out.print(classifier.classifyToString(str, "tsv", false));
		}

	}

}
