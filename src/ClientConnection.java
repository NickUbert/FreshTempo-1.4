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

				// Send the last message to the server.
				try {
					sendMessage(message);
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				// Empty the arrayList because the loops rely on the arraylist's size and not
				// total index.
				cs.getDowntimeQueue().clear();

			} else {
				// If the entry is not the last one in the queue, generate a random number for
				// the buffer.
				int randInt = (int) ((Math.random() * 5) + 1);

				if (randInt == 5) {
					// If it passes the buffer check, send the message to the server.
					try {
						sendMessage(message);
					} catch (UnknownHostException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					// Increment the current index of the flush.
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
			// increment the seconds being used for the downtimeTimer
			downTimeSec++;

			// If 300 seconds have passed since the last check, test the host connection.
			if (downTimeSec % 300 == 0) {
				
				//If the host is connected.
				if (hostAvailabilityCheck()) {
					CurrentSession cs = new CurrentSession();
					//Update the session variables
					cs.setCurrentlyFlushing(true);
					cs.setServerUp(true);
					//reset the downtime seconds for next downtime.
					downTimeSec = 0;
					//Stop the checking process and start the flushing process.
					downTimeTimer.stop();
					flushTimer.start();
				}

			}
		}
	});

	/*
	 * getFlushIndex is simply used to get the current index of the data entry being
	 * flushed to the server.
	 */
	public int getFlushIndex() {
		return flushIndex;

	}
}
