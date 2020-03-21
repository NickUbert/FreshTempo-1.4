import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;

//TODO
public class TimerToggles {
	CurrentSession cs = new CurrentSession();
	StartUp su = new StartUp();
	PageManager pm = new PageManager();

	// Toggle panel and toggle bounds.
	private static Dimension mtk = Toolkit.getDefaultToolkit().getScreenSize();
	private static final int screenY = ((int) mtk.getHeight());
	private static final int screenX = ((int) mtk.getWidth());
	private int timerTogglePanelX = (int) (.76923 * screenX);
	private int toggleScrollX = (int) (screenX);
	private int toggleScrollDimX = (int) (.06128 * screenX);
	private int toggleX = (int) (toggleScrollX * .18);
	private int toggleY = (int) (screenY * .15);
	private int scrollRowsNum = 1+ ((cs.getCNOT() + 1) / 5);
	private int toggleScrollValue = ((scrollRowsNum) * (toggleY));

	// Fonts
	private Font toggleFont = new Font("Helvetica", Font.BOLD, ((int) (.02 * screenX)));
	private Font bannerFont = new Font("Helvetica", Font.BOLD, ((int) (.03 * screenX)));
	// Colors
	private Color backgroundColor = Color.decode("#223843");
	private Color exitColor = Color.decode("#CC2936");
	private Color toggledColor = Color.decode("#4DA167");

	private JLabel toggleBanner = new JLabel("Choose Active Timers");

	// Create togglePanel and scrollPanel
	JPanel togglePanel = new JPanel();
	JScrollPane toggleScrollPanel = new JScrollPane(togglePanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
			JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

	/*
	 * The TimerToggles constructor handles updating values needed for the panel and
	 * setting the dimensions/graphics up before calling addToggles.
	 */
	public TimerToggles() {

		// Clear the page and values before doing a value update of ANOT.
		pm.clearPage();
		cs.setANOT(0);
		cs.setMenuOpen(true);
		togglePanel.removeAll();

		// Prevents overflow for the scroll page.
		if (((cs.getCNOT() + 1) % 5) > 0) {
			toggleScrollValue = ((scrollRowsNum) * toggleY) + (int) (toggleY * 1.05);
		}

		// Set up layout and dims for togglePanel
		togglePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		toggleScrollPanel.getVerticalScrollBar().setPreferredSize(new Dimension(toggleScrollDimX, 0));
		togglePanel.setBorder(new EmptyBorder(0, 0, 0, 0));
		toggleScrollPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
		toggleScrollPanel.setPreferredSize(new Dimension(toggleScrollX, screenY - (int) (screenY * .12)));
		togglePanel.setBackground(backgroundColor);
		togglePanel.setPreferredSize(new Dimension(timerTogglePanelX, toggleScrollValue));

		toggleScrollPanel.getVerticalScrollBar().setBackground(backgroundColor);
	

		toggleBanner.setPreferredSize(new Dimension(screenX, (int) (toggleY / 2)));
		toggleBanner.setFont(bannerFont);
		toggleBanner.setHorizontalAlignment(JLabel.CENTER);
		;
		toggleBanner.setForeground(Color.WHITE);
		togglePanel.add(toggleBanner);

		// Exit button labeled "Save/Close"
		JButton exitButton = new JButton("SAVE/CLOSE");
		exitButton.setPreferredSize(new Dimension(toggleX, toggleY));
		exitButton.setForeground(Color.WHITE);
		exitButton.setBackground(exitColor);
		exitButton.setFont(toggleFont);
		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// update the graphics when exiting the toggle panel.
				cs.setMenuOpen(false);
				togglePanel.removeAll();
				togglePanel.setVisible(false);
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

		// Add the panel to the page and call addToggles.
		togglePanel.add(exitButton);
		cs.addToCurrentPage(toggleScrollPanel);
		addToggles();

	}

	/*
	 * addToggles is the method that handles picking out which timers need to be
	 * added to the toggle panel. Once a timer is found that isn't null it calls
	 * createToggle and increases the ANOT if needed.
	 */
	private void addToggles() {

		for (int curTimerID = 0; curTimerID < cs.getTNOT(); curTimerID++) {
			if (CurrentSession.itHash.get(curTimerID) != null) {

				togglePanel.add(createToggle(CurrentSession.itHash.get(curTimerID).getTitle(), curTimerID));

				if (CurrentSession.itHash.get(curTimerID).getToggled()) {
					cs.increaseANOT();
				}
			}

		}
		togglePanel.repaint();
		togglePanel.revalidate();
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
			toggleButton.setBackground(Color.decode("#4DA167"));
			toggleButton.setForeground(Color.WHITE);
		} else {
			toggleButton.setBackground(Color.LIGHT_GRAY);
			toggleButton.setForeground(Color.DARK_GRAY);
		}
		toggleButton.setFocusable(false);
		toggleButton.setFont(toggleFont);

		// Action performed when toggle button is clicked.
		toggleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// updates graphics and session/itemtimer values.
				if (it.getToggled()) {
					cs.decreaseANOT();
					toggleButton.setBackground(Color.LIGHT_GRAY);
					toggleButton.setForeground(Color.DARK_GRAY);
					CurrentSession.itHash.get(id).getTimer().stop();

					// update graphics and session/itemtimer values.
				} else {
					cs.increaseANOT();
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
