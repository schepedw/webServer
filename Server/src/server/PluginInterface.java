package server;

import protocol.HttpRequest;

public interface PluginInterface {
	
	/**
	 * Direct request to the appropriate plugin
	 */
	public void directRequest(HttpRequest request);

	public boolean isAuthenticated();
}
