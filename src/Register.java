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
import java.util.Date;
import java.security.*;
import java.sql.*;
public class Register extends HttpServlet{
	
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
		resp.setHeader("Pragma", "no-cache"); // HTTP 1.0.
		resp.setDateHeader("Expires", 0); // Proxies.
		if(req.getSession(false)!=null) {
			
			resp.sendRedirect("/TalkWebClient/home");
		}else {
			
			RequestDispatcher rs = req.getRequestDispatcher("register.html");
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
		String name = req.getParameter("name");
		String surname = req.getParameter("surname");
		
		try {
			Sm.connectToDB();
			
			PreparedStatement ps = Sm.conn.prepareStatement("select * from users where login =?");
			ps.setString(1, login);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				RequestDispatcher rdisp = req.getRequestDispatcher("register.html");
				rdisp.forward(req, resp);
			}else {
				PreparedStatement insert = Sm.conn.prepareStatement("insert into users(login,password,name,surname) values(?,?,?,?)");
				insert.setString(1, login);
				insert.setString(2, password);
				insert.setString(3, name);
				insert.setString(4, surname);
				insert.executeUpdate();
				
				resp.sendRedirect("/TalkWebClient/login");
			}
		} catch (ClassNotFoundException | SQLException e) {
			
			e.printStackTrace();
		}
		finally {
			Sm.disconnect();
		}
		
		
	}
}
