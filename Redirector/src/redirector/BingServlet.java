package redirector;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import server.Servlet;


public class BingServlet implements Servlet {

	@Override
	public void doGet() {
		if(Desktop.isDesktopSupported())
		{
		  try {
			Desktop.getDesktop().browse(new URI("http://www.bing.com"));
		} catch (IOException | URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	}

	@Override
	public void doPut() {
		//will not do
	}

	@Override
	public void doPost() {
		if(Desktop.isDesktopSupported())
		{
		  try {
			Desktop.getDesktop().browse(new URI("http://www.bing.com/search?q="+ Plugin.body));
		} catch (IOException | URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	}

	@Override
	public void doDelete() {
		//will not do
	}

}
