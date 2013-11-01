package server;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;

public class ServletLoader {

	public HashMap<String, ServletInterface> generateHash() {
		HashMap<String, ServletInterface> servletHash = new HashMap<String, ServletInterface>();
		File servletsFolder = new File("servlets");
		File[] servletFiles = servletsFolder.listFiles();
		
		try {
			@SuppressWarnings("deprecation")
			URL servletURL = servletsFolder.toURL();
			URL[] urls = new URL[]{ servletURL };
			
			@SuppressWarnings("resource")
			ClassLoader loader = new URLClassLoader(urls);			
			
			for (File servletFile : servletFiles) {
				// Build the URL - strip servlets\ and find servlet.class
				String pack = servletFile.toString().substring(8);
				String classURL = pack + ".servlet";
				ServletInterface servlet = (ServletInterface) loader.loadClass(classURL).newInstance();
				servletHash.put(pack, servlet);
			}

		} catch (MalformedURLException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			System.out.println("CRITICAL - Error loading servlets folder! Exiting");
			System.exit(-1);
		}
		
		return servletHash;
	}

}
