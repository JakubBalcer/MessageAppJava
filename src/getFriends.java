import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
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

import org.json.JSONObject;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;

import java.security.*;
import java.sql.*;
public class getFriends extends HttpServlet{

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		resp.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
		resp.setHeader("Pragma", "no-cache"); // HTTP 1.0.
		resp.setDateHeader("Expires", 0); // Proxies.
		
		if(req.getSession(false)!=null) {
			
		PrintWriter out = resp.getWriter();
		JSONObject obj = new JSONObject();
		String sql = "select connections.user1, connections.user2 from users " + 
				"left join connections on users.id=connections.user1 or users.id=connections.user2 " + 
				"where users.id = ?";
		try {
			Sm.connectToDB();
			PreparedStatement ps = Sm.conn.prepareStatement(sql);
			ps.setInt(1, (int)req.getSession().getAttribute("id"));
			ResultSet rs = ps.executeQuery();
			int i=1;
			while(rs.next()) {
				if(rs.getInt(1)!=(int)req.getSession().getAttribute("id")) {
					PreparedStatement ps2 = Sm.conn.prepareStatement("select users.name, users.surname, users.id from users where id=?");
					ps2.setInt(1, rs.getInt(1));
					ResultSet rs2 = ps2.executeQuery();
					JSONObject jso = new JSONObject();
					jso.put("name", rs2.getString(1));
					jso.put("surname", rs2.getString(2));
					jso.put("id", rs2.getInt(3));
					obj.put("Friend"+i, jso);

				}
				if(rs.getInt(2)!=(int)req.getSession().getAttribute("id")) {
					PreparedStatement ps2 = Sm.conn.prepareStatement("select users.name, users.surname, users.id from users where id=?");
					ps2.setInt(1, rs.getInt(2));
					ResultSet rs2 = ps2.executeQuery();
					JSONObject jso = new JSONObject();
					jso.put("name", rs2.getString(1));
					jso.put("surname", rs2.getString(2));
					jso.put("id", rs2.getInt(3));
					obj.put("Friend"+i, jso);

				}
				i++;
			}
			
			
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}finally {
			out.write(obj.toString());
			Sm.disconnect();
		}
		
		}else {
		
			resp.sendRedirect("/TalkWebClient/login");
			
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		
		
	}

	
	
}
