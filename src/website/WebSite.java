package website;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.mcavallo.opencloud.Cloud;
import org.mcavallo.opencloud.Tag;

import core.TrendingTopic;
import core.TrendingTopicSearch;

public class WebSite {
	static final String FOLDER_NAME = TrendingTopicSearch.FOLDER_NAME;
	static final String TWITTER_FOLDER = TrendingTopicSearch.TWITTER_FOLDER;
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

	public static String getTrendURL(String date, String trend) {
		return TWITTER_FOLDER + File.separator + date + File.separator + trend;
	}

	public static List<Tag> getCloudTags(String date, String trend) throws IOException {
		Cloud cloud = new Cloud(); // create cloud
		cloud.setMaxWeight(30.0); // max font size
		cloud.setMinWeight(10.0);

		String words = DIRECTORY + File.separator + date + File.separator + trend + File.separator + "words.txt";
		String tags = "";
		for (String line : Files.readAllLines(Paths.get(words))) {
			tags = tags + line;
		}
		cloud.addText(tags);
		return cloud.tags();
	}

}
