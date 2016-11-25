package core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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

	public static void searchTrends(int trendCount, int tweetCount) throws TwitterException, IOException {
		// create root folder if it doesn't exist
		File newFile = new File(FOLDER_NAME);
		if (!newFile.exists()) {
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

	public static QueryResult findTrendingTopicTweets(Trend trend, int tweetCount) throws TwitterException {
		// System.out.println(t.getName());
		Query query = new Query(trend.getQuery());
		query.setCount(tweetCount);
		query.setLang("en");
		query.setResultType(ResultType.popular);
		return twitter.search(query);
	}

	public static void saveTrendingTopic(TrendingTopic trend) throws IOException {
		// create Trend root folder
		File newFile = new File(FOLDER_NAME + File.separator + trend.getFormattedName());
		newFile.mkdir();

		// initialize variables
		OntologyModel model = new OntologyModel();
		List<Status> tweets = new ArrayList<Status>();

		// output for tweets json file
		OutputStreamWriter file = new OutputStreamWriter(
				new FileOutputStream(
						FOLDER_NAME + File.separator + trend.getFormattedName() + File.separator + "tweets.json"),
				StandardCharsets.UTF_8);

		// add tweets to array
		for (Status status : trend.getTweets()) {
			System.out.println(status.getCreatedAt());
			tweets.add(status);
		}

		// create ontology individual
		model.createTrendingTopic(trend);
		// save ontology model
		model.save(FOLDER_NAME + File.separator + trend.getFormattedName() + File.separator + "ontology.owl");

		// save tweets
		JSONArray jsonArray = new JSONArray(tweets);
		file.write(jsonArray.toString());
		file.flush();
		file.close();

		// text mining
		Runtime.getRuntime().exec("python tweets.py tweets.json");
	}

}
