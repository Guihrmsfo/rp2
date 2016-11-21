package core;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import twitter4j.*;
import twitter4j.Query.ResultType;
import twitter4j.api.TrendsResources;
import twitter4j.conf.*;

public class Main {

	static final int USA_WOEID = 23424977;
	static final String OAUTHCONSUMERKEY = Twitter4jConfig.OAUTHCONSUMERKEY;
	static final String OAUTHCONSUMERSECRET = Twitter4jConfig.OAUTHCONSUMERSECRET;
	static final String OAUTHACCESSTOKEN = Twitter4jConfig.OAUTHACCESSTOKEN;
	static final String OAUTHACCESSTOKENSECRET = Twitter4jConfig.OAUTHACCESSTOKENSECRET;

	public static void main(String[] args) throws IOException {

		//FileWriter file = new FileWriter("tweets.json");
		OutputStreamWriter file =  new OutputStreamWriter(new FileOutputStream("tweets.json"), StandardCharsets.UTF_8);

		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true).setOAuthConsumerKey(OAUTHCONSUMERKEY).setOAuthConsumerSecret(OAUTHCONSUMERSECRET)
				.setOAuthAccessToken(OAUTHACCESSTOKEN).setOAuthAccessTokenSecret(OAUTHACCESSTOKENSECRET);

		TwitterFactory twitterFactory = new TwitterFactory(cb.build());
		Twitter twitter = twitterFactory.getInstance();
		List<Status> tweets = new ArrayList<Status>();
		OntologyModel model = new OntologyModel();

		int x;

		try {
			Trends trends = twitter.trends().getPlaceTrends(USA_WOEID);
			for (Trend t : trends.getTrends()) {
				System.out.println(t.getName());

				Query query = new Query(t.getQuery());
				query.setCount(1);
				query.setLang("en");
				query.setResultType(ResultType.popular);
				QueryResult result = twitter.search(query);

				TrendingTopic trendingTopic = new TrendingTopic(t.getName(), "en");
				trendingTopic.setTweets(result.getTweets());

				x = 0;
				for (Status status : trendingTopic.getTweets()) {
					System.out.println(status.getCreatedAt());

					tweets.add(status);
					x++;
					/*
					 * System.out.println(x + ":"); System.out.println("@" +
					 * status.getUser().getScreenName() + ": " +
					 * status.getText()); System.out.println("");
					 */
				}
				
				model.createTrendingTopic(trendingTopic);

			}
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		JSONArray jsonArray = new JSONArray(tweets);

		file.write(jsonArray.toString());
		
		file.flush();
		file.close();
		
		Process p = Runtime.getRuntime().exec("python tweets.py tweets.json");
		
		model.save("twitter.owl");

	}

}
