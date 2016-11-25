package core;

import java.io.IOException;

import twitter4j.TwitterException;

public class Main {

	public static void main(String[] args) throws IOException, TwitterException {
		TrendingTopicSearch.searchTrends(1, 1);
	}

}
