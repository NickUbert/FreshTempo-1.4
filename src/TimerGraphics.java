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

	private int refreshY = (int) (.16456 * cardY);
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

	private Color colorCodeA = Color.decode("#FFFFFF");
	private Color colorCodeB = Color.decode("#0AD3FF");

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
				if (timer.getColorCode() == 0) {
					timerPanel.setBackground(colorCodeA);
				}
				if (timer.getColorCode() == 2) {
					timerPanel.setBackground(colorCodeB);
				}

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
					refreshAnimation();
					String resetTime = timer.timeValueToString(0, timer.getStartMin(), timer.getStartHour());
					timeLabel.setForeground(prgRemainder);
					timeLabel.setText(resetTime);
					timeLabel.repaint();
					timeLabel.revalidate();

					timer.setJustRefreshed(true);
					

				}

			}

		});

		// Check which format to use.
		if (cs.getCardLayout()) {
			createCardUI();
		} else {
			createTabUI();
		}

	}

	public void refreshTimer() {
		// Record the timer refresh data no matter what
		/*
		 * Analytics an = new Analytics(); try { an.recordTimeData(timer); } catch
		 * (IOException e1) { e1.printStackTrace(); }
		 */

		Database db = new Database();

		// TODO

		

		try {
			db.recordItem(timer.getTimerID(), timerShelfSec);
		} catch (SQLException e) {
			System.out.println("Unable to record item...");
			e.printStackTrace();
		}

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
	 * createCardUI is used to paint the timergraphics needed for timers running in
	 * a card layout session.
	 */
	@SuppressWarnings("static-access")
	private void createCardUI() {

		// Remove all components before creating the card.
		Icon resizedRefreshIcon = new ImageIcon(
				new ImageIcon(getClass().getClassLoader().getResource("FT-icon-refresh.png")).getImage()
						.getScaledInstance(refreshXY, refreshXY, Image.SCALE_SMOOTH));

		refreshBtn.setIcon(resizedRefreshIcon);
		timerPanel.removeAll();
		timerPanel.setLayout(null);
		timerPanel.setPreferredSize(new Dimension(cardX, cardY));
		timerPanel.setOpaque(false);

		// Check for color code in case a layout change was made.
		if (timer.getColorCode() == 0) {
			timerPanel.setBackground(colorCodeA);
		}
		if (timer.getColorCode() == 2) {
			timerPanel.setBackground(colorCodeB);
		}

		// Set card specific progress bar properties.
		prg.setOrientation(SwingConstants.VERTICAL);
		prg.setBounds(0, prgy, prgwidth, prgheight);
		prg.setStringPainted(false);

		// Update fonts and bounds
		titleLabel.setFont(cardTitleFont);
		timeLabel.setBounds(timeDisplayXL, timeDisplayY, timeDisplayWidth, timeDisplayHeight);
		timeLabel.setFont(cardTimeFont);
		titleLabel.setBounds(titleLabelXL, titleLabely, titleLabelwidth, titleLabelheight);
		refreshBtn.setBounds(refreshXL, refreshY, refreshXY, refreshXY);

		// Add components
		timerPanel.add(titleLabel);
		timerPanel.add(prg);
		timerPanel.add(refreshBtn);
		timerPanel.add(timeLabel);
		timerPanel.repaint();
		timerPanel.revalidate();

		// Add to page if needed.
		CurrentSession cs = new CurrentSession();
		if (timer.getToggled()) {
			cs.addToCAP(timerPanel);
		}

		StartUp su = new StartUp();
		su.window.repaint();
		su.window.revalidate();

	}

	/*
	 * createTabUI is used to paint the components needed for timers running in a
	 * tab layout session.
	 */
	@SuppressWarnings("static-access")
	private void createTabUI() {

		// Clear all components before making a tab layout timer.
		Icon resizedRefreshIcon = new ImageIcon(
				new ImageIcon(getClass().getClassLoader().getResource("FT-icon-refresh.png")).getImage()
						.getScaledInstance(tabRefreshXY, tabRefreshXY, Image.SCALE_SMOOTH));
		refreshBtn.setIcon(resizedRefreshIcon);
		timerPanel.removeAll();
		timerPanel.setLayout(null);
		timerPanel.setPreferredSize(new Dimension(tabX, tabY));
		timerPanel.setOpaque(false);

		// Check for color code in case of layout switch.
		if (timer.getColorCode() == 0) {
			timerPanel.setBackground(colorCodeA);
		}
		if (timer.getColorCode() == 2) {
			timerPanel.setBackground(colorCodeB);
		}
		// Update tab specific progress bar properties.
		prg.setOrientation(SwingConstants.HORIZONTAL);
		prg.setBounds(prgTXL, 1, prgTX, tabY - shadowGap - 1);

		prg.setString(timer.getTitle());
		prg.setStringPainted(true);
		prg.setFont(tabTitleFont);

		// Update bounds and fonts.
		timeLabel.setBounds(tabTimerLabelXL, 0, tabTimerLabelX, tabY);
		timeLabel.setFont(tabTimeFont);
		refreshBtn.setBounds(tabRefreshXL, tabRefreshYL, tabRefreshXY, tabRefreshXY);

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
		increment = ((double) spaceToFill / 50);
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
	 * switchLayout is used to change the layout of each timer.
	 */
	public void switchLayout() {
		if (cs.getCardLayout()) {
			createCardUI();
		} else {
			createTabUI();
		}
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