import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import javax.swing.JPanel;

/**
 * Sorter class is responsible for the sorting by urgency feature for the
 * program. It also ensures that each timer is sorted and displayed in the
 * proper spot and page.
 * 
 * Author: @nu Date Modified: 8/10/2019
 */

public class Sorter {
	static int cAP;
	static int cNOT;
	static int tNOT;
	static int aNOT;
	static JPanel[] pageArray = new JPanel[cAP];
	static CurrentSession cs = new CurrentSession();
	static HashMap<Integer, ItemTimer> prgHash = cs.getItemTimerHash();
	ItemTimer[][] slots = new ItemTimer[cs.getCAP() + 1][cs.getFrameLimit()];

	/*
	 * sort is the method used to choose which sorting method to use based on
	 * session settings
	 */
	public void sort(HashMap<Integer, ItemTimer> hm) {
		cs.updateCAP();
		slots = new ItemTimer[cs.getCAP() + 1][cs.getFrameLimit()];
		if (cs.getANOT() != 0) {
			if (cs.getAutoSortEnabled()) {
				valueSort(hm);
			} else {
				stringSort(hm);
			}
		}
	}

	/*
	 * valueSort takes a hashmap as a parameter and collects the values by putting
	 * them into an arraylist. These values are sorted along with their keys. The
	 * keys are moved from the unsorted array into the sorted array. If it does not
	 * interfere with any current graphics, the sort method will then call for a
	 * page sort to be done with the sorted keys array.
	 */
	public void valueSort(HashMap<Integer, ItemTimer> hm) {
		// update the current session values
		cAP = cs.getCAP();
		cNOT = cs.getCNOT();
		tNOT = cs.getTNOT();
		aNOT = cs.getANOT();

		// create arrays used for storing keys, sizes are set to tNOT so there will be
		// null
		// values stored when timers are removed.
		int[] unsortedKeys = new int[tNOT];
		int[] sortedKeys = new int[tNOT];

		int key;
		ArrayList<Double> timerValues = new ArrayList<Double>();

		// this loop fills the timerValues ArrayList with timers that will be added to
		// the screen, also stores the ID's for these in an unsorted array.
		for (int curTimerID = 0; curTimerID < cs.getTNOT(); curTimerID++) {
			if (hm.get(curTimerID) != null) {
				if (hm.get(curTimerID).getToggled()) {
					timerValues.add((Double) (hm.get(curTimerID)).getPrgVelocity());
					unsortedKeys[curTimerID] = curTimerID;
				}
			}
		}
		// Actual sorting of values
		Collections.sort(timerValues);

		// This loop fills the sorted array by returning the key associated with each
		// sorted value.
		for (int sortKey = 0; sortKey < aNOT; sortKey++) {
			key = (int) getDoubleKey(hm, timerValues.get(sortKey));
			sortedKeys[sortKey] = key;
		}

		// this sends the sorted array over to the pageSort function if the timers need
		// to be added to the screen before calling the actual display timers function
		// also unsortedKeys is set to sorted keys, this may help with performance if a
		// check is made to see whether the timers need to be updated based on whether
		// these arrays equal each other
		if (!cs.getMenuOpen()) {
			pageSort(sortedKeys);
			unsortedKeys = sortedKeys;
			displayTimers();
		}

	}

