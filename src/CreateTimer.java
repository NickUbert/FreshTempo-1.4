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
import java.net.UnknownHostException;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

/**
 * CreateTimer class does not actually create timer objects, but actually
 * display the timer creation menu that allows users to create their own
 * customized timer. It allows for only three inputs which are title, hours, and
 * min. In the future this will maybe include a fourth option for days.
 * 
 */
public class CreateTimer {

	private Dimension mtk = Toolkit.getDefaultToolkit().getScreenSize();

	// Bounds

	// TODO Update naming for coponents
	private final int screenX = ((int) mtk.getWidth());
	private final int screenY = ((int) mtk.getHeight());
	private int cardX = (int) (.3125 * screenX);
	private int cardY = (int) (.875 * screenY);

	private int connectionPanelX = (int) (.4 * screenX);
	private int connectionPanelY = (int) (.275 * screenY);

	private int titleTextFieldXL = (int) (.44 * cardX);
	private int titleTextFieldYL = (int) (.16456 * cardY);
	private int titleTextFieldX = (int) (.48 * cardX);
	private int titleTextFieldY = (int) (.074 * cardY);

	private int minTextFieldYL = (int) (.08861 * cardY);

	private int keyPadButtonX = (int) (.3 * cardX);
	private int keyPadButtonY = (int) (.165 * cardY);

	private int hTextFieldYL = (int) (.01266 * cardY);

	private int minNewLabelYL = (int) (.10127 * cardY);

	private int switchLabelX = (int) (.24 * cardX);
	private int switchLabelXL = (int) (.85 * cardX - (switchLabelX / 2));
	private int switchLabelY = (int) (.05 * cardY);

	private int titleNewLabelYL = (int) (.17722 * cardY);
	private int titleNewLabelXL = (int) (.01 * cardX);
	private int titleNewLabelY = (int) (.03544 * cardY);
	private int titleNewLabelX = (int) (.4 * cardX);

	private int initialLabelYL = (int) (.26 * cardY);
	private int initialLabelXL = (int) (.01 * cardX);
	private int initialLabelX = (int) (.4 * cardX);
	private int initialLabelY = (int) (.03 * cardY);

	private int initialBoxYL = (int) (.25 * cardY);
	private int initialBoxXL = (int) (.45 * cardX);
	private int initialBoxXY = (int) (.065 * cardX);

	private int switchYL = (int) (.065 * cardY);
	private int switchXL = (int) (.8125 * cardX);
	private int switchXY = (int) (.075 * cardX);

	private int textFieldX = (int) (.24 * cardX);
	private int textFieldXL = (int) (.5 * cardX - (textFieldX / 2));
	private int textFieldY = (int) (.06329 * cardY);

	private int backspaceX = (int) (.13 * screenX);
	private int switchToNumBtnX = (int) (.075 * screenX);

	private int newLabelX = (int) (.32 * cardX);
	private int newLabelY = (int) (.035 * cardY);

	private int hNewLabelX = (int) (newLabelX - (cardX * .0425));
	private int hNewLabelYL = (int) (.02532 * cardY);

	private int keyboardX = (int) (.0935 * screenX);
	private int keyboardBottomRowX = (int) (.091 * screenX);
	private int keyboardY = (int) (.183 * screenY);

	private int keyPadYL = (int) (.3 * cardY);
	private int keyPadY = (int) (.75949 * cardY);

	private int curveD = (int) (.03125 * screenX);

	private int addyTextFieldX = (int) (.7 * cardX);
	private int addyTextFieldY = (int) (.08329 * cardY);
	private int addyTextFieldXL = (int) (.5 * cardX - (addyTextFieldX / 2));
	private int addyTextFieldYL = (int) (.15 * cardY);

	private int otherwiseLabelYL = (int) (.08 * cardY);
	private int otherwiseLabelY = (int) (.03532 * cardY);
	private int otherwiseLabelX = cardX;

	private int storeIDLabelYL = (int) (.02 * cardY);
	private int storeIDLabelX = cardX;
	private int storeIDLabelY = (int) (.03532 * cardY);

	private int troubleLabelAYL = ((int) (.45 * connectionPanelY));
	private int troubleLabelBYL = ((int) (.6 * connectionPanelY));
	private int troubleLabelY = (int) (.03532 * cardY);

