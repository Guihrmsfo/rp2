package website;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/trends")
public class Trends extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public Trends() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String date = request.getParameter("date");
		String[] trends = WebSite.getAvailableTrends(date);
		request.setAttribute("trends", trends);
		request.setAttribute("date", date);
		request.getRequestDispatcher("trends.jsp").forward(request, response);

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
