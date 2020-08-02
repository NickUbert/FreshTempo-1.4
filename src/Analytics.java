import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Analytics class is used for data collection and crash logs.
 * 
 * usageData records data in the format:
 * MM/DD/YY[space]hh:mm:ss$expiredBooleanInteger$title$hour$min$sec
 * [space]represents an actual space, see recordTimeData for reference
 * 
 */
public class Analytics {
	
	/*
	 * getDateAndTime is used to return the current date and time in a proper
	 * format.
	 */
	public String getDateAndTime() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yy HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();

		return dtf.format(now);
	}
	
	/*
	 * getNumberOfExpirations returns the number of timers refreshed past their
	 * expirations. This must also be called after average time calc.
	 */
	public String getNumberOfRotations() throws IOException {
		FileReader fw = new FileReader("./usageData.txt");
		BufferedReader bw = new BufferedReader(fw);
		int numOfLines = 0;
		String temp = bw.readLine();
		while (temp != null) {
			numOfLines++;
			temp = bw.readLine();
		}
		bw.close();
		return "" + numOfLines;
	}

	/*
	 * recordTimeData is used to write the timer's refresh info to the usageData
	 * text file.
	 */

	public void recordTimeData(ItemTimer it, String initials) throws IOException {

		FileWriter fw = new FileWriter("./usageData.txt", true);
		BufferedWriter bw = new BufferedWriter(fw);

		// Collect data from the itemTimer used to call this method.
		String title = it.getTitle();
		int recordHour = it.getCurHour();
		int recordMin = it.getCurMin();
		int recordSec = it.getCurSec();
		String date = getDateAndTime();

		// Set the integer boolean value for the timers expiration.
		int expired = 0;
		if (recordHour < 0 || recordMin < 0 || recordSec < 0) {
			expired = 1;
		}

		// Write the new line.
		String dataToWrite = date + "$" + expired + "$" + title + "$" + initials + "$" + recordHour + "$" + recordMin
				+ "$" + recordSec;
		bw.append(dataToWrite);
		bw.newLine();

		bw.flush();
		bw.close();

		// TODO FLUSH FILE
		/*
		 * // After recording data, send to server if the client is connected.
		 * CurrentSession cs = new CurrentSession(); if (cs.getClientConnected()) {
		 * ClientConnection cc = new ClientConnection(); // Check if server is up if
		 * (cs.getServerUp()) { if (cc.hostAvailabilityCheck()) { cs.setServerUp(true);
		 * // If a flush is taking place, this entry gets added to the end of the queue.
		 * if (cs.getCurrentlyFlushing()) { cc.addToDowntimeQueue(dataToWrite); } else {
		 * // If a flush isn't happening but the queue has data, this starts it. if
		 * (!(cs.getDowntimeQueue().size() == 0)) { cc.addToDowntimeQueue(dataToWrite);
		 * cs.setCurrentlyFlushing(true); if (!(cc.flushTimer.isRunning())) {
		 * cc.flushTimer.start(); } // Otherwise the message is simply sent to the
		 * server. } else { cc.sendMessage(dataToWrite); } } } else {
		 * cs.setServerUp(false); } } else { cs.setServerUp(false); // If the queue is
		 * empty while the server is down, this must be the first entry // during
		 * downtime so the timer is started.
		 * 
		 * cc.addToDowntimeQueue(dataToWrite); if (cs.getDowntimeQueue().size() == 0) {
		 * cc.downTimeTimer.start(); } }
		 * 
		 * }
		 */

	}

	/*
	 * generateCrashLog writes the session data to the crashlog text file.
	 */
	public void generateCrashLog() throws IOException {

		FileWriter fw = new FileWriter("./CRASHLOG.txt");
		BufferedWriter bw = new BufferedWriter(fw);

		String currentDateAndTime = getDateAndTime();

		// Set strings used for the crashlog message.
		String crashLine0 = "FRESHTEMPO CRASHLOG:";
		String crashLine1 = "The day and time of the software exiting was: " + currentDateAndTime;

		String crashLine2 = "If this exit was not intended, we apologize for any inconvenience that this has caused your establishment.";
		String crashLine3 = "You can attempt to reload the software and resume where you left off. If this does not work we would advise you ";
		String crashLine4 = "to record the times listed below to manually monitor your expirations until the system is working again. If ";
		String crashLine5 = "these problems persist, please contact Badger Software support at any of the following options: ";
		String crashLine6 = "badger.software";
		String crashLine7 = "support@badger.software";
		String crashLine8 = "262 345 4995 ";
		String crashLine9 = "Thank you again for your patience.";
		String crashLine10 = "Timers recorded at system exit (" + currentDateAndTime + "):";

		// Write each string to the crashLog.
		bw.write(crashLine0);
		bw.newLine();
		bw.newLine();
		bw.write(crashLine1);
		bw.newLine();
		bw.write(crashLine2);
		bw.newLine();
		bw.write(crashLine3);
		bw.newLine();
		bw.write(crashLine4);
		bw.newLine();
		bw.write(crashLine5);
		bw.newLine();
		bw.newLine();
		bw.write(crashLine6);
		bw.newLine();
		bw.write(crashLine7);
		bw.newLine();
		bw.write(crashLine8);
		bw.newLine();
		bw.newLine();
		bw.write(crashLine9);
		bw.newLine();
		bw.write(crashLine10);
		bw.newLine();
		bw.newLine();

		// After the crashlog message was written, write the timer info for each
		// itemTimer to the log.
		CurrentSession cs = new CurrentSession();
		for (int timerID = 0; timerID < cs.getTNOT(); timerID++) {
			if (CurrentSession.itHash.get(timerID) != null) {
				String title = CurrentSession.itHash.get(timerID).getTitle();
				String time = CurrentSession.itHash.get(timerID).getCurTimeString();
				bw.write(" " + title + ": " + time);
				bw.newLine();
			}
		}
		bw.flush();
		bw.close();

	}

	/*
	 * getExpiredTitles is used to return the arrayList of expired titles.
	 */
	public ArrayList<String> getRotationTitles() throws IOException {
		FileReader fr = new FileReader("./usageData.txt");
		BufferedReader br = new BufferedReader(fr);
		ArrayList<String> rotationTitles = new ArrayList<String>();

		String temp = br.readLine();
		while (temp != null) {
			int sep = 0;
			while (sep != 2) {
				if (temp.charAt(0) == '$') {
					sep++;
				}
				temp = temp.substring(1);
			}
			rotationTitles.add(temp.substring(0, temp.indexOf('$')));
			temp = br.readLine();
		}
		br.close();
		return rotationTitles;
	}
	
	/*
	 * getExpiredTitles is used to return the arrayList of expired titles.
	 */
	public ArrayList<String> getRotationInitilas() throws IOException {
		FileReader fr = new FileReader("./usageData.txt");
		BufferedReader br = new BufferedReader(fr);
		ArrayList<String> rotationInitials = new ArrayList<String>();

		String temp = br.readLine();
		while (temp != null) {
			int sep = 0;
			while (sep != 3) {
				if (temp.charAt(0) == '$') {
					sep++;
				}
				temp = temp.substring(1);
			}
			rotationInitials.add(temp.substring(0, temp.indexOf('$')));
			temp = br.readLine();
		}
		br.close();
		return rotationInitials;
	}

	/*
	 * getExpiredDates is used to return the arrayList of expired dates.
	 */
	public ArrayList<String> getRotationDates() throws IOException {
		FileReader fr = new FileReader("./usageData.txt");
		BufferedReader br = new BufferedReader(fr);
		ArrayList<String> rotationDates = new ArrayList<String>();

		String temp = br.readLine();
		while (temp != null) {

			rotationDates.add(temp.substring(0, temp.indexOf('$')));
			temp = br.readLine();
		}
		br.close();
		return rotationDates;
	}

	/*
	 * getExpiredTimes is used to return the arrayList of expiredTimes.
	 */
	public ArrayList<String> getRotationTimes() throws IOException {
		FileReader fr = new FileReader("./usageData.txt");
		BufferedReader br = new BufferedReader(fr);
		ArrayList<String> rotationTimes = new ArrayList<String>();

		String temp = br.readLine();
		while (temp != null) {
			int sep = 0;
			while (sep != 4) {
				if (temp.charAt(0) == '$') {
					sep++;
				}
				temp = temp.substring(1);
			}
			String hourString = temp.substring(0, temp.indexOf('$'));
			String minString = temp.substring(temp.indexOf('$') + 1, temp.lastIndexOf('$'));
			String secString = temp.substring(temp.lastIndexOf('$') + 1);

			int sec = Integer.parseInt(secString);
			int min = Integer.parseInt(minString);
			int hour = Integer.parseInt(hourString);
			ItemTimer it = new ItemTimer();
			String timeString = it.timeValueToString(sec, min, hour);
			rotationTimes.add(timeString);

			temp = br.readLine();
		}
		br.close();
		return rotationTimes;
	}
}
