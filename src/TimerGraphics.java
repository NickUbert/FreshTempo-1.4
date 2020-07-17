import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
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
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

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
	private final int tabX = (int) (.3 * screenX);
	private final int tabY = (int) (.1575 * screenY);

	private int shadowGap = (int) (.007 * screenX);

	private int tabTimerLabelXL = (int) (.69737 * tabX);
	private int tabTimerLabelX = (int) (.26316 * tabX);

	private int tabDetailsBtnXL = (int) (.7 * tabX);
	private int tabDetailsBtnX = (int) (.26316 * tabX);
	private int tabDetailsBtnY = (int) (.325 * tabY);
	private int tabDetailsIconYL = (int) (.6 * tabY);
	private int tabDetailsIconXY = (int) (.09 * tabX);

	private int detailLabelXL = (int) (.3 * tabX);
	private int detailLabelX = (int) (.6 * tabX);
	private int detailLabelY = (int) (.3 * tabY);

	private int detailYL = (int) (1.1 * tabY);
	private int detailBtnXY = (int) (.8 * tabY);
	private int detailBtnLeftXL = (int) (.025 * tabX);

	private int scrollShelfPanelXL = (int) (.55 * tabX);
	private int scrollShelfPanelX = (int) (.425 * tabX);
	private int scrollShelfItemX = (int) (.15 * tabX);
	private int scrollShelfPanelY = (int) (.45 * tabY);

	private int descriptionLabelYL = detailYL + scrollShelfPanelY;

	private int descriptionTextX = (int) (.65 * tabX);
	private int descriptionTextY = (int) (.7 * tabY);

	private int scrollPanelY = tabY;
	private int scrollPanelYL;

	private int prgTXL = (int) (.17105 * tabX);
	private int prgTX = (int) (.525 * tabX);

	private int tabRefreshXL = (int) (.0125 * tabX);
	private int tabRefreshXY = (int) (.15158 * tabX);

	// Timer Colors
	private Color prgRemainder = Color.decode("#4DA167");
	private Color prgExpired = Color.decode("#CC2936");
	private Color detailBtnColor = Color.decode("#6B818C");

	// Progress Bar
	private JProgressBar prg = new JProgressBar();

	// Text Labels
	private JLabel titleLabel = new JLabel();
	private JLabel timeLabel = new JLabel("waiting", SwingConstants.CENTER);

	private JButton refreshBtn = new JButton();
	private JButton detailsBtn = new JButton();
	private JButton changeColorBtn = new JButton("Change Color");

	boolean open = false;

	// Timer Panel
	private RoundedPanel timerPanel = new RoundedPanel();

	Icon resizedColorIcon = new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("FT-icon-color.png"))
			.getImage().getScaledInstance(tabDetailsIconXY, tabDetailsIconXY, Image.SCALE_SMOOTH));
	Icon resizedDeactivateIcon = new ImageIcon(
			new ImageIcon(getClass().getClassLoader().getResource("FT-icon-deactivate.png")).getImage()
					.getScaledInstance(tabDetailsIconXY, tabDetailsIconXY, Image.SCALE_SMOOTH));
	Icon resizedDescription = new ImageIcon(
			new ImageIcon(getClass().getClassLoader().getResource("FT-icon-description.png")).getImage()
					.getScaledInstance(tabDetailsIconXY, tabDetailsIconXY, Image.SCALE_SMOOTH));

	Icon resizedDetailDown = new ImageIcon(
			new ImageIcon(getClass().getClassLoader().getResource("FT-icon-drop-down.png")).getImage()
					.getScaledInstance(tabDetailsIconXY, tabDetailsIconXY, Image.SCALE_SMOOTH));

	Icon resizedDetailUp = new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("FT-icon-drop-up.png"))
			.getImage().getScaledInstance(tabDetailsIconXY, tabDetailsIconXY, Image.SCALE_SMOOTH));

	// Text Fonts
	String fontName = "Helvetica";
	private Font tabTitleFont = new Font(fontName, Font.BOLD, (int) (.08 * tabX));
	private Font tabTitleSmallFont = new Font(fontName, Font.BOLD, (int) (.06 * tabX));
	private Font tabTimeFont = new Font(fontName, Font.ITALIC, (int) (.07319 * tabX));
	private Font detailsFont = new Font(fontName, Font.BOLD, (int) (.04 * tabX));
	private Font detailsBtnFont = new Font(fontName, Font.BOLD, (int) (.025 * tabX));
	private Font smallDetailsFont = new Font(fontName, Font.BOLD, (int) (.03 * tabX));
	private Font descriptionFont = new Font(fontName, Font.PLAIN, (int) (.03 * tabX));

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

		detailsBtn.setContentAreaFilled(false);
		detailsBtn.setFocusPainted(false);
		detailsBtn.setBorder(null);
		detailsBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				detailAnimation(!open);
				open = !open;

			}
		});

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

	int detailsSpaceToFill;
	double detailsIncrement;

	private void detailAnimation(boolean opening) {

		if (opening) {
			timerPanel.setPreferredSize(new Dimension(tabX, (int) (tabY * 2.5)));
			timerPanel.repaint();
			timerPanel.revalidate();
			detailsBtn.setIcon(resizedDetailUp);
			detailsBtn.repaint();
			addDetails();
		} else {
			timerPanel.setPreferredSize(new Dimension(tabX, tabY));
			timerPanel.repaint();
			timerPanel.revalidate();
			detailsBtn.setIcon(resizedDetailDown);
			detailsBtn.repaint();
		}
	}

	private void addDetails() {

		changeColorBtn.setFocusPainted(false);
		changeColorBtn.setFont(detailsBtnFont);
		changeColorBtn.setBackground(Color.WHITE);
		changeColorBtn.setIcon(resizedColorIcon);
		changeColorBtn.setVerticalTextPosition(SwingConstants.TOP);
		changeColorBtn.setHorizontalTextPosition(SwingConstants.CENTER);

		JLabel detailText = new JLabel();

		String shelfLife = timer.getShelfString();

		detailText.setBounds(detailLabelXL, detailYL, detailLabelX, detailLabelY);
		detailText.setVerticalAlignment(SwingConstants.TOP);
		JLabel descriptionLabel = new JLabel("Description:");
		if (!timer.getTask()) {
			detailText.setText("Shelf Life: " + shelfLife);
			detailText.setFont(detailsFont);
			descriptionLabel.setFont(detailsFont);
		} else {
			// TODO
			// TODO

			detailText.setText("Scheduled Times:");
			detailText.setFont(smallDetailsFont);
			descriptionLabel.setFont(smallDetailsFont);

			int numOfDeadlines = timer.getDeadlines().size();

			JPanel deadlinesPanel = new JPanel();
			deadlinesPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

			deadlinesPanel.setPreferredSize(
					new Dimension(numOfDeadlines * (int) (scrollShelfItemX * 1.09), scrollShelfPanelY));
			// Add ScrollPane and Scroll Bar
			JScrollPane deadlinesScrollPanel = new JScrollPane(deadlinesPanel, JScrollPane.VERTICAL_SCROLLBAR_NEVER,
					JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

			deadlinesScrollPanel.getHorizontalScrollBar()
					.setPreferredSize(new Dimension(scrollShelfPanelX, (int) (detailLabelY * .4)));
			deadlinesPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
			deadlinesScrollPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
			deadlinesScrollPanel.setBounds(scrollShelfPanelXL, (int) (detailYL * .98), scrollShelfPanelX,
					scrollShelfPanelY);

			for (int i = 0; i < numOfDeadlines; i++) {
				JLabel deadlineLabel = new JLabel();
				deadlineLabel.setText(timer.getDeadlines().get(i).toString());
				deadlineLabel.setHorizontalAlignment(SwingConstants.CENTER);
				deadlineLabel.setVerticalAlignment(SwingConstants.TOP);
				deadlineLabel.setFont(smallDetailsFont);
				deadlineLabel.setPreferredSize(new Dimension(scrollShelfItemX, detailLabelY));
				deadlinesPanel.add(deadlineLabel);
			}

			timerPanel.add(deadlinesScrollPanel);

		}
		timerPanel.add(detailText);

		if (timer.hasDescription()) {
			descriptionLabel.setBounds(detailLabelXL, descriptionLabelYL, detailLabelX, detailLabelY);
			descriptionLabel.setVerticalAlignment(SwingConstants.TOP);
			timerPanel.add(descriptionLabel);

			JTextArea descriptionText = new JTextArea(timer.getDescription());
			descriptionText.setFont(descriptionFont);
			descriptionText.setLineWrap(true);
			descriptionText.setWrapStyleWord(true);
			descriptionText.setBounds(detailLabelXL, descriptionLabelYL + (int) (detailLabelY * .5), descriptionTextX,
					descriptionTextY);
			timerPanel.add(descriptionText);
		}

		changeColorBtn.setBounds(detailBtnLeftXL, detailYL, detailBtnXY, detailBtnXY);

		changeColorBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				timer.increaseColorCode();
				switchColorGroup();
			}
		});

		timerPanel.add(changeColorBtn);
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
		 * Database db = new Database();
		 * 
		 * try { db.recordItem(timer.getTimerID(), timerShelfSec); } catch (SQLException
		 * e) { System.out.println("Unable to record item..."); e.printStackTrace(); }
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
		if (timer.getTitle().length() > 9) {
			prg.setFont(tabTitleSmallFont);
		} else {
			prg.setFont(tabTitleFont);
		}
		// Update bounds and fonts.
		timeLabel.setBounds(tabTimerLabelXL, 0, tabTimerLabelX, tabY);
		timeLabel.setFont(tabTimeFont);

		refreshBtn.setBounds(tabRefreshXL, 0, tabRefreshXY, tabY);
		refreshBtn.setVerticalAlignment(SwingConstants.CENTER);

		detailsBtn.setBounds(tabDetailsBtnXL, tabDetailsIconYL, tabDetailsBtnX, tabDetailsBtnY);
		detailsBtn.setHorizontalAlignment(SwingConstants.CENTER);
		detailsBtn.setVerticalAlignment(SwingConstants.TOP);
		detailsBtn.setIcon(resizedDetailDown);

		// Add components.
		timerPanel.add(prg);
		timerPanel.add(refreshBtn);
		timerPanel.add(timeLabel);
		timerPanel.add(detailsBtn);
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
		animationTimerPrg.start();

	}

	Timer animationTimerPrg = new Timer(5, new ActionListener() {
		public void actionPerformed(ActionEvent ae) {
			spaceToFill -= increment;
			if (spaceToFill <= 0) {
				prg.setValue(0);
				prg.repaint();
				prg.revalidate();
				animationTimerPrg.stop();

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

		Icon resizedBlackCheckIcon = new ImageIcon(
				new ImageIcon(getClass().getClassLoader().getResource("FT-icon-task-check-black.png")).getImage()
						.getScaledInstance(tabRefreshXY, tabRefreshXY, Image.SCALE_SMOOTH));

		Icon resizedBlueCheckIcon = new ImageIcon(
				new ImageIcon(getClass().getClassLoader().getResource("FT-icon-task-check-blue.png")).getImage()
						.getScaledInstance(tabRefreshXY, tabRefreshXY, Image.SCALE_SMOOTH));

		Icon resizedOrangeCheckIcon = new ImageIcon(
				new ImageIcon(getClass().getClassLoader().getResource("FT-icon-task-check-orange.png")).getImage()
						.getScaledInstance(tabRefreshXY, tabRefreshXY, Image.SCALE_SMOOTH));
		Icon resizedPinkCheckIcon = new ImageIcon(
				new ImageIcon(getClass().getClassLoader().getResource("FT-icon-task-check-pink.png")).getImage()
						.getScaledInstance(tabRefreshXY, tabRefreshXY, Image.SCALE_SMOOTH));

		Icon resizedGreenCheckIcon = new ImageIcon(
				new ImageIcon(getClass().getClassLoader().getResource("FT-icon-task-check-green.png")).getImage()
						.getScaledInstance(tabRefreshXY, tabRefreshXY, Image.SCALE_SMOOTH));
		if (timer.getTask()) {
			switch (timer.getColorCode()) {
			case 0:
				// Black
				refreshBtn.setIcon(resizedBlackCheckIcon);
				break;
			case 1:
				// Blue
				refreshBtn.setIcon(resizedBlueCheckIcon);
				break;
			case 2:
				// Orange
				refreshBtn.setIcon(resizedOrangeCheckIcon);
				break;
			case 3:
				// Pink
				refreshBtn.setIcon(resizedPinkCheckIcon);
				break;
			case 4:
				// Green
				refreshBtn.setIcon(resizedGreenCheckIcon);
				break;
			}
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