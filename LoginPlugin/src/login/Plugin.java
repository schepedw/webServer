package login;

import java.io.File;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import protocol.HttpRequest;
import protocol.HttpResponse;
import protocol.HttpResponseFactory;
import protocol.Protocol;
import server.PluginInterface;

public class Plugin implements PluginInterface {
	
	private boolean authenticated = false;

	@Override
	public void directRequest(HttpRequest request) {
		// This plugin is a simple demonstration of login, and doesnt do anything
	}

	@Override
	public boolean isAuthenticated(HttpRequest request) {
		System.out.println(authenticated);
		if(authenticated)
			return true;
		
		Map<String, String> header = request.getHeader();
		System.out.println(header);
		String authorization = header.get("authorization");
		if(authorization != null) {
			String encodedLogin = authorization.split(" ")[1];
			String decodedLogin = new String(DatatypeConverter.parseBase64Binary(encodedLogin));
			// SUPER SECURE LOGIN INFORMATION DO NOT READ LOL
			// We could use a db or like env vars, but I'm tired
			// This is super basic authentication
			if (decodedLogin.equals("rob:wagner")) {
				authenticated = true;
			}
		} 
		return authenticated;
	}

	@Override
	public HttpResponse getResponse() {
		File file = new File("web/super_secret.html");
		
		return HttpResponseFactory.create200OK(file, Protocol.CLOSE);
	}

}
