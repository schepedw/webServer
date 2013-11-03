package server;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;

public class PluginLoader {

	public HashMap<String, PluginInterface> generateHash() {
		HashMap<String, PluginInterface> PluginInterfaceHash = new HashMap<String, PluginInterface>();
		File PluginFolder = new File("plugins");
		File[] PluginFiles = PluginFolder.listFiles();
		
		try {
			@SuppressWarnings("deprecation")
			URL PluginInterfaceURL = PluginFolder.toURL();
			URL[] urls = new URL[]{ PluginInterfaceURL };
			
			@SuppressWarnings("resource")
			ClassLoader loader = new URLClassLoader(urls);			
			
			for (File PluginFile : PluginFiles) {
				// Build the URL - strip PluginInterfaces\ and find PluginInterface.class
				String pack = PluginFile.toString().substring(8);
				String classURL = pack + ".Plugin";
				PluginInterface PluginInterface = (PluginInterface) loader.loadClass(classURL).newInstance();
				PluginInterfaceHash.put(pack, PluginInterface);
			}

		} catch (MalformedURLException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			System.out.println("CRITICAL - Error invalid PluginInterface detected!");
			// TODO: DISABLED FOR WATCHER TESTING
			// System.exit(-1);
		}
		
		return PluginInterfaceHash;
	}

}
