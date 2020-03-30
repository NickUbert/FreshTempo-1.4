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
	public void recordItem(String itemName, int shelfSec) throws SQLException {
		String sql = "INSERT INTO Item (Store_ID, Item_Name, Mod_Time, Mod_Hour, Shelf_Time, Expired) VALUES (?, ?, ?, ?, ?, ?)";
		PreparedStatement message = connection.prepareStatement(sql);

		CurrentSession cs = new CurrentSession();

		Date date = new Date();
		Timestamp sysTime = new Timestamp(date.getTime());
		int storeID = cs.getSessionAddress();
		int sysHour = sysTime.getHours();
		boolean expired = shelfSec < 0;

		message.setInt(1, storeID);
		message.setString(2, itemName);
		message.setString(3, sysTime.toString());
		message.setInt(4, sysHour);
		message.setInt(5, shelfSec);
		message.setBoolean(6, expired);
		
		message.executeUpdate();

	}

}
