import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
public class getConversation extends HttpServlet{
	

	@SuppressWarnings("unchecked")
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		resp.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
		resp.setHeader("Pragma", "no-cache"); // HTTP 1.0.
		resp.setDateHeader("Expires", 0); // Proxies.
		
		if(req.getSession(false)!=null) {
		
		int friendId = Integer.parseInt(req.getParameter("id"));
		int userId = (int) req.getSession().getAttribute("id");
		
		
		File msgDirc = new File("C:/Users/Jakub/eclipse-workspace/TalkWebClient/Conversations/"+friendId+"-"+userId);
		File msgDir2c = new File("C:/Users/Jakub/eclipse-workspace/TalkWebClient/Conversations/"+userId+"-"+friendId);
		
		if(!msgDirc.exists() && !msgDir2c.exists()) {
			msgDirc.mkdir();
		}
		
		File msgDirectory=null;
		if(msgDirc.exists()){
			msgDirectory = msgDirc;
		}else if(msgDir2c.exists()) {
			msgDirectory = msgDir2c;
		}else {
			return;
		}
		
		
		File[] files = msgDirectory.listFiles();
		if(files.length>0) {
			
		int n = files.length-1;
		FileReader fw;
		BufferedReader br;
		JSONParser parser = new JSONParser();
		PrintWriter pw = resp.getWriter();
		JSONObject msgs = new JSONObject();
		int counter = 1;
		
		int msgsLoaded=Integer.parseInt(req.getParameter("counter"));
		int msgsToLoad=files.length;
		msgsToLoad-=msgsLoaded;
		if(msgsToLoad>=10) {
		msgsToLoad = 10;
		}
		
		for(int i = n-msgsLoaded; i>n-(msgsToLoad+msgsLoaded); i--) {
			Object obj;
			try {
				obj = parser.parse(new FileReader(files[i]));
	            JSONObject jsonObject = (JSONObject) obj;
	            msgs.put(""+counter, jsonObject);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			counter++;
		}
		
		
		pw.write(msgs.toJSONString());
		
		}else {
			resp.sendRedirect("/TalkWebClient/login");
		}
		
		}
		
//		File file = new File("C:/Users/Jakub/eclipse-workspace/TalkWebClient/Conversations/conv.json");
//		JSONParser pars = new JSONParser();
//		FileReader fr;
//		if(file.exists() && !file.isDirectory()) {
//			 fr = new FileReader("C:/Users/Jakub/eclipse-workspace/TalkWebClient/Conversations/conv.json");
//		}else {
//			file.createNewFile();
//			FileWriter fw = new FileWriter(file);
//			JSONObject conversation = new JSONObject();
//			conversation.put("conv", "true");
//		    fw.write(conversation.toJSONString());
//			fr = new FileReader("C:/Users/Jakub/eclipse-workspace/TalkWebClient/Conversations/conv.json");
//		}
//		
//		Object obj;
//		try {
//			obj = pars.parse(fr);
//			JSONObject jsonObject = (JSONObject) obj;
//			PrintWriter out = resp.getWriter();
//			out.write(jsonObject.toJSONString());
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}

        
		
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		doGet(req,resp);
		
	}

	
	
}
