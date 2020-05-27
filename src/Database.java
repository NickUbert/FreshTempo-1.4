import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Database {

	//TODO make connection dynamic so that everyone isnt connecting as admin
	private String url = "jdbc:mysql://freshnet.cjcf0bgozlin.us-east-2.rds.amazonaws.com:3306/freshnet?user=admin&password=Tempo2019";

	private Connection connection;

	public Database() {

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

	@SuppressWarnings("deprecation")
	public void recordItem(int itemID, int shelfSec) throws SQLException {

		// TODO send item data to the server when a timer is created or deleted
		String sql = "INSERT INTO RotationData (Store_ID, Item_ID, Mod_Time, Shelf_Time) VALUES (?, ?, ?, ?)";
		PreparedStatement message = connection.prepareStatement(sql);

		CurrentSession cs = new CurrentSession();

		Date date = new Date();
		Timestamp sysTime = new Timestamp(date.getTime());
		int storeID = cs.getSessionAddress();

		message.setInt(1, storeID);
		message.setInt(2, itemID);
		message.setString(3, sysTime.toString());
		message.setInt(4, shelfSec);

		message.executeUpdate();

	}

	// Record new item is used when a store adds a timer themselves, the item is
	// recorded and sent to the database.
	public void recordNewItem(int itemID, String itemName, int shelfSec) throws SQLException {
		connect();

		String sql = "INSERT INTO ItemData (Store_ID, Item_ID, Item_Name, Shelf_Life) VALUES (?, ?, ?, ?)";
		PreparedStatement message = connection.prepareStatement(sql);

		CurrentSession cs = new CurrentSession();
		int storeID = cs.getSessionAddress();

		message.setInt(1, storeID);
		message.setInt(2, itemID);
		message.setString(3, itemName);
		message.setInt(4, shelfSec);

		message.executeUpdate();
		// TODO disconnect
	}

}
