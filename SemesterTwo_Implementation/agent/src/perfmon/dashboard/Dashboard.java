package perfmon.dashboard;

import perfmon.database.DatabaseDashboard;
import perfmon.util.Config;
import perfmon.util.Pair;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

import java.util.Hashtable;

import com.google.gson.Gson;

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
		java.util.Map<String, String[]> params = request.getParameterMap();
		PrintWriter out = response.getWriter();
		Gson json = new Gson();
		response.setContentType("text/json");


		if (params.get("context") == null){
			response.setContentType("text/plain");
			out.println("No context specified.");
		} else if (params.get("context")[0].equals("nodes")){
			out.println(json.toJson(this.database.getNodes(10)));
		} else if (params.get("context")[0].equals("sysinfo")){
			if (params.get("node") != null){
				out.println(json.toJson(this.database.getSysinfo(Integer.parseInt(params.get("node")[0]))));
			}
		} else if (params.get("context")[0].equals("netadapters")){
			if (params.get("node") != null){
				out.println(json.toJson(this.database.getNetadapters(Integer.parseInt(params.get("node")[0]))));
			}
		} else if (params.get("context")[0].equals("netusage")){
			if (params.get("adapter") != null){
				out.println(json.toJson(this.database.getNetusage(Integer.parseInt(params.get("adapter")[0]))));
			}
		} else if (params.get("context")[0].equals("hdds")){
			if (params.get("node") != null){
				out.println(json.toJson(this.database.getHardDrives(Integer.parseInt(params.get("node")[0]))));
			}
		} else if (params.get("context")[0].equals("hddusage")){
			if (params.get("hdd") != null){
				out.println(json.toJson(this.database.getHardDriveUsage(Integer.parseInt(params.get("hdd")[0]))));
			}
		}


		this.database.close();
	}

	public void destroy(){}
}
