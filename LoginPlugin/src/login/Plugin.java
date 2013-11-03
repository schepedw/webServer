package login;

import java.util.Map;

import protocol.HttpRequest;
import protocol.Protocol;
import server.PluginInterface;

public class Plugin implements PluginInterface {
	
	private boolean authenticated = false;

	@Override
	public void directRequest(HttpRequest request) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isAuthenticated(HttpRequest request) {
		Map<String, String> header = request.getHeader();
		System.out.println(header.get(Protocol.AUTHORIZATION));
		return false;
	}

}
