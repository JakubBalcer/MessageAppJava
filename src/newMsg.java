import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
import org.apache.commons.io.FilenameUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;

import java.security.*;
import java.sql.*;
public class newMsg extends HttpServlet{

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		doPost(req,resp);
		
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		if(req.getSession(false)!=null) {
		
		try
	    {

	        resp.setContentType("text/event-stream, charset=UTF-8");

	        PrintWriter pw = resp.getWriter();
	        
	        double newMsg;
	        
	        int id = (int) req.getSession().getAttribute("id");
	        int to = (int) req.getSession().getAttribute("currentChat");
	        
	        File dir1 = new File("C:/Users/Jakub/eclipse-workspace/TalkWebClient/Conversations/"+id+"-"+to);
	        File dir2 = new File("C:/Users/Jakub/eclipse-workspace/TalkWebClient/Conversations/"+to+"-"+id);
	        
	        File dir;
	        if(dir1.exists()){
				dir = dir1;
			}else if(dir2.exists()) {
				dir = dir2;
			}else {
				return;
			}

	       newMsg = Double.parseDouble(FilenameUtils.removeExtension(Sm.lastFileModified(dir.getAbsolutePath()).getName()));
	       
	       double lastMsg = (double) req.getSession().getAttribute("lastMsg");
	        
	        
	        	System.out.println("LAST "+lastMsg);
	        	System.out.println("NEW  "+newMsg);
	        	if(newMsg>lastMsg) {

	            System.out.println("Data Sent!!!");
	            pw.write("event: server-time\n\n");  //take note of the 2 \n 's, also on the next line.
	            pw.write("data: "+ "NEW Message" + "\n\n");
	            
	            
	        	}
	        

	    }catch(Exception e){
	        e.printStackTrace();
	    }
		
		}else {
			
			resp.sendRedirect("/TalkWebClient/login");
			
		}
	}
}
