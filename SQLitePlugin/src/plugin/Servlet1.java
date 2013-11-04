package plugin;

import protocol.HttpResponse;
import protocol.HttpResponseFactory;
import server.Servlet;
import java.sql.*;

public class Servlet1 implements Servlet {
	
	public Servlet1() {
		System.out.println("stuff");
		Connection c = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:test.db");
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		System.out.println("Opened database successfully");
	}

	@Override
	public void doGet() {
		//HttpResponse response = HttpResponseFactory.create200OK(null, connection);
	}

	@Override
	public void doPut() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doPost() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doDelete() {
		// TODO Auto-generated method stub
		
	}

}
