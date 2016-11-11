package core;

import java.util.Date;
import java.util.List;

import twitter4j.*;

public class TrendingTopic {

	private String name;
	private Date date;
	private String language;
	private List<Status> tweets;

	public TrendingTopic(String name, String language) {
		super();
		this.name = name;
		this.language = language;
	}

	public String getName() {
		return name;
	}

	public Date getDate() {
		return date;
	}

	public String getLanguage() {
		return language;
	}

	public List<Status> getTweets() {
		return tweets;
	}

	public void setTweets(List<Status> tweets) {
		this.tweets = tweets;
		this.date = this.getStartDate();
	}

	private Date getStartDate() {
		if (tweets.size() <= 0) {
			return null;
		}

		Date date = tweets.get(0).getCreatedAt();
		for (int x = 1; x < tweets.size(); x++) {
			if (tweets.get(x).getCreatedAt().before(date)) {
				date = tweets.get(x).getCreatedAt();
			}
		}
		return date;
	}

	private Date getFinishDate() {
		if (tweets.size() <= 0) {
			return null;
		}

		Date date = tweets.get(0).getCreatedAt();
		for (int x = 1; x < tweets.size(); x++) {
			if (tweets.get(x).getCreatedAt().after(date)) {
				date = tweets.get(x).getCreatedAt();
			}
		}
		return date;
	}

}
