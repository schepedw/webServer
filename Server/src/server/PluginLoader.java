package server;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;

public class PluginLoader {

	public HashMap<String, Plugin> generateHash() {
		HashMap<String, Plugin> pluginHash = new HashMap<String, Plugin>();
		File pluginsFolder = new File("plugins");
		File[] pluginFiles = pluginsFolder.listFiles();
		
		try {
			@SuppressWarnings("deprecation")
			URL pluginURL = pluginsFolder.toURL();
			URL[] urls = new URL[]{ pluginURL };
			
			@SuppressWarnings("resource")
			ClassLoader loader = new URLClassLoader(urls);			
			
			for (File pluginFile : pluginFiles) {
				// Build the URL - strip plugins\ and find Plugin.class
				String pack = pluginFile.toString().substring(8);
				String classURL = pack + ".Plugin";
				Plugin plugin = (Plugin) loader.loadClass(classURL).newInstance();
				pluginHash.put(pack, plugin);
			}

		} catch (MalformedURLException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			System.out.println("CRITICAL - Error invalid plugin detected!");
			// TODO: DISABLED FOR WATCHER TESTING
			// System.exit(-1);
		}
		
		return pluginHash;
	}

}
