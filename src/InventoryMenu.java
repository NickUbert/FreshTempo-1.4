import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;

//TODO
public class InventoryMenu {
	CurrentSession cs = new CurrentSession();
	StartUp su = new StartUp();
	PageManager pm = new PageManager();

	// Toggle panel and toggle bounds.
	private static Dimension mtk = Toolkit.getDefaultToolkit().getScreenSize();
	private static final int screenY = ((int) mtk.getHeight());
	private static final int screenX = ((int) mtk.getWidth());
	private int timerTogglePanelX = (int) (.86923 * screenX);
	private int toggleScrollX = (int) (screenX);
	private int toggleScrollDimX = (int) (.06128 * screenX);
	private int toggleX = (int) (toggleScrollX * .18);
	private int exitButtonX = (int) (toggleScrollX * .15);
	private int newFolderButtonX = (int) (toggleScrollX * .07);
	private int toggleY = (int) (screenY * .1);
	private int flowGap = (int) (screenY * .004);
	private int newFolderIconXY = (int) (screenX * .042);

	private int newTimerIconXY = (int) (screenX * .045);
	private int newTaskIconXY = (int) (screenX * .047);

	private int scrollRowsNum = 4 + ((cs.getCNOT() + 1) / 5) + (CurrentSession.folders.size() / 5);
	private int toggleScrollValue = (((scrollRowsNum) * (toggleY + flowGap)));

	// Fonts
	private Font toggleFont = new Font("Helvetica", Font.BOLD, ((int) (.02 * screenX)));
	private Font bannerFont = new Font("Helvetica", Font.BOLD, ((int) (.03 * screenX)));
	// Colors
	private Color backgroundColor = Color.decode("#223843");
	private Color exitColor = Color.decode("#CC2936");
	private Color toggledColor = Color.decode("#4DA167");
	private Color newFolderColor = Color.decode("#6B818C");

	private JLabel inventoryBanner = new JLabel("Inventory Menu");
	Border toggleBorder = BorderFactory.createLineBorder(Color.GREEN, 3);
	Border exitBorder = BorderFactory.createLineBorder(Color.RED, 3);
	Border unToggledBorder = BorderFactory.createLineBorder(Color.WHITE, 3);

	Icon resizedNewFolderIcon = new ImageIcon(
			new ImageIcon(getClass().getClassLoader().getResource("FT-icon-new-folder.png")).getImage()
					.getScaledInstance(newFolderIconXY, newFolderIconXY, Image.SCALE_SMOOTH));

	Icon resizedNewTimerIcon = new ImageIcon(
			new ImageIcon(getClass().getClassLoader().getResource("FT-icon-new-timer.png")).getImage()
					.getScaledInstance(newTimerIconXY, newTimerIconXY, Image.SCALE_SMOOTH));

	Icon resizedNewTaskIcon = new ImageIcon(
			new ImageIcon(getClass().getClassLoader().getResource("FT-icon-new-task.png")).getImage()
					.getScaledInstance(newTaskIconXY, newTaskIconXY, Image.SCALE_SMOOTH));

