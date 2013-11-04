package plugin;

import java.io.File;
import java.sql.*;

import protocol.HttpRequest;
import protocol.HttpResponse;
import protocol.HttpResponseFactory;
import protocol.Protocol;
import server.PluginInterface;

public class Plugin implements PluginInterface {

	@Override
	public void directRequest(HttpRequest request) {
		Connection c = null;
		Statement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:test.db");
			stmt = c.createStatement();

			String sql = "INSERT INTO t1 VALUES (NULL, '" + request.getBody() + "');";
			stmt.execute(sql);
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		System.out.println("Successful operation");
		
		
		
	}

	@Override
	public boolean isAuthenticated(HttpRequest request) {
		return true;
	}

	@Override
	public HttpResponse getResponse() {
		File file = new File("Server/web/success.html");
		return HttpResponseFactory.create200OK(file, Protocol.CLOSE);
	}

}
