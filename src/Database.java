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

	private String url = "jdbc:sqlite:C:/Users/NickU/Documents/FreshNetServer/FreshNet.db";

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
	
		//TODO send item data to the server when a timer is created or deleted
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
		
		
		//message.executeUpdate();
		prepareMessages();

	}
	
	private void prepareMessages() throws SQLException {
		//Record  rotations
		connect();
		String[] storeIDs = new String[] { "10010010", "10010020", "10010030", "10010040", "10010050", "10010060",
				"10010070", "10010080", "10010090", "10020010", "10020020", "10020030", "20010010", "20010020",
				"20010030", "20010040", "20010050", "20010060", "20010070", "30010010", "30010020", "30010030" };
		for (int i = 0; i < 4000; i++) {
			int itemID = (int) (Math.random() * 25);
			// 25 items per store

			int year = 120;
			int month = (int) (Math.random() * 4);
			int day = (int) (Math.random() * 29);
			int hour = (int) (Math.random() * 24);
			int min = (int) (Math.random() * 60);
			int sec = (int) (Math.random() * 60);
			int nano = (int) (Math.random() * 60);

			Timestamp sysTime = new Timestamp(year, month, day, hour, min, sec, nano);
			int shelfSec = 1100 - (int) (Math.random() * 2200);
			int store = (int) (Math.random() * 21);
			
			String sql = "INSERT INTO RotationData (Store_ID, Item_ID, Mod_Time, Shelf_Time) VALUES (?, ?, ?, ?)";
			PreparedStatement message = connection.prepareStatement(sql);

			message.setInt(1, Integer.parseInt(storeIDs[store]));
			message.setInt(2, itemID);
			message.setString(3, sysTime.toString());
			message.setInt(4, shelfSec);
			
			message.executeUpdate();

		}

	}

}



