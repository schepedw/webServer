package server;

import protocol.HttpRequest;

public interface Plugin {
	
	/**
	 * Direct request to the appropriate plugin
	 */
	public void directRequest(HttpRequest request);
}
