import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/*
 * The ClientConnection class handles all server messages being sent for use on FreshNet
 * 
 */
public class ClientConnection {

	// Server IP should be static if it is hard coded here
	String serverIP = "127.0.0.1";

	// Port number should also be static if it is hard coded here
	int portNum = 5000;

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
		if (!(data.contains("" + cs.getSessionAddress()))) {
			messageToServer = cs.getSessionAddress() + "$" + data;
		} else {
			messageToServer = data;
		}

		if (hostAvailabilityCheck()) {
			try {

				socket = new Socket(serverIP, portNum);

				out = new DataOutputStream(socket.getOutputStream());
				out.writeUTF(messageToServer);
				out.writeUTF("");
				out.flush();

			} catch (UnknownHostException u) {

			}

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
		CurrentSession cs = new CurrentSession();
		if (cs.getServerUp()) {
			try {
				// This lil stunt causes EOF exception for server
				Socket s = new Socket();
				s.connect(new InetSocketAddress(serverIP, portNum), 1000);
				boolean connected = s.isConnected();
				s.close();
				return connected;
			} catch (IOException ex) {

				cs.setServerUp(false);
				collectDowntimeMessages(messageToServer, false);

			}
		}
		return false;
	}

	/*
	 * collectDowntimeMessages is used to save the messages that need to be sent to
	 * the server and stores them in an ArrayList of strings. If the server is known
	 * to be down, analytics will call this method directly and will need a header
	 * added to include the sessionAddress number.
	 */
	public void collectDowntimeMessages(String message, boolean headerNeeded) {

		CurrentSession cs = new CurrentSession();
		if (headerNeeded) {
			message = cs.getSessionAddress() + "$" + message;
		}

		if (!cs.getDowntimeMessages().contains(message)) {
			cs.addToDowntimeMessages(message);
		}

	}

	/*
	 * flushOutDowntimeMessages is used to clear out all remaining messages to be
	 * sent, ideally should do this on exit since it will take a while to send each
	 * message so maybe force a restart once server is reconnected. After messages
	 * are sent, the collection is cleared to ensure that data wont be collected
	 * twice.
	 */
	public void flushOutDowntimeMessages() {
		CurrentSession cs = new CurrentSession();

		// This creates a loop that runs according.
		for (int i = 0; i < cs.getDowntimeMessages().size(); i++) {
			try {
				sendMessage(cs.getDowntimeMessages().get(i));
			} catch (UnknownHostException e) {

			} catch (IOException e) {

			}
		}
		if (hostAvailabilityCheck()) {
			cs.clearDowntimeMessages();
		}
	}

}
