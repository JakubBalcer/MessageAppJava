import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;

import java.security.*;
import java.sql.*;
public class Login extends HttpServlet{

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
		resp.setHeader("Pragma", "no-cache"); // HTTP 1.0.
		resp.setDateHeader("Expires", 0); // Proxies.
		if(req.getSession(false)!=null) {
			
			resp.sendRedirect("/TalkWebClient/home");
		}else {
		RequestDispatcher rs = req.getRequestDispatcher("login.html");
		rs.forward(req, resp);
		}
		
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
		resp.setHeader("Pragma", "no-cache"); // HTTP 1.0.
		resp.setDateHeader("Expires", 0); // Proxies.
		String login = req.getParameter("login");
		String password = req.getParameter("password");
		
		try {
			Sm.connectToDB();
			PreparedStatement ps = Sm.conn.prepareStatement("select * from users where login=? and password=?");
			ps.setString(1, login);
			ps.setString(2, password);
			
			ResultSet rs = ps.executeQuery();
			
			if(rs.next()) {
				HttpSession session = req.getSession();
				session.setAttribute("id", rs.getInt(1));
				session.setAttribute("login", login);
				session.setAttribute("name", rs.getString(4));
				session.setAttribute("surname", rs.getString(5));
				session.setAttribute("age", rs.getInt(6));
				
				resp.sendRedirect("/TalkWebClient/home");
			}else {
				
				RequestDispatcher reqdi = req.getRequestDispatcher("login.html");
				reqdi.include(req, resp);
			}
			
		} catch (SQLException | ClassNotFoundException e) {
			
			e.printStackTrace();
		}finally {
			Sm.disconnect();
		}
		
	}

	
	
}
