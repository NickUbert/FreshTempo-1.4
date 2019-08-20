import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

public class ClientConnection {

	String serverIP = "192.168.1.73";

	int portNum = 5000;

	private Socket socket;
	private DataOutputStream out;
	private String messageToServer;

	public ClientConnection() throws UnknownHostException, IOException {

	}

	public void sendMessage(String data) throws UnknownHostException, IOException {
		CurrentSession cs = new CurrentSession();
		messageToServer = cs.getSessionAddress() + "$" + data;

		if (hostAvailabilityCheck()) {
			try {
				socket = new Socket(serverIP, portNum);

				out = new DataOutputStream(socket.getOutputStream());

			} catch (UnknownHostException u) {

			}
			try {

				out.writeUTF(messageToServer);
				out.flush();

			} catch (IOException i) {

			}
		}

	}

	public boolean hostAvailabilityCheck() {
		CurrentSession cs = new CurrentSession();
		if (cs.getServerUp()) {
			try {
				@SuppressWarnings("resource")
				Socket s = new Socket();
				s.connect(new InetSocketAddress(serverIP, portNum), 1000);
				return s.isConnected();
			} catch (IOException ex) {
				cs.setServerUp(false);
				collectDowntimeMessages(messageToServer, false);
			}
		}
		return false;
	}

	public void collectDowntimeMessages(String message, boolean headerNeeded) {
		CurrentSession cs = new CurrentSession();
		if (headerNeeded) {
			message = cs.getSessionAddress() + "$" + message;
		}

		if (!cs.getDowntimeMessages().contains(message)) {
			cs.addToDowntimeMessages(message);
		}

	}

	public void flushOutDowntimeMessages() {
		CurrentSession cs = new CurrentSession();
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
