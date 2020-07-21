import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class Database {

	// TODO make connection dynamic so that everyone isnt connecting as admin
	private String url = "jdbc:mysql://freshnet.cjcf0bgozlin.us-east-2.rds.amazonaws.com:3306/freshnet?user=admin&password=Tempo2019";

	private Connection connection;

	public Database() {

	}

	public boolean isConnected() {
		return connection != null;
	}

	public void connect() throws SQLException {
		connection = DriverManager.getConnection(url);
	}

	public void disconnect() throws SQLException {
		connection.close();
	}

	public ResultSet runQuery(String query) throws SQLException {
		PreparedStatement stmt = connection.prepareStatement(query);
		ResultSet results = stmt.executeQuery();
		return results;
	}

	public void addStore() {
	}

	@SuppressWarnings("deprecation")
	public void recordItem(int itemID, int shelfSec) throws SQLException {
		connect();
		String sql = "INSERT INTO RotationData (Store_ID, Item_ID, Mod_Time, Shelf_Time, Emp_Initials) VALUES (?, ?, ?, ?, ?)";
		PreparedStatement message = connection.prepareStatement(sql);

		CurrentSession cs = new CurrentSession();

		Date date = new Date();
		Timestamp sysTime = new Timestamp(date.getTime());
		int storeID = cs.getSessionAddress();

		message.setInt(1, storeID);
		message.setInt(2, itemID);
		message.setString(3, sysTime.toString());
		message.setInt(4, shelfSec);
		message.setString(5, "n/a");

		message.executeUpdate();
		disconnect();
	}

	// Overloaded method for initials
	public void recordItem(int itemID, int shelfSec, String initials) throws SQLException {
		connect();
		String sql = "INSERT INTO RotationData (Store_ID, Item_ID, Mod_Time, Shelf_Time, Emp_Initials) VALUES (?, ?, ?, ?, ?)";
		PreparedStatement message = connection.prepareStatement(sql);

		CurrentSession cs = new CurrentSession();

		Date date = new Date();
		Timestamp sysTime = new Timestamp(date.getTime());
		int storeID = cs.getSessionAddress();

		message.setInt(1, storeID);
		message.setInt(2, itemID);
		message.setString(3, sysTime.toString());
		message.setInt(4, shelfSec);
		message.setString(5, initials);

		message.executeUpdate();
		disconnect();
	}

	// Record new item is used when a store adds a timer themselves, the item is
	// recorded and sent to the database. Used for shelf timers
	public void recordNewItem(int itemID, String itemName, int shelfSec, boolean initialsRequired) throws SQLException {
		connect();
		String sql = "INSERT INTO ItemData (Store_ID, Item_ID, Item_Name, Shelf_String, Emp_Initials, Scheduled) VALUES (?, ?, ?, ?, ?, ?)";
		PreparedStatement message = connection.prepareStatement(sql);

		CurrentSession cs = new CurrentSession();
		int storeID = cs.getSessionAddress();
		int initialsInt = 0;
		if (initialsRequired) {
			initialsInt = 1;
		}

		message.setInt(1, storeID);
		message.setInt(2, itemID);
		message.setString(3, itemName);
		message.setString(4, "" + shelfSec);
		message.setInt(5, initialsInt);
		message.setInt(6, 0);

		message.executeUpdate();
		disconnect();
	}

	// used for scheduled timers
	public void recordNewTask(int itemID, String itemName, ArrayList<Time> deadlines) throws SQLException {
		connect();
		String sql = "INSERT INTO ItemData (Store_ID, Item_ID, Item_Name, Shelf_String, Emp_Initials, Scheduled) VALUES (?, ?, ?, ?, ?, ?)";
		PreparedStatement message = connection.prepareStatement(sql);

		CurrentSession cs = new CurrentSession();
		int storeID = cs.getSessionAddress();

		String shelfString = "";
		for (int i = 0; i < deadlines.size() - 1; i++) {
			shelfString += deadlines.get(i).toString() + ",";
		}
		shelfString += deadlines.get(deadlines.size() - 1);

		message.setInt(1, storeID);
		message.setInt(2, itemID);
		message.setString(3, itemName);
		message.setString(4, shelfString);
		message.setInt(5, 1);
		message.setInt(6, 1);
		message.executeUpdate();
		disconnect();
	}

}
