package redirector;
import java.io.File;
import java.util.StringTokenizer;

import protocol.HttpRequest;
import protocol.HttpResponse;
import protocol.HttpResponseFactory;
import protocol.Protocol;


public class Plugin implements server.PluginInterface {
	
	public static String body;

	@Override
	public void directRequest(HttpRequest request) {
		String reqMethod = request.getMethod();
		String uri = request.getUri();
		String servletName = getServletFromURI(uri);
		this.body = request.getBody();
		if(servletName.equals("google")){
			runGoogleServlet(reqMethod, request);
		}else{
			runBingServlet(reqMethod, request);
		}
	}
	
	@Override
	public HttpResponse getResponse(){
		File file = new File("plugins/redirector/response.html");
		
		return HttpResponseFactory.create200OK(file, Protocol.CLOSE);
	}

	private void runBingServlet(String reqMethod, HttpRequest request) {

		BingServlet bserv = new BingServlet();
		switch(reqMethod){
		case "GET":
			bserv.doGet();
			break;
		case "POST":
			bserv.doPost();
			break;
		case "Put":
			break;
		case "Delete":
			break;
		
		}
	}

	private void runGoogleServlet(String reqMethod, HttpRequest request) {
		GoogleServlet gserv = new GoogleServlet();
		switch(reqMethod){
		case "GET":
			gserv.doGet();
			break;
		case "POST":
			gserv.doPost();
			break;
		case "Put":
			break;
		case "Delete":
			break;
		}
	}

	private String getServletFromURI(String uri) {
		StringTokenizer uriTokenizer = new StringTokenizer(uri, "/");
		String servletName = uriTokenizer.nextToken();
		return servletName = uriTokenizer.nextToken();
	}

	@Override
	public boolean isAuthenticated(HttpRequest request) {
		// TODO Auto-generated method stub
		return true;
	}
	
	
	

}
