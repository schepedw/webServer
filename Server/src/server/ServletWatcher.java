package server;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

import java.io.IOException;
import java.nio.file.ClosedWatchServiceException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ServletWatcher {

	private ExecutorService service = Executors.newCachedThreadPool();
	private FileSystem fs;
	private WatchService ws;
	private Map<WatchKey, Path> keys;

	public ServletWatcher() {
		try {
			fs = FileSystems.getDefault();
			ws = fs.newWatchService();
			keys = new ConcurrentHashMap<>();
			register(fs.getPath("servlets"), keys, ws);
			
			service.submit(new ServletFolderScanner());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
		
	static void walk(Path root, final Map<WatchKey, Path> keys, final WatchService ws) throws IOException {
		Files.walkFileTree(root, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
				register(dir, keys, ws);
				return super.preVisitDirectory(dir, attrs);
			}
		});
	}

	static void register(Path dir, Map<WatchKey, Path> keys, WatchService ws) throws IOException {
		WatchKey key = dir.register(ws, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
		keys.put(key, dir);
	}
	
	public void stop() throws IOException {
		ws.close();
		service.shutdownNow();
	}
	
	@SuppressWarnings("unchecked")
	static <T> WatchEvent<T> cast(WatchEvent<?> event) {
		return (WatchEvent<T>) event;
	}
	
	private class ServletFolderScanner implements Runnable {
		
		@Override
		public void run() {
			while (Thread.interrupted() == false) {
				WatchKey key;
				try {
					key = ws.poll(10, TimeUnit.MILLISECONDS);
				} catch (InterruptedException | ClosedWatchServiceException e) {
					break;
				}
				if (key != null) {
					Path path = keys.get(key);
					for (WatchEvent<?> i : key.pollEvents()) {
						WatchEvent<Path> event = cast(i);
						WatchEvent.Kind<Path> kind = event.kind();
						Path name = event.context();
						Path child = path.resolve(name);

						if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
							// Walk the directory if multiple files added (like the whole servlet folder)
							if (Files.isDirectory(child, LinkOption.NOFOLLOW_LINKS)) {
								try {
									walk(child, keys, ws);
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}
					}
					if (key.reset() == false) {
						keys.remove(key);
						if (keys.isEmpty()) {
							break;
						}
					}
				}
			}
		}
		
	}
}
