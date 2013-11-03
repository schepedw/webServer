package server;

import java.net.InetAddress;
import java.util.Date;

public class ClientInfo {
	private InetAddress IP;
	private Date startTime;
	private int request;
	private static int TIMEOUT_IN_MINS = 1;
	private static int REQUEST_COUNT_LIMIT = 10000;

	public ClientInfo(InetAddress ip) {
		this.IP = ip;
		this.startTime = new Date();
		this.request = 1;
	}

	public synchronized void incrementRequest() {
		this.request++;
	}

	public boolean isIP(InetAddress ip) {
		return this.IP.equals(ip);
	}

	public boolean isAnAttacker() {

		if (this.request > REQUEST_COUNT_LIMIT) {
			Date now = new Date();
			long difference = now.getTime() - this.startTime.getTime();
			return difference < TIMEOUT_IN_MINS * 60 * 1000;
		} else {
			return false;
		}
	}
}
