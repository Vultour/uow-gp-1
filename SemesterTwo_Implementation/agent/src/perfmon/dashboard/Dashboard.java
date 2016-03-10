package perfmon.dashboard;

import perfmon.database.DatabaseDashboard;
import perfmon.util.Config;
import perfmon.util.Pair;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

import java.util.Hashtable;

public class Dashboard extends HttpServlet{
	private DatabaseDashboard database;

	public void init() throws ServletException{}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		this.database = new DatabaseDashboard(
			Config.DATABASE_HOST,
			Config.DATABASE_PORT,
			Config.DATABASE_NAME,
			Config.DATABASE_USER,
			Config.DATABASE_PASS
		);


		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<h1>It probably doesn't work...</h1>");
		Hashtable<Integer, String> nodes = this.database.getNodes(10);
		for (Integer key: nodes.keySet()){ out.println(nodes.get(key) + ": " + key.toString()); }


		this.database.close();
	}

	public void destroy(){}
}
