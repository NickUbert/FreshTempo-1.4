import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.sql.SQLException;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.Timer;

/**
 * TimerGraphics class is used to create and paint the components of each timer.
 * These components are changed depending on what layout is in use. Each timer
 * has an individual TimerGraphics object created which is why the methods for
 * updating components are found in this class and not in its own class.
 */
public class TimerGraphics {
	CurrentSession cs = new CurrentSession();
	private Dimension mtk = Toolkit.getDefaultToolkit().getScreenSize();
	private ItemTimer timer;
	int timerShelfSec;

	// Timer Bounds
	private final int screenX = ((int) mtk.getWidth());
	private final int screenY = ((int) mtk.getHeight());
	private final int cardX = (int) (.3125 * screenX);
	private final int cardY = (int) (.875 * screenY);
	private final int tabX = (int) (.3 * screenX);
	private final int tabY = (int) (.1575 * screenY);

	private int timeDisplayXL = (int) (.02 * cardX);
	private int timeDisplayY = (int) (.86076 * cardY);
	private int timeDisplayWidth = (int) (.96 * cardX);
	private int timeDisplayHeight = (int) (.1519 * cardY);

	private int shadowGap = (int) (.007 * screenX);

	private int prgwidth = cardX - shadowGap;
	private int prgy = (int) (.29114 * cardY);
	private int prgheight = (int) (.56962 * cardY);

	private int titleLabely = 0;
	private int titleLabelwidth = (int) (.96 * cardX);
	private int titleLabelXL = (int) ((.5 * cardX) - (titleLabelwidth / 2));
	private int titleLabelheight = (int) (.17722 * cardY);

	private int refreshY = (int) (tabY);
	private int refreshXY = (int) (.11658 * cardY);
	private int refreshXL = (int) (.5 * cardX - (refreshXY / 2));

	private int tabTimerLabelXL = (int) (.69737 * tabX);
	private int tabTimerLabelX = (int) (.26316 * tabX);

	private int prgTXL = (int) (.17105 * tabX);
	private int prgTX = (int) (.525 * tabX);

	private int tabRefreshXL = (int) (.0125 * tabX);
	private int tabRefreshXY = (int) (.15158 * tabX);
	private int tabRefreshYL = (int) (.5 * tabY - (tabRefreshXY / 2));

	// Timer Colors
	private Color prgRemainder = Color.decode("#4DA167");
	private Color prgExpired = Color.decode("#CC2936");
	private Color backgroundColor = Color.decode("#223843");

	// Black
	private Color colorCodeA = Color.decode("#000000");
	// Blue
	private Color colorCodeB = Color.decode("#00A8E8");
	// Orange
	private Color colorCodeC = Color.decode("#FF7D00");
	// Pink
	private Color colorCodeD = Color.decode("#FF00FF");
	// Green
	private Color colorCodeE = Color.decode("#6BD425");

	// Progress Bar
	private JProgressBar prg = new JProgressBar();

	// Text Labels
	private JLabel titleLabel = new JLabel();
	private JLabel timeLabel = new JLabel("waiting", SwingConstants.CENTER);

	private JButton refreshBtn = new JButton();

	// Timer Panel
	private RoundedPanel timerPanel = new RoundedPanel();

	// Text Fonts
	String fontName = "Helvetica";
	private Font cardTitleFont = new Font(fontName, Font.BOLD, (int) (.0725 * screenX));
	private Font tabTitleFont = new Font(fontName, Font.BOLD, (int) (.08 * tabX));
	private Font tabTitleSmallFont = new Font(fontName, Font.BOLD, (int) (.06 * tabX));
	private Font cardTimeFont = new Font(fontName, Font.TRUETYPE_FONT, (int) (.0475 * screenX));
	private Font tabTimeFont = new Font(fontName, Font.ITALIC, (int) (.07319 * tabX));

	public TimerGraphics(ItemTimer it) {
		timer = it;
	}

