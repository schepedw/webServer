package server;

import java.net.InetAddress;
import java.util.ArrayList;

public class Blacklist {
	private ArrayList<InetAddress> list;

	public Blacklist() {
		this.list = new ArrayList<InetAddress>();
		// for testing purpose, manually adding ip that should be blacklisted
		/*
		 * try {
		 * list.add(Inet4Address.getByAddress(getBytesFromAddress("10.0.0.5")));
		 * this.log.info("Added 10.0.0.5 to the blacklist"); } catch
		 * (UnknownHostException e) { e.printStackTrace(); }
		 */
	}

	public boolean isBlacklisted(InetAddress check) {
		if (list.contains(check))
			return true;
		return false;
	}

	public void addAddressToBlacklist(InetAddress add) {
		list.add(add);
	}
}
