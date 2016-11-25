package core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import twitter4j.JSONArray;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Trend;
import twitter4j.Trends;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.Query.ResultType;
import twitter4j.conf.ConfigurationBuilder;

public class TrendingTopicSearch {

	static final int USA_WOEID = 23424977;
	static final String OAUTHCONSUMERKEY = Twitter4jConfig.OAUTHCONSUMERKEY;
	static final String OAUTHCONSUMERSECRET = Twitter4jConfig.OAUTHCONSUMERSECRET;
	static final String OAUTHACCESSTOKEN = Twitter4jConfig.OAUTHACCESSTOKEN;
	static final String OAUTHACCESSTOKENSECRET = Twitter4jConfig.OAUTHACCESSTOKENSECRET;
	static final ConfigurationBuilder cb = new ConfigurationBuilder().setDebugEnabled(true)
			.setOAuthConsumerKey(OAUTHCONSUMERKEY).setOAuthConsumerSecret(OAUTHCONSUMERSECRET)
			.setOAuthAccessToken(OAUTHACCESSTOKEN).setOAuthAccessTokenSecret(OAUTHACCESSTOKENSECRET);
	static final TwitterFactory twitterFactory = new TwitterFactory(cb.build());
	static final Twitter twitter = twitterFactory.getInstance();
	static final String FOLDER_NAME = "Twitter";
	static final double UNLIMITED = Double.POSITIVE_INFINITY;
	static String DATE_FOLDER;
	static String DIRECTORY;

	public static void searchTrends(double trendCount, int tweetCount) throws TwitterException, IOException {
		// set current date
		DATE_FOLDER = new SimpleDateFormat("d-M-Y").format(Calendar.getInstance().getTime());
		DIRECTORY = FOLDER_NAME + File.separator + DATE_FOLDER;

		// create root folder if it doesn't exist
		File newFile = new File(DIRECTORY);
		if (!newFile.exists()) {
			if (!newFile.getParentFile().exists()) {
				newFile.getParentFile().mkdir();
			}
			newFile.mkdir();
		}

		int x = 0;
		Trends trends = twitter.trends().getPlaceTrends(USA_WOEID);
		for (Trend t : trends.getTrends()) {
			x++;
			QueryResult result = findTrendingTopicTweets(t, tweetCount);
			TrendingTopic trendingTopic = new TrendingTopic(t.getName(), "en");
			trendingTopic.setTweets(result.getTweets());
			saveTrendingTopic(trendingTopic);

			if (x >= trendCount) {
				break;
			}
		}

	}

	private static QueryResult findTrendingTopicTweets(Trend trend, int tweetCount) throws TwitterException {
		// System.out.println(t.getName());
		Query query = new Query(trend.getQuery());
		query.setCount(tweetCount);
		query.setLang("en");
		query.setResultType(ResultType.popular);
		return twitter.search(query);
	}

	private static void saveTrendingTopic(TrendingTopic trend) throws IOException {
		// create Trend root folder
		String directory = DIRECTORY + File.separator + trend.getFormattedName();
		File newFile = new File(directory);
		newFile.mkdir();

		// output for tweets json file
		String tweetsFileName = directory + File.separator + "tweets.json";
		OutputStreamWriter file = new OutputStreamWriter(new FileOutputStream(tweetsFileName), StandardCharsets.UTF_8);

		// add tweets to array
		List<Status> tweets = new ArrayList<Status>();
		for (Status status : trend.getTweets()) {
			tweets.add(status);
		}

		// save model
		TrendingTopicSearch.saveOntology(trend, directory);

		// save tweets
		JSONArray jsonArray = new JSONArray(tweets);
		file.write(jsonArray.toString());
		file.flush();
		file.close();

		// text mining
		Process p = Runtime.getRuntime()
				.exec("python tweets.py " + FOLDER_NAME + " \"" + trend.getFormattedName() + "\"");
		TrendingTopicSearch.printProcessErrorLog(p.getInputStream());
	}

	private static void saveOntology(TrendingTopic trend, String directory) throws IOException {
		String ontologyFileName = directory + File.separator + "ontology.owl";
		OntologyModel model = new OntologyModel();
		// create ontology individual
		model.createTrendingTopic(trend);
		// save ontology model
		model.save(ontologyFileName);
	}

	private static void printProcessErrorLog(InputStream error) throws IOException {
		InputStreamReader isrerror = new InputStreamReader(error);
		BufferedReader bre = new BufferedReader(isrerror);
		String line;
		while ((line = bre.readLine()) != null) {
			System.out.println(line);
		}
	}

}
