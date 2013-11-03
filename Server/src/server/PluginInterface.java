package server;

import protocol.HttpRequest;
import protocol.HttpResponse;

public interface PluginInterface {
	
	/**
	 * Direct request to the appropriate plugin
	 */
	public void directRequest(HttpRequest request);

	/**
	 * The plugin MUST implement its own authentication login, return true if you want
	 * to ignore authentication
	 * @param request
	 */
	public boolean isAuthenticated(HttpRequest request);
	
	public HttpResponse getResponse();
}
