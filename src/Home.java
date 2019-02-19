import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Date;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;

import java.security.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
@SuppressWarnings("unused")
public class Home extends HttpServlet{

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
		resp.setHeader("Pragma", "no-cache"); // HTTP 1.0.
		resp.setDateHeader("Expires", 0); // Proxies.
		
		if(req.getSession(false)!=null) {
			
			RequestDispatcher rs = req.getRequestDispatcher("home.html");
			rs.forward(req, resp);
		}else {
			resp.sendRedirect("/TalkWebClient/login");
		}
		
		
	}

	@SuppressWarnings({ "resource", "unchecked" })
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String msg = req.getParameter("msg");
		if(msg.isEmpty() || msg==null) {
			return;
		};
		String to = req.getParameter("to");
		LocalDateTime date = LocalDateTime.now();
		long secs = (new Date().getTime());
		DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"); 
	    String formattedDate = date.format(myFormatObj); 
	    int id = (int) req.getSession().getAttribute("id");
		JSONObject newMsg = new JSONObject();
		newMsg.put("message", msg);
		newMsg.put("showDate", formattedDate);
		newMsg.put("computeDate", secs);
		newMsg.put("from", id);
		
//		System.out.println("message: "+newMsg.get("message")+" | showDate: "+newMsg.get("showDate") + " | cDate: "+ newMsg.get("computeDate") + " | from: "+newMsg.get("from"));
		
		File msgDir1 = new File("C:/Users/Jakub/eclipse-workspace/TalkWebClient/Conversations/"+id+"-"+to);
		File msgDir2 = new File("C:/Users/Jakub/eclipse-workspace/TalkWebClient/Conversations/"+to+"-"+id);
		FileWriter message;
		if(msgDir1.exists()) {
			message = new FileWriter("C:/Users/Jakub/eclipse-workspace/TalkWebClient/Conversations/"+id+"-"+to+"/"+secs+".json");
		}else if(msgDir2.exists()) {
			message = new FileWriter("C:/Users/Jakub/eclipse-workspace/TalkWebClient/Conversations/"+to+"-"+id+"/"+secs+".json");
		}else {
			return;
		}
		
		message.write(newMsg.toJSONString());
		message.flush();
		message.close();
		
		resp.sendRedirect("/TalkWebClient/home");
		
	}
	
	
}
