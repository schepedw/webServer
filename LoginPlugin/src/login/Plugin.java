package login;

import protocol.HttpRequest;
import server.PluginInterface;

public class Plugin implements PluginInterface {

	@Override
	public void directRequest(HttpRequest request) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isAuthenticated() {
		return true;
	}

}
