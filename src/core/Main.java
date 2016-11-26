package core;

import java.io.IOException;

import twitter4j.TwitterException;

public class Main {

	public static void main(String[] args) throws IOException, TwitterException {
		//TrendingTopicSearch.searchTrends(TrendingTopicSearch.UNLIMITED, 1);
		TrendingTopicSearch.searchTrends(TrendingTopicSearch.UNLIMITED, 100);
	}

}
