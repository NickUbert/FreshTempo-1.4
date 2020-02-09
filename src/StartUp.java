import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * StartUp Class contains the main method of the program and is responsible for
 * a lot of initial setup for the software. It also handles the loading and
 * saving of timers to allow continious use.
 * 
 * Author: @nu Last Modified: 8/9/19
 * 
 */
public class StartUp {

	// Jframes and screen dimensions
	static JFrame window;
	static JFrame loadingWindow;
	private static Dimension mtk = Toolkit.getDefaultToolkit().getScreenSize();

	// Hashmaps
	static HashMap<Integer, ItemTimer> ItemTimerHash = new HashMap<Integer, ItemTimer>();
	static public HashMap<Integer, JPanel> backgroundHash = new HashMap<Integer, JPanel>();

	// Bounds
	private static final int screenX = ((int) mtk.getWidth());
	private static final int screenY = ((int) mtk.getHeight());
	private final static int flowGapTabH = (int) (.02329 * screenX) + 1;
	private final static int flowGapTabV = (int) (.02 * screenY) + 1;
	private final static int flowGapCardH = (int) (.01329 * screenX) + 1;
	private final static int flowGapCardV = (int) (.00997 * screenY) + 1;
	private static int keyboardXGap = (int) (.0026 * screenX);
	private static int keyboardYGap = (int) (.0074 * screenY);
	static private int keyboardPanelXL = (int) (.00625 * screenX);
	static private int keyboardPanelYL = (int) (.29167 * screenY);
	static private int keyboardCurve = (int) (.00005 * screenX);
	static private int keyboardX = (int) (.9855 * screenX);
	static private int keyboardY = (int) (.585 * screenY);
	static private int optionXL = (int) (.2575 * screenX);
	static private int optionYL = (int) (.06 * screenY);
	static private int optionY = (int) (.737 * screenY);
	static private int optionX = (int) (.45 * screenX);
	static private int taskGap = (int) (.1 * screenY);
	static FlowLayout cardLayout = new FlowLayout(FlowLayout.CENTER, flowGapCardH, flowGapCardV);
	static FlowLayout tabLayout = new FlowLayout(FlowLayout.CENTER, flowGapTabH, flowGapTabV);

	// Number of background panels available.
	private static int windowCount = 50;

	// Graphic components including jpanels with rounded corners.
	static Color backgroundColor = Color.decode("#223843");

	// Main Keyboard with rounded corners element
	static RoundedPanel mainKeyboard = new RoundedPanel();

	// Option Panel with rounded corners element
	static JPanel optionPanel = new RoundedPanel();