	// Create togglePanel and scrollPanel
	JPanel inventoryPanel = new JPanel();
	JScrollPane toggleScrollPanel = new JScrollPane(inventoryPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
			JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

	/*
	 * The TimerToggles constructor handles updating values needed for the panel and
	 * setting the dimensions/graphics up before calling addToggles.
	 */
	public InventoryMenu() {

		// Clear the page and values before doing a value update of ANOT.
		pm.clearPage();
		cs.setANOT(0);
		cs.setMenuOpen(true);
		inventoryPanel.removeAll();

		// Prevents overflow for the scroll page.
		if (((cs.getCNOT() + 1) % 5) > 0) {
			toggleScrollValue = ((scrollRowsNum + 2) * toggleY) + (int) (toggleY * 1.3);
		}

		// Set up layout and dims for togglePanel
		inventoryPanel.setLayout(new FlowLayout(FlowLayout.CENTER, flowGap, flowGap));
		toggleScrollPanel.getVerticalScrollBar().setPreferredSize(new Dimension(toggleScrollDimX, 0));
		inventoryPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
		toggleScrollPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
		toggleScrollPanel.setPreferredSize(new Dimension(toggleScrollX, screenY - (int) (screenY * .12)));
		inventoryPanel.setBackground(backgroundColor);
		inventoryPanel.setPreferredSize(new Dimension(timerTogglePanelX, toggleScrollValue));

		toggleScrollPanel.getVerticalScrollBar().setBackground(backgroundColor);

		inventoryBanner.setPreferredSize(new Dimension(screenX, (int) (toggleY / 1.2)));
		inventoryBanner.setFont(bannerFont);
		inventoryBanner.setHorizontalAlignment(JLabel.CENTER);

		inventoryBanner.setForeground(Color.WHITE);
		inventoryPanel.add(inventoryBanner);
		// Exit button labeled "Save/Close"
		JButton exitButton = new JButton("SAVE/CLOSE");
		exitButton.setPreferredSize(new Dimension(exitButtonX, toggleY));
		exitButton.setForeground(Color.WHITE);
		exitButton.setBackground(exitColor);
		exitButton.setBorder(exitBorder);
		exitButton.setFont(toggleFont);
		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// update the graphics when exiting the toggle panel.
				cs.setMenuOpen(false);
				inventoryPanel.removeAll();
				inventoryPanel.setVisible(false);
				toggleScrollPanel.setVisible(false);
				cs.setCurrentPage(0);
				StartUp.switchLayoutGaps();

				// prevents null calls when no timers need to be sorted.
				if (cs.getANOT() != 0) {
					Sorter so = new Sorter();
					so.sort(CurrentSession.itHash);
				}

			}
		});

		inventoryPanel.add(exitButton);

		JButton newFolderButton = new JButton();
		newFolderButton.setPreferredSize(new Dimension(newFolderButtonX, toggleY));
		newFolderButton.setBackground(newFolderColor);
		newFolderButton.setFocusable(false);
		newFolderButton.setIcon(resizedNewFolderIcon);
		newFolderButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				InventoryFolder inf = new InventoryFolder();
				inf.openNewFolder();
			}
		});

		if (CurrentSession.folders.size() < 15) {
			inventoryPanel.add(newFolderButton);
		}

		JButton newTimerButton = new JButton();
		newTimerButton.setPreferredSize(new Dimension(newFolderButtonX, toggleY));
		newTimerButton.setBackground(newFolderColor);
		newTimerButton.setFocusable(false);
		newTimerButton.setIcon(resizedNewTimerIcon);
		newTimerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// Update session values and open new toggle page
				cs.setMenuOpen(true);
				cs.setTyping(false);

				inventoryPanel.removeAll();
				inventoryPanel.setVisible(false);
				toggleScrollPanel.setVisible(false);

				// Updates layout type and proper gap spacing
				StartUp.switchToAddingGraphics();

				TaskBar tb = new TaskBar();
				tb.updateBar("UNDO");

				// This loop may not be needed since the addition of the menu used for adding
				while (cs.getCurrentPage() < cs.getCAP()) {
					cs.nextPage();
					@SuppressWarnings("unused")
					Sorter so = new Sorter();
				}

				PageManager pm = new PageManager();
				pm.clearPage();

				CreateTimer ct = new CreateTimer();
				ct.paintCreatePanel();

			}
		});

		inventoryPanel.add(newTimerButton);

		JButton newTaskButton = new JButton();
		newTaskButton.setPreferredSize(new Dimension(newFolderButtonX, toggleY));
		newTaskButton.setBackground(newFolderColor);
		newTaskButton.setFocusable(false);
		newTaskButton.setIcon(resizedNewTaskIcon);
		newTaskButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// Update session values and open new toggle page
				cs.setMenuOpen(true);
				cs.setTyping(false);

				inventoryPanel.removeAll();
				inventoryPanel.setVisible(false);
				toggleScrollPanel.setVisible(false);

				// Updates layout type and proper gap spacing
				StartUp.switchToAddingGraphics();

				TaskBar tb = new TaskBar();
				tb.updateBar("UNDO");

				// This loop may not be needed since the addition of the menu used for adding
				while (cs.getCurrentPage() < cs.getCAP()) {
					cs.nextPage();
					@SuppressWarnings("unused")
					Sorter so = new Sorter();
				}

				PageManager pm = new PageManager();
				pm.clearPage();

				CreateTask ct = new CreateTask();
				ct.paintCreatePanel();

			}
		});

		// TODO add the taskTimer option
		inventoryPanel.add(newTaskButton);

		addFolderGap();

		InventoryFolder iv = new InventoryFolder();

		if (cs.checkFolderTitle("ALL")) {
			iv.createNewIcon("ALL");
		}
		for (int i = 0; i < CurrentSession.folders.size(); i++) {
			JPanel folder = iv.createNewIcon(CurrentSession.folders.get(i).getName());
			inventoryPanel.add(folder);
		}

		addFolderGap();

		cs.addToCurrentPage(toggleScrollPanel);
		addToggles();

	}

	public void addFolderGap() {
		JPanel gapRow = new JPanel();
		gapRow.setPreferredSize(new Dimension(screenX, (int) (toggleY * .5)));
		gapRow.setBackground(null);
		inventoryPanel.add(gapRow);
	}

	/*
	 * addToggles is the method that handles picking out which timers need to be
	 * added to the toggle panel. Once a timer is found that isn't null it calls
	 * createToggle and increases the ANOT if needed.
	 */
	private void addToggles() {
		int key;
		ArrayList<String> timerTitles = new ArrayList<String>();
		ItemTimer[] activeList = new ItemTimer[cs.getTNOT()];
		int[] sortedKeys = new int[cs.getTNOT()];
		HashMap<Integer, ItemTimer> hm = CurrentSession.itHash;

		// This loop adds the enabled timer's names to the timerValues Arraylist
		for (int curTimerID = 0; curTimerID < cs.getTNOT(); curTimerID++) {
			if (hm.get(curTimerID) != null) {
				timerTitles.add(hm.get(curTimerID).getTitle());
				activeList[curTimerID] = hm.get(curTimerID);

			}
		}
		// Actual sorting for timer names
		Collections.sort(timerTitles);

		// This loop fills the sorted array with the ID's of the timers by looking up
		// the keys associated with the sorted timer names.
		for (int sortKey = 0; sortKey < timerTitles.size(); sortKey++) {
			key = getStringKey(activeList, timerTitles.get(sortKey));
			sortedKeys[sortKey] = key;
		}

		for (int curTimerID = 0; curTimerID < cs.getTNOT(); curTimerID++) {
			if (CurrentSession.itHash.get(sortedKeys[curTimerID]) != null) {

				inventoryPanel.add(createToggle(CurrentSession.itHash.get(sortedKeys[curTimerID]).getTitle(),
						sortedKeys[curTimerID]));

				if (CurrentSession.itHash.get(sortedKeys[curTimerID]).getToggled()) {
					cs.increaseANOT();
				}
			}

		}

		inventoryPanel.repaint();
		inventoryPanel.revalidate();
	}

	public void addToggles(String groupName, boolean grouping) {
		int key;
		ArrayList<String> timerTitles = new ArrayList<String>();
		ItemTimer[] activeList = new ItemTimer[cs.getTNOT()];
		int[] sortedKeys = new int[cs.getTNOT()];
		HashMap<Integer, ItemTimer> hm = CurrentSession.itHash;

		// This loop adds the enabled timer's names to the timerValues Arraylist
		if (!grouping) {
			for (int curTimerID = 0; curTimerID < cs.getTNOT(); curTimerID++) {
				if (hm.get(curTimerID) != null) {
					if (hm.get(curTimerID).getInventoryGroups().contains(groupName)) {
						timerTitles.add(hm.get(curTimerID).getTitle());
						activeList[curTimerID] = hm.get(curTimerID);
					}

				}
			}
		} else {
			for (int curTimerID = 0; curTimerID < cs.getTNOT(); curTimerID++) {
				if (hm.get(curTimerID) != null) {
					timerTitles.add(hm.get(curTimerID).getTitle());
					activeList[curTimerID] = hm.get(curTimerID);

				}
			}
		}
		// Actual sorting for timer names
		Collections.sort(timerTitles);

		// This loop fills the sorted array with the ID's of the timers by looking up
		// the keys associated with the sorted timer names.
		for (int sortKey = 0; sortKey < timerTitles.size(); sortKey++) {
			key = getStringKey(activeList, timerTitles.get(sortKey));
			sortedKeys[sortKey] = key;
		}

		if (grouping) {
			for (int curTimerID = 0; curTimerID < cs.getTNOT(); curTimerID++) {
				if (CurrentSession.itHash.get(sortedKeys[curTimerID]) != null) {

					inventoryPanel.add(createGroupToggle(CurrentSession.itHash.get(sortedKeys[curTimerID]).getTitle(),
							sortedKeys[curTimerID], groupName));
				}

			}
		} else {
			for (int curTimerID = 0; curTimerID < cs.getTNOT(); curTimerID++) {
				if (CurrentSession.itHash.get(sortedKeys[curTimerID]) != null) {

					inventoryPanel.add(createToggle(CurrentSession.itHash.get(sortedKeys[curTimerID]).getTitle(),
							sortedKeys[curTimerID]));
				}

			}
		}

		inventoryPanel.repaint();
		inventoryPanel.revalidate();
	}

	/*
	 * getStringKey allows for the sort method to retrieve the key associated with
	 * the timer's name.
	 */
	public static Integer getStringKey(ItemTimer[] list, String v) {
		ItemTimer temp = list[0];

		for (int i = 0; i < list.length; i++) {
			temp = list[i];
			if (temp != null) {
				if (temp.getTitle().equals(v)) {
					list[i] = null;
					return temp.getTimerID();
				}
			}
		}
		return null;
	}

	private JButton createGroupToggle(String name, int id, String groupName) {
		// Create a reference object for the ID being called.
		ItemTimer it = CurrentSession.itHash.get(id);

		// Create the toggle button for this itemTimer
		JButton toggleButton = new JButton(name);
		toggleButton.setPreferredSize(new Dimension(toggleX, toggleY));

		boolean groupedAlready = it.getInventoryGroups().contains(groupName);
		if (groupedAlready) {
			toggleButton.setBorder(toggleBorder);
			toggleButton.setBackground(Color.decode("#4DA167"));
			toggleButton.setForeground(Color.WHITE);
		} else {
			toggleButton.setBorder(unToggledBorder);
			toggleButton.setBackground(Color.WHITE);
			toggleButton.setForeground(backgroundColor);
		}

		toggleButton.setFocusable(false);
		toggleButton.setFont(toggleFont);

		// Action performed when toggle button is clicked.
		toggleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (!groupName.contentEquals("ALL")) {
					if (it.getInventoryGroups().contains(groupName)) {

						toggleButton.setBorder(unToggledBorder);
						toggleButton.setBackground(Color.WHITE);
						toggleButton.setForeground(backgroundColor);
						CurrentSession.itHash.get(id).removeFromGroup(groupName);

						// update graphics and session/itemtimer values.
					} else {
						toggleButton.setBorder(toggleBorder);
						toggleButton.setBackground(toggledColor);
						toggleButton.setForeground(Color.WHITE);
						CurrentSession.itHash.get(id).addToGroup(groupName);
					}
				}

			}
		});

		return toggleButton;
	}

	/*
	 * createToggle is the method that actually creates the toggleButtons and adds
	 * the needed graphics. It uses the timer name and ID number as its parameters
	 * for graphics and function use.
	 */
	private JButton createToggle(String name, int id) {

		// Create a reference object for the ID being called.
		ItemTimer it = CurrentSession.itHash.get(id);

		// Create the toggle button for this itemTimer
		JButton toggleButton = new JButton(name);
		toggleButton.setPreferredSize(new Dimension(toggleX, toggleY));

		// Toggle graphics updated depending on whether it is toggled or not.
		if (it.getToggled()) {
			toggleButton.setBorder(toggleBorder);
			toggleButton.setBackground(Color.decode("#4DA167"));
			toggleButton.setForeground(Color.WHITE);
		} else {
			toggleButton.setBorder(unToggledBorder);
			toggleButton.setBackground(Color.WHITE);
			toggleButton.setForeground(backgroundColor);
		}
		toggleButton.setFocusable(false);
		toggleButton.setFont(toggleFont);

		// Action performed when toggle button is clicked.
		toggleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// updates graphics and session/itemtimer values.
				if (it.getToggled()) {
					cs.decreaseANOT();
					if (it.getPrgPercentage() < .7) {
						it.getTimerGraphics().recordTimer();
					}
					toggleButton.setBorder(unToggledBorder);
					toggleButton.setBackground(Color.WHITE);
					toggleButton.setForeground(backgroundColor);
					CurrentSession.itHash.get(id).getTimer().stop();

					// update graphics and session/itemtimer values.
				} else {
					cs.increaseANOT();
					toggleButton.setBorder(toggleBorder);
					toggleButton.setBackground(toggledColor);
					toggleButton.setForeground(Color.WHITE);
					CurrentSession.itHash.get(id).startCountDown(false);
					CurrentSession.itHash.get(id).switchToResumedGraphics();
					CurrentSession.itHash.get(id).setPause(false);

				}
				// Update this timers toggled value.
				it.setToggled(!it.getToggled());
			}
		});

		return toggleButton;

	}
}
