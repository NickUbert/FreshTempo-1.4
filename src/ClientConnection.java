import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.Timer;

/*
 * The ClientConnection class handles all server messages being sent for use on FreshNet
 * 
 */
public class ClientConnection {

	// Server IP should be static if it is hard coded here
	String serverIP = "18.217.139.63";

	// Port number should also be static if it is hard coded here
	int portNum = 5000;

	private static int downTimeSec = 0;
	private static int flushIndex = 0;

	private Socket socket;
	private DataOutputStream out;
	private String messageToServer;

	// Empty constructor
	public ClientConnection() throws UnknownHostException, IOException {

	}

	/*
	 * sendMessage checks if the server is up before trying to establish a socket
	 * and output connection. If the connection doesn't fail it writes out the
	 * string passed in as a parameter.
	 */
	public void sendMessage(String data) throws UnknownHostException, IOException {
		CurrentSession cs = new CurrentSession();

		// This makes sure that the sessionAddress has been appended already.
		if (!(data.contains("" + cs.getSessionAddress()))) {
			messageToServer = cs.getSessionAddress() + "$" + data;
		} else {
			messageToServer = data;
		}

		if (hostAvailabilityCheck()) {
			try {

				// Connect to socket and write out the message.
				socket = new Socket(serverIP, portNum);
				out = new DataOutputStream(socket.getOutputStream());
				out.writeUTF(messageToServer);
				out.writeUTF("");
				out.flush();

			} catch (UnknownHostException u) {

			}

		} else {
			// Send data to queue just in case when the message doesn't send to make sure
			// nothing gets lost on the initial crash.
			cs.setServerUp(false);

			addToDowntimeQueue(messageToServer);
		}

	}

	/*
	 * hostAvailabilityCheck tries to make a connection to the server. If the
	 * connection times out in 1 sec, then the server is flagged as being down and
	 * all messages trying to be sent will be collected and sent once the server is
	 * found to be up again. The server is checked every 10 refreshes after it has
	 * been flagged.
	 */
	public boolean hostAvailabilityCheck() {
		try {

			Socket s = new Socket();
			// Check if the server is up, wait 3/4 second. Might shorten this later.
			s.connect(new InetSocketAddress(serverIP, portNum), 750);
			boolean connected = s.isConnected();
			s.close();

			return connected;
		} catch (IOException ex) {
			return false;

		}
	}

	/*
	 * addToDownTimeQueue takes any string and adds it to the end of the
	 * downtimeQueue.
	 */
	public void addToDowntimeQueue(String message) {
		CurrentSession cs = new CurrentSession();
		cs.addToDowntimeQueue(message);
	}

	/*
	 * flushTimer is used to read through the current queue of pending messages. It
	 * reads in the raw text of a message, and sends them in a buffered style. I'm
	 * worried that if the server has extended downtime and all clients connect at
	 * the same time, data will be lost. Instead it generates a random number
	 * between 1-5 and and if it equals 5 it sends the message.
	 */
	Timer flushTimer = new Timer(1000, new ActionListener() {
		public void actionPerformed(ActionEvent ae) {
			CurrentSession cs = new CurrentSession();

			// flushIndex is 0 indexed.
			String message = cs.getDowntimeQueue().get(flushIndex);

			// Check if the current message is the last one.
			if (flushIndex == cs.getDowntimeQueue().size() - 1) {
				flushIndex = 0;
				cs.setCurrentlyFlushing(false);
				flushTimer.stop();

				try {
					sendMessage(message);
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				cs.getDowntimeQueue().clear();

			} else {
				int randInt = (int) ((Math.random() * 5) + 1);
				if (randInt == 5) {
					try {
						sendMessage(message);
					} catch (UnknownHostException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					flushIndex++;
				}

			}

		}

	});

	/*
	 * downTimeTimer is used to check the clients connection to the sever every 5
	 * min. If the connnection works, the timer is stopped and the flush timer
	 * begins.
	 */
	Timer downTimeTimer = new Timer(1000, new ActionListener() {
		public void actionPerformed(ActionEvent ae) {
			downTimeSec++;
			if (downTimeSec % 300 == 0) {
				if (hostAvailabilityCheck()) {
					CurrentSession cs = new CurrentSession();
					cs.setCurrentlyFlushing(true);
					cs.setServerUp(true);
					downTimeSec = 0;
					downTimeTimer.stop();
					flushTimer.start();
				}

			}
		}
	});

	public int getFlushIndex() {
		return flushIndex;

	}
}
