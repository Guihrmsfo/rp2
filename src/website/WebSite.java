package website;

import java.io.File;

import core.TrendingTopic;
import core.TrendingTopicSearch;

public class WebSite {
	static final String FOLDER_NAME = TrendingTopicSearch.FOLDER_NAME;
	static final String ABSOLUTE_PATH = "C:\\Users\\Guilherme\\workspace\\RP2";
	static final String DIRECTORY = ABSOLUTE_PATH + File.separator + FOLDER_NAME;

	public static String[] getAvailableDates() {
		File datesDirectory = new File(DIRECTORY);
		return datesDirectory.list();
	}

	public static String[] getAvailableTrends(String date) {
		File trendsDirectory = new File(DIRECTORY + File.separator + date);
		return trendsDirectory.list();
	}

	public static TrendingTopic getTrend(String date, String trend) {
		File trendDirectory = new File(DIRECTORY + File.separator + date + File.separator + trend);
		TrendingTopic tt = new TrendingTopic(trend, "en");
		return tt;
	}

}
