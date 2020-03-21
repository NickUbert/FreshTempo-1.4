import java.awt.Component;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * CurrentSession class is used mainly for the getting and setting of session
 * related variables. These values dictate how everything in the program runs
 * including the sorting and graphics.
 * 
 * cAP = current adding panel cNOT = current number of timers tNOT = total
 * number of timers used in session aNOT = active number of timers (toggled)
 * nOAE = number of active expirations
 * 
 */
public class CurrentSession {
	private static boolean cardLayout = true;
	private static boolean autoSortEnabled = false;
	private static int currentPage = 0;
	private static int cAP;
	private static int cNOT = 1;
	private static int tNOT = 1;
	private static int aNOT = 0;
	private static int nOAE = 0;
	private static int frameLimit;
	private static boolean menuOpen = true;
	private static boolean typing = false;
	private static boolean activeExpiration = false;
	private static boolean serverUp = true;
	private static boolean currentlyFlushing = false;

	// If sessionAddress is set to the default 1111111 then the user does not want
	// FreshNet support and data will not be collected.
	private static int sessionAddress = 11111111;
	private static boolean clientConnected;

	// itHash is the collection of itemTimers. formerly prgHash
	static HashMap<Integer, ItemTimer> itHash = new HashMap<Integer, ItemTimer>();
	static ArrayList<String> downtimeQueue = new ArrayList<String>();

	/*
	 * nextPage is used for naviagtion and updating graphics
	 */
	public void nextPage() {
		PageManager pm = new PageManager();
		pm.nextPage();

	}

	/*
	 * prevPage is used for navigation and updating graphics
	 */
	public void prevPage() {
		PageManager pm = new PageManager();
		pm.prevPage();

	}

	/*
	 * updateCap is used to update the current adding panel, the frame limits are
	 * hard-coded so they will need to be changed when new designs are added
	 */
	public void updateCAP() {
		if (cardLayout) {
			frameLimit = 3;
		} else {
			frameLimit = 15;
		}

		cAP = 0;
		for (int curTimerNum = 0; curTimerNum < aNOT; curTimerNum++) {
			if (curTimerNum % frameLimit == 0 && curTimerNum != 0) {
				cAP++;
			}
		}
	}

	/*
	 * setCurrentPage updates the current page value and updates graphics
	 */
	public void setCurrentPage(int i) {
		currentPage = i;
		PageManager pm = new PageManager();
		pm.updatePage();
	}

	/*
	 * addToCAP is used to add new timers to the Current Adding Panel
	 */
	public void addToCAP(Component c) {
		if (getCNOT() != 0) {
			updateCAP();
		}

		int currentAddingPanel = getCAP();

		StartUp.backgroundHash.get(currentAddingPanel).add(c);
		StartUp.backgroundHash.get(currentAddingPanel).revalidate();
	}

	/*
	 * addToCurrentPage adds a component to the current page
	 */
	public void addToCurrentPage(Component c) {

		StartUp.backgroundHash.get(currentPage).add(c);
		StartUp.backgroundHash.get(currentPage).revalidate();
	}

	public boolean getCardLayout() {
		return cardLayout;
	}

	public void setCardLayout(boolean b) {
		cardLayout = b;
	}

	public boolean getAutoSortEnabled() {
		return autoSortEnabled;
	}

	public void setAutoSortEnabled(boolean b) {
		autoSortEnabled = b;
	}

	public boolean getMenuOpen() {
		return menuOpen;
	}

	public void setMenuOpen(boolean b) {
		menuOpen = b;
	}

	public boolean getTyping() {
		return typing;
	}

	public void setTyping(boolean b) {
		typing = b;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public int getTNOT() {
		return tNOT;
	}

	public void setTNOT(int i) {
		tNOT = i;
	}

	public void setCNOT(int i) {
		cNOT = i;
	}

	public void increaseTNOT() {
		tNOT++;
	}

	public void decreaseTNOT() {
		tNOT--;
	}

	public int getCNOT() {
		return cNOT;
	}

	public void increaseCNOT() {
		cNOT++;
	}

	public void decreaseCNOT() {
		cNOT--;
	}

	public void increaseANOT() {
		aNOT++;
	}

	public void decreaseANOT() {
		aNOT--;
	}

	public void setANOT(int i) {
		aNOT = i;
	}

	public int getANOT() {
		return aNOT;
	}

	public void increaseNOAE() {
		nOAE++;
	}

	public void decreaseNOAE() {
		nOAE--;
	}

	public int getNOAE() {
		return nOAE;
	}

	public int getFrameLimit() {
		return frameLimit;
	}

	public void setFrameLimit(int i) {
		frameLimit = i;
	}

	public boolean getActiveExpirations() {
		return activeExpiration;
	}

	public void setActiveExpiratons(boolean b) {
		activeExpiration = b;
	}

	public int getCAP() {
		return cAP;
	}

	public HashMap<Integer, ItemTimer> getItemTimerHash() {
		return itHash;
	}

	public int getSessionAddress() {
		return sessionAddress;
	}

	public void setSessionAddress(int i) {
		sessionAddress = i;
	}

	public boolean getClientConnected() {
		return clientConnected;
	}

	public void setClientConnected(boolean b) {
		clientConnected = b;
	}

	public ArrayList<String> getDowntimeQueue() {
		return downtimeQueue;
	}

	public void addToDowntimeQueue(String s) {
		downtimeQueue.add(s);
	}

	public void clearDowntimeQueue() {
		downtimeQueue.clear();
	}

	public void setServerUp(boolean b) {
		serverUp = b;
	}

	public boolean getServerUp() {
		return serverUp;
	}

	public void setCurrentlyFlushing(boolean b) {
		currentlyFlushing = b;
	}

	public boolean getCurrentlyFlushing() {
		return currentlyFlushing;
	}

}