	/*
	 * createTimerUI is used to create initial components without specifying what
	 * size or shape they use. It then calls the method needed based on what layout
	 * is being used to actually add the specifics for components and add it to
	 * frame.
	 */
	public void createTimerUI() {

		// This mouse listener allows users to cycle through colors.
		timerPanel.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				timer.increaseColorCode();
				switchColorGroup();
			}
		});

		// Creates progress bar but doesn't add specific components
		prg.setMaximum(timer.getMax());
		prg.setValue(0);
		prg.setStringPainted(false);
		prg.setForeground(prgExpired);
		prg.setBackground(prgRemainder);
		prg.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				// Resuming the timer
				if (!timer.getTask()) {
					if (timer.getPause()) {
						prg.setBackground(prgRemainder);
						prg.setForeground(prgExpired);
						timer.setPause(false);

						// update the NOAE value for the session
						if (timer.getCurrentlyExpired()) {
							cs.increaseNOAE();
							if (cs.getNOAE() == 1) {
								cs.setActiveExpiratons(true);
							}
						}
						timer.countDown.start();
					} else {
						// Pausing the timer
						prg.setBackground(Color.GRAY);
						prg.setForeground(Color.darkGray);

						timer.setPause(true);
						// Update the NOAE value for the session.
						if (timer.getCurrentlyExpired()) {
							cs.decreaseNOAE();
							if (cs.getNOAE() == 0) {

								cs.setActiveExpiratons(false);
							}

						}

						timer.countDown.stop();
					}
				}

			}
		});

		// Creates text label for the Item's name
		titleLabel = new JLabel(timer.getTitle(), SwingConstants.CENTER);
		titleLabel.setVisible(true);
		titleLabel.setForeground(Color.BLACK);

		// Sets the style for time display
		timeLabel.setVisible(true);
		timeLabel.setForeground(Color.BLACK);

		// Refresh button
		refreshBtn = new JButton("");
		refreshBtn.setContentAreaFilled(false);
		refreshBtn.setFocusPainted(false);
		refreshBtn.setBorder(null);
		refreshBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				timerShelfSec = timer.getShelfSec();
				if (timer.getPause()) {
					timer.setPause(false);
					switchToResumedGraphics();
					timer.countDown.start();
				}

				if (!timer.getDoubleTap()) {

					if (timer.getInitialsRequired()) {
						InitialsMenu im = new InitialsMenu(timer);
					} else {
						refreshTimer();
					}

				}

			}

		});

		createTabUI();

	}

	public void refreshTimer() {
		refreshAnimation();
		String resetTime = timer.timeValueToString(0, timer.getStartMin(), timer.getStartHour());
		timeLabel.setForeground(prgRemainder);
		timeLabel.setText(resetTime);
		timeLabel.repaint();
		timeLabel.revalidate();

		timer.setJustRefreshed(true);
	}

	public void recordTimer() {
		// Record the timer refresh data no matter what

		Analytics an = new Analytics();
		try {
			an.recordTimeData(timer, "n/a");
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		// TODO DEMO DISCONNECT
		/*
		Database db = new Database();

		try {
			db.recordItem(timer.getTimerID(), timerShelfSec);
		} catch (SQLException e) {
			System.out.println("Unable to record item...");
			e.printStackTrace();
		}
		*/
		// Update the timer and session values for expirations.
		if (timer.getCurrentlyExpired()) {
			timer.setCurrentlyExpired(false);
			cs.decreaseNOAE();
			if (cs.getNOAE() == 0) {
				cs.setActiveExpiratons(false);
			}
		}
		
		timer.startCountDown(true);
	}

	/*
	 * createTabUI is used to paint the components needed for timers running in a
	 * tab layout session.
	 */
	@SuppressWarnings("static-access")
	private void createTabUI() {

		// Clear all components before making a tab layout timer.

		timerPanel.removeAll();

		// Check for color code in case of layout switch.
		switchColorGroup();

		timerPanel.setLayout(null);
		timerPanel.setPreferredSize(new Dimension(tabX, tabY));
		timerPanel.setOpaque(false);

		// Update tab specific progress bar properties.
		prg.setOrientation(SwingConstants.HORIZONTAL);
		prg.setBounds(prgTXL, 1, prgTX, tabY - shadowGap - 1);

		prg.setString(timer.getTitle());
		prg.setStringPainted(true);
		if(timer.getTitle().length()>9) {
			prg.setFont(tabTitleSmallFont);
		} else {
		prg.setFont(tabTitleFont);
		}
		// Update bounds and fonts.
		timeLabel.setBounds(tabTimerLabelXL, 0, tabTimerLabelX, tabY);
		timeLabel.setFont(tabTimeFont);
		refreshBtn.setBounds(tabRefreshXL, 0, tabRefreshXY, tabY);
		refreshBtn.setVerticalAlignment(SwingConstants.CENTER);

		// Add components.
		timerPanel.add(prg);
		timerPanel.add(refreshBtn);
		timerPanel.add(timeLabel);
		timerPanel.repaint();
		timerPanel.revalidate();

		// Add to frame if needed.
		CurrentSession cs = new CurrentSession();
		if (timer.getToggled()) {
			cs.addToCAP(timerPanel);
		}
		StartUp su = new StartUp();
		su.window.repaint();
		su.window.revalidate();

	}

	int spaceToFill;
	double increment;

	private void refreshAnimation() {
		spaceToFill = ((prg.getValue()));
		increment = ((double) spaceToFill / 40);
		prg.setBackground(prgRemainder);
		animationTimer.start();

	}

	Timer animationTimer = new Timer(5, new ActionListener() {
		public void actionPerformed(ActionEvent ae) {
			spaceToFill -= increment;
			if (spaceToFill <= 0) {
				prg.setValue(0);
				prg.repaint();
				prg.revalidate();
				animationTimer.stop();

			} else {
				prg.setValue(spaceToFill);
				prg.repaint();
				prg.revalidate();

			}
		}
	});

	private void switchColorGroup() {

		Icon resizedBlackRefreshIcon = new ImageIcon(
				new ImageIcon(getClass().getClassLoader().getResource("FT-icon-refresh-black.png")).getImage()
						.getScaledInstance(tabRefreshXY, tabRefreshXY, Image.SCALE_SMOOTH));
		Icon resizedGreenRefreshIcon = new ImageIcon(
				new ImageIcon(getClass().getClassLoader().getResource("FT-icon-refresh-green.png")).getImage()
						.getScaledInstance(tabRefreshXY, tabRefreshXY, Image.SCALE_SMOOTH));
		Icon resizedPinkRefreshIcon = new ImageIcon(
				new ImageIcon(getClass().getClassLoader().getResource("FT-icon-refresh-pink.png")).getImage()
						.getScaledInstance(tabRefreshXY, tabRefreshXY, Image.SCALE_SMOOTH));
		Icon resizedOrangeRefreshIcon = new ImageIcon(
				new ImageIcon(getClass().getClassLoader().getResource("FT-icon-refresh-orange.png")).getImage()
						.getScaledInstance(tabRefreshXY, tabRefreshXY, Image.SCALE_SMOOTH));
		Icon resizedBlueRefreshIcon = new ImageIcon(
				new ImageIcon(getClass().getClassLoader().getResource("FT-icon-refresh-blue.png")).getImage()
						.getScaledInstance(tabRefreshXY, tabRefreshXY, Image.SCALE_SMOOTH));

		Icon resizedTaskCheckIcon = new ImageIcon(
				new ImageIcon(getClass().getClassLoader().getResource("FT-icon-task-check.png")).getImage()
						.getScaledInstance(tabRefreshXY, tabRefreshXY, Image.SCALE_SMOOTH));
		if (timer.getTask()) {
			refreshBtn.setIcon(resizedTaskCheckIcon);
		} else {

			switch (timer.getColorCode()) {
			case 0:
				// Black
				refreshBtn.setIcon(resizedBlackRefreshIcon);
				break;
			case 1:
				// Blue
				refreshBtn.setIcon(resizedBlueRefreshIcon);
				break;
			case 2:
				// Orange
				refreshBtn.setIcon(resizedOrangeRefreshIcon);
				break;
			case 3:
				// Pink
				refreshBtn.setIcon(resizedPinkRefreshIcon);
				break;
			case 4:
				// Green
				refreshBtn.setIcon(resizedGreenRefreshIcon);
				break;
			}
		}
	}

	/*
	 * switchToPauseGraphics just changes the colors on this timers progress bar to
	 * gray.
	 */
	public void switchToPauseGraphics() {
		prg.setBackground(Color.GRAY);
		prg.setForeground(Color.darkGray);
	}

	/*
	 * switchToResumedGraphics just changes the colors on this timers progress bar
	 * to the normal green/red.
	 */
	public void switchToResumedGraphics() {
		prg.setBackground(prgRemainder);
		prg.setForeground(prgExpired);
	}

	/*
	 * getPrg is used to return this timers progresss bar.
	 */
	public JProgressBar getPrg() {
		return prg;
	}

	/*
	 * getTimerDisplay is used to return the timers countdown display.
	 */
	public JLabel getTimeDisplay() {
		return timeLabel;
	}

	/*
	 * getTimerPanel is used to return the timers panel.
	 */
	public JPanel getTimerPanel() {
		return timerPanel;
	}

}