	// exitXYL, exitXYL, exitXY, exitXY
	private int exitXY = (int) (.12 * connectionPanelX);
	private int exitXL = ((int) (.85 * connectionPanelX));
	private int exitYL = (int) (.04 * connectionPanelY);

	// Textfields
	private JTextField titleTf = new JTextField();
	private JTextField longerTf = new JTextField();
	private JTextField shorterTf = new JTextField();
	private JTextField addyTf = new JTextField();

	// Colors
	private Color backColor = Color.decode("#CC2936");
	private Color confirmColor = Color.decode("#4DA167");
	private Color keyboardBackgroundColor = Color.decode("#C2C1C2");
	private Color switchToNumColor = Color.decode("#6B818C");

	// Panels
	private RoundedPanel createPanel;
	private RoundedPanel addressPanel;
	private RoundedPanel connectedPanel;
	private JPanel keyPadPanel = new JPanel();
	private JPanel keyboardPanel = StartUp.mainKeyboard;

	// Borders
	Border keyBorder = BorderFactory.createLineBorder(Color.WHITE, 1);
	Border kbBorder = BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1);
	Border fieldBorder = BorderFactory.createLineBorder(Color.DARK_GRAY, 1);
	Border errorBorder = BorderFactory.createLineBorder(Color.RED, 2);

	// Fonts
	Font createPanelFont = new Font("Helvetica", Font.BOLD, (int) (cardX * .048));
	Font switchFont = new Font("Helvetica", Font.BOLD, (int) (cardX * .038));
	Font addyPanelFont = new Font("Helvetica", Font.BOLD, (int) (cardX * .068));
	Font connectionPanelFont = new Font("Helvetica", Font.BOLD, (int) (cardX * .058));
	Font keyboardFont = new Font("Helvetica", Font.TRUETYPE_FONT, (int) (.0625 * screenY));
	Font backspaceFont = new Font("Helvetica", Font.TRUETYPE_FONT, (int) (.03 * screenY));
	Font numSwitchFont = new Font("Helvetica", Font.ITALIC, (int) (.035 * screenY));

	Icon resizedUncheckedBoxIcon = new ImageIcon(
			new ImageIcon(getClass().getClassLoader().getResource("FT-icon-box-unchecked.png")).getImage()
					.getScaledInstance(initialBoxXY, initialBoxXY, Image.SCALE_SMOOTH));

	Icon resizedCheckedBoxIcon = new ImageIcon(
			new ImageIcon(getClass().getClassLoader().getResource("FT-icon-box-checked.png")).getImage()
					.getScaledInstance(initialBoxXY, initialBoxXY, Image.SCALE_SMOOTH));

	Icon resizedSwitchOn = new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("FT-icon-switch-on.png"))
			.getImage().getScaledInstance(switchXY, switchXY, Image.SCALE_SMOOTH));

	Icon resizedSwitchOff = new ImageIcon(
			new ImageIcon(getClass().getClassLoader().getResource("FT-icon-switch-off.png")).getImage()
					.getScaledInstance(switchXY, switchXY, Image.SCALE_SMOOTH));

	// Default user entered values
	private int userMin = 0;
	private int userHour = 0;
	private String userTitle = "";
	private boolean initialsRequired = false;
	private boolean longerShelf = false;

	/*
	 * paintCreatePanel is used to fill the creation panel with all components
	 */
	public void paintCreatePanel() {

		// Create the create panel.

		createPanel = new RoundedPanel();

		// Hour input textfield.
		longerTf.setFont(createPanelFont);
		longerTf.setBounds(textFieldXL, hTextFieldYL, textFieldX, textFieldY);
		longerTf.setBorder(fieldBorder);
		longerTf.setColumns(5);
		longerTf.setHorizontalAlignment(SwingConstants.CENTER);
		longerTf.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				displayKeypad(longerTf, true);
				CurrentSession cs = new CurrentSession();
				cs.setTyping(false);
			}
		});
		createPanel.add(longerTf);

		// Min input textfield.
		shorterTf.setFont(createPanelFont);
		shorterTf.setBounds(textFieldXL, minTextFieldYL, textFieldX, textFieldY);
		shorterTf.setColumns(5);
		shorterTf.setBorder(fieldBorder);
		shorterTf.setHorizontalAlignment(SwingConstants.CENTER);
		shorterTf.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				displayKeypad(shorterTf, true);
				CurrentSession cs = new CurrentSession();
				cs.setTyping(false);
			}
		});
		createPanel.add(shorterTf);

		// Title input textfield.
		titleTf.setFont(createPanelFont);
		titleTf.setBounds(titleTextFieldXL, titleTextFieldYL, titleTextFieldX, titleTextFieldY);
		titleTf.setColumns(5);
		titleTf.setBorder(fieldBorder);
		titleTf.setHorizontalAlignment(SwingConstants.CENTER);
		titleTf.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				CurrentSession cs = new CurrentSession();
				if (!cs.getTyping()) {
					displayKeyboard(titleTf);
					cs.setTyping(true);
				}
			}
		});
		createPanel.add(titleTf);

		// Set title label.
		JLabel newTitleLabel = new JLabel("Set Timer Name");
		newTitleLabel.setFont(createPanelFont);
		newTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		newTitleLabel.setBounds(titleNewLabelXL, titleNewLabelYL, titleNewLabelX, titleNewLabelY);
		newTitleLabel.setVisible(true);
		createPanel.add(newTitleLabel);

		// Set min label.
		JLabel shorterLabel = new JLabel("Set Minutes");
		shorterLabel.setFont(createPanelFont);
		shorterLabel.setBounds(0, minNewLabelYL, newLabelX, newLabelY);
		shorterLabel.setHorizontalAlignment(SwingConstants.CENTER);
		shorterLabel.setPreferredSize(new Dimension(newLabelX, newLabelY));
		createPanel.add(shorterLabel);

		// Set hour label.
		JLabel longerLabel = new JLabel("Set Hours");
		longerLabel.setFont(createPanelFont);
		longerLabel.setBounds(0, hNewLabelYL, hNewLabelX, newLabelY);
		longerLabel.setHorizontalAlignment(SwingConstants.CENTER);
		longerLabel.setPreferredSize(new Dimension(newLabelX, newLabelY));
		createPanel.add(longerLabel);

		JLabel initialLabel = new JLabel("Require Initials?");
		initialLabel.setFont(createPanelFont);
		initialLabel.setBounds(initialLabelXL, initialLabelYL, initialLabelX, initialLabelY);
		initialLabel.setHorizontalAlignment(SwingConstants.CENTER);
		initialLabel.setPreferredSize(new Dimension(newLabelX, newLabelY));
		createPanel.add(initialLabel);

		JButton checkBox = new JButton();
		checkBox.setIcon(resizedUncheckedBoxIcon);
		checkBox.setBounds(initialBoxXL, initialBoxYL, initialBoxXY, initialBoxXY);
		checkBox.setFocusable(false);
		checkBox.setBackground(switchToNumColor);
		checkBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				initialsRequired = !initialsRequired;
				if (initialsRequired) {
					checkBox.setIcon(resizedCheckedBoxIcon);
				} else {
					checkBox.setIcon(resizedUncheckedBoxIcon);
				}
			}
		});

		createPanel.add(checkBox);

		JLabel shortSwitchLabel = new JLabel("Short");
		shortSwitchLabel.setFont(switchFont);
		shortSwitchLabel.setBounds(switchLabelXL, hNewLabelYL, switchLabelX, switchLabelY);
		shortSwitchLabel.setHorizontalAlignment(SwingConstants.CENTER);
		shortSwitchLabel.setPreferredSize(new Dimension(newLabelX, newLabelY));
		createPanel.add(shortSwitchLabel);

		JButton switchButton = new JButton();
		switchButton.setIcon(resizedSwitchOn);
		switchButton.setBounds(switchXL, switchYL, switchXY, switchXY);
		switchButton.setFocusable(false);
		switchButton.setBackground(Color.WHITE);
		switchButton.setBorderPainted(false);
		switchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				longerShelf = !longerShelf;
				if (!longerShelf) {
					shorterLabel.setText("Set Minutes");
					shorterLabel.setHorizontalAlignment(SwingConstants.CENTER);
					longerLabel.setText("Set Hours");
					longerLabel.setHorizontalAlignment(SwingConstants.CENTER);
					switchButton.setIcon(resizedSwitchOn);
				} else {
					shorterLabel.setText(" Set Days");
					shorterLabel.setHorizontalAlignment(SwingConstants.LEFT);
					longerLabel.setText(" Set Weeks");
					longerLabel.setHorizontalAlignment(SwingConstants.LEFT);
					switchButton.setIcon(resizedSwitchOff);
				}
			}
		});

		createPanel.add(switchButton);

		JLabel longSwitchLabel = new JLabel("Long");
		longSwitchLabel.setFont(switchFont);
		longSwitchLabel.setBounds(switchLabelXL, minNewLabelYL, switchLabelX, switchLabelY);
		longSwitchLabel.setHorizontalAlignment(SwingConstants.CENTER);
		longSwitchLabel.setPreferredSize(new Dimension(newLabelX, newLabelY));
		createPanel.add(longSwitchLabel);

		// Set create panel properties and add it.
		createPanel.setPreferredSize(new Dimension(cardX, cardY));
		createPanel.setOpaque(false);
		createPanel.setLayout(null);
		createPanel.setBackground(Color.white);

		CurrentSession cs = new CurrentSession();
		cs.addToCurrentPage(createPanel);
	}

	/*
	 * Display keyboard is used to fill the keyboard panel (initialized in the main
	 * method) with all components and setting visible.
	 */
	public void displayKeyboard(JTextField tf) {

		String[] qwerty = new String[] { "Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P", "A", "S", "D", "F", "G", "H",
				"J", "K", "L", "Z", "X", "C", "V", "B", "N", "M" };
		JButton[] KBArray = new JButton[26];

		// Clear out the old components.
		keyboardPanel.removeAll();
		// Remove keyPadPanel in case it was visable before.
		createPanel.remove(keyPadPanel);
		keyPadPanel.setVisible(false);

		// Fill in keys for the rows before the backspace button.
		for (int curKeyNum = 0; curKeyNum < 19; curKeyNum++) {
			String thisButtonsValue = (qwerty[curKeyNum]);
			KBArray[curKeyNum] = new JButton((qwerty[curKeyNum]));
			KBArray[curKeyNum].setPreferredSize(new Dimension(keyboardX, keyboardY));
			KBArray[curKeyNum].setFont(keyboardFont);
			KBArray[curKeyNum].setFocusable(false);
			KBArray[curKeyNum].setBackground(keyboardBackgroundColor);
			KBArray[curKeyNum].setBorder(kbBorder);
			KBArray[curKeyNum].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					String priorText = tf.getText();
					tf.setText(priorText += thisButtonsValue);
				}
			});
			keyboardPanel.add(KBArray[curKeyNum]);
		}

		// Backspace button for keyboard
		JButton bspBtn = new JButton("Backspace");
		bspBtn.setPreferredSize(new Dimension(backspaceX, keyboardY));
		bspBtn.setFont(backspaceFont);
		bspBtn.setForeground(Color.WHITE);
		bspBtn.setFocusable(false);
		bspBtn.setBackground(backColor);
		bspBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent ae) {
				if (tf.getText().length() > 0) {
					StringBuffer sb = new StringBuffer(tf.getText());
					sb = sb.deleteCharAt(tf.getText().length() - 1);
					tf.setText(sb.toString());
				}
			}
		});
		keyboardPanel.add(bspBtn);

		// Fill in the remaining keys for the bottom row of keyboard.
		for (int curKeyNum = 19; curKeyNum < 26; curKeyNum++) {
			String thisButtonsValue = (qwerty[curKeyNum]);
			KBArray[curKeyNum] = new JButton((qwerty[curKeyNum]));
			KBArray[curKeyNum].setPreferredSize(new Dimension(keyboardBottomRowX, keyboardY));
			KBArray[curKeyNum].setFont(keyboardFont);
			KBArray[curKeyNum].setBackground(keyboardBackgroundColor);
			KBArray[curKeyNum].setFocusable(false);
			KBArray[curKeyNum].setBorder(kbBorder);
			KBArray[curKeyNum].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					String priorText = tf.getText();
					tf.setText(priorText += thisButtonsValue);
				}
			});
			keyboardPanel.add(KBArray[curKeyNum]);
		}

		JButton switchToNumBtn = new JButton("123");
		switchToNumBtn.setPreferredSize(new Dimension(switchToNumBtnX, keyboardY));
		switchToNumBtn.setFont(numSwitchFont);
		switchToNumBtn.setFocusable(false);
		switchToNumBtn.setBackground(switchToNumColor);
		switchToNumBtn.setForeground(Color.WHITE);
		switchToNumBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				displayKeypad(titleTf, true);

			}
		});
		keyboardPanel.add(switchToNumBtn);
		// Add the start button to the keyboard.
		JButton startBtn = new JButton("Enter");
		startBtn.setBackground(confirmColor);
		startBtn.setFont(backspaceFont);
		startBtn.setForeground(Color.WHITE);
		startBtn.setPreferredSize(new Dimension(keyboardX, keyboardY));
		startBtn.setVisible(true);
		startBtn.setFocusable(true);

		startBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				// First test for inputs
				if (titleTf.getText().length() != 0) {
					userTitle = titleTf.getText();
				}

				if (shorterTf.getText().length() != 0 && longerTf.getText().length() < 4) {
					userMin = Integer.parseInt(shorterTf.getText());
				}

				if (longerTf.getText().length() != 0 && longerTf.getText().length() < 4) {
					userHour = Integer.parseInt(longerTf.getText());
				}

				startButtonPressed();

			}
		});
		keyboardPanel.add(startBtn);
		keyboardPanel.setVisible(true);

	};

	/*
	 * displayKeypad is used to add keypad components to the itemtimer's create
	 * panel. It takes a textfield as a parameter in order to know which textfield
	 * it should be setting the text for.
	 */
	public void displayKeypad(JTextField tf, boolean isCreatePanel) {
		CurrentSession cs = new CurrentSession();
		// Creating panels
		keyPadPanel.setOpaque(false);
		keyPadPanel.setBounds(0, keyPadYL, cardX, keyPadY);
		keyPadPanel.setLayout(new FlowLayout());
		keyPadPanel.removeAll();
		keyboardPanel.setVisible(false);
		keyPadPanel.setVisible(true);

		// Numbers for keypad in the order they appear.
		String[] kpdigits = new String[] { "7", "8", "9", "4", "5", "6", "1", "2", "3", "0" };

		JButton[] buttonArray = new JButton[10];

		// Add buttons to keypad before backspace button.
		for (int curButtonNum = 0; curButtonNum < 9; curButtonNum++) {
			int thisButtonsValue = Integer.parseInt(kpdigits[curButtonNum]);

			buttonArray[curButtonNum] = new JButton((kpdigits[curButtonNum]));
			buttonArray[curButtonNum].setPreferredSize(new Dimension(keyPadButtonX, keyPadButtonY));
			buttonArray[curButtonNum].setFont(keyboardFont);
			buttonArray[curButtonNum].setBorder(keyBorder);
			buttonArray[curButtonNum].setFocusable(false);
			buttonArray[curButtonNum].setBackground(keyboardBackgroundColor);
			buttonArray[curButtonNum].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					tf.setText(tf.getText() + thisButtonsValue);
				}
			});
			keyPadPanel.add(buttonArray[curButtonNum]);
		}

		// Add the backspace button to the keypad.
		JButton bspBtn = new JButton("X");
		bspBtn.setPreferredSize(new Dimension(keyPadButtonX, keyPadButtonY));
		bspBtn.setFont(keyboardFont);
		bspBtn.setForeground(Color.WHITE);
		bspBtn.setFocusable(false);
		bspBtn.setBorder(keyBorder);
		bspBtn.setBackground(backColor);
		bspBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent ae) {
				if (tf.getText().length() > 0) {
					StringBuffer sb = new StringBuffer(tf.getText());
					sb = sb.deleteCharAt(tf.getText().length() - 1);
					tf.setText(sb.toString());
				}
			}
		});
		keyPadPanel.add(bspBtn);

		// Add the zero button between the backspace and start button.
		String lastBtnText = "0";
		Font lastBtnFont = keyboardFont;
		Color lastBtnColor = keyboardBackgroundColor;

		if (tf == titleTf) {
			lastBtnText = "ABC";
			lastBtnFont = numSwitchFont;
			lastBtnColor = switchToNumColor;
		}
		buttonArray[9] = new JButton((lastBtnText));
		buttonArray[9].setPreferredSize(new Dimension(keyPadButtonX, keyPadButtonY));
		buttonArray[9].setFont(lastBtnFont);
		buttonArray[9].setBorder(keyBorder);
		buttonArray[9].setFocusable(false);
		buttonArray[9].setBackground(lastBtnColor);
		if (tf == titleTf) {
			buttonArray[9].setForeground(Color.WHITE);
		}
		buttonArray[9].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if (tf == titleTf) {
					displayKeyboard(titleTf);
					keyPadPanel.setVisible(false);
					createPanel.repaint();
					createPanel.revalidate();

				} else {
					tf.setText(tf.getText() + 0);
				}
			}
		});
		keyPadPanel.add(buttonArray[9]);

		// Add the start button to the keypad.
		JButton startBtn = new JButton("O");
		startBtn.setBackground(confirmColor);
		startBtn.setFocusable(false);
		startBtn.setFont(keyboardFont);
		startBtn.setBorder(keyBorder);
		startBtn.setForeground(Color.WHITE);
		startBtn.setPreferredSize(new Dimension(keyPadButtonX, keyPadButtonY));
		startBtn.setVisible(true);

		keyPadPanel.add(startBtn);
		startBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {

				// Execute if the start btn was on the create timer panel.
				if (isCreatePanel) {
					// First check for inputs.
					if (titleTf.getText().length() != 0) {
						userTitle = titleTf.getText();
					}

					if (shorterTf.getText().length() != 0) {
						userMin = Integer.parseInt(shorterTf.getText());
					}

					if (longerTf.getText().length() != 0) {
						userHour = Integer.parseInt(longerTf.getText());
					}

					startButtonPressed();
					// If it came from the address panel else executes.
				} else {
					// If the user didn't enter anything and clicked start.
					if (addyTf.getText().length() == 0) {
						// Filler address that will never be assigned to a store.
						cs.setSessionAddress(11111111);
						cs.setClientConnected(false);
						returnToToggleScreen();
						// If the user enetered a valid length of a store address execute this.
					} else if (addyTf.getText().length() == 8) {
						// Set the address equal to whatever integers were entered.
						cs.setSessionAddress(Integer.parseInt(addyTf.getText()));

						// Update the session settings so that the system knows to try to send data to
						// the server.
						cs.setClientConnected(true);

						// Send initial connection message to server that will establish its data
						// folders and the time conversion needed for the server.
						try {
							ClientConnection cc = new ClientConnection();
							Analytics an = new Analytics();

							// The @ symbol indicates that there is a connection message.
							cc.sendMessage(cs.getSessionAddress() + "@" + an.getDateAndTime());

							// Display the status of the connection after a host check is done.
							paintConnectionMessage(cc.hostAvailabilityCheck());
							TaskBar tb = new TaskBar();
							tb.updateBar("UNDO");
						} catch (UnknownHostException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}

					} else {
						// If the user entered something >0 and <8
						addyTf.setBorder(errorBorder);
						addyTf.setText("");
					}
				}
			}
		});
		if (isCreatePanel) {
			// This handles update the graphics revalidation I guess but I don't remember
			// adding this.
			createPanel.add(keyPadPanel);
			createPanel.revalidate();
		} else {
			addressPanel.add(keyPadPanel);
			addressPanel.revalidate();
		}
	}

	/*
	 * startButtonPressed is called by all keypads and keyboards. It makes sure all
	 * inputs are valids before updating the graphics and creating a new itemtimer
	 * using the user inputs.
	 */
	private void startButtonPressed() {
		// Reset all validation values and graphics
		boolean validTitle = true;
		boolean validMin = true;
		boolean validHour = true;
		shorterTf.setBorder(fieldBorder);
		longerTf.setBorder(fieldBorder);
		titleTf.setBorder(fieldBorder);

		// Check to see if a title length is too long.
		if (userTitle.length() == 0 || userTitle.length() > 11) {
			validTitle = false;
			titleTf.setText("");
			titleTf.setBorder(errorBorder);
		}

		// Make sure minutes value isn't over 60.
		if (userMin >= 60) {
			validMin = false;
			shorterTf.setText("");
			shorterTf.setBorder(errorBorder);
		}

		// Hour value is capped at 100
		if (userHour >= 52) {
			validHour = false;
			longerTf.setText("");
			longerTf.setBorder(errorBorder);
		}

		// Make sure a time value was entered for either minutes or hours.
		if (userHour == 0 && userMin == 0) {
			validHour = false;
			validMin = false;
			longerTf.setText("");
			shorterTf.setText("");
			longerTf.setBorder(errorBorder);
			shorterTf.setBorder(errorBorder);

		}

		// Only if all three conditions are met and the title is unique, then the timer
		// will be created.Graphics are updated and a timer creation is called using the
		// user inputs as constructor values.
		if (validTitle && validMin && validHour) {

			CurrentSession cs = new CurrentSession();

			if (longerShelf) {				
				userHour = (userHour*168) + (userMin*24);
				System.out.println(userHour);
				userMin=0;
			}

			int shelfSec = (userHour * 36060) + (userMin * 60);

			// TODO DEMO DISCONNECT

			Database db = new Database();
			try {
				db.recordNewItem(cs.getTNOT(), userTitle, shelfSec);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			cs.increaseCNOT();
			cs.increaseTNOT();
			cs.updateCAP();

			createPanel.setVisible(false);
			keyboardPanel.setVisible(false);
			keyPadPanel.setVisible(false);

			TaskBar tb = new TaskBar();
			tb.updateBar("MENU");
			cs.setTyping(false);
			cs.increaseANOT();

			// Suppresses are used because these are contructor calls
			@SuppressWarnings("unused")
			ItemTimer it = new ItemTimer(userMin, userHour, userTitle, cs.getTNOT() - 1, false, true, initialsRequired);
			@SuppressWarnings("unused")
			InventoryMenu im = new InventoryMenu();

		}

	}

	// TODO this isn't used currently but might be useful
	private boolean existingTitle(String s) {
		CurrentSession cs = new CurrentSession();
		for (int curTimerID = 0; curTimerID < cs.getTNOT(); curTimerID++) {
			if (CurrentSession.itHash.get(curTimerID) != null) {
				if (CurrentSession.itHash.get(curTimerID).getTitle().equals(s)) {
					return true;
				}
			}
		}
		return false;
	}

	/*
	 * paintAddressPanel is used to create and fill the graphic components of the
	 * address panel which is used to assign the store's address, an 8 digit code
	 * unique to each client.
	 */
	public void paintAddressPanel() {
		CurrentSession cs = new CurrentSession();
		cs.setTyping(true);
		addressPanel = new RoundedPanel();

		addressPanel.setPreferredSize(new Dimension(cardX, cardY));
		addressPanel.setOpaque(false);
		addressPanel.setLayout(null);
		addressPanel.setBackground(Color.white);

		// Set min label.
		JLabel storeIDLabel = new JLabel("Enter Store ID for FreshNet");
		storeIDLabel.setFont(addyPanelFont);
		storeIDLabel.setBounds(0, storeIDLabelYL, storeIDLabelX, storeIDLabelY);
		storeIDLabel.setHorizontalAlignment(SwingConstants.CENTER);
		storeIDLabel.setPreferredSize(new Dimension(newLabelX, newLabelY));
		addressPanel.add(storeIDLabel);

		JLabel otherwiseLabel = new JLabel("Otherwise leave blank");
		otherwiseLabel.setFont(addyPanelFont);
		otherwiseLabel.setBounds(0, otherwiseLabelYL, otherwiseLabelX, otherwiseLabelY);
		otherwiseLabel.setHorizontalAlignment(SwingConstants.CENTER);
		otherwiseLabel.setPreferredSize(new Dimension(newLabelX, newLabelY));
		addressPanel.add(otherwiseLabel);

		// Address input textfield.
		addyTf.setFont(addyPanelFont);

		addyTf.setBounds(addyTextFieldXL, addyTextFieldYL, addyTextFieldX, addyTextFieldY);
		addyTf.setBorder(fieldBorder);
		addyTf.setColumns(5);
		addyTf.setHorizontalAlignment(SwingConstants.CENTER);
		addyTf.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				CurrentSession cs = new CurrentSession();
				cs.setTyping(true);
			}
		});
		addressPanel.add(addyTf);
		displayKeypad(addyTf, false);

		cs.addToCurrentPage(addressPanel);
	}

	/*
	 * returnToMainScreen is used to eliminate all elements that aren't needed
	 * anymore and return to the home screen.
	 */
	public void returnToToggleScreen() {
		CurrentSession cs = new CurrentSession();
		if (cs.getTNOT() >= 2) {
			connectedPanel.setVisible(false);
			addressPanel.setVisible(false);
			keyPadPanel.setVisible(false);
			cs.setMenuOpen(true);
			cs.setCurrentPage(0);
			@SuppressWarnings("unused")
			InventoryMenu im = new InventoryMenu();
			TaskBar tb = new TaskBar();
			tb.updateTaskBar();
		} else {
			cs.setTyping(false);
			connectedPanel.setVisible(false);
			addressPanel.setVisible(false);
			keyPadPanel.setVisible(false);
			paintCreatePanel();
		}

	}

	/*
	 * paintConnectionMessage is used to tell the user whether or not they connected
	 * to the internet. It opens a small box with a message in it that will depend
	 * on the state of their connection. The parameter should only even be used with
	 * a hostAvailabilityCheck.
	 */
	private void paintConnectionMessage(boolean connected) {
		addressPanel.setVisible(false);
		keyPadPanel.setVisible(false);

		connectedPanel = new RoundedPanel();

		connectedPanel.setPreferredSize(new Dimension(connectionPanelX, connectionPanelY));
		connectedPanel.setOpaque(false);
		connectedPanel.setLayout(null);
		connectedPanel.setBackground(Color.white);

		JButton exitBtn = new JButton("X");
		exitBtn.setBounds(exitXL, exitYL, exitXY, exitXY);
		exitBtn.setFont(connectionPanelFont);
		exitBtn.setForeground(Color.WHITE);
		exitBtn.setBackground(backColor);
		exitBtn.setFocusable(false);
		exitBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				connectedPanel.removeAll();
				returnToToggleScreen();
				TaskBar tb = new TaskBar();
				tb.updateBar("MENU");
			}
		});
		connectedPanel.add(exitBtn);

		String message = "Connection successful!";
		String troubleMessageA = "Close this message to get started.";
		if (!connected) {
			message = "Connection Unsuccessful.";
			troubleMessageA = "Make sure you are connected to Wifi.";
		}

		JLabel connectionLabel = new JLabel(message);
		connectionLabel.setFont(connectionPanelFont);
		connectionLabel.setBounds(0, storeIDLabelYL, connectionPanelX, storeIDLabelY);
		connectionLabel.setHorizontalAlignment(SwingConstants.CENTER);
		connectionLabel.setPreferredSize(new Dimension(newLabelX, newLabelY));

		connectedPanel.add(connectionLabel);

		JLabel troubleLabelLineA = new JLabel(troubleMessageA);
		troubleLabelLineA.setFont(connectionPanelFont);
		troubleLabelLineA.setBounds(0, troubleLabelAYL, connectionPanelX, troubleLabelY);
		troubleLabelLineA.setHorizontalAlignment(SwingConstants.CENTER);
		troubleLabelLineA.setPreferredSize(new Dimension(newLabelX, newLabelY));
		connectedPanel.add(troubleLabelLineA);

		if (!connected) {

			String troubleMessageB = "Data will still be collected, try again later.";
			JLabel troubleLabelLineB = new JLabel(troubleMessageB);
			troubleLabelLineB.setFont(connectionPanelFont);
			troubleLabelLineB.setBounds(0, troubleLabelBYL, connectionPanelX, troubleLabelY);
			troubleLabelLineB.setHorizontalAlignment(SwingConstants.CENTER);
			troubleLabelLineB.setPreferredSize(new Dimension(newLabelX, newLabelY));
			connectedPanel.add(troubleLabelLineB);
		}
		CurrentSession cs = new CurrentSession();
		cs.addToCurrentPage(connectedPanel);
	}

	public void hideKeyPanels() {
		keyboardPanel.setVisible(false);
		keyPadPanel.setVisible(false);
		CurrentSession cs = new CurrentSession();
		cs.setTyping(false);
	}
}