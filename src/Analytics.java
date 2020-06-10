import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Analytics class is used for data collection and crash logs. In the future
 * this should make data available to end users using graphics.
 * 
 * usageData records data in the format:
 * MM/DD/YY[space]hh:mm:ss$expiredBooleanInteger$title$hour$min$sec
 * [space]represents an actual space, see recordTimeData for reference
 * 
 */
public class Analytics {
	private static int numOfExpirations = 0;
	@SuppressWarnings("unused")
	private static int sessionDays = 0;
	private static String holderDate;
	private ArrayList<String> expiredTitles = new ArrayList<String>();
	private ArrayList<String> expiredDates = new ArrayList<String>();
	private ArrayList<String> expiredTimes = new ArrayList<String>();

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
	 * getAverageExpirationTime is used to read in all the info from text files and
	 * then calculate the average time, and return the formatted time. in the future
	 * when more info is being collected by the analytics class, this method will
	 * need to be divided into the constructor so that you dont have to call methods
	 * in a certain order.
	 * 
	 * If you ever want to flex, replace this perfectly fine and working code with
	 * recursion.
	 */
	@SuppressWarnings("resource")
	public String getAverageExpirationTime() throws IOException {
		// Set default values.
		numOfExpirations = 0;
		String finalMessage = "";
		int totalHour = 0;
		int totalMin = 0;
		int totalSec = 0;

		FileReader fr = new FileReader("./usageData.txt");
		BufferedReader br = new BufferedReader(fr);
		ArrayList<String> priorData = new ArrayList<String>();

		// Add text from usage data to a string ArrayList.
		String curString = br.readLine();
		while (curString != null) {
			priorData.add(curString);
			curString = br.readLine();
		}

		// Make sure theres something to be averaged.
		if (priorData.size() != 0) {

			for (int i = 0; i < priorData.size(); i++) {

				// Line is the entire line of text
				String line = priorData.get(i);
				// info is the important info that follows the formatted date.
				String info = line.substring(line.indexOf("$") + 1);

				// Seperate the date.
				String dateAndTime = line.substring(0, line.indexOf("$"));
				if (i == 0) {
					holderDate = dateAndTime;
				}

				// GapIndex is the position of the '$' seperator. Important becuase lenghts of
				// data being collected can vary.
				int gapIndex = info.indexOf("$");

				String expireString = info.substring(0, gapIndex);
				// '1' would be the expired value if it was true.
				if (Integer.parseInt(expireString) == 1) {
					numOfExpirations++;
					// Set a new info string to begin collecting the expired timers data.
					info = info.substring(gapIndex + 1);

					// Update gap index of '$'
					gapIndex = info.indexOf("$");

					// Collect the expirations time and date.
					String date = dateAndTime.substring(0, dateAndTime.indexOf(" "));
					expiredDates.add(date);

					// Collect the title of expired timer.
					String title = info.substring(0, gapIndex);
					expiredTitles.add(title);

					// update info string and gap index.
					info = info.substring(gapIndex + 1);
					gapIndex = info.indexOf("$");

					// Collect hour text as a string.
					String hourString = info.substring(0, gapIndex);

					// Update info string and gap index.
					info = info.substring(gapIndex + 1);
					gapIndex = info.indexOf("$");

					// Collect min text as a string
					String minString = info.substring(0, gapIndex);

					// Update info string and record the rest as the seconds text.
					info = info.substring(gapIndex + 1);
					String secString = info.substring(0);

					// Collect int values from the stored text.
					int sec = Integer.parseInt(secString);
					int min = Integer.parseInt(minString);
					int hour = Integer.parseInt(hourString);

					// Add to running total.
					totalHour += hour;
					totalMin += min;
					totalSec += sec;

					// Use the timeValueToString to get a formatted time value.
					ItemTimer it = new ItemTimer();
					String timeString = it.timeValueToString(sec, min, hour);
					expiredTimes.add(timeString);

				}

			}

			// Create the average integers;
			int avgHour = 0;
			int avgMin = 0;
			int avgSec = 0;
			// Prevents dividing errors
			if (numOfExpirations != 0) {
				avgHour = totalHour / numOfExpirations;
				avgMin = totalMin / numOfExpirations;
				avgSec = totalSec / numOfExpirations;
			}
			// Formatted string of average expiration time.
			String avgTime = "-" + Math.abs(avgHour) + ":" + Math.abs(avgMin) + ":" + Math.abs(avgSec);

			finalMessage = avgTime;
		}
		return finalMessage;
	}

	/*
	 * getSessionLength is used to return the integer value of days past since the
	 * first refresh was recorded.
	 */
	public int getSessionLength() {
		return getDayCount(holderDate);
	}

	/*
	 * getDayCount is used to generate an int that represents how many days have
	 * passed since the first timer was recorded. It must be called after
	 * getAverageExpirationTime since it requires certain data to be collected
	 * before it finds the session length.
	 */
	private int getDayCount(String dateAndTimeString) {
		// Start by breaking off the date and time from the rest of the string read in
		// from usage data.
		String startDateString = dateAndTimeString.substring(0, dateAndTimeString.indexOf(" "));

		// Break the line up into month day and year strings that are separated by '/'.
		String monthString = startDateString.substring(0, startDateString.indexOf("/"));
		startDateString = startDateString.substring(startDateString.indexOf("/") + 1);
		String dayString = startDateString.substring(0, startDateString.indexOf("/"));
		String yearString = startDateString.substring(startDateString.indexOf("/") + 1);

		// Generate the current date and time as a string and break it up into day month
		// and year since it is formatted and seperated by '/'.
		String curDateAndTime = getDateAndTime();
		String curDateString = curDateAndTime.substring(0, curDateAndTime.indexOf(" "));
		String curMonthString = curDateString.substring(0, curDateString.indexOf("/"));
		curDateString = curDateString.substring(curDateString.indexOf("/") + 1);
		String curDayString = curDateString.substring(0, curDateString.indexOf("/"));
		String curYearString = curDateString.substring(curDateString.indexOf("/") + 1);

		// Pull the ints from the collected strings.
		int startMonth = Integer.parseInt(monthString);
		int startDay = Integer.parseInt(dayString);
		int startYear = Integer.parseInt(yearString);

		int curMonth = Integer.parseInt(curMonthString);
		int curDay = Integer.parseInt(curDayString);
		int curYear = Integer.parseInt(curYearString);

		int elapsedYears = curYear - startYear;
		int elapsedMonths = curMonth - startMonth;
		int elapsedDays = curDay - startDay;

		// Calculate elapsed days.
		int days = (elapsedDays + (elapsedMonths * 30) + (elapsedYears * 365));

		return days;
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
		return "" + numOfLines;
	}

	/*
	 * recordTimeData is used to write the timer's refresh info to the usageData
	 * text file.
	 */

	public void recordTimeData(ItemTimer it) throws IOException {

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
		String dataToWrite = date + "$" + expired + "$" + title + "$" + recordHour + "$" + recordMin + "$" + recordSec;
		bw.append(dataToWrite);
		bw.newLine();

		bw.flush();
		bw.close();

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

		return rotationTitles;
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
			while (sep != 3) {
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

		return rotationTimes;
	}
}
