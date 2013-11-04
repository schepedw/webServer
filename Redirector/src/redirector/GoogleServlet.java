package redirector;
import server.Servlet;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class GoogleServlet implements Servlet {

	@Override
	public void doGet() {
		if(Desktop.isDesktopSupported())
		{
		  try {
			Desktop.getDesktop().browse(new URI("http://www.google.com"));
		} catch (IOException | URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}

	}

	@Override
	public void doPut() {
		// Will not do
		
	}

	@Override
	public void doPost() {
		if(Desktop.isDesktopSupported())
		{
		  try {
			Desktop.getDesktop().browse(new URI("http://www.google.com/search?q="+Plugin.body));
			System.out.println("DO I RUN!");
		} catch (IOException | URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	}

	@Override
	public void doDelete() {
		// Will not do
		
	}

}