	/*
	 * stringSort takes the title of each timer and sorts them alphabetically
	 * similar to the value sort.
	 */
	public void stringSort(HashMap<Integer, ItemTimer> hm) {
		// update the current session values
		cs.updateCAP();
		cAP = cs.getCAP();
		cNOT = cs.getCNOT();
		tNOT = cs.getTNOT();
		aNOT = cs.getANOT();

		int key;
		// create the two arrays responsible for holding timerIDs
		int[] unsortedKeys = new int[tNOT];
		int[] sortedKeys = new int[tNOT];

		ArrayList<String> timerTitles = new ArrayList<String>();

		// This loop adds the enabled timer's names to the timerValues Arraylist
		for (int curTimerID = 0; curTimerID < cs.getTNOT(); curTimerID++) {
			if (hm.get(curTimerID) != null) {
				if (hm.get(curTimerID).getToggled()) {
					timerTitles.add(hm.get(curTimerID).getTitle());
					unsortedKeys[curTimerID] = curTimerID;
				}
			}
		}
		// Actual sorting for timer names
		Collections.sort(timerTitles);

		// This loop fills the sorted array with the ID's of the timers by looking up
		// the keys associated with the sorted timer names.
		for (int sortKey = 0; sortKey < aNOT; sortKey++) {
			key = (int) getStringKey(hm, timerTitles.get(sortKey));
			sortedKeys[sortKey] = key;
		}

		// This checks to see whether or not timers need to be painted to the screen,
		// should be adjusted for performance some day
		if (!cs.getMenuOpen()) {
			pageSort(sortedKeys);
			unsortedKeys = sortedKeys;
			displayTimers();
		}
	}

	/*
	 * displayTimers is used mainly for graphics after a page sort is performed. It
	 * goes through the filled slots in the 2D array and adds the itemTimer to it's
	 * correct page
	 * 
	 */
	@SuppressWarnings("static-access")
	public void displayTimers() {

		StartUp su = new StartUp();
		int page = cs.getCurrentPage();
		su.backgroundHash.get(page).removeAll();
		for (int slot = 0; slot < cs.getFrameLimit(); slot++) {
			if (slots[page][slot] != null) {
				su.backgroundHash.get(page).add(slots[page][slot].getTimerPanel());
				su.backgroundHash.get(page).repaint();
				su.backgroundHash.get(page).revalidate();
			}
		}
	}

	/*
	 * pageSort takes an array of sorted keys from the hashmap and sorts them
	 * according to whatever layout is selected. The entire array is filled with
	 * itemTimer objects to ensure that the correct ones are being used by
	 * displayTimers. We are too close to launch for me to test only filling the
	 * required slots to see how it affects runtime speeds. It will likely cause a
	 * large amount of null pointer exc when making the transition.
	 * 
	 */
	private void pageSort(int[] ka) {
		int limit = cs.getFrameLimit();
		aNOT = cs.getANOT();
		cs.updateCAP();
		cAP = cs.getCAP();

		int leftOverTimers = (aNOT - (limit * cAP));
		int leftOverSlots = limit - leftOverTimers;
		if (cAP != 0) {
			for (int pageNum = 0; pageNum < cAP; pageNum++) {
				for (int slotNum = 0; slotNum < limit; slotNum++) {
					slots[pageNum][slotNum] = prgHash.get(ka[(limit * pageNum) + slotNum]);
				}
			}
		}

		if (leftOverTimers != 0) {
			for (int slotNum = 0; slotNum < leftOverTimers; slotNum++) {

				slots[cAP][slotNum] = prgHash.get(ka[(limit * cAP) + slotNum]);

			}
		}
		for (int slotNum = limit - leftOverSlots; slotNum < leftOverSlots; slotNum++) {
			slots[cAP][slotNum] = null;
		}
	}

	/*
	 * getValueKey allows for the sort method to retrieve the key associated with
	 * the timer's current value.
	 */

	public static Object getDoubleKey(HashMap<Integer, ItemTimer> hm, Double v) {
		for (Object o : hm.keySet()) {
			if ((hm.get(o).getPrgVelocity()) == v) {
				return o;
			}
		}
		return null;
	}

	/*
	 * getStringKey allows for the sort method to retrieve the key associated with
	 * the timer's name.
	 */
	public static Object getStringKey(HashMap<Integer, ItemTimer> hm, String v) {
		for (Object o : hm.keySet()) {
			if ((hm.get(o).getTitle()).equals(v)) {
				return o;
			}
		}
		return null;
	}

}