import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

/**
 * Option Class is used to mainly handle the graphics of the option menu. There
 * is not too much work done through the functions here since they usually call
 * something in the CurrentSession class.
 * 
 */
public class Options {

	CurrentSession cs = new CurrentSession();
	private Dimension mtk = Toolkit.getDefaultToolkit().getScreenSize();
	private final int screenX = ((int) mtk.getWidth());
	private final int screenY = ((int) mtk.getHeight());

	// Bounds
	private int textFontX = (int) (.0625 * screenY);
	private int optionsX = (int) (.4875 * screenX);
	private int optionsY = (int) (.75 * screenY);
	private int autoSortYL = (int) (.57813 * optionsY);
	private int exitXY = (int) (.15385 * optionsX);
	private int exitXYL = (int) (.01282 * optionsX);
	private int removeScrollXL = (int) (.025 * optionsX);
	private int removeScrollYL = (int) (.20 * optionsY);
	private int removeScrollXY = (int) (.875 * optionsX);
	private int layoutYL = (int) (.40625 * optionsY);
	private int scrollDimX = (int) (.053 * optionsX);
	private int listPanelX = (int) (.85 * optionsX);
	private int settingsLabelX = (int) (.64103 * optionsX);
	private int settingsLabelY = (int) (.21875 * optionsY);
	private int settingsLabelXL = (int) (.2 * optionsX);
	private int removeLabelY = (int) (.15 * optionsY);
	private int removeLabelXL = (int) (.205 * optionsX);
	private int removeLabelYL = (int) (.03 * optionsY);
	private int optionsBtnXL = (int) (.03846 * optionsX);
	private int optionsBtnY = (int) (.15625 * optionsY);
	private int analyticsYL = (int) (.75 * optionsY);
	private int removeYL = (int) (.23438 * optionsY);
	private int listItemX = (int) (.46154 * optionsX);
	private int analyticsItemX = (int) (.85 * optionsX);
	private int listItemY = (int) (.115 * optionsY);
	private int analyticsItemY = (int) (.09 * optionsY);
	private int removeLabelX = (int) (optionsX - (optionsX * .15));
	private int optionsBtnX = (int) (optionsX - (optionsBtnXL * 4));
	private int analyticsFontX = (int) (.03 * screenY);
	private int sessionFontX = (int) (.025 * screenY);
	private int scrollPanelGap = (int) (.046 * screenY);

	// Fonts
	private Font optionFont = new Font("Helvetica", Font.PLAIN, (int) (.03 * screenX));
	private Font settingsFont = new Font("Helvetica", Font.BOLD, (int) (.05 * screenX));
	private Font removeFont = new Font("Helvetica", Font.BOLD, (int) (.045 * screenX));
	private Font exitFont = new Font("Helvetica", Font.TRUETYPE_FONT, textFontX);
	private Font analyticsFont = new Font("Helvetica", Font.TRUETYPE_FONT, analyticsFontX);
	private Font sessionFont = new Font("Helvetica", Font.BOLD, sessionFontX);

	// Color
	private Color optionButtonBackground = Color.decode("#C2C1C2");
	private Color returnColor = Color.decode("#ED217C");
	private Color backgroundColor = Color.decode("#223843");

	private JLabel settingsLabel;
	JButton freshNetBtn;

	private int scrollValue = cs.getCNOT() * ((int) (.125 * optionsY));

	/*
	 * openMenu is used to adjust the currentSession values and make the menu
	 * visible over all other components in the frame.
	 */
	public void openMenu() {
		// clear page and update session values.
		cs.setTyping(true);
		cs.setMenuOpen(true);
		StartUp.optionPanel.setVisible(true);
		StartUp.optionPanel.setBackground(Color.WHITE);
		PageManager pm = new PageManager();
		pm.clearPage();
		fillMenu();
	}