	public static void main(String[] args) throws IOException {

		// Create file paths
		File crashData = new File("./CRASHLOG.txt");
		File usageData = new File("./usageData.txt");
		File timerData = new File("./savedTimerData.txt");
		File flushData = new File("./flushFile.txt");

		// Check if prior sessions have already created the files
		if (!crashData.exists()) {
			crashData.createNewFile();
		}
		if (!usageData.exists()) {
			usageData.createNewFile();
		}
		if (!timerData.exists()) {
			timerData.createNewFile();
		}
		if (!flushData.exists()) {
			flushData.createNewFile();
		}

		// Create the program's fullscren window but not opening it yet.
		window = new JFrame();
		window.setLayout(null);
		window.setExtendedState(Frame.MAXIMIZED_BOTH);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Adding the option panel so that it appears on top of other components.
		optionPanel.setBounds(optionXL, optionYL, optionX, optionY);
		optionPanel.setLayout(null);
		optionPanel.setOpaque(false);
		optionPanel.setVisible(false);
		window.add(optionPanel);

		// Adding the keyboard panel so that it appears on "top" of other components.
		mainKeyboard.setCurve(keyboardCurve);
		mainKeyboard.setBounds(keyboardPanelXL, keyboardPanelYL, keyboardX, keyboardY);
		mainKeyboard.setLayout(new FlowLayout(FlowLayout.CENTER, keyboardXGap, keyboardYGap));
		mainKeyboard.setOpaque(false);
		mainKeyboard.setVisible(false);
		window.add(mainKeyboard);

		// Text color on tab bars.
		javax.swing.UIManager.put("ProgressBar.selectionBackground", Color.white);
		javax.swing.UIManager.put("ProgressBar.selectionForeground", Color.white);

		// Add initial taskbar to frame.
		TaskBar tb = new TaskBar();
		tb.updateTaskBar();

		// Create backgrounds.
		for (int curWindowNum = 0; curWindowNum < windowCount; curWindowNum++) {
			JPanel display = new JPanel();
			display.setBounds(0, 0, screenX, screenY - taskGap);
			display.setLayout(cardLayout);
			display.setBackground(backgroundColor);
			// display.setBackground(Color.RED);
			backgroundHash.put(curWindowNum, display);
			window.add(backgroundHash.get(curWindowNum));
		}
		window.repaint();
		window.revalidate();

		// Must be within a try catch block because of file checking.
		try {
			if (checkForPriors()) {
				loadPriors();
				CurrentSession cs = new CurrentSession();
				cs.setMenuOpen(false);
				// After timers are created from loadPriors a one time sort is made for initial
				// use.
				Sorter so = new Sorter();
				so.sort(CurrentSession.itHash);

			} else {
				// If no priors exist the system just opens a timer creation menu.
				window.setVisible(true);
				freshStart();

			}

			// If data exists in the flush file you should load it.
			if (checkFlushFile()) {
				loadFlushData();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		// Method to execute once the program exits.
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@SuppressWarnings("static-access")
			public void run() {
				try {
					FileWriter fw = new FileWriter("./savedTimerData.txt");
					BufferedWriter bw = new BufferedWriter(fw);
					CurrentSession cs = new CurrentSession();

					// Collect current session info and set the cNOT.
					int timerCount = 0;
					for (int j = 0; j < cs.getTNOT(); j++) {
						if (cs.itHash.get(j) != null) {
							timerCount++;
						}
					}

					// Prevent useless writing.
					if (timerCount != 0) {
						// Create ints used to represent the boolean values for the session details,
						// makes writing and reading easier.
						int autoSortInt = 0;
						int cardLayoutInt = 0;
						int sessionAddress = cs.getSessionAddress();

						if (cs.getAutoSortEnabled()) {
							autoSortInt = 1;
						}
						if (cs.getCardLayout()) {
							cardLayoutInt = 1;
						}

						// Write session info before looping through timers.
						bw.write(cs.getTNOT() + "," + timerCount + "," + cardLayoutInt + "," + autoSortInt + ","
								+ sessionAddress);
						bw.newLine();

						// This loop writes the indivial timer data.
						for (int timerID = 0; timerID < cs.getTNOT(); timerID++) {
							if (cs.itHash.get(timerID) != null) {
								int toggleInt = 0;
								if (cs.itHash.get(timerID).getToggled()) {
									toggleInt = 1;
								}
								bw.write(cs.itHash.get(timerID).getTimerID() + ",");
								bw.write(cs.itHash.get(timerID).getStartMin() + ",");
								bw.write(cs.itHash.get(timerID).getStartHour() + ",");
								bw.write(cs.itHash.get(timerID).getCurSec() + ",");
								bw.write(cs.itHash.get(timerID).getCurMin() + ",");
								bw.write(cs.itHash.get(timerID).getCurHour() + ",");
								bw.write(toggleInt + ",");
								bw.write(cs.itHash.get(timerID).getTitle());
								bw.newLine();
							}
						}

						bw.flush();
						bw.close();
						// Rewrite crashlog with new info in case the exit was not intentional.
						Analytics an = new Analytics();
						an.generateCrashLog();

						// Writes flush file.
						if (!(cs.getDowntimeQueue().size() == 0)) {
							FileWriter ffw = new FileWriter("./flushFile.txt");
							BufferedWriter fbw = new BufferedWriter(ffw);
							ClientConnection cc = new ClientConnection();
							// Writes the data in the flush queue starting at the current flush index.
							for (int i = cc.getFlushIndex(); i < cs.getDowntimeQueue().size(); i++) {
								fbw.write(cs.getDowntimeQueue().get(i));
								fbw.newLine();
							}
							fbw.flush();
							fbw.close();
						}
					}
				} catch (IOException e) {

					e.printStackTrace();
				}

			}
		}));

	}

	/*
	 * checkForPriors is used to return a boolean representing whether or not
	 * something has been previously written to the timer logs. Because of the
	 * prevention of writing 0 timers to the log, any writing shows that at least
	 * one timer exists.
	 */
	static boolean checkForPriors() throws IOException {
		File priorFile = new File("./savedTimerData.txt");
		return !(priorFile.length() == 0);
	}

	/*
	 * loadPriors is called if priors do exist. It triggers a number of processes
	 * including reading from file and setting currentSession values.
	 */
	static void loadPriors() throws IOException {

		FileReader fr = new FileReader("./savedTimerData.txt");
		@SuppressWarnings("resource")
		BufferedReader br = new BufferedReader(fr);
		String sessionData = br.readLine();
		ArrayList<String> stringData = new ArrayList<String>();

		// Fill the stringData arraylist.
		String curLine = br.readLine();
		int dataIndex = 0;
		CurrentSession cs = new CurrentSession();

		// Add each line of existing text to ArrayList.
		while (curLine != null) {
			stringData.add(dataIndex, curLine);
			dataIndex++;
			curLine = br.readLine();
		}

		// Initialize arrays with hardcoded values for how much session data is needed.
		String[] sessionStrings = new String[5];
		int[] sessionDataInt = new int[5];

		// Convert session data strings to values stored in an int array.
		int curSessionIndex = 0;
		String curSessionString = sessionData;
		for (int curStringNum = 0; curStringNum < sessionStrings.length; curStringNum++) {
			curSessionIndex = curSessionString.indexOf(',');
			if (curSessionIndex == -1) {
				sessionDataInt[4] = Integer.parseInt(curSessionString);

			} else {
				sessionStrings[curStringNum] = curSessionString.substring(0, curSessionIndex);
				sessionDataInt[curStringNum] = Integer.parseInt(sessionStrings[curStringNum]);
				curSessionString = curSessionString.substring(curSessionIndex + 1);
			}
		}

		// Sets base values for session details.
		cs.setCardLayout(sessionDataInt[2] == 1);
		cs.setCNOT(0);

		// Initialize ItemTimers from stored data.
		for (int curStringNum = 0; curStringNum < stringData.size(); curStringNum++) {
			int curDataIndex = 0;
			String curDataString = stringData.get(curStringNum);
			String title = "";

			// String lengths are hardcoded, alter these when storing more data on
			// individual timers.
			String[] rawDataStrings = new String[8];
			int[] timerValues = new int[8];

			// Seperates each data value and stores them appropiately.
			for (int j = 0; j < rawDataStrings.length; j++) {
				curDataIndex = curDataString.indexOf(',');

				if (curDataIndex == -1) {
					title = curDataString;

				} else {
					rawDataStrings[j] = curDataString.substring(0, curDataIndex);
					timerValues[j] = Integer.parseInt(rawDataStrings[j]);
					curDataString = curDataString.substring(curDataIndex + 1);
				}
			}

			// Assigning values to use when creating the new itemTimer.
			int id = timerValues[0];
			int startMin = timerValues[1];
			int startHour = timerValues[2];
			int curSec = timerValues[3];
			int curMin = timerValues[4];
			int curHour = timerValues[5];
			boolean toggled = (timerValues[6] == 1);

			cs.increaseCNOT();

			if (toggled) {
				cs.increaseANOT();
			}

			ItemTimer it = new ItemTimer(startMin, startHour, title, id, true, toggled);

			// Set the timers progress so that timers can save where they were.
			it.setCurSec(curSec);
			it.setCurMin(curMin);
			it.setCurHour(curHour);

			// Prg is the progress value of the bar, basically converting everything to
			// seconds and subtracting total by current seconds.
			int prgValue = (startMin * 60) + (startHour * 3600) - (curSec + (curMin * 60) + (curHour * 3600));
			it.setPrgValue(prgValue);
		}

		// SwitchLayoutGaps is called no matter what since it performs its own session
		// check to set gaps.
		switchLayoutGaps();

		// Update taskbar in case there is an overflow of timers past the first page.
		TaskBar tb = new TaskBar();
		tb.updateTaskBar();

		// Set the session details.
		cs.setTNOT(sessionDataInt[0]);
		cs.setCNOT(sessionDataInt[1]);
		cs.setAutoSortEnabled(sessionDataInt[3] == 1);
		cs.setSessionAddress(sessionDataInt[4]);

		// 11111111 is used as a holder address.
		if (sessionDataInt[4] == 11111111) {
			cs.setClientConnected(false);
		} else {
			cs.setClientConnected(true);
		}

		// Set to the first page after creating timers, because of old autoNext feature
		// when adding.
		cs.setCurrentPage(0);
		PageManager pm = new PageManager();
		pm.updatePage();

		window.setVisible(true);

	}

	/*
	 * checkFlushFile is used to see whether or not the flushFile is empty or not.
	 */
	private static boolean checkFlushFile() {
		File flushFile = new File("./flushFile.txt");
		return !(flushFile.length() == 0);

	}

	/*
	 * loadFlushData is used to pull all data from the flushFile and add it to the
	 * downtimeQueue. This one is pretty easy since the data is read and written in
	 * order and no info needs to be parsed, and no header is needed.
	 */
	private static void loadFlushData() throws IOException {
		FileReader fr = new FileReader("./flushFile.txt");
		BufferedReader br = new BufferedReader(fr);

		// Read the first line before entering loop.
		String curLine = br.readLine();
		CurrentSession cs = new CurrentSession();

		// Loop until all data is added to queue.
		while (curLine != null) {
			cs.getDowntimeQueue().add(curLine);
			curLine = br.readLine();
		}
		br.close();

		// Empty the file after loading.
		FileWriter fw = new FileWriter("./flushFile.txt");
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write("");
		bw.flush();
		bw.close();

	}

	/*
	 * switchLayoutGaps() is used to adjust the horizontal and verticle gaps for
	 * graphical elements depending on what session layout is in use. Gaps are
	 * smaller for card layout and larger for tablayout but card layout is always
	 * used for the creation page because of the creation menu format.
	 * 
	 */
	public static void switchLayoutGaps() {
		CurrentSession cs = new CurrentSession();

		if (cs.getCardLayout()) {
			// Loop through and change gap size for cardLayout
			for (int i = 0; i < cs.getCAP(); i++) {
				backgroundHash.get(i).setLayout(cardLayout);
			}
		} else {
			// Loop though and change gap size for tabLayout
			for (int i = 0; i <= cs.getCAP(); i++) {
				backgroundHash.get(i).setLayout(tabLayout);
			}
		}
	}

	// Switches the layout to cardLayout gaps because the adding menu was designed
	// around the cardlayout.
	public static void switchToAddingGraphics() {
		CurrentSession cs = new CurrentSession();
		backgroundHash.get(cs.getCurrentPage()).setLayout(cardLayout);
	}

	/*
	 * freshStart is used to do a traditional startup which only displays a create
	 * menu upon boot.
	 */
	static void freshStart() {
		CreateTimer ct = new CreateTimer();
		ct.paintAddressPanel();
	}

}