package website;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mcavallo.opencloud.Tag;

import core.TrendingTopic;

/**
 * Servlet implementation class Trend
 */
@WebServlet("/trend")
public class Trend extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public Trend() {
		super();

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String date = request.getParameter("date");
		String trendName = request.getParameter("trend");
		String url = WebSite.getTrendURL(date, trendName);
		List<Tag> tags = WebSite.getCloudTags(date, trendName);
		request.setAttribute("trend", trendName);
		request.setAttribute("date", date);
		request.setAttribute("url", url);
		request.setAttribute("tags", tags);
		request.getRequestDispatcher("trend.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

}
