import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

public class InitialsMenu {
	CurrentSession cs = new CurrentSession();
	PageManager pm = new PageManager();
	TaskBar tb = new TaskBar();

	RoundedPanel menu = new RoundedPanel();

	private JPanel keyboardPanel = StartUp.mainKeyboard;

	private JTextField initialField = new JTextField();

	private Dimension mtk = Toolkit.getDefaultToolkit().getScreenSize();

	// Bounds
	private final int screenX = ((int) mtk.getWidth());
	private final int screenY = ((int) mtk.getHeight());

	Font keyboardFont = new Font("Helvetica", Font.TRUETYPE_FONT, (int) (.0625 * screenY));
	Font backspaceFont = new Font("Helvetica", Font.TRUETYPE_FONT, (int) (.03 * screenY));
	Font promptFont = new Font("Helvetica", Font.BOLD, (int) (.05 * screenY));
	Font exitFont = new Font("Helvetica", Font.BOLD, (int) (.025 * screenY));

	private int keyboardX = (int) (.0935 * screenX);
	private int keyboardBottomRowX = (int) (.0935 * screenX);
	private int keyboardY = (int) (.183 * screenY);
	private int backspaceX = (int) (.13 * screenX);
	private int startButtonX = (int) (.13 * screenX);

	private int initialMenuX = (int) (.7 * screenX);
	private int initialMenuY = (int) (.2 * screenY);

	private int menuItemY = (int) (.33 * initialMenuY);
	private int promptX = (int) (.35 * initialMenuX);
	private int cancelX = (int) (.12 * initialMenuX);

	private int initialFieldX = (int) (.15 * initialMenuX);
	private int fillerGap = (int) (.33 * initialMenuX);

	ItemTimer timer;
	JLabel prompt;

	Border keyBorder = BorderFactory.createLineBorder(Color.WHITE, 1);
	Border kbBorder = BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1);
	Border fieldBorder = BorderFactory.createLineBorder(Color.DARK_GRAY, 1);
	Border errorBorder = BorderFactory.createLineBorder(Color.RED, 2);

	private Color backColor = Color.decode("#CC2936");
	private Color confirmColor = Color.decode("#4DA167");
	private Color keyboardBackgroundColor = Color.decode("#C2C1C2");
	private Color menuColor = Color.decode("#FFFFFF");

	public InitialsMenu(ItemTimer it) {
		timer = it;
		openInitialsMenu();
	}

	public void openInitialsMenu() {
		pm.clearPage();
		cs.setMenuOpen(true);
		paintMenu();
		displayKeyboard(initialField);

		tb.updateBar("UNDO");

	}

	private void paintMenu() {
		menu.setPreferredSize(new Dimension(initialMenuX, initialMenuY));
		menu.setOpaque(false);
		menu.setLayout(new FlowLayout());
		menu.setBackground(menuColor);

		JPanel divider2 = new JPanel();
		divider2.setPreferredSize(new Dimension(initialMenuX - (int) (.04 * screenX), menuItemY / 3));
		divider2.setBackground(Color.WHITE);
		menu.add(divider2);

		// Exit button labeled "Save/Close"
		JButton exitButton = new JButton("CANCEL");
		exitButton.setPreferredSize(new Dimension(cancelX, menuItemY));
		exitButton.setForeground(Color.WHITE);
		exitButton.setBackground(backColor);

		exitButton.setFont(exitFont);
		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// update the graphics when exiting the toggle panel.
				cs.setMenuOpen(false);
				menu.removeAll();
				menu.setVisible(false);
				keyboardPanel.setVisible(false);
				cs.setCurrentPage(0);

				// prevents null calls when no timers need to be sorted.
				if (cs.getANOT() != 0) {
					Sorter so = new Sorter();
					so.sort(CurrentSession.itHash);
				}

			}
		});

		menu.add(exitButton);

		JLabel textLabel = new JLabel("Enter Your Initials ");
		textLabel.setFont(exitFont);
		textLabel.setPreferredSize(new Dimension(promptX, menuItemY));
		textLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		menu.add(textLabel);

		initialField.setPreferredSize(new Dimension(initialFieldX, menuItemY));
		initialField.setBorder(fieldBorder);
		initialField.setHorizontalAlignment(SwingConstants.CENTER);
		initialField.setFont(promptFont);

		menu.add(initialField);

		JPanel fillerSpaceR = new JPanel();
		fillerSpaceR.setPreferredSize(new Dimension(fillerGap, menuItemY));
		fillerSpaceR.setBackground(menuColor);

		menu.add(fillerSpaceR);

		ArrayList<String> recents = CurrentSession.recentInitials;
		if (recents.size() > 0) {
			JLabel recentLabel = new JLabel("Autofill");
			recentLabel.setFont(exitFont);
			recentLabel.setPreferredSize(new Dimension(cancelX, menuItemY));
			recentLabel.setHorizontalAlignment(SwingConstants.CENTER);

			menu.add(recentLabel);

			for (int i = recents.size() - 1; i >= 0; i--) {
				JButton recentButton = new JButton(recents.get(i));
				recentButton.setBackground(keyboardBackgroundColor);
				recentButton.setFont(exitFont);
				recentButton.setPreferredSize(new Dimension(cancelX, menuItemY));

				recentButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ae) {
						initialField.setBorder(fieldBorder);
						initialField.setText(recentButton.getText());

					}
				});

				menu.add(recentButton);
			}

		}

		cs.addToCurrentPage(menu);
	}

	public void displayKeyboard(JTextField tf) {

		String[] qwerty = new String[] { "Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P", "A", "S", "D", "F", "G", "H",
				"J", "K", "L", "Z", "X", "C", "V", "B", "N", "M" };
		JButton[] KBArray = new JButton[26];

		// Clear out the old components.
		keyboardPanel.removeAll();

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

		// Add the start button to the keyboard.
		JButton startBtn = new JButton("Enter");
		startBtn.setBackground(confirmColor);
		startBtn.setFont(backspaceFont);
		startBtn.setForeground(Color.WHITE);
		startBtn.setPreferredSize(new Dimension(startButtonX, keyboardY));
		startBtn.setVisible(true);
		startBtn.setFocusable(true);

		startBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				String input = tf.getText();
				if ((input.length() > 0) && (input.length() < 4)) {
					cs.addInitials(input);
					recordTimer(input);

				} else {
					tf.setBorder(errorBorder);
					tf.setText("");
				}

			}
		});
		keyboardPanel.add(startBtn);
		keyboardPanel.setVisible(true);

	};

	private void recordTimer(String initials) {
		Analytics an = new Analytics();
		try {
			an.recordTimeData(timer,initials);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		// TODO DEMO DISCONNECT

		Database db = new Database();
		int timerShelfSec = timer.getShelfSec();

		try {
			db.recordItem(timer.getTimerID(), timerShelfSec, initials);
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
		close();
	}

	private void close() {
		cs.setMenuOpen(false);
		menu.removeAll();
		menu.setVisible(false);
		keyboardPanel.setVisible(false);
		cs.setCurrentPage(0);

		// prevents null calls when no timers need to be sorted.
		if (cs.getANOT() != 0) {
			Sorter so = new Sorter();
			so.sort(CurrentSession.itHash);
		}

	}

}