	/*
	 * fillMenu is used to add components to the optionpanel including the buttons
	 * along with their fucntions all other option panel functions are contained
	 * within these action listeners.
	 */
	private void fillMenu() {

		// FreshTempo Title on Menu
		settingsLabel = new JLabel("FreshTempo", SwingConstants.CENTER);
		settingsLabel.setBounds(settingsLabelXL, 0, settingsLabelX, settingsLabelY);
		settingsLabel.setVisible(true);
		settingsLabel.setFont(settingsFont);
		StartUp.optionPanel.add(settingsLabel);

		JPanel removePanel = new JPanel();
		removePanel.setLayout(new FlowLayout(FlowLayout.LEFT));

		// Add ScrollPane and Scroll Bar
		JScrollPane removeScrollPanel = new JScrollPane(removePanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		removeScrollPanel.getVerticalScrollBar().setPreferredSize(new Dimension(scrollDimX, 0));
		removePanel.setBackground(Color.WHITE);
		removePanel.setBorder(new EmptyBorder(0, 0, 0, 0));
		removeScrollPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
		removeScrollPanel.setBounds(removeScrollXL, removeScrollYL, removeScrollXY,
				(optionsY - removeScrollYL) - scrollPanelGap);

		// Remove page title
		JLabel removePanelTitle = new JLabel("Remove Timer:");
		removePanelTitle.setFont(removeFont);
		removePanelTitle.setBounds(removeLabelXL, removeLabelYL, removeLabelX, removeLabelY);

		StartUp.optionPanel.add(removePanelTitle);
		removePanelTitle.setVisible(false);

		// Exit button
		JButton exitBtn = new JButton("X");
		exitBtn.setBounds(exitXYL, exitXYL, exitXY, exitXY);
		exitBtn.setFont(exitFont);
		exitBtn.setForeground(Color.WHITE);
		exitBtn.setBackground(returnColor);
		exitBtn.setFocusable(false);
		exitBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				cs.setMenuOpen(false);
				cs.setTyping(false);
				// This checks to make sure there are timers needing to be sorted, prevents null
				// calls
				if (cs.getANOT() > 0) {
					Sorter so = new Sorter();
					if (cs.getAutoSortEnabled()) {
						so.valueSort(CurrentSession.itHash);
					} else {
						so.stringSort(CurrentSession.itHash);
					}
				}
				// Update the taskbar and page graphics.
				TaskBar tb = new TaskBar();
				tb.updateTaskBar();
				StartUp.optionPanel.setVisible(false);
				StartUp.optionPanel.removeAll();
				StartUp.optionPanel.repaint();
				StartUp.optionPanel.revalidate();

			}
		});

		// Audio Button
		JButton autoSortBtn = new JButton();
		autoSortBtn.setVisible(true);
		autoSortBtn.setBounds(optionsBtnXL, autoSortYL, optionsBtnX, optionsBtnY);
		autoSortBtn.setFocusable(false);
		autoSortBtn.setFont(optionFont);
		autoSortBtn.setBackground(Color.LIGHT_GRAY);
		autoSortBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {

				cs.setAutoSortEnabled(!cs.getAutoSortEnabled());
				if (cs.getAutoSortEnabled()) {
					autoSortBtn.setText("Urgency Sort-ON");
				} else {
					autoSortBtn.setText("Urgency Sort-OFF");
				}

			}

		});

		// Initial value check for autosorting.
		if (cs.getAutoSortEnabled()) {
			autoSortBtn.setText("Urgency Sort-ON");
		} else {
			autoSortBtn.setText("Urgency Sort-OFF");
		}
		StartUp.optionPanel.add(autoSortBtn);

		// License Buttons
		JButton removebtn = new JButton("Remove Timers");
		JButton analyticsBtn = new JButton("Analytics");
		analyticsBtn.setVisible(true);
		analyticsBtn.setBounds(optionsBtnXL, analyticsYL, optionsBtnX, optionsBtnY);
		analyticsBtn.setFont(optionFont);
		analyticsBtn.setFocusable(false);
		analyticsBtn.setBackground(optionButtonBackground);
		analyticsBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				// Create return button
				JButton returnbtn = new JButton("X");
				returnbtn.setBounds(exitXYL, exitXYL, exitXY, exitXY);
				returnbtn.setFont(exitFont);
				returnbtn.setForeground(Color.WHITE);
				returnbtn.setBackground(returnColor);
				returnbtn.setFocusable(false);
				returnbtn.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ae) {
						// Update graphics when returning to session pages.
						removePanel.removeAll();
						removeScrollPanel.setVisible(false);
						removePanel.setVisible(false);
						returnbtn.setVisible(false);
						exitBtn.setVisible(true);
						autoSortBtn.setVisible(true);
						removebtn.setVisible(true);
						freshNetBtn.setVisible(true);
						analyticsBtn.setVisible(true);
					}
				});
				// Update to standard menu for analytics
				removeScrollPanel.setVisible(true);
				returnbtn.setVisible(true);
				StartUp.optionPanel.add(returnbtn);

				autoSortBtn.setVisible(false);
				removebtn.setVisible(false);
				analyticsBtn.setVisible(false);
				exitBtn.setVisible(false);
				freshNetBtn.setVisible(false);

				Analytics an = new Analytics();

				int numOfRotations = 0;
				try {
					numOfRotations = Integer.parseInt(an.getNumberOfRotations());
				} catch (NumberFormatException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

				// Analytics scroll value is calculated by multiplying the height of each timer
				// label by the number of timer labels. Sets the height of the scroll page.

				int anScrollValue = (numOfRotations + 3) * (analyticsItemY + 5);

				// Create remove panel
				removePanel.setPreferredSize(new Dimension(listPanelX, anScrollValue));
				removePanel.setVisible(true);
				StartUp.optionPanel.add(removeScrollPanel);

				// Label for the header of the expirations list
				JLabel itemsRecordedLabel = new JLabel("Rotations Recorded:");
				itemsRecordedLabel.setFont(sessionFont);
				itemsRecordedLabel.setPreferredSize(new Dimension(analyticsItemX, analyticsItemY));
				itemsRecordedLabel.setHorizontalAlignment(SwingConstants.CENTER);
				itemsRecordedLabel.setVisible(true);
				removePanel.add(itemsRecordedLabel);

				JLabel itemsFormatLabel = new JLabel("Item Name : Shelf Time : Time Stamp : Emp. Initials");
				itemsFormatLabel.setFont(sessionFont);
				itemsFormatLabel.setPreferredSize(new Dimension(analyticsItemX, analyticsItemY));
				itemsFormatLabel.setHorizontalAlignment(SwingConstants.CENTER);
				itemsFormatLabel.setVisible(true);
				removePanel.add(itemsFormatLabel);

				JLabel[] labelArray = new JLabel[numOfRotations];
				ArrayList<String> rotationDates = new ArrayList<String>();
				ArrayList<String> rotationTitles = new ArrayList<String>();
				ArrayList<String> rotationInitials = new ArrayList<String>();
				ArrayList<String> rotationTimes = new ArrayList<String>();
				try {
					rotationDates = an.getRotationDates();
					rotationTitles = an.getRotationTitles();
					rotationTimes = an.getRotationTimes();
					rotationInitials = an.getRotationInitilas();
				} catch (IOException e) {
					e.printStackTrace();
				}

				// Create individual expiration info labels
				for (int curLabelNum = 0; curLabelNum < numOfRotations; curLabelNum++) {

					// Set values for individual expirations
					labelArray[curLabelNum] = new JLabel();
					String date = rotationDates.get(curLabelNum);
					String title = rotationTitles.get(curLabelNum);
					String initials = rotationInitials.get(curLabelNum);

					// Create string representing the date and time of expiration. Might be cut off
					// in some cases.
					String time = rotationTimes.get(curLabelNum);
					if (initials.equals("n/a")) {
						labelArray[curLabelNum].setText(title + " : " + time + " : " + date);
					} else {
						labelArray[curLabelNum].setText(title + " : " + time + " : " + date + " : " + initials);
					}
					if (time.contains("-")) {
						labelArray[curLabelNum].setForeground(Color.RED);
					}
					labelArray[curLabelNum].setPreferredSize(new Dimension(analyticsItemX, analyticsItemY));
					labelArray[curLabelNum].setHorizontalAlignment(SwingConstants.CENTER);
					labelArray[curLabelNum].setFont(analyticsFont);

					labelArray[curLabelNum].setVisible(true);

					removePanel.add(labelArray[curLabelNum]);

					StartUp.optionPanel.repaint();
					StartUp.optionPanel.revalidate();
				}
			}

		});
		StartUp.optionPanel.add(analyticsBtn);

		// Remove Button
		removebtn.setVisible(true);
		removebtn.setBounds(optionsBtnXL, removeYL, optionsBtnX, optionsBtnY);
		removebtn.setFocusable(false);
		removebtn.setFont(optionFont);
		removebtn.setBackground(optionButtonBackground);
		removebtn.addActionListener(new ActionListener() {
			@SuppressWarnings("static-access")
			public void actionPerformed(ActionEvent ae) {
				// Create a new returnBtn
				JButton returnbtn = new JButton("X");
				returnbtn.setBounds(exitXYL, exitXYL, exitXY, exitXY);
				returnbtn.setFont(exitFont);
				returnbtn.setForeground(Color.WHITE);
				returnbtn.setBackground(returnColor);
				returnbtn.setFocusable(false);
				returnbtn.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ae) {
						// Just updates the proper graphics.
						removePanel.removeAll();
						removeScrollPanel.setVisible(false);
						removePanel.setVisible(false);
						returnbtn.setVisible(false);
						exitBtn.setVisible(true);
						autoSortBtn.setVisible(true);
						removebtn.setVisible(true);
						freshNetBtn.setVisible(true);
						analyticsBtn.setVisible(true);
					}
				});
				// Update graphics for scroll panel.
				removeScrollPanel.setVisible(true);
				removePanel.setVisible(true);
				returnbtn.setVisible(true);
				StartUp.optionPanel.add(returnbtn);

				autoSortBtn.setVisible(false);
				removebtn.setVisible(false);
				analyticsBtn.setVisible(false);
				exitBtn.setVisible(false);
				freshNetBtn.setVisible(false);

				// Create the remove panel and set Dims.
				removePanel.setPreferredSize(new Dimension(listPanelX, scrollValue));
				removePanel.setVisible(true);
				StartUp.optionPanel.add(removeScrollPanel);

				CurrentSession cs = new CurrentSession();

				// This loop creates the buttons used to remove timers.
				JButton[] buttonArray = new JButton[cs.getTNOT()];
				for (int curButtonNum = 0; curButtonNum < cs.getTNOT(); curButtonNum++) {
					if (cs.itHash.get(curButtonNum) != null) {
						int removeTimerID = curButtonNum;

						// Create the button that will remove the timer.
						buttonArray[curButtonNum] = new JButton();
						buttonArray[curButtonNum].setText(cs.itHash.get(curButtonNum).getTitle());
						buttonArray[curButtonNum].setPreferredSize(new Dimension(listItemX, listItemY));
						buttonArray[curButtonNum].setFont(exitFont);
						buttonArray[curButtonNum].setBackground(Color.LIGHT_GRAY);
						buttonArray[curButtonNum].setFocusable(false);
						buttonArray[curButtonNum].addActionListener(new ActionListener() {
							
							public void actionPerformed(ActionEvent ae) {
								// check and update session values.
								cs.setMenuOpen(true);
								cs.decreaseCNOT();

								// update active timers if needed.
								if (cs.itHash.get(removeTimerID).getToggled()) {
									cs.decreaseANOT();
								}
								// update the number of active expirations if needed.
								if (cs.itHash.get(removeTimerID).getCurrentlyExpired()) {
									cs.decreaseNOAE();

									// update the background flash if this timer is the last to go from active
									// expirations.
									if (cs.getNOAE() == 0) {
										cs.setActiveExpiratons(false);
										for (int i = 0; i <= cs.getCAP(); i++) {
											StartUp.backgroundHash.get(i).setBackground(backgroundColor);
										}

									}
								}

								// the actual removing of the timer from the itHash.
								cs.itHash.get(removeTimerID).countDown.stop();
								cs.itHash.remove(removeTimerID);

								// update page graphics after removing. If users complain about slow process of
								// removing multiple timers, rewrite this portion.
								cs.setCurrentPage(0);
								cs.updateCAP();
								cs.setMenuOpen(false);
								exitBtn.doClick();

								TaskBar tb = new TaskBar();
								tb.updateTaskBar();
							}
						});
						// Adding the individual button to the scroll panel.
						removePanel.add(buttonArray[curButtonNum]);
					}
					StartUp.optionPanel.repaint();
					StartUp.optionPanel.revalidate();
				}
			}
		});

		// Store ID Button
		freshNetBtn = new JButton("FreshNet ID");
		freshNetBtn.setVisible(true);
		freshNetBtn.setBounds(optionsBtnXL, layoutYL, optionsBtnX, optionsBtnY);
		freshNetBtn.setFocusable(false);
		freshNetBtn.setFont(optionFont);
		freshNetBtn.setBackground(Color.LIGHT_GRAY);
		freshNetBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				cs.setMenuOpen(true);
				StartUp.optionPanel.setVisible(false);
				StartUp.optionPanel.removeAll();
				TaskBar tb = new TaskBar();
				tb.updateBar("UNDO");
				CreateTimer ct = new CreateTimer();
				ct.paintAddressPanel();

			}
		});

		StartUp.optionPanel.add(freshNetBtn);

		StartUp.optionPanel.add(removebtn);
		StartUp.optionPanel.setBackground(Color.WHITE);
		StartUp.optionPanel.add(exitBtn);
	}

}